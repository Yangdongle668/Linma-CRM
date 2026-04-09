package com.crm.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.message.domain.entity.MsgNotification;
import org.apache.ibatis.annotations.Mapper;

/**
 * 站内消息Mapper接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Mapper
public interface MsgNotificationMapper extends BaseMapper<MsgNotification> {
}
