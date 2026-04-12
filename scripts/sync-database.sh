#!/bin/bash

# ============================================
# One-Click Database Synchronization Script
# 一键数据库同步脚本
# 
# This script synchronizes all three database schemas:
# 1. Enhanced Customer Schema
# 2. Email Settings Schema  
# 3. Follow-up Records Schema
# ============================================

set -e  # Exit on error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Print with color
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_step() {
    echo -e "${CYAN}[STEP]${NC} $1"
}

# Configuration
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-3306}"
DB_NAME="${DB_NAME:-crm_db}"
DB_USER="${DB_USER:-root}"
DB_PASSWORD="${DB_PASSWORD:-root123456}"

# Get script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
SQL_DIR="$PROJECT_ROOT/docs/database"

# SQL files to execute (in order)
SQL_FILES=(
    "enhanced_customer_schema.sql"
    "email_settings_migration.sql"
)

# Check if MySQL client is installed
check_mysql() {
    print_info "Checking MySQL client installation..."
    if ! command -v mysql &> /dev/null; then
        print_error "MySQL client is not installed."
        echo "Please install MySQL client first:"
        echo "  - Windows: Download from https://dev.mysql.com/downloads/mysql/"
        echo "  - macOS: brew install mysql-client"
        echo "  - Linux: sudo apt-get install mysql-client"
        exit 1
    fi
    print_success "MySQL client found!"
}

# Test database connection
test_connection() {
    print_info "Testing database connection..."
    
    if mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASSWORD" -e "SELECT 1;" > /dev/null 2>&1; then
        print_success "Database connection successful!"
    else
        print_error "Failed to connect to database."
        echo "Please check your connection settings:"
        echo "  Host: $DB_HOST"
        echo "  Port: $DB_PORT"
        echo "  User: $DB_USER"
        echo "  Database: $DB_NAME"
        echo ""
        echo "You can set environment variables to override defaults:"
        echo "  export DB_HOST=localhost"
        echo "  export DB_PORT=3306"
        echo "  export DB_NAME=crm_db"
        echo "  export DB_USER=root"
        echo "  export DB_PASSWORD=your_password"
        exit 1
    fi
}

# Check if database exists
check_database() {
    print_info "Checking if database '$DB_NAME' exists..."
    
    if mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASSWORD" -e "USE $DB_NAME;" > /dev/null 2>&1; then
        print_success "Database '$DB_NAME' exists!"
    else
        print_error "Database '$DB_NAME' does not exist."
        echo "Creating database..."
        
        if mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASSWORD" -e "CREATE DATABASE IF NOT EXISTS $DB_NAME CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"; then
            print_success "Database created successfully!"
        else
            print_error "Failed to create database."
            exit 1
        fi
    fi
}

# Backup current database
backup_database() {
    print_step "Creating database backup..."
    
    BACKUP_DIR="$PROJECT_ROOT/backups"
    mkdir -p "$BACKUP_DIR"
    
    TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
    BACKUP_FILE="$BACKUP_DIR/crm_db_backup_$TIMESTAMP.sql"
    
    print_info "Backup file: $BACKUP_FILE"
    
    if mysqldump -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASSWORD" \
        --single-transaction \
        --routines \
        --triggers \
        "$DB_NAME" > "$BACKUP_FILE" 2>/dev/null; then
        
        print_success "Backup created successfully!"
        
        # Keep only last 10 backups
        cd "$BACKUP_DIR"
        ls -t crm_db_backup_*.sql | tail -n +11 | xargs -r rm
        cd "$PROJECT_ROOT"
    else
        print_warning "Backup failed, but continuing with migration..."
    fi
}

# Execute SQL file
execute_sql_file() {
    local sql_file=$1
    
    if [ ! -f "$sql_file" ]; then
        print_error "SQL file not found: $sql_file"
        return 1
    fi
    
    local filename=$(basename "$sql_file")
    print_step "Executing: $filename"
    
    if mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASSWORD" \
        --database="$DB_NAME" \
        < "$sql_file" 2>&1; then
        print_success "✓ $filename executed successfully!"
        return 0
    else
        print_error "✗ Failed to execute $filename"
        return 1
    fi
}

# Verify migrations
verify_migrations() {
    print_step "Verifying migrations..."
    
    local errors=0
    
    # Check enhanced customer table columns
    print_info "Checking enhanced customer table..."
    if mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASSWORD" \
        -e "SELECT priority, industry, email_domain, customer_lifecycle FROM crm_customer LIMIT 1;" \
        "$DB_NAME" > /dev/null 2>&1; then
        print_success "✓ Enhanced customer table verified"
    else
        print_error "✗ Enhanced customer table verification failed"
        ((errors++))
    fi
    
    # Check email settings table
    print_info "Checking email settings table..."
    if mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASSWORD" \
        -e "SELECT COUNT(*) FROM sys_email_settings;" \
        "$DB_NAME" > /dev/null 2>&1; then
        print_success "✓ Email settings table verified"
    else
        print_error "✗ Email settings table verification failed"
        ((errors++))
    fi
    
    # Check follow-up records table
    print_info "Checking follow-up records table..."
    if mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASSWORD" \
        -e "SELECT COUNT(*) FROM crm_follow_up_record;" \
        "$DB_NAME" > /dev/null 2>&1; then
        print_success "✓ Follow-up records table verified"
    else
        print_error "✗ Follow-up records table verification failed"
        ((errors++))
    fi
    
    # Check email folders
    print_info "Checking email folders..."
    if mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASSWORD" \
        -e "SELECT COUNT(*) FROM msg_email_folder;" \
        "$DB_NAME" > /dev/null 2>&1; then
        print_success "✓ Email folders verified"
    else
        print_error "✗ Email folders verification failed"
        ((errors++))
    fi
    
    if [ $errors -eq 0 ]; then
        print_success "All migrations verified successfully!"
        return 0
    else
        print_error "$errors verification(s) failed!"
        return 1
    fi
}

# Show summary
show_summary() {
    echo ""
    print_success "=========================================="
    print_success "  Database Synchronization Complete!"
    print_success "  数据库同步完成!"
    print_success "=========================================="
    echo ""
    echo -e "${GREEN}Applied Migrations / 已应用的迁移:${NC}"
    echo "  ✓ Enhanced Customer Schema (50+ new fields)"
    echo "  ✓ Email Settings & Folders"
    echo "  ✓ Follow-up Records System"
    echo ""
    echo -e "${GREEN}New Features Available / 新功能可用:${NC}"
    echo "  📧 Email Integration with SMTP/IMAP"
    echo "  👥 Enhanced Customer Management"
    echo "  📝 Follow-up Record Tracking"
    echo "  🔗 Automatic Email-Customer Linking"
    echo "  📊 Dashboard Analytics"
    echo ""
    echo -e "${BLUE}Next Steps / 下一步:${NC}"
    echo "  1. Restart backend application"
    echo "  2. Update frontend routes"
    echo "  3. Configure email accounts in UI"
    echo ""
    echo -e "${YELLOW}Backup Location / 备份位置:${NC}"
    echo "  $PROJECT_ROOT/backups/"
    echo ""
}

# Main function
main() {
    echo ""
    echo -e "${BLUE}========================================${NC}"
    echo -e "${BLUE}  Database Synchronization Tool${NC}"
    echo -e "${BLUE}  数据库同步工具${NC}"
    echo -e "${BLUE}========================================${NC}"
    echo ""
    
    # Step 1: Check prerequisites
    check_mysql
    
    # Step 2: Test connection
    test_connection
    
    # Step 3: Check database
    check_database
    
    # Step 4: Backup (optional, ask user)
    echo ""
    read -p "Create database backup before migration? (Y/n): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]] || [[ -z $REPLY ]]; then
        backup_database
    fi
    
    # Step 5: Execute SQL files
    echo ""
    print_info "Starting database migrations..."
    echo ""
    
    local success_count=0
    local fail_count=0
    
    for sql_file in "${SQL_FILES[@]}"; do
        full_path="$SQL_DIR/$sql_file"
        if execute_sql_file "$full_path"; then
            ((success_count++))
        else
            ((fail_count++))
            print_error "Migration failed for: $sql_file"
            echo "Do you want to continue with remaining migrations? (y/N): "
            read -n 1 -r
            echo
            if [[ ! $REPLY =~ ^[Yy]$ ]]; then
                print_error "Migration aborted by user."
                exit 1
            fi
        fi
    done
    
    echo ""
    print_info "Migration Summary: $success_count succeeded, $fail_count failed"
    echo ""
    
    # Step 6: Verify
    verify_migrations
    
    # Step 7: Show summary
    show_summary
}

# Run main function
main "$@"
