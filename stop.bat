@echo off
chcp 65001 >nul
echo ========================================
echo   外贸CRM系统 - 停止服务
echo   Foreign Trade CRM Stop Services
echo ========================================
echo.

echo [1/2] 停止所有Docker容器...
docker-compose down
if %ERRORLEVEL% EQU 0 (
    echo [成功] 所有服务已停止
) else (
    echo [错误] 停止服务失败
    pause
    exit /b 1
)

echo.
echo [2/2] 清理未使用的Docker资源(可选)...
set /p CLEAN="是否清理未使用的Docker镜像和卷? (y/n): "
if /i "%CLEAN%"=="y" (
    docker system prune -f
    echo [成功] Docker资源清理完成
)

echo.
echo ========================================
echo   服务已停止
echo ========================================
echo.
pause
