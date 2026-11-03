package com.bytter.accountReceivable;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/**
 * 票据服务 Spring Boot Starter。
 *
 * @author zhaohc
 */

@SpringBootApplication(scanBasePackages = {"com.bytter.accountReceivable","com.bytter.core","com.bytter.workflow"})
@EntityScan( basePackages = {"com.bytter.accountReceivable.*.entity"} )
//配置扫描mapper接口的地址，不用在接口类添加@Mapper
@MapperScan(basePackages = {"com.bytter.accountReceivable.*.dao"})
@EnableDiscoveryClient
@EnableDubbo
public class BytterAccountReceivableStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(BytterAccountReceivableStartApplication.class, args);
    }
}
