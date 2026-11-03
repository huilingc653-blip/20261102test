package com.bytter.check.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: FC_BANK_BILL_PROC
 * @author onlineGenerator
 * @date 2017-10-17 17:03:07
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_BANK_BILL_PROC", schema = "")
@SuppressWarnings("serial")
public class FcBankBillProc extends IdEntity implements java.io.Serializable {
	/**余额调节表ID*/
	private String ajustId;
	/**银行对账单ID*/
	private String fcBankBillId;
	/**历史对账标志*/
	private String compareSignHis;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  余额调节表ID
	 */
	@Column(name ="AJUST_ID")
	public String getAjustId(){
		return this.ajustId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  余额调节表ID
	 */
	public void setAjustId(String ajustId){
		this.ajustId = ajustId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行对账单ID
	 */
	@Column(name ="FC_BANK_BILL_ID")
	public String getFcBankBillId(){
		return this.fcBankBillId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行对账单ID
	 */
	public void setFcBankBillId(String fcBankBillId){
		this.fcBankBillId = fcBankBillId;
	}
	@Column(name ="COMPARE_SIGN_HIS")
	public String getCompareSignHis() {
		return compareSignHis;
	}
	public void setCompareSignHis(String compareSignHis) {
		this.compareSignHis = compareSignHis;
	}
	
}
