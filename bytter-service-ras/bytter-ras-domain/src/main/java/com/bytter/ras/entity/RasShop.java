package com.bytter.ras.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bytter.core.base.entity.IdEntity;

/**
 * @author yuwj
 * @projectName T2
 * @description: 营收稽核门店维护实体类
 * @date 2019/5/6 8:54
 */
@Entity
@Table(name = "RAS_SHOP", schema = "")
public class RasShop extends IdEntity implements java.io.Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**门店编码*/
    private String code;
    /**门店名称*/
    private String name;
    /**单位*/
    private String corpId;
    /**门店类别*/
    private String shopType;
    /**开始稽核日期*/
    private Date asDate;
    /**尾款阈值*/
    private BigDecimal ecMoney;
    /**商户号*/
    private String shopNo;
    /**商户名称*/
    private String shopName;
    /**备注*/
    private String rmk;
    /**状态*/
    private String vSign;
    /**是否单位基本门店 1是，0否*/
    private String isCorp;
    /**对应其他系统门店编码*/
    private String extemalNo;
    /**创建人登录名称*/
    private String createBy;
    /**创建人名称*/
    private String createName;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新人名称*/
    private String updateName;
    /**更新日期*/
    private Date updateDate;

    @Column(name ="CODE",nullable=false)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    @Column(name ="NAME",nullable=false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name ="CORP_ID",nullable=false,length=32)
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name ="SHOP_TYPE",nullable=false)
    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }
    @Column(name ="AUDIT_START_DATE",nullable=false)
    public Date getAsDate() {
        return asDate;
    }

    public void setAsDate(Date asDate) {
        this.asDate = asDate;
    }
    @Column(name ="END_CHECK_MONEY",nullable=false)
    public BigDecimal getEcMoney() {
        return ecMoney;
    }

    public void setEcMoney(BigDecimal ecMoney) {
        this.ecMoney = ecMoney;
    }
    @Column(name ="SHOP_NO",nullable=true)
    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }
    @Column(name ="SHOP_NAME",nullable=true)
    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    @Column(name ="RMK",nullable=true)
    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name ="VALID_SIGN",nullable=false)
    public String getvSign() {
        return vSign;
    }

    public void setvSign(String vSign) {
        this.vSign = vSign;
    }
    @Column(name ="IS_CORP",nullable=false)
    public String getIsCorp() {
        return isCorp;
    }

    public void setIsCorp(String isCorp) {
        this.isCorp = isCorp;
    }
    @Column(name ="EXTEMAL_NO",nullable=true)
    public String getExtemalNo() {
        return extemalNo;
    }

    public void setExtemalNo(String extemalNo) {
        this.extemalNo = extemalNo;
    }
    @Column(name ="CREATE_BY",nullable=true)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name ="CREATE_NAME",nullable=true)
    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }
    @Column(name ="CREATE_DATE",nullable=true)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name ="UPDATE_BY",nullable=true)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name ="UPDATE_NAME",nullable=true)
    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }
    @Column(name ="UPDATE_DATE",nullable=true)
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
