package com.bytter.bill.domain.bill.entity;
import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: 票据主信息
 * @author onlineGenerator
 * @date 2017-06-21 15:50:47
 * @version 1.0
 *
 */
@Entity
@Table(name = "AB_VOUCHERINFO", schema = "")
@SuppressWarnings("serial")
public class AbVoucherinfo extends IdEntity implements java.io.Serializable {
	/**票据号码*/
	private String origNo;
	/**票据类型：10 银票、11 商票*/
	private Integer billType;
	/**不得转让标记EM00可再转让EM01不得转让*/
	private String banendrsmtmk;
	/**处理标记 10正在处理11处理完成*/
	private Integer boxStatus;
	/**票据状态*/
	private String billStatus;
	/**流通介质：10 纸票、11 电票*/
	private Integer billFlag;
	/**票据金额*/
	private java.math.BigDecimal billMoney;
	/**出票日期*/
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
	private Date startDate;
	/**到期日期*/
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
	private Date endDate;
	/**出票人*/
	private String payer;
	/**出票人账号*/
	private String payeracc;
	/**出票人开户银行名称*/
	private String payerBankName;
	/**出票人开户银行行号*/
	private String payerbankno;
	/**收票人全称*/
	private String payee;
	/**收票人账号*/
	private String payeeacc;
	// 收票人银行类型（冗余）
	private String payeeBankTypeId;
	/**收票人开户银行名称*/
	private String payeeBankName;
	/**收票人开户银行行号*/
	private String payeebankno;
	/**承兑人*/
	private String acceptor;
	/**承兑人账号*/
	private String acceptoracc;
	// 承兑人账号银行类型（冗余）
	private String acceptorAccBankTypeId;
	/**承兑人开户行行号*/
	private String acceptorbankno;
	/**承兑人开户行名称*/
	private String acceptorBankName;
	/**承兑人开户行地址*/
	private String acceptorbankaddress;
	/**瑕疵类型 通过数据字典维护*/
	private String arXclx;
	/**承兑合同号*/
	private String acontractNo;
	/**发票号*/
	private String ainvcnb;
	/**承兑协议编号*/
	private String accptncagrmtnb;
	/** 来源类型 0应付票据，1应收票据*/
	private String sourceType;
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
	/**开票信用等级*/
	private String payerCdtRatgs;
	/**开票评级机构*/
	private String payerCdtRatgGgcy;
	/**开票评级到期日*/
	private Date payerCdtRatgDuedt;
	private Boolean isSaveImport;
	/** 出票标志 */
	private String newStmIssuofpprInd = "0";
	/** 是否允许分包 */
	private String blgrdbagcrclPrmtInd = "0";
	/** 子票区间开始序号 */
	private String sbbllRngStrtSn = "0";
	/** 子票区间结束序号 */
	private String sbbllRngEndSn = "0";
	/** 标准金额 */
	private BigDecimal blStdAmt = new BigDecimal(0.01);
	/** 出票人统一识别号码 */
	private String blRmtrUnnIdNo;
	/** 收票人统一信用代码 */
	private String blRcvpymtpsUnnIdNo;
	/** 承兑人统一信用代码 */
	private String blAcptrUnnIdNo;
	private String ebillBlCrclIndc;//EBILL_BL_CRCL_INDC 流通标志

	/** 背书次数 */
	private Integer endorsementCount;

    /** 备注 */
	private String rmk;

	@Column(name ="ENDORSEMENT_COUNT")
	public Integer getEndorsementCount() {
		return endorsementCount;
	}

	public void setEndorsementCount(Integer endorsementCount) {
		this.endorsementCount = endorsementCount;
	}

	@Column(name ="EBILL_BL_CRCL_INDC")
	public String getEbillBlCrclIndc() {
		return ebillBlCrclIndc;
	}
	public void setEbillBlCrclIndc(String ebillBlCrclIndc) {
		this.ebillBlCrclIndc = ebillBlCrclIndc;
	}
	/**
	 * 合作银行号
	 */
	private String cooperativeBankNo;
	/**
	 * 拆分前的票据id
	 */
	private String oldDraftId;
	private String blBsnTpcd;//票据业务类型代码 BL_BSN_TPCD

	private String riskBillType; //风险票据类型
	private String disountIncIf; //是否已包贴
	private String cashIncIf; //是否已保兑
	private String avoidClaimDebtsIf; //是否免追索票据
	private String payerChannelName; //出票人办理渠道名称
	private String acceptorChannelName; //承兑人办理渠道名称
	private String payeeChannelName; //收票人办理渠道名称

	/**
	 * 原始票据区间
	 */
	private String orgSbbllRngStrtSn;

	/**
	 * 原始票据区间
	 */
	private String orgSbbllRngEndSn;

	private String accptncagrmtBranch;
	private String accptncagrmtBranchNo;
	private String cvmLimitNo;
	private String riskContractNumber;
	private String cmbBnkbilnegId;//招行合作框架协议表id
	private String cmbCrlConId;//招行风险合同信息表id
	/**
	 * 招行列表保证金选择标识 0-否 1-是
	 */
	private String cmbBailFlag;

	@Column(name = "CMB_BAIL_FLAG")
	public String getCmbBailFlag() {
		return cmbBailFlag;
	}

	public void setCmbBailFlag(String cmbBailFlag) {
		this.cmbBailFlag = cmbBailFlag;
	}

	@Column(name ="CMB_BNKBILNEG_ID")
	public String getCmbBnkbilnegId() {
		return cmbBnkbilnegId;
	}

	public void setCmbBnkbilnegId(String cmbBnkbilnegId) {
		this.cmbBnkbilnegId = cmbBnkbilnegId;
	}

	@Column(name ="CMB_CRLCON_ID")
	public String getCmbCrlConId() {
		return cmbCrlConId;
	}

	public void setCmbCrlConId(String cmbCrlConId) {
		this.cmbCrlConId = cmbCrlConId;
	}

	@Column(name ="Accptncagrmt_Branch")
	public String getAccptncagrmtBranch() {
		return accptncagrmtBranch;
	}

	public void setAccptncagrmtBranch(String accptncagrmtBranch) {
		this.accptncagrmtBranch = accptncagrmtBranch;
	}

	@Column(name ="Accptncagrmt_Branch_No")
	public String getAccptncagrmtBranchNo() {
		return accptncagrmtBranchNo;
	}

	public void setAccptncagrmtBranchNo(String accptncagrmtBranchNo) {
		this.accptncagrmtBranchNo = accptncagrmtBranchNo;
	}

	@Column(name ="Cvm_Limit_No")
	public String getCvmLimitNo() {
		return cvmLimitNo;
	}

	public void setCvmLimitNo(String cvmLimitNo) {
		this.cvmLimitNo = cvmLimitNo;
	}

	@Column(name ="Risk_Contract_Number")
	public String getRiskContractNumber() {
		return riskContractNumber;
	}

	public void setRiskContractNumber(String riskContractNumber) {
		this.riskContractNumber = riskContractNumber;
	}

	@Column(name ="BL_BSN_TPCD")
	public String getBlBsnTpcd() {
		return blBsnTpcd;
	}
	public void setBlBsnTpcd(String blBsnTpcd) {
		this.blBsnTpcd = blBsnTpcd;
	}
	@Column(name ="OLD_DRAFT_ID")
	public String getOldDraftId() {
		return oldDraftId;
	}
	public void setOldDraftId(String oldDraftId) {
		this.oldDraftId = oldDraftId;
	}
	@Column(name ="COOPERATIVE_BANK_NO")
	public String getCooperativeBankNo() {
		return cooperativeBankNo;
	}
	public void setCooperativeBankNo(String cooperativeBankNo) {
		this.cooperativeBankNo = cooperativeBankNo;
	}
	@Transient
	public Boolean getSaveImport() {
		return isSaveImport;
	}
	public void setSaveImport(Boolean saveImport) {
		isSaveImport = saveImport;
	}
	@Column(name ="NEW_STM_ISSUOFPPR_IND")
	public String getNewStmIssuofpprInd() {
		return newStmIssuofpprInd;
	}
	public void setNewStmIssuofpprInd(String newStmIssuofpprInd) {
		this.newStmIssuofpprInd = newStmIssuofpprInd;
	}
	@Column(name ="BLGRDBAGCRCL_PRMT_IND")
	public String getBlgrdbagcrclPrmtInd() {
		return blgrdbagcrclPrmtInd;
	}
	public void setBlgrdbagcrclPrmtInd(String blgrdbagcrclPrmtInd) {
		this.blgrdbagcrclPrmtInd = blgrdbagcrclPrmtInd;
	}
	@Column(name ="SBBLL_RNG_STRT_SN")
	public String getSbbllRngStrtSn() {
		return sbbllRngStrtSn;
	}
	public void setSbbllRngStrtSn(String sbbllRngStrtSn) {
		this.sbbllRngStrtSn = sbbllRngStrtSn;
	}
	@Column(name ="SBBLL_RNG_END_SN")
	public String getSbbllRngEndSn() {
		return sbbllRngEndSn;
	}
	public void setSbbllRngEndSn(String sbbllRngEndSn) {
		this.sbbllRngEndSn = sbbllRngEndSn;
	}
	@Column(name ="BL_STD_AMT")
	public BigDecimal getBlStdAmt() {
		return (blStdAmt == null ? new BigDecimal(0.01) : blStdAmt);
	}
	public void setBlStdAmt(BigDecimal blStdAmt) {
		this.blStdAmt = blStdAmt;
	}
	@Column(name ="BL_RMTR_UNN_ID_NO")
	public String getBlRmtrUnnIdNo() {
		return blRmtrUnnIdNo;
	}
	public void setBlRmtrUnnIdNo(String blRmtrUnnIdNo) {
		this.blRmtrUnnIdNo = blRmtrUnnIdNo;
	}
	@Column(name ="BL_RCVPYMTPS_UNN_ID_NO")
	public String getBlRcvpymtpsUnnIdNo() {
		return blRcvpymtpsUnnIdNo;
	}
	public void setBlRcvpymtpsUnnIdNo(String blRcvpymtpsUnnIdNo) {
		this.blRcvpymtpsUnnIdNo = blRcvpymtpsUnnIdNo;
	}
	@Column(name ="BL_ACPTR_UNN_ID_NO")
	public String getBlAcptrUnnIdNo() {
		return blAcptrUnnIdNo;
	}
	public void setBlAcptrUnnIdNo(String blAcptrUnnIdNo) {
		this.blAcptrUnnIdNo = blAcptrUnnIdNo;
	}
	@Column(name ="PAYEE_BANK_TYPE_ID")
	public String getPayeeBankTypeId() {
		return payeeBankTypeId;
	}
	public void setPayeeBankTypeId(String payeeBankTypeId) {
		this.payeeBankTypeId = payeeBankTypeId;
	}
	@Column(name ="ACCEPTOR_ACC_BANK_TYPE_ID")
	public String getAcceptorAccBankTypeId() {
		return acceptorAccBankTypeId;
	}
	public void setAcceptorAccBankTypeId(String acceptorAccBankTypeId) {
		this.acceptorAccBankTypeId = acceptorAccBankTypeId;
	}
	@Column(name ="PAYER_CDT_RATGS")
	public String getPayerCdtRatgs() {
		return payerCdtRatgs;
	}
	public void setPayerCdtRatgs(String payerCdtRatgs) {
		this.payerCdtRatgs = payerCdtRatgs;
	}
	@Column(name ="PAYER_CDT_RATG_AGCY")
	public String getPayerCdtRatgGgcy() {
		return payerCdtRatgGgcy;
	}
	public void setPayerCdtRatgGgcy(String payerCdtRatgGgcy) {
		this.payerCdtRatgGgcy = payerCdtRatgGgcy;
	}
	@Column(name ="PAYER_CDT_RATG_DUEDT")
	public Date getPayerCdtRatgDuedt() {
		return payerCdtRatgDuedt;
	}
	public void setPayerCdtRatgDuedt(Date payerCdtRatgDuedt) {
		this.payerCdtRatgDuedt = payerCdtRatgDuedt;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  票据号码
	 */
	@Column(name ="ORIG_NO")
	public String getOrigNo(){
		return this.origNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  票据号码
	 */
	public void setOrigNo(String origNo){
		this.origNo = origNo;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  票据类型：10 银票、11 商票
	 */
	@Column(name ="BILL_TYPE")
	public Integer getBillType(){
		return this.billType;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  票据类型：10 银票、11 商票
	 */
	public void setBillType(Integer billType){
		this.billType = billType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  不得转让标记EM00可再转让EM01不得转让
	 */
	@Column(name ="BANENDRSMTMK")
	public String getBanendrsmtmk(){
		return this.banendrsmtmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  不得转让标记EM00可再转让EM01不得转让
	 */
	public void setBanendrsmtmk(String banendrsmtmk){
		this.banendrsmtmk = banendrsmtmk;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  处理标记 10正在处理11处理完成
	 */
	@Column(name ="BOX_STATUS")
	public Integer getBoxStatus(){
		return this.boxStatus;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  处理标记 10正在处理11处理完成
	 */
	public void setBoxStatus(Integer boxStatus){
		this.boxStatus = boxStatus;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  票据状态
	 */
	@Column(name ="BILL_STATUS")
	public String getBillStatus(){
		return this.billStatus;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  票据状态
	 */
	public void setBillStatus(String billStatus){
		this.billStatus = billStatus;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  流通介质：10 纸票、11 电票
	 */
	@Column(name ="BILL_FLAG")
	public Integer getBillFlag(){
		return this.billFlag;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  流通介质：10 纸票、11 电票
	 */
	public void setBillFlag(Integer billFlag){
		this.billFlag = billFlag;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  票据金额
	 */
	@Column(name ="BILL_MONEY")
	public java.math.BigDecimal getBillMoney(){
		return this.billMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  票据金额
	 */
	public void setBillMoney(java.math.BigDecimal billMoney){
		this.billMoney = billMoney;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  出票日期
	 */
	@Column(name ="START_DATE")
	public Date getStartDate(){
		return this.startDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  出票日期
	 */
	public void setStartDate(Date startDate){
		this.startDate = startDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  到期日期
	 */
	@Column(name ="END_DATE")
	public Date getEndDate(){
		return this.endDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  到期日期
	 */
	public void setEndDate(Date endDate){
		this.endDate = endDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  出票人
	 */
	@Column(name ="PAYER")
	public String getPayer(){
		return this.payer;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  出票人
	 */
	public void setPayer(String payer){
		this.payer = payer;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  出票人账号
	 */
	@Column(name ="PAYERACC")
	public String getPayeracc(){
		return this.payeracc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  出票人账号
	 */
	public void setPayeracc(String payeracc){
		this.payeracc = payeracc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  出票人开户银行名称
	 */
	@Column(name ="PAYER_BANK_NAME")
	public String getPayerBankName(){
		return this.payerBankName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  出票人开户银行名称
	 */
	public void setPayerBankName(String payerBankName){
		this.payerBankName = payerBankName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  出票人开户银行行号
	 */
	@Column(name ="PAYERBANKNO")
	public String getPayerbankno(){
		return this.payerbankno;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  出票人开户银行行号
	 */
	public void setPayerbankno(String payerbankno){
		this.payerbankno = payerbankno;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收票人全称
	 */
	@Column(name ="PAYEE")
	public String getPayee(){
		return this.payee;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收票人全称
	 */
	public void setPayee(String payee){
		this.payee = payee;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收票人账号
	 */
	@Column(name ="PAYEEACC")
	public String getPayeeacc(){
		return this.payeeacc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收票人账号
	 */
	public void setPayeeacc(String payeeacc){
		this.payeeacc = payeeacc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收票人开户银行名称
	 */
	@Column(name ="PAYEE_BANK_NAME")
	public String getPayeeBankName(){
		return this.payeeBankName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收票人开户银行名称
	 */
	public void setPayeeBankName(String payeeBankName){
		this.payeeBankName = payeeBankName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收票人开户银行行号
	 */
	@Column(name ="PAYEEBANKNO")
	public String getPayeebankno(){
		return this.payeebankno;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收票人开户银行行号
	 */
	public void setPayeebankno(String payeebankno){
		this.payeebankno = payeebankno;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  承兑人
	 */
	@Column(name ="ACCEPTOR")
	public String getAcceptor(){
		return this.acceptor;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  承兑人
	 */
	public void setAcceptor(String acceptor){
		this.acceptor = acceptor;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  承兑人账号
	 */
	@Column(name ="ACCEPTORACC")
	public String getAcceptoracc(){
		return this.acceptoracc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  承兑人账号
	 */
	public void setAcceptoracc(String acceptoracc){
		this.acceptoracc = acceptoracc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  承兑人开户行行号
	 */
	@Column(name ="ACCEPTORBANKNO")
	public String getAcceptorbankno(){
		return this.acceptorbankno;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  承兑人开户行行号
	 */
	public void setAcceptorbankno(String acceptorbankno){
		this.acceptorbankno = acceptorbankno;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  承兑人开户行名称
	 */
	@Column(name ="ACCEPTOR_BANK_NAME")
	public String getAcceptorBankName(){
		return this.acceptorBankName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  承兑人开户行名称
	 */
	public void setAcceptorBankName(String acceptorBankName){
		this.acceptorBankName = acceptorBankName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  承兑人开户行地址
	 */
	@Column(name ="ACCEPTORBANKADDRESS")
	public String getAcceptorbankaddress(){
		return this.acceptorbankaddress;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  承兑人开户行地址
	 */
	public void setAcceptorbankaddress(String acceptorbankaddress){
		this.acceptorbankaddress = acceptorbankaddress;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  瑕疵类型 通过数据字典维护
	 */
	@Column(name ="AR_XCLX")
	public String getArXclx(){
		return this.arXclx;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  瑕疵类型 通过数据字典维护
	 */
	public void setArXclx(String arXclx){
		this.arXclx = arXclx;
	}
	/**
	 * 方法: 取得java.lang.String
	 * @return: java.lang.String 承兑合同号
	 */
	@Column(name = "A_CONTRACT_NO")
	public String getAcontractNo() {
		return this.acontractNo;
	}
	/**
	 * 方法: 设置java.lang.String
	 * @param: java.lang.String  承兑合同号
	 *
	 */
	public void setAcontractNo(String acontractNo) {
		this.acontractNo = acontractNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  发票号
	 */
	@Column(name ="A_INVCNB")
	public String getAinvcnb(){
		return this.ainvcnb;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  发票号
	 */
	public void setAinvcnb(String ainvcnb){
		this.ainvcnb = ainvcnb;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  承兑协议编号
	 */
	@Column(name ="ACCPTNCAGRMTNB")
	public String getAccptncagrmtnb(){
		return this.accptncagrmtnb;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  承兑协议编号
	 */
	public void setAccptncagrmtnb(String accptncagrmtnb){
		this.accptncagrmtnb = accptncagrmtnb;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人名称
	 */
	@Column(name ="CREATE_NAME")
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
	@Column(name ="CREATE_BY")
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
	@Column(name ="CREATE_DATE")
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
	@Column(name ="UPDATE_NAME")
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
	@Column(name ="UPDATE_BY")
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
	@Column(name ="UPDATE_DATE")
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
	
	@Column(name ="SOURCE_TYPE")
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	@Column(name ="RISK_BILL_TYPE")
	public String getRiskBillType() {
		return riskBillType;
	}

	public void setRiskBillType(String riskBillType) {
		this.riskBillType = riskBillType;
	}
	@Column(name ="DISOUNT_INC_IF")
	public String getDisountIncIf() {
		return disountIncIf;
	}

	public void setDisountIncIf(String disountIncIf) {
		this.disountIncIf = disountIncIf;
	}

	@Column(name ="CASH_INC_IF")
	public String getCashIncIf() {
		return cashIncIf;
	}

	public void setCashIncIf(String cashIncIf) {
		this.cashIncIf = cashIncIf;
	}

	@Column(name ="AVOID_CLAIM_DEBTS_IF")
	public String getAvoidClaimDebtsIf() {
		return avoidClaimDebtsIf;
	}

	public void setAvoidClaimDebtsIf(String avoidClaimDebtsIf) {
		this.avoidClaimDebtsIf = avoidClaimDebtsIf;
	}

	@Column(name ="PAYER_CHANNEL_NAME")
	public String getPayerChannelName() {
		return payerChannelName;
	}

	public void setPayerChannelName(String payerChannelName) {
		this.payerChannelName = payerChannelName;
	}

	@Column(name ="ACCEPTOR_CHANNEL_NAME")
	public String getAcceptorChannelName() {
		return acceptorChannelName;
	}

	public void setAcceptorChannelName(String acceptorChannelName) {
		this.acceptorChannelName = acceptorChannelName;
	}

	@Column(name ="PAYEE_CHANNEL_NAME")
	public String getPayeeChannelName() {
		return payeeChannelName;
	}

	public void setPayeeChannelName(String payeeChannelName) {
		this.payeeChannelName = payeeChannelName;
	}

	@Column(name ="ORG_SBBLL_RNG_STRT_SN")
	public String getOrgSbbllRngStrtSn() {
		return orgSbbllRngStrtSn;
	}

	public void setOrgSbbllRngStrtSn(String orgSbbllRngStrtSn) {
		this.orgSbbllRngStrtSn = orgSbbllRngStrtSn;
	}

	@Column(name ="ORG_SBBLL_RNG_END_SN")
	public String getOrgSbbllRngEndSn() {
		return orgSbbllRngEndSn;
	}

	public void setOrgSbbllRngEndSn(String orgSbbllRngEndSn) {
		this.orgSbbllRngEndSn = orgSbbllRngEndSn;
	}

    @Column(name ="RMK")
    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    @Override
	public String toString() {
		return "AbVoucherinfo{" +
				"origNo='" + origNo + '\'' +
				", billType=" + billType +
				", banendrsmtmk='" + banendrsmtmk + '\'' +
				", boxStatus=" + boxStatus +
				", billStatus='" + billStatus + '\'' +
				", billFlag=" + billFlag +
				", billMoney=" + billMoney +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", payer='" + payer + '\'' +
				", payeracc='" + payeracc + '\'' +
				", payerBankName='" + payerBankName + '\'' +
				", payerbankno='" + payerbankno + '\'' +
				", payee='" + payee + '\'' +
				", payeeacc='" + payeeacc + '\'' +
				", payeeBankName='" + payeeBankName + '\'' +
				", payeebankno='" + payeebankno + '\'' +
				", acceptor='" + acceptor + '\'' +
				", acceptoracc='" + acceptoracc + '\'' +
				", acceptorbankno='" + acceptorbankno + '\'' +
				", acceptorBankName='" + acceptorBankName + '\'' +
				", acceptorbankaddress='" + acceptorbankaddress + '\'' +
				", arXclx='" + arXclx + '\'' +
				", acontractNo='" + acontractNo + '\'' +
				", ainvcnb='" + ainvcnb + '\'' +
				", accptncagrmtnb='" + accptncagrmtnb + '\'' +
				", sourceType='" + sourceType + '\'' +
				", createName='" + createName + '\'' +
				", createBy='" + createBy + '\'' +
				", createDate=" + createDate +
				", updateName='" + updateName + '\'' +
				", updateBy='" + updateBy + '\'' +
				", updateDate=" + updateDate +
				", payerCdtRatgs='" + payerCdtRatgs + '\'' +
				", payerCdtRatgGgcy='" + payerCdtRatgGgcy + '\'' +
				", payerCdtRatgDuedt=" + payerCdtRatgDuedt +
				", isSaveImport=" + isSaveImport +
				", newStmIssuofpprInd='" + newStmIssuofpprInd + '\'' +
				", blgrdbagcrclPrmtInd='" + blgrdbagcrclPrmtInd + '\'' +
				", sbbllRngStrtSn='" + sbbllRngStrtSn + '\'' +
				", sbbllRngEndSn='" + sbbllRngEndSn + '\'' +
				", blStdAmt=" + blStdAmt +
				", blRmtrUnnIdNo='" + blRmtrUnnIdNo + '\'' +
				", blRcvpymtpsUnnIdNo='" + blRcvpymtpsUnnIdNo + '\'' +
				", blAcptrUnnIdNo='" + blAcptrUnnIdNo + '\'' +
				", ebillBlCrclIndc='" + ebillBlCrclIndc + '\'' +
				", cooperativeBankNo='" + cooperativeBankNo + '\'' +
				", oldDraftId='" + oldDraftId + '\'' +
				", blBsnTpcd='" + blBsnTpcd + '\'' +
				", riskBillType='" + riskBillType + '\'' +
				", disountIncIf='" + disountIncIf + '\'' +
				", cashIncIf='" + cashIncIf + '\'' +
				", avoidClaimDebtsIf='" + avoidClaimDebtsIf + '\'' +
				", payerChannelName='" + payerChannelName + '\'' +
				", acceptorChannelName='" + acceptorChannelName + '\'' +
				", payeeChannelName='" + payeeChannelName + '\'' +
				", blBsnTpcd='" + blBsnTpcd + '\'' +
				", orgSbbllRngStrtSn='" + orgSbbllRngStrtSn + '\'' +
				", orgSbbllRngEndSn='" + orgSbbllRngEndSn + '\'' +
				'}';
	}
	
}
