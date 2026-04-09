# CRM Frontend - 外贸CRM系统前端

基于 Vue 3 + TypeScript + Element Plus + Vite 构建的外贸CRM系统前端项目。

## 技术栈

- **核心框架**: Vue 3.4
- **语言**: TypeScript 5.3
- **UI组件库**: Element Plus 2.5
- **构建工具**: Vite 5.0
- **状态管理**: Pinia 2.1
- **路由**: Vue Router 4.2
- **HTTP客户端**: Axios 1.6
- **图表库**: ECharts 5.4
- **富文本编辑器**: WangEditor 5
- **CSS预处理**: Sass

## 快速开始

```bash
# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build

# 代码检查
npm run lint

# 代码格式化
npm run format
```

## 项目结构

```
src/
├── main.ts                  # 入口文件
├── App.vue                  # 根组件
├── router/                  # 路由配置
│   ├── index.ts
│   └── modules/             # 路由模块
├── stores/                  # Pinia状态管理
│   ├── user.ts              # 用户信息
│   ├── permission.ts        # 权限路由
│   └── app.ts               # 应用配置
├── api/                     # API接口
│   ├── system/              # 系统管理API
│   ├── customer/            # 客户管理API
│   ├── inquiry/             # 询盘管理API
│   ├── quotation/           # 报价单API
│   ├── order/               # 订单API
│   ├── product/             # 产品API
│   ├── contract/            # 合同API
│   ├── shipping/            # 物流API
│   ├── finance/             # 财务API
│   ├── analytics/           # 数据分析API
│   └── message/             # 消息通知API
├── views/                   # 页面视图
│   ├── login/               # 登录页
│   ├── dashboard/           # 首页仪表盘
│   ├── system/              # 系统管理
│   ├── customer/            # 客户管理
│   ├── inquiry/             # 询盘管理
│   ├── quotation/           # 报价单管理
│   ├── order/               # 订单管理
│   ├── product/             # 产品管理
│   ├── contract/            # 合同管理
│   ├── shipping/            # 物流管理
│   ├── finance/             # 财务管理
│   ├── analytics/           # 数据分析
│   └── message/             # 消息中心
├── components/              # 公共组件
│   ├── SearchForm/          # 搜索表单
│   ├── Pagination/          # 分页器
│   ├── UploadFile/          # 文件上传
│   ├── RichEditor/          # 富文本编辑器
│   ├── DictSelect/          # 字典下拉框
│   └── Chart/               # 图表组件
├── layout/                  # 布局组件
│   ├── index.vue
│   └── components/
│       ├── Sidebar/         # 侧边栏
│       ├── Navbar/          # 顶部导航
│       ├── TagsView/        # 标签页
│       └── AppMain/         # 主内容区
├── utils/                   # 工具函数
│   ├── request.ts           # Axios封装
│   ├── auth.ts              # 认证工具
│   └── validate.ts          # 验证工具
├── hooks/                   # 组合式API
│   ├── useTable.ts          # 表格Hook
│   └── useForm.ts           # 表单Hook
├── styles/                  # 样式文件
│   ├── index.scss
│   └── variables.scss
├── locales/                 # 国际化
│   ├── zh-CN.ts
│   └── en-US.ts
└── types/                   # TypeScript类型
    └── api.d.ts
```

## 开发规范

### 命名规范

- **文件名**: kebab-case (如 `user-list.vue`)
- **组件名**: PascalCase (如 `UserList`)
- **变量/函数**: camelCase (如 `getUserList`)
- **常量**: UPPER_SNAKE_CASE (如 `API_BASE_URL`)
- **类型/接口**: PascalCase (如 `UserInfo`)

### 组件开发

```vue
<script setup lang="ts">
import { ref, computed } from 'vue'

// Props定义
interface Props {
  title: string
  data?: any[]
}

const props = withDefaults(defineProps<Props>(), {
  data: () => []
})

// Emits定义
interface Emits {
  (e: 'update', value: any): void
}

const emit = defineEmits<Emits>()

// 响应式数据
const loading = ref(false)

// 计算属性
const totalCount = computed(() => props.data.length)

// 方法
const handleUpdate = () => {
  emit('update', {})
}
</script>

<template>
  <div class="component-name">
    <!-- 模板内容 -->
  </div>
</template>

<style scoped lang="scss">
.component-name {
  // 样式
}
</style>
```

### API调用

```typescript
// api/customer/index.ts
import request from '@/utils/request'

export interface CustomerQuery {
  pageNum: number
  pageSize: number
  companyName?: string
  level?: string
}

export function getCustomerList(params: CustomerQuery) {
  return request({
    url: '/customer/list',
    method: 'post',
    data: params
  })
}

export function getCustomerDetail(id: number) {
  return request({
    url: `/customer/${id}`,
    method: 'get'
  })
}
```

### 使用示例

```vue
<script setup lang="ts">
import { getCustomerList } from '@/api/customer'
import { useTable } from '@/hooks/useTable'

const { tableData, loading, pagination, search, reset } = useTable({
  api: getCustomerList,
  defaultParams: {
    pageNum: 1,
    pageSize: 10
  }
})
</script>
```

## 环境变量

```env
# .env.development
VITE_APP_TITLE=外贸CRM系统
VITE_APP_BASE_API=/api
VITE_APP_WS_URL=ws://localhost:8080/ws

# .env.production
VITE_APP_TITLE=外贸CRM系统
VITE_APP_BASE_API=/api
VITE_APP_WS_URL=wss://your-domain.com/ws
```

## 浏览器支持

- Chrome >= 90
- Firefox >= 88
- Safari >= 14
- Edge >= 90

## 许可证

MIT
