package com.bytter.ras.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
//import com.bytter.core.injections.IBaseDataInject;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @Title: Entity
 * @Description: 营收稽核POS机绑定VO类
 * @author zhenghj
 * @date 2019-05-06
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
@ExcelTarget("RasPosBindingVO")
public class RasPosBindingVO  extends ExcelModel implements java.io.Serializable{

  private java.lang.String id;
  /**单位ID*/
  @BtCompositeSearch(fieldName="s.id")
  private java.lang.String corpId;
  /**单位名称*/
  @Excel(name = "单位名称",isImportField="true")
  @BtCompositeSearch(fieldName="s.code")
  private java.lang.String corpName;
  /**单位编码*/
  @Excel(name = "单位代码",isImportField="true")
  private java.lang.String corpCode;
  /**门店ID */
  @BtCompositeSearch(fieldName="r.id")
  private java.lang.String shopId;
  /**门店名称 */
  @Excel(name = "门店名称",isImportField="true")
  @BtCompositeSearch(fieldName="r.name")
  private java.lang.String shopName;
  /**门店编码*/
  @Excel(name = "门店编码",isImportField="true")
  @BtCompositeSearch(fieldName="r.code")
  private java.lang.String shopCode;
  /** 状态:01正常02待确认03已借出*/
  @Dict(dictGroup = "RAS_POS_BD_STATUS", inject = "statusName")
  @BtCompositeSearch(fieldName="p.status")
  private java.lang.String status;
  private java.lang.String statusName;
  /**有效标志：0，禁用 1，启用  */
  private java.lang.String validSign;
  /** POS机终端编号*/
  @Excel(name = "*终端号",isImportField="true")
  @BtCompositeSearch(fieldName="POS_NO")
  private java.lang.String posNo;
  /** 备注*/
  @Excel(name = "备注",isImportField="true")
  private java.lang.String rmk;
  /** 创建人名称*/
  private java.lang.String createName;
  /**创建人登录名称 */
  private java.lang.String createBy;
  /** 创建日期*/
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  @BtCompositeSearch(fieldName="p.create_Date")
  private java.util.Date createDate;
  /**更新人名称*/
  private java.lang.String updateName;
  /** 更新人登录名称*/
  private java.lang.String updateBy;
  /**更新日期 */
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  private java.util.Date updateDate;

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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStatusName() {
    return statusName;
  }

  public void setStatusName(String statusName) {
    this.statusName = statusName;
  }

  public String getValidSign() {
    return validSign;
  }

  public void setValidSign(String validSign) {
    this.validSign = validSign;
  }

  public String getPosNo() {
    return posNo;
  }

  public void setPosNo(String posNo) {
    this.posNo = posNo;
  }

  public String getRmk() {
    return rmk;
  }

  public void setRmk(String rmk) {
    this.rmk = rmk;
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

  @Override
  public String toString() {
    return "RasPosBindingVO{" +
            "id='" + id + '\'' +
            ", corpId='" + corpId + '\'' +
            ", corpName='" + corpName + '\'' +
            ", corpCode='" + corpCode + '\'' +
            ", shopId='" + shopId + '\'' +
            ", shopName='" + shopName + '\'' +
            ", shopCode='" + shopCode + '\'' +
            ", status='" + status + '\'' +
            ", statusName='" + statusName + '\'' +
            ", validSign='" + validSign + '\'' +
            ", posNo='" + posNo + '\'' +
            ", rmk='" + rmk + '\'' +
            ", createName='" + createName + '\'' +
            ", createBy='" + createBy + '\'' +
            ", createDate=" + createDate +
            ", updateName='" + updateName + '\'' +
            ", updateBy='" + updateBy + '\'' +
            ", updateDate=" + updateDate +
            '}';
  }

  @Override
  public String getBeanName() {
    return "rasPosBindingService";
  }
}
