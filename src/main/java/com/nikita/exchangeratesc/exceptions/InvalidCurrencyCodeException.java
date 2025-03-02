package com.nikita.exchangeratesc.exceptions;

public class InvalidCurrencyCodeException extends IllegalArgumentException{

    public InvalidCurrencyCodeException(String message) {
        super(message);
    }

}
