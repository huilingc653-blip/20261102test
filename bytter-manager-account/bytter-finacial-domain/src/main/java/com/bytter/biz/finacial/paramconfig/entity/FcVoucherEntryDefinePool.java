package com.bytter.biz.finacial.paramconfig.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: FC_VOUCHER_ENTRY_DEFINE_POOL
 * @author onlineGenerator
 * @date 2017-05-22 16:44:06
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_VOUCHER_ENTRY_DEFINE_POOL", schema = "")
@SuppressWarnings("serial")
public class FcVoucherEntryDefinePool extends IdEntity implements java.io.Serializable{
	/**业务规则ID*/
	private String businessRuleDefineId;
	/**所属类型*/
	private Integer belongType;
	/**借贷标识*/
	private Integer balanceWay;
	/**科目*/
	private String subjectCode;
	/**账号*/
	private String account;
	/**币别代码*/
	private String curId;
	/**摘要*/
	private String summary;
	/**分录类型*/
	private Integer entryType;
	/**组号*/
	private Integer groupNum;
	/**凭证种类*/
	private String voucherType;
	/**createTime*/
	private Date createTime;
	/**createBy*/
	private String createBy;
	/**updateTime*/
	private Date updateTime;
	/**updateBy*/
	private String updateBy;
	@Column(name="BUSINESS_RULE_DEFINE_ID")
	public String getBusinessRuleDefineId() {
		return businessRuleDefineId;
	}
	public void setBusinessRuleDefineId(String businessRuleDefineId) {
		this.businessRuleDefineId = businessRuleDefineId;
	}
	@Column(name="BELONG_TYPE")
	public Integer getBelongType() {
		return belongType;
	}
	public void setBelongType(Integer belongType) {
		this.belongType = belongType;
	}
	@Column(name="BALANCE_WAY")
	public Integer getBalanceWay() {
		return balanceWay;
	}
	public void setBalanceWay(Integer balanceWay) {
		this.balanceWay = balanceWay;
	}
	@Column(name="SUBJECT_CODE")
	public String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	@Column(name="ACCOUNT")
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	@Column(name="CUR_ID")
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	@Column(name="SUMMARY")
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	@Column(name="ENTRY_TYPE")
	public Integer getEntryType() {
		return entryType;
	}
	public void setEntryType(Integer entryType) {
		this.entryType = entryType;
	}
	@Column(name="GROUP_NUM")
	public Integer getGroupNum() {
		return groupNum;
	}
	public void setGroupNum(Integer groupNum) {
		this.groupNum = groupNum;
	}
	@Column(name="VOUCHER_TYPE")
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	@Column(name="CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name="CREATE_BY")
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	@Column(name="UPDATE_TIME")
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@Column(name="UPDATE_BY")
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
}
