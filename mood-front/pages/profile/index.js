const { request } = require('../../utils/request')
const { resolveMediaUrl } = require('../../utils/url')
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
    isPaused: false,
    currentMusic: null,
    barDragY: 0
  },
  audioContext: null,

  onLoad() {
    this.checkLogin()
  },

  onShow() {
    this.checkLogin()
  },

  onPullDownRefresh() {
    this.loadUserInfo().finally(() => wx.stopPullDownRefresh())
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

  onFeedback() {
    wx.navigateTo({
      url: '/pages/feedback/index'
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
          isPaused: false,
          currentMusic: music
        })
        if (!this.audioContext) {
          this.audioContext = wx.createInnerAudioContext()
          this.audioContext.onEnded(() => {
            this.setData({ isPlaying: false, isPaused: false, currentMusic: null })
          })
          this.audioContext.onError((err) => {
            console.error('播放失败', err)
            this.setData({ isPlaying: false, isPaused: false, currentMusic: null })
            wx.showToast({ title: '播放失败', icon: 'none' })
          })
        }
        this.audioContext.src = resolveMediaUrl(music.fileUrl)
        this.audioContext.play()
      } else {
        wx.showToast({ title: '暂无可用音乐', icon: 'none' })
      }
    } catch (err) {
      console.error('获取音乐失败', err)
      wx.showToast({ title: '获取音乐失败', icon: 'none' })
    }
  },

  onTogglePlayPause() {
    if (!this.audioContext) return
    if (this.data.isPaused) {
      this.audioContext.play()
      this.setData({ isPaused: false })
    } else {
      this.audioContext.pause()
      this.setData({ isPaused: true })
    }
  },

  onStopMusic() {
    if (this.audioContext) {
      this.audioContext.stop()
    }
    this.setData({ isPlaying: false, isPaused: false, currentMusic: null })
  },

  onBarTouchStart(e) {
    this._dragStartY = e.touches[0].clientY
    this._dragStartDragY = this.data.barDragY
  },

  onBarTouchMove(e) {
    const deltaY = this._dragStartY - e.touches[0].clientY
    let newDragY = this._dragStartDragY + deltaY
    // 只允许向上拖动，最大 400px，避免拖到 TabBar 下
    newDragY = Math.max(0, Math.min(400, newDragY))
    this.setData({ barDragY: newDragY })
  },

  onUnload() {
    if (this.audioContext) {
      this.audioContext.destroy()
      this.audioContext = null
    }
  }
})
