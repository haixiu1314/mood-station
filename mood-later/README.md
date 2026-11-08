# 情绪小站后端说明文档

## 一、项目概述

**情绪小站**是一个基于Spring Boot + MyBatis + MySQL开发的微信小程序后端服务，提供情绪记录、情绪分享社区、情绪周边商品导购等功能。

### 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.7.18 | 核心框架 |
| MyBatis | 2.3.1 | ORM框架 |
| MySQL | 8.0+ | 关系型数据库 |
| JWT (jjwt) | 0.9.1 | 用户认证 |
| Lombok | - | 简化实体类代码 |

### 项目结构

```
mood-later/
├── pom.xml                          # Maven依赖配置
├── sql/
│   └── init.sql                     # 数据库初始化脚本
├── src/
│   ├── main/
│   │   ├── java/com/moodlater/
│   │   │   ├── MoodLaterApplication.java    # 启动类
│   │   │   ├── common/
│   │   │   │   └── Result.java              # 统一返回结果类
│   │   │   ├── config/
│   │   │   │   └── WebMvcConfig.java        # Web配置（拦截器、跨域）
│   │   │   ├── controller/
│   │   │   │   ├── UserController.java      # 用户接口
│   │   │   │   ├── DiaryController.java     # 日记接口
│   │   │   │   ├── PostController.java      # 帖子接口
│   │   │   │   ├── ProductController.java   # 商品接口
│   │   │   │   ├── HomeController.java      # 首页接口
│   │   │   │   ├── CartController.java      # 购物车接口
│   │   │   │   ├── OrderController.java     # 订单接口
│   │   │   │   └── AddressController.java   # 地址接口
│   │   │   ├── entity/
│   │   │   │   ├── User.java                # 用户实体
│   │   │   │   ├── Diary.java               # 日记实体
│   │   │   │   ├── Post.java                # 帖子实体
│   │   │   │   ├── LikeRecord.java          # 点赞记录实体
│   │   │   │   ├── Comment.java             # 评论实体
│   │   │   │   ├── Product.java             # 商品实体
│   │   │   │   ├── HealingQuote.java        # 治愈语录实体
│   │   │   │   ├── Banner.java              # 轮播图实体
│   │   │   │   ├── Cart.java                # 购物车实体
│   │   │   │   ├── CartVO.java              # 购物车VO（含商品信息）
│   │   │   │   ├── Order.java               # 订单实体
│   │   │   │   ├── OrderItem.java           # 订单明细实体
│   │   │   │   ├── OrderRefund.java         # 退款申请实体
│   │   │   │   ├── OrderVO.java             # 订单VO（含明细）
│   │   │   │   └── Address.java             # 收货地址实体
│   │   │   ├── interceptor/
│   │   │   │   └── JwtInterceptor.java      # JWT认证拦截器
│   │   │   ├── mapper/
│   │   │   │   ├── UserMapper.java          # 用户Mapper接口
│   │   │   │   ├── DiaryMapper.java         # 日记Mapper接口
│   │   │   │   ├── PostMapper.java          # 帖子Mapper接口
│   │   │   │   ├── LikeRecordMapper.java    # 点赞Mapper接口
│   │   │   │   ├── CommentMapper.java       # 评论Mapper接口
│   │   │   │   ├── ProductMapper.java       # 商品Mapper接口
│   │   │   │   ├── HealingQuoteMapper.java  # 语录Mapper接口
│   │   │   │   ├── BannerMapper.java        # 轮播图Mapper接口
│   │   │   │   ├── CartMapper.java          # 购物车Mapper接口
│   │   │   │   ├── OrderMapper.java         # 订单Mapper接口
│   │   │   │   ├── OrderItemMapper.java     # 订单明细Mapper接口
│   │   │   │   ├── OrderRefundMapper.java   # 退款申请Mapper接口
│   │   │   │   └── AddressMapper.java       # 地址Mapper接口
│   │   │   ├── service/
│   │   │   │   ├── UserService.java         # 用户服务
│   │   │   │   ├── DiaryService.java        # 日记服务
│   │   │   │   ├── PostService.java         # 帖子服务
│   │   │   │   ├── ProductService.java      # 商品服务
│   │   │   │   ├── HomeService.java         # 首页服务
│   │   │   │   ├── CartService.java         # 购物车服务
│   │   │   │   ├── OrderService.java        # 订单服务
│   │   │   │   └── AddressService.java      # 地址服务
│   │   │   └── util/
│   │   │       └── JwtUtil.java             # JWT工具类
│   │   └── resources/
│   │       ├── application.yml              # 应用配置文件
│   │       └── mapper/
│   │           ├── UserMapper.xml           # 用户SQL映射
│   │           ├── DiaryMapper.xml          # 日记SQL映射
│   │           ├── PostMapper.xml           # 帖子SQL映射
│   │           ├── LikeRecordMapper.xml     # 点赞SQL映射
│   │           ├── CommentMapper.xml        # 评论SQL映射
│   │           ├── ProductMapper.xml        # 商品SQL映射
│   │           ├── HealingQuoteMapper.xml   # 语录SQL映射
│   │           ├── BannerMapper.xml         # 轮播图SQL映射
│   │           ├── CartMapper.xml           # 购物车SQL映射
│   │           ├── OrderMapper.xml          # 订单SQL映射
│   │           ├── OrderItemMapper.xml      # 订单明细SQL映射
│   │           ├── OrderRefundMapper.xml    # 退款申请SQL映射
│   │           └── AddressMapper.xml        # 地址SQL映射
```

---

## 二、数据库设计

### 2.1 数据库连接信息

| 配置项 | 值 |
|--------|-----|
| 数据库名 | mood |
| 主机 | localhost |
| 端口 | 3306 |
| 用户名 | root |
| 密码 | 123456 |

### 2.2 数据表清单

| 表名 | 说明 | 主要字段 |
|------|------|----------|
| `user` | 用户表 | id, openid, nickname, avatar_url, phone, gender, coin_balance |
| `diary` | 日记表 | id, user_id, emotion, emotion_label, title, content, images, is_public, deleted_by, appeal_status |
| `post` | 帖子表 | id, user_id, content, images, like_count, comment_count, source, deleted_by, appeal_status |
| `like_record` | 点赞记录表 | id, user_id, post_id |
| `comment` | 评论表 | id, post_id, user_id, content |
| `product` | 商品表 | id, name, cover_image, price, original_price, description, tags, taobao_url, stock |
| `healing_quote` | 治愈语录表 | id, content, author, category, is_active |
| `banner` | 轮播图表 | id, title, image_url, link_url, sort_order, is_active |
| `cart` | 购物车表 | id, user_id, product_id, quantity |
| `order` | 订单表 | id, user_id, order_no, total_amount, status, express_company, express_no, express_courier, express_status |
| `order_item` | 订单明细表 | id, order_id, product_id, product_name, product_image, price, quantity |
| `order_refund` | 退款申请表 | id, order_id, user_id, reason, status |
| `address` | 收货地址表 | id, user_id, name, phone, province, city, district, detail, is_default |

### 2.3 初始化

执行 `sql/init.sql` 脚本即可自动创建数据库、数据表及初始数据：

```bash
mysql -u root -p123456 < sql/init.sql
```

---

## 三、API接口文档

### 3.1 通用说明

- **基础URL**: `http://localhost:8080`
- **认证方式**: JWT Token，通过请求头 `Authorization: Bearer <token>` 传递
- **返回格式**: JSON

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### 3.2 用户模块 `/api/user`

#### 登录/注册

```
POST /api/user/login
```

**请求体**:
```json
{
  "code": "微信登录code",
  "nickname": "用户昵称",
  "avatarUrl": "头像URL"
}
```

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "JWT Token",
    "user": {
      "id": 1,
      "openid": "xxx",
      "nickname": "用户昵称",
      "avatarUrl": "头像URL",
      "gender": 0
    }
  }
}
```

#### 获取用户信息

```
GET /api/user/info
```

**请求头**: `Authorization: Bearer <token>`

**响应**: 返回当前用户信息

#### 更新用户信息

```
PUT /api/user/info
```

**请求头**: `Authorization: Bearer <token>`

**请求体**:
```json
{
  "nickname": "新昵称",
  "avatarUrl": "新头像URL",
  "gender": 1
}
```

---

### 3.3 日记模块 `/api/diary`

#### 创建日记

```
POST /api/diary
```

**请求头**: `Authorization: Bearer <token>`

**请求体**:
```json
{
  "emotion": "happy",
  "emotionLabel": "开心",
  "title": "日记标题",
  "content": "日记内容",
  "images": "图片URL1,图片URL2",
  "isPublic": 0
}
```

#### 获取日记详情

```
GET /api/diary/{id}
```

#### 获取我的日记列表

```
GET /api/diary/list
```

#### 获取最近日记（首页展示）

```
GET /api/diary/recent?limit=5
```

#### 更新日记

```
PUT /api/diary/{id}
```

#### 删除日记

```
DELETE /api/diary/{id}
```

---

### 3.4 情绪分享模块 `/api/post`

#### 发布帖子

```
POST /api/post
```

**请求头**: `Authorization: Bearer <token>`

**请求体**:
```json
{
  "content": "帖子内容",
  "images": "图片URL1,图片URL2",
  "source": "original",
  "sourceDiaryId": null
}
```

#### 获取帖子列表

```
GET /api/post/list
```

**响应**: 返回帖子列表，包含作者信息、点赞数、评论数

#### 获取帖子详情

```
GET /api/post/{id}
```

#### 点赞帖子

```
POST /api/post/like/{postId}
```

#### 取消点赞

```
DELETE /api/post/like/{postId}
```

#### 发表评论

```
POST /api/post/comment
```

**请求体**:
```json
{
  "postId": 1,
  "content": "评论内容"
}
```

#### 获取帖子评论列表

```
GET /api/post/comment/{postId}
```

---

### 3.5 商品模块 `/api/product`

#### 获取商品列表

```
GET /api/product/list
```

**响应**: 返回上架商品列表

#### 获取商品详情

```
GET /api/product/{id}
```

---

### 3.6 首页模块 `/api/home`

#### 获取轮播图列表

```
GET /api/home/banners
```

**响应**: 返回启用的轮播图列表

#### 获取今日锦囊（治愈语录）

```
GET /api/home/quote
```

**响应**: 随机返回一条启用的治愈语录

---

### 3.7 购物车模块 `/api/cart` 🆕 新增

#### 加入购物车

```
POST /api/cart/add
```

**请求头**: `Authorization: Bearer <token>`

**请求体**:
```json
{
  "productId": 1,
  "quantity": 2
}
```

#### 获取购物车列表

```
GET /api/cart/list
```

**响应**: 返回购物车商品列表，包含商品详情

#### 更新购物车数量

```
PUT /api/cart/update
```

**请求体**:
```json
{
  "productId": 1,
  "quantity": 5
}
```

#### 移除购物车商品

```
DELETE /api/cart/remove/{productId}
```

#### 清空购物车

```
DELETE /api/cart/clear
```

---

### 3.8 订单模块 `/api/order` 🆕 新增

#### 创建订单

```
POST /api/order/create
```

**请求头**: `Authorization: Bearer <token>`

**请求体**:
```json
{
  "cartIds": [1, 2],
  "addressId": 1
}
```

#### 获取订单列表

```
GET /api/order/list
```

#### 按状态获取订单

```
GET /api/order/list/{status}
```

**状态值**: `pending`（待发货）、`shipped`（已发货）、`completed`（已完成）、`cancelled`（已取消）、`refunded`（已退款）

#### 获取订单详情

```
GET /api/order/{id}
```

#### 取消订单（待发货状态）

```
POST /api/order/cancel/{id}
```

#### 申请退款（已发货状态）

```
POST /api/order/refund/{id}
```

**请求体**:
```json
{
  "reason": "退款原因"
}
```

#### 确认收货

```
POST /api/order/confirm/{id}
```

---

### 3.9 地址模块 `/api/address` 🆕 新增

#### 获取地址列表

```
GET /api/address/list
```

#### 获取默认地址

```
GET /api/address/default
```

#### 添加地址

```
POST /api/address/add
```

**请求体**:
```json
{
  "name": "收货人",
  "phone": "13800138000",
  "province": "广东省",
  "city": "深圳市",
  "district": "南山区",
  "detail": "详细地址",
  "isDefault": 1
}
```

#### 更新地址

```
PUT /api/address/update
```

#### 删除地址

```
DELETE /api/address/delete/{id}
```

#### 设置默认地址

```
PUT /api/address/setDefault/{id}
```

---

## 四、核心功能说明

### 4.1 JWT认证

- 用户通过微信登录code进行登录/注册
- 登录成功后返回JWT Token
- 后续请求需在Header中携带 `Authorization: Bearer <token>`
- 拦截器自动校验Token有效性，未登录返回401

### 4.2 情绪标签

支持的情绪类型及颜色：

| 情绪类型 | 标签 | 背景色 | 文字色 |
|----------|------|--------|--------|
| happy | 开心 | #C5F082 | #2D5016 |
| sad | 难过 | #F5A623 | #FFFFFF |
| angry | 生气 | #FF6B6B | #FFFFFF |
| calm | 平静 | #87CEEB | #FFFFFF |
| anxious | 焦虑 | #DDA0DD | #FFFFFF |

### 4.3 帖子来源

- `original`: 原创帖子
- `diary`: 从日记转发

### 4.4 点赞机制

- 同一用户对同一帖子只能点赞一次
- 重复点赞不会重复计数
- 支持取消点赞

### 4.5 订单状态流转  新增

订单状态流转如下：

```
pending(待发货) → shipped(已发货) → signed(已签收) → completed(已完成)
        ↓              ↓
   cancelled(已取消)  refunded(已退款)
```

- **取消订单**：仅待发货状态可取消，取消后心动币退回、库存恢复
- **申请退款**：已发货状态可申请退款，需管理端审核
- **确认收货**：快递状态为已签收且订单未完成时，用户可确认收货
- **成交统计**：仅用户确认收货后，成交单数和成交金额才会增加

### 4.6 快递状态管理 🆕 新增

- 管理端独立页面，与订单管理并列
- 展示所有已发货订单的快递信息
- 可更新快递状态：派送中 → 已签收
- 用户端同步显示快递状态，已签收后可点击确认收货

### 4.7 帖子申诉机制 🆕 新增

- 用户可删除自己的帖子（进入回收站）
- 被系统删除的帖子可申诉
- 管理端审核申诉：通过则帖子恢复，不通过则仅保留彻底删除功能

---

## 五、启动说明

### 5.1 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+

### 5.2 启动步骤

1. **初始化数据库**
   ```bash
   mysql -u root -p123456 < sql/init.sql
   ```

2. **修改配置**（如需）
   编辑 `src/main/resources/application.yml`，确认数据库连接信息正确

3. **编译项目**
   ```bash
   mvn clean package -DskipTests
   ```

4. **启动服务**
   ```bash
   java -jar target/mood-later-1.0.0.jar
   ```

   或使用IDE直接运行 `MoodLaterApplication` 类

5. **验证服务**
   访问 `http://localhost:8080`，服务正常启动即可

---

## 六、注意事项

1. **微信登录**: 当前 `getOpenidByCode` 方法为Mock实现，实际部署时需要调用微信API获取真实openid
2. **图片存储**: 当前设计使用URL字符串存储，建议接入OSS等云存储服务
3. **分页**: 当前帖子列表未实现分页，数据量大时建议添加分页参数
4. **事务**: 点赞和评论操作已添加事务保证数据一致性
5. **安全**: 生产环境请修改JWT密钥和数据库密码
