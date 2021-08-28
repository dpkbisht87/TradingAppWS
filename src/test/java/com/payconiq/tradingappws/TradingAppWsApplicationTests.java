package com.payconiq.tradingappws;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
class TradingAppWsApplicationTests {
    
    @Test
    void contextLoads() {
    }
    
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
