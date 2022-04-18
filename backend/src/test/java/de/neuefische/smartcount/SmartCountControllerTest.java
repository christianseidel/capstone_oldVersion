package de.neuefische.smartcount;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static de.neuefische.smartcount.Currency.EUR;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SmartCountControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void addExpenses() {
        Expense expense01 = new Expense();
        expense01.setPurpose("Reifen wechseln");
        expense01.setDescription("Auto Wagner");
        expense01.setAmount(26.0);
        expense01.setCurrency(EUR);
        expense01.setUser("Andrea");

    }
}

/*
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SmartCountControllerTest {

    // The Following is Test Driven Development (TDD).
    // Incremental. Chronological.
/*
    @Test
    void splittingUpExpenses() {

       // ExpensesRepository testRepo = new ExpensesRepository();
        SmartCountService testService = new SmartCountService(testRepo);

        // verify if database is empty

        // when
        double sum = testService.getSum();
        // then
        Assertions.assertEquals(0, sum);
    }

}
*/