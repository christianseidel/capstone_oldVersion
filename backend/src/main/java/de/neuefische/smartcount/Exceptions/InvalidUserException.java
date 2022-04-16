package de.neuefische.smartcount.Exceptions;

public class InvalidUserException extends IllegalStateException {

    public InvalidUserException() {
        super("You don't have access to this item");
    }
}
