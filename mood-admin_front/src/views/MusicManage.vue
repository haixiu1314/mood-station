<template>
  <div class="page-container">
    <div class="page-header">
      <div class="filter-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索歌曲名或歌手"
          style="width: 240px"
          clearable
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        
        <el-select v-model="typeFilter" placeholder="类型筛选" style="width: 140px" clearable>
          <el-option label="纯音乐" value="pure" />
          <el-option label="流行音乐" value="pop" />
        </el-select>
        
        <el-button type="primary" @click="showAddDialog">
          <el-icon><Plus /></el-icon>
          添加音乐
        </el-button>
      </div>
    </div>
    
    <el-table :data="filteredMusic" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="歌曲名" min-width="150" />
      <el-table-column prop="singer" label="歌手" width="120" />
      <el-table-column label="类型" width="100">
        <template #default="{ row }">
          <el-tag :type="row.type === 'pure' ? 'success' : 'primary'" size="small">
            {{ row.type === 'pure' ? '纯音乐' : '流行音乐' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" fixed="right" width="200" align="center">
        <template #default="{ row }">
          <div class="action-buttons">
            <el-button size="small" class="btn-edit" @click="showEditDialog(row)">编辑</el-button>
            <el-button size="small" class="btn-delete" @click="handleDelete(row)">删除</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    
    <el-dialog v-model="musicDialogVisible" :title="isEdit ? '编辑音乐' : '添加音乐'" width="500px">
      <el-form :model="musicForm" label-width="80px">
        <el-form-item label="歌曲名">
          <el-input v-model="musicForm.name" placeholder="请输入歌曲名" />
        </el-form-item>
        <el-form-item label="歌手">
          <el-input v-model="musicForm.singer" placeholder="请输入歌手名" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="musicForm.type" placeholder="请选择类型">
            <el-option label="纯音乐" value="pure" />
            <el-option label="流行音乐" value="pop" />
          </el-select>
        </el-form-item>
        <el-form-item label="音乐文件">
          <el-upload
            class="music-uploader"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload"
          >
            <el-icon v-if="!musicForm.fileUrl" class="music-uploader-icon"><Upload /></el-icon>
            <template v-else>
              <div class="music-file-info">
                <el-icon><Headset /></el-icon>
                <span>{{ getFileName(musicForm.fileUrl) }}</span>
              </div>
            </template>
          </el-upload>
          <div class="form-tip">支持 MP3、WAV、OGG、AAC 等音频格式，文件不超过20MB</div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="musicDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveMusic">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Upload, Headset } from '@element-plus/icons-vue'
import { getMusicList, addMusic, updateMusic, deleteMusic } from '@/api/music'

const loading = ref(false)
const musicList = ref([])
const searchKeyword = ref('')
const typeFilter = ref('')
const musicDialogVisible = ref(false)
const isEdit = ref(false)

const musicForm = ref({
  id: null,
  name: '',
  singer: '',
  type: '',
  fileUrl: ''
})

const uploadUrl = ref('/api/admin/music/upload')
const uploadHeaders = computed(() => ({
  Authorization: `Bearer ${localStorage.getItem('adminToken')}`
}))

const filteredMusic = computed(() => {
  let result = musicList.value
  if (typeFilter.value !== '') {
    result = result.filter(m => m.type === typeFilter.value)
  }
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(m => 
      (m.name && m.name.toLowerCase().includes(keyword)) ||
      (m.singer && m.singer.toLowerCase().includes(keyword))
    )
  }
  return result
})

const getFileName = (url) => {
  if (!url) return ''
  const parts = url.split('/')
  return parts[parts.length - 1] || ''
}

const loadMusic = async () => {
  loading.value = true
  try {
    const res = await getMusicList()
    musicList.value = res.data || []
  } catch (err) {
    console.error('加载音乐列表失败', err)
  } finally {
    loading.value = false
  }
}

const showAddDialog = () => {
  isEdit.value = false
  musicForm.value = {
    id: null,
    name: '',
    singer: '',
    type: '',
    fileUrl: ''
  }
  musicDialogVisible.value = true
}

const showEditDialog = (music) => {
  isEdit.value = true
  musicForm.value = { ...music }
  musicDialogVisible.value = true
}

const beforeUpload = (file) => {
  const validTypes = ['audio/mpeg', 'audio/wav', 'audio/ogg', 'audio/mp3', 'audio/x-mpeg', 'audio/aac']
  const isValidType = validTypes.includes(file.type) || file.name.match(/\.(mp3|wav|ogg|aac)$/i)
  if (!isValidType) {
    ElMessage.error('只能上传音频文件（MP3、WAV、OGG、AAC）')
    return false
  }
  const isLt20M = file.size / 1024 / 1024 < 20
  if (!isLt20M) {
    ElMessage.error('文件大小不能超过20MB')
    return false
  }
  return true
}

const handleUploadSuccess = (res) => {
  if (res.code === 200) {
    musicForm.value.fileUrl = res.data
    ElMessage.success('文件上传成功')
  }
}

const handleSaveMusic = async () => {
  if (!musicForm.value.name) {
    ElMessage.warning('请输入歌曲名')
    return
  }
  if (!musicForm.value.singer) {
    ElMessage.warning('请输入歌手名')
    return
  }
  if (!musicForm.value.type) {
    ElMessage.warning('请选择音乐类型')
    return
  }
  if (!musicForm.value.fileUrl) {
    ElMessage.warning('请上传音乐文件')
    return
  }
  
  try {
    if (isEdit.value) {
      await updateMusic(musicForm.value)
      ElMessage.success('更新成功')
    } else {
      await addMusic(musicForm.value)
      ElMessage.success('添加成功')
    }
    musicDialogVisible.value = false
    loadMusic()
  } catch (err) {
    console.error('保存失败', err)
  }
}

const handleDelete = async (music) => {
  try {
    await ElMessageBox.confirm(`确定要删除歌曲「${music.name}」吗？`, '确认删除', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteMusic(music.id)
    ElMessage.success('删除成功')
    loadMusic()
  } catch (err) {
    if (err !== 'cancel') console.error('删除失败', err)
  }
}

onMounted(() => {
  loadMusic()
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

.form-tip {
  margin-top: 8px;
  font-size: 12px;
  color: var(--text-secondary);
}

.action-buttons {
  display: flex;
  gap: 4px;
  justify-content: center;
}

.music-uploader {
  border: 1px dashed var(--el-border-color);
  border-radius: 8px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
  width: 200px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.music-uploader:hover {
  border-color: var(--el-color-primary);
}

.music-uploader-icon {
  font-size: 28px;
  color: #8c939d;
}

.music-file-info {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background-color: var(--bg-base);
  border-radius: 4px;
  
  span {
    font-size: 13px;
    color: var(--text-regular);
    max-width: 150px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
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