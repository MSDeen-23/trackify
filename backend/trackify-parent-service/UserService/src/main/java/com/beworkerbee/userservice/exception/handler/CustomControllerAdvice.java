package com.beworkerbee.userservice.exception.handler;

import com.beworkerbee.userservice.exception.AlreadyExistsException;
import com.beworkerbee.userservice.exception.ErrorResponse;
import com.beworkerbee.userservice.exception.OtpVerificationException;
import com.beworkerbee.userservice.exception.UserNotVerifiedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    @ExceptionHandler(UserNotVerifiedException.class)
    public ResponseEntity<ErrorResponse> handleUserNotVerifiedException(UserNotVerifiedException e){
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS,
                        e.getMessage(),e.getStackTrace()
                ),HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);

    }

    @ExceptionHandler(OtpVerificationException.class)
    public ResponseEntity<ErrorResponse> handleOtpVerificationException(OtpVerificationException e){
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.EXPECTATION_FAILED,
                        e.getMessage(),e.getStackTrace()
                ),HttpStatus.EXPECTATION_FAILED);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e){
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                        e.getMessage(),e.getStackTrace()
                ),HttpStatus.INTERNAL_SERVER_ERROR);

    }



}
