//package com.bytter.cloud.common.core.util;
//import com.bytter.cloud.common.core.persistence.vo.IUser;
//import com.bytter.cloud.common.core.persistence.vo.UserImpl;
//import org.springframework.util.CollectionUtils;
//import java.util.Map;
///**
// * 用户上下文
// *
// * @author zhoul 2021-01-14
// * @since 1.0.0
// */
//public class UserContextHolder {
//    private final ThreadLocal<IUser> threadLocal;
//    private UserContextHolder() {
//        this.threadLocal = new ThreadLocal<>();
//    }
//    /**
//     * 创建实例
//     *
//     * @return 用户上下文对象
//     */
//    public static UserContextHolder getInstance() {
//        return SingletonHolder.USER_CONTEXT_HOLDER;
//    }
//    /**
//     * 静态内部类单例模式
//     * 单例初使化
//     */
//    private static class SingletonHolder {
//        private static final UserContextHolder USER_CONTEXT_HOLDER = new UserContextHolder();
//    }
//    /**
//     * 用户上下文中放入信息
//     *
//     * @param map 设置值
//     */
//    public void setContext(Map<String, String> map) {
//        if (CollectionUtils.isEmpty(map)) {
//            threadLocal.set(new UserImpl());
//            return;
//        }
//        UserImpl user = new UserImpl();
//        user.setUsername(map.get("user_name"));
//        user.setTenantId(map.get("tenantId"));
//        user.setCurrentUserId(map.get("userId"));
//        user.setCorpId(map.get("corpId"));
//        user.setDataScope(map.get("dataScope"));
//        threadLocal.set(user);
//    }
//    /**
//     * 获取上下文中的信息
//     *
//     * @return 返回值
//     */
//    public IUser getContext() {
//        return threadLocal.get();
//    }
//    /**
//     * 清空用户上下文
//     */
//    public void clear() {
//        threadLocal.remove();
//    }
//}
