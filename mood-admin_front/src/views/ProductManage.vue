<template>
  <div class="page-container">
    <div class="page-header">
      <div class="filter-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索商品名称"
          style="width: 240px"
          clearable
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        
        <el-select v-model="statusFilter" placeholder="状态筛选" style="width: 140px" clearable>
          <el-option label="上架" :value="1" />
          <el-option label="下架" :value="0" />
        </el-select>
        
        <el-button type="primary" @click="showAddDialog">
          <el-icon><Plus /></el-icon>
          添加商品
        </el-button>
      </div>
    </div>
    
    <el-table :data="filteredProducts" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column label="封面" width="100">
        <template #default="{ row }">
          <el-image :src="row.coverImage" style="width: 60px; height: 60px; border-radius: 4px" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="商品名称" min-width="180" />
      <el-table-column label="价格" width="120">
        <template #default="{ row }">
          <span class="price-cell">{{ row.price }} 心动币</span>
        </template>
      </el-table-column>
      <el-table-column label="库存" width="100">
        <template #default="{ row }">
          <el-tag :type="row.stock > 0 ? 'success' : 'danger'" size="small">
            {{ row.stock > 0 ? row.stock : '售罄' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
            {{ row.status === 1 ? '上架' : '下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" fixed="right" width="320" align="center">
        <template #default="{ row }">
          <div class="action-buttons">
            <el-button size="small" class="btn-edit" @click="showEditDialog(row)">编辑</el-button>
            <el-button size="small" class="btn-restock" @click="showRestockDialog(row)">补货</el-button>
            <el-button
              size="small"
              :class="row.status === 1 ? 'btn-offshelf' : 'btn-onshelf'"
              @click="toggleStatus(row)"
            >
              {{ row.status === 1 ? '下架' : '上架' }}
            </el-button>
            <el-button size="small" class="btn-delete" @click="handleDelete(row)">删除</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    
    <el-dialog v-model="productDialogVisible" :title="isEdit ? '编辑商品' : '添加商品'" width="650px">
      <el-form :model="productForm" label-width="100px">
        <el-form-item label="商品名称">
          <el-input v-model="productForm.name" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="封面图片">
          <el-upload
            class="cover-uploader"
            :action="uploadCoverUrl"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleCoverSuccess"
            :before-upload="beforeCoverUpload"
          >
            <img v-if="productForm.coverImage" :src="productForm.coverImage" class="cover-image" />
            <el-icon v-else class="cover-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="详情图片">
          <el-upload
            v-model:file-list="detailFileList"
            :action="uploadImagesUrl"
            :headers="uploadHeaders"
            list-type="picture-card"
            :limit="4"
            :on-success="handleDetailSuccess"
            :on-remove="handleDetailRemove"
            :before-upload="beforeDetailUpload"
            name="files"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
          <div class="form-tip">最多上传4张，单张不超过10MB</div>
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="productForm.price" :min="0" :precision="2" />
          <span class="form-tip">心动币</span>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="productForm.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="标签">
          <el-input v-model="productForm.tags" placeholder="多个标签用逗号分隔" />
        </el-form-item>
        <el-form-item label="规格">
          <div class="specs-editor">
            <el-tag
              v-for="(spec, index) in specTags"
              :key="index"
              closable
              @close="removeSpec(index)"
              class="spec-tag"
            >
              {{ spec }}
            </el-tag>
            <el-input
              v-if="inputSpecVisible"
              v-model="inputSpecValue"
              class="spec-input"
              size="small"
              @keyup.enter="addSpec"
              @blur="addSpec"
              placeholder="输入规格后回车"
            />
            <el-button v-else class="button-new-tag" size="small" @click="showSpecInput">
              + 添加规格
            </el-button>
          </div>
          <div class="form-tip">点击"添加规格"可自定义多个规格标签，如：红色、白色、圆形等</div>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="productForm.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number v-model="productForm.stock" :min="0" :max="99999" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="productDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveProduct">保存</el-button>
      </template>
    </el-dialog>
    
    <el-dialog v-model="restockDialogVisible" title="商品补货" width="400px">
      <el-form :model="restockForm" label-width="80px">
        <el-form-item label="商品">
          <span>{{ restockForm.product?.name }}</span>
        </el-form-item>
        <el-form-item label="当前库存">
          <span>{{ restockForm.product?.stock || 0 }}</span>
        </el-form-item>
        <el-form-item label="补货数量">
          <el-input-number v-model="restockForm.amount" :min="1" :max="10000" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="restockDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleRestock">确认补货</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus } from '@element-plus/icons-vue'
import { getProductList, addProduct, updateProduct, updateProductStatus, restockProduct, deleteProduct } from '@/api/product'

const loading = ref(false)
const products = ref([])
const searchKeyword = ref('')
const statusFilter = ref('')
const productDialogVisible = ref(false)
const restockDialogVisible = ref(false)
const isEdit = ref(false)

const productForm = ref({
  id: null,
  name: '',
  coverImage: '',
  images: '',
  price: 0,
  description: '',
  tags: '',
  specs: '',
  stock: 0,
  sortOrder: 0
})

const specTags = ref([])
const inputSpecVisible = ref(false)
const inputSpecValue = ref('')

const detailFileList = ref([])
const uploadCoverUrl = ref('/api/admin/product/uploadCover')
const uploadImagesUrl = ref('/api/admin/product/uploadImages')
const uploadHeaders = computed(() => ({
  Authorization: `Bearer ${localStorage.getItem('adminToken')}`
}))

const restockForm = ref({ product: null, amount: 100 })

const filteredProducts = computed(() => {
  let result = products.value
  if (statusFilter.value !== '') {
    result = result.filter(p => p.status === statusFilter.value)
  }
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(p => p.name && p.name.toLowerCase().includes(keyword))
  }
  return result
})

const loadProducts = async () => {
  loading.value = true
  try {
    const res = await getProductList()
    products.value = res.data || []
  } catch (err) {
    console.error('加载商品列表失败', err)
  } finally {
    loading.value = false
  }
}

const showAddDialog = () => {
  isEdit.value = false
  productForm.value = {
    id: null, name: '', coverImage: '', images: '',
    price: 0, description: '', tags: '', specs: '', stock: 0, sortOrder: 0
  }
  specTags.value = []
  detailFileList.value = []
  productDialogVisible.value = true
}

const showEditDialog = (product) => {
  isEdit.value = true
  productForm.value = { ...product }
  // 初始化规格标签
  specTags.value = product.specs ? product.specs.split(',').filter(Boolean) : []
  // 初始化详情图片文件列表
  detailFileList.value = []
  if (product.images) {
    const urls = product.images.split(',').filter(Boolean)
    detailFileList.value = urls.map((url, index) => ({
      name: `image${index}`,
      url: url
    }))
  }
  productDialogVisible.value = true
}

const showSpecInput = () => {
  inputSpecVisible.value = true
  inputSpecValue.value = ''
}

const addSpec = () => {
  const value = inputSpecValue.value.trim()
  if (value && !specTags.value.includes(value)) {
    specTags.value.push(value)
  }
  inputSpecVisible.value = false
  inputSpecValue.value = ''
}

const removeSpec = (index) => {
  specTags.value.splice(index, 1)
}

const showRestockDialog = (product) => {
  restockForm.value = { product, amount: 100 }
  restockDialogVisible.value = true
}

// 封面图片上传
const beforeCoverUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    ElMessage.error('只能上传图片文件')
    return false
  }
  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isLt10M) {
    ElMessage.error('图片大小不能超过10MB')
    return false
  }
  return true
}

const handleCoverSuccess = (res) => {
  if (res.code === 200) {
    productForm.value.coverImage = res.data
    ElMessage.success('封面上传成功')
  }
}

// 详情图片上传
const beforeDetailUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    ElMessage.error('只能上传图片文件')
    return false
  }
  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isLt10M) {
    ElMessage.error('图片大小不能超过10MB')
    return false
  }
  return true
}

const handleDetailSuccess = (res, file, fileList) => {
  if (res.code === 200) {
    // 从 fileList 中提取所有图片URL
    const urls = fileList.map(f => {
      // 新上传的文件，URL在 response 中
      if (f.response && f.response.data) {
        return Array.isArray(f.response.data) ? f.response.data[0] : f.response.data
      }
      // 已有的文件（编辑时加载的），URL在 url 字段
      return f.url
    }).filter(Boolean)
    productForm.value.images = urls.join(',')
  }
}

const handleDetailRemove = (file, fileList) => {
  const urls = fileList.map(f => {
    if (f.response && f.response.data) {
      return Array.isArray(f.response.data) ? f.response.data[0] : f.response.data
    }
    return f.url
  }).filter(Boolean)
  productForm.value.images = urls.join(',')
}

const handleSaveProduct = async () => {
  try {
    // 将规格标签数组转换为逗号分隔的字符串
    productForm.value.specs = specTags.value.join(',')
    
    if (isEdit.value) {
      await updateProduct(productForm.value)
      ElMessage.success('更新成功')
    } else {
      await addProduct(productForm.value)
      ElMessage.success('添加成功')
    }
    productDialogVisible.value = false
    loadProducts()
  } catch (err) {
    console.error('保存失败', err)
  }
}

const handleRestock = async () => {
  try {
    await restockProduct({ productId: restockForm.value.product.id, quantity: restockForm.value.amount })
    ElMessage.success('补货成功')
    restockDialogVisible.value = false
    loadProducts()
  } catch (err) {
    console.error('补货失败', err)
  }
}

const toggleStatus = async (product) => {
  const newStatus = product.status === 1 ? 0 : 1
  try {
    await updateProductStatus(product.id, newStatus)
    ElMessage.success(product.status === 1 ? '下架成功' : '上架成功')
    loadProducts()
  } catch (err) {
    console.error('操作失败', err)
  }
}

const handleDelete = async (product) => {
  try {
    await ElMessageBox.confirm(`确定要删除商品「${product.name}」吗？`, '确认删除', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteProduct(product.id)
    ElMessage.success('删除成功')
    loadProducts()
  } catch (err) {
    if (err !== 'cancel') console.error('删除失败', err)
  }
}

onMounted(() => {
  loadProducts()
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

.price-cell {
  color: var(--color-primary);
  font-weight: 500;
}

.form-tip {
  margin-left: 8px;
  font-size: 12px;
  color: var(--text-secondary);
}

.specs-editor {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  align-items: center;
  width: 100%;
}

.spec-tag {
  margin: 0;
}

.spec-input {
  width: 120px;
  margin-right: 8px;
}

.button-new-tag {
  height: 24px;
  line-height: 22px;
  padding: 0 8px;
  border-style: dashed;
}

.action-buttons {
  display: flex;
  gap: 4px;
  justify-content: center;
  flex-wrap: nowrap;
}

.cover-uploader {
  border: 1px dashed var(--el-border-color);
  border-radius: 8px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.cover-uploader:hover {
  border-color: var(--el-color-primary);
}

.cover-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
  line-height: 178px;
}

.cover-image {
  width: 178px;
  height: 178px;
  display: block;
  object-fit: cover;
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

.btn-restock {
  background-color: #E6A23C;
  border-color: #E6A23C;
  color: #fff;
  
  &:hover {
    background-color: #EBB45A;
    border-color: #EBB45A;
  }
}

.btn-onshelf {
  background-color: var(--color-success);
  border-color: var(--color-success);
  color: #fff;
  
  &:hover {
    background-color: #8BA88E;
    border-color: #8BA88E;
  }
}

.btn-offshelf {
  background-color: var(--color-danger);
  border-color: var(--color-danger);
  color: #fff;
  
  &:hover {
    background-color: #E27A7A;
    border-color: #E27A7A;
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
