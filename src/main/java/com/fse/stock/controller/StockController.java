package com.fse.stock.controller;

import com.fse.stock.model.AllStockResponse;
import com.fse.stock.model.Stock;
import com.fse.stock.service.StockServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1.0/market/stock")
@SecurityRequirement(name = "stockapi")
public class StockController {

    @Autowired
    private StockServiceImpl stockService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> addStock(@RequestBody Stock stock) {
        stockService.addStock(stock);
        return new ResponseEntity<Object>(stock, HttpStatus.CREATED);
    }

    @GetMapping("/get/{companyCode}/{startDate}/{endDate}")
    public ResponseEntity<Object> findByCompanyCodeAndStockDateBetween(@PathVariable String companyCode, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        AllStockResponse response = stockService.findByCompanyCodeAndStockDateBetween(companyCode, startDate, endDate);
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    @GetMapping("/get/{companyCode}")
    public ResponseEntity<Object> findTopByCompanyCodeOrderByStockDateDesc(@PathVariable String companyCode) {
        Stock stock = stockService.findTopByCompanyCodeOrderByStockDateDesc(companyCode);
        return new ResponseEntity<Object>(stock, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{companyCode}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteByCompanyCode(@PathVariable String companyCode) {
        stockService.deleteByCompanyCode(companyCode);
    }
}
