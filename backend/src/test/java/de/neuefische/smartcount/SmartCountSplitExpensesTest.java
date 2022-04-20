package de.neuefische.smartcount;

import de.neuefische.smartcount.Users.User;
import de.neuefische.smartcount.Users.UserRepository;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import java.util.*;

import static de.neuefische.smartcount.Currency.EUR;
import static org.assertj.core.api.Assertions.*;

class SmartCountSplitExpensesTest {

    @Test
    void getListOfUsers() {

        // given
        User user1 = new User("778899", "Achim", "pwd12345677");
        User user2 = new User("778900", "Barbara", "pwd1234556");
        User user3 = new User("778901", "Clemens", "pwd125556");
        User user4 = new User("778902", "Elfi", "pwd12444");

        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        SmartCountService expenseService = new SmartCountService(repo, userRepository);

        List<User> userList = List.of(user1, user2, user3, user4);
        Mockito.when(userRepository.findAll()).thenReturn(userList);

        // when
        List<String> actual = expenseService.getUserList();
        List<String> listOfUsers = List.of("Achim", "Barbara", "Clemens", "Elfi");

        // then
        assertThat(actual).isEqualTo(listOfUsers);
    }

    @Test
    void twoUsersOnePayingTheOther() {
        // given
        Expense expenseA1 = new Expense();
        expenseA1.setPurpose("Tanken");
        expenseA1.setDescription("Aral");
        expenseA1.setAmount(48.5);
        expenseA1.setCurrency(EUR);
        expenseA1.setUser("Achim");

        Expense expenseB1 = new Expense();
        expenseB1.setPurpose("Tanken");
        expenseB1.setPurpose("Jet");
        expenseB1.setAmount(90.0);
        expenseB1.setCurrency(EUR);
        expenseB1.setUser("Bernadette");

        User user1 = new User("778899", "Achim", "pwd12345677");
        User user2 = new User("778900", "Bernadette", "pwd1234556");

        UserRepository userRepository = Mockito.mock(UserRepository.class);

        List<User> userList = List.of(user1, user2);
        Mockito.when(userRepository.findAll()).thenReturn(userList);

        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        Collection<Expense> allByAchim = List.of(expenseA1);
        Mockito.when(repo.findAllByUser("Achim")).thenReturn(allByAchim);
        Collection<Expense> allByBernadette = List.of(expenseB1);
        Mockito.when(repo.findAllByUser("Bernadette")).thenReturn(allByBernadette);

        SmartCountService expenseService = new SmartCountService(repo, userRepository);

        // when
        expenseService.createExpense(expenseA1);
        expenseService.createExpense(expenseB1);
        List<Expense> expenseList = List.of(expenseA1, expenseB1);
        Mockito.when(repo.findAll()).thenReturn(expenseList);

        List<TransactionsDTO> actual = expenseService.amountPerPerson();
        TransactionsDTO transactionDTO = new TransactionsDTO("Achim", "Bernadette", 20.75);
        List<TransactionsDTO> transactionsList = List.of(transactionDTO);

        // then
        assertThat(actual).isEqualTo(transactionsList);
    }

    @Test
    void groupOfThreeWithOnePaying() {

    }

    @Test
    void groupOfThreeWithTwoPaying() {

    }

    @Test
    void groupOfFourWhereOneHasNoExpenses() {

    }

    @Test
    void groupOfFiveWhereTwoHaveNoExpenses() {

    }

    @Test
    void groupOfFiveWhereAllHaveExpenses() {
        // given
        Expense expenseA1 = new Expense();
        expenseA1.setPurpose("Tanken");
        expenseA1.setDescription("Aral");
        expenseA1.setAmount(65.5);
        expenseA1.setCurrency(EUR);
        expenseA1.setUser("Achim");

        Expense expenseB1 = new Expense();
        expenseB1.setPurpose("Tanken");
        expenseB1.setPurpose("Jet");
        expenseB1.setAmount(70.0);
        expenseB1.setCurrency(EUR);
        expenseB1.setUser("Bernadette");

        Expense expenseB2 = new Expense();
        expenseB2.setPurpose("Blumen");
        expenseB2.setPurpose("Blume5000");
        expenseB2.setAmount(12.95);
        expenseB2.setCurrency(EUR);
        expenseB2.setUser("Bernadette");

        Expense expenseC1 = new Expense();
        expenseC1.setPurpose("Tomaten");
        expenseC1.setPurpose("Lidl");
        expenseC1.setAmount(3.98);
        expenseC1.setCurrency(EUR);
        expenseC1.setUser("Claus");

        Expense expenseD1 = new Expense();
        expenseD1.setPurpose("Brot");
        expenseD1.setPurpose("Bäcker");
        expenseD1.setAmount(5.60);
        expenseD1.setCurrency(EUR);
        expenseD1.setUser("Detlev");

        Expense expenseD2 = new Expense();
        expenseD2.setPurpose("Butter");
        expenseD2.setPurpose("Lidl");
        expenseD2.setAmount(1.20);
        expenseD2.setCurrency(EUR);
        expenseD2.setUser("Detlev");

        Expense expenseD3 = new Expense();
        expenseD3.setPurpose("Käse");
        expenseD3.setPurpose("Lidl");
        expenseD3.setAmount(17.06);
        expenseD3.setCurrency(EUR);
        expenseD3.setUser("Detlev");
     
        Expense expenseE1 = new Expense();
        expenseE1.setPurpose("Kuchen");
        expenseE1.setPurpose("Bäcker");
        expenseE1.setAmount(12.17);
        expenseE1.setCurrency(EUR);
        expenseE1.setUser("Eva");

        Expense expenseE2 = new Expense();
        expenseE2.setPurpose("Pizza");
        expenseE2.setPurpose("da Toni");
        expenseE2.setAmount(62.03);
        expenseE2.setCurrency(EUR);
        expenseE2.setUser("Eva");

        Expense expenseE3 = new Expense();
        expenseE3.setPurpose("Einkauf");
        expenseE3.setPurpose("Aldi");
        expenseE3.setAmount(77.10);
        expenseE3.setCurrency(EUR);
        expenseE3.setUser("Eva");

        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);


        User user1 = new User("778899", "Achim", "pwd12345677");
        User user2 = new User("778900", "Bernadette", "pwd1234556");
        User user3 = new User("778901", "Claus", "pwd125556");
        User user4 = new User("778902", "Detlev", "pwd12444");
        User user5 = new User("778903", "Eva", "pwd127777");

        UserRepository userRepository = Mockito.mock(UserRepository.class);
        List<User> userList = List.of(user1, user2, user3, user4, user5);
        Mockito.when(userRepository.findAll()).thenReturn(userList);

        SmartCountService expenseService = new SmartCountService(repo, userRepository);

        Collection<Expense> allByAchim = List.of(expenseA1);
        Mockito.when(repo.findAllByUser("Achim")).thenReturn(allByAchim);
        Collection<Expense> allByBernadette = List.of(expenseB1,expenseB2);
        Mockito.when(repo.findAllByUser("Bernadette")).thenReturn(allByBernadette);
        Collection<Expense> allByClaus = List.of(expenseC1);
        Mockito.when(repo.findAllByUser("Claus")).thenReturn(allByClaus);
        Collection<Expense> allByDetlev = List.of(expenseD1, expenseD2, expenseD3);
        Mockito.when(repo.findAllByUser("Detlev")).thenReturn(allByDetlev);
        Collection<Expense> allByEva = List.of(expenseE1, expenseE2, expenseE3);
        Mockito.when(repo.findAllByUser("Eva")).thenReturn(allByEva);

        List<Expense> expenseList = List.of(expenseA1, expenseB1, expenseB2, expenseC1, expenseD1, expenseD2, expenseD3, expenseE1, expenseE2, expenseE3);
        Mockito.when(repo.findAll()).thenReturn(expenseList);

        // when
        expenseService.amountPerPerson();

    }


}