package com.bytter.cloud.auth;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 鉴权服务启动类。
 *
 * @author zhaohc
 * @since 2025-12-16
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.bytter"})
@Slf4j
@EnableDubbo
public class BytterAuthApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(BytterAuthApplication.class, args);
        } catch (Exception ex) {
            log.error("eeee", ex);
            System.exit(0);
        }
    }
}
