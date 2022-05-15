package de.neuefische.smartcount;

import de.neuefische.smartcount.Exceptions.InvalidUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin
@RequiredArgsConstructor
public class SmartCountController {

    private final SmartCountService smartCountService;

    @PostMapping
    public Expense createExpense(@RequestBody Expense expense, Principal principal) {
        expense.setUser(principal.getName());
       return smartCountService.createExpense(expense);
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable String id, Principal principal) {
        try {
            smartCountService.deleteExpense(id, principal.getName());
        } catch (RuntimeException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> editExpense(@PathVariable String id, @RequestBody Expense expense, Principal principal) {
        expense.setUser(principal.getName());
         return ResponseEntity.of(smartCountService.editExpense(id, expense, principal.getName()));
    }

    @GetMapping
    public ExpensesDTO getExpensesDTO() {
        return new ExpensesDTO(smartCountService.getAllExpenses(), smartCountService.getSum());
    }

    @GetMapping("/user")
    public ExpensesDTO getExpensesDTOByUser(Principal principal) {
        return new ExpensesDTO(smartCountService.getExpensesByUser(principal.getName()), smartCountService.getSumByUser(principal.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Expense>> getSingleExpense(@PathVariable String id, Principal principal) {
        try {
            return ResponseEntity.ok(smartCountService.getSingleExpense(id, principal.getName()));
        } catch (InvalidUserException e) {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping("/userlist")
    public List<String> getUserList() {
        return smartCountService.getUserList();
    }

    @GetMapping("/balance")
    public List<TransactionsDTO> getBalance() {
        return smartCountService.amountPerPerson();
    }
}
