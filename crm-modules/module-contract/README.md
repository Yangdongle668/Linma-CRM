# 合同管理模块 (module-contract)

## 模块概述
合同管理模块是外贸CRM系统的核心业务模块之一，负责管理合同的完整生命周期，包括合同创建、审批、签署、归档等流程。

## 核心功能

### 1. 合同管理
- ✅ 合同CRUD操作
- ✅ 合同号自动生成(格式: CON20260409001)
- ✅ 合同类型管理(sales/purchase/agency/nda)
- ✅ 合同状态流转(draft→pending_approval→approved→archived)

### 2. 审批流程
- ✅ 提交审批
- ✅ 审批通过/拒绝
- ✅ 审批意见记录
- ✅ 电子签名集成(预留接口)

### 3. 合同模板
- ✅ 模板CRUD管理
- ✅ 变量替换({{variableName}})
- ✅ 多语言支持(zh/en)
- ✅ 默认模板设置

### 4. 合同归档
- ✅ PDF文件上传
- ✅ 归档时间记录
- ✅ 关联订单/报价单

### 5. 统计分析
- ✅ 待审批合同统计
- ✅ 本月签订金额统计

## 技术架构

### 依赖管理
- **Spring Boot**: 3.2.0
- **MyBatis Plus**: 3.5.5
- **Hutool**: 5.8.24 (工具类)
- **Knife4j**: 4.3.0 (Swagger增强)

### 分层架构
```
Controller → Service → Mapper
```

## API接口列表

### 合同接口
1. `GET /contract/page` - 分页查询合同
2. `GET /contract/{id}` - 获取合同详情
3. `POST /contract` - 创建合同
4. `PUT /contract` - 更新合同
5. `DELETE /contract` - 删除合同
6. `GET /contract/generate-no` - 生成合同号
7. `POST /contract/{id}/submit-approval` - 提交审批
8. `POST /contract/approve` - 审批合同
9. `POST /contract/{id}/archive` - 归档合同
10. `POST /contract/{id}/cancel` - 取消合同
11. `POST /contract/{id}/signature` - 上传电子签名
12. `POST /contract/generate-from-template` - 从模板生成合同
13. `GET /contract/export` - 导出合同
14. `GET /contract/count/pending-approval` - 统计待审批数量
15. `GET /contract/sum/monthly-amount` - 统计本月签订金额

### 合同模板接口
1. `GET /contract/template/page` - 分页查询模板
2. `GET /contract/template/{id}` - 获取模板详情
3. `POST /contract/template` - 创建模板
4. `PUT /contract/template` - 更新模板
5. `DELETE /contract/template` - 删除模板
6. `GET /contract/template/list` - 查询模板列表
7. `GET /contract/template/default` - 获取默认模板
8. `POST /contract/template/{id}/set-default` - 设置默认模板
9. `GET /contract/template/check-code` - 检查模板编码唯一性

## 数据库设计

### crm_contract (合同表)
```sql
CREATE TABLE crm_contract (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '合同ID',
    contract_no VARCHAR(50) NOT NULL COMMENT '合同号',
    contract_name VARCHAR(200) NOT NULL COMMENT '合同名称',
    contract_type VARCHAR(20) NOT NULL COMMENT '合同类型',
    customer_id BIGINT COMMENT '客户ID',
    customer_name VARCHAR(200) COMMENT '客户名称',
    contact_id BIGINT COMMENT '联系人ID',
    contact_name VARCHAR(100) COMMENT '联系人姓名',
    supplier_id BIGINT COMMENT '供应商ID',
    supplier_name VARCHAR(200) COMMENT '供应商名称',
    contract_amount DECIMAL(15,2) NOT NULL COMMENT '合同金额',
    currency VARCHAR(10) DEFAULT 'USD' COMMENT '币种',
    exchange_rate DECIMAL(10,4) COMMENT '汇率',
    sign_date DATE COMMENT '签订日期',
    effective_date DATE COMMENT '生效日期',
    expiry_date DATE COMMENT '到期日期',
    payment_terms VARCHAR(200) COMMENT '付款方式',
    delivery_terms VARCHAR(100) COMMENT '交货方式',
    port_of_loading VARCHAR(100) COMMENT '装运港',
    port_of_destination VARCHAR(100) COMMENT '目的港',
    status VARCHAR(20) DEFAULT 'draft' COMMENT '状态',
    approver_id BIGINT COMMENT '审批人ID',
    approver_name VARCHAR(50) COMMENT '审批人姓名',
    approval_time DATETIME COMMENT '审批时间',
    approval_comment TEXT COMMENT '审批意见',
    electronic_signature VARCHAR(500) COMMENT '电子签名URL',
    contract_file_url VARCHAR(500) COMMENT '合同文件URL',
    order_id BIGINT COMMENT '订单ID',
    order_no VARCHAR(50) COMMENT '订单号',
    quotation_id BIGINT COMMENT '报价单ID',
    quotation_no VARCHAR(50) COMMENT '报价单号',
    owner_id BIGINT COMMENT '负责人ID',
    owner_name VARCHAR(50) COMMENT '负责人姓名',
    department_id BIGINT COMMENT '部门ID',
    archive_time DATETIME COMMENT '归档时间',
    remark TEXT COMMENT '备注',
    created_by BIGINT COMMENT '创建者',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新者',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE INDEX idx_contract_no (contract_no),
    INDEX idx_customer_id (customer_id),
    INDEX idx_status (status),
    INDEX idx_owner_id (owner_id),
    INDEX idx_contract_type (contract_type),
    INDEX idx_sign_date (sign_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='合同表';
```

### crm_contract_template (合同模板表)
```sql
CREATE TABLE crm_contract_template (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '模板ID',
    template_name VARCHAR(100) NOT NULL COMMENT '模板名称',
    template_code VARCHAR(50) COMMENT '模板编码',
    contract_type VARCHAR(20) NOT NULL COMMENT '合同类型',
    template_content TEXT NOT NULL COMMENT '模板内容',
    language VARCHAR(10) DEFAULT 'zh' COMMENT '语言',
    version VARCHAR(20) DEFAULT '1.0' COMMENT '版本号',
    is_default TINYINT DEFAULT 0 COMMENT '是否默认模板',
    status TINYINT DEFAULT 1 COMMENT '状态',
    description TEXT COMMENT '描述',
    created_by BIGINT COMMENT '创建者',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新者',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE INDEX idx_template_code (template_code),
    INDEX idx_contract_type (contract_type),
    INDEX idx_language (language)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='合同模板表';
```

## 使用示例

### 创建合同
```bash
curl -X POST http://localhost:8080/contract \
  -H "Content-Type: application/json" \
  -d '{
    "contractName": "Sales Contract - ABC Company",
    "contractType": "sales",
    "customerId": 1,
    "customerName": "ABC Company",
    "contactName": "John Smith",
    "contractAmount": 50000.00,
    "currency": "USD",
    "paymentTerms": "T/T 30% deposit, 70% before shipment",
    "deliveryTerms": "FOB Shanghai",
    "portOfLoading": "Shanghai",
    "portOfDestination": "Los Angeles"
  }'
```

### 从模板生成合同
```bash
curl -X POST http://localhost:8080/contract/generate-from-template?templateId=1 \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "ABC Company",
    "contractAmount": "50000.00",
    "currency": "USD",
    "signDate": "2026-04-09"
  }'
```

### 审批合同
```bash
curl -X POST http://localhost:8080/contract/approve \
  -H "Content-Type: application/json" \
  -d '{
    "contractId": 1,
    "approvalResult": "approve",
    "approvalComment": "同意签订",
    "electronicSignature": "https://example.com/signature.png"
  }'
```

## 模板变量说明

合同模板支持使用 `{{variableName}}` 格式的占位符，常用变量包括：

- `{{customerName}}` - 客户名称
- `{{contactName}}` - 联系人姓名
- `{{contractAmount}}` - 合同金额
- `{{currency}}` - 币种
- `{{signDate}}` - 签订日期
- `{{paymentTerms}}` - 付款方式
- `{{deliveryTerms}}` - 交货方式
- `{{portOfLoading}}` - 装运港
- `{{portOfDestination}}` - 目的港

## 注意事项

1. **状态流转**: 合同状态只能按既定流程流转
2. **修改限制**: 已审批或已归档的合同不能修改
3. **电子签名**: 当前为预留接口，实际项目中需集成第三方电子签名服务
4. **模板变量**: 模板中的变量名必须与传入的变量映射key一致
5. **默认模板**: 每种合同类型和语言组合只能有一个默认模板

## 开发团队
CRM Development Team

## 版本历史
- v1.0.0 (2026-04-09): 初始版本
