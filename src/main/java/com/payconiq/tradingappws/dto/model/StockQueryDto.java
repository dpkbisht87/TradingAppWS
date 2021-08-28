package com.payconiq.tradingappws.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockQueryDto  implements Comparable {
    private int id;
    
    private String name;
    
    private BigDecimal currentPrice;
    
    private Date creationDate;
    
    private Date lastUpdate;
    
    private boolean locked;
    
    @Override
    public int compareTo(Object o) {
        return this.getName().compareTo(((StockQueryDto) o).getName());
    }
}
