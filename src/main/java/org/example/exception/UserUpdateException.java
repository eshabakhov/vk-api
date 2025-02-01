package org.example.exception;

public class UserUpdateException extends RuntimeException {
    public UserUpdateException(String message) {
        super(message);
    }
}
