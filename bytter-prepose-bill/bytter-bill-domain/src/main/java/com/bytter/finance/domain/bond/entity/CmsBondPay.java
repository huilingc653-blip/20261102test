package com.bytter.finance.domain.bond.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: CMS_BOND_PAY
 * @author onlineGenerator
 * @date 2022-06-07 10:07:46
 * @version 1.0
 *
 */
@Entity
@Table(name = "CMS_BOND_PAY", schema = "")
@SuppressWarnings("serial")
public class CmsBondPay extends IdEntity implements java.io.Serializable {
    /** 所属单位 */
    private String corpId;
    /** 债劵注册id */
    private String registerId;
    /** 债劵发行id */
    private String issueId;
    /** 收款账号 */
    private String onlinePaymentAcc;
    /** 收款账户名称 */
    private String onlineAccName;
    /** 收款人开户行行号 */
    private String onlineBankCode;
    /** 收款账户开户行名称 */
    private String onlineBankName;
    /** 状态 */
    private Integer status;
    /** 支付订单号 */
    private String payNo;
    /** 支付方式 */
    private String payType;
    /** 线下线上还款标识 */
    private String onlineSign;
    /** 备注 */
    private String rmk;
    /** 用途 */
    private String purpose;
    /** 支付日期 */
    private Date payDate;
    /** 还款本金 */
    private BigDecimal repayMoney;
    /** 还款总金额 */
    private BigDecimal repayTotalMoney;
    /** 利息 */
    private BigDecimal interest;
    /** 是否付息 */
    private String isPayInterest;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    private Date createDate;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private Date updateDate;
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "REGISTER_ID")
    public String getRegisterId() {
        return registerId;
    }
    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }
    @Column(name = "ISSUE_ID")
    public String getIssueId() {
        return issueId;
    }
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }
    @Column(name = "ONLINE_PAYMENT_ACC")
    public String getOnlinePaymentAcc() {
        return onlinePaymentAcc;
    }
    public void setOnlinePaymentAcc(String onlinePaymentAcc) {
        this.onlinePaymentAcc = onlinePaymentAcc;
    }
    @Column(name = "ONLINE_ACC_NAME")
    public String getOnlineAccName() {
        return onlineAccName;
    }
    public void setOnlineAccName(String onlineAccName) {
        this.onlineAccName = onlineAccName;
    }
    @Column(name = "ONLINE_BANK_CODE")
    public String getOnlineBankCode() {
        return onlineBankCode;
    }
    public void setOnlineBankCode(String onlineBankCode) {
        this.onlineBankCode = onlineBankCode;
    }
    @Column(name = "ONLINE_BANK_NAME")
    public String getOnlineBankName() {
        return onlineBankName;
    }
    public void setOnlineBankName(String onlineBankName) {
        this.onlineBankName = onlineBankName;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "PAY_NO")
    public String getPayNo() {
        return payNo;
    }
    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }
    @Column(name = "PAY_TYPE")
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    @Column(name = "ONLINE_SIGN")
    public String getOnlineSign() {
        return onlineSign;
    }
    public void setOnlineSign(String onlineSign) {
        this.onlineSign = onlineSign;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "PURPOSE")
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    @Column(name = "PAY_DATE")
    public Date getPayDate() {
        return payDate;
    }
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
    @Column(name = "REPAY_MONEY")
    public BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    @Column(name = "INTEREST")
    public BigDecimal getInterest() {
        return interest;
    }
    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }
    @Column(name = "REPAY_TOTAL_MONEY")
    public BigDecimal getRepayTotalMoney() {
        return repayTotalMoney;
    }
    public void setRepayTotalMoney(BigDecimal repayTotalMoney) {
        this.repayTotalMoney = repayTotalMoney;
    }
    @Column(name = "IS_PAY_INTEREST")
    public String getIsPayInterest() {
        return isPayInterest;
    }
    public void setIsPayInterest(String isPayInterest) {
        this.isPayInterest = isPayInterest;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
