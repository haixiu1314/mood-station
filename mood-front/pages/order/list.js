const { request } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    orders: [],
    activeTab: 'all',
    tabs: [
      { value: 'all', label: '全部' },
      { value: 'pending', label: '待发货' },
      { value: 'shipped', label: '已发货' },
      { value: 'completed', label: '已完成' },
      { value: 'refunded', label: '已退款' }
    ]
  },

  onLoad() {
    this.loadOrders()
  },

  onShow() {
    this.loadOrders()
  },

  onTabChange(e) {
    const tab = e.currentTarget.dataset.tab
    this.setData({ activeTab: tab })
    this.loadOrders(tab)
  },

  async loadOrders(status) {
    try {
      let url = config.api.orderList
      if (status && status !== 'all') {
        url = config.api.orderByStatus + status
      }
      
      const res = await request({ url })
      this.setData({
        orders: res.data || []
      })
    } catch (err) {
      console.error('加载订单失败', err)
    }
  },

  onOrderTap(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({
      url: `/pages/order/detail?id=${id}`
    })
  },

  // 取消订单
  onCancelOrder(e) {
    const id = e.currentTarget.dataset.id
    wx.showModal({
      title: '取消订单',
      content: '确定要取消该订单吗？心动币将自动退回',
      confirmText: '取消订单',
      confirmColor: '#FF6B6B',
      success: async (res) => {
        if (res.confirm) {
          try {
            await request({
              url: `${config.api.orderCancel}${id}`,
              method: 'POST'
            })
            wx.showToast({ title: '订单已取消', icon: 'success' })
            this.loadOrders(this.data.activeTab)
          } catch (err) {
            wx.showToast({ title: err.data?.message || '取消失败', icon: 'none' })
          }
        }
      }
    })
  },

  // 申请退款
  onApplyRefund(e) {
    const id = e.currentTarget.dataset.id
    wx.showModal({
      title: '申请退款',
      editable: true,
      placeholderText: '请输入退款原因',
      success: async (res) => {
        if (res.confirm) {
          const reason = res.content?.trim()
          if (!reason) {
            wx.showToast({ title: '请输入退款原因', icon: 'none' })
            return
          }
          try {
            await request({
              url: `${config.api.orderRefund}${id}`,
              method: 'POST',
              data: { reason }
            })
            wx.showToast({ title: '退款申请已提交', icon: 'success' })
            this.loadOrders(this.data.activeTab)
          } catch (err) {
            wx.showToast({ title: err.data?.message || '申请失败', icon: 'none' })
          }
        }
      }
    })
  },

  // 确认收货
  onConfirmReceived(e) {
    const id = e.currentTarget.dataset.id
    wx.showModal({
      title: '确认收货',
      content: '请确认收到的商品无误再确认收货，收货后将无法退款',
      confirmText: '确认收货',
      success: async (res) => {
        if (res.confirm) {
          try {
            const result = await request({
              url: `${config.api.orderConfirm}${id}`,
              method: 'POST'
            })
            if (result.data && result.data.rewardMessage) {
              wx.showModal({
                title: '奖励通知',
                content: result.data.rewardMessage,
                showCancel: false,
                confirmText: '知道了'
              })
            } else {
              wx.showToast({ title: '已确认收货', icon: 'success' })
            }
            this.loadOrders(this.data.activeTab)
          } catch (err) {
            wx.showToast({ title: err.data?.message || '确认失败', icon: 'none' })
          }
        }
      }
    })
  },

  // 查看快递信息
  onShowExpress(e) {
    const item = e.currentTarget.dataset.item
    let content = `快递公司: ${item.expressCompany || '未知'}\n快递单号: ${item.expressNo || '未知'}`
    if (item.senderName) {
      content += `\n发货人: ${item.senderName}`
    }
    if (item.senderPhone) {
      content += `\n联系电话: ${item.senderPhone}`
    }
    if (item.senderAddress) {
      content += `\n发货地址: ${item.senderAddress}`
    }
    wx.showModal({
      title: '快递信息',
      content: content,
      showCancel: false
    })
  }
})
