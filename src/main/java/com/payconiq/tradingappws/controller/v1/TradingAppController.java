package com.payconiq.tradingappws.controller.v1;

import com.payconiq.tradingappws.controller.v1.request.CreateStockRequest;
import com.payconiq.tradingappws.controller.v1.request.UpdateStockRequest;
import com.payconiq.tradingappws.dto.model.StockCreateDto;
import com.payconiq.tradingappws.dto.model.StockQueryDto;
import com.payconiq.tradingappws.dto.model.StockUpdateDto;
import com.payconiq.tradingappws.service.StockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")
@Api(tags = {"TradingAppController"})
public class TradingAppController {
    
    @Autowired
    private StockService stockService;
    
    @ApiOperation(value = "View a list of all available stocks", response = Iterable.class)
    @GetMapping("/stocks")
    public ResponseEntity<List<StockQueryDto>>  getAllStocks(){
        final List<StockQueryDto> stockList = new ArrayList<>();
        Iterable<StockQueryDto> iterable = stockService.getAllStocks();
        iterable.forEach(stockList::add);
        return !stockList.isEmpty() ? new ResponseEntity<>(stockList, HttpStatus.OK)
                       : ResponseEntity.notFound().build();
    }
    
    @ApiOperation(value = "Create a new Stock", response = StockQueryDto.class)
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
    
    @ApiOperation(value = "Get details of a single stock", response = StockQueryDto.class)
    @GetMapping("/stocks/{id}")
    public ResponseEntity<StockQueryDto> getStockById(@PathVariable("id")  @Digits(integer=5, fraction=0) @DecimalMin(value = "0", inclusive = false) int id){
        StockQueryDto stockQueryDto = stockService.getStockById(id);
        if (stockQueryDto != null) {
            return ResponseEntity.ok().body(stockQueryDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @ApiOperation(value = "Update price of a single stock", response = StockQueryDto.class)
    @PutMapping("/stocks/{id}")
    public ResponseEntity<StockQueryDto> updateStockPrice(@PathVariable int id, 
                                                          @Valid @RequestBody UpdateStockRequest updateStockRequest){
        StockUpdateDto stockUpdateDto = new StockUpdateDto()
                                                .setCurrentPrice(updateStockRequest.getCurrentPrice());
        StockQueryDto stockQueryDto =  stockService.updateStockPrice(id, stockUpdateDto);
        if (stockQueryDto != null) {
            return ResponseEntity.ok().body(stockQueryDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @ApiOperation(value = "Delete stock with Id", response = StockQueryDto.class)
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
