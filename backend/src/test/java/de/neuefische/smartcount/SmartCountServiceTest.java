package de.neuefische.smartcount;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

class SmartCountServiceTest {

    @Test
    void addNewExpense() {
        //given
        Expense expense01 = new Expense();
        expense01.setAmount(34.34);
        expense01.setCurrency(Currency.Euro);
        expense01.setPurpose("Einkaufen");
        expense01.setUser("Stefan");

        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        SmartCountService expenseService = new SmartCountService(repo);

        //when
        expenseService.createExpense(expense01);

        //then
        verify(repo).save(expense01);
    }


    @Test
    void retrieveAllExpenses() {
        // given
        Expense expense03 = new Expense();
        expense03.setAmount(9.70);
        expense03.setCurrency(Currency.Euro);
        expense03.setPurpose("Shoppen");
        expense03.setUser("Kim");
        Expense expense04 = new Expense();
        expense04.setAmount(62.01);
        expense04.setCurrency(Currency.Euro);
        expense04.setPurpose("Tanken");
        expense04.setUser("Sabine");

        List<Expense> expenseList = List.of(expense03, expense04);
        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        Mockito.when(repo.findAll()).thenReturn(expenseList);

        // when
        SmartCountService expenseService = new SmartCountService(repo);
        Collection<Expense> actual = expenseService.getExpenses();

        //then
        assertThat(actual).isEqualTo(expenseList);
    }

    @Test
    void deleteOneExpense() {
        // given
        Expense expense03 = new Expense();
        expense03.setAmount(9.70);
        expense03.setCurrency(Currency.Euro);
        expense03.setPurpose("Shoppen");
        expense03.setUser("Kim");
        Expense expense04 = new Expense();
        expense04.setAmount(62.01);
        expense04.setCurrency(Currency.Euro);
        expense04.setPurpose("Tanken");
        expense04.setUser("Sabine");
        Expense expense05 = new Expense();
        expense05.setAmount(11.20);
        expense05.setDescription("Brot und Käse");
        expense05.setCurrency(Currency.Euro);
        expense05.setPurpose("Markt");
        expense05.setUser("Lydia");

        ExpensesRepository repo = Mockito.mock(ExpensesRepository.class);
        SmartCountService expenseService = new SmartCountService(repo);
        expenseService.createExpense(expense03);
        expenseService.createExpense(expense04);
        expenseService.createExpense(expense05);

        //when
        expenseService.deleteExpense(expense04.getId());

        //then
        verify(repo).deleteById(expense04.getId());
    }
}