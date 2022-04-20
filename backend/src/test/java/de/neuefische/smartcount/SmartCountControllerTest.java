package de.neuefische.smartcount;

import de.neuefische.smartcount.Users.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static de.neuefische.smartcount.Currency.EUR;

//
// The Following is Test Driven Development (TDD).
// Incremental. Chronological.
//

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SmartCountControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
/*
    @Test
    void addExpenses() {
*/
        // register a new user
     //   ResponseEntity
     //   UserController userController = new UserController();

/*
        // create new expense
        Expense expense01 = new Expense();
        expense01.setPurpose("Reifen wechseln");
        expense01.setDescription("Auto Wagner");
        expense01.setAmount(26.0);
        expense01.setCurrency(EUR);
        expense01.setUser("Andrea");
*/
/*
    }
}

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
*/
}
