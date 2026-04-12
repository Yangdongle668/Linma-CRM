package com.crm.customer.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 客户视图对象VO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "客户信息")
public class CustomerVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "客户ID")
    private Long id;

    @Schema(description = "客户编号")
    private String customerNo;

    @Schema(description = "公司名称(英文)")
    private String companyName;

    @Schema(description = "公司名称(中文)")
    private String companyNameCn;

    @Schema(description = "国家代码")
    private String country;

    @Schema(description = "国家名称")
    private String countryName;

    @Schema(description = "省份/州")
    private String province;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "公司网站")
    private String website;

    @Schema(description = "邮箱域名")
    private String emailDomain;

    @Schema(description = "客户头像URL")
    private String customerAvatar;

    @Schema(description = "公司Logo URL")
    private String companyLogo;

    @Schema(description = "LinkedIn主页")
    private String socialLinkedin;

    @Schema(description = "Facebook主页")
    private String socialFacebook;

    @Schema(description = "Twitter主页")
    private String socialTwitter;

    @Schema(description = "行业分类")
    private String industry;

    @Schema(description = "公司规模")
    private String companySize;

    @Schema(description = "业务类型")
    private String businessType;

    @Schema(description = "主营产品")
    private String mainProducts;

    @Schema(description = "主要市场")
    private String mainMarkets;

    @Schema(description = "成立年份")
    private Integer establishedYear;

    @Schema(description = "年营业额(美元)")
    private BigDecimal annualRevenue;

    @Schema(description = "税号/VAT号")
    private String taxId;

    @Schema(description = "注册号")
    private String registrationNumber;

    @Schema(description = "进口许可证号")
    private String importLicense;

    @Schema(description = "客户来源(展会/阿里/官网/转介绍)")
    private String source;

    @Schema(description = "获客渠道")
    private String acquisitionChannel;

    @Schema(description = "营销活动来源")
    private String campaignSource;

    @Schema(description = "首次接触日期")
    private LocalDate firstContactDate;

    @Schema(description = "客户等级(A/B/C/D)")
    private String level;

    @Schema(description = "优先级(high/medium/low)")
    private String priority;

    @Schema(description = "付款条件")
    private String paymentTerms;

    @Schema(description = "信用评级")
    private String creditRating;

    @Schema(description = "时区")
    private String timezone;

    @Schema(description = "语言偏好")
    private String languagePreference;

    @Schema(description = "状态(1正常 0禁用)")
    private Integer status;

    @Schema(description = "客户生命周期")
    private String customerLifecycle;

    @Schema(description = "ISO认证")
    private Integer certificationIso;

    @Schema(description = "CE认证")
    private Integer certificationCe;

    @Schema(description = "FDA认证")
    private Integer certificationFda;

    @Schema(description = "风险等级")
    private String riskLevel;

    @Schema(description = "风险因素")
    private String riskFactors;

    @Schema(description = "商机价值预估")
    private BigDecimal opportunityValue;

    @Schema(description = "转化概率(0-100%)")
    private Integer conversionProbability;

    @Schema(description = "预计成交日期")
    private LocalDate expectedCloseDate;

    @Schema(description = "满意度评分(1-5)")
    private Integer satisfactionScore;

    @Schema(description = "净推荐值(-100到100)")
    private Integer npsScore;

    @Schema(description = "最后邮件联系时间")
    private LocalDateTime lastEmailTime;

    @Schema(description = "邮件往来次数")
    private Integer emailCount;

    @Schema(description = "最后电话沟通时间")
    private LocalDateTime lastCallTime;

    @Schema(description = "电话沟通次数")
    private Integer callCount;

    @Schema(description = "最后会议时间")
    private LocalDateTime lastMeetingTime;

    @Schema(description = "会议次数")
    private Integer meetingCount;

    @Schema(description = "竞争对手信息")
    private String competitorInfo;

    @Schema(description = "SWOT分析")
    private String swotAnalysis;

    @Schema(description = "标签(JSON)")
    private String tagsJson;

    @Schema(description = "自定义字段(JSON)")
    private String customFields;

    @Schema(description = "负责人ID")
    private Long ownerId;

    @Schema(description = "负责人姓名")
    private String ownerName;

    @Schema(description = "所属部门")
    private Long departmentId;

    @Schema(description = "部门名称")
    private String departmentName;

    @Schema(description = "下次跟进时间")
    private LocalDateTime nextFollowTime;

    @Schema(description = "询盘总数")
    private Integer totalInquiry;

    @Schema(description = "累计订单金额")
    private BigDecimal totalOrder;

    @Schema(description = "最后下单日期")
    private LocalDate lastOrderDate;

    @Schema(description = "创建者")
    private Long createdBy;

    @Schema(description = "创建者姓名")
    private String creatorName;

    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

    @Schema(description = "更新时间")
    private LocalDateTime updatedTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "标签列表")
    private List<TagVO> tags;

    @Schema(description = "联系人数量")
    private Integer contactCount;

    @Schema(description = "最近跟进时间")
    private LocalDateTime lastFollowTime;

    @Schema(description = "天数未跟进")
    private Integer daysSinceLastFollow;
}
