package com.bytter.report.domain.cms.vo;

import com.bytter.core.annotation.Dict;


/**
 * @author luxy
 * @Title: RatBkCalculateSearchVO
 * @ProjectName T3.4
 * @Description: TODO
 * @date 2020/7/28 11:01
 */
public class RatBkCalculateSearchVO  {
    /** 授信银行 */
    private java.lang.String bankCode;
    /** 银行名称 */
    private java.lang.String bankName;
    /** 授信单位 */
    private java.lang.String corpId;
    /** 单位名称 */
    private java.lang.String corpName;
    /** 授信品种*/
    @Dict(tableName="RAT_ITEM_CUSTOM",valueFieldName="id",showFiledName="NAME",inject="itemName")
    private String itemCode;
    private String itemName;

    /** 授信币种 */
    private java.lang.String curCode;
    /** 授信币种 */
    private java.lang.String curName;
    /** 是否折合币别*/
    private String isConvertCur;
    /** 折合币别*/
    private String convertCurId;
    /** 预测结束日期 */
    private String calEndDate;
    /** 汇率类型 */
    private String exRateType;
    /** 查询标识 */
    private String searchFlag;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getIsConvertCur() {
        return isConvertCur;
    }

    public void setIsConvertCur(String isConvertCur) {
        this.isConvertCur = isConvertCur;
    }

    public String getConvertCurId() {
        return convertCurId;
    }

    public void setConvertCurId(String convertCurId) {
        this.convertCurId = convertCurId;
    }

    public String getCalEndDate() {
        return calEndDate;
    }

    public void setCalEndDate(String calEndDate) {
        this.calEndDate = calEndDate;
    }

    public String getExRateType() {
        return exRateType;
    }

    public void setExRateType(String exRateType) {
        this.exRateType = exRateType;
    }

    public String getSearchFlag() {
        return searchFlag;
    }

    public void setSearchFlag(String searchFlag) {
        this.searchFlag = searchFlag;
    }
}
