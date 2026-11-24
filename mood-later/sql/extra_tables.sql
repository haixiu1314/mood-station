-- ============================================
-- 情绪小站 - 通知 / 留言 / 每日奖励 表
-- 说明：init.sql 等脚本未包含以下三张表，部署时需一并执行本脚本
-- ============================================
USE mood;

-- 消息通知表
CREATE TABLE IF NOT EXISTS `notification` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '通知ID',
    `user_id` BIGINT NOT NULL COMMENT '接收用户ID',
    `category` VARCHAR(32) DEFAULT NULL COMMENT '分类：interaction互动/system系统/order订单',
    `type` VARCHAR(32) DEFAULT NULL COMMENT '类型：like点赞/comment评论/forward转发/message留言/hug抱抱/appeal申诉等',
    `title` VARCHAR(255) DEFAULT NULL COMMENT '标题',
    `content` TEXT COMMENT '通知内容',
    `related_id` BIGINT DEFAULT NULL COMMENT '关联业务ID',
    `is_read` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已读 0-未读 1-已读',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_user_read` (`user_id`, `is_read`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息通知表';

-- 用户留言表
CREATE TABLE IF NOT EXISTS `user_message` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '留言ID',
    `from_user_id` BIGINT NOT NULL COMMENT '发送者ID',
    `from_nickname` VARCHAR(50) DEFAULT NULL COMMENT '发送者昵称',
    `from_avatar` VARCHAR(255) DEFAULT NULL COMMENT '发送者头像',
    `to_user_id` BIGINT NOT NULL COMMENT '接收者ID',
    `content` TEXT NOT NULL COMMENT '留言内容',
    `is_read` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已读 0-未读 1-已读',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_to_user` (`to_user_id`),
    KEY `idx_from_user` (`from_user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户留言表';

-- 每日奖励记录表
CREATE TABLE IF NOT EXISTS `daily_reward` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `reward_type` VARCHAR(50) NOT NULL COMMENT '奖励类型：login/diary/post/forward/like/comment等',
    `reward_date` DATE NOT NULL COMMENT '奖励日期',
    `amount` INT NOT NULL DEFAULT 0 COMMENT '奖励心动币数量',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_type_date` (`user_id`, `reward_type`, `reward_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='每日奖励记录表';
