package com.bytter.biz.bdlink.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author onlineGenerator
 * @Description 资金调拨流水规则维护
 * @date 2024-05-08
 */
@Data
public class NisFlowRulePurposeDTO implements Serializable{
	/** id*/
	private String id;
	/** 规则id*/
	private String ruleId;
	/** 用途*/
	private String purpose;
	/** 银行类型id*/
	private String bankTypeId;
	/** 银行类型名称*/
	private String bankName;
	/** 银行接口代码*/
	private String bifCode;

}
