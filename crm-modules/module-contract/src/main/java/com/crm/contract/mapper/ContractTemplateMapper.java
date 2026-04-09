package com.crm.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.contract.domain.dto.ContractTemplateQuery;
import com.crm.contract.domain.entity.CrmContractTemplate;
import com.crm.contract.domain.vo.ContractTemplateVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 合同模板Mapper接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Mapper
public interface ContractTemplateMapper extends BaseMapper<CrmContractTemplate> {

    /**
     * 分页查询模板
     *
     * @param page 分页对象
     * @param query 查询条件
     * @return 模板列表
     */
    Page<ContractTemplateVO> selectTemplatePage(Page<ContractTemplateVO> page, @Param("query") ContractTemplateQuery query);

    /**
     * 根据模板编码查询
     *
     * @param templateCode 模板编码
     * @return 模板信息
     */
    CrmContractTemplate selectByTemplateCode(@Param("templateCode") String templateCode);

    /**
     * 查询指定类型的默认模板
     *
     * @param contractType 合同类型
     * @param language 语言
     * @return 模板信息
     */
    CrmContractTemplate selectDefaultTemplate(@Param("contractType") String contractType, 
                                               @Param("language") String language);

    /**
     * 查询模板列表
     *
     * @param query 查询条件
     * @return 模板列表
     */
    List<ContractTemplateVO> selectTemplateList(@Param("query") ContractTemplateQuery query);
}
