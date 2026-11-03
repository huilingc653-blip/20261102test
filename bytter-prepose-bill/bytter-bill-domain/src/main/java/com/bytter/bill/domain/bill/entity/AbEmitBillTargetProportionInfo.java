package com.bytter.bill.domain.bill.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
* @Description 银承开票占比目标维护表
* @author zhous
* @date 2025/4/22 10:23
*/
@Entity
@Table(name = "ab_emit_bill_target_ratio_info", schema = "")
@SuppressWarnings("serial")
public class AbEmitBillTargetProportionInfo extends IdEntity implements java.io.Serializable {

    /**单位ID*/
    private String corpId;
    /**单位编码*/
    private String corpCode;
    /**单位名称*/
    private String corpName;
    /**当前月份*/
    private String currentYear;
    /**目标占比*/
    private BigDecimal targetProportion;
    /**是否删除 1-是*/
    private String isDelete;
    /**创建人*/
    private String createBy;
    /**创建时间*/
    private Date createDate;
    /**修改人*/
    private String updateBy;
    /**修改时间*/
    private Date updateDate;

    @Column(name = "IS_DELETE")
    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    @Column(name = "CORP_CODE")
    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    @Column(name = "TARGET_PROPORTION")
    public BigDecimal getTargetProportion() {
        return targetProportion;
    }

    public void setTargetProportion(BigDecimal targetProportion) {
        this.targetProportion = targetProportion;
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

    @Column(name = "CURRENT_YEAR")
    public String getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(String currentYear) {
        this.currentYear = currentYear;
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
