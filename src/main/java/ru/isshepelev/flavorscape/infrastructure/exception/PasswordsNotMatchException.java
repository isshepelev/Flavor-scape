package ru.isshepelev.flavorscape.infrastructure.exception;

public class PasswordsNotMatchException extends RuntimeException {

    public PasswordsNotMatchException(String message) {
        super(message);
    }
}
