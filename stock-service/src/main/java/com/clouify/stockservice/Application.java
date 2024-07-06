package com.clouify.stockservice;

import com.clouify.stockservice.entity.Stock;
import com.clouify.stockservice.service.StockService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.Random;
import java.util.stream.IntStream;

@SpringBootApplication
@EnableDiscoveryClient
public class Application implements CommandLineRunner {

    private final StockService stockService;

    public Application(StockService stockService) {
        this.stockService = stockService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Random random = new Random();
        IntStream.range(0, 10).forEach(i -> {
            Stock stock = new Stock();
            stock.setName("Product-" + (i + 1));
            stock.setQuantity(random.nextInt(50) + 1); // 1 ile 50 arasında rastgele stok miktarı
            stockService.addProduct(stock);
        });
    }

}
