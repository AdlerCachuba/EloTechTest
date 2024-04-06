package com.elotech.elotechapi.exceptionhandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;

@RestControllerAdvice
public class PersonControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException exception) {
        StringBuilder errors = new StringBuilder();
        exception.getBindingResult().getFieldErrors().forEach(
                error -> errors.append(error.getDefaultMessage()).append(". ")
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.toString());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraint(ConstraintViolationException exception) {
        StringBuilder errors = new StringBuilder();
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        violations.forEach(
                error -> errors.append(error.getMessage()).append(". ")
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.toString());
    }

}
