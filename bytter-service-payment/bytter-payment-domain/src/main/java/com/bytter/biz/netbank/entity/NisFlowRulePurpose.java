package com.bytter.biz.netbank.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 资金调拨流水规则用途表
 * @author onlineGenerator
 * @date 2024-05-08
 * @version 1.0
 *
 */
@Entity
@Table(name = "NIS_FLOW_RULE_PURPOSE", schema = "")
@SuppressWarnings("serial")
public class NisFlowRulePurpose extends IdEntity implements java.io.Serializable {
	/** 规则id*/
	private String ruleId;
	/** 用途*/
	private String purpose;
	/** 银行类型id*/
	private String bankTypeId;

	@Column(name ="RULE_ID",nullable=false,length=32)
	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	@Column(name ="PURPOSE",nullable=false,length=100)
	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	@Column(name ="BANK_TYPE_ID",nullable=true,length=32)
	public String getBankTypeId() {
		return bankTypeId;
	}

	public void setBankTypeId(String bankTypeId) {
		this.bankTypeId = bankTypeId;
	}
}
