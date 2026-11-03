package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.check.vo.IBaseDataInject;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @Title: VO
 * @Description: 单位敞口上报表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 *
 */
public class FexExposureMainVO implements IBaseDataInject, java.io.Serializable, IBusinessKey {
    /**ID*/
    private String id;
    /**单位ID*/
    @BtCompositeSearch(fieldName = "t.corp_Id")
    @Corp(inject = "corpName")
    private String corpId;
    @BtCompositeSearch(fieldName = "t.corp_Id")
    private String corpName;
    /**币别ID*/
    private String curId;
    private String curName;
    private String englishCode;
    /**模板ID*/
    @BtCompositeSearch(fieldName = "t.template_Id")
    private String templateId;
    @BtCompositeSearch(fieldName = "tm.template_Name")
    private String templateName;
    /**编制时间*/
    @BtCompositeSearch(fieldName = "t.enrol_Date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date enrolDate;
    /**状态*/
    @Dict(dictGroup = "nisBillhead.status", inject = "statusName")
    @BtCompositeSearch(fieldName = "t.status")
    private Integer status;
    @BtCompositeSearch(fieldName = "t.status")
    private String statusName;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getEnglishCode() {
        return englishCode;
    }

    public void setEnglishCode(String englishCode) {
        this.englishCode = englishCode;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Date getEnrolDate() {
        return enrolDate;
    }

    public void setEnrolDate(Date enrolDate) {
        this.enrolDate = enrolDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getIsScrollFill() {
        return isScrollFill;
    }

    public void setIsScrollFill(String isScrollFill) {
        this.isScrollFill = isScrollFill;
    }

    public String getIsYearFill() {
        return isYearFill;
    }

    public void setIsYearFill(String isYearFill) {
        this.isYearFill = isYearFill;
    }

    public String getIsTotalFill() {
        return isTotalFill;
    }

    public void setIsTotalFill(String isTotalFill) {
        this.isTotalFill = isTotalFill;
    }

    public Integer getTotalFillPeriod() {
        return totalFillPeriod;
    }

    public void setTotalFillPeriod(Integer totalFillPeriod) {
        this.totalFillPeriod = totalFillPeriod;
    }

    public String getTotalFillUnit() {
        return totalFillUnit;
    }

    public void setTotalFillUnit(String totalFillUnit) {
        this.totalFillUnit = totalFillUnit;
    }

    public Integer getScrollFillPeriod() {
        return scrollFillPeriod;
    }

    public void setScrollFillPeriod(Integer scrollFillPeriod) {
        this.scrollFillPeriod = scrollFillPeriod;
    }

    public String getScrollFillUnit() {
        return scrollFillUnit;
    }

    public void setScrollFillUnit(String scrollFillUnit) {
        this.scrollFillUnit = scrollFillUnit;
    }

    public Integer getScrollResolvePeriod() {
        return scrollResolvePeriod;
    }

    public void setScrollResolvePeriod(Integer scrollResolvePeriod) {
        this.scrollResolvePeriod = scrollResolvePeriod;
    }

    public String getScrollResolveUnit() {
        return scrollResolveUnit;
    }

    public void setScrollResolveUnit(String scrollResolveUnit) {
        this.scrollResolveUnit = scrollResolveUnit;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String getBusinessKey() {
        return getId();
    }
}
