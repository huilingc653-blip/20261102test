package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.check.vo.IBaseDataInject;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: VO
 * @Description: 外汇牌价表
 * @author huangx
 * @date 2020-07-16
 *
 */
public class FexRateInfoVO extends ExcelModel implements Serializable, IBaseDataInject {
    private static final long serialVersionUID = 3650731181127660208L;
    /**ID*/
    private String id;
    /**外汇牌价查询指令*/
    private String rateExcId;
    /**外汇牌价类型*/
    @BtCompositeSearch(fieldName = "r.rate_Type_Id")
    private String rateTypeId;
    @Excel(name = "*外汇牌价类型")
    @NotNull( message = "fex.rateInfo.message.rateType.null")
    @BtCompositeSearch(fieldName = "t.type_Name")
    private String rateTypeName;
    /**接口*/
    @Dict(tableName="BIS_BIF_INIT",valueFieldName="id",showFiledName="NAME",inject="bifName")
    private String bifCode;
    private String bifName;
    /**标准币种*/
    @BtCompositeSearch(fieldName = "r.cur")
    private String cur;
    private String englishCode;
    @Excel(name = "*标准币种")
    @NotNull( message = "fex.rateInfo.message.cur.null")
    @BtCompositeSearch(fieldName = "c.cur_Name")
    private String curName;
    private String curEnCode;
    /**报价币种*/
    @BtCompositeSearch(fieldName = "r.refer_cur")
    private String referCur;
    private String referEnglishCode;
    @Excel(name = "*报价币种")
    @NotNull( message = "fex.rateInfo.message.referCur.null")
    @BtCompositeSearch(fieldName = "rc.cur_Name")
    private String referCurName;
    private String referCurEnCode;
    /**汇买价*/
    @Excel(name = "*汇买价", align = HorizontalAlignment.RIGHT, numFormat = "##########0.000000")
    @NotNull( message = "fex.rateInfo.message.purchaseRate.null")
    @BtCompositeSearch(fieldName = "r.purchase_Rate")
    private BigDecimal purchaseRate;
    /**汇卖价*/
    @Excel(name = "*汇卖价", align = HorizontalAlignment.RIGHT, numFormat = "##########0.000000")
    @NotNull( message = "fex.rateInfo.message.sellRate.null")
    @BtCompositeSearch(fieldName = "r.sell_Rate")
    private BigDecimal sellRate;
    /**中间价*/
    @Excel(name = "中间价", align = HorizontalAlignment.RIGHT, numFormat = "##########0.000000")
    @BtCompositeSearch(fieldName = "r.middle_Rate")
    private BigDecimal middleRate;
    /**汇率日期*/
    @Excel(name = "汇率日期", format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "r.exchange_rate_date")
    private Date exchangeRateDate;
    /**期限*/
    @Excel(name = "期限")
    @BtCompositeSearch(fieldName = "r.term")
    private String term;
    /**备注*/
    @Excel(name = "备注")
    private String rmk;
    /**交易状态*/
    @Dict(dictGroup = "bisExc.voucherStat", inject = "voucherStatName")
    private String voucherStat;
    private String voucherStatName;
    /**返回时间*/
    @BtCompositeSearch(fieldName = "e.return_Time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date returnTime;


    private String publicationDate;
    /**返回信息*/
    private String returnMsg;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    /**参考汇率*/
    private BigDecimal referenceRate;

    /**发布时间*/
    @Excel(name = "*发布时间")
    @NotNull( message = "fex.rateInfo.message.returnTime.null")
    @BtCompositeSearch(fieldName = "r.effect_Date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectDate;
    /**报价金额*/
    @Excel(name = "报价金额")
    @BtCompositeSearch(fieldName = "r.cur_Unit")
    private String curUnit;

    public BigDecimal getReferenceRate() {
        return referenceRate;
    }

    public void setReferenceRate(BigDecimal referenceRate) {
        this.referenceRate = referenceRate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRateExcId() {
        return rateExcId;
    }

    public void setRateExcId(String rateExcId) {
        this.rateExcId = rateExcId;
    }

    public String getRateTypeId() {
        return rateTypeId;
    }

    public void setRateTypeId(String rateTypeId) {
        this.rateTypeId = rateTypeId;
    }

    public String getRateTypeName() {
        return rateTypeName;
    }

    public void setRateTypeName(String rateTypeName) {
        this.rateTypeName = rateTypeName;
    }

    public String getBifCode() {
        return bifCode;
    }

    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }

    public String getBifName() {
        return bifName;
    }

    public void setBifName(String bifName) {
        this.bifName = bifName;
    }

    public String getCur() {
        return cur;
    }

    public void setCur(String cur) {
        this.cur = cur;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getReferCur() {
        return referCur;
    }

    public void setReferCur(String referCur) {
        this.referCur = referCur;
    }

    public String getReferCurName() {
        return referCurName;
    }

    public void setReferCurName(String referCurName) {
        this.referCurName = referCurName;
    }

    public String getCurEnCode() {
        return curEnCode;
    }

    public void setCurEnCode(String curEnCode) {
        this.curEnCode = curEnCode;
    }

    public String getReferCurEnCode() {
        return referCurEnCode;
    }

    public void setReferCurEnCode(String referCurEnCode) {
        this.referCurEnCode = referCurEnCode;
    }

    public BigDecimal getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(BigDecimal purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    public BigDecimal getSellRate() {
        return sellRate;
    }

    public void setSellRate(BigDecimal sellRate) {
        this.sellRate = sellRate;
    }

    public BigDecimal getMiddleRate() {
        return middleRate;
    }

    public void setMiddleRate(BigDecimal middleRate) {
        this.middleRate = middleRate;
    }

    public Date getExchangeRateDate() {
        return exchangeRateDate;
    }

    public void setExchangeRateDate(Date exchangeRateDate) {
        this.exchangeRateDate = exchangeRateDate;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public String getVoucherStat() {
        return voucherStat;
    }

    public void setVoucherStat(String voucherStat) {
        this.voucherStat = voucherStat;
    }

    public String getVoucherStatName() {
        return voucherStatName;
    }

    public void setVoucherStatName(String voucherStatName) {
        this.voucherStatName = voucherStatName;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
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

    public String getEnglishCode() {
        return englishCode;
    }

    public void setEnglishCode(String englishCode) {
        this.englishCode = englishCode;
    }

    public String getReferEnglishCode() {
        return referEnglishCode;
    }

    public void setReferEnglishCode(String referEnglishCode) {
        this.referEnglishCode = referEnglishCode;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Date getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(Date effectDate) {
        this.effectDate = effectDate;
    }

    public String getCurUnit() {
        return curUnit;
    }

    public void setCurUnit(String curUnit) {
        this.curUnit = curUnit;
    }

    @Override
    public String getBeanName() {
        return "fexRateInfoService";
    }
}
