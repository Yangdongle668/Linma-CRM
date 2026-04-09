# GitHub 部署指南

本文档详细说明如何将外贸CRM系统上传到GitHub并配置自动部署。

## 📋 目录

1. [准备GitHub仓库](#1-准备github仓库)
2. [上传代码](#2-上传代码)
3. [配置Secrets](#3-配置secrets)
4. [配置CI/CD](#4-配置cicd)
5. [一键部署脚本](#5-一键部署脚本)
6. [生产环境部署](#6-生产环境部署)

---

## 1. 准备GitHub仓库

### 1.1 创建新仓库

1. 访问 https://github.com/new
2. 填写仓库信息:
   - **Repository name**: `Linma-CRM`
   - **Description**: Linma CRM - Foreign Trade Customer Management System
   - **Visibility**: Public (公开) 或 Private (私有)
   - ✅ Initialize with README (不要勾选,我们已有README)

3. 点击 "Create repository"

### 1.2 添加.gitignore

仓库已包含 `.gitignore` 文件,无需额外配置。

---

## 2. 上传代码

### 方式一: 使用Git命令(推荐)

```bash
# 进入项目目录
cd C:\Users\Yang\Desktop\lima\foreign-trade-crm

# 初始化Git仓库
git init

# 添加所有文件
git add .

# 首次提交
git commit -m "feat: initial commit - complete CRM system"

# 添加远程仓库(替换YOUR_USERNAME为你的GitHub用户名)
git remote add origin https://github.com/Yangdongle668/Linma-CRM.git

# 推送到GitHub
git branch -M main
git push -u origin main
```

### 方式二: 使用GitHub Desktop

1. 下载并安装 [GitHub Desktop](https://desktop.github.com/)
2. File → Add Local Repository → 选择项目文件夹
3. Commit all changes
4. Publish repository to GitHub

### 方式三: 直接上传ZIP

1. 压缩整个项目文件夹
2. 在GitHub仓库页面点击 "Add file" → "Upload files"
3. 拖拽ZIP文件上传
4. Commit changes

---

## 3. 配置Secrets

在GitHub仓库中配置以下Secrets(用于CI/CD):

### 3.1 进入Settings

1. 访问你的仓库: `https://github.com/Yangdongle668/Linma-CRM`
2. 点击 **Settings** 标签
3. 左侧菜单选择 **Secrets and variables** → **Actions**

### 3.2 添加Secrets

点击 "New repository secret",依次添加:

#### Docker Hub认证(如需推送Docker镜像)
```
Name: DOCKER_USERNAME
Value: your-dockerhub-username

Name: DOCKER_PASSWORD
Value: your-dockerhub-password-or-token
```

#### 生产服务器SSH(如需自动部署)
```
Name: SERVER_HOST
Value: your-server-ip-or-domain

Name: SERVER_USER
Value: deploy-user

Name: SERVER_SSH_KEY
Value: |
  -----BEGIN OPENSSH PRIVATE KEY-----
  your-private-ssh-key-here
  -----END OPENSSH PRIVATE KEY-----
```

#### SonarQube(如需代码质量检查)
```
Name: SONAR_TOKEN
Value: your-sonarqube-token

Name: SONAR_HOST_URL
Value: https://sonarcloud.io
```

#### 企业微信通知(可选)
```
Name: WECOM_WEBHOOK
Value: https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=your-key
```

---

## 4. 配置CI/CD

项目已包含GitHub Actions配置文件: `.github/workflows/ci-cd.yml`

### 4.1 CI/CD流程

```
Push to main/develop
        ↓
┌───────────────────┐
│  Backend Build    │ ← Maven编译+测试
└─────────┬─────────┘
          │
┌─────────▼─────────┐
│ Frontend Build    │ ← NPM构建+Lint
└─────────┬─────────┘
          │
┌─────────▼─────────┐
│  Docker Build     │ ← 构建Docker镜像
└─────────┬─────────┘
          │
┌─────────▼─────────┐
│   Deploy to Prod  │ ← SSH部署到服务器
└─────────┬─────────┘
          │
┌─────────▼─────────┐
│   Notification    │ ← 发送通知
└───────────────────┘
```

### 4.2 触发条件

- **Push** 到 `main` 或 `develop` 分支
- **Pull Request** 到 `main` 分支

### 4.3 查看执行结果

访问: `https://github.com/Yangdongle668/Linma-CRM/actions`

---

## 5. 一键部署脚本

### 5.1 Linux/Mac部署

项目根目录的 `deploy.sh` 脚本提供一键部署:

```bash
# 赋予执行权限
chmod +x deploy.sh

# 运行部署
./deploy.sh
```

### 5.2 Windows部署

双击运行 `start.bat` 或使用PowerShell:

```powershell
.\deploy.ps1
```

### 5.3 部署内容

脚本会自动:
1. ✅ 检查Docker环境
2. ✅ 创建数据目录
3. ✅ 停止旧容器
4. ✅ 启动新容器(MySQL、Redis、RabbitMQ、ES、MinIO、Backend)
5. ✅ 等待数据库初始化
6. ✅ 显示访问地址和账号信息

---

## 6. 生产环境部署

### 6.1 准备工作

#### 修改生产配置

编辑 `docker-compose.prod.yml`:

```yaml
# 修改密码为强密码
environment:
  DB_PASSWORD: YourStrongPassword123!
  REDIS_PASSWORD: YourStrongRedisPass!
  JWT_SECRET: VeryLongRandomStringHere...
```

#### 配置HTTPS(推荐)

1. 申请SSL证书(Let's Encrypt免费)
2. 放置证书到 `nginx/ssl/` 目录
3. 取消注释 `docker-compose.prod.yml` 中的nginx服务

### 6.2 部署步骤

```bash
# 1. 克隆仓库
git clone https://github.com/YOUR_USERNAME/foreign-trade-crm.git
cd foreign-trade-crm

# 2. 创建.env文件
cat > .env << EOF
DB_PASSWORD=YourStrongPassword123!
REDIS_PASSWORD=YourStrongRedisPass!
RABBITMQ_USER=crm_admin
RABBITMQ_PASSWORD=YourRabbitMQPass!
MINIO_USER=minioadmin
MINIO_PASSWORD=YourMinioPass!
JWT_SECRET=VeryLongRandomStringAtLeast32Characters!
EOF

# 3. 启动服务
docker-compose -f docker-compose.prod.yml up -d

# 4. 查看日志
docker-compose -f docker-compose.prod.yml logs -f backend

# 5. 检查健康状态
curl http://localhost:8080/api/actuator/health
```

### 6.3 更新部署

```bash
# 拉取最新代码
git pull origin main

# 重新构建并启动
docker-compose -f docker-compose.prod.yml down
docker-compose -f docker-compose.prod.yml build --no-cache
docker-compose -f docker-compose.prod.yml up -d

# 清理旧镜像
docker image prune -f
```

### 6.4 备份策略

#### 数据库备份

```bash
# 创建备份脚本 backup.sh
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
docker exec crm-mysql mysqldump -u root -p${DB_PASSWORD} crm_db > backup/crm_db_${DATE}.sql

# 保留最近7天的备份
find backup/ -name "*.sql" -mtime +7 -delete
```

#### 定时备份(Crontab)

```bash
# 编辑crontab
crontab -e

# 每天凌晨2点备份
0 2 * * * /path/to/backup.sh
```

---

## 🔧 常见问题

### Q1: Docker启动失败?

```bash
# 检查Docker状态
docker info

# 查看容器日志
docker-compose logs -f mysql

# 重启服务
docker-compose restart
```

### Q2: 数据库连接失败?

```bash
# 检查MySQL是否就绪
docker exec crm-mysql mysqladmin ping

# 查看MySQL日志
docker-compose logs mysql
```

### Q3: 前端无法访问后端?

检查 `crm-frontend/.env.development`:
```env
VITE_APP_BASE_API=http://localhost:8080/api
```

确保后端CORS配置正确。

### Q4: 如何重置管理员密码?

```sql
-- 连接到MySQL
docker exec -it crm-mysql mysql -u root -p

-- 重置密码为 admin123
USE crm_db;
UPDATE sys_user SET password='$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2' WHERE username='admin';
```

---

## 📊 监控和维护

### 查看服务状态

```bash
# 所有容器状态
docker-compose ps

# 资源使用情况
docker stats

# 磁盘使用
docker system df
```

### 日志查看

```bash
# 实时日志
docker-compose logs -f backend

# 最近100行
docker-compose logs --tail=100 backend

# 导出日志
docker-compose logs backend > backend.log
```

### 性能优化

```bash
# 清理未使用的资源
docker system prune -a

# 限制容器资源(已在docker-compose.prod.yml配置)
# CPU: 2 cores
# Memory: 2GB per service
```

---

## 🎯 下一步

1. ✅ 上传代码到GitHub
2. ✅ 配置CI/CD Secrets
3. ✅ 测试自动化部署
4. ✅ 配置生产环境
5. ✅ 设置监控告警
6. ✅ 编写运维文档

---

**祝部署顺利!** 🚀

如有问题,请查看:
- GitHub Issues: https://github.com/Yangdongle668/Linma-CRM/issues
- 项目文档: README.md
