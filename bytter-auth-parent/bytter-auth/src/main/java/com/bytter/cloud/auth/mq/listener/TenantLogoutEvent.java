package com.bytter.cloud.auth.mq.listener;

import com.bytter.auth.api.ApiIAuthenticationService;
import com.bytter.auth.api.dto.TenantLogoutMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.util.StringUtils;

import java.util.function.Consumer;

/**
 * 消费内管端发送的租户下线消息。
 */
@Slf4j
@Configuration
public class TenantLogoutEvent {

    private static final String TENANT_ID_HEADER = "X-Tenant-Id";

    private final ApiIAuthenticationService authenticationService;

    public TenantLogoutEvent(ApiIAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * 注册租户下线消费函数。
     *
     * @return MQ 消费函数
     */
    @Bean
    public Consumer<Message<TenantLogoutMessage>> tenantLogout() {
        log.info("[tenant-logout] consumer registered: function=tenantLogout, binding=tenantLogout-in-0");
        return this::process;
    }

    /**
     * 校验租户消息并调用原令牌注销逻辑。
     *
     * @param message 租户下线消息
     */
    private void process(Message<TenantLogoutMessage> message) {
        TenantLogoutMessage payload = message.getPayload();
        String headerTenantId = (String) message.getHeaders().get(TENANT_ID_HEADER);
        if (!StringUtils.hasText(headerTenantId)) {
            throw new IllegalArgumentException("tenant logout message is missing X-Tenant-Id");
        }
        if (!StringUtils.hasText(payload.getTenantId())) {
            payload.setTenantId(headerTenantId);
        } else if (!headerTenantId.equals(payload.getTenantId())) {
            throw new IllegalArgumentException("tenant logout message tenant does not match X-Tenant-Id");
        }

        log.info("[tenant-logout] consume start: messageId={}, tenantId={}, clientIds={}",
                message.getHeaders().getId(), payload.getTenantId(), payload.getClientIds());
        boolean loggedOut = authenticationService.tenantLogouts(payload.getTenantId(), payload.getClientIds());
        log.info("[tenant-logout] consume success: messageId={}, tenantId={}, clientIds={}, loggedOut={}",
                message.getHeaders().getId(), payload.getTenantId(), payload.getClientIds(), loggedOut);
    }
}
