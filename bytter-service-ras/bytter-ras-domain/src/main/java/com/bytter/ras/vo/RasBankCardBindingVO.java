package com.bytter.ras.vo;

import java.io.Serializable;
import java.util.Date;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
//import com.bytter.core.injections.IBaseDataInject;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
* @Title:  营收稽核银行卡绑定VO类   
* @Package com.bytter.ras.vo   
* @Description:    TODO   
* @author: Jinyong.cao   
* @date:   2019-5-5 下午5:58:19   
 */
public class RasBankCardBindingVO extends ExcelModel implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private java.lang.String id;
	
	/**所属单位ID*/
    @BtCompositeSearch(fieldName="s.id")
    private String  corpId;
    /**单位名称*/
    @Excel(name = "*单位名称",isImportField="true")
    private java.lang.String corpName;
    /**单位编码*/
    @Excel(name = "*单位代码",isImportField="true")
    private java.lang.String corpCode;
    /**门店ID*/
    @BtCompositeSearch(fieldName="r.id")
    private String  shopId;      
    /**门店名称 */
    @Excel(name = "*门店名称",isImportField="true")
    private java.lang.String shopName;
    /**门店编码*/
    @Excel(name = "*门店编码",isImportField="true")
    @BtCompositeSearch(fieldName="r.code")
    private java.lang.String shopCode;
    /**有效标志*/
    @Dict(dictGroup = "RAS_BANK_VALID_SIGN", inject = "validSignName")
 	@BtCompositeSearch(fieldName="ras.VALID_SIGN")
    private String  validSign;   
    private String  validSignName;   
    
    /**是否虚拟账号*/
    @Dict(dictGroup = "sf_yn", inject = "isVirtualAccName")
	@BtCompositeSearch(fieldName="ras.is_Virtual_Acc")
    private String  isVirtualAcc;
    @Excel(name = "*是否虚拟账号",isImportField="true")
    private String  isVirtualAccName;
    
    /**银行账户ID*/
    private String  accId;       
    /**银行账户*/
    @Excel(name = "*银行账号",isImportField="true")
    @BtCompositeSearch(fieldName="ras.acc_No")
    private String  accNo;      
    /**银行账号户名*/
    @Excel(name = "*账户名称",isImportField="true")
    private String  accName;    
    /**银行账户开户行名称*/
    @Excel(name = "*开户行名称",isImportField="true")
    private String  accBankName; 
    /**实体账户ID*/
    private String  entityAccId; 
    /**实体账号*/
    @Excel(name = "*关联实体账号",isImportField="true")
    private String  entityAccNo; 
    /**备注*/
    @Excel(name = "备注",isImportField="true")
    private String  rmk;       
    /**创建人名称*/
    private String  createName;  
    /**创建人登录名称*/
    private String  createBy;   
    /**创建日期*/
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date  createDate;   
    /**更新人名称*/
    private String  updateName;   
    /**更新人登录名称*/
    private String  updateBy;     
    /**更新日期*/
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date  updateDate;
    
    public RasBankCardBindingVO(){
    	
    }

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public java.lang.String getCorpName() {
		return corpName;
	}

	public void setCorpName(java.lang.String corpName) {
		this.corpName = corpName;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public java.lang.String getShopName() {
		return shopName;
	}

	public void setShopName(java.lang.String shopName) {
		this.shopName = shopName;
	}

	public java.lang.String getShopCode() {
		return shopCode;
	}

	public void setShopCode(java.lang.String shopCode) {
		this.shopCode = shopCode;
	}

	public String getValidSign() {
		return validSign;
	}

	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}

	public String getIsVirtualAcc() {
		return isVirtualAcc;
	}

	public void setIsVirtualAcc(String isVirtualAcc) {
		this.isVirtualAcc = isVirtualAcc;
	}

	public String getIsVirtualAccName() {
		return isVirtualAccName;
	}

	public void setIsVirtualAccName(String isVirtualAccName) {
		this.isVirtualAccName = isVirtualAccName;
	}

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public String getAccBankName() {
		return accBankName;
	}

	public void setAccBankName(String accBankName) {
		this.accBankName = accBankName;
	}

	public String getEntityAccId() {
		return entityAccId;
	}

	public void setEntityAccId(String entityAccId) {
		this.entityAccId = entityAccId;
	}

	public String getEntityAccNo() {
		return entityAccNo;
	}

	public void setEntityAccNo(String entityAccNo) {
		this.entityAccNo = entityAccNo;
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

	public String getValidSignName() {
		return validSignName;
	}

	public void setValidSignName(String validSignName) {
		this.validSignName = validSignName;
	}

	public java.lang.String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(java.lang.String corpCode) {
		this.corpCode = corpCode;
	}

	@Override
	public String toString() {
		return "RasBankCardBindingVO [id=" + id + ", corpId=" + corpId
				+ ", corpName=" + corpName + ", corpCode=" + corpCode
				+ ", shopId=" + shopId + ", shopName=" + shopName
				+ ", shopCode=" + shopCode + ", validSign=" + validSign
				+ ", validSignName=" + validSignName + ", isVirtualAcc="
				+ isVirtualAcc + ", isVirtualAccName=" + isVirtualAccName
				+ ", accId=" + accId + ", accNo=" + accNo + ", accName="
				+ accName + ", accBankName=" + accBankName + ", entityAccId="
				+ entityAccId + ", entityAccNo=" + entityAccNo + ", rmk=" + rmk
				+ ", createName=" + createName + ", createBy=" + createBy
				+ ", createDate=" + createDate + ", updateName=" + updateName
				+ ", updateBy=" + updateBy + ", updateDate=" + updateDate + "]";
	}
    
    
    
	  @Override
	  public String getBeanName() {
	    return "rasBankCardBindingService";
	  }
	
	
}
