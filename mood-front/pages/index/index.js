const { request } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    nickname: '',
    banners: [],
    quote: '',
    quoteFlipped: false,
    tipIconSrc: '/images/锦囊.png',
    timeContext: { icon: '/images/白天.png', tip: '' },
    diaryList: [],
    emotionMap: {
      happy: { label: '开心', bg: '#FFF3CD', color: '#D4A017', icon: '😊' },
      sad: { label: '难过', bg: '#E8F0FE', color: '#6B8DD6', icon: '😢' },
      angry: { label: '生气', bg: '#FFF0F0', color: '#FF8B94', icon: '😠' },
      calm: { label: '平静', bg: '#DCEDC1', color: '#2E7D32', icon: '😌' },
      anxious: { label: '焦虑', bg: '#F3E5F5', color: '#9C27B0', icon: '😰' }
    }
  },

  onLoad() {
    this.setupTimeContext()
    this.checkLogin()
  },

  onShow() {
    this.checkLogin()
  },

  onPullDownRefresh() {
    this.setupTimeContext()
    this.checkLogin()
    setTimeout(() => wx.stopPullDownRefresh(), 600)
  },

  checkLogin() {
    const app = getApp()
    const token = app.globalData.token || wx.getStorageSync('token')
    if (token) {
      this.loadHomeData()
    } else {
      this.setData({
        nickname: '游客',
        banners: [
          { id: 1, image: '/images/轮播图1.jpg', url: '' },
          { id: 2, image: '/images/轮播图2.jpg', url: '' }
        ],
        quote: '每一片乌云背后都有阳光'
      })
    }
  },

  async loadHomeData() {
    try {
      const userInfo = wx.getStorageSync('userInfo')
      this.setData({
        nickname: userInfo?.nickname || 'Hi~'
      })

      const [bannersRes, diaryRes] = await Promise.all([
        request({ url: config.api.homeBanners }).catch(() => ({ data: [] })),
        request({ url: config.api.diaryRecent + '?limit=5' }).catch(() => ({ data: [] }))
      ])

      // 获取今日锦囊（每天只抽取一次）
      const quote = this.getTodayQuote()

      this.setData({
        banners: bannersRes.data || [],
        quote: quote,
        diaryList: diaryRes.data || []
      })
    } catch (err) {
      console.error('加载首页数据失败', err)
    }
  },

  // 获取今日锦囊（每天只抽取一次，本地存储）
  getTodayQuote() {
    const today = this.getTodayString()
    const stored = wx.getStorageSync('todayQuote')

    if (stored && stored.date === today && stored.quote) {
      // 今天已经抽过了，直接返回存储的锦囊
      return stored.quote
    }

    // 今天还没抽，随机抽取一个并存储
    const quotes = [
      '每一片乌云背后都有阳光',
      '今天的你，已经很棒了',
      '慢慢来，一切都会变好的',
      '心若向阳，无畏悲伤',
      '今天的努力，是明天的礼物',
      '愿你被这个世界温柔以待',
      '一切都会好起来的',
      '你值得所有的美好',
      '今天也要元气满满哦',
      '保持热爱，奔赴山海',
      '生活明朗，万物可爱',
      '今天的心情，由你自己决定',
      '愿你每天都有好心情',
      '心之所向，素履以往',
      '今天的你，比昨天更棒了',
      '一切美好，正在路上',
      '今天的阳光，为你而灿烂',
      '愿你笑口常开，心情常在',
      '今天的小确幸，等着你发现',
      '心若安好，便是晴天'
    ]

    const randomIndex = Math.floor(Math.random() * quotes.length)
    const quote = quotes[randomIndex]

    // 存储到本地，带日期
    wx.setStorageSync('todayQuote', {
      date: today,
      quote: quote
    })

    return quote
  },

  // 获取今日日期字符串
  getTodayString() {
    const now = new Date()
    const year = now.getFullYear()
    const month = String(now.getMonth() + 1).padStart(2, '0')
    const day = String(now.getDate()).padStart(2, '0')
    return `${year}-${month}-${day}`
  },

  onBannerTap(e) {
    const url = e.currentTarget.dataset.url
    if (url) {
      // tabBar 页面需要用 switchTab，普通页面用 navigateTo
      const tabBarPages = ['/pages/index/index', '/pages/share/index', '/pages/shop/index', '/pages/profile/index']
      if (tabBarPages.includes(url)) {
        wx.switchTab({ url })
      } else {
        wx.navigateTo({ url })
      }
    }
  },

  // 检测当地时间，切换白天/黑夜图标与温馨提示（纯前端本地时间，无需后端与数据库）
  setupTimeContext() {
    const now = new Date()
    const hour = now.getHours()
    const minute = now.getMinutes()
    const pad = n => String(n).padStart(2, '0')
    const timeStr = `${pad(hour)}:${pad(minute)}`
    const isDay = hour >= 6 && hour < 18
    let tip = ''
    if (hour >= 6 && hour < 9) tip = `现在是 ${timeStr}，可以享用早餐或出门晨跑`
    else if (hour >= 9 && hour < 12) tip = `现在是 ${timeStr}，元气满满，专注做好眼前的事`
    else if (hour >= 12 && hour < 14) tip = `现在是 ${timeStr}，记得吃午餐，给自己充充电`
    else if (hour >= 14 && hour < 18) tip = `现在是 ${timeStr}，喝杯热茶，让心情慢慢舒展`
    else if (hour >= 18 && hour < 22) tip = `现在是 ${timeStr}，好好吃晚餐，陪陪自己`
    else tip = `现在是 ${timeStr}，夜深了，早点休息哦`
    this.setData({
      timeContext: { icon: isDay ? '/images/白天.png' : '/images/黑夜.png', tip }
    })
  },

  onQuoteTap() {
    const flipped = !this.data.quoteFlipped
    // 先切换图标（未点击→锦囊激活图），再执行翻牌
    this.setData({ tipIconSrc: flipped ? '/images/锦囊_激活.png' : '/images/锦囊.png' })
    if (flipped) {
      setTimeout(() => this.setData({ quoteFlipped: true }), 140)
    } else {
      this.setData({ quoteFlipped: false })
    }
  },

  onDiaryTap(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({
      url: `/pages/diary/detail?id=${id}`
    })
  },

  onWriteDiary() {
    wx.navigateTo({
      url: '/pages/diary/edit'
    })
  }
})
