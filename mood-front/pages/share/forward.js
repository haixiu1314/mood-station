const { request } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    diaries: [],
    loading: false,
    emotionMap: {
      happy: { label: '开心', bg: '#FFF3CD', color: '#D4A017', icon: '😊' },
      sad: { label: '难过', bg: '#E8F0FE', color: '#6B8DD6', icon: '😢' },
      angry: { label: '生气', bg: '#FFF0F0', color: '#FF8B94', icon: '😠' },
      calm: { label: '平静', bg: '#DCEDC1', color: '#2E7D32', icon: '😌' },
      anxious: { label: '焦虑', bg: '#F3E5F5', color: '#9C27B0', icon: '😰' }
    }
  },

  onLoad() {
    this.loadDiaries()
  },

  async loadDiaries() {
    this.setData({ loading: true })
    try {
      const res = await request({ url: config.api.diaryList })
      this.setData({
        diaries: res.data || [],
        loading: false
      })
    } catch (err) {
      console.error('加载日记列表失败', err)
      this.setData({ loading: false })
    }
  },

  onSelectDiary(e) {
    const diaryId = e.currentTarget.dataset.id
    wx.navigateTo({
      url: `/pages/share/publish?diaryId=${diaryId}`
    })
  }
})
