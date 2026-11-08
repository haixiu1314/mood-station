const { request } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    formData: {
      nickname: '',
      avatarUrl: '',
      gender: 0,
      birthday: '',
      bio: '',
      phone: ''
    },
    genderOptions: ['未知', '男', '女'],
    genderIndex: 0,
    bioLength: 0,
    showPasswordModal: false,
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
  },

  onLoad() {
    this.loadUserInfo()
  },

  async loadUserInfo() {
    try {
      const res = await request({ url: config.api.userInfo })
      const user = res.data
      const genderMap = { 0: 0, 1: 1, 2: 2 }
      const genderIndex = genderMap[user.gender] !== undefined ? genderMap[user.gender] : 0
      
      this.setData({
        formData: {
          nickname: user.nickname || '',
          avatarUrl: user.avatarUrl || '',
          gender: user.gender !== undefined ? user.gender : 0,
          birthday: user.birthday || '',
          bio: user.bio || '',
          phone: user.phone || ''
        },
        genderIndex: genderIndex,
        bioLength: (user.bio || '').length
      })
    } catch (err) {
      console.error('加载用户信息失败', err)
      wx.showToast({ title: '加载失败', icon: 'none' })
    }
  },

  onInput(e) {
    const field = e.currentTarget.dataset.field
    const value = e.detail.value
    this.setData({
      [`formData.${field}`]: value
    })
    if (field === 'bio') {
      this.setData({ bioLength: value.length })
    }
  },

  onGenderChange(e) {
    const index = parseInt(e.detail.value)
    const genderMap = { 0: 0, 1: 1, 2: 2 }
    this.setData({
      genderIndex: index,
      'formData.gender': genderMap[index]
    })
  },

  onBirthdayChange(e) {
    this.setData({
      'formData.birthday': e.detail.value
    })
  },

  onChooseAvatar() {
    wx.chooseMedia({
      count: 1,
      mediaType: ['image'],
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const tempFilePath = res.tempFiles[0].tempFilePath
        const fileSize = res.tempFiles[0].size
        
        if (fileSize > 2 * 1024 * 1024) {
          wx.showToast({ title: '图片不能超过2M', icon: 'none' })
          return
        }
        
        wx.showLoading({ title: '上传中...' })
        wx.uploadFile({
          url: `${config.baseUrl}/api/upload/image`,
          filePath: tempFilePath,
          name: 'file',
          header: {
            'Authorization': wx.getStorageSync('token')
          },
          success: (uploadRes) => {
            wx.hideLoading()
            const data = JSON.parse(uploadRes.data)
            if (data.code === 200) {
              this.setData({
                'formData.avatarUrl': data.data
              })
              wx.showToast({ title: '头像已更新', icon: 'success' })
            } else {
              wx.showToast({ title: data.message || '上传失败', icon: 'none' })
            }
          },
          fail: () => {
            wx.hideLoading()
            wx.showToast({ title: '上传失败', icon: 'none' })
          }
        })
      }
    })
  },

  onChangePassword() {
    this.setData({
      showPasswordModal: true,
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    })
  },

  onClosePasswordModal() {
    this.setData({ showPasswordModal: false })
  },

  onPwdInput(e) {
    const field = e.currentTarget.dataset.field
    this.setData({ [field]: e.detail.value })
  },

  async onConfirmPassword() {
    const { oldPassword, newPassword, confirmPassword } = this.data
    
    if (!oldPassword) {
      wx.showToast({ title: '请输入原密码', icon: 'none' })
      return
    }
    if (!newPassword) {
      wx.showToast({ title: '请输入新密码', icon: 'none' })
      return
    }
    if (newPassword !== confirmPassword) {
      wx.showToast({ title: '两次密码输入不一致', icon: 'none' })
      return
    }
    if (newPassword.length > 8) {
      wx.showToast({ title: '密码不能超过8位', icon: 'none' })
      return
    }
    
    try {
      await request({
        url: config.api.changePassword,
        method: 'PUT',
        data: { oldPassword, newPassword }
      })
      wx.showToast({ title: '密码修改成功，请重新登录', icon: 'success', duration: 2000 })
      this.setData({ showPasswordModal: false })
      
      setTimeout(() => {
        wx.removeStorageSync('token')
        wx.reLaunch({ url: '/pages/login/index' })
      }, 2000)
    } catch (err) {
      wx.showToast({ title: err.message || '修改失败', icon: 'none' })
    }
  },

  onPrivacySetting() {
    wx.showToast({ title: '功能开发中', icon: 'none' })
  },

  async onSave() {
    const { nickname, phone } = this.data.formData
    
    if (!nickname || !nickname.trim()) {
      wx.showToast({ title: '昵称不能为空', icon: 'none' })
      return
    }
    
    if (phone && !/^1[3-9]\d{9}$/.test(phone)) {
      wx.showToast({ title: '手机号格式不正确', icon: 'none' })
      return
    }
    
    try {
      await request({
        url: config.api.updateUserInfo,
        method: 'PUT',
        data: this.data.formData
      })
      
      wx.showToast({ title: '保存成功', icon: 'success' })
      
      setTimeout(() => {
        wx.navigateBack()
      }, 1500)
    } catch (err) {
      wx.showToast({ title: err.message || '保存失败', icon: 'none' })
    }
  }
})
