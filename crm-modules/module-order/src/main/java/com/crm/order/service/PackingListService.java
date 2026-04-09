package com.crm.order.service;

import com.alibaba.excel.EasyExcel;
import com.crm.order.domain.entity.CrmOrder;
import com.crm.order.domain.entity.CrmOrderItem;
import com.crm.order.mapper.CrmOrderItemMapper;
import com.crm.order.mapper.CrmOrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 装箱单服务
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PackingListService {

    private final CrmOrderMapper orderMapper;
    private final CrmOrderItemMapper orderItemMapper;

    /**
     * 生成装箱单Excel
     *
     * @param orderId 订单ID
     * @return Excel字节数组
     */
    public byte[] generatePackingListExcel(Long orderId) {
        CrmOrder order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        List<CrmOrderItem> items = orderItemMapper.selectByOrderId(orderId);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // 准备装箱单数据
            List<PackingListRow> rows = buildPackingListRows(order, items);

            // 使用EasyExcel生成Excel
            EasyExcel.write(baos, PackingListRow.class)
                    .sheet("Packing List")
                    .doWrite(rows);

            log.info("生成装箱单成功: {}", order.getOrderNo());
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("生成装箱单失败", e);
            throw new RuntimeException("生成装箱单失败", e);
        }
    }

    /**
     * 计算总体积和总重量
     *
     * @param orderId 订单ID
     * @return 计算结果[总体积, 总毛重, 总净重, 总箱数]
     */
    public BigDecimal[] calculateTotals(Long orderId) {
        List<CrmOrderItem> items = orderItemMapper.selectByOrderId(orderId);

        BigDecimal totalVolume = BigDecimal.ZERO;
        BigDecimal totalGrossWeight = BigDecimal.ZERO;
        BigDecimal totalNetWeight = BigDecimal.ZERO;
        int totalCartonCount = 0;

        for (CrmOrderItem item : items) {
            if (item.getTotalVolume() != null) {
                totalVolume = totalVolume.add(item.getTotalVolume());
            }
            if (item.getTotalGrossWeight() != null) {
                totalGrossWeight = totalGrossWeight.add(item.getTotalGrossWeight());
            }
            if (item.getTotalNetWeight() != null) {
                totalNetWeight = totalNetWeight.add(item.getTotalNetWeight());
            }
            if (item.getCartonCount() != null) {
                totalCartonCount += item.getCartonCount();
            }
        }

        return new BigDecimal[]{totalVolume, totalGrossWeight, totalNetWeight, new BigDecimal(totalCartonCount)};
    }

    /**
     * 构建装箱单行数据
     */
    private List<PackingListRow> buildPackingListRows(CrmOrder order, List<CrmOrderItem> items) {
        List<PackingListRow> rows = new ArrayList<>();

        int lineNo = 1;
        for (CrmOrderItem item : items) {
            PackingListRow row = new PackingListRow();
            row.setLineNo(lineNo++);
            row.setProductNo(item.getProductNo());
            row.setProductName(item.getProductName());
            row.setSpecification(item.getSpecification());
            row.setQuantity(item.getQuantity());
            row.setUnit(item.getUnit());
            row.setQuantityPerCarton(item.getQuantityPerCarton());
            row.setCartonCount(item.getCartonCount());
            row.setGrossWeight(item.getTotalGrossWeight());
            row.setNetWeight(item.getTotalNetWeight());
            row.setVolume(item.getTotalVolume());
            row.setRemark(item.getRemark());
            rows.add(row);
        }

        // 添加合计行
        BigDecimal[] totals = calculateTotals(order.getId());
        PackingListRow totalRow = new PackingListRow();
        totalRow.setLineNo(0);
        totalRow.setProductName("TOTAL");
        totalRow.setCartonCount(totals[3].intValue());
        totalRow.setGrossWeight(totals[1]);
        totalRow.setNetWeight(totals[2]);
        totalRow.setVolume(totals[0]);
        rows.add(totalRow);

        return rows;
    }

    /**
     * 装箱单行数据
     */
    @lombok.Data
    public static class PackingListRow {
        @com.alibaba.excel.annotation.ExcelProperty(value = "No.", index = 0)
        private Integer lineNo;

        @com.alibaba.excel.annotation.ExcelProperty(value = "Product No.", index = 1)
        private String productNo;

        @com.alibaba.excel.annotation.ExcelProperty(value = "Description", index = 2)
        private String productName;

        @com.alibaba.excel.annotation.ExcelProperty(value = "Specification", index = 3)
        private String specification;

        @com.alibaba.excel.annotation.ExcelProperty(value = "Quantity", index = 4)
        private BigDecimal quantity;

        @com.alibaba.excel.annotation.ExcelProperty(value = "Unit", index = 5)
        private String unit;

        @com.alibaba.excel.annotation.ExcelProperty(value = "Qty/Ctn", index = 6)
        private Integer quantityPerCarton;

        @com.alibaba.excel.annotation.ExcelProperty(value = "Cartons", index = 7)
        private Integer cartonCount;

        @com.alibaba.excel.annotation.ExcelProperty(value = "G.W.(KG)", index = 8)
        private BigDecimal grossWeight;

        @com.alibaba.excel.annotation.ExcelProperty(value = "N.W.(KG)", index = 9)
        private BigDecimal netWeight;

        @com.alibaba.excel.annotation.ExcelProperty(value = "Volume(CBM)", index = 10)
        private BigDecimal volume;

        @com.alibaba.excel.annotation.ExcelProperty(value = "Remark", index = 11)
        private String remark;
    }
}
