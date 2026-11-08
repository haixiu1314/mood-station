const { request } = require('../../utils/request')
const config = require('../../config/api')

Page({
  data: {
    userInfo: null,
    balance: 0,
    isLoggedIn: false,
    showCoinHelp: false,
    unreadCount: 0,
    systemUnread: 0,
    messageUnread: 0,
    showMusicMenu: false,
    showPopSingers: false,
    singers: [],
    isPlaying: false,
    currentMusic: null
  },
  audioContext: null,

  onLoad() {
    this.checkLogin()
  },

  onShow() {
    this.checkLogin()
    if (this.audioContext && this.data.isPlaying) {
      this.audioContext.play()
    }
  },

  checkLogin() {
    const app = getApp()
    const token = app.globalData.token || wx.getStorageSync('token')
    if (token) {
      this.loadUserInfo()
    } else {
      this.setData({ isLoggedIn: false })
    }
  },

  async loadUserInfo() {
    try {
      const [userRes, balanceRes, unreadRes] = await Promise.all([
        request({ url: config.api.userInfo }),
        request({ url: config.api.userBalance }),
        request({ url: config.api.notificationUnreadCountByType })
      ])
      
      const unreadData = unreadRes.data || {}
      const total = unreadData.total || 0
      
      this.setData({
        userInfo: userRes.data,
        balance: balanceRes.data,
        unreadCount: total,
        systemUnread: unreadData.system || 0,
        messageUnread: unreadData.message || 0,
        isLoggedIn: true
      })
      
      wx.setStorageSync('userInfo', userRes.data)
      getApp().globalData.userInfo = userRes.data
      
      // 设置TabBar红点
      if (total > 0) {
        wx.setTabBarBadge({
          index: 3,
          text: total > 99 ? '99+' : String(total)
        })
      } else {
        wx.removeTabBarBadge({ index: 3 })
      }
    } catch (err) {
      console.error('加载用户信息失败', err)
    }
  },

  onShowCoinHelp() {
    this.setData({ showCoinHelp: true })
  },

  onHideCoinHelp() {
    this.setData({ showCoinHelp: false })
  },

  onLogin() {
    wx.navigateTo({
      url: '/pages/login/index'
    })
  },

  onEditProfile() {
    wx.navigateTo({
      url: '/pages/profile/edit'
    })
  },

  onMyDiary() {
    wx.navigateTo({
      url: '/pages/diary/list'
    })
  },

  onMyOrders() {
    wx.navigateTo({
      url: '/pages/order/list'
    })
  },

  onMyPosts() {
    wx.navigateTo({
      url: '/pages/post/list'
    })
  },

  onMyMessages() {
    wx.navigateTo({
      url: '/pages/message/list'
    })
  },

  onMyAddress() {
    wx.navigateTo({
      url: '/pages/address/list'
    })
  },

  onNotification() {
    wx.navigateTo({
      url: '/pages/notification/list'
    })
  },

  onLogout() {
    wx.showModal({
      title: '提示',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          wx.removeStorageSync('token')
          wx.removeStorageSync('userInfo')
          getApp().globalData.token = ''
          getApp().globalData.userInfo = null
          wx.reLaunch({
            url: '/pages/login/index'
          })
        }
      }
    })
  },

  onMusicToggle() {
    if (this.data.isPlaying) {
      this.onStopMusic()
    } else {
      this.setData({ showMusicMenu: true })
    }
  },

  onHideMusicMenu() {
    this.setData({ showMusicMenu: false })
  },

  onSelectPureMusic() {
    this.setData({ showMusicMenu: false })
    this.playRandomMusic('pure')
  },

  onTogglePopMusic() {
    const showPopSingers = !this.data.showPopSingers
    this.setData({ showPopSingers })
    if (showPopSingers && this.data.singers.length === 0) {
      this.loadSingers()
    }
  },

  onSelectSinger(e) {
    const singer = e.currentTarget.dataset.singer
    this.setData({ showMusicMenu: false, showPopSingers: false })
    this.playRandomMusic('pop', singer)
  },

  async loadSingers() {
    try {
      const res = await request({
        url: `/api/music/singers?type=pop`
      })
      if (res.code === 200) {
        this.setData({ singers: res.data })
      }
    } catch (err) {
      console.error('加载歌手列表失败', err)
    }
  },

  async playRandomMusic(type, singer = null) {
    try {
      let url = `/api/music/random?type=${type}`
      if (singer) {
        url += `&singer=${encodeURIComponent(singer)}`
      }
      const res = await request({ url })
      if (res.code === 200 && res.data) {
        const music = res.data
        this.setData({
          isPlaying: true,
          currentMusic: music
        })
        if (!this.audioContext) {
          this.audioContext = wx.createInnerAudioContext()
          this.audioContext.onEnded(() => {
            this.setData({ isPlaying: false, currentMusic: null })
          })
          this.audioContext.onError((err) => {
            console.error('播放失败', err)
            this.setData({ isPlaying: false, currentMusic: null })
            wx.showToast({ title: '播放失败', icon: 'none' })
          })
        }
        this.audioContext.src = music.fileUrl.startsWith('http') ? music.fileUrl : `${config.baseUrl}${music.fileUrl}`
        this.audioContext.play()
      } else {
        wx.showToast({ title: '暂无可用音乐', icon: 'none' })
      }
    } catch (err) {
      console.error('获取音乐失败', err)
      wx.showToast({ title: '获取音乐失败', icon: 'none' })
    }
  },

  onStopMusic() {
    if (this.audioContext) {
      this.audioContext.stop()
    }
    this.setData({ isPlaying: false, currentMusic: null })
  },

  onUnload() {
    if (this.audioContext) {
      this.audioContext.destroy()
      this.audioContext = null
    }
  },

  onHide() {
    if (this.audioContext && this.data.isPlaying) {
      this.audioContext.pause()
    }
  }
})
