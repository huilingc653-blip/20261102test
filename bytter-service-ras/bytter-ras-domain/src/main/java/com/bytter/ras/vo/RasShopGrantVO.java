package com.bytter.ras.vo;

import com.bytter.core.annotation.Corp;
import com.bytter.core.base.entity.IdEntity;
//import com.bytter.core.injections.IBaseDataInject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 营收稽核门店授权VO类
 * @author zhenghj
 * @date 2019-05-13
 */
@SuppressWarnings("serial")
public class RasShopGrantVO  implements java.io.Serializable{
  /**门店ID */
  private String shopId;
  /**门店名称 */
  private java.lang.String shopName;
  /**门店类别*/
  private String shopType;
  /**单位*/
  @Corp(inject="corpName")
  private String corpId;
  /**单位名称*/
  private String corpName;
  /**商户名称*/
  private String merchantName;
  /** 用户Id */
  private String userId;
  /** 用户名称 */
  private String username;
  /** 授权稽核权限:1是0否 */
  private String canAudit;
  /** 授权查询权限1是0否 */
  private String canQuery;

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

  public String getShopType() {
    return shopType;
  }

  public void setShopType(String shopType) {
    this.shopType = shopType;
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

  public String getMerchantName() {
    return merchantName;
  }

  public void setMerchantName(String merchantName) {
    this.merchantName = merchantName;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getCanAudit() {
    return canAudit;
  }

  public void setCanAudit(String canAudit) {
    this.canAudit = canAudit;
  }

  public String getCanQuery() {
    return canQuery;
  }

  public void setCanQuery(String canQuery) {
    this.canQuery = canQuery;
  }

  @Override
  public String toString() {
    return "RasShopGrantVO{" +
            "shopId='" + shopId + '\'' +
            ", shopName='" + shopName + '\'' +
            ", shopType='" + shopType + '\'' +
            ", corpId='" + corpId + '\'' +
            ", corpName='" + corpName + '\'' +
            ", merchantName='" + merchantName + '\'' +
            ", userId='" + userId + '\'' +
            ", username='" + username + '\'' +
            ", canAudit='" + canAudit + '\'' +
            ", canQuery='" + canQuery + '\'' +
            '}';
  }
}
