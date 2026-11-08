const { request } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    products: [],
    cartCount: 0
  },

  onLoad() {
    this.loadProducts()
    this.loadCartCount()
  },

  onShow() {
    this.loadCartCount()
  },

  async loadProducts() {
    try {
      const res = await request({ url: config.api.productList })
      this.setData({
        products: res.data || []
      })
    } catch (err) {
      console.error('加载商品列表失败', err)
    }
  },

  async loadCartCount() {
    try {
      const res = await request({ url: config.api.cartList })
      const count = res.data?.reduce((sum, item) => sum + item.quantity, 0) || 0
      this.setData({ cartCount: count })
    } catch (err) {
      console.error('加载购物车数量失败', err)
    }
  },

  onCartTap() {
    wx.navigateTo({
      url: '/pages/shop/cart'
    })
  },

  onProductTap(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({
      url: `/pages/shop/detail?id=${id}`
    })
  }
})
