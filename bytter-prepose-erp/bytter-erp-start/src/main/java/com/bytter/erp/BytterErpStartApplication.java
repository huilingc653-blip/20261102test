package com.bytter.erp;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EntityScan( basePackages = {"com.bytter.biz.*.entity","com.bytter.biz.*.*.entity","com.bytter.biz.erp.*.view"} )
//配置扫描mapper接口的地址，不用在接口类添加@Mapper
@MapperScan(basePackages = {"com.bytter.biz.*.dao"})
@Configuration
@ComponentScan(basePackages = { "com.bytter"})
@EnableDiscoveryClient
@EnableDubbo
public class BytterErpStartApplication {

    public static void main(String[] args) {
        SpringApplication.run(BytterErpStartApplication.class, args);
    }

}
