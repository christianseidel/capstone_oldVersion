package de.neuefische.smartcount.Exceptions;

public class PasswordsDoNotMatchException extends IllegalStateException {

    public PasswordsDoNotMatchException() {
        super("passwords do not match");
    }
}
