package ru.isshepelev.flavorscape.infrastructure.exception;

public class FriendRequestAlreadySentException extends RuntimeException {

    public FriendRequestAlreadySentException(String message) {
        super(message);
    }
}
