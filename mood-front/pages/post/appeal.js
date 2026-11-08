const { request } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    trashId: null,
    reason: ''
  },

  onLoad(options) {
    this.setData({
      trashId: options.trashId
    })
  },

  onReasonInput(e) {
    this.setData({ reason: e.detail.value })
  },

  async onSubmit() {
    if (!this.data.reason.trim()) {
      wx.showToast({ title: '请输入申诉原因', icon: 'none' })
      return
    }

    try {
      await request({
        url: config.api.postAppeal,
        method: 'POST',
        data: {
          postId: this.data.trashId,
          reason: this.data.reason
        }
      })
      wx.showToast({ title: '申诉提交成功', icon: 'success' })
      setTimeout(() => {
        wx.navigateBack()
      }, 1500)
    } catch (err) {
      wx.showToast({ title: err.data?.message || '申诉提交失败', icon: 'none' })
    }
  }
})
