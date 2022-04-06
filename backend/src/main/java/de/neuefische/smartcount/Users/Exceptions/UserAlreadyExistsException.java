package de.neuefische.smartcount.Users.Exceptions;

public class UserAlreadyExistsException extends IllegalStateException {

    public UserAlreadyExistsException() {
        super("user already exists");
    }
}
