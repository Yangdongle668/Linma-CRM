package com.crm.quotation.service;

import com.crm.quotation.domain.entity.CrmQuotation;
import com.crm.quotation.domain.entity.CrmQuotationItem;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
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
 * 报价单PDF生成器
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationPdfGenerator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 生成英文报价单PDF
     *
     * @param quotation 报价单主表
     * @param items 报价单明细
     * @return PDF字节数组
     */
    public byte[] generateEnglishPdf(CrmQuotation quotation, List<CrmQuotationItem> items) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);

            // 添加标题
            Paragraph title = new Paragraph("QUOTATION")
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // 添加水印
            addWatermark(document, "QUOTATION");

            // 添加基本信息表格
            Table infoTable = createInfoTable(quotation, false);
            document.add(infoTable);

            // 添加产品明细表格
            Table itemTable = createItemTable(items, false);
            document.add(itemTable);

            // 添加总金额
            Paragraph totalPara = new Paragraph()
                    .add("Total Amount: ")
                    .add(formatCurrency(quotation.getTotalAmount(), quotation.getCurrency()))
                    .setBold()
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(totalPara);

            // 添加条款和条件
            addTermsAndConditions(document, quotation, false);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("生成英文PDF失败", e);
            throw new RuntimeException("生成PDF失败", e);
        }
    }

    /**
     * 生成中文报价单PDF
     *
     * @param quotation 报价单主表
     * @param items 报价单明细
     * @return PDF字节数组
     */
    public byte[] generateChinesePdf(CrmQuotation quotation, List<CrmQuotationItem> items) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);

            // 注意: 中文字体需要特殊处理,这里使用简化版本
            Paragraph title = new Paragraph("报 价 单")
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // 添加水印
            addWatermark(document, "报价单");

            // 添加基本信息表格
            Table infoTable = createInfoTable(quotation, true);
            document.add(infoTable);

            // 添加产品明细表格
            Table itemTable = createItemTable(items, true);
            document.add(itemTable);

            // 添加总金额
            Paragraph totalPara = new Paragraph()
                    .add("总金额: ")
                    .add(formatCurrency(quotation.getTotalAmount(), quotation.getCurrency()))
                    .setBold()
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(totalPara);

            // 添加条款和条件
            addTermsAndConditions(document, quotation, true);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("生成中文PDF失败", e);
            throw new RuntimeException("生成PDF失败", e);
        }
    }

    /**
     * 创建基本信息表格
     */
    private Table createInfoTable(CrmQuotation quotation, boolean isChinese) {
        float[] columnWidths = {150, 300};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        // 报价单号
        addTableRow(table, isChinese ? "报价单号:" : "Quotation No:", quotation.getQuotationNo());

        // 日期
        String dateStr = quotation.getQuotationDate() != null
                ? quotation.getQuotationDate().format(DATE_FORMATTER) : "";
        addTableRow(table, isChinese ? "日期:" : "Date:", dateStr);

        // 客户名称
        addTableRow(table, isChinese ? "客户:" : "Customer:", quotation.getCustomerName());

        // 联系人
        addTableRow(table, isChinese ? "联系人:" : "Contact:", quotation.getContactName());

        // 贸易术语
        addTableRow(table, isChinese ? "贸易术语:" : "Trade Term:", quotation.getTradeTerm());

        // 装运港
        if (quotation.getPortOfLoading() != null) {
            addTableRow(table, isChinese ? "装运港:" : "Port of Loading:", quotation.getPortOfLoading());
        }

        // 目的港
        if (quotation.getPortOfDestination() != null) {
            addTableRow(table, isChinese ? "目的港:" : "Port of Destination:", quotation.getPortOfDestination());
        }

        // 付款方式
        if (quotation.getPaymentTerms() != null) {
            addTableRow(table, isChinese ? "付款方式:" : "Payment Terms:", quotation.getPaymentTerms());
        }

        // 交货期
        if (quotation.getDeliveryDays() != null) {
            addTableRow(table, isChinese ? "交货期:" : "Delivery Time:",
                    quotation.getDeliveryDays() + " " + (isChinese ? "天" : "days"));
        }

        // 有效期
        String validUntil = quotation.getValidUntil() != null
                ? quotation.getValidUntil().format(DATE_FORMATTER) : "";
        addTableRow(table, isChinese ? "有效期至:" : "Valid Until:", validUntil);

        return table;
    }

    /**
     * 创建产品明细表格
     */
    private Table createItemTable(List<CrmQuotationItem> items, boolean isChinese) {
        float[] columnWidths = {40, 150, 80, 80, 80, 80};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        // 表头
        String[] headers = isChinese
                ? new String[]{"序号", "产品描述", "数量", "单位", "单价", "金额"}
                : new String[]{"No.", "Description", "Quantity", "Unit", "Unit Price", "Amount"};

        for (String header : headers) {
            Cell headerCell = new Cell().add(new Paragraph(header))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setBold();
            table.addHeaderCell(headerCell);
        }

        // 数据行
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);

        int lineNo = 1;
        for (CrmQuotationItem item : items) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(lineNo++))));
            table.addCell(new Cell().add(new Paragraph(item.getProductName() != null
                    ? item.getProductName() : "")));
            table.addCell(new Cell().add(new Paragraph(item.getQuantity() != null
                    ? numberFormat.format(item.getQuantity()) : "0")));
            table.addCell(new Cell().add(new Paragraph(item.getUnit() != null
                    ? item.getUnit() : "")));
            table.addCell(new Cell().add(new Paragraph(item.getUnitPrice() != null
                    ? formatCurrency(item.getUnitPrice(), null) : "0.00")));
            table.addCell(new Cell().add(new Paragraph(item.getTotalAmount() != null
                    ? formatCurrency(item.getTotalAmount(), null) : "0.00")));
        }

        return table;
    }

    /**
     * 添加条款和条件
     */
    private void addTermsAndConditions(Document document, CrmQuotation quotation, boolean isChinese) {
        document.add(new Paragraph("\n"));

        Paragraph termsTitle = new Paragraph(isChinese ? "条款与条件:" : "Terms and Conditions:")
                .setBold()
                .setFontSize(12);
        document.add(termsTitle);

        List<String> terms = isChinese
                ? List.of(
                "1. 本报价单有效期为" + quotation.getValidityDays() + "天",
                "2. 付款方式: " + (quotation.getPaymentTerms() != null ? quotation.getPaymentTerms() : "T/T"),
                "3. 交货期: " + (quotation.getDeliveryDays() != null ? quotation.getDeliveryDays() : "30") + "天",
                "4. 贸易术语: " + quotation.getTradeTerm(),
                "5. 以上价格以最终确认为准"
        )
                : List.of(
                "1. This quotation is valid for " + quotation.getValidityDays() + " days",
                "2. Payment Terms: " + (quotation.getPaymentTerms() != null ? quotation.getPaymentTerms() : "T/T"),
                "3. Delivery Time: " + (quotation.getDeliveryDays() != null ? quotation.getDeliveryDays() : "30") + " days",
                "4. Trade Term: " + quotation.getTradeTerm(),
                "5. Prices are subject to final confirmation"
        );

        for (String term : terms) {
            document.add(new Paragraph(term).setFontSize(10));
        }
    }

    /**
     * 添加水印
     */
    private void addWatermark(Document document, String watermarkText) {
        // iText 7的水印需要通过事件处理器实现,这里简化处理
        // 实际项目中需要使用PdfCanvas和透明度设置
        log.debug("添加水印: {}", watermarkText);
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
