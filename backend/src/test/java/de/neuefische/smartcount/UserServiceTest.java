package de.neuefische.smartcount;

import de.neuefische.smartcount.Exceptions.PasswordsDoNotMatchException;
import de.neuefische.smartcount.Exceptions.UserAlreadyExistsException;
import de.neuefische.smartcount.Users.User;
import de.neuefische.smartcount.Users.UserCreationData;
import de.neuefische.smartcount.Users.UserRepository;
import de.neuefische.smartcount.Users.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class UserServiceTest {

//     I don't succeed making this first test run. Don't know why...

    @Test
    void createUser() {
        // given
        UserCreationData newUserCreationData = new UserCreationData(null, "Franz", "franz234", "franz234");
        User user = new User(null, "Franz", "myAbsolutelySecureHash");
        User savedUser = new User("3322", "Franz", "myAbsolutelySecureHash");

        UserRepository repo = mock(UserRepository.class);
        when(repo.save(user)).thenReturn(savedUser);

        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        when(passwordEncoder.encode(newUserCreationData.getPassword())).thenReturn("myAbsolutelySecureHash");

        // when
        UserService userService = new UserService(repo, passwordEncoder);
        User actual = userService.createUser(newUserCreationData);

        // then
        assertThat(actual).isEqualTo(savedUser);
    }

    @Test
    void userNotCreated_PasswordsDoNotMatch() {
        // given
        UserCreationData newUser = new UserCreationData();
        newUser.setUsername("Hannah");
        newUser.setPassword("PasswortNr1");
        newUser.setPasswordAgain("SchonVergessen!?");

        UserRepository repo = Mockito.mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        // when
        UserService userService = new UserService(repo, passwordEncoder);

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

        UserRepository repo = Mockito.mock(UserRepository.class);
        when(repo.findByUsername("Bernd")).thenReturn(Optional.of(existingUser));

        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        when(passwordEncoder.encode("222333")).thenReturn("MySuperDuperPassword");

        // when
        UserService userService = new UserService(repo, passwordEncoder);

        // then
        assertThatExceptionOfType(UserAlreadyExistsException.class)
                .isThrownBy(() -> userService.createUser(newUser))
                .withMessage("user already exists");
    }

}