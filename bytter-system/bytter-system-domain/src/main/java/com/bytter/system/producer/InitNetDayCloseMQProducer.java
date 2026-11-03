package com.bytter.system.producer;
import com.alibaba.fastjson2.JSONObject;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.queue.TenantAwareStreamBridge;
import com.bytter.system.api.dto.InitNetDayCloseMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
/**
 * @author YonJar
 * @date 2023/6/28.
 * @description
 */
@Slf4j
@Component
public class InitNetDayCloseMQProducer {

//    @Autowired
//    private StreamBridge streamBridge;

    @Autowired
    private TenantAwareStreamBridge tenantAwareStreamBridge;

    public void sendMessage(InitNetDayCloseMessageDTO message){
        String mesId = UUID.randomUUID().toString().replaceAll("-", "");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //获取的时间，可能会不准确
        message.setMsgSendDateTime(simpleDateFormat.format(new Date()));
        message.setMsgId(mesId);
        String jsonString = JSONObject.toJSONString(message);
//        boolean send = outputInitNetDayCloseBinding.output().send(MessageBuilder.withPayload(jsonString).build());

//        MimeType mimeType = MimeType.valueOf("application/json");
//        boolean send = streamBridge.send("init-net-day-close-out-0", MessageBuilder.withPayload(jsonString).setHeader("InitNetDay","InitNetDay").build(), mimeType);

        HashMap<String, Object> headers = new HashMap<>();
        headers.put("InitNetDay", "InitNetDay"); // routingKey
        //页面操作触发，获取当前上下文，设置租户ID
        boolean send = tenantAwareStreamBridge.send("init-net-day-close-out-0", jsonString, UserContextHelper.helper().getTenantId(), headers);

        log.info("InitNetDayCloseMQProducer 发送消息:{}，status:{}",jsonString,send);
    }
}
