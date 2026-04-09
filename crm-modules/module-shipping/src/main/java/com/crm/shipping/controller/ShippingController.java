package com.crm.shipping.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.crm.common.core.domain.Result;
import com.crm.shipping.domain.dto.ShippingCreateDTO;
import com.crm.shipping.domain.dto.ShippingQuery;
import com.crm.shipping.domain.dto.ShippingUpdateDTO;
import com.crm.shipping.domain.vo.ShippingVO;
import com.crm.shipping.service.ShippingService;
import com.crm.shipping.service.TrackingService;
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
import java.util.Map;

/**
 * 物流管理控制器
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Tag(name = "物流管理", description = "发货单的增删改查、物流追踪、报关管理等功能")
@RestController
@RequestMapping("/shipping")
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingService shippingService;
    private final TrackingService trackingService;

    @Operation(summary = "分页查询发货单列表")
    @GetMapping("/page")
    public Result<IPage<ShippingVO>> pageShippings(
            @Parameter(description = "查询条件") ShippingQuery query,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        IPage<ShippingVO> page = shippingService.pageShippings(query, pageNum, pageSize);
        return Result.success(page);
    }

    @Operation(summary = "根据ID查询发货单详情")
    @GetMapping("/{id}")
    public Result<ShippingVO> getShippingById(
            @Parameter(description = "发货单ID") @PathVariable Long id) {
        ShippingVO shipping = shippingService.getShippingById(id);
        return Result.success(shipping);
    }

    @Operation(summary = "创建发货单")
    @PostMapping
    public Result<Long> createShipping(
            @Parameter(description = "发货单信息") @Valid @RequestBody ShippingCreateDTO dto) {
        Long shippingId = shippingService.createShipping(dto);
        return Result.success(shippingId);
    }

    @Operation(summary = "更新发货单")
    @PutMapping
    public Result<Void> updateShipping(
            @Parameter(description = "发货单信息") @Valid @RequestBody ShippingUpdateDTO dto) {
        boolean success = shippingService.updateShipping(dto);
        return success ? Result.success() : Result.error("更新失败");
    }

    @Operation(summary = "删除发货单")
    @DeleteMapping("/{ids}")
    public Result<Void> deleteShippings(
            @Parameter(description = "发货单ID列表") @PathVariable List<Long> ids) {
        boolean success = shippingService.deleteShippings(ids);
        return success ? Result.success() : Result.error("删除失败");
    }

    @Operation(summary = "生成发货单号")
    @GetMapping("/generate-no")
    public Result<String> generateShippingNo() {
        String shippingNo = shippingService.generateShippingNo();
        return Result.success(shippingNo);
    }

    @Operation(summary = "更新发货状态")
    @PostMapping("/{id}/update-status")
    public Result<Void> updateStatus(
            @Parameter(description = "发货单ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam String status) {
        boolean success = shippingService.updateStatus(id, status);
        return success ? Result.success() : Result.error("状态更新失败");
    }

    @Operation(summary = "录入运单号")
    @PostMapping("/{id}/update-tracking")
    public Result<Void> updateTrackingNo(
            @Parameter(description = "发货单ID") @PathVariable Long id,
            @Parameter(description = "运单号") @RequestParam String trackingNo) {
        boolean success = shippingService.updateTrackingNo(id, trackingNo);
        return success ? Result.success() : Result.error("运单号录入失败");
    }

    @Operation(summary = "查询物流轨迹")
    @GetMapping("/{id}/tracking")
    public Result<Map<String, Object>> trackShipment(
            @Parameter(description = "发货单ID") @PathVariable Long id) {
        ShippingVO shipping = shippingService.getShippingById(id);
        if (shipping == null || shipping.getTrackingNo() == null) {
            return Result.error("未找到运单号");
        }

        Map<String, Object> trackingInfo = trackingService.trackShipment(
                shipping.getTrackingNo(), shipping.getCarrier());
        return Result.success(trackingInfo);
    }

    @Operation(summary = "查询待报关的发货单")
    @GetMapping("/pending-customs")
    public Result<List<ShippingVO>> getPendingCustomsShippings() {
        List<ShippingVO> shippings = shippingService.getPendingCustomsShippings();
        return Result.success(shippings);
    }

    @Operation(summary = "导出发货单(Excel)")
    @GetMapping("/export")
    public void exportShippings(
            @Parameter(description = "查询条件") ShippingQuery query,
            HttpServletResponse response) throws IOException {
        List<ShippingVO> shippings = shippingService.exportShippings(query);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("发货单列表_" + System.currentTimeMillis(), "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), ShippingVO.class)
                .sheet("发货单列表")
                .doWrite(shippings);
    }
}
