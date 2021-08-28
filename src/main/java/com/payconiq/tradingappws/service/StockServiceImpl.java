package com.payconiq.tradingappws.service;

import com.payconiq.tradingappws.dao.entity.Stock;
import com.payconiq.tradingappws.dao.repository.StockRepository;
import com.payconiq.tradingappws.dto.model.StockCreateDto;
import com.payconiq.tradingappws.dto.model.StockQueryDto;
import com.payconiq.tradingappws.dto.model.StockUpdateDto;
import com.payconiq.tradingappws.exception.DuplicateStockException;
import com.payconiq.tradingappws.exception.StockLockedException;
import com.payconiq.tradingappws.exception.StockNotfoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StockServiceImpl implements StockService{
    
    @Autowired
    private StockRepository stockRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Override
    public Set<StockQueryDto> getAllStocks() {
        return stockRepository.findAll()
                .stream()
                .map(stock -> modelMapper.map(stock, StockQueryDto.class))
                .collect(Collectors.toCollection(TreeSet::new));
    }
    
    @Override
    public StockQueryDto createStock(StockCreateDto stockCreateDto) {
        // Verify that stock with the same name does not exist
        Optional<Stock> duplicateStock = Optional.ofNullable(stockRepository.findByName(stockCreateDto.getName()));
        if (!duplicateStock.isPresent()) {
            int uniqueId = generateUniqueId();
            if (uniqueId == -1){
                return null;
            }
            Stock stockModel = new Stock()
                                       .setId(uniqueId)
                                       .setName(stockCreateDto.getName())
                                       .setCurrentPrice(stockCreateDto.getCurrentPrice())
                                       .setCreationDate(new Date())
                                       .setLocked(true);
            stockRepository.save(stockModel);
            return modelMapper.map(stockModel, StockQueryDto.class);
        }
        String message = "Stock with name : "+ stockCreateDto.getName() +" already exists.";
        throw new DuplicateStockException(message);
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
    public StockQueryDto getStockById(int id) {
        Optional<Stock> stock = stockRepository.findById(id);
        if (stock.isPresent()){
            return modelMapper.map(stock.get(), StockQueryDto.class); 
        }
        String message = "Stock with id : "+ id +" not found";
        throw new StockNotfoundException(message);
    }
    
    @Override
    public StockQueryDto updateStockPrice(int id, StockUpdateDto stockUpdateDto) {
        Optional<Stock> stock = stockRepository.findById(id);
        if (stock.isPresent()) {
            Stock stockModel = stock.get();
            if(!stockModel.isLocked()){
                stockModel.setLastUpdate(new Date());
                stockModel.setLocked(true);
                stockModel.setCurrentPrice(stockUpdateDto.getCurrentPrice());
                stockModel = stockRepository.save(stockModel);
                return modelMapper.map(stockModel, StockQueryDto.class);
            } else {
                String message = "Stock with id : "+ id +" is locked. Please try after sometime.";
                throw new StockLockedException(message);
            }
        } else {
            String message = "Stock with id : "+ id +"  not found";
            throw new StockNotfoundException(message);
        }
    }
    
    @Override
    public StockQueryDto deleteStock(int id) {
        Optional<Stock> stock = stockRepository.findById(id);
        if(stock.isPresent()){
            Stock stockModel = stock.get();
            if(!stockModel.isLocked()){
                stockRepository.deleteById(id);
                return modelMapper.map(stock.get(), StockQueryDto.class);
            }
            String message = "Stock with id : "+ id +" is locked. Please try after sometime.";
            throw new StockLockedException(message);
        }
        String message = "Stock with id : "+ id +"  not found";
        throw new StockNotfoundException(message);
    }
    
}
