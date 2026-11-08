/*
 Navicat Premium Data Transfer

 Source Server         : mood
 Source Server Type    : MySQL
 Source Server Version : 80043
 Source Host           : localhost:3306
 Source Schema         : mood

 Target Server Type    : MySQL
 Target Server Version : 80043
 File Encoding         : 65001

 Date: 22/06/2026 00:32:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '地址ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系电话',
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '省份',
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '城市',
  `district` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '区县',
  `detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '详细地址',
  `is_default` tinyint NULL DEFAULT 0 COMMENT '是否默认 0-否 1-是',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  CONSTRAINT `address_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '收货地址表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of address
-- ----------------------------
INSERT INTO `address` VALUES (1, 1, '许', '12345678223', '北京市', '北京市', '东城区', '111号路', 1, '2026-06-02 00:22:02', '2026-06-21 18:32:50');
INSERT INTO `address` VALUES (2, 1, '李', '14673986543', '北京市', '北京市', '东城区', '11号', 0, '2026-06-21 16:02:08', '2026-06-21 18:32:50');
INSERT INTO `address` VALUES (3, 2, '周杰伦', '14567766445', '台湾省', '台北市', '中正区', '11111111111', 0, '2026-06-21 22:27:50', '2026-06-21 22:27:50');

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '管理员账号',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码（加密存储）',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'operator' COMMENT '角色: super_admin超级管理员/operator运营人员',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', 'admin123', '超级管理员', 'super_admin', 1, '2026-05-22 11:29:17', '2026-05-22 11:29:17');

-- ----------------------------
-- Table structure for admin_log
-- ----------------------------
DROP TABLE IF EXISTS `admin_log`;
CREATE TABLE `admin_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `admin_id` bigint NOT NULL COMMENT '管理员ID',
  `action` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作类型',
  `target_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目标类型: user/post/comment/order/product',
  `target_id` bigint NULL DEFAULT NULL COMMENT '目标ID',
  `detail` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作详情',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_admin_id`(`admin_id`) USING BTREE,
  INDEX `idx_action`(`action`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理端操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin_log
-- ----------------------------

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '轮播图ID',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片URL',
  `link_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '跳转链接',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `is_active` tinyint NULL DEFAULT 1 COMMENT '是否启用 0-禁用 1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sort_active`(`sort_order`, `is_active`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '轮播图表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of banner
-- ----------------------------
INSERT INTO `banner` VALUES (4, '写下你的心情故事', 'http://localhost:8081/uploads/2aa597a9-5769-4898-99ec-7225366995f3.png', '/pages/diary/edit', 1, 1, '2026-06-19 18:52:59');
INSERT INTO `banner` VALUES (5, '发现更多治愈好物', 'http://localhost:8081/uploads/c2f12237-66be-43d2-9431-19e3477b8e44.png', '/pages/shop/index', 2, 1, '2026-06-19 18:54:19');

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `quantity` int NULL DEFAULT 1 COMMENT '数量',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `selected_spec` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '选中的规格',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_product`(`user_id`, `product_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `product_id`(`product_id`) USING BTREE,
  CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '购物车表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart
-- ----------------------------

-- ----------------------------
-- Table structure for coin_gift_log
-- ----------------------------
DROP TABLE IF EXISTS `coin_gift_log`;
CREATE TABLE `coin_gift_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `user_nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `coin_amount` int NOT NULL COMMENT '赠送心动币数量',
  `previous_balance` int NULL DEFAULT 0 COMMENT '赠送前余额',
  `current_balance` int NULL DEFAULT 0 COMMENT '赠送后余额',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '赠送备注',
  `admin_id` bigint NOT NULL COMMENT '操作管理员ID',
  `admin_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '管理员昵称',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '赠送时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_admin_id`(`admin_id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '心动币赠送日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of coin_gift_log
-- ----------------------------

-- ----------------------------
-- Table structure for coin_log
-- ----------------------------
DROP TABLE IF EXISTS `coin_log`;
CREATE TABLE `coin_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `change_amount` int NOT NULL COMMENT '变动数量（正数增加，负数减少）',
  `balance_after` int NOT NULL COMMENT '变动后余额',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类型: register注册/gift赠送/order订单/reward奖励/refund退款',
  `reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '原因说明',
  `operator_id` bigint NULL DEFAULT NULL COMMENT '操作管理员ID',
  `related_id` bigint NULL DEFAULT NULL COMMENT '关联ID（如订单ID）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_type`(`type`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '心动币变动日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of coin_log
-- ----------------------------
INSERT INTO `coin_log` VALUES (1, 1, 100, 260, 'gift', '新用户奖励', 1, NULL, '2026-06-01 23:13:05');
INSERT INTO `coin_log` VALUES (2, 2, 100, 300, 'gift', '管理员赠送', 1, NULL, '2026-06-02 00:59:46');
INSERT INTO `coin_log` VALUES (3, 1, 300, 397, 'gift', '管理员赠送', 1, NULL, '2026-06-02 17:11:05');
INSERT INTO `coin_log` VALUES (4, 1, 39, 397, 'refund', '订单退款 - 订单号: 17803915296552961', NULL, 5, '2026-06-02 17:13:04');
INSERT INTO `coin_log` VALUES (5, 1, 40, 402, 'refund', '订单退款审核通过 - 订单号: 17803309273036805', NULL, 1, '2026-06-16 23:11:14');
INSERT INTO `coin_log` VALUES (6, 1, 35, 265, 'refund', '订单退款审核通过 - 订单号: 17816303232516290', NULL, 12, '2026-06-17 01:27:59');
INSERT INTO `coin_log` VALUES (7, 1, 35, 265, 'refund', '订单退款审核通过 - 订单号: 17816704793888395', NULL, 13, '2026-06-17 12:31:34');
INSERT INTO `coin_log` VALUES (8, 1, 10000, 10226, 'gift', '管理员赠送', 1, NULL, '2026-06-19 21:57:15');
INSERT INTO `coin_log` VALUES (9, 1, 10000, 20226, 'gift', '管理员赠送', 1, NULL, '2026-06-19 21:57:25');
INSERT INTO `coin_log` VALUES (10, 1, 10000, 30226, 'gift', '管理员赠送', 1, NULL, '2026-06-19 21:57:30');
INSERT INTO `coin_log` VALUES (11, 1, 10000, 40226, 'gift', '管理员赠送', 1, NULL, '2026-06-19 21:57:36');
INSERT INTO `coin_log` VALUES (12, 1, 100, 31520, 'gift', '管理员赠送', 1, NULL, '2026-06-21 17:37:20');
INSERT INTO `coin_log` VALUES (13, 1, 100, 31620, 'gift', '管理员赠送', 1, NULL, '2026-06-21 17:37:47');
INSERT INTO `coin_log` VALUES (14, 1, 160, 31620, 'refund', '订单退款审核通过 - 订单号: 17820348981260312', NULL, 24, '2026-06-21 17:43:37');
INSERT INTO `coin_log` VALUES (15, 1, 68, 31377, 'refund', '订单退款审核通过 - 订单号: 17820591059553533', NULL, 31, '2026-06-22 00:26:29');

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `post_id` bigint NOT NULL COMMENT '帖子ID',
  `user_id` bigint NOT NULL COMMENT '评论用户ID',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_post_id`(`post_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (2, 1, 2, '哎哟不错哦', '2026-05-31 17:38:15');
INSERT INTO `comment` VALUES (3, 3, 2, '哎呦，说脏话了哦', '2026-06-02 18:41:31');
INSERT INTO `comment` VALUES (4, 4, 1, '666', '2026-06-05 13:03:16');
INSERT INTO `comment` VALUES (5, 7, 1, '11111', '2026-06-21 13:20:36');
INSERT INTO `comment` VALUES (6, 8, 2, '加油', '2026-06-21 17:49:51');
INSERT INTO `comment` VALUES (7, 10, 1, '1111', '2026-06-21 18:32:07');
INSERT INTO `comment` VALUES (8, 10, 1, '1111', '2026-06-21 18:32:10');
INSERT INTO `comment` VALUES (9, 10, 1, '1111', '2026-06-21 18:32:11');
INSERT INTO `comment` VALUES (10, 10, 1, '1111', '2026-06-21 18:32:11');
INSERT INTO `comment` VALUES (11, 10, 1, '1111', '2026-06-21 18:32:11');
INSERT INTO `comment` VALUES (12, 10, 1, '1111', '2026-06-21 18:32:12');
INSERT INTO `comment` VALUES (13, 10, 1, '1111', '2026-06-21 18:32:15');
INSERT INTO `comment` VALUES (14, 10, 1, '1111', '2026-06-21 18:32:15');
INSERT INTO `comment` VALUES (15, 11, 1, '111', '2026-06-21 22:20:11');
INSERT INTO `comment` VALUES (16, 15, 2, '111', '2026-06-21 22:32:20');
INSERT INTO `comment` VALUES (17, 17, 1, '111', '2026-06-22 00:27:17');

-- ----------------------------
-- Table structure for daily_reward
-- ----------------------------
DROP TABLE IF EXISTS `daily_reward`;
CREATE TABLE `daily_reward`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `reward_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '奖励类型：daily_login-每日登录，daily_diary-每日日记，daily_post-每日帖子，daily_forward-每日转发，daily_like-每日点赞，daily_comment-每日评论',
  `reward_date` date NOT NULL COMMENT '奖励日期',
  `amount` int NOT NULL DEFAULT 0 COMMENT '奖励金额',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_type_date`(`user_id`, `reward_type`, `reward_date`) USING BTREE,
  INDEX `idx_user_date`(`user_id`, `reward_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '每日奖励记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of daily_reward
-- ----------------------------
INSERT INTO `daily_reward` VALUES (1, 2, 'daily_login', '2026-06-21', 2);
INSERT INTO `daily_reward` VALUES (2, 2, 'daily_post', '2026-06-21', 2);
INSERT INTO `daily_reward` VALUES (3, 2, 'daily_comment', '2026-06-21', 1);
INSERT INTO `daily_reward` VALUES (4, 2, 'daily_like', '2026-06-21', 1);
INSERT INTO `daily_reward` VALUES (5, 2, 'daily_diary', '2026-06-21', 2);
INSERT INTO `daily_reward` VALUES (6, 1, 'daily_login', '2026-06-21', 2);
INSERT INTO `daily_reward` VALUES (7, 1, 'daily_post', '2026-06-22', 2);
INSERT INTO `daily_reward` VALUES (8, 1, 'daily_forward', '2026-06-22', 1);
INSERT INTO `daily_reward` VALUES (9, 1, 'daily_diary', '2026-06-22', 2);
INSERT INTO `daily_reward` VALUES (10, 1, 'daily_like', '2026-06-22', 1);
INSERT INTO `daily_reward` VALUES (11, 1, 'daily_comment', '2026-06-22', 1);
INSERT INTO `daily_reward` VALUES (12, 2, 'daily_login', '2026-06-22', 2);

-- ----------------------------
-- Table structure for diary
-- ----------------------------
DROP TABLE IF EXISTS `diary`;
CREATE TABLE `diary`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日记ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `emotion` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '情绪类型: happy开心/sad难过/angry生气/calm平静/anxious焦虑',
  `emotion_label` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '情绪标签显示文字',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '日记标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '日记内容',
  `images` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片URL列表,逗号分隔',
  `is_public` tinyint NULL DEFAULT 0 COMMENT '是否公开 0-私密 1-公开',
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '是否已删除 0-正常 1-已删除',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '日记表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of diary
-- ----------------------------
INSERT INTO `diary` VALUES (1, 1, 'happy', '开心', '周末快乐', '今天是周五，明天可以好好休息，规划出行，好开心', '', 0, 0, '2026-05-31 16:40:47', '2026-05-31 16:40:47');
INSERT INTO `diary` VALUES (2, 1, 'sad', '难过', '今天下雨了，衣服被淋湿了', '天气预报明明说的今天没雨，结果在半路上下起暴雨，我刚好又没带伞，衣服都湿了，好难过', '', 0, 0, '2026-05-31 16:42:15', '2026-05-31 16:42:15');
INSERT INTO `diary` VALUES (4, 1, 'happy', '开心', '测试', '1111', '', 0, 1, '2026-06-09 17:27:49', '2026-06-09 17:28:11');
INSERT INTO `diary` VALUES (5, 1, 'happy', '开心', '测试1', '66666666666666666666666666666666', '', 0, 1, '2026-06-09 17:46:21', '2026-06-09 17:46:28');
INSERT INTO `diary` VALUES (6, 1, 'happy', '开心', '测试啊', 'eerrrtrt', '', 0, 1, '2026-06-09 18:41:44', '2026-06-09 18:42:03');
INSERT INTO `diary` VALUES (7, 1, 'happy', '开心', '测试', '哒哒哒dsad\n', '', 0, 1, '2026-06-09 18:52:46', '2026-06-09 18:52:52');
INSERT INTO `diary` VALUES (9, 1, 'sad', '难过', '测试', '111111111111111111111111111111111111111111111111', '', 0, 0, '2026-06-16 15:37:57', '2026-06-19 23:47:17');
INSERT INTO `diary` VALUES (10, 1, 'anxious', '焦虑', '111111111', '11111111111111111111111111111', '', 0, 0, '2026-06-20 00:11:01', '2026-06-20 00:23:00');
INSERT INTO `diary` VALUES (11, 1, 'angry', '生气', '好多作业，不开心', '111111111111111111111111', '', 1, 0, '2026-06-21 18:31:32', '2026-06-21 23:06:16');
INSERT INTO `diary` VALUES (12, 1, 'happy', '开心', '11111', '111', '', 0, 0, '2026-06-21 18:39:12', '2026-06-21 18:39:12');
INSERT INTO `diary` VALUES (13, 2, 'happy', '开心', '又是下雨天', '今天又是下雨天，不舒服', '', 0, 0, '2026-06-21 18:46:11', '2026-06-21 18:46:11');
INSERT INTO `diary` VALUES (14, 3, 'happy', '开心', '11111111111111', '11111111111', '', 0, 0, '2026-06-21 18:56:44', '2026-06-21 18:56:44');
INSERT INTO `diary` VALUES (15, 6, 'happy', '开心', '11111', '1111111111', '', 0, 0, '2026-06-21 21:08:44', '2026-06-21 21:08:44');
INSERT INTO `diary` VALUES (16, 1, 'happy', '开心', '测试', '11111', '', 0, 0, '2026-06-21 22:10:11', '2026-06-21 22:10:11');
INSERT INTO `diary` VALUES (17, 1, 'happy', '开心', '测试1', '111111', '', 0, 0, '2026-06-21 22:17:25', '2026-06-21 22:17:25');
INSERT INTO `diary` VALUES (18, 2, 'happy', '开心', '测试1', '123333', '', 0, 0, '2026-06-21 22:26:40', '2026-06-21 22:26:40');
INSERT INTO `diary` VALUES (19, 2, 'happy', '开心', '测试2', '12344', '', 0, 0, '2026-06-21 22:26:51', '2026-06-21 22:26:51');
INSERT INTO `diary` VALUES (20, 2, 'happy', '开心', '测试2', '1111', '', 0, 0, '2026-06-21 22:32:35', '2026-06-21 22:32:35');
INSERT INTO `diary` VALUES (21, 2, 'happy', '开心', '111', '111', '', 1, 0, '2026-06-21 23:04:30', '2026-06-21 23:04:30');
INSERT INTO `diary` VALUES (22, 1, 'happy', '开心', '1111', '1111', '', 1, 0, '2026-06-22 00:24:22', '2026-06-22 00:24:22');

-- ----------------------------
-- Table structure for diary_trash
-- ----------------------------
DROP TABLE IF EXISTS `diary_trash`;
CREATE TABLE `diary_trash`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '回收站记录ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `diary_id` bigint NOT NULL COMMENT '原日记ID',
  `emotion` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '情绪类型',
  `emotion_label` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '情绪标签',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '内容',
  `images` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片',
  `is_public` tinyint(1) NULL DEFAULT 0 COMMENT '是否公开',
  `original_create_time` datetime NULL DEFAULT NULL COMMENT '原日记创建时间',
  `deleted_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '删除时间',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '过期时间（删除后30天）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_diary_id`(`diary_id`) USING BTREE,
  INDEX `idx_expire_time`(`expire_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '日记回收站表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of diary_trash
-- ----------------------------

-- ----------------------------
-- Table structure for express_company
-- ----------------------------
DROP TABLE IF EXISTS `express_company`;
CREATE TABLE `express_company`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '快递公司名称',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '快递公司编码',
  `placeholder` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '单号输入提示',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '快递公司信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of express_company
-- ----------------------------
INSERT INTO `express_company` VALUES (1, '顺丰速运', 'SF', '例如: SF1234567890', 1);
INSERT INTO `express_company` VALUES (2, '中通快递', 'ZT', '例如: 753123456789', 2);
INSERT INTO `express_company` VALUES (3, '圆通速递', 'YT', '例如: YT1234567890', 3);
INSERT INTO `express_company` VALUES (4, '韵达', 'YD', '例如: 4312345678901', 4);
INSERT INTO `express_company` VALUES (5, '申通快递', 'ST', '例如: 771234567890', 5);
INSERT INTO `express_company` VALUES (6, '极兔速递', 'JT', '例如: JT1234567890', 6);
INSERT INTO `express_company` VALUES (7, '邮政EMS', 'EMS', '例如: EI123456789CN', 7);

-- ----------------------------
-- Table structure for healing_quote
-- ----------------------------
DROP TABLE IF EXISTS `healing_quote`;
CREATE TABLE `healing_quote`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '语录ID',
  `content` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '语录内容',
  `author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '作者/出处',
  `category` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'daily' COMMENT '分类: daily日常/morning晨间/evening晚间',
  `is_active` tinyint NULL DEFAULT 1 COMMENT '是否启用 0-禁用 1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_active`(`category`, `is_active`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '治愈语录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of healing_quote
-- ----------------------------
INSERT INTO `healing_quote` VALUES (1, '每一片乌云背后都有阳光', 'Unknown', 'daily', 1, '2026-05-22 11:17:08');
INSERT INTO `healing_quote` VALUES (2, '你今天已经做得很好了', 'Unknown', 'daily', 1, '2026-05-22 11:17:08');
INSERT INTO `healing_quote` VALUES (3, '允许自己休息，也是一种勇敢', 'Unknown', 'daily', 1, '2026-05-22 11:17:08');
INSERT INTO `healing_quote` VALUES (4, '慢慢来，比较快', 'Unknown', 'daily', 1, '2026-05-22 11:17:08');
INSERT INTO `healing_quote` VALUES (5, '你值得被这个世界温柔以待', 'Unknown', 'daily', 1, '2026-05-22 11:17:08');
INSERT INTO `healing_quote` VALUES (6, '所有的美好都在路上', 'Unknown', 'daily', 1, '2026-05-22 11:17:08');
INSERT INTO `healing_quote` VALUES (7, '今天的你，比昨天更强大', 'Unknown', 'daily', 1, '2026-05-22 11:17:08');
INSERT INTO `healing_quote` VALUES (8, '深呼吸，一切都会好起来的', 'Unknown', 'daily', 1, '2026-05-22 11:17:08');
INSERT INTO `healing_quote` VALUES (9, '生活虽然 tough，但你比它更 tough！加油！', '系统', 'hug', 1, '2026-06-21 23:01:46');
INSERT INTO `healing_quote` VALUES (10, '每一个不曾起舞的日子，都是对生命的辜负。', '尼采', 'hug', 1, '2026-06-21 23:01:46');
INSERT INTO `healing_quote` VALUES (11, '你不需要很厉害才能开始，但你需要开始才能变得很厉害。', '系统', 'hug', 1, '2026-06-21 23:01:46');
INSERT INTO `healing_quote` VALUES (12, '今天的你也是独一无二的，记得好好爱自己哦~', '系统', 'hug', 1, '2026-06-21 23:01:46');
INSERT INTO `healing_quote` VALUES (13, '无论遇到什么困难，都要相信明天会更好！', '系统', 'hug', 1, '2026-06-21 23:01:46');
INSERT INTO `healing_quote` VALUES (14, '你的努力，时间都会看得见。', '系统', 'hug', 1, '2026-06-21 23:01:46');
INSERT INTO `healing_quote` VALUES (15, '抱抱你，一切都会好起来的~', '系统', 'hug', 1, '2026-06-21 23:01:46');
INSERT INTO `healing_quote` VALUES (16, '你是最棒的，不要放弃哦！', '系统', 'hug', 1, '2026-06-21 23:01:46');

-- ----------------------------
-- Table structure for like_record
-- ----------------------------
DROP TABLE IF EXISTS `like_record`;
CREATE TABLE `like_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` bigint NOT NULL COMMENT '点赞用户ID',
  `post_id` bigint NOT NULL COMMENT '帖子ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_post`(`user_id`, `post_id`) USING BTREE,
  INDEX `idx_post_id`(`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '点赞记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of like_record
-- ----------------------------
INSERT INTO `like_record` VALUES (3, 1, 2, '2026-05-31 17:35:09');
INSERT INTO `like_record` VALUES (5, 2, 1, '2026-05-31 17:37:52');
INSERT INTO `like_record` VALUES (6, 2, 2, '2026-05-31 17:37:53');
INSERT INTO `like_record` VALUES (7, 1, 3, '2026-06-02 17:51:13');
INSERT INTO `like_record` VALUES (8, 2, 3, '2026-06-02 18:41:33');
INSERT INTO `like_record` VALUES (9, 1, 7, '2026-06-21 13:20:44');
INSERT INTO `like_record` VALUES (11, 2, 8, '2026-06-21 17:49:53');
INSERT INTO `like_record` VALUES (15, 2, 10, '2026-06-21 18:46:29');
INSERT INTO `like_record` VALUES (16, 1, 13, '2026-06-21 22:20:04');
INSERT INTO `like_record` VALUES (17, 1, 11, '2026-06-21 22:20:05');
INSERT INTO `like_record` VALUES (19, 2, 15, '2026-06-21 22:32:24');
INSERT INTO `like_record` VALUES (22, 1, 17, '2026-06-22 00:27:12');

-- ----------------------------
-- Table structure for music
-- ----------------------------
DROP TABLE IF EXISTS `music`;
CREATE TABLE `music`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '歌曲名',
  `singer` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '歌手',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '音乐类型：pure（纯音乐）、pop（流行音乐）',
  `file_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '音乐文件URL',
  `is_active` tinyint NULL DEFAULT 1 COMMENT '是否启用：0-禁用 1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '音乐库表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of music
-- ----------------------------
INSERT INTO `music` VALUES (5, '发如雪', '周杰伦', 'pop', '/uploads/music/2616e18a-b389-4624-9260-d8ba86c5666b.mp3', 1, '2026-06-22 00:17:56', '2026-06-22 00:21:06');
INSERT INTO `music` VALUES (6, '轻松、治愈儿童音乐', '未知', 'pure', '/uploads/music/e5eba2db-9c85-46b4-a411-20d791109d02.mp3', 1, '2026-06-22 00:18:22', '2026-06-22 00:21:06');
INSERT INTO `music` VALUES (7, '安静、钢琴、轻松', '未知', 'pure', '/uploads/music/b6d3c239-4787-4afe-8aa4-87c0bd7d0b45.mp3', 1, '2026-06-22 00:18:39', '2026-06-22 00:21:06');

-- ----------------------------
-- Table structure for notification
-- ----------------------------
DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '接收用户ID',
  `category` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'system' COMMENT '通知分类：system-系统通知，message-消息通知',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知类型',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知标题',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知内容',
  `related_id` bigint NULL DEFAULT NULL COMMENT '关联ID',
  `is_read` tinyint NULL DEFAULT 0 COMMENT '是否已读：0未读 1已读',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_is_read`(`is_read`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户通知表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notification
-- ----------------------------
INSERT INTO `notification` VALUES (1, 1, 'system', 'coin_gift', '心动币到账', '你收到了100心动币，管理员希望你每天保持开心哦(づ￣3￣)づ╭❤～', NULL, 1, '2026-06-21 17:37:20');
INSERT INTO `notification` VALUES (2, 1, 'system', 'coin_gift', '心动币到账', '你收到了100心动币，管理员希望你每天保持开心哦(づ￣3￣)づ╭❤～', NULL, 1, '2026-06-21 17:37:47');
INSERT INTO `notification` VALUES (3, 1, 'system', 'post_delete', '帖子被删除', '你的帖子存在违规行为，请检查后再重新发布哦，也可以进行申诉哦w(ﾟДﾟ)w', 9, 1, '2026-06-21 17:38:39');
INSERT INTO `notification` VALUES (4, 1, 'system', 'appeal_success', '申诉成功', '您的帖子申诉成功了，是我有眼不识泰山，给您带来不便了，希望得到您的谅解┭┮﹏┭┮', 9, 1, '2026-06-21 17:39:34');
INSERT INTO `notification` VALUES (5, 1, 'system', 'post_delete', '帖子被删除', '你的帖子存在违规行为，请检查后再重新发布哦，也可以进行申诉哦w(ﾟДﾟ)w', 9, 1, '2026-06-21 17:40:04');
INSERT INTO `notification` VALUES (6, 1, 'system', 'appeal_fail', '申诉失败', '您的帖子申诉失败了，确实存在违规，没办法恢复，抱歉哈╮(๑•́ ₃•̀๑)╭', 9, 1, '2026-06-21 17:40:29');
INSERT INTO `notification` VALUES (7, 1, 'system', 'order_ship', '订单发货', '您的订单发货成功，慢慢等待美好送达哦｡:.ﾟヽ(｡◕‿◕｡)ﾉﾟ.:｡+ﾟ', 24, 1, '2026-06-21 17:41:59');
INSERT INTO `notification` VALUES (8, 1, 'system', 'refund_success', '退款成功', '您的订单退款成功了', 24, 1, '2026-06-21 17:43:37');
INSERT INTO `notification` VALUES (9, 1, 'system', 'order_ship', '订单发货', '您的订单发货成功，慢慢等待美好送达哦｡:.ﾟヽ(｡◕‿◕｡)ﾉﾟ.:｡+ﾟ', 25, 1, '2026-06-21 17:44:20');
INSERT INTO `notification` VALUES (10, 1, 'system', 'refund_fail', '退款失败', '您的订单退款失败', 25, 1, '2026-06-21 17:45:01');
INSERT INTO `notification` VALUES (11, 1, 'message', 'post_comment', '帖子收到评论', '有位帅哥或美女给你的帖子评论了，快去看看', 8, 1, '2026-06-21 17:49:51');
INSERT INTO `notification` VALUES (12, 1, 'message', 'post_like', '帖子被点赞', '有位帅哥或美女给你的帖子点赞了，快去看看', 8, 1, '2026-06-21 17:49:53');
INSERT INTO `notification` VALUES (13, 1, 'system', 'order_ship', '订单发货', '您的订单发货成功，慢慢等待美好送达哦｡:.ﾟヽ(｡◕‿◕｡)ﾉﾟ.:｡+ﾟ', 26, 1, '2026-06-21 18:34:41');
INSERT INTO `notification` VALUES (14, 1, 'message', 'post_like', '帖子被点赞', '有位帅哥或美女给你的帖子点赞了，快去看看', 10, 1, '2026-06-21 18:46:29');
INSERT INTO `notification` VALUES (15, 1, 'system', 'order_ship', '订单发货', '您的订单发货成功，慢慢等待美好送达哦｡:.ﾟヽ(｡◕‿◕｡)ﾉﾟ.:｡+ﾟ', 27, 1, '2026-06-21 22:18:50');
INSERT INTO `notification` VALUES (16, 6, 'message', 'post_like', '帖子被点赞', '有位帅哥或美女给你的帖子点赞了，快去看看', 11, 0, '2026-06-21 22:20:05');
INSERT INTO `notification` VALUES (17, 6, 'message', 'post_comment', '帖子收到评论', '有位帅哥或美女给你的帖子评论了，快去看看', 11, 0, '2026-06-21 22:20:11');
INSERT INTO `notification` VALUES (18, 2, 'system', 'order_ship', '订单发货', '您的订单发货成功，慢慢等待美好送达哦｡:.ﾟヽ(｡◕‿◕｡)ﾉﾟ.:｡+ﾟ', 28, 1, '2026-06-21 22:28:21');
INSERT INTO `notification` VALUES (19, 2, 'system', 'order_ship', '订单发货', '您的订单发货成功，慢慢等待美好送达哦｡:.ﾟヽ(｡◕‿◕｡)ﾉﾟ.:｡+ﾟ', 29, 1, '2026-06-21 22:31:10');
INSERT INTO `notification` VALUES (20, 1, 'message', 'post_comment', '帖子收到评论', '有位帅哥或美女给你的帖子评论了，快去看看', 15, 1, '2026-06-21 22:32:20');
INSERT INTO `notification` VALUES (21, 1, 'message', 'post_like', '帖子被点赞', '有位帅哥或美女给你的帖子点赞了，快去看看', 15, 1, '2026-06-21 22:32:21');
INSERT INTO `notification` VALUES (22, 1, 'message', 'post_like', '帖子被点赞', '有位帅哥或美女给你的帖子点赞了，快去看看', 15, 1, '2026-06-21 22:32:24');
INSERT INTO `notification` VALUES (23, 1, 'message', 'user_hug', '收到抱抱', '周杰伦抱抱你，并对你说：今天的你也是独一无二的，记得好好爱自己哦~', 1, 1, '2026-06-21 23:04:46');
INSERT INTO `notification` VALUES (24, 1, 'message', 'user_message', '收到留言', '周杰伦给你留言了：j加油', 2, 1, '2026-06-21 23:04:54');
INSERT INTO `notification` VALUES (25, 1, 'system', 'order_ship', '订单发货', '您的订单发货成功，慢慢等待美好送达哦｡:.ﾟヽ(｡◕‿◕｡)ﾉﾟ.:｡+ﾟ', 31, 1, '2026-06-22 00:25:54');
INSERT INTO `notification` VALUES (26, 1, 'system', 'refund_success', '退款成功', '您的订单退款成功了', 31, 1, '2026-06-22 00:26:29');
INSERT INTO `notification` VALUES (27, 2, 'message', 'post_like', '帖子被点赞', '有位帅哥或美女给你的帖子点赞了，快去看看', 17, 1, '2026-06-22 00:27:12');
INSERT INTO `notification` VALUES (28, 2, 'message', 'post_comment', '帖子收到评论', '有位帅哥或美女给你的帖子评论了，快去看看', 17, 1, '2026-06-22 00:27:17');
INSERT INTO `notification` VALUES (29, 2, 'system', 'order_ship', '订单发货', '您的订单发货成功，慢慢等待美好送达哦｡:.ﾟヽ(｡◕‿◕｡)ﾉﾟ.:｡+ﾟ', 32, 1, '2026-06-22 00:29:50');

-- ----------------------------
-- Table structure for operation_log
-- ----------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `admin_id` bigint NOT NULL COMMENT '操作管理员ID',
  `admin_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '管理员昵称',
  `operation_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作类型 add添加/update更新/delete删除/ban封禁/unban解封/gift赠送等',
  `target_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目标类型 user用户/post帖子/product商品/order订单/banner轮播图/comment评论等',
  `target_id` bigint NULL DEFAULT NULL COMMENT '目标ID',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作描述',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作IP地址',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_admin_id`(`admin_id`) USING BTREE,
  INDEX `idx_target`(`target_type`, `target_id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of operation_log
-- ----------------------------

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `total_amount` int NOT NULL COMMENT '心动币总价',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'pending' COMMENT '状态: pending待发货/shipped已发货/refunded已退款',
  `address_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人姓名',
  `address_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系电话',
  `address_detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货地址',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `shipped_time` datetime NULL DEFAULT NULL COMMENT '发货时间',
  `refunded_time` datetime NULL DEFAULT NULL COMMENT '退款时间',
  `express_company` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '快递公司名称',
  `express_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '快递单号',
  `express_courier` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '快递员/派送人',
  `express_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '快递状态: delivering派送中 signed已签收',
  `sender_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发货人姓名',
  `sender_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发货人地址',
  `sender_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发货人电话',
  `received_time` datetime NULL DEFAULT NULL COMMENT '签收时间',
  `confirmed_time` datetime NULL DEFAULT NULL COMMENT '用户确认收货时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_no`(`order_no`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  CONSTRAINT `order_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES (2, '17803886401896701', 1, 49, 'refunded', '海阔天空', '', '', '2026-06-02 16:24:00', NULL, '2026-06-02 16:37:13', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES (3, '17803894210567713', 1, 35, 'refunded', '许', '12345678223', '北京市北京市东城区111号路', '2026-06-02 16:37:01', NULL, '2026-06-02 16:38:33', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES (4, '17803908690302035', 1, 39, 'refunded', '许', '12345678223', '北京市北京市东城区111号路', '2026-06-02 17:01:09', NULL, '2026-06-02 17:03:16', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES (5, '17803915296552961', 1, 39, 'refunded', '许', '12345678223', '北京市北京市东城区111号路', '2026-06-02 17:12:09', NULL, '2026-06-02 17:13:04', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES (6, '17803916432917024', 1, 35, 'cancelled', '许', '12345678223', '北京市北京市东城区111号路', '2026-06-02 17:14:03', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES (7, '17816008045893655', 1, 39, 'cancelled', '许', '12345678223', '北京市北京市东城区111号路', '2026-06-16 17:06:44', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES (8, '17816227240366964', 1, 49, 'completed', '许', '12345678223', '北京市北京市东城区111号路', '2026-06-16 23:12:04', '2026-06-16 23:14:44', NULL, '顺丰快递', 'SF12345678901', '小明', 'signed', NULL, NULL, NULL, '2026-06-16 23:16:18', '2026-06-16 23:16:18');
INSERT INTO `order` VALUES (9, '17816246770921350', 1, 35, 'pending', '许', '12345678223', '北京市北京市东城区111号路', '2026-06-16 23:44:37', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES (10, '17816246909533208', 1, 49, 'shipped', '许', '12345678223', '北京市北京市东城区111号路', '2026-06-16 23:44:50', '2026-06-16 23:52:49', NULL, '顺丰快递', 'SF12345678901', '小黄', 'signed', NULL, NULL, NULL, '2026-06-17 19:32:27', NULL);
INSERT INTO `order` VALUES (11, '17816302260795933', 1, 39, 'pending', '许', '12345678223', '北京市北京市东城区111号路', '2026-06-17 01:17:06', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES (12, '17816303232516290', 1, 35, 'refunded', '许', '12345678223', '北京市北京市东城区111号路', '2026-06-17 01:18:43', '2026-06-17 01:21:18', '2026-06-17 01:27:59', '顺丰快递', 'SF12345678901', '小刚', 'delivering', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES (13, '17816704793888395', 1, 35, 'refunded', '许', '12345678223', '北京市北京市东城区111号路', '2026-06-17 12:27:59', '2026-06-17 12:30:26', '2026-06-17 12:31:34', '顺丰快递', 'SF12345678901', '小李', 'delivering', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES (14, '17818508534265437', 1, 39, 'shipped', '许', '12345678223', '北京市北京市东城区111号路', '2026-06-19 14:34:13', '2026-06-21 14:30:56', NULL, '顺丰速运', 'SF1234567894', NULL, 'delivering', '海', '广东省广州市从化区', '123456786990', NULL, NULL);
INSERT INTO `order` VALUES (15, '17818775483632750', 1, 6800, 'shipped', '许', '12345678223', '北京市北京市东城区111号路', '2026-06-19 21:59:08', '2026-06-19 22:01:27', NULL, '邮政速递', 'SF12345678902', '小华', 'delivering', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES (16, '17820204658069198', 1, 189, 'completed', '许', '12345678223', '北京市北京市东城区111号路', '2026-06-21 13:41:05', '2026-06-21 13:41:51', NULL, '顺丰公司', 'SF12345678901', '小黑', 'signed', NULL, NULL, NULL, '2026-06-22 00:28:27', '2026-06-22 00:28:27');
INSERT INTO `order` VALUES (17, '17820235672772871', 1, 520, 'completed', '许', '12345678223', '北京市北京市东城区111号路', '2026-06-21 14:32:47', '2026-06-21 14:33:40', NULL, '顺丰速运', 'SF12356748902', NULL, 'signed', '海', '广东省广州市从化区', '123456786990', '2026-06-22 00:28:15', '2026-06-22 00:28:15');
INSERT INTO `order` VALUES (18, '17820271309098143', 1, 520, 'cancelled', '许', '12345678223', '北京市北京市东城区111号路', '2026-06-21 15:32:10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES (19, '17820271488626824', 1, 520, 'completed', '许', '12345678223', '北京市北京市东城区111号路', '2026-06-21 15:32:28', '2026-06-21 15:35:09', NULL, '顺丰速运', 'SF12345678901', NULL, 'signed', '海', '广东省广州市从化区', '123456786990', '2026-06-21 15:43:37', '2026-06-21 15:43:37');
INSERT INTO `order` VALUES (20, '17820285309356710', 1, 68, 'pending', '许', '12345678223', '北京市北京市东城区111号路', '2026-06-21 15:55:30', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES (21, '17820288653972105', 1, 189, 'pending', '许', '12345678223', '北京市北京市东城区111号路', '2026-06-21 16:01:05', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES (22, '17820289469776916', 1, 520, 'pending', '李', '14673986543', '北京市北京市东城区11号', '2026-06-21 16:02:26', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES (23, '17820348772486690', 1, 160, 'cancelled', '李', '14673986543', '北京市北京市东城区11号', '2026-06-21 17:41:17', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES (24, '17820348981260312', 1, 160, 'refunded', '李', '14673986543', '北京市北京市东城区11号', '2026-06-21 17:41:38', '2026-06-21 17:41:59', '2026-06-21 17:43:37', '顺丰速运', 'SF1234567897', NULL, 'delivering', '许', '广东省广州市从化区', '123456786990', NULL, NULL);
INSERT INTO `order` VALUES (25, '17820350375832792', 1, 68, 'completed', '李', '14673986543', '北京市北京市东城区11号', '2026-06-21 17:43:57', '2026-06-21 17:44:20', NULL, '顺丰速运', 'SF12345678905', NULL, 'signed', '许', '广东省广州市从化区', '123456786990', '2026-06-22 00:28:08', '2026-06-22 00:28:08');
INSERT INTO `order` VALUES (26, '17820379730195171', 1, 68, 'completed', '许', '12345678223', '北京市北京市东城区111号路', '2026-06-21 18:32:53', '2026-06-21 18:34:41', NULL, '顺丰速运', 'SF12345678906', NULL, 'signed', '许', '广东省广州市从化区', '123456786990', '2026-06-21 18:36:00', '2026-06-21 18:36:00');
INSERT INTO `order` VALUES (27, '17820514854021195', 1, 68, 'completed', '许', '12345678223', '北京市北京市东城区111号路', '2026-06-21 22:18:05', '2026-06-21 22:18:50', NULL, '顺丰速运', 'SF12345678909', NULL, 'signed', '许', '广东省广州市从化区', '123456786990', '2026-06-21 22:19:26', '2026-06-21 22:19:26');
INSERT INTO `order` VALUES (28, '17820520749103695', 2, 68, 'completed', '周杰伦', '14567766445', '台湾省台北市中正区11111111111', '2026-06-21 22:27:54', '2026-06-21 22:28:21', NULL, '顺丰速运', 'SF12345678908', NULL, 'signed', '许', '广东省广州市从化区', '123456786990', '2026-06-21 22:28:49', '2026-06-21 22:28:49');
INSERT INTO `order` VALUES (29, '17820522493764703', 2, 68, 'completed', '周杰伦', '14567766445', '台湾省台北市中正区11111111111', '2026-06-21 22:30:49', '2026-06-21 22:31:10', NULL, '顺丰速运', 'SF12345678908', NULL, 'signed', '许', '广东省广州市从化区', '123456786990', '2026-06-21 22:31:34', '2026-06-21 22:31:34');
INSERT INTO `order` VALUES (30, '17820590905108131', 1, 68, 'pending', '许', '12345678223', '北京市北京市东城区111号路', '2026-06-22 00:24:50', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `order` VALUES (31, '17820591059553533', 1, 68, 'refunded', '许', '12345678223', '北京市北京市东城区111号路', '2026-06-22 00:25:05', '2026-06-22 00:25:54', '2026-06-22 00:26:29', '顺丰速运', 'SF12345678909', NULL, 'delivering', 'XU', '广东省广州市从化区', '123456786990', NULL, NULL);
INSERT INTO `order` VALUES (32, '17820593694137843', 2, 68, 'completed', '周杰伦', '14567766445', '台湾省台北市中正区11111111111', '2026-06-22 00:29:29', '2026-06-22 00:29:50', NULL, '顺丰速运', 'SF12345678908', NULL, 'signed', 'XU', '广东省广州市从化区', '123456786990', '2026-06-22 00:30:14', '2026-06-22 00:30:14');

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单商品ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `product_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片',
  `price` int NOT NULL COMMENT '下单时单价（心动币）',
  `quantity` int NOT NULL COMMENT '数量',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `selected_spec` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '选中的规格',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  INDEX `product_id`(`product_id`) USING BTREE,
  CONSTRAINT `order_item_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `order_item_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单商品关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_item
-- ----------------------------
INSERT INTO `order_item` VALUES (2, 2, 3, '情绪日记本', 'https://example.com/diary.jpg', 49, 1, '2026-06-02 16:24:00', NULL);
INSERT INTO `order_item` VALUES (3, 3, 4, '暖心治愈书籍《蛤蟆先生去看心理医生》', 'https://example.com/book.jpg', 35, 1, '2026-06-02 16:37:01', NULL);
INSERT INTO `order_item` VALUES (4, 4, 1, '治愈系香薰蜡烛', 'https://example.com/candle.jpg', 39, 1, '2026-06-02 17:01:09', NULL);
INSERT INTO `order_item` VALUES (5, 5, 1, '治愈系香薰蜡烛', 'https://example.com/candle.jpg', 39, 1, '2026-06-02 17:12:09', NULL);
INSERT INTO `order_item` VALUES (6, 6, 4, '暖心治愈书籍《蛤蟆先生去看心理医生》', 'https://example.com/book.jpg', 35, 1, '2026-06-02 17:14:03', NULL);
INSERT INTO `order_item` VALUES (7, 7, 1, '治愈系香薰蜡烛', 'https://example.com/candle.jpg', 39, 1, '2026-06-16 17:06:44', NULL);
INSERT INTO `order_item` VALUES (8, 8, 3, '情绪日记本', 'https://example.com/diary.jpg', 49, 1, '2026-06-16 23:12:04', NULL);
INSERT INTO `order_item` VALUES (9, 9, 4, '暖心治愈书籍《蛤蟆先生去看心理医生》', 'https://example.com/book.jpg', 35, 1, '2026-06-16 23:44:37', NULL);
INSERT INTO `order_item` VALUES (10, 10, 3, '情绪日记本', 'https://example.com/diary.jpg', 49, 1, '2026-06-16 23:44:50', NULL);
INSERT INTO `order_item` VALUES (11, 11, 1, '治愈系香薰蜡烛', 'https://example.com/candle.jpg', 39, 1, '2026-06-17 01:17:06', NULL);
INSERT INTO `order_item` VALUES (12, 12, 4, '暖心治愈书籍《蛤蟆先生去看心理医生》', 'https://example.com/book.jpg', 35, 1, '2026-06-17 01:18:43', NULL);
INSERT INTO `order_item` VALUES (13, 13, 4, '暖心治愈书籍《蛤蟆先生去看心理医生》', 'https://example.com/book.jpg', 35, 1, '2026-06-17 12:27:59', NULL);
INSERT INTO `order_item` VALUES (14, 14, 1, '治愈系香薰蜡烛', 'https://example.com/candle.jpg', 39, 1, '2026-06-19 14:34:13', NULL);
INSERT INTO `order_item` VALUES (15, 15, 5, '解压魔方', 'http://localhost:8081/uploads/product/eda426c5-a368-4f8d-a4f2-02a27a8d315f.png', 68, 100, '2026-06-19 21:59:08', '黑白');
INSERT INTO `order_item` VALUES (16, 16, 4, '暖心治愈书籍《蛤蟆先生去看心理医生》', 'http://localhost:8081/uploads/product/863f75b5-a321-4ab4-b04f-113885a85fef.png', 189, 1, '2026-06-21 13:41:05', NULL);
INSERT INTO `order_item` VALUES (17, 17, 1, '星落冰川香薰蜡烛', 'http://localhost:8081/uploads/product/e1fb04f7-64b6-4db4-82c6-fec3b0b16941.png', 520, 1, '2026-06-21 14:32:47', NULL);
INSERT INTO `order_item` VALUES (18, 18, 1, '星落冰川香薰蜡烛', 'http://localhost:8081/uploads/product/e1fb04f7-64b6-4db4-82c6-fec3b0b16941.png', 520, 1, '2026-06-21 15:32:10', NULL);
INSERT INTO `order_item` VALUES (19, 19, 1, '星落冰川香薰蜡烛', 'http://localhost:8081/uploads/product/e1fb04f7-64b6-4db4-82c6-fec3b0b16941.png', 520, 1, '2026-06-21 15:32:28', NULL);
INSERT INTO `order_item` VALUES (20, 20, 5, '解压魔方', 'http://localhost:8081/uploads/product/eda426c5-a368-4f8d-a4f2-02a27a8d315f.png', 68, 1, '2026-06-21 15:55:30', '黑白');
INSERT INTO `order_item` VALUES (21, 21, 4, '暖心治愈书籍《蛤蟆先生去看心理医生》', 'http://localhost:8081/uploads/product/863f75b5-a321-4ab4-b04f-113885a85fef.png', 189, 1, '2026-06-21 16:01:05', '默认');
INSERT INTO `order_item` VALUES (22, 22, 1, '星落冰川香薰蜡烛', 'http://localhost:8081/uploads/product/e1fb04f7-64b6-4db4-82c6-fec3b0b16941.png', 520, 1, '2026-06-21 16:02:26', NULL);
INSERT INTO `order_item` VALUES (23, 23, 3, '情绪日记本', 'http://localhost:8081/uploads/product/bc151cc8-bbad-4ff7-aa62-daf2d1e75143.png', 160, 1, '2026-06-21 17:41:17', NULL);
INSERT INTO `order_item` VALUES (24, 24, 3, '情绪日记本', 'http://localhost:8081/uploads/product/bc151cc8-bbad-4ff7-aa62-daf2d1e75143.png', 160, 1, '2026-06-21 17:41:38', NULL);
INSERT INTO `order_item` VALUES (25, 25, 5, '解压魔方', 'http://localhost:8081/uploads/product/eda426c5-a368-4f8d-a4f2-02a27a8d315f.png', 68, 1, '2026-06-21 17:43:57', NULL);
INSERT INTO `order_item` VALUES (26, 26, 5, '解压魔方', 'http://localhost:8081/uploads/product/eda426c5-a368-4f8d-a4f2-02a27a8d315f.png', 68, 1, '2026-06-21 18:32:53', NULL);
INSERT INTO `order_item` VALUES (27, 27, 5, '解压魔方', 'http://localhost:8081/uploads/product/eda426c5-a368-4f8d-a4f2-02a27a8d315f.png', 68, 1, '2026-06-21 22:18:05', NULL);
INSERT INTO `order_item` VALUES (28, 28, 5, '解压魔方', 'http://localhost:8081/uploads/product/eda426c5-a368-4f8d-a4f2-02a27a8d315f.png', 68, 1, '2026-06-21 22:27:54', NULL);
INSERT INTO `order_item` VALUES (29, 29, 5, '解压魔方', 'http://localhost:8081/uploads/product/eda426c5-a368-4f8d-a4f2-02a27a8d315f.png', 68, 1, '2026-06-21 22:30:49', NULL);
INSERT INTO `order_item` VALUES (30, 30, 5, '解压魔方', 'http://localhost:8081/uploads/product/eda426c5-a368-4f8d-a4f2-02a27a8d315f.png', 68, 1, '2026-06-22 00:24:50', NULL);
INSERT INTO `order_item` VALUES (31, 31, 5, '解压魔方', 'http://localhost:8081/uploads/product/eda426c5-a368-4f8d-a4f2-02a27a8d315f.png', 68, 1, '2026-06-22 00:25:05', '黑红');
INSERT INTO `order_item` VALUES (32, 32, 5, '解压魔方', 'http://localhost:8081/uploads/product/eda426c5-a368-4f8d-a4f2-02a27a8d315f.png', 68, 1, '2026-06-22 00:29:29', '黑绿');

-- ----------------------------
-- Table structure for order_refund
-- ----------------------------
DROP TABLE IF EXISTS `order_refund`;
CREATE TABLE `order_refund`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '退款申请ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `user_id` bigint NOT NULL COMMENT '申请用户ID',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单号',
  `total_amount` int NULL DEFAULT 0 COMMENT '订单金额',
  `reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '退款原因',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态: 0待审核 1通过 2不通过',
  `apply_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `review_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `reviewer_id` bigint NULL DEFAULT NULL COMMENT '审核人ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单退款申请表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_refund
-- ----------------------------
INSERT INTO `order_refund` VALUES (1, 1, 1, '17803309273036805', 40, '不想要了', 1, '2026-06-16 23:10:42', '2026-06-16 23:11:14', 1);
INSERT INTO `order_refund` VALUES (2, 10, 1, '17816246909533208', 49, '不喜欢', 2, '2026-06-16 23:54:40', '2026-06-16 23:56:09', 1);
INSERT INTO `order_refund` VALUES (3, 12, 1, '17816303232516290', 35, '不需要了', 1, '2026-06-17 01:27:38', '2026-06-17 01:27:59', 1);
INSERT INTO `order_refund` VALUES (4, 13, 1, '17816704793888395', 35, '不需要了', 1, '2026-06-17 12:31:17', '2026-06-17 12:31:34', 1);
INSERT INTO `order_refund` VALUES (5, 17, 1, '17820235672772871', 520, '无', 2, '2026-06-21 14:58:46', '2026-06-21 14:59:03', 1);
INSERT INTO `order_refund` VALUES (6, 24, 1, '17820348981260312', 160, '无', 1, '2026-06-21 17:43:23', '2026-06-21 17:43:37', 1);
INSERT INTO `order_refund` VALUES (7, 25, 1, '17820350375832792', 68, '1', 2, '2026-06-21 17:44:54', '2026-06-21 17:45:01', 1);
INSERT INTO `order_refund` VALUES (8, 31, 1, '17820591059553533', 68, '1', 1, '2026-06-22 00:26:19', '2026-06-22 00:26:29', 1);

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
  `user_id` bigint NOT NULL COMMENT '作者用户ID',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '帖子标题',
  `emotion` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '情绪类型: happy开心/sad难过/angry生气/calm平静/anxious焦虑',
  `emotion_label` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '情绪标签显示文字',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '帖子内容',
  `images` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片URL列表,逗号分隔',
  `like_count` int NULL DEFAULT 0 COMMENT '点赞数',
  `comment_count` int NULL DEFAULT 0 COMMENT '评论数',
  `source` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'original' COMMENT '来源: original原创/diary日记转发',
  `source_diary_id` bigint NULL DEFAULT NULL COMMENT '来源日记ID(如果是转发)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否已删除: 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '帖子表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post
-- ----------------------------
INSERT INTO `post` VALUES (1, 1, NULL, NULL, NULL, '今天去潮州玩，潮州很漂亮，欢迎来玩', 'http://localhost:8080/uploads/adf37c1d-c507-4dd1-a90d-b7b164c9385b.jpg,http://localhost:8080/uploads/94bca868-f185-4ba3-bca7-577609ecc349.jpg', 1, 1, 'original', NULL, '2026-05-31 16:43:20', '2026-06-21 18:39:01', 0);
INSERT INTO `post` VALUES (2, 1, NULL, NULL, NULL, '今天是周五，明天可以好好休息，规划出行，好开心', '', 2, 1, 'diary', 1, '2026-05-31 17:34:51', '2026-06-16 15:32:00', 1);
INSERT INTO `post` VALUES (4, 1, '周末快乐', 'happy', '开心', '今天是周五，明天可以好好休息，规划出行，好开心', '', 0, 1, 'diary', 1, '2026-06-05 11:32:41', '2026-06-21 13:20:47', 0);
INSERT INTO `post` VALUES (5, 1, '测试', 'happy', '开心', 'llllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', '', 0, 0, 'original', NULL, '2026-06-05 13:00:18', '2026-06-05 13:00:18', 0);
INSERT INTO `post` VALUES (7, 1, '测试', 'happy', '开心', '111111111111111111111111111111111111111111111111', '', 1, 1, 'diary', 9, '2026-06-16 15:38:41', '2026-06-21 13:20:44', 0);
INSERT INTO `post` VALUES (8, 1, '快考试了，好焦虑啊', 'sad', '难过', '还有两周就期末考了，好焦虑啊', '', 1, 1, 'original', NULL, '2026-06-21 13:21:33', '2026-06-21 17:49:53', 0);
INSERT INTO `post` VALUES (9, 1, '测试1111111111111111', 'happy', '开心', '无聊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊', '', 0, 0, 'original', NULL, '2026-06-21 17:38:28', '2026-06-21 17:40:04', 1);
INSERT INTO `post` VALUES (10, 1, '好多作业，不开心', 'angry', '生气', '111111111111111111111111', '', 1, 8, 'diary', 11, '2026-06-21 18:31:48', '2026-06-21 18:46:29', 0);
INSERT INTO `post` VALUES (11, 6, '111111', 'happy', '开心', '111111111', '', 1, 1, 'original', NULL, '2026-06-21 21:08:55', '2026-06-21 22:20:11', 0);
INSERT INTO `post` VALUES (12, 1, '1111', 'happy', '开心', '1111', '', 0, 0, 'original', NULL, '2026-06-21 22:17:39', '2026-06-21 22:17:39', 0);
INSERT INTO `post` VALUES (13, 1, '111', 'happy', '开心', '1111', '', 1, 0, 'original', NULL, '2026-06-21 22:17:48', '2026-06-21 22:20:04', 0);
INSERT INTO `post` VALUES (14, 1, '测试1', 'happy', '开心', '111111', '', 0, 0, 'diary', 17, '2026-06-21 22:20:22', '2026-06-21 22:20:22', 0);
INSERT INTO `post` VALUES (15, 1, '测试1', 'happy', '开心', '111111', '', 1, 1, 'diary', 17, '2026-06-21 22:20:26', '2026-06-21 22:32:24', 0);
INSERT INTO `post` VALUES (16, 2, '测试2', 'happy', '开心', '12344', '', 0, 0, 'diary', 19, '2026-06-21 22:26:59', '2026-06-21 22:26:59', 0);
INSERT INTO `post` VALUES (17, 2, '111', 'happy', '开心', '111', '', 1, 1, 'original', NULL, '2026-06-21 22:27:11', '2026-06-22 00:27:17', 0);
INSERT INTO `post` VALUES (18, 2, '111', 'happy', '开心', '111', '', 0, 0, 'original', NULL, '2026-06-21 22:32:10', '2026-06-21 22:32:10', 0);
INSERT INTO `post` VALUES (19, 1, '测试1', 'happy', '开心', '111111', '', 0, 0, 'diary', 17, '2026-06-22 00:24:05', '2026-06-22 00:27:09', 0);
INSERT INTO `post` VALUES (20, 1, '测试1', 'happy', '开心', '111111', '', 0, 0, 'diary', 17, '2026-06-22 00:24:11', '2026-06-22 00:27:10', 0);

-- ----------------------------
-- Table structure for post_appeal
-- ----------------------------
DROP TABLE IF EXISTS `post_appeal`;
CREATE TABLE `post_appeal`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '申诉ID',
  `user_id` bigint NOT NULL COMMENT '申诉用户ID',
  `post_id` bigint NOT NULL COMMENT '被申诉帖子ID',
  `post_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '帖子标题',
  `post_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '帖子内容',
  `reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '申诉原因',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '申诉状态: 0待审核 1通过 2不通过',
  `appeal_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申诉时间',
  `review_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `reviewer_id` bigint NULL DEFAULT NULL COMMENT '审核人ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_post_id`(`post_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '帖子申诉表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post_appeal
-- ----------------------------
INSERT INTO `post_appeal` VALUES (1, 1, 7, '测试', '111111111111111111111111111111111111111111111111', '为什么删除我的帖子', 1, '2026-06-16 15:52:15', '2026-06-16 15:52:33', 1);
INSERT INTO `post_appeal` VALUES (2, 1, 7, '测试', '111111111111111111111111111111111111111111111111', '为啥删我帖子', 1, '2026-06-16 16:14:39', '2026-06-16 16:14:48', 1);
INSERT INTO `post_appeal` VALUES (3, 1, 6, '测试', '111111111111111111111111111111111111111111111111', '为什么删我帖子', 2, '2026-06-16 16:15:39', '2026-06-16 16:15:49', 1);
INSERT INTO `post_appeal` VALUES (4, 1, 9, '测试1111111111111111', '无聊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊', '为什么删我帖子', 1, '2026-06-21 17:39:21', '2026-06-21 17:39:34', 1);
INSERT INTO `post_appeal` VALUES (5, 1, 9, '测试1111111111111111', '无聊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊', '》》》》》', 2, '2026-06-21 17:40:22', '2026-06-21 17:40:29', 1);

-- ----------------------------
-- Table structure for post_trash
-- ----------------------------
DROP TABLE IF EXISTS `post_trash`;
CREATE TABLE `post_trash`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '回收站记录ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '原帖子ID',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `emotion` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '情绪类型',
  `emotion_label` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '情绪标签',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '内容',
  `images` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片',
  `like_count` int NULL DEFAULT 0 COMMENT '点赞数',
  `comment_count` int NULL DEFAULT 0 COMMENT '评论数',
  `deleted_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'user' COMMENT '删除者: user用户 admin管理员',
  `deleted_by_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '删除者名称',
  `original_create_time` datetime NULL DEFAULT NULL COMMENT '原帖子创建时间',
  `deleted_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '删除时间',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '过期时间（删除后30天）',
  `appeal_status` tinyint(1) NULL DEFAULT NULL COMMENT '申诉审核状态: null未申诉 0申诉中 1通过 2不通过',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_post_id`(`post_id`) USING BTREE,
  INDEX `idx_expire_time`(`expire_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '帖子回收站表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post_trash
-- ----------------------------
INSERT INTO `post_trash` VALUES (8, 1, 9, '测试1111111111111111', 'happy', '开心', '无聊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊', '', 0, 0, 'admin', '系统管理员', '2026-06-21 17:38:28', '2026-06-21 17:40:04', '2026-07-21 17:40:04', 2);

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `cover_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '封面图片URL',
  `images` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片列表,逗号分隔',
  `price` decimal(10, 2) NOT NULL COMMENT '现价',
  `original_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '原价',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商品描述',
  `tags` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签,逗号分隔',
  `taobao_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '淘宝链接',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序权重',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 0-下架 1-上架',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `stock` int NULL DEFAULT 100 COMMENT '库存数量',
  `specs` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '规格标签，逗号分隔',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status_sort`(`status`, `sort_order`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1, '星落冰川香薰蜡烛', 'http://localhost:8081/uploads/product/e1fb04f7-64b6-4db4-82c6-fec3b0b16941.png', 'http://localhost:8081/uploads/product/6554af94-3099-42de-8196-1664f24f79c1.png,http://localhost:8081/uploads/product/dd016317-20ae-4151-bc0b-8ac60e965fd3.png,http://localhost:8081/uploads/product/10e91115-5b27-4be2-a595-bd1b02e85335.png,http://localhost:8081/uploads/product/de9457b5-43d0-4d95-aae2-67b607504a4e.png', 520.00, 59.00, '清新香味，帮助放松入眠', '治愈系,热销', 'https://item.taobao.com/item.htm?id=1', 3, 1, '2026-05-22 11:17:08', '2026-06-21 16:02:26', 497, '微风茉莉味,乌木沉香味');
INSERT INTO `product` VALUES (3, '情绪日记本', 'http://localhost:8081/uploads/product/bc151cc8-bbad-4ff7-aa62-daf2d1e75143.png', 'http://localhost:8081/uploads/product/b95f5870-c3d0-4fa7-8298-4e3a5b1df6d8.png,http://localhost:8081/uploads/product/634bf731-40d4-4527-85a8-c828c07259fe.png,http://localhost:8081/uploads/product/453536f0-1d36-499e-bc27-5938837c42c8.png,http://localhost:8081/uploads/product/3e68e21d-1b35-41d3-9eb6-08b4ca53faca.png', 160.00, 69.00, '精装手账本，记录每天的心情', '文具,治愈系', 'https://item.taobao.com/item.htm?id=3', 4, 1, '2026-05-22 11:17:08', '2026-06-21 17:43:37', 100, '米白+白,蓝白,青白,粉');
INSERT INTO `product` VALUES (4, '暖心治愈书籍《蛤蟆先生去看心理医生》', 'http://localhost:8081/uploads/product/863f75b5-a321-4ab4-b04f-113885a85fef.png', 'http://localhost:8081/uploads/product/2d7745e6-4331-4f0b-b3fa-979be728eb26.png,http://localhost:8081/uploads/product/e49cb949-4fda-4fa4-9fec-bf81f5d3a2f6.png,http://localhost:8081/uploads/product/c0f60eca-2c04-4db4-9426-c878eececa6d.png,http://localhost:8081/uploads/product/3591652c-eca1-4200-90fb-7a2f4f7a4a4e.png', 188.50, 55.00, '心理学入门经典，帮助你了解自己的情绪', '书籍,心理', 'https://item.taobao.com/item.htm?id=4', 1, 1, '2026-05-22 11:17:08', '2026-06-21 16:01:05', 198, '默认');
INSERT INTO `product` VALUES (5, '解压魔方', 'http://localhost:8081/uploads/product/eda426c5-a368-4f8d-a4f2-02a27a8d315f.png', 'http://localhost:8081/uploads/product/f53249f6-53c8-4e9f-9334-d445498120dc.png,http://localhost:8081/uploads/product/41e9ccea-64e3-4f39-ad74-3b55c689fb2c.png,http://localhost:8081/uploads/product/c230fe58-cb83-44b5-b158-ef1668f687fd.png,http://localhost:8081/uploads/product/371e4575-72b4-4999-a21d-756db69eb625.png', 68.00, 49.00, '六面不同解压设计，随身携带', '解压,办公', 'https://item.taobao.com/item.htm?id=2', 1, 1, '2026-05-31 19:48:10', '2026-06-22 00:29:29', 592, '黑白,白玫红,灰黑,黑红,黑绿');

-- ----------------------------
-- Table structure for restock_log
-- ----------------------------
DROP TABLE IF EXISTS `restock_log`;
CREATE TABLE `restock_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品名称',
  `previous_stock` int NULL DEFAULT 0 COMMENT '补货前库存',
  `restock_amount` int NOT NULL COMMENT '补货数量',
  `current_stock` int NULL DEFAULT 0 COMMENT '补货后库存',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '补货备注',
  `admin_id` bigint NOT NULL COMMENT '操作管理员ID',
  `admin_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '管理员昵称',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '补货时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  INDEX `idx_admin_id`(`admin_id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品补货日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of restock_log
-- ----------------------------

-- ----------------------------
-- Table structure for stock_log
-- ----------------------------
DROP TABLE IF EXISTS `stock_log`;
CREATE TABLE `stock_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `change_amount` int NOT NULL COMMENT '变动数量（正数补货，负数扣减）',
  `stock_after` int NOT NULL COMMENT '变动后库存',
  `operator_id` bigint NULL DEFAULT NULL COMMENT '操作管理员ID',
  `reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '原因说明',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品库存变动日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stock_log
-- ----------------------------
INSERT INTO `stock_log` VALUES (1, 1, 100, 200, 1, '管理端补货', '2026-05-31 19:52:55');
INSERT INTO `stock_log` VALUES (2, 5, 100, 100, 1, '管理端补货', '2026-06-19 22:39:12');
INSERT INTO `stock_log` VALUES (3, 5, 500, 600, 1, '管理端补货', '2026-06-19 22:39:21');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `openid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '微信openid',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账号名',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像URL',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `gender` tinyint NULL DEFAULT 0 COMMENT '性别 0-未知 1-男 2-女',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `bio` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '个性签名',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `coin_balance` int NULL DEFAULT 200 COMMENT '心动币余额',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'normal' COMMENT '账号状态: normal正常/banned封禁/deleted删除',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_openid`(`openid`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'account_user1_1780216722575', 'user1', '123456', '海阔天空1', 'http://localhost:8080/uploads/8cd4aa3e-8218-4d48-943b-5a429d155ea7.jpg', '16789056789', 1, '2004-06-21', '我是一个可爱的大学生', '2026-05-31 16:38:42', '2026-06-22 00:28:27', 31394, 'normal', NULL);
INSERT INTO `user` VALUES (2, 'account_user2_1780220246926', 'user2', '123456', '周杰伦', 'http://localhost:8080/uploads/bb26bb8a-9e6a-4c41-9412-3a6a8f1b86a6.jpg', '', 0, NULL, '', '2026-05-31 17:37:26', '2026-06-22 00:30:14', 128, 'normal', NULL);
INSERT INTO `user` VALUES (6, 'account_user4_1782047261781', 'user4', '123456', '小红', 'http://localhost:8080/uploads/dd7eacf7-74e1-4676-b27f-54c758649fa9.png', '14567845686', 2, NULL, NULL, '2026-06-21 21:07:41', '2026-06-21 21:08:56', 206, 'normal', NULL);

-- ----------------------------
-- Table structure for user_message
-- ----------------------------
DROP TABLE IF EXISTS `user_message`;
CREATE TABLE `user_message`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `from_user_id` bigint NOT NULL COMMENT '发送者用户ID',
  `from_nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发送者昵称',
  `from_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发送者头像',
  `to_user_id` bigint NOT NULL COMMENT '接收者用户ID',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '留言内容',
  `is_read` tinyint NULL DEFAULT 0 COMMENT '是否已读：0未读 1已读',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_from_user_id`(`from_user_id`) USING BTREE,
  INDEX `idx_to_user_id`(`to_user_id`) USING BTREE,
  INDEX `idx_is_read`(`is_read`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户留言表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_message
-- ----------------------------
INSERT INTO `user_message` VALUES (1, 2, '周杰伦', 'http://localhost:8080/uploads/bb26bb8a-9e6a-4c41-9412-3a6a8f1b86a6.jpg', 1, '[抱抱] 今天的你也是独一无二的，记得好好爱自己哦~', 1, '2026-06-21 23:04:46');
INSERT INTO `user_message` VALUES (2, 2, '周杰伦', 'http://localhost:8080/uploads/bb26bb8a-9e6a-4c41-9412-3a6a8f1b86a6.jpg', 1, 'j加油', 1, '2026-06-21 23:04:54');

-- ----------------------------
-- View structure for v_order_stats_7days
-- ----------------------------
DROP VIEW IF EXISTS `v_order_stats_7days`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_order_stats_7days` AS select cast(`order`.`create_time` as date) AS `order_date`,count(0) AS `order_count`,sum(`order`.`total_amount`) AS `order_amount` from `order` where ((`order`.`create_time` >= (curdate() - interval 7 day)) and (`order`.`status` in ('unpaid','shipped','completed'))) group by cast(`order`.`create_time` as date) order by `order_date`;

-- ----------------------------
-- View structure for v_product_ranking
-- ----------------------------
DROP VIEW IF EXISTS `v_product_ranking`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_product_ranking` AS select `p`.`id` AS `product_id`,`p`.`name` AS `product_name`,coalesce(sum(`oi`.`quantity`),0) AS `sales_count`,coalesce(sum((`oi`.`price` * `oi`.`quantity`)),0) AS `sales_amount` from ((`product` `p` left join `order_item` `oi` on((`p`.`id` = `oi`.`product_id`))) left join `order` `o` on(((`oi`.`order_id` = `o`.`id`) and (`o`.`create_time` >= (curdate() - interval 7 day)) and (`o`.`status` in ('unpaid','shipped','completed'))))) group by `p`.`id`,`p`.`name` order by `sales_count` desc;

SET FOREIGN_KEY_CHECKS = 1;
