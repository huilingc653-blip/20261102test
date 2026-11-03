package com.bytter.ras.vo;

import java.io.Serializable;

//import com.bytter.core.injections.IBaseDataInject;
import com.bytter.poi.handler.ExcelModel;

/**
 * 
* @Title:   营收稽核 导入配置明细VO类  
* @Package com.bytter.ras.vo   
* @Description:    TODO   
* @author: Jinyong.cao   
* @date:   2019-5-15 下午3:22:42   
* @version V1.0
 */
public class RasImportConfDtlVO extends ExcelModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7657353377247096295L;
	/** 导入格式主表ID */
	private String icId;
	/**
	 * 类型：对应字段: 营收 （serialNo交易流水号orderNO营收订单号shopName门店名称transDate交易日期
	 * money营收金额payType缴款方式custName客户名称custAcc客户账号rmk备注） 实收
	 * （serialNo交易流水号orderNO实收订单号shopName门店名称payeeAcc
	 * 收款账号payeeAccName收款户名payeeBankName收款账号开户行transDate交易日期payType缴款方式money
	 * 实收金额yhMoney优惠金额transMoney交易金额custName客户名称custAcc客户账号rmk备注）
	 */
	private String fieldKey;
	/** 对应起始值 */
	private String startVal;
	/** 对应结束值 */
	private String endVal;
	public String getIcId() {
		return icId;
	}
	public void setIcId(String icId) {
		this.icId = icId;
	}
	public String getFieldKey() {
		return fieldKey;
	}
	public void setFieldKey(String fieldKey) {
		this.fieldKey = fieldKey;
	}
	public String getStartVal() {
		return startVal;
	}
	public void setStartVal(String startVal) {
		this.startVal = startVal;
	}
	public String getEndVal() {
		return endVal;
	}
	public void setEndVal(String endVal) {
		this.endVal = endVal;
	}
	
	public RasImportConfDtlVO(){
		
	}
	
	@Override
	public String toString() {
		return "RasImportConfDtlVO [icId=" + icId + ", fieldKey=" + fieldKey
				+ ", startVal=" + startVal + ", endVal=" + endVal + "]";
	}
	
	
}
