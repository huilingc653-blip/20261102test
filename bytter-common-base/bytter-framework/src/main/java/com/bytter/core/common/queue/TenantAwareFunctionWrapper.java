//package com.bytter.core.common.queue;
//
//import com.bytter.core.common.context.UserContextHelper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.messaging.Message;
//
//import java.lang.reflect.Field;
//import java.util.function.Consumer;
//import java.util.function.Function;
//
///**
// * create by LuoYJ on 2026/1/9.
// * 租户感知函数包装器 (针对 3.0.14.RELEASE 版本)
// */
//@Slf4j
//public class TenantAwareFunctionWrapper {
//
//    /**
//     * 包装 Consumer 类型的函数
//     */
//    @SuppressWarnings({"unchecked", "rawtypes"})
//    public static Consumer wrapConsumer(Consumer original, String functionName) {
//        return input -> {
//            String tenantId = null;
//            try {
//                // 从 Message 头中提取租户ID
//                if (input instanceof Message) {
//                    Message message = (Message) input;
//                    tenantId = (String) message.getHeaders().get(TenantHeaderConstant.TENANT_ID_HEADER);
//
//                    if (tenantId != null) {
//                        UserContextHelper.helper().setTTLTenant(tenantId);
//                        log.debug("Consumer '{}': 设置租户上下文: {}", functionName, tenantId);
//                    } else {
//                        log.warn("Consumer '{}': 消息头中未找到租户ID ({}).",
//                                functionName, TenantHeaderConstant.TENANT_ID_HEADER);
//                    }
//                }
//                // 执行原始函数
//                original.accept(input);
//            } catch (Exception e) {
//                log.error("Consumer '{}' 执行失败. 租户: {}", functionName, tenantId, e);
//                throw e;
//            } finally {
//                // 清理上下文
//                UserContextHelper.helper().clearTTLTenant();
//            }
//        };
//    }
//
//    /**
//     * 包装 Function 类型的函数
//     */
//    @SuppressWarnings({"unchecked", "rawtypes"})
//    public static Function wrapFunction(Function original, String functionName) {
//        return input -> {
//            String tenantId = null;
//            try {
//                if (input instanceof Message) {
//                    Message message = (Message) input;
//                    tenantId = (String) message.getHeaders().get(TenantHeaderConstant.TENANT_ID_HEADER);
//
//                    if (tenantId != null) {
//                        UserContextHelper.helper().setTTLTenant(tenantId);
//                        log.debug("Function '{}': 设置租户上下文: {}", functionName, tenantId);
//                    }
//                }
//                // 执行原始函数并返回结果
//                return original.apply(input);
//            } catch (Exception e) {
//                log.error("Function '{}' 执行失败. 租户: {}", functionName, tenantId, e);
//                throw e;
//            } finally {
//                UserContextHelper.helper().clearTTLTenant();
//            }
//        };
//    }
//
//    /**
//     * 包装FunctionInvocationWrapper中的目标函数
//     */
//    public static Object wrapFunctionInvocationWrapper(Object wrapper, String functionName) {
//        try {
//            if (wrapper == null) return null;
//
//            // 获取FunctionInvocationWrapper的target字段
//            Field targetField = wrapper.getClass().getDeclaredField("target");
//            targetField.setAccessible(true);
//            Object target = targetField.get(wrapper);
//
//            if (target instanceof Consumer) {
//                Consumer wrapped = wrapConsumer((Consumer) target, functionName);
//                targetField.set(wrapper, wrapped);
//                log.debug("包装了FunctionInvocationWrapper中的Consumer: {}", functionName);
//            } else if (target instanceof Function) {
//                Function wrapped = wrapFunction((Function) target, functionName);
//                targetField.set(wrapper, wrapped);
//                log.debug("包装了FunctionInvocationWrapper中的Function: {}", functionName);
//            }
//            // Supplier等其他类型可以按需扩展
//
//            return wrapper;
//        } catch (Exception e) {
//            log.error("包装FunctionInvocationWrapper失败: {}", functionName, e);
//            return wrapper;
//        }
//    }
//}
