package de.neuefische.smartcount;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@RequiredArgsConstructor
@Service
public class SmartCountService {

    private final ExpensesRepository expensesRepository;

        public void createExpense(Expense expense) {
           expensesRepository.save(expense);
        }

        public Collection<Expense> getExpenses() {
            return expensesRepository.findAll()
                    .stream()
                    .toList();
        }

        public void deleteExpense(String id) {
            expensesRepository.deleteById(id);
        }
}
