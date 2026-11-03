package com.bytter.bill.domain.billbank.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
@Entity
@Table(name = "BT_BANK_ACC", schema = "")
public class BillBtBankAcc extends IdEntity implements java.io.Serializable {
	private static final long serialVersionUID = 1291859506997225441L;
	/**
	 * 银行账号
	 */
	private String bankAcc;
	/**
	 * 账户名称
	 */
	private String accName;
	/**
	 * 单位ID
	 */
	private String corpId;
	/**
	 * 账户类别
	 */
	private String accType;
	/**
	 * 账户属性
	 */
	private String attributeId;
	/**
	 * 账户性质
	 */
	private String natureId;
	/**
	 * 银行类别
	 */
	private String bankTypeId;
	/**
	 * 开户行行号
	 */
	private String bankCode;
	/**
	 * 开户行名称
	 */
	private String bankName;
	/**
	 * 所在省
	 */
	private String prov;
	/**
	 * 所在市
	 */
	private String city;
	/**
	 * 接口代码
	 */
	private String bifCode;
	/**
	 * 电票专户
	 */
	private String electricBill;
	/**
	 * 是否上线
	 */
	private String isOnline;
	/**
	 * 备注
	 */
	private String rmk;
	/**
	 * 有效标志
	 */
	private String validSign;
	/**
	 * 建行机构号
	 */
	private String orgCode;
	/**
	 * 流通方式 0-线下 1-线上
	 **/
	private String circulationMode;
	/**
	 * 到期日期
	 */
	private Date endDate;
	/**
	 * 银行组织机构代码
	 */
	private String bankOrgCode;
	@Column(name = "BANK_ORG_CODE", nullable = true, length = 20)
	public String getBankOrgCode() {
		return bankOrgCode;
	}
	public void setBankOrgCode(String bankOrgCode) {
		this.bankOrgCode = bankOrgCode;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  银行账号
	 */
	@Column(name = "BANK_ACC", nullable = true, length = 30)
	public String getBankAcc() {
		return this.bankAcc;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  银行账号
	 */
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  账户名称
	 */
	@Column(name = "ACC_NAME", nullable = true, length = 140)
	public String getAccName() {
		return this.accName;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  账户名称
	 */
	public void setAccName(String accName) {
		this.accName = accName;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  单位ID
	 */
	@Column(name = "CORP_ID", nullable = true, length = 32)
	public String getCorpId() {
		return this.corpId;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  单位ID
	 */
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  账户类别
	 */
	@Column(name = "ACC_TYPE", nullable = true, length = 2)
	public String getAccType() {
		return this.accType;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  账户类别
	 */
	public void setAccType(String accType) {
		this.accType = accType;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  账户属性
	 */
	@Column(name = "ATTRIBUTE_ID", nullable = true, length = 32)
	public String getAttributeId() {
		return this.attributeId;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  账户属性
	 */
	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  账户性质
	 */
	@Column(name = "NATURE_ID", nullable = true, length = 32)
	public String getNatureId() {
		return this.natureId;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  账户性质
	 */
	public void setNatureId(String natureId) {
		this.natureId = natureId;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  银行类别
	 */
	@Column(name = "BANK_TYPE_ID", nullable = true, length = 32)
	public String getBankTypeId() {
		return this.bankTypeId;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  银行类别
	 */
	public void setBankTypeId(String bankTypeId) {
		this.bankTypeId = bankTypeId;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  开户行行号
	 */
	@Column(name = "BANK_CODE", nullable = true, length = 20)
	public String getBankCode() {
		return this.bankCode;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  开户行行号
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  开户行名称
	 */
	@Column(name = "BANK_NAME", nullable = true, length = 150)
	public String getBankName() {
		return this.bankName;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  开户行名称
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  所在省
	 */
	@Column(name = "PROV", nullable = true, length = 32)
	public String getProv() {
		return this.prov;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  所在省
	 */
	public void setProv(String prov) {
		this.prov = prov;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  所在市
	 */
	@Column(name = "CITY", nullable = true, length = 32)
	public String getCity() {
		return this.city;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  所在市
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  接口代码
	 */
	@Column(name = "BIF_CODE", nullable = true, length = 4)
	public String getBifCode() {
		return this.bifCode;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  接口代码
	 */
	public void setBifCode(String bifCode) {
		this.bifCode = bifCode;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  电票专户
	 */
	@Column(name = "ELECTRIC_BILL", nullable = true, length = 1)
	public String getElectricBill() {
		return this.electricBill;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  电票专户
	 */
	public void setElectricBill(String electricBill) {
		this.electricBill = electricBill;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  是否上线
	 */
	@Column(name = "IS_ONLINE", nullable = true, length = 1)
	public String getIsOnline() {
		return this.isOnline;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  是否上线
	 */
	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  备注
	 */
	@Column(name = "RMK", nullable = true, length = 100)
	public String getRmk() {
		return this.rmk;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  备注
	 */
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  有效标志
	 */
	@Column(name = "VALID_SIGN", nullable = true, length = 1)
	public String getValidSign() {
		return this.validSign;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  有效标志
	 */
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  建行机构号
	 */
	@Column(name = "ORG_CODE", nullable = true, length = 50)
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	
	@Column(name = "CIRCULATION_MODE")
	public String getCirculationMode() {
		return circulationMode;
	}
	public void setCirculationMode(String circulationMode) {
		this.circulationMode = circulationMode;
	}
	@Column(name = "END_DATE")
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
