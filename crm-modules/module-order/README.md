# 订单管理模块 (Module Order)

## 模块概述

订单管理模块是外贸CRM系统的核心业务模块,提供从订单创建到完成的完整生命周期管理,包括生产进度跟踪、装箱单管理、发票管理和出口退税等功能。

## 主要功能

### 1. 订单CRUD
- 支持订单主表和明细表的完整增删改查
- 订单号自动生成(格式: ORD20260409001)
- 从报价单快速转化

### 2. 订单状态机
完整的订单状态流转:
```
待确认 → 生产中 → 已完工 → 待发货 → 已发货 → 已完成
                ↓
              已取消
```

### 3. 生产进度跟踪
- 实时更新生产进度百分比
- 关键节点拍照上传
- 预计完成日期管理
- 自动状态转换(生产100%自动转已完工)

### 4. 装箱单管理
- 自动计算总体积、总重量、箱数
- 使用EasyExcel生成专业装箱单
- 支持每箱数量、毛重、净重计算

### 5. 发票管理
- PI形式发票(Proforma Invoice)
- CI商业发票(Commercial Invoice)
- 使用iText 7生成PDF
- 包含银行信息和贸易条款

### 6. 收款管理
- 记录每次收款金额
- 自动计算收款进度百分比
- 支持多币种收款

### 7. 出口退税
- 记录退税金额和退税率
- 自动计算退税收益

### 8. 装箱信息
- 自动计算每行产品的箱数
- 总体积(CBM)、总毛重、总净重
- 支持不同包装方式

## 核心实体

### CrmOrder (订单主表)
```java
- id: 订单ID
- orderNo: 订单号(ORD20260409001)
- quotationId: 关联报价单ID
- customerId: 客户ID
- currency: 币种
- tradeTerm: 贸易术语
- totalAmount: 总金额
- profitMargin: 利润率
- status: 状态(pending_confirm/producing/completed/pending_shipment/shipped/completed/cancelled)
- productionProgress: 生产进度(%)
- receivedAmount: 已收款金额
- paymentProgress: 收款进度(%)
- piInvoiceNo: PI形式发票号
- ciInvoiceNo: CI商业发票号
- packingListNo: 装箱单号
- totalVolume: 总体积(CBM)
- totalGrossWeight: 总毛重(KG)
- totalNetWeight: 总净重(KG)
- cartonCount: 箱数
- taxRefundAmount: 出口退税金额
```

### CrmOrderItem (订单明细)
```java
- id: 明细ID
- orderId: 订单ID
- productId: 产品ID
- productName: 产品名称
- quantity: 订单数量
- producedQuantity: 已生产数量
- shippedQuantity: 已发货数量
- unitPrice: 单价
- totalAmount: 总金额
- quantityPerCarton: 每箱数量
- cartonCount: 箱数
- totalGrossWeight: 总毛重
- totalNetWeight: 总净重
- totalVolume: 总体积
```

## API接口

### 基础CRUD
- `GET /order/page` - 分页查询订单列表
- `GET /order/{id}` - 查询订单详情
- `POST /order` - 创建订单
- `PUT /order` - 更新订单
- `DELETE /order/{ids}` - 删除订单

### 状态管理
- `POST /order/change-status` - 变更订单状态
- `POST /order/update-production-progress` - 更新生产进度
- `POST /order/{id}/cancel` - 取消订单

### 发票管理
- `GET /order/{id}/pi-invoice/pdf` - 生成PI发票PDF
- `GET /order/{id}/ci-invoice/pdf` - 生成CI发票PDF
- `POST /order/{id}/generate-pi` - 生成PI发票号
- `POST /order/{id}/generate-ci` - 生成CI发票号

### 装箱单
- `GET /order/{id}/packing-list/excel` - 生成装箱单Excel
- `POST /order/{id}/generate-packing-list` - 生成装箱单号

### 收款管理
- `POST /order/{id}/confirm-payment` - 确认收款

### 其他功能
- `POST /order/from-quotation/{quotationId}` - 从报价单创建
- `POST /order/{id}/copy` - 复制订单
- `GET /order/export` - 导出Excel
- `GET /order/upcoming-delivery` - 即将交货的订单
- `GET /order/producing` - 生产中的订单

## Service层

### OrderService
主要业务逻辑服务,包含订单的CRUD、状态管理、生产进度、收款管理等核心功能。

### PackingListService
装箱单服务,自动计算体积重量,生成Excel格式的装箱单。

### InvoiceService
发票服务,生成PI形式发票和CI商业发票的PDF文档。

## 技术栈

- **ORM框架**: MyBatis Plus
- **PDF生成**: iText 7
- **Excel导出**: EasyExcel
- **工具类**: Hutool
- **API文档**: Swagger/Knife4j

## 数据库表

需要创建以下数据库表:
- `crm_order` - 订单主表
- `crm_order_item` - 订单明细表
- `crm_order_photo` - 订单照片表(可选,用于存储生产节点照片)
- `crm_order_payment` - 订单收款记录表(可选)

## 状态转换规则

合法的状态转换:
- `pending_confirm` → `producing` (开始生产)
- `pending_confirm` → `cancelled` (取消订单)
- `producing` → `completed` (生产完成)
- `completed` → `pending_shipment` (准备发货)
- `pending_shipment` → `shipped` (已发货)
- `shipped` → `completed` (订单完成)

## 使用示例

### 创建订单
```java
OrderCreateDTO dto = new OrderCreateDTO();
dto.setCustomerId(1L);
dto.setCurrency("USD");
dto.setTradeTerm("CIF");
dto.setPaymentTerms("T/T");
dto.setRequiredDeliveryDate(LocalDate.now().plusDays(60));

List<OrderItemDTO> items = new ArrayList<>();
OrderItemDTO item = new OrderItemDTO();
item.setProductName("Product A");
item.setQuantity(new BigDecimal("1000"));
item.setUnitPrice(new BigDecimal("10.00"));
item.setQuantityPerCarton(100);
item.setGrossWeightPerCarton(new BigDecimal("15.5"));
item.setNetWeightPerCarton(new BigDecimal("14.0"));
item.setVolumePerCarton(new BigDecimal("0.05"));
items.add(item);

dto.setItems(items);
Long orderId = orderService.createOrder(dto);
```

### 更新生产进度
```java
ProductionProgressDTO dto = new ProductionProgressDTO();
dto.setOrderId(orderId);
dto.setProgress(new BigDecimal("50"));
dto.setEstimatedCompletionDate(LocalDate.now().plusDays(30));
orderService.updateProductionProgress(dto);
```

### 生成PI发票
```java
byte[] pdfBytes = invoiceService.generatePiInvoice(orderId);
// 返回PDF字节数组,可下载或邮件发送
```

### 确认收款
```java
orderService.confirmPayment(orderId, new BigDecimal("5000.00"));
```

## 装箱单计算逻辑

系统自动计算:
1. **箱数** = ceil(订单数量 / 每箱数量)
2. **总毛重** = 箱数 × 单箱毛重
3. **总净重** = 箱数 × 单箱净重
4. **总体积** = 箱数 × 单箱体积

## 注意事项

1. 只有待确认状态的订单可以修改
2. 只有待确认或已取消的订单可以删除
3. 已完成或已发货的订单不能取消
4. 生产进度达到100%时自动转为已完工状态
5. 装箱单会自动汇总所有明细行的体积重量
6. PI和CI发票号自动生成,格式为PI/CI + 订单号后段

## 扩展建议

1. 集成物流追踪API,实时查询货物位置
2. 添加生产节点照片管理功能
3. 实现收款计划管理(分期收款)
4. 添加订单风险评估
5. 集成海关申报系统
6. 添加订单利润分析报表
7. 支持多工厂协同生产管理
8. 添加订单预警功能(延期风险提醒)

## 与报价单的集成

订单可以从报价单直接转化:
```java
Long orderId = orderService.createFromQuotation(quotationId);
```

转化时会自动:
- 复制报价单的客户和产品信息
- 复制价格和条款
- 关联原报价单ID
- 更新报价单状态为"已转订单"
