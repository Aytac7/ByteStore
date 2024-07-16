package com.example.startapp.exception;

public class PasswordInvalidException extends RuntimeException{
    public PasswordInvalidException(String code, String message){
        super(message);
    }

}