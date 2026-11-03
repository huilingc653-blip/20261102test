package com.bytter.system;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Spring Boot Starter
 * @author Frank Zhang
 */
@SpringBootApplication(scanBasePackages = {"com.bytter","com.qcloud"})
@EntityScan(basePackages = {"com.bytter.basic.entity", "com.bytter.system.entity", "com.bytter.biz.pool.entity", "com.bytter.subject.entity", "com.bytter.system.inneraccount.entity","com.bytter.pricing.entity"})
@EnableDiscoveryClient
@EnableDubbo
public class BytterSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BytterSystemApplication.class, args);
    }
}
