package com.bytter.ras.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
//import com.bytter.core.injections.IBaseDataInject;
import com.bytter.core.utils.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yuwj
 * @projectName T2
 * @description: 营收稽核已处理差异明细vo
 * @date 2019/5/16 13:40
 */
public class RasDifferentDtlQueryVO implements java.io.Serializable{
    private String id;
    /**稽核码*/
    @BtCompositeSearch(fieldName = "rai.audit_No")
    private String auditNo;
    /**门店id*/
    @BtCompositeSearch(fieldName = "rai.shop_Id")
    private String shopId;
    /**门店名称*/
    @BtCompositeSearch(fieldName = "rs.name")
    private String shopName;
    /**单位id*/
    @BtCompositeSearch(fieldName = "rs.corp_Id")
    private String corpId;
    /**单位代码*/
    private String corpCode;
    /**单位名称*/
    @BtCompositeSearch(fieldName = "sc.code")
    private String corpName;

    /**缴款方式*/
    @Dict(dictGroup = "RAS_PAY_TYPE",inject = "payTypeName")
    @BtCompositeSearch(fieldName = "rai.pay_Type")
    private String payType;
    @BtCompositeSearch(fieldName = "rai.pay_Type")
    private String payTypeName;

    /**营收合计*/
    @BtCompositeSearch(fieldName = "rai.revenue_Money")
    private BigDecimal revenueMoney;
    /**营收笔数*/
    @BtCompositeSearch(fieldName = "rai.revenue_Size")
    private Integer revenueSize;

    /**实收合计*/
    @BtCompositeSearch(fieldName = "rai.received_Money")
    private BigDecimal receivedMoney;
    /**实收笔数*/
    @BtCompositeSearch(fieldName = "rai.received_Size")
    private Integer receivedSize;

    /**差异金额*/
    @BtCompositeSearch(fieldName = "rai.less_Money")
    private BigDecimal lessMoney;
    /**确认差异日期*/
    @BtCompositeSearch(fieldName = "rai.create_Date")
    private Date createDate;

    /**备注*/
    @BtCompositeSearch(fieldName = "rai.rmk")
    private String rmk;
    /**稽核人*/
    @BtCompositeSearch(fieldName = "rai.create_Name")
    private String createName;
    @BtCompositeSearch(fieldName = "rai.create_By")
    private String createBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuditNo() {
        return auditNo;
    }

    public void setAuditNo(String auditNo) {
        this.auditNo = auditNo;
    }

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

    public BigDecimal getRevenueMoney() {
        return revenueMoney;
    }

    public void setRevenueMoney(BigDecimal revenueMoney) {
        this.revenueMoney = revenueMoney;
    }

    public Integer getRevenueSize() {
        return revenueSize;
    }

    public void setRevenueSize(Integer revenueSize) {
        this.revenueSize = revenueSize;
    }

    public BigDecimal getReceivedMoney() {
        return receivedMoney;
    }

    public void setReceivedMoney(BigDecimal receivedMoney) {
        this.receivedMoney = receivedMoney;
    }

    public Integer getReceivedSize() {
        return receivedSize;
    }

    public void setReceivedSize(Integer receivedSize) {
        this.receivedSize = receivedSize;
    }

    public BigDecimal getLessMoney() {
        return lessMoney;
    }

    public void setLessMoney(BigDecimal lessMoney) {
        this.lessMoney = lessMoney;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
}
