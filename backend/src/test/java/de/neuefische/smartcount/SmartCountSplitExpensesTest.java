package de.neuefische.smartcount;

import de.neuefische.smartcount.Users.User;
import de.neuefische.smartcount.Users.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;
import static de.neuefische.smartcount.Currency.*;
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
    void twoUsersOnePayingTheOtherWithOneItem() {
        // given
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
        Collection<Expense> allByBernadette = List.of(expenseB1);
        Mockito.when(repo.findAllByUser("Bernadette")).thenReturn(allByBernadette);

        SmartCountService expenseService = new SmartCountService(repo, userRepository);

        expenseService.createExpense(expenseB1);
        List<Expense> expenseList = List.of(expenseB1);
        Mockito.when(repo.findAll()).thenReturn(expenseList);

        List<TransactionsDTO> actual = expenseService.amountPerPerson();
        TransactionsDTO transactionDTO = new TransactionsDTO("Achim", "Bernadette", 45.0);
        List<TransactionsDTO> transactionsList = List.of(transactionDTO);

        // then
        assertThat(actual).isEqualTo(transactionsList);
    }

    @Test
    void twoUsersOnePayingTheOtherWithOneItemEach() {
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
    void groupOfThreeWithOnePayingTwice() {
        // given
        Expense expenseA1 = new Expense();
        expenseA1.setPurpose("Tanken");
        expenseA1.setDescription("Aral");
        expenseA1.setAmount(60.0);
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
        User user3 = new User("778900", "Claudius", "pwd1256656");

        UserRepository userRepository = Mockito.mock(UserRepository.class);

        List<User> userList = List.of(user1, user2, user3);
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
        TransactionsDTO transactionDTO1 = new TransactionsDTO("Claudius", "Achim", 10.0);
        TransactionsDTO transactionDTO2 = new TransactionsDTO("Claudius", "Bernadette", 40.0);
        List<TransactionsDTO> transactionsList = List.of(transactionDTO1, transactionDTO2);

        // then
        assertThat(actual).isEqualTo(transactionsList);

    }

    @Test
    void groupOfThreeWithTwoPaying() {
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

        Expense expenseB2 = new Expense();
        expenseB2.setPurpose("Blumen");
        expenseB2.setPurpose("Blume5000");
        expenseB2.setAmount(12.95);
        expenseB2.setCurrency(EUR);
        expenseB2.setUser("Bernadette");

        User user1 = new User("778899", "Achim", "pwd12345677");
        User user2 = new User("778900", "Bernadette", "pwd1234556");
        User user3 = new User("778900", "Claudius", "pwd1256656");

        UserRepository userRepository = Mockito.mock(UserRepository.class);

        List<User> userList = List.of(user1, user2, user3);
        Mockito.when(userRepository.findAll()).thenReturn(userList);

        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        Collection<Expense> allByAchim = List.of(expenseA1);
        Mockito.when(repo.findAllByUser("Achim")).thenReturn(allByAchim);
        Collection<Expense> allByBernadette = List.of(expenseB1, expenseB2);
        Mockito.when(repo.findAllByUser("Bernadette")).thenReturn(allByBernadette);

        SmartCountService expenseService = new SmartCountService(repo, userRepository);

        // when
        expenseService.createExpense(expenseA1);
        expenseService.createExpense(expenseB1);
        expenseService.createExpense(expenseB2);
        List<Expense> expenseList = List.of(expenseA1, expenseB1, expenseB2);
        Mockito.when(repo.findAll()).thenReturn(expenseList);

        List<TransactionsDTO> actual = expenseService.amountPerPerson();
        TransactionsDTO transactionDTO1 = new TransactionsDTO("Achim", "Bernadette", 1.98);
        TransactionsDTO transactionDTO2 = new TransactionsDTO("Claudius", "Bernadette", 50.48);
        List<TransactionsDTO> transactionsList = List.of(transactionDTO1, transactionDTO2);

        // then
        assertThat(actual).isEqualTo(transactionsList);
    }

    @Test
    void dealWithOneCentInAGroupOfThree() {
        // given
        Expense expenseA1 = new Expense();
        expenseA1.setPurpose("NerdEnergy");
        expenseA1.setDescription("full power");
        expenseA1.setAmount(0.01);
        expenseA1.setCurrency(EUR);
        expenseA1.setUser("Achim");

        User user1 = new User("778899", "Achim", "pwd12345677");
        User user2 = new User("778900", "Bernadette", "pwd1234556");
        User user3 = new User("778900", "Claudius", "pwd1256656");

        UserRepository userRepository = Mockito.mock(UserRepository.class);

        List<User> userList = List.of(user1, user2, user3);
        Mockito.when(userRepository.findAll()).thenReturn(userList);

        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        Collection<Expense> allByAchim = List.of(expenseA1);
        Mockito.when(repo.findAllByUser("Achim")).thenReturn(allByAchim);

        SmartCountService expenseService = new SmartCountService(repo, userRepository);

        // when
        expenseService.createExpense(expenseA1);
        List<Expense> expenseList = List.of(expenseA1);
        Mockito.when(repo.findAll()).thenReturn(expenseList);

        // then
        Assertions.assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> expenseService.amountPerPerson())
                .withMessage("There is no sum big enough to be divided.");
    }

    @Test
    void dealWithTenCentsInAGroupOfThree() {
        // given
        Expense expenseA1 = new Expense();
        expenseA1.setPurpose("Wasser");
        expenseA1.setAmount(0.1);
        expenseA1.setCurrency(EUR);
        expenseA1.setUser("Achim");

        User user1 = new User("778899", "Achim", "pwd12345677");
        User user2 = new User("778900", "Bernadette", "pwd1234556");
        User user3 = new User("778900", "Claudius", "pwd1256656");

        UserRepository userRepository = Mockito.mock(UserRepository.class);

        List<User> userList = List.of(user1, user2, user3);
        Mockito.when(userRepository.findAll()).thenReturn(userList);

        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        Collection<Expense> allByAchim = List.of(expenseA1);
        Mockito.when(repo.findAllByUser("Achim")).thenReturn(allByAchim);

        SmartCountService expenseService = new SmartCountService(repo, userRepository);

        // when
        expenseService.createExpense(expenseA1);
        List<Expense> expenseList = List.of(expenseA1);
        Mockito.when(repo.findAll()).thenReturn(expenseList);

        List<TransactionsDTO> actual = expenseService.amountPerPerson();
        TransactionsDTO transactionDTO1 = new TransactionsDTO("Claudius", "Achim", 0.03);
        TransactionsDTO transactionDTO2 = new TransactionsDTO("Bernadette", "Achim", 0.03);
        List<TransactionsDTO> transactionsList = List.of(transactionDTO1, transactionDTO2);

        // then
        assertThat(actual).isEqualTo(transactionsList);
    }

    @Test
    void dealWithSixCentsInAGroupOfSeven() {
        // given
        Expense expenseA1 = new Expense();
        expenseA1.setPurpose("NerdEnergy");
        expenseA1.setAmount(0.06);
        expenseA1.setCurrency(EUR);
        expenseA1.setUser("Achim");

        User user1 = new User("778899", "Achim", "pwd12345677");
        User user2 = new User("778900", "Bernadette", "pwd1234556");
        User user3 = new User("778900", "Claudius", "pwd1256656");
        User user4 = new User("778900", "Damian", "pwd1256656");
        User user5 = new User("778900", "Enrique", "pwd1256656");
        User user6 = new User("778900", "Fabienne", "pwd1256656");
        User user7 = new User("778900", "Gabi", "pwd1256656");

        UserRepository userRepository = Mockito.mock(UserRepository.class);

        List<User> userList = List.of(user1, user2, user3, user4, user5, user6, user7);
        Mockito.when(userRepository.findAll()).thenReturn(userList);

        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        Collection<Expense> allByAchim = List.of(expenseA1);
        Mockito.when(repo.findAllByUser("Achim")).thenReturn(allByAchim);

        SmartCountService expenseService = new SmartCountService(repo, userRepository);

        // when
        expenseService.createExpense(expenseA1);
        List<Expense> expenseList = List.of(expenseA1);
        Mockito.when(repo.findAll()).thenReturn(expenseList);

        List<TransactionsDTO> actual = expenseService.amountPerPerson();
        TransactionsDTO transactionDTO1 = new TransactionsDTO("Gabi", "Achim", 0.01);
        TransactionsDTO transactionDTO2 = new TransactionsDTO("Fabienne", "Achim", 0.01);
        TransactionsDTO transactionDTO3 = new TransactionsDTO("Enrique", "Achim", 0.01);
        TransactionsDTO transactionDTO4 = new TransactionsDTO("Damian", "Achim", 0.01);
        TransactionsDTO transactionDTO5 = new TransactionsDTO("Claudius", "Achim", 0.01);
        TransactionsDTO transactionDTO6 = new TransactionsDTO("Bernadette", "Achim", 0.01);
        List<TransactionsDTO> transactionsList = List.of(transactionDTO1, transactionDTO2, transactionDTO3, transactionDTO4, transactionDTO5, transactionDTO6);

        // then
        assertThat(actual).isEqualTo(transactionsList);
    }

    @Test
    void dealWithSevenDollarsInAGroupOfSeven() {
        // given
        Expense expenseA1 = new Expense();
        expenseA1.setPurpose("Brötchen");
        expenseA1.setAmount(7.0);
        expenseA1.setCurrency(USD);
        expenseA1.setUser("Achim");

        User user1 = new User("778899", "Achim", "pwd12345677");
        User user2 = new User("778900", "Bernadette", "pwd1234556");
        User user3 = new User("778900", "Claudius", "pwd1256656");
        User user4 = new User("778900", "Damian", "pwd1256656");
        User user5 = new User("778900", "Enrique", "pwd1256656");
        User user6 = new User("778900", "Fabienne", "pwd1256656");
        User user7 = new User("778900", "Gabi", "pwd1256656");

        UserRepository userRepository = Mockito.mock(UserRepository.class);

        List<User> userList = List.of(user1, user2, user3, user4, user5, user6, user7);
        Mockito.when(userRepository.findAll()).thenReturn(userList);

        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        Collection<Expense> allByAchim = List.of(expenseA1);
        Mockito.when(repo.findAllByUser("Achim")).thenReturn(allByAchim);

        SmartCountService expenseService = new SmartCountService(repo, userRepository);

        // when
        expenseService.createExpense(expenseA1);
        List<Expense> expenseList = List.of(expenseA1);
        Mockito.when(repo.findAll()).thenReturn(expenseList);

        List<TransactionsDTO> actual = expenseService.amountPerPerson();
        TransactionsDTO transactionDTO1 = new TransactionsDTO("Gabi", "Achim", 1.0);
        TransactionsDTO transactionDTO2 = new TransactionsDTO("Fabienne", "Achim", 1.0);
        TransactionsDTO transactionDTO3 = new TransactionsDTO("Enrique", "Achim", 1.0);
        TransactionsDTO transactionDTO4 = new TransactionsDTO("Damian", "Achim", 1.0);
        TransactionsDTO transactionDTO5 = new TransactionsDTO("Claudius", "Achim", 1.0);
        TransactionsDTO transactionDTO6 = new TransactionsDTO("Bernadette", "Achim", 1.0);
        List<TransactionsDTO> transactionsList = List.of(transactionDTO1, transactionDTO2, transactionDTO3, transactionDTO4, transactionDTO5, transactionDTO6);

        // then
        assertThat(actual).isEqualTo(transactionsList);
    }

    @Test
    void dealWithSevenCentsInAGroupOfSeven() {
        // given
        Expense expenseA1 = new Expense();
        expenseA1.setPurpose("some salt");
        expenseA1.setAmount(0.07);
        expenseA1.setCurrency(USD);
        expenseA1.setUser("Achim");

        User user1 = new User("778899", "Achim", "pwd12345677");
        User user2 = new User("778900", "Bernadette", "pwd1234556");
        User user3 = new User("778900", "Claudius", "pwd1256656");
        User user4 = new User("778900", "Damian", "pwd1256656");
        User user5 = new User("778900", "Enrique", "pwd1256656");
        User user6 = new User("778900", "Fabienne", "pwd1256656");
        User user7 = new User("778900", "Gabi", "pwd1256656");

        UserRepository userRepository = Mockito.mock(UserRepository.class);

        List<User> userList = List.of(user1, user2, user3, user4, user5, user6, user7);
        Mockito.when(userRepository.findAll()).thenReturn(userList);

        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        Collection<Expense> allByAchim = List.of(expenseA1);
        Mockito.when(repo.findAllByUser("Achim")).thenReturn(allByAchim);

        SmartCountService expenseService = new SmartCountService(repo, userRepository);

        // when
        expenseService.createExpense(expenseA1);
        List<Expense> expenseList = List.of(expenseA1);
        Mockito.when(repo.findAll()).thenReturn(expenseList);

        List<TransactionsDTO> actual = expenseService.amountPerPerson();
        TransactionsDTO transactionDTO1 = new TransactionsDTO("Gabi", "Achim", 0.01);
        TransactionsDTO transactionDTO2 = new TransactionsDTO("Fabienne", "Achim", 0.01);
        TransactionsDTO transactionDTO3 = new TransactionsDTO("Enrique", "Achim", 0.01);
        TransactionsDTO transactionDTO4 = new TransactionsDTO("Damian", "Achim", 0.01);
        TransactionsDTO transactionDTO5 = new TransactionsDTO("Claudius", "Achim", 0.01);
        TransactionsDTO transactionDTO6 = new TransactionsDTO("Bernadette", "Achim", 0.01);
        List<TransactionsDTO> transactionsList = List.of(transactionDTO1, transactionDTO2, transactionDTO3, transactionDTO4, transactionDTO5, transactionDTO6);

        // then
        assertThat(actual).isEqualTo(transactionsList);
    }

    @Test
    void OneUserOnly() {
        // given
        User user1 = new User("778899", "Achim", "pwd12345677");
        List<User> userList = List.of(user1);

        UserRepository userRepository = Mockito.mock(UserRepository.class);
        Mockito.when(userRepository.findAll()).thenReturn(userList);

        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        SmartCountService expenseService = new SmartCountService(repo, userRepository);

        // then
        Assertions.assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> expenseService.amountPerPerson())
                .withMessage("There is no sum big enough to be divided.");
    }

    @Test
    void OneUserOnlyWithOneExpense() {
        // given
        Expense expenseB1 = new Expense();
        expenseB1.setPurpose("Tanken");
        expenseB1.setPurpose("Jet");
        expenseB1.setAmount(90.0);
        expenseB1.setCurrency(EUR);
        expenseB1.setUser("Bernadette");

        User user = new User("778900", "Bernadette", "pwd1234556");
        List<User> userList = List.of(user);

        UserRepository userRepository = Mockito.mock(UserRepository.class);
        Mockito.when(userRepository.findAll()).thenReturn(userList);

        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        Collection<Expense> allByBernadette = List.of(expenseB1);
        Mockito.when(repo.findAllByUser("Bernadette")).thenReturn(allByBernadette);

        SmartCountService expenseService = new SmartCountService(repo, userRepository);

        expenseService.createExpense(expenseB1);
        List<Expense> expenseList = List.of(expenseB1);
        Mockito.when(repo.findAll()).thenReturn(expenseList);

        // then
        Assertions.assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> expenseService.amountPerPerson())
                .withMessage("There is only one user in this group. No transactions will take place.");
    }

    @Test
    void groupOfFourWhereAllHaveNoExpenses() {
        //given
        User user1 = new User("778899", "Achim", "pwd12345677");
        User user2 = new User("778900", "Bernadette", "pwd1234556");
        User user3 = new User("778900", "Claudius", "pwd1256656");
        User user4 = new User("778900", "Damian", "pwd1256656");

        UserRepository userRepository = Mockito.mock(UserRepository.class);

        List<User> userList = List.of(user1, user2, user3, user4);
        Mockito.when(userRepository.findAll()).thenReturn(userList);
        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        SmartCountService expenseService = new SmartCountService(repo, userRepository);

        // then
        Assertions.assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> expenseService.amountPerPerson())
                .withMessage("There is no sum big enough to be divided.");
    }

    @Test
    void groupOfFiveWhereTwoHaveNoExpenses() {
        // given
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

        User user1 = new User("778899", "Achim", "pwd12345677");
        User user2 = new User("778900", "Bernadette", "pwd1234556");
        User user3 = new User("778901", "Claus", "pwd125556");
        User user4 = new User("778902", "Detlev", "pwd12444");
        User user5 = new User("778903", "Eva", "pwd127777");

        UserRepository userRepository = Mockito.mock(UserRepository.class);
        List<User> userList = List.of(user1, user2, user3, user4, user5);
        Mockito.when(userRepository.findAll()).thenReturn(userList);

        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        SmartCountService expenseService = new SmartCountService(repo, userRepository);

        Collection<Expense> allByBernadette = List.of(expenseB1, expenseB2);
        Mockito.when(repo.findAllByUser("Bernadette")).thenReturn(allByBernadette);
        Collection<Expense> allByDetlev = List.of(expenseD1, expenseD2, expenseD3);
        Mockito.when(repo.findAllByUser("Detlev")).thenReturn(allByDetlev);
        Collection<Expense> allByEva = List.of(expenseE1, expenseE2, expenseE3);
        Mockito.when(repo.findAllByUser("Eva")).thenReturn(allByEva);

        List<Expense> expenseList = List.of(expenseB1, expenseB2, expenseD1, expenseD2, expenseD3, expenseE1, expenseE2, expenseE3);
        Mockito.when(repo.findAll()).thenReturn(expenseList);

        // when
        List<TransactionsDTO> actual = expenseService.amountPerPerson();

        TransactionsDTO transactionDTO1 = new TransactionsDTO("Detlev", "Bernadette", 27.76);
        TransactionsDTO transactionDTO2 = new TransactionsDTO("Claus", "Bernadette", 3.57);
        TransactionsDTO transactionDTO3 = new TransactionsDTO("Claus", "Eva", 48.05);
        TransactionsDTO transactionDTO4 = new TransactionsDTO("Achim", "Eva", 51.62);
        List<TransactionsDTO> transactionsList = List.of(transactionDTO1, transactionDTO2, transactionDTO3, transactionDTO4);

        expenseService.amountPerPerson();

        // then
        assertThat(actual).isEqualTo(transactionsList);
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

        User user1 = new User("778899", "Achim", "pwd12345677");
        User user2 = new User("778900", "Bernadette", "pwd1234556");
        User user3 = new User("778901", "Claus", "pwd125556");
        User user4 = new User("778902", "Detlev", "pwd12444");
        User user5 = new User("778903", "Eva", "pwd127777");

        UserRepository userRepository = Mockito.mock(UserRepository.class);
        List<User> userList = List.of(user1, user2, user3, user4, user5);
        Mockito.when(userRepository.findAll()).thenReturn(userList);

        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        SmartCountService expenseService = new SmartCountService(repo, userRepository);

        Collection<Expense> allByAchim = List.of(expenseA1);
        Mockito.when(repo.findAllByUser("Achim")).thenReturn(allByAchim);
        Collection<Expense> allByBernadette = List.of(expenseB1, expenseB2);
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
        List<TransactionsDTO> actual = expenseService.amountPerPerson();

        TransactionsDTO transactionDTO1 = new TransactionsDTO("Achim", "Bernadette", 0.02);
        TransactionsDTO transactionDTO2 = new TransactionsDTO("Detlev", "Bernadette", 17.41);
        TransactionsDTO transactionDTO3 = new TransactionsDTO("Detlev", "Eva", 24.25);
        TransactionsDTO transactionDTO4 = new TransactionsDTO("Claus", "Eva", 61.54);
        List<TransactionsDTO> transactionsList = List.of(transactionDTO1, transactionDTO2, transactionDTO3, transactionDTO4);

        expenseService.amountPerPerson();

        // then
        assertThat(actual).isEqualTo(transactionsList);
    }
}