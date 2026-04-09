package com.crm.message.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.crm.common.core.domain.Result;
import com.crm.message.domain.dto.NotificationQuery;
import com.crm.message.domain.vo.NotificationVO;
import com.crm.message.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 站内消息控制器
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Tag(name = "站内消息", description = "消息列表、未读计数、标记已读等功能")
@RestController
@RequestMapping("/message/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "分页查询消息列表")
    @GetMapping("/page")
    public Result<IPage<NotificationVO>> pageNotifications(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "查询条件") NotificationQuery query,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        IPage<NotificationVO> page = notificationService.pageNotifications(userId, query, pageNum, pageSize);
        return Result.success(page);
    }

    @Operation(summary = "获取未读消息数量")
    @GetMapping("/unread-count")
    public Result<Long> getUnreadCount(
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        Long count = notificationService.getUnreadCount(userId);
        return Result.success(count);
    }

    @Operation(summary = "标记消息为已读")
    @PostMapping("/{id}/mark-read")
    public Result<Void> markAsRead(
            @Parameter(description = "消息ID") @PathVariable Long id,
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        boolean success = notificationService.markAsRead(id, userId);
        return success ? Result.success() : Result.error("标记失败");
    }

    @Operation(summary = "批量标记已读")
    @PostMapping("/batch-mark-read")
    public Result<Void> batchMarkAsRead(
            @Parameter(description = "消息ID列表") @RequestBody List<Long> messageIds,
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        boolean success = notificationService.batchMarkAsRead(messageIds, userId);
        return success ? Result.success() : Result.error("批量标记失败");
    }
}
