package com.clouify.stockservice.service;

import com.clouify.stockservice.constants.ApplicationConstants;
import com.clouify.stockservice.entity.Stock;
import com.clouify.stockservice.repository.StockRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockService {
    private final StockRepository stockRepository;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public StockService(final StockRepository stockRepository,
                        final KafkaTemplate<String, String> kafkaTemplate) {
        this.stockRepository = stockRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Stock addProduct(final Stock stock) {
        return stockRepository.save(stock);
    }

    @Transactional
    public void updateStock(final Long productId, final Integer quantity) {
        final Stock stock = stockRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        stock.setQuantity(stock.getQuantity() - quantity);
        stockRepository.save(stock);

        if (stock.getQuantity() < 10) {
            kafkaTemplate.send(ApplicationConstants.STOCK_TOPIC, stock.toString());
        }
    }

    public boolean isStockAvailable(final Long productId, final Integer quantity) {
        final Stock stock = stockRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        return stock.getQuantity() >= quantity;
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }
}
