package de.neuefische.smartcount.Users.Exceptions;

public class PasswordsDoNotMatchException extends IllegalStateException {

    public PasswordsDoNotMatchException() {
        super("passwords do not match");
    }
}
