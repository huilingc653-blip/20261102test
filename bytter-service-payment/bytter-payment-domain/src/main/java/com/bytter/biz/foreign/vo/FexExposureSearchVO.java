package com.bytter.biz.foreign.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: VO
 * @Description: 单位敞口上报表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 */
public class FexExposureSearchVO implements Serializable {
    /**ID*/
    private String id;
    /**币种*/
    private String curId;
    /**科目*/
    private String itemCode;
    /**敞口周期-开始日期*/
    private Date beginDate;
    /**敞口周期-结束日期*/
    private Date endDate;
    /**汇总维度*/
    private String sumUnit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getSumUnit() {
        return sumUnit;
    }

    public void setSumUnit(String sumUnit) {
        this.sumUnit = sumUnit;
    }
}
