const { request, uploadFile } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    username: '',
    password: '',
    nickname: '',
    avatarUrl: '',
    phone: '',
    gender: 0,
    genderOptions: [
      { value: 0, label: '保密' },
      { value: 1, label: '男' },
      { value: 2, label: '女' }
    ],
    genderIndex: 0
  },

  onUsernameInput(e) {
    this.setData({ username: e.detail.value })
  },

  onPasswordInput(e) {
    this.setData({ password: e.detail.value })
  },

  onNicknameInput(e) {
    this.setData({ nickname: e.detail.value })
  },

  onAvatarInput(e) {
    this.setData({ avatarUrl: e.detail.value })
  },

  onPhoneInput(e) {
    this.setData({ phone: e.detail.value })
  },

  onGenderChange(e) {
    const index = e.detail.value
    this.setData({
      genderIndex: index,
      gender: this.data.genderOptions[index].value
    })
  },

  onChooseAvatar() {
    wx.chooseMedia({
      count: 1,
      mediaType: ['image'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        this.setData({
          avatarUrl: res.tempFiles[0].tempFilePath
        })
      }
    })
  },

  async onRegister() {
    if (!this.data.username.trim()) {
      wx.showToast({ title: '请输入账号', icon: 'none' })
      return
    }

    if (!/^[a-zA-Z0-9]+$/.test(this.data.username.trim())) {
      wx.showToast({ title: '用户名只能包含英文字母和数字', icon: 'none' })
      return
    }

    if (this.data.username.length > 8) {
      wx.showToast({ title: '账号不能超过8位', icon: 'none' })
      return
    }

    if (!this.data.password) {
      wx.showToast({ title: '请输入密码', icon: 'none' })
      return
    }

    if (this.data.password.length > 8) {
      wx.showToast({ title: '密码不能超过8位', icon: 'none' })
      return
    }

    if (!this.data.nickname.trim()) {
      wx.showToast({ title: '请输入昵称', icon: 'none' })
      return
    }

    if (this.data.phone && !/^1\d{10}$/.test(this.data.phone)) {
      wx.showToast({ title: '请输入正确的手机号', icon: 'none' })
      return
    }

    wx.showLoading({ title: '注册中...' })

    let avatarUrl = this.data.avatarUrl.trim()
    
    // 如果头像是本地临时文件，先上传到服务器
    if (avatarUrl && (avatarUrl.startsWith('wxfile://') || avatarUrl.startsWith('http://tmp') || avatarUrl.startsWith('http://usr'))) {
      try {
        avatarUrl = await uploadFile(avatarUrl)
      } catch (err) {
        wx.hideLoading()
        console.error('头像上传失败', err)
        wx.showToast({ title: '头像上传失败', icon: 'none' })
        return
      }
    }

    request({
      method: 'POST',
      url: config.api.register,
      data: {
        username: this.data.username.trim(),
        password: this.data.password,
        nickname: this.data.nickname.trim(),
        avatarUrl: avatarUrl,
        phone: this.data.phone.trim() || '',
        gender: String(this.data.gender)
      }
    })
      .then((result) => {
        wx.hideLoading()

        wx.showToast({ title: '注册成功', icon: 'success' })

        setTimeout(() => {
          wx.redirectTo({
            url: '/pages/login/index'
          })
        }, 1500)
      })
      .catch((err) => {
        wx.hideLoading()
        console.error('注册失败', err)
      })
  },

  onGoToLogin() {
    wx.redirectTo({
      url: '/pages/login/index'
    })
  }
})
