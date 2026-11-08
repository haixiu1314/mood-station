-- ============================================
-- 情绪小站数据库扩展脚本（购物车、订单、地址）
-- ============================================

-- 关闭外键检查（避免删除表时因外键约束报错）
SET FOREIGN_KEY_CHECKS = 0;

USE mood;

-- ============================================
-- 1. 用户表扩展：添加心动币余额字段
-- 2. 商品表扩展：添加库存字段
-- （使用存储过程实现幂等，避免重复执行报错）
-- ============================================
DROP PROCEDURE IF EXISTS `AddColumnsIfNotExists`;
DELIMITER $$
CREATE PROCEDURE `AddColumnsIfNotExists`()
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS 
        WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'user' AND COLUMN_NAME = 'coin_balance'
    ) THEN
        ALTER TABLE `user` ADD COLUMN `coin_balance` INT DEFAULT 200 COMMENT '心动币余额';
    END IF;
    
    IF NOT EXISTS (
        SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS 
        WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'product' AND COLUMN_NAME = 'stock'
    ) THEN
        ALTER TABLE `product` ADD COLUMN `stock` INT DEFAULT 100 COMMENT '库存数量';
    END IF;
    
    IF NOT EXISTS (
        SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS 
        WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'user' AND COLUMN_NAME = 'status'
    ) THEN
        ALTER TABLE `user` ADD COLUMN `status` VARCHAR(20) DEFAULT 'normal' COMMENT '状态: normal-正常/banned-已封禁';
    END IF;
    
    IF NOT EXISTS (
        SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS 
        WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'user' AND COLUMN_NAME = 'last_login_time'
    ) THEN
        ALTER TABLE `user` ADD COLUMN `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间';
    END IF;
END$$
DELIMITER ;
CALL `AddColumnsIfNotExists`();
DROP PROCEDURE IF EXISTS `AddColumnsIfNotExists`;

-- ============================================
-- 3. 购物车表
-- ============================================
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `product_id` BIGINT NOT NULL COMMENT '商品ID',
    `quantity` INT DEFAULT 1 COMMENT '数量',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_product` (`user_id`, `product_id`),
    KEY `idx_user_id` (`user_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    FOREIGN KEY (`product_id`) REFERENCES `product`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- ============================================
-- 4. 收货地址表
-- ============================================
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '地址ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `name` VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    `phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
    `province` VARCHAR(50) NOT NULL COMMENT '省份',
    `city` VARCHAR(50) NOT NULL COMMENT '城市',
    `district` VARCHAR(50) NOT NULL COMMENT '区县',
    `detail` VARCHAR(255) NOT NULL COMMENT '详细地址',
    `is_default` TINYINT DEFAULT 0 COMMENT '是否默认 0-否 1-是',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收货地址表';

-- ============================================
-- 5. 订单表
-- ============================================
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no` VARCHAR(32) NOT NULL COMMENT '订单号',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `total_amount` INT NOT NULL COMMENT '心动币总价',
    `status` VARCHAR(20) DEFAULT 'pending' COMMENT '状态: pending待发货/shipped已发货/refunded已退款',
    `address_name` VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    `address_phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
    `address_detail` VARCHAR(255) NOT NULL COMMENT '收货地址',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `shipped_time` DATETIME DEFAULT NULL COMMENT '发货时间',
    `refunded_time` DATETIME DEFAULT NULL COMMENT '退款时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- ============================================
-- 6. 订单商品关联表
-- ============================================
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单商品ID',
    `order_id` BIGINT NOT NULL COMMENT '订单ID',
    `product_id` BIGINT NOT NULL COMMENT '商品ID',
    `product_name` VARCHAR(200) NOT NULL COMMENT '商品名称',
    `product_image` VARCHAR(500) DEFAULT NULL COMMENT '商品图片',
    `price` INT NOT NULL COMMENT '下单时单价（心动币）',
    `quantity` INT NOT NULL COMMENT '数量',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    FOREIGN KEY (`order_id`) REFERENCES `order`(`id`),
    FOREIGN KEY (`product_id`) REFERENCES `product`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单商品关联表';

-- 恢复外键检查
SET FOREIGN_KEY_CHECKS = 1;
