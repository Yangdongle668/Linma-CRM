package com.crm.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.customer.domain.dto.*;
import com.crm.customer.domain.entity.CrmContact;
import com.crm.customer.domain.entity.CrmCustomer;
import com.crm.customer.domain.vo.CustomerVO;
import com.crm.customer.mapper.CrmCustomerMapper;
import com.crm.customer.service.ContactService;
import com.crm.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 客户服务实现类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl extends ServiceImpl<CrmCustomerMapper, CrmCustomer> implements CustomerService {

    private final CrmCustomerMapper customerMapper;
    private final ContactService contactService;

    @Override
    public IPage<CustomerVO> pageCustomers(CustomerQuery query, int pageNum, int pageSize) {
        Page<CrmCustomer> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CrmCustomer> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(CrmCustomer::getCreatedTime);
        
        IPage<CrmCustomer> customerPage = this.page(page, wrapper);
        
        // 转换为VO
        return customerPage.convert(this::convertToVO);
    }

    @Override
    public CustomerVO getCustomerById(Long id) {
        CrmCustomer customer = this.getById(id);
        if (customer == null) {
            return null;
        }
        return convertToVO(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createCustomer(CustomerCreateDTO dto) {
        // 检查重复
        List<CustomerVO> duplicates = checkDuplicate(dto.getCompanyName(), null, null, dto.getWebsite());
        if (!duplicates.isEmpty()) {
            throw new RuntimeException("客户已存在，请检查公司名称或网站");
        }

        CrmCustomer customer = new CrmCustomer();
        BeanUtil.copyProperties(dto, customer);
        
        // 生成客户编号
        customer.setCustomerNo(generateCustomerNo());
        
        // 设置默认状态
        if (customer.getStatus() == null) {
            customer.setStatus(1);
        }
        
        // 设置默认等级
        if (StrUtil.isBlank(customer.getLevel())) {
            customer.setLevel("C");
        }
        
        return this.save(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCustomer(CustomerUpdateDTO dto) {
        CrmCustomer customer = this.getById(dto.getId());
        if (customer == null) {
            throw new RuntimeException("客户不存在");
        }

        // 如果修改了公司名或网站，需要重新查重
        if (StrUtil.isNotBlank(dto.getCompanyName()) && !dto.getCompanyName().equals(customer.getCompanyName())) {
            List<CustomerVO> duplicates = checkDuplicate(dto.getCompanyName(), null, null, dto.getWebsite());
            if (!duplicates.isEmpty()) {
                throw new RuntimeException("客户已存在，请检查公司名称");
            }
        }

        BeanUtil.copyProperties(dto, customer, "id", "customerNo", "createdTime", "createdBy");
        
        // 解析下次跟进时间
        if (StrUtil.isNotBlank(dto.getNextFollowTime())) {
            customer.setNextFollowTime(LocalDateTime.parse(dto.getNextFollowTime()));
        }
        
        return this.updateById(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCustomers(List<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public List<CustomerVO> checkDuplicate(String companyName, String email, String phone, String website) {
        List<CrmCustomer> duplicates = new ArrayList<>();
        
        // 根据公司名查询
        if (StrUtil.isNotBlank(companyName)) {
            duplicates.addAll(customerMapper.selectByCompanyName(companyName));
        }
        
        // 根据邮箱查询
        if (StrUtil.isNotBlank(email)) {
            duplicates.addAll(customerMapper.selectByEmail(email));
        }
        
        // 根据电话查询
        if (StrUtil.isNotBlank(phone)) {
            duplicates.addAll(customerMapper.selectByPhone(phone));
        }
        
        // 根据网站查询
        if (StrUtil.isNotBlank(website)) {
            duplicates.addAll(customerMapper.selectByWebsite(website));
        }
        
        // 去重并转换为VO
        return duplicates.stream()
                .collect(Collectors.toMap(CrmCustomer::getId, c -> c, (c1, c2) -> c1))
                .values()
                .stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public IPage<CustomerVO> getHighSeaCustomers(int pageNum, int pageSize) {
        Page<CrmCustomer> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CrmCustomer> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNull(CrmCustomer::getOwnerId)
               .eq(CrmCustomer::getStatus, 1)
               .orderByDesc(CrmCustomer::getCreatedTime);
        
        IPage<CrmCustomer> customerPage = this.page(page, wrapper);
        return customerPage.convert(this::convertToVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean claimFromHighSea(HighSeaClaimDTO dto) {
        List<CrmCustomer> customers = this.listByIds(dto.getCustomerIds());
        if (customers.isEmpty()) {
            throw new RuntimeException("客户不存在");
        }
        
        // 检查是否都是公海池客户
        for (CrmCustomer customer : customers) {
            if (customer.getOwnerId() != null) {
                throw new RuntimeException("客户[" + customer.getCompanyName() + "]已被领取");
            }
        }
        
        // TODO: 从SecurityContext获取当前用户ID
        Long currentUserId = 1L;
        
        // 批量更新负责人
        for (CrmCustomer customer : customers) {
            customer.setOwnerId(currentUserId);
            customer.setRemark(StrUtil.isNotBlank(dto.getRemark()) 
                    ? customer.getRemark() + " | " + dto.getRemark() 
                    : customer.getRemark());
        }
        
        return this.updateBatchById(customers);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean releaseToHighSea(List<Long> customerIds) {
        List<CrmCustomer> customers = this.listByIds(customerIds);
        if (customers.isEmpty()) {
            return false;
        }
        
        // 清空负责人
        for (CrmCustomer customer : customers) {
            customer.setOwnerId(null);
        }
        
        return this.updateBatchById(customers);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int autoRecycleToHighSea(Integer days) {
        if (days == null || days <= 0) {
            days = 30; // 默认30天
        }
        
        List<CrmCustomer> recycleCustomers = customerMapper.selectRecycleCustomers(days);
        if (recycleCustomers.isEmpty()) {
            return 0;
        }
        
        // 清空负责人，释放到公海池
        for (CrmCustomer customer : recycleCustomers) {
            customer.setOwnerId(null);
        }
        
        this.updateBatchById(recycleCustomers);
        log.info("自动回收{}个客户到公海池", recycleCustomers.size());
        
        return recycleCustomers.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImportResult importCustomers(List<CustomerImportDTO> importList) {
        int successCount = 0;
        int failCount = 0;
        List<String> errorMessages = new ArrayList<>();
        
        for (int i = 0; i < importList.size(); i++) {
            CustomerImportDTO importDTO = importList.get(i);
            try {
                // 验证必填字段
                if (StrUtil.isBlank(importDTO.getCompanyName())) {
                    errorMessages.add("第" + (i + 2) + "行：公司名称不能为空");
                    failCount++;
                    continue;
                }
                
                // 检查重复
                List<CustomerVO> duplicates = checkDuplicate(
                        importDTO.getCompanyName(), 
                        importDTO.getEmail(), 
                        importDTO.getPhone(), 
                        importDTO.getWebsite()
                );
                if (!duplicates.isEmpty()) {
                    errorMessages.add("第" + (i + 2) + "行：客户已存在 - " + importDTO.getCompanyName());
                    failCount++;
                    continue;
                }
                
                // 创建客户
                CustomerCreateDTO createDTO = new CustomerCreateDTO();
                BeanUtil.copyProperties(importDTO, createDTO);
                createCustomer(createDTO);
                
                // 如果有联系人信息，创建联系人
                if (StrUtil.isNotBlank(importDTO.getContactName())) {
                    CrmContact contact = new CrmContact();
                    // TODO: 获取刚创建的客户ID
                    contact.setCustomerId(1L);
                    contact.setFullName(importDTO.getContactName());
                    contact.setPosition(importDTO.getPosition());
                    contact.setPhone(importDTO.getPhone());
                    contact.setEmail(importDTO.getEmail());
                    contactService.save(contact);
                }
                
                successCount++;
            } catch (Exception e) {
                log.error("导入客户失败，第{}行", i + 2, e);
                errorMessages.add("第" + (i + 2) + "行：" + e.getMessage());
                failCount++;
            }
        }
        
        return new ImportResult(successCount, failCount, errorMessages);
    }

    @Override
    public List<CustomerVO> exportCustomers(CustomerQuery query) {
        LambdaQueryWrapper<CrmCustomer> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(CrmCustomer::getCreatedTime);
        
        List<CrmCustomer> customers = this.list(wrapper);
        return customers.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignCustomers(CustomerAssignDTO dto) {
        List<CrmCustomer> customers = this.listByIds(dto.getCustomerIds());
        if (customers.isEmpty()) {
            return false;
        }
        
        // 批量更新负责人
        for (CrmCustomer customer : customers) {
            customer.setOwnerId(dto.getOwnerId());
            if (StrUtil.isNotBlank(dto.getRemark())) {
                customer.setRemark(StrUtil.isNotBlank(customer.getRemark()) 
                        ? customer.getRemark() + " | " + dto.getRemark() 
                        : dto.getRemark());
            }
        }
        
        return this.updateBatchById(customers);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean mergeCustomers(CustomerMergeDTO dto) {
        CrmCustomer mainCustomer = this.getById(dto.getMainCustomerId());
        if (mainCustomer == null) {
            throw new RuntimeException("主客户不存在");
        }
        
        List<CrmCustomer> mergeCustomers = this.listByIds(dto.getMergeCustomerIds());
        if (mergeCustomers.isEmpty()) {
            throw new RuntimeException("被合并客户不存在");
        }
        
        // TODO: 实现客户合并逻辑
        // 1. 转移联系人
        // 2. 转移跟进记录
        // 3. 转移订单
        // 4. 删除被合并客户
        
        log.info("合并客户：主客户={}, 被合并客户={}", dto.getMainCustomerId(), dto.getMergeCustomerIds());
        
        // 这里简化处理，实际需要根据业务需求完善
        return true;
    }

    @Override
    public String generateCustomerNo() {
        // 格式：CUS + 日期(yyyyMMdd) + 序号(3位)
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "CUS" + dateStr;
        
        // 查询今天已有的最大序号
        LambdaQueryWrapper<CrmCustomer> wrapper = new LambdaQueryWrapper<>();
        wrapper.likeRight(CrmCustomer::getCustomerNo, prefix)
               .orderByDesc(CrmCustomer::getCustomerNo)
               .last("LIMIT 1");
        
        CrmCustomer lastCustomer = this.getOne(wrapper);
        
        int sequence = 1;
        if (lastCustomer != null && StrUtil.isNotBlank(lastCustomer.getCustomerNo())) {
            String lastSeq = lastCustomer.getCustomerNo().substring(prefix.length());
            sequence = Integer.parseInt(lastSeq) + 1;
        }
        
        return prefix + String.format("%03d", sequence);
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<CrmCustomer> buildQueryWrapper(CustomerQuery query) {
        LambdaQueryWrapper<CrmCustomer> wrapper = new LambdaQueryWrapper<>();
        
        if (query == null) {
            return wrapper;
        }
        
        // 客户编号
        if (StrUtil.isNotBlank(query.getCustomerNo())) {
            wrapper.eq(CrmCustomer::getCustomerNo, query.getCustomerNo());
        }
        
        // 公司名称（模糊搜索）
        if (StrUtil.isNotBlank(query.getCompanyName())) {
            wrapper.and(w -> w.like(CrmCustomer::getCompanyName, query.getCompanyName())
                    .or()
                    .like(CrmCustomer::getCompanyNameCn, query.getCompanyName()));
        }
        
        // 国家
        if (StrUtil.isNotBlank(query.getCountry())) {
            wrapper.eq(CrmCustomer::getCountry, query.getCountry());
        }
        
        // 省份
        if (StrUtil.isNotBlank(query.getProvince())) {
            wrapper.eq(CrmCustomer::getProvince, query.getProvince());
        }
        
        // 城市
        if (StrUtil.isNotBlank(query.getCity())) {
            wrapper.eq(CrmCustomer::getCity, query.getCity());
        }
        
        // 行业
        if (StrUtil.isNotBlank(query.getIndustry())) {
            wrapper.eq(CrmCustomer::getIndustry, query.getIndustry());
        }
        
        // 来源
        if (StrUtil.isNotBlank(query.getSource())) {
            wrapper.eq(CrmCustomer::getSource, query.getSource());
        }
        
        // 等级
        if (StrUtil.isNotBlank(query.getLevel())) {
            wrapper.eq(CrmCustomer::getLevel, query.getLevel());
        }
        
        // 状态
        if (query.getStatus() != null) {
            wrapper.eq(CrmCustomer::getStatus, query.getStatus());
        }
        
        // 负责人
        if (query.getOwnerId() != null) {
            wrapper.eq(CrmCustomer::getOwnerId, query.getOwnerId());
        }
        
        // 部门
        if (query.getDepartmentId() != null) {
            wrapper.eq(CrmCustomer::getDepartmentId, query.getDepartmentId());
        }
        
        // 关键字搜索（公司名、网站、备注）
        if (StrUtil.isNotBlank(query.getKeyword())) {
            wrapper.and(w -> w.like(CrmCustomer::getCompanyName, query.getKeyword())
                    .or()
                    .like(CrmCustomer::getCompanyNameCn, query.getKeyword())
                    .or()
                    .like(CrmCustomer::getWebsite, query.getKeyword())
                    .or()
                    .like(CrmCustomer::getRemark, query.getKeyword()));
        }
        
        // 创建时间范围
        if (StrUtil.isNotBlank(query.getStartTime())) {
            wrapper.ge(CrmCustomer::getCreatedTime, LocalDateTime.parse(query.getStartTime()));
        }
        if (StrUtil.isNotBlank(query.getEndTime())) {
            wrapper.le(CrmCustomer::getCreatedTime, LocalDateTime.parse(query.getEndTime()));
        }
        
        return wrapper;
    }

    /**
     * 实体转VO
     */
    private CustomerVO convertToVO(CrmCustomer customer) {
        CustomerVO vo = new CustomerVO();
        BeanUtil.copyProperties(customer, vo);
        
        // TODO: 补充关联数据
        // - 国家名称
        // - 负责人姓名
        // - 部门名称
        // - 创建者姓名
        // - 标签列表
        // - 联系人数量
        // - 最近跟进时间
        
        return vo;
    }
}
