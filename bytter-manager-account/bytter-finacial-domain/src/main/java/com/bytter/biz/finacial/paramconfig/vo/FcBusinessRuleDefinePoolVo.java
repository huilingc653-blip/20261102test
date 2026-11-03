package com.bytter.biz.finacial.paramconfig.vo;
import com.bytter.core.annotation.Dict;
import java.util.Date;
import java.util.List;
public class FcBusinessRuleDefinePoolVo implements java.io.Serializable  {
	private String id;
	/** 所属资金池 */
	private String poolId;
	/** 指定科目 */
	private String subjectId;
	private String subjectName;
	/** 币别代码 */
	private String curId;
	private String curName;
	/** 有效标志 */
	private String validSign;
	/**凭证种类*/
	private String voucherType;
	private String voucherName;
	/** 业务品种 */
	private String sysRaceCode;
	/** createTime */
	private java.util.Date createTime;
	/** createBy */
	private String createBy;
	/** updateTime */
	private java.util.Date updateTime;
	/** updateBy */
	private String updateBy;
	
	/** 分录类型 **/
	private List<FcVoucherEntryDefineVo> vouchers;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPoolId() {
		return poolId;
	}
	public void setPoolId(String poolId) {
		this.poolId = poolId;
	}
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
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public String getSysRaceCode() {
		return sysRaceCode;
	}
	public void setSysRaceCode(String sysRaceCode) {
		this.sysRaceCode = sysRaceCode;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public List<FcVoucherEntryDefineVo> getVouchers() {
		return vouchers;
	}
	public void setVouchers(List<FcVoucherEntryDefineVo> vouchers) {
		this.vouchers = vouchers;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public String getVoucherName() {
		return voucherName;
	}
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}
}
