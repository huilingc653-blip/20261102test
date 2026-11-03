package com.bytter.credential.domain.international.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**信用证押汇展期表**/
@Entity
@Table(name = "LC_DEPOSIT_DELAY", schema = "")
public class LcDepositDelay  extends IdEntity implements java.io.Serializable{
    
    /**单位编号**/
    private String corpId;
    /**展期流水号**/
    private  String delayNo;
    /**押汇ID**/
    private  String depositId;
    /**押汇流水号**/
    private  String depositNo;
    /**展期天数**/
    private Integer delayDays;
    /**展期日期**/
    private Date delayDate;
    /**展期到期日期**/
    private Date expiryDate;
    /**利率**/
    private BigDecimal interestRate;
    /**备注**/
    private String rmk;
    /**状态**/
    private Integer status;
    /**创建日期**/
    private Date createDate;
    /**创建人登录名称**/
    private String createBy;
    /**更新日期**/
    private Date updateDate;
    /**更新人登录名称**/
    private String updateBy;
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
    
    @Column(name ="DELAY_NO")
    public String getDelayNo() {
        return delayNo;
    }
    public void setDelayNo(String delayNo) {
        this.delayNo = delayNo;
    }
    @Column(name ="DEPOSIT_ID")
    public String getDepositId() {
        return depositId;
    }
    public void setDepositId(String depositId) {
        this.depositId = depositId;
    }
    @Column(name ="DEPOSIT_NO")
    public String getDepositNo() {
        return depositNo;
    }
    public void setDepositNo(String depositNo) {
        this.depositNo = depositNo;
    }
    @Column(name ="DELAY_DAYS")
    public Integer getDelayDays() {
        return delayDays;
    }
    public void setDelayDays(Integer delayDays) {
        this.delayDays = delayDays;
    }
    @Column(name ="DELAY_DATE")
    public Date getDelayDate() {
        return delayDate;
    }
    public void setDelayDate(Date delayDate) {
        this.delayDate = delayDate;
    }
    @Column(name ="INTEREST_RATE")
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
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
    @Column(name = "EXPIRY_DATE")
    public Date getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
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
}
