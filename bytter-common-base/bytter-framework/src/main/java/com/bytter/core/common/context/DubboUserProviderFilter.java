package com.bytter.core.common.context;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * Dubbo生产者，获取用户信息。
 *
 * @author zhaohc
 * @since 2025/2/19 14:27
 */
@Slf4j
@Activate(group = {CommonConstants.PROVIDER}, order = 1)
public class DubboUserProviderFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        // 获取Attachment中的用户信息和租户信息
        String userToken = RpcContext.getServerAttachment().getAttachment("shiroUser");
        String ttlTenant = RpcContext.getServerAttachment().getAttachment("ttlTenant");
        // 获取国际化 Locale，确保跨服务 Dubbo 调用时国际化数据一致
        String localeStr = RpcContext.getServerAttachment().getAttachment("locale");

        // 判断是否有需要设置的上下文信息
        boolean hasUserToken = ObjectUtil.isNotEmpty(userToken);
        boolean hasTenantToken = ObjectUtil.isNotEmpty(ttlTenant);
        boolean hasLocale = ObjectUtil.isNotEmpty(localeStr);

        // 如果没有传递任何上下文信息，直接执行，不污染当前线程上下文
        if (!hasUserToken && !hasTenantToken && !hasLocale) {
            try {
                return invoker.invoke(invocation);
            } finally {
                RpcContext.getServerAttachment().clearAttachments();
            }
        }

        // 保存原始上下文（仅在需要设置新上下文时才保存和恢复）
        // 重要：必须使用 getLocalUser() 而非 getCurrentUser()
        // getCurrentUser() 在 TTL 为空时会回退读取 Dubbo ServerAttachment，
        // 导致把本次请求传入的 shiroUser 误当作 Provider 线程的原始用户信息，
        // 最终在 finally 恢复时残留到 Provider 线程池中，造成跨请求数据污染
        ICurrentUser originalUser = UserContextHelper.helper().getLocalUser();
        String originalTenant = UserContextHelper.helper().getTTLTenant();
        // 保存 Provider 线程原有的 Locale，用于调用结束后恢复，防止线程池污染
        Locale originalLocale = LocaleContextHolder.getLocale();

        // 解析并校验传入的用户上下文，校验其与租户上下文的一致性
        ShiroUser incomingUser = null;
        if (hasUserToken) {
            incomingUser = JsonUtils.parseObject(userToken, ShiroUser.class);
            if (incomingUser == null) {
                // 用户信息解析失败，无法建立正确的用户上下文，拒绝执行（fail-closed）
                RpcContext.getServerAttachment().clearAttachments();
                log.error("Dubbo Provider 解析 shiroUser 失败，拒绝执行：userToken={}", userToken);
                throw new RpcException("shiroUser 解析失败，无法建立用户上下文");
            }
            // 一致性校验：用户租户与独立租户上下文必须一致，不一致则拒绝（fail-closed）
            if (hasTenantToken && !ttlTenant.equals(incomingUser.getTenantId())) {
                RpcContext.getServerAttachment().clearAttachments();
                log.error("Dubbo Provider 租户上下文不一致，拒绝执行：shiroUser.tenantId={}, ttlTenant={}",
                        incomingUser.getTenantId(), ttlTenant);
                throw new RpcException("租户上下文不一致：shiroUser.tenantId 与 ttlTenant 不匹配");
            }
        }

        try {
            // 安装用户上下文（setUser 会连带安装租户上下文，已通过一致性校验）
            if (hasUserToken) {
                UserContextHelper.helper().setUser(incomingUser);
            } else if (hasTenantToken) {
                // 只传了租户ID
                UserContextHelper.helper().setTTLTenant(ttlTenant);
            }
            if (hasLocale) {
                LocaleContextHolder.setLocale(LocaleUtils.toLocale(localeStr));
            }

            // 执行实际的远程调用
            return invoker.invoke(invocation);
        } finally {
            // 恢复原始上下文，防止线程池污染
            // 关键：只恢复被我们修改的部分，不清除 Consumer 线程已有的上下文
            // 1. 恢复用户上下文
            if (hasUserToken) {
                if (originalUser != null) {
                    UserContextHelper.helper().setUser((ShiroUser) originalUser);
                } else {
                    UserContextHelper.helper().removeUser();
                }
            }
            // 2. 独立精确恢复租户上下文（setUser/removeUser 都会连带修改租户上下文，必须显式恢复）
            if (hasUserToken || hasTenantToken) {
                if (originalTenant != null) {
                    UserContextHelper.helper().setTTLTenant(originalTenant);
                } else {
                    UserContextHelper.helper().clearTTLTenant();
                }
            }
            // 3. 恢复原始 Locale
            if (hasLocale) {
                LocaleContextHolder.setLocale(originalLocale);
            }
            // 手动清理附件，防止数据污染
            RpcContext.getServerAttachment().clearAttachments();
        }
    }

    /**
     * 构建服务URL
     */
    private String buildServiceUrl(Invoker<?> invoker, Invocation invocation) {
        try {
            String serviceName = invoker.getInterface().getName();
            String methodName = invocation.getMethodName();
            String localHost = RpcContext.getServiceContext().getLocalHost();
            int localPort = RpcContext.getServiceContext().getLocalPort();

            return String.format("dubbo://%s:%s/%s.%s", localHost, localPort, serviceName, methodName);
        } catch (Exception e) {
            return "URL构建失败: " + e.getMessage();
        }
    }

}
