package com.beworkerbee.leadsservice.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;


@Data
public class ErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timestamp;

    private int code;

    private String status;

    private String message;

    private StackTraceElement[] stackTrace;

    private Object data;

    public ErrorResponse() {
        timestamp = new Date();
    }

    public ErrorResponse(HttpStatus httpStatus, String message) {
        this();
        this.code = httpStatus.value();
        this.status = httpStatus.name();
        this.message = message;
    }

    public ErrorResponse(HttpStatus httpStatus, String message, StackTraceElement[] stackTrace) {
        this(httpStatus, message);
        this.stackTrace = stackTrace;
    }

    public ErrorResponse(HttpStatus httpStatus, String message, StackTraceElement[] stackTrace, Object data) {
        this(httpStatus,message, stackTrace);
        this.data = data;
    }
}
