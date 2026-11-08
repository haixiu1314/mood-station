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
        
        <el-select v-model="statusFilter" placeholder="状态筛选" style="width: 140px" clearable>
          <el-option label="待发货" value="pending" />
          <el-option label="已发货" value="shipped" />
          <el-option label="已退款" value="refunded" />
          <el-option label="已取消" value="cancelled" />
          <el-option label="已完成" value="completed" />
        </el-select>

        <!-- 待处理订单按钮 -->
        <el-button 
          :type="pendingRefundCount > 0 ? 'danger' : ''" 
          :icon="Bell" 
          @click="showPendingRefunds = true"
          class="pending-btn"
        >
          待处理订单
          <el-badge v-if="pendingRefundCount > 0" :value="pendingRefundCount" class="pending-badge" />
        </el-button>

        <!-- 删除订单按钮 -->
        <el-button 
          :type="deleteMode ? 'danger' : ''" 
          :icon="Delete" 
          @click="toggleDeleteMode"
          class="delete-btn"
        >
          {{ deleteMode ? '取消删除' : '删除订单' }}
        </el-button>
      </div>
    </div>
    
    <el-table :data="filteredOrders" style="width: 100%" v-loading="loading">
      <el-table-column v-if="deleteMode" label="" width="60" align="center" fixed="left">
        <template #default="{ row }">
          <el-button 
            type="danger" 
            :icon="Minus" 
            circle 
            size="small"
            @click="handleDeleteOrder(row)"
            class="delete-row-btn"
          />
        </template>
      </el-table-column>
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
                :title="item.productName"
                @click="showProductDetail(item.productName)"
              >
                {{ truncateName(item.productName) }}
              </span>
            </template>
            <span v-else class="no-product">-</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="金额" width="100" align="center">
        <template #default="{ row }">
          <span class="amount-cell">{{ row.totalAmount }} 心动币</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="90" align="center">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)" size="small">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="联系电话" width="130">
        <template #default="{ row }">
          <span class="phone-cell">{{ row.addressPhone || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="收货地址" min-width="200" show-overflow-tooltip>
        <template #default="{ row }">
          {{ row.addressDetail || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" fixed="right" width="200" align="center">
        <template #default="{ row }">
          <div class="action-buttons">
            <el-button
              v-if="row.status === 'pending'"
              size="small"
              class="btn-ship"
              @click="handleShip(row)"
            >
              发货
            </el-button>
            <el-button size="small" class="btn-detail" @click="showDetailDialog(row)">详情</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 订单详情对话框 -->
    <el-dialog v-model="detailDialogVisible" title="订单详情" width="600px">
      <div v-if="currentOrder" class="order-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单号">{{ currentOrder.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(currentOrder.status)" size="small">
              {{ getStatusText(currentOrder.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="金额">{{ currentOrder.totalAmount }} 心动币</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentOrder.createTime }}</el-descriptions-item>
          <el-descriptions-item label="收货人">{{ currentOrder.addressName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ currentOrder.addressPhone || '-' }}</el-descriptions-item>
          <el-descriptions-item label="收货地址" :span="2">{{ currentOrder.addressDetail || '-' }}</el-descriptions-item>
          <el-descriptions-item v-if="currentOrder.expressCompany" label="快递公司">{{ currentOrder.expressCompany }}</el-descriptions-item>
          <el-descriptions-item v-if="currentOrder.expressNo" label="快递单号">{{ currentOrder.expressNo }}</el-descriptions-item>
          <el-descriptions-item v-if="currentOrder.expressCourier" label="派送人">{{ currentOrder.expressCourier }}</el-descriptions-item>
          <el-descriptions-item v-if="currentOrder.expressStatus" label="快递状态">
            {{ currentOrder.expressStatus === 'delivering' ? '派送中' : currentOrder.expressStatus === 'signed' ? '已签收' : '' }}
          </el-descriptions-item>
        </el-descriptions>
        
        <div class="order-items">
          <h4 class="items-title">商品清单</h4>
          <el-table :data="currentOrder.items || []" style="width: 100%">
            <el-table-column prop="productName" label="商品名称" />
            <el-table-column prop="selectedSpec" label="规格" width="120" align="center">
              <template #default="{ row }">
                <el-tag v-if="row.selectedSpec" size="small" type="info">{{ row.selectedSpec }}</el-tag>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column label="单价" width="120" align="center">
              <template #default="{ row }">
                {{ row.price }} 心动币
              </template>
            </el-table-column>
            <el-table-column prop="quantity" label="数量" width="80" align="center" />
          </el-table>
        </div>
      </div>
    </el-dialog>

    <!-- 发货对话框 -->
    <el-dialog v-model="shipDialogVisible" title="订单发货" width="550px">
      <div v-if="currentOrder" class="ship-form">
        <el-descriptions :column="1" border class="address-info">
          <el-descriptions-item label="收货人">{{ currentOrder.addressName }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ currentOrder.addressPhone }}</el-descriptions-item>
          <el-descriptions-item label="收货地址">{{ currentOrder.addressDetail }}</el-descriptions-item>
        </el-descriptions>
        
        <el-form :model="shipForm" label-width="90px" style="margin-top: 20px;">
          <el-form-item label="快递公司" required>
            <el-select v-model="shipForm.expressCompany" placeholder="请选择快递公司" style="width: 100%" @change="onExpressCompanyChange">
              <el-option
                v-for="item in expressCompanies"
                :key="item.code"
                :label="item.name"
                :value="item.name"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="快递单号" required>
            <el-input v-model="shipForm.expressNo" :placeholder="expressNoPlaceholder" />
          </el-form-item>
          <div style="height: 12px;"></div>
          <el-divider content-position="center" style="margin: 0 0 16px 0;">发货人信息</el-divider>
          <el-form-item label="发货人" required>
            <el-input v-model="shipForm.senderName" placeholder="请输入发货人姓名" />
          </el-form-item>
          <el-form-item label="发货地址" required>
            <el-input v-model="shipForm.senderAddress" placeholder="请输入发货地址" />
          </el-form-item>
          <el-form-item label="联系电话" required>
            <el-input v-model="shipForm.senderPhone" placeholder="请输入发货人电话" />
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <el-button @click="shipDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmShip">确认发货</el-button>
      </template>
    </el-dialog>

    <!-- 待处理退款申请对话框 -->
    <el-dialog v-model="showPendingRefunds" title="待处理退款申请" width="700px">
      <el-table :data="pendingRefunds" style="width: 100%" v-loading="refundLoading">
        <el-table-column prop="id" label="ID" width="60" align="center" />
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column label="用户" width="100">
          <template #default="{ row }">{{ row.userNickname || '未知' }}</template>
        </el-table-column>
        <el-table-column label="金额" width="100" align="center">
          <template #default="{ row }">{{ row.totalAmount }} 心动币</template>
        </el-table-column>
        <el-table-column prop="reason" label="退款原因" min-width="200" show-overflow-tooltip />
        <el-table-column prop="applyTime" label="申请时间" width="180" />
        <el-table-column label="操作" width="160" align="center">
          <template #default="{ row }">
            <div class="refund-action">
              <el-button size="default" type="success" @click="handleApproveRefund(row)">退款</el-button>
              <el-button size="default" type="danger" @click="handleRejectRefund(row)">拒绝</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
    
    <el-dialog v-model="productDialogVisible" :title="currentProductName" width="400px">
      <p class="product-full-name">{{ currentProductName }}</p>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Bell, Delete, Minus } from '@element-plus/icons-vue'
import { getOrderList, shipOrder, refundOrder, getPendingRefunds, approveRefund, rejectRefund, deleteOrder } from '@/api/order'
import { getExpressCompanies } from '@/api/express'

const loading = ref(false)
const orders = ref([])
const searchKeyword = ref('')
const statusFilter = ref('')
const detailDialogVisible = ref(false)
const currentOrder = ref(null)
const productDialogVisible = ref(false)
const currentProductName = ref('')

// 发货相关
const shipDialogVisible = ref(false)
const shipForm = ref({
  expressCompany: '',
  expressNo: '',
  senderName: '',
  senderAddress: '',
  senderPhone: ''
})
const expressCompanies = ref([])
const expressNoPlaceholder = ref('请输入快递单号')

// 待处理退款
const showPendingRefunds = ref(false)
const pendingRefunds = ref([])
const refundLoading = ref(false)
const pendingRefundCount = ref(0)

// 删除模式
const deleteMode = ref(false)

const filteredOrders = computed(() => {
  let result = orders.value
  if (statusFilter.value) {
    result = result.filter(o => o.status === statusFilter.value)
  }
  if (searchKeyword.value) {
    result = result.filter(o => o.orderNo && o.orderNo.includes(searchKeyword.value))
  }
  return result
})

const truncateName = (name) => {
  if (!name) return ''
  return name.length > 12 ? name.slice(0, 12) + '...' : name
}

const showProductDetail = (name) => {
  if (name && name.length > 12) {
    currentProductName.value = name
    productDialogVisible.value = true
  }
}

const getStatusType = (status) => {
  const map = { pending: 'warning', shipped: 'success', refunded: 'danger', cancelled: 'info', completed: 'success' }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = { pending: '待发货', shipped: '已发货', refunded: '已退款', cancelled: '已取消', completed: '已完成' }
  return map[status] || status
}

const loadOrders = async () => {
  loading.value = true
  try {
    const res = await getOrderList()
    orders.value = res.data || []
  } catch (err) {
    console.error('加载订单列表失败', err)
  } finally {
    loading.value = false
  }
}

const loadPendingRefunds = async () => {
  refundLoading.value = true
  try {
    const res = await getPendingRefunds()
    pendingRefunds.value = res.data || []
    pendingRefundCount.value = pendingRefunds.value.length
  } catch (err) {
    console.error('加载待处理退款失败', err)
  } finally {
    refundLoading.value = false
  }
}

const handleShip = async (order) => {
  currentOrder.value = order
  shipForm.value = {
    expressCompany: '',
    expressNo: '',
    senderName: '',
    senderAddress: '',
    senderPhone: ''
  }
  expressNoPlaceholder.value = '请输入快递单号'
  shipDialogVisible.value = true
}

const onExpressCompanyChange = (companyName) => {
  const company = expressCompanies.value.find(c => c.name === companyName)
  if (company && company.placeholder) {
    expressNoPlaceholder.value = company.placeholder
  } else {
    expressNoPlaceholder.value = '请输入快递单号'
  }
}

const confirmShip = async () => {
  if (!shipForm.value.expressCompany || !shipForm.value.expressNo) {
    ElMessage.warning('请填写快递公司和快递单号')
    return
  }
  if (!shipForm.value.senderName || !shipForm.value.senderAddress || !shipForm.value.senderPhone) {
    ElMessage.warning('请填写完整的发货人信息')
    return
  }
  try {
    await shipOrder(currentOrder.value.id, shipForm.value)
    ElMessage.success('发货成功')
    shipDialogVisible.value = false
    loadOrders()
  } catch (err) {
    console.error('发货失败', err)
  }
}

const handleApproveRefund = async (refund) => {
  try {
    await ElMessageBox.confirm(`确定同意订单「${refund.orderNo}」的退款申请吗？心动币将退还给用户`, '确认退款', {
      confirmButtonText: '同意',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await approveRefund(refund.id)
    ElMessage.success('退款成功')
    loadPendingRefunds()
    loadOrders()
  } catch (err) {
    if (err !== 'cancel') console.error('退款失败', err)
  }
}

const handleRejectRefund = async (refund) => {
  try {
    await ElMessageBox.confirm(`确定拒绝订单「${refund.orderNo}」的退款申请吗？`, '确认拒绝', {
      confirmButtonText: '拒绝',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await rejectRefund(refund.id)
    ElMessage.success('已拒绝退款申请')
    loadPendingRefunds()
    loadOrders()
  } catch (err) {
    if (err !== 'cancel') console.error('拒绝失败', err)
  }
}

const showDetailDialog = (order) => {
  currentOrder.value = order
  detailDialogVisible.value = true
}

const toggleDeleteMode = () => {
  deleteMode.value = !deleteMode.value
}

const handleDeleteOrder = async (order) => {
  try {
    await ElMessageBox.confirm('是否删除订单，删除后将无法恢复', '确认删除', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteOrder(order.id)
    ElMessage.success('订单已删除')
    loadOrders()
  } catch (err) {
    if (err !== 'cancel') console.error('删除失败', err)
  }
}

const loadExpressCompanies = async () => {
  try {
    const res = await getExpressCompanies()
    expressCompanies.value = res.data || []
  } catch (err) {
    console.error('加载快递公司列表失败', err)
  }
}

onMounted(() => {
  loadOrders()
  loadPendingRefunds()
  loadExpressCompanies()
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

.pending-btn {
  position: relative;
}

.pending-badge {
  position: absolute;
  top: -8px;
  right: -8px;
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
  cursor: pointer;
  
  &:hover {
    text-decoration: underline;
  }
}

.no-product {
  font-size: 13px;
  color: var(--text-secondary);
}

.amount-cell {
  color: var(--color-primary);
  font-weight: 500;
  font-size: 13px;
}

.phone-cell {
  font-size: 13px;
  color: var(--text-regular);
}

.action-buttons {
  display: flex;
  gap: 4px;
  justify-content: center;
  flex-wrap: nowrap;
}

.btn-ship {
  background-color: var(--color-success);
  border-color: var(--color-success);
  color: #fff;
  
  &:hover {
    background-color: #8BA88E;
    border-color: #8BA88E;
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

.btn-detail {
  background-color: var(--color-primary);
  border-color: var(--color-primary);
  color: #fff;
  
  &:hover {
    background-color: var(--color-primary-light);
    border-color: var(--color-primary-light);
  }
}

.delete-btn {
  &:hover {
    background-color: var(--color-danger);
    border-color: var(--color-danger);
  }
}

.order-detail {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.items-title {
  font-size: 14px;
  font-weight: 500;
  color: var(--text-primary);
  margin-bottom: 12px;
}

.product-full-name {
  font-size: 14px;
  line-height: 1.6;
  color: var(--text-primary);
  word-break: break-all;
}

.ship-form {
  .address-info {
    margin-bottom: 16px;
  }
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

:deep(.el-button--primary) {
  background-color: var(--color-primary);
  border-color: var(--color-primary);
  
  &:hover {
    background-color: var(--color-primary-light);
    border-color: var(--color-primary-light);
  }
}

.refund-action {
  display: flex;
  gap: 8px;
  justify-content: center;
  align-items: center;
}

.delete-row-btn {
  width: 24px !important;
  height: 24px !important;
  min-width: 24px !important;
  padding: 0 !important;
  display: inline-flex !important;
  align-items: center;
  justify-content: center;
}
</style>
