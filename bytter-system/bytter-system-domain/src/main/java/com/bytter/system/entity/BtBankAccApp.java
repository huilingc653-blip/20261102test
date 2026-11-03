package com.bytter.system.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 银行账号申请表
 * @author onlineGenerator
 * @date 2017-05-23 17:37:39
 * @version 1.0
 *
 */
@Entity
@Table(name = "BT_BANK_ACC_APP", schema = "")
@SuppressWarnings("serial")
public class BtBankAccApp extends IdEntity implements java.io.Serializable {
	/**序列号*/
	private java.lang.String accNo;
	/**银行账号*/
	private java.lang.String bankAcc;
	/**账户名称*/
	private java.lang.String accName;
	/**单位ID*/
	private java.lang.String corpId;
	/**币别ID*/
	private java.lang.String curId;
	/**业务类型*/
	private java.lang.String busType;
	/**账户类别*/
	private java.lang.String accType;
	/**账户属性*/
	private java.lang.String attributeId;
	/**账户性质*/
	private java.lang.String natureId;
	/**银行类别*/
	private java.lang.String bankTypeId;
	/**开户行行号*/
	private java.lang.String bankCode;
	/**开户行名称*/
	private java.lang.String bankName;
	/**所在省*/
	private java.lang.String prov;
	/**所在市*/
	private java.lang.String city;
	/**电票专户*/
	private java.lang.String electricBill;
	/**定期期限*/
	private java.lang.String regulars;
	/**定期到期时间*/
	private java.util.Date regularDate;
	/**通知类型*/
	private java.lang.String noticeType;
	/**协定金额*/
	private java.math.BigDecimal agrMoney;
	/**协定利率*/
	private java.math.BigDecimal agrInterest;
	/**协定合同*/
	private java.lang.String agrContract;
	/**协定开始时间*/
	private java.util.Date agrBeginDate;
	/**协定结束时间*/
	private java.util.Date agrEndDate;
	/**申请原因*/
	private java.lang.String opRession;
	/**状态*/
	private java.lang.Integer status;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	
	private String flag;
	/**附件ID*/
	private java.lang.String attachmentId;
	/**实体账号**/
	private java.lang.String entityAccId;
	/**
	 * 流通方式 0-线下 1-线上
	 **/
	private java.lang.String circulationMode;
	/**查询数据来源app为账号申请表，acc为银行账号表*/
	private String isFrom;
	/**
	 * 是否境内银行 1-境内，0-境外
	 */
	private String isDomesticBank;

	private String isOffshoreAccount;

	@Column(name ="IS_OFFSHORE_ACCOUNT",nullable=true)
	public String getIsOffshoreAccount() {
		return isOffshoreAccount;
	}

	public void setIsOffshoreAccount(String isOffshoreAccount) {
		this.isOffshoreAccount = isOffshoreAccount;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  序列号
	 */


	@Column(name ="ACC_NO",nullable=true,length=40)
	public java.lang.String getAccNo(){
		return this.accNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  序列号
	 */
	public void setAccNo(java.lang.String accNo){
		this.accNo = accNo;
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
	 *@return: java.lang.String  账户名称
	 */
	@Column(name ="ACC_NAME",nullable=true,length=150)
	public java.lang.String getAccName(){
		return this.accName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账户名称
	 */
	public void setAccName(java.lang.String accName){
		this.accName = accName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单位ID
	 */
	@Column(name ="CORP_ID",nullable=true,length=32)
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别ID
	 */
	@Column(name ="CUR_ID",nullable=true,length=32)
	public java.lang.String getCurId(){
		return this.curId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别ID
	 */
	public void setCurId(java.lang.String curId){
		this.curId = curId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务类型
	 */
	@Column(name ="BUS_TYPE",nullable=true,length=1)
	public java.lang.String getBusType(){
		return this.busType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务类型
	 */
	public void setBusType(java.lang.String busType){
		this.busType = busType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账户类别
	 */
	@Column(name ="ACC_TYPE",nullable=true,length=2)
	public java.lang.String getAccType(){
		return this.accType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账户类别
	 */
	public void setAccType(java.lang.String accType){
		this.accType = accType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账户属性
	 */
	@Column(name ="ATTRIBUTE_ID",nullable=true,length=32)
	public java.lang.String getAttributeId(){
		return this.attributeId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账户属性
	 */
	public void setAttributeId(java.lang.String attributeId){
		this.attributeId = attributeId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账户性质
	 */
	@Column(name ="NATURE_ID",nullable=true,length=32)
	public java.lang.String getNatureId(){
		return this.natureId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账户性质
	 */
	public void setNatureId(java.lang.String natureId){
		this.natureId = natureId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行类别
	 */
	@Column(name ="BANK_TYPE_ID",nullable=true,length=32)
	public java.lang.String getBankTypeId(){
		return this.bankTypeId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行类别
	 */
	public void setBankTypeId(java.lang.String bankTypeId){
		this.bankTypeId = bankTypeId;
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
	 *@return: java.lang.String  开户行名称
	 */
	@Column(name ="BANK_NAME",nullable=true,length=150)
	public java.lang.String getBankName(){
		return this.bankName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  开户行名称
	 */
	public void setBankName(java.lang.String bankName){
		this.bankName = bankName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所在省
	 */
	@Column(name ="PROV",nullable=true,length=20)
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
	@Column(name ="CITY",nullable=true,length=20)
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
	 *@return: java.lang.String  电票专户
	 */
	@Column(name ="ELECTRIC_BILL",nullable=true,length=1)
	public java.lang.String getElectricBill(){
		return this.electricBill;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  电票专户
	 */
	public void setElectricBill(java.lang.String electricBill){
		this.electricBill = electricBill;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  定期期限
	 */
	@Column(name ="REGULARS",nullable=true,length=10)
	public java.lang.String getRegulars(){
		return this.regulars;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  定期期限
	 */
	public void setRegulars(java.lang.String regulars){
		this.regulars = regulars;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  定期到期时间
	 */
	@Column(name ="REGULAR_DATE",nullable=true)
	public java.util.Date getRegularDate(){
		return this.regularDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  定期到期时间
	 */
	public void setRegularDate(java.util.Date regularDate){
		this.regularDate = regularDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  通知类型
	 */
	@Column(name ="NOTICE_TYPE",nullable=true,length=10)
	public java.lang.String getNoticeType(){
		return this.noticeType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  通知类型
	 */
	public void setNoticeType(java.lang.String noticeType){
		this.noticeType = noticeType;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  协定金额
	 */
	@Column(name ="AGR_MONEY",nullable=true,scale=4,length=20)
	public java.math.BigDecimal getAgrMoney(){
		return this.agrMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  协定金额
	 */
	public void setAgrMoney(java.math.BigDecimal agrMoney){
		this.agrMoney = agrMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  协定利率
	 */
	@Column(name ="AGR_INTEREST",nullable=true,scale=4,length=20)
	public java.math.BigDecimal getAgrInterest(){
		return this.agrInterest;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  协定利率
	 */
	public void setAgrInterest(java.math.BigDecimal agrInterest){
		this.agrInterest = agrInterest;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  协定合同
	 */
	@Column(name ="AGR_CONTRACT",nullable=true,length=40)
	public java.lang.String getAgrContract(){
		return this.agrContract;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  协定合同
	 */
	public void setAgrContract(java.lang.String agrContract){
		this.agrContract = agrContract;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  协定开始时间
	 */
	@Column(name ="AGR_BEGIN_DATE",nullable=true)
	public java.util.Date getAgrBeginDate(){
		return this.agrBeginDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  协定开始时间
	 */
	public void setAgrBeginDate(java.util.Date agrBeginDate){
		this.agrBeginDate = agrBeginDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  协定结束时间
	 */
	@Column(name ="AGR_END_DATE",nullable=true)
	public java.util.Date getAgrEndDate(){
		return this.agrEndDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  协定结束时间
	 */
	public void setAgrEndDate(java.util.Date agrEndDate){
		this.agrEndDate = agrEndDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  申请原因
	 */
	@Column(name ="OP_RESSION",nullable=true,length=100)
	public java.lang.String getOpRession(){
		return this.opRession;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  申请原因
	 */
	public void setOpRession(java.lang.String opRession){
		this.opRession = opRession;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  状态
	 */
	@Column(name ="STATUS",nullable=true,length=10)
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
	@Column(name ="CREATE_BY",nullable=true,length=50)
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
	@Column(name ="CREATE_DATE",nullable=true)
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
	@Column(name ="UPDATE_BY",nullable=true,length=50)
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
	@Column(name ="UPDATE_DATE",nullable=true)
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
	/**
	 *方法: 设置java.lang.String
	 *@param: 设置java.lang.String  附件
	 */
	@Column(name ="ATTACHMENT_ID",nullable=true,length=32)
	public String getAttachmentId() {
		return this.attachmentId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@param: 取得java.lang.String  附件
	 */
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	@Transient
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	@Column(name ="ENTITY_ACC_ID",nullable=true,length=32)
	public String getEntityAccId() {
		return entityAccId;
	}
	public void setEntityAccId(String entityAccId) {
		this.entityAccId = entityAccId;
	}
	@Column(name = "CIRCULATION_MODE")
	public String getCirculationMode() {
		return circulationMode;
	}
	public void setCirculationMode(String circulationMode) {
		this.circulationMode = circulationMode;
	}
	@Transient
	public String getIsFrom() {
		return isFrom;
	}
	public void setIsFrom(String isFrom) {
		this.isFrom = isFrom;
	}
	@Column(name = "IS_DOMESTIC_BANK", nullable = true, length = 1)
	public String getIsDomesticBank() {
		return isDomesticBank;
	}
	public void setIsDomesticBank(String isDomesticBank) {
		this.isDomesticBank = isDomesticBank;
	}
}
