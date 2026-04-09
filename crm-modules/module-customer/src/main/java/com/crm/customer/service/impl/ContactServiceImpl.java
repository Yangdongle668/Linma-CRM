package com.crm.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.customer.domain.dto.ContactCreateDTO;
import com.crm.customer.domain.dto.ContactUpdateDTO;
import com.crm.customer.domain.entity.CrmContact;
import com.crm.customer.domain.vo.ContactVO;
import com.crm.customer.mapper.CrmContactMapper;
import com.crm.customer.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 联系人服务实现类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Service
@RequiredArgsConstructor
public class ContactServiceImpl extends ServiceImpl<CrmContactMapper, CrmContact> implements ContactService {

    private final CrmContactMapper contactMapper;

    @Override
    public IPage<ContactVO> pageContacts(Long customerId, String keyword, int pageNum, int pageSize) {
        Page<CrmContact> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CrmContact> wrapper = new LambdaQueryWrapper<>();
        
        // 客户ID过滤
        if (customerId != null) {
            wrapper.eq(CrmContact::getCustomerId, customerId);
        }
        
        // 关键字搜索
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.and(w -> w.like(CrmContact::getFullName, keyword)
                    .or()
                    .like(CrmContact::getFirstName, keyword)
                    .or()
                    .like(CrmContact::getLastName, keyword)
                    .or()
                    .like(CrmContact::getEmail, keyword)
                    .or()
                    .like(CrmContact::getPhone, keyword));
        }
        
        wrapper.orderByDesc(CrmContact::getIsKeyPerson)
               .orderByAsc(CrmContact::getCreatedTime);
        
        IPage<CrmContact> contactPage = this.page(page, wrapper);
        return contactPage.convert(this::convertToVO);
    }

    @Override
    public ContactVO getContactById(Long id) {
        CrmContact contact = this.getById(id);
        if (contact == null) {
            return null;
        }
        return convertToVO(contact);
    }

    @Override
    public List<ContactVO> getContactsByCustomerId(Long customerId) {
        List<CrmContact> contacts = contactMapper.selectByCustomerId(customerId);
        return contacts.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createContact(ContactCreateDTO dto) {
        // 检查邮箱或电话是否重复
        if (StrUtil.isNotBlank(dto.getEmail())) {
            List<CrmContact> existContacts = contactMapper.selectByEmail(dto.getEmail());
            if (!existContacts.isEmpty()) {
                throw new RuntimeException("该邮箱已存在联系人");
            }
        }
        
        if (StrUtil.isNotBlank(dto.getPhone())) {
            List<CrmContact> existContacts = contactMapper.selectByPhone(dto.getPhone());
            if (!existContacts.isEmpty()) {
                throw new RuntimeException("该电话已存在联系人");
            }
        }

        CrmContact contact = new CrmContact();
        BeanUtil.copyProperties(dto, contact);
        
        // 解析生日
        if (StrUtil.isNotBlank(dto.getBirthday())) {
            contact.setBirthday(LocalDate.parse(dto.getBirthday()));
        }
        
        // 自动生成全名
        if (StrUtil.isBlank(contact.getFullName()) 
                && StrUtil.isNotBlank(contact.getFirstName()) 
                && StrUtil.isNotBlank(contact.getLastName())) {
            contact.setFullName(contact.getFirstName() + " " + contact.getLastName());
        }
        
        return this.save(contact);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateContact(ContactUpdateDTO dto) {
        CrmContact contact = this.getById(dto.getId());
        if (contact == null) {
            throw new RuntimeException("联系人不存在");
        }

        BeanUtil.copyProperties(dto, contact, "id", "customerId", "createdTime", "createdBy");
        
        // 解析生日
        if (StrUtil.isNotBlank(dto.getBirthday())) {
            contact.setBirthday(LocalDate.parse(dto.getBirthday()));
        }
        
        // 更新全名
        if (StrUtil.isNotBlank(contact.getFirstName()) && StrUtil.isNotBlank(contact.getLastName())) {
            contact.setFullName(contact.getFirstName() + " " + contact.getLastName());
        }
        
        return this.updateById(contact);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteContacts(List<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setKeyPerson(Long contactId, String isKeyPerson, String keyPersonType) {
        CrmContact contact = this.getById(contactId);
        if (contact == null) {
            throw new RuntimeException("联系人不存在");
        }
        
        contact.setIsKeyPerson(isKeyPerson);
        contact.setKeyPersonType(keyPersonType);
        
        return this.updateById(contact);
    }

    /**
     * 实体转VO
     */
    private ContactVO convertToVO(CrmContact contact) {
        ContactVO vo = new ContactVO();
        BeanUtil.copyProperties(contact, vo);
        
        // TODO: 补充关联数据
        // - 公司名称
        
        return vo;
    }
}
