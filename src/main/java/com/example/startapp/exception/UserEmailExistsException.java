package com.example.startapp.exception;

public class UserEmailExistsException extends RuntimeException{
    public UserEmailExistsException(String code, String message){
        super(message);
    }

}