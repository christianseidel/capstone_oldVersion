package de.neuefische.smartcount.Users;

import de.neuefische.smartcount.Users.Exceptions.PasswordsDoNotMatchException;
import de.neuefische.smartcount.Users.Exceptions.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserCreationData userCreationData) {
        try {
            userService.createUser(userCreationData);
            return ResponseEntity.status(201).body("user was created");
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (PasswordsDoNotMatchException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
