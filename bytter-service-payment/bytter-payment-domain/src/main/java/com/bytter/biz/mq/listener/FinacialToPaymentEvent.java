package com.bytter.biz.mq.listener;

import com.alibaba.fastjson.JSON;
import com.bytter.biz.mq.message.entity.ConsumerMessageRecord;
import com.bytter.biz.mq.message.service.ConsumerMessageRecordService;
import com.bytter.biz.mq.message.service.FinacialMessageHandler;
import com.bytter.biz.payment.api.dto.TransactionMqMsg;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.core.common.context.LocalMessageService;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.exception.AbstractBytterException;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.queue.CommonMqMsg;
import com.bytter.core.common.queue.TenantAwareStreamBridge;
import com.bytter.core.enums.IMessageEnum;
import com.bytter.core.enums.MsgHandleStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author luoyj
 * @date 2024/4/11.
 */
@Configuration
@Slf4j
public class FinacialToPaymentEvent {

    @Autowired
    private ConsumerMessageRecordService consumerMessageRecordService;

    @Autowired
    private FinacialMessageHandler finacialMessageHandler;

    @Autowired
    private LocalMessageService localMessageService;


    @Bean
    public Consumer<Message<CommonMqMsg<TransactionMqMsg>>> finacialToPayment(){
        log.info("FinacialToPaymentEvent start...");
        return this::process;
    }

    private void process(Message<CommonMqMsg<TransactionMqMsg>> message) {
        log.info("FinacialToPaymentEvent Received message：{}",message);

        CommonMqMsg<TransactionMqMsg> payload = message.getPayload();
        String msgId = payload.getMsgId();
        TransactionMqMsg data = payload.getData();

        try {
            // 1. 从消息头获取租户ID（fail-closed：缺失即拒绝，并落失败记录堵住重投循环）
            String tenantId = (String) message.getHeaders().get(TenantAwareStreamBridge.TENANT_ID_HEADER);
            if (!StringUtils.hasText(tenantId)) {
                log.warn("FinacialToPaymentEvent 拒绝处理：消息缺少 X-Tenant-Id 租户头，msgId={}", msgId);
                markFailed(payload);
                throw new ParameterException("消息缺少 X-Tenant-Id 租户头，拒绝处理");
            }

            // 2. 设置到TTL上下文
            UserContextHelper.helper().setTTLTenant(tenantId);
            log.debug("FinacialToPaymentEvent Set tenant context from message: {}", tenantId);

            //3.处理业务逻辑
            ConsumerMessageRecord record = consumerMessageRecordService.getById(msgId);
            if (Objects.isNull(record)){
                ConsumerMessageRecord cmr = ConsumerMessageRecord.builder().msgId(payload.getMsgId())
                        .businessType(data.getBusinessType())
                        .businessId(data.getBusinessId())
                        .tenantId(tenantId)
                        .message(JSON.toJSONString(payload))
                        .msgHandleStatus(MsgHandleStatus.N_HANDLE).createTime(LocalDateTime.now()).build();
                consumerMessageRecordService.save(cmr);
            }else {
                //表里有数据；查询状态；状态为 处理成功/失败 都不再做处理
                if (record.getMsgHandleStatus().equals(MsgHandleStatus.F_HANDLE) || record.getMsgHandleStatus().equals(MsgHandleStatus.Y_HANDLE)){
                    log.info("Message processed！msgId:{} ",msgId);
                    return;
                }
            }

            try {
                finacialMessageHandler.handler(data);
                consumerMessageRecordService.updateById(ConsumerMessageRecord.builder().msgId(msgId).updateTime(LocalDateTime.now()).msgHandleStatus(MsgHandleStatus.Y_HANDLE).build());
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                //获取异常消息枚举
                IMessageEnum messageEnum = getMessageEnum(e);
                //若不是业务异常和参数异常，则记录为未知错误。
                if (messageEnum != null) {
                    AbstractBytterException abstractBytterException = (AbstractBytterException) e;
                    sb.append(localMessageService.getLangByEnum(messageEnum, abstractBytterException.getParams()));
                }
                String stackTrace = ExceptionUtils.getStackTrace(e);
                consumerMessageRecordService.updateById(ConsumerMessageRecord.builder().msgId(msgId).updateTime(LocalDateTime.now()).msgHandleStatus(MsgHandleStatus.F_HANDLE).exceptionMsg(sb.append(stackTrace).toString()).build());
                throw new RuntimeException(e);
            }
        } finally {
            // 4. 清理上下文
            UserContextHelper.helper().clearTTLTenant();
            log.debug("FinacialToPaymentEvent Cleared tenant context");
        }
    }

    /**
     * 缺租户时无 TTL 上下文、也无法关联业务字段，落一条失败记录堵住重投循环。
     */
    private void markFailed(CommonMqMsg<TransactionMqMsg> payload) {
        try {
            ConsumerMessageRecord cmr = ConsumerMessageRecord.builder().msgId(payload.getMsgId())
                    .message(JSON.toJSONString(payload))
                    .msgHandleStatus(MsgHandleStatus.F_HANDLE).createTime(LocalDateTime.now())
                    .exceptionMsg("消息缺少 X-Tenant-Id 租户头，拒绝处理").build();
            consumerMessageRecordService.save(cmr);
        } catch (Exception markEx) {
            log.error("FinacialToPaymentEvent 落失败记录失败，msgId={}", payload.getMsgId(), markEx);
        }
    }

    /**
     * 根据异常获取消息枚举
     *
     */
    private IMessageEnum getMessageEnum(Exception ex) {
        IMessageEnum messageEnum = null;
        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;
            messageEnum = businessException.getIMessageEnum();
        } else if (ex instanceof ParameterException) {
            ParameterException parameterException = (ParameterException) ex;
            messageEnum = parameterException.getIMessageEnum();
        }
        return messageEnum;
    }


}
