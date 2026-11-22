const { request } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    content: ''
  },

  onInput(e) {
    this.setData({ content: e.detail.value })
  },

  async onSubmit() {
    const content = this.data.content.trim()
    if (!content) {
      wx.showToast({ title: '请输入反馈内容', icon: 'none' })
      return
    }

    try {
      await request({
        url: config.api.feedbackSubmit,
        method: 'POST',
        data: { content }
      })
      wx.showToast({ title: '反馈提交成功', icon: 'success' })
      setTimeout(() => {
        wx.navigateBack()
      }, 1500)
    } catch (err) {
      console.error('提交反馈失败', err)
    }
  }
})
