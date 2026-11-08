<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h1 class="login-title">情绪小站</h1>
        <p class="login-subtitle">管理后台</p>
      </div>
      
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        class="login-form"
        @submit.prevent="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
            v-model="form.username"
            placeholder="请输入管理员账号"
            size="large"
            prefix-icon="User"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            prefix-icon="Lock"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="login-btn"
            :loading="loading"
            @click="handleLogin"
          >
            登 录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api/admin'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入管理员账号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  
  loading.value = true
  try {
    const res = await login(form)
    localStorage.setItem('adminToken', res.data.token)
    localStorage.setItem('adminInfo', JSON.stringify({
      id: res.data.adminId,
      nickname: res.data.nickname
    }))
    ElMessage.success('登录成功')
    router.push('/')
  } catch (err) {
    console.error('登录失败', err)
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--bg-base);
}

.login-card {
  width: 400px;
  padding: 48px 40px;
  background: var(--bg-card);
  border: 1px solid var(--border-base);
  border-radius: var(--radius-base);
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
}

.login-title {
  font-family: var(--font-serif);
  font-size: 28px;
  font-weight: 500;
  color: var(--text-primary);
  margin-bottom: 8px;
}

.login-subtitle {
  font-size: 14px;
  color: var(--text-secondary);
  letter-spacing: 2px;
}

.login-form {
  .el-form-item {
    margin-bottom: 24px;
  }
}

.login-btn {
  width: 100%;
  height: var(--btn-height);
  background-color: var(--color-primary);
  border-color: var(--color-primary);
  border-radius: var(--radius-base);
  font-size: 15px;
  letter-spacing: 4px;
  
  &:hover, &:focus {
    background-color: var(--color-primary-light);
    border-color: var(--color-primary-light);
  }
}

:deep(.el-input__wrapper) {
  border-radius: var(--radius-base);
  box-shadow: none;
  border: 1px solid var(--border-input);
  
  &.is-focus {
    border-color: var(--color-primary);
  }
}
</style>
