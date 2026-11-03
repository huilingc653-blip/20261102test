package com.bytter.biz;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Spring Boot Admin启动类
 *
 * @author wade
 */
@SpringBootApplication(scanBasePackages = "com.bytter",exclude = {DataSourceAutoConfiguration.class})
@EntityScan( basePackages = {"com.bytter.biz.*.entity"} )
//配置扫描mapper接口的地址，不用在接口类添加@Mapper
@MapperScan(basePackages = {"com.bytter.biz.*.*.dao"})
@Configuration
@ComponentScan(basePackages = { "com.bytter" })
@EnableDiscoveryClient
@EnableDubbo
public class BytterServicePaymentBoot {
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(BytterServicePaymentBoot.class, args);
		Environment environment = context.getEnvironment();
		/*String property = environment.getProperty("spring");
		System.out.println("############################: "+property);*/
		/*for(String s : context.getBeanDefinitionNames()) {
			System.out.println("===================="+s);
		}*/

	}
}
