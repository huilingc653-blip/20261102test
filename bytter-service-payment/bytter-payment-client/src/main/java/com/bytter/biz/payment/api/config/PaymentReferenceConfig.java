package com.bytter.biz.payment.api.config;

import com.bytter.biz.payment.api.service.IBisExcBackService;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.biz.payment.api.service.INisAllocationReqService;
import com.bytter.biz.payment.api.service.INisBillheadService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.ReferenceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 暴露的接口配置为springBean 其他服务使用autowried注入 vmTag需要
 */
@Configuration
public class PaymentReferenceConfig {

    @DubboReference
    @Bean
    public ReferenceBean<INisAllocationReqService> nisAllocationReqServiceI() {
        return new ReferenceBean<>();
    }

    @DubboReference(group = "nisAllocationReq")
    @Bean
    public ReferenceBean<IBisExcBackService> allocationBisExcBackServiceImpl(){
        return new ReferenceBean<>();
    }

    @DubboReference(group = "nisBillhead")
    @Bean
    public ReferenceBean<IBisExcBackService> billheadBisExcBackServiceImpl(){
        return new ReferenceBean<>();
    }

    @DubboReference(group = "nisWage")
    @Bean
    public ReferenceBean<IBisExcBackService> wageBisExcBackServiceImpl(){
        return new ReferenceBean<>();
    }

    @DubboReference
    @Bean
    public ReferenceBean<INisBillheadService> nisBillheadServiceI(){
        return new ReferenceBean<>();
    }
    @DubboReference
    @Bean
    public ReferenceBean<IFcVoucherTypeService> fcVoucherTypeServiceI(){
        return new ReferenceBean<>();
    }

}
