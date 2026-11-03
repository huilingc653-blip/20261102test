package com.bytter.cashVoucher;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.bytter"})
@EntityScan( basePackages = {"com.bytter.cashVoucher.entity"} )
@EnableDiscoveryClient
@EnableDubbo
public class BytterCashVoucherStartApplication {

    public static void main(String[] args) {
        SpringApplication.run(BytterCashVoucherStartApplication.class, args);
    }

}
