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
@Table(name = "NIS_BILLHEAD_POOL", schema = "")
@SuppressWarnings("serial")
public class NisBillheadPool extends IdEntity implements java.io.Serializable {
	/**付款单号*/
	private String billNo;
	/**单位*/
	private String corpId;
	/**付款方式*/
	private String voucherTypeId;
	/**账号*/
	private String accId;
	/**付款账号*/
	private String payerAcc;
	/**付款账号名称*/
	private String payerAccName;
	/**付款开户行名称*/
	private String payerBank;
	/**下拨账号*/
	private String downAccId;
	/**币别*/
	private String curId;
	/**金额*/
	private BigDecimal payMoney;
	/**票据金额*/
	private BigDecimal billMoney;
	/**收款账号*/
	private String payeeAcc;
	/**收款账号名称*/
	private String payeeAccName;
	/**收款方开户行行号*/
	private String payeeBankCode;
	/**收款开户行名称*/
	private String payeeBank;
	/**收款行别*/
	private String bankTypeId;
	/**所在省*/
	private String prov;
	/**所在市*/
	private String city;
	/**期望支付日期*/
	private Date wishPayday;
	/**加急标志*/
	private String isQuick;
	/**是否对私付款*/
	private String isForindividual;
	/**接口代码*/
	private String bifCode;
	/**跨行标识*/
	private String difBank;
	/**地域标识*/
	private String areaSign;
	/**会计凭证号*/
	private String bursarBillNo;
	/**用途*/
	private String purpose;
	/**附言*/
	private String addWord;
	/**是否直接付款*/
	private String directPayFlag;
	/**证书信息*/
	private String caInfoId;
	/**记账日期*/
	private Date tallyDate;
	/**凭证流水号*/
	private String orderNo;
	/**记账凭证号码*/
	private String voucherNo;
	/**ERP编号*/
	private String erpId;
	/**业务类别*/
	private String busType;
	/**单据来源*/
	private String billFrom;
	/**拆分前单据号*/
	private String splitId;
	/**原始拆分前单据号*/
	private String origSplitId;
	/**合并后单据号*/
	private String mergeId;
	/**状态*/
	private Integer status;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**附件ID*/
	private String attachmentId;
	/**是否已付款*/
	private String isPayed;
	/**内部资金计划标识*/
	private String innerSign;
	/**记账标志**/
	private String tallySign;
	/**记账员代码**/
	private String tallyCode;
	/**是否垫支**/
	private String isAdvance;
	/**营运资金比例**/
	private BigDecimal workCapitalRatio;
	/**预计收回日期**/
	private Date takeBackDate;
	/**是否已开票**/
	private String isOpenBill;
	/**银行流水号**/
	private String bankSerialNo;
	/**是否自动支付*/
	private String isAutoPay;
	/**策略ID*/
	private String policyId;
	/**票据类型*/
	private Integer billType;
	/**票据流通介质*/
	private Integer billFlag;
	/**时间戳*/
	private String timeStamp;
	/**备注说明*/
	private String rmk;
	/**业务单位（票据付款专用）*/
	private String busCorpId;
	/**业务人员（票据付款专用）*/
	private String busPersonnel;
    /**排期要素id SCHEDULING_ELEMENTS_ID*/
	private String schedulingElementsId;
    /**排期日期 SCHEDULING_DATE*/
    private Date schedulingDate;
    /**付款单id*/
    private String nisBillheadId;
    /**退回意见*/
    private String reply;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款单号
	 */
	@Column(name ="BILL_NO",nullable=false,length=12)
	public String getBillNo(){
		return this.billNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款单号
	 */
	public void setBillNo(String billNo){
		this.billNo = billNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单位
	 */
	@Column(name ="CORP_ID",nullable=false,length=32)
	public String getCorpId(){
		return this.corpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单位
	 */
	public void setCorpId(String corpId){
		this.corpId = corpId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款方式
	 */
	@Column(name ="VOUCHER_TYPE_ID",nullable=true,length=32)
	public String getVoucherTypeId(){
		return this.voucherTypeId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款方式
	 */
	public void setVoucherTypeId(String voucherTypeId){
		this.voucherTypeId = voucherTypeId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账号
	 */
	@Column(name ="ACC_ID",nullable=true,length=32)
	public String getAccId(){
		return this.accId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账号
	 */
	public void setAccId(String accId){
		this.accId = accId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款账号
	 */
	@Column(name ="PAYER_ACC",nullable=true,length=50)
	public String getPayerAcc(){
		return this.payerAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款账号
	 */
	public void setPayerAcc(String payerAcc){
		this.payerAcc = payerAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款账号名称
	 */
	@Column(name ="PAYER_ACC_NAME",nullable=true,length=150)
	public String getPayerAccName(){
		return this.payerAccName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款账号名称
	 */
	public void setPayerAccName(String payerAccName){
		this.payerAccName = payerAccName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款开户行名称
	 */
	@Column(name ="PAYER_BANK",nullable=true,length=150)
	public String getPayerBank(){
		return this.payerBank;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款开户行名称
	 */
	public void setPayerBank(String payerBank){
		this.payerBank = payerBank;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  下拨账号
	 */
	@Column(name ="DOWN_ACC_ID",nullable=true,length=32)
	public String getDownAccId(){
		return this.downAccId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  下拨账号
	 */
	public void setDownAccId(String downAccId){
		this.downAccId = downAccId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别
	 */
	@Column(name ="CUR_ID",nullable=true,length=32)
	public String getCurId(){
		return this.curId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别
	 */
	public void setCurId(String curId){
		this.curId = curId;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  金额
	 */
	@Column(name ="PAY_MONEY",nullable=false,scale=2,length=15)
	public BigDecimal getPayMoney(){
		return this.payMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  金额
	 */
	public void setPayMoney(BigDecimal payMoney){
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
	public String getPayeeAcc(){
		return this.payeeAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款账号
	 */
	public void setPayeeAcc(String payeeAcc){
		this.payeeAcc = payeeAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款账号名称
	 */
	@Column(name ="PAYEE_ACC_NAME",nullable=true,length=150)
	public String getPayeeAccName(){
		return this.payeeAccName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款账号名称
	 */
	public void setPayeeAccName(String payeeAccName){
		this.payeeAccName = payeeAccName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款方开户行行号
	 */
	@Column(name ="PAYEE_BANK_CODE",nullable=true,length=20)
	public String getPayeeBankCode(){
		return this.payeeBankCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款方开户行行号
	 */
	public void setPayeeBankCode(String payeeBankCode){
		this.payeeBankCode = payeeBankCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款开户行名称
	 */
	@Column(name ="PAYEE_BANK",nullable=true,length=150)
	public String getPayeeBank(){
		return this.payeeBank;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款开户行名称
	 */
	public void setPayeeBank(String payeeBank){
		this.payeeBank = payeeBank;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款行别
	 */
	@Column(name ="BANK_TYPE_ID",nullable=true,length=32)
	public String getBankTypeId(){
		return this.bankTypeId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款行别
	 */
	public void setBankTypeId(String bankTypeId){
		this.bankTypeId = bankTypeId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所在省
	 */
	@Column(name ="PROV",nullable=true,length=20)
	public String getProv(){
		return this.prov;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所在省
	 */
	public void setProv(String prov){
		this.prov = prov;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所在市
	 */
	@Column(name ="CITY",nullable=true,length=20)
	public String getCity(){
		return this.city;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所在市
	 */
	public void setCity(String city){
		this.city = city;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  期望支付日期
	 */
	@Column(name ="WISH_PAYDAY",nullable=true)
	public Date getWishPayday(){
		return this.wishPayday;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  期望支付日期
	 */
	public void setWishPayday(Date wishPayday){
		this.wishPayday = wishPayday;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  加急标志
	 */
	@Column(name ="IS_QUICK",nullable=true,length=1)
	public String getIsQuick(){
		return this.isQuick;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  加急标志
	 */
	public void setIsQuick(String isQuick){
		this.isQuick = isQuick;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否对私付款
	 */
	@Column(name ="IS_FORINDIVIDUAL",nullable=true,length=1)
	public String getIsForindividual(){
		return this.isForindividual;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否对私付款
	 */
	public void setIsForindividual(String isForindividual){
		this.isForindividual = isForindividual;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  接口代码
	 */
	@Column(name ="BIF_CODE",nullable=true,length=32)
	public String getBifCode(){
		return this.bifCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  接口代码
	 */
	public void setBifCode(String bifCode){
		this.bifCode = bifCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  跨行标识
	 */
	@Column(name ="DIF_BANK",nullable=true,length=1)
	public String getDifBank(){
		return this.difBank;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  跨行标识
	 */
	public void setDifBank(String difBank){
		this.difBank = difBank;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  地域标识
	 */
	@Column(name ="AREA_SIGN",nullable=true,length=1)
	public String getAreaSign(){
		return this.areaSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  地域标识
	 */
	public void setAreaSign(String areaSign){
		this.areaSign = areaSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  会计凭证号
	 */
	@Column(name ="BURSAR_BILL_NO",nullable=true,length=40)
	public String getBursarBillNo(){
		return this.bursarBillNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  会计凭证号
	 */
	public void setBursarBillNo(String bursarBillNo){
		this.bursarBillNo = bursarBillNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  用途
	 */
	@Column(name ="PURPOSE",nullable=true,length=60)
	public String getPurpose(){
		return this.purpose;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  用途
	 */
	public void setPurpose(String purpose){
		this.purpose = purpose;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  附言
	 */
	@Column(name ="ADD_WORD",nullable=true,length=100)
	public String getAddWord(){
		return this.addWord;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  附言
	 */
	public void setAddWord(String addWord){
		this.addWord = addWord;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否直接付款
	 */
	@Column(name ="DIRECT_PAY_FLAG",nullable=true,length=1)
	public String getDirectPayFlag(){
		return this.directPayFlag;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否直接付款
	 */
	public void setDirectPayFlag(String directPayFlag){
		this.directPayFlag = directPayFlag;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  证书信息
	 */
	@Column(name ="CA_INFO_ID",nullable=true,length=32)
	public String getCaInfoId(){
		return this.caInfoId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  证书信息
	 */
	public void setCaInfoId(String caInfoId){
		this.caInfoId = caInfoId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  记账日期
	 */
	@Column(name ="TALLY_DATE",nullable=true)
	public Date getTallyDate(){
		return this.tallyDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  记账日期
	 */
	public void setTallyDate(Date tallyDate){
		this.tallyDate = tallyDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  凭证流水号
	 */
	@Column(name ="ORDER_NO",nullable=true,length=18)
	public String getOrderNo(){
		return this.orderNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证流水号
	 */
	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  记账凭证号码
	 */
	@Column(name ="VOUCHER_NO",nullable=true,length=40)
	public String getVoucherNo(){
		return this.voucherNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  记账凭证号码
	 */
	public void setVoucherNo(String voucherNo){
		this.voucherNo = voucherNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  ERP编号
	 */
	@Column(name ="ERP_ID",nullable=true,length=32)
	public String getErpId(){
		return this.erpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  ERP编号
	 */
	public void setErpId(String erpId){
		this.erpId = erpId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务类别
	 */
	@Column(name ="BUS_TYPE",nullable=true,length=32)
	public String getBusType(){
		return this.busType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务类别
	 */
	public void setBusType(String busType){
		this.busType = busType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单据来源
	 */
	@Column(name ="BILL_FROM",nullable=true,length=2)
	public String getBillFrom(){
		return this.billFrom;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单据来源
	 */
	public void setBillFrom(String billFrom){
		this.billFrom = billFrom;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  拆分前单据号
	 */
	@Column(name ="SPLIT_ID",nullable=true,length=32)
	public String getSplitId(){
		return this.splitId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  拆分前单据号
	 */
	public void setSplitId(String splitId){
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
	public String getMergeId(){
		return this.mergeId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  合并后单据号
	 */
	public void setMergeId(String mergeId){
		this.mergeId = mergeId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  状态
	 */
	@Column(name ="STATUS",nullable=true,length=10)
	public Integer getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  状态
	 */
	public void setStatus(Integer status){
		this.status = status;
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
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  附件ID
	 */
	@Column(name ="ATTACHMENT_ID",nullable=true,length=32)
	public String getAttachmentId() {
		return attachmentId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  附件ID
	 */
	public void setAttachmentId(String attachmentId) {
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
	public String getTallySign() {
		return tallySign;
	}
	public void setTallySign(String tallySign) {
		this.tallySign = tallySign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  记账标志
	 */
	@Column(name ="TALLY_CODE",length=32)
	public String getTallyCode() {
		return tallyCode;
	}
	public void setTallyCode(String tallyCode) {
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
    @Column(name ="NIS_BILLHEAD_ID")
    public String getNisBillheadId() {
        return nisBillheadId;
    }
    public void setNisBillheadId(String nisBillheadId) {
        this.nisBillheadId = nisBillheadId;
    }
    @Column(name ="REPLY")
    public String getReply() {
        return reply;
    }
    public void setReply(String reply) {
        this.reply = reply;
    }
}
