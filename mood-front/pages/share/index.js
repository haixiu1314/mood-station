const { request } = require('../../utils/request')
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

  async loadPosts() {
    if (this.data.loading) return
    this.setData({ loading: true })
    
    try {
      const res = await request({ url: config.api.postList })
      const posts = (res.data || []).map(post => {
        if (post.images && typeof post.images === 'string') {
          post.images = post.images.split(',').filter(img => img.trim())
        }
        return post
      })
      this.setData({
        posts: posts
      })
    } catch (err) {
      console.error('加载帖子列表失败', err)
    } finally {
      this.setData({ loading: false })
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
    const images = e.currentTarget.dataset.images
    const current = e.currentTarget.dataset.current
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
