package com.moodlater.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 订单项实体类
 * 用于存储订单中的商品信息，包括商品ID、商品名称、商品图片、价格、数量等
 */
@Data
public class OrderItem {
    private Long id;                    // 订单项ID
    private Long orderId;               // 所属订单ID
    private Long productId;             // 商品ID
    private String productName;         // 商品名称
    private String productImage;        // 商品图片URL
    private String selectedSpec;        // 选中的规格
    private Integer price;              // 商品单价（单位：分）
    private Integer quantity;           // 商品数量
    private LocalDateTime createTime;   // 创建时间
}
