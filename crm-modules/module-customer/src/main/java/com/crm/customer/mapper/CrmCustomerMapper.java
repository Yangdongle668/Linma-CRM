package com.crm.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.customer.domain.entity.CrmCustomer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户Mapper接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Mapper
public interface CrmCustomerMapper extends BaseMapper<CrmCustomer> {

    /**
     * 根据公司名查询客户（用于查重）
     *
     * @param companyName 公司名称
     * @return 客户列表
     */
    List<CrmCustomer> selectByCompanyName(@Param("companyName") String companyName);

    /**
     * 根据邮箱查询客户（用于查重）
     *
     * @param email 邮箱
     * @return 客户列表
     */
    List<CrmCustomer> selectByEmail(@Param("email") String email);

    /**
     * 根据电话查询客户（用于查重）
     *
     * @param phone 电话
     * @return 客户列表
     */
    List<CrmCustomer> selectByPhone(@Param("phone") String phone);

    /**
     * 根据网站查询客户（用于查重）
     *
     * @param website 网站
     * @return 客户列表
     */
    List<CrmCustomer> selectByWebsite(@Param("website") String website);

    /**
     * 查询公海池客户（owner_id为NULL的客户）
     *
     * @return 公海池客户列表
     */
    List<CrmCustomer> selectHighSeaCustomers();

    /**
     * 查询需要自动回收的客户（超过指定天数未跟进）
     *
     * @param days 天数
     * @return 需要回收的客户列表
     */
    List<CrmCustomer> selectRecycleCustomers(@Param("days") Integer days);
}
