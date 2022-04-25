package de.neuefische.smartcount;

import de.neuefische.smartcount.Users.Authentification.Token;
import de.neuefische.smartcount.Users.UserController;
import de.neuefische.smartcount.Users.UserCreationData;
import de.neuefische.smartcount.Users.UserLoginData;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static de.neuefische.smartcount.Currency.EUR;
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

        ResponseEntity<String> responseRegisterUser1 = restTemplate.postForEntity("/api/register", createUser1, String.class);

        assertThat(responseRegisterUser1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertEquals("user successfully created", responseRegisterUser1.getBody());

        // should log in user
        UserLoginData loginUser1 = new UserLoginData();
        loginUser1.setUsername("Alfred");
        loginUser1.setPassword("234SoSafe");

        ResponseEntity<Token> responseLoginUser1 = restTemplate.postForEntity("/api/login", loginUser1, Token.class);

        assertThat(responseLoginUser1.getStatusCode()).isEqualTo(HttpStatus.OK);

        // should log out user

        // should not log in user
        UserLoginData loginUser1f = new UserLoginData();
        loginUser1f.setUsername(createUser1.getUsername());
        loginUser1f.setPassword("234NotSoSafe");

        ResponseEntity<Token> responseLoginUser1false = restTemplate.postForEntity("/api/login", loginUser1f, Token.class);

        assertThat(responseLoginUser1false.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        // should not log in user
        UserLoginData loginUser1twice = new UserLoginData();
        loginUser1twice.setUsername(createUser1.getUsername());
        loginUser1twice.setPassword("IWantToBePartOfIt");

        ResponseEntity<String> responseLoginUser1twice = restTemplate.postForEntity("/api/login", loginUser1f, String.class);

        assertThat(responseLoginUser1twice.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        // log in user again
        UserLoginData loginNewUser1 = new UserLoginData();
        loginNewUser1.setUsername("Alfred");
        loginNewUser1.setPassword("234SoSafe");
        ResponseEntity<Token> loginResponse = restTemplate.postForEntity("/api/login", loginNewUser1, Token.class);

        String token = loginResponse.getBody().toString();
        HttpHeaders bearerHeader = new HttpHeaders();
        bearerHeader.setBearerAuth(token);

        // should add new expenses
        Expense expense01 = new Expense();
        expense01.setPurpose("Grillkohle");
        expense01.setDescription("Aral");
        expense01.setAmount(12.9);
        expense01.setCurrency(EUR);

        ResponseEntity<Expense> createExpense01 = restTemplate.exchange(
                "/api/expenses",
                HttpMethod.POST,
                new HttpEntity<>(expense01, bearerHeader),
                Expense.class
        );

        Expense createdExpense = createExpense01.getBody();

 //       Assertions.assertThat(createExpense01.getStatusCode()).isEqualTo(HttpStatus.OK);
//        Assertions.assertThat(createdExpense).isEqualTo(createExpense01);

//        ResponseEntity<Expense> createExpense01 = restTemplate.postForEntity("/api/expenses", expense01, Expense.class);
//        Assertions.assertThat(createExpense01.getStatusCode()).isEqualTo(HttpStatus.OK);

        Expense expense02 = new Expense();
        expense02.setPurpose("Servietten");
        expense02.setDescription("Lidl");
        expense02.setAmount(2.75);
        expense02.setCurrency(EUR);
        expense02.setUser("Andrea");

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
