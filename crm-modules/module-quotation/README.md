# 报价单管理模块 (Module Quotation)

## 模块概述

报价单管理模块是外贸CRM系统的核心功能之一,提供完整的报价单生命周期管理,包括创建、审批、PDF生成、邮件发送和转订单等功能。

## 主要功能

### 1. 报价单CRUD
- 支持报价单主表和明细表的完整增删改查
- 报价单号自动生成(格式: QT20260409001)
- 版本管理:每次修改自动生成新版本

### 2. 多币种支持
- 支持USD/EUR/CNY/GBP等多种货币
- 实时汇率转换
- 自动计算利润和利润率

### 3. 贸易术语
- 支持FOB/CIF/DDP/EXW等国际贸易术语
- 装运港和目的港管理
- 付款方式和交货期设置

### 4. 审批流程
- 利润率<10%自动触发审批流程
- 支持提交审批、批准/拒绝操作
- 审批意见记录

### 5. PDF生成
- 使用iText 7生成专业PDF报价单
- 支持中英文双语
- 可添加水印

### 6. 邮件发送
- 支持邮件发送报价单PDF
- 追踪发送次数和打开率
- 自定义邮件主题和正文

### 7. 从询盘转化
- 支持从询盘信息快速创建报价单
- 自动填充客户和产品信息

### 8. 转为订单
- 已批准或已发送的报价单可转为订单
- 自动复制报价单信息到订单

## 核心实体

### CrmQuotation (报价单主表)
```java
- id: 报价单ID
- quotationNo: 报价单号(QT20260409001)
- version: 版本号
- customerId: 客户ID
- currency: 币种
- tradeTerm: 贸易术语
- totalAmount: 总金额
- profitMargin: 利润率
- status: 状态(draft/pending_approval/approved/rejected/sent/converted/expired)
```

### CrmQuotationItem (报价单明细)
```java
- id: 明细ID
- quotationId: 报价单ID
- productId: 产品ID
- productName: 产品名称
- quantity: 数量
- unitPrice: 单价
- totalAmount: 总金额
- costPrice: 成本价
- profitMargin: 利润率
```

## API接口

### 基础CRUD
- `GET /quotation/page` - 分页查询报价单列表
- `GET /quotation/{id}` - 查询报价单详情
- `POST /quotation` - 创建报价单
- `PUT /quotation` - 更新报价单
- `DELETE /quotation/{ids}` - 删除报价单

### 审批管理
- `POST /quotation/{id}/submit-approval` - 提交审批
- `POST /quotation/approve` - 审批报价单

### PDF和邮件
- `POST /quotation/{id}/generate-pdf` - 生成PDF
- `POST /quotation/send-email` - 发送邮件

### 其他功能
- `POST /quotation/from-inquiry/{inquiryId}` - 从询盘创建
- `POST /quotation/{id}/convert-to-order` - 转为订单
- `GET /quotation/{id}/versions` - 获取历史版本
- `POST /quotation/{id}/copy` - 复制报价单
- `GET /quotation/export` - 导出Excel

## Service层

### QuotationService
主要业务逻辑服务,包含报价单的CRUD、审批、转订单等核心功能。

### QuotationPdfGenerator
PDF生成服务,使用iText 7生成专业的中英双语报价单。

### QuotationApprovalService
审批服务,处理低利润报价单的自动审批流程。

## 技术栈

- **ORM框架**: MyBatis Plus
- **PDF生成**: iText 7
- **Excel导出**: EasyExcel
- **工具类**: Hutool
- **API文档**: Swagger/Knife4j

## 数据库表

需要创建以下数据库表:
- `crm_quotation` - 报价单主表
- `crm_quotation_item` - 报价单明细表

## 使用示例

### 创建报价单
```java
QuotationCreateDTO dto = new QuotationCreateDTO();
dto.setCustomerId(1L);
dto.setCurrency("USD");
dto.setTradeTerm("FOB");
dto.setValidityDays(30);

List<QuotationItemDTO> items = new ArrayList<>();
QuotationItemDTO item = new QuotationItemDTO();
item.setProductName("Product A");
item.setQuantity(new BigDecimal("100"));
item.setUnitPrice(new BigDecimal("10.00"));
items.add(item);

dto.setItems(items);
Long quotationId = quotationService.createQuotation(dto);
```

### 生成PDF
```java
String pdfPath = quotationService.generatePdf(quotationId, "en");
```

### 转为订单
```java
Long orderId = quotationService.convertToOrder(quotationId);
```

## 注意事项

1. 只有草稿状态的报价单可以修改和删除
2. 利润率<10%的报价单需要审批才能发送
3. 只有已批准或已发送的报价单可以转为订单
4. 每次修改报价单会自动增加版本号
5. PDF生成需要配置中文字体文件

## 扩展建议

1. 集成邮件服务器实现真实邮件发送
2. 添加邮件打开追踪功能(像素追踪)
3. 集成实时汇率API
4. 添加报价单模板管理
5. 支持批量生成PDF
6. 添加报价单统计分析功能
