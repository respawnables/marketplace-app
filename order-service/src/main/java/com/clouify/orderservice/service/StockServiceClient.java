package com.clouify.orderservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "stock-service")
public interface StockServiceClient {

    @GetMapping(value = "/stocks/check",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    Boolean checkStock(@RequestParam("productId") Long productId, @RequestParam("quantity") Integer quantity);

    @PostMapping(
            value = "/stocks/update",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateStock(@RequestParam("productId") Long productId, @RequestParam("quantity") Integer quantity);
}
