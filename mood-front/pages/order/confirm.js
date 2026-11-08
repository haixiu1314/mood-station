const { request } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    cartIds: '',
    productId: '',
    quantity: 1,
    orderItems: [],
    totalAmount: 0,
    address: null,
    selectedAddress: null
  },

  onLoad(options) {
    if (options.cartIds) {
      this.setData({ cartIds: options.cartIds })
      this.loadCartItems(options.cartIds)
    } else if (options.productId) {
      const selectedSpec = options.selectedSpec ? decodeURIComponent(options.selectedSpec) : ''
      this.setData({
        productId: options.productId,
        quantity: options.quantity || 1,
        selectedSpec: selectedSpec
      })
      this.loadProduct(options.productId, options.quantity, selectedSpec)
    }
    this.loadAddress()
  },

  onShow() {
    if (this.data.selectedAddress) {
      this.setData({ address: this.data.selectedAddress })
      this.setData({ selectedAddress: null })
    }
  },

  async loadCartItems(cartIds) {
    try {
      const res = await request({ url: config.api.cartList })
      const allItems = res.data || []
      const ids = cartIds.split(',').map(Number)
      const items = allItems.filter(item => ids.some(id => Number(id) === Number(item.id)))
      const totalAmount = items.reduce((sum, item) => 
        sum + (item.price || 0) * item.quantity, 0
      )
      this.setData({
        orderItems: items,
        totalAmount: totalAmount.toFixed(2)
      })
    } catch (err) {
      console.error('加载购物车失败', err)
    }
  },

  async loadProduct(productId, quantity, selectedSpec) {
    try {
      const res = await request({ url: config.api.productDetail + productId })
      const product = res.data
      this.setData({
        orderItems: [{
          id: productId,
          productName: product.name,
          productImage: product.coverImage,
          selectedSpec: selectedSpec || '',
          price: product.price,
          quantity: parseInt(quantity)
        }],
        totalAmount: (product.price * parseInt(quantity)).toFixed(2)
      })
    } catch (err) {
      console.error('加载商品失败', err)
    }
  },

  async loadAddress() {
    try {
      const res = await request({ url: config.api.addressDefault })
      if (res.data) {
        this.setData({ address: res.data })
      }
    } catch (err) {
      console.error('加载地址失败', err)
    }
  },

  onChooseAddress() {
    wx.navigateTo({
      url: '/pages/address/list?selectMode=true'
    })
  },

  async onSubmitOrder() {
    if (!this.data.address) {
      wx.showToast({ title: '请选择收货地址', icon: 'none' })
      return
    }

    if (this.data.orderItems.length === 0) {
      wx.showToast({ title: '请选择商品', icon: 'none' })
      return
    }

    wx.showLoading({ title: '提交中...' })

    try {
      let requestData = {}
      if (this.data.productId) {
        requestData = {
          productId: parseInt(this.data.productId),
          quantity: parseInt(this.data.quantity),
          selectedSpec: this.data.selectedSpec || null,
          addressId: this.data.address.id
        }
      } else {
        let cartIds = []
        if (this.data.cartIds) {
          cartIds = this.data.cartIds.split(',').map(Number)
        } else {
          cartIds = this.data.orderItems.map(item => item.id)
        }
        requestData = { 
          cartIds,
          addressId: this.data.address.id
        }
      }

      await request({
        method: 'POST',
        url: config.api.orderCreate,
        data: requestData
      })

      wx.hideLoading()
      wx.showToast({ title: '下单成功', icon: 'success' })
      
      setTimeout(() => {
        wx.redirectTo({
          url: '/pages/order/list'
        })
      }, 1500)
    } catch (err) {
      wx.hideLoading()
      console.error('下单失败', err)
    }
  }
})
