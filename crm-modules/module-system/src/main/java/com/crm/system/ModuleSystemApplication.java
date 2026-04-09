package com.crm.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 系统管理模块启动类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@SpringBootApplication
@MapperScan("com.crm.system.mapper")
public class ModuleSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleSystemApplication.class, args);
    }
}
