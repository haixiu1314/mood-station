const { request } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    id: '',
    name: '',
    phone: '',
    region: [],
    province: '',
    city: '',
    district: '',
    detail: '',
    isDefault: false
  },

  onLoad(options) {
    if (options.id) {
      this.setData({ id: options.id })
      this.loadAddress(options.id)
    }
  },

  async loadAddress(id) {
    try {
      const res = await request({ url: config.api.addressList })
      const address = (res.data || []).find(a => a.id == id)
      if (address) {
        this.setData({
          name: address.name,
          phone: address.phone,
          region: [address.province, address.city, address.district],
          province: address.province,
          city: address.city,
          district: address.district,
          detail: address.detail,
          isDefault: address.isDefault === 1
        })
      }
    } catch (err) {
      console.error('加载地址详情失败', err)
    }
  },

  onNameInput(e) {
    this.setData({ name: e.detail.value })
  },

  onPhoneInput(e) {
    this.setData({ phone: e.detail.value })
  },

  onRegionChange(e) {
    this.setData({
      region: e.detail.value,
      province: e.detail.value[0],
      city: e.detail.value[1],
      district: e.detail.value[2]
    })
  },

  onDetailInput(e) {
    this.setData({ detail: e.detail.value })
  },

  onDefaultChange(e) {
    this.setData({ isDefault: e.detail.value })
  },

  async onSave() {
    if (!this.data.name.trim()) {
      wx.showToast({ title: '请输入收货人姓名', icon: 'none' })
      return
    }

    if (!this.data.phone.trim() || this.data.phone.length !== 11) {
      wx.showToast({ title: '请输入正确的手机号码', icon: 'none' })
      return
    }

    if (this.data.region.length < 3) {
      wx.showToast({ title: '请选择所在地区', icon: 'none' })
      return
    }

    if (!this.data.detail.trim()) {
      wx.showToast({ title: '请输入详细地址', icon: 'none' })
      return
    }

    try {
      const data = {
        name: this.data.name,
        phone: this.data.phone,
        province: this.data.province,
        city: this.data.city,
        district: this.data.district,
        detail: this.data.detail,
        isDefault: this.data.isDefault ? 1 : 0
      }

      if (this.data.id) {
        await request({
          method: 'PUT',
          url: config.api.addressUpdate,
          data: { ...data, id: this.data.id }
        })
        wx.showToast({ title: '更新成功', icon: 'success' })
      } else {
        await request({
          method: 'POST',
          url: config.api.addressAdd,
          data
        })
        wx.showToast({ title: '添加成功', icon: 'success' })
      }

      setTimeout(() => {
        wx.navigateBack()
      }, 1500)
    } catch (err) {
      console.error('保存地址失败', err)
    }
  },

  onCancel() {
    wx.navigateBack()
  }
})
