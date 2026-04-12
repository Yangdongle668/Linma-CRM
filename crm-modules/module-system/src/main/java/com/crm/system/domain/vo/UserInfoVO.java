package com.crm.system.domain.vo;

import lombok.Data;
import java.util.List;

/**
 * 用户信息 VO
 *
 * @author CRM System
 * @since 2026-04-11
 */
@Data
public class UserInfoVO {

    private Long id;
    private String username;
    private String realName;
    private String email;
    private String phone;
    private String avatar;
    private List<String> roles;
    private List<String> permissions;
}
