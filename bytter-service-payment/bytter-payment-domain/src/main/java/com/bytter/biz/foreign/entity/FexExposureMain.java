package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 单位敞口上报表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 *
 */
@Entity
@Table(name = "FEX_EXPOSURE_MAIN", schema = "")
public class FexExposureMain extends IdEntity implements java.io.Serializable {
    /**单位ID*/
    private String corpId;
    /**币别ID*/
    private String curId;
    /**模板ID*/
    private String templateId;
    /**编制时间*/
    private Date enrolDate;
    /**状态*/
    private Integer status;
    /**是否滚动填报 0-否 1-是*/
    private String isScrollFill;
    /**前面按周期填报，后续是否按年度填报 0-否 1-是*/
    private String isYearFill;
    /**前面按周期填报，后续是否按总额填报 0-否 1-是*/
    private String isTotalFill;
    /**总填报周期*/
    private Integer totalFillPeriod;
    /**总填报周期单位*/
    private String totalFillUnit;
    /**滚动总周期*/
    private Integer scrollFillPeriod;
    /**滚动总周期单位*/
    private String scrollFillUnit;
    /**滚动分解子周期*/
    private Integer scrollResolvePeriod;
    /**滚动分解子周期单位*/
    private String scrollResolveUnit;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;

    @Column(name ="CORP_ID")
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name ="CUR_ID")
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name ="TEMPLATE_ID")
    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    @Column(name ="ENROL_DATE",nullable=false)
    public Date getEnrolDate() {
        return enrolDate;
    }

    public void setEnrolDate(Date enrolDate) {
        this.enrolDate = enrolDate;
    }

    @Column(name ="STATUS")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name ="IS_SCROLL_FILL")
    public String getIsScrollFill() {
        return isScrollFill;
    }

    public void setIsScrollFill(String isScrollFill) {
        this.isScrollFill = isScrollFill;
    }

    @Column(name ="IS_YEAR_FILL")
    public String getIsYearFill() {
        return isYearFill;
    }

    public void setIsYearFill(String isYearFill) {
        this.isYearFill = isYearFill;
    }

    @Column(name ="IS_TOTAL_FILL")
    public String getIsTotalFill() {
        return isTotalFill;
    }

    public void setIsTotalFill(String isTotalFill) {
        this.isTotalFill = isTotalFill;
    }

    @Column(name ="TOTAL_FILL_PERIOD")
    public Integer getTotalFillPeriod() {
        return totalFillPeriod;
    }

    public void setTotalFillPeriod(Integer totalFillPeriod) {
        this.totalFillPeriod = totalFillPeriod;
    }

    @Column(name ="TOTAL_FILL_UNIT")
    public String getTotalFillUnit() {
        return totalFillUnit;
    }

    public void setTotalFillUnit(String totalFillUnit) {
        this.totalFillUnit = totalFillUnit;
    }

    @Column(name ="SCROLL_FILL_PERIOD")
    public Integer getScrollFillPeriod() {
        return scrollFillPeriod;
    }

    public void setScrollFillPeriod(Integer scrollFillPeriod) {
        this.scrollFillPeriod = scrollFillPeriod;
    }

    @Column(name ="SCROLL_FILL_UNIT")
    public String getScrollFillUnit() {
        return scrollFillUnit;
    }

    public void setScrollFillUnit(String scrollFillUnit) {
        this.scrollFillUnit = scrollFillUnit;
    }

    @Column(name ="SCROLL_RESOLVE_PERIOD")
    public Integer getScrollResolvePeriod() {
        return scrollResolvePeriod;
    }

    public void setScrollResolvePeriod(Integer scrollResolvePeriod) {
        this.scrollResolvePeriod = scrollResolvePeriod;
    }

    @Column(name ="SCROLL_RESOLVE_UNIT")
    public String getScrollResolveUnit() {
        return scrollResolveUnit;
    }

    public void setScrollResolveUnit(String scrollResolveUnit) {
        this.scrollResolveUnit = scrollResolveUnit;
    }
    @Column(name ="CREATE_BY",nullable=true,length=50)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name ="CREATE_DATE",nullable=true)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name ="UPDATE_BY",nullable=true,length=50)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name ="UPDATE_DATE",nullable=true)
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
