package com.moodlater.entity;

import lombok.Data;

@Data
public class ProductSalesVO {
    private Long productId;
    private String productName;
    private Long salesQuantity;
    private Long salesAmount;
}
