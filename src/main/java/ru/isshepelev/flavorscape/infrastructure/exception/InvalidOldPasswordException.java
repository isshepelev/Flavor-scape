package ru.isshepelev.flavorscape.infrastructure.exception;

public class InvalidOldPasswordException extends RuntimeException {

    public InvalidOldPasswordException(String message) {
        super(message);
    }
}
