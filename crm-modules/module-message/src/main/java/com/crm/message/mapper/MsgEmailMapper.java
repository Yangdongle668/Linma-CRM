package com.crm.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.message.domain.entity.MsgEmail;
import org.apache.ibatis.annotations.Mapper;

/**
 * 邮件Mapper接口
 *
 * @author CRM System
 * @since 2026-04-12
 */
@Mapper
public interface MsgEmailMapper extends BaseMapper<MsgEmail> {
}
