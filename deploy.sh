#!/bin/bash

# ============================================
# Foreign Trade CRM - One-Click Deploy Script
# 外贸CRM系统 - 一键部署脚本
# Repository: https://github.com/Yangdongle668/Linma-CRM
# ============================================

set -e  # Exit on error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
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

# Check if Docker is installed
check_docker() {
    print_info "Checking Docker installation..."
    if ! command -v docker &> /dev/null; then
        print_error "Docker is not installed. Please install Docker first."
        echo "Download from: https://www.docker.com/products/docker-desktop"
        exit 1
    fi

    # Check for Docker Compose (support both v1 and v2)
    # v2 uses 'docker compose' (plugin), v1 uses 'docker-compose' (standalone)
    COMPOSE_VERSION=""
    if docker compose version &> /dev/null; then
        COMPOSE_VERSION="v2"
        print_success "Docker Compose v2 (plugin) detected"
    elif command -v docker-compose &> /dev/null; then
        COMPOSE_VERSION="v1"
        print_success "Docker Compose v1 (standalone) detected"
    else
        print_error "Docker Compose is not installed."
        echo "For Docker Compose v2: docker compose plugin"
        echo "For Docker Compose v1: sudo apt-get install docker-compose"
        exit 1
    fi

    # Check if Docker daemon is running
    if ! docker info &> /dev/null; then
        print_error "Docker daemon is not running. Please start Docker Desktop."
        exit 1
    fi

    print_success "Docker is ready!"
}

# Docker Compose command wrapper (supports both v1 and v2)
docker_compose() {
    if [ "$COMPOSE_VERSION" = "v2" ]; then
        docker compose "$@"
    else
        docker-compose "$@"
    fi
}

# Create necessary directories
create_directories() {
    print_info "Creating data directories..."
    mkdir -p data/mysql
    mkdir -p data/redis
    mkdir -p data/rabbitmq
    mkdir -p data/elasticsearch
    mkdir -p data/minio
    mkdir -p logs

    print_success "Directories created!"
}

# Verify database migration files exist
verify_database_files() {
    print_info "Verifying database migration files..."
    
    local db_files=(
        "docs/database/schema.sql"
        "docs/database/email_settings_migration.sql"
        "docs/database/enhanced_customer_schema.sql"
    )
    
    local all_exist=true
    for file in "${db_files[@]}"; do
        if [ -f "$file" ]; then
            print_success "✓ Found: $file"
        else
            print_error "✗ Missing: $file"
            all_exist=false
        fi
    done
    
    if [ "$all_exist" = false ]; then
        print_error "Some database migration files are missing!"
        exit 1
    fi
    
    print_success "All database migration files verified!"
}

# Stop existing containers
stop_existing() {
    print_info "Stopping existing containers..."
    docker_compose down 2>/dev/null || true
    print_success "Existing containers stopped!"
}

# Pull latest images (optional)
pull_images() {
    print_info "Pulling latest Docker images..."
    docker_compose pull || print_warning "Some images may not be available in remote registry"
}

# Pull latest code from GitHub
pull_code() {
    print_info "Checking for latest code from GitHub..."
    
    # Check if git is installed
    if ! command -v git &> /dev/null; then
        print_warning "Git is not installed. Skipping code update."
        return
    fi
    
    # Check if this is a git repository
    if [ ! -d ".git" ]; then
        print_warning "Not a git repository. Skipping code update."
        print_info "To clone from GitHub, run:"
        echo "  git clone https://github.com/Yangdongle668/Linma-CRM.git"
        return
    fi
    
    # Fetch and pull latest changes
    if git fetch origin main &> /dev/null; then
        LOCAL=$(git rev-parse HEAD)
        REMOTE=$(git rev-parse origin/main)
        
        if [ "$LOCAL" != "$REMOTE" ]; then
            print_info "New version available on GitHub. Pulling updates..."
            git pull origin main
            print_success "Code updated successfully!"
        else
            print_success "Already running the latest version."
        fi
    else
        print_warning "Could not check for updates. Continuing with current version."
    fi
}

# Build and start services
start_services() {
    print_info "Starting services with Docker Compose..."
    docker_compose up -d

    print_success "All services started!"
}

# Wait for database initialization
wait_for_database() {
    print_info "Waiting for database initialization (30 seconds)..."
    sleep 30

    # Check if MySQL is ready
    if docker_compose exec -T mysql mysqladmin ping -h localhost --silent 2>/dev/null; then
        print_success "Database is ready!"
        
        # Verify all database migration files were executed
        print_info "Verifying database migrations..."
        local migrations=("schema.sql" "email_settings_migration.sql" "enhanced_customer_schema.sql")
        for migration in "${migrations[@]}"; do
            if docker_compose exec -T mysql mysql -uroot -proot123456 crm_db -e "SELECT 'Migration $migration executed successfully';" 2>/dev/null; then
                print_success "✓ Database schema verified: $migration"
            else
                print_warning "⚠ Migration may not have executed: $migration"
            fi
        done
    else
        print_warning "Database may still be initializing. Please check logs."
    fi
}

# Show service status
show_status() {
    print_info "Service Status:"
    echo ""
    docker_compose ps
    echo ""
}

# Show access information
show_access_info() {
    echo ""
    print_success "=========================================="
    print_success "  Deployment Complete! 部署完成!"
    print_success "=========================================="
    echo ""
    echo -e "${GREEN}Access URLs / 访问地址:${NC}"
    echo "  📊 Swagger Docs:    http://localhost:8080/api/doc.html"
    echo "  🌐 Frontend:        http://localhost:80 (after frontend deployment)"
    echo "  💾 MinIO Console:   http://localhost:9001"
    echo "  📨 RabbitMQ:        http://localhost:15672"
    echo "  🗄️  MySQL:           localhost:3306"
    echo "  🔴 Redis:            localhost:6379"
    echo ""
    echo -e "${GREEN}Default Credentials / 默认账号:${NC}"
    echo "  👤 Username: admin"
    echo "  🔑 Password: admin123"
    echo ""
    echo -e "${YELLOW}⚠️  IMPORTANT: Please change the password after first login!${NC}"
    echo ""
    echo -e "${BLUE}Useful Commands / 常用命令:${NC}"
    echo "  View logs:     docker_compose logs -f backend"
    echo "  Stop services: docker_compose down"
    echo "  Restart:       docker_compose restart"
    echo ""
}

# Main deployment function
main() {
    echo ""
    echo -e "${BLUE}========================================${NC}"
    echo -e "${BLUE}  Foreign Trade CRM Deployment Script${NC}"
    echo -e "${BLUE}  外贸CRM系统 - 一键部署脚本${NC}"
    echo -e "${BLUE}========================================${NC}"
    echo ""

    # Check if running as root (not recommended)
    if [ "$EUID" -eq 0 ]; then
        print_warning "Running as root is not recommended. Consider using a regular user with sudo."
    fi

    # Step 1: Check Docker
    check_docker

    # Step 2: Pull latest code from GitHub (optional)
    # Uncomment the next line to enable auto-update from GitHub
    # pull_code

    # Step 3: Create directories
    create_directories

    # Step 4: Verify database migration files
    verify_database_files

    # Step 5: Stop existing containers
    stop_existing

    # Step 6: Pull images (optional, comment out if not needed)
    # pull_images

    # Step 7: Start services
    start_services

    # Step 8: Wait for database
    wait_for_database

    # Step 9: Show status
    show_status

    # Step 8: Show access info
    show_access_info

    print_success "Deployment script completed successfully!"
}

# Run main function
main "$@"
