package de.neuefische.smartcount;

import de.neuefische.smartcount.Users.Authentification.Token;
import de.neuefische.smartcount.Users.UserController;
import de.neuefische.smartcount.Users.UserCreationData;
import de.neuefische.smartcount.Users.UserLoginData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
        createUser1.setUsername("Jakob");
        createUser1.setPassword("234SoSafe");
        createUser1.setPasswordAgain("234SoSafe");

        ResponseEntity<String> responseRegisterUser1 = restTemplate.postForEntity("/api/register", createUser1, String.class);

        assertThat(responseRegisterUser1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertEquals("user successfully created", responseRegisterUser1.getBody());

        // should log in user
        UserLoginData loginUser1 = new UserLoginData();
        loginUser1.setUsername("Jakob");
        loginUser1.setPassword("234SoSafe");

        ResponseEntity<Token> responseLoginUser1 = restTemplate.postForEntity("/api/login", loginUser1, Token.class);

        assertThat(responseLoginUser1.getStatusCode()).isEqualTo(HttpStatus.OK);

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

        // (log in user again)
        UserLoginData loginNewUser1 = new UserLoginData();
        loginNewUser1.setUsername("Jakob");
        loginNewUser1.setPassword("234SoSafe");
        ResponseEntity<Token> loginResponse = restTemplate.postForEntity("/api/login", loginNewUser1, Token.class);

        String token = loginResponse.getBody().getToken();
        HttpHeaders bearerHeader = new HttpHeaders();
        bearerHeader.setBearerAuth(token);

        // should add new expense
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
        expense01.setId(createExpense01.getBody().getId());
        expense01.setUser(createExpense01.getBody().getUser());

        Assertions.assertThat(createExpense01.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(createdExpense).isEqualTo(expense01);

        // create two additional expenses
        Expense expense02 = new Expense();
        expense02.setPurpose("Servietten");
        expense02.setDescription("Lidl");
        expense02.setAmount(2.75);
        expense02.setCurrency(EUR);
        Expense expense03 = new Expense();
        expense03.setPurpose("Gurken");
        expense03.setDescription("Lidl");
        expense03.setAmount(3.50);
        expense03.setCurrency(EUR);

        restTemplate.exchange("/api/expenses",HttpMethod.POST,new HttpEntity<>(expense02, bearerHeader),Expense.class);
        restTemplate.exchange("/api/expenses",HttpMethod.POST,new HttpEntity<>(expense03, bearerHeader),Expense.class);

        // sum should add up to 19.15 EUR
        ResponseEntity<ExpensesDTO> expensesByJakob = restTemplate.exchange(
                "/api/expenses/user",
                HttpMethod.GET,
                new HttpEntity<>(bearerHeader),
                ExpensesDTO.class
        );
        double sum = expensesByJakob.getBody().getSum();
        Assertions.assertThat(sum).isEqualTo(19.15);

        // register second user
        UserCreationData createUser2 = new UserCreationData();
        createUser2.setUsername("Eva");
        createUser2.setPassword("VerySafeToo");
        createUser2.setPasswordAgain("VerySafeToo");
        
        ResponseEntity<String> responseRegisterUser2 = restTemplate.postForEntity("/api/register", createUser2, String.class);

        assertThat(responseRegisterUser2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        
        // log in second user
        UserLoginData loginUser2 = new UserLoginData();
        loginUser2.setUsername("Eva");
        loginUser2.setPassword("VerySafeToo");
        ResponseEntity<Token> responseLoginUser2 = restTemplate.postForEntity("/api/login", loginUser2, Token.class);
        assertThat(responseLoginUser2.getStatusCode()).isEqualTo(HttpStatus.OK);

        // get token
        String tokenUser2 = responseLoginUser2.getBody().getToken();
        HttpHeaders bearerHeaderUser2 = new HttpHeaders();
        bearerHeaderUser2.setBearerAuth(tokenUser2);

        // (create additional expenses)
        Expense expense04 = new Expense();
        expense04.setPurpose("Würstchen");
        expense04.setDescription("Markt");
        expense04.setAmount(15.60);
        expense04.setCurrency(EUR);
        Expense expense05 = new Expense();
        expense05.setPurpose("Bauernbrot");
        expense05.setDescription("Wochenmarkt");
        expense05.setAmount(4.40);
        expense05.setCurrency(EUR);
        Expense expense06 = new Expense();
        expense06.setPurpose("Senf");
        expense06.setDescription("Rewe");
        expense06.setAmount(3.75);
        expense06.setCurrency(EUR);
        Expense expense07 = new Expense();
        expense07.setPurpose("zwei Kästen Bier");
        expense07.setDescription("Rewe");
        expense07.setAmount(28.60);
        expense07.setCurrency(EUR);
        
        restTemplate.exchange("/api/expenses",HttpMethod.POST,new HttpEntity<>(expense04, bearerHeaderUser2),Expense.class);
        restTemplate.exchange("/api/expenses",HttpMethod.POST,new HttpEntity<>(expense05, bearerHeaderUser2),Expense.class);
        restTemplate.exchange("/api/expenses",HttpMethod.POST,new HttpEntity<>(expense06, bearerHeaderUser2),Expense.class);
        ResponseEntity<Expense> createExpense07 = restTemplate.exchange(
                "/api/expenses",
                HttpMethod.POST,
                new HttpEntity<>(expense07, bearerHeaderUser2),
                Expense.class
        );
        String expense07Id = Objects.requireNonNull(createExpense07.getBody()).getId();  // get id for later use


        // check sum adds up to 52.35 EUR
        ResponseEntity<ExpensesDTO> expensesByEva = restTemplate.exchange(
                "/api/expenses/user",
                HttpMethod.GET,
                new HttpEntity<>(bearerHeaderUser2),
                ExpensesDTO.class
        );
        double sumEva = expensesByEva.getBody().getSum();
        Assertions.assertThat(sumEva).isEqualTo(52.35);

        // create yet another expense and retrieve id
        Expense expense08 = new Expense();
        expense08.setPurpose("Salat");
        expense08.setDescription("Rewe");
        expense08.setAmount(2.60);
        expense08.setCurrency(EUR);

        ResponseEntity<Expense> createExpense08 = restTemplate.exchange(
                "/api/expenses",
                HttpMethod.POST,
                new HttpEntity<>(expense08, bearerHeaderUser2),
                Expense.class
        );

        // should change amount
        String expense08Id = createExpense08.getBody().getId();
        expense08.setId(expense08Id);
        expense08.setUser("Eva");
        expense08.setAmount(12.60);

        ResponseEntity<Expense> changeAmountExpense08 = restTemplate.exchange(
                "/api/expenses/" + expense08Id,
                HttpMethod.PUT,
                new HttpEntity<>(expense08, bearerHeaderUser2),
                Expense.class
        );

        Assertions.assertThat(changeAmountExpense08.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(changeAmountExpense08.getBody().getAmount()).isEqualTo(12.6);

        // should delete expense item
        ResponseEntity<String> deleteExpense08 = restTemplate.exchange(
                "/api/expenses/" + expense08Id,
                HttpMethod.DELETE,
                new HttpEntity<>(bearerHeaderUser2),
                String.class
        );

        Assertions.assertThat(deleteExpense08.getStatusCode()).isEqualTo(HttpStatus.OK);

        // get single expense
        ResponseEntity<String> getExpense07 = restTemplate.exchange(
                "/api/expenses/" + expense07Id,
                HttpMethod.GET,
                new HttpEntity<>(bearerHeaderUser2),
                String.class
        );

        Assertions.assertThat(getExpense07.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(getExpense07.getBody().contains("zwei Kästen Bier"));

        // should get all expenses
        ResponseEntity<ExpensesDTO> getAllExpenses = restTemplate.exchange(
                "/api/expenses",
                HttpMethod.GET,
                new HttpEntity<>(bearerHeaderUser2),
                ExpensesDTO.class
        );

        Assertions.assertThat(getAllExpenses.getStatusCode()).isEqualTo(HttpStatus.OK);

        Assertions.assertThat(getAllExpenses.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(getAllExpenses.getBody().getSum()).isEqualTo(71.5);

        // should get list of users
        ResponseEntity<String> getUserList = restTemplate.exchange(
                "/api/expenses/userlist",
                HttpMethod.GET,
                new HttpEntity<>(bearerHeaderUser2),
                String.class
        );

        Assertions.assertThat(getUserList.getBody()).isEqualTo("[\"Jakob\",\"Eva\"]");

        // should retrieve list of transactions
        ResponseEntity<TransactionsDTO[]> getTransactions = restTemplate.exchange(
                "/api/expenses/balance",
                HttpMethod.GET,
                new HttpEntity<>(bearerHeaderUser2),
                TransactionsDTO[].class
        );

        Assertions.assertThat(getTransactions.getBody().length).isEqualTo(1);
        Assertions.assertThat(getTransactions.getBody()[0].getUserFrom()).isEqualTo("Jakob");
        Assertions.assertThat(getTransactions.getBody()[0].getUserTo()).isEqualTo("Eva");
        Assertions.assertThat(getTransactions.getBody()[0].getBalance()).isEqualTo(16.6);
    }
}
