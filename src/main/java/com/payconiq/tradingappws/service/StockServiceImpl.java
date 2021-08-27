package com.payconiq.tradingappws.service;

import com.payconiq.tradingappws.dao.entity.Stock;
import com.payconiq.tradingappws.dao.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StockServiceImpl implements StockService{
    
    @Autowired
    private StockRepository stockRepository;
    
    @Override
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }
    
    @Override
    public Stock createStock(Stock stock) {
        int uniqueId = generateUniqueId();
        if (uniqueId == -1){
            return null;
        }
        Stock newStock = new Stock();
        newStock.setId(generateUniqueId());
        newStock.setName(stock.getName());
        newStock.setCurrentPrice(stock.getCurrentPrice());
        newStock.setCreationDate(new Date());
        newStock.setLocked(true);
        return stockRepository.save(newStock);
    }
    
    private int generateUniqueId() {
        int retry = 0;
        Random rand = new Random();
        while (retry < 3) {
            int newId = rand.nextInt(10000000);
            Optional<Stock> stock = stockRepository.findById(newId);
            if (!stock.isPresent()) {
                return newId;
            } else {
                retry++;
            }
        }
        return -1;
    }
    
    @Override
    public Stock getStockById(int id) {
        Optional<Stock> stock = stockRepository.findById(id);
        return stock.orElse(null);
    }
    
    @Override
    public Stock updateStockPrice(int id, Stock stockUpdateDto) {
        Optional<Stock> stock = stockRepository.findById(id);
        if (stock.isPresent()) {
            Stock stk = stock.get();
            stk.setLastUpdate(new Date());
            stk.setLocked(true);
            stk.setCurrentPrice(stockUpdateDto.getCurrentPrice());
            stk = stockRepository.save(stk);
            return stk;
        } else {
            return null;
        }
    }
    
    @Override
    public Stock deleteStock(int id) {
        Optional<Stock> stock = stockRepository.findById(id);
        if(stock.isPresent()){
            stockRepository.deleteById(id);
            return stock.get();
        }
        return null;
    }
}
