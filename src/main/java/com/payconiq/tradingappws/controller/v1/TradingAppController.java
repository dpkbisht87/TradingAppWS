package com.payconiq.tradingappws.controller.v1;

import com.payconiq.tradingappws.controller.v1.request.CreateStockRequest;
import com.payconiq.tradingappws.controller.v1.request.UpdateStockRequest;
import com.payconiq.tradingappws.dto.model.StockCreateDto;
import com.payconiq.tradingappws.dto.model.StockQueryDto;
import com.payconiq.tradingappws.dto.model.StockUpdateDto;
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
    public ResponseEntity<List<StockQueryDto>>  getAllStocks(){
        final List<StockQueryDto> stockList = new ArrayList<>();
        Iterable<StockQueryDto> iterable = stockService.getAllStocks();
        iterable.forEach(stockList::add);
        return !stockList.isEmpty() ? new ResponseEntity<>(stockList, HttpStatus.OK)
                       : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/stocks")
    public ResponseEntity<StockQueryDto> createStock(@Valid @RequestBody CreateStockRequest createStockRequest){
        StockQueryDto stockQueryDto = createNewStock(createStockRequest);
        if (stockQueryDto != null) {
            return ResponseEntity.ok().body(stockQueryDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    private StockQueryDto createNewStock(CreateStockRequest createStockRequest) {
        StockCreateDto stockCreateDto = new StockCreateDto()
                .setName(createStockRequest.getName())
                .setCurrentPrice(createStockRequest.getCurrentPrice());
        return stockService.createStock(stockCreateDto);
    }
    
    @GetMapping("/stocks/{id}")
    public ResponseEntity<StockQueryDto> getStockById(@PathVariable int id){
        StockQueryDto stockQueryDto = stockService.getStockById(id);
        if (stockQueryDto != null) {
            return ResponseEntity.ok().body(stockQueryDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/stocks/{id}")
    public ResponseEntity<StockQueryDto> updateStockPrice(@PathVariable int id, 
                                                          @Valid @RequestBody UpdateStockRequest updateStockRequest){
        StockUpdateDto stockUpdateDto = new StockUpdateDto()
                                                .setCurrentPrice(updateStockRequest.getPrice());
        StockQueryDto stockQueryDto =  stockService.updateStockPrice(id, stockUpdateDto);
        if (stockQueryDto != null) {
            return ResponseEntity.ok().body(stockQueryDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/stocks/{id}")
    public ResponseEntity<StockQueryDto> deleteStockById(@PathVariable int id){
        StockQueryDto stockQueryDto = stockService.deleteStock(id);
        if (stockQueryDto != null) {
            return ResponseEntity.ok().body(stockQueryDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
