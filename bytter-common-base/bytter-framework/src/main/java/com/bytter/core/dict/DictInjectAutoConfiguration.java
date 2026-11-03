package com.bytter.core.dict;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ConditionalOnClass({Controller.class, RestController.class})
public class DictInjectAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DictInjectAspect dictInjectAspect() {
        return new DictInjectAspect();
    }
}