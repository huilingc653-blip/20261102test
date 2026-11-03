package com.bytter.ras.vo;

import javax.persistence.Entity;

import com.bytter.core.annotation.Dict;
//import com.bytter.core.injections.IBaseDataInject;
import com.bytter.ras.api.constant.RasConstant;

/**
 * @Title: 策略规则详细
 * @author zhengg
 * @date 2019-05-15 15:43:05
 * @version 1.0
 */
public class RasRulesVO implements java.io.Serializable{
	
	public RasRulesVO(){
		
	}

	public RasRulesVO(String rules){
		this.rules=rules;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -2619499051426138041L;
	/** 规则：多个以,隔开01订单号02交易日期03交易流水04门店名称05单位名称06现金汇总稽核07其他方式汇总稽核 */
    @Dict(dictGroup = RasConstant.RAS_AUDIT_RULES_DICT, inject = "rulesName")
	private String rules;
	
	private String rulesName;

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	public String getRulesName() {
		return rulesName;
	}

	public void setRulesName(String rulesName) {
		this.rulesName = rulesName;
	}

	@Override
	public String toString() {
		return "RasRulesVO [rules=" + rules + ", rulesName=" + rulesName + "]";
	}
}
