package com.bytter.bill;
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
@SpringBootApplication(scanBasePackages = {"com.bytter.bill", "com.bytter.credit", "com.bytter.guarantee", "com.bytter.finance", "com.bytter.letter","com.bytter.core","com.bytter.supply", "com.bytter.workflow"})
@EntityScan(basePackages = {"com.bytter.*.*.*.entity", "com.bytter.*.*.entity", "com.bytter.bill.*.*.*.*.entity", "com.bytter.bill.domain.billbank.pool.entity"})
// 配置扫描mapper接口的地址，不用在接口类添加@Mapper
@MapperScan(basePackages = {"com.bytter.*.*.*.dao", "com.bytter.*.*.dao", "com.bytter.bill.*.*.*.*.dao", "com.bytter.bill.domain.billbank.pool.dao"})
@EnableDiscoveryClient
@EnableDubbo
public class BytterBillStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(BytterBillStartApplication.class, args);
    }
}
