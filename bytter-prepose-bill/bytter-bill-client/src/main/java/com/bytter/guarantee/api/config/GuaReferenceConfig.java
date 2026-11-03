package com.bytter.guarantee.api.config;

import com.bytter.guarantee.api.IFcBondAddService;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.ReferenceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * 暴露的接口配置为springBean 其他服务使用autowried注入
 */
@Configuration
public class GuaReferenceConfig {
    @DubboReference
    @Bean
    public ReferenceBean<IGuaPledgeOccupyService> guaPledgeOccupyServiceI() {
        return new ReferenceBean<>();
    }
    @DubboReference
    @Bean
    public ReferenceBean<IGuaContractOccupServie> guaContractOccupServiceI(){
        return new ReferenceBean<>();
    }
    @DubboReference
    @Bean
    public ReferenceBean<IFcBondHookService> fcBondHookServiceI(){
        return new ReferenceBean<>();
    }

    @DubboReference
    @Bean
    public ReferenceBean<IFcBondAddService> fcBondAddServiceI(){
        return new ReferenceBean<>();
    }

}
