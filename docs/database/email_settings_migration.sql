-- ============================================
-- Email Settings and Folders Schema
-- 邮件设置和文件夹数据库表结构
-- ============================================

-- 用户邮件设置表 - 存储SMTP/IMAP配置
CREATE TABLE IF NOT EXISTS `sys_email_settings` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `email_address` VARCHAR(255) NOT NULL COMMENT '邮箱地址',
    `display_name` VARCHAR(100) COMMENT '显示名称',

    -- SMTP Configuration
    `smtp_host` VARCHAR(255) NOT NULL COMMENT 'SMTP服务器地址',
    `smtp_port` INT DEFAULT 465 COMMENT 'SMTP端口',
    `smtp_username` VARCHAR(255) NOT NULL COMMENT 'SMTP用户名',
    `smtp_password` VARCHAR(500) NOT NULL COMMENT 'SMTP密码/授权码（加密存储）',
    `smtp_encryption` VARCHAR(10) DEFAULT 'ssl' COMMENT 'SMTP加密方式(none/ssl/tls)',
    `smtp_auth` TINYINT(1) DEFAULT 1 COMMENT '是否需要SMTP认证',

    -- IMAP Configuration
    `imap_host` VARCHAR(255) NOT NULL COMMENT 'IMAP服务器地址',
    `imap_port` INT DEFAULT 993 COMMENT 'IMAP端口',
    `imap_username` VARCHAR(255) NOT NULL COMMENT 'IMAP用户名',
    `imap_password` VARCHAR(500) NOT NULL COMMENT 'IMAP密码/授权码（加密存储）',
    `imap_encryption` VARCHAR(10) DEFAULT 'ssl' COMMENT 'IMAP加密方式(none/ssl/tls)',

    -- Settings
    `enabled` TINYINT(1) DEFAULT 1 COMMENT '是否启用',
    `is_default` TINYINT(1) DEFAULT 0 COMMENT '是否为默认账户',
    `sync_interval` INT DEFAULT 5 COMMENT '同步间隔(分钟)',
    `keep_server_copy` TINYINT(1) DEFAULT 1 COMMENT '保留服务器副本',
    `email_signature` TEXT COMMENT '自动添加签名',
    `page_size` INT DEFAULT 20 COMMENT '每页显示数量',

    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_email_address` (`email_address`, `deleted`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_is_default` (`is_default`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户邮件设置表';

-- 邮件文件夹表
CREATE TABLE IF NOT EXISTS `msg_email_folder` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `folder_name` VARCHAR(50) NOT NULL COMMENT '文件夹名称(inbox/sent/drafts/trash/spam/archive)',
    `display_name` VARCHAR(100) NOT NULL COMMENT '显示名称',
    `folder_type` VARCHAR(20) DEFAULT 'custom' COMMENT '文件夹类型(system/custom)',
    `parent_id` BIGINT DEFAULT NULL COMMENT '父文件夹ID',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `unread_count` INT DEFAULT 0 COMMENT '未读数量',
    `total_count` INT DEFAULT 0 COMMENT '总数量',
    `color` VARCHAR(20) COMMENT '文件夹颜色',
    `icon` VARCHAR(50) COMMENT '图标',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除',

    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_folder_name` (`folder_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='邮件文件夹表';

-- 邮件标签表
CREATE TABLE IF NOT EXISTS `msg_email_label` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `label_name` VARCHAR(50) NOT NULL COMMENT '标签名称',
    `color` VARCHAR(20) DEFAULT '#409EFF' COMMENT '标签颜色',
    `description` VARCHAR(255) COMMENT '描述',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除',

    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='邮件标签表';

-- 扩展现有邮件日志表，添加更多字段
ALTER TABLE `msg_email_log`
ADD COLUMN `folder` VARCHAR(50) DEFAULT 'sent' COMMENT '文件夹' AFTER `status`,
ADD COLUMN `starred` TINYINT(1) DEFAULT 0 COMMENT '是否星标' AFTER `folder`,
ADD COLUMN `read` TINYINT(1) DEFAULT 0 COMMENT '是否已读' AFTER `starred`,
ADD COLUMN `replied` TINYINT(1) DEFAULT 0 COMMENT '是否已回复' AFTER `read`,
ADD COLUMN `forwarded` TINYINT(1) DEFAULT 0 COMMENT '是否已转发' AFTER `replied`,
ADD COLUMN `priority` VARCHAR(10) DEFAULT 'normal' COMMENT '优先级(low/normal/high)' AFTER `forwarded`,
ADD COLUMN `message_id` VARCHAR(255) COMMENT '邮件Message-ID' AFTER `priority`,
ADD COLUMN `in_reply_to` VARCHAR(255) COMMENT '回复的邮件ID' AFTER `message_id`,
ADD COLUMN `references` TEXT COMMENT '邮件引用链' AFTER `in_reply_to`,
ADD COLUMN `html_content` LONGTEXT COMMENT 'HTML内容' AFTER `content`,
ADD COLUMN `text_content` LONGTEXT COMMENT '纯文本内容' AFTER `html_content`,
ADD COLUMN `received_time` DATETIME COMMENT '接收时间' AFTER `sent_time`,
ADD INDEX `idx_folder` (`folder`),
ADD INDEX `idx_starred` (`starred`),
ADD INDEX `idx_read` (`read`);

-- 邮件-标签关联表
CREATE TABLE IF NOT EXISTS `msg_email_label_relation` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `email_id` BIGINT NOT NULL COMMENT '邮件ID',
    `label_id` BIGINT NOT NULL COMMENT '标签ID',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_email_label` (`email_id`, `label_id`),
    KEY `idx_label_id` (`label_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='邮件-标签关联表';

-- 插入默认文件夹数据
INSERT INTO `msg_email_folder` (`id`, `user_id`, `folder_name`, `display_name`, `folder_type`, `sort_order`, `icon`) VALUES
(1, 1, 'inbox', '收件箱', 'system', 1, 'Message'),
(2, 1, 'starred', '星标邮件', 'system', 2, 'Star'),
(3, 1, 'sent', '已发送', 'system', 3, 'Promotion'),
(4, 1, 'drafts', '草稿箱', 'system', 4, 'Document'),
(5, 1, 'archive', '归档', 'system', 5, 'Box'),
(6, 1, 'spam', '垃圾邮件', 'system', 6, 'WarnTriangleFilled'),
(7, 1, 'trash', '已删除', 'system', 7, 'Delete');

-- 插入示例邮件标签
INSERT INTO `msg_email_label` (`id`, `user_id`, `label_name`, `color`, `sort_order`) VALUES
(1, 1, '重要', '#F56C6C', 1),
(2, 1, '工作', '#409EFF', 2),
(3, 1, '个人', '#67C23A', 3),
(4, 1, '财务', '#E6A23C', 4),
(5, 1, '客户', '#909399', 5);
