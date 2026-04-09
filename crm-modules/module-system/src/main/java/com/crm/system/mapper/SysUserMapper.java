package com.crm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.system.domain.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}
