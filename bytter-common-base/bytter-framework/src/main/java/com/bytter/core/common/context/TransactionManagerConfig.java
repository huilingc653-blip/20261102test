package com.bytter.core.common.context;
import com.alibaba.druid.pool.DruidDataSource;
import com.bytter.core.jdbc.TenantDataSource;
import com.bytter.core.jdbc.TenantProperties;
//import io.seata.rm.datasource.DataSourceProxy;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.*;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 全局事务配置
 */
@Slf4j
@Aspect
@Configuration
@EnableConfigurationProperties(JpaProperties.class)
@EnableTransactionManagement
public class TransactionManagerConfig {

    private final JpaProperties jpaProperties;

    public TransactionManagerConfig(JpaProperties jpaProperties) {
        this.jpaProperties = jpaProperties;
    }

    @Resource
    private TenantProperties tenantProperties;

    private static final int AOP_TIME_OUT = 50000;
    /**
     * Aop 切面切入点表达式
     */
    private static final String AOP_POINTCUT_EXPRESSION = "(execution (* com.bytter..service..*.*(..))) or (execution (* com.byttersoft..service..*.*(..)))";

    // ==================== 数据源配置 ====================

    /**
     * 创建Druid数据源（延迟初始化）
     */
    @Bean(initMethod = "init", destroyMethod = "close")
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DruidDataSource druidDataSource() {
        log.info("创建Druid数据源...");
        return new DruidDataSource();
    }

    /**
     * 创建多租户数据源（包装Druid）
     * 正确的包装顺序：TenantDataSource -> DataSourceProxy -> DruidDataSource
     * 这样Seata可以正确识别和管理AT事务分支
     */
    @Primary
    @Bean(name = "dataSource")
    @DependsOn("druidDataSource")  // 明确依赖关系
    public DataSource dataSource() {
        log.info("创建多租户数据源...");
        return new TenantDataSource(druidDataSource(), tenantProperties);

        /*DataSourceProxy dataSourceProxy = new DataSourceProxy(druidDataSource());
        return new TenantDataSource(dataSourceProxy, tenantProperties);*/

    }

    // ==================== JPA实体管理器配置 ====================

    /**
     * 实体管理器工厂 - 明确配置以避免自动配置冲突
     */
    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        log.info("创建实体管理器工厂>>>>>>>>>>>>>>>");
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.bytter.biz.*.entity",
                "com.bytter.biz.*.*.entity",
                "com.bytter.*.entity",
                "com.bytter.*.*.entity",
                "com.bytter.*.*.*.entity",
                "com.bytter.*.*.*.*.entity",
                "com.bytter.*.*.*.*.*.entity",
                "com.byttersoft.*.*.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(false); // 根据你的 ddl-auto 配置
        em.setJpaVendorAdapter(vendorAdapter);

        // 从配置文件中获取 Hibernate 属性
        Map<String, String> properties = jpaProperties.getProperties();

        // 关键：确保 Session 上下文与 Spring 集成
        properties.put("hibernate.current_session_context_class",
                "org.springframework.orm.hibernate5.SpringSessionContext");

        // 确保连接释放模式正确
        properties.put("hibernate.connection.handling_mode", "DELAYED_ACQUISITION_AND_RELEASE_AFTER_TRANSACTION");

        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public TransactionInterceptor txAdvice(PlatformTransactionManager transactionManager) {
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        /** 查询方法， 只读事务，不做更新操作 */
        RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
        readOnlyTx.setReadOnly(true);
        readOnlyTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);
        /** 增、删、改 需要的事务 */
        RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
        /** 什么异常需要回滚 */
        requiredTx.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        /** 当前存在事务就使用当前事务，当前不存在事务就创建一个新的事务 */
        requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        requiredTx.setTimeout(AOP_TIME_OUT);
        Map<String, TransactionAttribute> methodMap = new HashMap<>();
        methodMap.put("add*", requiredTx);
        methodMap.put("save*", requiredTx);
        methodMap.put("creat*", requiredTx);
        methodMap.put("update*", requiredTx);
        methodMap.put("modify*", requiredTx);
        methodMap.put("edit*", requiredTx);
        methodMap.put("insert*", requiredTx);
        methodMap.put("delete*", requiredTx);
        methodMap.put("remove*", requiredTx);
        methodMap.put("*", readOnlyTx);
        source.setNameMap(methodMap);
        TransactionInterceptor txAdvice = new TransactionInterceptor(transactionManager, source);
        return txAdvice;
    }

    @Bean(name = "txAdviceAdvisor")
    public Advisor txAdviceAdvisor(TransactionInterceptor txAdvice) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, txAdvice);
    }
}