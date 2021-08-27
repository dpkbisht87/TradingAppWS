package com.payconiq.tradingappws.service;

import com.payconiq.tradingappws.dao.entity.Stock;

import java.util.Iterator;
import java.util.List;

public interface StockService {
    List<Stock> getAllStocks();
    
    Stock getStockById(int id);
    
    // Create a new Stock
    Stock createStock(Stock stock);
    
    // Update the price of existing Stock
    Stock updateStockPrice(int id, Stock stock);
    
    // Delete the Stock by Id
    Stock deleteStock(int id);
}
