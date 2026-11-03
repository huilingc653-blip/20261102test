package com.bytter.core.common.context;

import cn.hutool.json.JSONUtil;
import com.bytter.core.base.entity.ICurrentUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * 定认Dubbo请求拦截器，传递请求Token信息（用户信息）。
 *
 * @author zhaohc
 * @since 2024/6/15 13:35
 */
@Slf4j
@Activate(group = {CommonConstants.CONSUMER}, order = 1)
public class DubboTokenFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            // 获取dubbo请求携带的用户信息
            ICurrentUser currentUser = UserContextHelper.helper().getCurrentUser();
            // 获取dubbo请求携带的租户ID
            String ttlTenant = UserContextHelper.helper().getTTLTenant();
            if (currentUser != null) {
                // 将Token存储到dubbo的ThreadLocal中
                RpcContext.getClientAttachment().setAttachment("shiroUser", JSONUtil.toJsonStr(currentUser));
                // log.info("dubbo消费者传递用户信息: {}, URL: {}", currentUser.getUsername(), serviceUrl);
            }
            if (ttlTenant != null) {
                // 独立发送租户ID，与用户信息解耦，确保 Provider 能同时收到两者并做一致性校验
                RpcContext.getClientAttachment().setAttachment("ttlTenant", ttlTenant);
                // log.info("dubbo消费者传递租户信息: {}, URL: {}", ttlTenant, serviceUrl);
            }

            // 传递国际化 Locale，确保跨服务 Dubbo 调用时国际化数据一致
            Locale locale = LocaleContextHolder.getLocale();
            RpcContext.getClientAttachment().setAttachment("locale", locale.toString());

            // 执行实际的远程调用
            return invoker.invoke(invocation);
        } finally {
            // 手动清理附件，防止数据污染
            RpcContext.getClientAttachment().clearAttachments();
        }
    }

    /**
     * 构建服务URL
     */
    private String buildServiceUrl(Invoker<?> invoker, Invocation invocation) {
        try {
            // 使用 ServiceContext 获取服务信息
            String serviceName = invoker.getInterface().getName();
            String methodName = invocation.getMethodName();
            String remoteHost = RpcContext.getServiceContext().getRemoteHost();
            int remotePort = RpcContext.getServiceContext().getRemotePort();

            return String.format("dubbo://%s:%s/%s.%s", remoteHost, remotePort, serviceName, methodName);
        } catch (Exception e) {
            return "URL构建失败: " + e.getMessage();
        }
    }

}
