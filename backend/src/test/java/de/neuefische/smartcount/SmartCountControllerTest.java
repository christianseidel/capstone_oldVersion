package de.neuefische.smartcount;

import de.neuefische.smartcount.Users.UserController;
import de.neuefische.smartcount.Users.UserCreationData;
import de.neuefische.smartcount.Users.UserLoginData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SmartCountControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void integrationTest() {

        // should register new user
        UserCreationData createUser1 = new UserCreationData();
        createUser1.setUsername("Alfred");
        createUser1.setPassword("234SoSafe");
        createUser1.setPasswordAgain("234SoSafe");

        ResponseEntity<String> responseRegister = restTemplate.postForEntity("/api/register", createUser1, String.class);

        assertThat(responseRegister.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertEquals("user successfully created", responseRegister.getBody());

        // should log in user


}

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
