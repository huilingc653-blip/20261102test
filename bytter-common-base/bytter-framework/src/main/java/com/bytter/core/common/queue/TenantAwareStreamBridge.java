package com.bytter.core.common.queue;

import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.exception.ParameterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 2.StreamBridge包装器，统一处理租户ID的传递
 * create by LuoYJ on 2026/1/8.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TenantAwareStreamBridge {

    public static final String TENANT_ID_HEADER = "X-Tenant-Id";

    private final StreamBridge streamBridge;

    /**
     * 发送消息（自动添加当前上下文租户ID到header）
     */
    public boolean send(String bindingName, Object data) {
        return send(bindingName, data, null);
    }

    /**
     * 发送消息（自动添加当前上下文租户ID到header）
     */
    public boolean send(String bindingName, Object data, Map<String,Object> headers) {
        String tenantId = UserContextHelper.helper().getTTLTenant();

        // 无租户上下文时拒绝发送，避免消费侧因缺失 X-Tenant-Id 而放行
        if (!StringUtils.hasText(tenantId)) {
            throw new ParameterException("发送 MQ 消息缺少租户上下文，binding=" + bindingName);
        }

        MessageBuilder<?> builder = MessageBuilder.withPayload(data);

        // 1. 添加租户ID到header（核心操作）
        builder.setHeader(TENANT_ID_HEADER, tenantId);
        log.debug("Added X-Tenant-Id header: {}", tenantId);

        // 2. 添加其他需要的header
        if (headers != null) {
            headers.forEach(builder::setHeader);
        }

        // 3. 发送消息
        return streamBridge.send(bindingName, builder.build());
    }

    /**
     * 发送消息（主动传递上下文租户ID到header）
     * @param bindingName
     * @param data 消息
     * @param tenantId 租户ID
     * @param headers 请求头
     */
    public boolean send(String bindingName, Object data, String tenantId, Map<String,Object> headers) {
        // 显式租户缺失时拒绝发送，避免消费侧因缺失 X-Tenant-Id 而放行
        if (!StringUtils.hasText(tenantId)) {
            throw new ParameterException("发送 MQ 消息缺少租户上下文，binding=" + bindingName);
        }

        MessageBuilder<?> builder = MessageBuilder.withPayload(data);

        // 1. 添加租户ID到header（核心操作）
        builder.setHeader(TENANT_ID_HEADER, tenantId);
        log.debug("Added X-Tenant-Id header: {}", tenantId);

        // 2. 添加其他需要的header
        if (headers != null) {
            headers.forEach(builder::setHeader);
        }

        MimeType mimeType = MimeType.valueOf("application/json");

        // 3. 发送消息
        return streamBridge.send(bindingName, builder.build(), mimeType);
    }

}
