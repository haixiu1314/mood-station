const { request } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    diary: null,
    emotionMap: {
      happy: { label: '开心', bg: '#FFF3CD', color: '#D4A017', icon: '😊' },
      sad: { label: '难过', bg: '#E8F0FE', color: '#6B8DD6', icon: '😢' },
      angry: { label: '生气', bg: '#FFF0F0', color: '#FF8B94', icon: '😠' },
      calm: { label: '平静', bg: '#DCEDC1', color: '#2E7D32', icon: '😌' },
      anxious: { label: '焦虑', bg: '#F3E5F5', color: '#9C27B0', icon: '😰' }
    }
  },

  onLoad(options) {
    if (options.id) {
      this.loadDiary(options.id)
    }
  },

  async loadDiary(id) {
    try {
      const res = await request({ url: config.api.diaryDetail + id })
      this.setData({ diary: res.data })
    } catch (err) {
      console.error('加载日记详情失败', err)
    }
  },

  onEdit() {
    wx.navigateTo({
      url: `/pages/diary/edit?id=${this.data.diary.id}`
    })
  },

  onShare() {
    wx.showModal({
      title: '提示',
      content: '确定要将此日记转发到情绪分享吗？',
      success: (res) => {
        if (res.confirm) {
          wx.navigateTo({
            url: `/pages/share/publish?diaryId=${this.data.diary.id}`
          })
        }
      }
    })
  },

  onDelete() {
    wx.showModal({
      title: '提示',
      content: '确定要删除这篇日记吗？删除后可在回收站恢复',
      success: async (res) => {
        if (res.confirm) {
          try {
            await request({
              url: `${config.api.diaryDelete}${this.data.diary.id}`,
              method: 'DELETE'
            })
            wx.showToast({ title: '已移入回收站', icon: 'success' })
            setTimeout(() => {
              wx.navigateBack()
            }, 1500)
          } catch (err) {
            wx.showToast({ title: err.data?.message || '删除失败', icon: 'none' })
          }
        }
      }
    })
  }
})
