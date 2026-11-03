//package com.bytter.core.common.queue;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.function.context.FunctionRegistration;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.event.ContextRefreshedEvent;
//
//import java.util.Map;
//import java.util.function.Consumer;
//import java.util.function.Function;
//
///**
// * 通过监听ContextRefreshedEvent包装函数 - 更简单安全
// */
//@Configuration
//@Slf4j
//public class TenantAwareFunctionRegistrationListener implements ApplicationListener<ContextRefreshedEvent> {
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        Map<String, FunctionRegistration> registrations = event.getApplicationContext().getBeansOfType(FunctionRegistration.class);
//
//        if (registrations.isEmpty()) {
//            log.info("未找到FunctionRegistration，跳过函数包装");
//            return;
//        }
//        log.info("发现 {} 个FunctionRegistration，开始包装...", registrations.size());
//
//        for (Map.Entry<String, FunctionRegistration> entry : registrations.entrySet()) {
//            FunctionRegistration registration = entry.getValue();
//            String functionName = entry.getKey();
//
//            try {
//                Object target = registration.getTarget();
//                // 跳过已经是FunctionInvocationWrapper的情况
//                if (target != null && target.getClass().getName().contains("FunctionInvocationWrapper")) {
//                    log.debug("跳过FunctionInvocationWrapper: {}", functionName);
//                    continue;
//                }
//
//                // 包装原始函数
//                if (target instanceof Consumer) {
//                    Consumer<?> wrapped = TenantAwareFunctionWrapper.wrapConsumer((Consumer<?>) target, functionName);
//                    registration.target(wrapped);
//                    log.info("包装Consumer: {}", functionName);
//                } else if (target instanceof Function) {
//                    Function<?, ?> wrapped = TenantAwareFunctionWrapper.wrapFunction((Function<?, ?>) target, functionName);
//                    registration.target(wrapped);
//                    log.info("包装Function: {}", functionName);
//                }
//            } catch (Exception e) {
//                log.error("包装函数失败: {}", functionName, e);
//            }
//        }
//
//        log.info("函数包装完成");
//    }
//}
