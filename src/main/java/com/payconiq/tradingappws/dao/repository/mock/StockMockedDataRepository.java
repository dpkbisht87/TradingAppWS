package com.payconiq.tradingappws.dao.repository.mock;

import com.payconiq.tradingappws.dao.entity.Stock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockMockedDataRepository {
    List<Stock> findAll();
    
    Stock findByName(String name);
    
    Stock save(Stock stock);
    
    Optional<Stock> findById(int id);
    
    void deleteById(int id);
}
