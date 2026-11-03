package com.bytter.credential.domain.international.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title Entity
 * @Description 信用证到期收款表
 * @Author liusy
 * @Date 2018-11-28 19:45
 * @Version 1.0
 */
@Entity
@Table(name = "LC_INCOMING")
public class LcIncoming extends IdEntity implements Serializable {
    /*单位编号*/
    private  String corpId;
    /*交单ID*/
    private  String arrivalId;
    /*信用证登记ID*/
    private  String lcId;
    /*币别*/
    private  String curId;
    /*收款单号*/
    private  String incomeNo;
    /*收款金额*/
    private BigDecimal incomingMoney;
    /*手续费*/
    private  BigDecimal handlingCharge;
    /*汇率*/
    private  BigDecimal exchangeRate;
    /*利率*/
    private  BigDecimal interestRate;
    /*备注*/
    private  String rmk;
    /*状态*/
    private  Integer status;
    /*创建人登录名称*/
    private  String createBy;
    /*更新日期*/
    private Date updateDate;
    /*创建日期*/
    private  Date createDate;
    /*更新人登录名称*/
    private  String updateBy;
    /**
     * 附件id
     */
    private String attachmentId;
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "ARRIVAL_ID")
    public String getArrivalId() {
        return arrivalId;
    }
    public void setArrivalId(String arrivalId) {
        this.arrivalId = arrivalId;
    }
    @Column(name = "LC_ID")
    public String getLcId() {
        return lcId;
    }
    public void setLcId(String lcId) {
        this.lcId = lcId;
    }
    @Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name = "INCOME_NO")
    public String getIncomeNo() {
        return incomeNo;
    }
    public void setIncomeNo(String incomeNo) {
        this.incomeNo = incomeNo;
    }
    @Column(name = "INCOMING_MONEY")
    public BigDecimal getIncomingMoney() {
        return incomingMoney;
    }
    public void setIncomingMoney(BigDecimal incomingMoney) {
        this.incomingMoney = incomingMoney;
    }
    @Column(name = "HANDLING_CHARGE")
    public BigDecimal getHandlingCharge() {
        return handlingCharge;
    }
    public void setHandlingCharge(BigDecimal handlingCharge) {
        this.handlingCharge = handlingCharge;
    }
    @Column(name = "EXCHANGE_RATE")
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }
    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
    @Column(name = "INTEREST_RATE")
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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
    @Column(name = "ATTACHMENT_ID")
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    @Override
    public String toString() {
        return "LcIncoming{" +
                "corpId='" + corpId + '\'' +
                ", arrivalId='" + arrivalId + '\'' +
                ", lcId='" + lcId + '\'' +
                ", curId='" + curId + '\'' +
                ", incomeNo='" + incomeNo + '\'' +
                ", incomingMoney=" + incomingMoney +
                ", handlingCharge=" + handlingCharge +
                ", exchangeRate=" + exchangeRate +
                ", interestRate=" + interestRate +
                ", rmk='" + rmk + '\'' +
                ", status=" + status +
                ", createBy='" + createBy + '\'' +
                ", updateDate=" + updateDate +
                ", createDate=" + createDate +
                ", updateBy='" + updateBy + '\'' +
                '}';
    }
}
