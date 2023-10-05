package com.beworkerbee.userservice.exception.handler;

import com.beworkerbee.userservice.exception.ErrorResponse;
import com.beworkerbee.userservice.exception.OrganizationAlreadyPresentException;
import com.beworkerbee.userservice.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(OrganizationAlreadyPresentException.class)
    public ResponseEntity<ErrorResponse> handleOrganizationAlreadyPresentException(OrganizationAlreadyPresentException e){
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.CONFLICT,
                        e.getMessage()
                        ),HttpStatus.CONFLICT);

    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e){
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.CONFLICT,
                        e.getMessage()
                ),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e){
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                        e.getMessage(),e.getStackTrace()
                ),HttpStatus.CONFLICT);

    }

}
