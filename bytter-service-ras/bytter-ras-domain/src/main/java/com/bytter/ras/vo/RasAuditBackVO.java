package com.bytter.ras.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.base.entity.IdEntity;
//import com.bytter.core.injections.IBaseDataInject;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: vo
 * @Description: 反稽核处理VO类
 * @author zhenghj
 * @date 2019-05-16
 * @version 1.0
 *
 */
public class RasAuditBackVO  extends ExcelModel implements java.io.Serializable{

  private java.lang.String id;
  /**单位ID*/
  @BtCompositeSearch(fieldName="c.id")
  private java.lang.String corpId;
  /**单位名称*/
  @BtCompositeSearch(fieldName="c.code")
  private java.lang.String corpName;
  /**单位编码*/
  private java.lang.String corpCode;
  /** 缴款方式:001现金002支付宝003微信004银联POS机005收钱吧006其它 */
  @Dict(dictGroup = "RAS_PAY_TYPE",inject = "payTypeName")
  @BtCompositeSearch(fieldName = "a.pay_type")
  private String payType;
  private String payTypeName;
  /**门店id*/
  @BtCompositeSearch(fieldName="s.id")
  private String shopId;
  /**门店名称 */
  @BtCompositeSearch(fieldName="s.name")
  private java.lang.String shopName;
  /**门店编码*/
  private java.lang.String shopCode;
  /** 稽核码 */
  @BtCompositeSearch(fieldName = "a.audit_no")
  private String auditNo;
  /**稽核方式*/
  @Dict(dictGroup = "RAS_AUDIT_TYPE",inject = "typeName")
  private String type;
  @BtCompositeSearch(fieldName = "a.type")
  private String typeName;
  /** 营收笔数 */
  private Integer revenueSize;
  /** 实收笔数 */
  private Integer receivedSize;
  /** 营收总额 */
  @BtCompositeSearch(fieldName="a.revenue_Money")
  private BigDecimal revenueMoney;
  /** 实收总额 */
  @BtCompositeSearch(fieldName="a.received_Money")
  private BigDecimal receivedMoney;
  /** 差异金额：营收总额减去实收总额 */
  @BtCompositeSearch(fieldName="less_Money")
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
  @BtCompositeSearch(fieldName="a.create_Name")
  private String createName;
  /**创建日期*/
  @BtCompositeSearch(fieldName="a.create_Date")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date createDate;
  private Date createDateStart;
  private Date createDateEnd;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public BigDecimal getLessMoneyEnd() {
    return lessMoneyEnd;
  }

  public void setLessMoneyEnd(BigDecimal lessMoneyEnd) {
    this.lessMoneyEnd = lessMoneyEnd;
  }

  public Date getCreateDateStart() {
    return createDateStart;
  }

  public void setCreateDateStart(Date createDateStart) {
    this.createDateStart = createDateStart;
  }

  public Date getCreateDateEnd() {
    return createDateEnd;
  }

  public void setCreateDateEnd(Date createDateEnd) {
    this.createDateEnd = createDateEnd;
  }

  @Override
  public String toString() {
    return "RasAuditBackVO{" +
            "id='" + id + '\'' +
            ", corpId='" + corpId + '\'' +
            ", corpName='" + corpName + '\'' +
            ", corpCode='" + corpCode + '\'' +
            ", payType='" + payType + '\'' +
            ", payTypeName='" + payTypeName + '\'' +
            ", shopId='" + shopId + '\'' +
            ", shopName='" + shopName + '\'' +
            ", shopCode='" + shopCode + '\'' +
            ", auditNo='" + auditNo + '\'' +
            ", type='" + type + '\'' +
            ", typeName='" + typeName + '\'' +
            ", revenueSize=" + revenueSize +
            ", receivedSize=" + receivedSize +
            ", revenueMoney=" + revenueMoney +
            ", receivedMoney=" + receivedMoney +
            ", lessMoney=" + lessMoney +
            ", lessMoneyEnd=" + lessMoneyEnd +
            ", status=" + status +
            ", statusName='" + statusName + '\'' +
            ", rmk='" + rmk + '\'' +
            ", createBy='" + createBy + '\'' +
            ", createName='" + createName + '\'' +
            ", createDate=" + createDate +
            ", createDateStart=" + createDateStart +
            ", createDateEnd=" + createDateEnd +
            '}';
  }
}
