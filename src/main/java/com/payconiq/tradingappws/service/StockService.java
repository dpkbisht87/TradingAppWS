package com.payconiq.tradingappws.service;

import com.payconiq.tradingappws.dao.entity.Stock;
import com.payconiq.tradingappws.dto.model.StockCreateDto;
import com.payconiq.tradingappws.dto.model.StockQueryDto;
import com.payconiq.tradingappws.dto.model.StockUpdateDto;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public interface StockService {
    List<StockQueryDto> getAllStocks();
    
    StockQueryDto getStockById(int id);
    
    // Create a new Stock
    StockQueryDto createStock(StockCreateDto stockCreateDto);
    
    // Update the price of existing Stock
    StockQueryDto updateStockPrice(int id, StockUpdateDto stockUpdateDto);
    
    // Delete the Stock by Id
    StockQueryDto deleteStock(int id);
}
