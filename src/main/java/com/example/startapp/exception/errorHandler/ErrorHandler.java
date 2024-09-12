package com.example.startapp.exception.errorHandler;

import com.example.startapp.exception.*;

import com.example.startapp.exception.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.HashMap;
import java.util.Map;

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


    @ExceptionHandler(CustomLockedException.class)
    @ResponseStatus(HttpStatus.LOCKED)
    public ErrorResponse handleLockedException(CustomLockedException exception) {
        log.error("handleLockedException {}", exception.getMessage());

        return ErrorResponse.builder()
                .code(HttpStatus.LOCKED.name())
                .message(exception.getMessage())
                .build();
    }
    @ExceptionHandler(RefreshTokenNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerRefreshTokenNotFoundException(Exception exception) {
        log.error("handlerRefreshTokenNotFoundException {}", exception.getMessage());

        return ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.name())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handlerRefreshTokenExpiredException(Exception exception) {
        log.error("handlerRefreshTokenExpiredException {}", exception.getMessage());

        return ErrorResponse.builder()
                .code(HttpStatus.UNAUTHORIZED.name())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(InvalidOtpException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerInvalidOtpException(Exception exception) {
        log.error("handlerInvalidOtpException {}", exception.getMessage());

        return ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.name())
                .message(exception.getMessage())
                .build();
    }



    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerCategoryNotFoundException(Exception exception) {
        log.error("handlerCategoryNotFoundException{}", exception.getMessage());

        return ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.name())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(ModelNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerModelNotFoundException(Exception exception) {
        log.error("handlerModelNotFoundException{}", exception.getMessage());

        return ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.name())
                .message(exception.getMessage())
                .build();
    }
    @ExceptionHandler(BrandNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerBrandNotFoundException(Exception exception) {
        log.error("handlerBrandNotFoundException{}", exception.getMessage());

        return ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.name())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(SubcategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerSubcategoryNotFoundException(Exception exception) {
        log.error("handlerSubcategoryNotFoundException{}", exception.getMessage());

        return ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.name())
                .message(exception.getMessage())
                .build();
    }




//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach(error -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return errors;
//    }
}