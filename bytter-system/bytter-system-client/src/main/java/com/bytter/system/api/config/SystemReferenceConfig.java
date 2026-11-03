package com.bytter.system.api.config;


import com.bytter.core.tenant.TenantProvider;
import com.bytter.system.api.*;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.ReferenceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 暴露的接口配置为springBean 其他服务使用autowried注入 vmTag需要
 */
@Configuration
public class SystemReferenceConfig {
    @DubboReference
    @Bean
    public ReferenceBean<Ii18nService> i18nServiceI() {
        return new ReferenceBean<>();
    }

    @DubboReference
    @Bean
    public ReferenceBean<IMenuService> menuServiceI() {
        return new ReferenceBean<>();
    }

    @DubboReference
    @Bean
    public ReferenceBean<IFcAccService> fcAccServiceI() {
        return new ReferenceBean<>();
    }

    @DubboReference
    @Bean
    public ReferenceBean<IConfigService> configServiceI() {
        return new ReferenceBean<>();
    }

    @DubboReference
    @Bean
    public ReferenceBean<IDictService> dictServiceI() {
        return new ReferenceBean<>();
    }

    @DubboReference
    @Bean
    public ReferenceBean<IBtCurrencyService> btCurrencyServiceI() {
        return new ReferenceBean<>();
    }

    /*@DubboReference
    @Bean
    @Qualifier("multiLangServiceImpl")
    public ReferenceBean<IMultiLangService> multiLangServiceI() {
        return new ReferenceBean<>();
    }*/

    @DubboReference
    @Bean
    public ReferenceBean<IMultiLangService> multiLangServiceI() {
        ReferenceBean<IMultiLangService> referenceBean = new ReferenceBean<>();
        ReferenceConfig referenceConfig = new ReferenceConfig();
        referenceConfig.setVersion("1.0.0");
        referenceConfig.setGroup("multiLangServiceI");
        referenceConfig.setInterface("com.bytter.system.api.IMultiLangService");
        referenceConfig.setInterface(IMultiLangService.class);
        referenceBean.setKeyAndReferenceConfig("multiLangServiceI", referenceConfig);
        return referenceBean;
    }

    @DubboReference
    @Bean
    public ReferenceBean<IFcExcrateAdjustService> fcExcrateAdjustServiceI() {
        return new ReferenceBean<>();
    }

    @DubboReference
    @Bean
    public ReferenceBean<IUserService> userServiceI() {
        return new ReferenceBean<>();
    }


    @DubboReference
    @Bean
    public ReferenceBean<ICorpService> corpServiceImpl() {
        return new ReferenceBean<>();
    }

    @DubboReference
    @Bean
    public ReferenceBean<IBtBankAccService> btBankAccServiceImpl() {
        return new ReferenceBean<>();
    }

    @DubboReference
    @Bean
    public ReferenceBean<IInnerAccountService> innerAccountServiceI() {
        return new ReferenceBean<>();
    }

    @DubboReference
    @Bean
    public ReferenceBean<ICommonUtilService> commonUtilServiceI() {
        return new ReferenceBean<>();
    }

    @DubboReference
    @Bean
    public ReferenceBean<TenantProvider> tenantProviderI() {
        return new ReferenceBean<>();
    }

}
