package com.bytter.biz.finacial.api.dto;
import java.util.List;

public class FcOtherBusinessRuleDefineDTO implements java.io.Serializable {
	private String id;
	/**编码*/
	private String code;
	/**名称*/
	private String name;
	/** 所属网点 */
	private String netId;
	/** 系统业务品种 */
	private String sysRaceCode;
	private String cdSign;
	/** 币别代码 */
	private String curCode;
	/** 有效标志 */
	private String validSign;
	/** createTime */
	private java.util.Date createTime;
	/** createBy */
	private String createBy;
	/** updateTime */
	private java.util.Date updateTime;
	/** updateBy */
	private String updateBy;
	/**凭证种类*/
	private String voucherType;
	/** 分录类型 **/
	private List<FcVoucherEntryDefineDTO> vouchers;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getSysRaceCode() {
		return sysRaceCode;
	}
	public void setSysRaceCode(String sysRaceCode) {
		this.sysRaceCode = sysRaceCode;
	}
	public String getCurCode() {
		return curCode;
	}
	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public java.util.Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public List<FcVoucherEntryDefineDTO> getVouchers() {
		return vouchers;
	}
	public void setVouchers(List<FcVoucherEntryDefineDTO> vouchers) {
		this.vouchers = vouchers;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}

	public String getCode() {
		return code;
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

	public String getCdSign() {
		return cdSign;
	}

	public void setCdSign(String cdSign) {
		this.cdSign = cdSign;
	}
}
