package com.crm.contract;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 合同管理模块启动类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@SpringBootApplication
@MapperScan("com.crm.contract.mapper")
public class ModuleContractApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleContractApplication.class, args);
        System.out.println("========================================");
        System.out.println("   合同管理模块启动成功!");
        System.out.println("========================================");
    }
}
