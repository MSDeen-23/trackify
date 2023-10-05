package com.beworkerbee.userservice.exception;

public class UserAlreadyExistsException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public UserAlreadyExistsException() {
        super("User already exists");
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
