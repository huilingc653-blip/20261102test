package com.bytter.workflow.config;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@MapperScan(basePackages = "com.bytter.workflow.domain.mapper")
@Configuration(proxyBeanMethods = false)
public class FlowMapperScannerConfig {
}
