package com.crm.customer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.customer.domain.dto.ContactCreateDTO;
import com.crm.customer.domain.dto.ContactUpdateDTO;
import com.crm.customer.domain.entity.CrmContact;
import com.crm.customer.domain.vo.ContactVO;

import java.util.List;

/**
 * 联系人服务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface ContactService extends IService<CrmContact> {

    /**
     * 分页查询联系人列表
     *
     * @param customerId 客户ID（可选）
     * @param keyword 关键字搜索
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 联系人分页数据
     */
    IPage<ContactVO> pageContacts(Long customerId, String keyword, int pageNum, int pageSize);

    /**
     * 根据ID查询联系人详情
     *
     * @param id 联系人ID
     * @return 联系人详情
     */
    ContactVO getContactById(Long id);

    /**
     * 根据客户ID获取联系人列表
     *
     * @param customerId 客户ID
     * @return 联系人列表
     */
    List<ContactVO> getContactsByCustomerId(Long customerId);

    /**
     * 创建联系人
     *
     * @param dto 联系人创建信息
     * @return 是否成功
     */
    boolean createContact(ContactCreateDTO dto);

    /**
     * 更新联系人
     *
     * @param dto 联系人更新信息
     * @return 是否成功
     */
    boolean updateContact(ContactUpdateDTO dto);

    /**
     * 删除联系人
     *
     * @param ids 联系人ID列表
     * @return 是否成功
     */
    boolean deleteContacts(List<Long> ids);

    /**
     * 设置关键人
     *
     * @param contactId 联系人ID
     * @param isKeyPerson 是否关键人(0否 1是)
     * @param keyPersonType 关键人类型
     * @return 是否成功
     */
    boolean setKeyPerson(Long contactId, String isKeyPerson, String keyPersonType);
}
