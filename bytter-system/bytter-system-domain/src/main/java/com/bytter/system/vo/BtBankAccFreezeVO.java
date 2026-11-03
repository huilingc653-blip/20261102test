package com.bytter.system.vo;
import java.math.BigDecimal;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
/**   
 * @Title: Entity
 * @Description: BT_BANK_ACC_FREEZE
 * @author onlineGenerator
 * @date 2017-06-14 14:26:46
 * @version 1.0
 *
 */
public class BtBankAccFreezeVO  implements java.io.Serializable,IBusinessKey {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1250096789883774305L;
	
	private String id;
	/**bankAccId*/
	private java.lang.String bankAccId;
	/**银行账号*/
	@BtCompositeSearch(fieldName = "freeze.bank_Acc")
	private java.lang.String bankAcc;
	/**单位ID*/
	@Corp(inject="corpName")
	@BtCompositeSearch(fieldName = "freeze.corp_Id")
	private java.lang.String corpId;
	@BtCompositeSearch(fieldName = "freeze.corp_Id")
	private String corpName;
	/**余额*/
	private java.math.BigDecimal bal;
	/**冻结类型*/
	@Dict(dictGroup = "bankAccApp.ufFlag", inject = "ufFlagName")
	@BtCompositeSearch(fieldName = "freeze.uf_Flag")
	private java.lang.String ufFlag;
	private java.lang.String ufFlagName;
	/**冻结，解冻金额*/
	@BtCompositeSearch(fieldName = "freeze.freeze_Money")
	private java.math.BigDecimal freezeMoney;
	/**可冻结金额*/
	private java.math.BigDecimal avalBal;
	
	/**可解冻金额*/
	private java.math.BigDecimal ufavalBal;
	private String ufavalBalStr;
	/**已解冻金额*/
	private java.math.BigDecimal ufFreezeMoney;
	private String ufFreezeMoneyStr;
	/**冻结，解冻日期*/
	@BtCompositeSearch(fieldName = "freeze.freeze_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date freezeDate;
	private String freezeDateStr;
	/**原因*/
	private java.lang.String freezeRession;
	/**被冻结信息*/
	private java.lang.String fUuid;
	/**状态*/
	@Dict(dictGroup = "bisExc.status", inject = "statusName")
	@BtCompositeSearch(fieldName = "freeze.status")
	private java.lang.Integer status;
	private String statusName;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date updateDate;
	/**冻结次数*/
	 private Integer numberFreeze;
	 /**解冻次数*/
	 private Integer numberUnFreeze;
	 /**冻结金额合计   */
	 private BigDecimal freezeMoneySum;
	 /**解冻金额合计   */
	 private BigDecimal unFreezeMoneySum;
	 /**冻结次数   */
	 private Integer freezeCount;
	 /**解冻次数   */
	 private Integer unfreezeCount;
	
	 /**币别*/
	 @Currency(inject="curName")
	 private java.lang.String curId;
	 private String curName;
	/**编号*/
	@BtCompositeSearch(fieldName = "freeze.freeze_No")
	 private java.lang.String freezeNo;
	/**冻结编号*/
	 private String unfreezeNo;
	/**户名*/
	 private String accName;
	/**开户行*/
	 private String bankName;
	/**在途解冻金额*/
	private java.math.BigDecimal unwaitMoney;
	 
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  bankAccId
	 */
	public java.lang.String getBankAccId(){
		return this.bankAccId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  bankAccId
	 */
	public void setBankAccId(java.lang.String bankAccId){
		this.bankAccId = bankAccId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行账号
	 */
	public java.lang.String getBankAcc(){
		return this.bankAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行账号
	 */
	public void setBankAcc(java.lang.String bankAcc){
		this.bankAcc = bankAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单位ID
	 */
	public java.lang.String getCorpId(){
		return this.corpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单位ID
	 */
	public void setCorpId(java.lang.String corpId){
		this.corpId = corpId;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  余额
	 */
	public java.math.BigDecimal getBal(){
		return this.bal;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  余额
	 */
	public void setBal(java.math.BigDecimal bal){
		this.bal = bal;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  冻结类型
	 */
	public java.lang.String getUfFlag(){
		return this.ufFlag;
	}
	public String getUfFlagName() {
		return ufFlagName;
	}
	public void setUfFlagName(String ufFlagName) {
		this.ufFlagName = ufFlagName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  冻结类型
	 */
	public void setUfFlag(java.lang.String ufFlag){
		this.ufFlag = ufFlag;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  冻结，解冻金额
	 */
	public java.math.BigDecimal getFreezeMoney(){
		return this.freezeMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  冻结，解冻金额
	 */
	public void setFreezeMoney(java.math.BigDecimal freezeMoney){
		this.freezeMoney = freezeMoney;
	}
	
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  可冻结金额
	 */
	public java.math.BigDecimal getAvalBal(){
		return this.avalBal;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  可冻结金额
	 */
	public void setAvalBal(java.math.BigDecimal avalBal){
		this.avalBal = avalBal;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  冻结，解冻日期
	 */
	public java.util.Date getFreezeDate(){
		return this.freezeDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  冻结，解冻日期
	 */
	public void setFreezeDate(java.util.Date freezeDate){
		this.freezeDate = freezeDate;
		if(freezeDate != null) {
			this.freezeDateStr = DateUtils.formatDate(freezeDate);
		}
	}
	
	public String getFreezeDateStr() {
		return freezeDateStr;
	}
	public void setFreezeDateStr(String freezeDateStr) {
		this.freezeDateStr = freezeDateStr;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  原因
	 */
	public java.lang.String getFreezeRession(){
		return this.freezeRession;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  原因
	 */
	public void setFreezeRession(java.lang.String freezeRession){
		this.freezeRession = freezeRession;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  被冻结信息
	 */
	public java.lang.String getFUuid(){
		return this.fUuid;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  被冻结信息
	 */
	public void setFUuid(java.lang.String fUuid){
		this.fUuid = fUuid;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  状态
	 */
	public java.lang.Integer getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  状态
	 */
	public void setStatus(java.lang.Integer status){
		this.status = status;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */
	public java.lang.String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人登录名称
	 */
	public void setCreateBy(java.lang.String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
	public java.util.Date getCreateDate(){
		return this.createDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人登录名称
	 */
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人登录名称
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	public java.util.Date getUpdateDate(){
		return this.updateDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setUpdateDate(java.util.Date updateDate){
		this.updateDate = updateDate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public java.lang.String getfUuid() {
		return fUuid;
	}
	public void setfUuid(java.lang.String fUuid) {
		this.fUuid = fUuid;
	}
	public Integer getNumberFreeze() {
		return numberFreeze;
	}
	public void setNumberFreeze(Integer numberFreeze) {
		this.numberFreeze = numberFreeze;
	}
	public Integer getNumberUnFreeze() {
		return numberUnFreeze;
	}
	public void setNumberUnFreeze(Integer numberUnFreeze) {
		this.numberUnFreeze = numberUnFreeze;
	}
	public java.lang.String getCurId() {
		return curId;
	}
	public void setCurId(java.lang.String curId) {
		this.curId = curId;
	}
	public java.lang.String getFreezeNo() {
		return freezeNo;
	}
	public void setFreezeNo(java.lang.String freezeNo) {
		this.freezeNo = freezeNo;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public BigDecimal getFreezeMoneySum() {
		return freezeMoneySum;
	}
	public void setFreezeMoneySum(BigDecimal freezeMoneySum) {
		this.freezeMoneySum = freezeMoneySum;
	}
	public BigDecimal getUnFreezeMoneySum() {
		return unFreezeMoneySum;
	}
	public void setUnFreezeMoneySum(BigDecimal unFreezeMoneySum) {
		this.unFreezeMoneySum = unFreezeMoneySum;
	}
	public Integer getFreezeCount() {
		return freezeCount;
	}
	public void setFreezeCount(Integer freezeCount) {
		this.freezeCount = freezeCount;
	}
	public Integer getUnfreezeCount() {
		return unfreezeCount;
	}
	public void setUnfreezeCount(Integer unfreezeCount) {
		this.unfreezeCount = unfreezeCount;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public java.math.BigDecimal getUfavalBal() {
		return ufavalBal;
	}
	public void setUfavalBal(java.math.BigDecimal ufavalBal) {
		this.ufavalBal = ufavalBal;
	}
	public String getUfavalBalStr() {
		return ufavalBalStr;
	}
	public void setUfavalBalStr(String ufavalBalStr) {
		this.ufavalBalStr = ufavalBalStr;
	}
	public java.math.BigDecimal getUfFreezeMoney() {
		return ufFreezeMoney;
	}
	public void setUfFreezeMoney(java.math.BigDecimal ufFreezeMoney) {
		this.ufFreezeMoney = ufFreezeMoney;
	}
	public String getUfFreezeMoneyStr() {
		return ufFreezeMoneyStr;
	}
	public void setUfFreezeMoneyStr(String ufFreezeMoneyStr) {
		this.ufFreezeMoneyStr = ufFreezeMoneyStr;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	@Override
	public String getBusinessKey() {
		return getId();
	}
	public String getUnfreezeNo() {
		return unfreezeNo;
	}
	public void setUnfreezeNo(String unfreezeNo) {
		this.unfreezeNo = unfreezeNo;
	}
	public BigDecimal getUnwaitMoney() {
		return unwaitMoney;
	}
	public void setUnwaitMoney(BigDecimal unwaitMoney) {
		this.unwaitMoney = unwaitMoney;
	}
}
