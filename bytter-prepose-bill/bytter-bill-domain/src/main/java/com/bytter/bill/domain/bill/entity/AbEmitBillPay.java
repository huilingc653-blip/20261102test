package com.bytter.bill.domain.bill.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
/**
 * @Title: Entity
 * @Description: 票据线上兑付信息
 * @author onlineGenerator
 * @date 2017-11-8 15:40:01
 * @version 1.0
 *
 */
@Entity
@Table(name = "AB_EMITBILL_PAY", schema = "")
@SuppressWarnings("serial")
public class AbEmitBillPay extends IdEntity implements java.io.Serializable {
	/**票据从表ID*/
	private String aid;
	/**单位ID*/
	private String corpId;
	private String corpName;
	/**收票人名称*/
	private String payeeName;
	/**收票人账号*/
	private String payeeAcc;
	/**收票人开户行行号*/
	private String payeeBankNo;
	/**收票人开户行行名*/
	private String payeeBankName;
	/**金额*/
	private BigDecimal payMoney;
	/**用途*/
	private String purpose;
	/**备注*/
	private String remark;
	/**有效标志：0无效；1有效*/
	private String validSign;
	/**付款单号*/
	private String billNo;
	/**所在省*/
	private String prov;
	private String provName;
	/**所在市*/
	private String city;
	private String cityName;
	/**创建人名称*/
	private String createName;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人名称*/
	private String updateName;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	@Column(name = "CORP_NAME")
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	@Column(name = "PROV_NAME")
	public String getProvName() {
		return provName;
	}
	public void setProvName(String provName) {
		this.provName = provName;
	}
	@Column(name = "CITY_NAME")
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	@Column(name ="AID",nullable=true,length=32)
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	@Column(name ="CORP_ID",nullable=true,length=32)
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	@Column(name ="PAYEE_NAME",nullable=true,length=100)
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	@Column(name ="PAYEE_ACC",nullable=true,length=50)
	public String getPayeeAcc() {
		return payeeAcc;
	}
	public void setPayeeAcc(String payeeAcc) {
		this.payeeAcc = payeeAcc;
	}
	@Column(name ="PAYEE_BANK_NO",nullable=true,length=100)
	public String getPayeeBankNo() {
		return payeeBankNo;
	}
	public void setPayeeBankNo(String payeeBankNo) {
		this.payeeBankNo = payeeBankNo;
	}
	@Column(name ="PAYEE_BANK_NAME",nullable=true,length=100)
	public String getPayeeBankName() {
		return payeeBankName;
	}
	public void setPayeeBankName(String payeeBankName) {
		this.payeeBankName = payeeBankName;
	}
	@Column(name ="PAY_MONEY",nullable=true,scale=2,length=15)
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	@Column(name ="PURPOSE",nullable=true,length=200)
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	@Column(name ="REMARK",nullable=true,length=200)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name ="VALID_SIGN",nullable=true,length=1)
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	@Column(name ="BILL_NO",nullable=true,length=50)
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	@Column(name ="PROV",nullable=true,length=32)
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	@Column(name ="CITY",nullable=true,length=32)
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	@Column(name ="CREATE_NAME",nullable=true,length=50)
	public String getCreateName(){
		return this.createName;
	}
	public void setCreateName(String createName){
		this.createName = createName;
	}
	@Column(name ="CREATE_BY",nullable=true,length=50)
	public String getCreateBy(){
		return this.createBy;
	}
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}
	@Column(name ="CREATE_DATE",nullable=true,scale=6)
	public java.util.Date getCreateDate(){
		return this.createDate;
	}
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	@Column(name ="UPDATE_NAME",nullable=true,length=50)
	public String getUpdateName(){
		return this.updateName;
	}
	public void setUpdateName(String updateName){
		this.updateName = updateName;
	}
	@Column(name ="UPDATE_BY",nullable=true,length=50)
	public String getUpdateBy(){
		return this.updateBy;
	}
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}
	@Column(name ="UPDATE_DATE",nullable=true,scale=6)
	public java.util.Date getUpdateDate(){
		return this.updateDate;
	}
	public void setUpdateDate(java.util.Date updateDate){
		this.updateDate = updateDate;
	}
	@Override
	public String toString() {
		return "AbEmitBillPay{" +
				"aid='" + aid + '\'' +
				", corpId='" + corpId + '\'' +
				", payeeName='" + payeeName + '\'' +
				", payeeAcc='" + payeeAcc + '\'' +
				", payeeBankNo='" + payeeBankNo + '\'' +
				", payeeBankName='" + payeeBankName + '\'' +
				", payMoney=" + payMoney +
				", purpose='" + purpose + '\'' +
				", remark='" + remark + '\'' +
				", validSign='" + validSign + '\'' +
				", billNo='" + billNo + '\'' +
				", prov='" + prov + '\'' +
				", city='" + city + '\'' +
				", createName='" + createName + '\'' +
				", createBy='" + createBy + '\'' +
				", createDate=" + createDate +
				", updateName='" + updateName + '\'' +
				", updateBy='" + updateBy + '\'' +
				", updateDate=" + updateDate +
				'}';
	}
}
