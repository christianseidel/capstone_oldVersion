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

    public Optional<List<String>> getUserList() {
        return userRepo.findAllUsers();
    }

    //  --------- Doing The Math ---------   //
/*
    public List<String> listOfUsers() {
        return expensesRepository.getUsers();
    }

    public int numberOfUsers() {
        List<String> set = expensesRepository.getUsers();
        return set.size();
    }

    public void amountPerPerson() {

        List<String> userList = listOfUsers();
        int numberOfPersons = userList.size();

        System.out.println("number of people: " + numberOfPersons);                              //printout

        double arithMean = getSum()/numberOfPersons;

        System.out.println("mean = " + arithMean);                              //printout

        Map<String, Double> mapOfUsersWithExcess = new HashMap<>();
        Map<String, Double> mapOfUsersWithDeficit = new HashMap<>();

        for (int i = 0; i < numberOfPersons; i++) {
            String user = userList.get(i);
            double sum = getSumByUser(user);
            if ((sum - arithMean) >= 0.0) {
                mapOfUsersWithExcess.put(user, sum);
            } else {
                mapOfUsersWithDeficit.put(user, sum);
            }
        }

        System.out.println("\nPeople with Excess are: " + mapOfUsersWithExcess);                              //printout
        System.out.println("People with Deficit are: " + mapOfUsersWithDeficit + "\n");                            //printout

        HashMap<String, Double> mapOfUsersWithExcessSorted = new HashMap<>();
        HashMap<String, Double> mapOfUsersWithDeficitSorted = new HashMap<>();

        System.out.println("Nutzer mit Überschuss: ");
        System.out.println(mapOfUsersWithExcess);
        mapOfUsersWithExcess
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(item -> mapOfUsersWithExcessSorted.put(item.getKey(), item.getValue()));
        System.out.println(mapOfUsersWithExcessSorted);


        System.out.println("Nutzer mit Defizit: ");
        System.out.println(mapOfUsersWithDeficit);
        mapOfUsersWithDeficit.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Double::compareTo))
                .forEach(item -> mapOfUsersWithDeficitSorted.put(item.getKey(), item.getValue()));
        System.out.println(mapOfUsersWithDeficitSorted);

        // same with printout:
        mapOfUsersWithExcess
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Double::compareTo))
                .forEach(System.out::println);
        mapOfUsersWithDeficit.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Double::compareTo))
                .forEachOrdered(System.out::println);



    }

 */
}
