package com.crm.customer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.crm.common.core.domain.Result;
import com.crm.customer.domain.dto.FollowUpCreateDTO;
import com.crm.customer.domain.vo.FollowUpVO;
import com.crm.customer.service.FollowUpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 跟进记录管理控制器
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Tag(name = "跟进记录管理", description = "跟进记录的增删改查、提醒设置等功能")
@RestController
@RequestMapping("/followup")
@RequiredArgsConstructor
public class FollowUpController {

    private final FollowUpService followUpService;

    @Operation(summary = "分页查询跟进记录列表")
    @GetMapping("/page")
    public Result<IPage<FollowUpVO>> pageFollowUps(
            @Parameter(description = "客户ID") @RequestParam(required = false) Long customerId,
            @Parameter(description = "跟进人ID") @RequestParam(required = false) Long followUserId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        IPage<FollowUpVO> page = followUpService.pageFollowUps(customerId, followUserId, pageNum, pageSize);
        return Result.success(page);
    }

    @Operation(summary = "根据ID查询跟进记录详情")
    @GetMapping("/{id}")
    public Result<FollowUpVO> getFollowUpById(
            @Parameter(description = "跟进记录ID") @PathVariable Long id) {
        FollowUpVO followUp = followUpService.getFollowUpById(id);
        return Result.success(followUp);
    }

    @Operation(summary = "获取客户的跟进记录时间轴")
    @GetMapping("/timeline/{customerId}")
    public Result<List<FollowUpVO>> getFollowUpTimeline(
            @Parameter(description = "客户ID") @PathVariable Long customerId) {
        List<FollowUpVO> timeline = followUpService.getFollowUpTimeline(customerId);
        return Result.success(timeline);
    }

    @Operation(summary = "创建跟进记录")
    @PostMapping
    public Result<Void> createFollowUp(
            @Parameter(description = "跟进记录信息") @Valid @RequestBody FollowUpCreateDTO dto) {
        boolean success = followUpService.createFollowUp(dto);
        return success ? Result.success() : Result.error("创建失败");
    }

    @Operation(summary = "更新跟进记录")
    @PutMapping("/{id}")
    public Result<Void> updateFollowUp(
            @Parameter(description = "跟进记录ID") @PathVariable Long id,
            @Parameter(description = "跟进内容") @RequestParam(required = false) String followContent,
            @Parameter(description = "下一步计划") @RequestParam(required = false) String nextPlan,
            @Parameter(description = "下次跟进时间") @RequestParam(required = false) String nextFollowTime,
            @Parameter(description = "客户满意度") @RequestParam(required = false) Integer satisfaction) {
        boolean success = followUpService.updateFollowUp(id, followContent, nextPlan, nextFollowTime, satisfaction);
        return success ? Result.success() : Result.error("更新失败");
    }

    @Operation(summary = "删除跟进记录")
    @DeleteMapping("/{ids}")
    public Result<Void> deleteFollowUps(
            @Parameter(description = "跟进记录ID列表") @PathVariable List<Long> ids) {
        boolean success = followUpService.deleteFollowUps(ids);
        return success ? Result.success() : Result.error("删除失败");
    }

    @Operation(summary = "设置跟进提醒")
    @PutMapping("/reminder/{followUpId}")
    public Result<Void> setReminder(
            @Parameter(description = "跟进记录ID") @PathVariable Long followUpId,
            @Parameter(description = "下次跟进时间") @RequestParam String nextFollowTime) {
        boolean success = followUpService.setReminder(followUpId, nextFollowTime);
        return success ? Result.success() : Result.error("设置失败");
    }

    @Operation(summary = "今日需跟进客户列表")
    @GetMapping("/today")
    public Result<List<FollowUpVO>> getTodayFollowUps() {
        List<FollowUpVO> todayFollowUps = followUpService.getTodayFollowUps();
        return Result.success(todayFollowUps);
    }

    @Operation(summary = "即将到期的跟进提醒")
    @GetMapping("/upcoming")
    public Result<List<FollowUpVO>> getUpcomingReminders(
            @Parameter(description = "未来多少小时") @RequestParam(required = false) Integer hours) {
        List<FollowUpVO> reminders = followUpService.getUpcomingReminders(hours);
        return Result.success(reminders);
    }
}
