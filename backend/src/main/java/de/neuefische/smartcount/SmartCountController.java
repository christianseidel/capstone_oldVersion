package de.neuefische.smartcount;

import de.neuefische.smartcount.Exceptions.InvalidUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.Optional;

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
            smartCountService.deleteExpense(id, getPrincipal());
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
        return new ExpensesDTO(smartCountService.getAllExpenses(getPrincipal()), smartCountService.getSum());
    }

    @GetMapping("/user")
    public ExpensesDTO getExpensesDTOByUser() {
        return new ExpensesDTO(smartCountService.getExpensesByUser(getPrincipal()), smartCountService.getSumByUser(getPrincipal()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Expense>> getSingleExpense(@PathVariable String id) {
        try {
            return ResponseEntity.ok(smartCountService.getSingleExpense(id, getPrincipal()));
        } catch (InvalidUserException e) {
            return ResponseEntity.status(403).build();
        }
    }

    private String getPrincipal() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }
}
