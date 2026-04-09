package com.crm.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 产品管理模块启动类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@SpringBootApplication
@MapperScan("com.crm.product.mapper")
public class ModuleProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleProductApplication.class, args);
        System.out.println("========================================");
        System.out.println("   产品管理模块启动成功!");
        System.out.println("========================================");
    }
}
