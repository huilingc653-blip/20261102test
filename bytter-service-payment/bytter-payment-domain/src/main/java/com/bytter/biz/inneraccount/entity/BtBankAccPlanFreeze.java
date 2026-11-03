package com.bytter.biz.inneraccount.entity;

import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 计划冻结申请表
 *
 * @author fangcf
 * @date 2025-02-11
 */
@Entity
@Table(name = "BT_BANK_ACC_PLAN_FREEZE", schema = "")
@SuppressWarnings("serial")
public class BtBankAccPlanFreeze extends IdEntity implements java.io.Serializable {

    /**
     * 计划冻结单号
     */
    private String planFreezeNo;

    /**
     * 冻结申请单号
     */
    private String freezeNo;
    /**
     * 银行账号ID
     */
    private String bankAccId;
    /**
     * 银行账号
     */
    private String bankAcc;
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 申请冻结金额
     */
    private BigDecimal applyFreezeMoney;
    /**
     * 实际已冻结金额
     */
    private BigDecimal realFreezeMoney;
    /**
     * 冻结开始时间
     */
    private Date freezeStartDate;
    /**
     * 冻结开始时间
     */
    private Date freezeEndDate;
    /**
     * 原因
     */
    private String freezeRession;
    /**
     * 币别id
     */
    private String curId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 冻结状态：0-未冻结，1-部分冻结，2-全部冻结
     */
    private Integer freezeStatus;
    /**
     * 是否冻结完成提醒：0-否，1-是
     */
    private String isFreezeComplete;
    /**
     * 是否解冻：0-否，1-是
     */
    private String isUnfreeze;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;

    @Column(name ="FREEZE_NO", nullable=true, length=32)
    public String getFreezeNo() {
        return freezeNo;
    }

    public void setFreezeNo(String freezeNo) {
        this.freezeNo = freezeNo;
    }

    @Column(name ="PLAN_FREEZE_NO", nullable=true, length=32)
    public String getPlanFreezeNo() {
        return planFreezeNo;
    }

    public void setPlanFreezeNo(String planFreezeNo) {
        this.planFreezeNo = planFreezeNo;
    }

    @Column(name ="BANK_ACC_ID", nullable=true, length=32)
    public String getBankAccId() {
        return bankAccId;
    }

    public void setBankAccId(String bankAccId) {
        this.bankAccId = bankAccId;
    }

    @Column(name ="BANK_ACC", nullable=true, length=30)
    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    @Column(name ="CORP_ID", nullable=true, length=32)
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name ="APPLY_FREEZE_MONEY", nullable=true, scale=2, length=19)
    public BigDecimal getApplyFreezeMoney() {
        return applyFreezeMoney;
    }

    public void setApplyFreezeMoney(BigDecimal applyFreezeMoney) {
        this.applyFreezeMoney = applyFreezeMoney;
    }

    @Column(name ="REAL_FREEZE_MONEY", nullable=true, scale=2, length=19)
    public BigDecimal getRealFreezeMoney() {
        return realFreezeMoney;
    }

    public void setRealFreezeMoney(BigDecimal realFreezeMoney) {
        this.realFreezeMoney = realFreezeMoney;
    }

    @Column(name ="FREEZE_START_DATE", nullable=true)
    public Date getFreezeStartDate() {
        return freezeStartDate;
    }

    public void setFreezeStartDate(Date freezeStartDate) {
        this.freezeStartDate = freezeStartDate;
    }

    @Column(name ="FREEZE_END_DATE", nullable=true)
    public Date getFreezeEndDate() {
        return freezeEndDate;
    }

    public void setFreezeEndDate(Date freezeEndDate) {
        this.freezeEndDate = freezeEndDate;
    }

    @Column(name ="FREEZE_RESSION", nullable=true)
    public String getFreezeRession() {
        return freezeRession;
    }

    public void setFreezeRession(String freezeRession) {
        this.freezeRession = freezeRession;
    }

    @Column(name ="CUR_ID", nullable=true, length=32)
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name ="STATUS", nullable=true)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name ="FREEZE_STATUS", nullable=true)
    public Integer getFreezeStatus() {
        return freezeStatus;
    }

    public void setFreezeStatus(Integer freezeStatus) {
        this.freezeStatus = freezeStatus;
    }

    @Column(name ="IS_FREEZE_COMPLETE", nullable=true, length=1)
    public String getIsFreezeComplete() {
        return isFreezeComplete;
    }

    public void setIsFreezeComplete(String isFreezeComplete) {
        this.isFreezeComplete = isFreezeComplete;
    }

    @Column(name ="IS_UNFREEZE", nullable=true, length=1)
    public String getIsUnfreeze() {
        return isUnfreeze;
    }

    public void setIsUnfreeze(String isUnfreeze) {
        this.isUnfreeze = isUnfreeze;
    }

    @Column(name ="CREATE_BY", nullable=true, length=32)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name ="CREATE_DATE", nullable=true)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name ="UPDATE_BY", nullable=true, length=32)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name ="UPDATE_DATE", nullable=true)
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
