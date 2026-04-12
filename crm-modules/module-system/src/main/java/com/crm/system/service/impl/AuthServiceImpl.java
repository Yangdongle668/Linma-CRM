package com.crm.system.service.impl;

import com.crm.common.core.exception.BusinessException;
import com.crm.common.core.utils.JwtUtils;
import com.crm.system.domain.dto.LoginDTO;
import com.crm.system.domain.entity.SysUser;
import com.crm.system.domain.vo.LoginVO;
import com.crm.system.domain.vo.UserInfoVO;
import com.crm.system.mapper.SysUserMapper;
import com.crm.system.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务实现类
 *
 * @author CRM System
 * @since 2026-04-11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        log.info("尝试登录 - 用户名: {}", loginDTO.getUsername());
        
        // 查询用户
        SysUser user = userMapper.selectByUsername(loginDTO.getUsername());

        if (user == null) {
            log.warn("用户不存在: {}", loginDTO.getUsername());
            throw new BusinessException("用户名或密码错误");
        }

        log.info("找到用户 - ID: {}, 用户名: {}, 状态: {}", user.getId(), user.getUsername(), user.getStatus());
        log.debug("数据库中的密码哈希: {}", user.getPassword());

        // 验证密码
        boolean passwordMatches = passwordEncoder.matches(loginDTO.getPassword(), user.getPassword());
        log.info("密码验证结果: {}", passwordMatches);
        
        if (!passwordMatches) {
            log.warn("密码错误 - 输入密码: {}", loginDTO.getPassword());
            throw new BusinessException("用户名或密码错误");
        }

        // 检查用户状态
        if ("1".equals(user.getStatus())) {
            throw new BusinessException("账号已被停用");
        }

        // 生成 Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        String token = JwtUtils.generateToken(user.getId(), user.getUsername(), claims);

        // 构建响应
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setRefreshToken(token);

        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setRealName(user.getNickname());
        userInfo.setEmail(user.getEmail());
        userInfo.setPhone(user.getPhone());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setRoles(new ArrayList<>());
        userInfo.setPermissions(new ArrayList<>());

        loginVO.setUserInfo(userInfo);

        log.info("登录成功 - 用户: {}", user.getUsername());
        return loginVO;
    }

    @Override
    public String refreshToken(String refreshToken) {
        if (!JwtUtils.validateToken(refreshToken)) {
            throw new BusinessException("Token 无效或已过期");
        }

        Long userId = JwtUtils.getUserIdFromToken(refreshToken);
        String username = JwtUtils.getUsernameFromToken(refreshToken);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        return JwtUtils.generateToken(userId, username, claims);
    }

    @Override
    public UserInfoVO getCurrentUser(Long userId) {
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setRealName(user.getNickname());
        userInfo.setEmail(user.getEmail());
        userInfo.setPhone(user.getPhone());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setRoles(new ArrayList<>());
        userInfo.setPermissions(new ArrayList<>());

        return userInfo;
    }
}
