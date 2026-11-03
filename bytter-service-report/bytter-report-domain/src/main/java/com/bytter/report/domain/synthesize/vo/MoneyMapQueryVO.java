package com.bytter.report.domain.synthesize.vo;
import java.util.Date;
/**
 * 资金地图查询条件VO对象
 *
 * @author zuoy
 * @since 2020-02-11
 */
public class MoneyMapQueryVO {
    /**
     * 单位ID
     */
   private String corpId;
    /**
     * 余额日期
     */
    private Date balanceDate;
    /**
     * 单位ID
     */
   private String curId;
    /**
     * 金额单位
     */
   private String moneyUnit;
    /**
     * 开户行名称
     */
   private String bankName;
    /**
     * 账号
     */
   private String account;
    /**
     * 余额类型 1-内部存款、2-银行存款
     */
   private String balanceType;
    /**
     * 统计维度 1-按单位统计、2-按银行统计
     */
   private String statisticalDimension;
    /**
     * 展示方式 1-按表格展示、2-按地图展示
     */
   private String showType;
    /**
     * 省份名称
     */
   private String provName;
    /**
     * 国家所属大区
     */
    private String continent;
    /**
     * 国家名称显示语言  zh-中文 en-英文
     */
    private String countryNameLanguage;
    /**
     * 折合币种
     */
    private String reducedCurId;
    public String getReducedCurId() {
        return reducedCurId;
    }
    public void setReducedCurId(String reducedCurId) {
        this.reducedCurId = reducedCurId;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public Date getBalanceDate() {
        return balanceDate;
    }
    public void setBalanceDate(Date balanceDate) {
        this.balanceDate = balanceDate;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getMoneyUnit() {
        return moneyUnit;
    }
    public void setMoneyUnit(String moneyUnit) {
        this.moneyUnit = moneyUnit;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getBalanceType() {
        return balanceType;
    }
    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }
    public String getStatisticalDimension() {
        return statisticalDimension;
    }
    public void setStatisticalDimension(String statisticalDimension) {
        this.statisticalDimension = statisticalDimension;
    }
    public String getShowType() {
        return showType;
    }
    public void setShowType(String showType) {
        this.showType = showType;
    }
    public String getProvName() {
        return provName;
    }
    public void setProvName(String provName) {
        this.provName = provName;
    }
    public String getContinent() {
        return continent;
    }
    public void setContinent(String continent) {
        this.continent = continent;
    }
    public String getCountryNameLanguage() {
        return countryNameLanguage;
    }
    public void setCountryNameLanguage(String countryNameLanguage) {
        this.countryNameLanguage = countryNameLanguage;
    }
}
