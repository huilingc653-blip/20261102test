package com.bytter.erp.config;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.ReferenceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.bytter.erp.service.IErpPayInfoErpService;
/**
 * 暴露的接口配置为springBean 其他服务使用autowried注入 vmTag需要
 */
@Configuration
public class ErpReferenceConfig {
    @DubboReference
    @Bean
    public ReferenceBean<IErpPayInfoErpService> iErpPayInfoErpService() {
        return new ReferenceBean<>();
    }
    
}
