# Foreign Trade CRM System - 外贸CRM系统

<div align="center">

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)
![License](https://img.shields.io/badge/license-MIT-green.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)
![Vue](https://img.shields.io/badge/Vue-3.4-success.svg)
![Java](https://img.shields.io/badge/Java-17-orange.svg)
![Docker](https://img.shields.io/badge/Docker-Supported-blue.svg)

**一套符合中国人使用习惯的现代化外贸客户管理系统**

[English](#english) | [中文](#中文)

</div>

---

## 📖 Introduction / 项目简介

A comprehensive CRM system designed for foreign trade companies, featuring customer management, inquiry tracking, quotation generation, order processing, and business analytics.

专为外贸企业设计的完整CRM系统,包含客户管理、询盘跟踪、报价生成、订单处理和商业分析等功能。

### ✨ Key Features / 核心特性

- 🎯 **Complete Business Flow**: Lead → Customer → Inquiry → Quotation → Order → Shipping → Payment
- 🌍 **Multi-language Support**: Chinese & English interface
- 📊 **Smart Analytics**: Sales funnel, RFM model, churn prediction
- 🔐 **Enterprise Security**: JWT auth, RBAC permissions, data scope control
- 🚀 **High Performance**: Redis caching, async tasks, read-write separation ready
- 🇨🇳 **China-Friendly**: WeCom/DingTalk integration ready, Qichacha API support

---

## 🖥️ Tech Stack / 技术栈

### Backend / 后端
- **Framework**: Spring Boot 3.2 + JDK 17
- **ORM**: MyBatis Plus 3.5
- **Database**: MySQL 8.0 + Redis 7.0
- **Search**: Elasticsearch 8.x
- **Queue**: RabbitMQ 3.12
- **Storage**: MinIO
- **Docs**: Knife4j (Swagger)

### Frontend / 前端
- **Framework**: Vue 3.4 + TypeScript 5.3
- **UI**: Element Plus 2.5
- **Build**: Vite 5.0
- **State**: Pinia 2.1
- **Charts**: ECharts 5.4

### DevOps / 运维
- **Container**: Docker + Docker Compose
- **CI/CD**: GitHub Actions (ready)

---

## 🚀 Quick Start / 快速开始

### Prerequisites / 前置要求

- Docker & Docker Compose
- JDK 17+ (for local development)
- Node.js 18+ (for frontend development)
- MySQL 8.0 (if not using Docker)

### One-Click Deployment / 一键部署

#### Option 1: Docker Compose (Recommended) / 方式一: Docker Compose(推荐)

```bash
# Clone repository
git clone https://github.com/YOUR_USERNAME/foreign-trade-crm.git
cd foreign-trade-crm

# Start all services
docker-compose up -d

# Wait for database initialization (about 30 seconds)
sleep 30

# Check logs
docker-compose logs -f backend
```

#### Option 2: Local Development / 方式二: 本地开发

```bash
# 1. Initialize database
mysql -u root -p < docs/database/schema.sql

# 2. Start infrastructure services
docker-compose up -d mysql redis rabbitmq elasticsearch minio

# 3. Build and start backend
mvn clean install
cd crm-admin
mvn spring-boot:run

# 4. Start frontend (optional)
cd ../crm-frontend
npm install
npm run dev
```

### Access URLs / 访问地址

| Service | URL | Credentials |
|---------|-----|-------------|
| **Backend API** | http://localhost:8080/api | - |
| **Swagger Docs** | http://localhost:8080/api/doc.html | - |
| **Frontend** | http://localhost:3000 | admin / admin123 |
| **MinIO Console** | http://localhost:9001 | minioadmin / minio123456 |
| **RabbitMQ** | http://localhost:15672 | admin / admin123456 |
| **MySQL** | localhost:3306 | root / root123456 |
| **Redis** | localhost:6379 | Password: redis123456 |

**Default Admin Account / 默认管理员账号:**
- Username: `admin`
- Password: `admin123`

⚠️ **Please change the password after first login!**

---

## 📁 Project Structure / 项目结构

```
foreign-trade-crm/
├── 📄 README.md                     # This file
├── 📄 docker-compose.yml            # Docker orchestration
├── 📄 pom.xml                       # Parent POM
├── 📂 docs/                         # Documentation
│   ├── database/schema.sql         # Database initialization
│   └── frontend/                    # Frontend design docs
├── 📂 crm-common/                   # Common modules
│   ├── common-core/                # Core utilities
│   ├── common-security/            # JWT & Security
│   ├── common-log/                 # Operation logging
│   ├── common-redis/               # Redis utilities
│   └── common-mybatis/             # MyBatis config
├── 📂 crm-modules/                  # Business modules
│   ├── module-system/              # System management
│   ├── module-customer/            # Customer management
│   ├── module-inquiry/             # Inquiry management
│   ├── module-quotation/           # Quotation management
│   ├── module-order/               # Order management
│   ├── module-product/             # Product management
│   ├── module-contract/            # Contract management
│   ├── module-shipping/            # Shipping management
│   ├── module-finance/             # Financial management
│   ├── module-analytics/           # Business analytics
│   └── module-message/             # Message notification
├── 📂 crm-admin/                    # Admin application
│   ├── src/main/java/              # Java source code
│   ├── src/main/resources/         # Configuration files
│   └── Dockerfile                   # Docker image build
└── 📂 crm-frontend/                 # Frontend application
    ├── src/                        # Vue source code
    ├── package.json                # Dependencies
    └── vite.config.ts              # Vite configuration
```

---

## 🎯 Modules Overview / 模块概览

### 1. System Management / 系统管理
- ✅ User Management (CRUD, Import/Export)
- ✅ Role Management (RBAC, Data Permissions)
- ✅ Menu Management (Dynamic Routing)
- ✅ Department Management (Tree Structure)
- ✅ Dictionary Management
- ✅ Configuration Management
- ✅ Operation Logs
- ✅ Login Logs

### 2. Customer Management / 客户管理
- ✅ Customer List (Advanced Search, Pagination)
- ✅ Customer Detail (Timeline View)
- ✅ Duplicate Detection (Company/Email/Phone/Website)
- ✅ High Seas Pool (Auto-recycle, Claim)
- ✅ Batch Import/Export (Excel)
- ✅ Customer Assignment & Merge
- ✅ Contact Management
- ✅ Follow-up Records (Reminders, Templates)
- ✅ Customer Tags (Auto/Manual)
- ✅ Visit Check-in (GPS + Photos)

### 3. Inquiry Management / 询盘管理
- ✅ Inquiry CRUD
- ✅ Auto Assignment (Round-robin/Load-balancing/Region-based)
- ✅ Priority Management
- ✅ Convert to Quotation
- ✅ Reply History

### 4. Quotation Management / 报价单管理
- ✅ Quotation Editor (Multi-currency, Trade Terms)
- ✅ Version Control
- ✅ Approval Workflow
- ✅ PDF Generation (Bilingual, Watermark)
- ✅ Email Sending with Tracking
- ✅ Convert from Inquiry / To Order

### 5. Order Management / 订单管理
- ✅ Order CRUD
- ✅ Production Progress Tracking
- ✅ Packing List (Auto-calculate Volume/Weight)
- ✅ Invoice Management (PI/CI)
- ✅ Export Tax Refund Records
- ✅ Order Status Machine

### 6. Product Management / 产品管理
- ✅ Product Categories (Tree)
- ✅ Product CRUD (with Images)
- ✅ SKU Management
- ✅ Price System (Cost/Factory/FOB/CIF)
- ✅ Multi-currency Price Conversion
- ✅ Inventory Management

### 7. Contract Management / 合同管理
- ✅ Contract CRUD
- ✅ Contract Templates (Variable Substitution)
- ✅ Approval Workflow
- ✅ E-signature Integration (Ready)
- ✅ Contract Archiving

### 8. Shipping Management / 物流管理
- ✅ Shipment Orders
- ✅ Carrier Selection (DHL/FedEx/UPS/Sea/Air)
- ✅ Tracking Number & Status
- ✅ Customs Declaration
- ✅ Logistics Tracking (17track API Ready)

### 9. Financial Management / 财务管理
- ✅ Accounts Receivable (Overdue Alerts, Aging Analysis)
- ✅ Accounts Payable (Payment Approval)
- ✅ Profit Calculation (Per Order & Statistics)
- ✅ Invoice Management

### 10. Business Analytics / 数据分析
- ✅ Sales Funnel Analysis
- ✅ Performance Rankings
- ✅ Customer Analytics (RFM Model, Churn Prediction)
- ✅ Product Analytics (Top Sellers, Slow-moving)
- ✅ Regional Analysis (World Map Heatmap)
- ✅ Trend Analysis (MoM, YoY Growth)
- ✅ Dashboard (Today's Tasks, Monthly Performance)

### 11. Message Notification / 消息通知
- ✅ In-app Notifications
- ✅ Email Service (SMTP, Templates, Tracking)
- ✅ WeCom Integration (Ready)
- ✅ DingTalk Integration (Ready)
- ✅ SMS Service (Ready)

---

## 🗄️ Database / 数据库

### Tables Overview / 数据表概览

- **System Tables** (11): `sys_user`, `sys_role`, `sys_menu`, `sys_dept`, etc.
- **Business Tables** (15): `crm_customer`, `crm_inquiry`, `crm_quotation`, `crm_order`, etc.
- **Message Tables** (3): `msg_notification`, `msg_email_log`, `msg_template`

Total: **30+ tables** with complete indexes and initial data.

### Initialize Database / 初始化数据库

```bash
mysql -u root -p < docs/database/schema.sql
```

---

## 🔧 Configuration / 配置

### Environment Variables / 环境变量

Create `.env` file in project root or modify `docker-compose.yml`:

```env
# Database
DB_PASSWORD=root123456

# Redis
REDIS_PASSWORD=redis123456

# RabbitMQ
RABBITMQ_USER=admin
RABBITMQ_PASSWORD=admin123456

# MinIO
MINIO_ROOT_USER=minioadmin
MINIO_ROOT_PASSWORD=minio123456

# JWT (Change in production!)
JWT_SECRET=your-secret-key-change-in-production
```

### Application Properties / 应用配置

Main config: `crm-admin/src/main/resources/application.yml`

Profiles:
- `application-dev.yml` - Development environment
- `application-prod.yml` - Production environment

---

## 🐳 Docker Deployment / Docker部署

### Production Deployment / 生产环境部署

1. **Update production config:**
   ```bash
   # Edit application-prod.yml with your production settings
   vim crm-admin/src/main/resources/application-prod.yml
   ```

2. **Build images:**
   ```bash
   docker-compose build
   ```

3. **Start services:**
   ```bash
   docker-compose -f docker-compose.prod.yml up -d
   ```

4. **Check health:**
   ```bash
   curl http://localhost:8080/api/actuator/health
   ```

### Update Deployment / 更新部署

```bash
# Pull latest code
git pull origin main

# Rebuild and restart
docker-compose down
docker-compose build --no-cache
docker-compose up -d
```

---

## 📚 API Documentation / API文档

### Swagger UI / Swagger界面

Access after starting backend:
```
http://localhost:8080/api/doc.html
```

Features:
- 300+ RESTful APIs
- Request/Response examples
- Online testing
- Export to Postman

### API Examples / API示例

**Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

**Get Customer List:**
```bash
curl -X POST http://localhost:8080/api/customer/list \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"pageNum":1,"pageSize":10}'
```

---

## 🧪 Testing / 测试

### Backend Tests / 后端测试

```bash
# Run all tests
mvn test

# Run specific module tests
mvn test -pl crm-modules/module-customer

# Generate coverage report
mvn jacoco:report
```

### Frontend Tests / 前端测试

```bash
cd crm-frontend

# Unit tests
npm run test

# E2E tests
npm run test:e2e

# Lint check
npm run lint
```

---

## 📊 Performance / 性能

### Benchmarks / 性能指标

- **API Response Time**: < 500ms (P95)
- **Concurrent Users**: 200+
- **Database Queries**: Optimized with indexes
- **Cache Hit Rate**: > 80% (Redis)

### Optimization Tips / 优化建议

1. Enable read-write separation for MySQL
2. Use Redis cluster for high availability
3. Implement CDN for static assets
4. Enable GZIP compression
5. Use connection pooling (HikariCP configured)

---

## 🔒 Security / 安全

### Security Features / 安全特性

- ✅ JWT Authentication
- ✅ RBAC Authorization
- ✅ Data Scope Control (5 levels)
- ✅ Password BCrypt Encryption
- ✅ Operation Audit Logs
- ✅ SQL Injection Prevention (Prepared Statements)
- ✅ XSS Protection (Input Sanitization)
- ✅ CORS Configuration
- ✅ HTTPS Ready

### Security Checklist / 安全检查清单

Before production deployment:
- [ ] Change default passwords
- [ ] Update JWT secret key
- [ ] Enable HTTPS
- [ ] Configure firewall rules
- [ ] Set up backup strategy
- [ ] Enable rate limiting
- [ ] Review security headers

---

## 🤝 Contributing / 贡献指南

We welcome contributions! Please follow these steps:

1. **Fork** the repository
2. **Create** your feature branch (`git checkout -b feature/AmazingFeature`)
3. **Commit** your changes (`git commit -m 'feat: add some amazing feature'`)
4. **Push** to the branch (`git push origin feature/AmazingFeature`)
5. **Open** a Pull Request

### Commit Convention / 提交规范

Use [Conventional Commits](https://www.conventionalcommits.org/):

```
feat: new feature
fix: bug fix
docs: documentation
style: formatting
refactor: code refactoring
test: adding tests
chore: maintenance
```

---

## 📝 License / 许可证

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👥 Team / 团队

Developed with ❤️ using AI-assisted multi-agent collaboration.

**Architecture Design**: Lingma AI Assistant  
**Backend Development**: Multi-Agent System  
**Frontend Development**: Multi-Agent System  

---

## 📞 Support / 支持

- 📧 Email: support@yourcompany.com
- 🐛 Issues: [GitHub Issues](https://github.com/YOUR_USERNAME/foreign-trade-crm/issues)
- 📖 Documentation: [Wiki](https://github.com/YOUR_USERNAME/foreign-trade-crm/wiki)

---

## 🌟 Star History / 星标历史

If this project helps you, please give it a star! ⭐

[![Star History Chart](https://api.star-history.com/svg?repos=YOUR_USERNAME/foreign-trade-crm&type=Date)](https://star-history.com/#YOUR_USERNAME/foreign-trade-crm&Date)

---

<div align="center">

**Made with ❤️ by Foreign Trade CRM Team**

[⬆ Back to Top](#foreign-trade-crm-system---外贸crm系统)

</div>
