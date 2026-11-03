package com.bytter.ras.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 营收稽核门店授权数据库实体类
 * @author zhenghj
 * @date 2019-05-13
 */
@Entity
@Table(name = "RAS_SHOP_GRANT", schema = "")
public class RasShopGrant  extends IdEntity implements java.io.Serializable {
  /**门店ID */
  private String shopId;
  /** 用户Id */
  private String userId;
  /** 授权稽核权限:1是0否 */
  private String canAudit;
  /** 授权查询权限1是0否 */
  private String canQuery;
  /** 创建人名称 */
  private String createName;
  /** 创建人登录名称 */
  private String createBy;
  /** 创建日期 */
  private java.util.Date createDate;
  /** 更新人名称 */
  private String updateName;
  /** 更新人登录名称 */
  private String updateBy;
  /** 更新日期 */
  private java.util.Date updateDate;

  @Column(name ="SHOP_ID",nullable=true,length=32)
  public String getShopId() {
    return shopId;
  }

  public void setShopId(String shopId) {
    this.shopId = shopId;
  }

  @Column(name ="USER_ID",nullable=true,length=32)
  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  @Column(name ="CAN_AUDIT",nullable=true,length=1)
  public String getCanAudit() {
    return canAudit;
  }

  public void setCanAudit(String canAudit) {
    this.canAudit = canAudit;
  }

  @Column(name ="CAN_QUERY",nullable=true,length=1)
  public String getCanQuery() {
    return canQuery;
  }

  public void setCanQuery(String canQuery) {
    this.canQuery = canQuery;
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
    return "RasShopGrant{" +
            "shopId='" + shopId + '\'' +
            ", userId='" + userId + '\'' +
            ", canAudit='" + canAudit + '\'' +
            ", canQuery='" + canQuery + '\'' +
            ", createName='" + createName + '\'' +
            ", createBy='" + createBy + '\'' +
            ", createDate=" + createDate +
            ", updateName='" + updateName + '\'' +
            ", updateBy='" + updateBy + '\'' +
            ", updateDate=" + updateDate +
            '}';
  }
}
