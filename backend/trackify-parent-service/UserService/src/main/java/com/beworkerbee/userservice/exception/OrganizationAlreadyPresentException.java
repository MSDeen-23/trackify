package com.beworkerbee.userservice.exception;


public class OrganizationAlreadyPresentException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public OrganizationAlreadyPresentException() {
        super("Organization name is already in use");
    }

    public OrganizationAlreadyPresentException(String message) {
        super(message);
    }
}
