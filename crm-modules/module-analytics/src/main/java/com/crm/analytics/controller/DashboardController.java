package com.crm.analytics.controller;

import com.crm.analytics.service.DashboardService;
import com.crm.common.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 首页仪表盘控制器
 *
 * @author CRM System
 * @since 2026-04-12
 */
@Tag(name = "首页仪表盘", description = "提供首页统计数据、待办事项等")
@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "获取关键指标")
    @GetMapping("/metrics")
    public Result<Map<String, Object>> getKeyMetrics() {
        Map<String, Object> metrics = dashboardService.getKeyMetrics();
        return Result.success(metrics);
    }
}
