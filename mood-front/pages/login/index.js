const { request } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    username: '',
    password: '',
    showPassword: false
  },

  onUsernameInput(e) {
    this.setData({ username: e.detail.value })
  },

  onPasswordInput(e) {
    this.setData({ password: e.detail.value })
  },

  onTogglePassword() {
    this.setData({ showPassword: !this.data.showPassword })
  },

  onLogin() {
    if (!this.data.username.trim()) {
      wx.showToast({ title: '请输入账号', icon: 'none' })
      return
    }

    if (!this.data.password) {
      wx.showToast({ title: '请输入密码', icon: 'none' })
      return
    }

    wx.showLoading({ title: '登录中...' })

    request({
      method: 'POST',
      url: config.api.loginAccount,
      data: {
        username: this.data.username.trim(),
        password: this.data.password
      }
    })
      .then((result) => {
        wx.hideLoading()

        if (result.data.banned) {
          wx.showModal({
            title: '账号已封禁',
            content: result.data.message || '您已被封号，请联系管理员处理',
            showCancel: false
          })
          return
        }

        const app = getApp()
        app.globalData.token = result.data.token
        app.globalData.userInfo = result.data.user
        wx.setStorageSync('token', result.data.token)
        wx.setStorageSync('userInfo', result.data.user)

        if (result.data && result.data.rewardMessage) {
          wx.showModal({
            title: '奖励通知',
            content: result.data.rewardMessage,
            showCancel: false,
            confirmText: '知道了'
          })
        } else {
          wx.showToast({ title: '登录成功', icon: 'success' })
        }

        setTimeout(() => {
          wx.reLaunch({
            url: '/pages/profile/index'
          })
        }, 1500)
      })
      .catch((err) => {
        wx.hideLoading()
        console.error('登录失败', err)
      })
  },

  onQuickLogin() {
    wx.showLoading({ title: '登录中...' })

    wx.login({
      success: async (res) => {
        if (res.code) {
          try {
            const result = await request({
              method: 'POST',
              url: config.api.login,
              data: {
                code: res.code,
                nickname: '微信用户',
                avatarUrl: ''
              }
            })

            wx.hideLoading()

            if (result.data.banned) {
              wx.showModal({
                title: '账号已封禁',
                content: result.data.message || '您已被封号，请联系管理员处理',
                showCancel: false
              })
              return
            }

            const app = getApp()
            app.globalData.token = result.data.token
            app.globalData.userInfo = result.data.user
            wx.setStorageSync('token', result.data.token)
            wx.setStorageSync('userInfo', result.data.user)

            wx.showToast({ title: '登录成功', icon: 'success' })

            setTimeout(() => {
              wx.reLaunch({
                url: '/pages/profile/index'
              })
            }, 1500)
          } catch (err) {
            wx.hideLoading()
            console.error('登录失败', err)
          }
        }
      },
      fail: (err) => {
        wx.hideLoading()
        wx.showToast({ title: '登录失败，请重试', icon: 'none' })
        console.error('wx.login 失败', err)
      }
    })
  },

  onGoToRegister() {
    wx.redirectTo({
      url: '/pages/register/index'
    })
  }
})
