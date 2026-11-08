const { request } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    trashList: [],
    showActionModal: false,
    selectedTrashId: null,
    emotionMap: {
      happy: { label: '开心', bg: '#FFF3CD', color: '#D4A017' },
      sad: { label: '难过', bg: '#E8F0FE', color: '#6B8DD6' },
      angry: { label: '生气', bg: '#FFF0F0', color: '#FF8B94' },
      calm: { label: '平静', bg: '#DCEDC1', color: '#2E7D32' },
      anxious: { label: '焦虑', bg: '#F3E5F5', color: '#9C27B0' }
    }
  },

  onLoad() {
    this.loadTrashList()
  },

  onShow() {
    this.loadTrashList()
  },

  async loadTrashList() {
    try {
      const res = await request({ url: config.api.diaryTrash })
      this.setData({ trashList: res.data || [] })
    } catch (err) {
      console.error('加载回收站失败', err)
    }
  },

  onShowMore(e) {
    this.setData({
      showActionModal: true,
      selectedTrashId: e.currentTarget.dataset.id
    })
  },

  onHideActionModal() {
    this.setData({ showActionModal: false, selectedTrashId: null })
  },

  async onRestore() {
    const trashId = this.data.selectedTrashId
    try {
      await request({
        url: `${config.api.diaryRestore}${trashId}/restore`,
        method: 'POST'
      })
      wx.showToast({ title: '恢复成功', icon: 'success' })
      this.setData({ showActionModal: false, selectedTrashId: null })
      this.loadTrashList()
    } catch (err) {
      wx.showToast({ title: err.data?.message || '恢复失败', icon: 'none' })
    }
  },

  async onPermanentDelete() {
    const trashId = this.data.selectedTrashId
    wx.showModal({
      title: '提示',
      content: '一旦彻底删除将永久无法恢复，确定要删除吗？',
      confirmColor: '#FF6B6B',
      success: async (res) => {
        if (res.confirm) {
          try {
            await request({
              url: `${config.api.diaryTrash}/${trashId}`,
              method: 'DELETE'
            })
            wx.showToast({ title: '已彻底删除', icon: 'success' })
            this.setData({ showActionModal: false, selectedTrashId: null })
            this.loadTrashList()
          } catch (err) {
            wx.showToast({ title: '删除失败', icon: 'none' })
          }
        }
      }
    })
  }
})
