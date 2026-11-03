package com.bytter.bill.domain.bill.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 银行开票授信资料维护表
 *
 * @author fangcf
 * @date 2025-06-26
 */
@Entity
@Table(name = "AB_BILL_CREDIT_INFO_MAINTAIN", schema = "")
public class AbBillCreditInfoMaintain extends IdEntity implements java.io.Serializable {

    /**
     * 单位id
     */
    private String corpId;
    /**
     * 银行类型
     */
    private String bankTypeId;
    /**
     * 授信类型：敞口和低风险
     */
    private String creditType;
    /**
     * 保证金比例
     */
    private BigDecimal bondRatio;
    /**
     * 开票手续费
     */
    private BigDecimal openBillRate;
    /**
     * 保证金利率
     */
    private String bondRate;
    /**
     * 是否能正常使用：1-是，0-否
     */
    private String isUsed;
    /**
     * 无
     */
    private String isDeleted;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateTime;

    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name = "BANK_TYPE_ID")
    public String getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }

    @Column(name = "CREDIT_TYPE")
    public String getCreditType() {
        return creditType;
    }

    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }

    @Column(name = "BOND_RATIO")
    public BigDecimal getBondRatio() {
        return bondRatio;
    }

    public void setBondRatio(BigDecimal bondRatio) {
        this.bondRatio = bondRatio;
    }

    @Column(name = "OPEN_BILL_RATE")
    public BigDecimal getOpenBillRate() {
        return openBillRate;
    }

    public void setOpenBillRate(BigDecimal openBillRate) {
        this.openBillRate = openBillRate;
    }

    @Column(name = "BOND_RATE")
    public String getBondRate() {
        return bondRate;
    }

    public void setBondRate(String bondRate) {
        this.bondRate = bondRate;
    }

    @Column(name = "IS_USED")
    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    @Column(name = "IS_DELETED")
    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "UPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
