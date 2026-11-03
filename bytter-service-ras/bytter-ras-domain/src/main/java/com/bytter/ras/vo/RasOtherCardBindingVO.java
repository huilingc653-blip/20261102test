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
* @Title:  营收稽核第三方账户绑定VO类   
* @Package com.bytter.ras.vo   
* @Description:    TODO   
* @author: Jinyong.cao   
* @date:   2019-5-6 下午5:58:19   
 */
public class RasOtherCardBindingVO extends ExcelModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6037832429281315456L;

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
    /**门店编码*/
    @Excel(name = "*门店编码",isImportField="true")
    @BtCompositeSearch(fieldName="r.code")
    private java.lang.String shopCode;
    /**门店名称 */
    @Excel(name = "*门店名称",isImportField="true")
    private java.lang.String shopName;
    /**有效标志*/
    @Dict(dictGroup = "RAS_BANK_VALID_SIGN", inject = "validSignName")
    private String validSign;
    private String validSignName;   
    /**缴款方式*/
    @Dict(dictGroup = "RAS_PAY_TYPE", inject = "payTypeName")
    @BtCompositeSearch(fieldName="ras.pay_Type")
    private String payType; 
    @Excel(name = "*缴款方式",isImportField="true")
    private String payTypeName;
    /**账号*/
    @Excel(name = "*账号",isImportField="true")
    @BtCompositeSearch(fieldName="ras.acc_No")
    private String accNo;
    /**账号名称*/
    @Excel(name = "*账户名称",isImportField="true")
    private String accName;  
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
    
    public RasOtherCardBindingVO(){
    	
    }
    
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
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

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public String getValidSignName() {
		return validSignName;
	}

	public void setValidSignName(String validSignName) {
		this.validSignName = validSignName;
	}

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}
	
	
	public java.lang.String getCorpName() {
		return corpName;
	}
	
	public void setCorpName(java.lang.String corpName) {
		this.corpName = corpName;
	}
	
	public java.lang.String getCorpCode() {
		return corpCode;
	}
	
	public void setCorpCode(java.lang.String corpCode) {
		this.corpCode = corpCode;
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
	

	@Override
	public String toString() {
		return "RasOtherCardBindingVO [id=" + id + ", corpId=" + corpId
				+ ", corpName=" + corpName + ", corpCode=" + corpCode + ", shopId="
				+ shopId + ", shopCode=" + shopCode + ", shopName=" + shopName
				+ ", validSign=" + validSign + ", validSignName=" + validSignName
				+ ", payType=" + payType + ", payTypeName=" + payTypeName
				+ ", accNo=" + accNo + ", accName=" + accName + ", rmk=" + rmk
				+ ", createName=" + createName + ", createBy=" + createBy
				+ ", createDate=" + createDate + ", updateName=" + updateName
				+ ", updateBy=" + updateBy + ", updateDate=" + updateDate + "]";
	}
	
	@Override
	public String getBeanName() {
	   return "rasOtherCardBindingService";
	}
	
}
