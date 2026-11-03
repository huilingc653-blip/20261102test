package com.bytter.biz.limit.enums;

import com.bytter.core.enums.IMessageEnum;

/**
 * 企业端银行限额异常消息枚举。
 *
 * 枚举名会由 LocalMessageService 转换为国际化 key，请与
 * sys_multi_lang.LANG_KEY 保持一致。
 */
public enum EnterpriseLimitMessageEnum implements IMessageEnum {

    /** 付款限额校验上下文不完整 */
    LIMIT_CONTROL_CONTEXT_INCOMPLETE("08001"),
    /** 付款限额台账来源标识不完整 */
    LIMIT_CONTROL_SOURCE_IDENTITY_INCOMPLETE("08002"),
    /** 当前租户未维护签约银行机构 */
    LIMIT_CONTROL_TENANT_RELATION_MISSING("08003"),
    /** 付款超出银行限额规则 */
    LIMIT_CONTROL_AMOUNT_EXCEEDS_RULE("08004"),
    /** 限额币别不存在 */
    LIMIT_CONTROL_LIMIT_CURRENCY_NOT_EXIST("08005"),
    /** 付款币别到限额币别的折算汇率不存在 */
    LIMIT_CONTROL_EXCHANGE_RATE_NOT_EXIST("08006"),
    /** 不支持的限额方式 */
    LIMIT_CONTROL_UNSUPPORTED_LIMIT_MODE("08007"),
    /** 报销成功金额或付款信息与限额占用快照不一致 */
    LIMIT_CONTROL_RETAIN_SNAPSHOT_MISMATCH("08008"),
    /** 银行限额台账未记录企业端申请人 */
    LIMIT_CONTROL_TXN_APPLICANT_MISSING("08009"),
    /** 银行限额台账申请人不存在 */
    LIMIT_CONTROL_TXN_APPLICANT_NOT_EXIST("08010"),
    /** 银行限额台账申请人与回调租户不一致 */
    LIMIT_CONTROL_TXN_APPLICANT_TENANT_MISMATCH("08011"),
    /** 不支持的银行限额回调业务类型 */
    LIMIT_CONTROL_UNSUPPORTED_CALLBACK_BIZ_TYPE("08012"),
    /** 未找到当前业务单据的有效限额占用 */
    LIMIT_CONTROL_CALLBACK_ACTIVE_TXN_NOT_FOUND("08013"),
    /** 银行限额审批结果属于旧付款尝试 */
    LIMIT_CONTROL_CALLBACK_STALE_ATTEMPT("08014"),
    /** 银行限额审批回调机构与付款限额台账不一致 */
    LIMIT_CONTROL_CALLBACK_BANK_ORG_MISMATCH("08015"),
    /** 未找到当前付款尝试对应的银行限额审批案例 */
    LIMIT_CONTROL_CALLBACK_CASE_NOT_FOUND("08016"),
    /** 银行限额审批回调案例号与企业端不一致 */
    LIMIT_CONTROL_CALLBACK_CASE_NO_MISMATCH("08017"),
    /** 银行限额审批案例已有不同的最终处理结果 */
    LIMIT_CONTROL_CALLBACK_RESULT_CONFLICT("08018"),
    /** 银行限额审批回调参数不完整 */
    LIMIT_CONTROL_CALLBACK_PARAM_INVALID("08019"),
    /** 银行限额审批结果仅支持A同意或R拒绝 */
    LIMIT_CONTROL_CALLBACK_UNSUPPORTED_RESULT("08020"),
    /** 银行限额回调eventId已被其他审批结果使用 */
    LIMIT_CONTROL_CALLBACK_EVENT_CONFLICT("08021"),
    /** 付款单状态已变化，不能续办银行限额审批结果 */
    LIMIT_CONTROL_NIS_CONTINUE_STATUS_CHANGED("08022"),
    /** 付款单结算方式不支持银行限额审批后续办指令 */
    LIMIT_CONTROL_NIS_UNSUPPORTED_SETTLE_TYPE("08023"),
    /** 付款单状态已变化，不能处理银行限额审批拒绝结果 */
    LIMIT_CONTROL_NIS_REJECT_STATUS_CHANGED("08024"),
    /** 境外付款单状态已变化，不能续办银行限额审批结果 */
    LIMIT_CONTROL_FEX_CONTINUE_STATUS_CHANGED("08025"),
    /** 境外付款单状态已变化，不能处理银行限额审批拒绝结果 */
    LIMIT_CONTROL_FEX_REJECT_STATUS_CHANGED("08026"),
    /** 境外付款限额校验无法取得当前操作人 */
    LIMIT_CONTROL_FEX_OPERATOR_MISSING("08027"),
    /** 境外付款限额校验无法取得币别英文编码 */
    LIMIT_CONTROL_FEX_CURRENCY_CODE_MISSING("08028"),
    /** 付款限额校验无法取得当前操作人 */
    LIMIT_CONTROL_NIS_OPERATOR_MISSING("08029"),
    /** 付款限额校验无法取得币别英文编码 */
    LIMIT_CONTROL_NIS_CURRENCY_CODE_MISSING("08030"),
    /** 自动付款限额校验无法取得付款单申请人 */
    LIMIT_CONTROL_AUTO_PAY_APPLICANT_MISSING("08031"),
    /** 费用报销存在等待银行结果的明细 */
    LIMIT_CONTROL_EXPENSE_WAIT_RESULT("08032"),
    /** 报销成功明细金额超过汇总付款金额 */
    LIMIT_CONTROL_EXPENSE_SUCCESS_AMOUNT_EXCEEDS("08033");

    private final String code;

    EnterpriseLimitMessageEnum(String code) {
        this.code = code;
    }

    /**
     * 根据内管限额业务码查找企业端消息枚举，未知业务码返回null并沿用原始错误信息。
     */
    public static EnterpriseLimitMessageEnum fromCode(String code) {
        for (EnterpriseLimitMessageEnum messageEnum : values()) {
            if (messageEnum.code.equals(code)) {
                return messageEnum;
            }
        }
        return null;
    }

    @Override
    public Enum getEnum() {
        return this;
    }

    @Override
    public String getCode() {
        return code;
    }
}
