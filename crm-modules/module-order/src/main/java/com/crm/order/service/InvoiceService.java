package com.crm.order.service;

import com.crm.order.domain.entity.CrmOrder;
import com.crm.order.domain.entity.CrmOrderItem;
import com.crm.order.mapper.CrmOrderItemMapper;
import com.crm.order.mapper.CrmOrderMapper;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * 发票服务(PI形式发票、CI商业发票)
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final CrmOrderMapper orderMapper;
    private final CrmOrderItemMapper orderItemMapper;

    /**
     * 生成PI形式发票PDF
     *
     * @param orderId 订单ID
     * @return PDF字节数组
     */
    public byte[] generatePiInvoice(Long orderId) {
        CrmOrder order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        List<CrmOrderItem> items = orderItemMapper.selectByOrderId(orderId);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);

            // 标题
            Paragraph title = new Paragraph("PROFORMA INVOICE")
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // 基本信息
            Table infoTable = createInvoiceInfoTable(order, true);
            document.add(infoTable);

            // 产品明细
            Table itemTable = createInvoiceItemTable(items, order.getCurrency());
            document.add(itemTable);

            // 总金额
            addTotalAmount(document, order, true);

            // 银行信息
            addBankInfo(document);

            // 条款
            addInvoiceTerms(document, true);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("生成PI发票失败", e);
            throw new RuntimeException("生成PI发票失败", e);
        }
    }

    /**
     * 生成CI商业发票PDF
     *
     * @param orderId 订单ID
     * @return PDF字节数组
     */
    public byte[] generateCiInvoice(Long orderId) {
        CrmOrder order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        List<CrmOrderItem> items = orderItemMapper.selectByOrderId(orderId);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);

            // 标题
            Paragraph title = new Paragraph("COMMERCIAL INVOICE")
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // 基本信息
            Table infoTable = createInvoiceInfoTable(order, false);
            document.add(infoTable);

            // 产品明细
            Table itemTable = createInvoiceItemTable(items, order.getCurrency());
            document.add(itemTable);

            // 总金额
            addTotalAmount(document, order, false);

            // 装箱信息
            addPackingInfo(document, order);

            // 条款
            addInvoiceTerms(document, false);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("生成CI发票失败", e);
            throw new RuntimeException("生成CI发票失败", e);
        }
    }

    /**
     * 创建发票基本信息表格
     */
    private Table createInvoiceInfoTable(CrmOrder order, boolean isPi) {
        float[] columnWidths = {150, 300};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        String invoiceNo = isPi ? order.getPiInvoiceNo() : order.getCiInvoiceNo();
        addTableRow(table, "Invoice No:", invoiceNo != null ? invoiceNo : "");

        String dateStr = order.getOrderDate() != null
                ? order.getOrderDate().format(DATE_FORMATTER) : "";
        addTableRow(table, "Date:", dateStr);

        addTableRow(table, "Customer:", order.getCustomerName());
        addTableRow(table, "Contact:", order.getContactName());
        addTableRow(table, "Trade Term:", order.getTradeTerm());

        if (order.getPortOfLoading() != null) {
            addTableRow(table, "Port of Loading:", order.getPortOfLoading());
        }
        if (order.getPortOfDestination() != null) {
            addTableRow(table, "Port of Destination:", order.getPortOfDestination());
        }
        if (order.getPaymentTerms() != null) {
            addTableRow(table, "Payment Terms:", order.getPaymentTerms());
        }

        return table;
    }

    /**
     * 创建发票产品明细表格
     */
    private Table createInvoiceItemTable(List<CrmOrderItem> items, String currency) {
        float[] columnWidths = {40, 180, 80, 80, 100, 100};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        // 表头
        String[] headers = {"No.", "Description", "Quantity", "Unit", "Unit Price", "Amount"};
        for (String header : headers) {
            Cell headerCell = new Cell().add(new Paragraph(header))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setBold();
            table.addHeaderCell(headerCell);
        }

        // 数据行
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);

        int lineNo = 1;
        for (CrmOrderItem item : items) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(lineNo++))));
            table.addCell(new Cell().add(new Paragraph(
                    item.getProductName() + "\n" +
                            (item.getSpecification() != null ? item.getSpecification() : "")
            )));
            table.addCell(new Cell().add(new Paragraph(item.getQuantity() != null
                    ? numberFormat.format(item.getQuantity()) : "0")));
            table.addCell(new Cell().add(new Paragraph(item.getUnit() != null
                    ? item.getUnit() : "")));
            table.addCell(new Cell().add(new Paragraph(item.getUnitPrice() != null
                    ? formatCurrency(item.getUnitPrice(), currency) : "0.00")));
            table.addCell(new Cell().add(new Paragraph(item.getTotalAmount() != null
                    ? formatCurrency(item.getTotalAmount(), currency) : "0.00")));
        }

        return table;
    }

    /**
     * 添加总金额
     */
    private void addTotalAmount(Document document, CrmOrder order, boolean isPi) {
        document.add(new Paragraph("\n"));

        String totalLabel = isPi ? "Total Amount:" : "Total Value:";
        Paragraph totalPara = new Paragraph()
                .add(totalLabel + " ")
                .add(formatCurrency(order.getTotalAmount(), order.getCurrency()))
                .setBold()
                .setTextAlignment(TextAlignment.RIGHT);
        document.add(totalPara);

        if (!isPi && order.getTotalVolume() != null) {
            Paragraph packingPara = new Paragraph()
                    .add("Total Volume: " + order.getTotalVolume() + " CBM\n")
                    .add("Total G.W.: " + order.getTotalGrossWeight() + " KG\n")
                    .add("Total N.W.: " + order.getTotalNetWeight() + " KG\n")
                    .add("Total Cartons: " + order.getCartonCount())
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(packingPara);
        }
    }

    /**
     * 添加银行信息
     */
    private void addBankInfo(Document document) {
        document.add(new Paragraph("\n"));
        Paragraph bankTitle = new Paragraph("BANK INFORMATION:")
                .setBold();
        document.add(bankTitle);

        // TODO: 从配置中读取银行信息
        document.add(new Paragraph("Bank Name: [Your Bank Name]"));
        document.add(new Paragraph("Account No: [Your Account Number]"));
        document.add(new Paragraph("SWIFT Code: [Your SWIFT Code]"));
        document.add(new Paragraph("Bank Address: [Your Bank Address]"));
    }

    /**
     * 添加装箱信息
     */
    private void addPackingInfo(Document document, CrmOrder order) {
        document.add(new Paragraph("\n"));
        Paragraph packingTitle = new Paragraph("PACKING DETAILS:")
                .setBold();
        document.add(packingTitle);

        if (order.getTotalVolume() != null) {
            document.add(new Paragraph("Total Volume: " + order.getTotalVolume() + " CBM"));
        }
        if (order.getTotalGrossWeight() != null) {
            document.add(new Paragraph("Total G.W.: " + order.getTotalGrossWeight() + " KG"));
        }
        if (order.getTotalNetWeight() != null) {
            document.add(new Paragraph("Total N.W.: " + order.getTotalNetWeight() + " KG"));
        }
        if (order.getCartonCount() != null) {
            document.add(new Paragraph("Total Cartons: " + order.getCartonCount()));
        }
    }

    /**
     * 添加发票条款
     */
    private void addInvoiceTerms(Document document, boolean isPi) {
        document.add(new Paragraph("\n"));

        Paragraph termsTitle = new Paragraph(isPi ? "TERMS AND CONDITIONS:" : "REMARKS:")
                .setBold();
        document.add(termsTitle);

        if (isPi) {
            document.add(new Paragraph("1. This Proforma Invoice is valid for 30 days"));
            document.add(new Paragraph("2. Payment should be made as per agreed terms"));
            document.add(new Paragraph("3. Delivery time is subject to production schedule"));
            document.add(new Paragraph("4. Prices are based on current exchange rates"));
        } else {
            document.add(new Paragraph("1. We declare that this invoice is true and correct"));
            document.add(new Paragraph("2. The goods are of Chinese origin"));
            document.add(new Paragraph("3. All information provided is accurate"));
        }
    }

    /**
     * 添加表格行
     */
    private void addTableRow(Table table, String label, String value) {
        table.addCell(new Cell().add(new Paragraph(label)).setBold());
        table.addCell(new Cell().add(new Paragraph(value != null ? value : "")));
    }

    /**
     * 格式化货币
     */
    private String formatCurrency(BigDecimal amount, String currency) {
        if (amount == null) {
            return "0.00";
        }
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        String formatted = formatter.format(amount);
        return currency != null ? currency + " " + formatted : formatted;
    }
}
