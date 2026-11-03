package com.bytter.ras.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
//import com.bytter.core.injections.IBaseDataInject;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @Title: Entity
 * @Description: 营收稽核POS机借入表VO类
 * @author zhenghj
 * @date 2019-05-06
 */
@SuppressWarnings("serial")
public class RasPosBorrowVO  implements java.io.Serializable {


  /**借入表id*/
  private String id;

    /**pos机绑定表id*/
  private String bdId;
  /**单位ID*/
  @BtCompositeSearch(fieldName="s.id")
  private String corpId;
  /**单位名称*/
  @BtCompositeSearch(fieldName="s.code")
  private java.lang.String corpName;
  /**单位编码*/
  private java.lang.String corpCode;
  /**门店ID */
  @BtCompositeSearch(fieldName="r.id")
  private String shopId;
  /**门店名称 */
  @BtCompositeSearch(fieldName="r.name")
  private java.lang.String shopName;
  /**门店编码*/
  @BtCompositeSearch(fieldName="r.code")
  private java.lang.String shopCode;
  /** 借入状态-1打回01待确认02借用03已归还*/
  @Dict(dictGroup = "RAS_POS_BR_STATUS", inject = "statusName")
  private String status;
  @BtCompositeSearch(fieldName="p.status")
  private java.lang.String statusName;
  /** POS机终端编号*/
  @BtCompositeSearch(fieldName="b.pos_no")
  private java.lang.String posNo;
  /** 备注*/
  private String rmk;
  /**绑定时间*/
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  @BtCompositeSearch(fieldName="p.bd_Date")
  private Date bdDate;
  /**解绑时间*/
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  @BtCompositeSearch(fieldName="p.ubd_Date")
  private Date ubdDate;
  /**打回原因*/
  private String msg;
  /** 创建人名称*/
  private String createName;
  /**创建人登录名称 */
  private String createBy;
  /** 创建日期*/
  private Date createDate;
  /**更新人名称*/
  private String updatName;
  /** 更新人登录名称*/
  private String updateBy;
  /**更新日期 */
  private Date updateDate;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getBdId() {
    return bdId;
  }

  public void setBdId(String bdId) {
    this.bdId = bdId;
  }

  public String getCorpId() {
    return corpId;
  }

  public void setCorpId(String corpId) {
    this.corpId = corpId;
  }

  public String getCorpCode() {
    return corpCode;
  }

  public void setCorpCode(String corpCode) {
    this.corpCode = corpCode;
  }

  public String getCorpName() {
    return corpName;
  }

  public void setCorpName(String corpName) {
    this.corpName = corpName;
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

  public Date getBdDate() {
    return bdDate;
  }

  public void setBdDate(Date bdDate) {
    this.bdDate = bdDate;
  }

  public Date getUbdDate() {
    return ubdDate;
  }

  public void setUbdDate(Date ubdDate) {
    this.ubdDate = ubdDate;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
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

  public String getUpdatName() {
    return updatName;
  }

  public void setUpdatName(String updatName) {
    this.updatName = updatName;
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
    return "RasPosBorrowVO{" +
            "id='" + id + '\'' +
            ", bdId='" + bdId + '\'' +
            ", corpId='" + corpId + '\'' +
            ", corpName='" + corpName + '\'' +
            ", corpCode='" + corpCode + '\'' +
            ", shopId='" + shopId + '\'' +
            ", shopName='" + shopName + '\'' +
            ", shopCode='" + shopCode + '\'' +
            ", status='" + status + '\'' +
            ", statusName='" + statusName + '\'' +
            ", posNo='" + posNo + '\'' +
            ", rmk='" + rmk + '\'' +
            ", bdDate=" + bdDate +
            ", ubdDate=" + ubdDate +
            ", msg='" + msg + '\'' +
            ", createName='" + createName + '\'' +
            ", createBy='" + createBy + '\'' +
            ", createDate=" + createDate +
            ", updatName='" + updatName + '\'' +
            ", updateBy='" + updateBy + '\'' +
            ", updateDate=" + updateDate +
            '}';
  }
}
