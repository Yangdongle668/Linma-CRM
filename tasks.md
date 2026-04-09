# 外贸CRM系统 - 产品、询盘、合同模块开发任务清单

## 模块1: 产品管理 (module-product)

### 实体类
- [x] 创建 CrmProductCategory.java (产品分类实体)
- [x] 创建 CrmProduct.java (产品实体)

### Mapper层
- [x] 创建 ProductCategoryMapper.java
- [x] 创建 ProductMapper.java

### DTO/VO
- [x] 创建 ProductCategoryCreateDTO.java
- [x] 创建 ProductCategoryUpdateDTO.java
- [x] 创建 ProductCategoryQuery.java
- [x] 创建 ProductCategoryTreeVO.java
- [x] 创建 ProductCreateDTO.java
- [x] 创建 ProductUpdateDTO.java
- [x] 创建 ProductQuery.java
- [x] 创建 ProductVO.java
- [x] 创建 ProductPriceVO.java

### Service层
- [x] 创建 ProductCategoryService.java (接口)
- [x] 创建 ProductCategoryServiceImpl.java (实现)
- [x] 创建 ProductService.java (接口)
- [x] 创建 ProductServiceImpl.java (实现)

### Controller层
- [x] 创建 ProductCategoryController.java
- [x] 创建 ProductController.java

### 其他
- [x] 创建 ModuleProductApplication.java
- [x] 创建 pom.xml
- [x] 创建 README.md

## 模块2: 询盘管理 (module-inquiry)

### 实体类
- [x] 创建 CrmInquiry.java (询盘实体)

### Mapper层
- [x] 创建 InquiryMapper.java

### DTO/VO
- [x] 创建 InquiryCreateDTO.java
- [x] 创建 InquiryUpdateDTO.java
- [x] 创建 InquiryQuery.java
- [x] 创建 InquiryReplyDTO.java
- [x] 创建 InquiryVO.java

### Service层
- [x] 创建 InquiryService.java (接口)
- [x] 创建 InquiryServiceImpl.java (实现)
- [x] 创建 InquiryAutoAssignService.java (自动分配服务)

### Controller层
- [x] 创建 InquiryController.java

### 其他
- [x] 创建 ModuleInquiryApplication.java
- [x] 创建 pom.xml
- [x] 创建 README.md

## 模块3: 合同管理 (module-contract)

### 实体类
- [x] 创建 CrmContract.java (合同实体)
- [x] 创建 CrmContractTemplate.java (合同模板实体)

### Mapper层
- [x] 创建 ContractMapper.java
- [x] 创建 ContractTemplateMapper.java

### DTO/VO
- [x] 创建 ContractCreateDTO.java
- [x] 创建 ContractUpdateDTO.java
- [x] 创建 ContractQuery.java
- [x] 创建 ContractApprovalDTO.java
- [x] 创建 ContractTemplateCreateDTO.java
- [x] 创建 ContractTemplateUpdateDTO.java
- [x] 创建 ContractTemplateQuery.java
- [x] 创建 ContractVO.java
- [x] 创建 ContractTemplateVO.java

### Service层
- [x] 创建 ContractService.java (接口)
- [x] 创建 ContractServiceImpl.java (实现)
- [x] 创建 ContractTemplateService.java (接口)
- [x] 创建 ContractTemplateServiceImpl.java (实现)

### Controller层
- [x] 创建 ContractController.java
- [x] 创建 ContractTemplateController.java

### 其他
- [x] 创建 ModuleContractApplication.java
- [x] 创建 pom.xml
- [x] 创建 README.md

## 模块4: 物流管理 (module-shipping)

### 实体类
- [x] 创建 CrmShipping.java (发货单实体)

### Mapper层
- [x] 创建 CrmShippingMapper.java
- [x] 创建 CrmShippingMapper.xml (MyBatis XML映射)

### DTO/VO
- [x] 创建 ShippingCreateDTO.java
- [x] 创建 ShippingUpdateDTO.java
- [x] 创建 ShippingQuery.java
- [x] 创建 ShippingVO.java

### Service层
- [x] 创建 ShippingService.java (接口)
- [x] 创建 ShippingServiceImpl.java (实现)
- [x] 创建 TrackingService.java (物流追踪服务,预留API接口)
- [x] 创建 TrackingServiceImpl.java (实现)

### Controller层
- [x] 创建 ShippingController.java (/shipping/*)

### 其他
- [x] 创建 ModuleShippingApplication.java
- [x] 创建 pom.xml

## 模块5: 财务管理 (module-finance)

### 实体类
- [x] 创建 CrmReceivable.java (应收账款实体)
- [x] 创建 CrmPayable.java (应付账款实体)

### Mapper层
- [x] 创建 CrmReceivableMapper.java
- [x] 创建 CrmPayableMapper.java

### DTO/VO
- [x] 创建 ReceivableQuery.java
- [x] 创建 PaymentRecordDTO.java
- [x] 创建 PayableQuery.java
- [x] 创建 PaymentApprovalDTO.java
- [x] 创建 ReceivableVO.java
- [x] 创建 PayableVO.java

### Service层
- [x] 创建 ReceivableService.java (应收账款)
- [x] 创建 ReceivableServiceImpl.java (实现)
- [x] 创建 PayableService.java (应付账款)
- [x] 创建 PayableServiceImpl.java (实现)
- [x] 创建 ProfitService.java (利润核算)
- [x] 创建 ProfitServiceImpl.java (实现)

### Controller层
- [x] 创建 ReceivableController.java (/finance/receivable/*)
- [x] 创建 PayableController.java (/finance/payable/*)
- [x] 创建 ProfitController.java (/finance/profit/*)

### 其他
- [x] 创建 ModuleFinanceApplication.java
- [x] 创建 pom.xml

## 模块6: 数据分析BI (module-analytics)

### Mapper层
- [x] 创建 AnalyticsMapper.java
- [x] 创建 AnalyticsMapper.xml (复杂SQL查询示例)

### Service层
- [x] 创建 FunnelService.java (销售漏斗)
- [x] 创建 FunnelServiceImpl.java (实现,含SQL示例)
- [x] 创建 RankingService.java (业绩排行)
- [x] 创建 RankingServiceImpl.java (实现,含SQL示例)
- [x] 创建 CustomerAnalyticsService.java (客户分析)
- [x] 创建 CustomerAnalyticsServiceImpl.java (实现,含SQL示例)
- [x] 创建 ProductAnalyticsService.java (产品分析)
- [x] 创建 ProductAnalyticsServiceImpl.java (实现,含SQL示例)
- [x] 创建 RegionAnalyticsService.java (区域分析)
- [x] 创建 RegionAnalyticsServiceImpl.java (实现,含SQL示例)
- [x] 创建 TrendService.java (趋势分析)
- [x] 创建 TrendServiceImpl.java (实现,含SQL示例)
- [x] 创建 DashboardService.java (仪表盘)
- [x] 创建 DashboardServiceImpl.java (实现)

### Controller层
- [x] 创建 AnalyticsController.java (/analytics/*) - 统一BI入口

### 其他
- [x] 创建 ModuleAnalyticsApplication.java
- [x] 创建 pom.xml

## 模块7: 消息通知 (module-message)

### 实体类
- [x] 创建 MsgNotification.java (站内消息)
- [x] 创建 MsgEmailLog.java (邮件日志)
- [x] 创建 MsgTemplate.java (消息模板)

### Mapper层
- [x] 创建 MsgNotificationMapper.java
- [x] 创建 MsgEmailLogMapper.java
- [x] 创建 MsgTemplateMapper.java

### DTO/VO
- [x] 创建 NotificationQuery.java
- [x] 创建 EmailSendDTO.java
- [x] 创建 TemplateCreateDTO.java
- [x] 创建 NotificationVO.java

### Service层
- [x] 创建 NotificationService.java (站内消息)
- [x] 创建 NotificationServiceImpl.java (实现)
- [x] 创建 EmailService.java (邮件服务)
- [x] 创建 EmailServiceImpl.java (实现,Spring Mail集成)
- [x] 创建 TemplateService.java (模板管理)
- [x] 创建 TemplateServiceImpl.java (实现)

### Controller层
- [x] 创建 NotificationController.java (/message/notification/*)
- [x] 创建 EmailController.java (/message/email/*)
- [x] 创建 TemplateController.java (/message/template/*)

### 其他
- [x] 创建 ModuleMessageApplication.java
- [x] 创建 pom.xml (包含spring-boot-starter-mail依赖)

## 项目配置
- [x] 更新 crm-modules/pom.xml (添加四个新模块: shipping, finance, analytics, message)
