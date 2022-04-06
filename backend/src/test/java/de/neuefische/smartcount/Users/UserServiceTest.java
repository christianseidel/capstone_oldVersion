package de.neuefische.smartcount.Users;

import de.neuefische.smartcount.Users.Exceptions.PasswordsDoNotMatchException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Test
    void createUser() {
        // given
        UserCreationData newUser = new UserCreationData();
        newUser.setUsername("Franz");
        newUser.setPassword("franz234");
        newUser.setPasswordAgain("franz234");
        User user = new User(null, "Franz", "franz234");
        User savedUser = new User("3333", "Franz", "franz234");

        UserRepository repo = Mockito.mock(UserRepository.class);
        when(repo.save(user)).thenReturn(savedUser);

        // when
        UserService userService = new UserService(repo);
        User actual = userService.createUser(newUser);

        // then
        assertThat(actual).isSameAs(savedUser);
    }

    @Test
    void userNotCreated_PasswordsDoNotMatch() {
        // given
        UserCreationData newUser = new UserCreationData();
        newUser.setUsername("Hannah");
        newUser.setPassword("PasswortNr1");
        newUser.setPasswordAgain("SchonVergessen!?");

        UserRepository repo = Mockito.mock(UserRepository.class);

        // when
        UserService userService = new UserService(repo);

        // then
        assertThatExceptionOfType(PasswordsDoNotMatchException.class)
                .isThrownBy(()-> userService.createUser(newUser))
                .withMessage("passwords do not match");
    }

    @Test
    void userNotCreated_UserAlreadyExists() {
        // given
        UserCreationData newUser = new UserCreationData();
        newUser.setUsername("Bernd");
        newUser.setPassword("myPasswordNo1");
        newUser.setPasswordAgain("myPasswordNo1");
        User existingUser = new User("222333", "Bernd", "myPasswordNo1");

       when


    }

}