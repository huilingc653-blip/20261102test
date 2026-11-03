package com.bytter.finance.domain.lease.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
@Entity
@Table(name = "CMS_LEASE_EXTENDED", schema = "")
@SuppressWarnings("serial")
public class LeaseExtend extends IdEntity implements java.io.Serializable {
    /** 租赁表ID */
    private String leaseId;
    /**
     * 上次展期时间
     */
    private Date lastExpendedDate;
    /** 展期到期日期 */
    private Date expendedDate;
    /** 展期合同编号 */
    private String expendedCode;
    /** 展期期限 */
    private String expendedLimit;
    /** 期限单位 */
    private String ternUnit;
    /** 点数或者比例 */
    private String pointsProportionSelect;
    /** 点数或者比例值 */
    private BigDecimal pointsProportionText;
    /** 利率 */
    private BigDecimal rate;
    /** 状态 */
    private Integer status;
    /** 备注 */
    private String rmk;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 修改日期
     */
    private Date updateDate;
    @Column(name = "LEASE_ID")
    public String getLeaseId() {
        return leaseId;
    }
    public void setLeaseId(String leaseId) {
        this.leaseId = leaseId;
    }
    @Column(name = "LAST_EXPENDED_DATE")
    public Date getLastExpendedDate() {
        return lastExpendedDate;
    }
    public void setLastExpendedDate(Date lastExpendedDate) {
        this.lastExpendedDate = lastExpendedDate;
    }
    @Column(name = "EXPENDED_DATE")
    public Date getExpendedDate() {
        return expendedDate;
    }
    public void setExpendedDate(Date expendedDate) {
        this.expendedDate = expendedDate;
    }
    @Column(name = "EXPENDED_CODE")
    public String getExpendedCode() {
        return expendedCode;
    }
    public void setExpendedCode(String expendedCode) {
        this.expendedCode = expendedCode;
    }
    @Column(name = "EXPENDED_LIMIT")
    public String getExpendedLimit() {
        return expendedLimit;
    }
    public void setExpendedLimit(String expendedLimit) {
        this.expendedLimit = expendedLimit;
    }
    @Column(name = "TERN_UNIT")
    public String getTernUnit() {
        return ternUnit;
    }
    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }
    @Column(name = "POINTS_PROPORTION_SELECT")
    public String getPointsProportionSelect() {
        return pointsProportionSelect;
    }
    public void setPointsProportionSelect(String pointsProportionSelect) {
        this.pointsProportionSelect = pointsProportionSelect;
    }
    @Column(name = "POINTS_PROPORTION_TEXT")
    public BigDecimal getPointsProportionText() {
        return pointsProportionText;
    }
    public void setPointsProportionText(BigDecimal pointsProportionText) {
        this.pointsProportionText = pointsProportionText;
    }
    @Column(name = "RATE")
    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public LeaseExtend() {
    }
}
