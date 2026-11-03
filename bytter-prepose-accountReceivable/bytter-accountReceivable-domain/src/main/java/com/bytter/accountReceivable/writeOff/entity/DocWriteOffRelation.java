package com.bytter.accountReceivable.writeOff.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 应收单据核销关系表
 */
@Entity
@Table(name = "DOC_WIRTE_OFF_RELATION")
public class DocWriteOffRelation extends IdEntity implements Serializable {

    /** 核销批次号 */
    private String batchNo;
    /** 取消核销批次号 */
    private String cancelBatchNo;
    /** 应收单据ID */
    private String docId;
    /** 关联数据ID */
    private String relationId;
    /** 结算方式 */
    private String paymentMeans;
    /** 匹配规则描述 */
    private String rulesName;
    /** 本次核销汇率 */
    private BigDecimal exchangeRate;
    /** 可核销原币金额 */
    private BigDecimal relationOriginMoney;
    /** 可核销应收币种金额 */
    private BigDecimal relationConvertMoney;
    /** 汇兑损益 */
    //private BigDecimal exchangeDiff;
    /**
     * 核销状态
     */
    private String writeOffStatus;
    /**
     * 核销类型
     */
    private String writeOffType;
    /**
     * 审批状态
     */
    private Integer status;
    /**
     * 租户id
     */
    private String tenantId;
    private String createBy;
    private Date createDate;
    private String forceReasonType;
    private String forcePaymentMeans;
    private String forceReason;

    /** 核销笔数 11一对一，12一对多，21多对一，10强制核销 */
    private String writeOffNum;
    /**核销前状态*/
    private String beforeWriteOffStatus;
    /**核销前金额*/
    private BigDecimal beforeWriteOffBal;

    private BigDecimal executeMoney;
    @Column(name ="EXECUTE_MONEY",nullable=false)
    public BigDecimal getExecuteMoney() {
        return executeMoney;
    }

    public void setExecuteMoney(BigDecimal executeMoney) {
        this.executeMoney = executeMoney;
    }

    @Column(name ="BEFORE_WRITE_OFF_BAL",nullable=false)
    public BigDecimal getBeforeWriteOffBal() {
        return beforeWriteOffBal;
    }

    public void setBeforeWriteOffBal(BigDecimal beforeWriteOffBal) {
        this.beforeWriteOffBal = beforeWriteOffBal;
    }

    @Column(name ="BEFORE_WRITE_OFF_STATUS",nullable=false)
    public String getBeforeWriteOffStatus() {
        return beforeWriteOffStatus;
    }

    public void setBeforeWriteOffStatus(String beforeWriteOffStatus) {
        this.beforeWriteOffStatus = beforeWriteOffStatus;
    }
    @Column(name ="BATCH_NO")
    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    @Column(name ="CANCEL_BATCH_NO")
    public String getCancelBatchNo() {
        return cancelBatchNo;
    }

    public void setCancelBatchNo(String cancelBatchNo) {
        this.cancelBatchNo = cancelBatchNo;
    }

    @Column(name ="DOC_ID")
    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    @Column(name ="RELATION_ID")
    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    @Column(name ="PAYMENT_MEANS")
    public String getPaymentMeans() {
        return paymentMeans;
    }

    public void setPaymentMeans(String paymentMeans) {
        this.paymentMeans = paymentMeans;
    }

    @Column(name ="RULES_NAME")
    public String getRulesName() {
        return rulesName;
    }

    public void setRulesName(String rulesName) {
        this.rulesName = rulesName;
    }

    @Column(name ="EXCHANGE_RATE")
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @Column(name ="RELATION_ORIGIN_MONEY")
    public BigDecimal getRelationOriginMoney() {
        return relationOriginMoney;
    }

    public void setRelationOriginMoney(BigDecimal relationOriginMoney) {
        this.relationOriginMoney = relationOriginMoney;
    }

    @Column(name ="RELATION_CONVERT_MONEY")
    public BigDecimal getRelationConvertMoney() {
        return relationConvertMoney;
    }

    public void setRelationConvertMoney(BigDecimal relationConvertMoney) {
        this.relationConvertMoney = relationConvertMoney;
    }

    /*@Column(name ="EXCHANGE_DIFF")
    public BigDecimal getExchangeDiff() {
        return exchangeDiff;
    }

    public void setExchangeDiff(BigDecimal exchangeDiff) {
        this.exchangeDiff = exchangeDiff;
    }*/

    @Column(name ="WRITE_OFF_STATUS")
    public String getWriteOffStatus() {
        return writeOffStatus;
    }

    public void setWriteOffStatus(String writeOffStatus) {
        this.writeOffStatus = writeOffStatus;
    }

    @Column(name ="WRITE_OFF_TYPE")
    public String getWriteOffType() {
        return writeOffType;
    }

    public void setWriteOffType(String writeOffType) {
        this.writeOffType = writeOffType;
    }

    @Column(name ="STATUS")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name ="TENANT_ID")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Column(name ="CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name ="CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name ="FORCE_REASON_TYPE")
    public String getForceReasonType() {
        return forceReasonType;
    }

    public void setForceReasonType(String forceReasonType) {
        this.forceReasonType = forceReasonType;
    }

    @Column(name ="FORCE_PAYMENT_MEANS")
    public String getForcePaymentMeans() {
        return forcePaymentMeans;
    }

    public void setForcePaymentMeans(String forcePaymentMeans) {
        this.forcePaymentMeans = forcePaymentMeans;
    }

    @Column(name ="FORCE_REASON")
    public String getForceReason() {
        return forceReason;
    }

    public void setForceReason(String forceReason) {
        this.forceReason = forceReason;
    }

    @Column(name ="WRITE_OFF_NUM")
    public String getWriteOffNum() {
        return writeOffNum;
    }

    public void setWriteOffNum(String writeOffNum) {
        this.writeOffNum = writeOffNum;
    }
}
