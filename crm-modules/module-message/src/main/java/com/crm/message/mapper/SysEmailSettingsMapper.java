package com.crm.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.message.domain.entity.SysEmailSettings;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 邮件设置Mapper
 *
 * @author CRM System
 * @since 2026-04-12
 */
@Mapper
public interface SysEmailSettingsMapper extends BaseMapper<SysEmailSettings> {

    /**
     * 查询用户的邮件设置列表
     */
    List<SysEmailSettings> selectByUserId(@Param("userId") Long userId);

    /**
     * 查询用户的默认邮件设置
     */
    SysEmailSettings selectDefaultByUserId(@Param("userId") Long userId);

    /**
     * 根据邮箱地址查询
     */
    SysEmailSettings selectByEmailAddress(@Param("emailAddress") String emailAddress);
}
