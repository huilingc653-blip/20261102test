package com.bytter.check;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
/**
 * 资金核对启动类。
 *
 * @author zhaohc
 */
@SpringBootApplication(scanBasePackages = {"com.bytter"})
@EntityScan( basePackages = {"com.bytter.biz.dataexchange.basic.entity","com.bytter.check.entity","com.bytter.biz.transfer.swap.entity","com.bytter.check.reconcile.entity"} )
@EnableDiscoveryClient
//配置扫描mapper接口的地址，不用在接口类添加@Mapper
@MapperScan(basePackages = {"com.bytter.biz.*.dao"})
@Configuration
@ComponentScan(basePackages = { "com.bytter" })
@EnableDubbo
public class BytterCheckStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(BytterCheckStartApplication.class, args);
    }
}
