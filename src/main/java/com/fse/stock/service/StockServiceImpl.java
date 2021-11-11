package com.fse.stock.service;

import com.fse.stock.model.AllStockResponse;
import com.fse.stock.model.Stock;
import com.fse.stock.repo.StockRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockServiceImpl implements StockService {

    Logger LOGGER = LoggerFactory.getLogger(StockServiceImpl.class);

    @Autowired
    private StockRepo stockRepo;

    public Stock addStock(Stock stock) {
        LocalDateTime localDate = LocalDateTime.now();
        stock.setStockDate(localDate);
        LOGGER.info("New Stock Added with details: {}", stock.toString());
        return stockRepo.save(stock);
    }

    public Stock findTopByCompanyCodeOrderByStockDateDesc(String companyCode) {
        LOGGER.info("Finding current stock price details by company code: {}", companyCode);
        return stockRepo.findTopByCompanyCodeOrderByStockDateDesc(companyCode);
    }

    public AllStockResponse findByCompanyCodeAndStockDateBetween(String companyCode, LocalDate startDate, LocalDate endDate) {
        LOGGER.info("Finding history of stock price details by company code: {}", companyCode);
        AllStockResponse allStockResponse = new AllStockResponse();
        List<Stock> stocks = stockRepo.findByCompanyCodeAndStockDateBetween(companyCode, startDate, endDate);
        double min = 0;
        double max = 0;
        double avg = 0;
        double total = 0;
        if (!stocks.isEmpty()) {
            min = stocks.get(0).getStockPrice();
            max = stocks.get(0).getStockPrice();
            for (Stock stock : stocks) {
                if (stock.getStockPrice() < min) {
                    min = stock.getStockPrice();
                }
                if (stock.getStockPrice() > max) {
                    max = stock.getStockPrice();
                }
                total += stock.getStockPrice();
            }
            avg = total / stocks.size();

        }
        allStockResponse.setAllStocks(stocks);
        allStockResponse.setMax(max);
        allStockResponse.setMin(min);
        allStockResponse.setAvg(avg);
        return allStockResponse;
    }

    public void deleteByCompanyCode(String companyCode) {
        LOGGER.info("Deleting stocks for company code: {}", companyCode);
        stockRepo.deleteByCompanyCode(companyCode);
    }
}
