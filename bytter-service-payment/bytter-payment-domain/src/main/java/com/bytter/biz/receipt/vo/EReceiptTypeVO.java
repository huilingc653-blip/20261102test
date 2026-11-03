package com.bytter.biz.receipt.vo;
/**
 * @Title: Entity
 * @Description: 电子回单类型表
 * @author onlineGenerator
 * @date 2017-06-05 10:52:28
 * @version 1.0
 *
 */
public class EReceiptTypeVO implements java.io.Serializable{
	/**类型编码*/
	private String billType;
	/**类型名称*/
	private String billName;
	/**对应表名*/
	private String taleName;
	/**有效标志 1-有效 0-无效*/
	private String validSign;
	private String billText;
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getBillName() {
		return billName;
	}
	public void setBillName(String billName) {
		this.billName = billName;
	}
	public String getTaleName() {
		return taleName;
	}
	public void setTaleName(String taleName) {
		this.taleName = taleName;
	}
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	public String getBillText() {
		return billText;
	}
	public void setBillText(String billText) {
		this.billText = billText;
	}
}
