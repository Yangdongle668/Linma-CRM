package com.crm.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.finance.domain.entity.CrmPayable;
import org.apache.ibatis.annotations.Mapper;

/**
 * 应付账款Mapper接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Mapper
public interface CrmPayableMapper extends BaseMapper<CrmPayable> {
}
