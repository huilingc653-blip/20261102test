package com.bytter.ras.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
//import com.bytter.core.injections.IBaseDataInject;
import com.bytter.core.utils.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yuwj
 * @projectName T2
 * @description: 营收稽核待稽核统计vo
 * @date 2019/5/22 13:40
 */
public class RasShopNotAuditQueryVO implements java.io.Serializable{
    /**门店id*/
    @BtCompositeSearch(fieldName = "rr2.shopId")
    private String shopId;
    /**门店名称*/
    @BtCompositeSearch(fieldName = "rs.name")
    private String shopName;
    /**单位id*/
    @BtCompositeSearch(fieldName = "sc.id")
    private String corpId;
    /**单位Code*/
    private String corpCode;
    /**单位名称*/
    @BtCompositeSearch(fieldName = "sc.code")
    private String corpName;

    /**缴款方式*/
    @Dict(dictGroup = "RAS_PAY_TYPE",inject = "payTypeName")
    @BtCompositeSearch(fieldName = "rr2.payType")
    private String payType;
    @BtCompositeSearch(fieldName = "rr2.payType")
    private String payTypeName;

    /**营收合计（待稽核）*/
    @BtCompositeSearch(fieldName = "rr2.nueMoney")
    private BigDecimal nueMoney;
    /**实收合计（待稽核）*/
    @BtCompositeSearch(fieldName = "rr2.vedMoney")
    private BigDecimal vedMoney;

    /**数据日期（营收、实收数据的交易日期）*/
    //字符串用于条件查询，避免用date条件查询很慢
    private String transDateStr;
    @BtCompositeSearch(fieldName = "rr2.transDateStr")
    private Date transDate;


    /**状态:1待稽核2稽核中3已稽核4差异稽核5稽核完成0待确认-1打回-2作废*/
    @Dict(dictGroup = "RAS_AUDIT_STATUS",inject = "statusName")
    @BtCompositeSearch(fieldName = "rr2.status")
    private Integer status;
    @BtCompositeSearch(fieldName = "rr2.status")
    private String statusName;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpName() {
        if(StringUtils.isNotEmpty(corpCode)
                &&StringUtils.isNotEmpty(corpName)){
            return "("+corpCode+")"+corpName;
        }
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }

    public BigDecimal getNueMoney() {
        return nueMoney;
    }

    public void setNueMoney(BigDecimal nueMoney) {
        this.nueMoney = nueMoney;
    }

    public BigDecimal getVedMoney() {
        return vedMoney;
    }

    public void setVedMoney(BigDecimal vedMoney) {
        this.vedMoney = vedMoney;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getTransDateStr() {
        return transDateStr;
    }

    public void setTransDateStr(String transDateStr) {
        this.transDateStr = transDateStr;
    }
}
