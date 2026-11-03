package com.bytter.biz.budget;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
/**
 * Spring Boot Starter
 *
 * @author Frank Zhang
 */
@SpringBootApplication(scanBasePackages = { "com.bytter" })
@EnableDubbo(scanBasePackages = "com.bytter.biz.budget.service.impl")
@EnableDiscoveryClient
public class BytterPlanManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BytterPlanManagerApplication.class, args);
        /*ApplicationContext context = SpringApplication.run(BytterPlanManagerApplication.class, args);
        for(String s : context.getBeanDefinitionNames()) {
            System.out.println("===================="+s);
        }*/
    }
}
