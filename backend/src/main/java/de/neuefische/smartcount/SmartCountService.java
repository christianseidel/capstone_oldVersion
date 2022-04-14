package de.neuefische.smartcount;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class SmartCountService {

    private final ExpensesRepository expensesRepository;

    public Expense createExpense(Expense expense) {
        expense.setUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return expensesRepository.save(expense);
    }

    public Collection<Expense> getExpenses() {
        return expensesRepository.findAll();
    }

    public Collection<Expense> getExpensesByUser() {
        String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return expensesRepository.findAll().stream()
                .filter(element -> "user".equals(user))
                .toList();
    }

    public double getSum() {
        return expensesRepository.findAll()
                .stream()
                .mapToDouble(a -> a.getAmount()).sum();
    }

    public void deleteExpense(String id) {
        var item = expensesRepository.findById(id);
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

    public Optional<Expense> getSingleExpense(String id) {
        return expensesRepository.findById(id);
    }

}
