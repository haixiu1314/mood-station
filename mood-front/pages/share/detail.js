const { request } = require('../../utils/request')
const { resolveMediaUrl } = require('../../utils/url')
const config = require('../../config/api')

Page({
  data: {
    postId: '',
    post: {},
    comments: [],
    commentContent: '',
    showCommentInput: false
  },

  onLoad(options) {
    if (options.id) {
      this.setData({ postId: options.id })
      this.loadPostDetail(options.id)
      this.loadComments(options.id)
    }
  },

  async loadPostDetail(id) {
    try {
      const res = await request({ url: config.api.postDetail + id })
      const post = res.data
      if (post.images && typeof post.images === 'string') {
        post.images = post.images.split(',').filter(img => img.trim())
      }
      this.setData({ post })
    } catch (err) {
      console.error('加载帖子详情失败', err)
    }
  },

  async loadComments(postId) {
    try {
      const res = await request({ url: config.api.postCommentList + postId })
      this.setData({ comments: res.data || [] })
    } catch (err) {
      console.error('加载评论失败', err)
    }
  },

  async onLikeTap() {
    const postId = this.data.postId
    const isLiked = this.data.post.isLiked
    
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
      
      this.setData({
        'post.isLiked': !isLiked,
        'post.likeCount': isLiked ? this.data.post.likeCount - 1 : this.data.post.likeCount + 1
      })
    } catch (err) {
      console.error('点赞失败', err)
    }
  },

  onShowCommentInput() {
    this.setData({ showCommentInput: true })
  },

  onHideCommentInput() {
    this.setData({ showCommentInput: false })
  },

  onCommentInput(e) {
    const value = e.detail.value
    if (value.length > 300) {
      wx.showToast({ title: '最多300字', icon: 'none' })
      this.setData({ commentContent: value.substring(0, 300) })
    } else {
      this.setData({ commentContent: value })
    }
  },

  async onSubmitComment() {
    if (!this.data.commentContent.trim()) {
      wx.showToast({ title: '请输入评论内容', icon: 'none' })
      return
    }

    try {
      const result = await request({
        method: 'POST',
        url: config.api.postComment,
        data: {
          postId: this.data.postId,
          content: this.data.commentContent
        }
      })

      if (result.data && result.data.rewardMessage) {
        wx.showModal({
          title: '奖励通知',
          content: result.data.rewardMessage,
          showCancel: false,
          confirmText: '知道了'
        })
      } else {
        wx.showToast({ title: '评论成功', icon: 'success' })
      }
      this.setData({ commentContent: '', showCommentInput: false })
      this.loadComments(this.data.postId)
    } catch (err) {
      console.error('评论失败', err)
    }
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
  }
})
