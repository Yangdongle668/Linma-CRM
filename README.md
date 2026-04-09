# 外贸CRM系统 (Foreign Trade CRM)

## 📖 项目简介

一套符合中国人使用习惯的外贸客户管理系统(CRM),采用Java Spring Boot + Vue技术栈,支持客户管理、订单管理、报价单、数据分析等全功能模块。

### 核心特性

✅ **完整业务流程**: 线索→客户→询盘→报价→订单→发货→收款全流程管理  
✅ **中国特色功能**: 企业微信/钉钉集成、企查查API、农历节日提醒、拼音搜索  
✅ **多语言支持**: 中英文双语界面,邮件模板多语言  
✅ **智能分析**: 销售漏斗、业绩排行、RFM模型、流失预警  
✅ **数据安全**: JWT认证、RBAC权限、数据权限、操作审计  
✅ **高性能**: Redis缓存、读写分离、异步任务、虚拟滚动  

---

## 🏗️ 技术架构

### 后端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17 | JDK版本 |
| Spring Boot | 3.2.0 | 核心框架 |
| MyBatis Plus | 3.5.5 | ORM框架 |
| MySQL | 8.0 | 数据库 |
| Redis | 7.0 | 缓存 |
| RabbitMQ | 3.12 | 消息队列 |
| Elasticsearch | 8.x | 搜索引擎 |
| MinIO | latest | 对象存储 |
| Knife4j | 4.3.0 | API文档 |
| EasyExcel | 3.3.3 | Excel处理 |
| iText | 7.2.5 | PDF生成 |

### 前端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.4 | 核心框架 |
| TypeScript | 5.3 | 类型系统 |
| Element Plus | 2.5 | UI组件库 |
| Vite | 5.0 | 构建工具 |
| Pinia | 2.1 | 状态管理 |
| ECharts | 5.4 | 图表库 |
| Axios | 1.6 | HTTP客户端 |

---

## 📁 项目结构

```
foreign-trade-crm/
├── pom.xml                          # 父POM
├── docker-compose.yml               # Docker编排
├── docs/                            # 项目文档
│   └── database/
│       └── schema.sql              # 数据库初始化脚本
│
├── crm-common/                      # 公共模块
│   ├── common-core/                # 核心工具类
│   ├── common-security/            # 安全认证
│   ├── common-log/                 # 操作日志AOP
│   ├── common-redis/               # Redis缓存
│   └── common-mybatis/             # MyBatis配置
│
├── crm-modules/                     # 业务模块
│   ├── module-system/              # 系统管理(用户、角色、菜单、部门、字典)
│   ├── module-customer/            # 客户管理(客户、联系人、跟进、标签)
│   ├── module-inquiry/             # 询盘管理
│   ├── module-quotation/           # 报价单管理
│   ├── module-order/               # 订单管理
│   ├── module-product/             # 产品管理
│   ├── module-contract/            # 合同管理
│   ├── module-shipping/            # 物流管理
│   ├── module-finance/             # 财务管理(应收应付、利润)
│   ├── module-analytics/           # 数据分析BI
│   └── module-message/             # 消息通知(邮件、短信、企微、钉钉)
│
├── crm-admin/                       # 后台管理启动模块
│   └── src/main/
│       ├── java/com/crm/admin/
│       │   └── CrmAdminApplication.java
│       └── resources/
│           ├── application.yml
│           ├── application-dev.yml
│           └── application-prod.yml
│
└── crm-frontend/                    # 前端项目(Vue 3)
    └── src/
        ├── api/                     # API接口
        ├── views/                   # 页面视图
        ├── components/              # 公共组件
        ├── router/                  # 路由配置
        ├── stores/                  # Pinia状态管理
        └── utils/                   # 工具函数
```

---

## 🚀 快速开始

### 方式一: Docker Compose一键启动(推荐)

```bash
# 1. 克隆项目
git clone <repository-url>
cd foreign-trade-crm

# 2. 启动所有服务(MySQL、Redis、RabbitMQ、ES、MinIO、Backend、Frontend)
docker-compose up -d

# 3. 查看日志
docker-compose logs -f backend

# 4. 访问应用
# 前端: http://localhost
# 后端API: http://localhost:8080/api
# Swagger文档: http://localhost:8080/api/doc.html
# MinIO控制台: http://localhost:9001
# RabbitMQ管理: http://localhost:15672
```

### 方式二: 本地开发环境

#### 1. 环境准备

- JDK 17+
- Maven 3.8+
- Node.js 18+
- MySQL 8.0
- Redis 7.0

#### 2. 数据库初始化

```bash
# 执行数据库脚本
mysql -u root -p < docs/database/schema.sql
```

#### 3. 后端启动

```bash
# 编译项目
mvn clean install

# 启动后端
cd crm-admin
mvn spring-boot:run

# 或使用IDE直接运行 CrmAdminApplication
```

#### 4. 前端启动

```bash
cd crm-frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 访问 http://localhost:3000
```

---

## 🔑 默认账号

**管理员账号:**
- 用户名: `admin`
- 密码: `admin123`

**登录后请立即修改密码!**

---

## 📊 核心功能模块

### 1. 系统管理 (module-system)
- ✅ 用户管理(CRUD、导入导出、密码重置)
- ✅ 角色管理(RBAC权限、数据权限)
- ✅ 菜单管理(动态路由、按钮权限)
- ✅ 部门管理(树形结构)
- ✅ 字典管理(系统字典、业务字典)
- ✅ 参数配置(系统参数、业务参数)
- ✅ 操作日志(AOP记录)
- ✅ 登录日志(IP追踪)

### 2. 客户管理 (module-customer)
- ✅ 客户列表(高级搜索、分页、自定义列)
- ✅ 客户详情(时间轴展示)
- ✅ 客户查重(公司名、邮箱、电话、网站)
- ✅ 公海池管理(自动回收、领取、释放)
- ✅ 批量导入导出(Excel智能匹配)
- ✅ 客户标签(手动/自动打标)
- ✅ 联系人管理(关键人标记)
- ✅ 跟进记录(提醒、模板、录音)

### 3. 询盘管理 (module-inquiry)
- ✅ 询盘CRUD
- ✅ 自动分配(轮询/负载均衡/区域匹配)
- ✅ 优先级管理
- ✅ 转为报价单
- ✅ 询盘回复记录

### 4. 报价单管理 (module-quotation)
- ✅ 报价单编辑(多币种、多贸易术语)
- ✅ 版本管理(支持回滚)
- ✅ 审批流程(利润率控制)
- ✅ PDF生成(中英双语+水印)
- ✅ 邮件发送(打开追踪)
- ✅ 从询盘转化/转为订单

### 5. 订单管理 (module-order)
- ✅ 订单CRUD
- ✅ 生产进度跟踪(拍照上传)
- ✅ 装箱单管理(自动计算体积重量)
- ✅ 发票管理(PI/CI)
- ✅ 出口退税记录
- ✅ 订单状态机

### 6. 产品管理 (module-product)
- ✅ 产品分类树
- ✅ 产品CRUD(含图片管理)
- ✅ SKU管理
- ✅ 价格体系(成本价/出厂价/FOB/CIF)
- ✅ 多币种价格换算
- ✅ 库存管理

### 7. 合同管理 (module-contract)
- ✅ 合同CRUD
- ✅ 合同模板(变量替换)
- ✅ 合同审批流程
- ✅ 电子签名集成(预留)
- ✅ 合同归档

### 8. 物流管理 (module-shipping)
- ✅ 发货单管理
- ✅ 物流商选择
- ✅ 运单号追踪
- ✅ 报关状态管理
- ✅ 物流轨迹查询(预留17track API)

### 9. 财务管理 (module-finance)
- ✅ 应收账款(逾期提醒、账龄分析)
- ✅ 应付账款(付款审批)
- ✅ 利润核算(单笔订单/统计报表)
- ✅ 发票管理

### 10. 数据分析BI (module-analytics)
- ✅ 销售漏斗分析
- ✅ 业绩排行榜
- ✅ 客户分析(RFM模型、流失预警)
- ✅ 产品分析(热销/滞销)
- ✅ 区域分析(世界地图热力图)
- ✅ 趋势分析(同比/环比)
- ✅ 首页仪表盘

### 11. 消息通知 (module-message)
- ✅ 站内消息
- ✅ 邮件服务(SMTP、模板、追踪)
- ✅ 企业微信集成(预留)
- ✅ 钉钉集成(预留)
- ✅ 短信服务(预留)

---

## 📝 API文档

启动后端后,访问Swagger文档:

```
http://localhost:8080/api/doc.html
```

所有接口都有详细的请求参数和响应示例。

---

## 🔧 配置说明

### 后端配置 (application-dev.yml)

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/crm_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: root123456

  data:
    redis:
      host: localhost
      port: 6379
      password: redis123456

  rabbitmq:
    host: localhost
    port: 5672
    username: admin
    password: admin123456

custom:
  minio:
    endpoint: http://localhost:9000
    accessKey: minioadmin
    secretKey: minio123456
    bucketName: crm-files

  jwt:
    secret: your-secret-key-here-change-in-production
    expiration: 7200000  # 2小时
```

### 前端配置 (.env.development)

```env
VITE_APP_TITLE=外贸CRM系统
VITE_APP_BASE_API=/api
VITE_APP_WS_URL=ws://localhost:8080/ws
```

---

## 🧪 测试

### 后端测试

```bash
# 运行单元测试
mvn test

# 运行指定模块测试
mvn test -pl crm-modules/module-customer
```

### 前端测试

```bash
# 运行单元测试
npm run test

# 运行E2E测试
npm run test:e2e
```

---

## 📦 部署

### 生产环境部署

1. **修改配置**: 更新 `application-prod.yml` 中的数据库、Redis等连接信息
2. **构建后端**: `mvn clean package -DskipTests`
3. **构建前端**: `npm run build`
4. **Docker部署**: `docker-compose -f docker-compose.prod.yml up -d`

详见 `docs/deployment/production.md`

---

## 🛠️ 开发规范

### 代码规范

- 遵循[阿里巴巴Java开发手册](https://github.com/alibaba/p3c)
- 使用Lombok简化代码
- 所有方法添加中文注释
- DTO/VO严格分离
- 统一使用Result封装返回结果

### Git提交规范

```
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式调整
refactor: 重构代码
test: 测试相关
chore: 构建/工具链相关
```

### 分支策略

- `main`: 主分支,仅接受merge request
- `develop`: 开发分支
- `feature/xxx`: 功能分支
- `hotfix/xxx`: 紧急修复分支

---

## 🤝 贡献指南

欢迎提交Issue和Pull Request!

1. Fork本仓库
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'feat: add some amazing feature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启Pull Request

---

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

---

## 👥 团队

- 架构设计: Lingma AI Assistant
- 后端开发: AI Agent Team
- 前端开发: (待补充)
- UI设计: (待补充)

---

## 📞 联系方式

- 项目地址: [GitHub Repository](your-repo-url)
- 问题反馈: [Issues](your-issues-url)
- 邮箱: support@yourcompany.com

---

## ⭐ Star History

如果这个项目对你有帮助,请给一个Star! ⭐

---

**最后更新**: 2026-04-09  
**版本**: v1.0.0
