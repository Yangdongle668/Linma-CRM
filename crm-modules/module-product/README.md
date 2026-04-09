# 产品管理模块 (module-product)

## 模块概述
产品管理模块是外贸CRM系统的核心业务模块之一，负责管理产品信息、分类、价格体系和库存。

## 核心功能

### 1. 产品分类管理
- ✅ 多级分类树形结构
- ✅ 分类CRUD操作
- ✅ 分类编码唯一性校验
- ✅ 级联删除子分类

### 2. 产品管理
- ✅ 产品CRUD操作
- ✅ 产品编号自动生成(格式: PRD20260409001)
- ✅ SKU管理
- ✅ 产品图片管理
- ✅ 产品导入导出

### 3. 价格体系
- ✅ 成本价(CNY)
- ✅ 出厂价(CNY)
- ✅ FOB价格(USD)
- ✅ CIF价格(USD)
- ✅ 多币种自动换算(USD/EUR/GBP/JPY/CNY)

### 4. 库存管理
- ✅ 库存数量管理
- ✅ 库存增减操作
- ✅ 最小起订量(MOQ)

### 5. 其他信息
- ✅ HS编码
- ✅ 退税率
- ✅ 毛重/净重
- ✅ 包装尺寸

## 技术架构

### 依赖管理
- **Spring Boot**: 3.2.0
- **MyBatis Plus**: 3.5.5
- **EasyExcel**: 3.3.3 (Excel导出)
- **Hutool**: 5.8.24 (工具类)
- **Knife4j**: 4.3.0 (Swagger增强)

### 分层架构
```
Controller → Service → Mapper
```

### DTO/VO分离
- **DTO**: 数据传输对象，用于接收请求参数
- **VO**: 视图对象，用于返回响应数据

## API接口列表

### 产品分类接口
1. `GET /product/category/tree` - 获取分类树
2. `GET /product/category/children/{parentId}` - 获取子分类
3. `GET /product/category/list` - 查询分类列表
4. `GET /product/category/{id}` - 获取分类详情
5. `POST /product/category` - 创建分类
6. `PUT /product/category` - 更新分类
7. `DELETE /product/category/{id}` - 删除分类

### 产品接口
1. `GET /product/page` - 分页查询产品
2. `GET /product/{id}` - 获取产品详情
3. `POST /product` - 创建产品
4. `PUT /product` - 更新产品
5. `DELETE /product` - 删除产品
6. `GET /product/generate-no` - 生成产品编号
7. `GET /product/{id}/price` - 获取产品价格
8. `PUT /product/{id}/price` - 更新产品价格
9. `PUT /product/{id}/stock` - 更新库存
10. `POST /product/import` - 导入产品
11. `GET /product/export` - 导出产品
12. `GET /product/check-sku` - 检查SKU唯一性

## 数据库设计

### crm_product_category (产品分类表)
```sql
CREATE TABLE crm_product_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID',
    category_name_cn VARCHAR(100) NOT NULL COMMENT '分类名称(中文)',
    category_name_en VARCHAR(100) NOT NULL COMMENT '分类名称(英文)',
    category_code VARCHAR(50) COMMENT '分类编码',
    sort_order INT DEFAULT 0 COMMENT '显示顺序',
    level INT DEFAULT 1 COMMENT '分类层级',
    status TINYINT DEFAULT 1 COMMENT '状态(1正常 0禁用)',
    icon VARCHAR(100) COMMENT '图标',
    description TEXT COMMENT '描述',
    created_by BIGINT COMMENT '创建者',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新者',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_parent_id (parent_id),
    INDEX idx_category_code (category_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品分类表';
```

### crm_product (产品表)
```sql
CREATE TABLE crm_product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '产品ID',
    product_no VARCHAR(50) NOT NULL COMMENT '产品编号',
    product_name VARCHAR(200) NOT NULL COMMENT '产品名称(英文)',
    product_name_cn VARCHAR(200) COMMENT '产品名称(中文)',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    sku VARCHAR(50) COMMENT 'SKU编码',
    specification VARCHAR(200) COMMENT '规格型号',
    description TEXT COMMENT '产品描述',
    unit VARCHAR(20) NOT NULL COMMENT '单位',
    cost_price DECIMAL(15,4) COMMENT '成本价(CNY)',
    factory_price DECIMAL(15,4) COMMENT '出厂价(CNY)',
    fob_price DECIMAL(15,4) COMMENT 'FOB价格(USD)',
    cif_price DECIMAL(15,4) COMMENT 'CIF价格(USD)',
    currency VARCHAR(10) DEFAULT 'USD' COMMENT '币种',
    stock_quantity INT DEFAULT 0 COMMENT '库存数量',
    min_order_quantity INT DEFAULT 1 COMMENT '最小起订量',
    hs_code VARCHAR(20) COMMENT 'HS编码',
    tax_refund_rate DECIMAL(5,2) COMMENT '退税率(%)',
    gross_weight DECIMAL(10,3) COMMENT '毛重(KG)',
    net_weight DECIMAL(10,3) COMMENT '净重(KG)',
    packing_size VARCHAR(50) COMMENT '包装尺寸(长*宽*高 cm)',
    images TEXT COMMENT '产品图片URL(多个用逗号分隔)',
    main_image VARCHAR(500) COMMENT '主图URL',
    status TINYINT DEFAULT 1 COMMENT '状态(1上架 0下架)',
    owner_id BIGINT COMMENT '负责人ID',
    department_id BIGINT COMMENT '部门ID',
    remark TEXT COMMENT '备注',
    created_by BIGINT COMMENT '创建者',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新者',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE INDEX idx_product_no (product_no),
    UNIQUE INDEX idx_sku (sku),
    INDEX idx_category_id (category_id),
    INDEX idx_status (status),
    INDEX idx_owner_id (owner_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品表';
```

## 使用示例

### 创建产品
```bash
curl -X POST http://localhost:8080/product \
  -H "Content-Type: application/json" \
  -d '{
    "productName": "LED Light",
    "productNameCn": "LED灯",
    "categoryId": 1,
    "sku": "LED-001",
    "specification": "10W",
    "unit": "PCS",
    "costPrice": 10.50,
    "factoryPrice": 15.00,
    "fobPrice": 2.50,
    "cifPrice": 3.00,
    "currency": "USD",
    "stockQuantity": 1000,
    "minOrderQuantity": 100,
    "hsCode": "9405409000",
    "taxRefundRate": 13.00,
    "status": 1
  }'
```

### 获取产品价格(含多币种换算)
```bash
curl http://localhost:8080/product/1/price
```

响应示例:
```json
{
  "code": 200,
  "data": {
    "productId": 1,
    "productNo": "PRD20260409001",
    "productName": "LED Light",
    "costPrice": 10.50,
    "factoryPrice": 15.00,
    "fobPrice": 2.50,
    "cifPrice": 3.00,
    "eurPrice": 2.31,
    "gbpPrice": 2.00,
    "jpyPrice": 375,
    "profitMargin": 42.86,
    "currency": "USD",
    "usdToCnyRate": 7.2000,
    "eurToCnyRate": 7.8000,
    "gbpToCnyRate": 9.0000,
    "jpyToCnyRate": 0.0480
  }
}
```

## 注意事项

1. **汇率配置**: 当前使用固定汇率，实际项目中应从数据库或实时API获取
2. **图片存储**: 图片URL应指向MinIO或其他对象存储服务
3. **价格精度**: 所有价格字段使用DECIMAL类型，保留4位小数
4. **库存管理**: 库存变更使用事务保证数据一致性
5. **导入导出**: 大数据量导入导出时应考虑分批处理

## 开发团队
CRM Development Team

## 版本历史
- v1.0.0 (2026-04-09): 初始版本
