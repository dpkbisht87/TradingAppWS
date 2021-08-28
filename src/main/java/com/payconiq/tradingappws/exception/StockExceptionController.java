package com.payconiq.tradingappws.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class StockExceptionController {
    @ExceptionHandler(value = StockNotfoundException.class)
    public ResponseEntity<Object> exception(StockNotfoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(value=StockLockedException.class)
    public ResponseEntity<Object> exception(StockLockedException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.LOCKED);
    }
    
    @ExceptionHandler(value=DuplicateStockException.class)
    public ResponseEntity<Object> exception(DuplicateStockException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
