const { request } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    activeTab: 'system',
    notifications: [],
    systemUnread: 0,
    messageUnread: 0
  },

  onLoad() {
    this.loadUnreadCount()
    this.loadNotifications()
  },

  onShow() {
    this.loadUnreadCount()
    this.loadNotifications()
  },

  async loadUnreadCount() {
    try {
      const res = await request({ url: config.api.notificationUnreadCountByType })
      const data = res.data || {}
      this.setData({
        systemUnread: data.system || 0,
        messageUnread: data.message || 0
      })
    } catch (err) {
      console.error('加载未读数量失败', err)
    }
  },

  async loadNotifications() {
    try {
      const res = await request({
        url: config.api.notificationList,
        data: { type: this.data.activeTab }
      })
      const list = (res.data || []).map(item => ({
        ...item,
        createTime: this.formatTime(item.createTime)
      }))
      this.setData({ notifications: list })
    } catch (err) {
      console.error('加载通知失败', err)
    }
  },

  onSwitchTab(e) {
    const tab = e.currentTarget.dataset.tab
    this.setData({ activeTab: tab })
    this.loadNotifications()
  },

  async onRead(e) {
    const id = e.currentTarget.dataset.id
    try {
      await request({
        url: `${config.api.notificationRead}/${id}`,
        method: 'PUT'
      })
      const notifications = this.data.notifications.map(item => {
        if (item.id === id) {
          return { ...item, isRead: 1 }
        }
        return item
      })
      this.setData({ notifications })
      this.loadUnreadCount()
    } catch (err) {
      console.error('标记已读失败', err)
    }
  },

  formatTime(timeStr) {
    if (!timeStr) return ''
    const date = new Date(timeStr.replace('T', ' '))
    const now = new Date()
    const diff = now - date
    const minutes = Math.floor(diff / 60000)
    const hours = Math.floor(diff / 3600000)
    const days = Math.floor(diff / 86400000)

    if (minutes < 1) return '刚刚'
    if (minutes < 60) return `${minutes}分钟前`
    if (hours < 24) return `${hours}小时前`
    if (days < 7) return `${days}天前`
    
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    return `${month}-${day}`
  }
})
