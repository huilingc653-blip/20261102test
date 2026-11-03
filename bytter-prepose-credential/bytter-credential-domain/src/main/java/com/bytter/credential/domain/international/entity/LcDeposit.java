package com.bytter.credential.domain.international.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**信用证押汇表**/
@Entity
@Table(name = "LC_DEPOSIT", schema = "")
public class LcDeposit  extends IdEntity implements java.io.Serializable{
    /**单位编号**/
    private String corpId;
    /**到单ID**/
    private String arrivalId;
    /**信用证登记ID**/
    private String lcId;
    /**押汇流水号**/
    private String depositNo;
    /**币别**/
    private String curId;
    /**币别名称**/
    private String curName;
    /**汇率**/
    private BigDecimal exchangeRate;
    /**利率**/
    private BigDecimal interestRate;
    /**押汇金额**/
    private BigDecimal depositMoney;
    /**押汇日期**/
    private Date depositDate;
    /**到期日期**/
    private Date expiryDate;
    /**期限**/
    private Integer timeLimit;
    /**期限单位**/
    private String termUnit;
    /**是否补录**/
    private String isSkipProcess;
    /**备注**/
    private String rmk;
    /**状态**/
    private Integer status;
    /**还押汇总额**/
    private BigDecimal reapyMoney;
    /**剩余金额**/
    private BigDecimal leftMoney;
    /**创建日期**/
    private Date createDate;
    /**创建人登录名称**/
    private String createBy;
    /**更新日期**/
    private Date updateDate;
    /**更新人登录名称**/
    private String updateBy;
    /**付息日期**/
    private Date payInterestDate;
    /**付息金额**/
    private BigDecimal payInterestMoney;
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
    @Column(name ="DEPOSIT_MONEY")
    public BigDecimal getDepositMoney() {
        return depositMoney;
    }
    public void setDepositMoney(BigDecimal depositMoney) {
        this.depositMoney = depositMoney;
    }
    @Column(name ="DEPOSIT_DATE")
    public Date getDepositDate() {
        return depositDate;
    }
    public void setDepositDate(Date depositDate) {
        this.depositDate = depositDate;
    }
    @Column(name ="EXPIRY_DATE")
    public Date getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    @Column(name ="TIME_LIMIT")
    public Integer getTimeLimit() {
        return timeLimit;
    }
    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }
    @Column(name ="TERM_UNIT")
    public String getTermUnit() {
        return termUnit;
    }
    public void setTermUnit(String termUnit) {
        this.termUnit = termUnit;
    }
    @Column(name ="IS_SKIP_PROCESS")
    public String getIsSkipProcess() {
        return isSkipProcess;
    }
    public void setIsSkipProcess(String isSkipProcess) {
        this.isSkipProcess = isSkipProcess;
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
    @Column(name ="REAPY_MONEY")
    public BigDecimal getReapyMoney() {
        return reapyMoney;
    }
    public void setReapyMoney(BigDecimal reapyMoney) {
        this.reapyMoney = reapyMoney;
    }
    @Column(name ="LEFT_MONEY")
    public BigDecimal getLeftMoney() {
        return leftMoney;
    }
    public void setLeftMoney(BigDecimal leftMoney) {
        this.leftMoney = leftMoney;
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
}
