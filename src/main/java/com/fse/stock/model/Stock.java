package com.fse.stock.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Document(collection = "Stock")
public class Stock {
    private String companyCode;
    private double stockPrice;
    private LocalDateTime stockDate;
}
