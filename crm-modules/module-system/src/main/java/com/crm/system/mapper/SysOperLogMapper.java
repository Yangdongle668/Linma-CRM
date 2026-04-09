package com.crm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.system.domain.entity.SysOperLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志Mapper接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Mapper
public interface SysOperLogMapper extends BaseMapper<SysOperLog> {

}
