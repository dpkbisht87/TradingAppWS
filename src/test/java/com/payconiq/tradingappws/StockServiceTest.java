package com.payconiq.tradingappws;

import com.payconiq.tradingappws.dao.entity.Stock;
import com.payconiq.tradingappws.dao.repository.mock.StockMockedDataRepository;
import com.payconiq.tradingappws.dto.model.StockCreateDto;
import com.payconiq.tradingappws.dto.model.StockQueryDto;
import com.payconiq.tradingappws.dto.model.StockUpdateDto;
import com.payconiq.tradingappws.service.StockServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {
    
    @Mock
//    private StockRepository stockRepository;
    private StockMockedDataRepository stockRepository;
    
    @InjectMocks
    StockServiceImpl stockService;
    
    @BeforeEach
    void initUseCase() {
        String lockTimeout = "3000";
        ModelMapper modelMapper = new ModelMapper();
        stockService = new StockServiceImpl(stockRepository, modelMapper , lockTimeout);
    }
    
    @Test
    public void getAllStocksSuccess() {
        Stock stock = new Stock();
        stock.setId(1);
        stock.setName("Amazon");
        stock.setCurrentPrice(new BigDecimal("5000"));
        stock.setCreationDate(new Date());
    
        List<Stock> stockList = new ArrayList<>();
        stockList.add(stock);
    
        when(stockRepository.findAll()).thenReturn(stockList);
    
        List<StockQueryDto> fetchedStocks = stockService.getAllStocks();
        Assertions.assertEquals(fetchedStocks.size(), 1);
    }
    
    @Test
    public void saveStockSuccess() {
        Stock stock = new Stock();
        stock.setId(1);
        stock.setName("Amazon");
        stock.setCurrentPrice(new BigDecimal("5000"));
        stock.setCreationDate(new Date());
        
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);
        when(stockRepository.findByName(any(String.class))).thenReturn(null);
        
        StockCreateDto stockCreateDto = new StockCreateDto()
                                                .setId(stock.getId())
                                                .setName("Amazon1")
                                                .setCurrentPrice(stock.getCurrentPrice());
        StockQueryDto savedStock = stockService.createStock(stockCreateDto);
        Assertions.assertEquals(savedStock.getName(), "Amazon");
    }
    
    @Test
    public void searchStockSuccess() {
        Stock stock = new Stock();
        stock.setId(1);
        stock.setName("Amazon");
        stock.setCurrentPrice(new BigDecimal("5000"));
        stock.setCreationDate(new Date());
    
        when(stockRepository.findById(any(Integer.class))).thenReturn(Optional.of(stock));
        
        StockQueryDto fetchedStock = stockService.getStockById(stock.getId());
        Assertions.assertNotNull(fetchedStock);
    }
    
    @Test
    public void updateStockPriceSuccess(){
        Stock stock = new Stock();
        stock.setId(1);
        stock.setName("Amazon");
        stock.setCurrentPrice(new BigDecimal("5000"));
        stock.setCreationDate(new Date());
    
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);
        when(stockRepository.findById(any(Integer.class))).thenReturn(Optional.of(stock));
    
        StockQueryDto updatedStock = stockService.updateStockPrice(1, new StockUpdateDto().setCurrentPrice(new BigDecimal("5000")));
        Assertions.assertTrue(updatedStock.isLocked());
    }
    
    @Test
    public void deleteStockSuccess(){
        Stock stock = new Stock();
        stock.setId(1);
        stock.setName("Amazon");
        stock.setCurrentPrice(new BigDecimal("5000"));
        stock.setCreationDate(new Date());
    
        when(stockRepository.findById(any(Integer.class))).thenReturn(Optional.of(stock));
        
        StockQueryDto deletedStock = stockService.deleteStock(1);
        Assertions.assertNotNull(deletedStock);
    }
}
