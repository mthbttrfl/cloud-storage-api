package org.example.cloudstorageapi.exception;

public class UsernameAlreadyExists extends RuntimeException {
    public UsernameAlreadyExists(String message, Throwable throwable) {
        super(message, throwable);
    }
}
