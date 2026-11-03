package com.bytter.core.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * mybatis-plus 配置各种插件。
 *
 * @author zhaohc
 * @date 2023/3/9 15:36
 */
@Configuration
@Slf4j
public class MybatisPlusConfig {

    @Value("${jdbc.dbType}")
    private String dbType;

    /**
     * 分页插件 3.4.X
     */
    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        paginationInterceptor.setMaxLimit(-1L);
        paginationInterceptor.setDbType(DbType.getDbType(dbType));
        return paginationInterceptor;
    }
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        log.info("mybatisPlusInterceptor 初始化 mybatis-plus 拦截器>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        // 注入数据权限拦截器
        // mybatisPlusInterceptor.addInnerInterceptor(new DataPermissionInterceptor(new DataPermissionHandlerImpl()));
        // 添加分页拦截器
        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor());
        // 添加乐观锁@Version
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    /**
     * oracle 空值插入兼容
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "jdbc",name = "dbType",havingValue = "oracle")
    public ConfigurationCustomizer configurationCustomizer() {
        return new BytterCustomizer();
    }

    static class BytterCustomizer implements ConfigurationCustomizer {
        @Override
        public void customize(MybatisConfiguration configuration) {
            configuration.setJdbcTypeForNull(JdbcType.NULL);
        }
    }
}
