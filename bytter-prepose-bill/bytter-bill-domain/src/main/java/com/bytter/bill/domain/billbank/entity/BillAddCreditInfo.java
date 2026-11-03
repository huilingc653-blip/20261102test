package com.bytter.bill.domain.billbank.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**  *
 * <br>
 * <b>功能：</b>增信信息<br>
 * <b>作者：</b>zhengg<br>
 * <b>日期：</b> 2023-12-25 <br>
 * @throws Exception
 */
@Entity
@Table(name = "AB_ADD_CREDIT_INFO",schema = "")
public class BillAddCreditInfo extends IdEntity implements java.io.Serializable{
	private static final long serialVersionUID = 1L;

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

	private Date createTime;

	@Column(name = "VID")
	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	@Column(name = "TYPE_NAME")
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "BL_UNN_ID_NO")
	public String getBlUnnIdNo() {
		return blUnnIdNo;
	}

	public void setBlUnnIdNo(String blUnnIdNo) {
		this.blUnnIdNo = blUnnIdNo;
	}

	@Column(name = "GRA_ADDRESS")
	public String getGraAddress() {
		return graAddress;
	}

	public void setGraAddress(String graAddress) {
		this.graAddress = graAddress;
	}

	@Column(name = "BILL_ACC_NAME")
	public String getBillAccName() {
		return billAccName;
	}

	public void setBillAccName(String billAccName) {
		this.billAccName = billAccName;
	}

	@Column(name = "CHANNEL_NAME")
	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	@Column(name = "BILL_ACC_NO")
	public String getBillAccNo() {
		return billAccNo;
	}

	public void setBillAccNo(String billAccNo) {
		this.billAccNo = billAccNo;
	}

	@Column(name = "BANK_NAME")
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	@Column(name = "BANK_NO")
	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	@Column(name = "BANK_ACC_NO")
	public String getBankAccNo() {
		return bankAccNo;
	}

	public void setBankAccNo(String bankAccNo) {
		this.bankAccNo = bankAccNo;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
