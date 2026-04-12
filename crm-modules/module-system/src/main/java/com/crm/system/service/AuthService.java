package com.crm.system.service;

import com.crm.system.domain.dto.LoginDTO;
import com.crm.system.domain.vo.LoginVO;
import com.crm.system.domain.vo.UserInfoVO;

/**
 * 认证服务接口
 *
 * @author CRM System
 * @since 2026-04-11
 */
public interface AuthService {

    /**
     * 用户登录
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 获取当前用户信息
     */
    UserInfoVO getCurrentUser(Long userId);

    /**
     * 刷新 Token
     */
    String refreshToken(String refreshToken);
}
