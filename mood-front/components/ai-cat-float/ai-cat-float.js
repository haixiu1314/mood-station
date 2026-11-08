Component({
  properties: {
    visible: {
      type: Boolean,
      value: true
    }
  },

  data: {
    expanded: false,
    // 悬浮球位置：默认藏在边缘只露半圆
    ballTop: '50%',
    ballRight: '-50rpx',  // 默认隐藏一半
    // 拖动状态
    isDragging: false,
    dragStartY: 0,
    dragStartTop: 0,
    // 屏幕高度
    screenHeight: 0
  },

  lifetimes: {
    attached() {
      const systemInfo = wx.getSystemInfoSync();
      this.setData({
        screenHeight: systemInfo.windowHeight
      });
    }
  },

  methods: {
    // 触摸开始
    onTouchStart(e) {
      if (this.data.expanded) return;
      
      const touch = e.touches[0];
      let currentTopPercent = 50;
      if (this.data.ballTop && this.data.ballTop.includes('%')) {
        currentTopPercent = parseFloat(this.data.ballTop);
      }
      
      this.setData({
        isDragging: false,
        dragStartY: touch.clientY,
        dragStartTop: currentTopPercent
      });
    },

    // 触摸移动
    onTouchMove(e) {
      if (this.data.expanded) return;
      
      const touch = e.touches[0];
      const deltaY = touch.clientY - this.data.dragStartY;
      
      if (Math.abs(deltaY) > 10 && !this.data.isDragging) {
        this.setData({ isDragging: true });
      }
      
      if (this.data.isDragging) {
        const screenHeight = this.data.screenHeight;
        const deltaPercent = (deltaY / screenHeight) * 100;
        let newTop = this.data.dragStartTop + deltaPercent;
        newTop = Math.max(10, Math.min(90, newTop));
        
        this.setData({
          ballTop: newTop + '%'
        });
      }
    },

    // 触摸结束
    onTouchEnd(e) {
      if (this.data.isDragging) {
        this.setData({ isDragging: false });
      }
    },

    // 点击小球
    onBallTap() {
      if (this.data.isDragging) return;
      
      if (!this.data.expanded) {
        // 展开：小球向左移动完整显示
        this.setData({
          expanded: true,
          ballRight: '32rpx'
        });
      } else {
        // 收起：小球回到边缘只露半圆
        this.setData({
          expanded: false,
          ballRight: '-50rpx'
        });
      }
    },

    // 点击关闭按钮
    onCloseTap() {
      this.setData({
        expanded: false,
        ballRight: '-50rpx'
      });
    },

    // 点击开始对话
    onEnterTap() {
      this.setData({
        expanded: false,
        ballRight: '-50rpx'
      });
      wx.navigateTo({
        url: '/pages/ai-cat/index'
      });
    }
  }
});
