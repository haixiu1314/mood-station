<template>
  <div class="page-container">
    <div class="page-header">
      <div class="filter-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索昵称或反馈内容"
          style="width: 280px"
          clearable
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </div>
    </div>

    <el-table :data="filteredList" style="width: 100%" v-loading="loading" empty-text="暂无用户反馈">
      <el-table-column type="expand">
        <template #default="{ row }">
          <div class="feedback-detail">
            <div class="feedback-detail-label">反馈内容</div>
            <div class="feedback-detail-content">{{ row.content }}</div>
            <div class="feedback-detail-meta">
              用户ID：{{ row.userId }}　昵称：{{ row.nickname || '匿名' }}　提交时间：{{ row.createTime }}
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column label="用户" width="160">
        <template #default="{ row }">{{ row.nickname || '匿名' }}</template>
      </el-table-column>
      <el-table-column prop="content" label="反馈内容" min-width="320" show-overflow-tooltip />
      <el-table-column prop="createTime" label="提交时间" width="180" />
    </el-table>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { getFeedbackList } from '@/api/feedback'

const loading = ref(false)
const feedbackList = ref([])
const searchKeyword = ref('')

const filteredList = computed(() => {
  if (!searchKeyword.value) return feedbackList.value
  const keyword = searchKeyword.value.toLowerCase()
  return feedbackList.value.filter(f =>
    (f.nickname && f.nickname.toLowerCase().includes(keyword)) ||
    (f.content && f.content.toLowerCase().includes(keyword))
  )
})

const loadFeedback = async () => {
  loading.value = true
  try {
    const res = await getFeedbackList()
    feedbackList.value = res.data || []
  } catch (err) {
    console.error('加载反馈列表失败', err)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadFeedback()
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

.feedback-detail {
  padding: 16px 24px;
  background-color: var(--bg-base);
  border-radius: 8px;

  .feedback-detail-label {
    font-size: 12px;
    color: var(--text-secondary);
    margin-bottom: 8px;
  }

  .feedback-detail-content {
    font-size: 14px;
    color: var(--text-regular);
    line-height: 1.8;
    white-space: pre-wrap;
    word-break: break-word;
  }

  .feedback-detail-meta {
    margin-top: 12px;
    font-size: 12px;
    color: var(--text-secondary);
  }
}

:deep(.el-table) {
  --el-table-border-color: var(--border-light);
  --el-table-header-bg-color: var(--bg-base);

  th.el-table__cell {
    font-weight: 500;
    color: var(--text-regular);
  }
}

:deep(.el-input__wrapper) {
  border-radius: var(--radius-base);
  box-shadow: none;
  border: 1px solid var(--border-input);
}
</style>
