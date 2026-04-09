package com.crm.customer.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 客户创建DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "客户创建信息")
public class CustomerCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "公司名称(英文)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "公司名称(英文)不能为空")
    private String companyName;

    @Schema(description = "公司名称(中文)")
    private String companyNameCn;

    @Schema(description = "国家代码")
    private String country;

    @Schema(description = "省份/州")
    private String province;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "公司网站")
    private String website;

    @Schema(description = "行业分类")
    private String industry;

    @Schema(description = "客户来源(展会/阿里/官网/转介绍)")
    private String source;

    @Schema(description = "客户等级(A/B/C/D)")
    private String level;

    @Schema(description = "负责人ID")
    private Long ownerId;

    @Schema(description = "所属部门")
    private Long departmentId;

    @Schema(description = "备注")
    private String remark;
}
