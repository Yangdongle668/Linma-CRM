@echo off
chcp 65001 >nul
echo ========================================
echo   外贸CRM系统 - 快速启动脚本
echo   Foreign Trade CRM Quick Start
echo ========================================
echo.

REM 检查Docker是否安装
where docker >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [错误] Docker未安装,请先安装Docker Desktop
    echo 下载地址: https://www.docker.com/products/docker-desktop
    pause
    exit /b 1
)

echo [1/4] 检查Docker服务...
docker info >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [错误] Docker服务未启动,请启动Docker Desktop
    pause
    exit /b 1
)
echo [成功] Docker服务运行正常

echo.
echo [2/4] 创建数据目录...
if not exist "data\mysql" mkdir data\mysql
if not exist "data\redis" mkdir data\redis
if not exist "data\rabbitmq" mkdir data\rabbitmq
if not exist "data\elasticsearch" mkdir data\elasticsearch
if not exist "data\minio" mkdir data\minio
if not exist "logs" mkdir logs
echo [成功] 数据目录创建完成

echo.
echo [3/4] 启动基础设施服务(MySQL、Redis、RabbitMQ、ES、MinIO)...
docker-compose up -d mysql redis rabbitmq elasticsearch minio
if %ERRORLEVEL% NEQ 0 (
    echo [错误] 服务启动失败,请检查docker-compose.yml配置
    pause
    exit /b 1
)
echo [成功] 基础设施服务启动完成

echo.
echo [4/4] 等待数据库初始化...(约30秒)
timeout /t 30 /nobreak >nul

echo.
echo ========================================
echo   服务启动成功!
echo ========================================
echo.
echo 访问地址:
echo   - Swagger文档: http://localhost:8080/api/doc.html
echo   - MinIO控制台: http://localhost:9001 (minioadmin/minio123456)
echo   - RabbitMQ管理: http://localhost:15672 (admin/admin123456)
echo   - MySQL: localhost:3306 (root/root123456)
echo   - Redis: localhost:6379 (密码: redis123456)
echo.
echo 默认管理员账号:
echo   用户名: admin
echo   密码: admin123
echo.
echo 查看日志: docker-compose logs -f backend
echo 停止服务: docker-compose down
echo.
pause
