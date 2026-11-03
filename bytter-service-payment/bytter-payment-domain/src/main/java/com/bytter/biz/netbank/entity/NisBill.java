package com.bytter.biz.netbank.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
import java.math.BigDecimal;
/**   
 * @Title: Entity
 * @Description: 付款票据关联表
 * @author onlineGenerator
 * @date 2017-05-23 15:20:33
 * @version 1.0
 *
 */
@Entity
@Table(name = "NIS_BILL", schema = "")
@SuppressWarnings("serial")
public class NisBill extends IdEntity implements java.io.Serializable {
	
	/**付款单ID*/
	private String payId;
	
	/**票据ID*/
	private String billId;
	/**挂接付款金额*/
	private BigDecimal balance;
	@Column(name ="PAY_ID",nullable=false,length=32)
	public String getPayId() {
		return payId;
	}
	public void setPayId(String payId) {
		this.payId = payId;
	}
	@Column(name ="BILL_ID",nullable=false,length=32)
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	@Column(name ="BALANCE",nullable=false,length=15,scale=2)
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
}
