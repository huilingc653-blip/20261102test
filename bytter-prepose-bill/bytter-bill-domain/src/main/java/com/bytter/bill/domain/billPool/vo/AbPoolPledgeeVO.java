package com.bytter.bill.domain.billPool.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
public class AbPoolPledgeeVO  implements Serializable{
	
	private String id;
	
	private String pId;
	
	/**
     * 票据池ID
     */
    private String bpId;
    /**
     * 票据池代码
     */
    private String bpCode;
    /**
     * 票据池名称
     */
	@BtCompositeSearch(fieldName="ap.bp_Name")
    private String bpName;
	
	/**
	 * 票据池控制机构
	 */
	@BtCompositeSearch(fieldName="ap.bp_org")
	private String bpOrg;
    
	@BtCompositeSearch(fieldName="ap.bp_Type")
	@Dict(dictGroup = "AB_BILL_POOL_BP_TYPE", inject = "bpTypeName")
    private String bpType;
	private String bpTypeName;
	
    /**
     * 质权人账号
     */
    private String bankAcc;
    
    
    /**
     * 质权人名称
     */
    private String accName;
    /**
     * 质权人联行号
     */
    private String bankNo;
    /**
     * 质权人银行名称
     */
    private String bankName;
    
    /**
     * 状态：1启用、0禁用 默认禁用
     */
	@BtCompositeSearch(fieldName="ct.valid_Sign")
	@Dict(dictGroup = "AB_POOL_VALID_SIGN", inject = "validSignName")
	private String validSign;
	private String validSignName;
	/**
     * 备注
     */
	private String rmk;
	/**
     * 创建人id
     */
    private String createId;
    /**
     * 创建日期
     */
    private java.util.Date createTime;
	 /**
     * 更新人id
     */
    private String updateId;
    
    /**
     * 更新日期
     */
    private java.util.Date updateTime;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBpId() {
		return bpId;
	}
	public void setBpId(String bpId) {
		this.bpId = bpId;
	}
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getBankNo() {
		return bankNo;
	}
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	public String getUpdateId() {
		return updateId;
	}
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}
	public java.util.Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getBpCode() {
		return bpCode;
	}
	public void setBpCode(String bpCode) {
		this.bpCode = bpCode;
	}
	public String getBpName() {
		return bpName;
	}
	public void setBpName(String bpName) {
		this.bpName = bpName;
	}
	public AbPoolPledgeeVO(){
		
	}
	public String getBpOrg() {
		return bpOrg;
	}
	public void setBpOrg(String bpOrg) {
		this.bpOrg = bpOrg;
	}
	public String getValidSignName() {
		return validSignName;
	}
	public void setValidSignName(String validSignName) {
		this.validSignName = validSignName;
	}
	public String getBpType() {
		return bpType;
	}
	public void setBpType(String bpType) {
		this.bpType = bpType;
	}
	public String getBpTypeName() {
		return bpTypeName;
	}
	public void setBpTypeName(String bpTypeName) {
		this.bpTypeName = bpTypeName;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
    
	
    
}
