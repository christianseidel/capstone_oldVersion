package de.neuefische.smartcount;

import de.neuefische.smartcount.Exceptions.InvalidUserException;
import de.neuefische.smartcount.Users.User;
import de.neuefische.smartcount.Users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SmartCountService {

    private final ExpensesRepository expensesRepository;
    private final UserRepository userRepo;

    public Expense createExpense(Expense expense) {
        return expensesRepository.save(expense);
    }

    public Collection<Expense> getAllExpenses() {
        return expensesRepository.findAll();
    }

    public double getSum() {
        return Math.round(expensesRepository.findAll()
                .stream()
                .mapToDouble(a -> a.getAmount()).sum()*100)/100.0;
    }

    public Collection<Expense> getExpensesByUser(String user) {
        return expensesRepository.findAllByUser(user);
    }

    public double getSumByUser(String user) {
        return Math.round(expensesRepository.findAllByUser(user)
                .stream()
                .mapToDouble(a -> a.getAmount()).sum()*100)/100.0;
    }

    public void deleteExpense(String id, String user) {
        var item = expensesRepository.findByIdAndUser(id, user);
        if (item.isEmpty()) {
            throw new RuntimeException("Die Ausgabe mit der Id " + id + " ist nicht bekannt!");  // maybe rethink runtime exception !!??
        } else {
            expensesRepository.deleteById(id);
        }
    }

    public Optional<Expense> editExpense(String id, Expense expense, String user) {
        return expensesRepository.findByIdAndUser(id, user)
                .map(e -> expensesRepository.save(expense));
    }

    public Optional<Expense> getSingleExpense(String id, String user) {
        Optional<Expense> expense = expensesRepository.findByIdAndUser(id, user);
        if (!expense.isPresent()) {
            throw new InvalidUserException();
        }
        return expense;
    }

    public List<String> getUserList() {
        return userRepo.findAll().stream()
                .map(user -> user.getUsername()).toList();
    }

    //  ----------------   Doing The Math   ----------------   //
    //  -----   Who needs to pay how much at the end   -----   //

    public List<TransactionsDTO> amountPerPerson() {

        List<User> userList = userRepo.findAll();
        int numberOfPersons = userList.size();

        // arithmetic mean of all expenses
        double arithMean = Math.round(getSum()/numberOfPersons*100)/100.0;
        if (arithMean==0.0) {
            throw new RuntimeException("There is no sum big enough to be divided.");
        }

        List<UserDOO> usersWithExcess = new ArrayList<>();
        List<UserDOO> usersWithDeficit = new ArrayList<>();

        for (int i = 0; i < numberOfPersons; i++) {
            String user = userList.get(i).getUsername();
            double sum = getSumByUser(user);
            if ((sum - arithMean) >= 0.0) {
                double delta = Math.round((sum - arithMean)*100)/100.0;
                var userRecord = new UserDOO (user, delta);
                usersWithExcess.add(userRecord);
            } else {
                double delta = Math.round((arithMean - sum)*100)/100.0;
                var userRecord = new UserDOO(user, delta);
                usersWithDeficit.add(userRecord);
            }
        }

        usersWithExcess.sort(Comparator.comparing(UserDOO::userDelta).reversed());
        usersWithDeficit.sort(Comparator.comparing(UserDOO::userDelta).reversed());

        int e = usersWithExcess.size()-1;
        int d = usersWithDeficit.size()-1;

        if (usersWithDeficit.size() == 0) {
            throw new RuntimeException("There is only one user in this group. No transaction will take place.");
        }

        // Make sure, all surpluses = all deficits
        double sumPluses = 0.0;
        for (int i = 0; i < usersWithExcess.size(); i++) {
            sumPluses =  sumPluses + usersWithExcess.get(i).userDelta();
        };
        double sumMinuses = 0.0;
        for (int i = 0; i < usersWithDeficit.size(); i++) {
            sumMinuses =  sumMinuses + usersWithDeficit.get(i).userDelta();
        };
        // Any difference will be added to the account of the group member with the biggest stock of expenses.
        // This ensures that the while loop will produce no glitch.
        double newDelta = Math.round((usersWithExcess.get(0).userDelta() - (sumPluses-sumMinuses))*100)/100.0;
        UserDOO firstUser = usersWithExcess.get(0);
        usersWithExcess.set(0, firstUser.changeDelta(newDelta));

        // Establish all transactions needed to settle accounts
        ArrayList<TransactionsDTO> listOfTransactions = new ArrayList<>();
        while (e >= 0) {
            UserDOO userWithExcess = usersWithExcess.get(e);
            UserDOO userWithDeficit = usersWithDeficit.get(d);
            double exc = userWithExcess.userDelta();
            double defi = userWithDeficit.userDelta();
            if (exc == defi) {
                listOfTransactions.add(new TransactionsDTO(usersWithDeficit.get(d).userName(), usersWithExcess.get(e).userName(), exc, Currency.EUR));  // The handling of different currencies is here being prepared for, but will only be implemented at a later point in time.
                e--;
                d--;
            }
            else if (exc > defi) {
                listOfTransactions.add(new TransactionsDTO(usersWithDeficit.get(d).userName(), usersWithExcess.get(e).userName(), defi, Currency.EUR));
                d--;
                usersWithExcess.set(e, userWithExcess.changeDelta(Math.round((exc-defi)*100)/100.0));
            }
            else {
                listOfTransactions.add(new TransactionsDTO(usersWithDeficit.get(d).userName(), usersWithExcess.get(e).userName(), exc, Currency.EUR));
                e--;
                usersWithDeficit.set(d, userWithDeficit.changeDelta(Math.round((defi-exc)*100)/100.0));
            }
        }
        return listOfTransactions;
    }
}
