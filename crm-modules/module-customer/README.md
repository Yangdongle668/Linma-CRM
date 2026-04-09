# 客户管理模块 (module-customer)

## 模块概述

客户管理模块是外贸CRM系统的核心模块，提供完整的客户生命周期管理功能，包括客户信息维护、联系人管理、跟进记录管理、标签管理等。

## 功能特性

### 1. 客户管理 (Customer Management)

#### 基础功能
- ✅ 客户CRUD操作（创建、查询、更新、删除）
- ✅ 客户列表分页查询（支持高级搜索）
- ✅ 客户详情查看
- ✅ 客户编号自动生成（格式：CUS20260409001）

#### 高级功能
- ✅ **客户查重**：支持按公司名、邮箱、电话、网站进行重复检查
- ✅ **公海池管理**：
  - 查询公海池客户（owner_id为NULL的客户）
  - 从公海池领取客户
  - 释放客户到公海池
  - 自动回收客户（定时任务接口，默认30天未跟进自动回收）
- ✅ **批量导入导出**：
  - Excel格式导入（使用EasyExcel）
  - Excel格式导出
  - 导入结果反馈（成功数、失败数、错误信息）
- ✅ **客户分配**：批量分配客户给指定负责人
- ✅ **客户合并**：合并重复或相似客户

#### 搜索功能
- 按客户编号精确搜索
- 按公司名称模糊搜索（中英文）
- 按国家、省份、城市筛选
- 按行业分类筛选
- 按客户来源筛选
- 按客户等级(A/B/C/D)筛选
- 按负责人筛选
- 按部门筛选
- 按标签筛选
- 关键字综合搜索（公司名、网站、备注）
- 按创建时间范围筛选

### 2. 联系人管理 (Contact Management)

#### 基础功能
- ✅ 联系人CRUD操作
- ✅ 根据客户ID获取联系人列表
- ✅ 联系人分页查询（支持关键字搜索）
- ✅ 联系人详情查看

#### 特色功能
- ✅ **关键人标识**：标记决策者、影响者、使用者、把关者
- ✅ **联系人查重**：按邮箱、电话检查重复
- ✅ **全名自动生成**：根据姓和名自动组合

### 3. 跟进记录管理 (Follow-up Management)

#### 基础功能
- ✅ 跟进记录CRUD操作
- ✅ 获取客户的跟进记录时间轴
- ✅ 跟进记录分页查询

#### 提醒功能
- ✅ **设置跟进提醒**：设置下次跟进时间
- ✅ **今日需跟进客户列表**：查询今天需要跟进的客户
- ✅ **即将到期提醒**：查询未来N小时内到期的跟进提醒

#### 跟进方式
- 邮件 (email)
- 电话 (phone)
- 微信 (wechat)
- 面谈 (meeting)
- 视频会议 (video)

#### 其他功能
- 附件上传（支持多个附件）
- 拜访照片上传
- 通话时长记录
- 客户满意度评分(1-5星)
- GPS定位记录（拜访地点）

### 4. 标签管理 (Tag Management)

#### 基础功能
- ✅ 标签CRUD操作
- ✅ 标签列表查询（支持按名称、类型筛选）
- ✅ 为客户打标签
- ✅ 移除客户标签
- ✅ 获取客户的标签列表

#### 自动打标
- ✅ **手动标签**：用户手动添加
- ✅ **自动标签**：基于规则自动打标（预留接口）
- ✅ **使用次数统计**：记录每个标签的使用频率

## 技术架构

### 使用的技术栈
- **框架**: Spring Boot 3.2.0
- **ORM**: MyBatis Plus 3.5.5
- **数据库**: MySQL 8.0
- **Excel处理**: EasyExcel 3.3.3
- **工具类**: Hutool 5.8.24
- **API文档**: Swagger/Knife4j 4.3.0
- **日志**: SLF4J + Logback

### 代码结构
```
module-customer/
├── src/main/java/com/crm/customer/
│   ├── ModuleCustomerApplication.java    # 模块启动类
│   ├── controller/                        # 控制器层
│   │   ├── CustomerController.java       # 客户管理接口
│   │   ├── ContactController.java        # 联系人管理接口
│   │   ├── FollowUpController.java       # 跟进记录接口
│   │   └── TagController.java            # 标签管理接口
│   ├── service/                           # 服务层接口
│   │   ├── CustomerService.java
│   │   ├── ContactService.java
│   │   ├── FollowUpService.java
│   │   └── TagService.java
│   ├── service/impl/                      # 服务层实现
│   │   ├── CustomerServiceImpl.java
│   │   ├── ContactServiceImpl.java
│   │   ├── FollowUpServiceImpl.java
│   │   └── TagServiceImpl.java
│   ├── mapper/                            # Mapper接口
│   │   ├── CrmCustomerMapper.java
│   │   ├── CrmContactMapper.java
│   │   ├── CrmFollowUpMapper.java
│   │   ├── CrmTagMapper.java
│   │   └── CrmCustomerTagMapper.java
│   └── domain/
│       ├── entity/                        # 实体类
│       │   ├── CrmCustomer.java
│       │   ├── CrmContact.java
│       │   ├── CrmFollowUp.java
│       │   ├── CrmTag.java
│       │   └── CrmCustomerTag.java
│       ├── dto/                           # 数据传输对象
│       │   ├── CustomerCreateDTO.java
│       │   ├── CustomerUpdateDTO.java
│       │   ├── CustomerQuery.java
│       │   ├── ContactCreateDTO.java
│       │   ├── ContactUpdateDTO.java
│       │   ├── FollowUpCreateDTO.java
│       │   ├── TagCreateDTO.java
│       │   ├── CustomerAssignDTO.java
│       │   ├── CustomerMergeDTO.java
│       │   ├── HighSeaClaimDTO.java
│       │   └── CustomerImportDTO.java
│       └── vo/                            # 视图对象
│           ├── CustomerVO.java
│           ├── ContactVO.java
│           ├── FollowUpVO.java
│           └── TagVO.java
└── src/main/resources/
    └── mapper/                            # MyBatis XML映射文件
        ├── CrmCustomerMapper.xml
        ├── CrmContactMapper.xml
        ├── CrmFollowUpMapper.xml
        └── CrmCustomerTagMapper.xml
```

## API接口说明

### 客户管理接口 (/customer/*)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /customer/page | 分页查询客户列表 |
| GET | /customer/{id} | 查询客户详情 |
| POST | /customer | 创建客户 |
| PUT | /customer | 更新客户 |
| DELETE | /customer/{ids} | 删除客户 |
| GET | /customer/check-duplicate | 客户查重检查 |
| GET | /customer/highsea/page | 获取公海池客户 |
| POST | /customer/highsea/claim | 从公海池领取 |
| POST | /customer/highsea/release | 释放到公海池 |
| POST | /customer/highsea/auto-recycle | 自动回收客户 |
| POST | /customer/import | 导入客户(Excel) |
| GET | /customer/export | 导出客户(Excel) |
| POST | /customer/assign | 分配客户 |
| POST | /customer/merge | 合并客户 |
| GET | /customer/generate-no | 生成客户编号 |

### 联系人管理接口 (/contact/*)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /contact/page | 分页查询联系人 |
| GET | /contact/{id} | 查询联系人详情 |
| GET | /contact/customer/{customerId} | 获取客户联系人列表 |
| POST | /contact | 创建联系人 |
| PUT | /contact | 更新联系人 |
| DELETE | /contact/{ids} | 删除联系人 |
| PUT | /contact/set-key-person | 设置关键人 |

### 跟进记录接口 (/followup/*)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /followup/page | 分页查询跟进记录 |
| GET | /followup/{id} | 查询跟进记录详情 |
| GET | /followup/timeline/{customerId} | 获取跟进时间轴 |
| POST | /followup | 创建跟进记录 |
| PUT | /followup/{id} | 更新跟进记录 |
| DELETE | /followup/{ids} | 删除跟进记录 |
| PUT | /followup/reminder/{followUpId} | 设置跟进提醒 |
| GET | /followup/today | 今日需跟进列表 |
| GET | /followup/upcoming | 即将到期提醒 |

### 标签管理接口 (/tag/*)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /tag/page | 分页查询标签 |
| GET | /tag/all | 获取所有标签 |
| GET | /tag/{id} | 查询标签详情 |
| POST | /tag | 创建标签 |
| PUT | /tag/{id} | 更新标签 |
| DELETE | /tag/{ids} | 删除标签 |
| POST | /tag/customer/{customerId}/add | 为客户打标签 |
| POST | /tag/customer/{customerId}/remove | 移除客户标签 |
| GET | /tag/customer/{customerId} | 获取客户标签 |
| POST | /tag/auto-tagging | 执行自动打标 |

## 数据库表说明

### crm_customer (客户表)
- 主键: id
- 唯一索引: customer_no (客户编号)
- 全文索引: company_name, company_name_cn (用于快速搜索)
- 普通索引: owner_id, level, country, created_time

### crm_contact (联系人表)
- 主键: id
- 外键: customer_id (关联客户表)
- 索引: email, phone (用于查重)

### crm_follow_up (跟进记录表)
- 主键: id
- 外键: customer_id, contact_id, follow_user_id
- 索引: next_follow_time (用于提醒查询), follow_time

### crm_tag (标签表)
- 主键: id
- 唯一索引: tag_name
- 字段: tag_type (manual/auto), auto_rule (JSON格式)

### crm_customer_tag (客户标签关联表)
- 联合主键: customer_id, tag_id

## 特色功能实现说明

### 1. 客户编号生成规则
- 格式: `CUS` + `yyyyMMdd` + `3位序号`
- 示例: CUS20260409001
- 实现: 每天从001开始递增

### 2. 客户查重机制
- 多维度查重: 公司名(模糊匹配)、邮箱(精确匹配)、电话(精确匹配)、网站(精确匹配)
- 使用全文索引提高搜索效率
- 返回所有可能的重复客户供用户确认

### 3. 公海池管理
- 定义: owner_id为NULL的客户属于公海池
- 领取: 将owner_id设置为当前用户
- 释放: 将owner_id设置为NULL
- 自动回收: 定时任务检查超过N天未跟进的客户，自动释放到公海池

### 4. 数据权限控制
- 预留@DataPermission注解位置
- 可根据部门负责人、自定义数据范围进行过滤
- 支持全部数据、本部门数据、本人数据等权限级别

## 待完善功能

以下功能已在代码中标记TODO，需要根据实际业务需求完善：

1. **Security集成**: 从SecurityContext获取当前用户ID
2. **关联数据补充**: VO中的关联字段（国家名称、负责人姓名等）需要从其他服务获取
3. **客户合并逻辑**: 需要实现联系人、跟进记录、订单的转移
4. **自动打标规则**: 需要解析JSON规则并执行匹配
5. **定时任务配置**: 配置公海池自动回收和自动打标的定时任务
6. **Excel模板下载**: 提供标准导入模板下载
7. **操作日志**: 记录重要操作的审计日志
8. **消息通知**: 跟进提醒、公海池领取等场景发送通知

## 编译和运行

### 前置条件
- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 编译命令
```bash
cd foreign-trade-crm
mvn clean install -pl crm-modules/module-customer -am
```

### 运行模块
```bash
mvn spring-boot:run -pl crm-modules/module-customer
```

## 注意事项

1. 本模块依赖common-core、common-security、common-mybatis等公共模块
2. 数据库表结构参考 docs/database/schema.sql
3. API文档访问地址: http://localhost:8080/doc.html
4. 所有接口都支持Swagger在线调试
5. 导入导出功能需要安装EasyExcel依赖

## 版本历史

- v1.0.0 (2026-04-09): 初始版本，完成基础功能开发

## 作者

CRM System Development Team
