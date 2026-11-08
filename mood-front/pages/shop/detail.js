const { request } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    product: null,
    quantity: 1,
    images: [],
    specs: [],
    selectedSpec: ''
  },

  onLoad(options) {
    if (options.id) {
      this.loadProduct(options.id)
    }
  },

  async loadProduct(id) {
    try {
      const res = await request({ url: config.api.productDetail + id })
      const product = res.data
      console.log('商品详情数据:', product)
      
      // 解析图片列表：优先使用 images 字段，如果没有则使用封面图
      let images = []
      if (product.images && product.images.trim()) {
        images = product.images.split(',').filter(img => img.trim())
      }
      if (images.length === 0 && product.coverImage) {
        images = [product.coverImage]
      }
      
      // 解析规格列表
      const specs = product.specs ? product.specs.split(',').filter(Boolean) : []
      
      console.log('图片列表:', images)
      console.log('规格列表:', specs)
      
      this.setData({
        product,
        images,
        specs,
        selectedSpec: specs.length > 0 ? specs[0] : ''
      })
    } catch (err) {
      console.error('加载商品详情失败', err)
    }
  },

  onSpecSelect(e) {
    this.setData({ selectedSpec: e.currentTarget.dataset.spec })
  },

  onAddToCart() {
    if (this.data.specs.length > 0 && !this.data.selectedSpec) {
      wx.showToast({ title: '请选择规格', icon: 'none' })
      return
    }
    wx.showLoading({ title: '添加中...' })
    
    request({
      method: 'POST',
      url: config.api.cartAdd,
      data: {
        productId: this.data.product.id,
        quantity: this.data.quantity,
        selectedSpec: this.data.selectedSpec || null
      }
    }).then(() => {
      wx.hideLoading()
      wx.showToast({ title: '已加入购物车', icon: 'success' })
    }).catch(() => {
      wx.hideLoading()
    })
  },

  onBuyNow() {
    if (this.data.specs.length > 0 && !this.data.selectedSpec) {
      wx.showToast({ title: '请选择规格', icon: 'none' })
      return
    }
    const specParam = this.data.selectedSpec ? `&selectedSpec=${encodeURIComponent(this.data.selectedSpec)}` : ''
    wx.navigateTo({
      url: `/pages/order/confirm?productId=${this.data.product.id}&quantity=${this.data.quantity}${specParam}`
    })
  },

  onQuantityChange(e) {
    const action = e.currentTarget.dataset.action
    let quantity = this.data.quantity
    
    if (action === 'minus' && quantity > 1) {
      quantity--
    } else if (action === 'plus') {
      quantity++
    }
    
    this.setData({ quantity })
  }
})
