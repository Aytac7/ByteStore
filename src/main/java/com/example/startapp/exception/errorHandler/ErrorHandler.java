package com.example.startapp.exception.errorHandler;

import com.example.startapp.exception.PasswordInvalidException;
import com.example.startapp.exception.UserEmailExistsException;
import com.example.startapp.exception.UserNotFoundException;

import com.example.startapp.exception.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerUserNotFoundException(Exception exception) {
        log.error("handlerUserNotFoundException {}", exception.getMessage());

        return ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.name())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(PasswordInvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerPasswordInvalidException(Exception exception) {
        log.error("handlerPasswordInvalidException {}", exception.getMessage());

        return ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.name())
                .message(exception.getMessage())
                .build();
    }


    @ExceptionHandler(UserEmailExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerUserEmailExistsException(Exception exception) {
        log.error("handlerUserEmailExistsException {}", exception.getMessage());

        return ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.name())
                .message(exception.getMessage())
                .build();
    }

}