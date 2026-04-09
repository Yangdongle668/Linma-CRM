package com.crm.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.crm.common.core.domain.Result;
import com.crm.finance.domain.dto.PaymentRecordDTO;
import com.crm.finance.domain.dto.ReceivableQuery;
import com.crm.finance.domain.vo.ReceivableVO;
import com.crm.finance.service.ReceivableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 应收账款控制器
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Tag(name = "应收账款管理", description = "应收账款的查询、收款记录、逾期提醒、账龄分析等功能")
@RestController
@RequestMapping("/finance/receivable")
@RequiredArgsConstructor
public class ReceivableController {

    private final ReceivableService receivableService;

    @Operation(summary = "分页查询应收账款列表")
    @GetMapping("/page")
    public Result<IPage<ReceivableVO>> pageReceivables(
            @Parameter(description = "查询条件") ReceivableQuery query,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        IPage<ReceivableVO> page = receivableService.pageReceivables(query, pageNum, pageSize);
        return Result.success(page);
    }

    @Operation(summary = "根据ID查询应收账款详情")
    @GetMapping("/{id}")
    public Result<ReceivableVO> getReceivableById(
            @Parameter(description = "应收ID") @PathVariable Long id) {
        ReceivableVO receivable = receivableService.getReceivableById(id);
        return Result.success(receivable);
    }

    @Operation(summary = "记录收款")
    @PostMapping("/record-payment")
    public Result<Void> recordPayment(
            @Parameter(description = "收款记录信息") @Valid @RequestBody PaymentRecordDTO dto) {
        boolean success = receivableService.recordPayment(dto);
        return success ? Result.success() : Result.error("收款记录失败");
    }

    @Operation(summary = "获取逾期应收列表")
    @GetMapping("/overdue")
    public Result<List<ReceivableVO>> getOverdueReceivables() {
        List<ReceivableVO> receivables = receivableService.getOverdueReceivables();
        return Result.success(receivables);
    }

    @Operation(summary = "获取账龄分析数据")
    @GetMapping("/aging-analysis")
    public Result<Map<String, Object>> getAgingAnalysis() {
        Map<String, Object> analysis = receivableService.getAgingAnalysis();
        return Result.success(analysis);
    }

    @Operation(summary = "生成应收单号")
    @GetMapping("/generate-no")
    public Result<String> generateReceivableNo() {
        String receivableNo = receivableService.generateReceivableNo();
        return Result.success(receivableNo);
    }
}
