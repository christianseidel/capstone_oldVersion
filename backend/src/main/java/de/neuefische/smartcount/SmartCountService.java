package de.neuefische.smartcount;

import de.neuefische.smartcount.Exceptions.InvalidUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SmartCountService {

    private final ExpensesRepository expensesRepository;

    public Expense createExpense(Expense expense) {
        return expensesRepository.save(expense);
    }

    public Collection<Expense> getAllExpenses(String user) {
        if (expensesRepository.existsAllByUser(user)) {
            return expensesRepository.findAll();
        }
        throw new InvalidUserException();
    }

    public double getSum() {
        return expensesRepository.findAll()
                .stream()
                .mapToDouble(a -> a.getAmount()).sum();
    }

    public Collection<Expense> getExpensesByUser(String user) {
        return expensesRepository.findAllByUser(user);
    }

    public double getSumByUser(String user) {
        return expensesRepository.findAllByUser(user)
                .stream()
                .mapToDouble(a -> a.getAmount()).sum();
    }

    public void deleteExpense(String id, String user) {
        var item = expensesRepository.findByIdAndUser(id, user);
        if (item.isEmpty()) {
            throw new RuntimeException("Die Ausgabe mit der Id " + id + " ist nicht bekannt!");
        } else {
            expensesRepository.deleteById(id);
        }
    }

    public Optional<Expense> editExpense(String id, Expense expense) {
        return expensesRepository.findById(id)
                .map(e -> expensesRepository.save(expense));
    }

    public Optional<Expense> getSingleExpense(String id, String user) {
        Optional<Expense> expense = expensesRepository.findByIdAndUser(id, user);
        if (!expense.isPresent()) {
            throw new InvalidUserException();
        }
        return expense;
    }

}
