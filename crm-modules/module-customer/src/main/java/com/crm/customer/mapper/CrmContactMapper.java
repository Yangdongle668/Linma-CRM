package com.crm.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.customer.domain.entity.CrmContact;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 联系人Mapper接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Mapper
public interface CrmContactMapper extends BaseMapper<CrmContact> {

    /**
     * 根据客户ID获取联系人列表
     *
     * @param customerId 客户ID
     * @return 联系人列表
     */
    List<CrmContact> selectByCustomerId(@Param("customerId") Long customerId);

    /**
     * 根据邮箱查询联系人（用于查重）
     *
     * @param email 邮箱
     * @return 联系人列表
     */
    List<CrmContact> selectByEmail(@Param("email") String email);

    /**
     * 根据电话查询联系人（用于查重）
     *
     * @param phone 电话
     * @return 联系人列表
     */
    List<CrmContact> selectByPhone(@Param("phone") String phone);
}
