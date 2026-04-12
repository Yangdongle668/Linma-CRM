package com.crm.analytics.service.impl;

import com.crm.analytics.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * 首页仪表盘服务实现类 - 使用真实数据库数据
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> getTodayTodos(Long userId) {
        log.info("获取今日待办 - 用户ID:{}", userId);

        List<Map<String, Object>> todos = new ArrayList<>();

        try {
            // 查询今日需要跟进的客户（根据用户角色）
            String todoSql = """
                SELECT 
                    c.id as customerId,
                    c.company_name as customerName,
                    c.next_follow_time as nextFollowTime,
                    'follow_up' as type,
                    CASE 
                        WHEN c.priority = 'high' THEN 'high'
                        WHEN c.priority = 'medium' THEN 'medium'
                        ELSE 'low'
                    END as priority
                FROM crm_customer c
                WHERE c.deleted = 0
                  AND c.status = 1
                  AND DATE(c.next_follow_time) = CURDATE()
                  %s
                ORDER BY c.priority DESC, c.next_follow_time ASC
                LIMIT 10
                """;

            String whereClause = (userId != null) ? "AND c.owner_id = ?" : "";
            String sql = String.format(todoSql, whereClause);

            List<Map<String, Object>> followUpTodos = (userId != null) 
                ? jdbcTemplate.queryForList(sql, userId)
                : jdbcTemplate.queryForList(sql);

            todos.addAll(followUpTodos);

            // 查询即将逾期的应收账款
            String receivableSql = """
                SELECT 
                    r.id as receivableId,
                    c.company_name as customerName,
                    r.amount,
                    r.due_date as dueDate,
                    'receivable' as type,
                    'high' as priority
                FROM crm_receivable r
                LEFT JOIN crm_customer c ON r.customer_id = c.id
                WHERE r.deleted = 0
                  AND r.status = 'unpaid'
                  AND r.due_date <= DATE_ADD(CURDATE(), INTERVAL 7 DAY)
                  %s
                ORDER BY r.due_date ASC
                LIMIT 5
                """;

            String receivableWhere = (userId != null) ? "AND r.owner_id = ?" : "";
            String receivableQuery = String.format(receivableSql, receivableWhere);

            List<Map<String, Object>> receivableTodos = (userId != null)
                ? jdbcTemplate.queryForList(receivableQuery, userId)
                : jdbcTemplate.queryForList(receivableQuery);

            todos.addAll(receivableTodos);

        } catch (Exception e) {
            log.error("获取待办事项失败", e);
        }

        return todos;
    }

    @Override
    public Map<String, Object> getMonthlyPerformance(Long userId) {
        log.info("获取本月业绩 - 用户ID:{}", userId);

        Map<String, Object> performance = new HashMap<>();

        try {
            // 统计本月业绩（根据用户角色）
            String sql = """
                SELECT
                    COUNT(*) as orderCount,
                    COALESCE(SUM(total_amount), 0) as totalSales,
                    COALESCE(SUM(profit_amount), 0) as totalProfit,
                    CASE 
                        WHEN COUNT(*) > 0 THEN COALESCE(AVG(profit_margin), 0)
                        ELSE 0
                    END as avgProfitMargin
                FROM crm_order
                WHERE deleted = 0
                  AND status != 'cancelled'
                  %s
                  AND YEAR(order_date) = YEAR(CURDATE())
                  AND MONTH(order_date) = MONTH(CURDATE())
                """;

            String whereClause = (userId != null) ? "AND owner_id = ?" : "";
            String query = String.format(sql, whereClause);

            Map<String, Object> result = (userId != null)
                ? jdbcTemplate.queryForMap(query, userId)
                : jdbcTemplate.queryForMap(query);

            performance.put("orderCount", result.get("orderCount"));
            performance.put("totalSales", result.get("totalSales"));
            performance.put("totalProfit", result.get("totalProfit"));
            performance.put("avgProfitMargin", result.get("avgProfitMargin"));

            // 计算目标完成率（假设月度目标为100000）
            double target = 100000.0;
            double totalSales = ((Number) result.get("totalSales")).doubleValue();
            performance.put("targetCompletion", Math.round((totalSales / target) * 100 * 100.0) / 100.0);

        } catch (Exception e) {
            log.error("获取本月业绩失败", e);
            performance.put("orderCount", 0);
            performance.put("totalSales", 0.0);
            performance.put("totalProfit", 0.0);
            performance.put("avgProfitMargin", 0.0);
            performance.put("targetCompletion", 0.0);
        }

        return performance;
    }

    @Override
    public Map<String, Object> getKeyMetrics() {
        log.info("获取关键指标");

        Map<String, Object> metrics = new HashMap<>();

        try {
            // 总客户数
            metrics.put("totalCustomers", getCount("SELECT COUNT(*) FROM crm_customer WHERE deleted = 0 AND status = 1"));

            // 本月新增客户
            metrics.put("newCustomersThisMonth", getCount(
                "SELECT COUNT(*) FROM crm_customer WHERE deleted = 0 AND YEAR(created_time) = YEAR(CURDATE()) AND MONTH(created_time) = MONTH(CURDATE())"
            ));

            // 公海客户数
            metrics.put("highSeaCustomers", getCount(
                "SELECT COUNT(*) FROM crm_customer WHERE deleted = 0 AND is_high_sea = 1"
            ));

            // 待处理询盘
            metrics.put("pendingInquiries", getCount(
                "SELECT COUNT(*) FROM crm_inquiry WHERE deleted = 0 AND status = 'pending'"
            ));

            // 本月订单数
            metrics.put("ordersThisMonth", getCount(
                "SELECT COUNT(*) FROM crm_order WHERE deleted = 0 AND status != 'cancelled' AND YEAR(order_date) = YEAR(CURDATE()) AND MONTH(order_date) = MONTH(CURDATE())"
            ));

            // 本月销售额
            metrics.put("salesThisMonth", getSum(
                "SELECT COALESCE(SUM(total_amount), 0) FROM crm_order WHERE deleted = 0 AND status != 'cancelled' AND YEAR(order_date) = YEAR(CURDATE()) AND MONTH(order_date) = MONTH(CURDATE())"
            ));

            // 应收金额
            metrics.put("receivableAmount", getSum(
                "SELECT COALESCE(SUM(amount), 0) FROM crm_receivable WHERE deleted = 0 AND status = 'unpaid'"
            ));

            // 逾期应收
            metrics.put("overdueReceivable", getSum(
                "SELECT COALESCE(SUM(amount), 0) FROM crm_receivable WHERE deleted = 0 AND status = 'unpaid' AND due_date < CURDATE()"
            ));

        } catch (Exception e) {
            log.error("获取关键指标失败", e);
            // 设置默认值
            metrics.putIfAbsent("totalCustomers", 0);
            metrics.putIfAbsent("newCustomersThisMonth", 0);
            metrics.putIfAbsent("highSeaCustomers", 0);
            metrics.putIfAbsent("pendingInquiries", 0);
            metrics.putIfAbsent("ordersThisMonth", 0);
            metrics.putIfAbsent("salesThisMonth", 0.0);
            metrics.putIfAbsent("receivableAmount", 0.0);
            metrics.putIfAbsent("overdueReceivable", 0.0);
        }

        return metrics;
    }

    @Override
    public Map<String, Object> getDashboardData(Long userId) {
        log.info("获取仪表盘完整数据 - 用户ID:{}", userId);

        Map<String, Object> dashboardData = new HashMap<>();

        // 组装所有仪表盘数据
        dashboardData.put("keyMetrics", getKeyMetrics());
        dashboardData.put("todayTodos", getTodayTodos(userId));
        dashboardData.put("monthlyPerformance", getMonthlyPerformance(userId));

        // 添加图表数据
        dashboardData.put("salesTrend", getLast7DaysSales());
        dashboardData.put("topProducts", getTop5Products());
        dashboardData.put("customerDistribution", getCustomerDistribution());

        return dashboardData;
    }

    /**
     * 获取最近7天销售趋势
     */
    private List<Map<String, Object>> getLast7DaysSales() {
        try {
            String sql = """
                SELECT 
                    DATE(order_date) as date,
                    COALESCE(SUM(total_amount), 0) as sales,
                    COUNT(*) as orderCount
                FROM crm_order
                WHERE deleted = 0
                  AND status != 'cancelled'
                  AND order_date >= DATE_SUB(CURDATE(), INTERVAL 6 DAY)
                GROUP BY DATE(order_date)
                ORDER BY date ASC
                """;

            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            log.error("获取销售趋势失败", e);
            return new ArrayList<>();
        }
    }

    /**
     * 获取热销产品TOP5
     */
    private List<Map<String, Object>> getTop5Products() {
        try {
            String sql = """
                SELECT 
                    p.product_name as productName,
                    SUM(oi.quantity) as totalQuantity,
                    COALESCE(SUM(oi.total_price), 0) as totalSales
                FROM crm_order_item oi
                LEFT JOIN crm_product p ON oi.product_id = p.id
                WHERE oi.deleted = 0
                  AND YEAR(oi.created_time) = YEAR(CURDATE())
                  AND MONTH(oi.created_time) = MONTH(CURDATE())
                GROUP BY p.id, p.product_name
                ORDER BY totalSales DESC
                LIMIT 5
                """;

            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            log.error("获取热销产品失败", e);
            return new ArrayList<>();
        }
    }

    /**
     * 获取客户分布（按国家/地区）
     */
    private List<Map<String, Object>> getCustomerDistribution() {
        try {
            String sql = """
                SELECT 
                    country,
                    COUNT(*) as count
                FROM crm_customer
                WHERE deleted = 0
                  AND status = 1
                  AND country IS NOT NULL
                  AND country != ''
                GROUP BY country
                ORDER BY count DESC
                LIMIT 10
                """;

            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            log.error("获取客户分布失败", e);
            return new ArrayList<>();
        }
    }

    /**
     * 辅助方法：获取计数
     */
    private long getCount(String sql) {
        try {
            return jdbcTemplate.queryForObject(sql, Long.class);
        } catch (Exception e) {
            log.warn("执行计数查询失败: {}", sql, e);
            return 0;
        }
    }

    /**
     * 辅助方法：获取总和
     */
    private double getSum(String sql) {
        try {
            Number result = jdbcTemplate.queryForObject(sql, Number.class);
            return result != null ? result.doubleValue() : 0.0;
        } catch (Exception e) {
            log.warn("执行求和查询失败: {}", sql, e);
            return 0.0;
        }
    }
}
