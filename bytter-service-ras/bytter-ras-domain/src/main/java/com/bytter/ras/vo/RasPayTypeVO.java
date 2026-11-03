package com.bytter.ras.vo;

import com.bytter.core.annotation.Dict;
//import com.bytter.core.injections.IBaseDataInject;
import com.bytter.ras.api.constant.RasConstant;

/**
 * @Title: 缴款方式详细
 * @author zhengg
 * @date 2019-05-15 15:43:05
 * @version 1.0
 */
public class RasPayTypeVO implements java.io.Serializable{
	
	public RasPayTypeVO(){
		
	}

	public RasPayTypeVO(String payType){
		this.payType=payType;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -2619499051426138041L;
	/** 缴款方式:多个,隔开001现金002支付宝003微信004银联POS机005收钱吧006其它 */
    @Dict(dictGroup = RasConstant.RAS_PAYTYPE_DICT, inject = "payTypeName")
	private String payType;
	
	private String payTypeName;

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}
	
	
}
