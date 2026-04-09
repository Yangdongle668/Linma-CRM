package com.crm.finance.controller;

import com.crm.common.core.domain.Result;
import com.crm.finance.service.ProfitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 利润核算控制器
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Tag(name = "利润核算", description = "订单利润计算、利润统计报表、利润趋势分析等功能")
@RestController
@RequestMapping("/finance/profit")
@RequiredArgsConstructor
public class ProfitController {

    private final ProfitService profitService;

    @Operation(summary = "计算单笔订单利润")
    @GetMapping("/order/{orderId}")
    public Result<Map<String, Object>> calculateOrderProfit(
            @Parameter(description = "订单ID") @PathVariable Long orderId) {
        Map<String, Object> profit = profitService.calculateOrderProfit(orderId);
        return Result.success(profit);
    }

    @Operation(summary = "获取利润统计报表")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getProfitStatistics(
            @Parameter(description = "开始日期") @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) String endDate) {
        Map<String, Object> statistics = profitService.getProfitStatistics(startDate, endDate);
        return Result.success(statistics);
    }

    @Operation(summary = "获取月度利润趋势")
    @GetMapping("/monthly-trend")
    public Result<List<Map<String, Object>>> getMonthlyProfitTrend(
            @Parameter(description = "年份") @RequestParam(required = false) Integer year) {
        if (year == null) {
            year = java.time.Year.now().getValue();
        }
        List<Map<String, Object>> trend = profitService.getMonthlyProfitTrend(year);
        return Result.success(trend);
    }

    @Operation(summary = "获取产品利润排行")
    @GetMapping("/product-ranking")
    public Result<List<Map<String, Object>>> getProductProfitRanking(
            @Parameter(description = "数量限制") @RequestParam(required = false, defaultValue = "10") Integer limit) {
        List<Map<String, Object>> ranking = profitService.getProductProfitRanking(limit);
        return Result.success(ranking);
    }

    @Operation(summary = "获取客户利润贡献排行")
    @GetMapping("/customer-contribution")
    public Result<List<Map<String, Object>>> getCustomerProfitContribution(
            @Parameter(description = "数量限制") @RequestParam(required = false, defaultValue = "10") Integer limit) {
        List<Map<String, Object>> contribution = profitService.getCustomerProfitContribution(limit);
        return Result.success(contribution);
    }
}
