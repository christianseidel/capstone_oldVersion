package de.neuefische.smartcount;

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

        public Collection<Expense> getExpenses() {
            return expensesRepository.findAll()
                    .stream()
                    .toList();
        }

        public void deleteExpense(String id) {
            expensesRepository.deleteById(id);
        }

        public Expense editExpense(String id, Expense expense) {
            var item = expensesRepository.findById(id);
            if (item.isEmpty()) {
                throw new RuntimeException("Diese Id ist nicht bekannt!");
            } else {
                return expensesRepository.save(expense);
            }
        }

        public Optional<Expense> getSingleExpense(String id) {
            return expensesRepository.findById(id);
        }
       }
