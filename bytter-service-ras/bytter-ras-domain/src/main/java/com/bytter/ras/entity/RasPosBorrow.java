package com.bytter.ras.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 营收稽核POS机借入表实体类
 * @author zhenghj
 * @date 2019-05-06
 */
@Entity
@Table(name = "RAS_POS_BORROW", schema = "")
public class RasPosBorrow extends IdEntity implements java.io.Serializable {

  /**pos机绑定表id*/
  private String bdId;
  /**单位ID*/
  private String corpId;
  /**门店ID */
  private String shopId;
  /** 借入状态-1打回01待确认02借用03已归还*/
  private String status;
  /** 备注*/
  private String rmk;
  /**绑定时间*/
  private Date bdDate;
  /**解绑时间*/
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
  private String updateName;
  /** 更新人登录名称*/
  private String updateBy;
  /**更新日期 */
  private Date updateDate;

  @Column(name ="BD_ID",nullable=true,length=32)
  public String getBdId() {
    return bdId;
  }

  public void setBdId(String bdId) {
    this.bdId = bdId;
  }

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

  @Column(name ="RMK",nullable=true,length=200)
  public String getRmk() {
    return rmk;
  }

  public void setRmk(String rmk) {
    this.rmk = rmk;
  }

  @Column(name ="BD_DATE",nullable=true)
  public Date getBdDate() {
    return bdDate;
  }

  public void setBdDate(Date bdDate) {
    this.bdDate = bdDate;
  }

  @Column(name ="UBD_DATE",nullable=true)
  public Date getUbdDate() {
    return ubdDate;
  }

  public void setUbdDate(Date ubdDate) {
    this.ubdDate = ubdDate;
  }

  @Column(name ="MSG",nullable=true,length=100)
  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
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
    return "RasPosBorrow{" +
            "bdId='" + bdId + '\'' +
            ", corpId='" + corpId + '\'' +
            ", shopId='" + shopId + '\'' +
            ", status='" + status + '\'' +
            ", rmk='" + rmk + '\'' +
            ", bdDate=" + bdDate +
            ", ubdDate=" + ubdDate +
            ", msg='" + msg + '\'' +
            ", createName='" + createName + '\'' +
            ", createBy='" + createBy + '\'' +
            ", createDate=" + createDate +
            ", updateName='" + updateName + '\'' +
            ", updateBy='" + updateBy + '\'' +
            ", updateDate=" + updateDate +
            '}';
  }
}
