package com.bytter.biz.wage.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: NIS_WAGE_DETAIL 代发工资明细表
 * @author onlineGenerator
 * @date 2018-01-08 17:19:59
 * @version 1.0
 *
 */
@Entity
@Table(name = "NIS_WAGE_DETAIL", schema = "")
@SuppressWarnings("serial")
public class NisWageDetail extends IdEntity implements java.io.Serializable {
	/**单据号码*/
	private java.lang.String wageDtlNo;
	/**代发工资ID*/
	private java.lang.String wageId;
	/**工号*/
	private java.lang.String workNo;
	/**姓名*/
	private java.lang.String userName;
	/**银行账号*/
	private java.lang.String bankAcc;
	/**户名*/
	private java.lang.String accName;
	/**实发工资*/
	private java.lang.String wagesVerify;
	/**附言*/
	private java.lang.String purpose;
	/**所在省*/
	private java.lang.String prov;
	/**所在市*/
	private java.lang.String city;
	/**开户行行号*/
	private java.lang.String bankCode;
	/**开户银行名称*/
	private java.lang.String bankName;
	/**跨行标识 0-同行 1-他行*/
	private java.lang.String difBank;
	/**地域标识 0-同城 1-异地*/
	private java.lang.String areaSign;
	/**交易状态*/
	private java.lang.String voucherStat;
	/**返回时间*/
	private java.util.Date returnTime;
	/**返回信息（交易前）*/
	private java.lang.String firstReturnMsg;
	/**返回信息（交易后）*/
	private java.lang.String nextReturnMsg;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**指令单号*/
	private String voucherNo;
	/**证件类型*/
	private String idType;
	/**证件号（部分银行需要身份证信息，已知北京银行、沧州银行）*/
	private String idNo;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单据号码
	 */
	@Column(name ="WAGE_DTL_NO",nullable=false,length=12)
	public java.lang.String getWageDtlNo(){
		return this.wageDtlNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单据号码
	 */
	public void setWageDtlNo(java.lang.String wageDtlNo){
		this.wageDtlNo = wageDtlNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  代发工资ID
	 */
	@Column(name ="WAGE_ID",nullable=false,length=32)
	public java.lang.String getWageId(){
		return this.wageId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  代发工资ID
	 */
	public void setWageId(java.lang.String wageId){
		this.wageId = wageId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  工号
	 */
	@Column(name ="WORK_NO",nullable=true,length=30)
	public java.lang.String getWorkNo(){
		return this.workNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  工号
	 */
	public void setWorkNo(java.lang.String workNo){
		this.workNo = workNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  姓名
	 */
	@Column(name ="USER_NAME",nullable=true,length=50)
	public java.lang.String getUserName(){
		return this.userName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  姓名
	 */
	public void setUserName(java.lang.String userName){
		this.userName = userName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行账号
	 */
	@Column(name ="BANK_ACC",nullable=true,length=30)
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
	 *@return: java.lang.String  户名
	 */
	@Column(name ="ACC_NAME",nullable=true,length=150)
	public java.lang.String getAccName(){
		return this.accName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  户名
	 */
	public void setAccName(java.lang.String accName){
		this.accName = accName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  实发工资
	 */
	@Column(name ="WAGES_VERIFY",nullable=true,length=50)
	public java.lang.String getWagesVerify(){
		return this.wagesVerify;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  实发工资
	 */
	public void setWagesVerify(java.lang.String wagesVerify){
		this.wagesVerify = wagesVerify;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  附言
	 */
	@Column(name ="PURPOSE",nullable=true,length=120)
	public java.lang.String getPurpose(){
		return this.purpose;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  附言
	 */
	public void setPurpose(java.lang.String purpose){
		this.purpose = purpose;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所在省
	 */
	@Column(name ="PROV",nullable=true,length=32)
	public java.lang.String getProv(){
		return this.prov;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所在省
	 */
	public void setProv(java.lang.String prov){
		this.prov = prov;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所在市
	 */
	@Column(name ="CITY",nullable=true,length=32)
	public java.lang.String getCity(){
		return this.city;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所在市
	 */
	public void setCity(java.lang.String city){
		this.city = city;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  开户行行号
	 */
	@Column(name ="BANK_CODE",nullable=true,length=20)
	public java.lang.String getBankCode(){
		return this.bankCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  开户行行号
	 */
	public void setBankCode(java.lang.String bankCode){
		this.bankCode = bankCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  开户银行名称
	 */
	@Column(name ="BANK_NAME",nullable=true,length=150)
	public java.lang.String getBankName(){
		return this.bankName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  开户银行名称
	 */
	public void setBankName(java.lang.String bankName){
		this.bankName = bankName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  跨行标识 0-同行 1-他行
	 */
	@Column(name ="DIF_BANK",nullable=true,length=1)
	public java.lang.String getDifBank(){
		return this.difBank;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  跨行标识 0-同行 1-他行
	 */
	public void setDifBank(java.lang.String difBank){
		this.difBank = difBank;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  地域标识 0-同城 1-异地
	 */
	@Column(name ="AREA_SIGN",nullable=true,length=1)
	public java.lang.String getAreaSign(){
		return this.areaSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  地域标识 0-同城 1-异地
	 */
	public void setAreaSign(java.lang.String areaSign){
		this.areaSign = areaSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  交易状态
	 */
	@Column(name ="VOUCHER_STAT",nullable=true,length=1)
	public java.lang.String getVoucherStat(){
		return this.voucherStat;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  交易状态
	 */
	public void setVoucherStat(java.lang.String voucherStat){
		this.voucherStat = voucherStat;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  返回时间
	 */
	@Column(name ="RETURN_TIME",nullable=true)
	public java.util.Date getReturnTime(){
		return this.returnTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  返回时间
	 */
	public void setReturnTime(java.util.Date returnTime){
		this.returnTime = returnTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  返回信息（交易前）
	 */
	@Column(name ="FIRST_RETURN_MSG",nullable=true,length=2000)
	public java.lang.String getFirstReturnMsg(){
		return this.firstReturnMsg;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  返回信息（交易前）
	 */
	public void setFirstReturnMsg(java.lang.String firstReturnMsg){
		this.firstReturnMsg = firstReturnMsg;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  返回信息（交易后）
	 */
	@Column(name ="NEXT_RETURN_MSG",nullable=true,length=2000)
	public java.lang.String getNextReturnMsg(){
		return this.nextReturnMsg;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  返回信息（交易后）
	 */
	public void setNextReturnMsg(java.lang.String nextReturnMsg){
		this.nextReturnMsg = nextReturnMsg;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */
	@Column(name ="CREATE_BY",nullable=true,length=32)
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
	@Column(name ="CREATE_DATE",nullable=true,scale=6)
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
	@Column(name ="UPDATE_BY",nullable=true,length=32)
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
	@Column(name ="UPDATE_DATE",nullable=true,scale=6)
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
	@Column(name ="VOUCHER_NO")
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	@Column(name ="ID_TYPE")
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	@Column(name ="ID_NO")
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
}
