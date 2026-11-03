package com.bytter.finance.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * 银行借款消息提示枚举类
 */
public enum ProvideMessageEnum implements IMessageEnum {
    /**
     * 银行借款04 -05
     */
    /** 单位不能为空 */
    CMS_PROVIDE_LOANINFO_CORPEMPTY("04001"),
    /** 借款种类不能为空 */
    CMS_PROVIDE_LOANINFO_PLCLASSEMPTY("04003"),
    /** 借款银行类别不能为空 */
    CMS_PROVIDE_LOANINFO_BANKTYPEEMPTY("04005"),
    /** 借款银行不能为空 */
    CMS_PROVIDE_LOANINFO_BANKIDEMPTY("04007"),
    /** 金额不能为空 */
    CMS_PROVIDE_LOANINFO_PLMONEYEMPTY("04009"),
    /** 借款币种不能为空 */
    CMS_PROVIDE_LOANINFO_CURIDEMPTY("04011"),
    /** 借款期限不能为空 */
    CMS_PROVIDE_LOANINFO_PLTERMEMPTY("04013"),
    /** 借款日期不能大于还款日期 */
    CMS_PROVIDE_LOANINFO_PLDATELEREPAYDATE("04015"),
    /** 还款日期不能小于发放日期*/
    CMS_PROVIDE_LOANINFO_PAYDATELEREPLDATE("04016"),
    /** 还款方式不能为空 */
    CMS_PROVIDE_LOANINFO_REPAYTYPEEMPTY("04017"),
    /** 合同期限不能为空 */
    CMS_PROVIDE_LOANINFO_CONTRACTTERMEMPTY("04019"),
    /** 浮动方式不能为空 */
    CMS_PROVIDE_LOANINFO_FLOATWAYEMPTY("04021"),
    /** 非固定利率情况下利率调整生效日期不能为空 */
    CMS_PROVIDE_LOANINFO_EFFECTDATEEMPTY("04023"),
    /** 借款利率不能为空 */
    CMS_PROVIDE_LOANINFO_PlRATEEMPTY("04024"),
    /** 借款用途不能为空 */
    CMS_PROVIDE_LOANINFO_PLPURPOSEEMPTY("04025"),
    /** 借款用途字符长度不能大于40 */
    CMS_PROVIDE_LOANINFO_PURPOSEEMPTY_LENGTH("04026"),
    /** 担保主业务类型不能为空 */
    CMS_PROVIDE_LOANINFO_MAINPLTYPEEMPTY("04027"),
    /** 担保方式不能为空 */
    CMS_PROVIDE_LOANINFO_PLTYPEEMPTY("04029"),
    /** 借款日期不能为空 */
    CMS_PROVIDE_LOANINFO_PLDATEEMPTY("04031"),
    /** 还款日期不能为空 */
    CMS_PROVIDE_LOANINFO_REPAYDATEEMPTY("04033"),
    /** 合同号不能为空 */
    CMS_PROVIDE_LOANINFO_CONTRACTNOEMPTY("04035"),
    /** 合同号重复 */
    CMS_PROVIDE_LOANINFO_CONTRACTNOREPEAT("04036"),
    /** 逾期利率不能为空 */
    CMS_PROVIDE_LOANINFO_OVERDUERATEEMPTY("04037"),
    /** 计提方式不能为空 */
    CMS_PROVIDE_LOANINFO_ACCRUALTYPEEMPTY("04038"),
    /** 借款发放编码不能为空 */
    CMS_PROVIDE_LOANSEND_CONTRACTNOEMPTY("04039"),
    /** 借款发放编码不能重复 */
    CMS_PROVIDE_LOANSEND_CONTRACTCODEREPEAT("04040"),
    /** 发放金额不能为0 */
    CMS_PROVIDE_LOANSEND_SENDMONEYZERO("04041"),
    /** 发放日期不能为空 */
    CMS_PROVIDE_LOANSEND_SENDDATEEMPTY("04043"),
    /** 到期日期不能为空 */
    CMS_PROVIDE_LOANSEND_REPAYDATEEMPTY("04045"),
    /** 发放日期不能大于到期日期 */
    CMS_PROVIDE_LOANSEND_SENDDATELEREPAYDATE("04046"),
    /** 计息方式不能为空 */
    CMS_PROVIDE_LOANSEND_INTERESTWAYEMPTY("04047"),
    /** 还款计划不能为空 */
    CMS_PROVIDE_LOANSEND_REPAYMENTSCHEDULEMPTY("04048"),
    /** 还款计划需要重新保存 */
    CMS_PROVIDE_LOANSEND_REPAYMENTSCHEDULRESAVE("04049"),
    /** 到期处理类型不能为空 */
    CMS_PROVIDE_DEFERRED_EXPIRYHANDLETYPEEMPTY("04051"),
    /** 展期到期日不能为空 */
    CMS_PROVIDE_DEFERRED_DEFERREDREPAYDATEEMPTY("04053"),
    /** 展期期限不能为空 */
    CMS_PROVIDE_DEFERRED_DEFERREDLIMTEMPTY("04055"),
    /** 展期利率不能为空 */
    CMS_PROVIDE_DEFERRED_DEFERREDRATEEMPTY("04057"),
    /** 展期合同号不能为空 */
    CMS_PROVIDE_DEFERRED_CONTRACTCODEEMPTY("04059"),
    /** 展期合同号不能重复 */
    CMS_PROVIDE_DEFERRED_CONTRACTCODE_NOT_UNIQUE("04061"),
    /** 展期开始日期不能为空 */
    CMS_PRECATIVE_DEFERRED_DEFERREDSTARTDATEEMPTY("04063"),
    /** 展期开始日期必须大于贷款（发放）到期日期！ */
    CMS_PROVIDE_DEFERRED_NOT_UNIQUE("07007"),
    /** 还款金额不能为空 */
    CMS_PROVIDE_LOANREPAY_REPAYMONEYEMPTY("04071"),
    /** 还款金额不能大于借款余额 */
    CMS_PROVIDE_LOANREPAY_REPAYMONEYLEPLBALANCE("04073"),
    /** 备注不能为空 */
    CMS_PROVIDE_LOANREPAY_RMKEMPTY("04074"),
    /** 还款利息不能为空 */
    CMS_PROVIDE_LOANREPAY_REPAYACCRUALEMPTY("04075"),
    /** 实付利息不能为空 */
    CMS_PROVIDE_LOANREPAY_ONLINEREALITYINTERESTEMPTY("04076"),
    /** 金额不能为空 */
    CMS_PROVIDE_LOANREPAY_ONLINEMONEYEMPTY("04076"),
    /** 收款帐号不能为空 */
    CMS_PROVIDE_LOANREPAY_ONLINEPAYMENTACCEMPTY("04076"),
    /** 收款账户名称不能为空 */
    CMS_PROVIDE_LOANREPAY_ONLINEACCNAMEEMPTY("04076"),
    /** 收款开户行名称不能为空 */
    CMS_PROVIDE_LOANREPAY_ONLINEBANKNAMEEMPTY("04076"),
    /** 银行活期账号号为空，请先开户 */
    CMS_PROVIDE_FCACC_IS_NULL("04083"),
    /** 业务规则定义里的组号不能为空 */
    CMS_PROVIDE_GROUPNUM_IS_NULL("04084"),
    /** 业务规则定义里的科目不能为空 */
    CMS_PROVIDE_SUBJECT_IS_NULL("04085"),
    /** 结算中心未开设利息支出-银行借款 */
    CMS_PROVIDE_LOANACC_IS_NULL("04086"),
    /** 结算中心未开设 其他应付款 */
    CMS_PROVIDE_OTHERACC_IS_NULL("04087"),
    /** 结算中心未开设 */
    CMS_ABBILL_FCACC_IS_NULL("04088"),
    /** 银行账户未关联内部账户 */
    CMS_BANKACC_RELATION_BLNCTRACC_IS_NULL("04089"),
    /** 牵头行只能有一个，不能有多个！ */
    CMS_CMSLOANINFO_LEADINGBANK_UNIQUE("05001");
    ProvideMessageEnum(String code) {
        this.code = code;
    }
    private String code;
    public void setCode(String code) {
        this.code = code;
    }
    @SuppressWarnings("rawtypes")
    @Override
    public Enum getEnum() {
        return this;
    }
    @Override
    public String getCode() {
        return code;
    }
}
