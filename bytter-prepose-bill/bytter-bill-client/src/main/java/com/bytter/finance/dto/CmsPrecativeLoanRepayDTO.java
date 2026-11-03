package com.bytter.finance.dto;
/**
 * Title: Entity Description: 委托贷款还款信息表
 * 
 * @author onlineGenerator
 * @since 2017-08-02 14:05:07
 * @version 1.0
 *
 */
public class CmsPrecativeLoanRepayDTO implements java.io.Serializable {
    private String id;
    private String sendId;
    private String billCode;
    /** 借款单位代码 */
    private String corpId;
    /** 贷款合同号 */
    private String contractCode;
    /** 贷款发放单号 */
    private String sendCode;
    /** 还款金额 */
    private java.math.BigDecimal repayMoney;
    /** 在途金额 */
    private java.math.BigDecimal passageMoney;
    /** 归还金额(在途和已还) */
    private java.math.BigDecimal sumMoney;
    /** 还款日期 */
    private java.util.Date repayDate;
    /** 还款利息 */
    private java.math.BigDecimal repayAccrual;
    /** 记账凭证号 */
    private String voucherNo;
    /** 记账流水号 */
    private String orderNo;
    /** 贷款账号 */
    private String loanAcc;
    /** 还款账号 */
    private String repayAcc;
    /** 支付方式 */
    private String payType;
    /** 附件名称 */
    private String annexName;
    /** 还款计划 */
    private String repaymentScheduleId;
    /** 还款方式 */
    private String repayway;
    /** 备注 */
    private String rmk;
    /** 状态 */
    private Integer status;
    /** 业务状态 */
    private String bizStatus;
    /** 创建人名称 */
    private String createName;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    private java.util.Date createDate;
    /** 更新人名称 */
    private String updateName;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private java.util.Date updateDate;
    /** 展期日期 */
    private java.util.Date deferredRepayDate;
    /** 展期利率 */
    private java.math.BigDecimal deferredRate;
    /** 单据编号 */
    private String infoBillCode;
    /** 期限 */
    private Integer term;
    /** 期限单位 */
    private String ternUnit;
    /** 贷款币种 */
    private String curId;
    /** 担保方式 */
    private String plType;
    private String precativeId;
    private java.util.Date sendDate;
    private java.util.Date deadlineDate;
    /** 借款人 */
    private String loanOpp;
    /** 还款金额 */
    private java.math.BigDecimal sendMoney;
    private String repayType;
    private String loanSendOutType;
    private String interestWay;
    /** 记账标志 */
    private String tallySign;
    /** 记账员代码 */
    private String tallyCode;
    /**原始凭证号*/
    private String oldVoucher;
    
    public String getOldVoucher() {
		return oldVoucher;
	}
	public void setOldVoucher(String oldVoucher) {
		this.oldVoucher = oldVoucher;
	}
	public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    public String getSendCode() {
        return sendCode;
    }
    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
    }
    public java.math.BigDecimal getPassageMoney() {
        return passageMoney;
    }
    public void setPassageMoney(java.math.BigDecimal passageMoney) {
        this.passageMoney = passageMoney;
    }
    public java.math.BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(java.math.BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    public java.util.Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(java.util.Date repayDate) {
        this.repayDate = repayDate;
    }
    public java.math.BigDecimal getRepayAccrual() {
        return repayAccrual;
    }
    public void setRepayAccrual(java.math.BigDecimal repayAccrual) {
        this.repayAccrual = repayAccrual;
    }
    public String getVoucherNo() {
        return voucherNo;
    }
    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
    public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public String getLoanAcc() {
        return loanAcc;
    }
    public void setLoanAcc(String loanAcc) {
        this.loanAcc = loanAcc;
    }
    public String getRepayAcc() {
        return repayAcc;
    }
    public void setRepayAcc(String repayAcc) {
        this.repayAcc = repayAcc;
    }
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    public String getAnnexName() {
        return annexName;
    }
    public void setAnnexName(String annexName) {
        this.annexName = annexName;
    }
    public String getRepaymentScheduleId() {
        return repaymentScheduleId;
    }
    public void setRepaymentScheduleId(String repaymentScheduleId) {
        this.repaymentScheduleId = repaymentScheduleId;
    }
    public String getRepayway() {
        return repayway;
    }
    public void setRepayway(String repayway) {
        this.repayway = repayway;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getCreateName() {
        return createName;
    }
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public java.util.Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateName() {
        return updateName;
    }
    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public java.util.Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getInfoBillCode() {
        return infoBillCode;
    }
    public void setInfoBillCode(String infoBillCode) {
        this.infoBillCode = infoBillCode;
    }
    public Integer getTerm() {
        return term;
    }
    public void setTerm(Integer term) {
        this.term = term;
    }
    public java.util.Date getSendDate() {
        return sendDate;
    }
    public void setSendDate(java.util.Date sendDate) {
        this.sendDate = sendDate;
    }
    public java.util.Date getDeadlineDate() {
        return deadlineDate;
    }
    public void setDeadlineDate(java.util.Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }
    public String getTernUnit() {
        return ternUnit;
    }
    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }
    public String getSendId() {
        return sendId;
    }
    public void setSendId(String sendId) {
        this.sendId = sendId;
    }
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public java.math.BigDecimal getSumMoney() {
        return sumMoney;
    }
    public void setSumMoney(java.math.BigDecimal sumMoney) {
        this.sumMoney = sumMoney;
    }
    public String getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }
    public java.util.Date getDeferredRepayDate() {
        return deferredRepayDate;
    }
    public void setDeferredRepayDate(java.util.Date deferredRepayDate) {
        this.deferredRepayDate = deferredRepayDate;
    }
    public java.math.BigDecimal getDeferredRate() {
        return deferredRate;
    }
    public void setDeferredRate(java.math.BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getPlType() {
        return plType;
    }
    public void setPlType(String plType) {
        this.plType = plType;
    }
    public String getPrecativeId() {
        return precativeId;
    }
    public void setPrecativeId(String precativeId) {
        this.precativeId = precativeId;
    }
    public String getLoanOpp() {
        return loanOpp;
    }
    public void setLoanOpp(String loanOpp) {
        this.loanOpp = loanOpp;
    }
    public java.math.BigDecimal getSendMoney() {
        return sendMoney;
    }
    public void setSendMoney(java.math.BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    public String getLoanSendOutType() {
        return loanSendOutType;
    }
    public void setLoanSendOutType(String loanSendOutType) {
        this.loanSendOutType = loanSendOutType;
    }
    public String getInterestWay() {
        return interestWay;
    }
    public void setInterestWay(String interestWay) {
        this.interestWay = interestWay;
    }
    public String getTallySign() {
        return tallySign;
    }
    public void setTallySign(String tallySign) {
        this.tallySign = tallySign;
    }
    public String getTallyCode() {
        return tallyCode;
    }
    public void setTallyCode(String tallyCode) {
        this.tallyCode = tallyCode;
    }
}
