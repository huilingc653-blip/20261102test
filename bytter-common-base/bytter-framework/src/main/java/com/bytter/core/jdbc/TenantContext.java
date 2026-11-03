//package com.bytter.core.jdbc;
//
//import org.springframework.stereotype.Component;
//
///**
// * 租户上下文管理器。
// *
// * @author zhaohc
// * @since 2025/12/18 14:40
// */
//@Component
//public class TenantContext {
//    /**
//     * 当前租户ID
//     */
//    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();
//    /**
//     * 是否启用租户过滤
//     */
//    private static final ThreadLocal<Boolean> TENANT_FILTER_ENABLED = ThreadLocal.withInitial(() -> true);
//
//    /**
//     * 设置当前租户ID
//     */
//    public static void setTenantId(String tenantId) {
//        CURRENT_TENANT.set(tenantId);
//    }
//
//    /**
//     * 获取当前租户ID
//     */
//    public static String getTenantId() {
//        return CURRENT_TENANT.get();
//    }
//
//    /**
//     * 清除租户ID
//     */
//    public static void clear() {
//        CURRENT_TENANT.remove();
//        TENANT_FILTER_ENABLED.remove();
//    }
//
//    /**
//     * 启用租户过滤
//     */
//    public static void enableTenantFilter() {
//        TENANT_FILTER_ENABLED.set(true);
//    }
//
//    /**
//     * 禁用租户过滤
//     */
//    public static void disableTenantFilter() {
//        TENANT_FILTER_ENABLED.set(false);
//    }
//
//    /**
//     * 检查租户过滤是否启用
//     */
//    public static boolean isTenantFilterEnabled() {
//        return TENANT_FILTER_ENABLED.get();
//    }
//}
