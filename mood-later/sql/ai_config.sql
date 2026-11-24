-- 情绪小喵 AI 配置表（每个用户独立配置自己的 url / key / model）
CREATE TABLE IF NOT EXISTS `ai_config` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `base_url` VARCHAR(500) NOT NULL COMMENT 'AI 服务 Base URL，如 https://api.deepseek.com',
    `api_key` VARCHAR(500) NOT NULL COMMENT 'API Key',
    `model` VARCHAR(100) NOT NULL COMMENT '模型名称，如 deepseek-chat',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI配置表';
