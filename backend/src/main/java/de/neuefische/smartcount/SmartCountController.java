package de.neuefische.smartcount;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin
@RequiredArgsConstructor
public class SmartCountController {

    private final SmartCountService smartCountService;

    @PostMapping
    public Expense createExpense(@RequestBody Expense expense) {
        return smartCountService.createExpense(expense);
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable String id) {
        try {
            smartCountService.deleteExpense(id);
        } catch (RuntimeException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> editExpense(@PathVariable String id, @RequestBody Expense expense) {
         return ResponseEntity.of(smartCountService.editExpense(id, expense));
    }

    @GetMapping
    public ExpensesDTO getExpensesDTO() {
        return new ExpensesDTO(smartCountService.getExpenses(), smartCountService.getSum());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getSingleExpense(@PathVariable String id) {
        return ResponseEntity.of(smartCountService.getSingleExpense(id));
    }
}
