package com.bytter.bill.domain.bill.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
* @Description 票据保证金利息维护表VO
* @author zhous
* @date 2025/3/5 15:26
*/
@SuppressWarnings("serial")
public class AbEmitBailInterestInfoVO extends IdEntity implements java.io.Serializable {

    /**单位ID*/
    @BtCompositeSearch(fieldName = "corp_id")
    private String corpId;
    /**单位名称*/
    private String corpName;
    /**票据账号*/
    @BtCompositeSearch(fieldName = "bank_acc")
    private String bankAcc;
    /**银行类型id*/
    @BtCompositeSearch(fieldName = "bank_type_id")
    private String bankTypeId;
    /**银行类型名称*/
    private String bankTypeName;
    /**当前月份*/
    @BtCompositeSearch(fieldName = "current_month")
    private String currentMonth;
    /**票面金额*/
    @BtCompositeSearch(fieldName = "bill_money")
    private BigDecimal billMoney;
    /**保证金金额*/
    @BtCompositeSearch(fieldName = "bail_money")
    private BigDecimal bailMoney;
    /**到期解付金额*/
    @BtCompositeSearch(fieldName = "DUE_SETTLEMENT_AMOUNT")
    private BigDecimal dueSettlementAmount;
    /**实际结算金额*/
    @BtCompositeSearch(fieldName = "ACTUAL_SETTLEMENT_AMOUNT")
    private BigDecimal actualSettlementAmount;
    /**利息差额*/
    @BtCompositeSearch(fieldName = "INTEREST_MARGIN")
    private BigDecimal interestMargin;
    /**单据状态 0-暂存,1-已确认*/
    @BtCompositeSearch(fieldName = "status")
    private String status;
    private String statusName;
    /**推送状态 0-未推送,1-待推送,2-已推送*/
    @BtCompositeSearch(fieldName = "push_status")
    private String pushStatus;
    private String pushStatusName;
    /**记录来源 0-自动生成 1-手动增加*/
    private String recordSource;
    /**最后操作人*/
    private String lastOperator;

    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
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

    public String getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }

    public String getBankTypeName() {
        return bankTypeName;
    }

    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }

    public String getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(String currentMonth) {
        this.currentMonth = currentMonth;
    }

    public BigDecimal getBillMoney() {
        return billMoney;
    }

    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }

    public BigDecimal getBailMoney() {
        return bailMoney;
    }

    public void setBailMoney(BigDecimal bailMoney) {
        this.bailMoney = bailMoney;
    }

    public BigDecimal getDueSettlementAmount() {
        return dueSettlementAmount;
    }

    public void setDueSettlementAmount(BigDecimal dueSettlementAmount) {
        this.dueSettlementAmount = dueSettlementAmount;
    }

    public BigDecimal getActualSettlementAmount() {
        return actualSettlementAmount;
    }

    public void setActualSettlementAmount(BigDecimal actualSettlementAmount) {
        this.actualSettlementAmount = actualSettlementAmount;
    }

    public BigDecimal getInterestMargin() {
        return interestMargin;
    }

    public void setInterestMargin(BigDecimal interestMargin) {
        this.interestMargin = interestMargin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(String pushStatus) {
        this.pushStatus = pushStatus;
    }

    public String getPushStatusName() {
        return pushStatusName;
    }

    public void setPushStatusName(String pushStatusName) {
        this.pushStatusName = pushStatusName;
    }

    public String getRecordSource() {
        return recordSource;
    }

    public void setRecordSource(String recordSource) {
        this.recordSource = recordSource;
    }

    public String getLastOperator() {
        return lastOperator;
    }

    public void setLastOperator(String lastOperator) {
        this.lastOperator = lastOperator;
    }
}
