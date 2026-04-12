package com.crm.system.controller;

import com.crm.common.core.domain.Result;
import com.crm.common.core.utils.JwtUtils;
import com.crm.system.domain.dto.LoginDTO;
import com.crm.system.domain.vo.LoginVO;
import com.crm.system.domain.vo.UserInfoVO;
import com.crm.system.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 *
 * @author CRM System
 * @since 2026-04-11
 */
@Tag(name = "认证管理", description = "用户登录、Token刷新等认证功能")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = authService.login(loginDTO);
        return Result.success(loginVO);
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/userinfo")
    public Result<UserInfoVO> getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        Long userId = JwtUtils.getUserIdFromToken(token);
        UserInfoVO userInfo = authService.getCurrentUser(userId);
        return Result.success(userInfo);
    }

    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public Result<Void> logout() {
        // JWT 是无状态的，客户端只需删除 token
        return Result.success();
    }

    @Operation(summary = "刷新Token")
    @PostMapping("/refresh")
    public Result<String> refreshToken(@RequestParam String refreshToken) {
        String newToken = authService.refreshToken(refreshToken);
        return Result.success(newToken);
    }
}
