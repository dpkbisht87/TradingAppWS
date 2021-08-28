package com.payconiq.tradingappws.exception;

public class CreateStockFailedException extends RuntimeException {
    public CreateStockFailedException() { }
    
    public CreateStockFailedException(String message) {
        super(message);
    }
    
    public CreateStockFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
