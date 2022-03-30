package de.neuefische.smartcount;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/expenses")
@CrossOrigin
@RequiredArgsConstructor
public class SmartCountController {

    private final SmartCountService smartCountService;

    @PostMapping
    public Expense createExpense(@RequestBody Expense expense) {
        return smartCountService.createExpense(expense);
        }

    @GetMapping
    public Collection<Expense> getAllExpenses() {
        return smartCountService.getExpenses();
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable String id) {
        smartCountService.deleteExpense(id);
    }

    @PutMapping("/{id}")
    public Expense editExpense(@PathVariable String id, @RequestBody Expense expense) {
        return smartCountService.editExpense(id, expense);
    }

}
