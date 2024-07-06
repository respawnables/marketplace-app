package com.clouify.stockservice.repository;

import com.clouify.stockservice.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock findByName(String name);
}
