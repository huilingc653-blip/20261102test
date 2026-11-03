package com.bytter.biz.fundwarn.vo;
import java.io.Serializable;
import java.util.Date;
public class HomeWarnDataSearchVO implements Serializable {
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 预警类型
     */
    private String typeCode;
    /**
     * 预警维度
     */
    private String dimensionCode;
    /**
     * 开始日期
     */
    private Date beginDate;
    /**
     * 结束日期
     */
    private Date endDate;
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getTypeCode() {
        return typeCode;
    }
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
    public String getDimensionCode() {
        return dimensionCode;
    }
    public void setDimensionCode(String dimensionCode) {
        this.dimensionCode = dimensionCode;
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
}
