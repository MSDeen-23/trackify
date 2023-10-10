package com.beworkerbee.userservice.exception.handler;

import com.beworkerbee.userservice.exception.AlreadyExistsException;
import com.beworkerbee.userservice.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExistsException(AlreadyExistsException e){
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
