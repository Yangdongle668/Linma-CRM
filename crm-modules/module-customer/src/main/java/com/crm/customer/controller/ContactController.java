package com.crm.customer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.crm.common.core.domain.Result;
import com.crm.customer.domain.dto.ContactCreateDTO;
import com.crm.customer.domain.dto.ContactUpdateDTO;
import com.crm.customer.domain.vo.ContactVO;
import com.crm.customer.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 联系人管理控制器
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Tag(name = "联系人管理", description = "联系人的增删改查功能")
@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @Operation(summary = "分页查询联系人列表")
    @GetMapping("/page")
    public Result<IPage<ContactVO>> pageContacts(
            @Parameter(description = "客户ID") @RequestParam(required = false) Long customerId,
            @Parameter(description = "关键字搜索") @RequestParam(required = false) String keyword,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        IPage<ContactVO> page = contactService.pageContacts(customerId, keyword, pageNum, pageSize);
        return Result.success(page);
    }

    @Operation(summary = "根据ID查询联系人详情")
    @GetMapping("/{id}")
    public Result<ContactVO> getContactById(
            @Parameter(description = "联系人ID") @PathVariable Long id) {
        ContactVO contact = contactService.getContactById(id);
        return Result.success(contact);
    }

    @Operation(summary = "根据客户ID获取联系人列表")
    @GetMapping("/customer/{customerId}")
    public Result<List<ContactVO>> getContactsByCustomerId(
            @Parameter(description = "客户ID") @PathVariable Long customerId) {
        List<ContactVO> contacts = contactService.getContactsByCustomerId(customerId);
        return Result.success(contacts);
    }

    @Operation(summary = "创建联系人")
    @PostMapping
    public Result<Void> createContact(
            @Parameter(description = "联系人信息") @Valid @RequestBody ContactCreateDTO dto) {
        boolean success = contactService.createContact(dto);
        return success ? Result.success() : Result.error("创建失败");
    }

    @Operation(summary = "更新联系人")
    @PutMapping
    public Result<Void> updateContact(
            @Parameter(description = "联系人信息") @Valid @RequestBody ContactUpdateDTO dto) {
        boolean success = contactService.updateContact(dto);
        return success ? Result.success() : Result.error("更新失败");
    }

    @Operation(summary = "删除联系人")
    @DeleteMapping("/{ids}")
    public Result<Void> deleteContacts(
            @Parameter(description = "联系人ID列表") @PathVariable List<Long> ids) {
        boolean success = contactService.deleteContacts(ids);
        return success ? Result.success() : Result.error("删除失败");
    }

    @Operation(summary = "设置关键人")
    @PutMapping("/set-key-person")
    public Result<Void> setKeyPerson(
            @Parameter(description = "联系人ID") @RequestParam Long contactId,
            @Parameter(description = "是否关键人(0否 1是)") @RequestParam String isKeyPerson,
            @Parameter(description = "关键人类型") @RequestParam(required = false) String keyPersonType) {
        boolean success = contactService.setKeyPerson(contactId, isKeyPerson, keyPersonType);
        return success ? Result.success() : Result.error("设置失败");
    }
}
