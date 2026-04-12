@echo off
REM ============================================
REM One-Click Database Synchronization Script (Windows)
REM 一键数据库同步脚本 (Windows版本)
REM 
REM This script synchronizes all database schemas
REM ============================================

setlocal enabledelayedexpansion

:: Configuration
set DB_HOST=%DB_HOST:localhost%
set DB_PORT=%DB_PORT:3306%
set DB_NAME=%DB_NAME:crm_db%
set DB_USER=%DB_USER:root%
set DB_PASSWORD=%DB_PASSWORD:root123456%

:: Get script directory
set SCRIPT_DIR=%~dp0
set PROJECT_ROOT=%SCRIPT_DIR%..
set SQL_DIR=%PROJECT_ROOT%\docs\database

echo.
echo ========================================
echo   Database Synchronization Tool
echo   数据库同步工具
echo ========================================
echo.

:: Check if MySQL client is installed
echo [INFO] Checking MySQL client installation...
where mysql >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] MySQL client is not installed.
    echo Please install MySQL from: https://dev.mysql.com/downloads/mysql/
    pause
    exit /b 1
)
echo [SUCCESS] MySQL client found!
echo.

:: Test database connection
echo [INFO] Testing database connection...
mysql -h %DB_HOST% -P %DB_PORT% -u %DB_USER% -p%DB_PASSWORD% -e "SELECT 1;" >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Failed to connect to database.
    echo Please check your connection settings:
    echo   Host: %DB_HOST%
    echo   Port: %DB_PORT%
    echo   User: %DB_USER%
    echo   Database: %DB_NAME%
    echo.
    echo You can set environment variables to override defaults:
    echo   set DB_HOST=localhost
    echo   set DB_PORT=3306
    echo   set DB_NAME=crm_db
    echo   set DB_USER=root
    echo   set DB_PASSWORD=your_password
    pause
    exit /b 1
)
echo [SUCCESS] Database connection successful!
echo.

:: Check if database exists
echo [INFO] Checking if database '%DB_NAME%' exists...
mysql -h %DB_HOST% -P %DB_PORT% -u %DB_USER% -p%DB_PASSWORD% -e "USE %DB_NAME%;" >nul 2>&1
if %errorlevel% neq 0 (
    echo [WARNING] Database '%DB_NAME%' does not exist. Creating...
    mysql -h %DB_HOST% -P %DB_PORT% -u %DB_USER% -p%DB_PASSWORD% -e "CREATE DATABASE IF NOT EXISTS %DB_NAME% CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
    if %errorlevel% neq 0 (
        echo [ERROR] Failed to create database.
        pause
        exit /b 1
    )
    echo [SUCCESS] Database created successfully!
) else (
    echo [SUCCESS] Database '%DB_NAME%' exists!
)
echo.

:: Backup current database
echo [STEP] Creating database backup...
set BACKUP_DIR=%PROJECT_ROOT%\backups
if not exist "%BACKUP_DIR%" mkdir "%BACKUP_DIR%"

:: Generate timestamp
for /f "tokens=2 delims==" %%I in ('wmic os get localdatetime /value') do set datetime=%%I
set TIMESTAMP=%datetime:~0,8%_%datetime:~8,6%
set BACKUP_FILE=%BACKUP_DIR%\crm_db_backup_%TIMESTAMP%.sql

echo [INFO] Backup file: %BACKUP_FILE%

mysqldump -h %DB_HOST% -P %DB_PORT% -u %DB_USER% -p%DB_PASSWORD% ^
    --single-transaction ^
    --routines ^
    --triggers ^
    %DB_NAME% > "%BACKUP_FILE%" 2>nul

if %errorlevel% equ 0 (
    echo [SUCCESS] Backup created successfully!
) else (
    echo [WARNING] Backup failed, but continuing with migration...
)
echo.

:: Execute SQL files
echo [INFO] Starting database migrations...
echo.

set SUCCESS_COUNT=0
set FAIL_COUNT=0

for %%F in (
    "enhanced_customer_schema.sql"
    "email_settings_migration.sql"
) do (
    set SQL_FILE=%SQL_DIR%\%%~F
    echo [STEP] Executing: %%~F
    
    if exist "!SQL_FILE!" (
        mysql -h %DB_HOST% -P %DB_PORT% -u %DB_USER% -p%DB_PASSWORD% ^
            --database=%DB_NAME% ^
            < "!SQL_FILE!" 2>&1
        
        if !errorlevel! equ 0 (
            echo [SUCCESS] ^✓ %%~F executed successfully!
            set /a SUCCESS_COUNT+=1
        ) else (
            echo [ERROR] ^✗ Failed to execute %%~F
            set /a FAIL_COUNT+=1
            
            choice /M "Do you want to continue with remaining migrations"
            if errorlevel 2 (
                echo [ERROR] Migration aborted by user.
                pause
                exit /b 1
            )
        )
    ) else (
        echo [ERROR] SQL file not found: !SQL_FILE!
        set /a FAIL_COUNT+=1
    )
    echo.
)

echo [INFO] Migration Summary: %SUCCESS_COUNT% succeeded, %FAIL_COUNT% failed
echo.

:: Verify migrations
echo [STEP] Verifying migrations...

echo [INFO] Checking enhanced customer table...
mysql -h %DB_HOST% -P %DB_PORT% -u %DB_USER% -p%DB_PASSWORD% ^
    -e "SELECT priority, industry, email_domain, customer_lifecycle FROM crm_customer LIMIT 1;" ^
    %DB_NAME% >nul 2>&1
if %errorlevel% equ 0 (
    echo [SUCCESS] ^✓ Enhanced customer table verified
) else (
    echo [ERROR] ^✗ Enhanced customer table verification failed
)

echo [INFO] Checking email settings table...
mysql -h %DB_HOST% -P %DB_PORT% -u %DB_USER% -p%DB_PASSWORD% ^
    -e "SELECT COUNT(*) FROM sys_email_settings;" ^
    %DB_NAME% >nul 2>&1
if %errorlevel% equ 0 (
    echo [SUCCESS] ^✓ Email settings table verified
) else (
    echo [ERROR] ^✗ Email settings table verification failed
)

echo [INFO] Checking follow-up records table...
mysql -h %DB_HOST% -P %DB_PORT% -u %DB_USER% -p%DB_PASSWORD% ^
    -e "SELECT COUNT(*) FROM crm_follow_up_record;" ^
    %DB_NAME% >nul 2>&1
if %errorlevel% equ 0 (
    echo [SUCCESS] ^✓ Follow-up records table verified
) else (
    echo [ERROR] ^✗ Follow-up records table verification failed
)

echo [INFO] Checking email folders...
mysql -h %DB_HOST% -P %DB_PORT% -u %DB_USER% -p%DB_PASSWORD% ^
    -e "SELECT COUNT(*) FROM msg_email_folder;" ^
    %DB_NAME% >nul 2>&1
if %errorlevel% equ 0 (
    echo [SUCCESS] ^✓ Email folders verified
) else (
    echo [ERROR] ^✗ Email folders verification failed
)

echo.
echo ========================================
echo   Database Synchronization Complete!
echo   数据库同步完成!
echo ========================================
echo.
echo Applied Migrations / 已应用的迁移:
echo   ^✓ Enhanced Customer Schema (50+ new fields^)
echo   ^✓ Email Settings ^& Folders
echo   ^✓ Follow-up Records System
echo.
echo New Features Available / 新功能可用:
echo   [EMAIL] Email Integration with SMTP/IMAP
echo   [CUSTOMER] Enhanced Customer Management
echo   [FOLLOWUP] Follow-up Record Tracking
echo   [AUTO-LINK] Automatic Email-Customer Linking
echo   [DASHBOARD] Dashboard Analytics
echo.
echo Next Steps / 下一步:
echo   1. Restart backend application
echo   2. Update frontend routes
echo   3. Configure email accounts in UI
echo.
echo Backup Location / 备份位置:
echo   %BACKUP_DIR%
echo.

pause
