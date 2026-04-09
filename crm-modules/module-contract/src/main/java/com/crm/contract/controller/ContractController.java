package com.crm.contract.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.common.core.domain.Result;
import com.crm.contract.domain.dto.ContractApprovalDTO;
import com.crm.contract.domain.dto.ContractCreateDTO;
import com.crm.contract.domain.dto.ContractQuery;
import com.crm.contract.domain.dto.ContractUpdateDTO;
import com.crm.contract.domain.vo.ContractVO;
import com.crm.contract.service.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 合同管理Controller
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Tag(name = "合同管理", description = "合同相关接口")
@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    /**
     * 分页查询合同
     */
    @Operation(summary = "分页查询合同", description = "根据条件分页查询合同列表")
    @GetMapping("/page")
    public Result<IPage<ContractVO>> getContractPage(
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量", example = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            ContractQuery query) {
        Page<ContractVO> page = new Page<>(pageNum, pageSize);
        IPage<ContractVO> result = contractService.getContractPage(page, query);
        return Result.success(result);
    }

    /**
     * 获取合同详情
     */
    @Operation(summary = "获取合同详情", description = "根据ID获取合同详细信息")
    @GetMapping("/{id}")
    public Result<ContractVO> getContract(
            @Parameter(description = "合同ID") @PathVariable Long id) {
        ContractVO contract = contractService.getContractById(id);
        return Result.success(contract);
    }

    /**
     * 创建合同
     */
    @Operation(summary = "创建合同", description = "创建新合同")
    @PostMapping
    public Result<Long> createContract(@Valid @RequestBody ContractCreateDTO dto) {
        Long contractId = contractService.createContract(dto);
        return Result.success(contractId);
    }

    /**
     * 更新合同
     */
    @Operation(summary = "更新合同", description = "更新合同信息")
    @PutMapping
    public Result<Void> updateContract(@Valid @RequestBody ContractUpdateDTO dto) {
        contractService.updateContract(dto);
        return Result.success();
    }

    /**
     * 删除合同
     */
    @Operation(summary = "删除合同", description = "批量删除合同")
    @DeleteMapping
    public Result<Void> deleteContracts(
            @Parameter(description = "合同ID列表") @RequestBody List<Long> ids) {
        contractService.deleteContracts(ids);
        return Result.success();
    }

    /**
     * 生成合同号
     */
    @Operation(summary = "生成合同号", description = "自动生成合同号")
    @GetMapping("/generate-no")
    public Result<String> generateContractNo() {
        String contractNo = contractService.generateContractNo();
        return Result.success(contractNo);
    }

    /**
     * 提交审批
     */
    @Operation(summary = "提交审批", description = "将合同提交审批")
    @PostMapping("/{id}/submit-approval")
    public Result<Void> submitForApproval(
            @Parameter(description = "合同ID") @PathVariable Long id) {
        contractService.submitForApproval(id);
        return Result.success();
    }

    /**
     * 审批合同
     */
    @Operation(summary = "审批合同", description = "审批合同(通过/拒绝)")
    @PostMapping("/approve")
    public Result<Void> approveContract(@Valid @RequestBody ContractApprovalDTO approvalDTO) {
        contractService.approveContract(approvalDTO);
        return Result.success();
    }

    /**
     * 归档合同
     */
    @Operation(summary = "归档合同", description = "归档已审批的合同")
    @PostMapping("/{id}/archive")
    public Result<Void> archiveContract(
            @Parameter(description = "合同ID") @PathVariable Long id,
            @Parameter(description = "合同文件URL") @RequestParam String fileUrl) {
        contractService.archiveContract(id, fileUrl);
        return Result.success();
    }

    /**
     * 取消合同
     */
    @Operation(summary = "取消合同", description = "取消合同并记录原因")
    @PostMapping("/{id}/cancel")
    public Result<Void> cancelContract(
            @Parameter(description = "合同ID") @PathVariable Long id,
            @Parameter(description = "取消原因") @RequestParam String reason) {
        contractService.cancelContract(id, reason);
        return Result.success();
    }

    /**
     * 上传电子签名
     */
    @Operation(summary = "上传电子签名", description = "为合同上传电子签名")
    @PostMapping("/{id}/signature")
    public Result<Void> uploadSignature(
            @Parameter(description = "合同ID") @PathVariable Long id,
            @Parameter(description = "电子签名URL") @RequestParam String signatureUrl) {
        contractService.uploadSignature(id, signatureUrl);
        return Result.success();
    }

    /**
     * 从模板生成合同
     */
    @Operation(summary = "从模板生成合同", description = "使用模板和变量生成合同内容")
    @PostMapping("/generate-from-template")
    public Result<String> generateFromTemplate(
            @Parameter(description = "模板ID") @RequestParam Long templateId,
            @Parameter(description = "变量映射") @RequestBody Map<String, Object> variables) {
        String content = contractService.generateContractFromTemplate(templateId, variables);
        return Result.success(content);
    }

    /**
     * 导出合同
     */
    @Operation(summary = "导出合同", description = "根据条件导出合同数据")
    @GetMapping("/export")
    public Result<List<ContractVO>> exportContracts(ContractQuery query) {
        List<ContractVO> contracts = contractService.exportContracts(query);
        return Result.success(contracts);
    }

    /**
     * 统计待审批合同数量
     */
    @Operation(summary = "统计待审批合同", description = "统计待审批的合同数量")
    @GetMapping("/count/pending-approval")
    public Result<Integer> countPendingApproval() {
        Integer count = contractService.countPendingApproval();
        return Result.success(count);
    }

    /**
     * 统计本月签订金额
     */
    @Operation(summary = "统计本月签订金额", description = "统计本月签订的合同总金额")
    @GetMapping("/sum/monthly-amount")
    public Result<java.math.BigDecimal> sumMonthlyContractAmount() {
        java.math.BigDecimal amount = contractService.sumMonthlyContractAmount();
        return Result.success(amount);
    }
}
