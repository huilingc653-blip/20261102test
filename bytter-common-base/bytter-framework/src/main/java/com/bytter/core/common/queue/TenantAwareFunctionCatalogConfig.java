//package com.bytter.core.common.queue;
//
//import com.bytter.core.common.context.UserContextHelper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.cloud.function.context.FunctionCatalog;
//import org.springframework.cloud.function.context.catalog.FunctionInspector;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.messaging.Message;
//import java.lang.reflect.Method;
//import java.util.Collections;
//import java.util.Set;
//import java.util.function.Consumer;
//import java.util.function.Function;
//
///**
// * 方案B：增强的Function拦截 - 兼容所有MQ -- 无法启动
// */
//@Configuration
//@Slf4j
//public class TenantAwareFunctionCatalogConfig {
//    /**
//     * 包装FunctionCatalog，添加租户处理
//     */
////    @Primary
//    @Bean
//    @ConditionalOnMissingBean(name = "tenantAwareFunctionCatalog")
//    public FunctionCatalog tenantAwareFunctionCatalog(
//            FunctionCatalog originalCatalog) {
//
//        return new FunctionCatalog() {
//            @Override
//            public <T> T lookup(Class<?> type, String functionDefinition) {
//                T function = originalCatalog.lookup(type, functionDefinition);
//
//                if (function != null) {
//                    // 包装Consumer类型函数
//                    if (Consumer.class.isAssignableFrom(type)) {
//                        return wrapConsumer(type, function, functionDefinition);
//                    }
//                    // 包装Function类型函数
//                    else if (Function.class.isAssignableFrom(type)) {
//                        return wrapFunction(type, function, functionDefinition);
//                    }
//                }
//
//                return function;
//            }
//
//            @Override
//            public Set<String> getNames(Class<?> type) {
//                return Collections.emptySet();
//            }
//
//            @SuppressWarnings("unchecked")
//            private <T> T wrapConsumer(Class<?> type, T function, String functionName) {
//                Consumer<Message<?>> original = (Consumer<Message<?>>) function;
//
//                Consumer<Message<?>> wrapped = message -> {
//                    try {
//                        // 1. 从消息头获取租户ID
//                        String tenantId = (String) message.getHeaders().get(TenantHeaderConstant.TENANT_ID_HEADER);
//
//                        if (tenantId != null) {
//                            // 2. 设置到TTL上下文
//                            UserContextHelper.helper().setTTLTenant(tenantId);
//                            log.debug("Set tenant context from message: {}", tenantId);
//                        } else {
//                            log.warn("函数 {} 收到无租户消息", functionName);
//                            log.warn("No X-Tenant-Id header found in message");
//                        }
//
//                        log.debug("执行函数 {}，租户: {}", functionName, tenantId);
//
//                        // 3. 执行原始函数
//                        original.accept(message);
//
//                    } catch (Exception e) {
//                        log.error("函数 {} 执行异常", functionName, e);
//                        throw e;
//                    } finally {
//                        // 5. 清理上下文
//                        UserContextHelper.helper().clearTTLTenant();
//                    }
//                };
//
//                return (T) wrapped;
//            }
//
//            @SuppressWarnings("unchecked")
//            private <T> T wrapFunction(Class<?> type, T function, String functionName) {
//                // 类似地包装Function类型
//                return function;
//            }
//
//            // 实现其他必要方法...
//        };
//    }
//
//}
