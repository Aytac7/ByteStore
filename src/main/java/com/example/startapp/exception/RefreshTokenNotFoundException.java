package com.example.startapp.exception;

public class RefreshTokenNotFoundException extends RuntimeException{
    public RefreshTokenNotFoundException(String code, String message){
        super(message);
    }

}