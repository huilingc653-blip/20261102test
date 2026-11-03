package com.bytter.biz.finacial.api.enums.bankCollect;

import com.bytter.core.enums.IMessageEnum;

/**
 * 电子交易网络记账流程枚举
 *
 * @author lix
 * @since 2017-7-6
 */
public enum CollectMessageEnum implements IMessageEnum {
    /**
     * 07 电子交易网络记账
     *
     */
    /**
     * 查询记账对象出错
     */
    FIN_COLLECT_QUERY_BTNETCHALK_NULL("07001"),

    /**
     * 未找到对应凭证信息,请刷新重试
     */
    FIN_COLLECT_QUERY_ACCBOOK_NULL("07002"),

    /**
     * 未找到对应分录信息
     */
    FIN_COLLECT_QUERY_ACCBOOKDTL_NULL("07003"),

    /**
     * 业务数据异常！请重新申请
     */
    FIN_COLLECT_DATA_ERROR("07004"),

    /**
     * 请先维护银行账号与内部账号的关系
     */
    FIN_COLLECT_RELATION_BANKACC_FCACC("07005"),

    /**
     * 内部账号数据不存在,请检查！
     */
    FIN_COLLECT_FCACC_DATE_NOT_EXIST("07006"),

    /**
     * 银行账号为空，请重新申请
     */
    FIN_COLLECT_BANKACC_ISNULL("07007"),

    /**
     * 银行账号为空，请重新申请
     */
    FIN_COLLECT_PRECATIONACC_FIRST("07008"),

    /**
     * {sysCode}科目手工账规则为空，请先定义
     */
    FIN_COLLECT_MANUAL_FINACIAL_TYPE_ISNULL("07009"),

    /**
     * 编号nisBillhead.getBillNo()的付款指令不存在,请检查数据
     */
    FIN_COLLECT_MANUAL_BILLNO_ISNULL("07011"),

    /**
     * 付款账号的父账号为空
     */
    FIN_COLLECT_MANUAL_PARENTACCOUNT_ISNULL("07012"),

    /**
     * 内部账号为空
     */
    FIN_COLLECT_MANUAL_PAYACCOUNT_ISNULL("07013"),

    /**
     * 为找到对应下拨账号，请检查
     */
    FIN_COLLECT_MANUAL_DOWNACC_ISNULL("07014"),

    /**
     * 单据已复核通过,请刷新页面
     */
    FIN_COLLECT_MANUAL_ISPASS("07015"),

    /**
     * 单据已做打回处理,请刷新页面
     */
    FIN_COLLECT_MANUAL_ISBACK("07016"),

    /**
     * 单据状态已发生改变,请刷新页面
     */
    FIN_COLLECT_MANUAL_STATUSCHANGE("07017"),

    /**
     * 持票单位所在网点未开设xx账户
     */
    FIN_COLLECT_MANUAL_ABBILL_ACCOUNT_ISNULL("07018"),
    /*请先维护银行账号[{0}]与上级内部账号的关系*/
    FIN_COLLECT_MANUAL_SUPERCURRENTACC_ISNULL("07019"),

    /**
     * 银行活期对应的银行账号为空，请先维护
     */
    FIN_COLLECT_MANUAL_BANKACC_ISNULL("07020"),

    /**
     * 未找到凭证种类，请先维护
     */
    FIN_COLLECT_MANUAL_BUSRACE_ISNULL("07021"),

    /**
     * {0}未开设{0}账户,请先维护
     */
    FIN_COLLECT_MANUAL_ACCOUNT_ISNULL("07022"),

    /**
     * {0}未开设{0}银行代码为{0}账户,请先维护
     */
    FIN_COLLECT_MANUAL_ACCOUNT_BY_BANKTYPE_ISNULL("07023"),

    /**
     * 请至少选择两条数据
     */
    NETBANK_MESSAGE_CONFIRM_SELECTDATA("07024"),

    /**
     * 未找到会计科目,请先初始化!
     */
    FIN_MESSAGE_SUBJECT_IS_NULL("07025"),

    /**
     * 结算中心必须一致
     */
    FIN_COLLECT_NETID_NOTSAME("07026"),

    /**
     * 必须是同一个月内的交易流水
     */
    FIN_COLLECT_TRANSTIME_NOTSAME("07027"),

    /**
     * 必须是同一币种
     */
    FIN_COLLECT_CURID_NOTSAME("07028"),

    /**
     * 必须是同一本方账号
     */
    FIN_COLLECT_BANKACC_NOTSAME("07029"),

    /**
     * 必须是同一交易日期
     */
    FIN_COLLECT_TRANSDATE_NOTSAME("07030");

    CollectMessageEnum(String code) {
        this.code = code;
    }

    private String code;

    public void setCode(String code) {
        this.code = code;
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
