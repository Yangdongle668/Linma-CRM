-- ============================================
-- Enhanced Customer Information Schema
-- 增强版客户信息数据库表结构
-- ============================================

-- 扩展客户表，添加更多属性
ALTER TABLE `crm_customer`
ADD COLUMN `priority` VARCHAR(10) DEFAULT 'medium' COMMENT '优先级(high/medium/low)' AFTER `level`,
ADD COLUMN `company_size` VARCHAR(20) COMMENT '公司规模(1-10人/11-50人/51-200人/201-500人/500+人)' AFTER `industry`,
ADD COLUMN `annual_revenue` DECIMAL(18,2) COMMENT '年营业额(美元)' AFTER `company_size`,
ADD COLUMN `established_year` INT COMMENT '成立年份' AFTER `annual_revenue`,
ADD COLUMN `business_type` VARCHAR(20) COMMENT '业务类型(manufacturer/trader/distributor/retailer)' AFTER `industry`,
ADD COLUMN `main_products` TEXT COMMENT '主营产品' AFTER `business_type`,
ADD COLUMN `main_markets` VARCHAR(500) COMMENT '主要市场(多国用逗号分隔)' AFTER `main_products`,
ADD COLUMN `email_domain` VARCHAR(255) COMMENT '邮箱域名(自动从邮箱提取)' AFTER `website`,
ADD COLUMN `social_linkedin` VARCHAR(500) COMMENT 'LinkedIn主页' AFTER `website`,
ADD COLUMN `social_facebook` VARCHAR(500) COMMENT 'Facebook主页' AFTER `social_linkedin`,
ADD COLUMN `social_twitter` VARCHAR(500) COMMENT 'Twitter主页' AFTER `social_facebook`,
ADD COLUMN `phone_country_code` VARCHAR(10) COMMENT '电话国家代码' AFTER `website`,
ADD COLUMN `fax` VARCHAR(50) COMMENT '传真号码' AFTER `phone_country_code`,
ADD COLUMN `timezone` VARCHAR(50) COMMENT '时区' AFTER `country`,
ADD COLUMN `language_preference` VARCHAR(20) DEFAULT 'en' COMMENT '语言偏好(en/zh/es/fr等)' AFTER `timezone`,
ADD COLUMN `payment_terms` VARCHAR(100) COMMENT '付款条件(T/T, L/C等)' AFTER `level`,
ADD COLUMN `credit_rating` VARCHAR(10) COMMENT '信用评级(AAA/AA/A/B/C)' AFTER `payment_terms`,
ADD COLUMN `tax_id` VARCHAR(50) COMMENT '税号/VAT号' AFTER `address`,
ADD COLUMN `registration_number` VARCHAR(50) COMMENT '注册号' AFTER `tax_id`,
ADD COLUMN `import_license` VARCHAR(100) COMMENT '进口许可证号' AFTER `registration_number`,
ADD COLUMN `certification_iso` TINYINT(1) DEFAULT 0 COMMENT 'ISO认证' AFTER `import_license`,
ADD COLUMN `certification_ce` TINYINT(1) DEFAULT 0 COMMENT 'CE认证' AFTER `certification_iso`,
ADD COLUMN `certification_fda` TINYINT(1) DEFAULT 0 COMMENT 'FDA认证' AFTER `certification_ce`,
ADD COLUMN `competitor_info` TEXT COMMENT '竞争对手信息' AFTER `remark`,
ADD COLUMN `swot_analysis` TEXT COMMENT 'SWOT分析' AFTER `competitor_info`,
ADD COLUMN `customer_avatar` VARCHAR(500) COMMENT '客户头像URL' AFTER `company_name`,
ADD COLUMN `company_logo` VARCHAR(500) COMMENT '公司Logo URL' AFTER `customer_avatar`,
ADD COLUMN `last_email_time` DATETIME COMMENT '最后邮件联系时间' AFTER `last_order_date`,
ADD COLUMN `email_count` INT DEFAULT 0 COMMENT '邮件往来次数' AFTER `last_email_time`,
ADD COLUMN `last_call_time` DATETIME COMMENT '最后电话沟通时间' AFTER `last_email_time`,
ADD COLUMN `call_count` INT DEFAULT 0 COMMENT '电话沟通次数' AFTER `last_call_time`,
ADD COLUMN `last_meeting_time` DATETIME COMMENT '最后会议时间' AFTER `last_call_time`,
ADD COLUMN `meeting_count` INT DEFAULT 0 COMMENT '会议次数' AFTER `last_meeting_time`,
ADD COLUMN `satisfaction_score` INT COMMENT '满意度评分(1-5)' AFTER `meeting_count`,
ADD COLUMN `nps_score` INT COMMENT '净推荐值(-100到100)' AFTER `satisfaction_score`,
ADD COLUMN `risk_level` VARCHAR(10) DEFAULT 'low' COMMENT '风险等级(low/medium/high)' AFTER `level`,
ADD COLUMN `risk_factors` TEXT COMMENT '风险因素' AFTER `risk_level`,
ADD COLUMN `opportunity_value` DECIMAL(18,2) COMMENT '商机价值预估' AFTER `total_order`,
ADD COLUMN `conversion_probability` INT COMMENT '转化概率(0-100%)' AFTER `opportunity_value`,
ADD COLUMN `expected_close_date` DATE COMMENT '预计成交日期' AFTER `conversion_probability`,
ADD COLUMN `acquisition_channel` VARCHAR(50) COMMENT '获客渠道(SEO/SEM/社媒/展会/ referral等)' AFTER `source`,
ADD COLUMN `campaign_source` VARCHAR(100) COMMENT '营销活动来源' AFTER `acquisition_channel`,
ADD COLUMN `first_contact_date` DATE COMMENT '首次接触日期' AFTER `created_time`,
ADD COLUMN `customer_lifecycle` VARCHAR(20) DEFAULT 'prospect' COMMENT '客户生命周期(prospect/lead/opportunity/customer/churned)' AFTER `status`,
ADD COLUMN `tags_json` JSON COMMENT '标签(JSON格式)' AFTER `remark`,
ADD COLUMN `custom_fields` JSON COMMENT '自定义字段(JSON)' AFTER `tags_json`,
ADD INDEX `idx_priority` (`priority`),
ADD INDEX `idx_industry` (`industry`),
ADD INDEX `idx_email_domain` (`email_domain`),
ADD INDEX `idx_customer_lifecycle` (`customer_lifecycle`),
ADD INDEX `idx_risk_level` (`risk_level`);

-- 创建客户跟进记录表
CREATE TABLE IF NOT EXISTS `crm_follow_up_record` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `customer_id` BIGINT NOT NULL COMMENT '客户ID',
    `contact_id` BIGINT COMMENT '联系人ID',
    `follow_type` VARCHAR(20) NOT NULL COMMENT '跟进类型(email/call/meeting/visit/message/other)',
    `subject` VARCHAR(200) COMMENT '主题',
    `content` TEXT COMMENT '跟进内容',
    `direction` VARCHAR(10) DEFAULT 'outbound' COMMENT '方向(inbound/outbound)',
    `status` VARCHAR(20) DEFAULT 'completed' COMMENT '状态(planned/completed/cancelled)',
    `start_time` DATETIME COMMENT '开始时间',
    `end_time` DATETIME COMMENT '结束时间',
    `duration_minutes` INT COMMENT '持续时间(分钟)',
    `location` VARCHAR(200) COMMENT '地点',
    `participants` JSON COMMENT '参与人员(JSON数组)',
    `outcome` TEXT COMMENT '跟进结果',
    `next_step` TEXT COMMENT '下一步行动',
    `next_follow_date` DATE COMMENT '下次跟进日期',
    `sentiment` VARCHAR(10) COMMENT '情感倾向(positive/neutral/negative)',
    `importance` VARCHAR(10) DEFAULT 'normal' COMMENT '重要性(low/normal/high/urgent)',
    `related_inquiry_id` BIGINT COMMENT '关联询盘ID',
    `related_order_id` BIGINT COMMENT '关联订单ID',
    `related_contract_id` BIGINT COMMENT '关联合同ID',
    `email_message_id` VARCHAR(255) COMMENT '关联邮件Message-ID',
    `attachments_json` JSON COMMENT '附件列表(JSON)',
    `created_by` BIGINT COMMENT '创建人ID',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除',

    PRIMARY KEY (`id`),
    KEY `idx_customer_id` (`customer_id`),
    KEY `idx_follow_type` (`follow_type`),
    KEY `idx_status` (`status`),
    KEY `idx_next_follow_date` (`next_follow_date`),
    KEY `idx_created_time` (`created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户跟进记录表';

-- 创建客户活动_timeline_表（整合所有客户交互）
CREATE TABLE IF NOT EXISTS `crm_customer_activity` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `customer_id` BIGINT NOT NULL COMMENT '客户ID',
    `activity_type` VARCHAR(30) NOT NULL COMMENT '活动类型(email_sent/email_received/call/meeting/inquiry/order/follow_up/note)',
    `activity_title` VARCHAR(200) NOT NULL COMMENT '活动标题',
    `activity_summary` TEXT COMMENT '活动摘要',
    `activity_data` JSON COMMENT '活动详细数据(JSON)',
    `related_id` BIGINT COMMENT '关联记录ID',
    `related_type` VARCHAR(30) COMMENT '关联记录类型',
    `visibility` VARCHAR(10) DEFAULT 'team' COMMENT '可见范围(private/team/public)',
    `created_by` BIGINT COMMENT '创建人ID',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除',

    PRIMARY KEY (`id`),
    KEY `idx_customer_id` (`customer_id`),
    KEY `idx_activity_type` (`activity_type`),
    KEY `idx_created_time` (`created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户活动Timeline表';

-- 创建客户文档附件表
CREATE TABLE IF NOT EXISTS `crm_customer_attachment` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `customer_id` BIGINT NOT NULL COMMENT '客户ID',
    `file_name` VARCHAR(255) NOT NULL COMMENT '文件名',
    `file_path` VARCHAR(500) NOT NULL COMMENT '文件路径',
    `file_size` BIGINT COMMENT '文件大小(字节)',
    `file_type` VARCHAR(50) COMMENT '文件类型',
    `category` VARCHAR(30) COMMENT '分类(contract/quotation/certification/other)',
    `description` VARCHAR(500) COMMENT '描述',
    `uploaded_by` BIGINT COMMENT '上传人ID',
    `uploaded_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    `deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除',

    PRIMARY KEY (`id`),
    KEY `idx_customer_id` (`customer_id`),
    KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户文档附件表';

-- 插入示例行业分类数据
INSERT INTO `sys_dict_data` (`dict_type`, `dict_label`, `dict_value`, `dict_sort`, `status`) VALUES
('customer_industry', '电子产品', 'electronics', 1, '0'),
('customer_industry', '机械制造', 'machinery', 2, '0'),
('customer_industry', '纺织服装', 'textile', 3, '0'),
('customer_industry', '化工材料', 'chemical', 4, '0'),
('customer_industry', '食品饮料', 'food_beverage', 5, '0'),
('customer_industry', '医疗器械', 'medical', 6, '0'),
('customer_industry', '汽车配件', 'automotive', 7, '0'),
('customer_industry', '建筑材料', 'construction', 8, '0'),
('customer_industry', '家居用品', 'home_goods', 9, '0'),
('customer_industry', '其他', 'other', 99, '0')
ON DUPLICATE KEY UPDATE dict_label=VALUES(dict_label);

-- 插入示例优先级数据
INSERT INTO `sys_dict_data` (`dict_type`, `dict_label`, `dict_value`, `dict_sort`, `status`) VALUES
('customer_priority', '高优先级', 'high', 1, '0'),
('customer_priority', '中优先级', 'medium', 2, '0'),
('customer_priority', '低优先级', 'low', 3, '0')
ON DUPLICATE KEY UPDATE dict_label=VALUES(dict_label);

-- 插入示例业务类型数据
INSERT INTO `sys_dict_data` (`dict_type`, `dict_label`, `dict_value`, `dict_sort`, `status`) VALUES
('business_type', '制造商', 'manufacturer', 1, '0'),
('business_type', '贸易商', 'trader', 2, '0'),
('business_type', '分销商', 'distributor', 3, '0'),
('business_type', '零售商', 'retailer', 4, '0'),
('business_type', '批发商', 'wholesaler', 5, '0')
ON DUPLICATE KEY UPDATE dict_label=VALUES(dict_label);
