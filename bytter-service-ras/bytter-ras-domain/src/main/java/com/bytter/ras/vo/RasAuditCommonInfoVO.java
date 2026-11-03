package com.bytter.ras.vo;

import com.bytter.core.annotation.Dict;
//import com.bytter.core.injections.IBaseDataInject;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: RasAuditCommonInfoVO
 * @Description: 营收实收稽核公共信息
 * @author wangzy
 * @date 2019/9/26 16:09
 *
 */
public class RasAuditCommonInfoVO implements java.io.Serializable {
    /**单位名称*/
    private java.lang.String corpName;
    /**单位编码*/
    private java.lang.String corpCode;
    /**单位编码和名称*/
    private java.lang.String corpCodeAndName;
    /** 缴款方式:001现金002支付宝003微信004银联POS机005收钱吧006其它 */
    @Dict(dictGroup = "RAS_PAY_TYPE",inject = "payTypeName")
    private String payType;
    private String payTypeName;
    /**门店id*/
    private String shopId;
    /**门店名称 */
    private java.lang.String shopName;
    /**门店编码*/
    private java.lang.String shopCode;
    /** 稽核码 */
    private String auditNo;
    /**稽核方式*/
    @Dict(dictGroup = "RAS_AUDIT_TYPE",inject = "typeName")
    private String type;
    private String typeName;
    /** 营收笔数 */
    private Integer revenueSize;
    /** 实收笔数 */
    private Integer receivedSize;
    /** 营收总额 */
    private BigDecimal revenueMoney;
    /** 实收总额 */
    private BigDecimal receivedMoney;
    /** 差异金额：营收总额减去实收总额 */
    private BigDecimal lessMoney;
    private BigDecimal lessMoneyEnd;
    /**  状态:3已稽核4差异稽核5稽核完成-1稽核打回 */
    @Dict(dictGroup = "RAS_AUDIT_STATUS",inject = "statusName")
    private Integer status;
    private String statusName;
    /** 备注 */
    private String rmk;
    /**创建人登录名称*/
    private String createBy;
    /**创建人名称*/
    private String createName;
    /**创建日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /**差异日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lessDate;

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
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

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getAuditNo() {
        return auditNo;
    }

    public void setAuditNo(String auditNo) {
        this.auditNo = auditNo;
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

    public Integer getRevenueSize() {
        return revenueSize;
    }

    public void setRevenueSize(Integer revenueSize) {
        this.revenueSize = revenueSize;
    }

    public Integer getReceivedSize() {
        return receivedSize;
    }

    public void setReceivedSize(Integer receivedSize) {
        this.receivedSize = receivedSize;
    }

    public BigDecimal getRevenueMoney() {
        return revenueMoney;
    }

    public void setRevenueMoney(BigDecimal revenueMoney) {
        this.revenueMoney = revenueMoney;
    }

    public BigDecimal getReceivedMoney() {
        return receivedMoney;
    }

    public void setReceivedMoney(BigDecimal receivedMoney) {
        this.receivedMoney = receivedMoney;
    }

    public BigDecimal getLessMoney() {
        return lessMoney;
    }

    public void setLessMoney(BigDecimal lessMoney) {
        this.lessMoney = lessMoney;
    }

    public BigDecimal getLessMoneyEnd() {
        return lessMoneyEnd;
    }

    public void setLessMoneyEnd(BigDecimal lessMoneyEnd) {
        this.lessMoneyEnd = lessMoneyEnd;
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

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCorpCodeAndName() {
        return corpCodeAndName;
    }

    public void setCorpCodeAndName(String corpCodeAndName) {
        this.corpCodeAndName = corpCodeAndName;
    }

    public Date getLessDate() {
        return lessDate;
    }

    public void setLessDate(Date lessDate) {
        this.lessDate = lessDate;
    }
}
