package com.crm.system.domain.vo;

import lombok.Data;

/**
 * 登录响应 VO
 *
 * @author CRM System
 * @since 2026-04-11
 */
@Data
public class LoginVO {

    private String token;
    private String refreshToken;
    private UserInfoVO userInfo;
}
