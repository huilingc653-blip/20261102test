package com.bytter.biz.limit.dto;

import com.bytter.saas.management.exchange.api.dto.LimitHitDetailDto;
import lombok.Getter;
import lombok.Setter;

/** 批量提交限额拦截明细，补充当前业务单据的单号及表头国际化键。 */
@Getter
@Setter
public class PaymentLimitBatchDetailDto extends LimitHitDetailDto {
    private static final long serialVersionUID = 1L;

    private String businessNo;
    private String businessNoLabelKey;

    /**
     * 复制限额规则快照并关联当前业务单据。
     *
     * @param businessNoLabelKey 单号表头国际化键
     * @param businessNo 当前业务单号
     * @param source 限额规则快照
     * @return 批量限额拦截明细
     */
    public static PaymentLimitBatchDetailDto of(String businessNoLabelKey, String businessNo,
                                                 LimitHitDetailDto source) {
        PaymentLimitBatchDetailDto detail = new PaymentLimitBatchDetailDto();
        detail.setBusinessNoLabelKey(businessNoLabelKey);
        detail.setBusinessNo(businessNo);
        if (source == null) {
            return detail;
        }
        detail.setCheckLogId(source.getCheckLogId());
        detail.setTxnId(source.getTxnId());
        detail.setConditionId(source.getConditionId());
        detail.setRuleId(source.getRuleId());
        detail.setRuleCode(source.getRuleCode());
        detail.setRuleName(source.getRuleName());
        detail.setRuleScopeType(source.getRuleScopeType());
        detail.setLimitType(source.getLimitType());
        detail.setLimitMode(source.getLimitMode());
        detail.setLimitAmount(source.getLimitAmount());
        detail.setActualAmount(source.getActualAmount());
        detail.setExcessAmount(source.getExcessAmount());
        detail.setLimitCount(source.getLimitCount());
        detail.setActualCount(source.getActualCount());
        detail.setExcessCount(source.getExcessCount());
        detail.setExcessRatio(source.getExcessRatio());
        detail.setChannelCode(source.getChannelCode());
        detail.setDisplaySeq(source.getDisplaySeq());
        return detail;
    }

}
