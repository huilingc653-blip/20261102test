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
 * @Description 信用证抵押融资表
 * @Author liusy
 * @Date 2018-11-28 20:02
 * @Version 1.0
 */
@Entity
@Table(name = "LC_MORTGAGE")
public class LcMortgage extends IdEntity implements Serializable {
    /*单位编号*/
    private String corpId;
    /*交单ID*/
    private String arrivalId;
    /*信用证登记ID*/
    private String lcId;
    /*抵押单号*/
    private String mortgageNo;
    /*抵押金额*/
    private BigDecimal mortgageMoney;
    /*抵押日期*/
    private Date mortgageDate;
    /*到期日期*/
    private Date expiryDate;
    /*利率*/
    private BigDecimal interestRate;
    /*备注*/
    private String rmk;
    /*状态*/
    private Integer status;
    /*创建日期*/
    private Date createDate;
    /*创建人登录名称*/
    private String createBy;
    /*更新日期*/
    private Date updateDate;
    /*更新人登录名称*/
    private String updateBy;
    /*取消日期*/
    private Date delDate;
    /*取消人登录名称*/
    private String delBy;
    /*付息日期*/
    private Date payInterestDate;
    /*利息*/
    private BigDecimal payInterestMoney;
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
    @Column(name = "MORTGAGE_NO",length = 32)
    public String getMortgageNo() {
        return mortgageNo;
    }
    public void setMortgageNo(String mortgageNo) {
        this.mortgageNo = mortgageNo;
    }
    @Column(name = "MORTGAGE_MONEY")
    public BigDecimal getMortgageMoney() {
        return mortgageMoney;
    }
    public void setMortgageMoney(BigDecimal mortgageMoney) {
        this.mortgageMoney = mortgageMoney;
    }
    @Column(name = "MORTGAGE_DATE")
    public Date getMortgageDate() {
        return mortgageDate;
    }
    public void setMortgageDate(Date mortgageDate) {
        this.mortgageDate = mortgageDate;
    }
    @Column(name = "EXPIRY_DATE")
    public Date getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
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
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "DEL_DATE")
    public Date getDelDate() {
        return delDate;
    }
    public void setDelDate(Date delDate) {
        this.delDate = delDate;
    }
    @Column(name = "DEL_BY")
    public String getDelBy() {
        return delBy;
    }
    public void setDelBy(String delBy) {
        this.delBy = delBy;
    }
    @Column(name = "ATTACHMENT_ID")
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    @Column(name = "PAY_INTEREST_DATE")
    public Date getPayInterestDate() {
        return payInterestDate;
    }
    public void setPayInterestDate(Date payInterestDate) {
        this.payInterestDate = payInterestDate;
    }
    @Column(name = "PAY_INTEREST_MONEY")
    public BigDecimal getPayInterestMoney() {
        return payInterestMoney;
    }
    public void setPayInterestMoney(BigDecimal payInterestMoney) {
        this.payInterestMoney = payInterestMoney;
    }
    @Override
    public String toString() {
        return "LcMortgage{" +
                "corpId='" + corpId + '\'' +
                ", arrivalId='" + arrivalId + '\'' +
                ", lcId='" + lcId + '\'' +
                ", mortgageNo='" + mortgageNo + '\'' +
                ", mortgageMoney=" + mortgageMoney +
                ", mortgageDate=" + mortgageDate +
                ", expiryDate=" + expiryDate +
                ", interestRate=" + interestRate +
                ", rmk='" + rmk + '\'' +
                ", status=" + status +
                ", createDate=" + createDate +
                ", createBy='" + createBy + '\'' +
                ", updateDate=" + updateDate +
                ", updateBy='" + updateBy + '\'' +
                ", delDate=" + delDate +
                ", delBy='" + delBy + '\'' +
                ", payInterestDate=" + payInterestDate +
                ", payInterestMoney=" + payInterestMoney +
                ", attachmentId='" + attachmentId + '\'' +
                '}';
    }
}
