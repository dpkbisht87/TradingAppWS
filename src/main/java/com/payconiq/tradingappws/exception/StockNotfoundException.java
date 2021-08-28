package com.payconiq.tradingappws.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class StockNotfoundException extends RuntimeException{
   
    public StockNotfoundException(String message) {
        super(message);
    }
}
