const { request } = require('../../utils/request')
const config = require('../../config/api')

// 快捷选项文案池（每次进入随机抽取 3 条）
const QUICK_OPTION_POOL = [
  '我有点难过',
  '我有点焦虑',
  '我想分享开心的事',
  '我最近压力好大',
  '我感觉很孤独',
  '我遇到烦心事了',
  '今天心情不错',
  '我有点迷茫',
  '我有点生气',
  '想找人说说话'
]

Page({
  data: {
    messages: [],
    inputValue: '',
    isLoading: false,
    scrollToId: '',
    userAvatar: '',
    currentModel: '',
    quickOptions: [],
    showQuickOptions: true,
    // 配置提示弹窗
    showConfigModal: false,
    configModalText: '',
    configModalShowGoBtn: false
  },

  onLoad() {
    this.loadUserInfo()
  },

  onShow() {
    // 每次显示时加载历史消息和模型配置
    this.loadHistory()
    this.loadConfig()
    this.generateQuickOptions()
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

  // 加载当前模型配置（用于顶部显示）
  async loadConfig() {
    try {
      const res = await request({ url: config.api.aiConfigGet, silent: true })
      if (res.code === 200 && res.data && res.data.model) {
        this.setData({ currentModel: res.data.model })
      } else {
        this.setData({ currentModel: '' })
      }
    } catch (err) {
      this.setData({ currentModel: '' })
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

  // 点击小喵头像进入 AI 配置
  goConfig() {
    wx.navigateTo({ url: '/pages/ai-cat/config' })
  },

  // 关闭配置提示弹窗
  onCloseConfigModal() {
    this.setData({ showConfigModal: false })
  },

  // 前往配置
  onGoConfig() {
    this.setData({ showConfigModal: false })
    wx.navigateTo({ url: '/pages/ai-cat/config' })
  },

  // 随机生成快捷选项
  generateQuickOptions() {
    const pool = QUICK_OPTION_POOL.slice()
    const options = []
    for (let i = 0; i < 3 && pool.length > 0; i++) {
      const idx = Math.floor(Math.random() * pool.length)
      options.push(pool.splice(idx, 1)[0])
    }
    this.setData({ quickOptions: options, showQuickOptions: true })
  },

  // 点击快捷选项，填入输入框并隐藏选项
  onQuickTap(e) {
    const text = e.currentTarget.dataset.text
    this.setData({ inputValue: text, showQuickOptions: false })
  },

  // 发送消息
  async sendMessage() {
    const message = this.data.inputValue.trim()
    if (!message || this.data.isLoading) return

    // 添加用户消息到列表
    const userMessage = {
      role: 'user',
      content: message
    }

    this.setData({
      messages: [...this.data.messages, userMessage],
      inputValue: '',
      isLoading: true
    })

    this.scrollToBottom()

    try {
      const res = await request({
        url: config.api.aiChatSend,
        method: 'POST',
        data: { message },
        silent: true
      })

      // 添加 AI 回复
      const aiMessage = {
        role: 'assistant',
        content: res.data.reply
      }

      this.setData({
        messages: [...this.data.messages, aiMessage],
        isLoading: false
      })

      this.scrollToBottom()
    } catch (err) {
      console.log('发送消息失败', err)
      // 移除乐观加入的用户消息，恢复输入框内容
      const messages = this.data.messages.slice(0, -1)
      this.setData({ isLoading: false, messages, inputValue: message })

      if (err && err.code === 4001) {
        // 未配置模型
        this.setData({
          showConfigModal: true,
          configModalText: '当前模型没有配置，请先配置模型',
          configModalShowGoBtn: true
        })
      } else if (err && err.code === 4002) {
        // 模型不可用
        this.setData({
          showConfigModal: true,
          configModalText: err.message || '当前模型不可用，请检查一下',
          configModalShowGoBtn: false
        })
      } else {
        wx.showToast({
          title: '网络异常，请重试',
          icon: 'none'
        })
      }
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
