const { request } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    messages: [],
    inputValue: '',
    isLoading: false,
    scrollToId: '',
    todayCount: 0,
    dailyLimit: 50,
    // 用户信息
    userAvatar: ''
  },

  onLoad() {
    this.loadTodayCount()
    this.loadUserInfo()
  },

  onShow() {
    // 每次显示时加载历史消息
    this.loadHistory()
  },

  // 加载用户信息（头像）
  loadUserInfo() {
    const app = getApp()
    const userInfo = app.globalData.userInfo || wx.getStorageSync('userInfo')
    if (userInfo) {
      this.setData({
        userAvatar: userInfo.avatarUrl || ''
      })
    }
  },

  // 加载今日聊天次数
  async loadTodayCount() {
    try {
      const res = await request({
        url: config.api.aiChatTodayCount,
        method: 'GET'
      })
      if (res.code === 200) {
        this.setData({
          todayCount: res.data.todayCount || 0,
          dailyLimit: res.data.dailyLimit || 50
        })
      }
    } catch (err) {
      console.log('获取今日次数失败', err)
    }
  },

  // 加载聊天历史
  async loadHistory() {
    try {
      const res = await request({
        url: config.api.aiChatHistory,
        method: 'GET'
      })
      if (res.code === 200) {
        // 过滤掉 system 消息，只显示 user 和 assistant
        const messages = (res.data || []).filter(item => item.role === 'user' || item.role === 'assistant')
        this.setData({ messages })
        this.scrollToBottom()
      }
    } catch (err) {
      console.log('加载历史消息失败', err)
    }
  },

  // 输入框变化
  onInputChange(e) {
    this.setData({
      inputValue: e.detail.value
    })
  },

  // 发送消息
  async sendMessage() {
    const message = this.data.inputValue.trim()
    if (!message || this.data.isLoading) return

    // 检查次数限制
    if (this.data.todayCount >= this.data.dailyLimit) {
      wx.showToast({
        title: '今日聊天次数已达上限',
        icon: 'none'
      })
      return
    }

    // 添加用户消息到列表
    const userMessage = {
      role: 'user',
      content: message
    }

    const newMessages = [...this.data.messages, userMessage]
    this.setData({
      messages: newMessages,
      inputValue: '',
      isLoading: true
    })

    this.scrollToBottom()

    try {
      const res = await request({
        url: config.api.aiChatSend,
        method: 'POST',
        data: { message }
      })

      if (res.code === 200) {
        // 添加 AI 回复
        const aiMessage = {
          role: 'assistant',
          content: res.data.reply
        }

        this.setData({
          messages: [...this.data.messages, aiMessage],
          todayCount: res.data.todayCount || this.data.todayCount + 1,
          isLoading: false
        })

        this.scrollToBottom()
      } else {
        this.setData({ isLoading: false })
        wx.showToast({
          title: res.message || '发送失败',
          icon: 'none'
        })
      }
    } catch (err) {
      console.log('发送消息失败', err)
      this.setData({ isLoading: false })
      wx.showToast({
        title: '网络异常，请重试',
        icon: 'none'
      })
    }
  },

  // 滚动到底部
  scrollToBottom() {
    setTimeout(() => {
      const messages = this.data.messages
      if (messages.length > 0) {
        this.setData({
          scrollToId: `msg-${messages.length - 1}`
        })
      }
    }, 100)
  }
})
