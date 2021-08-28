package com.payconiq.tradingappws.service;

import com.payconiq.tradingappws.dao.entity.Stock;
import com.payconiq.tradingappws.dao.repository.StockRepository;
import com.payconiq.tradingappws.dto.model.StockCreateDto;
import com.payconiq.tradingappws.dto.model.StockQueryDto;
import com.payconiq.tradingappws.dto.model.StockUpdateDto;
import com.payconiq.tradingappws.exception.CreateStockFailedException;
import com.payconiq.tradingappws.exception.DuplicateStockException;
import com.payconiq.tradingappws.exception.StockLockedException;
import com.payconiq.tradingappws.exception.StockNotfoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@EnableAsync
public class StockServiceImpl implements StockService{
    
    private StockRepository stockRepository;
    
    
    private ModelMapper modelMapper;
    
    @Value("${stock.lock.timeout}")
    private String lockTimeout;
    
    public StockServiceImpl(StockRepository stockRepository, ModelMapper modelMapper, String lockTimeout) {
        this.stockRepository = stockRepository;
        this.modelMapper = modelMapper;
        this.lockTimeout = lockTimeout;
    }
    
    @Override
    public List<StockQueryDto> getAllStocks() {
        List<StockQueryDto> listOfStocks = stockRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, StockQueryDto.class))
                .collect(Collectors.toList());
        if (!listOfStocks.isEmpty()){
            return listOfStocks;
        }
        throw new StockNotfoundException("No stocks found.");
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
            try {
                CompletableFuture <Stock> stock = createStockAsync(stockModel);
                preventAbusivePriceUpdate(uniqueId);
                CompletableFuture.anyOf(stock).join();
                return modelMapper.map(stock.get(), StockQueryDto.class);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                String message = "Stock with name : "+ stockCreateDto.getName() +" creation failed.";
                throw new CreateStockFailedException(message);
            }
        }
        String message = "Stock with name : "+ stockCreateDto.getName() +" already exists.";
        throw new DuplicateStockException(message);
    }
    
    @Async
    public CompletableFuture<Stock> createStockAsync(Stock stockModel){
        return CompletableFuture.completedFuture(stockRepository.save(stockModel));
    }
    
    @Async
    public void preventAbusivePriceUpdate(int id){
        new Timer().schedule(
            new TimerTask() {
                @Override
                public void run() {
                    Optional<Stock> stock = stockRepository.findById(id);
                    if (stock.isPresent()){
                        Stock stockModel = stock.get();
                        stockModel.setLocked(false);
                        stockRepository.save(stockModel);
                    } else {
                        String message = "Stock with id : "+ id +" not found";
                        throw new StockNotfoundException(message);
                    }
                }
            },
            Long.parseLong(lockTimeout)
        );
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
                
                CompletableFuture <Stock> updatedStock = updateStockPriceAsync(stockModel);
                preventAbusivePriceUpdate(stockModel.getId());
                
                CompletableFuture.anyOf(updatedStock).join();
                
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
    
    @Async
    public CompletableFuture<Stock> updateStockPriceAsync(Stock stockModel){
        return CompletableFuture.completedFuture(stockRepository.save(stockModel));
    }
    
    @Override
    public StockQueryDto deleteStock(int id) {
        Optional<Stock> stock = stockRepository.findById(id);
        if(stock.isPresent()){
            Stock stockModel = stock.get();
            if(!stockModel.isLocked()){
                stockRepository.deleteById(id);
                StockQueryDto deletedStock = modelMapper.map(stock.get(), StockQueryDto.class);
                return deletedStock;
            }
            String message = "Stock with id : "+ id +" is locked. Please try after sometime.";
            throw new StockLockedException(message);
        }
        String message = "Stock with id : "+ id +"  not found";
        throw new StockNotfoundException(message);
    }
    
}
