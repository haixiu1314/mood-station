const { request } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    order: null
  },

  onLoad(options) {
    if (options.id) {
      this.loadOrder(options.id)
    }
  },

  async loadOrder(id) {
    try {
      const res = await request({ url: config.api.orderDetail + id })
      this.setData({ order: res.data })
    } catch (err) {
      console.error('加载订单详情失败', err)
    }
  },

  async onCancelOrder() {
    const { order } = this.data
    wx.showModal({
      title: '提示',
      content: '确定要取消该订单吗？心动币将退回',
      success: async (res) => {
        if (res.confirm) {
          try {
            await request({
              url: `${config.api.orderCancel}${order.id}`,
              method: 'POST'
            })
            wx.showToast({ title: '已取消', icon: 'success' })
            setTimeout(() => wx.navigateBack(), 1000)
          } catch (err) {
            wx.showToast({ title: '取消失败', icon: 'none' })
          }
        }
      }
    })
  },

  async onApplyRefund() {
    const { order } = this.data
    wx.showModal({
      title: '申请退款',
      editable: true,
      placeholderText: '请输入退款原因',
      success: async (res) => {
        if (res.confirm && res.content) {
          try {
            await request({
              url: config.api.orderRefund + order.id,
              method: 'POST',
              data: { reason: res.content }
            })
            wx.showToast({ title: '申请已提交', icon: 'success' })
            this.loadOrder(order.id)
          } catch (err) {
            wx.showToast({ title: '申请失败', icon: 'none' })
          }
        }
      }
    })
  },

  async onConfirmReceived() {
    const { order } = this.data
    wx.showModal({
      title: '提示',
      content: '确认已收到商品？',
      success: async (res) => {
        if (res.confirm) {
          try {
            const result = await request({
              url: `${config.api.orderConfirm}${order.id}`,
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
              wx.showToast({ title: '确认成功', icon: 'success' })
            }
            this.loadOrder(order.id)
          } catch (err) {
            console.error('确认收货失败:', err)
            wx.showToast({ title: err.message || err.data?.message || '操作失败', icon: 'none', duration: 3000 })
          }
        }
      }
    })
  }
})
