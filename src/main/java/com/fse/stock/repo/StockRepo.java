package com.fse.stock.repo;

import com.fse.stock.model.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface StockRepo extends MongoRepository<Stock, String> {

    /*public Optional<List<Stock>> findByCompanyCodeAndStockDateBetween(String companyCode, Date startDate, Date endDate);*/

    public List<Stock> findByCompanyCodeAndStockDateBetween(String companyCode, LocalDate startDate, LocalDate endDate);

    public Stock findTopByCompanyCodeOrderByStockDateDesc(String companyCode);

    public void deleteByCompanyCode(String companyCode);
}
