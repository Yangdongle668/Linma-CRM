package com.crm.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.crm.common.core.domain.Result;
import com.crm.finance.domain.dto.PayableQuery;
import com.crm.finance.domain.dto.PaymentApprovalDTO;
import com.crm.finance.domain.vo.PayableVO;
import com.crm.finance.service.PayableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 应付账款控制器
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Tag(name = "应付账款管理", description = "应付账款的查询、付款申请、付款审批等功能")
@RestController
@RequestMapping("/finance/payable")
@RequiredArgsConstructor
public class PayableController {

    private final PayableService payableService;

    @Operation(summary = "分页查询应付账款列表")
    @GetMapping("/page")
    public Result<IPage<PayableVO>> pagePayables(
            @Parameter(description = "查询条件") PayableQuery query,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        IPage<PayableVO> page = payableService.pagePayables(query, pageNum, pageSize);
        return Result.success(page);
    }

    @Operation(summary = "根据ID查询应付账款详情")
    @GetMapping("/{id}")
    public Result<PayableVO> getPayableById(
            @Parameter(description = "应付ID") @PathVariable Long id) {
        PayableVO payable = payableService.getPayableById(id);
        return Result.success(payable);
    }

    @Operation(summary = "提交付款申请")
    @PostMapping("/{id}/submit-application")
    public Result<Void> submitPaymentApplication(
            @Parameter(description = "应付ID") @PathVariable Long id) {
        boolean success = payableService.submitPaymentApplication(id);
        return success ? Result.success() : Result.error("提交失败");
    }

    @Operation(summary = "审批付款申请")
    @PostMapping("/approve")
    public Result<Void> approvePayment(
            @Parameter(description = "审批信息") @Valid @RequestBody PaymentApprovalDTO dto) {
        boolean success = payableService.approvePayment(dto);
        return success ? Result.success() : Result.error("审批失败");
    }

    @Operation(summary = "确认付款")
    @PostMapping("/{id}/confirm-payment")
    public Result<Void> confirmPayment(
            @Parameter(description = "应付ID") @PathVariable Long id,
            @Parameter(description = "付款金额") @RequestParam BigDecimal amount) {
        boolean success = payableService.confirmPayment(id, amount);
        return success ? Result.success() : Result.error("确认付款失败");
    }

    @Operation(summary = "生成应付单号")
    @GetMapping("/generate-no")
    public Result<String> generatePayableNo() {
        String payableNo = payableService.generatePayableNo();
        return Result.success(payableNo);
    }
}
