package com.bytter.guarantee.domain.vo;

import com.bytter.guarantee.dto.RepayPlanDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
@SuppressWarnings("serial")
public class RepayPlanVO implements Serializable {
    /** 贷款申请id */
    private String id;
    /** 贷款申请单号 */
    private String billCode;
    /** 还款开始日 */
    private String repayBeginDate;
    /** 还款周期 */
    private String repayCycel;
    /** 利率 */
    private BigDecimal loanRate;
    /** 发放日期 */
    private String loanDate;
    /** 还款日期 */
    private String repayDate;
    /** 还款方式 */
    private String repayType;
    /** 发放金额 */
    private BigDecimal loanMoney;
    /** 明细 */
    private List<RepayPlanDTO> repayMents;
    /** 还款计划主表ids */
    private String planId;
    /** 利息测算基数 */
    private String rateBase;
    /**
     * A1 -内部贷款录入 A2 -- 内部贷款发放 B1 -银行借款录入 B2 -- 银行借款发放
     */
    private String internalSign;
    /** 类型：0：借款 1：贷款 2：委托贷款 */
    private String type;
    /** 展期到期日期 */
    private String deferredRepayDate;
    /** 展期利率 */
    private BigDecimal zqRate;
    /** 借款发放中传的类型参数 0:新增 1:修改 */
    private String provideSendType;
    /** 已还利息 */
    private BigDecimal repayAccrual;
    private String tallySign;
    private String repayway;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public String getInternalSign() {
        return internalSign;
    }
    public void setInternalSign(String internalSign) {
        this.internalSign = internalSign;
    }
    public String getBillCode() {
        return billCode;
    }
    public String getRateBase() {
        return rateBase;
    }
    public void setRateBase(String rateBase) {
        this.rateBase = rateBase;
    }
    public List<RepayPlanDTO> getRepayMents() {
        return repayMents;
    }
    public void setRepayMents(List<RepayPlanDTO> repayMents) {
        this.repayMents = repayMents;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public BigDecimal getLoanRate() {
        return loanRate;
    }
    public void setLoanRate(BigDecimal loanRate) {
        this.loanRate = loanRate;
    }
    public String getLoanDate() {
        return loanDate;
    }
    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }
    public String getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(String repayDate) {
        this.repayDate = repayDate;
    }
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    public BigDecimal getLoanMoney() {
        return loanMoney;
    }
    public void setLoanMoney(BigDecimal loanMoney) {
        this.loanMoney = loanMoney;
    }
    public String getRepayCycel() {
        return repayCycel;
    }
    public void setRepayCycel(String repayCycel) {
        this.repayCycel = repayCycel;
    }
    public String getRepayBeginDate() {
        return repayBeginDate;
    }
    public void setRepayBeginDate(String repayBeginDate) {
        this.repayBeginDate = repayBeginDate;
    }
    public String getPlanId() {
        return planId;
    }
    public void setPlanId(String planId) {
        this.planId = planId;
    }
    public String getDeferredRepayDate() {
        return deferredRepayDate;
    }
    public void setDeferredRepayDate(String deferredReplayDate) {
        this.deferredRepayDate = deferredReplayDate;
    }
    public BigDecimal getZqRate() {
        return zqRate;
    }
    public void setZqRate(BigDecimal zqRate) {
        this.zqRate = zqRate;
    }
    public String getProvideSendType() {
        return provideSendType;
    }
    public void setProvideSendType(String provideSendType) {
        this.provideSendType = provideSendType;
    }
    public BigDecimal getRepayAccrual() {
        return repayAccrual;
    }
    public void setRepayAccrual(BigDecimal repayAccrual) {
        this.repayAccrual = repayAccrual;
    }
    public String getTallySign() {
        return tallySign;
    }
    public void setTallySign(String tallySign) {
        this.tallySign = tallySign;
    }
    public String getRepayway() {
        return repayway;
    }
    public void setRepayway(String repayway) {
        this.repayway = repayway;
    }
    @Override
    public String toString() {
        return "RepayPlanVO [billCode=" + billCode + ", repayBeginDate=" + repayBeginDate + ", repayCycel=" + repayCycel
                + ", rateType=" + ", loanRate=" + loanRate + ", loanDate=" + loanDate + ", repayDate=" + repayDate
                + ", repayType=" + repayType + ", loanMoney=" + loanMoney + ", repayMents=" + repayMents
                + ", internalSign=" + internalSign + "]";
    }
}
