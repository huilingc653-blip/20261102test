package com.bytter.bill.api.bill.config;
import com.bytter.bill.api.bill.service.IAccebillService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.ReferenceBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 暴露的接口配置为springBean 其他服务使用autowried注入 vmTag需要
 */
@Configuration
public class BillReferenceConfig {
    @DubboReference
    @Bean
    public ReferenceBean<IAccebillService> accebillServiceI() {
        return new ReferenceBean<>();
    }
}
