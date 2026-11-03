// package com.bytter.core.autoconfig;
//
// import com.alibaba.druid.pool.DruidDataSource;
// import com.bytter.core.jdbc.TenantDataSource;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.boot.context.properties.EnableConfigurationProperties;
// import org.springframework.context.annotation.*;
// import org.springframework.orm.jpa.JpaTransactionManager;
// import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
// import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
// import org.springframework.transaction.PlatformTransactionManager;
// import org.springframework.transaction.annotation.EnableTransactionManagement;
//
// import javax.persistence.EntityManagerFactory;
// import javax.sql.DataSource;
// import java.util.Map;
//
// /**
//  * 数据源配置。
//  *
//  * @author zhaohc
//  * @since 2025/12/18 14:58
//  */
// @Slf4j
// @Configuration
// @EnableTransactionManagement
// @EnableConfigurationProperties(JpaProperties.class)
// public class DataSourceConfig {
//
//     private final JpaProperties jpaProperties;
//
//     public DataSourceConfig(JpaProperties jpaProperties) {
//         this.jpaProperties = jpaProperties;
//     }
//
//     // /**
//     //  * 创建Druid数据源
//     //  */
//     // @Bean
//     // @ConfigurationProperties(prefix = "spring.datasource.druid")
//     // public DruidDataSource druidDataSource() {
//     //     return new DruidDataSource();
//     // }
//     //
//     // /**
//     //  * 创建多租户数据源（包装Druid）
//     //  */
//     // @Primary
//     // @Bean(name = "dataSource")
//     // public DataSource dataSource() {
//     //     log.info("创建多租户数据源>>>>>>>>>>>>>>>");
//     //     return new TenantDataSource(druidDataSource());
//     // }
//
//     // /**
//     //  * 实体管理器工厂 - 明确配置以避免自动配置冲突
//     //  */
//     // @Primary
//     // @Bean
//     // public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
//     //     log.info("创建实体管理器工厂>>>>>>>>>>>>>>>");
//     //     LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//     //     em.setDataSource(dataSource);
//     //     em.setPackagesToScan("com.bytter.*.entity",
//     //             "com.bytter.biz.*.entity",
//     //             "com.bytter.*.*.entity");
//     //
//     //     HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//     //     vendorAdapter.setGenerateDdl(false); // 根据你的 ddl-auto 配置
//     //     em.setJpaVendorAdapter(vendorAdapter);
//     //
//     //     // 从配置文件中获取 Hibernate 属性
//     //     Map<String, String> properties = jpaProperties.getProperties();
//     //
//     //     // 关键：确保 Session 上下文与 Spring 集成
//     //     properties.put("hibernate.current_session_context_class",
//     //             "org.springframework.orm.hibernate5.SpringSessionContext");
//     //
//     //     // 确保连接释放模式正确
//     //     properties.put("hibernate.connection.handling_mode", "DELAYED_ACQUISITION_AND_RELEASE_AFTER_TRANSACTION");
//     //
//     //     em.setJpaPropertyMap(properties);
//     //
//     //     return em;
//     // }
//     //
//     // /**
//     //  * 事务管理器 - 明确配置以避免自动配置冲突
//     //  */
//     // @Primary
//     // @Bean
//     // public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
//     //     return new JpaTransactionManager(entityManagerFactory);
//     // }
//
// }
