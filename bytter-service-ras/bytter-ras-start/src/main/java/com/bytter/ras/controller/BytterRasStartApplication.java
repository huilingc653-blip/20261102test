package com.bytter.ras.controller;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/**
 * 增值服务 Spring Boot Starter。
 *
 * @author zhaohc
 * @date 2023/2/27 16:35
 */
@SpringBootApplication(scanBasePackages = {"com.bytter"})
@EntityScan( basePackages = {"com.bytter.increment.domain.entity"} )
@EnableDiscoveryClient
@EnableDubbo
public class BytterRasStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(BytterRasStartApplication.class, args);
    }
}
