package com.crm.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 外贸CRM系统启动类
 *
 * @author Lingma AI Assistant
 * @version 1.0.0
 * @since 2026-04-09
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.crm"})
@MapperScan("com.crm.**.mapper")
@EnableAsync  // 启用异步支持(邮件发送等)
@EnableScheduling  // 启用定时任务(公海池回收、自动打标等)
public class CrmAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrmAdminApplication.class, args);
        System.out.println("""

                ╔══════════════════════════════════════════════════╗
                ║                                                  ║
                ║     外贸CRM系统启动成功!                          ║
                ║     Foreign Trade CRM System Started!            ║
                ║                                                  ║
                ║     版本: v1.0.0                                 ║
                ║     Swagger文档: http://localhost:8080/api/doc.html║
                ║                                                  ║
                ╚══════════════════════════════════════════════════╝
                """);
    }
}
