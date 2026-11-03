package com.bytter.finance.enums;
import com.bytter.core.enums.IMessageEnum;
public enum CmsBondMessageEnum implements IMessageEnum {
    /** 还款计划不能为空 */
    CMS_BOND_BONDCAPITALPLAN_EMPTY("9040"),
    /** 还款日期不能为空 */
    CMS_BOND_BONDPAY_PAYDATE_EMPTY("9041"),
    /** 还款日期不能小于债券发行日期 */
    CMS_BOND_BONDPAY_PAYDATE_BEGINDATE("9042"),
    /** 计划还款日期不能为空 */
    CMS_BOND_BONDPAY_PAYPLANDATE_EMPTY("9044"),
    /** 计划还款日期不能小于起租日期 */
    CMS_BOND_BONDPAY_PAYPLANDATE_BEGINDATE("9045"),
    /** 还款本金不能为空 */
    CMS_BOND_BONDPAY_REPAYMONEY_EMPTY("9047"),
    /** 还款本金不能大于未还金额 */
    CMS_BOND_BONDPAY_REPAYMONEY_SENDBALANCE("9048"),
    /** 实付利息不能为空 */
    CMS_BOND_BONDPAY_INTEREST_EMPTY("9049"),
    /** 金额（总金额）不能为空 */
    CMS_BOND_BONDPAY_BONDMONEY_EMPTY("9050"),
    /** 收款帐号不能为空 */
    CMS_BOND_BONDPAY_ONLINEPAYMENTACC_EMPTY("9051"),
    /** 收款账户名称不能为空 */
    CMS_BOND_BONDPAY_ONLINEACCNAME_EMPTY("9052"),
    /** 收款开户行名称不能为空 */
    CMS_BOND_BONDPAY_ONLINEBANKNAME_EMPTY("9053"),
    /** 用途不能为空 */
    CMS_BOND_BONDPAY_PURPOSE_EMPTY("9054"),
    /** 该发行还有还息和还款的数据在审批中，等审批通过以后才能进行操作 */
    CMS_BOND_BONDPAY_STATUS_COMMITAPPROVE("9055"),
    /** 该笔债券还款计划未登记，不可修改 */
    CMS_BOND_BONDCAPITALPLAN_PLANISSAVEISFALSE("9056"),
    /** 该笔债券还款计划已登记，不可修改 */
    CMS_BOND_BONDCAPITALPLAN_PLANISSAVEISTRUE("9057");
    CmsBondMessageEnum(String code) {
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
