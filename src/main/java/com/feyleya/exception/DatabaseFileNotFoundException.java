package com.feyleya.exception;

public class DatabaseFileNotFoundException extends RuntimeException{
    public DatabaseFileNotFoundException(String message) {
        super(message);
    }
}
