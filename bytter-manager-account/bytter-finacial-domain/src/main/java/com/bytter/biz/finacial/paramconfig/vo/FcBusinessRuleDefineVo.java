package com.bytter.biz.finacial.paramconfig.vo;
import com.bytter.core.annotation.Dict;
import java.util.List;
public class FcBusinessRuleDefineVo implements java.io.Serializable {
	private String id;
	private String subjectId;
	private String curId;
	private String voucherId;
	private String raceId;
	/** 所属网点 */
	private java.lang.String netId;
	/** 系统业务品种 */
	private java.lang.String sysRaceCode;
	/** 指定科目 */
	private java.lang.String subjCode;
	/** 币别代码 */
	private java.lang.String curCode;
	/** 有效标志 */
	private java.lang.String validSign;
	/** createTime */
	private java.util.Date createTime;
	/** createBy */
	private java.lang.String createBy;
	/** updateTime */
	private java.util.Date updateTime;
	/** updateBy */
	private java.lang.String updateBy;
	/**凭证种类*/
	private java.lang.String voucherType;
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(String voucherId) {
		this.voucherId = voucherId;
	}
	public String getRaceId() {
		return raceId;
	}
	public void setRaceId(String raceId) {
		this.raceId = raceId;
	}
	/** 分录类型 **/
	private List<FcVoucherEntryDefineVo> vouchers;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public java.lang.String getNetId() {
		return netId;
	}
	public void setNetId(java.lang.String netId) {
		this.netId = netId;
	}
	public java.lang.String getSysRaceCode() {
		return sysRaceCode;
	}
	public void setSysRaceCode(java.lang.String sysRaceCode) {
		this.sysRaceCode = sysRaceCode;
	}
	public java.lang.String getSubjCode() {
		return subjCode;
	}
	public void setSubjCode(java.lang.String subjCode) {
		this.subjCode = subjCode;
	}
	public java.lang.String getCurCode() {
		return curCode;
	}
	public void setCurCode(java.lang.String curCode) {
		this.curCode = curCode;
	}
	public java.lang.String getValidSign() {
		return validSign;
	}
	public void setValidSign(java.lang.String validSign) {
		this.validSign = validSign;
	}
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	public java.lang.String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(java.lang.String createBy) {
		this.createBy = createBy;
	}
	public java.util.Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
	public java.lang.String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(java.lang.String updateBy) {
		this.updateBy = updateBy;
	}
	public List<FcVoucherEntryDefineVo> getVouchers() {
		return vouchers;
	}
	public void setVouchers(List<FcVoucherEntryDefineVo> vouchers) {
		this.vouchers = vouchers;
	}
	public java.lang.String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(java.lang.String voucherType) {
		this.voucherType = voucherType;
	}
	
	
}
