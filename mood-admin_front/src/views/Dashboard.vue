<template>
  <div class="dashboard">
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon" style="background: #E8F0E8">
          <el-icon :size="24"><ShoppingCart /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ dashboard.completedOrders || 0 }}</span>
          <span class="stat-label">成交单量</span>
          <span class="stat-sub">成交金额：{{ dashboard.completedAmount || 0 }} 心动币</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: #FDE8D0">
          <el-icon :size="24"><RefreshLeft /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ dashboard.refundedOrders || 0 }}</span>
          <span class="stat-label">退款单量</span>
          <span class="stat-sub">退款金额：{{ dashboard.refundedAmount || 0 }} 心动币</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: #F5EDE3">
          <el-icon :size="24"><Clock /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ dashboard.pendingOrders || 0 }}</span>
          <span class="stat-label">待发货单量</span>
          <span class="stat-sub">等待处理</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: #FDE8E8">
          <el-icon :size="24"><Warning /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ pendingRefundCount || 0 }}</span>
          <span class="stat-label">待处理退款</span>
          <span class="stat-sub">需要审核</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: #F0E4D8">
          <el-icon :size="24"><ChatDotRound /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ postsCount }}</span>
          <span class="stat-label">帖子总数</span>
          <span class="stat-sub">社区活跃</span>
        </div>
      </div>
    </div>
    
    <div class="chart-section">
      <div class="section-header">
        <h3 class="section-title">近7日订单趋势</h3>
        <div class="section-actions">
          <el-button size="small" @click="loadDashboardData">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
      </div>
      
      <div class="chart-container" v-loading="loading">
        <div v-if="orderTrend.length === 0" class="empty-chart">
          <p>暂无数据</p>
        </div>
        <div v-else class="trend-table">
          <div class="trend-header">
            <span class="trend-col date-col">日期</span>
            <span class="trend-col">成交单量</span>
            <span class="trend-col">成交金额</span>
            <span class="trend-col">退款单量</span>
            <span class="trend-col">退款金额</span>
            <span class="trend-col">待发货</span>
          </div>
          <div v-for="(item, index) in orderTrend" :key="index" class="trend-row">
            <span class="trend-col date-col">{{ item.date }}</span>
            <span class="trend-col amount-col success">{{ item.completedCount }}</span>
            <span class="trend-col amount-col success-amount">{{ item.completedAmount }}</span>
            <span class="trend-col amount-col danger">{{ item.refundedCount }}</span>
            <span class="trend-col amount-col danger-amount">{{ item.refundedAmount }}</span>
            <span class="trend-col amount-col warning">{{ item.pendingCount }}</span>
          </div>
        </div>
      </div>
    </div>
    
    <div class="recent-section">
      <div class="section-header">
        <h3 class="section-title">最近订单</h3>
        <el-button size="small" text @click="$router.push('/orders')">
          查看全部
        </el-button>
      </div>
      
      <el-table :data="recentOrders" style="width: 100%" v-loading="loading">
        <el-table-column prop="orderNo" label="订单号" width="200" />
        <el-table-column label="用户" width="120">
          <template #default="{ row }">
            <span class="user-cell">{{ row.userNickname || '未知' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="金额" width="120">
          <template #default="{ row }">
            <span class="amount-cell">{{ row.totalAmount }} 心动币</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" size="small">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ShoppingCart, RefreshLeft, Clock, ChatDotRound, Refresh, Warning } from '@element-plus/icons-vue'
import { getDashboardData, getOrderList, getPendingRefunds } from '@/api/order'
import { getUserList } from '@/api/admin'
import { getProductList } from '@/api/product'
import { getPostList } from '@/api/post'
import dayjs from 'dayjs'

const loading = ref(false)
const dashboard = ref({})
const postsCount = ref(0)
const pendingRefundCount = ref(0)
const orderTrend = ref([])
const recentOrders = ref([])

const getStatusType = (status) => {
  const map = { pending: 'warning', shipped: 'success', refunded: 'danger', cancelled: 'info', completed: 'success' }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = { pending: '待发货', shipped: '已发货', refunded: '已退款', cancelled: '已取消', completed: '已完成' }
  return map[status] || status
}

const loadDashboardData = async () => {
  loading.value = true
  try {
    const [usersRes, productsRes, postsRes, ordersRes, dashboardRes, pendingRefundsRes] = await Promise.all([
      getUserList(),
      getProductList(),
      getPostList(),
      getOrderList(),
      getDashboardData(7),
      getPendingRefunds()
    ])
    
    postsCount.value = postsRes.data?.length || 0
    pendingRefundCount.value = pendingRefundsRes.data?.length || 0
    
    if (dashboardRes.data?.stats) {
      dashboard.value = dashboardRes.data.stats
    }
    
    if (dashboardRes.data?.dailyOrders) {
      orderTrend.value = dashboardRes.data.dailyOrders
        .sort((a, b) => dayjs(b.date).valueOf() - dayjs(a.date).valueOf())
        .map(item => ({
          date: dayjs(item.date).format('MM-DD'),
          completedCount: item.completedCount || 0,
          completedAmount: item.completedAmount || 0,
          refundedCount: item.refundedCount || 0,
          refundedAmount: item.refundedAmount || 0,
          pendingCount: item.pendingCount || 0
        }))
    }
    
    if (ordersRes.data) {
      recentOrders.value = ordersRes.data.slice(0, 5)
    }
  } catch (err) {
    console.error('加载数据失败', err)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadDashboardData()
})
</script>

<style lang="scss" scoped>
.dashboard {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 16px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  background: var(--bg-card);
  border: 1px solid var(--border-base);
  border-radius: var(--radius-base);
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-base);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  flex-shrink: 0;
}

.stat-content {
  display: flex;
  flex-direction: column;
}

.stat-value {
  font-size: 24px;
  font-weight: 500;
  color: var(--text-primary);
  line-height: 1.2;
}

.stat-label {
  font-size: 13px;
  color: var(--text-secondary);
  margin-top: 4px;
}

.stat-sub {
  font-size: 12px;
  color: var(--text-secondary);
  margin-top: 2px;
}

.chart-section, .recent-section {
  background: var(--bg-card);
  border: 1px solid var(--border-base);
  border-radius: var(--radius-base);
  padding: 20px;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.section-title {
  font-family: var(--font-serif);
  font-size: 16px;
  font-weight: 500;
  color: var(--text-primary);
}

.chart-container {
  min-height: 200px;
}

.empty-chart {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 200px;
  color: var(--text-secondary);
}

.trend-table {
  display: flex;
  flex-direction: column;
  gap: 0;
}

.trend-header, .trend-row {
  display: flex;
  align-items: center;
  padding: 10px 12px;
  border-bottom: 1px solid var(--border-light);
}

.trend-header {
  background: var(--bg-base);
  font-weight: 500;
  font-size: 13px;
  color: var(--text-regular);
}

.trend-row {
  font-size: 13px;
  color: var(--text-primary);
  
  &:last-child {
    border-bottom: none;
  }
  
  &:hover {
    background: var(--bg-base);
  }
}

.trend-col {
  flex: 1;
  text-align: center;
}

.date-col {
  flex: 1.2;
  text-align: left;
  color: var(--text-secondary);
}

.amount-col {
  font-weight: 500;
}

.success {
  color: var(--color-success);
}

.success-amount {
  color: var(--color-success);
}

.danger {
  color: var(--color-danger);
}

.danger-amount {
  color: var(--color-danger);
}

.warning {
  color: #E6A23C;
}

.user-cell, .amount-cell {
  font-size: 13px;
}

.amount-cell {
  color: var(--color-primary);
  font-weight: 500;
}

:deep(.el-table) {
  --el-table-border-color: var(--border-light);
  --el-table-header-bg-color: var(--bg-base);
  
  th.el-table__cell {
    font-weight: 500;
    color: var(--text-regular);
  }
}
</style>
