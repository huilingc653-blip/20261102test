package com.bytter.ras.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bytter.core.base.entity.IdEntity;

/**
 * @Title: Entity
 * @Description: 稽核策略维护表
 * @author onlineGenerator
 * @date 2019-05-15 15:43:05
 * @version 1.0
 *
 */
@Entity
@Table(name = "RAS_AUDIT_STRATEGY", schema = "")
public class RasAuditStrategy extends IdEntity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	/** 稽核规则名称 */
	private String name;
	/** 缴款方式缴款方式:001现金002支付宝003微信004银联POS机005收钱吧006其它 */
	private String payType;
	/** 排序编号 */
	private Integer sortNo;
	/** 有效标志：0，禁用 1，启用 */
	private String validSign;
	/** 规则：多个以,隔开01订单号02交易日期03交易流水04门店名称05单位名称06现金汇总稽核07其他方式汇总稽核 */
	private String rules;
	/** 备注 */
	private String rmk;
	/** 创建人名称 */
	private String createName;
	/** 创建人登录名称 */
	private String createBy;
	/** 创建日期 */
	private Date createDate;
	/** 创建人名称 */
	private String updateName;
	/** 创建人登录名称 */
	private String updateBy;
	/** 创建日期 */
	private Date updateDate;

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "pay_Type")
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	@Column(name = "sort_No")
	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	@Column(name = "valid_Sign")
	public String getValidSign() {
		return validSign;
	}

	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}

	@Column(name = "rules")
	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	@Column(name = "rmk")
	public String getRmk() {
		return rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}

	@Column(name = "CREATE_NAME")
	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	@Column(name = "CREATE_BY")
	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Column(name = "CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "UPDATE_NAME")
	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	@Column(name = "UPDATE_BY")
	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	@Column(name = "UPDATE_DATE")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	

	@Override
	public String toString() {
		return "RasAuditStrategy [name=" + name + ", payType=" + payType + ", sortNo=" + sortNo + ", validSign="
				+ validSign + ", rules=" + rules + ", rmk=" + rmk + ", createName=" + createName + ", createBy="
				+ createBy + ", createDate=" + createDate + ", updateName=" + updateName + ", updateBy=" + updateBy
				+ ", updateDate=" + updateDate + ", getName()=" + getName() + ", getPayType()=" + getPayType()
				+ ", getSortNo()=" + getSortNo() + ", getValidSign()=" + getValidSign() + ", getRules()=" + getRules()
				+ ", getRmk()=" + getRmk() + ", getCreateName()=" + getCreateName() + ", getCreateBy()=" + getCreateBy()
				+ ", getCreateDate()=" + getCreateDate() + ", getUpdateName()=" + getUpdateName() + ", getUpdateBy()="
				+ getUpdateBy() + ", getUpdateDate()=" + getUpdateDate() + "]";
	}
	
}
