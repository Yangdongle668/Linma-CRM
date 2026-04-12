# Enhanced Customer Management System / 增强版客户管理系统

## Overview / 概述

The enhanced customer management system provides comprehensive customer information tracking with deep email integration, automatic follow-up record generation, and a professional UI similar to modern CRM systems.

增强版客户管理系统提供全面的客户信息跟踪，深度邮件集成，自动生成跟进记录，以及类似现代CRM系统的专业界面。

---

## Key Features / 核心功能

### 1. Comprehensive Customer Attributes / 完善的客户属性

#### Basic Information / 基本信息
- Company Name (English & Chinese) / 公司名称（中英文）
- Company Website / 公司网站
- Email Domain (auto-extracted) / 邮箱域名（自动提取）
- Industry Classification / 行业分类
- Business Type (Manufacturer/Trader/Distributor/Retailer) / 业务类型
- Company Size / 公司规模
- Main Products / 主营产品
- Main Markets / 主要市场
- Established Year / 成立年份
- Annual Revenue / 年营业额

#### Contact Information / 联系信息
- Full Address (Country, Province, City, Street) / 完整地址
- Timezone / 时区
- Language Preference / 语言偏好
- Phone with Country Code / 带国家代码的电话
- Fax Number / 传真号码

#### Social Media / 社交媒体
- LinkedIn Profile / LinkedIn主页
- Facebook Page / Facebook主页
- Twitter Profile / Twitter主页

#### Business Details / 业务详情
- Tax ID / VAT Number / 税号/VAT号
- Registration Number / 注册号
- Import License / 进口许可证
- Payment Terms (T/T, L/C, etc.) / 付款条件
- Credit Rating (AAA/AA/A/B/C) / 信用评级

#### Customer Classification / 客户分级
- Customer Level (A/B/C/D) / 客户等级
- Priority (High/Medium/Low) / 优先级
- Customer Lifecycle Stage / 客户生命周期阶段
  - Prospect → Lead → Opportunity → Customer → Churned
- Acquisition Channel / 获客渠道
- Campaign Source / 营销活动来源
- First Contact Date / 首次接触日期

#### Certifications / 认证资质
- ISO Certification / ISO认证
- CE Certification / CE认证
- FDA Certification / FDA认证

#### Risk & Opportunity Analysis / 风险与机会分析
- Risk Level (Low/Medium/High) / 风险等级
- Risk Factors / 风险因素
- Opportunity Value / 商机价值
- Conversion Probability (0-100%) / 转化概率
- Expected Close Date / 预计成交日期

#### Satisfaction Metrics / 满意度指标
- Satisfaction Score (1-5) / 满意度评分
- Net Promoter Score (NPS, -100 to 100) / 净推荐值

#### Communication History / 沟通历史
- Last Email Time / 最后邮件联系时间
- Email Count / 邮件往来次数
- Last Call Time / 最后电话沟通时间
- Call Count / 电话沟通次数
- Last Meeting Time / 最后会议时间
- Meeting Count / 会议次数

#### Analysis / 分析信息
- Competitor Information / 竞争对手信息
- SWOT Analysis / SWOT分析
- Custom Fields (JSON) / 自定义字段
- Tags (JSON) / 标签

### 2. Email Integration / 邮件集成

#### Domain-Based Customer Linking / 基于域名的客户关联
- Automatically extracts email domain from email addresses
- Links incoming/outgoing emails to customers by domain
- Supports multiple contacts per customer domain
- 自动从邮箱地址提取域名
- 通过域名将收发邮件关联到客户
- 支持每个客户域名下的多个联系人

#### Click-to-Email / 点击发邮件
- One-click email composition using system default mail client
- Generates `mailto:` links with pre-filled subject and body
- Opens user's preferred email application (Outlook, Thunderbird, Apple Mail, etc.)
- 一键使用系统默认邮件客户端写信
- 生成预填充主题和正文的 `mailto:` 链接
- 打开用户首选的邮件应用

#### Automatic Follow-up Generation / 自动生成跟进记录
- Creates follow-up records when emails are sent
- Processes incoming emails and auto-links to customers
- Tracks email sentiment (positive/neutral/negative)
- Records email direction (inbound/outbound)
- 发送邮件时自动创建跟进记录
- 处理接收邮件并自动关联客户
- 跟踪邮件情感倾向
- 记录邮件方向

#### Email Statistics / 邮件统计
- Total email count per customer
- Last email communication time
- Email history timeline
- 每个客户的邮件总数
- 最后邮件沟通时间
- 邮件历史时间线

### 3. Follow-up Management / 跟进管理

#### Follow-up Types / 跟进类型
- Email / 邮件
- Phone Call / 电话
- Meeting / 会议
- Visit / 拜访
- Message / 消息
- Other / 其他

#### Follow-up Attributes / 跟进属性
- Subject & Content / 主题和内容
- Direction (Inbound/Outbound) / 方向
- Status (Planned/Completed/Cancelled) / 状态
- Start & End Time / 开始和结束时间
- Duration / 持续时间
- Location / 地点
- Participants / 参与人员
- Outcome / 跟进结果
- Next Steps / 下一步行动
- Next Follow-up Date / 下次跟进日期
- Sentiment Analysis / 情感分析
- Importance Level / 重要性级别

#### Related Records / 关联记录
- Related Inquiry / 关联询盘
- Related Order / 关联订单
- Related Contract / 关联合同
- Email Message ID / 关联邮件ID
- Attachments / 附件

### 4. Professional UI / 专业界面

#### Customer Detail Page / 客户详情页
- **Left Sidebar:**
  - Company avatar and basic info
  - Quick contact actions (email, phone, website)
  - Communication statistics
  - Social media links
  - Risk & opportunity overview

- **Right Tabs:**
  - **Basic Info Tab:** All customer attributes organized in sections
  - **Contacts Tab:** Multiple contacts management
  - **Follow-ups Tab:** Timeline view of all interactions
  - **Email History Tab:** Complete email correspondence
  - **Attachments Tab:** Document management

#### Interactive Features / 交互功能
- Inline editing mode
- Real-time validation
- Click-to-email from any contact
- Email domain auto-extraction
- Progress bars for conversion probability
- Star ratings for satisfaction
- Color-coded priority and risk levels

---

## Database Schema / 数据库结构

### Enhanced Customer Table / 增强客户表
```sql
ALTER TABLE crm_customer ADD COLUMN:
- priority, industry, company_size, business_type
- main_products, main_markets, established_year, annual_revenue
- email_domain, social_linkedin, social_facebook, social_twitter
- tax_id, registration_number, import_license
- payment_terms, credit_rating, timezone, language_preference
- customer_lifecycle, acquisition_channel, campaign_source
- first_contact_date, certifications (iso, ce, fda)
- risk_level, risk_factors, opportunity_value
- conversion_probability, expected_close_date
- satisfaction_score, nps_score
- last_email_time, email_count
- last_call_time, call_count
- last_meeting_time, meeting_count
- competitor_info, swot_analysis
- tags_json, custom_fields
```

### Follow-up Records Table / 跟进记录表
```sql
CREATE TABLE crm_follow_up_record:
- id, customer_id, contact_id
- follow_type, subject, content, direction, status
- start_time, end_time, duration_minutes, location
- participants_json, outcome, next_step, next_follow_date
- sentiment, importance
- related_inquiry_id, related_order_id, related_contract_id
- email_message_id, attachments_json
- created_by, created_time, updated_time, deleted
```

### Customer Activity Timeline / 客户活动时间线
```sql
CREATE TABLE crm_customer_activity:
- id, customer_id, activity_type
- activity_title, activity_summary, activity_data
- related_id, related_type, visibility
- created_by, created_time, deleted
```

### Customer Attachments / 客户附件
```sql
CREATE TABLE crm_customer_attachment:
- id, customer_id
- file_name, file_path, file_size, file_type
- category, description
- uploaded_by, uploaded_time, deleted
```

---

## API Endpoints / API 接口

### Email Integration / 邮件集成

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/customer/email-integration/extract-domain` | Extract domain from email |
| GET | `/customer/email-integration/find-by-domain` | Find customers by domain |
| GET | `/customer/email-integration/find-by-email` | Find best match customer by email |
| POST | `/customer/email-integration/send-and-follow-up` | Send email and create follow-up |
| POST | `/customer/email-integration/process-incoming` | Process incoming email |
| GET | `/customer/email-integration/email-history/{customerId}` | Get customer email history |
| POST | `/customer/email-integration/analyze-sentiment` | Analyze email sentiment |
| GET | `/customer/email-integration/mailto-link` | Generate mailto link |
| GET | `/customer/email-integration/open-mail-client` | Open system mail client |

### Follow-up Records / 跟进记录

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/customer/follow-up/list` | Get follow-up list |
| POST | `/customer/follow-up` | Create follow-up record |
| PUT | `/customer/follow-up` | Update follow-up record |
| DELETE | `/customer/follow-up/{id}` | Delete follow-up record |
| GET | `/customer/follow-up/stats/{customerId}` | Get follow-up statistics |
| GET | `/customer/follow-up/pending` | Get pending follow-ups |

---

## Usage Examples / 使用示例

### 1. Click-to-Email / 点击发邮件

```typescript
// In customer detail page
const primaryContact = contacts.find(c => c.isPrimary)

// Generate mailto link
const mailtoLink = generateMailtoLink(
  primaryContact.email,
  `Re: Product Inquiry - ${customer.companyName}`,
  `Dear ${primaryContact.name},\n\n...`
)

// Open system email client
window.open(mailtoLink, '_blank')
```

### 2. Send Email with Auto Follow-up / 发送邮件并自动创建跟进

```typescript
// Send email via API
const response = await sendEmailAndCreateFollowUp(customerId, {
  to: contact.email,
  subject: 'Product Quotation',
  content: '<p>Dear...</p>',
  direction: 'outbound',
  userId: currentUserId
})

// Returns follow-up record ID
console.log('Follow-up created:', response.data)
```

### 3. Process Incoming Email / 处理接收邮件

```typescript
// When receiving email via IMAP
await processIncomingEmail({
  from: 'customer@example.com',
  to: 'sales@yourcompany.com',
  subject: 'Re: Quotation Request',
  content: 'Thank you for the quote...',
  receivedTime: new Date(),
  ownerId: salesPersonId
})

// System automatically:
// 1. Extracts domain: example.com
// 2. Finds customer with matching email_domain
// 3. Updates customer email stats
// 4. Creates follow-up record
```

### 4. Query Customers by Domain / 按域名查询客户

```typescript
// Find all customers with @example.com domain
const customers = await findCustomersByDomain('example.com')

// Returns list of matching customers
customers.forEach(customer => {
  console.log(customer.companyName, customer.emailDomain)
})
```

---

## Implementation Files / 实现文件

### Backend / 后端

```
crm-modules/module-customer/
├── src/main/java/com/crm/customer/
│   ├── controller/
│   │   └── EmailIntegrationController.java      ✨ NEW
│   ├── service/
│   │   ├── EmailIntegrationService.java         ✨ NEW
│   │   └── impl/
│   │       └── EmailIntegrationServiceImpl.java ✨ NEW
│   ├── mapper/
│   │   └── CrmFollowUpRecordMapper.java         ✨ NEW
│   └── domain/
│       ├── entity/
│       │   ├── CrmCustomer.java                 ✨ ENHANCED
│       │   └── CrmFollowUpRecord.java           ✨ NEW
│       └── vo/
│           └── CustomerVO.java                  ✨ ENHANCED
└── src/main/resources/mapper/
    └── CrmFollowUpRecordMapper.xml              ✨ NEW
```

### Frontend / 前端

```
crm-frontend/src/
├── views/customer/
│   └── detail-enhanced.vue                    ✨ NEW
└── api/
    └── customer.ts                             (add new endpoints)
```

### Database / 数据库

```
docs/database/
├── enhanced_customer_schema.sql               ✨ NEW
└── email_settings_migration.sql               (created earlier)
```

---

## Deployment Steps / 部署步骤

### 1. Database Migration / 数据库迁移

```bash
# Run enhanced customer schema
mysql -u root -p crm_db < docs/database/enhanced_customer_schema.sql

# Verify tables created
mysql -u root -p -e "SHOW TABLES LIKE 'crm_%';" crm_db
```

### 2. Backend Build / 后端构建

```bash
cd Linma-CRM
mvn clean package -DskipTests

# Restart backend service
docker-compose restart backend
```

### 3. Frontend Build / 前端构建

```bash
cd crm-frontend
npm install
npm run build

# Or for development
npm run dev
```

### 4. Configure Email Settings / 配置邮件设置

Users need to configure their email accounts via the UI:
1. Go to Email Settings page
2. Add email account with SMTP/IMAP details
3. Test connection
4. Save settings

---

## Benefits / 优势

### For Sales Teams / 销售团队
✅ All customer information in one place
✅ Automatic email tracking and follow-up records
✅ Quick access to email clients
✅ Clear visibility of communication history
✅ Risk and opportunity analysis

### For Management / 管理层
✅ Comprehensive customer data for better decisions
✅ Sales pipeline visibility
✅ Team performance tracking
✅ Customer satisfaction metrics
✅ Risk assessment tools

### For Customer Service / 客服团队
✅ Complete interaction history
✅ Quick email responses
✅ Follow-up reminders
✅ Sentiment analysis
✅ Multi-contact management

---

## Future Enhancements / 未来增强

- [ ] AI-powered email response suggestions
- [ ] Automated lead scoring
- [ ] Email template library with merge fields
- [ ] Advanced email analytics dashboard
- [ ] Integration with WhatsApp/WeChat
- [ ] Voice call integration
- [ ] Meeting scheduling with calendar sync
- [ ] Customer portal for self-service
- [ ] Mobile app for field sales
- [ ] Predictive analytics for churn prevention

---

**Version**: 2.0.0
**Last Updated**: 2026-04-12
**Author**: CRM Development Team
