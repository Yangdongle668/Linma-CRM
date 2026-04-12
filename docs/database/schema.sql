-- =============================================
-- 外贸CRM系统数据库初始化脚本
-- 版本: v1.0
-- 日期: 2026-04-09
-- =============================================

-- 创建数据库
DROP DATABASE IF EXISTS crm_db;
CREATE DATABASE crm_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE crm_db;

-- =============================================
-- 系统管理表 (sys_前缀)
-- =============================================

-- 部门表
CREATE TABLE sys_dept (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '部门ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父部门ID',
    ancestors VARCHAR(500) DEFAULT '' COMMENT '祖级列表',
    dept_name VARCHAR(30) NOT NULL COMMENT '部门名称',
    order_num INT DEFAULT 0 COMMENT '显示顺序',
    leader VARCHAR(20) COMMENT '负责人',
    phone VARCHAR(11) COMMENT '联系电话',
    email VARCHAR(50) COMMENT '邮箱',
    status CHAR(1) DEFAULT '0' COMMENT '状态(0正常 1停用)',
    created_by BIGINT COMMENT '创建者',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新者',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 用户表
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    dept_id BIGINT COMMENT '部门ID',
    username VARCHAR(64) UNIQUE NOT NULL COMMENT '用户名',
    nickname VARCHAR(30) COMMENT '昵称',
    email VARCHAR(128) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    gender CHAR(1) DEFAULT '0' COMMENT '性别(0男 1女 2未知)',
    avatar VARCHAR(255) COMMENT '头像URL',
    password VARCHAR(100) NOT NULL COMMENT '密码(BCrypt加密)',
    status CHAR(1) DEFAULT '0' COMMENT '状态(0正常 1停用)',
    login_ip VARCHAR(128) COMMENT '最后登录IP',
    login_date DATETIME COMMENT '最后登录时间',
    pwd_update_date DATETIME COMMENT '密码最后更新时间',
    created_by BIGINT COMMENT '创建者',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新者',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_dept (dept_id),
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 角色表
CREATE TABLE sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    role_name VARCHAR(30) NOT NULL COMMENT '角色名称',
    role_key VARCHAR(100) NOT NULL COMMENT '角色权限字符串',
    role_sort INT DEFAULT 0 COMMENT '显示顺序',
    data_scope CHAR(1) DEFAULT '1' COMMENT '数据范围(1全部 2本部门 3本部门及以下 4仅本人 5自定义)',
    status CHAR(1) DEFAULT '0' COMMENT '状态(0正常 1停用)',
    created_by BIGINT COMMENT '创建者',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新者',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 菜单表
CREATE TABLE sys_menu (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '菜单ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父菜单ID',
    menu_name VARCHAR(50) NOT NULL COMMENT '菜单名称',
    menu_type CHAR(1) NOT NULL COMMENT '菜单类型(M目录 C菜单 F按钮)',
    path VARCHAR(200) DEFAULT '' COMMENT '路由地址',
    component VARCHAR(255) DEFAULT NULL COMMENT '组件路径',
    perms VARCHAR(100) COMMENT '权限标识',
    icon VARCHAR(100) DEFAULT '#' COMMENT '菜单图标',
    order_num INT DEFAULT 0 COMMENT '显示顺序',
    visible CHAR(1) DEFAULT '0' COMMENT '是否显示(0显示 1隐藏)',
    status CHAR(1) DEFAULT '0' COMMENT '状态(0正常 1停用)',
    created_by BIGINT COMMENT '创建者',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新者',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- 用户角色关联表
CREATE TABLE sys_user_role (
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 角色菜单关联表
CREATE TABLE sys_role_menu (
    role_id BIGINT NOT NULL COMMENT '角色ID',
    menu_id BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (role_id, menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- 字典类型表
CREATE TABLE sys_dict_type (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '字典ID',
    dict_name VARCHAR(100) NOT NULL COMMENT '字典名称',
    dict_type VARCHAR(100) NOT NULL UNIQUE COMMENT '字典类型',
    status CHAR(1) DEFAULT '0' COMMENT '状态(0正常 1停用)',
    created_by BIGINT COMMENT '创建者',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新者',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典类型表';

-- 字典数据表
CREATE TABLE sys_dict_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '字典数据ID',
    dict_type VARCHAR(100) NOT NULL COMMENT '字典类型',
    dict_label VARCHAR(100) NOT NULL COMMENT '字典标签',
    dict_value VARCHAR(100) NOT NULL COMMENT '字典键值',
    dict_sort INT DEFAULT 0 COMMENT '字典排序',
    css_class VARCHAR(100) COMMENT '样式属性',
    list_class VARCHAR(100) COMMENT '表格回显样式',
    is_default CHAR(1) DEFAULT 'N' COMMENT '是否默认(Y是 N否)',
    status CHAR(1) DEFAULT '0' COMMENT '状态(0正常 1停用)',
    created_by BIGINT COMMENT '创建者',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新者',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_dict_type (dict_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典数据表';

-- 参数配置表
CREATE TABLE sys_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '参数ID',
    config_name VARCHAR(100) NOT NULL COMMENT '参数名称',
    config_key VARCHAR(100) NOT NULL UNIQUE COMMENT '参数键名',
    config_value VARCHAR(500) NOT NULL COMMENT '参数键值',
    config_type CHAR(1) DEFAULT 'N' COMMENT '系统内置(Y是 N否)',
    remark VARCHAR(500) COMMENT '备注',
    created_by BIGINT COMMENT '创建者',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新者',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='参数配置表';

-- 操作日志表
CREATE TABLE sys_oper_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    title VARCHAR(50) COMMENT '模块标题',
    business_type INT DEFAULT 0 COMMENT '业务类型(0其它 1新增 2修改 3删除)',
    method VARCHAR(100) COMMENT '方法名称',
    request_method VARCHAR(10) COMMENT '请求方式',
    operator_type INT DEFAULT 0 COMMENT '操作类别(0其它 1后台用户 2手机端用户)',
    oper_name VARCHAR(50) COMMENT '操作人员',
    dept_name VARCHAR(50) COMMENT '部门名称',
    oper_url VARCHAR(255) COMMENT '请求URL',
    oper_ip VARCHAR(128) COMMENT '主机地址',
    oper_location VARCHAR(255) COMMENT '操作地点',
    oper_param VARCHAR(2000) COMMENT '请求参数',
    json_result VARCHAR(2000) COMMENT '返回参数',
    status INT DEFAULT 0 COMMENT '操作状态(0正常 1异常)',
    error_msg VARCHAR(2000) COMMENT '错误消息',
    oper_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    cost_time BIGINT DEFAULT 0 COMMENT '消耗时间',
    INDEX idx_oper_name (oper_name),
    INDEX idx_oper_time (oper_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 登录日志表
CREATE TABLE sys_logininfor (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '访问ID',
    username VARCHAR(50) COMMENT '用户账号',
    ipaddr VARCHAR(128) COMMENT '登录IP地址',
    login_location VARCHAR(255) COMMENT '登录地点',
    browser VARCHAR(50) COMMENT '浏览器类型',
    os VARCHAR(50) COMMENT '操作系统',
    status CHAR(1) DEFAULT '0' COMMENT '登录状态(0成功 1失败)',
    msg VARCHAR(255) COMMENT '提示消息',
    login_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
    INDEX idx_username (username),
    INDEX idx_login_time (login_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';

-- =============================================
-- 客户管理表 (crm_前缀)
-- =============================================

-- 客户表
CREATE TABLE crm_customer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '客户ID',
    customer_no VARCHAR(32) UNIQUE NOT NULL COMMENT '客户编号',
    company_name VARCHAR(200) NOT NULL COMMENT '公司名称(英文)',
    company_name_cn VARCHAR(200) COMMENT '公司名称(中文)',
    country VARCHAR(50) COMMENT '国家代码',
    province VARCHAR(50) COMMENT '省份/州',
    city VARCHAR(50) COMMENT '城市',
    address VARCHAR(500) COMMENT '详细地址',
    website VARCHAR(200) COMMENT '公司网站',
    industry VARCHAR(50) COMMENT '行业分类',
    source VARCHAR(50) COMMENT '客户来源(展会/阿里/官网/转介绍)',
    level CHAR(1) DEFAULT 'C' COMMENT '客户等级(A/B/C/D)',
    status TINYINT DEFAULT 1 COMMENT '状态(1正常 0禁用)',
    owner_id BIGINT COMMENT '负责人ID',
    department_id BIGINT COMMENT '所属部门',
    next_follow_time DATETIME COMMENT '下次跟进时间',
    total_inquiry INT DEFAULT 0 COMMENT '询盘总数',
    total_order DECIMAL(15,2) DEFAULT 0 COMMENT '累计订单金额',
    last_order_date DATE COMMENT '最后下单日期',
    created_by BIGINT COMMENT '创建者',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新者',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    remark TEXT COMMENT '备注',
    INDEX idx_owner (owner_id),
    INDEX idx_level (level),
    INDEX idx_country (country),
    INDEX idx_created_time (created_time),
    FULLTEXT INDEX ft_company_name (company_name, company_name_cn)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户表';

-- 联系人表
CREATE TABLE crm_contact (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '联系人ID',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    first_name VARCHAR(50) NOT NULL COMMENT '名',
    last_name VARCHAR(50) NOT NULL COMMENT '姓',
    full_name VARCHAR(100) COMMENT '全名',
    gender CHAR(1) COMMENT '性别',
    position VARCHAR(100) COMMENT '职位',
    department VARCHAR(100) COMMENT '部门',
    phone VARCHAR(20) COMMENT '手机',
    tel VARCHAR(20) COMMENT '电话',
    email VARCHAR(128) COMMENT '邮箱',
    wechat VARCHAR(50) COMMENT '微信',
    linkedin VARCHAR(200) COMMENT 'LinkedIn',
    is_key_person CHAR(1) DEFAULT '0' COMMENT '是否关键人(0否 1是)',
    key_person_type VARCHAR(20) COMMENT '关键人类型(decision_maker决策者 influencer影响者 user使用者 gatekeeper把关者)',
    birthday DATE COMMENT '生日',
    language_pref VARCHAR(20) DEFAULT 'en' COMMENT '语言偏好',
    communication_pref VARCHAR(20) COMMENT '沟通方式偏好',
    created_by BIGINT COMMENT '创建者',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新者',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    remark TEXT COMMENT '备注',
    INDEX idx_customer (customer_id),
    INDEX idx_email (email),
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='联系人表';

-- 跟进记录表
CREATE TABLE crm_follow_up (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '跟进ID',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    contact_id BIGINT COMMENT '联系人ID',
    follow_type VARCHAR(20) NOT NULL COMMENT '跟进方式(email/phone/wechat/meeting/video)',
    follow_content TEXT COMMENT '跟进内容',
    next_plan TEXT COMMENT '下一步计划',
    next_follow_time DATETIME COMMENT '下次跟进时间',
    attachment_urls JSON COMMENT '附件URL列表',
    duration INT COMMENT '通话时长(秒)',
    satisfaction TINYINT COMMENT '客户满意度(1-5星)',
    follow_user_id BIGINT NOT NULL COMMENT '跟进人ID',
    follow_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '跟进时间',
    location VARCHAR(100) COMMENT '拜访地点(GPS坐标)',
    photos JSON COMMENT '拜访照片',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_customer (customer_id),
    INDEX idx_follow_user (follow_user_id),
    INDEX idx_next_time (next_follow_time),
    INDEX idx_follow_time (follow_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='跟进记录表';

-- 客户标签表
CREATE TABLE crm_tag (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '标签ID',
    tag_name VARCHAR(50) NOT NULL COMMENT '标签名称',
    tag_color VARCHAR(20) COMMENT '标签颜色',
    tag_type VARCHAR(20) DEFAULT 'manual' COMMENT '标签类型(manual手动 auto自动)',
    auto_rule TEXT COMMENT '自动打标规则',
    usage_count INT DEFAULT 0 COMMENT '使用次数',
    created_by BIGINT COMMENT '创建者',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY uk_tag_name (tag_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户标签表';

-- 客户标签关联表
CREATE TABLE crm_customer_tag (
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    tag_id BIGINT NOT NULL COMMENT '标签ID',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (customer_id, tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户标签关联表';

-- =============================================
-- 询盘管理表
-- =============================================

CREATE TABLE crm_inquiry (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '询盘ID',
    inquiry_no VARCHAR(32) UNIQUE NOT NULL COMMENT '询盘编号',
    customer_id BIGINT COMMENT '客户ID',
    contact_id BIGINT COMMENT '联系人ID',
    source VARCHAR(50) COMMENT '询盘来源(alibaba/website/email/exhibition)',
    priority VARCHAR(10) DEFAULT 'MEDIUM' COMMENT '优先级(HIGH/MEDIUM/LOW)',
    status VARCHAR(20) DEFAULT 'NEW' COMMENT '状态(NEW新建 PROCESSING处理中 QUOTED已报价 CONVERTED已转化 CLOSED已关闭)',
    assigned_to BIGINT COMMENT '分配给',
    product_interest TEXT COMMENT '感兴趣产品',
    quantity INT COMMENT '数量',
    target_price DECIMAL(15,2) COMMENT '目标价格',
    currency VARCHAR(3) DEFAULT 'USD' COMMENT '币种',
    deadline DATE COMMENT '要求交期',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    closed_time DATETIME COMMENT '关闭时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    remark TEXT COMMENT '备注',
    INDEX idx_assigned (assigned_to),
    INDEX idx_status (status),
    INDEX idx_created (created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='询盘表';

-- =============================================
-- 报价单管理表
-- =============================================

CREATE TABLE crm_quotation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '报价单ID',
    quotation_no VARCHAR(32) UNIQUE NOT NULL COMMENT '报价单号',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    contact_id BIGINT COMMENT '联系人ID',
    inquiry_id BIGINT COMMENT '关联询盘ID',
    version INT DEFAULT 1 COMMENT '版本号',
    currency VARCHAR(3) DEFAULT 'USD' COMMENT '币种',
    exchange_rate DECIMAL(10,4) COMMENT '汇率',
    trade_term VARCHAR(20) COMMENT '贸易术语(FOB/CIF/DDP)',
    port_of_loading VARCHAR(100) COMMENT '装运港',
    port_of_discharge VARCHAR(100) COMMENT '目的港',
    delivery_days INT COMMENT '交货期(天)',
    validity_days INT DEFAULT 30 COMMENT '有效期(天)',
    valid_until DATE COMMENT '有效期至',
    payment_terms VARCHAR(200) COMMENT '付款条款',
    total_amount DECIMAL(15,2) COMMENT '总金额',
    cost_amount DECIMAL(15,2) COMMENT '成本金额',
    profit_rate DECIMAL(5,2) COMMENT '利润率(%)',
    discount_rate DECIMAL(5,2) DEFAULT 0 COMMENT '折扣率(%)',
    status TINYINT DEFAULT 1 COMMENT '状态(1草稿 2已发送 3已接受 4已拒绝 5已过期)',
    pdf_url VARCHAR(500) COMMENT 'PDF文件路径',
    remarks TEXT COMMENT '备注',
    sales_id BIGINT COMMENT '业务员ID',
    approved_by BIGINT COMMENT '审批人ID',
    approved_time DATETIME COMMENT '审批时间',
    sent_time DATETIME COMMENT '发送时间',
    created_by BIGINT COMMENT '创建者',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新者',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_customer (customer_id),
    INDEX idx_sales (sales_id),
    INDEX idx_status (status),
    INDEX idx_valid_until (valid_until)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报价单表';

CREATE TABLE crm_quotation_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
    quotation_id BIGINT NOT NULL COMMENT '报价单ID',
    product_id BIGINT COMMENT '产品ID',
    product_name VARCHAR(200) NOT NULL COMMENT '产品名称',
    description TEXT COMMENT '产品描述',
    quantity INT NOT NULL COMMENT '数量',
    unit VARCHAR(20) DEFAULT 'PCS' COMMENT '单位',
    unit_price DECIMAL(15,2) NOT NULL COMMENT '单价',
    discount_rate DECIMAL(5,2) DEFAULT 0 COMMENT '折扣率(%)',
    tax_rate DECIMAL(5,2) DEFAULT 0 COMMENT '税率(%)',
    total_price DECIMAL(15,2) COMMENT '总价',
    cost_price DECIMAL(15,2) COMMENT '成本价',
    sort_order INT DEFAULT 0 COMMENT '排序',
    remark VARCHAR(500) COMMENT '备注',
    INDEX idx_quotation (quotation_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报价单明细表';

-- =============================================
-- 订单管理表
-- =============================================

CREATE TABLE crm_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_no VARCHAR(32) UNIQUE NOT NULL COMMENT '订单号',
    quotation_id BIGINT COMMENT '关联报价单ID',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    contact_id BIGINT COMMENT '联系人ID',
    order_date DATE NOT NULL COMMENT '订单日期',
    required_delivery_date DATE COMMENT '要求交货期',
    actual_delivery_date DATE COMMENT '实际交货期',
    currency VARCHAR(3) DEFAULT 'USD' COMMENT '币种',
    exchange_rate DECIMAL(10,4) COMMENT '汇率',
    trade_term VARCHAR(20) COMMENT '贸易术语',
    port_of_loading VARCHAR(100) COMMENT '装运港',
    port_of_discharge VARCHAR(100) COMMENT '目的港',
    payment_terms VARCHAR(200) COMMENT '付款条款',
    deposit_ratio DECIMAL(5,2) COMMENT '定金比例(%)',
    total_amount DECIMAL(15,2) COMMENT '总金额',
    received_amount DECIMAL(15,2) DEFAULT 0 COMMENT '已收金额',
    status VARCHAR(20) DEFAULT 'PENDING_CONFIRM' COMMENT '状态(PENDING_CONFIRM待确认 IN_PRODUCTION生产中 COMPLETED已完工 PENDING_SHIPMENT待发货 SHIPPED已发货 COMPLETED已完成 CANCELLED已取消)',
    production_progress INT DEFAULT 0 COMMENT '生产进度(%)',
    pi_url VARCHAR(500) COMMENT '形式发票URL',
    ci_url VARCHAR(500) COMMENT '商业发票URL',
    sales_id BIGINT COMMENT '业务员ID',
    created_by BIGINT COMMENT '创建者',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新者',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    remark TEXT COMMENT '备注',
    INDEX idx_customer (customer_id),
    INDEX idx_sales (sales_id),
    INDEX idx_status (status),
    INDEX idx_order_date (order_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

CREATE TABLE crm_order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    product_id BIGINT COMMENT '产品ID',
    product_name VARCHAR(200) NOT NULL COMMENT '产品名称',
    sku VARCHAR(50) COMMENT 'SKU',
    description TEXT COMMENT '产品描述',
    quantity INT NOT NULL COMMENT '数量',
    unit VARCHAR(20) DEFAULT 'PCS' COMMENT '单位',
    unit_price DECIMAL(15,2) NOT NULL COMMENT '单价',
    total_price DECIMAL(15,2) COMMENT '总价',
    packaging VARCHAR(200) COMMENT '包装要求',
    quality_standard VARCHAR(200) COMMENT '质检标准',
    sort_order INT DEFAULT 0 COMMENT '排序',
    INDEX idx_order (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- =============================================
-- 产品管理表
-- =============================================

CREATE TABLE crm_product_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID',
    category_name VARCHAR(100) NOT NULL COMMENT '分类名称',
    category_name_en VARCHAR(100) COMMENT '分类名称(英文)',
    order_num INT DEFAULT 0 COMMENT '显示顺序',
    icon VARCHAR(100) COMMENT '图标',
    status CHAR(1) DEFAULT '0' COMMENT '状态(0正常 1停用)',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_parent (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品分类表';

CREATE TABLE crm_product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '产品ID',
    category_id BIGINT COMMENT '分类ID',
    sku VARCHAR(50) UNIQUE NOT NULL COMMENT 'SKU',
    product_name VARCHAR(200) NOT NULL COMMENT '产品名称',
    product_name_en VARCHAR(200) COMMENT '产品名称(英文)',
    description TEXT COMMENT '产品描述',
    specification TEXT COMMENT '规格参数',
    material VARCHAR(100) COMMENT '材质',
    color VARCHAR(50) COMMENT '颜色',
    size VARCHAR(50) COMMENT '尺寸',
    weight DECIMAL(10,2) COMMENT '重量(kg)',
    length DECIMAL(10,2) COMMENT '长(cm)',
    width DECIMAL(10,2) COMMENT '宽(cm)',
    height DECIMAL(10,2) COMMENT '高(cm)',
    cost_price DECIMAL(15,2) COMMENT '成本价(CNY)',
    factory_price DECIMAL(15,2) COMMENT '出厂价(CNY)',
    fob_price DECIMAL(15,2) COMMENT 'FOB价(USD)',
    cif_price DECIMAL(15,2) COMMENT 'CIF价(USD)',
    min_order_qty INT DEFAULT 1 COMMENT '最小起订量',
    stock_qty INT DEFAULT 0 COMMENT '库存数量',
    main_image VARCHAR(500) COMMENT '主图URL',
    images JSON COMMENT '图片URL列表',
    video_url VARCHAR(500) COMMENT '视频URL',
    status CHAR(1) DEFAULT '0' COMMENT '状态(0上架 1下架)',
    hs_code VARCHAR(20) COMMENT 'HS编码',
    tax_refund_rate DECIMAL(5,2) COMMENT '退税率(%)',
    created_by BIGINT COMMENT '创建者',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新者',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    remark TEXT COMMENT '备注',
    INDEX idx_category (category_id),
    INDEX idx_sku (sku),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品表';

-- =============================================
-- 合同管理表
-- =============================================

CREATE TABLE crm_contract (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '合同ID',
    contract_no VARCHAR(32) UNIQUE NOT NULL COMMENT '合同编号',
    order_id BIGINT COMMENT '关联订单ID',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    contract_type VARCHAR(20) DEFAULT 'SALES' COMMENT '合同类型(SALES销售 PURCHASE采购 AGENT代理 NDA保密)',
    template_id BIGINT COMMENT '模板ID',
    contract_amount DECIMAL(15,2) COMMENT '合同金额',
    currency VARCHAR(3) DEFAULT 'USD' COMMENT '币种',
    sign_date DATE COMMENT '签署日期',
    start_date DATE COMMENT '生效日期',
    end_date DATE COMMENT '到期日期',
    status VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态(DRAFT草稿 APPROVING审批中 SIGNED已签署 EXECUTING执行中 COMPLETED已完成 TERMINATED已终止)',
    pdf_url VARCHAR(500) COMMENT '合同PDF URL',
    signed_url VARCHAR(500) COMMENT '已签署版本URL',
    sales_id BIGINT COMMENT '业务员ID',
    approved_by BIGINT COMMENT '审批人ID',
    approved_time DATETIME COMMENT '审批时间',
    created_by BIGINT COMMENT '创建者',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新者',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    remark TEXT COMMENT '备注',
    INDEX idx_customer (customer_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='合同表';

-- =============================================
-- 物流管理表
-- =============================================

CREATE TABLE crm_shipping (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '发货单ID',
    shipping_no VARCHAR(32) UNIQUE NOT NULL COMMENT '发货单号',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    carrier VARCHAR(50) COMMENT '物流商(DHL/FedEx/UPS/海运/空运)',
    tracking_no VARCHAR(100) COMMENT '运单号',
    ship_date DATE COMMENT '发货日期',
    estimated_arrival DATE COMMENT '预计到达日期',
    actual_arrival DATE COMMENT '实际到达日期',
    package_count INT COMMENT '总箱数',
    total_volume DECIMAL(10,2) COMMENT '总体积(CBM)',
    total_gross_weight DECIMAL(10,2) COMMENT '总毛重(KG)',
    total_net_weight DECIMAL(10,2) COMMENT '总净重(KG)',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态(PENDING待发货 SHIPPED已发货 IN_TRANSIT运输中 CUSTOMS清关中 DELIVERED已送达)',
    customs_status VARCHAR(20) DEFAULT 'PENDING' COMMENT '报关状态(PENDING待报关 DECLARED已报关 RELEASED已放行)',
    tracking_data JSON COMMENT '物流轨迹数据',
    created_by BIGINT COMMENT '创建者',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新者',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_order (order_id),
    INDEX idx_tracking (tracking_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='发货单表';

-- =============================================
-- 财务管理表
-- =============================================

CREATE TABLE crm_receivable (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '应收ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    receivable_no VARCHAR(32) UNIQUE NOT NULL COMMENT '应收编号',
    amount DECIMAL(15,2) NOT NULL COMMENT '应收金额',
    received_amount DECIMAL(15,2) DEFAULT 0 COMMENT '已收金额',
    currency VARCHAR(3) DEFAULT 'USD' COMMENT '币种',
    due_date DATE NOT NULL COMMENT '到期日期',
    payment_type VARCHAR(20) COMMENT '款项类型(DEPOSIT定金 BALANCE尾款 FULL全款)',
    status VARCHAR(20) DEFAULT 'UNPAID' COMMENT '状态(UNPAID未收款 PARTIAL部分收款 PAID已收款 OVERDUE逾期)',
    received_time DATETIME COMMENT '收款时间',
    created_by BIGINT COMMENT '创建者',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    remark TEXT COMMENT '备注',
    INDEX idx_order (order_id),
    INDEX idx_customer (customer_id),
    INDEX idx_due_date (due_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应收账款表';

CREATE TABLE crm_payable (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '应付ID',
    payable_no VARCHAR(32) UNIQUE NOT NULL COMMENT '应付编号',
    order_id BIGINT COMMENT '关联订单ID',
    payee VARCHAR(100) NOT NULL COMMENT '收款方',
    amount DECIMAL(15,2) NOT NULL COMMENT '应付金额',
    paid_amount DECIMAL(15,2) DEFAULT 0 COMMENT '已付金额',
    currency VARCHAR(3) DEFAULT 'USD' COMMENT '币种',
    due_date DATE NOT NULL COMMENT '到期日期',
    payable_type VARCHAR(20) COMMENT '应付类型(PRODUCT采购 LOGISTICS物流 OTHER其他)',
    status VARCHAR(20) DEFAULT 'UNPAID' COMMENT '状态(UNPAID未支付 PARTIAL部分支付 PAID已支付)',
    paid_time DATETIME COMMENT '付款时间',
    created_by BIGINT COMMENT '创建者',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    remark TEXT COMMENT '备注',
    INDEX idx_due_date (due_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应付账款表';

-- =============================================
-- 消息通知表 (msg_前缀)
-- =============================================

CREATE TABLE msg_notification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '消息ID',
    user_id BIGINT NOT NULL COMMENT '接收用户ID',
    title VARCHAR(200) NOT NULL COMMENT '消息标题',
    content TEXT COMMENT '消息内容',
    msg_type VARCHAR(20) NOT NULL COMMENT '消息类型(SYSTEM系统 BUSINESS业务 REMIND提醒)',
    business_type VARCHAR(50) COMMENT '业务类型',
    business_id BIGINT COMMENT '业务ID',
    is_read TINYINT DEFAULT 0 COMMENT '是否已读(0未读 1已读)',
    read_time DATETIME COMMENT '阅读时间',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_user (user_id),
    INDEX idx_is_read (is_read),
    INDEX idx_created (created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='站内消息表';

CREATE TABLE msg_email_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    template_id BIGINT COMMENT '模板ID',
    to_emails VARCHAR(1000) NOT NULL COMMENT '收件人',
    cc_emails VARCHAR(1000) COMMENT '抄送',
    subject VARCHAR(500) NOT NULL COMMENT '邮件主题',
    content TEXT COMMENT '邮件内容',
    attachments JSON COMMENT '附件列表',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态(PENDING待发送 SENT已发送 FAILED发送失败)',
    sent_time DATETIME COMMENT '发送时间',
    open_count INT DEFAULT 0 COMMENT '打开次数',
    click_count INT DEFAULT 0 COMMENT '点击次数',
    error_msg TEXT COMMENT '错误信息',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_sent_time (sent_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='邮件发送日志表';

CREATE TABLE msg_template (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '模板ID',
    template_name VARCHAR(100) NOT NULL COMMENT '模板名称',
    template_type VARCHAR(20) NOT NULL COMMENT '模板类型(EMAIL邮件 SMS短信 WECOM企业微信 DINGTALK钉钉)',
    template_code VARCHAR(50) UNIQUE NOT NULL COMMENT '模板编码',
    template_content TEXT NOT NULL COMMENT '模板内容',
    variables JSON COMMENT '变量列表',
    status CHAR(1) DEFAULT '0' COMMENT '状态(0启用 1禁用)',
    created_by BIGINT COMMENT '创建者',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新者',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息模板表';

-- =============================================
-- 插入初始数据
-- =============================================

-- 默认管理员账号 (密码: admin123 BCrypt加密)
INSERT INTO sys_user (id, dept_id, username, nickname, password, status) VALUES
(1, 1, 'admin', '超级管理员', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '0');

-- 默认部门
INSERT INTO sys_dept (id, parent_id, ancestors, dept_name, order_num, leader) VALUES
(1, 0, '0', '总公司', 1, '超级管理员'),
(2, 1, '0,1', '销售部', 2, '销售经理'),
(3, 1, '0,1', '市场部', 3, '市场经理');

-- 默认角色
INSERT INTO sys_role (id, role_name, role_key, role_sort, data_scope) VALUES
(1, '超级管理员', 'admin', 1, '1'),
(2, '销售经理', 'sales_manager', 2, '2'),
(3, '销售代表', 'sales_rep', 3, '4');

-- 用户角色关联
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

-- 基础字典数据
INSERT INTO sys_dict_type (dict_name, dict_type) VALUES
('客户等级', 'customer_level'),
('国家地区', 'country'),
('贸易术语', 'trade_term'),
('币种', 'currency'),
('跟进方式', 'followup_type'),
('客户来源', 'customer_source'),
('行业分类', 'industry');

-- 客户等级
INSERT INTO sys_dict_data (dict_type, dict_label, dict_value, dict_sort, css_class, list_class) VALUES
('customer_level', 'A级客户', 'A', 1, 'el-tag--danger', 'danger'),
('customer_level', 'B级客户', 'B', 2, 'el-tag--warning', 'warning'),
('customer_level', 'C级客户', 'C', 3, 'el-tag--success', 'success'),
('customer_level', 'D级客户', 'D', 4, 'el-tag--info', 'info');

-- 贸易术语
INSERT INTO sys_dict_data (dict_type, dict_label, dict_value, dict_sort) VALUES
('trade_term', 'FOB', 'FOB', 1),
('trade_term', 'CIF', 'CIF', 2),
('trade_term', 'DDP', 'DDP', 3),
('trade_term', 'DDU', 'DDU', 4);

-- 币种
INSERT INTO sys_dict_data (dict_type, dict_label, dict_value, dict_sort) VALUES
('currency', '美元', 'USD', 1),
('currency', '欧元', 'EUR', 2),
('currency', '人民币', 'CNY', 3),
('currency', '英镑', 'GBP', 4);

-- 跟进方式
INSERT INTO sys_dict_data (dict_type, dict_label, dict_value, dict_sort) VALUES
('followup_type', '邮件', 'email', 1),
('followup_type', '电话', 'phone', 2),
('followup_type', '微信', 'wechat', 3),
('followup_type', '面谈', 'meeting', 4),
('followup_type', '视频会议', 'video', 5);

-- 系统参数配置
INSERT INTO sys_config (config_name, config_key, config_value, config_type) VALUES
('公海池自动回收天数', 'customer.highsea.recycle.days', '30', 'Y'),
('报价单默认有效期(天)', 'quotation.default.validity.days', '30', 'Y'),
('导出最大条数', 'export.max.size', '1000', 'Y'),
('是否启用邮件追踪', 'email.tracking.enabled', 'true', 'Y');
