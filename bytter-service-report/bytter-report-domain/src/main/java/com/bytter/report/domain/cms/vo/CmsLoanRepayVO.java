package com.bytter.report.domain.cms.vo;
import java.io.Serializable;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
public class CmsLoanRepayVO implements Serializable, IBusinessKey {
    private String id;
    private String sendId;
    /** 结算中心代码 */
//    @Corp(inject = "netName")
    private String netCode;
    private String netName;
    /** 单位代码 */
//    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    @BtCompositeSearch(fieldName = "r.bill_code")
    private String billCode;
//    @Currency(inject = "curName")
    private String curCode;
    private String curName;
    /** 借款合同号 */
    private String contractCode;
    /** 贷款单号 */
    @BtCompositeSearch(fieldName = "i.bill_code")
    private String loanCode;
    /** 贷款发放单号 */
    private String sendCode;
    /** 发放金额 */
    private java.math.BigDecimal loanMoney;
    /** 还款金额 */
    private java.math.BigDecimal repayMoney;
    /** 还款日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date repayDate;
    /** 还款利息 */
    private java.math.BigDecimal repayAccrual;
    /** 记账凭证号 */
    private String voucherNo;
    /** 记账流水号 */
    private String orderNo;
    /** 借款账号 */
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createDate;
    /** 更新人名称 */
    private String updateName;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date updateDate;
    public java.math.BigDecimal getLoanMoney() {
        return loanMoney;
    }
    public void setLoanMoney(java.math.BigDecimal loanMoney) {
        this.loanMoney = loanMoney;
    }
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getLoanCode() {
        return loanCode;
    }
    public void setLoanCode(String loanCode) {
        this.loanCode = loanCode;
    }
    public String getSendId() {
        return sendId;
    }
    public void setSendId(String sendId) {
        this.sendId = sendId;
    }
    /** 附件 */
    private String attachmentId;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNetCode() {
        return netCode;
    }
    public void setNetCode(String netCode) {
        this.netCode = netCode;
    }
    public String getNetName() {
        return netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
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
    public String getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    public String getBusinessKey() {
        return getId();
    }
}
