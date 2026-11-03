package com.bytter.finance.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * 委托贷款管理消息提示枚举类
 * 
 * @author lixi
 *
 */
public enum PrecativeMessageEnum implements IMessageEnum {
    MESSAGE_ERROR_VALISNOTNULL("99004"),
    /**
     * 委托贷款类型06 -07
     */
    /** 委托人不能为空 */
    CMS_PRECATIVE_PRECATIVEID_NOT_NULL("06001"),
    /** 受托人不能为空 */
    CMS_PRECATIVE_CORPID_NOT_NULL("06002"),
    /** 受托行不能为空 */
    CMS_PRECATIVE_BANKCODE_NOT_NULL("06003"),
    /** 借款人不能为空 */
    CMS_PRECATIVE_LOANOPP_NOT_NULL("06004"),
    /** 借款人和委托人不能为同一单位 */
    CMS_PRECATIVE_PRECATIVEIDLOAN_NOT_ONE("06005"),
    /** 金额不能为空 */
    CMS_PRECATIVE_MONEY_NOT_NULL("06006"),
    /** 币别不能为空 */
    CMS_PRECATIVE_CURID_NOT_NULL("06007"),
    /** 开始日期不能为空 */
    CMS_PRECATIVE_STARTDATE_NOT_NULL("06008"),
    /** 结束日期不能为空 */
    CMS_PRECATIVE_ENDDATE_NOT_NULL("06009"),
    /** 期限不能为空 */
    CMS_PRECATIVE_TERM_NOT_NULL("06010"),
    /** 手续费率不能为空 */
    CMS_PRECATIVE_POUNDAGERATE_NOT_NULL("06011"),
    /** 担保方式不能为空 */
    CMS_PRECATIVE_PLTYPE_NOT_NULL("06012"),
    /** 贷款用途不能为空 */
    CMS_PRECATIVE_PURPOSE_NOT_NULL("06013"),
    /** 担保方式不为信用时，担保说明不能为空 */
    CMS_PRECATIVE_SURETYEXPLAIN_NOT_NULL("06014"),
    /** 受托方为网点时，委托方和借款方为受托方单位 */
    CMS_PRECATIVE_NOT_SAME_CORP("06015"),
    /** 委托方和借款方为单位 */
    CMS_PRECATIVE_NOT_NET("06020"),
    /** 开始日期不能大于结束日期 */
    CMS_PRECATIVE_STARTDATEENDDATE_MORE("06016"),
    /** 基准利率不能为空，请先定义 */
    CMS_PRECATIVE_REFERENCERATETYPE_ISNULL("06017"),
    /** 贷款周期不能为空，请先定义 */
    CMS_PRECATIVE_REPAYCYCLE_ISNULL("06018"),
    /** 点数或者比例不能为空，请先定义 */
    CMS_PRECATIVE_POINTSPROPORTIONSELECT_ISNULL("06019"),
    /** 贷款合同号不能为空 */
    CMS_PRECATIVE_CONTRACTCODE_NOT_NULL("06035"),
    /** 还款方式不能为空 */
    CMS_PRECATIVE_REPAYTYPE_NOT_NULL("06036"),
    /** 计息方式不能为空 */
    CMS_PRECATIVE_INTERESTWAY_NOT_NULL("06037"),
    /** 发放方式不能为空 */
    CMS_PRECATIVE_LOANSENDOUTTYPE_NOT_NULL("06038"),
    /** 浮动方式不为固定利率时利率调整生效日期不能为空 */
    CMS_PRECATIVE_RATEENTRIESEFFECTDATE_NOT_NULL("06039"),
    /** 贷款利率不能为空 */
    CMS_PRECATIVE_RATE_NOT_NULL("06040"),
    /** 没有满足条件的贷款利率 */
    CMS_PRECATIVE_NO_RATE("06041"),
    /** 发放金额超过贷款总金额 */
    CMS_PRECATIVE_MONEY_ISMORE("06042"),
    /** 执行金额必须等于贷款金额！ */
    CMS_PRECATIVE_DETAILMONEY_EQ_LOANMONEY("06043"),
    /** 一次性发放时执行金额必须等于贷款金额！ */
    CMS_PRECATIVE_DETAILMONEY_EQ_LOANMONEY_WHEN_ONELOAN("06048"),
    /** 分次性发放时执行金额必须小于或等于贷款金额！ */
    CMS_PRECATIVE_DETAILMONEY_EQ_LOANMONEY_WHEN_REPEATEDLYLOAN("06049"),
    /** 执行金额必须等于归还利息 */
    CMS_PRECATIVE_DETAILMONEY_EQ_REPAYACCRUAL("06044"),
    /** 执行金额必须等于归还金额及所有利息 */
    CMS_PRECATIVE_DETAILMONEY_EQ_REPAYMONEY_AND_ACCRUAL("06045"),
    /** 执行金额必须等于归还金额及归还金额利息 */
    CMS_PRECATIVE_DETAILMONEY_EQ_REPAYMONEY_AND_REPAYACCRUAL("06046"),
    /** 执行金额必须等于归还金额 */
    CMS_PRECATIVE_DETAILMONEY_EQ_REPAYMONEY("06047"),
    /** 该委托贷款存在业务发放记录 */
    CMS_PRECATIVE_EXISTS_SEND("06051"),
    /** 发放编号重复 */
    CMS_PRECATIVE_SEND_CONTRACTCODE_NOT_UNIQUE("06052"),
    /** 发放金额不能为空 */
    CMS_PRECATIVE_SEND_MONEY_NOT_NULL("06053"),
    /** 发放日期不能为空 */
    CMS_PRECATIVE_SENDDATE_NOT_NULL("06054"),
    /** 到期日期不能为空 */
    CMS_PRECATIVE_DEADLINEDATE_NOT_NULL("06055"),
    /** 发放日期不能大于到期日期 */
    CMS_PRECATIVE_SENDATE_PASS_DEADLINEDATE("06056"),
    /** 发放日期不能小于开始日期 */
    CMS_PRECATIVE_SENDATE_NOTLESS_STARTDATE("06057"),
    /** 归还日期要大于发放日期 */
    CMS_PRECATIVE_REPAYDATE_PASS_SENDDATE("06058"),
    /** 归还日期不能为空 */
    CMS_PRECATIVE_REPAYDATE_NOT_NULL("06063"),
    /** 到期日期不能大于委托贷款结束日期 */
    CMS_PRECATIVE_SENDDATE_PASS_DEADLINEDETE("06059"),
    /** 到期日期应大于等于结束日期 */
    CMS_PRECATIVE_ENDDATE_PASS_DEADLINEDATE("06060"),
    /** 委托贷款存在审批中的发放记录，无法进行发放 */
    CMS_PRECATIVE_FAILE("06061"),
    /** 委托贷款第{0}次发放记录已存在，请重新刷新页面 */
    CMS_PRECATIVE_SENDTIME_EXISTS("06062"),
    /** 展期开始日期不能为空 */
    CMS_PRECATIVE_DEFERREDSTARTDATE_NOT_EMPTY("07000"),
    /** 展期到期日不能为空 */
    CMS_PRECATIVE_DEFERREDREPAYDATE_NOT_EMPTY("07001"),
    /** 到期处理类型不能为空 */
    CMS_PRECATIVE_EXPIRYHANDLETYPE_NOT_EMPTY("07002"),
    /** 展期期限不能为空 */
    CMS_PRECATIVE_DEFERREDLIMT_NOT_EMPTY("07003"),
    /** 展期利率不能为空 */
    CMS_PRECATIVE_DEFERREDRATE_NOT_EMPTY("07004"),
    /** 展期合同号不能为空 */
    CMS_PRECATIVE_CONTRACTCODE_NOT_EMPTY("07005"),
    /** 展期合同号不能重复 */
    CMS_PRECATIVE_CONTRACTCODE_NOT_UNIQUE("07006"),
    /** 展期到期日期必须大于展期开始日期 */
    CMS_PRECATIVE_DEFERRED_NOT_UNIQUE("07007"),
    /** 展期开始日期必须大于贷款（发放）到期日 */
    CMS_PRECATIVE_DEFERRED_NOT_UNIQUE_DATE("07008"),
    /** 展期到期日期必须大于上次展期到期日期 */
    CMS_PRECATIVE_DEFERRED_PASS_LASTDEFERREDDATE("07009"),
    /** 企业活期帐号为空，请先开户 */
    CMS_PRECATIVE_FCACC_IS_NULL("07040"),
    /** 业务规则定义里的组号不能为空 */
    CMS_PRECATIVE_GROUPNUM_IS_NULL("07041"),
    /** 业务规则定义里的科目不能为空 */
    CMS_PRECATIVE_SUBJECT_IS_NULL("07042"),
    /** 网点日结日期不能大于发放日期，请反日结 */
    CMS_PRECATIVE_SENDDATE_PASS_CLOSEDATE("07080"),
    /** 委托人无企业活期账户，请与结算中心联系。 */
    CMS_PRECATIVE_CURRENT_IS_NULL("07081"),
    /** 借款人无企业活期账户，请与结算中心联系 */
    CMS_LOANOPP_CURRENT_IS_NULL("07082"),
    /** 结息帐号为空，请先开户 */
    CMS_LOANOPP_REPAYACCRUAL_FCACC_IS_NULL("07083"),
    /** 冲预提账户不能为空,请先开户 */
    CMS_PRECATIVE_PREDRAW_IS_NULL("07084"),
    /** 记账数据错误 */
    CMS_PRECATIVE_TALLYDATA_ERROR("07085"),
    /** 还款计划不能为空 */
    CMS_PRECATIVE_PLANID_IS_NULL("07086"),;
    PrecativeMessageEnum(String code) {
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
