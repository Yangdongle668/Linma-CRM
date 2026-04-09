package com.crm.shipping.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.shipping.domain.dto.ShippingCreateDTO;
import com.crm.shipping.domain.dto.ShippingQuery;
import com.crm.shipping.domain.dto.ShippingUpdateDTO;
import com.crm.shipping.domain.entity.CrmShipping;
import com.crm.shipping.domain.vo.ShippingVO;
import com.crm.shipping.mapper.CrmShippingMapper;
import com.crm.shipping.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 发货单业务实现类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Service
@RequiredArgsConstructor
public class ShippingServiceImpl extends ServiceImpl<CrmShippingMapper, CrmShipping> implements ShippingService {

    private final CrmShippingMapper shippingMapper;

    @Override
    public IPage<ShippingVO> pageShippings(ShippingQuery query, int pageNum, int pageSize) {
        Page<ShippingVO> page = new Page<>(pageNum, pageSize);
        return shippingMapper.selectShippingPage(page, query);
    }

    @Override
    public ShippingVO getShippingById(Long id) {
        return null; // TODO: 实现详情查询
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createShipping(ShippingCreateDTO dto) {
        CrmShipping shipping = new CrmShipping();
        // TODO: 使用BeanUtils或MapStruct进行属性拷贝
        shipping.setShippingNo(generateShippingNo());
        shipping.setOrderId(dto.getOrderId());
        shipping.setCarrier(dto.getCarrier());
        shipping.setTrackingNo(dto.getTrackingNo());
        shipping.setShippingDate(dto.getShippingDate() != null ? dto.getShippingDate() : LocalDate.now());
        shipping.setEstimatedArrivalDate(dto.getEstimatedArrivalDate());
        shipping.setCartonCount(dto.getCartonCount());
        shipping.setTotalVolume(dto.getTotalVolume());
        shipping.setTotalGrossWeight(dto.getTotalGrossWeight());
        shipping.setTotalNetWeight(dto.getTotalNetWeight());
        shipping.setCustomsStatus(dto.getCustomsStatus() != null ? dto.getCustomsStatus() : "pending");
        shipping.setPortOfLoading(dto.getPortOfLoading());
        shipping.setPortOfDestination(dto.getPortOfDestination());
        shipping.setTradeTerm(dto.getTradeTerm());
        shipping.setRemark(dto.getRemark());
        shipping.setStatus("pending");

        save(shipping);
        return shipping.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateShipping(ShippingUpdateDTO dto) {
        CrmShipping shipping = getById(dto.getId());
        if (shipping == null) {
            return false;
        }

        // TODO: 更新字段
        shipping.setTrackingNo(dto.getTrackingNo());
        shipping.setShippingDate(dto.getShippingDate());
        shipping.setEstimatedArrivalDate(dto.getEstimatedArrivalDate());
        shipping.setActualArrivalDate(dto.getActualArrivalDate());
        shipping.setCartonCount(dto.getCartonCount());
        shipping.setTotalVolume(dto.getTotalVolume());
        shipping.setTotalGrossWeight(dto.getTotalGrossWeight());
        shipping.setTotalNetWeight(dto.getTotalNetWeight());
        shipping.setCustomsStatus(dto.getCustomsStatus());
        shipping.setCustomsDeclarationNo(dto.getCustomsDeclarationNo());
        shipping.setStatus(dto.getStatus());
        shipping.setRemark(dto.getRemark());

        return updateById(shipping);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteShippings(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    public String generateShippingNo() {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "SHP" + dateStr;

        // 查询当天最大序号
        LambdaQueryWrapper<CrmShipping> wrapper = new LambdaQueryWrapper<>();
        wrapper.likeRight(CrmShipping::getShippingNo, prefix)
                .orderByDesc(CrmShipping::getShippingNo)
                .last("LIMIT 1");

        CrmShipping lastShipping = getOne(wrapper);

        int sequence = 1;
        if (lastShipping != null && StrUtil.isNotBlank(lastShipping.getShippingNo())) {
            String lastSeq = lastShipping.getShippingNo().substring(prefix.length());
            sequence = Integer.parseInt(lastSeq) + 1;
        }

        return prefix + String.format("%03d", sequence);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long shippingId, String status) {
        CrmShipping shipping = getById(shippingId);
        if (shipping == null) {
            return false;
        }

        shipping.setStatus(status);
        return updateById(shipping);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTrackingNo(Long shippingId, String trackingNo) {
        CrmShipping shipping = getById(shippingId);
        if (shipping == null) {
            return false;
        }

        shipping.setTrackingNo(trackingNo);
        // 录入运单号后自动更新状态为已发货
        shipping.setStatus("shipped");
        return updateById(shipping);
    }

    @Override
    public List<ShippingVO> getPendingCustomsShippings() {
        LambdaQueryWrapper<CrmShipping> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmShipping::getCustomsStatus, "pending")
                .orderByAsc(CrmShipping::getCreatedTime);
        // TODO: 转换为VO
        return null;
    }

    @Override
    public List<ShippingVO> exportShippings(ShippingQuery query) {
        // TODO: 实现导出功能
        return null;
    }
}
