package com.bytter.biz.netbank.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: 付款通知表
 * @author onlineGenerator
 * @date 2017-05-23 15:20:33
 * @version 1.0
 *
 */
@Entity
@Table(name = "NIS_BILLHEAD", schema = "")
@SuppressWarnings("serial")
public class NisBillhead extends IdEntity implements java.io.Serializable {
	/**付款单号*/
	private java.lang.String billNo;
	/**单位*/
	private java.lang.String corpId;
	/**付款方式*/
	private java.lang.String voucherTypeId;
	/**账号*/
	private java.lang.String accId;
	/**付款账号*/
	private java.lang.String payerAcc;
	/**付款账号名称*/
	private java.lang.String payerAccName;
	/**付款开户行名称*/
	private java.lang.String payerBank;
	/**下拨账号*/
	private java.lang.String downAccId;
	/**币别*/
	private java.lang.String curId;
	/**金额*/
	private java.math.BigDecimal payMoney;
	/**票据金额*/
	private java.math.BigDecimal billMoney;
	/**收款账号*/
	private java.lang.String payeeAcc;
	/**收款账号名称*/
	private java.lang.String payeeAccName;
	/**收款方开户行行号*/
	private java.lang.String payeeBankCode;
	/**收款开户行名称*/
	private java.lang.String payeeBank;
	/**收款行别*/
	private java.lang.String bankTypeId;
	/**所在省*/
	private java.lang.String prov;
	/**所在市*/
	private java.lang.String city;
	/**期望支付日期*/
	private java.util.Date wishPayday;
	/**加急标志*/
	private java.lang.String isQuick;
	/**是否对私付款*/
	private java.lang.String isForindividual;
	/**接口代码*/
	private java.lang.String bifCode;
	/**跨行标识*/
	private java.lang.String difBank;
	/**地域标识*/
	private java.lang.String areaSign;
	/**会计凭证号*/
	private java.lang.String bursarBillNo;
	/**用途*/
	private java.lang.String purpose;
	/**附言*/
	private java.lang.String addWord;
	/**是否直接付款*/
	private java.lang.String directPayFlag;
	/**证书信息*/
	private java.lang.String caInfoId;
	/**记账日期*/
	private java.util.Date tallyDate;
	/**凭证流水号*/
	private java.lang.String orderNo;
	/**记账凭证号码*/
	private java.lang.String voucherNo;
	/**ERP编号*/
	private java.lang.String erpId;
	/**业务类别*/
	private java.lang.String busType;
	/**单据来源*/
	private java.lang.String billFrom;
	/**拆分前单据号*/
	private java.lang.String splitId;
	/**原始拆分前单据号*/
	private java.lang.String origSplitId;
	/**合并后单据号*/
	private java.lang.String mergeId;
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
	/**附件ID*/
	private java.lang.String attachmentId;
	/**是否已付款*/
	private java.lang.String isPayed;
	/**内部资金计划标识*/
	private String innerSign;
	/**记账标志**/
	private java.lang.String tallySign;
	/**记账员代码**/
	private java.lang.String tallyCode;
	/**是否垫支**/
	private java.lang.String isAdvance;
	/**营运资金比例**/
	private java.math.BigDecimal workCapitalRatio;
	/**预计收回日期**/
	private java.util.Date takeBackDate;
	/**是否已开票**/
	private java.lang.String isOpenBill;
	/**供应链金融-是否已有开立的应付债权凭证（付款方式51）**/
	private java.lang.String scfIsOpened;
	/**供应链金融-应付债权凭证开立金额（51：未开立时录入，已开立时取所选凭证合计）**/
	private java.math.BigDecimal scfPayableOpenAmt;
	/**供应链金融-应收转让凭证金额（52：应收债权凭证转让记录金额，预留）**/
	private java.math.BigDecimal scfTransferAmt;
	/**银行流水号**/
	private java.lang.String bankSerialNo;
	/**是否自动支付*/
	private java.lang.String isAutoPay;
	/**策略ID*/
	private java.lang.String policyId;
	/**票据类型*/
	private Integer billType;
	/**票据流通介质*/
	private Integer billFlag;
	/**时间戳*/
	private String timeStamp;
	/**备注说明*/
	private java.lang.String rmk;
	/**业务单位（票据付款专用）*/
	private String busCorpId;
	/**业务人员（票据付款专用）*/
	private String busPersonnel;
	/**票据池id（票据池开票专用）*/
	private String bpId;
	/**排期要素id SCHEDULING_ELEMENTS_ID*/
	private String schedulingElementsId;
	/**排期日期 SCHEDULING_DATE*/
	private Date schedulingDate;
	/**待付款池id*/
	private String nisBillheadPoolId;
	/**审批时间*/
	private java.util.Date approveDate;
	/**交易状态*/
	private String voucherStat;
	/**银行落地限额处理结果：A同意/R拒绝*/
	private String landingLimitResult;
	/**收款人统一社会信用代码*/
	private String blRcvpymtpsUnnIdNo;
	/**共享计划付款日期*/
	private Date sharingDate;
	/**结算中心实际付款账号*/
	private String realPayerAccId;
	/**共享业务类型*/
	private String shareBusTypeId;
	/**退款勾兑原单ID：只有退款勾兑新生成得单据才会有这个值*/
	private String refundOrgId;

	/**返回信息*/
	private String returnMsg;

	/**交易时间*/
	private Date transTime;

	@Column(name ="trans_time")
	public Date getTransTime() {
		return transTime;
	}

	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}

	@Column(name ="return_msg")
	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	@Column(name ="REFUND_ORG_ID")
	public String getRefundOrgId() {
		return refundOrgId;
	}

	public void setRefundOrgId(String refundOrgId) {
		this.refundOrgId = refundOrgId;
	}

	@Column(name ="BL_RCVPYMTPS_UNN_ID_NO",nullable=false,length=32)
	public String getBlRcvpymtpsUnnIdNo() {
		return blRcvpymtpsUnnIdNo;
	}

	public void setBlRcvpymtpsUnnIdNo(String blRcvpymtpsUnnIdNo) {
		this.blRcvpymtpsUnnIdNo = blRcvpymtpsUnnIdNo;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款单号
	 */
	@Column(name ="BILL_NO",nullable=false,length=12)
	public java.lang.String getBillNo(){
		return this.billNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款单号
	 */
	public void setBillNo(java.lang.String billNo){
		this.billNo = billNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单位
	 */
	@Column(name ="CORP_ID",nullable=false,length=32)
	public java.lang.String getCorpId(){
		return this.corpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单位
	 */
	public void setCorpId(java.lang.String corpId){
		this.corpId = corpId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款方式
	 */
	@Column(name ="VOUCHER_TYPE_ID",nullable=true,length=32)
	public java.lang.String getVoucherTypeId(){
		return this.voucherTypeId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款方式
	 */
	public void setVoucherTypeId(java.lang.String voucherTypeId){
		this.voucherTypeId = voucherTypeId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账号
	 */
	@Column(name ="ACC_ID",nullable=true,length=32)
	public java.lang.String getAccId(){
		return this.accId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账号
	 */
	public void setAccId(java.lang.String accId){
		this.accId = accId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款账号
	 */
	@Column(name ="PAYER_ACC",nullable=true,length=50)
	public java.lang.String getPayerAcc(){
		return this.payerAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款账号
	 */
	public void setPayerAcc(java.lang.String payerAcc){
		this.payerAcc = payerAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款账号名称
	 */
	@Column(name ="PAYER_ACC_NAME",nullable=true,length=150)
	public java.lang.String getPayerAccName(){
		return this.payerAccName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款账号名称
	 */
	public void setPayerAccName(java.lang.String payerAccName){
		this.payerAccName = payerAccName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款开户行名称
	 */
	@Column(name ="PAYER_BANK",nullable=true,length=150)
	public java.lang.String getPayerBank(){
		return this.payerBank;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款开户行名称
	 */
	public void setPayerBank(java.lang.String payerBank){
		this.payerBank = payerBank;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  下拨账号
	 */
	@Column(name ="DOWN_ACC_ID",nullable=true,length=32)
	public java.lang.String getDownAccId(){
		return this.downAccId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  下拨账号
	 */
	public void setDownAccId(java.lang.String downAccId){
		this.downAccId = downAccId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别
	 */
	@Column(name ="CUR_ID",nullable=true,length=32)
	public java.lang.String getCurId(){
		return this.curId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别
	 */
	public void setCurId(java.lang.String curId){
		this.curId = curId;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  金额
	 */
	@Column(name ="PAY_MONEY",nullable=false,scale=2,length=15)
	public java.math.BigDecimal getPayMoney(){
		return this.payMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  金额
	 */
	public void setPayMoney(java.math.BigDecimal payMoney){
		this.payMoney = payMoney;
	}
	@Column(name ="BILL_MONEY",nullable=false,scale=2,length=15)
	public BigDecimal getBillMoney() {
		return billMoney;
	}
	public void setBillMoney(BigDecimal billMoney) {
		this.billMoney = billMoney;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款账号
	 */
	@Column(name ="PAYEE_ACC",nullable=true,length=50)
	public java.lang.String getPayeeAcc(){
		return this.payeeAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款账号
	 */
	public void setPayeeAcc(java.lang.String payeeAcc){
		this.payeeAcc = payeeAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款账号名称
	 */
	@Column(name ="PAYEE_ACC_NAME",nullable=true,length=150)
	public java.lang.String getPayeeAccName(){
		return this.payeeAccName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款账号名称
	 */
	public void setPayeeAccName(java.lang.String payeeAccName){
		this.payeeAccName = payeeAccName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款方开户行行号
	 */
	@Column(name ="PAYEE_BANK_CODE",nullable=true,length=20)
	public java.lang.String getPayeeBankCode(){
		return this.payeeBankCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款方开户行行号
	 */
	public void setPayeeBankCode(java.lang.String payeeBankCode){
		this.payeeBankCode = payeeBankCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款开户行名称
	 */
	@Column(name ="PAYEE_BANK",nullable=true,length=150)
	public java.lang.String getPayeeBank(){
		return this.payeeBank;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款开户行名称
	 */
	public void setPayeeBank(java.lang.String payeeBank){
		this.payeeBank = payeeBank;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款行别
	 */
	@Column(name ="BANK_TYPE_ID",nullable=true,length=32)
	public java.lang.String getBankTypeId(){
		return this.bankTypeId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款行别
	 */
	public void setBankTypeId(java.lang.String bankTypeId){
		this.bankTypeId = bankTypeId;
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  期望支付日期
	 */
	@Column(name ="WISH_PAYDAY",nullable=true)
	public java.util.Date getWishPayday(){
		return this.wishPayday;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  期望支付日期
	 */
	public void setWishPayday(java.util.Date wishPayday){
		this.wishPayday = wishPayday;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  加急标志
	 */
	@Column(name ="IS_QUICK",nullable=true,length=1)
	public java.lang.String getIsQuick(){
		return this.isQuick;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  加急标志
	 */
	public void setIsQuick(java.lang.String isQuick){
		this.isQuick = isQuick;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否对私付款
	 */
	@Column(name ="IS_FORINDIVIDUAL",nullable=true,length=1)
	public java.lang.String getIsForindividual(){
		return this.isForindividual;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否对私付款
	 */
	public void setIsForindividual(java.lang.String isForindividual){
		this.isForindividual = isForindividual;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  接口代码
	 */
	@Column(name ="BIF_CODE",nullable=true,length=32)
	public java.lang.String getBifCode(){
		return this.bifCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  接口代码
	 */
	public void setBifCode(java.lang.String bifCode){
		this.bifCode = bifCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  跨行标识
	 */
	@Column(name ="DIF_BANK",nullable=true,length=1)
	public java.lang.String getDifBank(){
		return this.difBank;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  跨行标识
	 */
	public void setDifBank(java.lang.String difBank){
		this.difBank = difBank;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  地域标识
	 */
	@Column(name ="AREA_SIGN",nullable=true,length=1)
	public java.lang.String getAreaSign(){
		return this.areaSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  地域标识
	 */
	public void setAreaSign(java.lang.String areaSign){
		this.areaSign = areaSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  会计凭证号
	 */
	@Column(name ="BURSAR_BILL_NO",nullable=true,length=40)
	public java.lang.String getBursarBillNo(){
		return this.bursarBillNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  会计凭证号
	 */
	public void setBursarBillNo(java.lang.String bursarBillNo){
		this.bursarBillNo = bursarBillNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  用途
	 */
	@Column(name ="PURPOSE",nullable=true,length=60)
	public java.lang.String getPurpose(){
		return this.purpose;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  用途
	 */
	public void setPurpose(java.lang.String purpose){
		this.purpose = purpose;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  附言
	 */
	@Column(name ="ADD_WORD",nullable=true,length=100)
	public java.lang.String getAddWord(){
		return this.addWord;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  附言
	 */
	public void setAddWord(java.lang.String addWord){
		this.addWord = addWord;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否直接付款
	 */
	@Column(name ="DIRECT_PAY_FLAG",nullable=true,length=1)
	public java.lang.String getDirectPayFlag(){
		return this.directPayFlag;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否直接付款
	 */
	public void setDirectPayFlag(java.lang.String directPayFlag){
		this.directPayFlag = directPayFlag;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  证书信息
	 */
	@Column(name ="CA_INFO_ID",nullable=true,length=32)
	public java.lang.String getCaInfoId(){
		return this.caInfoId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  证书信息
	 */
	public void setCaInfoId(java.lang.String caInfoId){
		this.caInfoId = caInfoId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  记账日期
	 */
	@Column(name ="TALLY_DATE",nullable=true)
	public java.util.Date getTallyDate(){
		return this.tallyDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  记账日期
	 */
	public void setTallyDate(java.util.Date tallyDate){
		this.tallyDate = tallyDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  凭证流水号
	 */
	@Column(name ="ORDER_NO",nullable=true,length=18)
	public java.lang.String getOrderNo(){
		return this.orderNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证流水号
	 */
	public void setOrderNo(java.lang.String orderNo){
		this.orderNo = orderNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  记账凭证号码
	 */
	@Column(name ="VOUCHER_NO",nullable=true,length=40)
	public java.lang.String getVoucherNo(){
		return this.voucherNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  记账凭证号码
	 */
	public void setVoucherNo(java.lang.String voucherNo){
		this.voucherNo = voucherNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  ERP编号
	 */
	@Column(name ="ERP_ID",nullable=true,length=32)
	public java.lang.String getErpId(){
		return this.erpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  ERP编号
	 */
	public void setErpId(java.lang.String erpId){
		this.erpId = erpId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务类别
	 */
	@Column(name ="BUS_TYPE",nullable=true,length=32)
	public java.lang.String getBusType(){
		return this.busType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务类别
	 */
	public void setBusType(java.lang.String busType){
		this.busType = busType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单据来源
	 */
	@Column(name ="BILL_FROM",nullable=true,length=2)
	public java.lang.String getBillFrom(){
		return this.billFrom;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单据来源
	 */
	public void setBillFrom(java.lang.String billFrom){
		this.billFrom = billFrom;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  拆分前单据号
	 */
	@Column(name ="SPLIT_ID",nullable=true,length=32)
	public java.lang.String getSplitId(){
		return this.splitId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  拆分前单据号
	 */
	public void setSplitId(java.lang.String splitId){
		this.splitId = splitId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  原始拆分前单据号
	 */
	@Column(name ="ORIG_SPLIT_ID",nullable=true,length=32)
	public String getOrigSplitId() {
		return origSplitId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  原始拆分前单据号
	 */
	public void setOrigSplitId(String origSplitId) {
		this.origSplitId = origSplitId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  合并后单据号
	 */
	@Column(name ="MERGE_ID",nullable=true,length=32)
	public java.lang.String getMergeId(){
		return this.mergeId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  合并后单据号
	 */
	public void setMergeId(java.lang.String mergeId){
		this.mergeId = mergeId;
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  附件ID
	 */
	@Column(name ="ATTACHMENT_ID",nullable=true,length=32)
	public java.lang.String getAttachmentId() {
		return attachmentId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  附件ID
	 */
	public void setAttachmentId(java.lang.String attachmentId) {
		this.attachmentId = attachmentId;
	}
	@Column(name ="IS_PAYED",nullable=true,length=1)
	public String getIsPayed() {
		return isPayed;
	}
	public void setIsPayed(String isPayed) {
		this.isPayed = isPayed;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  内部资金计划标识
	 */
	@Column(name ="INNER_SIGN",length=1)
	public String getInnerSign() {
		return innerSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  内部资金计划标识
	 */
	public void setInnerSign(String innerSign) {
		this.innerSign = innerSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  记账标志
	 */
	@Column(name ="TALLY_SIGN",length=1)
	public java.lang.String getTallySign() {
		return tallySign;
	}
	public void setTallySign(java.lang.String tallySign) {
		this.tallySign = tallySign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  记账标志
	 */
	@Column(name ="TALLY_CODE",length=32)
	public java.lang.String getTallyCode() {
		return tallyCode;
	}
	public void setTallyCode(java.lang.String tallyCode) {
		this.tallyCode = tallyCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否垫支
	 */
	@Column(name ="IS_ADVANCE",nullable=true,length=1)
	public String getIsAdvance() {
		return isAdvance;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否垫支
	 */
	public void setIsAdvance(String isAdvance) {
		this.isAdvance = isAdvance;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  营运资金比例
	 */
	@Column(name ="WORK_CAPITAL_RATIO",nullable=true,scale=2,length=15)
	public BigDecimal getWorkCapitalRatio() {
		return workCapitalRatio;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  营运资金比例
	 */
	public void setWorkCapitalRatio(BigDecimal workCapitalRatio) {
		this.workCapitalRatio = workCapitalRatio;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  预计收回日期
	 */
	@Column(name ="TAKE_BACK_DATE",nullable=true)
	public Date getTakeBackDate() {
		return takeBackDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  预计收回日期
	 */
	public void setTakeBackDate(Date takeBackDate) {
		this.takeBackDate = takeBackDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否已开票
	 */
	@Column(name ="IS_OPEN_BILL",nullable=true,length=1)
	public String getIsOpenBill() {
		return isOpenBill;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否已开票
	 */
	public void setIsOpenBill(String isOpenBill) {
		this.isOpenBill = isOpenBill;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  供应链金融-是否已有开立的应付债权凭证
	 */
	@Column(name ="SCF_IS_OPENED",nullable=true,length=1)
	public String getScfIsOpened() {
		return scfIsOpened;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  供应链金融-是否已有开立的应付债权凭证
	 */
	public void setScfIsOpened(String scfIsOpened) {
		this.scfIsOpened = scfIsOpened;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  供应链金融-应付债权凭证开立金额
	 */
	@Column(name ="SCF_PAYABLE_OPEN_AMT",nullable=true,scale=2,length=15)
	public BigDecimal getScfPayableOpenAmt() {
		return scfPayableOpenAmt;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  供应链金融-应付债权凭证开立金额
	 */
	public void setScfPayableOpenAmt(BigDecimal scfPayableOpenAmt) {
		this.scfPayableOpenAmt = scfPayableOpenAmt;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  供应链金融-应收转让凭证金额
	 */
	@Column(name ="SCF_TRANSFER_AMT",nullable=true,scale=2,length=15)
	public BigDecimal getScfTransferAmt() {
		return scfTransferAmt;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  供应链金融-应收转让凭证金额
	 */
	public void setScfTransferAmt(BigDecimal scfTransferAmt) {
		this.scfTransferAmt = scfTransferAmt;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行流水号
	 */
	@Column(name ="BANK_SERIAL_NO",nullable=true,length=60)
	public String getBankSerialNo() {
		return bankSerialNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行流水号
	 */
	public void setBankSerialNo(String bankSerialNo) {
		this.bankSerialNo = bankSerialNo;
	}
	@Column(name ="IS_AUTO_PAY",nullable=true)
	public String getIsAutoPay() {
		return isAutoPay;
	}
	public void setIsAutoPay(String isAutoPay) {
		this.isAutoPay = isAutoPay;
	}
	@Column(name ="POLICY_ID",nullable=true)
	public String getPolicyId() {
		return policyId;
	}
	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}
	@Column(name ="BILL_TYPE")
	public Integer getBillType() {
		return billType;
	}
	public void setBillType(Integer billType) {
		this.billType = billType;
	}
	@Column(name ="BILL_FLAG")
	public Integer getBillFlag() {
		return billFlag;
	}
	public void setBillFlag(Integer billFlag) {
		this.billFlag = billFlag;
	}
	@Column(name ="TIME_STAMP")
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	@Column(name ="RMK")
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	@Column(name ="BUS_CORP_ID")
	public String getBusCorpId() {
		return busCorpId;
	}
	public void setBusCorpId(String busCorpId) {
		this.busCorpId = busCorpId;
	}
	@Column(name ="BUS_PERSONNEL")
	public String getBusPersonnel() {
		return busPersonnel;
	}
	public void setBusPersonnel(String busPersonnel) {
		this.busPersonnel = busPersonnel;
	}
	@Column(name ="BP_ID")
	public String getBpId() {
		return bpId;
	}
	public void setBpId(String bpId) {
		this.bpId = bpId;
	}
	@Column(name ="SCHEDULING_ELEMENTS_ID")
	public String getSchedulingElementsId() {
		return schedulingElementsId;
	}
	public void setSchedulingElementsId(String schedulingElementsId) {
		this.schedulingElementsId = schedulingElementsId;
	}
	@Column(name ="SCHEDULING_DATE")
	public Date getSchedulingDate() {
		return schedulingDate;
	}
	public void setSchedulingDate(Date schedulingDate) {
		this.schedulingDate = schedulingDate;
	}
	@Column(name ="NIS_BILLHEADPOOL_ID")
	public String getNisBillheadPoolId() {
		return nisBillheadPoolId;
	}
	public void setNisBillheadPoolId(String nisBillheadPoolId) {
		this.nisBillheadPoolId = nisBillheadPoolId;
	}
	@Column(name ="APPROVE_DATE")
	public Date getApproveDate() {
		return approveDate;
	}
	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}
	@Column(name ="VOUCHER_STAT")
	public String getVoucherStat() {
		return voucherStat;
	}
	public void setVoucherStat(String voucherStat) {
		this.voucherStat = voucherStat;
	}
	@Column(name ="SHARING_DATE")
	public Date getSharingDate() {
		return sharingDate;
	}

	public void setSharingDate(Date sharingDate) {
		this.sharingDate = sharingDate;
	}

	@Column(name ="REAL_PAYER_ACC_ID")
	public String getRealPayerAccId() {
		return realPayerAccId;
	}

	public void setRealPayerAccId(String realPayerAccId) {
		this.realPayerAccId = realPayerAccId;
	}

	@Column(name ="SHARE_BUS_TYPE_ID")
	public String getShareBusTypeId() {
		return shareBusTypeId;
	}

	public void setShareBusTypeId(String shareBusTypeId) {
		this.shareBusTypeId = shareBusTypeId;
	}

	@Column(name ="LANDING_LIMIT_RESULT", length=1)
	public String getLandingLimitResult() {
		return landingLimitResult;
	}

	public void setLandingLimitResult(String landingLimitResult) {
		this.landingLimitResult = landingLimitResult;
	}
}
