package com.crm.order.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.common.core.exception.BusinessException;
import com.crm.order.domain.dto.*;
import com.crm.order.domain.entity.CrmOrder;
import com.crm.order.domain.entity.CrmOrderItem;
import com.crm.order.domain.vo.OrderItemVO;
import com.crm.order.domain.vo.OrderVO;
import com.crm.order.mapper.CrmOrderItemMapper;
import com.crm.order.mapper.CrmOrderMapper;
import com.crm.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单业务实现类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<CrmOrderMapper, CrmOrder> implements OrderService {

    private final CrmOrderMapper orderMapper;
    private final CrmOrderItemMapper orderItemMapper;

    @Override
    public IPage<OrderVO> pageOrders(OrderQuery query, int pageNum, int pageSize) {
        Page<CrmOrder> page = new Page<>(pageNum, pageSize);
        IPage<CrmOrder> orderPage = orderMapper.selectOrderPage(
                page,
                query.getCustomerId(),
                query.getStatus(),
                query.getOrderNo(),
                query.getStartDate(),
                query.getEndDate(),
                query.getOwnerId()
        );

        // 转换为VO
        Page<OrderVO> voPage = new Page<>(pageNum, pageSize, orderPage.getTotal());
        List<OrderVO> voList = orderPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public OrderVO getOrderById(Long id) {
        CrmOrder order = getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        OrderVO vo = convertToVO(order);

        // 查询明细
        List<CrmOrderItem> items = orderItemMapper.selectByOrderId(id);
        List<OrderItemVO> itemVOs = items.stream()
                .map(this::convertItemToVO)
                .collect(Collectors.toList());
        vo.setItems(itemVOs);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOrder(OrderCreateDTO dto) {
        CrmOrder order = new CrmOrder();
        BeanUtils.copyProperties(dto, order);

        // 生成订单号
        order.setOrderNo(generateOrderNo());

        // 设置订单日期
        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDate.now());
        }

        // 设置初始状态
        order.setStatus("pending_confirm");

        // 初始化收款进度
        order.setReceivedAmount(BigDecimal.ZERO);
        order.setPaymentProgress(BigDecimal.ZERO);

        // 初始化生产进度
        order.setProductionProgress(BigDecimal.ZERO);

        // 计算总金额和利润率
        calculateTotals(order, dto.getItems());

        // 保存主表
        save(order);

        // 保存明细
        saveItems(order.getId(), dto.getItems());

        log.info("创建订单成功: {}", order.getOrderNo());
        return order.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrder(OrderUpdateDTO dto) {
        CrmOrder order = getById(dto.getId());
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 只有待确认状态可以修改
        if (!"pending_confirm".equals(order.getStatus())) {
            throw new BusinessException("只有待确认状态的订单可以修改");
        }

        CrmOrder updateOrder = new CrmOrder();
        BeanUtils.copyProperties(dto, updateOrder);
        updateOrder.setId(dto.getId());

        // 重新计算总金额和利润率
        calculateTotals(updateOrder, dto.getItems());

        // 更新主表
        updateById(updateOrder);

        // 删除旧明细，保存新明细
        orderItemMapper.deleteByOrderId(dto.getId());
        saveItems(dto.getId(), dto.getItems());

        log.info("更新订单成功: {}", order.getOrderNo());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteOrders(List<Long> ids) {
        for (Long id : ids) {
            CrmOrder order = getById(id);
            if (order != null) {
                // 只有待确认或已取消状态可以删除
                if (!"pending_confirm".equals(order.getStatus()) && !"cancelled".equals(order.getStatus())) {
                    throw new BusinessException("订单 " + order.getOrderNo() + " 不是待确认或已取消状态，不能删除");
                }
                // 删除明细
                orderItemMapper.deleteByOrderId(id);
            }
        }
        // 逻辑删除主表
        removeByIds(ids);
        log.info("删除订单成功: {}", ids);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changeStatus(OrderStatusChangeDTO dto) {
        CrmOrder order = getById(dto.getOrderId());
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 验证状态转换是否合法
        validateStatusTransition(order.getStatus(), dto.getTargetStatus());

        String oldStatus = order.getStatus();
        order.setStatus(dto.getTargetStatus());

        // 如果转为已完成，记录实际交货日期
        if ("completed".equals(dto.getTargetStatus())) {
            order.setActualDeliveryDate(LocalDate.now());
        }

        updateById(order);

        log.info("订单状态变更成功: {} -> {}, 原因: {}",
                order.getOrderNo(), oldStatus + " -> " + dto.getTargetStatus(), dto.getReason());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProductionProgress(ProductionProgressDTO dto) {
        CrmOrder order = getById(dto.getOrderId());
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        if (!"producing".equals(order.getStatus())) {
            throw new BusinessException("只有生产中的订单可以更新生产进度");
        }

        order.setProductionProgress(dto.getProgress());
        if (dto.getEstimatedCompletionDate() != null) {
            order.setEstimatedCompletionDate(dto.getEstimatedCompletionDate());
        }

        // 如果生产进度达到100%，自动转为已完工
        if (dto.getProgress().compareTo(new BigDecimal("100")) >= 0) {
            order.setStatus("completed");
            log.info("订单生产完成，自动转为已完工状态: {}", order.getOrderNo());
        }

        updateById(order);

        // TODO: 保存照片URL到订单照片表

        log.info("更新生产进度成功: {}, 进度: {}%", order.getOrderNo(), dto.getProgress());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createFromQuotation(Long quotationId) {
        // TODO: 从报价单信息创建订单
        log.info("从报价单创建订单: {}", quotationId);
        return null;
    }

    @Override
    public String generatePiInvoice(Long orderId) {
        CrmOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 生成PI发票号: PI + 订单号
        String piNo = "PI" + order.getOrderNo().substring(3);
        order.setPiInvoiceNo(piNo);
        updateById(order);

        log.info("生成PI形式发票: {}", piNo);
        return piNo;
    }

    @Override
    public String generateCiInvoice(Long orderId) {
        CrmOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 生成CI发票号: CI + 订单号
        String ciNo = "CI" + order.getOrderNo().substring(3);
        order.setCiInvoiceNo(ciNo);
        updateById(order);

        log.info("生成CI商业发票: {}", ciNo);
        return ciNo;
    }

    @Override
    public String generatePackingList(Long orderId) {
        CrmOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 生成装箱单号: PL + 订单号
        String plNo = "PL" + order.getOrderNo().substring(3);
        order.setPackingListNo(plNo);
        updateById(order);

        log.info("生成装箱单: {}", plNo);
        return plNo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmPayment(Long orderId, BigDecimal amount) {
        CrmOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 更新已收款金额
        BigDecimal newReceivedAmount = order.getReceivedAmount().add(amount);
        order.setReceivedAmount(newReceivedAmount);

        // 计算收款进度
        if (order.getTotalAmount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal progress = newReceivedAmount.divide(order.getTotalAmount(), 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
            order.setPaymentProgress(progress);
        }

        updateById(order);

        log.info("确认收款成功: {}, 金额: {}, 进度: {}%",
                order.getOrderNo(), amount, order.getPaymentProgress());
        return true;
    }

    @Override
    public String generateOrderNo() {
        // 格式: ORD + 年月日 + 三位序号
        String dateStr = LocalDate.now().toString().replace("-", "");
        String prefix = "ORD" + dateStr;

        // 查询当天最大序号
        LambdaQueryWrapper<CrmOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.likeRight(CrmOrder::getOrderNo, prefix)
                .orderByDesc(CrmOrder::getOrderNo)
                .last("LIMIT 1");

        CrmOrder lastOrder = getOne(wrapper);

        int sequence = 1;
        if (lastOrder != null && StrUtil.isNotBlank(lastOrder.getOrderNo())) {
            String lastNo = lastOrder.getOrderNo();
            String lastSeq = lastNo.substring(prefix.length());
            sequence = Integer.parseInt(lastSeq) + 1;
        }

        return String.format("%s%03d", prefix, sequence);
    }

    @Override
    public List<OrderVO> exportOrders(OrderQuery query) {
        LambdaQueryWrapper<CrmOrder> wrapper = buildQueryWrapper(query);
        List<CrmOrder> orders = list(wrapper);
        return orders.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long copyOrder(Long orderId) {
        CrmOrder source = getById(orderId);
        if (source == null) {
            throw new BusinessException("订单不存在");
        }

        // 复制主表
        CrmOrder newOrder = new CrmOrder();
        BeanUtils.copyProperties(source, newOrder);
        newOrder.setId(null);
        newOrder.setOrderNo(generateOrderNo());
        newOrder.setStatus("pending_confirm");
        newOrder.setReceivedAmount(BigDecimal.ZERO);
        newOrder.setPaymentProgress(BigDecimal.ZERO);
        newOrder.setProductionProgress(BigDecimal.ZERO);
        newOrder.setCreatedTime(null);
        newOrder.setUpdatedTime(null);

        save(newOrder);

        // 复制明细
        List<CrmOrderItem> sourceItems = orderItemMapper.selectByOrderId(orderId);
        List<OrderItemDTO> itemDTOs = sourceItems.stream().map(item -> {
            OrderItemDTO dto = new OrderItemDTO();
            BeanUtils.copyProperties(item, dto);
            dto.setId(null);
            return dto;
        }).collect(Collectors.toList());

        saveItems(newOrder.getId(), itemDTOs);

        log.info("复制订单成功: {} -> {}", source.getOrderNo(), newOrder.getOrderNo());
        return newOrder.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(Long orderId, String reason) {
        CrmOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        if ("completed".equals(order.getStatus()) || "shipped".equals(order.getStatus())) {
            throw new BusinessException("已完成或已发货的订单不能取消");
        }

        order.setStatus("cancelled");
        order.setRemark((order.getRemark() != null ? order.getRemark() + "\n" : "")
                + "取消原因: " + reason);
        updateById(order);

        log.info("订单取消成功: {}, 原因: {}", order.getOrderNo(), reason);
        return true;
    }

    @Override
    public List<OrderVO> getUpcomingDeliveryOrders(Integer days) {
        List<CrmOrder> orders = orderMapper.selectUpcomingDelivery(days != null ? days : 7);
        return orders.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderVO> getProducingOrders() {
        List<CrmOrder> orders = orderMapper.selectProducingOrders();
        return orders.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 验证状态转换是否合法
     */
    private void validateStatusTransition(String currentStatus, String targetStatus) {
        // 定义合法的状态转换
        List<String> allowedTransitions = List.of(
                "pending_confirm->producing",
                "pending_confirm->cancelled",
                "producing->completed",
                "completed->pending_shipment",
                "pending_shipment->shipped",
                "shipped->completed"
        );

        String transition = currentStatus + "->" + targetStatus;
        if (!allowedTransitions.contains(transition)) {
            throw new BusinessException("不允许的状态转换: " + transition);
        }
    }

    /**
     * 计算总金额和利润率
     */
    private void calculateTotals(CrmOrder order, List<OrderItemDTO> items) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal totalVolume = BigDecimal.ZERO;
        BigDecimal totalGrossWeight = BigDecimal.ZERO;
        BigDecimal totalNetWeight = BigDecimal.ZERO;
        int totalCartonCount = 0;

        for (OrderItemDTO item : items) {
            if (item.getQuantity() != null && item.getUnitPrice() != null) {
                BigDecimal itemTotal = item.getQuantity().multiply(item.getUnitPrice());
                totalAmount = totalAmount.add(itemTotal);
            }
            if (item.getQuantity() != null && item.getCostPrice() != null) {
                BigDecimal itemCost = item.getQuantity().multiply(item.getCostPrice());
                totalCost = totalCost.add(itemCost);
            }
        }

        order.setTotalAmount(totalAmount);
        order.setTotalCost(totalCost);

        // 计算利润和利润率
        if (totalCost.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal profit = totalAmount.subtract(totalCost);
            // 如果有汇率，转换为CNY计算
            if (order.getExchangeRate() != null) {
                profit = profit.multiply(order.getExchangeRate());
            }
            order.setProfitAmount(profit);
            order.setProfitMargin(profit.divide(totalCost, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100")));
        }

        order.setTotalVolume(totalVolume);
        order.setTotalGrossWeight(totalGrossWeight);
        order.setTotalNetWeight(totalNetWeight);
        order.setCartonCount(totalCartonCount);
    }

    /**
     * 保存明细
     */
    private void saveItems(Long orderId, List<OrderItemDTO> itemDTOs) {
        List<CrmOrderItem> items = new ArrayList<>();
        int lineNo = 1;

        for (OrderItemDTO dto : itemDTOs) {
            CrmOrderItem item = new CrmOrderItem();
            BeanUtils.copyProperties(dto, item);
            item.setId(null);
            item.setOrderId(orderId);
            item.setLineNo(lineNo++);

            // 计算行总金额
            if (dto.getQuantity() != null && dto.getUnitPrice() != null) {
                item.setTotalAmount(dto.getQuantity().multiply(dto.getUnitPrice()));
            }

            // 计算行总成本
            if (dto.getQuantity() != null && dto.getCostPrice() != null) {
                item.setTotalCost(dto.getQuantity().multiply(dto.getCostPrice()));
            }

            // 计算箱数
            if (dto.getQuantity() != null && dto.getQuantityPerCarton() != null
                    && dto.getQuantityPerCarton() > 0) {
                int cartonCount = dto.getQuantity().intValue() / dto.getQuantityPerCarton();
                if (dto.getQuantity().intValue() % dto.getQuantityPerCarton() > 0) {
                    cartonCount++;
                }
                item.setCartonCount(cartonCount);
            }

            // 计算总重量和体积
            if (item.getCartonCount() != null) {
                if (dto.getGrossWeightPerCarton() != null) {
                    item.setTotalGrossWeight(dto.getGrossWeightPerCarton()
                            .multiply(new BigDecimal(item.getCartonCount())));
                }
                if (dto.getNetWeightPerCarton() != null) {
                    item.setTotalNetWeight(dto.getNetWeightPerCarton()
                            .multiply(new BigDecimal(item.getCartonCount())));
                }
                if (dto.getVolumePerCarton() != null) {
                    item.setTotalVolume(dto.getVolumePerCarton()
                            .multiply(new BigDecimal(item.getCartonCount())));
                }
            }

            // 计算行利润率
            if (item.getTotalCost() != null && item.getTotalCost().compareTo(BigDecimal.ZERO) > 0
                    && item.getTotalAmount() != null) {
                BigDecimal profit = item.getTotalAmount().subtract(item.getTotalCost());
                item.setProfitMargin(profit.divide(item.getTotalCost(), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100")));
            }

            // 初始化生产和发货数量
            item.setProducedQuantity(BigDecimal.ZERO);
            item.setShippedQuantity(BigDecimal.ZERO);

            items.add(item);
        }

        if (!items.isEmpty()) {
            orderItemMapper.batchInsert(items);
        }
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<CrmOrder> buildQueryWrapper(OrderQuery query) {
        LambdaQueryWrapper<CrmOrder> wrapper = new LambdaQueryWrapper<>();

        if (query.getCustomerId() != null) {
            wrapper.eq(CrmOrder::getCustomerId, query.getCustomerId());
        }
        if (StrUtil.isNotBlank(query.getStatus())) {
            wrapper.eq(CrmOrder::getStatus, query.getStatus());
        }
        if (StrUtil.isNotBlank(query.getOrderNo())) {
            wrapper.like(CrmOrder::getOrderNo, query.getOrderNo());
        }
        if (query.getStartDate() != null) {
            wrapper.ge(CrmOrder::getOrderDate, query.getStartDate());
        }
        if (query.getEndDate() != null) {
            wrapper.le(CrmOrder::getOrderDate, query.getEndDate());
        }
        if (query.getOwnerId() != null) {
            wrapper.eq(CrmOrder::getOwnerId, query.getOwnerId());
        }
        if (StrUtil.isNotBlank(query.getCurrency())) {
            wrapper.eq(CrmOrder::getCurrency, query.getCurrency());
        }
        if (StrUtil.isNotBlank(query.getTradeTerm())) {
            wrapper.eq(CrmOrder::getTradeTerm, query.getTradeTerm());
        }
        if (query.getQuotationId() != null) {
            wrapper.eq(CrmOrder::getQuotationId, query.getQuotationId());
        }
        if (query.getMinAmount() != null) {
            wrapper.ge(CrmOrder::getTotalAmount, query.getMinAmount());
        }
        if (query.getMaxAmount() != null) {
            wrapper.le(CrmOrder::getTotalAmount, query.getMaxAmount());
        }
        if (StrUtil.isNotBlank(query.getKeyword())) {
            wrapper.and(w -> w.like(CrmOrder::getCustomerName, query.getKeyword())
                    .or()
                    .like(CrmOrder::getOrderNo, query.getKeyword()));
        }

        wrapper.orderByDesc(CrmOrder::getCreatedTime);
        return wrapper;
    }

    /**
     * 转换为主表VO
     */
    private OrderVO convertToVO(CrmOrder order) {
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order, vo);
        vo.setStatusDesc(vo.getStatusDesc()); // 触发getter计算状态描述
        return vo;
    }

    /**
     * 转换为明细VO
     */
    private OrderItemVO convertItemToVO(CrmOrderItem item) {
        OrderItemVO vo = new OrderItemVO();
        BeanUtils.copyProperties(item, vo);
        return vo;
    }
}
