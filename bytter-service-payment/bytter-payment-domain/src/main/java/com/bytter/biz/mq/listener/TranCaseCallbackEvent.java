package com.bytter.biz.mq.listener;

import com.bytter.biz.foreign.service.impl.TranCaseCallbackServiceImpl;
import com.bytter.biz.payment.api.dto.TranCaseCallbackDTO;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.queue.TenantAwareStreamBridge;
import com.bytter.core.utils.StringUtil;
import com.bytter.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

/**
 * 消费内管发送的落地限额处理结果。
 */
@Slf4j
@Configuration
public class TranCaseCallbackEvent {

    @Autowired
    private TranCaseCallbackServiceImpl tranCaseCallbackService;

    /**
     * 注册落地限额处理结果消费者。
     *
     * @return MQ 消费函数
     */
    @Bean
    public Consumer<Message<TranCaseCallbackDTO>> tranCaseCallback() {
        log.info("[tran-case-callback] consumer registered: function=tranCaseCallback, binding=tranCaseCallback-in-0");
        return this::process;
    }

    /**
     * 校验消息租户并交给原回调业务处理服务。
     *
     * @param message 落地限额处理结果消息
     */
    private void process(Message<TranCaseCallbackDTO> message) {
        long startTime = System.currentTimeMillis();
        TranCaseCallbackDTO dto = message.getPayload();
        String headerTenantId = (String) message.getHeaders().get(TenantAwareStreamBridge.TENANT_ID_HEADER);
        log.info("[tran-case-callback] receive start: messageId={}, headerTenantId={}, payloadTenantId={}, "
                        + "caseId={}, caseNo={}, businessId={}, businessNo={}, sourceBizType={}, caseStatus={}, "
                        + "handleResult={}",
                message.getHeaders().getId(), headerTenantId, dto.getTenantId(), dto.getCaseId(), dto.getCaseNo(),
                dto.getBusinessId(), dto.getBusinessNo(), dto.getSourceBizType(), dto.getCaseStatus(),
                dto.getHandleResult());
        try {
            if (StringUtils.isBlank(headerTenantId)) {
                throw new ParameterException("落地限额处理结果消息缺少 X-Tenant-Id");
            }
            if (StringUtils.isBlank(dto.getTenantId())) {
                dto.setTenantId(headerTenantId);
                log.info("[tran-case-callback] payload tenant restored from header: messageId={}, tenantId={}, caseId={}",
                        message.getHeaders().getId(), headerTenantId, dto.getCaseId());
            } else if (!headerTenantId.equals(dto.getTenantId())) {
                throw new ParameterException("落地限额处理结果消息租户与消息头不一致");
            }

            tranCaseCallbackService.caseWorkflowCallback(dto);
            log.info("[tran-case-callback] consume success: messageId={}, tenantId={}, caseId={}, caseNo={}, "
                            + "businessId={}, businessNo={}, elapsedMs={}",
                    message.getHeaders().getId(), dto.getTenantId(), dto.getCaseId(), dto.getCaseNo(),
                    dto.getBusinessId(), dto.getBusinessNo(), System.currentTimeMillis() - startTime);
        } catch (RuntimeException ex) {
            log.error("[tran-case-callback] consume failed: messageId={}, headerTenantId={}, payloadTenantId={}, "
                            + "caseId={}, caseNo={}, businessId={}, businessNo={}, sourceBizType={}, caseStatus={}, "
                            + "handleResult={}, elapsedMs={}",
                    message.getHeaders().getId(), headerTenantId, dto.getTenantId(), dto.getCaseId(), dto.getCaseNo(),
                    dto.getBusinessId(), dto.getBusinessNo(), dto.getSourceBizType(), dto.getCaseStatus(),
                    dto.getHandleResult(), System.currentTimeMillis() - startTime, ex);
            throw ex;
        }
    }
}
