# 外贸CRM系统 - 前端设计规范

## 🎨 设计理念

**大气、专业、易用** - 打造符合外贸行业特点的现代化管理系统界面

### 核心原则
1. **视觉层次清晰**: 通过色彩、间距、阴影建立明确的信息层级
2. **交互流畅自然**: 动画过渡、即时反馈、操作便捷
3. **数据可视化**: 图表丰富、直观易懂
4. **响应式设计**: 适配桌面、平板、手机
5. **国际化支持**: 中英文无缝切换

---

## 🎯 设计风格

### 色彩系统

#### 主色调
```scss
// 品牌色 - 科技蓝(体现专业、信任)
$primary-color: #2563EB;        // 主色
$primary-light: #3B82F6;        // 浅色
$primary-dark: #1D4ED8;         // 深色
$primary-bg: #EFF6FF;           // 背景色

// 辅助色
$success-color: #10B981;        // 成功/完成
$warning-color: #F59E0B;        // 警告/待处理
$danger-color: #EF4444;         // 危险/错误
$info-color: #3B82F6;           // 信息/提示
```

#### 中性色
```scss
// 文字颜色
$text-primary: #1F2937;         // 主要文字
$text-regular: #4B5563;         // 常规文字
$text-secondary: #6B7280;       // 次要文字
$text-placeholder: #9CA3AF;     // 占位符

// 边框和背景
$border-color: #E5E7EB;         // 边框
$bg-page: #F3F4F6;              // 页面背景
$bg-white: #FFFFFF;             // 白色背景
$bg-hover: #F9FAFB;             // 悬停背景
```

#### 渐变色
```scss
// 仪表盘渐变背景
$gradient-primary: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
$gradient-success: linear-gradient(135deg, #10b981 0%, #059669 100%);
$gradient-warning: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
$gradient-danger: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
```

### 字体系统

```scss
// 字体家族
$font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
              "Helvetica Neue", Arial, "Noto Sans SC", sans-serif;

// 字号
$font-size-xs: 12px;            // 辅助文字
$font-size-sm: 13px;            // 小字
$font-size-base: 14px;          // 正文
$font-size-lg: 16px;            // 小标题
$font-size-xl: 18px;            // 标题
$font-size-2xl: 24px;           // 大标题
$font-size-3xl: 32px;           // 超大标题

// 字重
$font-weight-normal: 400;
$font-weight-medium: 500;
$font-weight-semibold: 600;
$font-weight-bold: 700;
```

### 间距系统

```scss
// 8px网格系统
$spacing-xs: 4px;
$spacing-sm: 8px;
$spacing-md: 16px;
$spacing-lg: 24px;
$spacing-xl: 32px;
$spacing-2xl: 48px;
$spacing-3xl: 64px;
```

### 圆角和阴影

```scss
// 圆角
$radius-sm: 4px;
$radius-md: 8px;
$radius-lg: 12px;
$radius-xl: 16px;
$radius-full: 9999px;

// 阴影
$shadow-sm: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
$shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
$shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
$shadow-xl: 0 20px 25px -5px rgba(0, 0, 0, 0.1);
```

---

## 🏗️ 布局设计

### 整体布局结构

```
┌─────────────────────────────────────────────┐
│              顶部导航栏 (60px)                │
│  Logo | 面包屑 | 搜索 | 消息 | 用户头像       │
├──────────┬──────────────────────────────────┤
│          │                                  │
│  侧边栏   │        Tab标签栏 (40px)          │
│ (240px)  ├──────────────────────────────────┤
│          │                                  │
│  菜单树   │                                  │
│          │      主内容区 (自适应)             │
│          │                                  │
│          │                                  │
└──────────┴──────────────────────────────────┘
```

### 响应式断点

```scss
// 超小屏幕(手机)
@media (max-width: 767px) {
  .sidebar { display: none; }
  .main-content { width: 100%; }
}

// 小屏幕(平板)
@media (min-width: 768px) and (max-width: 1023px) {
  .sidebar { width: 200px; }
}

// 中等屏幕(桌面)
@media (min-width: 1024px) and (max-width: 1279px) {
  .sidebar { width: 220px; }
}

// 大屏幕(大桌面)
@media (min-width: 1280px) {
  .sidebar { width: 240px; }
}
```

---

## 🧩 组件设计规范

### 1. 表格组件 (Enhanced Table)

**设计要点:**
- 表头固定,内容滚动
- 斑马纹+悬停高亮
- 列宽可拖拽调整
- 列显示/隐藏控制
- 批量操作工具栏
- 分页器右对齐

**示例:**
```vue
<template>
  <div class="enhanced-table">
    <!-- 工具栏 -->
    <div class="table-toolbar">
      <el-button type="primary" :icon="Plus">新增</el-button>
      <el-button :icon="Download">导出</el-button>
      <el-button :icon="Upload">导入</el-button>
      <el-input v-model="searchKeyword" placeholder="搜索..." clearable />
    </div>

    <!-- 表格 -->
    <el-table
      :data="tableData"
      border
      stripe
      highlight-current-row
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="name" label="名称" min-width="150" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
      />
    </div>
  </div>
</template>
```

### 2. 表单组件 (Smart Form)

**设计要点:**
- 标签右对齐
- 必填项红色星号
- 实时校验反馈
- 分组展示(使用el-card或el-divider)
- 底部按钮右对齐

**示例:**
```vue
<template>
  <el-form
    ref="formRef"
    :model="formData"
    :rules="formRules"
    label-width="120px"
    label-position="right"
  >
    <el-row :gutter="24">
      <el-col :span="12">
        <el-form-item label="客户名称" prop="companyName" required>
          <el-input v-model="formData.companyName" placeholder="请输入客户名称" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="国家地区" prop="country">
          <el-select v-model="formData.country" placeholder="请选择" filterable>
            <el-option label="美国" value="US" />
            <el-option label="德国" value="DE" />
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>

    <el-form-item>
      <el-button type="primary" @click="handleSubmit">提交</el-button>
      <el-button @click="handleReset">重置</el-button>
    </el-form-item>
  </el-form>
</template>
```

### 3. 卡片组件 (Data Card)

**用于仪表盘数据展示**

```vue
<template>
  <el-card class="data-card" shadow="hover">
    <div class="card-header">
      <span class="card-title">{{ title }}</span>
      <el-icon :size="24" :color="iconColor">
        <component :is="icon" />
      </el-icon>
    </div>
    <div class="card-body">
      <div class="card-value">{{ value }}</div>
      <div class="card-footer">
        <span :class="trendClass">
          <el-icon><component :is="trendIcon" /></el-icon>
          {{ trendValue }}%
        </span>
        <span class="card-label">{{ label }}</span>
      </div>
    </div>
  </el-card>
</template>

<style scoped>
.data-card {
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.card-value {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 8px;
}
</style>
```

### 4. 搜索表单组件 (Search Form)

```vue
<template>
  <el-card class="search-form" shadow="never">
    <el-form :inline="true" :model="queryParams">
      <el-form-item label="客户名称">
        <el-input v-model="queryParams.companyName" placeholder="请输入" clearable />
      </el-form-item>
      <el-form-item label="客户等级">
        <el-select v-model="queryParams.level" placeholder="请选择" clearable>
          <el-option label="A级" value="A" />
          <el-option label="B级" value="B" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
        <el-button :icon="Refresh" @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
```

### 5. 详情页组件 (Detail View)

**Tab式布局,展示关联信息**

```vue
<template>
  <div class="detail-page">
    <!-- 头部信息卡片 -->
    <el-card class="header-card">
      <div class="header-content">
        <div class="header-left">
          <h2>{{ customer.companyName }}</h2>
          <p class="subtitle">{{ customer.companyNameEn }}</p>
          <div class="tags">
            <el-tag>{{ customer.level }}级客户</el-tag>
            <el-tag type="success">{{ customer.industry }}</el-tag>
          </div>
        </div>
        <div class="header-right">
          <el-button type="primary" @click="handleEdit">编辑</el-button>
          <el-button @click="handleFollowUp">新建跟进</el-button>
        </div>
      </div>
    </el-card>

    <!-- Tab内容区 -->
    <el-tabs v-model="activeTab" class="detail-tabs">
      <el-tab-pane label="基本信息" name="basic">
        <BasicInfo :data="customer" />
      </el-tab-pane>
      <el-tab-pane label="联系人" name="contacts">
        <ContactList :customerId="customerId" />
      </el-tab-pane>
      <el-tab-pane label="跟进记录" name="followups">
        <FollowUpTimeline :customerId="customerId" />
      </el-tab-pane>
      <el-tab-pane label="商机" name="opportunities">
        <OpportunityList :customerId="customerId" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
```

---

## 📊 数据可视化规范

### 图表配色方案

```typescript
const chartColors = [
  '#2563EB', // 蓝
  '#10B981', // 绿
  '#F59E0B', // 黄
  '#EF4444', // 红
  '#8B5CF6', // 紫
  '#EC4899', // 粉
  '#06B6D4', // 青
  '#F97316'  // 橙
];
```

### 常用图表类型

1. **销售漏斗** - Funnel Chart
2. **业绩趋势** - Line Chart
3. **客户分布** - Pie Chart / Map
4. **产品分析** - Bar Chart
5. **区域对比** - Radar Chart

---

## ✨ 交互动效

### 页面过渡动画

```scss
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
```

### 列表加载动画

```scss
@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.list-item {
  animation: slideIn 0.3s ease forwards;
}
```

### 按钮点击反馈

```scss
.el-button {
  transition: all 0.2s;

  &:active {
    transform: scale(0.95);
  }
}
```

---

## 🎭 主题定制

### Element Plus主题覆盖

```scss
// styles/element-plus.scss
:root {
  --el-color-primary: #2563EB;
  --el-color-success: #10B981;
  --el-color-warning: #F59E0B;
  --el-color-danger: #EF4444;
  --el-border-radius-base: 8px;
}

.el-button--primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

.el-card {
  border-radius: 12px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}
```

---

## 📱 移动端适配

### 移动端特殊处理

```vue
<template>
  <!-- 桌面端表格,移动端卡片 -->
  <div class="responsive-list">
    <!-- 桌面端 -->
    <el-table v-show="!isMobile" :data="list" />

    <!-- 移动端 -->
    <div v-show="isMobile" class="mobile-cards">
      <el-card v-for="item in list" :key="item.id" class="mobile-card">
        <div class="card-title">{{ item.name }}</div>
        <div class="card-info">{{ item.info }}</div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { useWindowSize } from '@vueuse/core'

const { width } = useWindowSize()
const isMobile = computed(() => width.value < 768)
</script>
```

---

## 🔍 无障碍设计

- 所有图标按钮添加 `aria-label`
- 表单字段关联 `label` 和 `input`
- 键盘导航支持
- 色盲友好配色(不只依赖颜色传达信息)

---

## 📝 命名规范

### 文件命名
- 组件: `PascalCase.vue` (如 `CustomerList.vue`)
- 样式: `kebab-case.scss` (如 `customer-list.scss`)
- 工具函数: `camelCase.ts` (如 `formatDate.ts`)

### CSS类名
```scss
// BEM命名规范
.customer-list {                    // Block
  &__item {                        // Element
    &--active {                    // Modifier
      // styles
    }
  }
}
```

---

## 🚀 性能优化

### 代码分割
```typescript
// 路由懒加载
const CustomerList = () => import('@/views/customer/list/index.vue')

// 组件懒加载
const HeavyComponent = defineAsyncComponent(() =>
  import('@/components/HeavyComponent.vue')
)
```

### 虚拟滚动
```vue
<el-table-v2
  :columns="columns"
  :data="largeDataSet"
  :width="1200"
  :height="600"
/>
```

### 图片优化
```vue
<img
  v-lazy="imageUrl"
  :alt="alt"
  loading="lazy"
/>
```

---

**设计规范版本**: v1.0  
**最后更新**: 2026-04-09
