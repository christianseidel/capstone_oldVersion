package de.neuefische.smartcount;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;

import javax.swing.text.html.Option;
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
        expense01.setUser("Stefan");

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
        Collection<Expense> actual = expenseService.getExpenses();

        // then
        assertThat(actual).isEqualTo(expenseList);
    }

    @Test
    void deleteOneExpense() {
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
        expense05.setAmount(11.20);
        expense05.setDescription("Brot und Käse");
        expense05.setCurrency(Currency.EUR);
        expense05.setPurpose("Markt");
        expense05.setUser("Lydia");

        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        SmartCountService expenseService = new SmartCountService(repo);
        expenseService.createExpense(expense03);
        expenseService.createExpense(expense04);
        expenseService.createExpense(expense05);

        // when
        expenseService.deleteExpense(expense04.getId());

        // then
        verify(repo).deleteById(expense04.getId());
    }

    @Test
    void retrieveOneExpense() {
        // given
        Expense expense06 = new Expense();
        expense06.setAmount(100.01);
        expense06.setCurrency(Currency.JPY);
        expense06.setPurpose("Shusi Essen");
        expense06.setDescription("am Strand");
        expense06.setUser("Franz");
        expense06.setId("2222");

        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        Mockito.when(repo.findById("2222")).thenReturn(Optional.of(expense06));

        // when
        SmartCountService expenseService = new SmartCountService(repo);
        Optional<Expense> actual = expenseService.getSingleExpense("2222");

        // then
        verify(repo).findById("2222");
    }

    @Test
    void changeExpense() {
        // given
        Expense expense07 = new Expense();
        expense07.setAmount(109.99);
        expense07.setCurrency(Currency.USD);
        expense07.setPurpose("Currywourst");
        expense07.setUser("Sven");
        expense07.setId("2333");

        Expense expense07changed = new Expense();
        expense07changed.setId("2333");
        expense07changed.setPurpose("Currywurst");
        expense07changed.setAmount(10.99);
        expense07changed.setCurrency(Currency.USD);
        expense07changed.setDescription("am Kotti");
        expense07changed.setUser("Sven");

        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        Mockito.when(repo.findById("2333")).thenReturn(Optional.of(expense07));

        // when
        SmartCountService expenseService = new SmartCountService(repo);
        expenseService.editExpense("2333", expense07changed);

        // then
        verify(repo).save(expense07changed);
    }
}