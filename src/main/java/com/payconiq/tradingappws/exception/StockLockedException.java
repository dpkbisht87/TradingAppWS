package com.payconiq.tradingappws.exception;

public class StockLockedException extends RuntimeException {
    public StockLockedException() { }
    
    public StockLockedException(String message) {
        super(message);
    }
    
    public StockLockedException(String message, Throwable cause) {
        super(message, cause);
    }
}
