package com.bytter.biz.foreign.service.impl;

import cn.hutool.json.JSONUtil;
import com.bytter.biz.payment.api.dto.TranCaseCallbackDTO;
import com.bytter.biz.payment.control.ITranCaseCallbackHandler;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 落地限额处理单（BT_TR_LIMIT_CASE）结果消息处理服务：还原租户后按 sourceBizType
 * 分发到各来源 {@link ITranCaseCallbackHandler}（资金收付/付款办理/代发工资）。
 *
 * @author zhukr
 */
@Service("tranCaseCallbackServiceImpl")
@Slf4j
public class TranCaseCallbackServiceImpl {

    @Autowired
    private List<ITranCaseCallbackHandler> tranCaseCallbackHandlers;

    /**
     * 校验并处理内管发送的落地限额结果消息。
     *
     * @param dto 落地限额处理结果
     */
    public void caseWorkflowCallback(TranCaseCallbackDTO dto) {
        log.info("[tran-case-callback] dispatch start: tenantId={}, caseId={}, caseNo={}, businessId={}, "
                        + "businessNo={}, sourceBizType={}, data={}",
                dto.getTenantId(), dto.getCaseId(), dto.getCaseNo(), dto.getBusinessId(), dto.getBusinessNo(),
                dto.getSourceBizType(), JSONUtil.toJsonStr(dto));
        if (dto == null || StringUtils.isBlank(dto.getBusinessId()) && StringUtils.isBlank(dto.getBusinessNo())) {
            log.error("[tran-case-callback] business validation failed: dto={}, reason=missing businessId/businessNo",
                    dto);
            throw new ParameterException("落地限额处理单回调缺少业务对应ID/业务单号");
        }
        // 内管端操作员非企业端身份，消费前清空线程可能残留的用户上下文
        UserContextHelper.helper().removeUser();
        // 签约客户ID = 企业端租户ID，按回调携带的租户还原，避免线程残留租户导致查不到单据
        boolean tenantRestored = StringUtils.isNotBlank(dto.getTenantId());
        if (tenantRestored) {
            UserContextHelper.helper().setTTLTenant(dto.getTenantId());
        }
        try {
            // 按来源业务类型分发（必传，不提供默认值）
            String sourceBizType = dto.getSourceBizType();
            if (StringUtils.isBlank(sourceBizType)) {
                log.error("[tran-case-callback] business validation failed: tenantId={}, caseId={}, caseNo={}, "
                                + "businessId={}, businessNo={}, reason=missing sourceBizType",
                        dto.getTenantId(), dto.getCaseId(), dto.getCaseNo(), dto.getBusinessId(), dto.getBusinessNo());
                throw new ParameterException("落地限额处理单回调缺少来源业务类型 sourceBizType");
            }
            ITranCaseCallbackHandler handler = tranCaseCallbackHandlers.stream()
                    .filter(h -> sourceBizType.equals(h.sourceBizType()))
                    .findFirst()
                    .orElse(null);
            if (handler == null) {
                log.error("[tran-case-callback] handler not found: tenantId={}, caseId={}, caseNo={}, "
                                + "businessId={}, businessNo={}, sourceBizType={}",
                        dto.getTenantId(), dto.getCaseId(), dto.getCaseNo(), dto.getBusinessId(), dto.getBusinessNo(),
                        sourceBizType);
                throw new ParameterException("暂不支持的落地限额来源业务类型: " + sourceBizType);
            }
            log.info("[tran-case-callback] dispatch start: tenantId={}, caseId={}, caseNo={}, businessId={}, "
                            + "businessNo={}, sourceBizType={}, handler={}",
                    dto.getTenantId(), dto.getCaseId(), dto.getCaseNo(), dto.getBusinessId(), dto.getBusinessNo(),
                    sourceBizType, handler.getClass().getSimpleName());
            handler.handle(dto);
            log.info("[tran-case-callback] dispatch success: tenantId={}, caseId={}, caseNo={}, businessId={}, "
                            + "businessNo={}, sourceBizType={}, handler={}",
                    dto.getTenantId(), dto.getCaseId(), dto.getCaseNo(), dto.getBusinessId(), dto.getBusinessNo(),
                    sourceBizType, handler.getClass().getSimpleName());
        } finally {
            // 防止 TTL 租户残留到 MQ 消费线程的后续消息
            if (tenantRestored) {
                 UserContextHelper.helper().clearTTLTenant();
            }
        }
    }
}
