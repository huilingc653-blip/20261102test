package com.bytter.velocity;

import com.bytter.velocity.i18n.BytterSessionLocaleResolver;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityEngineFactory;

import javax.servlet.Servlet;
import java.util.Locale;
import java.util.Properties;

@Configuration
@ConditionalOnClass({ VelocityEngine.class, VelocityEngineFactory.class, Servlet.class })
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@Order()
public class BtVelocityConfig {

    @Value("${spring.application.dslname:${spring.application.name}}")
    private String applicationName;
    
    private static final String TOOLBOX_COM = "config/velocityToolBox.xml";

    private static final String TOOLBOX_PRE = "velocity/";

    private static final String TOOLBOX_SUF = ".xml";

    @Bean
    @ConditionalOnMissingBean(VelocityConfigurer.class)
    public VelocityConfigurer velocityConfigurer() {
        VelocityConfigurer velocityConfigurer = new VelocityConfigurer();
        Resource resource = new PathMatchingResourcePatternResolver()
                .getResource("classpath:config/velocity.properties");
        velocityConfigurer.setConfigLocation(resource);
        
        // 配置宏定义
        Properties velocityProperties = new Properties();
        velocityProperties.setProperty("velocimacro.library", "/views/flow/flow.vm");
        velocityProperties.setProperty("velocimacro.library.autoreload", "true");
        velocityProperties.setProperty("velocimacro.permissions.allow.inline", "true");
        velocityProperties.setProperty("velocimacro.permissions.allow.inline.to.global", "true");
        velocityConfigurer.setVelocityProperties(velocityProperties);
        
        return velocityConfigurer;
    }

    @Bean
    @ConditionalOnMissingBean(name = "velocityViewResolver")
    public BytterVelocityViewResolver velocityViewResolver() {
    	System.out.println("spring.application.dslname======="+applicationName);
        BytterVelocityViewResolver bytterVelocityViewResolver = new BytterVelocityViewResolver();
        String toolLocation = String.format("%s,%s%s%s", TOOLBOX_COM, TOOLBOX_PRE, applicationName, TOOLBOX_SUF);
        bytterVelocityViewResolver.setToolboxConfigLocation(toolLocation);
        bytterVelocityViewResolver.setViewClass(VelocityToolbox2View.class);
        bytterVelocityViewResolver.setCache(false);
        bytterVelocityViewResolver.setPrefix("views/");
        bytterVelocityViewResolver.setSuffix(".vm");
        bytterVelocityViewResolver.setDateToolAttribute("date");
        bytterVelocityViewResolver.setExposeSpringMacroHelpers(true);
        bytterVelocityViewResolver.setRequestContextAttribute("rc");
        bytterVelocityViewResolver.setContentType("text/html;charset=UTF-8");
        return bytterVelocityViewResolver;
    }

    @Bean
    @ConditionalOnMissingBean
    public LocaleContextResolver localeResolver() {
        BytterSessionLocaleResolver localeResolver = new BytterSessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return localeResolver;
    }

    @Bean
    @ConditionalOnMissingBean
    public VelocityUtils velocityUtils(BytterVelocityViewResolver bytterVelocityViewResolver,
            VelocityConfigurer configurer) {
        return new VelocityUtils(bytterVelocityViewResolver, configurer);
    }
}
