package com.fse.stock.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AllStockResponse {

    private List<Stock> allStocks;
    private double max;
    private double min;
    private double avg;
}
