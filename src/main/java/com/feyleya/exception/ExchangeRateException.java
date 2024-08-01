package com.feyleya.exception;

public class ExchangeRateException extends RuntimeException{
    public ExchangeRateException(String message) {
        super(message);
    }
}
