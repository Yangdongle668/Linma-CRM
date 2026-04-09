package com.crm.customer.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 联系人实体类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("crm_contact")
public class CrmContact implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 联系人ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 名
     */
    private String firstName;

    /**
     * 姓
     */
    private String lastName;

    /**
     * 全名
     */
    private String fullName;

    /**
     * 性别(0男 1女 2未知)
     */
    private String gender;

    /**
     * 职位
     */
    private String position;

    /**
     * 部门
     */
    private String department;

    /**
     * 手机
     */
    private String phone;

    /**
     * 电话
     */
    private String tel;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 微信
     */
    private String wechat;

    /**
     * LinkedIn
     */
    private String linkedin;

    /**
     * 是否关键人(0否 1是)
     */
    private String isKeyPerson;

    /**
     * 关键人类型(decision_maker决策者 influencer影响者 user使用者 gatekeeper把关者)
     */
    private String keyPersonType;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 语言偏好
     */
    private String languagePref;

    /**
     * 沟通方式偏好
     */
    private String communicationPref;

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
