package de.neuefische.smartcount.Exceptions;

public class UserAlreadyExistsException extends IllegalStateException {

    public UserAlreadyExistsException() {
        super("user already exists");
    }
}
