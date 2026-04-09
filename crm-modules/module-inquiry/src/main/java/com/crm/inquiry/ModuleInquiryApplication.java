package com.crm.inquiry;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 询盘管理模块启动类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@SpringBootApplication
@MapperScan("com.crm.inquiry.mapper")
public class ModuleInquiryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleInquiryApplication.class, args);
        System.out.println("========================================");
        System.out.println("   询盘管理模块启动成功!");
        System.out.println("========================================");
    }
}
