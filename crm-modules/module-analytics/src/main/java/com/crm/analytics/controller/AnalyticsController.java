package com.crm.analytics.controller;

import com.crm.analytics.service.*;
import com.crm.common.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 数据分析BI控制器
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Tag(name = "数据分析BI", description = "销售漏斗、业绩排行、客户分析、产品分析、区域分析、趋势分析、仪表盘等功能")
@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final FunnelService funnelService;
    private final RankingService rankingService;
    private final CustomerAnalyticsService customerAnalyticsService;
    private final ProductAnalyticsService productAnalyticsService;
    private final RegionAnalyticsService regionAnalyticsService;
    private final TrendService trendService;
    private final DashboardService dashboardService;

    // ==================== 销售漏斗分析 ====================

    @Operation(summary = "获取销售漏斗数据")
    @GetMapping("/funnel")
    public Result<List<Map<String, Object>>> getSalesFunnel(
            @Parameter(description = "开始日期") @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) String endDate) {
        List<Map<String, Object>> funnel = funnelService.getSalesFunnel(startDate, endDate);
        return Result.success(funnel);
    }

    @Operation(summary = "获取各阶段转化率")
    @GetMapping("/funnel/conversion-rates")
    public Result<Map<String, Object>> getConversionRates(
            @Parameter(description = "开始日期") @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) String endDate) {
        Map<String, Object> rates = funnelService.getConversionRates(startDate, endDate);
        return Result.success(rates);
    }

    // ==================== 业绩排行榜 ====================

    @Operation(summary = "获取销售额排行榜")
    @GetMapping("/ranking/sales")
    public Result<List<Map<String, Object>>> getSalesRanking(
            @Parameter(description = "统计周期(month/quarter/year)") @RequestParam(required = false, defaultValue = "month") String period,
            @Parameter(description = "数量限制") @RequestParam(required = false, defaultValue = "10") Integer limit) {
        List<Map<String, Object>> ranking = rankingService.getSalesRanking(period, limit);
        return Result.success(ranking);
    }

    @Operation(summary = "获取订单数排行榜")
    @GetMapping("/ranking/orders")
    public Result<List<Map<String, Object>>> getOrderCountRanking(
            @Parameter(description = "统计周期") @RequestParam(required = false, defaultValue = "month") String period,
            @Parameter(description = "数量限制") @RequestParam(required = false, defaultValue = "10") Integer limit) {
        List<Map<String, Object>> ranking = rankingService.getOrderCountRanking(period, limit);
        return Result.success(ranking);
    }

    @Operation(summary = "获取团队对比数据")
    @GetMapping("/ranking/team-comparison")
    public Result<List<Map<String, Object>>> getTeamComparison(
            @Parameter(description = "统计周期") @RequestParam(required = false, defaultValue = "month") String period) {
        List<Map<String, Object>> comparison = rankingService.getTeamComparison(period);
        return Result.success(comparison);
    }

    // ==================== 客户分析 ====================

    @Operation(summary = "获取新增客户趋势")
    @GetMapping("/customer/new-trend")
    public Result<List<Map<String, Object>>> getNewCustomerTrend(
            @Parameter(description = "统计周期") @RequestParam(required = false, defaultValue = "month") String period) {
        List<Map<String, Object>> trend = customerAnalyticsService.getNewCustomerTrend(period);
        return Result.success(trend);
    }

    @Operation(summary = "获取客户国家分布")
    @GetMapping("/customer/distribution/country")
    public Result<List<Map<String, Object>>> getCustomerDistributionByCountry() {
        List<Map<String, Object>> distribution = customerAnalyticsService.getCustomerDistributionByCountry();
        return Result.success(distribution);
    }

    @Operation(summary = "获取客户行业分布")
    @GetMapping("/customer/distribution/industry")
    public Result<List<Map<String, Object>>> getCustomerDistributionByIndustry() {
        List<Map<String, Object>> distribution = customerAnalyticsService.getCustomerDistributionByIndustry();
        return Result.success(distribution);
    }

    @Operation(summary = "获取客户等级分布")
    @GetMapping("/customer/distribution/level")
    public Result<List<Map<String, Object>>> getCustomerDistributionByLevel() {
        List<Map<String, Object>> distribution = customerAnalyticsService.getCustomerDistributionByLevel();
        return Result.success(distribution);
    }

    @Operation(summary = "获取RFM模型分析")
    @GetMapping("/customer/rfm")
    public Result<List<Map<String, Object>>> getRFMAnalysis() {
        List<Map<String, Object>> rfm = customerAnalyticsService.getRFMAnalysis();
        return Result.success(rfm);
    }

    @Operation(summary = "获取流失预警客户")
    @GetMapping("/customer/churn-warning")
    public Result<List<Map<String, Object>>> getChurnWarningCustomers(
            @Parameter(description = "天数阈值") @RequestParam(required = false, defaultValue = "90") Integer days) {
        List<Map<String, Object>> customers = customerAnalyticsService.getChurnWarningCustomers(days);
        return Result.success(customers);
    }

    // ==================== 产品分析 ====================

    @Operation(summary = "获取热销产品TOP10")
    @GetMapping("/product/hot-products")
    public Result<List<Map<String, Object>>> getHotProducts(
            @Parameter(description = "统计周期") @RequestParam(required = false, defaultValue = "month") String period,
            @Parameter(description = "数量限制") @RequestParam(required = false, defaultValue = "10") Integer limit) {
        List<Map<String, Object>> products = productAnalyticsService.getHotProducts(period, limit);
        return Result.success(products);
    }

    @Operation(summary = "获取滞销产品预警")
    @GetMapping("/product/slow-moving")
    public Result<List<Map<String, Object>>> getSlowMovingProducts(
            @Parameter(description = "天数阈值") @RequestParam(required = false, defaultValue = "90") Integer days) {
        List<Map<String, Object>> products = productAnalyticsService.getSlowMovingProducts(days);
        return Result.success(products);
    }

    // ==================== 区域分析 ====================

    @Operation(summary = "获取世界地图热力图数据")
    @GetMapping("/region/world-map")
    public Result<List<Map<String, Object>>> getWorldMapHeatmapData(
            @Parameter(description = "统计周期") @RequestParam(required = false, defaultValue = "year") String period) {
        List<Map<String, Object>> data = regionAnalyticsService.getWorldMapHeatmapData(period);
        return Result.success(data);
    }

    @Operation(summary = "获取区域销售对比")
    @GetMapping("/region/sales-comparison")
    public Result<List<Map<String, Object>>> getRegionSalesComparison(
            @Parameter(description = "统计周期") @RequestParam(required = false, defaultValue = "month") String period) {
        List<Map<String, Object>> comparison = regionAnalyticsService.getRegionSalesComparison(period);
        return Result.success(comparison);
    }

    // ==================== 趋势分析 ====================

    @Operation(summary = "获取月度销售趋势")
    @GetMapping("/trend/monthly")
    public Result<List<Map<String, Object>>> getMonthlySalesTrend(
            @Parameter(description = "年份") @RequestParam(required = false) Integer year) {
        if (year == null) {
            year = java.time.Year.now().getValue();
        }
        List<Map<String, Object>> trend = trendService.getMonthlySalesTrend(year);
        return Result.success(trend);
    }

    @Operation(summary = "获取同比增长率")
    @GetMapping("/trend/yoy-growth")
    public Result<Map<String, Object>> getYearOverYearGrowth(
            @Parameter(description = "统计周期") @RequestParam(required = false, defaultValue = "month") String period) {
        Map<String, Object> growth = trendService.getYearOverYearGrowth(period);
        return Result.success(growth);
    }

    @Operation(summary = "获取环比增长率")
    @GetMapping("/trend/mom-growth")
    public Result<Map<String, Object>> getMonthOverMonthGrowth(
            @Parameter(description = "统计周期") @RequestParam(required = false, defaultValue = "month") String period) {
        Map<String, Object> growth = trendService.getMonthOverMonthGrowth(period);
        return Result.success(growth);
    }

    // ==================== 首页仪表盘 ====================

    @Operation(summary = "获取仪表盘数据")
    @GetMapping("/dashboard")
    public Result<Map<String, Object>> getDashboardData(
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId) {
        if (userId == null) {
            userId = 1L; // TODO: 从登录上下文获取
        }
        Map<String, Object> dashboard = dashboardService.getDashboardData(userId);
        return Result.success(dashboard);
    }

    @Operation(summary = "获取关键指标")
    @GetMapping("/dashboard/key-metrics")
    public Result<Map<String, Object>> getKeyMetrics() {
        Map<String, Object> metrics = dashboardService.getKeyMetrics();
        return Result.success(metrics);
    }

    @Operation(summary = "获取今日待办")
    @GetMapping("/dashboard/todos")
    public Result<List<Map<String, Object>>> getTodayTodos(
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId) {
        if (userId == null) {
            userId = 1L; // TODO: 从登录上下文获取
        }
        List<Map<String, Object>> todos = dashboardService.getTodayTodos(userId);
        return Result.success(todos);
    }
}
public class AnalyticsController {
    
}
