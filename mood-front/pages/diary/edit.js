const { request } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    id: '',
    emotion: 'happy',
    emotionLabel: '开心',
    emotionIndex: 0,
    title: '',
    content: '',
    isPublic: 0,
    isForward: false,
    emotions: [
      { value: 'happy', label: '开心', bg: '#FFF3CD', color: '#D4A017' },
      { value: 'sad', label: '难过', bg: '#E8F0FE', color: '#6B8DD6' },
      { value: 'angry', label: '生气', bg: '#FFF0F0', color: '#FF8B94' },
      { value: 'calm', label: '平静', bg: '#DCEDC1', color: '#2E7D32' },
      { value: 'anxious', label: '焦虑', bg: '#F3E5F5', color: '#9C27B0' }
    ]
  },

  onLoad(options) {
    if (options.type === 'forward') {
      this.setData({ isForward: true })
    }
    if (options.id) {
      this.setData({ id: options.id })
      this.loadDiary(options.id)
    }
  },

  async loadDiary(id) {
    try {
      const res = await request({ url: config.api.diaryDetail + id })
      const diary = res.data
      const emotionIndex = this.data.emotions.findIndex(e => e.value === diary.emotion)
      this.setData({
        emotion: diary.emotion,
        emotionLabel: diary.emotionLabel,
        emotionIndex: emotionIndex >= 0 ? emotionIndex : 0,
        title: diary.title || '',
        content: diary.content,
        isPublic: diary.isPublic
      })
    } catch (err) {
      console.error('加载日记失败', err)
    }
  },

  onEmotionChange(e) {
    const index = e.detail.value
    const emotion = this.data.emotions[index]
    this.setData({
      emotion: emotion.value,
      emotionLabel: emotion.label,
      emotionIndex: index
    })
  },

  onTitleInput(e) {
    this.setData({ title: e.detail.value })
  },

  onContentInput(e) {
    const value = e.detail.value
    if (value.length > 5000) {
      wx.showToast({ title: '最多5000字', icon: 'none' })
      this.setData({ content: value.substring(0, 5000) })
    } else {
      this.setData({ content: value })
    }
  },

  onPublicChange(e) {
    this.setData({ isPublic: e.detail.value ? 1 : 0 })
  },

  async onSave() {
    if (!this.data.title.trim()) {
      wx.showToast({ title: '请输入标题', icon: 'none' })
      return
    }
    if (this.data.title.length > 18) {
      wx.showToast({ title: '标题不能超过18字', icon: 'none' })
      return
    }
    if (!this.data.content.trim()) {
      wx.showToast({ title: '请输入日记内容', icon: 'none' })
      return
    }

    try {
      const data = {
        emotion: this.data.emotion,
        emotionLabel: this.data.emotionLabel,
        title: this.data.title,
        content: this.data.content,
        images: '',
        isPublic: this.data.isPublic
      }

      if (this.data.id) {
        await request({
          method: 'PUT',
          url: config.api.diaryUpdate + this.data.id,
          data
        })
        wx.showToast({ title: '更新成功', icon: 'success' })
      } else {
        const result = await request({
          method: 'POST',
          url: config.api.diaryCreate,
          data
        })
        if (result.data && result.data.rewardMessage) {
          wx.showModal({
            title: '奖励通知',
            content: result.data.rewardMessage,
            showCancel: false,
            confirmText: '知道了'
          })
        } else {
          wx.showToast({ title: '保存成功', icon: 'success' })
        }
      }

      setTimeout(() => {
        wx.navigateBack()
      }, 1500)
    } catch (err) {
      console.error('保存日记失败', err)
    }
  },

  async onDelete() {
    if (!this.data.id) return
    
    wx.showModal({
      title: '提示',
      content: '确定要删除这篇日记吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await request({
              method: 'DELETE',
              url: config.api.diaryDelete + this.data.id
            })
            wx.showToast({ title: '删除成功', icon: 'success' })
            setTimeout(() => {
              wx.navigateBack()
            }, 1500)
          } catch (err) {
            console.error('删除日记失败', err)
          }
        }
      }
    })
  }
})
