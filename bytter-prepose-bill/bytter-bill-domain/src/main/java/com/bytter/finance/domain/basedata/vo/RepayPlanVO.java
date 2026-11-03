package com.bytter.finance.domain.basedata.vo;
import com.bytter.finance.domain.provide.entity.CmsProvideInterestTemp;
import com.bytter.guarantee.dto.RepayPlanDTO;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
@SuppressWarnings("serial")
@ToString
@EqualsAndHashCode
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
    /** 标志 */
    private String repairSign;
    /**
     * 显示隐藏标识
     */
    private String showSign;
    /**
     * 借款利息预计明细ID 用来查询还款日期标记。通过还款日期标记查询这个日期内的全部利息明细
     */
    private String interestId;
    /**
     * 发送表id
     */
    private String sendId;
    /**
     * 发放编号
     */
    private String sendBillCode;

    /**
     * 预计利息明细
     */
    private List<CmsProvideInterestTemp> interestVoList;

    /**
     * 提前还款金额
     */
    private BigDecimal repayMoney;

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

    public String getRepairSign() {
        return repairSign;
    }

    public void setRepairSign(String repairSign) {
        this.repairSign = repairSign;
    }

    public String getShowSign() {
        return showSign;
    }

    public void setShowSign(String showSign) {
        this.showSign = showSign;
    }

    public String getInterestId() {
        return interestId;
    }

    public void setInterestId(String interestId) {
        this.interestId = interestId;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getSendBillCode() {
        return sendBillCode;
    }

    public void setSendBillCode(String sendBillCode) {
        this.sendBillCode = sendBillCode;
    }

    public List<CmsProvideInterestTemp> getInterestVoList() {
        return interestVoList;
    }

    public void setInterestVoList(List<CmsProvideInterestTemp> interestVoList) {
        this.interestVoList = interestVoList;
    }

    public BigDecimal getRepayMoney() {
        return repayMoney;
    }

    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
}
