package com.bytter.credential.domain.international.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**信用证还押汇表**/
@Entity
@Table(name = "LC_DEPOSIT_REPAY", schema = "")
public class LcDepositRepay extends IdEntity implements java.io.Serializable{
    
    /**单位编号**/
    private String corpId;
    /**还押汇流水号**/
    private String repayNo;
    /**押汇ID**/
    private String depositId;
    /**押汇流水号**/
    private String depositNo;
    /**币别**/
    private String curId;
    /**汇率**/
    private BigDecimal exchangeRate;
    /**还押汇日期**/
    private Date reapyDate;
    /**备注**/
    private String rmk;
    /**状态**/
    private Integer status;
    /**还押汇金额**/
    private BigDecimal repayMoney;
    /**创建日期**/
    private  Date createDate;
    /**创建人登录名称**/
    private String createBy;
    /**更新日期**/
    private Date updateDate;
    /**更新人登录名称**/
    private String updateBy;
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
    @Column(name ="REPAY_NO")
    public String getRepayNo() {
        return repayNo;
    }
    public void setRepayNo(String repayNo) {
        this.repayNo = repayNo;
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
    @Column(name ="CUR_ID")
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name ="EXCHANGE_RATE")
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }
    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
    @Column(name ="REAPY_DATE")
    public Date getReapyDate() {
        return reapyDate;
    }
    public void setReapyDate(Date reapyDate) {
        this.reapyDate = reapyDate;
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
    @Column(name ="REPAY_MONEY")
    public BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
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
