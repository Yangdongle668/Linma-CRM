package com.crm.customer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.customer.domain.dto.*;
import com.crm.customer.domain.entity.CrmCustomer;
import com.crm.customer.domain.vo.CustomerVO;

import java.util.List;

/**
 * 客户服务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface CustomerService extends IService<CrmCustomer> {

    /**
     * 分页查询客户列表(支持高级搜索)
     *
     * @param query 查询条件
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 客户分页数据
     */
    IPage<CustomerVO> pageCustomers(CustomerQuery query, int pageNum, int pageSize);

    /**
     * 根据ID查询客户详情
     *
     * @param id 客户ID
     * @return 客户详情
     */
    CustomerVO getCustomerById(Long id);

    /**
     * 创建客户
     *
     * @param dto 客户创建信息
     * @return 是否成功
     */
    boolean createCustomer(CustomerCreateDTO dto);

    /**
     * 更新客户
     *
     * @param dto 客户更新信息
     * @return 是否成功
     */
    boolean updateCustomer(CustomerUpdateDTO dto);

    /**
     * 删除客户
     *
     * @param ids 客户ID列表
     * @return 是否成功
     */
    boolean deleteCustomers(List<Long> ids);

    /**
     * 客户查重检查
     *
     * @param companyName 公司名称
     * @param email 邮箱
     * @param phone 电话
     * @param website 网站
     * @return 重复的客户列表
     */
    List<CustomerVO> checkDuplicate(String companyName, String email, String phone, String website);

    /**
     * 查询公海池客户列表
     *
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 公海池客户分页数据
     */
    IPage<CustomerVO> getHighSeaCustomers(int pageNum, int pageSize);

    /**
     * 从公海池领取客户
     *
     * @param dto 领取信息
     * @return 是否成功
     */
    boolean claimFromHighSea(HighSeaClaimDTO dto);

    /**
     * 释放客户到公海池
     *
     * @param customerIds 客户ID列表
     * @return 是否成功
     */
    boolean releaseToHighSea(List<Long> customerIds);

    /**
     * 自动回收客户到公海池(定时任务调用)
     *
     * @param days 超过多少天未跟进则回收
     * @return 回收的客户数量
     */
    int autoRecycleToHighSea(Integer days);

    /**
     * 批量导入客户(Excel)
     *
     * @param importList 导入数据列表
     * @return 导入结果(成功数、失败数、错误信息)
     */
    ImportResult importCustomers(List<CustomerImportDTO> importList);

    /**
     * 导出客户数据(Excel)
     *
     * @param query 查询条件
     * @return 客户列表
     */
    List<CustomerVO> exportCustomers(CustomerQuery query);

    /**
     * 分配客户给指定负责人
     *
     * @param dto 分配信息
     * @return 是否成功
     */
    boolean assignCustomers(CustomerAssignDTO dto);

    /**
     * 合并客户
     *
     * @param dto 合并信息
     * @return 是否成功
     */
    boolean mergeCustomers(CustomerMergeDTO dto);

    /**
     * 生成客户编号
     *
     * @return 客户编号(格式: CUS20260409001)
     */
    String generateCustomerNo();

    /**
     * 导入结果
     */
    class ImportResult {
        private int successCount;
        private int failCount;
        private List<String> errorMessages;

        public ImportResult(int successCount, int failCount, List<String> errorMessages) {
            this.successCount = successCount;
            this.failCount = failCount;
            this.errorMessages = errorMessages;
        }

        public int getSuccessCount() {
            return successCount;
        }

        public int getFailCount() {
            return failCount;
        }

        public List<String> getErrorMessages() {
            return errorMessages;
        }
    }
}
