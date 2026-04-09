<template>
  <div class="error-page">
    <div class="error-content">
      <h1 class="error-code">404</h1>
      <h2 class="error-title">页面不存在</h2>
      <p class="error-description">抱歉,您访问的页面不存在或已被删除</p>
      <el-button type="primary" @click="goHome">返回首页</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

/**
 * 返回首页
 */
function goHome() {
  router.push('/')
}
</script>

<style scoped lang="scss">
.error-page {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.error-content {
  text-align: center;
  color: white;
  
  .error-code {
    font-size: 120px;
    font-weight: bold;
    margin: 0;
    line-height: 1;
    text-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  }
  
  .error-title {
    font-size: 32px;
    margin: 20px 0 10px;
  }
  
  .error-description {
    font-size: 16px;
    opacity: 0.9;
    margin-bottom: 30px;
  }
  
  .el-button {
    padding: 12px 30px;
    font-size: 16px;
  }
}
</style>
