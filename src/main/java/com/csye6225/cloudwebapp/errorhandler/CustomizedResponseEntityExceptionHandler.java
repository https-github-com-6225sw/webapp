package com.csye6225.cloudwebapp.errorhandler;

import jakarta.servlet.ServletException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ServletException {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public final ResponseEntity<Object> handleMethodNotAllowedException(Exception ex, WebRequest request){
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Cache-Control", "no-cache, no-store, must-revalidate");
        responseHeaders.set("Pragma", "no-cache");
        responseHeaders.set("X-Content-Type-Options", "nosniff");
        return new ResponseEntity(responseHeaders, HttpStatus.METHOD_NOT_ALLOWED);
    }
}
