package com.bytter.biz.finacial.api.dto;
import java.math.BigDecimal;
import java.util.Date;
import java.lang.String;
import java.lang.Double;
import java.lang.Integer;
import javax.xml.soap.Text;
import java.sql.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;
import com.bytter.core.base.entity.IdEntity;
/**
 * Title: Entity
 * Description: FC_VOUCHER_ENTRY_DEFINE
 * @author onlineGenerator
 * @since 2017-05-22 16:44:06
 * @version 1.0
 *
 */
public class FcVoucherEntryDefineDTO implements java.io.Serializable {
	private java.lang.String id;
	/** 业务规则ID */
	private java.lang.String businessRuleDefineId;
	/** 所属类型 */
	private java.lang.Integer belongType;
	/** 借贷标识 */
	private java.lang.Integer balanceWay;
	/** 科目 */
	private java.lang.String subjectCode;
	/** 凭证种类 */
	private java.lang.String voucherType;
	/** 账号 */
	private java.lang.String account;
	/** 币别代码 */
	private java.lang.String curCode;
	/** 摘要 */
	private java.lang.String summary;
	/** 分录类型 */
	private java.lang.Integer entryType;
	/** 组号 */
	private java.lang.Integer groupNum;
	/** 临时组号 */
	private java.lang.Integer groupNums;

	/**
	 * 内部账号
	 */
	private java.lang.String blnctrAcc;

	public java.lang.String getId() {
		return id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getBusinessRuleDefineId() {
		return businessRuleDefineId;
	}
	public void setBusinessRuleDefineId(java.lang.String businessRuleDefineId) {
		this.businessRuleDefineId = businessRuleDefineId;
	}
	public java.lang.Integer getBelongType() {
		return belongType;
	}
	public void setBelongType(java.lang.Integer belongType) {
		this.belongType = belongType;
	}
	public java.lang.Integer getBalanceWay() {
		return balanceWay;
	}
	public void setBalanceWay(java.lang.Integer balanceWay) {
		this.balanceWay = balanceWay;
	}
	public java.lang.String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(java.lang.String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public java.lang.String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(java.lang.String voucherType) {
		this.voucherType = voucherType;
	}
	public java.lang.String getAccount() {
		return account;
	}
	public void setAccount(java.lang.String account) {
		this.account = account;
	}
	public java.lang.String getCurCode() {
		return curCode;
	}
	public void setCurCode(java.lang.String curCode) {
		this.curCode = curCode;
	}
	public java.lang.String getSummary() {
		return summary;
	}
	public void setSummary(java.lang.String summary) {
		this.summary = summary;
	}
	public java.lang.Integer getEntryType() {
		return entryType;
	}
	public void setEntryType(java.lang.Integer entryType) {
		this.entryType = entryType;
	}
	public java.lang.Integer getGroupNum() {
		return groupNum;
	}
	public void setGroupNum(java.lang.Integer groupNum) {
		this.groupNum = groupNum;
	}
	public java.lang.Integer getGroupNums() {
		return groupNums;
	}
	public void setGroupNums(java.lang.Integer groupNums) {
		this.groupNums = groupNums;
	}

	public String getBlnctrAcc() {
		return blnctrAcc;
	}

	public void setBlnctrAcc(String blnctrAcc) {
		this.blnctrAcc = blnctrAcc;
	}
}