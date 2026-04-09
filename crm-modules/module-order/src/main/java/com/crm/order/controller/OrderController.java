package com.crm.order.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.crm.common.core.domain.Result;
import com.crm.order.domain.dto.*;
import com.crm.order.domain.vo.OrderVO;
import com.crm.order.service.InvoiceService;
import com.crm.order.service.OrderService;
import com.crm.order.service.PackingListService;
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
 * 订单管理控制器
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Tag(name = "订单管理", description = "订单的增删改查、状态管理、生产进度、发票管理等功能")
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final PackingListService packingListService;
    private final InvoiceService invoiceService;

    @Operation(summary = "分页查询订单列表")
    @GetMapping("/page")
    public Result<IPage<OrderVO>> pageOrders(
            @Parameter(description = "查询条件") OrderQuery query,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        IPage<OrderVO> page = orderService.pageOrders(query, pageNum, pageSize);
        return Result.success(page);
    }

    @Operation(summary = "根据ID查询订单详情")
    @GetMapping("/{id}")
    public Result<OrderVO> getOrderById(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        OrderVO order = orderService.getOrderById(id);
        return Result.success(order);
    }

    @Operation(summary = "创建订单")
    @PostMapping
    public Result<Long> createOrder(
            @Parameter(description = "订单信息") @Valid @RequestBody OrderCreateDTO dto) {
        Long orderId = orderService.createOrder(dto);
        return Result.success(orderId);
    }

    @Operation(summary = "更新订单")
    @PutMapping
    public Result<Void> updateOrder(
            @Parameter(description = "订单信息") @Valid @RequestBody OrderUpdateDTO dto) {
        boolean success = orderService.updateOrder(dto);
        return success ? Result.success() : Result.error("更新失败");
    }

    @Operation(summary = "删除订单")
    @DeleteMapping("/{ids}")
    public Result<Void> deleteOrders(
            @Parameter(description = "订单ID列表") @PathVariable List<Long> ids) {
        boolean success = orderService.deleteOrders(ids);
        return success ? Result.success() : Result.error("删除失败");
    }

    @Operation(summary = "变更订单状态")
    @PostMapping("/change-status")
    public Result<Void> changeStatus(
            @Parameter(description = "状态变更信息") @Valid @RequestBody OrderStatusChangeDTO dto) {
        boolean success = orderService.changeStatus(dto);
        return success ? Result.success() : Result.error("状态变更失败");
    }

    @Operation(summary = "更新生产进度")
    @PostMapping("/update-production-progress")
    public Result<Void> updateProductionProgress(
            @Parameter(description = "生产进度信息") @Valid @RequestBody ProductionProgressDTO dto) {
        boolean success = orderService.updateProductionProgress(dto);
        return success ? Result.success() : Result.error("更新失败");
    }

    @Operation(summary = "从报价单创建订单")
    @PostMapping("/from-quotation/{quotationId}")
    public Result<Long> createFromQuotation(
            @Parameter(description = "报价单ID") @PathVariable Long quotationId) {
        Long orderId = orderService.createFromQuotation(quotationId);
        return Result.success(orderId);
    }

    @Operation(summary = "生成PI形式发票PDF")
    @GetMapping("/{id}/pi-invoice/pdf")
    public void generatePiInvoicePdf(
            @Parameter(description = "订单ID") @PathVariable Long id,
            HttpServletResponse response) throws IOException {
        byte[] pdfBytes = invoiceService.generatePiInvoice(id);

        response.setContentType("application/pdf");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("PI_Invoice_" + id + ".pdf", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.getOutputStream().write(pdfBytes);
        response.getOutputStream().flush();
    }

    @Operation(summary = "生成CI商业发票PDF")
    @GetMapping("/{id}/ci-invoice/pdf")
    public void generateCiInvoicePdf(
            @Parameter(description = "订单ID") @PathVariable Long id,
            HttpServletResponse response) throws IOException {
        byte[] pdfBytes = invoiceService.generateCiInvoice(id);

        response.setContentType("application/pdf");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("CI_Invoice_" + id + ".pdf", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.getOutputStream().write(pdfBytes);
        response.getOutputStream().flush();
    }

    @Operation(summary = "生成装箱单Excel")
    @GetMapping("/{id}/packing-list/excel")
    public void generatePackingListExcel(
            @Parameter(description = "订单ID") @PathVariable Long id,
            HttpServletResponse response) throws IOException {
        byte[] excelBytes = packingListService.generatePackingListExcel(id);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("Packing_List_" + id + ".xlsx", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.getOutputStream().write(excelBytes);
        response.getOutputStream().flush();
    }

    @Operation(summary = "生成PI形式发票号")
    @PostMapping("/{id}/generate-pi")
    public Result<String> generatePiInvoice(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        String piNo = orderService.generatePiInvoice(id);
        return Result.success(piNo);
    }

    @Operation(summary = "生成CI商业发票号")
    @PostMapping("/{id}/generate-ci")
    public Result<String> generateCiInvoice(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        String ciNo = orderService.generateCiInvoice(id);
        return Result.success(ciNo);
    }

    @Operation(summary = "生成装箱单号")
    @PostMapping("/{id}/generate-packing-list")
    public Result<String> generatePackingList(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        String plNo = orderService.generatePackingList(id);
        return Result.success(plNo);
    }

    @Operation(summary = "确认收款")
    @PostMapping("/{id}/confirm-payment")
    public Result<Void> confirmPayment(
            @Parameter(description = "订单ID") @PathVariable Long id,
            @Parameter(description = "收款金额") @RequestParam java.math.BigDecimal amount) {
        boolean success = orderService.confirmPayment(id, amount);
        return success ? Result.success() : Result.error("确认收款失败");
    }

    @Operation(summary = "取消订单")
    @PostMapping("/{id}/cancel")
    public Result<Void> cancelOrder(
            @Parameter(description = "订单ID") @PathVariable Long id,
            @Parameter(description = "取消原因") @RequestParam String reason) {
        boolean success = orderService.cancelOrder(id, reason);
        return success ? Result.success() : Result.error("取消失败");
    }

    @Operation(summary = "复制订单")
    @PostMapping("/{id}/copy")
    public Result<Long> copyOrder(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        Long newOrderId = orderService.copyOrder(id);
        return Result.success(newOrderId);
    }

    @Operation(summary = "生成订单号")
    @GetMapping("/generate-no")
    public Result<String> generateOrderNo() {
        String orderNo = orderService.generateOrderNo();
        return Result.success(orderNo);
    }

    @Operation(summary = "导出订单(Excel)")
    @GetMapping("/export")
    public void exportOrders(
            @Parameter(description = "查询条件") OrderQuery query,
            HttpServletResponse response) throws IOException {
        List<OrderVO> orders = orderService.exportOrders(query);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("订单列表_" + System.currentTimeMillis(), "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        // 使用EasyExcel导出
        EasyExcel.write(response.getOutputStream(), OrderVO.class)
                .sheet("订单列表")
                .doWrite(orders);
    }

    @Operation(summary = "获取即将交货的订单")
    @GetMapping("/upcoming-delivery")
    public Result<List<OrderVO>> getUpcomingDelivery(
            @Parameter(description = "天数") @RequestParam(required = false, defaultValue = "7") Integer days) {
        List<OrderVO> orders = orderService.getUpcomingDeliveryOrders(days);
        return Result.success(orders);
    }

    @Operation(summary = "获取生产中的订单")
    @GetMapping("/producing")
    public Result<List<OrderVO>> getProducingOrders() {
        List<OrderVO> orders = orderService.getProducingOrders();
        return Result.success(orders);
    }
}
