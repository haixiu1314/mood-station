import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('@/views/Layout.vue'),
    redirect: '/dashboard',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '数据概览' }
      },
      {
        path: 'users',
        name: 'Users',
        component: () => import('@/views/UserManage.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'products',
        name: 'Products',
        component: () => import('@/views/ProductManage.vue'),
        meta: { title: '商品管理' }
      },
      {
        path: 'orders',
        name: 'Orders',
        component: () => import('@/views/OrderManage.vue'),
        meta: { title: '订单管理' }
      },
      {
        path: 'express',
        name: 'Express',
        component: () => import('@/views/ExpressManage.vue'),
        meta: { title: '快递状态管理' }
      },
      {
        path: 'posts',
        name: 'Posts',
        component: () => import('@/views/PostManage.vue'),
        meta: { title: '帖子管理' }
      },
      {
        path: 'music',
        name: 'Music',
        component: () => import('@/views/MusicManage.vue'),
        meta: { title: '音乐管理' }
      },
      {
        path: 'banners',
        name: 'Banners',
        component: () => import('@/views/BannerManage.vue'),
        meta: { title: '轮播图管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('adminToken')
  if (to.meta.requiresAuth !== false && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/')
  } else {
    next()
  }
})

export default router
