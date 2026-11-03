package com.bytter.biz.finacial.consumer;

import com.alibaba.fastjson.JSONObject;
import com.bytter.biz.finacial.closedeal.service.FcDayCloseService;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.queue.TenantAwareStreamBridge;
import com.bytter.system.api.dto.InitNetDayCloseMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.util.StringUtils;

import java.util.function.Consumer;

/**
 * @author luoyj
 * @date 2024/4/23.
 */
@Configuration
@Slf4j
public class InitNetDayCloseEvent {

    @Autowired
    private FcDayCloseService fcDayCloseService;

    @Bean
    public Consumer<Message<String>> initNetDayClose(){
        return this::process;
    }

    private void process(Message<String> message){
        log.info("InitNetDayCloseMQConsumer 接收到消息：{}",message.getPayload());

        try {
            // 1. 从消息头获取租户ID
            String tenantId = (String) message.getHeaders().get(TenantAwareStreamBridge.TENANT_ID_HEADER);

            if (!StringUtils.hasText(tenantId)) {
                log.warn("InitNetDayCloseEvent 拒绝处理：消息缺少 X-Tenant-Id 租户头");
                throw new ParameterException("消息缺少 X-Tenant-Id 租户头，拒绝处理");
            }

            // 2. 设置到TTL上下文
            UserContextHelper.helper().setTTLTenant(tenantId);
            log.debug("Set tenant context from message: {}", tenantId);

            InitNetDayCloseMessageDTO dto = JSONObject.parseObject(message.getPayload(), InitNetDayCloseMessageDTO.class);
            if (dto.getMsgType().equals(InitNetDayCloseMessageDTO.Msg.N)){
                fcDayCloseService.initNetCloseDate(dto.getNetId(), dto.getCloseDate(), dto.getUserId());
            } else if (dto.getMsgType().equals(InitNetDayCloseMessageDTO.Msg.P)) {
                fcDayCloseService.initPoolNetCloseDate(dto.getNetId(), dto.getCloseDate(), dto.getUserId());
            }
        } finally {
            UserContextHelper.helper().clearTTLTenant();
            log.debug("Cleared tenant context");
        }
    }
}
