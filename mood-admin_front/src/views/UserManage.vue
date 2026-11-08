<template>
  <div class="page-container">
    <div class="page-header">
      <div class="filter-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索用户名/昵称"
          style="width: 240px"
          clearable
          @clear="loadUsers"
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        
        <el-select v-model="statusFilter" placeholder="状态筛选" style="width: 140px" clearable @change="handleSearch">
          <el-option label="正常" value="normal" />
          <el-option label="已封禁" value="banned" />
        </el-select>
        
        <el-button type="primary" @click="handleSearch">
          <el-icon><Search /></el-icon>
          搜索
        </el-button>
      </div>
    </div>
    
    <el-table :data="filteredUsers" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" align="center" />
      <el-table-column label="头像" width="80" align="center">
        <template #default="{ row }">
          <el-avatar :size="40" :src="row.avatarUrl || ''" class="user-avatar" v-if="row.avatarUrl">
          </el-avatar>
          <el-avatar :size="40" class="user-avatar" v-else>
            <el-icon :size="20"><UserFilled /></el-icon>
          </el-avatar>
        </template>
      </el-table-column>
      <el-table-column prop="username" label="用户名" width="140" />
      <el-table-column prop="nickname" label="昵称" width="140" />
      <el-table-column label="性别" width="80" align="center">
        <template #default="{ row }">
          <span v-if="row.gender === 1">男</span>
          <span v-else-if="row.gender === 2">女</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" width="140">
        <template #default="{ row }">
          {{ row.phone || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="心动币" width="100" align="center">
        <template #default="{ row }">
          <span class="coin-cell">{{ row.coinBalance || 0 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="90" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 'banned' ? 'danger' : 'success'" size="small">
            {{ row.status === 'banned' ? '已封禁' : '正常' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="注册时间" width="180" />
      <el-table-column label="操作" fixed="right" width="300" align="center">
        <template #default="{ row }">
          <div class="action-buttons">
            <el-button size="small" class="btn-gift" @click="showGiftDialog(row)">赠送心动币</el-button>
            <el-button
              v-if="row.status !== 'banned'"
              size="small"
              class="btn-ban"
              @click="handleBan(row)"
            >
              封禁
            </el-button>
            <el-button
              v-else
              size="small"
              class="btn-unban"
              @click="handleUnban(row)"
            >
              解封
            </el-button>
            <el-button size="small" class="btn-delete" @click="handleDelete(row)">删除</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    
    <el-dialog v-model="giftDialogVisible" title="赠送心动币" width="400px">
      <el-form :model="giftForm" label-width="80px">
        <el-form-item label="用户">
          <span>{{ giftForm.user?.nickname || giftForm.user?.username }}</span>
        </el-form-item>
        <el-form-item label="赠送数量">
          <el-input-number v-model="giftForm.amount" :min="1" :max="10000" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="giftDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleGiftCoins">确认赠送</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, UserFilled } from '@element-plus/icons-vue'
import { getUserList, banUser, unbanUser, deleteUser, giftCoins } from '@/api/admin'

const loading = ref(false)
const users = ref([])
const searchKeyword = ref('')
const statusFilter = ref('')
const giftDialogVisible = ref(false)
const giftForm = ref({ user: null, amount: 100 })

const filteredUsers = computed(() => {
  let result = users.value
  if (statusFilter.value) {
    result = result.filter(u => u.status === statusFilter.value)
  }
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(u => 
      (u.username && u.username.toLowerCase().includes(keyword)) ||
      (u.nickname && u.nickname.toLowerCase().includes(keyword))
    )
  }
  return result
})

const loadUsers = async () => {
  loading.value = true
  try {
    const res = await getUserList()
    users.value = res.data || []
  } catch (err) {
    console.error('加载用户列表失败', err)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {}

const showGiftDialog = (user) => {
  giftForm.value = { user, amount: 100 }
  giftDialogVisible.value = true
}

const handleGiftCoins = async () => {
  try {
    await giftCoins({ userId: giftForm.value.user.id, amount: giftForm.value.amount })
    ElMessage.success('赠送成功')
    giftDialogVisible.value = false
    loadUsers()
  } catch (err) {
    console.error('赠送失败', err)
  }
}

const handleBan = async (user) => {
  try {
    await ElMessageBox.confirm(`确定要封禁用户「${user.nickname}」吗？`, '确认封禁', {
      confirmButtonText: '封禁',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await banUser(user.id)
    ElMessage.success('封禁成功')
    loadUsers()
  } catch (err) {
    if (err !== 'cancel') console.error('封禁失败', err)
  }
}

const handleUnban = async (user) => {
  try {
    await unbanUser(user.id)
    ElMessage.success('解封成功')
    loadUsers()
  } catch (err) {
    console.error('解封失败', err)
  }
}

const handleDelete = async (user) => {
  try {
    await ElMessageBox.confirm(`确定要删除用户「${user.nickname}」吗？此操作不可恢复`, '确认删除', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'error'
    })
    await deleteUser(user.id)
    ElMessage.success('删除成功')
    loadUsers()
  } catch (err) {
    if (err !== 'cancel') console.error('删除失败', err)
  }
}

onMounted(() => {
  loadUsers()
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

.user-avatar {
  background-color: var(--bg-base);
  border: 1px solid var(--border-base);
}

.coin-cell {
  color: var(--color-primary);
  font-weight: 500;
}

.action-buttons {
  display: flex;
  gap: 6px;
  justify-content: center;
  flex-wrap: nowrap;
}

.btn-gift {
  background-color: var(--color-primary);
  border-color: var(--color-primary);
  color: #fff;
  
  &:hover {
    background-color: var(--color-primary-light);
    border-color: var(--color-primary-light);
  }
}

.btn-ban {
  background-color: var(--color-danger);
  border-color: var(--color-danger);
  color: #fff;
  
  &:hover {
    background-color: #E27A7A;
    border-color: #E27A7A;
  }
}

.btn-unban {
  background-color: var(--color-success);
  border-color: var(--color-success);
  color: #fff;
  
  &:hover {
    background-color: #8BA88E;
    border-color: #8BA88E;
  }
}

.btn-delete {
  background-color: var(--text-secondary);
  border-color: var(--text-secondary);
  color: #fff;
  
  &:hover {
    background-color: var(--text-regular);
    border-color: var(--text-regular);
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

:deep(.el-table__fixed-right) {
  border-left: 1px solid var(--border-light);
  box-shadow: -2px 0 4px rgba(0, 0, 0, 0.05);
}

:deep(.el-table__fixed-right-patch) {
  background-color: var(--bg-card);
}

:deep(.el-input__wrapper) {
  border-radius: var(--radius-base);
  box-shadow: none;
  border: 1px solid var(--border-input);
}

:deep(.el-button--primary) {
  background-color: var(--color-primary);
  border-color: var(--color-primary);
  
  &:hover {
    background-color: var(--color-primary-light);
    border-color: var(--color-primary-light);
  }
}

:deep(.el-button--danger) {
  background-color: var(--color-danger);
  border-color: var(--color-danger);
}

:deep(.el-button--success) {
  background-color: var(--color-success);
  border-color: var(--color-success);
}
</style>
