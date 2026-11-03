package com.bytter.bill.domain.billPool.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: 额度分配核准Entity
 * @Description: 额度分配核准
 * @date 2020-2-24
 */
@Entity
@Table(name = "AB_POOL_LIMIT_ALLOTCHECK", schema = "")
@SuppressWarnings("serial")
public class AbPoolLimitAllotCheck extends IdEntity implements java.io.Serializable {
    /**单位ID**/
    private String corpId;
    /**
     * 票据池Id
     */
    private String bpId;
    /**
     * 业务类型ID
     */
    private String busTypeId;
    /**
     * 额度种类:1：低风险， 2：非低风险
     */
    private String limtType;
    /**
     * 总额度
     */
    private BigDecimal poolMoney;
    /**
     * 可用额度
     */
    private BigDecimal validMoney;
    /**
     * 已用额度
     */
    private BigDecimal lockMoney;
    /**
     * 申请额度
     */
    private BigDecimal applyMoney;
    /**
     * 到期日期
     */
    private Date endDate;
    /**
     * 核准类型：1通过0不通过
     */
    private String checkType;
    /**
     * 核准信息
     */
    private String checkMsg;
    /**
     * 银行返回代码
     */
    private String returnCode;
    /**
     * 银行返回消息
     */
    private String returnMsg;
    /**
     * 指令状态：待发送16，发送失败-1，银行处理中18，银行交易成功19，银行交易失败-3
     */
    private String optStatus;
    /**
     * 审批状态：0暂存，-1打回，-2删除，11审批中，95审批通过
     */
    private Integer status;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 创建人名称
     */
    private String createId;
    /**
     * 创建日期
     */
    private Date createTime;
    /**
     * 更新人名称
     */
    private String updateId;
    /**
     * 更新日期
     */
    private Date updateTime;
    @Column(name = "CORP_ID", nullable = true, length = 32)
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "BP_ID", nullable = true, length = 32)
    public String getBpId() {
        return bpId;
    }
    public void setBpId(String bpId) {
        this.bpId = bpId;
    }
    @Column(name = "BUSTYPE_ID", nullable = true, length = 32)
    public String getBusTypeId() {
        return busTypeId;
    }
    public void setBusTypeId(String busTypeId) {
        this.busTypeId = busTypeId;
    }
    @Column(name = "LIMT_TYPE", nullable = true, length = 2)
    public String getLimtType() {
        return limtType;
    }
    public void setLimtType(String limtType) {
        this.limtType = limtType;
    }
    @Column(name ="POOL_MONEY",nullable=true,scale=2,length=15)
    public BigDecimal getPoolMoney() {
        return poolMoney;
    }
    public void setPoolMoney(BigDecimal poolMoney) {
        this.poolMoney = poolMoney;
    }
    @Column(name ="VALID_MONEY",nullable=true,scale=2,length=15)
    public BigDecimal getValidMoney() {
        return validMoney;
    }
    public void setValidMoney(BigDecimal validMoney) {
        this.validMoney = validMoney;
    }
    @Column(name ="LOCK_MONEY",nullable=true,scale=2,length=15)
    public BigDecimal getLockMoney() {
        return lockMoney;
    }
    public void setLockMoney(BigDecimal lockMoney) {
        this.lockMoney = lockMoney;
    }
    @Column(name ="APPLY_MONEY",nullable=true,scale=2,length=15)
    public BigDecimal getApplyMoney() {
        return applyMoney;
    }
    public void setApplyMoney(BigDecimal applyMoney) {
        this.applyMoney = applyMoney;
    }
    @Column(name = "END_DATE", nullable = true)
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    @Column(name = "CHECK_TYPE", nullable = true, length = 2)
    public String getCheckType() {
        return checkType;
    }
    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }
    @Column(name = "CHECK_MSG", nullable = true, length = 200)
    public String getCheckMsg() {
        return checkMsg;
    }
    public void setCheckMsg(String checkMsg) {
        this.checkMsg = checkMsg;
    }
    @Column(name = "RETURN_CODE", nullable = true, length = 2)
    public String getReturnCode() {
        return returnCode;
    }
    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }
    @Column(name = "RETURN_MSG", nullable = true, length = 2)
    public String getReturnMsg() {
        return returnMsg;
    }
    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }
    @Column(name = "OPT_STATUS", nullable = true, length = 2)
    public String getOptStatus() {
        return optStatus;
    }
    public void setOptStatus(String optStatus) {
        this.optStatus = optStatus;
    }
    @Column(name = "STATUS", nullable = true, length = 6)
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "RMK", nullable = true, length = 200)
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "CREATE_ID", nullable = true, length = 32)
    public String getCreateId() {
        return createId;
    }
    public void setCreateId(String createId) {
        this.createId = createId;
    }
    @Column(name = "CREATE_TIME", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Column(name = "UPDATE_ID", nullable = true, length = 32)
    public String getUpdateId() {
        return updateId;
    }
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    @Column(name = "UPDATE_TIME", nullable = true)
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    @Override
    public String toString() {
        return "AbPoolLimitAllotCheck{" +
                "corpId='" + corpId + '\'' +
                ", bpId='" + bpId + '\'' +
                ", busTypeId='" + busTypeId + '\'' +
                ", limtType='" + limtType + '\'' +
                ", poolMoney=" + poolMoney +
                ", validMoney=" + validMoney +
                ", lockMoney=" + lockMoney +
                ", applyMoney=" + applyMoney +
                ", endDate=" + endDate +
                ", checkType='" + checkType + '\'' +
                ", checkMsg='" + checkMsg + '\'' +
                ", returnCode='" + returnCode + '\'' +
                ", returnMsg='" + returnMsg + '\'' +
                ", optStatus='" + optStatus + '\'' +
                ", status=" + status +
                ", rmk='" + rmk + '\'' +
                ", createId='" + createId + '\'' +
                ", createTime=" + createTime +
                ", updateId='" + updateId + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
