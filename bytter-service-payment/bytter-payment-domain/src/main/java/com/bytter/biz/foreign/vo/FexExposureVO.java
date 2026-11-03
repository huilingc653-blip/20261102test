package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Currencys;
import com.bytter.check.vo.IBaseDataInject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Title: VO
 * @Description: 单位敞口上报表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 */
public class FexExposureVO implements IBaseDataInject, java.io.Serializable  {
    /**ID*/
    private String id;
    /**单位敞口主ID*/
    private String mainId;
    /**币别ID*/
    @BtCompositeSearch(fieldName = "t.cur_Id")
    @Currencys(
            {
                    @Currency(inject="curName"),
                    @Currency(inject="englishCode", field = "englishCode")
            }
    )
    private String curId;
    private String curName;
    private String englishCode;
    /**排序*/
    private Integer sort;
    /**开始时间*/
    private Date beginDate;
    /**结束时间*/
    private Date endDate;
    /**周期单位 1-年 2-季 3-月 4-周 5-天*/
    private String fillUnit;
    /**敞口上报明细*/
    private List<FexExposureDetailVO> listDtl = new ArrayList<>();
    /**周期*/
    private String exposurePeriod;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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

    public String getFillUnit() {
        return fillUnit;
    }

    public void setFillUnit(String fillUnit) {
        this.fillUnit = fillUnit;
    }

    public List<FexExposureDetailVO> getListDtl() {
        return listDtl;
    }

    public void setListDtl(List<FexExposureDetailVO> listDtl) {
        this.listDtl = listDtl;
    }

    public String getExposurePeriod() {
        return exposurePeriod;
    }

    public void setExposurePeriod(String exposurePeriod) {
        this.exposurePeriod = exposurePeriod;
    }
}
