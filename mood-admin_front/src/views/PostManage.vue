<template>
  <div class="page-container">
    <el-tabs v-model="activeTab" class="content-tabs">
      <el-tab-pane label="帖子管理" name="posts" />
      <el-tab-pane label="评论管理" name="comments" />
      <el-tab-pane name="appeals">
        <template #label>
          <span>申诉管理</span>
          <el-badge v-if="appealCount > 0" :value="appealCount" :max="99" class="appeal-badge" />
        </template>
      </el-tab-pane>
    </el-tabs>
    
    <div v-if="activeTab === 'posts'" class="tab-content">
      <div class="page-header">
        <div class="filter-bar">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索帖子内容"
            style="width: 240px"
            clearable
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </div>
      </div>
      
      <el-table :data="posts" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="70" align="center" />
        <el-table-column label="作者" width="120">
          <template #default="{ row }">
            <div class="author-cell">
              <el-avatar :size="24" :src="row.authorAvatar" class="author-avatar" />
              <span>{{ row.authorNickname || '未知' }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="标题" width="180">
          <template #default="{ row }">
            <span class="title-cell" :title="row.title">{{ row.title || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="情绪" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.emotionLabel" :style="{ backgroundColor: getEmotionColor(row.emotion), color: row.emotion === 'happy' ? '#2D5016' : '#fff', border: 'none' }" size="small">
              {{ row.emotionLabel }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="内容" min-width="250">
          <template #default="{ row }">
            <span class="content-cell" :class="{ expanded: expandedPosts[row.id] }" @click="toggleExpand('posts', row.id)">
              {{ expandedPosts[row.id] ? row.content : (row.content?.substring(0, 50) + (row.content?.length > 50 ? '...' : '')) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="点赞" width="80" align="center">
          <template #default="{ row }">
            {{ row.likeCount || 0 }}
          </template>
        </el-table-column>
        <el-table-column label="评论" width="80" align="center">
          <template #default="{ row }">
            {{ row.commentCount || 0 }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" fixed="right" width="120" align="center">
          <template #default="{ row }">
            <el-button size="small" class="btn-delete" @click="handleDeletePost(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <div v-if="activeTab === 'comments'" class="tab-content">
      <div class="page-header">
        <div class="filter-bar">
          <el-input
            v-model="commentSearchKeyword"
            placeholder="搜索评论内容"
            style="width: 240px"
            clearable
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </div>
      </div>
      
      <el-table :data="filteredComments" style="width: 100%" v-loading="commentLoading">
        <el-table-column prop="id" label="ID" width="70" align="center" />
        <el-table-column label="评论用户" width="140">
          <template #default="{ row }">
            <div class="author-cell">
              <el-avatar :size="24" :src="row.userAvatar" class="author-avatar" />
              <span>{{ row.userNickname || '未知' }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="内容" min-width="300">
          <template #default="{ row }">
            <span class="content-cell" :class="{ expanded: expandedComments[row.id] }" @click="toggleExpand('comments', row.id)">
              {{ expandedComments[row.id] ? row.content : (row.content?.substring(0, 80) + (row.content?.length > 80 ? '...' : '')) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" fixed="right" width="120" align="center">
          <template #default="{ row }">
            <el-button size="small" class="btn-delete" @click="handleDeleteComment(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 申诉管理 -->
    <div v-if="activeTab === 'appeals'" class="tab-content">
      <el-table :data="appeals" style="width: 100%" v-loading="appealLoading">
        <el-table-column prop="id" label="ID" width="70" align="center" />
        <el-table-column label="申诉人" width="140">
          <template #default="{ row }">
            <div class="author-cell">
              <el-avatar :size="24" :src="row.userAvatar" class="author-avatar" />
              <span>{{ row.userNickname || '未知' }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="帖子标题" width="180">
          <template #default="{ row }">
            <span class="title-cell" :title="row.postTitle">{{ row.postTitle || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="帖子内容" min-width="200">
          <template #default="{ row }">
            <span class="content-cell" :class="{ expanded: expandedAppeals[row.id] }" @click="toggleExpand('appeals', row.id)">
              {{ expandedAppeals[row.id] ? row.postContent : (row.postContent?.substring(0, 50) + (row.postContent?.length > 50 ? '...' : '')) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="申诉原因" min-width="200">
          <template #default="{ row }">
            <span class="content-cell" :class="{ expanded: expandedAppeals[row.id] }" @click="toggleExpand('appeals', row.id)">
              {{ expandedAppeals[row.id] ? row.reason : (row.reason?.substring(0, 50) + (row.reason?.length > 50 ? '...' : '')) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="appealTime" label="申诉时间" width="180" />
        <el-table-column label="操作" fixed="right" width="180" align="center">
          <template #default="{ row }">
            <el-button size="small" type="success" @click="handleReviewAppeal(row, 1)">通过</el-button>
            <el-button size="small" type="danger" @click="handleReviewAppeal(row, 2)">不通过</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { getPostList, deletePost, getCommentList, deleteComment, getAppealList, reviewAppeal, getAppealCount } from '@/api/post'

const loading = ref(false)
const commentLoading = ref(false)
const appealLoading = ref(false)
const activeTab = ref('posts')
const searchKeyword = ref('')
const commentSearchKeyword = ref('')
const posts = ref([])
const comments = ref([])
const appeals = ref([])
const appealCount = ref(0)
const expandedPosts = ref({})
const expandedComments = ref({})
const expandedAppeals = ref({})

const filteredComments = computed(() => {
  let result = comments.value
  if (commentSearchKeyword.value) {
    const keyword = commentSearchKeyword.value.toLowerCase()
    result = result.filter(c => c.content && c.content.toLowerCase().includes(keyword))
  }
  return result
})

const getEmotionColor = (emotion) => {
  const colors = {
    happy: '#C5F082',
    sad: '#F5A623',
    angry: '#FF6B6B',
    calm: '#87CEEB',
    anxious: '#DDA0DD'
  }
  return colors[emotion] || '#E5E7EB'
}

const loadPosts = async () => {
  loading.value = true
  try {
    const res = await getPostList()
    posts.value = res.data || []
  } catch (err) {
    console.error('加载帖子列表失败', err)
  } finally {
    loading.value = false
  }
}

const loadComments = async () => {
  commentLoading.value = true
  try {
    const res = await getCommentList()
    comments.value = res.data || []
  } catch (err) {
    console.error('加载评论列表失败', err)
  } finally {
    commentLoading.value = false
  }
}

const loadAppeals = async () => {
  appealLoading.value = true
  try {
    const res = await getAppealList()
    appeals.value = res.data || []
  } catch (err) {
    console.error('加载申诉列表失败', err)
  } finally {
    appealLoading.value = false
  }
}

const loadAppealCount = async () => {
  try {
    const res = await getAppealCount()
    appealCount.value = res.data || 0
  } catch (err) {
    console.error('加载申诉数量失败', err)
  }
}

const toggleExpand = (type, id) => {
  if (type === 'posts') {
    expandedPosts.value = { ...expandedPosts.value, [id]: !expandedPosts.value[id] }
  } else if (type === 'comments') {
    expandedComments.value = { ...expandedComments.value, [id]: !expandedComments.value[id] }
  } else if (type === 'appeals') {
    expandedAppeals.value = { ...expandedAppeals.value, [id]: !expandedAppeals.value[id] }
  }
}

watch(activeTab, (newTab) => {
  if (newTab === 'comments' && comments.value.length === 0) {
    loadComments()
  }
  if (newTab === 'appeals') {
    loadAppeals()
  }
})

const handleDeletePost = async (post) => {
  try {
    await ElMessageBox.confirm('确定要删除该帖子吗？', '确认删除', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deletePost(post.id)
    ElMessage.success('删除成功')
    loadPosts()
  } catch (err) {
    if (err !== 'cancel') console.error('删除失败', err)
  }
}

const handleDeleteComment = async (comment) => {
  try {
    await ElMessageBox.confirm('确定要删除该评论吗？', '确认删除', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteComment(comment.id)
    ElMessage.success('删除成功')
    loadComments()
  } catch (err) {
    if (err !== 'cancel') console.error('删除失败', err)
  }
}

const handleReviewAppeal = async (appeal, status) => {
  const actionText = status === 1 ? '通过' : '不通过'
  try {
    await ElMessageBox.confirm(`确定要${actionText}该申诉吗？`, `确认${actionText}`, {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await reviewAppeal({ id: appeal.id, status, reviewerId: 1 })
    ElMessage.success(`${actionText}成功`)
    loadAppeals()
    loadAppealCount()
  } catch (err) {
    if (err !== 'cancel') console.error('审核失败', err)
  }
}

onMounted(() => {
  loadPosts()
  loadAppealCount()
  // 定时刷新申诉数量
  setInterval(() => {
    loadAppealCount()
  }, 30000)
})
</script>

<style lang="scss" scoped>
.page-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.content-tabs {
  :deep(.el-tabs__header) {
    margin-bottom: 16px;
  }
  
  :deep(.el-tabs__item) {
    color: var(--text-secondary);
    
    &.is-active {
      color: var(--color-primary);
    }
    
    &:hover {
      color: var(--color-primary);
    }
  }
  
  :deep(.el-tabs__active-bar) {
    background-color: var(--color-primary);
  }
}

.appeal-badge {
  margin-left: 8px;
  :deep(.el-badge__content) {
    background-color: #FF6B6B;
  }
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.filter-bar {
  display: flex;
  gap: 12px;
  align-items: center;
}

.author-cell {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
}

.author-avatar {
  background-color: var(--bg-base);
  border: 1px solid var(--border-base);
}

.content-cell {
  font-size: 13px;
  color: var(--text-regular);
  cursor: pointer;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.5;
  transition: all 0.2s ease;
  padding: 4px 0;

  &:hover {
    color: var(--color-primary);
  }

  &.expanded {
    -webkit-line-clamp: unset;
    white-space: pre-wrap;
    word-break: break-all;
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
</style>
