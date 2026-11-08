package com.moodlater.entity;

import lombok.Data;

@Data
public class CartVO {
    private Long id;
    private Long productId;
    private String productName;
    private String productImage;
    private String selectedSpec;
    private Integer price;
    private Integer quantity;
    private Integer stock;
}
