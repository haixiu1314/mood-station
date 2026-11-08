const { request } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    userId: null,
    userInfo: {},
    activeTab: 'diary',
    diaries: [],
    messages: [],
    messageContent: '',
    currentUserId: null
  },

  onLoad(options) {
    if (options.userId) {
      this.setData({ userId: options.userId })
      // 获取当前登录用户ID
      const app = getApp()
      const userInfo = app.globalData.userInfo || wx.getStorageSync('userInfo')
      if (userInfo && userInfo.id) {
        this.setData({ currentUserId: userInfo.id })
      }
      this.loadUserProfile()
      this.loadUserDiaries()
      this.loadMessages()
    }
  },

  // 加载用户信息
  async loadUserProfile() {
    try {
      const res = await request({
        url: `${config.api.userPublicInfo}${this.data.userId}`
      })
      this.setData({ userInfo: res.data })
    } catch (err) {
      console.error('加载用户信息失败', err)
    }
  },

  // 加载用户公开日记
  async loadUserDiaries() {
    try {
      const res = await request({
        url: `${config.api.diaryPublic}${this.data.userId}`
      })
      this.setData({ diaries: res.data || [] })
    } catch (err) {
      console.error('加载日记失败', err)
    }
  },

  // 加载留言列表
  async loadMessages() {
    try {
      const res = await request({
        url: config.api.messageReceived
      })
      this.setData({ messages: res.data || [] })
    } catch (err) {
      console.error('加载留言失败', err)
    }
  },

  // Tab 切换
  onTabChange(e) {
    const tab = e.currentTarget.dataset.tab
    this.setData({ activeTab: tab })
  },

  // 留言输入
  onMessageInput(e) {
    this.setData({ messageContent: e.detail.value })
  },

  // 发送留言
  async onSendMessage() {
    const { messageContent, userId, currentUserId } = this.data
    
    if (!messageContent.trim()) {
      wx.showToast({ title: '请输入留言内容', icon: 'none' })
      return
    }

    if (userId === currentUserId) {
      wx.showToast({ title: '不能给自己留言', icon: 'none' })
      return
    }

    try {
      await request({
        method: 'POST',
        url: config.api.messageSend,
        data: {
          toUserId: userId,
          content: messageContent.trim()
        }
      })

      wx.showToast({ title: '留言成功', icon: 'success' })
      this.setData({ messageContent: '' })
      this.loadMessages()
    } catch (err) {
      wx.showToast({ title: err.data?.message || '留言失败', icon: 'none' })
    }
  },

  // 发送抱抱
  async onSendHug() {
    const { userId, currentUserId } = this.data

    if (userId === currentUserId) {
      wx.showToast({ title: '不能给自己抱抱', icon: 'none' })
      return
    }

    try {
      const res = await request({
        method: 'POST',
        url: config.api.messageHug,
        data: {
          toUserId: userId
        }
      })

      wx.showModal({
        title: '抱抱发送成功',
        content: res.data?.hugContent || '温暖的抱抱已送达~',
        showCancel: false
      })

      this.loadMessages()
    } catch (err) {
      wx.showToast({ title: err.data?.message || '抱抱失败', icon: 'none' })
    }
  }
})
