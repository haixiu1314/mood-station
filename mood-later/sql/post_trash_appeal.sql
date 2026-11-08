-- 1. 给post表增加is_deleted字段（软删除标记）
ALTER TABLE `post` 
ADD COLUMN `is_deleted` TINYINT(1) DEFAULT 0 COMMENT '是否已删除: 0未删除 1已删除' AFTER `update_time`;

-- 2. 创建帖子回收站表
CREATE TABLE `post_trash` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '回收站记录ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `post_id` BIGINT NOT NULL COMMENT '原帖子ID',
  `title` VARCHAR(50) DEFAULT NULL COMMENT '标题',
  `emotion` VARCHAR(20) DEFAULT NULL COMMENT '情绪类型',
  `emotion_label` VARCHAR(20) DEFAULT NULL COMMENT '情绪标签',
  `content` TEXT COMMENT '内容',
  `images` VARCHAR(1000) DEFAULT NULL COMMENT '图片',
  `like_count` INT DEFAULT 0 COMMENT '点赞数',
  `comment_count` INT DEFAULT 0 COMMENT '评论数',
  `deleted_by` VARCHAR(20) DEFAULT 'user' COMMENT '删除者: user用户 admin管理员',
  `deleted_by_name` VARCHAR(50) DEFAULT NULL COMMENT '删除者名称',
  `original_create_time` DATETIME DEFAULT NULL COMMENT '原帖子创建时间',
  `deleted_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '删除时间',
  `expire_time` DATETIME DEFAULT NULL COMMENT '过期时间（删除后30天）',
  `appeal_status` TINYINT(1) DEFAULT NULL COMMENT '申诉审核状态: null未申诉 1通过 2不通过',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子回收站表';

-- 3. 创建帖子申诉表
CREATE TABLE `post_appeal` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '申诉ID',
  `user_id` BIGINT NOT NULL COMMENT '申诉用户ID',
  `post_id` BIGINT NOT NULL COMMENT '被申诉帖子ID',
  `post_title` VARCHAR(50) DEFAULT NULL COMMENT '帖子标题',
  `post_content` TEXT COMMENT '帖子内容',
  `reason` TEXT COMMENT '申诉原因',
  `status` TINYINT(1) DEFAULT 0 COMMENT '申诉状态: 0待审核 1通过 2不通过',
  `appeal_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '申诉时间',
  `review_time` DATETIME DEFAULT NULL COMMENT '审核时间',
  `reviewer_id` BIGINT DEFAULT NULL COMMENT '审核人ID',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子申诉表';

-- 4. 如果post_trash表已存在，添加appeal_status字段（兼容已有数据库）
ALTER TABLE `post_trash` 
ADD COLUMN `appeal_status` TINYINT(1) DEFAULT NULL COMMENT '申诉审核状态: null未申诉 0申诉中 1通过 2不通过' AFTER `expire_time`;
