package com.bytter.credential.domain.international.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**信用证付汇表**/
@Entity
@Table(name = "LC_PAYMENT", schema = "")
public class LcPayment  extends IdEntity implements java.io.Serializable{
    
    /**单位编号**/
    private String corpId;
    /**到单ID**/
    private String arrivalId;
    /**信用证登记ID**/
    private String lcId;
    /**付汇流水号**/
    private String paymentNo;
    /**付汇日期**/
    private Date paymentDate;
    /**币别**/
    private String curId;
    /**币别名称**/
    private String curName;
    /**汇率**/
    private BigDecimal exchangeRate;
    /**利率**/
    private BigDecimal interestRate;
    /**到单手续费**/
    private BigDecimal handlingCharge;
    /**付汇金额**/
    private BigDecimal paymentMoney;
    /**信用证付款总额**/
    private BigDecimal paymentMoneyTotal;
    /**剩余金额**/
    private BigDecimal freezMoney;
    /**备注**/
    private String rmk;
    /**状态**/
    private Integer status;
    /**操作前状态**/
    private Integer lastStatus;
    /**创建日期**/
    private Date createDate;
    /**创建人登录名称**/
    private String createBy;
    /**更新日期**/
    private Date updateDate;
    /**更新人登录名称**/
    private String updateBy;
    /*结算汇率*/
    private BigDecimal settleRate;
    /*当日汇率*/
    private BigDecimal dayRate;
    /**
     * 是否释放剩余全部授信额度
     */
    private String isAllRelease;
    /**
     * 附件id
     */
    private String attachmentId;
    @Column(name ="CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name ="ARRIVAL_ID")
    public String getArrivalId() {
        return arrivalId;
    }
    public void setArrivalId(String arrivalId) {
        this.arrivalId = arrivalId;
    }
    @Column(name ="LC_ID")
    public String getLcId() {
        return lcId;
    }
    public void setLcId(String lcId) {
        this.lcId = lcId;
    }
    @Column(name ="PAYMENT_NO")
    public String getPaymentNo() {
        return paymentNo;
    }
    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }
    @Column(name ="PAYMENT_DATE")
    public Date getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
    @Column(name ="CUR_ID")
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name ="CUR_NAME")
    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    @Column(name ="EXCHANGE_RATE")
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }
    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
    @Column(name ="INTEREST_RATE")
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    @Column(name ="HANDLING_CHARGE")
    public BigDecimal getHandlingCharge() {
        return handlingCharge;
    }
    public void setHandlingCharge(BigDecimal handlingCharge) {
        this.handlingCharge = handlingCharge;
    }
    @Column(name ="PAYMENT_MONEY")
    public BigDecimal getPaymentMoney() {
        return paymentMoney;
    }
    public void setPaymentMoney(BigDecimal paymentMoney) {
        this.paymentMoney = paymentMoney;
    }
    @Column(name ="PAYMENT_MONEY_TOTAL")
    public BigDecimal getPaymentMoneyTotal() {
        return paymentMoneyTotal;
    }
    public void setPaymentMoneyTotal(BigDecimal paymentMoneyTotal) {
        this.paymentMoneyTotal = paymentMoneyTotal;
    }
    @Column(name ="FREEZ_MONEY")
    public BigDecimal getFreezMoney() {
        return freezMoney;
    }
    public void setFreezMoney(BigDecimal freezMoney) {
        this.freezMoney = freezMoney;
    }
    @Column(name ="RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name ="STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name ="LAST_STATUS")
    public Integer getLastStatus() {
        return lastStatus;
    }
    public void setLastStatus(Integer lastStatus) {
        this.lastStatus = lastStatus;
    }
    @Column(name ="CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name ="CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name ="UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name ="UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "SETTLE_RATE")
    public BigDecimal getSettleRate() {
        return settleRate;
    }
    public void setSettleRate(BigDecimal settleRate) {
        this.settleRate = settleRate;
    }
    @Column(name = "DAY_RATE")
    public BigDecimal getDayRate() {
        return dayRate;
    }
    public void setDayRate(BigDecimal dayRate) {
        this.dayRate = dayRate;
    }
    @Column(name = "IS_ALL_RELEASE")
    public String getIsAllRelease() {
        return isAllRelease;
    }
    public void setIsAllRelease(String isAllRelease) {
        this.isAllRelease = isAllRelease;
    }
    @Column(name = "ATTACHMENT_ID")
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
}
