package com.payconiq.tradingappws.dto.mapper;

import com.payconiq.tradingappws.dao.entity.Stock;
import com.payconiq.tradingappws.dto.model.StockQueryDto;
import org.springframework.stereotype.Component;

@Component
public class StockMapper {
    public static StockQueryDto toStockDto(Stock stock){
        return new StockQueryDto()
                       .setId(stock.getId())
                       .setName(stock.getName())
                       .setCurrentPrice(stock.getCurrentPrice())
                       .setLastUpdate(stock.getLastUpdate())
                       .setLocked(stock.isLocked());
    }
}
