package com.bytter.ras.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 营收稽核POS机绑定数据库实体类
 * @author zhenghj
 * @date 2019-05-06
 */
@Entity
@Table(name = "RAS_POS_BINDING", schema = "")
public class RasPosBinding extends IdEntity implements java.io.Serializable {
  /**单位ID*/
  private String corpId;
  /**门店ID */
  private String shopId;
  /** 状态:01正常02待确认03已借出*/
  private String status;
  /**有效标志：0，禁用 1，启用  */
  private String validSign;
  /** POS机终端编号*/
  private String posNo;
  /** 备注*/
  private String rmk;
  /** 创建人名称*/
  private String createName;
  /**创建人登录名称 */
  private String createBy;
  /** 创建日期*/
  private java.util.Date createDate;
  /**更新人名称*/
  private String updateName;
  /** 更新人登录名称*/
  private String updateBy;
  /**更新日期 */
  private java.util.Date updateDate;

  @Column(name ="CORP_ID",nullable=true,length=32)
  public String getCorpId() {
    return corpId;
  }

  public void setCorpId(String corpId) {
    this.corpId = corpId;
  }

  @Column(name ="SHOP_ID",nullable=true,length=32)
  public String getShopId() {
    return shopId;
  }

  public void setShopId(String shopId) {
    this.shopId = shopId;
  }

  @Column(name ="STATUS",nullable=true,length=2)
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Column(name ="VALID_SIGN",nullable=true,length=1)
  public String getValidSign() {
    return validSign;
  }

  public void setValidSign(String validSign) {
    this.validSign = validSign;
  }

  @Column(name ="POS_NO",nullable=true,length=50)
  public String getPosNo() {
    return posNo;
  }

  public void setPosNo(String posNo) {
    this.posNo = posNo;
  }

  @Column(name ="RMK",nullable=true,length=200)
  public String getRmk() {
    return rmk;
  }

  public void setRmk(String rmk) {
    this.rmk = rmk;
  }

  @Column(name ="CREATE_NAME",nullable=true,length=50)
  public String getCreateName() {
    return createName;
  }

  public void setCreateName(String createName) {
    this.createName = createName;
  }

  @Column(name ="CREATE_BY",nullable=true,length=50)
  public String getCreateBy() {
    return createBy;
  }

  public void setCreateBy(String createBy) {
    this.createBy = createBy;
  }

  @Column(name ="CREATE_DATE",nullable=true)
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Column(name ="UPDATE_NAME",nullable=true,length=50)
  public String getUpdateName() {
    return updateName;
  }

  public void setUpdateName(String updateName) {
    this.updateName = updateName;
  }

  @Column(name ="UPDATE_BY",nullable=true,length=50)
  public String getUpdateBy() {
    return updateBy;
  }

  public void setUpdateBy(String updateBy) {
    this.updateBy = updateBy;
  }

  @Column(name ="UPDATE_DATE",nullable=true)
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Override
  public String toString() {
    return "RasPosBinding{" +
            "corpId='" + corpId + '\'' +
            ", shopId='" + shopId + '\'' +
            ", status='" + status + '\'' +
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
}
