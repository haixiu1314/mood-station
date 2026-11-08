const config = require('../config/api')

function request(options) {
  return new Promise((resolve, reject) => {
    const app = getApp()
    const token = app.globalData.token || wx.getStorageSync('token')
    
    wx.request({
      url: config.baseUrl + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': options.header && options.header['Content-Type'] ? options.header['Content-Type'] : 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
      },
      success(res) {
        const newToken = res.header['Authorization'] || res.header['authorization']
        if (newToken) {
          const cleanToken = newToken.replace('Bearer ', '')
          app.globalData.token = cleanToken
          wx.setStorageSync('token', cleanToken)
        }
        
        if (res.data.code === 200) {
          resolve(res.data)
        } else if (res.data.code === 401) {
          wx.removeStorageSync('token')
          wx.removeStorageSync('userInfo')
          app.globalData.token = ''
          app.globalData.userInfo = null
          wx.showToast({
            title: '请先登录',
            icon: 'none'
          })
          wx.redirectTo({
            url: '/pages/login/index'
          })
          reject(res.data)
        } else if (res.data.code === 403 && res.data.banned) {
          wx.removeStorageSync('token')
          wx.removeStorageSync('userInfo')
          app.globalData.token = ''
          app.globalData.userInfo = null
          wx.showModal({
            title: '账号已封禁',
            content: '您已被封号',
            showCancel: false,
            success() {
              wx.redirectTo({
                url: '/pages/login/index'
              })
            }
          })
          reject(res.data)
        } else {
          wx.showToast({
            title: res.data.message || '请求失败',
            icon: 'none'
          })
          reject(res.data)
        }
      },
      fail(err) {
        wx.showToast({
          title: '网络异常',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}

function uploadFile(filePath) {
  return new Promise((resolve, reject) => {
    const app = getApp()
    const token = app.globalData.token || wx.getStorageSync('token')
    
    wx.uploadFile({
      url: config.baseUrl + config.api.uploadImage,
      filePath: filePath,
      name: 'file',
      header: {
        'Authorization': token ? `Bearer ${token}` : ''
      },
      success(res) {
        const data = JSON.parse(res.data)
        if (data.code === 200) {
          resolve(data.data)
        } else {
          wx.showToast({
            title: data.message || '上传失败',
            icon: 'none'
          })
          reject(data)
        }
      },
      fail(err) {
        wx.showToast({
          title: '网络异常',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}

module.exports = {
  request,
  uploadFile
}
