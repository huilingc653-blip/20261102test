package com.bytter.bill.domain.bill.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
* @Description 票据保证金利息维护表
* @author zhous
* @date 2025/3/5 15:26
*/
@Entity
@Table(name = "AB_EMIT_BAIL_INTEREST_INFO", schema = "")
@SuppressWarnings("serial")
public class AbEmitBailInterestInfo extends IdEntity implements java.io.Serializable {

    /**单位ID*/
    private String corpId;
    /**单位名称*/
    private String corpName;
    /**票据账号*/
    private String bankAcc;
    /**银行类型id*/
    private String bankTypeId;
    /**银行类型名称*/
    private String bankTypeName;
    /**当前月份*/
    private String currentMonth;
    /**票面金额*/
    private BigDecimal billMoney;
    /**保证金金额*/
    private BigDecimal bailMoney;
    /**到期解付金额*/
    private BigDecimal dueSettlementAmount;
    /**实际结算金额*/
    private BigDecimal actualSettlementAmount;
    /**利息差额*/
    private BigDecimal interestMargin;
    /**单据状态 0-暂存,1-已确认*/
    private String status;
    /**推送状态 0-未推送,1-待推送,2-已推送*/
    private String pushStatus;
    /**记录来源 0-自动生成 1-手动增加*/
    private String recordSource;
    /**创建人*/
    private String createBy;
    /**创建时间*/
    private Date createDate;
    /**修改人*/
    private String updateBy;
    /**修改时间*/
    private Date updateDate;

    @Column(name = "BANK_ACC")
    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name = "CORP_NAME")
    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    @Column(name = "BANK_TYPE_ID")
    public String getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }

    @Column(name = "BANK_TYPE_NAME")
    public String getBankTypeName() {
        return bankTypeName;
    }

    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }

    @Column(name = "CURRENT_MONTH")
    public String getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(String currentMonth) {
        this.currentMonth = currentMonth;
    }

    @Column(name = "BILL_MONEY")
    public BigDecimal getBillMoney() {
        return billMoney;
    }

    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }

    @Column(name = "BAIL_MONEY")
    public BigDecimal getBailMoney() {
        return bailMoney;
    }

    public void setBailMoney(BigDecimal bailMoney) {
        this.bailMoney = bailMoney;
    }

    @Column(name = "DUE_SETTLEMENT_AMOUNT")
    public BigDecimal getDueSettlementAmount() {
        return dueSettlementAmount;
    }

    public void setDueSettlementAmount(BigDecimal dueSettlementAmount) {
        this.dueSettlementAmount = dueSettlementAmount;
    }

    @Column(name = "ACTUAL_SETTLEMENT_AMOUNT")
    public BigDecimal getActualSettlementAmount() {
        return actualSettlementAmount;
    }

    public void setActualSettlementAmount(BigDecimal actualSettlementAmount) {
        this.actualSettlementAmount = actualSettlementAmount;
    }

    @Column(name = "INTEREST_MARGIN")
    public BigDecimal getInterestMargin() {
        return interestMargin;
    }

    public void setInterestMargin(BigDecimal interestMargin) {
        this.interestMargin = interestMargin;
    }

    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "PUSH_STATUS")
    public String getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(String pushStatus) {
        this.pushStatus = pushStatus;
    }

    @Column(name = "RECORD_SOURCE")
    public String getRecordSource() {
        return recordSource;
    }

    public void setRecordSource(String recordSource) {
        this.recordSource = recordSource;
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
