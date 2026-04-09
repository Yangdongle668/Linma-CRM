package com.crm.customer.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.crm.common.core.domain.Result;
import com.crm.customer.domain.dto.*;
import com.crm.customer.domain.vo.CustomerVO;
import com.crm.customer.service.CustomerService;
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
 * 客户管理控制器
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Tag(name = "客户管理", description = "客户的增删改查、公海池管理、导入导出等功能")
@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "分页查询客户列表")
    @GetMapping("/page")
    public Result<IPage<CustomerVO>> pageCustomers(
            @Parameter(description = "查询条件") CustomerQuery query,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        IPage<CustomerVO> page = customerService.pageCustomers(query, pageNum, pageSize);
        return Result.success(page);
    }

    @Operation(summary = "根据ID查询客户详情")
    @GetMapping("/{id}")
    public Result<CustomerVO> getCustomerById(
            @Parameter(description = "客户ID") @PathVariable Long id) {
        CustomerVO customer = customerService.getCustomerById(id);
        return Result.success(customer);
    }

    @Operation(summary = "创建客户")
    @PostMapping
    public Result<Void> createCustomer(
            @Parameter(description = "客户信息") @Valid @RequestBody CustomerCreateDTO dto) {
        boolean success = customerService.createCustomer(dto);
        return success ? Result.success() : Result.error("创建失败");
    }

    @Operation(summary = "更新客户")
    @PutMapping
    public Result<Void> updateCustomer(
            @Parameter(description = "客户信息") @Valid @RequestBody CustomerUpdateDTO dto) {
        boolean success = customerService.updateCustomer(dto);
        return success ? Result.success() : Result.error("更新失败");
    }

    @Operation(summary = "删除客户")
    @DeleteMapping("/{ids}")
    public Result<Void> deleteCustomers(
            @Parameter(description = "客户ID列表") @PathVariable List<Long> ids) {
        boolean success = customerService.deleteCustomers(ids);
        return success ? Result.success() : Result.error("删除失败");
    }

    @Operation(summary = "客户查重检查")
    @GetMapping("/check-duplicate")
    public Result<List<CustomerVO>> checkDuplicate(
            @Parameter(description = "公司名称") @RequestParam(required = false) String companyName,
            @Parameter(description = "邮箱") @RequestParam(required = false) String email,
            @Parameter(description = "电话") @RequestParam(required = false) String phone,
            @Parameter(description = "网站") @RequestParam(required = false) String website) {
        List<CustomerVO> duplicates = customerService.checkDuplicate(companyName, email, phone, website);
        return Result.success(duplicates);
    }

    @Operation(summary = "获取公海池客户列表")
    @GetMapping("/highsea/page")
    public Result<IPage<CustomerVO>> getHighSeaCustomers(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        IPage<CustomerVO> page = customerService.getHighSeaCustomers(pageNum, pageSize);
        return Result.success(page);
    }

    @Operation(summary = "从公海池领取客户")
    @PostMapping("/highsea/claim")
    public Result<Void> claimFromHighSea(
            @Parameter(description = "领取信息") @Valid @RequestBody HighSeaClaimDTO dto) {
        boolean success = customerService.claimFromHighSea(dto);
        return success ? Result.success() : Result.error("领取失败");
    }

    @Operation(summary = "释放客户到公海池")
    @PostMapping("/highsea/release")
    public Result<Void> releaseToHighSea(
            @Parameter(description = "客户ID列表") @RequestBody List<Long> customerIds) {
        boolean success = customerService.releaseToHighSea(customerIds);
        return success ? Result.success() : Result.error("释放失败");
    }

    @Operation(summary = "自动回收客户到公海池")
    @PostMapping("/highsea/auto-recycle")
    public Result<Integer> autoRecycleToHighSea(
            @Parameter(description = "超过多少天未跟进则回收") @RequestParam(required = false) Integer days) {
        int count = customerService.autoRecycleToHighSea(days);
        return Result.success(count);
    }

    @Operation(summary = "导入客户(Excel)")
    @PostMapping("/import")
    public Result<CustomerService.ImportResult> importCustomers(
            @Parameter(description = "导入数据列表") @RequestBody List<CustomerImportDTO> importList) {
        CustomerService.ImportResult result = customerService.importCustomers(importList);
        return Result.success(result);
    }

    @Operation(summary = "导出客户(Excel)")
    @GetMapping("/export")
    public void exportCustomers(
            @Parameter(description = "查询条件") CustomerQuery query,
            HttpServletResponse response) throws IOException {
        List<CustomerVO> customers = customerService.exportCustomers(query);
        
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("客户列表_" + System.currentTimeMillis(), "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        
        // 使用EasyExcel导出
        EasyExcel.write(response.getOutputStream(), CustomerVO.class)
                .sheet("客户列表")
                .doWrite(customers);
    }

    @Operation(summary = "分配客户")
    @PostMapping("/assign")
    public Result<Void> assignCustomers(
            @Parameter(description = "分配信息") @Valid @RequestBody CustomerAssignDTO dto) {
        boolean success = customerService.assignCustomers(dto);
        return success ? Result.success() : Result.error("分配失败");
    }

    @Operation(summary = "合并客户")
    @PostMapping("/merge")
    public Result<Void> mergeCustomers(
            @Parameter(description = "合并信息") @Valid @RequestBody CustomerMergeDTO dto) {
        boolean success = customerService.mergeCustomers(dto);
        return success ? Result.success() : Result.error("合并失败");
    }

    @Operation(summary = "生成客户编号")
    @GetMapping("/generate-no")
    public Result<String> generateCustomerNo() {
        String customerNo = customerService.generateCustomerNo();
        return Result.success(customerNo);
    }
}
