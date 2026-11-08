-- =============================================
-- 订单模块升级 SQL
-- 用于Navicat直接执行
-- =============================================

-- 1. 给order表增加快递相关字段
ALTER TABLE `order` 
ADD COLUMN `express_company` VARCHAR(50) DEFAULT NULL COMMENT '快递公司名称' AFTER `refunded_time`,
ADD COLUMN `express_no` VARCHAR(50) DEFAULT NULL COMMENT '快递单号' AFTER `express_company`,
ADD COLUMN `express_courier` VARCHAR(50) DEFAULT NULL COMMENT '快递员/派送人' AFTER `express_no`,
ADD COLUMN `express_status` VARCHAR(20) DEFAULT NULL COMMENT '快递状态: delivering派送中 signed已签收' AFTER `express_courier`,
ADD COLUMN `received_time` DATETIME DEFAULT NULL COMMENT '签收时间' AFTER `express_status`,
ADD COLUMN `confirmed_time` DATETIME DEFAULT NULL COMMENT '用户确认收货时间' AFTER `received_time`;

-- 2. 创建订单退款申请表
CREATE TABLE `order_refund` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '退款申请ID',
  `order_id` BIGINT NOT NULL COMMENT '订单ID',
  `user_id` BIGINT NOT NULL COMMENT '申请用户ID',
  `order_no` VARCHAR(50) DEFAULT NULL COMMENT '订单号',
  `total_amount` INT DEFAULT 0 COMMENT '订单金额',
  `reason` TEXT COMMENT '退款原因',
  `status` TINYINT(1) DEFAULT 0 COMMENT '状态: 0待审核 1通过 2不通过',
  `apply_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `review_time` DATETIME DEFAULT NULL COMMENT '审核时间',
  `reviewer_id` BIGINT DEFAULT NULL COMMENT '审核人ID',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单退款申请表';

-- 3. 如果order表已有express相关字段（兼容已有数据库，忽略重复添加错误）
-- 如果上面ALTER已执行成功，以下语句会报错但可忽略
-- ALTER TABLE `order` ADD COLUMN `express_company` VARCHAR(50) DEFAULT NULL COMMENT '快递公司名称' AFTER `refunded_time`;
-- ALTER TABLE `order` ADD COLUMN `express_no` VARCHAR(50) DEFAULT NULL COMMENT '快递单号' AFTER `express_company`;
-- ALTER TABLE `order` ADD COLUMN `express_courier` VARCHAR(50) DEFAULT NULL COMMENT '快递员/派送人' AFTER `express_no`;
-- ALTER TABLE `order` ADD COLUMN `express_status` VARCHAR(20) DEFAULT NULL COMMENT '快递状态' AFTER `express_courier`;
-- ALTER TABLE `order` ADD COLUMN `received_time` DATETIME DEFAULT NULL COMMENT '签收时间' AFTER `express_status`;
-- ALTER TABLE `order` ADD COLUMN `confirmed_time` DATETIME DEFAULT NULL COMMENT '用户确认收货时间' AFTER `received_time`;
