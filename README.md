# 情绪小站 · 微信小程序（全栈）

一个以「情绪记录 + 温暖社区 + 治愈商城 + AI 陪伴」为核心的微信小程序课设项目，包含 **小程序端、小程序后端、管理端前端、管理端后端** 四部分。

- 小程序端：情绪日记、情绪分享社区、情绪周边商城、个人中心、情绪小喵 AI 对话
- 管理端：数据概览、用户 / 商品 / 订单 / 快递 / 帖子 / 音乐 / 轮播图 / 用户反馈管理

> 详细的需求分析、页面设计与数据库设计见 [情绪小站-项目文档.md](./情绪小站-项目文档.md)；UI 迭代记录见 [情绪小站-UI优化记录.md](./情绪小站-UI优化记录.md)。

---

## 一、项目结构

```
mood-project/
├── mood-front/            # 微信小程序端（原生 WXML/WXSS/JS）
├── mood-later/            # 小程序后端（Spring Boot，端口 8080）
├── mood-admin_front/      # 管理端前端（Vue 3 + Vite + Element Plus，端口 3000）
├── mood-admin_later/      # 管理端后端（Spring Boot，端口 8081）
├── uploads/               # 上传文件目录（common / music / product 及图片）
├── scripts/dev.js         # 一键启动脚本
├── package.json           # 根目录启动脚本
└── 情绪小站-项目文档.md
```

---

## 界面展示

主页是这种奶黄治愈风格，并且在不同时间段有做实时温馨提醒、白天与夜晚会切换不同插画，此时是半夜12点，插画以月亮形式显示

<img src="./assets/screenshots/image-20261125000348473.png" alt="image-20261125000348473" style="zoom:50%;" />

轮播图下面的每日锦囊，藏着暖评语，当你情绪失落时打开这个锦囊时，可以收到暖评鼓励，这是小站情绪价值的体现。暖评语每日一次，在暖评库中随机抽取发送给用户

这里做了个图标切换效果，点击时，锦囊会充能，随后出现快速翻转动画，增加互动效果

<img src="./assets/screenshots/image-20261125000747205.png" alt="image-20261125000747205" style="zoom:50%;" />

翻转后，就能看到暖评，并且背景也有做星星闪烁动画效果

<img src="./assets/screenshots/image-20261125000638513.png" alt="image-20261125000638513" style="zoom:50%;" />

侧边栏藏着一只情绪小喵助手，点击展开后可以进入聊天界面，可以诉说你的情绪。ai助手是基于deepseek开发的，需要自己配置模型，点击小喵头像就可以配置了

<img src="./assets/screenshots/image-20261125003922302.png" alt="image-20261125003922302" style="zoom:50%;" />

写日记，记录你的每日情绪，可以选择情绪、也可以公开给其他用户分享自己情绪

<img src="./assets/screenshots/image-20261125001241242.png" alt="image-20261125001241242" style="zoom:50%;" />

情绪分享页，一个简单的论坛界面，是情绪宣泄与分享的地方，可以点赞，也可以进行评论

<img src="./assets/screenshots/image-20261125001458520.png" alt="image-20261125001458520" style="zoom:50%;" />

在这里，点击用户头像，可以看到用户发布的日记（前面公开日记则会显示，不公开则看不到）

<img src="./assets/screenshots/image-20261125001743943.png" alt="image-20261125001743943" style="zoom:50%;" />

也可以进行留言

<img src="./assets/screenshots/image-20261125001805198.png" alt="image-20261125001805198" style="zoom:50%;" />

点击抱抱会随机送一条暖评给对方，对方会在通知中看到来自xx用户的暖评及内容

<img src="./assets/screenshots/image-20261125001903562.png" alt="image-20261125001903562" style="zoom:50%;" />

情绪周边，一个简单的情绪商城，售卖的都是解压、治愈等情绪商品，有完整的电商流程闭环。以心动币作为货币购买商品，比起直接赤裸裸的明码标价，以心动币兑换更凸显情绪，因为它可以通过登录、点赞用户帖子、发布日记帖子来获取，新用户注册时，系统会直接赠送心动币，让每个用户第一次使用时都可以换一件让自己心情舒畅的商品

<img src="./assets/screenshots/image-20261125001954395.png" alt="image-20261125001954395" style="zoom:50%;" />

商品详情页，可以选款式，有实时库存显示

<img src="./assets/screenshots/image-20261125002518506.png" alt="image-20261125002518506" style="zoom:50%;" />

提交订单界面

<img src="./assets/screenshots/image-20261125002607742.png" alt="image-20261125002607742" style="zoom:50%;" />

订单提交后，会显示待发货，等待管理端确认订单发货，我的订单这里会变更订单信息，直到已完成为止

<img src="./assets/screenshots/image-20261125002711863.png" alt="image-20261125002711863" style="zoom:50%;" />

个人界面

<img src="./assets/screenshots/image-20261125003311349.png" alt="image-20261125003311349" style="zoom:50%;" />

这里能看到心动币余额，旁边的问号点击后会有心动币具体的说明

<img src="./assets/screenshots/image-20261125003345955.png" alt="image-20261125003345955" style="zoom:50%;" />

右上角的两个功能，一个是音乐功能，一个是通知功能

<img src="./assets/screenshots/image-20261125003422349.png" alt="image-20261125003422349" style="zoom:50%;" />

点击音乐可以选择纯音乐和流行音乐

<img src="./assets/screenshots/image-20261125003457356.png" alt="image-20261125003457356" style="zoom:50%;" />

音乐播放时显示，播放时，切换其他界面会隐藏播放条

<img src="./assets/screenshots/image-20261125003539088.png" alt="image-20261125003539088" style="zoom:50%;" />

通知功能

<img src="./assets/screenshots/image-20261125003651719.png" alt="image-20261125003651719" style="zoom:50%;" />

<img src="./assets/screenshots/image-20261125003706980.png" alt="image-20261125003706980" style="zoom:50%;" />



管理端（**管理端账号为：admin，密码为：admin123**）

![image-20261125004325329](./assets/screenshots/image-20261125004325329.png)

![image-20261125004455355](./assets/screenshots/image-20261125004455355.png)

![image-20261125004508774](./assets/screenshots/image-20261125004508774.png)

![image-20261125004521388](./assets/screenshots/image-20261125004521388.png)

![image-20261125004533263](./assets/screenshots/image-20261125004533263.png)

## 二、技术栈

| 端 | 技术栈 |
|---|---|
| 小程序端 | 微信小程序原生开发（WXML + WXSS + JavaScript），无构建 |
| 小程序后端 | Spring Boot 2.7.18 + MyBatis 2.3.1 + MySQL 8.0 + JWT + OkHttp + Gson |
| 管理端前端 | Vue 3 + Vite 4 + Element Plus 2.3 + Vue Router 4 + Axios + Pinia + Sass |
| 管理端后端 | Spring Boot 2.7.18 + MyBatis + MySQL 8.0 + JWT |
| 数据库 | MySQL 8.0，库名 `mood` |

---

## 三、环境要求

| 工具 | 版本要求 | 说明 |
|---|---|---|
| JDK | 1.8 及以上 | 两个 Spring Boot 服务均使用 Java 8 编译 |
| Maven | 3.6+ | 需加入 PATH 或配置 `MAVEN_HOME` |
| MySQL | 8.0 | 默认账号 `root / 123456`，可在 `application.yml` 修改 |
| Node.js | 16+（建议 18） | 管理端前端与一键启动脚本 |
| 微信开发者工具 | 最新稳定版 | 运行 `mood-front` 小程序 |

---

## 四、端口与访问地址（重点）

启动后各端地址如下，其中 `192.168.0.100` 为**开发机当前局域网 IP**（换网络后需改成自己电脑的 IPv4）。

| 端 | 目录 | 协议 / 端口 | 本机地址 | 局域网 / 真机地址 |
|---|---|---|---|---|
| 小程序后端 | `mood-later` | HTTP `8080` | http://localhost:8080 | http://192.168.0.100:8080 |
| 管理端后端 | `mood-admin_later` | HTTP `8081` | http://localhost:8081 | http://192.168.0.100:8081 |
| 管理端前端 | `mood-admin_front` | HTTP `3000` | http://localhost:3000 | http://192.168.0.100:3000 |
| 小程序端 | `mood-front` | 无独立地址 | 微信开发者工具导入运行 | 真机预览扫码 |
| MySQL 数据库 | — | TCP `3306` | `localhost:3306/mood` | — |
| 上传静态资源 | `uploads/` | HTTP `8080` | http://localhost:8080/uploads/... | http://192.168.0.100:8080/uploads/... |

**端口关系说明：**

- 管理端前端 `3000` 通过 Vite 代理把 `/api` 转发到管理端后端 `8081`，见 `mood-admin_front/vite.config.js`
- 小程序全部接口直连小程序后端 `8080`，见 `mood-front/config/api.js` 的 `baseUrl`
- 两个后端共享同一个上传目录，`/uploads/**` 由 8080 与 8081 分别映射为静态资源

**如何查看本机 IP：**

```powershell
ipconfig
# 找到「无线局域网适配器 WLAN」下的 IPv4 地址，例如 192.168.0.100
```

---

## 五、数据库初始化

> 注意：`init.sql` 会 `DROP TABLE` 重建核心表，已有数据会被清空，请勿在生产库直接执行。

按以下**顺序**在 MySQL 中执行（命令行或 Navicat 均可）：

```sql
-- 1. 核心业务表（user / diary / post / comment / product / banner 等）
source mood-later/sql/init.sql

-- 2. 购物车、地址、订单
source mood-later/sql/extend.sql

-- 3. 订单退款与快递字段（依赖第 2 步的 order 表）
source mood-later/sql/order_upgrade.sql

-- 4. 音乐库
source mood-later/sql/music.sql

-- 5. 管理员、心动币流水、库存流水、操作日志
source mood-later/sql/admin.sql

-- 6. 帖子回收站与申诉
source mood-later/sql/post_trash_appeal.sql

-- 7. AI 聊天记录
source mood-later/sql/ai_chat.sql

-- 8. 用户级 AI 配置
source mood-later/sql/ai_config.sql

-- 9. 用户反馈
source mood-later/sql/feedback.sql

-- 10. 通知、留言、每日奖励
source mood-later/sql/extra_tables.sql
```

**默认管理员账号：**

| 账号 | 密码 | 角色 |
|---|---|---|
| `admin` | `admin123` | 超级管理员 |

---

## 六、启动前配置

### 1. 修改上传目录（必改）

两个后端的 `src/main/resources/application.yml` 中：

```yaml
upload:
  path: e:/微信小程序课设/uploads
```

改为本项目实际的上传目录，例如：

```yaml
upload:
  path: e:/mood-project/uploads
```

> 仓库自带 `uploads/` 示例数据（商品图、音乐等）。若使用其它绝对路径，请确保 `common/`、`music/`、`product/` 子目录存在且可写，否则图片和音乐会 404。

### 2. 修改数据库连接（按需）

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mood?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
```

### 3. 修改小程序后端地址（真机必改）

`mood-front/config/api.js`：

```javascript
baseUrl: 'http://192.168.0.100:8080'
```

- 开发者工具模拟器：可保持 `localhost` 或局域网 IP
- 真机预览：必须改成电脑的**局域网 IPv4**，且手机与电脑连接**同一 WiFi**
- 同时需在微信开发者工具中勾选「详情 → 本地设置 → 不校验合法域名、web-view（业务域名）、TLS 版本以及 HTTPS 证书」

### 4. 配置 AI 能力（每个用户独立）

项目不再在服务端写死 AI Key，改为**每个用户在小程序内自行配置**：

1. 进入小程序「我的」页面
2. 点击右上角情绪小喵头像或顶部「当前模型：xxx」
3. 在配置页填写：
   - Base URL：如 `https://api.deepseek.com`
   - API Key：你的密钥（`sk-` 开头）
   - 模型：如 `deepseek-chat`
4. 保存后即可对话

> 未配置时对话会提示「未配置 AI」；模型不可用会提示「模型不可用」。

---

## 七、启动方式

### 方式一：一键启动（推荐）

在项目根目录执行：

```bash
npm run dev
```

脚本会依次启动：

| 服务 | 地址 |
|---|---|
| 小程序后端 | http://localhost:8080 |
| 管理端后端 | http://localhost:8081 |
| 管理端前端 | http://localhost:3000 |

> 小程序端不会被脚本启动，需在微信开发者工具中手动导入 `mood-front`。首次启动 Maven 编译较慢，请耐心等待；按 `Ctrl+C` 可停止全部服务。

### 方式二：分别启动

```bash
# 1. 小程序后端（8080）
cd mood-later
mvn spring-boot:run

# 2. 管理端后端（8081）
cd mood-admin_later
mvn spring-boot:run

# 3. 管理端前端（3000）
cd mood-admin_front
npm install
npm run dev

# 4. 小程序端
# 微信开发者工具 → 导入项目 → 选择 mood-front 目录 → AppID 可用测试号
```

### 启动后验证

| 检查项 | 预期结果 |
|---|---|
| 打开 http://localhost:3000 | 进入管理端登录页 |
| 使用 `admin / admin123` 登录 | 进入数据概览 |
| 微信开发者工具编译 `mood-front` | 首页正常展示轮播图与今日锦囊 |
| 访问 `http://localhost:8080/uploads/music/xxx.mp3` | 能正常返回音频（说明静态映射与文件路径正确） |

---

## 八、生产部署

### 1. 后端打包运行

```bash
cd mood-later
mvn clean package -DskipTests
java -jar target/mood-later-1.0.0.jar

cd ../mood-admin_later
mvn clean package -DskipTests
java -jar target/mood-admin-later-1.0.0.jar
```

- 部署前修改 `application.yml` 的数据库地址、账号密码、`upload.path`
- 服务器需放行 `8080`、`8081` 端口
- 上传目录需持久化并保证进程有读写权限

### 2. 管理端前端打包

```bash
cd mood-admin_front
npm install
npm run build
# 产物在 dist/，交给 Nginx 托管
```

Nginx 参考配置：

```nginx
server {
    listen 80;
    server_name your-domain.com;

    root /var/www/mood-admin/dist;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://127.0.0.1:8081/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

### 3. 小程序发布

1. 将 `mood-front/config/api.js` 的 `baseUrl` 改为已备案的 **HTTPS 域名**
2. 在微信公众平台配置「服务器域名 → request 合法域名」
3. 微信开发者工具「上传」代码 → 公众平台提交审核 → 发布

---

## 九、常见问题

| 问题 | 原因与解决 |
|---|---|
| 图片 / 音乐 404 | `upload.path` 未指向实际目录，或数据库 `file_url` 中的文件名在目录下不存在；检查 `uploads/common`、`uploads/music`、`uploads/product` |
| 音乐提示 `Unable to decode audio data` | 本质是该 URL 返回 404 的 HTML 页面；按上一条修复文件路径 |
| 真机预览请求失败 | `baseUrl` 仍是 `localhost`；改成电脑局域网 IP，手机与电脑同一 WiFi，并关闭电脑防火墙对 8080 的拦截 |
| 管理端登录 401 / 跳回登录页 | Token 过期或后端未启动；重新登录，确认 8081 正常 |
| 小程序接口返回 401 | 未登录或登录已过期，重新进入登录页 |
| AI 提示「未配置 AI」 | 进入 AI 配置页填写 Base URL、API Key、模型 |
| AI 提示「模型不可用」 | Key 无效、余额不足或模型名错误，检查配置 |
| 端口被占用 | 8080 / 8081 / 3000 已被其它程序占用，结束占用进程或修改 `application.yml` 与 `vite.config.js` 端口 |
| 首次启动很慢 | Maven 下载依赖与 Spring Boot 编译耗时，属正常现象 |

---

## 十、开发约定

- 小程序 UI 统一使用 `styles/healing.wxss` 与 `app.wxss` 中的 `--mood-*` 设计令牌，保持暖色治愈风格
- 立体按钮统一为「渐变 + 底部实心投影 + inset 高光」，主按钮使用珊瑚杏色系
- 所有请求统一走 `mood-front/utils/request.js`，自动携带 JWT 并处理 401
- 媒体地址统一通过 `utils/url.js` 的 `resolveMediaUrl` 处理，兼容历史 `localhost` 数据
