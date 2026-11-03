package com.bytter.report.domain.netbank.vo;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
public class HomeBankAccDtlDistributeSearchVO implements Serializable {
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 单位ID集合
     */
    private List<String> corpIds;
    /**
     * 账号ID集合
     */
    private List<String> accIds;
    /**
     * 日期类型
     */
    private String dateType;
    /**
     * 查询日期
     */
    private Date searchDate;
    /**
     * 金额单位
     */
    private Integer moneyType;
    /**
     * 查询日期-开始日期
     */
    private Date beginDate;
    /**
     * 查询日期-结束日期
     */
    private Date endDate;
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public List<String> getCorpIds() {
        return corpIds;
    }
    public void setCorpIds(List<String> corpIds) {
        this.corpIds = corpIds;
    }
    public List<String> getAccIds() {
        return accIds;
    }
    public void setAccIds(List<String> accIds) {
        this.accIds = accIds;
    }
    public String getDateType() {
        return dateType;
    }
    public void setDateType(String dateType) {
        this.dateType = dateType;
    }
    public Date getSearchDate() {
        return searchDate;
    }
    public void setSearchDate(Date searchDate) {
        this.searchDate = searchDate;
    }
    public Integer getMoneyType() {
        return moneyType;
    }
    public void setMoneyType(Integer moneyType) {
        this.moneyType = moneyType;
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
