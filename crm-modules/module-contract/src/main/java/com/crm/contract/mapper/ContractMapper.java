package com.crm.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.contract.domain.dto.ContractQuery;
import com.crm.contract.domain.entity.CrmContract;
import com.crm.contract.domain.vo.ContractVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 合同Mapper接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Mapper
public interface ContractMapper extends BaseMapper<CrmContract> {

    /**
     * 分页查询合同(带关联信息)
     *
     * @param page 分页对象
     * @param query 查询条件
     * @return 合同列表
     */
    Page<ContractVO> selectContractPage(Page<ContractVO> page, @Param("query") ContractQuery query);

    /**
     * 根据合同号查询
     *
     * @param contractNo 合同号
     * @return 合同信息
     */
    CrmContract selectByContractNo(@Param("contractNo") String contractNo);

    /**
     * 批量导出合同
     *
     * @param query 查询条件
     * @return 合同列表
     */
    List<ContractVO> selectContractForExport(@Param("query") ContractQuery query);

    /**
     * 统计待审批合同数量
     *
     * @return 数量
     */
    Integer countPendingApproval();

    /**
     * 统计本月签订金额
     *
     * @return 金额
     */
    java.math.BigDecimal sumMonthlyContractAmount();
}
