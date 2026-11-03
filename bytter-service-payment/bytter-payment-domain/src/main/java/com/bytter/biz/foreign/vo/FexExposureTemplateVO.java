package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.check.vo.IBaseDataInject;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Title: VO
 * @Description: 敞口模板表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 *
 */
public class FexExposureTemplateVO implements IBaseDataInject, java.io.Serializable {
    /**模板Id*/
    private String id;
    /**模板代码*/
    @BtCompositeSearch(fieldName = "f.template_Code")
    private String templateCode;
    /**模板名称*/
    @BtCompositeSearch(fieldName = "f.template_Name")
    private String templateName;
    /**前面按周期填报，后续是否按年度填报 0-否 1-是*/
    private String isYearFill;
    /**前面按周期填报，后续是否按总额填报 0-否 1-是*/
    private String isTotalFill;
    /**总填报周期*/
    @BtCompositeSearch(fieldName = "f.total_Fill_Period")
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
    /**有效标志 0 无效 1 有效*/
    @BtCompositeSearch(fieldName = "f.valid_Sign")
    private String validSign;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    @BtCompositeSearch(fieldName = "f.create_Date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    @BtCompositeSearch(fieldName = "f.update_Date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;
    /**模板对应科目信息*/
    List<FexExposureTemplateItemVO> listItem = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
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

    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
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

    public List<FexExposureTemplateItemVO> getListItem() {
        return listItem;
    }

    public void setListItem(List<FexExposureTemplateItemVO> listItem) {
        this.listItem = listItem;
    }
}
