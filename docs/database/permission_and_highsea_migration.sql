-- ============================================
-- 权限管理和公海客户系统迁移脚本
-- 创建日期: 2026-04-12
-- 说明: 添加用户角色、客户分配、公海自动回收等功能
-- ============================================

-- 1. 为 sys_user 表添加角色字段（简化版，用于快速判断）
ALTER TABLE sys_user 
ADD COLUMN role VARCHAR(20) DEFAULT 'sales' COMMENT '用户角色(admin管理员/sales业务员)' AFTER status;

-- 2. 为 crm_customer 表添加公海标记和最后跟进时间
ALTER TABLE crm_customer
ADD COLUMN is_high_sea TINYINT DEFAULT 0 COMMENT '是否在公海池(0否 1是)' AFTER owner_id,
ADD COLUMN last_follow_time DATETIME COMMENT '最后跟进时间' AFTER next_follow_time,
ADD INDEX idx_owner_id (owner_id),
ADD INDEX idx_is_high_sea (is_high_sea),
ADD INDEX idx_last_follow_time (last_follow_time);

-- 3. 插入默认角色数据到 sys_role 表
INSERT INTO sys_role (role_name, role_key, role_sort, data_scope, status, remark) VALUES
('系统管理员', 'admin', 1, '1', '0', '拥有所有权限，可查看所有客户'),
('业务员', 'sales', 2, '4', '0', '只能查看自己负责的客户')
ON DUPLICATE KEY UPDATE role_name = VALUES(role_name);

-- 4. 插入默认管理员账号（如果不存在）
-- 密码: admin123 (BCrypt加密后的值)
INSERT INTO sys_user (username, nickname, email, password, status, role, remark) VALUES
('admin', '系统管理员', 'admin@crm.com', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE/TUuXN.NN5tG', '0', 'admin', '系统超级管理员')
ON DUPLICATE KEY UPDATE username = VALUES(username);

-- 5. 关联管理员角色
INSERT INTO sys_user_role (user_id, role_id)
SELECT u.id, r.id FROM sys_user u, sys_role r
WHERE u.username = 'admin' AND r.role_key = 'admin'
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- 6. 创建定时任务配置表（用于自动回收客户到公海）
CREATE TABLE IF NOT EXISTS sys_job_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '任务ID',
    job_name VARCHAR(64) NOT NULL COMMENT '任务名称',
    job_group VARCHAR(64) NOT NULL COMMENT '任务组名',
    invoke_target VARCHAR(500) NOT NULL COMMENT '调用目标字符串',
    cron_expression VARCHAR(255) DEFAULT '' COMMENT 'cron执行表达式',
    misfire_policy VARCHAR(20) DEFAULT '3' COMMENT '计划执行错误策略(1立即执行 2执行一次 3放弃执行)',
    concurrent CHAR(1) DEFAULT '1' COMMENT '是否并发执行(0允许 1禁止)',
    status CHAR(1) DEFAULT '0' COMMENT '状态(0正常 1暂停)',
    created_by BIGINT COMMENT '创建者',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新者',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注信息',
    UNIQUE KEY uk_job_name_group (job_name, job_group)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定时任务配置表';

-- 7. 插入自动回收客户的定时任务配置（每天凌晨2点执行）
INSERT INTO sys_job_config (job_name, job_group, invoke_target, cron_expression, misfire_policy, concurrent, status, remark) VALUES
('autoRecycleHighSea', 'DEFAULT', 'customerService.autoRecycleToHighSea(7)', '0 0 2 * * ?', '3', '1', '0', '自动将超过7天未跟进的客户回收到公海池')
ON DUPLICATE KEY UPDATE job_name = VALUES(job_name);

-- 8. 更新现有客户数据：设置初始负责人为创建者
UPDATE crm_customer SET owner_id = created_by WHERE owner_id IS NULL OR owner_id = 0;

-- 9. 创建视图：我的客户（当前登录用户负责的客户）
CREATE OR REPLACE VIEW v_my_customers AS
SELECT c.*, 
       u.nickname as owner_name,
       CASE 
           WHEN c.last_follow_time IS NULL THEN DATEDIFF(NOW(), c.created_time)
           ELSE DATEDIFF(NOW(), c.last_follow_time)
       END as days_since_last_follow
FROM crm_customer c
LEFT JOIN sys_user u ON c.owner_id = u.id
WHERE c.deleted = 0;

-- 10. 创建视图：公海客户（无负责人或已释放的客户）
CREATE OR REPLACE VIEW v_high_sea_customers AS
SELECT c.*, 
       CASE 
           WHEN c.last_follow_time IS NULL THEN DATEDIFF(NOW(), c.created_time)
           ELSE DATEDIFF(NOW(), c.last_follow_time)
       END as days_since_last_follow
FROM crm_customer c
WHERE c.deleted = 0 
  AND (c.is_high_sea = 1 OR (c.owner_id IS NULL OR c.owner_id = 0));

-- 完成提示
SELECT '权限管理和公海客户系统迁移完成！' AS message;
