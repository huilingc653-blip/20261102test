package com.bytter.credential;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/**
 * 国际结算 服务 Spring Boot Starter。
 *
 * @author zhaohc
 */
@SpringBootApplication(scanBasePackages = {"com.bytter"})
@EntityScan( basePackages = {"com.bytter.credential.domain.*.entity"} )
@EnableDiscoveryClient
@EnableDubbo
public class BytterCredentialStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(BytterCredentialStartApplication.class, args);
    }
}
