const { request } = require('../../utils/request')
const { resolveMediaUrl } = require('../../utils/url')
const config = require('../../config/api')

Page({
  data: {
    posts: [],
    loading: false
  },

  onLoad() {
    this.loadPosts()
  },

  onShow() {
    this.loadPosts()
  },

  onPullDownRefresh() {
    this.loadPosts().finally(() => wx.stopPullDownRefresh())
  },

  async loadPosts() {
    if (this.data.loading) return
    this.setData({ loading: true })
    
    try {
      const res = await request({ url: config.api.postList })
      const posts = (res.data || []).map(post => {
        if (post.images && typeof post.images === 'string') {
          post.images = post.images.split(',').filter(img => img.trim())
        }
        post.revealed = false
        return post
      })
      this.setData({ posts: posts }, () => this.setupRevealObserver())
    } catch (err) {
      console.error('加载帖子列表失败', err)
    } finally {
      this.setData({ loading: false })
    }
  },

  // 帖子滚动虚化→清晰渐显
  setupRevealObserver() {
    if (this._observer) {
      this._observer.disconnect()
      this._observer = null
    }
    if (typeof wx.createIntersectionObserver !== 'function') {
      const posts = this.data.posts.map(p => ({ ...p, revealed: true }))
      this.setData({ posts })
      return
    }
    this._observer = wx.createIntersectionObserver(this, { observeAll: true })
    this._observer.relativeToViewport({ bottom: 0 }).observe('.post-item', (res) => {
      if (res.intersectionRatio > 0) {
        const index = res.dataset.index
        if (index !== undefined && index !== '') {
          this.setData({
            ['posts[' + index + '].revealed']: true
          })
        }
      }
    })
  },

  onUnload() {
    if (this._observer) {
      this._observer.disconnect()
      this._observer = null
    }
  },

  async onLikeTap(e) {
    const postId = e.currentTarget.dataset.id
    const index = e.currentTarget.dataset.index
    const isLiked = this.data.posts[index].isLiked
    
    try {
      if (isLiked) {
        await request({
          method: 'DELETE',
          url: config.api.postUnlike + postId
        })
      } else {
        const result = await request({
          method: 'POST',
          url: config.api.postLike + postId
        })
        if (result.data && result.data.rewardMessage) {
          wx.showModal({
            title: '奖励通知',
            content: result.data.rewardMessage,
            showCancel: false,
            confirmText: '知道了'
          })
        }
      }
      
      const posts = this.data.posts
      posts[index].isLiked = !isLiked
      posts[index].likeCount = isLiked ? posts[index].likeCount - 1 : posts[index].likeCount + 1
      
      this.setData({ posts })
    } catch (err) {
      console.error('点赞失败', err)
    }
  },

  onPublishTap() {
    wx.showActionSheet({
      itemList: ['写帖子', '从日记转发'],
      success: (res) => {
        if (res.tapIndex === 0) {
          wx.navigateTo({ url: '/pages/share/publish' })
        } else if (res.tapIndex === 1) {
          wx.navigateTo({ url: '/pages/share/forward' })
        }
      }
    })
  },

  onPostTap(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({
      url: `/pages/share/detail?id=${id}`
    })
  },

  onPreviewImage(e) {
    const rawImages = e.currentTarget.dataset.images || []
    const rawCurrent = e.currentTarget.dataset.current
    const images = rawImages.map(resolveMediaUrl)
    const current = resolveMediaUrl(rawCurrent)
    wx.previewImage({
      current,
      urls: images
    })
  },

  onAvatarTap(e) {
    const userId = e.currentTarget.dataset.userid
    if (userId) {
      wx.navigateTo({
        url: `/pages/user/profile?userId=${userId}`
      })
    }
  }
})
