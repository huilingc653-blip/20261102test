package com.bytter.report;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/**
 * 报表服务 Spring Boot Starter。
 *
 * @author zhaohc
 */
@SpringBootApplication(scanBasePackages = {"com.bytter"})
@EntityScan( basePackages = {"com.bytter.report.domain.*.entity"} )
@MapperScan(basePackages = {"com.bytter.olap.dao"})
@EnableDiscoveryClient
@EnableDubbo
public class BytterReportStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(BytterReportStartApplication.class, args);
    }
}
