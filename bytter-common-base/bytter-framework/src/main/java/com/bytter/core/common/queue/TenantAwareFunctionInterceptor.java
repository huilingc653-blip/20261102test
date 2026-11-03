//package com.bytter.core.common.queue;
//
//import com.bytter.core.common.context.UserContextHelper;
//import lombok.extern.slf4j.Slf4j;
////import org.springframework.cloud.function.context.catalog.FunctionInvocationWrapper;
//import org.springframework.cloud.function.context.catalog.SimpleFunctionRegistry;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.Message;
//
//import java.util.function.Consumer;
//import java.util.function.Function;
//
///**
// * 方案C：精细化的函数包装方案
// * create by AI Assistant on 2026/1/9.
// */
//@Configuration
//@Slf4j
//public class TenantAwareFunctionInterceptor {
//
//    @Bean
//    public SimpleFunctionRegistry.FunctionInvocationWrapperPostProcessor tenantAwareFunctionWrapperPostProcessor() {
//        return originalWrapper -> {
//            if (originalWrapper.isConsumer()) {
//                return wrapConsumer(originalWrapper);
//            } else if (originalWrapper.isFunction()) {
//                return wrapFunction(originalWrapper);
//            }
//            return originalWrapper;
//        };
//    }
//
//    /**
//     * 包装Consumer类型的函数
//     */
//    private FunctionInvocationWrapper wrapConsumer(SimpleFunctionRegistry.FunctionInvocationWrapper originalWrapper) {
//        Consumer<?> originalConsumer = (Consumer<?>) originalWrapper.getTarget();
//
//        Consumer<Object> wrappedConsumer = input -> {
//            String tenantId = null;
//            String functionName = originalWrapper.getFunctionDefinition();
//
//            try {
//                if (input instanceof Message) {
//                    Message<?> message = (Message<?>) input;
//                    tenantId = (String) message.getHeaders().get(TenantHeaderConstant.TENANT_ID_HEADER);
//
//                    if (tenantId != null) {
//                        UserContextHelper.helper().setTTLTenant(tenantId);
//                        log.debug("Consumer {}: Set tenant context from message: {}",
//                                functionName, tenantId);
//                    } else {
//                        log.warn("Consumer {}: No X-Tenant-Id header found", functionName);
//                    }
//                }
//
//                log.debug("执行Consumer {}，租户: {}", functionName, tenantId);
//                originalConsumer.accept(input);
//
//            } catch (Exception e) {
//                log.error("Consumer {} 执行异常，租户: {}", functionName, tenantId, e);
//                throw e;
//            } finally {
//                UserContextHelper.helper().clearTTLTenant();
//            }
//        };
//
//        // 创建新的FunctionInvocationWrapper
//        return new FunctionInvocationWrapper(
//                wrappedConsumer,
//                originalWrapper.getInputType(),
//                originalWrapper.getOutputType(),
//                originalWrapper.getFunctionDefinition(),
//                originalWrapper.isComposed(),
//                originalWrapper.isSupplier(),
//                true,  // isConsumer
//                false  // isFunction
//        );
//    }
//
//    /**
//     * 包装Function类型的函数
//     */
//    private FunctionInvocationWrapper wrapFunction(FunctionInvocationWrapper originalWrapper) {
//        Function<?, ?> originalFunction = (Function<?, ?>) originalWrapper.getTarget();
//
//        Function<Object, Object> wrappedFunction = input -> {
//            String tenantId = null;
//            String functionName = originalWrapper.getFunctionDefinition();
//
//            try {
//                if (input instanceof Message) {
//                    Message<?> message = (Message<?>) input;
//                    tenantId = (String) message.getHeaders().get(TenantHeaderConstant.TENANT_ID_HEADER);
//
//                    if (tenantId != null) {
//                        UserContextHelper.helper().setTTLTenant(tenantId);
//                        log.debug("Function {}: Set tenant context from message: {}",
//                                functionName, tenantId);
//                    }
//                }
//
//                log.debug("执行Function {}，租户: {}", functionName, tenantId);
//                return originalFunction.apply(input);
//
//            } catch (Exception e) {
//                log.error("Function {} 执行异常，租户: {}", functionName, tenantId, e);
//                throw e;
//            } finally {
//                UserContextHelper.helper().clearTTLTenant();
//            }
//        };
//
//        return new FunctionInvocationWrapper(
//                wrappedFunction,
//                originalWrapper.getInputType(),
//                originalWrapper.getOutputType(),
//                originalWrapper.getFunctionDefinition(),
//                originalWrapper.isComposed(),
//                originalWrapper.isSupplier(),
//                false,  // isConsumer
//                true    // isFunction
//        );
//    }
//
//}
