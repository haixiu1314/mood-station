const { request, uploadFile } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    title: '',
    content: '',
    images: [],
    imageUrls: [],
    source: 'original',
    sourceDiaryId: null,
    diaryId: '',
    emotion: 'happy',
    emotionLabel: '开心',
    emotionIndex: 0,
    emotions: [
      { value: 'happy', label: '开心', bg: '#FFF3CD', color: '#D4A017' },
      { value: 'sad', label: '难过', bg: '#E8F0FE', color: '#6B8DD6' },
      { value: 'angry', label: '生气', bg: '#FFF0F0', color: '#FF8B94' },
      { value: 'calm', label: '平静', bg: '#DCEDC1', color: '#2E7D32' },
      { value: 'anxious', label: '焦虑', bg: '#F3E5F5', color: '#9C27B0' }
    ]
  },

  onLoad(options) {
    if (options.diaryId) {
      this.setData({
        source: 'diary',
        sourceDiaryId: options.diaryId,
        diaryId: options.diaryId
      })
      this.loadDiaryContent(options.diaryId)
    }
  },

  async loadDiaryContent(diaryId) {
    try {
      const res = await request({ url: config.api.diaryDetail + diaryId })
      const diary = res.data
      const emotionIndex = this.data.emotions.findIndex(e => e.value === diary.emotion)
      this.setData({
        title: diary.title || '',
        content: diary.content,
        emotion: diary.emotion || 'happy',
        emotionLabel: diary.emotionLabel || '开心',
        emotionIndex: emotionIndex >= 0 ? emotionIndex : 0
      })
    } catch (err) {
      console.error('加载日记内容失败', err)
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

  onChooseImage() {
    wx.chooseMedia({
      count: 9 - this.data.images.length,
      mediaType: ['image'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const images = this.data.images.concat(res.tempFiles.map(f => f.tempFilePath))
        this.setData({ images })
      }
    })
  },

  onRemoveImage(e) {
    const index = e.currentTarget.dataset.index
    const images = this.data.images.filter((_, i) => i !== index)
    this.setData({ images })
  },

  async onPublish() {
    if (!this.data.title.trim()) {
      wx.showToast({ title: '请输入标题', icon: 'none' })
      return
    }
    if (this.data.title.length > 18) {
      wx.showToast({ title: '标题不能超过18字', icon: 'none' })
      return
    }
    if (!this.data.content.trim()) {
      wx.showToast({ title: '请输入内容', icon: 'none' })
      return
    }

    wx.showLoading({ title: '发布中...' })

    try {
      let imageUrls = this.data.imageUrls

      if (this.data.images.length > 0) {
        wx.hideLoading()
        wx.showLoading({ title: '上传图片中...' })
        const uploadPromises = this.data.images.map(img => uploadFile(img))
        imageUrls = await Promise.all(uploadPromises)
      }

      const result = await request({
        method: 'POST',
        url: config.api.postCreate,
        data: {
          title: this.data.title,
          emotion: this.data.emotion,
          emotionLabel: this.data.emotionLabel,
          content: this.data.content,
          images: imageUrls.join(','),
          source: this.data.source,
          sourceDiaryId: this.data.sourceDiaryId
        }
      })

      wx.hideLoading()
      if (result.data && result.data.rewardMessage) {
        wx.showModal({
          title: '奖励通知',
          content: result.data.rewardMessage,
          showCancel: false,
          confirmText: '知道了'
        })
      } else {
        wx.showToast({ title: '发布成功', icon: 'success' })
      }
      
      setTimeout(() => {
        wx.navigateBack()
      }, 1500)
    } catch (err) {
      wx.hideLoading()
      wx.showToast({ title: '发布失败', icon: 'none' })
      console.error('发布失败', err)
    }
  },

  onCancel() {
    wx.showModal({
      title: '提示',
      content: '确定要取消发布吗？',
      success: (res) => {
        if (res.confirm) {
          wx.navigateBack()
        }
      }
    })
  }
})
