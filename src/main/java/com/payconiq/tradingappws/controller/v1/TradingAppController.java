package com.payconiq.tradingappws.controller.v1;

import com.payconiq.tradingappws.dao.entity.Stock;
import com.payconiq.tradingappws.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TradingAppController {
    
    @Autowired
    private StockService stockService;
    
    @GetMapping("/stocks")
    public ResponseEntity<List<Stock>>  getAllStocks(){
        final List<Stock> stockList = new ArrayList<>();
        Iterable<Stock> iterable = stockService.getAllStocks();
        iterable.forEach(stockList::add);
        return !stockList.isEmpty() ? new ResponseEntity<>(stockList, HttpStatus.OK)
                       : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/stocks")
    public ResponseEntity<Stock> createStock(@Valid @RequestBody Stock stock){
        Stock createdStock = stockService.createStock(stock);
        if (createdStock != null) {
            return ResponseEntity.ok().body(createdStock);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/stocks/{id}")
    public ResponseEntity<Stock> getStockById(@PathVariable int id){
        Stock stock = stockService.getStockById(id);
        if (stock != null) {
            return ResponseEntity.ok().body(stock);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/stocks/{id}")
    public ResponseEntity<Stock> updateStockPrice(@PathVariable int id, @Valid @RequestBody Stock stock){
        Stock updatedStock = stockService.updateStockPrice(id, stock);
        if (updatedStock != null) {
            return ResponseEntity.ok().body(updatedStock);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/stocks/{id}")
    public ResponseEntity<Stock> deleteStockById(@PathVariable int id){
        Stock deletedStock = stockService.deleteStock(id);
        if (deletedStock != null) {
            return ResponseEntity.ok().body(deletedStock);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
