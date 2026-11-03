package com.bytter.core.autoconfig;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.bytter.core.annotation.impl.MyOracleDialect;
import com.bytter.core.base.AjaxJsonResponseConverter;
import com.bytter.core.base.DateConverter;
import com.bytter.core.common.context.LocalMessageService;
import com.bytter.core.mybatis.BytterMetaObjectHandler;
import com.bytter.core.utils.ApplicationContextUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class BytterCoreAutoConfig {
    @Bean
    @ConditionalOnMissingBean
    public MetaObjectHandler metaObjectHandler() {
        return new BytterMetaObjectHandler();
    }
    @Bean
    @ConditionalOnMissingBean
    public ApplicationContextUtil applicationContextUtil() {
        return new ApplicationContextUtil();
    }
    @Bean
    @ConditionalOnMissingBean
    public DateConverter dateConverter() {
        return new DateConverter();
    }



    @Bean
    public LocalMessageService localMessageService() {
        return new LocalMessageService();
    }
    
    
    @Bean
    @ConditionalOnMissingBean
    public MyOracleDialect myOracleDialect() {
        return new MyOracleDialect();
    }
    
    
    @Bean
    @Primary
    public MappingJackson2HttpMessageConverter httpMessageConverter() {
        ObjectMapper mapper = Jackson2ObjectMapperBuilder.json()
                .timeZone("GMT+8")
                .failOnUnknownProperties(false)
                .serializerByType(Date.class,new DateSerializer(false,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")))
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                        SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .build();
        return new AjaxJsonResponseConverter(mapper);
    }
}
