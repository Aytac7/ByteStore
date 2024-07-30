package com.example.startapp.exception;

public class DuplicatePhoneNumberException extends RuntimeException {

    public DuplicatePhoneNumberException(String message){
        super(message);
    }
}
