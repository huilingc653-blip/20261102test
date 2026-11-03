//package com.bytter.core.jdbc;
//
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.core.util.URLUtil;
//import com.bytter.core.base.entity.ShiroUser;
//import com.bytter.core.utils.JsonUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.nio.charset.StandardCharsets;
//
///**
// * 租户拦截器，从请求头或Session中提取租户ID。
// *
// * @author zhaohc
// * @since 2025/12/18 14:41
// */
//@Slf4j
//public class TenantInterceptor implements HandlerInterceptor {
//
//    // 租户ID参数名（可以从header、session、cookie等地方获取）
//    private static final String TENANT_HEADER = "X-Tenant-ID";
//    private static final String TENANT_PARAM = "tenantId";
//    private static final String X_CLIENT_TOKEN_USER = "x-client-token-user";
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//
//        // 1. 优先从Header中获取租户ID
//        String tenantIdStr = request.getHeader(TENANT_HEADER);
//
//        // 2. 如果Header中没有，尝试从参数中获取
//        if (tenantIdStr == null || tenantIdStr.isEmpty()) {
//            tenantIdStr = request.getParameter(TENANT_PARAM);
//        }
//
//        // 3. 如果参数中没有，尝试从用户Token中获取
//        if (tenantIdStr == null || tenantIdStr.isEmpty()) {
//            String tokenUser = request.getHeader(X_CLIENT_TOKEN_USER);
//            String decodeUser = URLUtil.decode(tokenUser, StandardCharsets.UTF_8);
//            if (ObjectUtil.isNotEmpty(decodeUser)){
//                ShiroUser shiroUser = JsonUtils.parseObject(decodeUser, ShiroUser.class);
//                tenantIdStr = shiroUser.getTenantId();
//            }
//        }
//
//        // 4. 如果获取到租户ID，设置到上下文 TODO here 不需要从 JWT 中获取？获取 RPC String tenantId = RpcContext.getServiceContext().getAttachment("tenantId");
//        if (tenantIdStr != null && !tenantIdStr.isEmpty()) {
//            log.info("设置租户ID：{}", tenantIdStr);
//            TenantContext.setTenantId(tenantIdStr);
//        } else {
//            // 如果没有租户ID，使用默认租户或抛出异常
//            log.warn("No tenant ID provided. Using default tenant.");
//        }
//
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        // 请求处理完成后清理租户上下文
//        TenantContext.clear();
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        // 确保资源清理
//        TenantContext.clear();
//    }
//}
