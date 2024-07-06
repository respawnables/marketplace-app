package com.clouify.stockservice.dto;

import lombok.Data;

@Data
public class UpdateStockRequest {
    private Long productId;

    private Integer quantity;
}
