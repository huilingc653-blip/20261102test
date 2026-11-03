package com.bytter.biz.finacial.api.config;
import com.bytter.biz.finacial.api.service.IFcAccbookDtlService;
import com.bytter.biz.finacial.api.service.IFcAccbookExtService;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.finacial.api.service.IFcBusinessRuleDefineService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.ReferenceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * 暴露的接口配置为springBean 其他服务使用autowried注入 vmTag需要
 */
@Configuration
public class FinacialReferenceConfig {
    @DubboReference
    @Bean
    public ReferenceBean<IFcAccbookExtService> fcAccbookExtServiceI() {
        return new ReferenceBean<>();
    }
    @DubboReference
    @Bean
    public ReferenceBean<IFcAccbookService> fcAccbookServiceI() {
        return new ReferenceBean<>();
    }
    @DubboReference
    @Bean
    public ReferenceBean<IFcAccbookDtlService> fcAccbookDtlServiceI() {
        return new ReferenceBean<>();
    }
    @DubboReference
    @Bean
    public ReferenceBean<IFcBusinessRuleDefineService> fcBusinessRuleDefineServiceI() {
        return new ReferenceBean<>();
    }
}
