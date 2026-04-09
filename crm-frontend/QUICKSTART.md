# 外贸CRM前端 - 快速启动指南

## 📋 前置要求

- Node.js >= 18.0.0
- npm >= 9.0.0

## 🎯 第一次运行

### 步骤1: 安装依赖

```bash
cd C:\Users\Yang\Desktop\lima\foreign-trade-crm\crm-frontend
npm install
```

如果下载速度慢,可以使用国内镜像:

```bash
npm config set registry https://registry.npmmirror.com
npm install
```

### 步骤2: 配置环境变量(可选)

默认配置已可用,如需修改API地址,编辑 `.env.development`:

```env
VITE_APP_BASE_API=http://localhost:8080
```

### 步骤3: 启动开发服务器

```bash
npm run dev
```

浏览器会自动打开 http://localhost:3000

### 步骤4: 测试登录

由于后端可能还未就绪,你可以:

1. **临时测试**: 修改 `src/views/login/index.vue`,注释掉API调用,直接跳转
2. **完整测试**: 确保后端服务运行在 http://localhost:8080

## 🔍 验证功能清单

启动后,你应该能看到:

- ✅ 登录页面(渐变背景 + 动态效果)
- ✅ 输入框聚焦动画
- ✅ 登录按钮loading效果
- ✅ 登录后跳转到Dashboard
- ✅ Dashboard统计卡片
- ✅ 左侧菜单可折叠/展开
- ✅ 顶部标签栏可关闭
- ✅ 右键菜单功能
- ✅ 全屏切换
- ✅ 退出登录

## 🐛 常见问题

### 1. 端口被占用

修改 `vite.config.ts` 中的 `port`:

```typescript
server: {
  port: 3001,  // 改为其他端口
}
```

### 2. 依赖安装失败

删除 `node_modules` 和 `package-lock.json`,重新安装:

```bash
rm -rf node_modules package-lock.json
npm install
```

### 3. TypeScript报错

确保安装了所有类型定义:

```bash
npm install --save-dev @types/node
```

### 4. Element Plus样式未加载

检查 `src/main.ts` 是否正确导入:

```typescript
import 'element-plus/dist/index.css'
```

## 🚀 下一步

1. **连接后端**: 配置正确的API地址
2. **添加业务模块**: 按照现有结构添加客户、产品等模块
3. **定制主题**: 修改 `src/styles/index.scss` 中的颜色变量
4. **部署上线**: 运行 `npm run build` 生成生产文件

## 📞 技术支持

如有问题,请查看:
- [Vue 3 文档](https://cn.vuejs.org/)
- [Element Plus 文档](https://element-plus.org/zh-CN/)
- [Vite 文档](https://cn.vitejs.dev/)

---

祝开发顺利! 🎉
