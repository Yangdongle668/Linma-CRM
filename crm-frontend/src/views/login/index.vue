<template>
  <div class="login-container">
    <!-- 左侧品牌展示区 -->
    <div class="login-left">
      <div class="brand-wrapper">
        <!-- 动态背景装饰 -->
        <div class="decoration">
          <div class="circle circle-1"></div>
          <div class="circle circle-2"></div>
          <div class="circle circle-3"></div>
        </div>
        
        <!-- 品牌信息 -->
        <div class="brand">
          <div class="logo">
            <el-icon :size="60"><Connection /></el-icon>
          </div>
          <h1 class="brand-title">外贸CRM系统</h1>
          <p class="brand-subtitle">Foreign Trade CRM System</p>
          <p class="brand-slogan">智能管理 · 高效获客 · 精准营销</p>
        </div>
      </div>
    </div>

    <!-- 右侧登录表单区 -->
    <div class="login-right">
      <div class="login-form-wrapper">
        <div class="login-header">
          <h2>欢迎登录</h2>
          <p>请输入您的账号信息</p>
        </div>

        <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          class="login-form"
          @keyup.enter="handleLogin"
        >
          <!-- 用户名 -->
          <el-form-item prop="username">
            <el-input
              v-model="loginForm.username"
              placeholder="请输入用户名"
              size="large"
              prefix-icon="User"
              clearable
            />
          </el-form-item>

          <!-- 密码 -->
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              size="large"
              prefix-icon="Lock"
              show-password
            />
          </el-form-item>

          <!-- 记住我 & 忘记密码 -->
          <div class="form-options">
            <el-checkbox v-model="loginForm.remember">记住我</el-checkbox>
            <el-link type="primary" :underline="false">忘记密码?</el-link>
          </div>

          <!-- 登录按钮 -->
          <el-form-item>
            <el-button
              type="primary"
              size="large"
              :loading="loading"
              class="login-button"
              @click="handleLogin"
            >
              {{ loading ? '登录中...' : '登 录' }}
            </el-button>
          </el-form-item>
        </el-form>

        <!-- 其他登录方式 -->
        <div class="other-login">
          <el-divider>其他登录方式</el-divider>
          <div class="social-login">
            <el-button circle icon="Message" />
            <el-button circle icon="ChatDotRound" />
            <el-button circle icon="Phone" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// 表单引用
const loginFormRef = ref<FormInstance>()

// 加载状态
const loading = ref(false)

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: '',
  remember: false,
})

// 表单验证规则
const loginRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在3-20个字符', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6-20个字符', trigger: 'blur' },
  ],
}

/**
 * 处理登录
 */
async function handleLogin() {
  // 验证表单
  if (!loginFormRef.value) return
  
  try {
    await loginFormRef.value.validate()
    
    // 设置加载状态
    loading.value = true
    
    // 调用登录API
    await userStore.login({
      username: loginForm.username,
      password: loginForm.password,
    })
    
    // 如果勾选了"记住我",保存用户名到localStorage
    if (loginForm.remember) {
      localStorage.setItem('remembered_username', loginForm.username)
    } else {
      localStorage.removeItem('remembered_username')
    }
    
    // 登录成功提示
    ElMessage.success('登录成功')
    
    // 跳转到首页或重定向页面
    const redirect = route.query.redirect as string
    router.push(redirect || '/')
  } catch (error) {
    console.error('登录失败:', error)
    ElMessage.error('登录失败,请检查用户名和密码')
  } finally {
    loading.value = false
  }
}

// 组件挂载时,如果有记住的用户名,自动填充
if (localStorage.getItem('remembered_username')) {
  loginForm.username = localStorage.getItem('remembered_username') || ''
  loginForm.remember = true
}
</script>

<style scoped lang="scss">
.login-container {
  display: flex;
  width: 100%;
  height: 100vh;
  overflow: hidden;
}

/* ==================== 左侧品牌区 ==================== */
.login-left {
  position: relative;
  flex: 1;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  
  @media (max-width: 768px) {
    display: none;
  }
}

.brand-wrapper {
  position: relative;
  z-index: 1;
  text-align: center;
  color: white;
}

// 动态装饰背景
.decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  
  .circle {
    position: absolute;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.1);
    animation: float 6s ease-in-out infinite;
    
    &.circle-1 {
      width: 300px;
      height: 300px;
      top: -100px;
      left: -100px;
      animation-delay: 0s;
    }
    
    &.circle-2 {
      width: 200px;
      height: 200px;
      bottom: -50px;
      right: -50px;
      animation-delay: 2s;
    }
    
    &.circle-3 {
      width: 150px;
      height: 150px;
      top: 50%;
      right: 10%;
      animation-delay: 4s;
    }
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0) scale(1);
  }
  50% {
    transform: translateY(-20px) scale(1.05);
  }
}

// 品牌信息
.brand {
  .logo {
    margin-bottom: 30px;
    animation: fadeInDown 0.8s ease-out;
  }
  
  .brand-title {
    font-size: 48px;
    font-weight: bold;
    margin: 0 0 15px;
    letter-spacing: 4px;
    animation: fadeInUp 0.8s ease-out 0.2s both;
  }
  
  .brand-subtitle {
    font-size: 18px;
    opacity: 0.9;
    margin: 0 0 20px;
    letter-spacing: 2px;
    animation: fadeInUp 0.8s ease-out 0.4s both;
  }
  
  .brand-slogan {
    font-size: 16px;
    opacity: 0.8;
    margin: 0;
    animation: fadeInUp 0.8s ease-out 0.6s both;
  }
}

@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* ==================== 右侧表单区 ==================== */
.login-right {
  width: 500px;
  background: white;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  
  @media (max-width: 768px) {
    width: 100%;
    padding: 20px;
  }
}

.login-form-wrapper {
  width: 100%;
  max-width: 380px;
  animation: fadeInRight 0.6s ease-out;
}

@keyframes fadeInRight {
  from {
    opacity: 0;
    transform: translateX(30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.login-header {
  margin-bottom: 40px;
  text-align: center;
  
  h2 {
    font-size: 28px;
    color: #1F2937;
    margin: 0 0 10px;
  }
  
  p {
    font-size: 14px;
    color: #6B7280;
    margin: 0;
  }
}

.login-form {
  :deep(.el-input__wrapper) {
    padding: 12px 15px;
    transition: all 0.3s ease;
    
    &:hover {
      box-shadow: 0 0 0 1px #667eea inset;
    }
  }
  
  :deep(.el-input__wrapper.is-focus) {
    box-shadow: 0 0 0 1px #667eea inset;
  }
  
  .form-options {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }
  
  .login-button {
    width: 100%;
    height: 48px;
    font-size: 16px;
    font-weight: 500;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
    transition: all 0.3s ease;
    
    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 16px rgba(102, 126, 234, 0.4);
    }
    
    &:active {
      transform: translateY(0);
    }
  }
}

.other-login {
  margin-top: 30px;
  
  :deep(.el-divider__text) {
    color: #9CA3AF;
    font-size: 13px;
  }
  
  .social-login {
    display: flex;
    justify-content: center;
    gap: 20px;
    margin-top: 20px;
    
    .el-button {
      width: 45px;
      height: 45px;
      transition: all 0.3s ease;
      
      &:hover {
        transform: translateY(-3px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      }
    }
  }
}
</style>
