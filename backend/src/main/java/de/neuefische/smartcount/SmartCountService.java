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

//  ---------   Doing The Math   ---------   //

    public List<String> getUserList() {
        return userRepo.findAll().stream()
                .map(user -> user.getUsername()).toList();
    }

    public List<TransactionsDTO> amountPerPerson() {

        List<User> userList = userRepo.findAll();
        int numberOfPersons = userList.size();

        System.out.println("number of people = " + numberOfPersons);                              //printout

        double arithMean = getSum()/numberOfPersons;

        System.out.println("mean = " + arithMean);                              //printout

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

        System.out.println("\nPeople with Excess are: " + usersWithExcess);                              //printout
        System.out.println("People with Deficit are: " + usersWithDeficit);                            //printout

        usersWithExcess.sort(Comparator.comparing(UserDOO::userDelta).reversed());
        usersWithDeficit.sort(Comparator.comparing(UserDOO::userDelta).reversed());

        System.out.println("\nPeople with Excess in sorted order are: " + usersWithExcess);                              //printout
        System.out.println("People with Deficit in sorted order are: " + usersWithDeficit);                            //printout

        ArrayList<TransactionsDTO> listOfTransactions = new ArrayList<>();

        int e = usersWithExcess.size()-1;
        int d = usersWithDeficit.size()-1;

        while (e >= 0) {
            UserDOO userWithExcess = usersWithExcess.get(e);
            UserDOO userWithDeficit = usersWithDeficit.get(d);
            double exc = userWithExcess.userDelta();
            double defi = userWithDeficit.userDelta();
            if (exc == defi) {
                listOfTransactions.add(new TransactionsDTO(usersWithDeficit.get(d).userName(), usersWithExcess.get(e).userName(), exc));
                e--;
                d--;
            }
            else if (exc > defi) {
                listOfTransactions.add(new TransactionsDTO(usersWithDeficit.get(d).userName(), usersWithExcess.get(e).userName(), defi));
                d--;
                usersWithExcess.set(e, userWithExcess.changeDelta(exc-defi));
            }
            else {
                listOfTransactions.add(new TransactionsDTO(usersWithDeficit.get(d).userName(), usersWithExcess.get(e).userName(), exc));
                e--;
                usersWithDeficit.set(d, userWithDeficit.changeDelta(defi-exc));
            }
        }
        System.out.println(listOfTransactions);
        return listOfTransactions;
    }
}
