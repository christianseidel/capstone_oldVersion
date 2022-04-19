package de.neuefische.smartcount;

import de.neuefische.smartcount.Exceptions.InvalidUserException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

class SmartCountServiceTest {

    @Test
    void addNewExpense() {
        //given
        Expense expense01 = new Expense();
        expense01.setAmount(34.34);
        expense01.setCurrency(Currency.EUR);
        expense01.setPurpose("Einkaufen");

        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        SmartCountService expenseService = new SmartCountService(repo);

        // when
        expenseService.createExpense(expense01);

        // then
        verify(repo).save(expense01);
    }

    @Test
    void retrieveAllExpenses() {
        // given
        Expense expense03 = new Expense();
        expense03.setAmount(9.70);
        expense03.setCurrency(Currency.EUR);
        expense03.setPurpose("Shoppen");
        expense03.setUser("Kim");
        Expense expense04 = new Expense();
        expense04.setAmount(62.01);
        expense04.setCurrency(Currency.EUR);
        expense04.setPurpose("Tanken");
        expense04.setUser("Sabine");

        List<Expense> expenseList = List.of(expense03, expense04);
        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        Mockito.when(repo.findAll()).thenReturn(expenseList);

        // when
        SmartCountService expenseService = new SmartCountService(repo);
        Collection<Expense> actual = expenseService.getAllExpenses();

        // then
        assertThat(actual).isEqualTo(expenseList);
    }

    @Test
    void retrieveTwoExpensesByUser() {
        // given
        Expense expense03 = new Expense();
        expense03.setAmount(9.70);
        expense03.setCurrency(Currency.EUR);
        expense03.setPurpose("Shoppen");
        expense03.setUser("Kim");
        Expense expense04 = new Expense();
        expense04.setAmount(62.01);
        expense04.setCurrency(Currency.EUR);
        expense04.setPurpose("Tanken");
        expense04.setUser("Sabine");
        Expense expense05 = new Expense();
        expense05.setAmount(77.07);
        expense05.setCurrency(Currency.EUR);
        expense05.setPurpose("Reifenwechsel");
        expense05.setUser("Kim");

        List<Expense> expenseList = List.of(expense03, expense05);
        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        Mockito.when(repo.findAllByUser("Kim")).thenReturn(expenseList);

        // when
        SmartCountService expenseService = new SmartCountService(repo);
        Collection<Expense> actual = expenseService.getExpensesByUser("Kim");

        // then
        assertThat(actual).isEqualTo(expenseList);
    }
    
    @Test
    void getSumOfExpenses() {
        // given
        Expense expense03 = new Expense();
        expense03.setAmount(9.70);
        expense03.setCurrency(Currency.EUR);
        expense03.setPurpose("Shoppen");
        expense03.setUser("Kim");
        Expense expense04 = new Expense();
        expense04.setAmount(62.01);
        expense04.setCurrency(Currency.EUR);
        expense04.setPurpose("Tanken");
        expense04.setUser("Sabine");

        List<Expense> expenseList = List.of(expense03, expense04);
        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        Mockito.when(repo.findAll()).thenReturn(expenseList);

        // when
        SmartCountService expenseService = new SmartCountService(repo);
        Double actual = expenseService.getSum();

        // then
        assertThat(actual).isEqualTo(71.71);
    }

    @Test
    void getSumOfTwoExpensesByUser() {
        // given
        Expense expenseA = new Expense();
        expenseA.setAmount(9.70);
        expenseA.setCurrency(Currency.EUR);
        expenseA.setPurpose("Shoppen");
        expenseA.setUser("hans");
        Expense expenseB = new Expense();
        expenseB.setAmount(62.01);
        expenseB.setCurrency(Currency.EUR);
        expenseB.setPurpose("Tanken");
        expenseB.setUser("franz");
        Expense expenseC = new Expense();
        expenseC.setAmount(35.35);
        expenseC.setCurrency(Currency.EUR);
        expenseC.setPurpose("Pizza");
        expenseC.setUser("hans");

        List<Expense> expenseList = List.of(expenseA, expenseC);
        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        Mockito.when(repo.findAllByUser("hans")).thenReturn(expenseList);

        // when
        SmartCountService expenseService = new SmartCountService(repo);
        Double actual = expenseService.getSumByUser("hans");

        // then
        assertThat(actual).isEqualTo(45.05);
    }

    @Test
    void deleteOneExpense() {
        // given
        Expense expense04 = new Expense();
        expense04.setAmount(62.01);
        expense04.setCurrency(Currency.EUR);
        expense04.setPurpose("Tanken");
        expense04.setUser("Sabine");
        expense04.setId(("0004"));

        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        SmartCountService expenseService = new SmartCountService(repo);
        Mockito.when(repo.findByIdAndUser("0004", "Sabine")).thenReturn(Optional.of(expense04));

        // when
        expenseService.deleteExpense("0004", "Sabine");

        // then
        verify(repo).deleteById("0004");
    }

    @Test
    void deleteWhenExpenseDoesntExist() {
        //given
        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        SmartCountService expenseService = new SmartCountService(repo);
        Mockito.when(repo.findByIdAndUser("0004", "Steffi")).thenReturn(Optional.empty());

        // then
        Assertions.assertThatExceptionOfType(RuntimeException.class)
            .isThrownBy(()->expenseService.deleteExpense("0004", "Steffi"));
    }

    @Test
    void deleteOneExpenseWithWrongUser() {
        // given
        Expense myExpense = new Expense();
        myExpense.setAmount(100.01);
        myExpense.setCurrency(Currency.JPY);
        myExpense.setPurpose("Sushi Essen");
        myExpense.setDescription("am See");
        myExpense.setUser("Marion");
        myExpense.setId("2444");

        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        Mockito.when(repo.findByIdAndUser("2444", "Marion")).thenReturn(Optional.of(myExpense));

        // when
        SmartCountService expenseService = new SmartCountService(repo);

        // then
        Assertions.assertThatExceptionOfType(InvalidUserException.class)
                .isThrownBy(() -> expenseService.getSingleExpense("2444", "Franz"))
                .withMessage("You don't have access to this item");
    }


    @Test
    void retrieveOneExpense() {
        // given
        Expense expense06 = new Expense();
        expense06.setAmount(100.01);
        expense06.setCurrency(Currency.JPY);
        expense06.setPurpose("Sushi Essen");
        expense06.setDescription("am Strand");
        expense06.setUser("Franz");
        expense06.setId("2222");

        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        Mockito.when(repo.findByIdAndUser("2222", "Franz")).thenReturn(Optional.of(expense06));

        // when
        SmartCountService expenseService = new SmartCountService(repo);
        Optional<Expense> actual = expenseService.getSingleExpense("2222", "Franz");

        // then
        Assertions.assertThat(actual).contains(expense06);
    }

    @Test
    void retrieveOneExpenseWithWrongUser() {
        // given
        Expense expense06 = new Expense();
        expense06.setAmount(100.01);
        expense06.setCurrency(Currency.JPY);
        expense06.setPurpose("Sushi Essen");
        expense06.setDescription("zuhause");
        expense06.setUser("Gabriele");
        expense06.setId("2222");

        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        Mockito.when(repo.findByIdAndUser("2222", "Gabriele")).thenReturn(Optional.of(expense06));

        // when
        SmartCountService expenseService = new SmartCountService(repo);

        // then
        Assertions.assertThatExceptionOfType(InvalidUserException.class)
                .isThrownBy(() -> expenseService.getSingleExpense("2222", "Franz"))
                .withMessage("You don't have access to this item");
    }


    @Test
    void changeExpense() {
        // given
        Expense expense07changed = new Expense();
        expense07changed.setId("2333");
        expense07changed.setPurpose("Currywurst");
        expense07changed.setAmount(10.99);
        expense07changed.setCurrency(Currency.USD);
        expense07changed.setDescription("am Kotti");
        expense07changed.setUser("Sven");

        Expense expense07saved = new Expense();
        expense07saved.setId("2333");
        expense07saved.setPurpose("Currywurst");
        expense07saved.setAmount(10.99);
        expense07saved.setCurrency(Currency.USD);
        expense07saved.setDescription("am Kotti");
        expense07saved.setUser("Sven");

        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        Mockito.when(repo.findByIdAndUser("2333", "Sven")).thenReturn(Optional.of(new Expense()));
        Mockito.when(repo.save(expense07changed)).thenReturn(expense07saved);

        // when
        SmartCountService expenseService = new SmartCountService(repo);
        Optional<Expense> actual = expenseService.editExpense("2333", expense07changed, "Sven");

        // then
        Assertions.assertThat(actual).contains(expense07saved);
    }

    @Test
    void changeExpenseWithWrongId() {

        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        Mockito.when(repo.findByIdAndUser("2333", "Mark")).thenReturn(Optional.empty());

        // when
        SmartCountService expenseService = new SmartCountService(repo);
        Optional<Expense> actual = expenseService.editExpense("2333", null, "Mark");

        // then
        Assertions.assertThat(actual).isEmpty();

    }

}