package com.crm.inquiry.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.common.core.domain.Result;
import com.crm.inquiry.domain.dto.InquiryCreateDTO;
import com.crm.inquiry.domain.dto.InquiryQuery;
import com.crm.inquiry.domain.dto.InquiryReplyDTO;
import com.crm.inquiry.domain.dto.InquiryUpdateDTO;
import com.crm.inquiry.domain.vo.InquiryVO;
import com.crm.inquiry.service.InquiryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 询盘管理Controller
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Tag(name = "询盘管理", description = "询盘相关接口")
@RestController
@RequestMapping("/inquiry")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    /**
     * 分页查询询盘
     */
    @Operation(summary = "分页查询询盘", description = "根据条件分页查询询盘列表")
    @GetMapping("/page")
    public Result<IPage<InquiryVO>> getInquiryPage(
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量", example = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            InquiryQuery query) {
        Page<InquiryVO> page = new Page<>(pageNum, pageSize);
        IPage<InquiryVO> result = inquiryService.getInquiryPage(page, query);
        return Result.success(result);
    }

    /**
     * 获取询盘详情
     */
    @Operation(summary = "获取询盘详情", description = "根据ID获取询盘详细信息")
    @GetMapping("/{id}")
    public Result<InquiryVO> getInquiry(
            @Parameter(description = "询盘ID") @PathVariable Long id) {
        InquiryVO inquiry = inquiryService.getInquiryById(id);
        return Result.success(inquiry);
    }

    /**
     * 创建询盘
     */
    @Operation(summary = "创建询盘", description = "创建新的询盘")
    @PostMapping
    public Result<Long> createInquiry(@Valid @RequestBody InquiryCreateDTO dto) {
        Long inquiryId = inquiryService.createInquiry(dto);
        return Result.success(inquiryId);
    }

    /**
     * 更新询盘
     */
    @Operation(summary = "更新询盘", description = "更新询盘信息")
    @PutMapping
    public Result<Void> updateInquiry(@Valid @RequestBody InquiryUpdateDTO dto) {
        inquiryService.updateInquiry(dto);
        return Result.success();
    }

    /**
     * 删除询盘
     */
    @Operation(summary = "删除询盘", description = "批量删除询盘")
    @DeleteMapping
    public Result<Void> deleteInquiries(
            @Parameter(description = "询盘ID列表") @RequestBody List<Long> ids) {
        inquiryService.deleteInquiries(ids);
        return Result.success();
    }

    /**
     * 生成询盘号
     */
    @Operation(summary = "生成询盘号", description = "自动生成询盘号")
    @GetMapping("/generate-no")
    public Result<String> generateInquiryNo() {
        String inquiryNo = inquiryService.generateInquiryNo();
        return Result.success(inquiryNo);
    }

    /**
     * 回复询盘
     */
    @Operation(summary = "回复询盘", description = "回复询盘并记录回复内容")
    @PostMapping("/reply")
    public Result<Void> replyInquiry(@Valid @RequestBody InquiryReplyDTO replyDTO) {
        inquiryService.replyInquiry(replyDTO);
        return Result.success();
    }

    /**
     * 变更询盘状态
     */
    @Operation(summary = "变更询盘状态", description = "变更询盘的状态")
    @PutMapping("/{id}/status")
    public Result<Void> changeStatus(
            @Parameter(description = "询盘ID") @PathVariable Long id,
            @Parameter(description = "新状态") @RequestParam String newStatus,
            @Parameter(description = "原因") @RequestParam(required = false) String reason) {
        inquiryService.changeStatus(id, newStatus, reason);
        return Result.success();
    }

    /**
     * 转为报价单
     */
    @Operation(summary = "转为报价单", description = "将询盘转化为报价单")
    @PostMapping("/{id}/convert-to-quotation")
    public Result<Long> convertToQuotation(
            @Parameter(description = "询盘ID") @PathVariable Long id) {
        Long quotationId = inquiryService.convertToQuotation(id);
        return Result.success(quotationId);
    }

    /**
     * 关闭询盘
     */
    @Operation(summary = "关闭询盘", description = "关闭询盘并记录原因")
    @PostMapping("/{id}/close")
    public Result<Void> closeInquiry(
            @Parameter(description = "询盘ID") @PathVariable Long id,
            @Parameter(description = "关闭原因") @RequestParam String reason) {
        inquiryService.closeInquiry(id, reason);
        return Result.success();
    }

    /**
     * 分配询盘
     */
    @Operation(summary = "分配询盘", description = "将询盘分配给指定负责人")
    @PostMapping("/{id}/assign")
    public Result<Void> assignInquiry(
            @Parameter(description = "询盘ID") @PathVariable Long id,
            @Parameter(description = "负责人ID") @RequestParam Long ownerId) {
        inquiryService.assignInquiry(id, ownerId);
        return Result.success();
    }

    /**
     * 自动分配询盘
     */
    @Operation(summary = "自动分配询盘", description = "根据策略自动分配询盘")
    @PostMapping("/{id}/auto-assign")
    public Result<Void> autoAssignInquiry(
            @Parameter(description = "询盘ID") @PathVariable Long id,
            @Parameter(description = "分配策略(round_robin/load_balance/region_match)") 
            @RequestParam(defaultValue = "round_robin") String strategy) {
        inquiryService.autoAssignInquiry(id, strategy);
        return Result.success();
    }

    /**
     * 导出询盘
     */
    @Operation(summary = "导出询盘", description = "根据条件导出询盘数据")
    @GetMapping("/export")
    public Result<List<InquiryVO>> exportInquiries(InquiryQuery query) {
        List<InquiryVO> inquiries = inquiryService.exportInquiries(query);
        return Result.success(inquiries);
    }

    /**
     * 统计待处理询盘数量
     */
    @Operation(summary = "统计待处理询盘", description = "统计指定负责人的待处理询盘数量")
    @GetMapping("/count/pending")
    public Result<Integer> countPendingInquiries(
            @Parameter(description = "负责人ID") @RequestParam Long ownerId) {
        Integer count = inquiryService.countPendingInquiries(ownerId);
        return Result.success(count);
    }

    /**
     * 统计已转化询盘数量
     */
    @Operation(summary = "统计已转化询盘", description = "统计指定负责人的已转化询盘数量")
    @GetMapping("/count/converted")
    public Result<Integer> countConvertedInquiries(
            @Parameter(description = "负责人ID") @RequestParam Long ownerId) {
        Integer count = inquiryService.countConvertedInquiries(ownerId);
        return Result.success(count);
    }
}
