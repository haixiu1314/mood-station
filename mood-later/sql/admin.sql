-- ============================================
-- 情绪小站 - 管理端相关表结构
-- 执行前请确保已执行 init.sql 和 extend.sql
-- ============================================

-- 关闭外键检查（避免删除表时因外键约束报错）
SET FOREIGN_KEY_CHECKS = 0;

USE mood;

-- ============================================
-- 1. 管理员表
-- ============================================
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
    `username` VARCHAR(50) NOT NULL COMMENT '管理员账号',
    `password` VARCHAR(100) NOT NULL COMMENT '密码（加密存储）',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `role` VARCHAR(20) DEFAULT 'operator' COMMENT '角色: super_admin超级管理员/operator运营人员',
    `status` TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 插入默认超级管理员（密码: admin123，实际使用时应加密）
INSERT INTO `admin` (`username`, `password`, `nickname`, `role`) VALUES
('admin', 'admin123', '超级管理员', 'super_admin');

-- ============================================
-- 2. 用户表扩展
-- （使用存储过程实现幂等，避免重复执行报错）
-- ============================================
DROP PROCEDURE IF EXISTS `AddAdminColumnsIfNotExists`;
DELIMITER $$
CREATE PROCEDURE `AddAdminColumnsIfNotExists`()
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS 
        WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'user' AND COLUMN_NAME = 'status'
    ) THEN
        ALTER TABLE `user` ADD COLUMN `status` VARCHAR(20) DEFAULT 'normal' COMMENT '账号状态: normal正常/banned封禁/deleted删除';
    END IF;
    
    IF NOT EXISTS (
        SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS 
        WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'user' AND COLUMN_NAME = 'last_login_time'
    ) THEN
        ALTER TABLE `user` ADD COLUMN `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间';
    END IF;
END$$
DELIMITER ;
CALL `AddAdminColumnsIfNotExists`();
DROP PROCEDURE IF EXISTS `AddAdminColumnsIfNotExists`;

-- ============================================
-- 3. 心动币变动日志表
-- ============================================
DROP TABLE IF EXISTS `coin_log`;
CREATE TABLE `coin_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `change_amount` INT NOT NULL COMMENT '变动数量（正数增加，负数减少）',
    `balance_after` INT NOT NULL COMMENT '变动后余额',
    `type` VARCHAR(20) NOT NULL COMMENT '类型: register注册/gift赠送/order订单/reward奖励/refund退款',
    `reason` VARCHAR(200) DEFAULT NULL COMMENT '原因说明',
    `operator_id` BIGINT DEFAULT NULL COMMENT '操作管理员ID',
    `related_id` BIGINT DEFAULT NULL COMMENT '关联ID（如订单ID）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_type` (`type`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心动币变动日志表';

-- ============================================
-- 4. 商品库存变动日志表
-- ============================================
DROP TABLE IF EXISTS `stock_log`;
CREATE TABLE `stock_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `product_id` BIGINT NOT NULL COMMENT '商品ID',
    `change_amount` INT NOT NULL COMMENT '变动数量（正数补货，负数扣减）',
    `stock_after` INT NOT NULL COMMENT '变动后库存',
    `operator_id` BIGINT DEFAULT NULL COMMENT '操作管理员ID',
    `reason` VARCHAR(200) DEFAULT NULL COMMENT '原因说明',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品库存变动日志表';

-- ============================================
-- 5. 管理端操作日志表
-- ============================================
DROP TABLE IF EXISTS `admin_log`;
CREATE TABLE `admin_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `admin_id` BIGINT NOT NULL COMMENT '管理员ID',
    `action` VARCHAR(50) NOT NULL COMMENT '操作类型',
    `target_type` VARCHAR(20) NOT NULL COMMENT '目标类型: user/post/comment/order/product',
    `target_id` BIGINT DEFAULT NULL COMMENT '目标ID',
    `detail` VARCHAR(500) DEFAULT NULL COMMENT '操作详情',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_admin_id` (`admin_id`),
    KEY `idx_action` (`action`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理端操作日志表';

-- 恢复外键检查
SET FOREIGN_KEY_CHECKS = 1;
