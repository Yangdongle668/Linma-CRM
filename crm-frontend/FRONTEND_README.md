# 外贸CRM系统 - 前端框架

基于 Vue 3 + TypeScript + Element Plus + Vite 构建的现代化外贸CRM系统前端框架。

## 📦 技术栈

- **核心框架**: Vue 3.4 (Composition API)
- **构建工具**: Vite 5
- **类型系统**: TypeScript 5
- **UI组件库**: Element Plus 2.5
- **状态管理**: Pinia 2.1
- **路由管理**: Vue Router 4.2
- **HTTP客户端**: Axios 1.6
- **样式方案**: SCSS
- **代码规范**: ESLint + Prettier

## 📁 项目结构

```
crm-frontend/
├── public/                  # 静态资源
├── src/
│   ├── api/                # API接口
│   │   └── auth.ts         # 认证相关API
│   ├── layout/             # 布局组件
│   │   ├── index.vue       # 主布局
│   │   └── components/
│   │       ├── Navbar/     # 顶部导航栏
│   │       ├── Sidebar/    # 侧边栏
│   │       ├── TagsView/   # 标签栏
│   │       └── AppMain.vue # 主内容区
│   ├── router/             # 路由配置
│   │   └── index.ts
│   ├── stores/             # Pinia状态管理
│   │   ├── user.ts         # 用户状态
│   │   └── app.ts          # 应用状态
│   ├── styles/             # 全局样式
│   │   └── index.scss
│   ├── utils/              # 工具函数
│   │   └── request.ts      # Axios封装
│   ├── views/              # 页面视图
│   │   ├── login/          # 登录页
│   │   ├── dashboard/      # 首页
│   │   └── error/          # 错误页面
│   ├── App.vue             # 根组件
│   ├── main.ts             # 入口文件
│   └── vite-env.d.ts       # 类型声明
├── .env.development        # 开发环境变量
├── .env.production         # 生产环境变量
├── index.html              # HTML入口
├── package.json            # 依赖配置
├── tsconfig.json           # TS配置
├── tsconfig.node.json      # Node环境TS配置
└── vite.config.ts          # Vite配置
```

## 🚀 快速开始

### 1. 安装依赖

```bash
npm install
```

### 2. 启动开发服务器

```bash
npm run dev
```

访问 http://localhost:3000

### 3. 构建生产版本

```bash
npm run build
```

### 4. 预览生产构建

```bash
npm run preview
```

## ✨ 核心功能

### 1. 登录页面
- 左右分屏设计,左侧品牌展示,右侧登录表单
- 渐变背景 + 动态装饰效果
- 表单验证 + Loading状态
- 记住我功能
- 响应式设计

### 2. 主布局系统
- **Navbar**: 折叠按钮、面包屑、全屏、消息通知、用户菜单
- **Sidebar**: Logo、多级菜单、支持折叠
- **TagsView**: 页面标签管理、右键菜单、刷新/关闭功能
- **AppMain**: 路由视图 + Keep-alive缓存

### 3. 路由系统
- 路由守卫(登录验证)
- NProgress进度条
- 动态页面标题
- 404错误页面

### 4. 状态管理
- **User Store**: Token管理、用户信息、登录/登出
- **App Store**: 侧边栏状态、设备类型、主题色

### 5. HTTP封装
- Axios拦截器(请求/响应)
- 自动添加Token
- 统一错误处理
- 401自动跳转登录

### 6. 全局样式
- CSS变量定义
- 通用工具类
- 滚动条美化
- 过渡动画

## 🎨 设计规范

### 颜色体系
```scss
$primary-color: #2563EB;    // 主色调
$success-color: #10B981;    // 成功色
$warning-color: #F59E0B;    // 警告色
$danger-color: #EF4444;     // 危险色
```

### 布局尺寸
```scss
$sidebar-width: 210px;              // 侧边栏宽度
$sidebar-collapsed-width: 64px;     // 折叠后宽度
$navbar-height: 60px;               // 导航栏高度
$tagsview-height: 40px;             // 标签栏高度
```

## 🔧 开发指南

### 添加新页面

1. 在 `src/views` 下创建页面组件
2. 在 `src/router/index.ts` 中添加路由配置
3. 如需加入菜单,在 `Sidebar` 组件中添加对应菜单项

### 调用API

```typescript
import { login } from '@/api/auth'

// 直接调用
const result = await login({ username, password })
```

### 使用状态管理

```typescript
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

// 登录
await userStore.login({ username, password })

// 获取用户信息
await userStore.fetchUserInfo()

// 登出
await userStore.logout()
```

## 📝 注意事项

1. **环境变量**: 根据环境修改 `.env.development` 和 `.env.production` 中的API地址
2. **代理配置**: 开发时API请求会通过Vite代理转发,生产环境需配置nginx反向代理
3. **TypeScript**: 所有新文件请使用TypeScript编写
4. **组件命名**: 使用PascalCase命名Vue组件
5. **代码注释**: 关键逻辑请添加中文注释

## 🌐 浏览器支持

- Chrome >= 90
- Firefox >= 88
- Safari >= 14
- Edge >= 90

## 📄 License

MIT

---

**开发团队**: 外贸CRM系统项目组  
**最后更新**: 2026-04-09
