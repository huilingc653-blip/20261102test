//package com.bytter.core.common.queue;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.BeanPostProcessor;
//import org.springframework.cloud.function.context.catalog.SimpleFunctionRegistry;
//import org.springframework.context.annotation.Configuration;
//
//import java.lang.reflect.Field;
//import java.util.Map;
//import java.util.function.Consumer;
//import java.util.function.Function;
//
///**
// * create by LuoYJ on 2026/1/9.
// * 租户感知函数注册后处理器 -- 无效果
// * 通过BeanPostProcessor机制包装SimpleFunctionRegistry中的函数
// */
//@Configuration
//@Slf4j
//public class TenantAwareFunctionPostProcessor implements BeanPostProcessor{
//
//    @SuppressWarnings({"rawtypes", "unchecked"})
//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        if (bean instanceof SimpleFunctionRegistry) {
//            log.info("开始处理 SimpleFunctionRegistry，包装函数以支持租户上下文...");
//            try {
//                wrapFunctionsInRegistry((SimpleFunctionRegistry) bean);
//            } catch (Exception e) {
//                log.error("处理SimpleFunctionRegistry失败", e);
//            }
//        }
//        return bean;
//    }
//
//    @SuppressWarnings({"rawtypes", "unchecked"})
//    private void wrapFunctionsInRegistry(SimpleFunctionRegistry registry) throws Exception {
//        // 1. 包装 registrationsByFunction 中的FunctionInvocationWrapper
//        Field byFunctionField = SimpleFunctionRegistry.class.getDeclaredField("registrationsByFunction");
//        byFunctionField.setAccessible(true);
//        Map<Object, Object> registrationsByFunction = (Map<Object, Object>) byFunctionField.get(registry);
//
//        for (Map.Entry<Object, Object> entry : registrationsByFunction.entrySet()) {
//            Object function = entry.getKey();
//            String functionName = getFunctionName(entry.getValue());
//
//            // 如果key是FunctionInvocationWrapper，直接包装它
//            if (function != null && function.getClass().getName().contains("FunctionInvocationWrapper")) {
//                TenantAwareFunctionWrapper.wrapFunctionInvocationWrapper(function, functionName);
//            }
//        }
//
//        // 2. 包装 registrationsByName 中的FunctionRegistration
//        Field byNameField = SimpleFunctionRegistry.class.getDeclaredField("registrationsByName");
//        byNameField.setAccessible(true);
//        Map<String, Object> registrationsByName = (Map<String, Object>) byNameField.get(registry);
//
//        for (Map.Entry<String, Object> entry : registrationsByName.entrySet()) {
//            String functionName = entry.getKey();
//            Object registration = entry.getValue();
//
//            // 从FunctionRegistration中获取target并包装
//            if (registration != null) {
//                wrapFunctionRegistration(registration, functionName);
//            }
//        }
//
//        log.info("SimpleFunctionRegistry 函数包装完成，共处理 {} 个函数",
//                registrationsByFunction.size());
//    }
//
//    private String getFunctionName(Object registration) {
//        try {
//            if (registration != null) {
//                Field namesField = registration.getClass().getDeclaredField("names");
//                namesField.setAccessible(true);
//                Object names = namesField.get(registration);
//                if (names instanceof String[]) {
//                    String[] nameArray = (String[]) names;
//                    if (nameArray.length > 0) {
//                        return nameArray[0];
//                    }
//                }
//            }
//        } catch (Exception e) {
//            // 忽略异常，返回默认值
//        }
//        return "unknown";
//    }
//
//    private void wrapFunctionRegistration(Object registration, String functionName) throws Exception {
//        Field targetField = registration.getClass().getDeclaredField("target");
//        targetField.setAccessible(true);
//        Object target = targetField.get(registration);
//
//        if (target instanceof Consumer) {
//            Consumer wrapped = TenantAwareFunctionWrapper.wrapConsumer((Consumer) target, functionName);
//            targetField.set(registration, wrapped);
//        } else if (target instanceof Function) {
//            Function wrapped = TenantAwareFunctionWrapper.wrapFunction((Function) target, functionName);
//            targetField.set(registration, wrapped);
//        }
//        // FunctionInvocationWrapper已经在第一步处理过了
//    }
//}
