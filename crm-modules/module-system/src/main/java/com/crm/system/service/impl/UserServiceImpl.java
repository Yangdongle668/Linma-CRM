package com.crm.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.system.domain.dto.UserCreateDTO;
import com.crm.system.domain.dto.UserQuery;
import com.crm.system.domain.dto.UserUpdateDTO;
import com.crm.system.domain.entity.SysUser;
import com.crm.system.domain.vo.UserVO;
import com.crm.system.mapper.SysUserMapper;
import com.crm.system.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements UserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public IPage<UserVO> pageUsers(UserQuery query, int pageNum, int pageSize) {
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysUser> wrapper = buildQueryWrapper(query);
        IPage<SysUser> userPage = this.page(page, wrapper);

        // 转换为VO
        return userPage.convert(this::convertToVO);
    }

    @Override
    public UserVO getUserById(Long id) {
        SysUser user = this.getById(id);
        if (user == null) {
            return null;
        }
        return convertToVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createUser(UserCreateDTO dto) {
        // 检查用户名是否已存在
        long count = this.count(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, dto.getUsername()));
        if (count > 0) {
            throw new RuntimeException("用户名已存在");
        }

        SysUser user = new SysUser();
        BeanUtil.copyProperties(dto, user);
        
        // 密码加密
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        
        // 设置默认值
        if (StrUtil.isBlank(user.getStatus())) {
            user.setStatus("0");
        }
        if (StrUtil.isBlank(user.getGender())) {
            user.setGender("0");
        }
        
        return this.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(UserUpdateDTO dto) {
        SysUser user = this.getById(dto.getId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        BeanUtil.copyProperties(dto, user);
        return this.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUsers(List<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resetPassword(Long userId, String newPassword) {
        SysUser user = this.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPwdUpdateDate(LocalDateTime.now());
        return this.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changeStatus(Long userId, String status) {
        SysUser user = this.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        user.setStatus(status);
        return this.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRoles(Long userId, List<Long> roleIds) {
        // TODO: 实现用户角色分配逻辑
        // 1. 删除用户原有的角色关联
        // 2. 插入新的角色关联
        log.info("为用户 {} 分配角色: {}", userId, roleIds);
        return true;
    }

    @Override
    public SysUser getUserByUsername(String username) {
        return this.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username));
    }

    @Override
    public List<UserVO> exportUsers(UserQuery query) {
        LambdaQueryWrapper<SysUser> wrapper = buildQueryWrapper(query);
        List<SysUser> userList = this.list(wrapper);
        return userList.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean importUsers(List<UserCreateDTO> userList) {
        List<SysUser> users = new ArrayList<>();
        for (UserCreateDTO dto : userList) {
            // 检查用户名是否已存在
            long count = this.count(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getUsername, dto.getUsername()));
            if (count > 0) {
                log.warn("用户 {} 已存在,跳过", dto.getUsername());
                continue;
            }

            SysUser user = new SysUser();
            BeanUtil.copyProperties(dto, user);
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            
            if (StrUtil.isBlank(user.getStatus())) {
                user.setStatus("0");
            }
            if (StrUtil.isBlank(user.getGender())) {
                user.setGender("0");
            }
            
            users.add(user);
        }
        
        return this.saveBatch(users);
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<SysUser> buildQueryWrapper(UserQuery query) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        
        if (query != null) {
            wrapper.like(StrUtil.isNotBlank(query.getUsername()), 
                    SysUser::getUsername, query.getUsername())
                   .like(StrUtil.isNotBlank(query.getNickname()), 
                    SysUser::getNickname, query.getNickname())
                   .eq(StrUtil.isNotBlank(query.getPhone()), 
                    SysUser::getPhone, query.getPhone())
                   .eq(StrUtil.isNotBlank(query.getEmail()), 
                    SysUser::getEmail, query.getEmail())
                   .eq(query.getDeptId() != null, 
                    SysUser::getDeptId, query.getDeptId())
                   .eq(StrUtil.isNotBlank(query.getStatus()), 
                    SysUser::getStatus, query.getStatus())
                   .eq(StrUtil.isNotBlank(query.getGender()), 
                    SysUser::getGender, query.getGender());
        }
        
        wrapper.orderByDesc(SysUser::getCreatedTime);
        return wrapper;
    }

    /**
     * 实体转VO
     */
    private UserVO convertToVO(SysUser user) {
        UserVO vo = new UserVO();
        BeanUtil.copyProperties(user, vo);
        // TODO: 查询部门名称和角色信息
        return vo;
    }
}
