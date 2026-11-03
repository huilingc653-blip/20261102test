package com.bytter.bill.domain.bill.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: 票据银行对于自动业务配置
 * @author zhenghj
 * @date 2017-09-28
 * @version 1.0
 *
 */
@Entity
@Table(name = "AB_BILL_BANK_AUTO_DTL", schema = "")
@SuppressWarnings("serial")
public class AbBillBankAutoDtl extends IdEntity implements java.io.Serializable {
	/**银行接口类型ID*/
	private String bifCode;
	private String bifId;
	private String bifName;
	private String bankTypeId;
	private String bankType;
	private String typeName;
	/**提示承兑（0否； 1是 ）*/
	private String autoAccept;
	/**提示收票（0否； 1是 ）*/
	private String autoInvoice;
	/**提示付款（0否； 1是 ）*/
	private String autoPay;
	/**票据池背书（0否； 1是 ）*/
	private String poolEndorse;
	/**票据池开票（0否； 1是 ）*/
	private String poolEmitBill;
	/**是否有效（0否； 1是 ）*/
	private String validSign;
	/**创建人名称*/
	private String createName;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人名称*/
	private String updateName;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**
	 * 是否支持新一代票据，1是，0否
	 */
	private String newBillFlg;
	private String newBifId;
	private String newBifCode;
	@Column(name ="new_bill_flg",nullable=true,length=1)
	public String getNewBillFlg() {
		return newBillFlg;
	}
	public void setNewBillFlg(String newBillFlg) {
		this.newBillFlg = newBillFlg;
	}
	@Column(name ="BIF_CODE",nullable=true,length=32)
	public String getBifCode() {
		return bifCode;
	}
	public void setBifCode(String bifCode) {
		this.bifCode = bifCode;
	}
	@Column(name ="bif_Id")
	public String getBifId() {
		return bifId;
	}
	public void setBifId(String bifId) {
		this.bifId = bifId;
	}
	@Column(name ="bif_name")
	public String getBifName() {
		return bifName;
	}
	public void setBifName(String bifName) {
		this.bifName = bifName;
	}
	@Column(name ="bank_type_id")
	public String getBankTypeId() {
		return bankTypeId;
	}
	public void setBankTypeId(String bankTypeId) {
		this.bankTypeId = bankTypeId;
	}
	@Column(name ="bank_type")
	public String getBankType() {
		return bankType;
	}
	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	@Column(name ="type_name")
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	@Column(name ="AUTO_ACCEPT")
	public String getAutoAccept() {
		return autoAccept;
	}
	public void setAutoAccept(String autoAccept) {
		this.autoAccept = autoAccept;
	}
	@Column(name ="AUTO_INVOICE")
	public String getAutoInvoice() {
		return autoInvoice;
	}
	public void setAutoInvoice(String autoInvoice) {
		this.autoInvoice = autoInvoice;
	}
	@Column(name ="AUTO_PAY")
	public String getAutoPay() {
		return autoPay;
	}
	public void setAutoPay(String autoPay) {
		this.autoPay = autoPay;
	}
	@Column(name ="POOL_ENDORSE")
	public String getPoolEndorse() {
		return poolEndorse;
	}
	public void setPoolEndorse(String poolEndorse) {
		this.poolEndorse = poolEndorse;
	}
	@Column(name ="POOL_EMITBILL")
	public String getPoolEmitBill() {
		return poolEmitBill;
	}
	public void setPoolEmitBill(String poolEmitBill) {
		this.poolEmitBill = poolEmitBill;
	}
	@Column(name ="VALID_SIGN")
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人名称
	 */
	@Column(name ="CREATE_NAME",nullable=true,length=50)
	public String getCreateName(){
		return this.createName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人名称
	 */
	public void setCreateName(String createName){
		this.createName = createName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */
	@Column(name ="CREATE_BY",nullable=true,length=50)
	public String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人登录名称
	 */
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
	@Column(name ="CREATE_DATE",nullable=true)
	public Date getCreateDate(){
		return this.createDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateDate(Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人名称
	 */
	@Column(name ="UPDATE_NAME",nullable=true,length=50)
	public String getUpdateName(){
		return this.updateName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人名称
	 */
	public void setUpdateName(String updateName){
		this.updateName = updateName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人登录名称
	 */
	@Column(name ="UPDATE_BY",nullable=true,length=50)
	public String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人登录名称
	 */
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	@Column(name ="UPDATE_DATE",nullable=true)
	public Date getUpdateDate(){
		return this.updateDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setUpdateDate(Date updateDate){
		this.updateDate = updateDate;
	}
	@Column(name = "NEW_BIF_ID")
	public String getNewBifId() {
		return newBifId;
	}
	public void setNewBifId(String newBifId) {
		this.newBifId = newBifId;
	}
	@Column(name = "NEW_BIF_CODE")
	public String getNewBifCode() {
		return newBifCode;
	}
	public void setNewBifCode(String newBifCode) {
		this.newBifCode = newBifCode;
	}
	@Override
	public String toString() {
		return "AbBillBankAutoDtl{" +
				"bifCode='" + bifCode + '\'' +
				", bifId='" + bifId + '\'' +
				", bifName='" + bifName + '\'' +
				", bankTypeId='" + bankTypeId + '\'' +
				", bankType='" + bankType + '\'' +
				", typeName='" + typeName + '\'' +
				", autoAccept='" + autoAccept + '\'' +
				", autoInvoice='" + autoInvoice + '\'' +
				", autoPay='" + autoPay + '\'' +
				", poolEndorse='" + poolEndorse + '\'' +
				", poolEmitBill='" + poolEmitBill + '\'' +
				", validSign='" + validSign + '\'' +
				", createName='" + createName + '\'' +
				", createBy='" + createBy + '\'' +
				", createDate=" + createDate +'\'' +
				", updateName='" + updateName + '\'' +
				", updateBy='" + updateBy + '\'' +
				", updateDate=" + updateDate +'\'' +
				", newBillFlg='" + newBillFlg + '\'' +
				", newBifId='" + newBifId + '\'' +
				", newBifCode='" + newBifCode + '\'' +
				'}';
	}
}
