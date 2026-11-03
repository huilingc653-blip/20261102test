package com.bytter.credit.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 授信分配品种明细视图
 * 
 * @author liyh
 *
 */
public class RatItemDisVO implements Serializable {
    /**
     * 业务id
     */
    private String billId;
    /**
     * 授信分配编号
     */
    private String disCode;
    /**
     * 授信品种
     */
    @BtCompositeSearch(fieldName = "tab.item_code")
    @Dict(tableName = "RAT_ITEM_CUSTOM", valueFieldName = "id", showFiledName = "NAME", inject = "itemName")
    private String itemCode;
    /**
     * 授信品种名称
     */
    private String itemName;
    /**
     * 业务品种名称
     */
    @Dict(tableName = "RAT_BUSINESS_RACE", valueFieldName = "id", showFiledName = "RACE_NAME", inject = "raceName")
    private String raceId;
    /**
     * 业务品种名称
     */
    private String raceName;
    /**
     * 授信品种综合标识
     */
    private Integer itemOrder;
    /**
     * 授信品种综合标识
     */
    private String itemFlag;
    /**
     * 授信品种额度
     */
//    @BtCompositeSearch(fieldName = "tab.money")
    private BigDecimal money;
    /**
     * 授信品种敞口额度
     */
    private BigDecimal openMoney;
    /**
     * 授信品种最高额度
     */
    private BigDecimal maxMoney;
    /**
     * 授信品种综合使用标识
     */
    private String compSign;
    /** 授信开始日期 **/
    @BtCompositeSearch(fieldName = "tab.begin_date")
    private Date beginDate;
    /** 授信结束日期 **/
    @BtCompositeSearch(fieldName = "tab.end_date")
    private Date endDate;
    /** 保证金比例 */
    private BigDecimal assurepercent;
    /** 利率 */
    private BigDecimal interestrate;
    /** 费率 **/
    private BigDecimal procedureFeeRate;
    /** 系统授信品种 */
    private String sysItemCode;
    /** 保证金币种 */
    private String bondCurid;
    /** 保证金金额 */
    private BigDecimal bondMoney;
    /** 业务名称 */
    @BtCompositeSearch(fieldName = "tab.bill_name")
    private String billName;
    /** 业务币种 */
    @BtCompositeSearch(fieldName = "tab.init_cur_code")
    private String initCurCode;
    /** 是否启用授信敞口 0—不启用，1—启用 */
    private String isOpenRat;
    /** 业务金额 */
    @BtCompositeSearch(fieldName = "tab.bill_money")
    private BigDecimal billMoney;
    /** 汇率 */
    private BigDecimal exchangeRate;
    /** 备注 */
    private String remark;
    /** 银行授信品种 */
    private String sysItemName;
    /** 业务币种id */
//    @Currency(inject = "initCurName")
    private String initCurId;
    /** 业务币种名称 */
    private String initCurName;
    /** 单位id */
    @BtCompositeSearch(fieldName = "tab.corp_id")
    private String corpId;
    /** 单位名称 */
    private String corpName;
    /** 授信银行（列表取 rb.bank_type_name） */
    @BtCompositeSearch(fieldName = "tab.bank_type_name")
    private String bankName;
    /** 转换本位币金额 */
    private BigDecimal convertPreMoney;
    /** 担保方式 */
    private String guaWay;
    /**
     * 是否挂担保
     */
    private String isHangGuarantee;
    /**
     * SYSCODE
     */
    private String sysCode;
    /**
     * 授信编号
     */
    private String ratCode;
    /** 额度类型 0-不循环 1-循环 */
//    @Dict(dictGroup = "rat.creditType", inject = "creditTypeName")
    private String creditType;
    /** 额度类型 0-不循环 1-循环 */
    private String creditTypeName;
    public BigDecimal getProcedureFeeRate() {
        return procedureFeeRate;
    }
    public void setProcedureFeeRate(BigDecimal procedureFeeRate) {
        this.procedureFeeRate = procedureFeeRate;
    }
    public String getDisCode() {
        return disCode;
    }
    public void setDisCode(String disCode) {
        this.disCode = disCode;
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
    public Integer getItemOrder() {
        return itemOrder;
    }
    public void setItemOrder(Integer itemOrder) {
        this.itemOrder = itemOrder;
    }
    public String getItemFlag() {
        return itemFlag;
    }
    public void setItemFlag(String itemFlag) {
        this.itemFlag = itemFlag;
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
    public BigDecimal getMaxMoney() {
        return maxMoney;
    }
    public void setMaxMoney(BigDecimal maxMoney) {
        this.maxMoney = maxMoney;
    }
    public String getCompSign() {
        return compSign;
    }
    public void setCompSign(String compSign) {
        this.compSign = compSign;
    }
    public BigDecimal getAssurepercent() {
        return assurepercent;
    }
    public void setAssurepercent(BigDecimal assurepercent) {
        this.assurepercent = assurepercent;
    }
    public BigDecimal getInterestrate() {
        return interestrate;
    }
    public void setInterestrate(BigDecimal interestrate) {
        this.interestrate = interestrate;
    }
    public String getSysItemCode() {
        return sysItemCode;
    }
    public void setSysItemCode(String sysItemCode) {
        this.sysItemCode = sysItemCode;
    }
    public String getBondCurid() {
        return bondCurid;
    }
    public void setBondCurid(String bondCurid) {
        this.bondCurid = bondCurid;
    }
    public BigDecimal getBondMoney() {
        return bondMoney;
    }
    public void setBondMoney(BigDecimal bondMoney) {
        this.bondMoney = bondMoney;
    }
    public String getBillName() {
        return billName;
    }
    public void setBillName(String billName) {
        this.billName = billName;
    }
    public String getIsOpenRat() {
        return isOpenRat;
    }
    public void setIsOpenRat(String isOpenRat) {
        this.isOpenRat = isOpenRat;
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
    public BigDecimal getBillMoney() {
        return billMoney;
    }
    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }
    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getSysItemName() {
        return sysItemName;
    }
    public void setSysItemName(String sysItemName) {
        this.sysItemName = sysItemName;
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
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public BigDecimal getConvertPreMoney() {
        return convertPreMoney;
    }
    public void setConvertPreMoney(BigDecimal convertPreMoney) {
        this.convertPreMoney = convertPreMoney;
    }
    public String getGuaWay() {
        return guaWay;
    }
    public void setGuaWay(String guaWay) {
        this.guaWay = guaWay;
    }
    public String getIsHangGuarantee() {
        return isHangGuarantee;
    }
    public void setIsHangGuarantee(String isHangGuarantee) {
        this.isHangGuarantee = isHangGuarantee;
    }
    public String getBillId() {
        return billId;
    }
    public void setBillId(String billId) {
        this.billId = billId;
    }
    public String getSysCode() {
        return sysCode;
    }
    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }
    public String getRatCode() {
        return ratCode;
    }
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }
    public String getInitCurId() {
        return initCurId;
    }
    public void setInitCurId(String initCurId) {
        this.initCurId = initCurId;
    }
    public String getRaceId() {
        return raceId;
    }
    public void setRaceId(String raceId) {
        this.raceId = raceId;
    }
    public String getRaceName() {
        return raceName;
    }
    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }
    public String getCreditType() {
        return creditType;
    }
    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }
    public String getCreditTypeName() {
        return creditTypeName;
    }
    public void setCreditTypeName(String creditTypeName) {
        this.creditTypeName = creditTypeName;
    }
}
