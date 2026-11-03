package com.bytter.bill.domain.bill.vo;

import java.math.BigDecimal;
import java.util.Date;

/**   
 * @Title: 票据类型余额VO
 * @author zhengg
 * @date 2017-09-28 19:20:01
 * @version 1.0
 */
public class AbBillAccBalTypeVO implements java.io.Serializable  {

	private static final long serialVersionUID = 268419642335938296L;

	public AbBillAccBalTypeVO(){
		initBalMoney();
	}

	public AbBillAccBalTypeVO(String billType){
		this.billType=billType;
		initBalMoney();
	}

	public AbBillAccBalTypeVO(String corpId, String bankId, String bankAcc, Date balDate, String curId){
		this.corpId=corpId;
		this.balDate=balDate;
		this.curId=curId;
		this.bankId=bankId;
		this.bankAcc=bankAcc;
		initBalMoney();
	}

	private void initBalMoney(){
		this.bal=new BigDecimal(0);
		this.availBal=new BigDecimal(0);
		this.tiedupBal=new BigDecimal(0);
	}

	private String id;
	
	private String billType;

	/**票据流水ID*/
	private String recordId;
	/**流水标志0借1贷2冻结*/
	private String cdSign;
	/**有效标记0无效1有效*/
	private String validSign;

	/**票据流水类型*/
	private String briefId;

	/**银行账户ID*/
	private String bankId;
	/**银行账号*/
	private String bankAcc;
	/**单位ID*/
	private String corpId;
	/**单位名称*/
	private String corpName;
	/**单位代码*/
	private String corpCode;
	/**币别ID*/
	private String curId;
	/**
	 * 币别
	 */
	private String curName;
	/**余额日期*/
	private Date balDate;
	/**余额*/
	private BigDecimal bal;
	/**可用余额*/
	private BigDecimal availBal;
	/**占用金额*/
	private BigDecimal tiedupBal;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public String getBankId(){
		return this.bankId;
	}

	/**票据流水ID*/
	public String getRecordId() {
		return recordId;
	}
	/**票据流水ID*/
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: String  银行账户ID
	 */
	public void setBankId(String bankId){
		this.bankId = bankId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: String  银行账号
	 */
	public String getBankAcc(){
		return this.bankAcc;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: String  银行账号
	 */
	public void setBankAcc(String bankAcc){
		this.bankAcc = bankAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: String  单位ID
	 */
	public String getCorpId(){
		return this.corpId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: String  单位ID
	 */
	public void setCorpId(String corpId){
		this.corpId = corpId;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: String  币别ID
	 */
	public String getCurId(){
		return this.curId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: String  币别ID
	 */
	public void setCurId(String curId){
		this.curId = curId;
	}

	public String getCurName() {
		return curName;
	}

	public void setCurName(String curName) {
		this.curName = curName;
	}

	/**
	 *方法: 取得Date
	 *@return: Date  余额日期
	 */
	public Date getBalDate(){
		return this.balDate;
	}

	/**
	 *方法: 设置Date
	 *@param: Date  余额日期
	 */
	public void setBalDate(Date balDate){
		this.balDate = balDate;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  余额
	 */
	public BigDecimal getBal(){
		return this.bal;
	}

	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  余额
	 */
	public void setBal(BigDecimal bal){
		this.bal = bal;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  可用余额
	 */
	public BigDecimal getAvailBal(){
		return this.availBal;
	}

	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  可用余额
	 */
	public void setAvailBal(BigDecimal availBal){
		this.availBal = availBal;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  占用金额
	 */
	public BigDecimal getTiedupBal(){
		return this.tiedupBal;
	}

	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  占用金额
	 */
	public void setTiedupBal(BigDecimal tiedupBal){
		this.tiedupBal = tiedupBal;
	}

	public String getCdSign() {
		return cdSign;
	}

	public void setCdSign(String cdSign) {
		this.cdSign = cdSign;
	}

	public String getValidSign() {
		return validSign;
	}

	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}

	public String getBriefId() {
		return briefId;
	}

	public void setBriefId(String briefId) {
		this.briefId = briefId;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	@Override
	public String toString() {
		return "AbBillAccBalVO{" +
				"id='" + id + '\'' +
				", billType=" + billType +
				", recordId='" + recordId + '\'' +
				", cdSign='" + cdSign + '\'' +
				", validSign='" + validSign + '\'' +
				", briefId='" + briefId + '\'' +
				", bankId='" + bankId + '\'' +
				", bankAcc='" + bankAcc + '\'' +
				", corpId='" + corpId + '\'' +
				", corpName='" + corpName + '\'' +
				", corpCode='" + corpCode + '\'' +
				", curId='" + curId + '\'' +
				", curName='" + curName + '\'' +
				", balDate=" + balDate +
				", bal=" + bal +
				", availBal=" + availBal +
				", tiedupBal=" + tiedupBal +
				'}';
	}

}
