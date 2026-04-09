package com.crm.customer.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 联系人视图对象VO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "联系人信息")
public class ContactVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "联系人ID")
    private Long id;

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "公司名称")
    private String companyName;

    @Schema(description = "名")
    private String firstName;

    @Schema(description = "姓")
    private String lastName;

    @Schema(description = "全名")
    private String fullName;

    @Schema(description = "性别(0男 1女 2未知)")
    private String gender;

    @Schema(description = "职位")
    private String position;

    @Schema(description = "部门")
    private String department;

    @Schema(description = "手机")
    private String phone;

    @Schema(description = "电话")
    private String tel;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "微信")
    private String wechat;

    @Schema(description = "LinkedIn")
    private String linkedin;

    @Schema(description = "是否关键人(0否 1是)")
    private String isKeyPerson;

    @Schema(description = "关键人类型(decision_maker/influencer/user/gatekeeper)")
    private String keyPersonType;

    @Schema(description = "生日")
    private LocalDate birthday;

    @Schema(description = "语言偏好")
    private String languagePref;

    @Schema(description = "沟通方式偏好")
    private String communicationPref;

    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

    @Schema(description = "更新时间")
    private LocalDateTime updatedTime;

    @Schema(description = "备注")
    private String remark;
}
