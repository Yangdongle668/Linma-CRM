package com.crm.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.finance.domain.dto.PaymentRecordDTO;
import com.crm.finance.domain.dto.ReceivableQuery;
import com.crm.finance.domain.entity.CrmReceivable;
import com.crm.finance.domain.vo.ReceivableVO;

import java.util.List;
import java.util.Map;

/**
 * 应收账款业务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface ReceivableService extends IService<CrmReceivable> {

    /**
     * 分页查询应收账款列表
     *
     * @param query 查询条件
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 应收账款分页数据
     */
    IPage<ReceivableVO> pageReceivables(ReceivableQuery query, int pageNum, int pageSize);

    /**
     * 根据ID查询应收账款详情
     *
     * @param id 应收ID
     * @return 应收账款详情
     */
    ReceivableVO getReceivableById(Long id);

    /**
     * 记录收款
     *
     * @param dto 收款记录信息
     * @return 是否成功
     */
    boolean recordPayment(PaymentRecordDTO dto);

    /**
     * 获取逾期应收列表
     *
     * @return 逾期应收列表
     */
    List<ReceivableVO> getOverdueReceivables();

    /**
     * 获取账龄分析数据
     *
     * @return 账龄分析Map,key为账龄区间,value为金额
     */
    Map<String, Object> getAgingAnalysis();

    /**
     * 生成应收单号
     *
     * @return 应收单号
     */
    String generateReceivableNo();
}
