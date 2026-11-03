package com.bytter.cloud.gateway;

import com.bytter.cloud.gateway.config.DevLbConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;


@SpringBootApplication()
@EnableDubbo
@EnableDiscoveryClient
@LoadBalancerClients(defaultConfiguration = {DevLbConfig.class})
public class BytterGatewayApplication {
	
    public static void main(String[] args) {
        SpringApplication.run(BytterGatewayApplication.class, args);
    }

}
