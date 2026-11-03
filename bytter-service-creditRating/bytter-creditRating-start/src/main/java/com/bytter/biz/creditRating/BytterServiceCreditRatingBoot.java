package com.bytter.biz.creditRating;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
/**
 * Spring Boot Starter
 *
 * @author Frank Zhang
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Configuration
@ComponentScan(basePackages= {"com.bytter"})
@EnableDiscoveryClient
@EnableDubbo
public class BytterServiceCreditRatingBoot {
    public static void main(String[] args) {
        SpringApplication.run(BytterServiceCreditRatingBoot.class, args);
    }
}
