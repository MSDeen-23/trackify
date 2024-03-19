package com.beworkerbee.leadsservice.exception;

public class ForbiddenException extends RuntimeException{
    public ForbiddenException(){
        super("Un authorized to perform this operation");
    }
}
