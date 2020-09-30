package com.exchange.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CurrencySymbolsException extends RuntimeException {
    public CurrencySymbolsException(String message) {
        super(message);
    }
}
