package com.bytter.biz.finacial;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

import static java.lang.System.out;

/**
 * Spring Boot Admin启动类
 *
 * @author wade
 */
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Configuration
@MapperScan(basePackages = {"com.bytter.biz.finacial.*.*.dao","com.bytter.tally.dao"})
@ComponentScan(basePackages = {"com.bytter"})
@EntityScan(basePackages = {"com.bytter"})
@EnableDiscoveryClient
@EnableDubbo
public class BytterFinacialApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(BytterFinacialApplication.class, args);
		ConfigurableEnvironment environment = applicationContext.getEnvironment();
		String url = environment.getProperty("spring.datasource.url");
		String username = environment.getProperty("spring.datasource.username");
		String password = environment.getProperty("spring.datasource.password");
		log.info("\n----------------------------------------------------\n\t"+
				"spring.datasource.url:"+ url + "\n\t" +
				"spring.datasource.username:"+ username + "\n\t" +
				"spring.datasource.password:"+ password + "\n\t");

	}
}
