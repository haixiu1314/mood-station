# 情绪小站 API 接口测试文档

## 基础信息

| 配置项 | 值 |
|--------|-----|
| 基础URL | `http://localhost:8080` |
| 认证方式 | JWT Token（请求头 `Authorization: Bearer <token>`） |
| 数据格式 | JSON |
| 端口 | 8080 |

---

## 统一返回格式

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| code | Integer | 状态码（200成功，401未登录，500错误） |
| message | String | 提示信息 |
| data | Object | 返回数据 |

---

## 一、用户模块 `/api/user`

### 1.1 登录/注册

```
POST http://localhost:8080/api/user/login
```

**请求头**: 无

**请求体**:
```json
{
  "code": "test_code_001",
  "nickname": "测试用户",
  "avatarUrl": "https://example.com/avatar.jpg"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "user": {
      "id": 1,
      "openid": "mock_openid_test_code_001",
      "nickname": "测试用户",
      "avatarUrl": "https://example.com/avatar.jpg",
      "phone": null,
      "gender": 0,
      "createTime": "2026-05-05T10:00:00",
      "updateTime": "2026-05-05T10:00:00"
    }
  }
}
```

> **注意**: 登录成功后需保存返回的 `token`，后续接口需在请求头携带 `Authorization: Bearer <token>`

---

### 1.2 获取用户信息

```
GET http://localhost:8080/api/user/info
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "openid": "mock_openid_test_code_001",
    "nickname": "测试用户",
    "avatarUrl": "https://example.com/avatar.jpg",
    "phone": null,
    "gender": 0,
    "coinBalance": 200,
    "createTime": "2026-05-05T10:00:00",
    "updateTime": "2026-05-05T10:00:00"
  }
}
```

---

### 1.3 更新用户信息

```
PUT http://localhost:8080/api/user/info
```

**请求头**:
```
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体**:
```json
{
  "nickname": "新昵称",
  "avatarUrl": "https://example.com/new-avatar.jpg",
  "gender": 1
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 1.4 获取心动币余额 🆕 新增

```
GET http://localhost:8080/api/user/balance
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": 200
}
```

---

### 1.5 修改昵称 🆕 新增

```
PUT http://localhost:8080/api/user/nickname
```

**请求头**:
```
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体**:
```json
{
  "nickname": "新昵称"
}
```

**响应示例（成功）**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

**响应示例（昵称过长）**:
```json
{
  "code": 500,
  "message": "昵称最多16个字符",
  "data": null
}
```

**响应示例（昵称为空）**:
```json
{
  "code": 500,
  "message": "昵称不能为空",
  "data": null
}
```

---

### 1.6 修改头像 🆕 新增

```
PUT http://localhost:8080/api/user/avatar
```

**请求头**:
```
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体**:
```json
{
  "avatarUrl": "https://example.com/new-avatar.jpg"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

## 二、日记模块 `/api/diary`

### 2.1 创建日记

```
POST http://localhost:8080/api/diary
```

**请求头**:
```
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体**:
```json
{
  "emotion": "happy",
  "emotionLabel": "开心",
  "title": "今天很开心",
  "content": "今天收到了礼物，感觉特别温暖...",
  "images": "https://example.com/img1.jpg,https://example.com/img2.jpg",
  "isPublic": 0
}
```

**情绪类型参考**:

| emotion值 | emotionLabel | 说明 |
|-----------|--------------|------|
| happy | 开心 | 高兴、喜悦 |
| sad | 难过 | 悲伤、失落 |
| angry | 生气 | 愤怒、不满 |
| calm | 平静 | 安宁、平和 |
| anxious | 焦虑 | 担心、紧张 |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "userId": 1,
    "emotion": "happy",
    "emotionLabel": "开心",
    "title": "今天很开心",
    "content": "今天收到了礼物，感觉特别温暖...",
    "images": "https://example.com/img1.jpg,https://example.com/img2.jpg",
    "isPublic": 0,
    "createTime": "2026-05-05T10:30:00",
    "updateTime": "2026-05-05T10:30:00"
  }
}
```

---

### 2.2 获取日记详情

```
GET http://localhost:8080/api/diary/1
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "userId": 1,
    "emotion": "happy",
    "emotionLabel": "开心",
    "title": "今天很开心",
    "content": "今天收到了礼物，感觉特别温暖...",
    "images": "https://example.com/img1.jpg,https://example.com/img2.jpg",
    "isPublic": 0,
    "createTime": "2026-05-05T10:30:00",
    "updateTime": "2026-05-05T10:30:00"
  }
}
```

---

### 2.3 获取我的日记列表

```
GET http://localhost:8080/api/diary/list
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "emotion": "happy",
      "emotionLabel": "开心",
      "title": "今天很开心",
      "content": "今天收到了礼物...",
      "images": null,
      "isPublic": 0,
      "createTime": "2026-05-05T10:30:00",
      "updateTime": "2026-05-05T10:30:00"
    },
    {
      "id": 2,
      "userId": 1,
      "emotion": "sad",
      "emotionLabel": "难过",
      "title": "有点失落",
      "content": "今天下雨了...",
      "images": null,
      "isPublic": 0,
      "createTime": "2026-05-04T15:00:00",
      "updateTime": "2026-05-04T15:00:00"
    }
  ]
}
```

---

### 2.4 获取最近日记（首页展示用）

```
GET http://localhost:8080/api/diary/recent?limit=5
```

**请求头**:
```
Authorization: Bearer <token>
```

**参数说明**:

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| limit | Integer | 否 | 5 | 返回条数 |

**响应示例**: 同日记列表，最多返回limit条

---

### 2.5 更新日记

```
PUT http://localhost:8080/api/diary/1
```

**请求头**:
```
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体**:
```json
{
  "emotion": "calm",
  "emotionLabel": "平静",
  "title": "修改后的标题",
  "content": "修改后的内容",
  "images": null,
  "isPublic": 1
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 2.6 删除日记

```
DELETE http://localhost:8080/api/diary/1
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

## 三、情绪分享模块 `/api/post`

### 3.1 发布帖子

```
POST http://localhost:8080/api/post
```

**请求头**:
```
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体（原创帖子）**:
```json
{
  "content": "今天天气真好，出去散步了~",
  "images": "https://example.com/post1.jpg,https://example.com/post2.jpg",
  "source": "original",
  "sourceDiaryId": null
}
```

**请求体（从日记转发）**:
```json
{
  "content": "今天收到了礼物，很开心",
  "images": null,
  "source": "diary",
  "sourceDiaryId": 1
}
```

**来源类型**:

| source值 | 说明 |
|----------|------|
| original | 原创帖子 |
| diary | 从日记转发 |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "userId": 1,
    "content": "今天天气真好，出去散步了~",
    "images": "https://example.com/post1.jpg",
    "likeCount": 0,
    "commentCount": 0,
    "source": "original",
    "sourceDiaryId": null,
    "createTime": "2026-05-05T11:00:00",
    "updateTime": "2026-05-05T11:00:00"
  }
}
```

---

### 3.2 获取帖子列表

```
GET http://localhost:8080/api/post/list
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "content": "今天天气真好，出去散步了~",
      "images": "https://example.com/post1.jpg",
      "likeCount": 12,
      "commentCount": 3,
      "source": "original",
      "createTime": "2026-05-05T11:00:00",
      "author": {
        "avatar": "https://example.com/avatar.jpg",
        "nickname": "测试用户"
      }
    },
    {
      "id": 2,
      "content": "每一片乌云背后都有阳光",
      "images": null,
      "likeCount": 8,
      "commentCount": 0,
      "source": "original",
      "createTime": "2026-05-04T14:30:00",
      "author": {
        "avatar": "https://example.com/avatar2.jpg",
        "nickname": "用户B"
      }
    }
  ]
}
```

---

### 3.3 获取帖子详情

```
GET http://localhost:8080/api/post/1
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "content": "今天天气真好，出去散步了~",
    "images": "https://example.com/post1.jpg",
    "likeCount": 12,
    "commentCount": 3,
    "source": "original",
    "createTime": "2026-05-05T11:00:00",
    "author": {
      "avatar": "https://example.com/avatar.jpg",
      "nickname": "测试用户"
    }
  }
}
```

---

### 3.4 点赞帖子

```
POST http://localhost:8080/api/post/like/1
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

> **说明**: 同一用户重复点赞不会重复计数

---

### 3.5 取消点赞

```
DELETE http://localhost:8080/api/post/like/1
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 3.6 发表评论

```
POST http://localhost:8080/api/post/comment
```

**请求头**:
```
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体**:
```json
{
  "postId": 1,
  "content": "写得真好！"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 3.7 获取帖子评论列表

```
GET http://localhost:8080/api/post/comment/1
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "content": "写得真好！",
      "createTime": "2026-05-05T11:30:00",
      "user": {
        "avatar": "https://example.com/avatar2.jpg",
        "nickname": "用户B"
      }
    },
    {
      "id": 2,
      "content": "同感~",
      "createTime": "2026-05-05T12:00:00",
      "user": {
        "avatar": "https://example.com/avatar3.jpg",
        "nickname": "用户C"
      }
    }
  ]
}
```

---

## 四、商品模块 `/api/product`

### 4.1 获取商品列表

```
GET http://localhost:8080/api/product/list
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "治愈系香薰蜡烛",
      "coverImage": "https://example.com/candle.jpg",
      "images": null,
      "price": 39.90,
      "originalPrice": 59.00,
      "description": "薰衣草香味，帮助放松入眠",
      "tags": "治愈系,热销",
      "taobaoUrl": "https://item.taobao.com/item.htm?id=1",
      "sortOrder": 0,
      "status": 1,
      "stock": 100,
      "createTime": "2026-05-05T09:00:00",
      "updateTime": "2026-05-05T09:00:00"
    },
    {
      "id": 2,
      "name": "解压魔方",
      "coverImage": "https://example.com/cube.jpg",
      "images": null,
      "price": 29.90,
      "originalPrice": 49.00,
      "description": "六面不同解压设计，随身携带",
      "tags": "解压,办公",
      "taobaoUrl": "https://item.taobao.com/item.htm?id=2",
      "sortOrder": 0,
      "status": 1,
      "stock": 50,
      "createTime": "2026-05-05T09:00:00",
      "updateTime": "2026-05-05T09:00:00"
    }
  ]
}
```

---

### 4.2 获取商品详情

```
GET http://localhost:8080/api/product/1
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "治愈系香薰蜡烛",
    "coverImage": "https://example.com/candle.jpg",
    "images": "https://example.com/candle1.jpg,https://example.com/candle2.jpg",
    "price": 39.90,
    "originalPrice": 59.00,
    "description": "薰衣草香味，帮助放松入眠\n\n产品特点：\n1. 天然植物蜡\n2. 精油调配\n3. 燃烧时间约40小时",
    "tags": "治愈系,热销",
    "taobaoUrl": "https://item.taobao.com/item.htm?id=1",
    "sortOrder": 0,
    "status": 1,
    "stock": 100,
    "createTime": "2026-05-05T09:00:00",
    "updateTime": "2026-05-05T09:00:00"
  }
}
```

---

### 4.3 获取商品库存 🆕 新增

```
GET http://localhost:8080/api/product/1/stock
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例（成功）**:
```json
{
  "code": 200,
  "message": "success",
  "data": 100
}
```

**响应示例（商品不存在）**:
```json
{
  "code": 500,
  "message": "商品不存在",
  "data": null
}
```

---

## 五、购物车模块 `/api/cart` 🆕 新增

### 5.1 加入购物车

```
POST http://localhost:8080/api/cart/add
```

**请求头**:
```
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体**:
```json
{
  "productId": 1,
  "quantity": 2
}
```

**响应示例（成功）**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

**响应示例（商品不存在）**:
```json
{
  "code": 500,
  "message": "商品不存在",
  "data": null
}
```

**响应示例（数量无效）**:
```json
{
  "code": 500,
  "message": "数量必须大于0",
  "data": null
}
```

---

### 5.2 获取购物车列表

```
GET http://localhost:8080/api/cart/list
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "productId": 1,
      "quantity": 2,
      "product": {
        "id": 1,
        "name": "治愈系香薰蜡烛",
        "coverImage": "https://example.com/candle.jpg",
        "price": 39.90,
        "stock": 100
      },
      "createTime": "2026-05-05T12:00:00",
      "updateTime": "2026-05-05T12:00:00"
    },
    {
      "id": 2,
      "userId": 1,
      "productId": 2,
      "quantity": 1,
      "product": {
        "id": 2,
        "name": "解压魔方",
        "coverImage": "https://example.com/cube.jpg",
        "price": 29.90,
        "stock": 50
      },
      "createTime": "2026-05-05T12:30:00",
      "updateTime": "2026-05-05T12:30:00"
    }
  ]
}
```

---

### 5.3 更新购物车数量

```
PUT http://localhost:8080/api/cart/update
```

**请求头**:
```
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体**:
```json
{
  "productId": 1,
  "quantity": 5
}
```

**响应示例（成功）**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

**响应示例（商品不在购物车）**:
```json
{
  "code": 500,
  "message": "商品不在购物车中",
  "data": null
}
```

---

### 5.4 移除购物车商品

```
DELETE http://localhost:8080/api/cart/remove/1
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 5.5 清空购物车

```
DELETE http://localhost:8080/api/cart/clear
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

## 六、订单模块 `/api/order` 🆕 新增

### 6.1 创建订单

```
POST http://localhost:8080/api/order/create
```

**请求头**:
```
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体**:
```json
{
  "cartIds": [1, 2]
}
```

**响应示例（成功）**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "orderNo": "17150000000001234",
    "totalAmount": 10970,
    "status": "pending",
    "statusText": "待发货",
    "addressName": "测试用户",
    "addressPhone": "",
    "addressDetail": "",
    "createTime": "2026-05-05T14:00:00",
    "shippedTime": null,
    "refundedTime": null,
    "items": [
      {
        "id": 1,
        "orderId": 1,
        "productId": 1,
        "productName": "治愈系香薰蜡烛",
        "productImage": "https://example.com/candle.jpg",
        "price": 3990,
        "quantity": 2
      },
      {
        "id": 2,
        "orderId": 1,
        "productId": 2,
        "productName": "解压魔方",
        "productImage": "https://example.com/cube.jpg",
        "price": 2990,
        "quantity": 1
      }
    ]
  }
}
```

**响应示例（余额不足）**:
```json
{
  "code": 500,
  "message": "心动币余额不足",
  "data": null
}
```

**响应示例（库存不足）**:
```json
{
  "code": 500,
  "message": "商品库存不足: 治愈系香薰蜡烛",
  "data": null
}
```

**响应示例（未选择商品）**:
```json
{
  "code": 500,
  "message": "请选择要购买的商品",
  "data": null
}
```

> **说明**: `totalAmount` 单位为分，10970 = 109.70元

---

### 6.2 获取订单列表

```
GET http://localhost:8080/api/order/list
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "orderNo": "17150000000001234",
      "totalAmount": 10970,
      "status": "pending",
      "statusText": "待发货",
      "addressName": "测试用户",
      "addressPhone": "",
      "addressDetail": "",
      "createTime": "2026-05-05T14:00:00",
      "shippedTime": null,
      "refundedTime": null,
      "items": [
        {
          "id": 1,
          "orderId": 1,
          "productId": 1,
          "productName": "治愈系香薰蜡烛",
          "productImage": "https://example.com/candle.jpg",
          "price": 3990,
          "quantity": 2
        }
      ]
    },
    {
      "id": 2,
      "orderNo": "17150000000005678",
      "totalAmount": 2990,
      "status": "shipped",
      "statusText": "已发货",
      "addressName": "测试用户",
      "addressPhone": "",
      "addressDetail": "",
      "createTime": "2026-05-04T10:00:00",
      "shippedTime": "2026-05-04T15:00:00",
      "refundedTime": null,
      "items": [
        {
          "id": 3,
          "orderId": 2,
          "productId": 2,
          "productName": "解压魔方",
          "productImage": "https://example.com/cube.jpg",
          "price": 2990,
          "quantity": 1
        }
      ]
    }
  ]
}
```

---

### 6.3 按状态获取订单

```
GET http://localhost:8080/api/order/list/pending
```

**请求头**:
```
Authorization: Bearer <token>
```

**路径参数说明**:

| 状态值 | 说明 |
|--------|------|
| pending | 待发货 |
| shipped | 已发货 |
| refunded | 已退款 |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "orderNo": "17150000000001234",
      "totalAmount": 10970,
      "status": "pending",
      "statusText": "待发货",
      "addressName": "测试用户",
      "addressPhone": "",
      "addressDetail": "",
      "createTime": "2026-05-05T14:00:00",
      "shippedTime": null,
      "refundedTime": null,
      "items": []
    }
  ]
}
```

---

### 6.4 获取订单详情

```
GET http://localhost:8080/api/order/1
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "orderNo": "17150000000001234",
    "totalAmount": 10970,
    "status": "pending",
    "statusText": "待发货",
    "addressName": "测试用户",
    "addressPhone": "",
    "addressDetail": "",
    "createTime": "2026-05-05T14:00:00",
    "shippedTime": null,
    "refundedTime": null,
    "items": [
      {
        "id": 1,
        "orderId": 1,
        "productId": 1,
        "productName": "治愈系香薰蜡烛",
        "productImage": "https://example.com/candle.jpg",
        "price": 3990,
        "quantity": 2
      },
      {
        "id": 2,
        "orderId": 1,
        "productId": 2,
        "productName": "解压魔方",
        "productImage": "https://example.com/cube.jpg",
        "price": 2990,
        "quantity": 1
      }
    ]
  }
}
```

---

### 6.5 申请退款

```
POST http://localhost:8080/api/order/refund/1
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例（成功）**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

**响应示例（订单不存在）**:
```json
{
  "code": 500,
  "message": "订单不存在",
  "data": null
}
```

**响应示例（非待发货状态）**:
```json
{
  "code": 500,
  "message": "只有待发货订单可以申请退款",
  "data": null
}
```

> **说明**: 退款成功后，心动币将原路返回，商品库存恢复

---

## 七、收货地址模块 `/api/address` 🆕 新增

### 7.1 添加地址

```
POST http://localhost:8080/api/address/add
```

**请求头**:
```
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体**:
```json
{
  "name": "张三",
  "phone": "13800138000",
  "province": "广东省",
  "city": "深圳市",
  "district": "南山区",
  "detail": "科技园南路88号",
  "isDefault": 1
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

> **说明**: `isDefault` 为1表示设为默认地址，会自动取消其他地址的默认状态

---

### 7.2 获取地址列表

```
GET http://localhost:8080/api/address/list
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "name": "张三",
      "phone": "13800138000",
      "province": "广东省",
      "city": "深圳市",
      "district": "南山区",
      "detail": "科技园南路88号",
      "isDefault": 1,
      "createTime": "2026-05-05T10:00:00",
      "updateTime": "2026-05-05T10:00:00"
    },
    {
      "id": 2,
      "userId": 1,
      "name": "张三",
      "phone": "13800138000",
      "province": "广东省",
      "city": "广州市",
      "district": "天河区",
      "detail": "体育西路66号",
      "isDefault": 0,
      "createTime": "2026-05-04T15:00:00",
      "updateTime": "2026-05-04T15:00:00"
    }
  ]
}
```

---

### 7.3 获取默认地址

```
GET http://localhost:8080/api/address/default
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例（有默认地址）**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "userId": 1,
    "name": "张三",
    "phone": "13800138000",
    "province": "广东省",
    "city": "深圳市",
    "district": "南山区",
    "detail": "科技园南路88号",
    "isDefault": 1,
    "createTime": "2026-05-05T10:00:00",
    "updateTime": "2026-05-05T10:00:00"
  }
}
```

**响应示例（无默认地址）**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 7.4 获取地址详情

```
GET http://localhost:8080/api/address/1
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "userId": 1,
    "name": "张三",
    "phone": "13800138000",
    "province": "广东省",
    "city": "深圳市",
    "district": "南山区",
    "detail": "科技园南路88号",
    "isDefault": 1,
    "createTime": "2026-05-05T10:00:00",
    "updateTime": "2026-05-05T10:00:00"
  }
}
```

---

### 7.5 更新地址

```
PUT http://localhost:8080/api/address/update
```

**请求头**:
```
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体**:
```json
{
  "id": 1,
  "name": "张三",
  "phone": "13900139000",
  "province": "广东省",
  "city": "深圳市",
  "district": "福田区",
  "detail": "华强北路99号",
  "isDefault": 0
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 7.6 删除地址

```
DELETE http://localhost:8080/api/address/delete/1
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 7.7 设置默认地址

```
PUT http://localhost:8080/api/address/setDefault/1
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 7.8 提交帖子申诉  新增

```
POST http://localhost:8080/api/post/appeal
```

**请求头**:
```
Authorization: Bearer <token>
```

**请求体**:
```json
{
  "postId": 5,
  "reason": "该帖子未违反社区规范，请求恢复"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 7.9 获取我的申诉列表  新增

```
GET http://localhost:8080/api/post/appeal
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "postId": 5,
      "userId": 3,
      "reason": "该帖子未违反社区规范，请求恢复",
      "status": 0,
      "createTime": "2026-05-20T10:00:00",
      "updateTime": "2026-05-20T10:00:00"
    }
  ]
}
```

| 返回字段 | 说明 |
|----------|------|
| status | 申诉状态：0申诉中/1申诉通过/2申诉不通过 |

---

## 八、首页模块 `/api/home`

### 8.1 获取轮播图列表

```
GET http://localhost:8080/api/home/banners
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "title": "欢迎来到情绪小站",
      "imageUrl": "https://example.com/banner1.jpg",
      "linkUrl": "/pages/index/index",
      "sortOrder": 1,
      "isActive": 1,
      "createTime": "2026-05-05T09:00:00"
    },
    {
      "id": 2,
      "title": "写下你的心情故事",
      "imageUrl": "https://example.com/banner2.jpg",
      "linkUrl": "/pages/index/index",
      "sortOrder": 2,
      "isActive": 1,
      "createTime": "2026-05-05T09:00:00"
    },
    {
      "id": 3,
      "title": "发现更多治愈好物",
      "imageUrl": "https://example.com/banner3.jpg",
      "linkUrl": "/pages/shop/index",
      "sortOrder": 3,
      "isActive": 1,
      "createTime": "2026-05-05T09:00:00"
    }
  ]
}
```

---

### 8.2 获取今日锦囊（治愈语录）

```
GET http://localhost:8080/api/home/quote
```

**请求头**:
```
Authorization: Bearer <token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 3,
    "content": "允许自己休息，也是一种勇敢",
    "author": "Unknown",
    "category": "daily",
    "isActive": 1,
    "createTime": "2026-05-05T09:00:00"
  }
}
```

> **说明**: 每次请求随机返回一条启用的治愈语录

---

## 九、接口汇总清单

| 序号 | 模块 | 方法 | 路径 | 说明 | 需登录 | 状态 |
|------|------|------|------|------|--------|------|
| 1 | 用户 | POST | `/api/user/login` | 登录/注册 | 否 | 原有 |
| 2 | 用户 | GET | `/api/user/info` | 获取用户信息 | 是 | 原有 |
| 3 | 用户 | PUT | `/api/user/info` | 更新用户信息 | 是 | 原有 |
| 4 | 用户 | GET | `/api/user/balance` | 获取心动币余额 | 是 | 🆕 |
| 5 | 用户 | PUT | `/api/user/nickname` | 修改昵称 | 是 | 🆕 |
| 6 | 用户 | PUT | `/api/user/avatar` | 修改头像 | 是 | 🆕 |
| 7 | 日记 | POST | `/api/diary` | 创建日记 | 是 | 原有 |
| 8 | 日记 | GET | `/api/diary/{id}` | 获取日记详情 | 是 | 原有 |
| 9 | 日记 | GET | `/api/diary/list` | 获取我的日记列表 | 是 | 原有 |
| 10 | 日记 | GET | `/api/diary/recent` | 获取最近日记 | 是 | 原有 |
| 11 | 日记 | PUT | `/api/diary/{id}` | 更新日记 | 是 | 原有 |
| 12 | 日记 | DELETE | `/api/diary/{id}` | 删除日记 | 是 | 原有 |
| 13 | 帖子 | POST | `/api/post` | 发布帖子 | 是 | 原有 |
| 14 | 帖子 | GET | `/api/post/list` | 获取帖子列表 | 是 | 原有 |
| 15 | 帖子 | GET | `/api/post/{id}` | 获取帖子详情 | 是 | 原有 |
| 16 | 帖子 | POST | `/api/post/like/{postId}` | 点赞帖子 | 是 | 原有 |
| 17 | 帖子 | DELETE | `/api/post/like/{postId}` | 取消点赞 | 是 | 原有 |
| 18 | 帖子 | POST | `/api/post/comment` | 发表评论 | 是 | 原有 |
| 19 | 帖子 | GET | `/api/post/comment/{postId}` | 获取评论列表 | 是 | 原有 |
| 20 | 帖子 | POST | `/api/post/appeal` | 提交帖子申诉 | 是 | 新增 |
| 21 | 帖子 | GET | `/api/post/appeal` | 获取我的申诉列表 | 是 | 新增 |
| 22 | 商品 | GET | `/api/product/list` | 获取商品列表 | 是 | 原有 |
| 23 | 商品 | GET | `/api/product/{id}` | 获取商品详情 | 是 | 原有 |
| 24 | 商品 | GET | `/api/product/{id}/stock` | 获取商品库存 | 是 | 新增 |
| 25 | 购物车 | POST | `/api/cart/add` | 加入购物车 | 是 | 新增 |
| 26 | 购物车 | GET | `/api/cart/list` | 获取购物车列表 | 是 | 新增 |
| 27 | 购物车 | PUT | `/api/cart/update` | 更新购物车数量 | 是 | 新增 |
| 28 | 购物车 | DELETE | `/api/cart/remove/{productId}` | 移除购物车商品 | 是 | 新增 |
| 29 | 购物车 | DELETE | `/api/cart/clear` | 清空购物车 | 是 | 新增 |
| 30 | 订单 | POST | `/api/order/create` | 创建订单 | 是 | 新增 |
| 31 | 订单 | GET | `/api/order/list` | 获取订单列表 | 是 | 新增 |
| 32 | 订单 | GET | `/api/order/list/{status}` | 按状态获取订单 | 是 | 新增 |
| 33 | 订单 | GET | `/api/order/{id}` | 获取订单详情 | 是 | 新增 |
| 34 | 订单 | POST | `/api/order/refund/{id}` | 申请退款 | 是 | 新增 |
| 35 | 地址 | POST | `/api/address/add` | 添加地址 | 是 | 新增 |
| 36 | 地址 | GET | `/api/address/list` | 获取地址列表 | 是 | 新增 |
| 37 | 地址 | GET | `/api/address/default` | 获取默认地址 | 是 | 新增 |
| 38 | 地址 | GET | `/api/address/{id}` | 获取地址详情 | 是 | 新增 |
| 39 | 地址 | PUT | `/api/address/update` | 更新地址 | 是 | 新增 |
| 40 | 地址 | DELETE | `/api/address/delete/{id}` | 删除地址 | 是 | 新增 |
| 41 | 地址 | PUT | `/api/address/setDefault/{id}` | 设置默认地址 | 是 | 新增 |
| 42 | 首页 | GET | `/api/home/banners` | 获取轮播图 | 是 | 原有 |
| 43 | 首页 | GET | `/api/home/quote` | 获取今日锦囊 | 是 | 原有 |

---

## 十、Postman 测试建议

### 10.1 环境变量设置

在 Postman 中创建环境变量：

| 变量名 | 值 |
|--------|-----|
| baseUrl | `http://localhost:8080` |
| token | （登录后自动获取） |

### 10.2 登录接口自动获取Token

在登录接口的 Tests 中添加：

```javascript
var jsonData = pm.response.json();
if (jsonData.code === 200) {
    pm.environment.set("token", jsonData.data.token);
}
```

### 10.3 其他接口请求头

在其他接口的 Headers 中添加：

```
Authorization: Bearer {{token}}
```

---

## 十一、cURL 测试示例

### 登录
```bash
curl -X POST http://localhost:8080/api/user/login \
  -H "Content-Type: application/json" \
  -d '{"code":"test001","nickname":"测试用户","avatarUrl":"https://example.com/avatar.jpg"}'
```

### 创建日记
```bash
curl -X POST http://localhost:8080/api/diary \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{"emotion":"happy","emotionLabel":"开心","title":"开心的一天","content":"今天天气很好","images":"","isPublic":0}'
```

### 获取帖子列表
```bash
curl -X GET http://localhost:8080/api/post/list \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 获取商品列表
```bash
curl -X GET http://localhost:8080/api/product/list \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 获取今日锦囊
```bash
curl -X GET http://localhost:8080/api/home/quote \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 🆕 获取心动币余额
```bash
curl -X GET http://localhost:8080/api/user/balance \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 🆕 修改昵称
```bash
curl -X PUT http://localhost:8080/api/user/nickname \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{"nickname":"新昵称"}'
```

### 🆕 加入购物车
```bash
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{"productId":1,"quantity":2}'
```

### 🆕 获取购物车列表
```bash
curl -X GET http://localhost:8080/api/cart/list \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 🆕 创建订单
```bash
curl -X POST http://localhost:8080/api/order/create \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{"cartIds":[1,2]}'
```

### 🆕 获取订单列表
```bash
curl -X GET http://localhost:8080/api/order/list \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 🆕 申请退款
```bash
curl -X POST http://localhost:8080/api/order/refund/1 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 🆕 添加收货地址
```bash
curl -X POST http://localhost:8080/api/address/add \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{"name":"张三","phone":"13800138000","province":"广东省","city":"深圳市","district":"南山区","detail":"科技园南路","isDefault":1}'
```

### 🆕 获取地址列表
```bash
curl -X GET http://localhost:8080/api/address/list \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 八、管理端模块 `/api/admin` 🆕 新增

> **说明**: 管理端运行在独立服务 `mood-admin-later`，默认端口 **8081**
> 除登录接口外，所有接口需在请求头携带 `Authorization: Bearer <admin_token>`

| 配置项 | 值 |
|--------|-----|
| 基础URL | `http://localhost:8081` |
| 认证方式 | JWT Token（请求头 `Authorization: Bearer <token>`） |
| 数据格式 | JSON |
| 端口 | 8081 |

---

### 8.1 管理员登录

```
POST http://localhost:8081/api/admin/login
```

**请求头**: 无

**请求体**:
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**响应示例（成功）**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

**响应示例（账号不存在）**:
```json
{
  "code": 500,
  "message": "管理员账号不存在",
  "data": null
}
```

**响应示例（密码错误）**:
```json
{
  "code": 500,
  "message": "密码错误",
  "data": null
}
```

**响应示例（账号已禁用）**:
```json
{
  "code": 500,
  "message": "管理员账号已被禁用",
  "data": null
}
```

> **注意**: 登录成功后需保存返回的 `token`，后续管理端接口需在请求头携带 `Authorization: Bearer <token>`

---

### 8.2 图片上传

```
POST http://localhost:8081/api/admin/upload/image
```

**请求头**:
```
Authorization: Bearer <admin_token>
Content-Type: multipart/form-data
```

**请求参数**:

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| file | File | 是 | - | 图片文件（JPG/PNG/GIF/WEBP，最大2MB） |
| type | String | 否 | common | 图片类型：banner（轮播图）/ product（商品）/ common（通用） |

**响应示例（成功）**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "url": "/uploads/banner/abc123.jpg",
    "fileName": "abc123.jpg"
  }
}
```

**响应示例（文件为空）**:
```json
{
  "code": 500,
  "message": "文件不能为空",
  "data": null
}
```

**响应示例（格式不支持）**:
```json
{
  "code": 500,
  "message": "仅支持 JPG/PNG/GIF/WEBP 格式图片",
  "data": null
}
```

**响应示例（文件过大）**:
```json
{
  "code": 500,
  "message": "文件大小不能超过2MB",
  "data": null
}
```

---

## 九、轮播图管理 `/api/admin/banner` 🆕 新增

### 9.1 获取轮播图列表

```
GET http://localhost:8081/api/admin/banner/list
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "title": "欢迎来到情绪小站",
      "imageUrl": "/uploads/banner/banner1.jpg",
      "linkUrl": "/pages/home/home",
      "sortOrder": 1,
      "isActive": 1,
      "createTime": "2026-05-20T10:00:00"
    },
    {
      "id": 2,
      "title": "新品上架",
      "imageUrl": "/uploads/banner/banner2.jpg",
      "linkUrl": "/pages/product/product",
      "sortOrder": 2,
      "isActive": 1,
      "createTime": "2026-05-20T11:00:00"
    }
  ]
}
```

---

### 9.2 获取轮播图详情

```
GET http://localhost:8081/api/admin/banner/1
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "欢迎来到情绪小站",
    "imageUrl": "/uploads/banner/banner1.jpg",
    "linkUrl": "/pages/home/home",
    "sortOrder": 1,
    "isActive": 1,
    "createTime": "2026-05-20T10:00:00"
  }
}
```

---

### 9.3 添加轮播图

```
POST http://localhost:8081/api/admin/banner/add
```

**请求头**:
```
Authorization: Bearer <admin_token>
Content-Type: application/json
```

**请求体**:
```json
{
  "title": "活动宣传",
  "imageUrl": "/uploads/banner/banner3.jpg",
  "linkUrl": "/pages/activity/activity",
  "sortOrder": 3,
  "isActive": 1
}
```

| 字段 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| title | String | 是 | - | 轮播图标题 |
| imageUrl | String | 是 | - | 图片URL（先调用上传接口获取） |
| linkUrl | String | 否 | null | 跳转链接（小程序页面路径） |
| sortOrder | Integer | 否 | 0 | 排序值（越小越靠前） |
| isActive | Integer | 否 | 1 | 状态：0-禁用 1-启用 |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 9.4 更新轮播图

```
PUT http://localhost:8081/api/admin/banner/update
```

**请求头**:
```
Authorization: Bearer <admin_token>
Content-Type: application/json
```

**请求体**:
```json
{
  "id": 1,
  "title": "修改后的标题",
  "imageUrl": "/uploads/banner/new-banner.jpg",
  "linkUrl": "/pages/new-page/new-page",
  "sortOrder": 5,
  "isActive": 1
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 9.5 删除轮播图

```
DELETE http://localhost:8081/api/admin/banner/delete/1
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 9.6 更新轮播图状态（启用/禁用）

```
PUT http://localhost:8081/api/admin/banner/status/1
```

**请求头**:
```
Authorization: Bearer <admin_token>
Content-Type: application/json
```

**请求体**:
```json
{
  "isActive": 0
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| isActive | Integer | 是 | 0-禁用 1-启用 |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

## 十、帖子与评论管理 `/api/admin/post` 🆕 新增

### 10.1 获取帖子列表

```
GET http://localhost:8081/api/admin/post/list
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "userId": 3,
      "content": "今天天气真好，出去散步了~",
      "images": "https://example.com/post1.jpg",
      "likeCount": 12,
      "commentCount": 3,
      "source": "original",
      "sourceDiaryId": null,
      "createTime": "2026-05-20T11:00:00",
      "updateTime": "2026-05-20T11:00:00",
      "authorNickname": "测试用户",
      "authorAvatar": "https://example.com/avatar.jpg"
    }
  ]
}
```

---

### 10.2 获取帖子详情

```
GET http://localhost:8081/api/admin/post/1
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**: 同帖子列表，返回单条帖子数据

---

### 10.3 删除帖子

```
DELETE http://localhost:8081/api/admin/post/delete/1
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 10.4 批量删除帖子

```
POST http://localhost:8081/api/admin/post/batch-delete
```

**请求头**:
```
Authorization: Bearer <admin_token>
Content-Type: application/json
```

**请求体**:
```json
{
  "ids": [1, 2, 3]
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 10.5 获取评论列表

```
GET http://localhost:8081/api/admin/post/comment/list
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "postId": 1,
      "userId": 5,
      "content": "写得真好！",
      "createTime": "2026-05-20T12:00:00",
      "userNickname": "用户B",
      "userAvatar": "https://example.com/avatar2.jpg"
    }
  ]
}
```

---

### 10.6 获取评论详情

```
GET http://localhost:8081/api/admin/post/comment/1
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**: 同评论列表，返回单条评论数据

---

### 10.7 删除评论

```
DELETE http://localhost:8081/api/admin/post/comment/delete/1
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 10.8 批量删除评论

```
POST http://localhost:8081/api/admin/post/comment/batch-delete
```

**请求头**:
```
Authorization: Bearer <admin_token>
Content-Type: application/json
```

**请求体**:
```json
{
  "ids": [1, 2, 3]
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 10.9 审核帖子申诉通过  新增

```
PUT http://localhost:8081/api/admin/post/appeal/approve/1
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

> **说明**: 申诉通过后，帖子恢复显示，用户端回收站帖子消失

---

### 10.10 审核帖子申诉不通过  新增

```
PUT http://localhost:8081/api/admin/post/appeal/reject/1
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

> **说明**: 申诉不通过后，帖子保持删除状态，用户端回收站显示「审核不通过」

---

## 十一、商品管理 `/api/admin/product`  新增

### 11.1 获取商品列表

```
GET http://localhost:8081/api/admin/product/list
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "治愈系香薰蜡烛",
      "coverImage": "/uploads/product/candle.jpg",
      "images": "/uploads/product/candle1.jpg,/uploads/product/candle2.jpg",
      "price": 3990,
      "originalPrice": 5900,
      "description": "薰衣草香味，帮助放松入眠",
      "tags": "治愈系,热销",
      "taobaoUrl": "https://item.taobao.com/item.htm?id=1",
      "sortOrder": 0,
      "status": 1,
      "stock": 100,
      "createTime": "2026-05-20T09:00:00",
      "updateTime": "2026-05-20T09:00:00"
    }
  ]
}
```

> **说明**: `price` 和 `originalPrice` 单位为分，3990 = 39.90元

---

### 11.2 获取商品详情

```
GET http://localhost:8081/api/admin/product/1
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**: 同商品列表，返回单条商品数据

---

### 11.3 添加商品

```
POST http://localhost:8081/api/admin/product/add
```

**请求头**:
```
Authorization: Bearer <admin_token>
Content-Type: application/json
```

**请求体**:
```json
{
  "name": "解压魔方",
  "coverImage": "/uploads/product/cube.jpg",
  "images": "/uploads/product/cube1.jpg,/uploads/product/cube2.jpg",
  "price": 2990,
  "originalPrice": 4900,
  "description": "六面不同解压设计，随身携带",
  "tags": "解压,办公",
  "taobaoUrl": "https://item.taobao.com/item.htm?id=2",
  "sortOrder": 0,
  "status": 1,
  "stock": 50
}
```

| 字段 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| name | String | 是 | - | 商品名称 |
| coverImage | String | 是 | - | 封面图URL |
| images | String | 否 | null | 详情图URL（逗号分隔） |
| price | Integer | 是 | - | 价格（分） |
| originalPrice | Integer | 否 | null | 原价（分） |
| description | String | 否 | null | 商品描述 |
| tags | String | 否 | null | 标签（逗号分隔） |
| taobaoUrl | String | 否 | null | 淘宝链接 |
| sortOrder | Integer | 否 | 0 | 排序值 |
| status | Integer | 否 | 1 | 状态：0-下架 1-上架 |
| stock | Integer | 否 | 0 | 库存数量 |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 11.4 更新商品

```
PUT http://localhost:8081/api/admin/product/update
```

**请求头**:
```
Authorization: Bearer <admin_token>
Content-Type: application/json
```

**请求体**:
```json
{
  "id": 1,
  "name": "修改后的商品名",
  "price": 4990,
  "stock": 200
}
```

> **说明**: 只需传需要修改的字段，未传字段不会变更

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 11.5 更新商品状态（上架/下架）

```
PUT http://localhost:8081/api/admin/product/status/1
```

**请求头**:
```
Authorization: Bearer <admin_token>
Content-Type: application/json
```

**请求体**:
```json
{
  "status": 0
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| status | Integer | 是 | 0-下架 1-上架 |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 11.6 商品补货

```
POST http://localhost:8081/api/admin/product/restock
```

**请求头**:
```
Authorization: Bearer <admin_token>
Content-Type: application/json
```

**请求体**:
```json
{
  "productId": 1,
  "quantity": 100
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| productId | Long | 是 | 商品ID |
| quantity | Integer | 是 | 补货数量 |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 11.7 删除商品

```
DELETE http://localhost:8081/api/admin/product/delete/1
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

## 十二、订单管理 `/api/admin/order` 🆕 新增

### 12.1 获取订单列表

```
GET http://localhost:8081/api/admin/order/list
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "orderNo": "17162000000001234",
      "userId": 3,
      "totalAmount": 10970,
      "status": "pending",
      "addressName": "张三",
      "addressPhone": "13800138000",
      "addressDetail": "广东省深圳市南山区科技园南路",
      "createTime": "2026-05-20T14:00:00",
      "shippedTime": null,
      "refundedTime": null,
      "userNickname": "测试用户",
      "userAvatar": "https://example.com/avatar.jpg"
    }
  ]
}
```

---

### 12.2 获取订单详情

```
GET http://localhost:8081/api/admin/order/1
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "order": {
      "id": 1,
      "orderNo": "17162000000001234",
      "userId": 3,
      "totalAmount": 10970,
      "status": "pending",
      "addressName": "张三",
      "addressPhone": "13800138000",
      "addressDetail": "广东省深圳市南山区科技园南路",
      "createTime": "2026-05-20T14:00:00",
      "shippedTime": null,
      "refundedTime": null,
      "userNickname": "测试用户",
      "userAvatar": "https://example.com/avatar.jpg"
    },
    "items": [
      {
        "id": 1,
        "orderId": 1,
        "productId": 1,
        "productName": "治愈系香薰蜡烛",
        "productImage": "/uploads/product/candle.jpg",
        "price": 3990,
        "quantity": 2,
        "createTime": "2026-05-20T14:00:00"
      },
      {
        "id": 2,
        "orderId": 1,
        "productId": 2,
        "productName": "解压魔方",
        "productImage": "/uploads/product/cube.jpg",
        "price": 2990,
        "quantity": 1,
        "createTime": "2026-05-20T14:00:00"
      }
    ]
  }
}
```

---

### 12.3 发货

```
PUT http://localhost:8081/api/admin/order/ship/1
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 12.4 退款审核通过

```
PUT http://localhost:8081/api/admin/order/refund/approve/1
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

> **说明**: 审核通过后，订单状态变为已退款，心动币退回用户，库存恢复

---

### 12.5 退款审核不通过

```
PUT http://localhost:8081/api/admin/order/refund/reject/1
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

> **说明**: 审核不通过后，订单状态不变，用户可再次申请退款

---

### 12.6 获取待处理退款申请列表

```
GET http://localhost:8081/api/admin/order/refund/pending
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "orderId": 5,
      "userId": 3,
      "reason": "商品有质量问题",
      "status": 0,
      "createTime": "2026-05-20T10:00:00",
      "updateTime": "2026-05-20T10:00:00"
    }
  ]
}
```

| 返回字段 | 说明 |
|----------|------|
| status | 审核状态：0待审核/1已通过/2已拒绝 |

---

### 12.7 获取待处理退款数量

```
GET http://localhost:8081/api/admin/order/refund/pending/count
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": 2
}
```

---

### 12.8 数据面板

```
GET http://localhost:8081/api/admin/order/dashboard?days=7
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**参数说明**:

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| days | Integer | 否 | 7 | 统计天数（7=近七天，30=近30天） |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "stats": {
      "orderCount": 25,
      "orderQuantity": 48,
      "orderAmount": 156780,
      "refundCount": 2
    },
    "dailyOrders": [
      {
        "date": "2026-05-14",
        "orderCount": 3,
        "orderAmount": 19970
      },
      {
        "date": "2026-05-15",
        "orderCount": 5,
        "orderAmount": 34950
      },
      {
        "date": "2026-05-16",
        "orderCount": 2,
        "orderAmount": 11980
      }
    ],
    "productSales": [
      {
        "productId": 1,
        "productName": "治愈系香薰蜡烛",
        "salesQuantity": 20,
        "salesAmount": 79800
      },
      {
        "productId": 2,
        "productName": "解压魔方",
        "salesQuantity": 15,
        "salesAmount": 44850
      }
    ]
  }
}
```

| 返回字段 | 说明 |
|----------|------|
| stats.orderCount | 订单总数 |
| stats.orderQuantity | 下单商品总数量 |
| stats.orderAmount | 下单总金额（分） |
| stats.refundCount | 退款订单数 |
| pendingRefundCount | 待处理退款申请数量 |
| dailyOrders | 每日订单统计（用于柱状图） |
| productSales | 商品销售排行（用于饼状图，Top 5） |

---

### 12.9 删除订单

```
DELETE http://localhost:8081/api/admin/order/1
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

> **说明**: 删除订单及其关联的商品明细记录，删除后无法恢复

---

## 十三、用户管理 `/api/admin/user`  新增

### 13.1 获取用户列表

```
GET http://localhost:8081/api/admin/user/list
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "openid": "oABC123...",
      "username": "user1",
      "nickname": "测试用户",
      "avatarUrl": "https://example.com/avatar.jpg",
      "phone": "13800138000",
      "gender": 1,
      "coinBalance": 200,
      "status": "normal",
      "lastLoginTime": "2026-05-20T10:00:00",
      "createTime": "2026-05-01T08:00:00",
      "updateTime": "2026-05-20T10:00:00"
    }
  ]
}
```

| 字段 | 说明 |
|------|------|
| status | normal-正常 / banned-已封禁 |

---

### 13.2 获取用户详情

```
GET http://localhost:8081/api/admin/user/1
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**: 同用户列表，返回单条用户数据

---

### 13.3 封禁用户

```
PUT http://localhost:8081/api/admin/user/ban/1
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 13.4 解封用户

```
PUT http://localhost:8081/api/admin/user/unban/1
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 13.5 删除用户

```
DELETE http://localhost:8081/api/admin/user/delete/1
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 13.6 赠送心动币

```
POST http://localhost:8081/api/admin/user/gift-coins
```

**请求头**:
```
Authorization: Bearer <admin_token>
Content-Type: application/json
```

**请求体**:
```json
{
  "userId": 1,
  "amount": 100,
  "reason": "新用户奖励"
}
```

| 字段 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| userId | Long | 是 | - | 用户ID |
| amount | Integer | 是 | - | 赠送数量（正数） |
| reason | String | 否 | 管理员赠送 | 赠送原因 |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

## 十四、快递状态管理 `/api/admin/express`  新增

> **说明**: 管理端独立页面，与订单管理并列，用于管理已发货订单的快递状态

### 14.1 获取已发货订单列表

```
GET http://localhost:8081/api/admin/express/shipped
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "orderNo": "17150000000001234",
      "userId": 1,
      "userNickname": "测试用户",
      "totalAmount": 10970,
      "status": "shipped",
      "expressCompany": "顺丰快递",
      "expressNo": "SF1234567890",
      "expressCourier": "张三",
      "expressStatus": "delivering",
      "createTime": "2026-05-20T14:00:00",
      "items": [
        {
          "id": 1,
          "orderId": 1,
          "productId": 1,
          "productName": "治愈系香薰蜡烛",
          "productImage": "/uploads/product/candle.jpg",
          "price": 3990,
          "quantity": 2
        }
      ]
    }
  ]
}
```

| 返回字段 | 说明 |
|----------|------|
| expressCompany | 快递公司 |
| expressNo | 快递单号 |
| expressCourier | 派送人 |
| expressStatus | 快递状态：delivering（派送中）/ signed（已签收） |

---

### 14.2 更新快递状态

```
PUT http://localhost:8081/api/admin/express/status/{id}
```

**请求头**:
```
Authorization: Bearer <admin_token>
Content-Type: application/json
```

**请求体**:
```json
{
  "expressStatus": "signed"
}
```

**参数说明**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| expressStatus | String | 是 | 快递状态：delivering（派送中）/ signed（已签收） |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

> **说明**: 更新为已签收后，用户端订单显示确认收货按钮

---

### 14.3 退款（快递状态管理页面）

```
PUT http://localhost:8081/api/admin/express/refund/{id}
```

**请求头**:
```
Authorization: Bearer <admin_token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

> **说明**: 直接在快递状态管理页面处理退款，订单状态变为已退款，心动币退回用户，库存恢复

---

## 附录：curl 快速测试命令

### 管理员登录
```bash
curl -X POST http://localhost:8081/api/admin/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### 获取轮播图列表
```bash
curl -X GET http://localhost:8081/api/admin/banner/list \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

### 添加轮播图
```bash
curl -X POST http://localhost:8081/api/admin/banner/add \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN" \
  -d '{"title":"活动宣传","imageUrl":"/uploads/banner/banner3.jpg","linkUrl":"/pages/activity/activity","sortOrder":3,"isActive":1}'
```

### 获取帖子列表
```bash
curl -X GET http://localhost:8081/api/admin/post/list \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

### 删除帖子
```bash
curl -X DELETE http://localhost:8081/api/admin/post/delete/1 \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

### 获取商品列表
```bash
curl -X GET http://localhost:8081/api/admin/product/list \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

### 商品补货
```bash
curl -X POST http://localhost:8081/api/admin/product/restock \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN" \
  -d '{"productId":1,"quantity":100}'
```

### 获取订单列表
```bash
curl -X GET http://localhost:8081/api/admin/order/list \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

### 发货
```bash
curl -X PUT http://localhost:8081/api/admin/order/ship/1 \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

### 数据面板
```bash
curl -X GET "http://localhost:8081/api/admin/order/dashboard?days=7" \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

### 获取用户列表
```bash
curl -X GET http://localhost:8081/api/admin/user/list \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

### 封禁用户
```bash
curl -X PUT http://localhost:8081/api/admin/user/ban/1 \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

### 赠送心动币
```bash
curl -X POST http://localhost:8081/api/admin/user/gift-coins \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN" \
  -d '{"userId":1,"amount":100,"reason":"新用户奖励"}'
```
