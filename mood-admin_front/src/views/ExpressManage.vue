<template>
  <div class="page-container">
    <div class="page-header">
      <div class="filter-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索订单号"
          style="width: 240px"
          clearable
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </div>
    </div>
    
    <el-table :data="filteredOrders" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" align="center" />
      <el-table-column prop="orderNo" label="订单号" width="200" />
      <el-table-column label="用户" width="100">
        <template #default="{ row }">
          <span class="user-cell">{{ row.userNickname || '未知' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="商品" min-width="180">
        <template #default="{ row }">
          <div class="product-cell">
            <template v-if="row.items && row.items.length > 0">
              <span
                v-for="(item, idx) in row.items"
                :key="idx"
                class="product-name"
              >
                {{ truncateName(item.productName) }}
              </span>
            </template>
            <span v-else class="no-product">-</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="快递公司" width="120">
        <template #default="{ row }">{{ row.expressCompany || '-' }}</template>
      </el-table-column>
      <el-table-column label="快递单号" width="150">
        <template #default="{ row }">{{ row.expressNo || '-' }}</template>
      </el-table-column>
      <el-table-column label="派送人" width="100">
        <template #default="{ row }">{{ row.expressCourier || '-' }}</template>
      </el-table-column>
      <el-table-column label="快递状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="row.expressStatus === 'delivering' ? 'warning' : 'success'" size="small">
            {{ row.expressStatus === 'delivering' ? '派送中' : row.expressStatus === 'signed' ? '已签收' : '未知' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="下单时间" width="180" />
      <el-table-column label="操作" fixed="right" width="200" align="center">
        <template #default="{ row }">
          <div class="action-buttons">
            <el-button size="small" class="btn-update" @click="handleUpdateStatus(row)">
              更新状态
            </el-button>
            <el-button size="small" class="btn-refund" @click="handleRefund(row)">
              退款
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 更新快递状态对话框 -->
    <el-dialog v-model="dialogVisible" title="更新快递状态" width="400px">
      <div v-if="currentOrder" class="express-info">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="订单号">{{ currentOrder.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="快递公司">{{ currentOrder.expressCompany || '-' }}</el-descriptions-item>
          <el-descriptions-item label="快递单号">{{ currentOrder.expressNo || '-' }}</el-descriptions-item>
          <el-descriptions-item label="派送人">{{ currentOrder.expressCourier || '-' }}</el-descriptions-item>
          <el-descriptions-item label="当前状态">
            <el-tag :type="currentOrder.expressStatus === 'delivering' ? 'warning' : 'success'" size="small">
              {{ currentOrder.expressStatus === 'delivering' ? '派送中' : currentOrder.expressStatus === 'signed' ? '已签收' : '未知' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
        
        <el-form style="margin-top: 20px;">
          <el-form-item label="新状态" required>
            <el-select v-model="newStatus" placeholder="选择新状态" style="width: 100%;">
              <el-option label="派送中" value="delivering" />
              <el-option label="已签收" value="signed" />
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmUpdate">确认更新</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { getShippedOrders, updateExpressStatus, refundOrder } from '@/api/express'

const loading = ref(false)
const orders = ref([])
const searchKeyword = ref('')
const dialogVisible = ref(false)
const currentOrder = ref(null)
const newStatus = ref('')

const filteredOrders = computed(() => {
  if (!searchKeyword.value) return orders.value
  return orders.value.filter(o => o.orderNo && o.orderNo.includes(searchKeyword.value))
})

const truncateName = (name) => {
  if (!name) return ''
  return name.length > 12 ? name.slice(0, 12) + '...' : name
}

const loadOrders = async () => {
  loading.value = true
  try {
    const res = await getShippedOrders()
    orders.value = res.data || []
  } catch (err) {
    console.error('加载订单列表失败', err)
  } finally {
    loading.value = false
  }
}

const handleUpdateStatus = (order) => {
  currentOrder.value = order
  newStatus.value = order.expressStatus || 'delivering'
  dialogVisible.value = true
}

const confirmUpdate = async () => {
  try {
    await updateExpressStatus(currentOrder.value.id, { expressStatus: newStatus.value })
    ElMessage.success('快递状态更新成功')
    dialogVisible.value = false
    loadOrders()
  } catch (err) {
    console.error('更新快递状态失败', err)
  }
}

const handleRefund = async (order) => {
  try {
    await refundOrder(order.id)
    ElMessage.success('退款成功')
    loadOrders()
  } catch (err) {
    console.error('退款失败', err)
  }
}

onMounted(() => {
  loadOrders()
})
</script>

<style lang="scss" scoped>
.page-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.filter-bar {
  display: flex;
  gap: 12px;
  align-items: center;
}

.user-cell {
  font-size: 13px;
}

.product-cell {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.product-name {
  font-size: 13px;
  color: var(--color-primary);
}

.no-product {
  font-size: 13px;
  color: var(--text-secondary);
}

.btn-update {
  background-color: var(--color-warning);
  border-color: var(--color-warning);
  color: #fff;
  
  &:hover {
    background-color: #E6A23C;
    border-color: #E6A23C;
  }
}

.btn-refund {
  background-color: var(--color-danger);
  border-color: var(--color-danger);
  color: #fff;
  
  &:hover {
    background-color: #E27A7A;
    border-color: #E27A7A;
  }
}

.action-buttons {
  display: flex;
  gap: 8px;
  justify-content: center;
}

.express-info {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

:deep(.el-table) {
  --el-table-border-color: var(--border-light);
  --el-table-header-bg-color: var(--bg-base);
  
  th.el-table__cell {
    font-weight: 500;
    color: var(--text-regular);
  }
  
  .el-table__row {
    height: var(--table-cell-height);
  }
}

:deep(.el-input__wrapper) {
  border-radius: var(--radius-base);
  box-shadow: none;
  border: 1px solid var(--border-input);
}

:deep(.el-descriptions__label) {
  color: var(--text-secondary);
}
</style>
