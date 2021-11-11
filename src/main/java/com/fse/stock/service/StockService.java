package com.fse.stock.service;

import com.fse.stock.model.AllStockResponse;
import com.fse.stock.model.Stock;

import java.time.LocalDate;

public interface StockService {
    public Stock addStock(Stock stock);

    public Stock findTopByCompanyCodeOrderByStockDateDesc(String companyCode);

    public AllStockResponse findByCompanyCodeAndStockDateBetween(String companyCode, LocalDate startDate, LocalDate endDate);

    public void deleteByCompanyCode(String companyCode);
}
