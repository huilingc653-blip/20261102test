package com.bytter.accountReceivable.agingAnalysis.entity;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/*@Entity
@Table(name = "DOC_MANAGE")*/
public class AgingAnalysis implements Serializable {
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 单据编号
     */
    private String code;
    /**
     * 账龄起算日期
     */
    private Date agingDate;
    /**
     * 应收金额
     */
    private BigDecimal receivableMoney;
    /**
     * 应收币种
     */
    private String curId;
    /**
     * 客商名称
     */
    private String externalName;
    /**
     * 合同编号
     */
    private String contractCode;
    /**
     * 应收余额
     */
    private BigDecimal writeOffBal;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 租户id
     */
    private String tenantId;

    @Column(name = "TENANT_ID", nullable = false, length = 32)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Column(name = "CORP_ID", nullable = false, length = 32)
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name ="CODE",nullable=false)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name ="AGING_DATE",nullable=false)
    public Date getAgingDate() {
        return agingDate;
    }

    public void setAgingDate(Date agingDate) {
        this.agingDate = agingDate;
    }

    @Column(name ="RECEIVABLE_MONEY",nullable=false)
    public BigDecimal getReceivableMoney() {
        return receivableMoney;
    }

    public void setReceivableMoney(BigDecimal receivableMoney) {
        this.receivableMoney = receivableMoney;
    }
    @Column(name ="CUR_ID",nullable=false)
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name ="EXTERNAL_NAME",nullable=false)
    public String getExternalName() {
        return externalName;
    }

    public void setExternalName(String externalName) {
    this.externalName = externalName;
    }

    @Column(name ="CONTRACT_CODE")
    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    @Column(name ="STATUS")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name ="WRITE_OFF_BAL")
    public BigDecimal getWriteOffBal() {
        return writeOffBal;
    }

    public void setWriteOffBal(BigDecimal writeOffBal) {
        this.writeOffBal = writeOffBal;
    }
}
