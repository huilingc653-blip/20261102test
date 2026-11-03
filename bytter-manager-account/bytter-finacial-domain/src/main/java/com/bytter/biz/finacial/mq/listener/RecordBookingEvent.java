package com.bytter.biz.finacial.mq.listener;

import com.alibaba.fastjson.JSON;
import com.bytter.biz.finacial.mq.message.entity.ConsumerMessageRecord;
import com.bytter.core.common.context.LocalMessageService;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.exception.AbstractBytterException;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.queue.TenantAwareStreamBridge;
import com.bytter.core.enums.IMessageEnum;
import com.bytter.core.enums.MsgHandleStatus;
import com.bytter.biz.finacial.mq.message.service.ConsumerMessageRecordService;
import com.bytter.biz.finacial.mq.message.service.PaymentMessageHandler;
import com.bytter.biz.payment.api.dto.TransactionMqMsg;
import com.bytter.biz.payment.api.enums.BusinessType;
import com.bytter.core.common.queue.CommonMqMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
 * 监听 paymentToFinacial 结算发送过来的消息
 * @date 2024/3/5.
 */
@Configuration
@Slf4j
public class RecordBookingEvent {

    @Autowired
    private ConsumerMessageRecordService consumerMessageRecordService;

    @Autowired
    private PaymentMessageHandler paymentMessageHandler;

    @Autowired
    private LocalMessageService localMessageService;

    @Bean
    public Consumer<Message<CommonMqMsg<TransactionMqMsg>>> recordBooking(){
        log.info("RecordBookingEvent start...");
        return this::process;
    }

    private void process(Message<CommonMqMsg<TransactionMqMsg>> message) {
        log.info("RecordBookingEvent Received message：{}",message);

        CommonMqMsg<TransactionMqMsg> payload = message.getPayload();
        String msgId = payload.getMsgId();
        TransactionMqMsg data = payload.getData();

        try {
            // 1. 从消息头获取租户ID（fail-closed：缺失即拒绝，并落失败记录堵住重投循环）
            String tenantId = (String) message.getHeaders().get(TenantAwareStreamBridge.TENANT_ID_HEADER);
            if (!StringUtils.hasText(tenantId)) {
                log.warn("RecordBookingEvent 拒绝处理：消息缺少 X-Tenant-Id 租户头，msgId={}", msgId);
                markFailed(payload);
                throw new ParameterException("消息缺少 X-Tenant-Id 租户头，拒绝处理");
            }

            // 2. 设置到TTL上下文
            UserContextHelper.helper().setTTLTenant(tenantId);
            log.info("RecordBookingEvent Set tenantId: {}", tenantId);

            ConsumerMessageRecord record = consumerMessageRecordService.getById(msgId);
            if (Objects.isNull(record)){
                ConsumerMessageRecord cmr = ConsumerMessageRecord.builder().msgId(payload.getMsgId())
                        .businessType(data.getBusinessType())
                        .businessId(data.getBusinessId())
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
                paymentMessageHandler.handler(data);
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
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } finally {
            UserContextHelper.helper().clearTTLTenant();
            log.debug("RecordBookingEvent Cleared tenant context");
        }
    }

    /**
     * 缺租户等前置校验失败时，落一条失败记录，避免消息因无限重投形成毒消息循环。
     */
    private void markFailed(CommonMqMsg<TransactionMqMsg> payload) {
        try {
            // 缺租户时该 msgId 记录必然不存在，需用 save 新建 F_HANDLE 记录；updateById 会命中 0 行导致空写
            // BUSINESS_TYPE/BUSINESS_ID 为 NOT NULL 列，需按正常路径同源取值；data 或字段为 null 时用占位兜底，
            // 保证 FAIL 记录一定能满足 NOT NULL 落库，堵住毒消息重投循环
            TransactionMqMsg data = payload.getData();
            BusinessType businessType = (data == null || data.getBusinessType() == null) ? BusinessType.UNKNOWN : data.getBusinessType();
            String businessId = (data == null || data.getBusinessId() == null) ? payload.getMsgId() : data.getBusinessId();
            ConsumerMessageRecord cmr = ConsumerMessageRecord.builder().msgId(payload.getMsgId())
                    .businessType(businessType)
                    .businessId(businessId)
                    .message(JSON.toJSONString(payload))
                    .msgHandleStatus(MsgHandleStatus.F_HANDLE).createTime(LocalDateTime.now())
                    .exceptionMsg("消息缺少 X-Tenant-Id 租户头，拒绝处理").build();
            consumerMessageRecordService.save(cmr);
        } catch (Exception markEx) {
            // 有意的 fail-closed 降级：落库失败仅记 error（含 msgId + 堆栈，可观测可追踪），
            // 主路径下方仍抛 ParameterException 拒收，绝不静默放行；持久性 DB 故障时的重投属系统级降级
            log.error("RecordBookingEvent 落失败记录失败，msgId={}", payload.getMsgId(), markEx);
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
