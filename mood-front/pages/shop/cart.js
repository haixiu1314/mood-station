const { request } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    cartItems: [],
    selectedIds: [],
    totalAmount: 0
  },

  onLoad() {
    this.loadCart()
  },

  onShow() {
    this.loadCart()
  },

  async loadCart() {
    try {
      const res = await request({ url: config.api.cartList })
      const cartItems = (res.data || []).map(item => ({
        ...item,
        selected: true
      }))
      this.setData({
        cartItems,
        selectedIds: cartItems.map(item => item.id)
      })
      this.calculateTotal()
    } catch (err) {
      console.error('加载购物车失败', err)
    }
  },

  onItemSelect(e) {
    const id = e.currentTarget.dataset.id
    const cartItems = this.data.cartItems.map(item => {
      if (Number(item.id) === Number(id)) {
        return { ...item, selected: !item.selected }
      }
      return item
    })
    
    const selectedIds = cartItems.filter(item => item.selected).map(item => item.id)
    
    this.setData({ cartItems, selectedIds })
    this.calculateTotal()
  },

  onQuantityChange(e) {
    const id = e.currentTarget.dataset.id
    const action = e.currentTarget.dataset.action
    const cartItems = this.data.cartItems
    const item = cartItems.find(i => i.id === id)
    
    if (action === 'minus' && item.quantity > 1) {
      item.quantity--
    } else if (action === 'plus') {
      item.quantity++
    }
    
    this.setData({ cartItems })
    this.updateCartQuantity(id, item.quantity)
    this.calculateTotal()
  },

  async updateCartQuantity(id, quantity) {
    try {
      const item = this.data.cartItems.find(i => i.id === id)
      await request({
        method: 'PUT',
        url: config.api.cartUpdate,
        data: {
          productId: item.productId,
          quantity
        }
      })
    } catch (err) {
      console.error('更新购物车数量失败', err)
    }
  },

  async onRemove(e) {
    const id = e.currentTarget.dataset.id
    
    wx.showModal({
      title: '提示',
      content: '确定要删除此商品吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await request({
              method: 'DELETE',
              url: config.api.cartRemove + id
            })
            wx.showToast({ title: '删除成功', icon: 'success' })
            this.loadCart()
          } catch (err) {
            console.error('删除商品失败', err)
          }
        }
      }
    })
  },

  calculateTotal() {
    const selectedItems = this.data.cartItems.filter(item => item.selected)
    const totalAmount = selectedItems.reduce((sum, item) => 
      sum + (item.price || 0) * item.quantity, 0
    )
    this.setData({ totalAmount: totalAmount.toFixed(2) })
  },

  onCheckout() {
    if (this.data.selectedIds.length === 0) {
      wx.showToast({ title: '请选择商品', icon: 'none' })
      return
    }
    
    const cartIds = this.data.selectedIds.join(',')
    wx.navigateTo({
      url: `/pages/order/confirm?cartIds=${cartIds}`
    })
  },

  onClearCart() {
    wx.showModal({
      title: '提示',
      content: '确定要清空购物车吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await request({
              method: 'DELETE',
              url: config.api.cartClear
            })
            wx.showToast({ title: '清空成功', icon: 'success' })
            this.loadCart()
          } catch (err) {
            console.error('清空购物车失败', err)
          }
        }
      }
    })
  }
})
