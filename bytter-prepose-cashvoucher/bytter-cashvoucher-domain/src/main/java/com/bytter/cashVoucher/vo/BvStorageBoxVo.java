package com.bytter.cashVoucher.vo;

import com.bytter.core.annotation.BtCompositeSearch;

import java.io.Serializable;
/**   
 * @Title: Vo
 * @Description: 库箱维护vo
 * @author Songkk
 * @date 2017-05-25 18:17:27
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class BvStorageBoxVo implements Serializable  {
	// @Dict(tableName="BV_STORAGE_BOX",valueFieldName="id",showFiledName="name",inject="name")
	private String id;
	/**更新日期*/
	private java.util.Date updateDate;
	/**库箱代码*/
	@BtCompositeSearch(fieldName="CODE")
	private String code;
	/**库存名称*/
	@BtCompositeSearch(fieldName="NAME")
	private String name;
	/**库箱所属单位*/
	@BtCompositeSearch(fieldName="CORP_ID")
	private String corpId;
	@BtCompositeSearch(fieldName="CORP_ID")
	private String corpName;
	/**库箱保管员*/
	@BtCompositeSearch(fieldName="KEEPER_UID")
	// @Dict(tableName="sys_user",inject = "keeperName",showFiledName="username",valueFieldName="id")
	private String keeperUid;
	/**库箱管理员名称*/
	@BtCompositeSearch(fieldName="KEEPER_UID")
	private String keeperName;
	/**库箱地址*/
	private String address;
	/**备注*/
	private String rmk;
	/**有效标志：0，停用  1，启用  2，已删除*/
	@BtCompositeSearch(fieldName="valid_Sign")
	// @Dict(dictGroup = "bvstorageBox_status", inject = "validSignName")
	private String validSign;
	@BtCompositeSearch(fieldName="valid_Sign")
	private String validSignName;
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getValidSignName() {
		return validSignName;
	}
	public void setValidSignName(String validSignName) {
		this.validSignName = validSignName;
	}
	/**创建人名称*/
	private String createName;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人名称*/
	private String updateName;
	public java.util.Date getUpdateDate() {
		return updateDate;
	}
	/**更新人登录名称*/
	private String updateBy;
	@Override
	public String toString() {
		return "BvStorageBoxVo [updateDate=" + updateDate + ", code=" + code + ", name=" + name + ", corpId=" + corpId
				+ ", keeperUid=" + keeperUid + ", address=" + address + ", rmk=" + rmk + ", validSign=" + validSign
				+ ", createName=" + createName + ", createBy=" + createBy + ", createDate=" + createDate
				+ ", updateName=" + updateName + ", updateBy=" + updateBy + ", keeperName=" + keeperName + "]";
	}
	public void setUpdateDate(java.util.Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getCode() {
		return code;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getKeeperUid() {
		return keeperUid;
	}
	public void setKeeperUid(String keeperUid) {
		this.keeperUid = keeperUid;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
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
	public java.util.Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(java.util.Date createDate) {
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
	public String getKeeperName() {
		return keeperName;
	}
	public void setKeeperName(String keeperName) {
		this.keeperName = keeperName;
	}
	
}
