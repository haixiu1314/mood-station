# 情绪小站 用户端 UI 优化记录

> 记录日期：2026-11-22
> 适用范围：仅 `mood-front`（微信原生小程序，用户端）
> 用途：下次继续优化时先读本文档，明确「哪些不能动」「哪些已统一」「哪些是坑」。

---

## 一、项目范围与不可改动项

| 目录 | 说明 | 规则 |
| --- | --- | --- |
| `E:\mood-project\mood-front` | 用户端小程序 | **唯一允许修改** |
| `mood-admin_front` | 管理端前端 | **禁止改动** |
| `mood-admin_later` | 管理端 Java 后端 | **禁止改动** |
| `mood-later` | 用户端 Java 后端 | **禁止改动**（只可给 SQL / 建议） |

以下内容属于「已定稿资产」，后续优化**不要改**：

1. **图标资源**：`images/`、`images/healing/`、`images/content/`、`images/avatars/` 下所有文件——不改名、不删除、不替换（除非用户明确指定新图）。
2. **每日锦囊翻牌动画**：多圈快速翻转后缓停（`.tip-front` 0 → -900deg，`.tip-back` 180deg → -720deg，`1.2s cubic-bezier(0.16,1,0.3,1)`）。
3. **锦囊翻牌后的星星特效**：`.star` 1~5 的 `tip-twinkle` 闪烁。
4. **尺寸适配**：按 iPhone 12/13（390×844，750rpx 设计稿）比例布局，新增元素不要突破该比例。
5. **整体治愈暖色方向**：用户明确否决过「纯黑」「暗绿」「Linear 暗色 / codex 暗绿」，不要回退到暗色。

---

## 二、图标规范（重点，最容易踩坑）

### 2.1 现有文件名实测清单（`mood-front/images/`）

**tabBar 四组（未选中 / 选中）**

- `首页.png` / `首页-选中.png`
- `情绪分享.png` / `情绪分享-选中.png`
- `情绪周边.png` / `情绪周边-选中.png`
- `我的.png` / `我的-选中.png`

**页面功能图标**

- `我的日记.png`、`我的订单.png`、`我的帖子.png`、`我的留言.png`、`我的收货地址.png`
- `我的-消息通知.png`、`我的-音乐.png`
- `编辑资料.png`、`退出登录.png`、`心动币.png`、`礼物.png`、`购物车.png`、`回收站.png`
- `留言板.png`、`抱抱.png`
- `猫猫.png`、`橘猫.png`
- `白天.png`、`黑夜.png`
- `锦囊.png`、`锦囊_激活.png`

### 2.2 易错点（务必遵守）

1. **`锦囊_激活.png` 是带下划线的**，不是 `锦囊激活.png`。写错会白图/裂图。
2. WXML 引用本地图必须写完整绝对路径，以 `/` 开头代表项目根：
   ```html
   <image src="/images/锦囊.png" mode="aspectFit" />
   ```
3. tabBar 图标只能写在 `app.json` 的 `tabBar.list` 里，**不能用网络图、不能用 emoji**。
4. **禁止用字符/emoji 代替图标**。例：发帖子浮动按钮的 `+` 是文字符号，只作为该按钮特例保留；其他位置的功能图标一律用 `images/` 下的 PNG。
5. `utils/media.wxs` 是**渲染层 WXS**：不能 `require` JS、不能运行时拼 URL、不能用 `wx.*`。新增映射只能硬编码进 `localImages` / `localAvatars` 两个对象。
6. 所有动态图片统一走 WXS 两个函数，不要手写拼接：
   - 头像：`{{media.avatar(item.author.avatar, item.author.avatarUrl)}}`
   - 内容图/帖子图：`{{media.src(img)}}`
   - 头像函数无参或空值时兜底 `/images/healing/avatar.png`。
7. **不要重命名现有图片**：tabBar 与多个 wxss 都按文件名引用，改名会连锁失效。
8. 新增图片放对应目录：功能图标放 `images/` 根目录，内容占位图放 `images/content/`，头像图放 `images/avatars/`。

### 2.3 今日已完成的图标替换（不要回退）

- tabBar 四个 tab 全部换成 `images/xxx.png` + `xxx-选中.png`。
- 首页右上角：按本地时间切换 `白天.png`（6:00–17:59）/ `黑夜.png`（18:00–5:59）。
- 每日锦囊：默认 `锦囊.png`；点击时**先**切到 `锦囊_激活.png`，**再**触发翻转。
- 我的页面功能入口：日记、订单、帖子、留言、收货地址、消息通知、音乐、编辑资料、退出登录、心动币、礼物、购物车、回收站、留言板、抱抱。

---

## 三、配色令牌（`app.wxss`）

所有页面颜色必须走 CSS 变量，禁止新写死色值（立体按钮的深色投影除外）。

| 变量 | 值 | 用途 |
| --- | --- | --- |
| `--mood-paper` | `#FBF6EF` | 页面背景（暖奶油米白） |
| `--mood-surface` | `#FFFFFF` | 卡片 / 面板背景 |
| `--mood-well` | `#F5ECE0` | 凹陷输入框 / 内嵌表面 |
| `--mood-sage-soft` | `#FCEFE5` | 轻微填充（头部、徽章、次按钮） |
| `--mood-ink` | `#3E332A` | 主文字（暖深棕，非纯黑） |
| `--mood-muted` | `#8A7A68` | 次级文字 |
| `--mood-faint` | `#B4A793` | 三级文字 |
| `--mood-sage` | `#E98D6F` | 品牌珊瑚：主 CTA |
| `--mood-sage-ink` | `#D9774F` | 强调珊瑚：链接 / 激活 / 价格 |
| `--mood-line` | `#EFE3D2` | 暖米边框 |
| `--mood-rose` | `#E4706B` | 生气 / 删除 |
| `--mood-honey` | `#E3A857` | 开心 / 金色 |
| `--mood-blue` | `#7FA7C9` | 难过 |

**历史隐藏 bug 提醒**：`var(--mood-surface)` 是白色，**不能**用它当浅色背景上的按钮文字色（会白字白底看不见）。按钮文字统一用 `#FFFFFF` 或 `var(--mood-sage-ink)`。

---

## 四、立体按钮统一规范

统一公式：**渐变背景 + 底部实心投影 + 顶部 inset 高光 + 循环扫光**。
基类样式集中在 `styles/healing.wxss`（`.mood-page .btn-*` 系列），页面级按钮在各自 wxss 中用同名规则覆盖。

| 类型 | 背景 | 投影 / 阴影 | 文字 |
| --- | --- | --- | --- |
| 主按钮（珊瑚） | `linear-gradient(180deg,#F0A184 0%,#E98D6F 100%)` | `0 6rpx 0 #C96A45, 0 10rpx 20rpx rgba(217,119,79,.28), inset 0 2rpx 0 rgba(255,255,255,.28)` | `#FFFFFF` |
| 次按钮（杏米） | `var(--mood-sage-soft)` | `0 4rpx 0 #EADBC8, inset 0 2rpx 0 rgba(255,255,255,.7)` | `var(--mood-sage-ink)` |
| 危险 / 删除 | `var(--mood-rose-soft)` | `0 4rpx 0 #F0C9C4, inset 0 2rpx 0 rgba(255,255,255,.7)` | `var(--mood-rose)` |
| 金色（抱抱） | `linear-gradient(180deg,#F0C27A 0%,#E3A857 100%)` | `0 6rpx 0 #C88D3E, 0 10rpx 20rpx rgba(227,168,87,.3), inset 0 2rpx 0 rgba(255,255,255,.35)` | `#FFFFFF` |
| 治愈绿（锦囊背面） | `linear-gradient(180deg,#6BA184 0%,#4A7C59 100%)` | `0 8rpx 0 #3D6649, 0 14rpx 28rpx rgba(74,124,89,.28), inset 0 2rpx 0 rgba(255,255,255,.22)` | `#FFFFFF` |

**扫光动画**（主按钮 / 发帖按钮）：

```css
.btn::after {
  content: '';
  position: absolute; top: -60%; left: -90%;
  width: 45%; height: 220%;
  background: linear-gradient(105deg, transparent 0%, rgba(255,255,255,.65) 50%, transparent 100%);
  transform: skewX(-20deg);
  animation: btn-shine 2.6s ease-in-out infinite;
}
@keyframes btn-shine { 0% { left: -90%; } 55%, 100% { left: 130%; } }
```

**已统一立体化的按钮**

- 首页：写日记、每日锦囊卡片（正面/背面）
- 情绪分享：发帖子圆形按钮（`.publish-btn`）
- 分享详情：写下你的评论（`.comment-trigger-btn`）
- 收货地址：新增收货地址（`.add-btn`）
- 留言板 / 用户主页：抱抱（金色 `.btn-hug`）
- 全局：`healing.wxss` 中 `.btn-save`、`.btn-publish`、`.btn-login`、`.btn-register`、`.save-btn`、`.submit-btn`、`.btn-buy`、`.btn-submit`、`.btn-checkout`、`.btn-confirm`、`.btn-send` 等

**特例（不再改）**：`写日记`、`发帖子`、`每日锦囊` 三个按钮样式已定稿，后续只做「统一其他按钮向它们看齐」，不要反向修改它们。

---

## 五、2026-11-22 改动清单

| # | 需求 | 涉及文件 | 做法 |
| --- | --- | --- | --- |
| 1 | 锦囊卡片立体感 + 背面治愈绿 | `pages/index/index.wxss` | `.tip-front` / `.tip-back` 加立体阴影；背面改绿渐变 `#6BA184→#4A7C59`；图标 118rpx + drop-shadow |
| 2 | 发帖子按钮立体化 | `pages/share/index.wxss` | `.publish-btn` 改珊瑚渐变 + `0 8rpx 0 #C96A45` + 扫光 |
| 3 | 下拉刷新 | `app.json` + `index` / `share/index` / `profile/index` / `order/list` / `notification/list` | 开启 `enablePullDownRefresh`；各页加 `onPullDownRefresh()`，数据 Promise `.finally()` 里 `wx.stopPullDownRefresh()` |
| 4 | 跳转先显示「加载中」 | `utils/request.js` | `loadingCount` 计数：首个请求 `wx.showLoading`，最后一个 `wx.hideLoading`；`success` 与 `fail` 都要 `endLoading()`（`uploadFile` 不参与计数） |
| 5 | 其他功能按钮统一立体 | `styles/healing.wxss`、`share/detail.wxss`、`address/list.wxss`、`user/profile.wxss` | 按第四节规范统一 |
| 6 | 帖子图片 404 | `utils/media.wxs` | `src()` 返回 `localImages[path] \|\| localAvatars[path] \|\| value`，让头像映射也能兜底帖子图 |
| 7 | 情绪分享页下滑虚化渐显 | `share/index.wxml` / `.js` / `.wxss` | 帖子项默认 `opacity:0; filter:blur(12rpx)`，`IntersectionObserver` 进入视口后加 `.revealed` 渐显；图片加 `lazy-load` |
| 8 | 编辑资料昵称左对齐 | `pages/profile/edit.wxss` | `.form-input` 的 `text-align: right` → `left` |

---

## 六、图片 404 问题（重要遗留）

**现象**：`Failed to load image http://localhost:8080/uploads/xxx.jpg 404`

**根因**：后端返回的图片 URL 是**硬编码 `http://localhost:8080`**。真机上 `localhost` 指向手机本身，必然 404；PC 开发者工具能显示只是因为本机恰好跑着后端。

**已完成（前端兜底）**：`utils/media.wxs` 中的 `localImages` / `localAvatars` 映射，把历史 demo 图替换成本地文件；`src()` 已支持把 `localhost` / `127.0.0.1` 绝对地址截成 `/uploads/...` 再查表。

**未彻底解决**：用户新上传、且不在任何映射表里的图片仍会 404。

**两个方案（涉及后端，需用户确认后才能动）**
1. 后端改为返回相对路径 `/uploads/xxx.jpg`，前端用 `config.baseUrl` 拼接（最干净，但要改 Java）。
2. 前端 WXS 里检测到 `http://localhost:8080/` 前缀时替换为真实 Host（不动后端，但需要真实可访问的 IP/域名）。

**规则**：遇到图片 404，**不要**在前端随意改图片组件或加 hack；先判断该图是否在映射表里，不在就按上面两方案上报。

---

## 七、加载与刷新约定

1. `utils/request.js` 的 `beginLoading()` / `endLoading()` **必须成对**：
   - `request()` 开头 `beginLoading()`；
   - `success()` 第一行、`fail()` 第一行都要 `endLoading()`；
   - `uploadFile()` 不调用 `beginLoading()`，所以其 `fail` 里**不要**加 `endLoading()`（会破坏计数）。
2. 全局 loading 文案固定「加载中」，`mask: false`，不阻塞操作。
3. 新增任何列表页，都需要：
   - `onPullDownRefresh()`：调用对应加载函数，`finally(() => wx.stopPullDownRefresh())`。
4. 页面跳转走 `wx.navigateTo` / `wx.redirectTo` 即可，**不要**在跳转前手动 `showLoading`——统一由 `request.js` 接管，避免重复 loading。

---

## 八、下次优化自检清单

- [ ] 是否只改了 `mood-front`？管理端和两个 Java 后端是否零改动？
- [ ] 新增图标是否使用 `images/` 下真实 PNG？是否核对过文件名（特别是 `锦囊_激活.png` 的下划线）？
- [ ] 是否有地方用 emoji / 文字符号替代了图标？
- [ ] 颜色是否全部走 `var(--mood-*)`？有没有引入纯黑或暗绿？
- [ ] 新按钮是否套用了第四节的立体规范（渐变 + 底部实心投影 + inset 高光）？
- [ ] 按钮文字是否避开了白色配浅底？
- [ ] `request.js` 的 loading 计数是否仍然平衡？
- [ ] 新增列表页是否补了 `onPullDownRefresh`？
- [ ] 锦囊翻牌动画、星星特效、白天/黑夜图标是否被误改？
- [ ] 390×844 比例是否仍然正常（无横向滚动、无内容溢出）？
- [ ] 动态图片是否走 `media.src()` / `media.avatar()`？

---

## 九、当前遗留 / 待办

1. **图片 404 彻底解决**：需后端配合（见第六节），当前仅前端映射兜底。
2. **按钮覆盖度复查**：建议用 `grep` 搜索各详情页的 `button` / `bindtap` 元素，确认是否还有未套用立体规范的漏网按钮（如商品详情、购物车、订单详情、日记详情等）。
3. **真机验证**：下拉刷新、虚化渐显、扫光动画在真机（非开发者工具）上的表现需再确认一次。
