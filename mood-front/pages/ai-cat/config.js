const { request } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    baseUrl: '',
    apiKey: '',
    model: '',
    saving: false
  },

  onLoad() {
    this.loadConfig()
  },

  async loadConfig() {
    try {
      const res = await request({ url: config.api.aiConfigGet })
      if (res.code === 200 && res.data) {
        this.setData({
          baseUrl: res.data.baseUrl || '',
          apiKey: res.data.apiKey || '',
          model: res.data.model || ''
        })
      }
    } catch (err) {
      console.log('加载配置失败', err)
    }
  },

  onBaseUrlInput(e) {
    this.setData({ baseUrl: e.detail.value })
  },

  onApiKeyInput(e) {
    this.setData({ apiKey: e.detail.value })
  },

  onModelInput(e) {
    this.setData({ model: e.detail.value })
  },

  async onSave() {
    const baseUrl = this.data.baseUrl.trim()
    const apiKey = this.data.apiKey.trim()
    const model = this.data.model.trim()
    if (!baseUrl || !apiKey || !model) {
      wx.showToast({ title: '请填写完整的 url、key 和 model', icon: 'none' })
      return
    }
    if (this.data.saving) return
    this.setData({ saving: true })
    try {
      await request({
        url: config.api.aiConfigSave,
        method: 'POST',
        data: { baseUrl, apiKey, model }
      })
      wx.showToast({ title: '保存成功', icon: 'success' })
      setTimeout(() => wx.navigateBack(), 800)
    } catch (err) {
      wx.showToast({ title: err.message || '保存失败', icon: 'none' })
    } finally {
      this.setData({ saving: false })
    }
  }
})
