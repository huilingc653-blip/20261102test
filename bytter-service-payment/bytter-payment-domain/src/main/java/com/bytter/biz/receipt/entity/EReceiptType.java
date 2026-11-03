package com.bytter.biz.receipt.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: 电子回单类型表
 * @author onlineGenerator
 * @date 2017-06-05 10:52:28
 * @version 1.0
 *
 */
@Entity
@Table(name = "E_RECEIPT_TYPE", schema = "")
@SuppressWarnings("serial")
public class EReceiptType implements java.io.Serializable{
	/**类型编码*/
	private String billType;
	/**类型名称*/
	private String billName;
	/**对应表名*/
	private String taleName;
	/**有效标志 1-有效 0-无效*/
	private String validSign;
	@Id
	@Column(name = "bill_Type", unique = true, length = 32)
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	@Column(name ="BILL_NAME",nullable=false,length=40)
	public String getBillName() {
		return billName;
	}
	public void setBillName(String billName) {
		this.billName = billName;
	}
	@Column(name ="TABLE_NAME",nullable=false,length=40)
	public String getTaleName() {
		return taleName;
	}
	public void setTaleName(String taleName) {
		this.taleName = taleName;
	}
	@Column(name ="VALID_SIGN",nullable=false,length=1)
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
}
