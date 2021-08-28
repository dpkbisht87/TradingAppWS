package com.payconiq.tradingappws.exception;

public class DuplicateStockException extends RuntimeException {
    
    public DuplicateStockException() { }
    
    public DuplicateStockException(String message) {
        super(message);
    }
    
    public DuplicateStockException(String message, Throwable cause) {
        super(message, cause);
    }
}
