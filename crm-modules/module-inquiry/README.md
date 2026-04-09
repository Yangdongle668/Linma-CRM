# 询盘管理模块 (module-inquiry)

## 模块概述
询盘管理模块是外贸CRM系统的核心业务模块之一，负责管理客户询盘的全生命周期，包括询盘接收、分配、处理、报价和转化。

## 核心功能

### 1. 询盘管理
- ✅ 询盘CRUD操作
- ✅ 询盘号自动生成(格式: INQ20260409001)
- ✅ 询盘来源追踪(alibaba/website/email/exhibition/referral)
- ✅ 优先级管理(HIGH/MEDIUM/LOW)

### 2. 自动分配
- ✅ 轮询分配(round_robin)
- ✅ 负载均衡分配(load_balance)
- ✅ 区域匹配分配(region_match)

### 3. 状态流转
- ✅ 新建(new)
- ✅ 处理中(processing)
- ✅ 已报价(quoted)
- ✅ 已转化(converted)
- ✅ 已关闭(closed)

### 4. 询盘回复
- ✅ 回复记录管理
- ✅ 回复方式追踪(email/phone/whatsapp/wechat)
- ✅ 附件管理

### 5. 转化管理
- ✅ 转为报价单
- ✅ 转为订单
- ✅ 转化率统计

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

### 询盘接口
1. `GET /inquiry/page` - 分页查询询盘
2. `GET /inquiry/{id}` - 获取询盘详情
3. `POST /inquiry` - 创建询盘
4. `PUT /inquiry` - 更新询盘
5. `DELETE /inquiry` - 删除询盘
6. `GET /inquiry/generate-no` - 生成询盘号
7. `POST /inquiry/reply` - 回复询盘
8. `PUT /inquiry/{id}/status` - 变更状态
9. `POST /inquiry/{id}/convert-to-quotation` - 转为报价单
10. `POST /inquiry/{id}/close` - 关闭询盘
11. `POST /inquiry/{id}/assign` - 分配询盘
12. `POST /inquiry/{id}/auto-assign` - 自动分配询盘
13. `GET /inquiry/export` - 导出询盘
14. `GET /inquiry/count/pending` - 统计待处理询盘
15. `GET /inquiry/count/converted` - 统计已转化询盘

## 数据库设计

### crm_inquiry (询盘表)
```sql
CREATE TABLE crm_inquiry (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '询盘ID',
    inquiry_no VARCHAR(50) NOT NULL COMMENT '询盘号',
    customer_id BIGINT COMMENT '客户ID',
    customer_name VARCHAR(200) COMMENT '客户名称',
    contact_id BIGINT COMMENT '联系人ID',
    contact_name VARCHAR(100) COMMENT '联系人姓名',
    contact_email VARCHAR(100) COMMENT '联系人邮箱',
    source VARCHAR(20) NOT NULL COMMENT '询盘来源',
    priority VARCHAR(10) DEFAULT 'MEDIUM' COMMENT '优先级',
    status VARCHAR(20) DEFAULT 'new' COMMENT '状态',
    subject VARCHAR(200) NOT NULL COMMENT '主题',
    content TEXT COMMENT '询盘内容',
    product_id BIGINT COMMENT '产品ID',
    product_name VARCHAR(200) COMMENT '产品名称',
    quantity INT COMMENT '需求数量',
    target_price DECIMAL(15,4) COMMENT '目标价格',
    currency VARCHAR(10) DEFAULT 'USD' COMMENT '币种',
    destination_country VARCHAR(50) COMMENT '目的国家',
    expected_delivery_date DATETIME COMMENT '期望交货期',
    owner_id BIGINT COMMENT '负责人ID',
    owner_name VARCHAR(50) COMMENT '负责人姓名',
    department_id BIGINT COMMENT '部门ID',
    last_reply_time DATETIME COMMENT '最后回复时间',
    reply_count INT DEFAULT 0 COMMENT '回复次数',
    quotation_id BIGINT COMMENT '报价单ID',
    order_id BIGINT COMMENT '订单ID',
    close_reason TEXT COMMENT '关闭原因',
    remark TEXT COMMENT '备注',
    created_by BIGINT COMMENT '创建者',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新者',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE INDEX idx_inquiry_no (inquiry_no),
    INDEX idx_customer_id (customer_id),
    INDEX idx_status (status),
    INDEX idx_owner_id (owner_id),
    INDEX idx_source (source),
    INDEX idx_priority (priority),
    INDEX idx_created_time (created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='询盘表';
```

## 使用示例

### 创建询盘
```bash
curl -X POST http://localhost:8080/inquiry \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "ABC Company",
    "contactName": "John Smith",
    "contactEmail": "john@example.com",
    "source": "alibaba",
    "priority": "HIGH",
    "subject": "Inquiry about LED lights",
    "content": "We are interested in your LED lights, please send us quotation.",
    "productName": "LED Light",
    "quantity": 1000,
    "targetPrice": 2.00,
    "currency": "USD",
    "destinationCountry": "USA"
  }'
```

### 回复询盘
```bash
curl -X POST http://localhost:8080/inquiry/reply \
  -H "Content-Type: application/json" \
  -d '{
    "inquiryId": 1,
    "replyContent": "Thank you for your inquiry. Please find attached quotation.",
    "replyMethod": "email",
    "markAsQuoted": true
  }'
```

### 自动分配询盘
```bash
curl -X POST http://localhost:8080/inquiry/1/auto-assign?strategy=round_robin
```

## 分配策略说明

### 1. 轮询分配 (round_robin)
按照销售人员顺序轮流分配询盘，确保每个销售人员获得均等的询盘机会。

### 2. 负载均衡 (load_balance)
将询盘分配给当前待处理询盘最少的销售人员，平衡工作负载。

### 3. 区域匹配 (region_match)
根据客户所在国家/地区匹配对应的销售区域负责人，提高专业性。

## 注意事项

1. **状态流转**: 询盘状态只能按既定流程流转，不能逆向
2. **自动分配**: 实际项目中应从数据库配置销售人员列表和区域映射
3. **转化率统计**: 建议定期统计各来源、各销售人员的转化率
4. **响应时效**: 高优先级询盘应在24小时内回复

## 开发团队
CRM Development Team

## 版本历史
- v1.0.0 (2026-04-09): 初始版本
