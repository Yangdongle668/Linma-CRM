package com.crm.contract.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.common.core.exception.BusinessException;
import com.crm.contract.domain.dto.ContractApprovalDTO;
import com.crm.contract.domain.dto.ContractCreateDTO;
import com.crm.contract.domain.dto.ContractQuery;
import com.crm.contract.domain.dto.ContractUpdateDTO;
import com.crm.contract.domain.entity.CrmContract;
import com.crm.contract.domain.vo.ContractVO;
import com.crm.contract.mapper.ContractMapper;
import com.crm.contract.service.ContractService;
import com.crm.contract.service.ContractTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 合同Service实现类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ContractServiceImpl extends ServiceImpl<ContractMapper, CrmContract> implements ContractService {

    private final ContractMapper contractMapper;
    private final ContractTemplateService templateService;

    @Override
    public IPage<ContractVO> getContractPage(Page<ContractVO> page, ContractQuery query) {
        return contractMapper.selectContractPage(page, query);
    }

    @Override
    public ContractVO getContractById(Long id) {
        CrmContract contract = this.getById(id);
        if (contract == null) {
            throw new BusinessException("合同不存在");
        }

        ContractVO vo = BeanUtil.copyProperties(contract, ContractVO.class);
        vo.setContractTypeDesc(getContractTypeDesc(contract.getContractType()));
        vo.setStatusDesc(getStatusDesc(contract.getStatus()));
        
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createContract(ContractCreateDTO dto) {
        CrmContract contract = BeanUtil.copyProperties(dto, CrmContract.class);
        
        // 生成合同号
        String contractNo = generateContractNo();
        contract.setContractNo(contractNo);
        
        // 设置初始状态
        contract.setStatus("draft");

        this.save(contract);
        log.info("创建合同成功, ID: {}, 合同号: {}", contract.getId(), contractNo);
        return contract.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateContract(ContractUpdateDTO dto) {
        CrmContract existContract = this.getById(dto.getId());
        if (existContract == null) {
            throw new BusinessException("合同不存在");
        }

        // 已审批或已归档的合同不能修改
        if ("approved".equals(existContract.getStatus()) || "archived".equals(existContract.getStatus())) {
            throw new BusinessException("已审批或已归档的合同不能修改");
        }

        CrmContract contract = BeanUtil.copyProperties(dto, CrmContract.class);
        this.updateById(contract);
        log.info("更新合同成功, ID: {}", contract.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteContracts(List<Long> ids) {
        this.removeByIds(ids);
        log.info("删除合同成功, IDs: {}", ids);
    }

    @Override
    public String generateContractNo() {
        String dateStr = DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
        String prefix = "CON" + dateStr;
        
        // 查询当天最大序号
        LambdaQueryWrapper<CrmContract> wrapper = new LambdaQueryWrapper<>();
        wrapper.likeRight(CrmContract::getContractNo, prefix)
               .orderByDesc(CrmContract::getContractNo)
               .last("LIMIT 1");
        
        CrmContract lastContract = this.getOne(wrapper);
        
        int sequence = 1;
        if (lastContract != null && StrUtil.isNotBlank(lastContract.getContractNo())) {
            String lastNo = lastContract.getContractNo();
            String lastSeq = lastNo.substring(prefix.length());
            sequence = Integer.parseInt(lastSeq) + 1;
        }
        
        return prefix + String.format("%03d", sequence);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitForApproval(Long contractId) {
        CrmContract contract = this.getById(contractId);
        if (contract == null) {
            throw new BusinessException("合同不存在");
        }

        if (!"draft".equals(contract.getStatus()) && !"rejected".equals(contract.getStatus())) {
            throw new BusinessException("只有草稿或被拒绝的合同可以提交审批");
        }

        contract.setStatus("pending_approval");
        this.updateById(contract);
        log.info("合同提交审批成功, 合同ID: {}", contractId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveContract(ContractApprovalDTO approvalDTO) {
        CrmContract contract = this.getById(approvalDTO.getContractId());
        if (contract == null) {
            throw new BusinessException("合同不存在");
        }

        if (!"pending_approval".equals(contract.getStatus())) {
            throw new BusinessException("合同不在待审批状态");
        }

        if ("approve".equals(approvalDTO.getApprovalResult())) {
            contract.setStatus("approved");
            contract.setApprovalTime(LocalDateTime.now());
            
            // 保存电子签名
            if (StrUtil.isNotBlank(approvalDTO.getElectronicSignature())) {
                contract.setElectronicSignature(approvalDTO.getElectronicSignature());
            }
        } else if ("reject".equals(approvalDTO.getApprovalResult())) {
            contract.setStatus("rejected");
        } else {
            throw new BusinessException("无效的审批结果");
        }

        contract.setApproverComment(approvalDTO.getApprovalComment());
        this.updateById(contract);
        log.info("合同审批完成, 合同ID: {}, 结果: {}", contract.getId(), approvalDTO.getApprovalResult());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void archiveContract(Long contractId, String fileUrl) {
        CrmContract contract = this.getById(contractId);
        if (contract == null) {
            throw new BusinessException("合同不存在");
        }

        if (!"approved".equals(contract.getStatus())) {
            throw new BusinessException("只有已审批的合同可以归档");
        }

        contract.setStatus("archived");
        contract.setContractFileUrl(fileUrl);
        contract.setArchiveTime(LocalDateTime.now());
        
        this.updateById(contract);
        log.info("合同归档成功, 合同ID: {}", contractId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelContract(Long contractId, String reason) {
        CrmContract contract = this.getById(contractId);
        if (contract == null) {
            throw new BusinessException("合同不存在");
        }

        if ("archived".equals(contract.getStatus())) {
            throw new BusinessException("已归档的合同不能取消");
        }

        contract.setStatus("cancelled");
        contract.setRemark(reason);
        
        this.updateById(contract);
        log.info("合同取消成功, 合同ID: {}, 原因: {}", contractId, reason);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadSignature(Long contractId, String signatureUrl) {
        CrmContract contract = this.getById(contractId);
        if (contract == null) {
            throw new BusinessException("合同不存在");
        }

        contract.setElectronicSignature(signatureUrl);
        this.updateById(contract);
        log.info("上传电子签名成功, 合同ID: {}", contractId);
    }

    @Override
    public String generateContractFromTemplate(Long templateId, Map<String, Object> variables) {
        CrmContractTemplate template = templateService.getById(templateId);
        if (template == null) {
            throw new BusinessException("模板不存在");
        }

        if (template.getStatus() == 0) {
            throw new BusinessException("模板已禁用");
        }

        return templateService.replaceVariables(template.getTemplateContent(), variables);
    }

    @Override
    public List<ContractVO> exportContracts(ContractQuery query) {
        return contractMapper.selectContractForExport(query);
    }

    @Override
    public Integer countPendingApproval() {
        return contractMapper.countPendingApproval();
    }

    @Override
    public java.math.BigDecimal sumMonthlyContractAmount() {
        return contractMapper.sumMonthlyContractAmount();
    }

    /**
     * 获取合同类型描述
     *
     * @param contractType 合同类型
     * @return 描述
     */
    private String getContractTypeDesc(String contractType) {
        if (contractType == null) {
            return "";
        }
        switch (contractType) {
            case "sales":
                return "销售合同";
            case "purchase":
                return "采购合同";
            case "agency":
                return "代理合同";
            case "nda":
                return "保密协议";
            default:
                return contractType;
        }
    }

    /**
     * 获取状态描述
     *
     * @param status 状态
     * @return 描述
     */
    private String getStatusDesc(String status) {
        if (status == null) {
            return "";
        }
        switch (status) {
            case "draft":
                return "草稿";
            case "pending_approval":
                return "待审批";
            case "approved":
                return "已审批";
            case "rejected":
                return "已拒绝";
            case "archived":
                return "已归档";
            case "cancelled":
                return "已取消";
            default:
                return status;
        }
    }
}
