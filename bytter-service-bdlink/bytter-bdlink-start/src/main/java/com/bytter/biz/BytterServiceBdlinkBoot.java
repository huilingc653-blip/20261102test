package com.bytter.biz;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
/**
 * Spring Boot Admin启动类
 *
 * @author wade
 */
@SpringBootApplication(scanBasePackages = "com.bytter",exclude = {DataSourceAutoConfiguration.class})
@Configuration
@MapperScan(basePackages = {"com.bytter.biz.*.*.dao"})
@ComponentScan(basePackages = { "com.bytter" })
@EntityScan( basePackages = {"com.bytter"} )
@EnableDubbo
public class BytterServiceBdlinkBoot {
	public static void main(String[] args) {
		SpringApplication.run(BytterServiceBdlinkBoot.class, args);
	}
}
