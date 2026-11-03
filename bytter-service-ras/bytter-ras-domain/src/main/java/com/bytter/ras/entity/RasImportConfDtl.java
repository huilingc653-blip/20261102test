package com.bytter.ras.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bytter.core.base.entity.IdEntity;
/**
 * 
* @Title:  营收稽核 导入配置明细Entity类  
* @Package com.bytter.ras.entity   
* @Description:    TODO   
* @author: Jinyong.cao   
* @date:   2019-5-15 下午3:10:41   
* @version V1.0
 */

@Entity
@Table(name = "RAS_IMPORT_CONF_DTL", schema = "")
@SuppressWarnings("serial")
public class RasImportConfDtl extends IdEntity implements java.io.Serializable {
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

	@Column(name ="ic_Id",nullable=false,length=32)
	public String getIcId() {
		return icId;
	}

	public void setIcId(String icId) {
		this.icId = icId;
	}

	@Column(name ="FIELD_KEY",nullable=true,length=20)
	public String getFieldKey() {
		return fieldKey;
	}

	public void setFieldKey(String fieldKey) {
		this.fieldKey = fieldKey;
	}

	@Column(name ="START_VAL",nullable=true,length=20)
	public String getStartVal() {
		return startVal;
	}

	public void setStartVal(String startVal) {
		this.startVal = startVal;
	}

	@Column(name ="END_VAL",nullable=true,length=20)
	public String getEndVal() {
		return endVal;
	}

	public void setEndVal(String endVal) {
		this.endVal = endVal;
	}

}
