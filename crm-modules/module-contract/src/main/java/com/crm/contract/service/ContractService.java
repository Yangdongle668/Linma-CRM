package com.crm.contract.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.contract.domain.dto.ContractApprovalDTO;
import com.crm.contract.domain.dto.ContractCreateDTO;
import com.crm.contract.domain.dto.ContractQuery;
import com.crm.contract.domain.dto.ContractUpdateDTO;
import com.crm.contract.domain.entity.CrmContract;
import com.crm.contract.domain.vo.ContractVO;

import java.util.List;
import java.util.Map;

/**
 * 合同Service接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface ContractService extends IService<CrmContract> {

    /**
     * 分页查询合同
     *
     * @param page 分页对象
     * @param query 查询条件
     * @return 合同分页列表
     */
    IPage<ContractVO> getContractPage(Page<ContractVO> page, ContractQuery query);

    /**
     * 根据ID查询合同详情
     *
     * @param id 合同ID
     * @return 合同详情
     */
    ContractVO getContractById(Long id);

    /**
     * 创建合同
     *
     * @param dto 创建DTO
     * @return 合同ID
     */
    Long createContract(ContractCreateDTO dto);

    /**
     * 更新合同
     *
     * @param dto 更新DTO
     */
    void updateContract(ContractUpdateDTO dto);

    /**
     * 删除合同
     *
     * @param ids 合同ID列表
     */
    void deleteContracts(List<Long> ids);

    /**
     * 生成合同号
     *
     * @return 合同号
     */
    String generateContractNo();

    /**
     * 提交审批
     *
     * @param contractId 合同ID
     */
    void submitForApproval(Long contractId);

    /**
     * 审批合同
     *
     * @param approvalDTO 审批DTO
     */
    void approveContract(ContractApprovalDTO approvalDTO);

    /**
     * 归档合同
     *
     * @param contractId 合同ID
     * @param fileUrl 合同文件URL
     */
    void archiveContract(Long contractId, String fileUrl);

    /**
     * 取消合同
     *
     * @param contractId 合同ID
     * @param reason 取消原因
     */
    void cancelContract(Long contractId, String reason);

    /**
     * 上传电子签名
     *
     * @param contractId 合同ID
     * @param signatureUrl 电子签名URL
     */
    void uploadSignature(Long contractId, String signatureUrl);

    /**
     * 从模板生成合同内容
     *
     * @param templateId 模板ID
     * @param variables 变量替换映射
     * @return 生成的合同内容
     */
    String generateContractFromTemplate(Long templateId, Map<String, Object> variables);

    /**
     * 导出合同
     *
     * @param query 查询条件
     * @return 合同列表
     */
    List<ContractVO> exportContracts(ContractQuery query);

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
