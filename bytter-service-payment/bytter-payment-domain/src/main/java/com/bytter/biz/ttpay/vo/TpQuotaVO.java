package com.bytter.biz.ttpay.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.poi.annotation.ExcelTarget;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author chenf
 * @version 1.0
 * @Title: VO
 * @Description: 第三方支付限额管理
 * @date 2022-5-6
 */
@ExcelTarget("TpQuotaVO")
public class TpQuotaVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    /**单位id*/
    @BtCompositeSearch(fieldName = "t.CORP_ID")
    private String corpId;
    /**单位名称*/
    private String corpName;
    /**商户号id*/
    private String merchantId;
    /**商户号*/
    @BtCompositeSearch(fieldName = "t.MERCHANT_NO")
    private String merchantNo;
    /**支付方式*/
    @BtCompositeSearch(fieldName = "t.PAY_WAY")
    private String payWay;
    /**备注*/
    private String rmk;
    /**有效标志*/
    private String validSign;
    /**生效日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectDate;
    /**日单笔限额**/
    private java.math.BigDecimal daySingle;
    /**日累计限额**/
    private java.math.BigDecimal dayTotal;
    /**日已用余额**/
    private java.math.BigDecimal dayTotalUse;
    /**日可用余额**/
    private java.math.BigDecimal dayTotalAvail;
    /**月累计限额**/
    private java.math.BigDecimal monthTotal;
    /**月已用余额**/
    private java.math.BigDecimal monthTotalUse;
    /**月可用余额**/
    private java.math.BigDecimal monthTotalAvail;
    private String createName;
    private String createBy;
    private Date createDate;
    private String updateName;
    private String updateBy;
    private Date updateDate;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getMerchantId() {
        return merchantId;
    }
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
    public String getMerchantNo() {
        return merchantNo;
    }
    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }
    public String getPayWay() {
        return payWay;
    }
    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    public Date getEffectDate() {
        return effectDate;
    }
    public void setEffectDate(Date effectDate) {
        this.effectDate = effectDate;
    }
    public BigDecimal getDaySingle() {
        return daySingle;
    }
    public void setDaySingle(BigDecimal daySingle) {
        this.daySingle = daySingle;
    }
    public BigDecimal getDayTotal() {
        return dayTotal;
    }
    public void setDayTotal(BigDecimal dayTotal) {
        this.dayTotal = dayTotal;
    }
    public BigDecimal getMonthTotal() {
        return monthTotal;
    }
    public void setMonthTotal(BigDecimal monthTotal) {
        this.monthTotal = monthTotal;
    }
    public String getCreateName() {
        return createName;
    }
    public void setCreateName(String createName) {
        this.createName = createName;
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
    public String getUpdateName() {
        return updateName;
    }
    public void setUpdateName(String updateName) {
        this.updateName = updateName;
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
    public BigDecimal getDayTotalUse() {
        return dayTotalUse;
    }
    public void setDayTotalUse(BigDecimal dayTotalUse) {
        this.dayTotalUse = dayTotalUse;
    }
    public BigDecimal getMonthTotalUse() {
        return monthTotalUse;
    }
    public void setMonthTotalUse(BigDecimal monthTotalUse) {
        this.monthTotalUse = monthTotalUse;
    }
    public BigDecimal getDayTotalAvail() {
        return dayTotalAvail;
    }
    public void setDayTotalAvail(BigDecimal dayTotalAvail) {
        this.dayTotalAvail = dayTotalAvail;
    }
    public BigDecimal getMonthTotalAvail() {
        return monthTotalAvail;
    }
    public void setMonthTotalAvail(BigDecimal monthTotalAvail) {
        this.monthTotalAvail = monthTotalAvail;
    }
}
