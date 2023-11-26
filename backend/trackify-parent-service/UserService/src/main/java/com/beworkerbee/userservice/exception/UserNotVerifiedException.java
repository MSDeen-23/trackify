package com.beworkerbee.userservice.exception;

public class UserNotVerifiedException extends RuntimeException{
    public UserNotVerifiedException(String username){
        super(username+" is not verified");
    }
}
