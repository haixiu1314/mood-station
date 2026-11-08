const { request } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    activeTab: 'received',
    receivedMessages: [],
    sentMessages: []
  },

  onLoad() {
    this.loadReceivedMessages()
    this.loadSentMessages()
  },

  onShow() {
    this.loadReceivedMessages()
    this.loadSentMessages()
  },

  // Tab 切换
  onTabChange(e) {
    const tab = e.currentTarget.dataset.tab
    this.setData({ activeTab: tab })
    
    // 切换到收到的留言时，标记全部已读
    if (tab === 'received') {
      this.markAllAsRead()
    }
  },

  // 加载收到的留言
  async loadReceivedMessages() {
    try {
      const res = await request({
        url: config.api.messageReceived
      })
      this.setData({ receivedMessages: res.data || [] })
    } catch (err) {
      console.error('加载收到的留言失败', err)
    }
  },

  // 加载发出的留言
  async loadSentMessages() {
    try {
      const res = await request({
        url: config.api.messageSent
      })
      this.setData({ sentMessages: res.data || [] })
    } catch (err) {
      console.error('加载发出的留言失败', err)
    }
  },

  // 标记全部已读
  async markAllAsRead() {
    try {
      await request({
        method: 'PUT',
        url: config.api.messageReadAll
      })
    } catch (err) {
      console.error('标记已读失败', err)
    }
  }
})
