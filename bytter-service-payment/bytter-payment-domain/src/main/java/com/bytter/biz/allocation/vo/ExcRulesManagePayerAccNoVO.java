package com.bytter.biz.allocation.vo;
import java.io.Serializable;
import java.util.Date;
/**
 * @author zhoup
 * @Description 例外规则表
 * @date 2018/2/6
 */
public class ExcRulesManagePayerAccNoVO implements Serializable {
	
	/**付款账号*/
	private String payerAccNo;
	public String getPayerAccNo() {
		return payerAccNo;
	}
	public void setPayerAccNo(String payerAccNo) {
		this.payerAccNo = payerAccNo;
	}
	
}
