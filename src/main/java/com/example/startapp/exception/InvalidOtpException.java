package com.example.startapp.exception;

public class InvalidOtpException extends RuntimeException {
    public InvalidOtpException(String code, String message) {
        super(message);
    }


}
