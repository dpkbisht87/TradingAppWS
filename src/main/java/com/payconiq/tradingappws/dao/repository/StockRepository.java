package com.payconiq.tradingappws.dao.repository;

import com.payconiq.tradingappws.dao.entity.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends MongoRepository<Stock, Integer> {
    Stock findByName(String name);
}
