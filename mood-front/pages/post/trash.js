const { request } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    trashList: [],
    showAction: false,
    currentTrashId: null,
    currentDeletedBy: '',
    currentPostId: null,
    currentAppealStatus: null,
    emotionMap: {
      happy: { label: '开心', bg: '#FFF3CD', color: '#D4A017' },
      sad: { label: '难过', bg: '#E8F0FE', color: '#6B8DD6' },
      angry: { label: '生气', bg: '#FFF0F0', color: '#FF8B94' },
      calm: { label: '平静', bg: '#DCEDC1', color: '#2E7D32' },
      anxious: { label: '焦虑', bg: '#F3E5F5', color: '#9C27B0' }
    }
  },

  onShow() {
    this.loadTrashList()
  },

  async loadTrashList() {
    try {
      const res = await request({ url: config.api.postTrash })
      this.setData({ trashList: res.data || [] })
    } catch (err) {
      console.error('加载回收站失败', err)
    }
  },

  onShowMore(e) {
    this.setData({
      showAction: true,
      currentTrashId: e.currentTarget.dataset.id,
      currentDeletedBy: e.currentTarget.dataset.deletedBy,
      currentPostId: e.currentTarget.dataset.postId,
      currentAppealStatus: e.currentTarget.dataset.appealStatus
    })
  },

  onHideAction() {
    this.setData({ showAction: false })
  },

  async onRestore() {
    this.setData({ showAction: false })
    try {
      await request({
        url: `${config.api.postRestore}${this.data.currentTrashId}/restore`,
        method: 'POST'
      })
      wx.showToast({ title: '恢复成功', icon: 'success' })
      this.loadTrashList()
    } catch (err) {
      wx.showToast({ title: err.data?.message || '恢复失败', icon: 'none' })
    }
  },

  onPermanentDelete() {
    this.setData({ showAction: false })
    wx.showModal({
      title: '提示',
      content: '一旦彻底删除将永久无法恢复',
      confirmText: '彻底删除',
      confirmColor: '#FF6B6B',
      success: async (res) => {
        if (res.confirm) {
          try {
            await request({
              url: `${config.api.postTrash}/${this.data.currentTrashId}`,
              method: 'DELETE'
            })
            wx.showToast({ title: '已彻底删除', icon: 'success' })
            this.loadTrashList()
          } catch (err) {
            wx.showToast({ title: err.data?.message || '删除失败', icon: 'none' })
          }
        }
      }
    })
  },

  onAppeal() {
    this.setData({ showAction: false })
    wx.navigateTo({
      url: `/pages/post/appeal?trashId=${this.data.currentTrashId}`
    })
  }
})
