package de.neuefische.smartcount.Users;

import de.neuefische.smartcount.Users.Exceptions.PasswordsDoNotMatchException;
import de.neuefische.smartcount.Users.Exceptions.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(UserCreationData userCreationData) {
        if (userCreationDataIsValid(userCreationData)) {
            User user = new User(null, userCreationData.getUsername(), passwordEncoder.encode(userCreationData.getPassword()));
            User saved = userRepository.save(user);
            return saved;
        }
        throw new PasswordsDoNotMatchException();
    }

    private boolean userCreationDataIsValid(UserCreationData userCreationData) {
        userRepository.findByUsername(userCreationData.getUsername())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException();
                });
        return Objects.equals(userCreationData.getPassword(), userCreationData.getPasswordAgain());
    }

    public Optional<User> findByUserName(String username) {
        return userRepository.findByUsername(username);
    }
}
