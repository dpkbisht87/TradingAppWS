package com.payconiq.tradingappws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class TradingAppWsApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(TradingAppWsApplication.class, args);
    }
    
}
