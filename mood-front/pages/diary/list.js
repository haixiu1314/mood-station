const { request } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    diaries: [],
    searchKeyword: '',
    filterDate: '',
    emptyText: '',
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

  onShow() {
    this.loadDiaries()
  },

  async loadDiaries() {
    try {
      let url = config.api.diaryList
      if (this.data.filterDate) {
        url = `${config.api.diaryFilter}?date=${this.data.filterDate}`
      } else if (this.data.searchKeyword) {
        url = `${config.api.diarySearch}?keyword=${encodeURIComponent(this.data.searchKeyword)}`
      }
      const res = await request({ url })
      this.setData({ diaries: res.data || [] })
      if (res.data && res.data.length === 0) {
        if (this.data.searchKeyword) {
          this.setData({ emptyText: '未找到相关日记' })
        } else if (this.data.filterDate) {
          this.setData({ emptyText: '该日期下没有日记' })
        } else {
          this.setData({ emptyText: '还没有日记，快去写一篇吧~' })
        }
      }
    } catch (err) {
      console.error('加载日记列表失败', err)
    }
  },

  onDateChange(e) {
    this.setData({
      filterDate: e.detail.value,
      searchKeyword: ''
    })
    this.loadDiaries()
  },

  onResetFilter() {
    this.setData({ filterDate: '', searchKeyword: '' })
    this.loadDiaries()
  },

  onSearchInput(e) {
    this.setData({ searchKeyword: e.detail.value })
  },

  onSearch() {
    if (this.data.searchKeyword.trim()) {
      this.setData({ filterDate: '' })
      this.loadDiaries()
    }
  },

  onDiaryTap(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({
      url: `/pages/diary/detail?id=${id}`
    })
  },

  onDeleteDiary(e) {
    const id = e.currentTarget.dataset.id
    wx.showModal({
      title: '提示',
      content: '确定要删除这篇日记吗？删除后可在回收站恢复',
      success: async (res) => {
        if (res.confirm) {
          try {
            await request({
              url: `${config.api.diaryDelete}${id}`,
              method: 'DELETE'
            })
            wx.showToast({ title: '已移入回收站', icon: 'success' })
            this.loadDiaries()
          } catch (err) {
            wx.showToast({ title: err.data?.message || '删除失败', icon: 'none' })
          }
        }
      }
    })
  },

  onGoTrash() {
    wx.navigateTo({
      url: '/pages/diary/trash'
    })
  }
})
