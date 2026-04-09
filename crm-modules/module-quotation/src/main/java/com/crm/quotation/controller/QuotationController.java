package com.crm.quotation.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.crm.common.core.domain.Result;
import com.crm.quotation.domain.dto.*;
import com.crm.quotation.domain.vo.QuotationVO;
import com.crm.quotation.service.QuotationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 报价单管理控制器
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Tag(name = "报价单管理", description = "报价单的增删改查、审批、PDF生成、邮件发送等功能")
@RestController
@RequestMapping("/quotation")
@RequiredArgsConstructor
public class QuotationController {

    private final QuotationService quotationService;

    @Operation(summary = "分页查询报价单列表")
    @GetMapping("/page")
    public Result<IPage<QuotationVO>> pageQuotations(
            @Parameter(description = "查询条件") QuotationQuery query,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        IPage<QuotationVO> page = quotationService.pageQuotations(query, pageNum, pageSize);
        return Result.success(page);
    }

    @Operation(summary = "根据ID查询报价单详情")
    @GetMapping("/{id}")
    public Result<QuotationVO> getQuotationById(
            @Parameter(description = "报价单ID") @PathVariable Long id) {
        QuotationVO quotation = quotationService.getQuotationById(id);
        return Result.success(quotation);
    }

    @Operation(summary = "创建报价单")
    @PostMapping
    public Result<Long> createQuotation(
            @Parameter(description = "报价单信息") @Valid @RequestBody QuotationCreateDTO dto) {
        Long quotationId = quotationService.createQuotation(dto);
        return Result.success(quotationId);
    }

    @Operation(summary = "更新报价单")
    @PutMapping
    public Result<Void> updateQuotation(
            @Parameter(description = "报价单信息") @Valid @RequestBody QuotationUpdateDTO dto) {
        boolean success = quotationService.updateQuotation(dto);
        return success ? Result.success() : Result.error("更新失败");
    }

    @Operation(summary = "删除报价单")
    @DeleteMapping("/{ids}")
    public Result<Void> deleteQuotations(
            @Parameter(description = "报价单ID列表") @PathVariable List<Long> ids) {
        boolean success = quotationService.deleteQuotations(ids);
        return success ? Result.success() : Result.error("删除失败");
    }

    @Operation(summary = "提交审批")
    @PostMapping("/{id}/submit-approval")
    public Result<Void> submitForApproval(
            @Parameter(description = "报价单ID") @PathVariable Long id) {
        boolean success = quotationService.submitForApproval(id);
        return success ? Result.success() : Result.error("提交审批失败");
    }

    @Operation(summary = "审批报价单")
    @PostMapping("/approve")
    public Result<Void> approveQuotation(
            @Parameter(description = "审批信息") @Valid @RequestBody QuotationApprovalDTO dto) {
        boolean success = quotationService.approveQuotation(dto);
        return success ? Result.success() : Result.error("审批失败");
    }

    @Operation(summary = "生成PDF报价单")
    @PostMapping("/{id}/generate-pdf")
    public Result<String> generatePdf(
            @Parameter(description = "报价单ID") @PathVariable Long id,
            @Parameter(description = "语言(en/cn)") @RequestParam(defaultValue = "en") String language) {
        String pdfPath = quotationService.generatePdf(id, language);
        return Result.success(pdfPath);
    }

    @Operation(summary = "发送邮件")
    @PostMapping("/send-email")
    public Result<Void> sendEmail(
            @Parameter(description = "邮件信息") @Valid @RequestBody QuotationEmailDTO dto) {
        boolean success = quotationService.sendEmail(dto);
        return success ? Result.success() : Result.error("发送失败");
    }

    @Operation(summary = "从询盘创建报价单")
    @PostMapping("/from-inquiry/{inquiryId}")
    public Result<Long> createFromInquiry(
            @Parameter(description = "询盘ID") @PathVariable Long inquiryId) {
        Long quotationId = quotationService.createFromInquiry(inquiryId);
        return Result.success(quotationId);
    }

    @Operation(summary = "转为订单")
    @PostMapping("/{id}/convert-to-order")
    public Result<Long> convertToOrder(
            @Parameter(description = "报价单ID") @PathVariable Long id) {
        Long orderId = quotationService.convertToOrder(id);
        return Result.success(orderId);
    }

    @Operation(summary = "生成报价单号")
    @GetMapping("/generate-no")
    public Result<String> generateQuotationNo() {
        String quotationNo = quotationService.generateQuotationNo();
        return Result.success(quotationNo);
    }

    @Operation(summary = "获取历史版本列表")
    @GetMapping("/{id}/versions")
    public Result<List<QuotationVO>> getVersionHistory(
            @Parameter(description = "报价单ID") @PathVariable Long id) {
        List<QuotationVO> versions = quotationService.getVersionHistory(id);
        return Result.success(versions);
    }

    @Operation(summary = "复制报价单")
    @PostMapping("/{id}/copy")
    public Result<Long> copyQuotation(
            @Parameter(description = "报价单ID") @PathVariable Long id) {
        Long newQuotationId = quotationService.copyQuotation(id);
        return Result.success(newQuotationId);
    }

    @Operation(summary = "导出报价单(Excel)")
    @GetMapping("/export")
    public void exportQuotations(
            @Parameter(description = "查询条件") QuotationQuery query,
            HttpServletResponse response) throws IOException {
        List<QuotationVO> quotations = quotationService.exportQuotations(query);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("报价单列表_" + System.currentTimeMillis(), "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        // 使用EasyExcel导出
        EasyExcel.write(response.getOutputStream(), QuotationVO.class)
                .sheet("报价单列表")
                .doWrite(quotations);
    }

    @Operation(summary = "检查是否过期")
    @GetMapping("/{id}/check-expired")
    public Result<Boolean> checkExpired(
            @Parameter(description = "报价单ID") @PathVariable Long id) {
        boolean expired = quotationService.isExpired(id);
        return Result.success(expired);
    }

    @Operation(summary = "自动标记过期报价单")
    @PostMapping("/auto-mark-expired")
    public Result<Integer> autoMarkExpired() {
        int count = quotationService.autoMarkExpired();
        return Result.success(count);
    }
}
