package com.payconiq.tradingappws.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StockCreateDto {
    @NotBlank(message = "The Id is required.")
    private Long id;
    
    @NotBlank(message = "The name is required.")
    private String name;
    private BigDecimal currentValue;
    private Date lastUpdate;
    
    private boolean locked;
}
