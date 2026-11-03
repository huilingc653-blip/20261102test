package com.bytter.biz.limit.constant;

import com.bytter.biz.limit.service.EnterpriseLimitControlService;

import java.util.HashMap;
import java.util.Map;

/**
 * 企业端支付限额/落地限额 业务类型开关（代码级，无 DB 配置）。
 *
 * <p>维护各来源业务类型（sourceBizType，对齐 {@link EnterpriseLimitControlService#BIZ_TYPE_NIS_BILLHEAD}
 * 等常量）是否受「支付限额 + 落地限额」控制。需要调整哪些业务走/不走限额时，只改
 * {@link #LIMIT_ENABLED} 的映射即可，业务代码无需改动。</p>
 *
 * <p>缺省（未在 map 中显式声明）视为「启用限额」，保持与历史行为一致。</p>
 *
 * @author wuxc
 */
public final class EnterpriseLimitBizTypeSwitch {

    /** 来源业务类型 -> 是否受支付限额/落地限额控制（true=受控，false=跳过）。 */
    private static final Map<String, Boolean> LIMIT_ENABLED = new HashMap<>();

    static {
        // 付款办理
        LIMIT_ENABLED.put(EnterpriseLimitControlService.BIZ_TYPE_NIS_BILLHEAD, Boolean.TRUE);
        // @todo 资金收付（收付交易）——汇率无法实时同步，暂取消支付限额与落地限额
        LIMIT_ENABLED.put(EnterpriseLimitControlService.BIZ_TYPE_FEX_CASH_FLOW, Boolean.FALSE);
        // 代发工资
        LIMIT_ENABLED.put(EnterpriseLimitControlService.BIZ_TYPE_NIS_WAGE, Boolean.TRUE);
        // 费用报销
        LIMIT_ENABLED.put(EnterpriseLimitControlService.BIZ_TYPE_NIS_EXPENSE_DETAIL, Boolean.TRUE);
    }

    private EnterpriseLimitBizTypeSwitch() {
    }

    /**
     * 指定来源业务是否受支付限额/落地限额控制。
     *
     * @param sourceBizType 来源业务类型，见 {@link EnterpriseLimitControlService} 中 BIZ_TYPE_XXX 常量
     * @return true 受控；false 跳过限额控制。未显式配置的类型默认受控（true）。
     */
    public static boolean isLimitEnabled(String sourceBizType) {
        Boolean enabled = LIMIT_ENABLED.get(sourceBizType);
        return enabled == null || enabled;
    }
}
