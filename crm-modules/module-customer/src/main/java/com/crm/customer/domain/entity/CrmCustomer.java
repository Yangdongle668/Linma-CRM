package com.crm.customer.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 客户实体类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("crm_customer")
public class CrmCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 客户编号（自动生成，格式：CUS20260409001）
     */
    private String customerNo;

    /**
     * 公司名称(英文)
     */
    private String companyName;

    /**
     * 公司名称(中文)
     */
    private String companyNameCn;

    /**
     * 国家代码
     */
    private String country;

    /**
     * 省份/州
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 客户头像URL
     */
    private String customerAvatar;

    /**
     * 公司Logo URL
     */
    private String companyLogo;

    /**
     * 公司网站
     */
    private String website;

    /**
     * 邮箱域名(自动从邮箱提取)
     */
    private String emailDomain;

    /**
     * 社交媒体 - LinkedIn
     */
    private String socialLinkedin;

    /**
     * 社交媒体 - Facebook
     */
    private String socialFacebook;

    /**
     * 社交媒体 - Twitter
     */
    private String socialTwitter;

    /**
     * 行业分类
     */
    private String industry;

    /**
     * 公司规模
     */
    private String companySize;

    /**
     * 业务类型(manufacturer/trader/distributor/retailer)
     */
    private String businessType;

    /**
     * 主营产品
     */
    private String mainProducts;

    /**
     * 主要市场(多国用逗号分隔)
     */
    private String mainMarkets;

    /**
     * 成立年份
     */
    private Integer establishedYear;

    /**
     * 年营业额(美元)
     */
    private BigDecimal annualRevenue;

    /**
     * 税号/VAT号
     */
    private String taxId;

    /**
     * 注册号
     */
    private String registrationNumber;

    /**
     * 进口许可证号
     */
    private String importLicense;

    /**
     * 客户来源(展会/阿里/官网/转介绍)
     */
    private String source;

    /**
     * 获客渠道(SEO/SEM/社媒/展会/referral等)
     */
    private String acquisitionChannel;

    /**
     * 营销活动来源
     */
    private String campaignSource;

    /**
     * 首次接触日期
     */
    private LocalDate firstContactDate;

    /**
     * 客户等级(A/B/C/D)
     */
    private String level;

    /**
     * 优先级(high/medium/low)
     */
    private String priority;

    /**
     * 付款条件(T/T, L/C等)
     */
    private String paymentTerms;

    /**
     * 信用评级(AAA/AA/A/B/C)
     */
    private String creditRating;

    /**
     * 时区
     */
    private String timezone;

    /**
     * 语言偏好(en/zh/es/fr等)
     */
    private String languagePreference;

    /**
     * 状态(1正常 0禁用)
     */
    private Integer status;

    /**
     * 客户生命周期(prospect/lead/opportunity/customer/churned)
     */
    private String customerLifecycle;

    /**
     * ISO认证
     */
    private Integer certificationIso;

    /**
     * CE认证
     */
    private Integer certificationCe;

    /**
     * FDA认证
     */
    private Integer certificationFda;

    /**
     * 风险等级(low/medium/high)
     */
    private String riskLevel;

    /**
     * 风险因素
     */
    private String riskFactors;

    /**
     * 商机价值预估
     */
    private BigDecimal opportunityValue;

    /**
     * 转化概率(0-100%)
     */
    private Integer conversionProbability;

    /**
     * 预计成交日期
     */
    private LocalDate expectedCloseDate;

    /**
     * 满意度评分(1-5)
     */
    private Integer satisfactionScore;

    /**
     * 净推荐值(-100到100)
     */
    private Integer npsScore;

    /**
     * 最后邮件联系时间
     */
    private LocalDateTime lastEmailTime;

    /**
     * 邮件往来次数
     */
    private Integer emailCount;

    /**
     * 最后电话沟通时间
     */
    private LocalDateTime lastCallTime;

    /**
     * 电话沟通次数
     */
    private Integer callCount;

    /**
     * 最后会议时间
     */
    private LocalDateTime lastMeetingTime;

    /**
     * 会议次数
     */
    private Integer meetingCount;

    /**
     * 竞争对手信息
     */
    private String competitorInfo;

    /**
     * SWOT分析
     */
    private String swotAnalysis;

    /**
     * 标签(JSON格式)
     */
    private String tagsJson;

    /**
     * 自定义字段(JSON)
     */
    private String customFields;

    /**
     * 负责人ID
     */
    private Long ownerId;

    /**
     * 是否在公海池(0否 1是)
     */
    private Integer isHighSea;

    /**
     * 最后跟进时间
     */
    private LocalDateTime lastFollowTime;

    /**
     * 所属部门
     */
    private Long departmentId;

    /**
     * 下次跟进时间
     */
    private LocalDateTime nextFollowTime;

    /**
     * 询盘总数
     */
    private Integer totalInquiry;

    /**
     * 累计订单金额
     */
    private BigDecimal totalOrder;

    /**
     * 最后下单日期
     */
    private LocalDate lastOrderDate;

    /**
     * 创建者
     */
    private Long createdBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新者
     */
    private Long updatedBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 备注
     */
    private String remark;
}
