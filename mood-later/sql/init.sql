-- ============================================
-- 情绪小站数据库初始化脚本
-- 数据库名: mood
-- ============================================

-- 关闭外键检查（避免删除表时因外键约束报错）
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS mood DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE mood;

-- ============================================
-- 1. 用户表
-- ============================================
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `openid` VARCHAR(64) NOT NULL COMMENT '微信openid',
    `username` VARCHAR(20) DEFAULT NULL COMMENT '账号名',
    `password` VARCHAR(50) DEFAULT NULL COMMENT '密码',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '用户昵称',
    `avatar_url` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `gender` TINYINT DEFAULT 0 COMMENT '性别 0-未知 1-男 2-女',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_openid` (`openid`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ============================================
-- 2. 日记表
-- ============================================
DROP TABLE IF EXISTS `diary`;
CREATE TABLE `diary` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日记ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `emotion` VARCHAR(20) NOT NULL COMMENT '情绪类型: happy开心/sad难过/angry生气/calm平静/anxious焦虑',
    `emotion_label` VARCHAR(20) NOT NULL COMMENT '情绪标签显示文字',
    `title` VARCHAR(100) DEFAULT NULL COMMENT '日记标题',
    `content` TEXT NOT NULL COMMENT '日记内容',
    `images` VARCHAR(1000) DEFAULT NULL COMMENT '图片URL列表,逗号分隔',
    `is_public` TINYINT DEFAULT 0 COMMENT '是否公开 0-私密 1-公开',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日记表';

-- ============================================
-- 3. 帖子表（情绪分享）
-- ============================================
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
    `user_id` BIGINT NOT NULL COMMENT '作者用户ID',
    `content` TEXT NOT NULL COMMENT '帖子内容',
    `images` VARCHAR(1000) DEFAULT NULL COMMENT '图片URL列表,逗号分隔',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `comment_count` INT DEFAULT 0 COMMENT '评论数',
    `source` VARCHAR(20) DEFAULT 'original' COMMENT '来源: original原创/diary日记转发',
    `source_diary_id` BIGINT DEFAULT NULL COMMENT '来源日记ID(如果是转发)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子表';

-- ============================================
-- 4. 点赞表
-- ============================================
DROP TABLE IF EXISTS `like_record`;
CREATE TABLE `like_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `user_id` BIGINT NOT NULL COMMENT '点赞用户ID',
    `post_id` BIGINT NOT NULL COMMENT '帖子ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_post` (`user_id`, `post_id`),
    KEY `idx_post_id` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞记录表';

-- ============================================
-- 5. 评论表
-- ============================================
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `post_id` BIGINT NOT NULL COMMENT '帖子ID',
    `user_id` BIGINT NOT NULL COMMENT '评论用户ID',
    `content` VARCHAR(500) NOT NULL COMMENT '评论内容',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_post_id` (`post_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- ============================================
-- 6. 商品表
-- ============================================
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品ID',
    `name` VARCHAR(100) NOT NULL COMMENT '商品名称',
    `cover_image` VARCHAR(255) NOT NULL COMMENT '封面图片URL',
    `images` VARCHAR(1000) DEFAULT NULL COMMENT '商品图片列表,逗号分隔',
    `price` DECIMAL(10,2) NOT NULL COMMENT '现价',
    `original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '原价',
    `description` TEXT COMMENT '商品描述',
    `tags` VARCHAR(200) DEFAULT NULL COMMENT '标签,逗号分隔',
    `taobao_url` VARCHAR(500) NOT NULL COMMENT '淘宝链接',
    `sort_order` INT DEFAULT 0 COMMENT '排序权重',
    `status` TINYINT DEFAULT 1 COMMENT '状态 0-下架 1-上架',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_status_sort` (`status`, `sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- ============================================
-- 7. 治愈语录表（今日锦囊）
-- ============================================
DROP TABLE IF EXISTS `healing_quote`;
CREATE TABLE `healing_quote` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '语录ID',
    `content` VARCHAR(200) NOT NULL COMMENT '语录内容',
    `author` VARCHAR(50) DEFAULT NULL COMMENT '作者/出处',
    `category` VARCHAR(20) DEFAULT 'daily' COMMENT '分类: daily日常/morning晨间/evening晚间',
    `is_active` TINYINT DEFAULT 1 COMMENT '是否启用 0-禁用 1-启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_category_active` (`category`, `is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='治愈语录表';

-- ============================================
-- 8. 轮播图表
-- ============================================
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '轮播图ID',
    `title` VARCHAR(50) DEFAULT NULL COMMENT '标题',
    `image_url` VARCHAR(255) NOT NULL COMMENT '图片URL',
    `link_url` VARCHAR(500) DEFAULT NULL COMMENT '跳转链接',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `is_active` TINYINT DEFAULT 1 COMMENT '是否启用 0-禁用 1-启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_sort_active` (`sort_order`, `is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='轮播图表';

-- ============================================
-- 插入初始数据
-- ============================================

-- 插入治愈语录示例数据
INSERT INTO `healing_quote` (`content`, `author`, `category`) VALUES
('每一片乌云背后都有阳光', 'Unknown', 'daily'),
('你今天已经做得很好了', 'Unknown', 'daily'),
('允许自己休息，也是一种勇敢', 'Unknown', 'daily'),
('慢慢来，比较快', 'Unknown', 'daily'),
('你值得被这个世界温柔以待', 'Unknown', 'daily'),
('所有的美好都在路上', 'Unknown', 'daily'),
('今天的你，比昨天更强大', 'Unknown', 'daily'),
('深呼吸，一切都会好起来的', 'Unknown', 'daily');

-- 插入商品示例数据
INSERT INTO `product` (`name`, `cover_image`, `price`, `original_price`, `description`, `tags`, `taobao_url`) VALUES
('治愈系香薰蜡烛', 'https://example.com/candle.jpg', 39.90, 59.00, '薰衣草香味，帮助放松入眠', '治愈系,热销', 'https://item.taobao.com/item.htm?id=1'),
('解压魔方', 'https://example.com/cube.jpg', 29.90, 49.00, '六面不同解压设计，随身携带', '解压,办公', 'https://item.taobao.com/item.htm?id=2'),
('情绪日记本', 'https://example.com/diary.jpg', 49.00, 69.00, '精装手账本，记录每天的心情', '文具,治愈系', 'https://item.taobao.com/item.htm?id=3'),
('暖心治愈书籍《蛤蟆先生去看心理医生》', 'https://example.com/book.jpg', 35.00, 55.00, '心理学入门经典，帮助你了解自己的情绪', '书籍,心理', 'https://item.taobao.com/item.htm?id=4');

-- 插入轮播图示例数据
INSERT INTO `banner` (`title`, `image_url`, `link_url`, `sort_order`) VALUES
('欢迎来到情绪小站', 'https://example.com/banner1.jpg', '/pages/index/index', 1),
('写下你的心情故事', 'https://example.com/banner2.jpg', '/pages/index/index', 2),
('发现更多治愈好物', 'https://example.com/banner3.jpg', '/pages/shop/index', 3);

-- 恢复外键检查
SET FOREIGN_KEY_CHECKS = 1;
