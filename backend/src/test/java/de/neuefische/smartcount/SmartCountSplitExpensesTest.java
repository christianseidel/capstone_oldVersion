package de.neuefische.smartcount;

import de.neuefische.smartcount.Users.UserRepository;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import org.mockito.Mockito;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static de.neuefische.smartcount.Currency.EUR;

class SmartCountSplitExpensesTest {

    @Test
    void getListOfUsers() {

        // given
        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        SmartCountService expenseService = new SmartCountService(repo, userRepository);

        List<String> userList = List.of("Achim", "Bernadette", "Claus");
        Mockito.when(userRepository.findAllUsers()).thenReturn(Optional.of(userList));

        // when
        Optional<List<String>> actual = expenseService.getUserList();

        // then
        assertThat(actual).contains(userList);
    }

    @Test
    void iDontKnowYet() {

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
        expenseB1.setAmount(70);
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
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        SmartCountService expenseService = new SmartCountService(repo, userRepository);

        List<String> userList = List.of("Achim", "Bernadette", "Claus", "Detlev", "Eva");
        Mockito.when(userRepository.findAllUsers()).thenReturn(Optional.of(userList));

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

        // service.amountPerPerson();

    }


}