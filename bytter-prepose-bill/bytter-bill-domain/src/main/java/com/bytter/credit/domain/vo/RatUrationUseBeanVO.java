package com.bytter.credit.domain.vo;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 授信占用DTO
 * 
 * @author liyh
 *
 */
public class RatUrationUseBeanVO implements Serializable {
    private String id;
    /**
     * 占用编号
     */
    private String urationCode;
    /** 业务名称 */
    private String type;
    private String typeName;
    /** 业务名称 */
    private String businessName;
    /** 单位代码 */
    private String corpId;
    private String corpName;
    /** 额度类型 */
    private String useType;
    /** 占用授信类型 */
    private String ratType;
    /** 授信额度编号 */
    private String ratCode;
    /** 品种标识 */
    private String itemFlag;
    /** 品种标识 */
    @Dict(tableName = "RAT_ITEM_CUSTOM", valueFieldName = "id", showFiledName = "NAME", inject = "itemName")
    private String itemCode;
    private String itemName;
    /** 银行编号 */
    private String bankCode;
    private String bankName;
    /** 币别 */
//    @Currency(inject = "curName")
    private String curCode;
    private String curName;
    /** 币别 */
//    @Currency(inject = "initCurName")
    private String initCurCode;
    private String initCurName;
    /** 占用金额 */
    private BigDecimal money;
    /** 敞口金额 */
    private BigDecimal openMoney;
    /** 业务金额 */
    private BigDecimal convertPreMoney;
    /** 汇率 */
    private BigDecimal exchangeRate;
    /** 开始日期 */
    private java.util.Date beginDate;
    /** 结束日期 */
    private java.util.Date endDate;
    /** 状态 */
    private Integer status;
    /** 票据号 */
    private String billNo;
    /** 业务金额 */
    private BigDecimal billMoney;
    /** 额度类型 0-不循环 1-循环 */
    private String creditType;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUrationCode() {
        return urationCode;
    }
    public void setUrationCode(String urationCode) {
        this.urationCode = urationCode;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getBusinessName() {
        return businessName;
    }
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
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
    public String getUseType() {
        return useType;
    }
    public void setUseType(String useType) {
        this.useType = useType;
    }
    public String getRatType() {
        return ratType;
    }
    public void setRatType(String ratType) {
        this.ratType = ratType;
    }
    public String getRatCode() {
        return ratCode;
    }
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }
    public String getItemFlag() {
        return itemFlag;
    }
    public void setItemFlag(String itemFlag) {
        this.itemFlag = itemFlag;
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
    public String getInitCurCode() {
        return initCurCode;
    }
    public void setInitCurCode(String initCurCode) {
        this.initCurCode = initCurCode;
    }
    public String getInitCurName() {
        return initCurName;
    }
    public void setInitCurName(String initCurName) {
        this.initCurName = initCurName;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public BigDecimal getOpenMoney() {
        return openMoney;
    }
    public void setOpenMoney(BigDecimal openMoney) {
        this.openMoney = openMoney;
    }
    public BigDecimal getConvertPreMoney() {
        return convertPreMoney;
    }
    public void setConvertPreMoney(BigDecimal convertPreMoney) {
        this.convertPreMoney = convertPreMoney;
    }
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }
    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
    public java.util.Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(java.util.Date beginDate) {
        this.beginDate = beginDate;
    }
    public java.util.Date getEndDate() {
        return endDate;
    }
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public BigDecimal getBillMoney() {
        return billMoney;
    }
    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }
    public String getCreditType() {
        return creditType;
    }
    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }
}
