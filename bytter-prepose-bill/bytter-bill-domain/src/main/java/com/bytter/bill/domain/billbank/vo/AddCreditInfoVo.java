/**  *
 *
 */
package com.bytter.bill.domain.billbank.vo;

import java.io.Serializable;
import java.sql.Date;

/**  *
 * @author zhengg
 * 增信信息
 */
public class AddCreditInfoVo implements Serializable {

	private String vid;//主表ID

	private String typeName;//保证人类型名称

	private String name;//保证人全称

	private String blUnnIdNo;//统一社会信用代码

	private String graAddress;//保证人地址

	private String billAccName;//账号名称

	private String channelName;//渠道名称

	private String billAccNo;//票据账号

	private String bankName;//开户银行名

	private String bankNo;//开户银行行号

	private String bankAccNo;//开户银行账号

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBlUnnIdNo() {
		return blUnnIdNo;
	}

	public void setBlUnnIdNo(String blUnnIdNo) {
		this.blUnnIdNo = blUnnIdNo;
	}

	public String getGraAddress() {
		return graAddress;
	}

	public void setGraAddress(String graAddress) {
		this.graAddress = graAddress;
	}

	public String getBillAccName() {
		return billAccName;
	}

	public void setBillAccName(String billAccName) {
		this.billAccName = billAccName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getBillAccNo() {
		return billAccNo;
	}

	public void setBillAccNo(String billAccNo) {
		this.billAccNo = billAccNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getBankAccNo() {
		return bankAccNo;
	}

	public void setBankAccNo(String bankAccNo) {
		this.bankAccNo = bankAccNo;
	}

	@Override
	public String toString() {
		return "AddCreditInfoVo{" +
				"typeName='" + typeName + '\'' +
				", name='" + name + '\'' +
				", blUnnIdNo='" + blUnnIdNo + '\'' +
				", graAddress='" + graAddress + '\'' +
				", billAccName='" + billAccName + '\'' +
				", channelName='" + channelName + '\'' +
				", billAccNo='" + billAccNo + '\'' +
				", bankName='" + bankName + '\'' +
				", bankNo='" + bankNo + '\'' +
				", bankAccNo='" + bankAccNo + '\'' +
				'}';
	}
}
