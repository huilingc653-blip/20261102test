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
@Table(name = "FEX_EXPOSURE", schema = "")
public class FexExposure extends IdEntity implements java.io.Serializable {
    /**单位敞口模板ID*/
    private String mainId;
    /**币别ID*/
    private String curId;
    /**排序*/
    private Integer sort;
    /**开始时间*/
    private Date beginDate;
    /**结束时间*/
    private Date endDate;
    /**周期单位 1-年 2-季 3-月*/
    private String fillUnit;

    @Column(name ="MAIN_ID")
    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    @Column(name ="CUR_ID")
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name = "SORT", columnDefinition = "INT default 500")
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Column(name ="BEGIN_DATE",nullable=false)
    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    @Column(name ="END_DATE",nullable=false)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column(name ="FILL_UNIT")
    public String getFillUnit() {
        return fillUnit;
    }

    public void setFillUnit(String fillUnit) {
        this.fillUnit = fillUnit;
    }
}
