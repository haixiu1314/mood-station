const { request } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    addresses: [],
    selectMode: false
  },

  onLoad(options) {
    if (options.selectMode === 'true') {
      this.setData({ selectMode: true })
    }
    this.loadAddresses()
  },

  onShow() {
    this.loadAddresses()
  },

  async loadAddresses() {
    try {
      const res = await request({ url: config.api.addressList })
      this.setData({ addresses: res.data || [] })
    } catch (err) {
      console.error('加载地址列表失败', err)
    }
  },

  onSelectAddress(e) {
    if (this.data.selectMode) {
      const item = e.currentTarget.dataset.item
      const pages = getCurrentPages()
      const prevPage = pages[pages.length - 2]
      if (prevPage) {
        prevPage.setData({ selectedAddress: item })
      }
      wx.navigateBack()
    }
  },

  onEditAddress(e) {
    const item = e.currentTarget.dataset.item
    wx.navigateTo({
      url: `/pages/address/edit?id=${item.id}`
    })
  },

  onAddAddress() {
    wx.navigateTo({
      url: '/pages/address/edit'
    })
  },

  async onDeleteAddress(e) {
    const item = e.currentTarget.dataset.item
    wx.showModal({
      title: '提示',
      content: '确定要删除这个地址吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await request({
              method: 'DELETE',
              url: config.api.addressDelete + item.id
            })
            wx.showToast({ title: '删除成功', icon: 'success' })
            this.loadAddresses()
          } catch (err) {
            console.error('删除地址失败', err)
          }
        }
      }
    })
  },

  async onSetDefault(e) {
    const item = e.currentTarget.dataset.item
    try {
      await request({
        method: 'PUT',
        url: config.api.addressSetDefault + item.id
      })
      wx.showToast({ title: '设置成功', icon: 'success' })
      this.loadAddresses()
    } catch (err) {
      console.error('设置默认地址失败', err)
    }
  }
})
