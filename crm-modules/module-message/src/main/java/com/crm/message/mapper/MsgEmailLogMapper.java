package com.crm.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.message.domain.entity.MsgEmailLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 邮件日志Mapper接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Mapper
public interface MsgEmailLogMapper extends BaseMapper<MsgEmailLog> {
}
