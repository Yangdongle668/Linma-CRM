package com.crm.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.common.core.exception.BusinessException;
import com.crm.product.domain.dto.ProductCreateDTO;
import com.crm.product.domain.dto.ProductQuery;
import com.crm.product.domain.dto.ProductUpdateDTO;
import com.crm.product.domain.entity.CrmProduct;
import com.crm.product.domain.vo.ProductPriceVO;
import com.crm.product.domain.vo.ProductVO;
import com.crm.product.mapper.ProductMapper;
import com.crm.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 产品Service实现类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, CrmProduct> implements ProductService {

    private final ProductMapper productMapper;

    // 汇率配置(实际项目中应从数据库或API获取)
    private static final BigDecimal USD_TO_CNY_RATE = new BigDecimal("7.2000");
    private static final BigDecimal EUR_TO_CNY_RATE = new BigDecimal("7.8000");
    private static final BigDecimal GBP_TO_CNY_RATE = new BigDecimal("9.0000");
    private static final BigDecimal JPY_TO_CNY_RATE = new BigDecimal("0.0480");

    @Override
    public IPage<ProductVO> getProductPage(Page<ProductVO> page, ProductQuery query) {
        return productMapper.selectProductPage(page, query);
    }

    @Override
    public ProductVO getProductById(Long id) {
        CrmProduct product = this.getById(id);
        if (product == null) {
            throw new BusinessException("产品不存在");
        }
        
        ProductVO vo = BeanUtil.copyProperties(product, ProductVO.class);
        
        // 设置分类名称(实际项目中应关联查询)
        // vo.setCategoryNameCn(categoryService.getCategoryName(product.getCategoryId()));
        
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createProduct(ProductCreateDTO dto) {
        // 检查SKU唯一性
        if (StrUtil.isNotBlank(dto.getSku())) {
            if (!checkSkuUnique(dto.getSku(), null)) {
                throw new BusinessException("SKU编码已存在");
            }
        }

        CrmProduct product = BeanUtil.copyProperties(dto, CrmProduct.class);
        
        // 生成产品编号
        String productNo = generateProductNo();
        product.setProductNo(productNo);

        this.save(product);
        log.info("创建产品成功, ID: {}, 产品编号: {}", product.getId(), productNo);
        return product.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProduct(ProductUpdateDTO dto) {
        CrmProduct existProduct = this.getById(dto.getId());
        if (existProduct == null) {
            throw new BusinessException("产品不存在");
        }

        // 检查SKU唯一性
        if (StrUtil.isNotBlank(dto.getSku())) {
            if (!checkSkuUnique(dto.getSku(), dto.getId())) {
                throw new BusinessException("SKU编码已存在");
            }
        }

        CrmProduct product = BeanUtil.copyProperties(dto, CrmProduct.class);
        this.updateById(product);
        log.info("更新产品成功, ID: {}", product.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProducts(List<Long> ids) {
        this.removeByIds(ids);
        log.info("删除产品成功, IDs: {}", ids);
    }

    @Override
    public String generateProductNo() {
        String dateStr = DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
        String prefix = "PRD" + dateStr;
        
        // 查询当天最大序号
        LambdaQueryWrapper<CrmProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.likeRight(CrmProduct::getProductNo, prefix)
               .orderByDesc(CrmProduct::getProductNo)
               .last("LIMIT 1");
        
        CrmProduct lastProduct = this.getOne(wrapper);
        
        int sequence = 1;
        if (lastProduct != null && StrUtil.isNotBlank(lastProduct.getProductNo())) {
            String lastNo = lastProduct.getProductNo();
            String lastSeq = lastNo.substring(prefix.length());
            sequence = Integer.parseInt(lastSeq) + 1;
        }
        
        return prefix + String.format("%03d", sequence);
    }

    @Override
    public ProductPriceVO getProductPrice(Long productId) {
        CrmProduct product = this.getById(productId);
        if (product == null) {
            throw new BusinessException("产品不存在");
        }

        ProductPriceVO priceVO = new ProductPriceVO();
        BeanUtil.copyProperties(product, priceVO);
        
        // 计算多币种价格
        if (product.getFobPrice() != null) {
            // FOB价格为USD基准
            priceVO.setEurPrice(convertCurrency(product.getFobPrice(), "USD", "EUR"));
            priceVO.setGbpPrice(convertCurrency(product.getFobPrice(), "USD", "GBP"));
            priceVO.setJpyPrice(convertCurrency(product.getFobPrice(), "USD", "JPY"));
        }
        
        // 计算利润率
        if (product.getCostPrice() != null && product.getFactoryPrice() != null 
                && product.getCostPrice().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal profit = product.getFactoryPrice().subtract(product.getCostPrice());
            BigDecimal margin = profit.divide(product.getCostPrice(), 4, RoundingMode.HALF_UP)
                                      .multiply(new BigDecimal("100"));
            priceVO.setProfitMargin(margin.setScale(2, RoundingMode.HALF_UP));
        }
        
        // 设置汇率
        priceVO.setUsdToCnyRate(USD_TO_CNY_RATE);
        priceVO.setEurToCnyRate(EUR_TO_CNY_RATE);
        priceVO.setGbpToCnyRate(GBP_TO_CNY_RATE);
        priceVO.setJpyToCnyRate(JPY_TO_CNY_RATE);
        
        return priceVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProductPrice(Long productId, BigDecimal costPrice, BigDecimal factoryPrice,
                                   BigDecimal fobPrice, BigDecimal cifPrice) {
        CrmProduct product = this.getById(productId);
        if (product == null) {
            throw new BusinessException("产品不存在");
        }

        product.setCostPrice(costPrice);
        product.setFactoryPrice(factoryPrice);
        product.setFobPrice(fobPrice);
        product.setCifPrice(cifPrice);
        
        this.updateById(product);
        log.info("更新产品价格成功, 产品ID: {}", productId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStock(Long productId, Integer quantity) {
        CrmProduct product = this.getById(productId);
        if (product == null) {
            throw new BusinessException("产品不存在");
        }

        int newStock = product.getStockQuantity() + quantity;
        if (newStock < 0) {
            throw new BusinessException("库存不足");
        }

        product.setStockQuantity(newStock);
        this.updateById(product);
        log.info("更新库存成功, 产品ID: {}, 变更数量: {}, 新库存: {}", productId, quantity, newStock);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importProducts(List<ProductCreateDTO> productList) {
        if (productList == null || productList.isEmpty()) {
            throw new BusinessException("导入数据不能为空");
        }

        int successCount = 0;
        int failCount = 0;
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < productList.size(); i++) {
            try {
                ProductCreateDTO dto = productList.get(i);
                createProduct(dto);
                successCount++;
            } catch (Exception e) {
                failCount++;
                errors.add(String.format("第%d行导入失败: %s", i + 1, e.getMessage()));
                log.error("导入产品失败, 行号: {}", i + 1, e);
            }
        }

        String result = String.format("导入完成! 成功: %d条, 失败: %d条", successCount, failCount);
        if (!errors.isEmpty()) {
            result += "\n错误详情:\n" + String.join("\n", errors);
        }
        
        log.info(result);
        return result;
    }

    @Override
    public List<ProductVO> exportProducts(ProductQuery query) {
        return productMapper.selectProductForExport(query);
    }

    @Override
    public boolean checkSkuUnique(String sku, Long excludeId) {
        LambdaQueryWrapper<CrmProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmProduct::getSku, sku);
        if (excludeId != null) {
            wrapper.ne(CrmProduct::getId, excludeId);
        }
        return this.count(wrapper) == 0;
    }

    @Override
    public boolean checkProductNoUnique(String productNo) {
        LambdaQueryWrapper<CrmProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmProduct::getProductNo, productNo);
        return this.count(wrapper) == 0;
    }

    /**
     * 货币转换
     *
     * @param amount 金额
     * @param fromCurrency 源币种
     * @param toCurrency 目标币种
     * @return 转换后的金额
     */
    private BigDecimal convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        // 先转换为CNY
        BigDecimal amountInCny;
        switch (fromCurrency.toUpperCase()) {
            case "USD":
                amountInCny = amount.multiply(USD_TO_CNY_RATE);
                break;
            case "EUR":
                amountInCny = amount.multiply(EUR_TO_CNY_RATE);
                break;
            case "GBP":
                amountInCny = amount.multiply(GBP_TO_CNY_RATE);
                break;
            case "JPY":
                amountInCny = amount.multiply(JPY_TO_CNY_RATE);
                break;
            case "CNY":
                amountInCny = amount;
                break;
            default:
                throw new BusinessException("不支持的币种: " + fromCurrency);
        }

        // 从CNY转换为目标币种
        BigDecimal result;
        switch (toCurrency.toUpperCase()) {
            case "USD":
                result = amountInCny.divide(USD_TO_CNY_RATE, 2, RoundingMode.HALF_UP);
                break;
            case "EUR":
                result = amountInCny.divide(EUR_TO_CNY_RATE, 2, RoundingMode.HALF_UP);
                break;
            case "GBP":
                result = amountInCny.divide(GBP_TO_CNY_RATE, 2, RoundingMode.HALF_UP);
                break;
            case "JPY":
                result = amountInCny.divide(JPY_TO_CNY_RATE, 0, RoundingMode.HALF_UP);
                break;
            case "CNY":
                result = amountInCny;
                break;
            default:
                throw new BusinessException("不支持的币种: " + toCurrency);
        }

        return result;
    }
}
