package com.crm.customer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 客户管理模块启动类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@SpringBootApplication
@MapperScan("com.crm.customer.mapper")
@ComponentScan(basePackages = {"com.crm"})
public class ModuleCustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleCustomerApplication.class, args);
        System.out.println("========================================");
        System.out.println("客户管理模块启动成功！");
        System.out.println("========================================");
    }
}
