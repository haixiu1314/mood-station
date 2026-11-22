<template>
  <div class="layout">
    <aside class="sidebar" :class="{ collapsed: isCollapsed }">
      <div class="sidebar-logo">
        <span v-if="!isCollapsed" class="logo-text">情绪小站</span>
        <span v-else class="logo-icon">情</span>
      </div>
      
      <nav class="sidebar-menu">
        <router-link
          v-for="item in menuItems"
          :key="item.path"
          :to="item.path"
          class="menu-item"
          :class="{ active: $route.path === item.path }"
        >
          <el-icon class="menu-icon"><component :is="item.icon" /></el-icon>
          <span v-if="!isCollapsed" class="menu-text">{{ item.title }}</span>
        </router-link>
      </nav>
    </aside>
    
    <div class="main-wrapper">
      <header class="header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="isCollapsed = !isCollapsed">
            <Fold v-if="!isCollapsed" />
            <Expand v-else />
          </el-icon>
          <span class="page-title">{{ currentTitle }}</span>
        </div>
        
        <div class="header-right">
          <span class="admin-name">{{ adminInfo.nickname || '管理员' }}</span>
          <el-button text class="logout-btn" @click="handleLogout">
            <el-icon><SwitchButton /></el-icon>
            <span v-if="!isCollapsed">退出</span>
          </el-button>
        </div>
      </header>
      
      <main class="main-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  Fold, Expand, SwitchButton,
  DataAnalysis, User, Goods, ShoppingCart,
  ChatDotRound, Picture, Van, Headset, Comment
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const isCollapsed = ref(false)

const adminInfo = computed(() => {
  try {
    return JSON.parse(localStorage.getItem('adminInfo') || '{}')
  } catch {
    return {}
  }
})

const currentTitle = computed(() => {
  return route.meta.title || ''
})

const menuItems = [
  { path: '/dashboard', title: '数据概览', icon: 'DataAnalysis' },
  { path: '/users', title: '用户管理', icon: 'User' },
  { path: '/products', title: '商品管理', icon: 'Goods' },
  { path: '/orders', title: '订单管理', icon: 'ShoppingCart' },
  { path: '/express', title: '快递状态管理', icon: 'Van' },
  { path: '/posts', title: '帖子管理', icon: 'ChatDotRound' },
  { path: '/music', title: '音乐管理', icon: 'Headset' },
  { path: '/banners', title: '轮播图管理', icon: 'Picture' },
  { path: '/feedback', title: '用户反馈', icon: 'Comment' }
]

const handleLogout = () => {
  localStorage.removeItem('adminToken')
  localStorage.removeItem('adminInfo')
  router.push('/login')
}
</script>

<style lang="scss" scoped>
.layout {
  display: flex;
  height: 100vh;
}

.sidebar {
  width: var(--sidebar-width);
  background: var(--bg-sidebar);
  border-right: 1px solid var(--border-base);
  display: flex;
  flex-direction: column;
  transition: width 0.3s ease;
  flex-shrink: 0;
  
  &.collapsed {
    width: var(--sidebar-collapsed-width);
  }
}

.sidebar-logo {
  height: var(--header-height);
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid var(--border-base);
}

.logo-text {
  font-family: var(--font-serif);
  font-size: 18px;
  font-weight: 500;
  color: var(--color-primary);
}

.logo-icon {
  font-size: 20px;
  color: var(--color-primary);
}

.sidebar-menu {
  flex: 1;
  padding: 16px 0;
  overflow-y: auto;
}

.menu-item {
  display: flex;
  align-items: center;
  height: 44px;
  padding: 0 20px;
  color: var(--text-regular);
  text-decoration: none;
  cursor: pointer;
  transition: all 0.2s;
  margin: 4px 12px;
  border-radius: var(--radius-small);
  
  &:hover {
    background: var(--border-light);
    color: var(--color-primary);
  }
  
  &.active {
    background: var(--color-primary);
    color: #fff;
    
    .menu-icon {
      color: #fff;
    }
  }
}

.menu-icon {
  font-size: 18px;
  flex-shrink: 0;
}

.menu-text {
  margin-left: 12px;
  font-size: 14px;
  white-space: nowrap;
  overflow: hidden;
}

.main-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.header {
  height: var(--header-height);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  background: var(--bg-card);
  border-bottom: 1px solid var(--border-base);
  flex-shrink: 0;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.collapse-btn {
  font-size: 20px;
  cursor: pointer;
  color: var(--text-secondary);
  
  &:hover {
    color: var(--color-primary);
  }
}

.page-title {
  font-family: var(--font-serif);
  font-size: 18px;
  font-weight: 500;
  color: var(--text-primary);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.admin-name {
  font-size: 14px;
  color: var(--text-regular);
}

.logout-btn {
  color: var(--text-secondary);
  
  &:hover {
    color: var(--color-danger);
  }
}

.main-content {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
  background: var(--bg-base);
}
</style>
