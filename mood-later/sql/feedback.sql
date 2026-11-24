-- ============================================
-- 情绪小站 - 用户反馈表
-- ============================================
USE mood;

CREATE TABLE IF NOT EXISTS `feedback` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '反馈ID',
    `user_id` BIGINT NOT NULL COMMENT '提交用户ID',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '用户昵称',
    `content` TEXT NOT NULL COMMENT '反馈内容',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户反馈表';
