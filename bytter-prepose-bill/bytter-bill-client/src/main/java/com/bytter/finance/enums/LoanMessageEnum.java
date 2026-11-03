package com.bytter.finance.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * 贷款管理消息提示枚举类
 * 
 * @author caozx
 */
public enum LoanMessageEnum implements IMessageEnum {
    /** 保证金占用金额不能大于可用余额! */
    FCBOND_APPLYMONEY_CHECK("01001"),
    /**
     * 贷款管理 02-03
     */
    /** 贷款合同编号已存在 */
    CMS_LOANINFO_CONTRACTCODE_EXISTS("02001"),
    /** 贷款发放合同编号已存在 */
    CMS_LOANSEND_CONTRACTCODE_EXISTS("02002"),
    /** 贷款展期合同编号已存在 */
    CMS_LOANDEFERRED_CONTRACTCODE_EXISTS("02003"),
    /** 放款金额超过贷款总金额 */
    CMS_LOANSEND_MONEY_ISMORE("02004"),
    /** 对应网点单位企业放款账号不存在 */
    CMS_ACCBOOK_1120_NOTEXISTS("02005"),
    /** 对应网点单位企业活期账号不存在 */
    CMS_ACCBOOK_2120_NOTEXISTS("02006"),
    /** 网点[{0}]利息收入-放款账号不存在 */
    CMS_ACCBOOK_501001_NOTEXISTS("02007"),
    /** 贷款发放未记账，还款无法记账，请检查参数控制 */
    CMS_LOANSEND_ISNOTTALLY("02008"),
    /** 网点[{0}]应收企业放款利息账号不存在 */
    CMS_ACCBOOK_103001_NOTEXISTS("02009"),
    /** 贷款存在审批中或合同待生成的发放记录，无法进行发放 */
    CMS_LOANSEND_FAILE("02010"),
    /** 贷款第{0}次发放记录已存在，请重新刷新页面 */
    CMS_LOANSEND_SENDTIME_EXISTS("02011"),
    /** 还款计划不能为空 */
    CMS_LOANAPPLY_PLAN_ISNULL("02013"),;
    LoanMessageEnum(String code) {
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
