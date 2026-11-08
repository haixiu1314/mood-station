<template>
  <div class="page-container">
    <div class="page-header">
      <div class="filter-bar">
        <el-select v-model="statusFilter" placeholder="状态筛选" style="width: 140px" clearable>
          <el-option label="启用" :value="1" />
          <el-option label="禁用" :value="0" />
        </el-select>
        
        <el-button type="primary" @click="showAddDialog">
          <el-icon><Plus /></el-icon>
          添加轮播图
        </el-button>
      </div>
    </div>
    
    <el-table :data="filteredBanners" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" align="center" />
      <el-table-column label="图片" width="160" align="center">
        <template #default="{ row }">
          <el-image :src="row.imageUrl" style="width: 120px; height: 60px; border-radius: 4px" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column prop="title" label="标题" width="180" />
      <el-table-column prop="linkUrl" label="跳转链接" min-width="200" show-overflow-tooltip />
      <el-table-column label="排序" width="80" align="center">
        <template #default="{ row }">
          {{ row.sortOrder || 0 }}
        </template>
      </el-table-column>
      <el-table-column label="状态" width="90" align="center">
        <template #default="{ row }">
          <el-tag :type="row.isActive === 1 ? 'success' : 'info'" size="small">
            {{ row.isActive === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" fixed="right" width="220" align="center">
        <template #default="{ row }">
          <div class="action-buttons">
            <el-button size="small" class="btn-edit" @click="showEditDialog(row)">编辑</el-button>
            <el-button
              size="small"
              :class="row.isActive === 1 ? 'btn-ban' : 'btn-unban'"
              @click="toggleStatus(row)"
            >
              {{ row.isActive === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button size="small" class="btn-delete" @click="handleDelete(row)">删除</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    
    <el-dialog v-model="bannerDialogVisible" :title="isEdit ? '编辑轮播图' : '添加轮播图'" width="500px">
      <el-form :model="bannerForm" label-width="100px">
        <el-form-item label="标题">
          <el-input v-model="bannerForm.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="轮播图片">
          <el-upload
            class="banner-uploader"
            action="#"
            :http-request="handleUploadBanner"
            :show-file-list="false"
            accept="image/*"
          >
            <img v-if="bannerForm.imageUrl" :src="bannerForm.imageUrl" class="banner-preview" />
            <el-icon v-else class="banner-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="跳转链接">
          <el-input v-model="bannerForm.linkUrl" placeholder="请输入跳转链接（可选）" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="bannerForm.sortOrder" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="bannerDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveBanner">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getBannerList, addBanner, updateBanner, updateBannerStatus, deleteBanner, uploadBannerImage } from '@/api/banner'

const loading = ref(false)
const banners = ref([])
const statusFilter = ref('')
const bannerDialogVisible = ref(false)
const isEdit = ref(false)
const uploading = ref(false)

const bannerForm = ref({
  id: null,
  title: '',
  imageUrl: '',
  linkUrl: '',
  sortOrder: 0
})

const filteredBanners = computed(() => {
  let result = banners.value
  if (statusFilter.value !== '') {
    result = result.filter(b => b.isActive === statusFilter.value)
  }
  return result
})

const loadBanners = async () => {
  loading.value = true
  try {
    const res = await getBannerList()
    banners.value = res.data || []
  } catch (err) {
    console.error('加载轮播图列表失败', err)
  } finally {
    loading.value = false
  }
}

const showAddDialog = () => {
  isEdit.value = false
  bannerForm.value = { id: null, title: '', imageUrl: '', linkUrl: '', sortOrder: 0 }
  bannerDialogVisible.value = true
}

const showEditDialog = (banner) => {
  isEdit.value = true
  bannerForm.value = { ...banner }
  bannerDialogVisible.value = true
}

const handleUploadBanner = async (options) => {
  uploading.value = true
  try {
    const res = await uploadBannerImage(options.file)
    bannerForm.value.imageUrl = res.data
    ElMessage.success('图片上传成功')
  } catch (err) {
    ElMessage.error('图片上传失败')
    console.error('上传失败', err)
  } finally {
    uploading.value = false
  }
}

const handleSaveBanner = async () => {
  if (!bannerForm.value.imageUrl) {
    ElMessage.warning('请先上传轮播图片')
    return
  }
  try {
    if (isEdit.value) {
      await updateBanner(bannerForm.value)
      ElMessage.success('更新成功')
    } else {
      await addBanner(bannerForm.value)
      ElMessage.success('添加成功')
    }
    bannerDialogVisible.value = false
    loadBanners()
  } catch (err) {
    console.error('保存失败', err)
  }
}

const toggleStatus = async (banner) => {
  const newStatus = banner.isActive === 1 ? 0 : 1
  try {
    await updateBannerStatus(banner.id, newStatus)
    ElMessage.success(banner.isActive === 1 ? '禁用成功' : '启用成功')
    loadBanners()
  } catch (err) {
    console.error('操作失败', err)
  }
}

const handleDelete = async (banner) => {
  try {
    await ElMessageBox.confirm(`确定要删除轮播图「${banner.title}」吗？`, '确认删除', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteBanner(banner.id)
    ElMessage.success('删除成功')
    loadBanners()
  } catch (err) {
    if (err !== 'cancel') console.error('删除失败', err)
  }
}

onMounted(() => {
  loadBanners()
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

.action-buttons {
  display: flex;
  gap: 6px;
  justify-content: center;
  flex-wrap: nowrap;
}

.btn-edit {
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

.banner-uploader {
  :deep(.el-upload) {
    border: 1px dashed var(--border-input);
    border-radius: 8px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
    
    &:hover {
      border-color: var(--color-primary);
    }
  }
}

.banner-uploader-icon {
  font-size: 28px;
  color: var(--text-placeholder);
  width: 178px;
  height: 178px;
  text-align: center;
  line-height: 178px;
}

.banner-preview {
  width: 178px;
  height: 178px;
  display: block;
  object-fit: cover;
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
