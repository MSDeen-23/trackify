package com.beworkerbee.leadsservice.exception.handler;

import com.beworkerbee.leadsservice.exception.ErrorResponse;
import com.beworkerbee.leadsservice.exception.ForbiddenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExistsException(ForbiddenException e){
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.FORBIDDEN,
                        e.getMessage()
                        ),HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e){
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.UNAUTHORIZED,
                        "Incorrect password"
                ),HttpStatus.UNAUTHORIZED);

    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException e){
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.BAD_REQUEST,
                        "Invalid username or password"
                ),HttpStatus.BAD_REQUEST);

    }

}
