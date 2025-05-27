package ru.isshepelev.flavorscape.infrastructure.exception;

public class UserBlockedException extends RuntimeException {

    public UserBlockedException(String message) {
        super(message);
    }
}
