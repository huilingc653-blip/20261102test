package com.bytter.ras.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bytter.core.base.entity.IdEntity;

/**
 * 
* @Title:  营收稽核银行卡绑定数据库实体类
* @Package com.bytter.ras.entity   
* @Description:    TODO   
* @author: Jinyong.cao   
* @date:   2019-5-5 下午5:57:26   
 */
@Entity
@Table(name = "RAS_BANK_CARD_BINDING", schema = "")
@SuppressWarnings("serial")
public class RasBankCardBinding  extends IdEntity implements java.io.Serializable{
	
	/**所属单位ID*/
    private String  corpId;
    /**门店ID*/
    private String  shopId;      
    /**有效标志*/
    private String  validSign;   
    /**是否虚拟账号*/
    private String  isVirtualAcc;
    /**银行账户ID*/
    private String  accId;       
    /**银行账户*/
    private String  accNo;      
    /**银行账号户名*/
    private String  accName;    
    /**银行账户开户行名称*/
    private String  accBankName; 
    /**实体账户ID*/
    private String  entityAccId; 
    /**实体账号*/
    private String  entityAccNo; 
    /**备注*/
    private String  rmk;       
    /**创建人名称*/
    private String  createName;  
    /**创建人登录名称*/
    private String  createBy;   
    /**创建日期*/
    private Date  createDate;   
    /**更新人名称*/
    private String  updateName;   
    /**更新人登录名称*/
    private String  updateBy;     
    /**更新日期*/
    private Date  updateDate;
    
    
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
	
	@Column(name ="VALID_SIGN",nullable=true,length=1)
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	
	@Column(name ="IS_VIRTUAL_ACC",nullable=true,length=1)
	public String getIsVirtualAcc() {
		return isVirtualAcc;
	}
	public void setIsVirtualAcc(String isVirtualAcc) {
		this.isVirtualAcc = isVirtualAcc;
	}
	
	@Column(name ="ACC_ID",nullable=true,length=32)
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	
	@Column(name ="ACC_NO",nullable=true,length=50)
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	
	@Column(name ="ACC_NAME",nullable=true,length=50)
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	
	@Column(name ="ACC_BANK_NAME",nullable=true,length=200)
	public String getAccBankName() {
		return accBankName;
	}
	public void setAccBankName(String accBankName) {
		this.accBankName = accBankName;
	}
	
	@Column(name ="ENTITY_ACC_ID",nullable=true,length=32)
	public String getEntityAccId() {
		return entityAccId;
	}
	public void setEntityAccId(String entityAccId) {
		this.entityAccId = entityAccId;
	}
	
	@Column(name ="ENTITY_ACC_NO",nullable=true,length=50)
	public String getEntityAccNo() {
		return entityAccNo;
	}
	public void setEntityAccNo(String entityAccNo) {
		this.entityAccNo = entityAccNo;
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
 
}
