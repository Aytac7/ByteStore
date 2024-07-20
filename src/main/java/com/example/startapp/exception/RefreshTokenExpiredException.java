package com.example.startapp.exception;

public class RefreshTokenExpiredException extends RuntimeException{
    public RefreshTokenExpiredException(String code, String message){
        super(message);
    }

}