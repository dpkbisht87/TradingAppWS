package com.payconiq.tradingappws.dao.repository.mock;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payconiq.tradingappws.dao.entity.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.List;
import java.util.Optional;

@Component
public class StockMockedDataRepositoryImpl implements StockMockedDataRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockMockedDataRepositoryImpl.class);
    
    @Autowired
    ResourceLoader resourceLoader;
    
    private static List<Stock> stockList;
    
    @PostConstruct
    public void init() {
        try {
            Resource resource = resourceLoader.getResource("classpath:data/mockdata.json");
            File jsonFile = resource.getFile();
            final ObjectMapper objectMapper = new ObjectMapper();
            stockList = objectMapper.readValue(jsonFile, new TypeReference<List<Stock>>(){});
            System.out.println(stockList.size());
        } catch (IOException | NullPointerException e) {
            LOGGER.error("Mock stock data could not be initialised. ", e);
        }
    }
    
    @Override
    public List<Stock> findAll() {
        return stockList;
    }
    
    @Override
    public Stock findByName(String name) {
        for(Stock stock: stockList){
            if(stock.getName().equals(name)){
                return stock;
            }
        }
        return null;
    }
    
    @Override
    public Stock save(Stock stock) {
        int index = - 1;
        for(Stock existingStock: stockList){
            if(existingStock.getName().equals(stock.getName())){
                index = stockList.indexOf(stock);
            }
        }
        if(index > -1) {
            stockList.set(index, stock);
        } else {
            stockList.add(stock);
        }
        return stock;
    }
    
    @Override
    public Optional<Stock> findById(int id) {
        Stock foundStock = null;
        for(Stock stock: stockList){
            if(stock.getId() == id){
                foundStock = stock;
            }
        }
        return Optional.ofNullable(foundStock);
    }
    
    @Override
    public void deleteById(int id) {
        int index = - 1;
        for(Stock stock: stockList){
            if(stock.getId() == id){
                index = stockList.indexOf(stock);
                break;
            }
        }
        if(index > -1) {
            stockList.remove(index);
        }
    }
}
