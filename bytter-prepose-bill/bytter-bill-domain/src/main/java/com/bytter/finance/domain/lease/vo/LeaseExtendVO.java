package com.bytter.finance.domain.lease.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class LeaseExtendVO implements Serializable, IBusinessKey {
    private String id;
    /** 租赁表ID */
    private String leaseId;
    /**
     * 上次展期时间
     */
    @BtCompositeSearch(fieldName = "M.LAST_EXPENDED_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lastExpendedDate;
    /** 展期到期日期 */
    @BtCompositeSearch(fieldName = "M.EXPENDED_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expendedDate;
    /** 到期日 */
    @BtCompositeSearch(fieldName = "I.END_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /** 展期合同编号 */
    @BtCompositeSearch(fieldName = "M.EXPENDED_CODE")
    private String expendedCode;
    /** 展期期限 */
    @BtCompositeSearch(fieldName = "M.EXPENDED_LIMIT")
    private String expendedLimit;
    /** 期限单位 */
    @BtCompositeSearch(fieldName = "M.TERN_UNIT")
    private String ternUnit;
    /** 点数或者比例 */
    @BtCompositeSearch(fieldName = "M.POINTS_PROPORTION_SELECT")
    private String pointsProportionSelect;
    /** 点数或者比例值 */
    @BtCompositeSearch(fieldName = "M.POINTS_PROPORTION_TEXT")
    private BigDecimal pointsProportionText;
    /** 利率 */
    @BtCompositeSearch(fieldName = "M.RATE")
    private BigDecimal rate;
    /** 状态 */
    @BtCompositeSearch(fieldName = "M.STATUS")
    private Integer status;
    /** 备注 */
    @BtCompositeSearch(fieldName = "M.RMK")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /**
     * 修改日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;
    /**
     * 融资租赁单号
     */
    @BtCompositeSearch(fieldName = "I.LEASE_NO")
    private String leaseNo;
    /**
     * 融资租赁类型
     */
    @BtCompositeSearch(fieldName = "I.LEASE_TYPE")
    private String leaseType;
    /**
     * 融资租赁合同编号
     */
    @BtCompositeSearch(fieldName = "I.LEASE_CODE")
    private String leaseCode;
    /**
     * 买卖合同编号
     */
    @BtCompositeSearch(fieldName = "I.PAY_CODE")
    private String payCode;
    /**
     * 承租人
     */
    @BtCompositeSearch(fieldName = "I.CORP_ID")
    @Corp(inject = "corpName")
    private String corpId;
    @BtCompositeSearch(fieldName = "I.CORP_NAME")
    private String corpName;
    /**
     * 出租人
     */
    @BtCompositeSearch(fieldName = "I.LESSOR")
    private String lessor;
    /**
     * 供货商
     */
    @BtCompositeSearch(fieldName = "I.SUPPLIER")
    private String supplier;
    /**
     * 展期次数
     */
    @BtCompositeSearch(fieldName = "I.EXPENDED_COUNT")
    private Integer expendedCount;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLeaseId() {
        return leaseId;
    }
    public void setLeaseId(String leaseId) {
        this.leaseId = leaseId;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Date getLastExpendedDate() {
        return lastExpendedDate;
    }
    public void setLastExpendedDate(Date lastExpendedDate) {
        this.lastExpendedDate = lastExpendedDate;
    }
    public Date getExpendedDate() {
        return expendedDate;
    }
    public void setExpendedDate(Date expendedDate) {
        this.expendedDate = expendedDate;
    }
    public String getExpendedCode() {
        return expendedCode;
    }
    public void setExpendedCode(String expendedCode) {
        this.expendedCode = expendedCode;
    }
    public String getExpendedLimit() {
        return expendedLimit;
    }
    public void setExpendedLimit(String expendedLimit) {
        this.expendedLimit = expendedLimit;
    }
    public String getTernUnit() {
        return ternUnit;
    }
    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }
    public String getPointsProportionSelect() {
        return pointsProportionSelect;
    }
    public void setPointsProportionSelect(String pointsProportionSelect) {
        this.pointsProportionSelect = pointsProportionSelect;
    }
    public BigDecimal getPointsProportionText() {
        return pointsProportionText;
    }
    public void setPointsProportionText(BigDecimal pointsProportionText) {
        this.pointsProportionText = pointsProportionText;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getLeaseNo() {
        return leaseNo;
    }
    public void setLeaseNo(String leaseNo) {
        this.leaseNo = leaseNo;
    }
    public String getLeaseType() {
        return leaseType;
    }
    public void setLeaseType(String leaseType) {
        this.leaseType = leaseType;
    }
    public String getLeaseCode() {
        return leaseCode;
    }
    public void setLeaseCode(String leaseCode) {
        this.leaseCode = leaseCode;
    }
    public String getPayCode() {
        return payCode;
    }
    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getLessor() {
        return lessor;
    }
    public void setLessor(String lessor) {
        this.lessor = lessor;
    }
    public String getSupplier() {
        return supplier;
    }
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    public Integer getExpendedCount() {
        return expendedCount;
    }
    public void setExpendedCount(Integer expendedCount) {
        this.expendedCount = expendedCount;
    }
    @Override
    public String getBusinessKey() {
        return getId();
    }
}
