package com.bytter.bill.domain.billbank.vo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.utils.StringUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**  *
 * 票据信息
 *
 */
public class VoucherInfoVo implements Serializable,SplitBaseImpVO {

	private static final long serialVersionUID = 5941616251426242335L;
	/**
	 * 票据表
	 */
	private String voucherId; //票据ID
	private String origNo; //原始票据号码
	private Integer billType; //票据种类
	private Timestamp startDate; //起始日期
	private Timestamp endDate; //到期日期
	private BigDecimal billMoney; //票据金额
	private String banendrsmtmk; //不得转让标记
	private String payee; //收款人
	private String payeeAcc; //收款人账号
	private String payeeBankName; //收款人开户行
	private String payeeBankNo; //收款人开户行行号
	private String payer; //出票人
	private String payerAcc; //出票人账号
	private String payerBankNo; //出票人开户行行号
	private String payerBankName; //付款人银行名称
	private String acceptor; //承兑人
	private String acceptorAcc; //承兑人账号
	private String acceptorBankNo; //承兑人开户行行号
	private String acceptorBankName; //承兑人开户行名称
	private String acceptorBankAddress;//承兑人开户行地址
	private Integer boxStatus; //处理标记
	private String billStatus; //票据状态
	private Long sysSubject; //系统科目
	private Integer billFlag; //票据类型
	private String openBill; //开票人
	private Timestamp acceptorDate; //票据承兑日
	private String flwNo; //签发流水号
	private String payerName;//出票人名称
	private String guarantor;//保证人
	private String guarantorAcc;//出票保证人账号
	private String guarantorBankNo;//保证人开户行行号
	private String guarantorBank;//保证人开户行名称
	private String guaranttorBankType;//保证人开户行银行类型
	private String guarantorAddr;//保证人地址
	private Timestamp guarantorDate;//保证时间
	private String aGuarantor;//承兑保证人
	private String aGuarantorAcc;//承兑保证人账号
	private String aGuarantorNo;//承兑保证人开户行行号
	private String aGuarantorAddr;//承兑保证人地址
	private Timestamp aGuarantorDate;//承兑保证时间
	private String eGuarantor;//背书保证人
	private String eGuarantorAcc;//背书保证人账号
	private String eGuarantorNo;//背书保证人开户行行号
	private Timestamp eGuarantorDate;//背书保证时间
	private Long payId;	// 付款单id(ERP_PAY_INFO表主键)
	private String preHand;//前手
	private String payStatus; //票据付款状态
	private String guaranteeMode;//担保方式
	private Integer trueFlag;//是否真票
	private Timestamp drwgbckdt;//未用退回日期
	private String newStmIssuofpprInd = "0";//		出票标志。0：老系统出票,   1：新系统出票
	private String blgrdbagcrclPrmtInd = "0";//		是否允许分包。1 是  0 否
	private String sbbllRngStrtSn = "0";//		子票区间开始序号。旧票据默认0，新票据 12位长度，不足12位前面补0。
	private String sbbllRngEndSn = "0";//				子票区间结束序号。旧票据默认0，新票据 12位长度，不足12位前面补0。
	private BigDecimal blStdAmt = new BigDecimal("0.01");//			标准金额。单位元.旧票据无，新票据默认为0.01
	private String blRmtrUnnIdNo;//			出票人统一信用代码
	private String blAcptrUnnIdNo;//			承兑人统一信用代码
	private String blRcvpymtpsUnnIdNo;//			收款人统一信用代码
	private String blAcptrIdTpcd;//			承兑人识别类型。DT01 票据账户DT02 银行账户
	private String blRcvpymtpsIdTpcd;//			收款人人识别类型。DT01 票据账户DT02
	private String ebillBlCrclIndc;//			票据流通标志（主表）。
	private String cooperativeBankNo; //合作银行号
	private String blBsnTpcd;//票据业务类型代码
	/**
	 * 流水表
	 */
	private String recordId; //流水ID
	private String vvid;//从表ID
	private String briefId; // 流水业务类型
	private String bifCode;//银行接口代码
	private String returnCode;//返回代码
	private String returnMsg;//返回消息
	private Timestamp tallyDate;//记账日期
	private String coptorId;//单位记账人
	private String contractNo;//业务合同号
	private String signBank;//签收行
	private String signMan;//签收人
	private Timestamp applyDate;//申请日期
	private String summary;//摘要
	private Integer budgetSign;//预算标志  10=没有挂预算、11=挂预算
	private Timestamp discountDate;//贴现日期
	private Integer daynum;//贴现天数
	private BigDecimal interestRates;//贴现利率
	private BigDecimal interest;//贴现利息
	private String lsno;//业务流水号
	private String userno;//柜员流水号
	private Integer discountType;//是否为同城  10=是、11=不是、12=是转贴现
	private String uuid;//审批id
	private String acctId;// 申请发起方账号
	private String acctAcctsvcr;// 申请发起方行号
	private String agcyAcctsvcr;//承接行号
	private String rpdmk;//贴现种类  RM00=买断式、RM01=回购式
	private Timestamp bussdt;//业务申请日期
	private String bankendrsmtmk;//不得转让标记 EM00=可再转让、EM01=不得转让
	private String sttlmmk;//线上清算标记  SM00=开启线上清算、SM01=关闭线上清算
	private Timestamp rpdopendt;//赎回开放日
	private Timestamp rpdduedt;//赎回截止日
	private BigDecimal rpdintrstrate;//申请赎回利率
	private BigDecimal rpdamt;//赎回或实付金额
	private String invcnb;//发票号码
	private BigDecimal rate;//实际赎回利率
	private Timestamp appldt;//通知日期
	private String rsn;//原因说明
	private String backacctId;// 业务接收方账号
	private String backacctAcctsvcr;// 业务接收方行号
	private String rcrsTp;//追索种类 RT00=拒付追索、RT01=非拒付追索
	private String rcrsrsncd;//追索理由代码
	private String msgid;//报文id
	private Timestamp msgcredttm;//报文发送时间
	private Timestamp nexttime;// 发起一笔新申请类业务时，把当前流水的nexttime赋值，把新生成的流水nexttime保持为空。
	private Integer status;//操作状态
	private String msgtype;//报文类型
	private String lastid;//上条流水id 新流水需要记录之前的record_id。(申请类业务)
	private String reqSocNo;	 //		业务申请人社会统一信用代码
	private String recSocNo;	 //		业务接收人社会统一信用代码
	private String recDghType; //		业务接收人识别类型。Dt01 票据账户dt02 银行账户默认为dt02 “银行账户”
	private Date settledate;	 //		结息日期
	private String settletype;	 //	清算类型：Ct01 全额清算，Ct02净额清算，默全额清算
	//保证金账号
	private String bailAcc;
	private BigDecimal bailmoney;//保证金
	private BigDecimal bailrate;//保证金利率
	private BigDecimal bailpercent;//保证金比例
	
	//保存金存出账号
	private String outBankAcc; 
	
	//保存金存出账号户名
	private String outBankAccName; 
	private BigDecimal feemoney;//手续费
	private Integer bail;//是否保证  10=否、11=是
	private Long lastsysSubject;//上个科目  新流水需要记录之前的科目
	private String transfer;//是否可转让  10=是、11=否 不得转让标记：EM00 可再转让；EM01 不得转让
	private Integer paydate;//划款天数
	private Integer deferdate;//延迟天数
	private Integer contractFlag;//是否生成合同号  1=生成、其它=未生成
	private String signUpFlag;// 签收标记，SU00：同意，SU01：拒绝
	private String checkName;//看票人名
	private String checkBankacc;//看票人账号/检验账号
	private String checkBank;//看票人开户行
	private Integer endorsementCount;//背书次数
	private String outgolosttype;//止付类型  SP00=挂失止付、SP01=公示催告止付
	private String receivebizId;//关联签收id 新流水需要记录e_draft_bill_receivebiz表的主键。(签收类业务)
	private BigDecimal buyerRatio;//买方付息比率
	private BigDecimal buyerInterest;//买方付息金额
	private Integer isOnestep;//是否一步完成出票登记、出票保证、提示承兑操作  1=是、-1=否
	private String lastbillstatus;//上笔票据的业务状态   新流水需要记录之前的票据状态
	private String crmrkbypropsr;//贴现人备注
	private String guarantorBanktype;//保证人银行类别
	private String remark;//保证人备注
	private String payeeBanktype;//
	private String guarantorBankname;//保证人开户行名称
	private String ucondlprmsmrk;//到期无条件支付委托
	private String paytype;//清算方式 SM00=线上清算、SM01=线下清算
	private String payratetype;//付息方式  1=卖方付息、2=买方付息
	private String busaccid;//业务申请账号
	private String iscib;//是否同行
	private String redmtype;//赎回类型
	private String guarnttype;//
	private String payinacc;//承清算入账账号

	private String payinaccname;//承清算入账账号名称

	private String payinbankno;//清算入账开户行
	private String paymenttype;//提示付款类型  0=全部、1=正常、2=逾期
	private String businessSign;//业务标识号
	private String tradeType;// 业务类型
	private String applysign;//代理申请标识
	private String biztyp;//质押类型
	private String collztnacctid;//质押/解除质押人账号
	private String ccy;//币种
	private String rechMoney;//提示付款金额
	private BigDecimal money;// 票据金额
	private String querySource;//用于判断数据来源 0=数据来源于流水表、1=数据来源于网银历史表
	private String corpId;//组织id
	private String mobile; //手机号码
	private String acctName;//业务发起方名称
	private String backAcctName;//业务接收人名称
	private String backAcctBankName;//业务接收人银行名称
	private String autoAcceptance;//自动提示承兑标志  0=提示、1=不提示）
	private String autoIssuse;//自动提示收票标志 0=提示、1=不提示）
	private String bankserialno;//网银流水号
	private String acctBankName;//业务发起方银行名称
	private String lastBriefId;//上条流水业务类型
	private String bailType;//保证类型:01开票保证，02承兑保证；03背书保证
	private String cmonid;//机构号
	private String accCmonid;//申请账号机构号
	private String adr;//保证人地址
	private String batchNo;//批次号
	private String billId;//外围系统票据ID
    private String pdID;//票据池明细表ID
    private String poolSign;//票据池业务标识 1为是0为否
    private String ratContractNo;//银行授信编号
    private String accptncagrmtnb;//承兑协议号
	private String useType;//票据用途
	private String blHoldUnnIdNo;//票据单位社会统一信用代码
	private String ebillBlCrclIndcd;//票据流通标(签收表)
	private String intpymtprtWay;//付息方式。必填， 1 卖方2 买方3 协议4 他方付息，默认为1
	private String buyerpayintrcntno;//买方付息合作协议编号
	private String intpymtPsnNm;//付息人名称
	private String intpymtPsnAccno;//付息人账号
	private String blRcvUnnIdNo;//业务接收人社会统一信用代码
	private String blReqUnnIdNo;//业务申请人社会统一信用代码
	private String blRcvIdTpcd;//业务接收人识别类型 Dt01 票据账户dt02 银行账户
	private String blReqIdTpcd;//业务申请人识别类型 Dt01 票据账户dt02 银行账
	private String oldDraftId;

	private String oldAccId;

	/*
	1是复制数据2是批量拆分数据
	 */
	private String copyFlag = "0";
	private BigDecimal orgMoney;//原始票据金额
	private String orgSbbllRngStrtSn;//原始票据区间
	private String orgSbbllRngEndSn;//原始票据区间
	private BigDecimal splitMoney;//拆分交易金额


	private String riskBillType; //风险票据类型
	private String disountIncIf; //是否已包贴
	private String cashIncIf; //是否已保兑
	private String avoidClaimDebtsIf; //是否免追索票据
	private String payerChannelName; //出票人办理渠道名称
	private String acceptorChannelName; //承兑人办理渠道名称
	private String payeeChannelName; //收票人办理渠道名称

	/**
	 * 增信信息列表
	 */
	private List<AddCreditInfoVo> addCreditInfoVoList=new ArrayList<AddCreditInfoVo>();

	public String getRiskBillType() {
		return riskBillType;
	}

	public void setRiskBillType(String riskBillType) {
		this.riskBillType = riskBillType;
	}

	public String getDisountIncIf() {
		return disountIncIf;
	}

	public void setDisountIncIf(String disountIncIf) {
		this.disountIncIf = disountIncIf;
	}

	public String getCashIncIf() {
		return cashIncIf;
	}

	public void setCashIncIf(String cashIncIf) {
		this.cashIncIf = cashIncIf;
	}

	public String getAvoidClaimDebtsIf() {
		return avoidClaimDebtsIf;
	}

	public void setAvoidClaimDebtsIf(String avoidClaimDebtsIf) {
		this.avoidClaimDebtsIf = avoidClaimDebtsIf;
	}

	public String getPayerChannelName() {
		return payerChannelName;
	}

	public void setPayerChannelName(String payerChannelName) {
		this.payerChannelName = payerChannelName;
	}

	public String getAcceptorChannelName() {
		return acceptorChannelName;
	}

	public void setAcceptorChannelName(String acceptorChannelName) {
		this.acceptorChannelName = acceptorChannelName;
	}

	public String getPayeeChannelName() {
		return payeeChannelName;
	}

	public void setPayeeChannelName(String payeeChannelName) {
		this.payeeChannelName = payeeChannelName;
	}

	public String getOldDraftId() {
		return oldDraftId;
	}
	public void setOldDraftId(String oldDraftId) {
		this.oldDraftId = oldDraftId;
	}
	public BigDecimal getOrgMoney() {
		return orgMoney;
	}
	public void setOrgMoney(BigDecimal orgMoney) {
		this.orgMoney = orgMoney;
	}
	public String getOrgSbbllRngStrtSn() {
		return orgSbbllRngStrtSn;
	}
	public void setOrgSbbllRngStrtSn(String orgSbbllRngStrtSn) {
		this.orgSbbllRngStrtSn = orgSbbllRngStrtSn;
	}
	public String getOrgSbbllRngEndSn() {
		return orgSbbllRngEndSn;
	}
	public void setOrgSbbllRngEndSn(String orgSbbllRngEndSn) {
		this.orgSbbllRngEndSn = orgSbbllRngEndSn;
	}
	public BigDecimal getSplitMoney() {
		return splitMoney;
	}
	public void setSplitMoney(BigDecimal splitMoney) {
		this.splitMoney = splitMoney;
	}
	public String getBlBsnTpcd() {
		return blBsnTpcd;
	}
	public void setBlBsnTpcd(String blBsnTpcd) {
		this.blBsnTpcd = blBsnTpcd;
	}
	/**
     * 是否经办 0或空为否，1为是
     */
    private String isHandling;
    /**
     * 开票方式：10 票据质押20 保证金30 信用
     */
	private String emitType;//开票方式
	/**
	 * 保证金类型：0活期保证金，1定期保证金
	 */
	private String bailMoneyType;
	
	/**
	 * 开票登记附件路径，多个以分号;为隔开
	 */
	private String attachmentPath;

	/**
	 * 票据附件ID
	 */
	private String attachmentId;

	private String isCreditDraw;
	private String lastCirculateCode;//上一票据流通标志
	private String lastBlBsnTpcd;//上一票据业务类型代码
	public String getLastBlBsnTpcd() {
		return lastBlBsnTpcd;
	}
	public void setLastBlBsnTpcd(String lastBlBsnTpcd) {
		this.lastBlBsnTpcd = lastBlBsnTpcd;
	}
	public String getLastCirculateCode() {
		return lastCirculateCode;
	}
	public void setLastCirculateCode(String lastCirculateCode) {
		this.lastCirculateCode = lastCirculateCode;
	}
	public String getBlHoldUnnIdNo() {
		return blHoldUnnIdNo;
	}
	public void setBlHoldUnnIdNo(String blHoldUnnIdNo) {
		this.blHoldUnnIdNo = blHoldUnnIdNo;
	}
	public String getBlAcptrUnnIdNo() {
		return blAcptrUnnIdNo;
	}
	public void setBlAcptrUnnIdNo(String blAcptrUnnIdNo) {
		this.blAcptrUnnIdNo = blAcptrUnnIdNo;
	}
	public String getBlRcvpymtpsUnnIdNo() {
		return blRcvpymtpsUnnIdNo;
	}
	public void setBlRcvpymtpsUnnIdNo(String blRcvpymtpsUnnIdNo) {
		this.blRcvpymtpsUnnIdNo = blRcvpymtpsUnnIdNo;
	}
	public String getIsCreditDraw() {
		return isCreditDraw;
	}
	public void setIsCreditDraw(String isCreditDraw) {
		this.isCreditDraw = isCreditDraw;
	}
	public String getCooperativeBankNo() {
		return cooperativeBankNo;
	}
	public void setCooperativeBankNo(String cooperativeBankNo) {
		this.cooperativeBankNo = cooperativeBankNo;
	}
	public String getEbillBlCrclIndcd() {
		return ebillBlCrclIndcd;
	}
	public void setEbillBlCrclIndcd(String ebillBlCrclIndcd) {
		this.ebillBlCrclIndcd = ebillBlCrclIndcd;
	}
	public String getIntpymtprtWay() {
		return intpymtprtWay;
	}
	public void setIntpymtprtWay(String intpymtprtWay) {
		this.intpymtprtWay = intpymtprtWay;
	}
	public String getBuyerpayintrcntno() {
		return buyerpayintrcntno;
	}
	public void setBuyerpayintrcntno(String buyerpayintrcntno) {
		this.buyerpayintrcntno = buyerpayintrcntno;
	}
	public String getIntpymtPsnNm() {
		return intpymtPsnNm;
	}
	public void setIntpymtPsnNm(String intpymtPsnNm) {
		this.intpymtPsnNm = intpymtPsnNm;
	}
	public String getIntpymtPsnAccno() {
		return intpymtPsnAccno;
	}
	public void setIntpymtPsnAccno(String intpymtPsnAccno) {
		this.intpymtPsnAccno = intpymtPsnAccno;
	}
	public String getBlRcvUnnIdNo() {
		return blRcvUnnIdNo;
	}
	public void setBlRcvUnnIdNo(String blRcvUnnIdNo) {
		this.blRcvUnnIdNo = blRcvUnnIdNo;
	}
	public String getBlReqUnnIdNo() {
		return blReqUnnIdNo;
	}
	public void setBlReqUnnIdNo(String blReqUnnIdNo) {
		this.blReqUnnIdNo = blReqUnnIdNo;
	}
	public String getBlRcvIdTpcd() {
		return blRcvIdTpcd;
	}
	public void setBlRcvIdTpcd(String blRcvIdTpcd) {
		this.blRcvIdTpcd = blRcvIdTpcd;
	}
	public String getBlReqIdTpcd() {
		return blReqIdTpcd;
	}
	public void setBlReqIdTpcd(String blReqIdTpcd) {
		this.blReqIdTpcd = blReqIdTpcd;
	}

	public String getNewStmIssuofpprInd() {
		if(StringUtil.isBlank_new(newStmIssuofpprInd)
				&& StringUtil.isNotEmpty(origNo)){
			if(origNo.startsWith("1") || origNo.startsWith("2")){
				setNewStmIssuofpprInd("0");
			}else{
				setNewStmIssuofpprInd("1");
			}
		}
		return newStmIssuofpprInd;
	}
	public void setNewStmIssuofpprInd(String newStmIssuofpprInd) {
		this.newStmIssuofpprInd = newStmIssuofpprInd;
	}
	public String getBlgrdbagcrclPrmtInd() {
		return blgrdbagcrclPrmtInd;
	}
	public void setBlgrdbagcrclPrmtInd(String blgrdbagcrclPrmtInd) {
		this.blgrdbagcrclPrmtInd = blgrdbagcrclPrmtInd;
	}
	public String getSbbllRngStrtSn() {
		return sbbllRngStrtSn;
	}
	public void setSbbllRngStrtSn(String sbbllRngStrtSn) {
		this.sbbllRngStrtSn = sbbllRngStrtSn;
	}
	public String getSbbllRngEndSn() {
		return sbbllRngEndSn;
	}
	public void setSbbllRngEndSn(String sbbllRngEndSn) {
		this.sbbllRngEndSn = sbbllRngEndSn;
	}
	public BigDecimal getBlStdAmt() {
		return blStdAmt;
	}
	public void setBlStdAmt(BigDecimal blStdAmt) {
		this.blStdAmt = blStdAmt;
	}
	public String getBlRmtrUnnIdNo() {
		return blRmtrUnnIdNo;
	}
	public void setBlRmtrUnnIdNo(String blRmtrUnnIdNo) {
		this.blRmtrUnnIdNo = blRmtrUnnIdNo;
	}
	public String getBlAcptrIdTpcd() {
		return blAcptrIdTpcd;
	}
	public void setBlAcptrIdTpcd(String blAcptrIdTpcd) {
		this.blAcptrIdTpcd = blAcptrIdTpcd;
	}
	public String getBlRcvpymtpsIdTpcd() {
		return blRcvpymtpsIdTpcd;
	}
	public void setBlRcvpymtpsIdTpcd(String blRcvpymtpsIdTpcd) {
		this.blRcvpymtpsIdTpcd = blRcvpymtpsIdTpcd;
	}
	public String getEbillBlCrclIndc() {
		return ebillBlCrclIndc;
	}
	public void setEbillBlCrclIndc(String ebillBlCrclIndc) {
		this.ebillBlCrclIndc = ebillBlCrclIndc;
	}
	public String getReqSocNo() {
		return reqSocNo;
	}
	public void setReqSocNo(String reqSocNo) {
		this.reqSocNo = reqSocNo;
	}
	public String getRecSocNo() {
		return recSocNo;
	}
	public void setRecSocNo(String recSocNo) {
		this.recSocNo = recSocNo;
	}
	public String getRecDghType() {
		return recDghType;
	}
	public void setRecDghType(String recDghType) {
		this.recDghType = recDghType;
	}
	public Date getSettledate() {
		return settledate;
	}
	public void setSettledate(Date settledate) {
		this.settledate = settledate;
	}
	public String getSettletype() {
		return settletype;
	}
	public void setSettletype(String settletype) {
		this.settletype = settletype;
	}
	public String getUseType() {
		return useType;
	}
	public void setUseType(String useType) {
		this.useType = useType;
	}
	public String getBailMoneyType() {
		return bailMoneyType;
	}
	public void setBailMoneyType(String bailMoneyType) {
		this.bailMoneyType = bailMoneyType;
	}
	public String getRatContractNo() {
		return ratContractNo;
	}
	public void setRatContractNo(String ratContractNo) {
		this.ratContractNo = ratContractNo;
	}
	public String getAdr() {
		return adr;
	}
	public void setAdr(String adr) {
		this.adr = adr;
	}
	public String getAcceptorAcc() {
		return acceptorAcc;
	}
	public void setAcceptorAcc(String acceptorAcc) {
		this.acceptorAcc = acceptorAcc;
	}
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getPayeeBankName() {
		return payeeBankName;
	}
	public void setPayeeBankName(String payeeBankName) {
		this.payeeBankName = payeeBankName;
	}
	public String getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(String voucherId) {
		this.voucherId = voucherId;
	}
	public String getOrigNo() {
		return origNo;
	}
	public void setOrigNo(String origNo) {
		this.origNo = origNo;
	}
	public Integer getBillType() {
		return billType;
	}
	public void setBillType(Integer billType) {
		this.billType = billType;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	public BigDecimal getBillMoney() {
		return billMoney;
	}
	public void setBillMoney(BigDecimal billMoney) {
		this.billMoney = billMoney;
	}
	public String getBanendrsmtmk() {
		return banendrsmtmk;
	}
	public void setBanendrsmtmk(String banendrsmtmk) {
		this.banendrsmtmk = banendrsmtmk;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public String getPayeeAcc() {
		return payeeAcc;
	}
	public void setPayeeAcc(String payeeAcc) {
		this.payeeAcc = payeeAcc;
	}
	public String getPayeeBankNo() {
		return payeeBankNo;
	}
	public void setPayeeBankNo(String payeeBankNo) {
		this.payeeBankNo = payeeBankNo;
	}
	public String getPayer() {
		return payer;
	}
	public void setPayer(String payer) {
		this.payer = payer;
	}
	public String getPayerAcc() {
		return payerAcc;
	}
	public void setPayerAcc(String payerAcc) {
		this.payerAcc = payerAcc;
	}
	public String getPayerBankNo() {
		return payerBankNo;
	}
	public void setPayerBankNo(String payerBankNo) {
		this.payerBankNo = payerBankNo;
	}
	public String getPayerBankName() {
		return payerBankName;
	}
	public void setPayerBankName(String payerBankName) {
		this.payerBankName = payerBankName;
	}
	public String getAcceptor() {
		return acceptor;
	}
	public void setAcceptor(String acceptor) {
		this.acceptor = acceptor;
	}
	public String getAcceptorBankNo() {
		return acceptorBankNo;
	}
	public void setAcceptorBankNo(String acceptorBankNo) {
		this.acceptorBankNo = acceptorBankNo;
	}
	public String getAcceptorBankName() {
		return acceptorBankName;
	}
	public void setAcceptorBankName(String acceptorBankName) {
		this.acceptorBankName = acceptorBankName;
	}
	public Integer getBoxStatus() {
		return boxStatus;
	}
	public void setBoxStatus(Integer boxStatus) {
		this.boxStatus = boxStatus;
	}
	public String getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}
	public Long getSysSubject() {
		return sysSubject;
	}
	public void setSysSubject(Long sysSubject) {
		this.sysSubject = sysSubject;
	}
	public Integer getBillFlag() {
		return billFlag;
	}
	public void setBillFlag(Integer billFlag) {
		this.billFlag = billFlag;
	}
	public String getOpenBill() {
		return openBill;
	}
	public void setOpenBill(String openBill) {
		this.openBill = openBill;
	}
	public Timestamp getAcceptorDate() {
		return acceptorDate;
	}
	public void setAcceptorDate(Timestamp acceptorDate) {
		this.acceptorDate = acceptorDate;
	}
	public String getFlwNo() {
		return flwNo;
	}
	public void setFlwNo(String flwNo) {
		this.flwNo = flwNo;
	}
	public String getBifCode() {
		return bifCode;
	}
	public void setBifCode(String bifCode) {
		this.bifCode = bifCode;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	public String getBriefId() {
		return briefId;
	}
	public void setBriefId(String briefId) {
		this.briefId = briefId;
	}
	public String getPayerName() {
		return payerName;
	}
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	public String getGuarantor() {
		return guarantor;
	}
	public void setGuarantor(String guarantor) {
		this.guarantor = guarantor;
	}
	public String getGuarantorAcc() {
		return guarantorAcc;
	}
	public void setGuarantorAcc(String guarantorAcc) {
		this.guarantorAcc = guarantorAcc;
	}
	public String getGuarantorBankNo() {
		return guarantorBankNo;
	}
	public void setGuarantorBankNo(String guarantorBankNo) {
		this.guarantorBankNo = guarantorBankNo;
	}
	public String getGuarantorBank() {
		return guarantorBank;
	}
	public void setGuarantorBank(String guarantorBank) {
		this.guarantorBank = guarantorBank;
	}
	public String getGuaranttorBankType() {
		return guaranttorBankType;
	}
	public void setGuaranttorBankType(String guaranttorBankType) {
		this.guaranttorBankType = guaranttorBankType;
	}
	public String getGuarantorAddr() {
		return guarantorAddr;
	}
	public void setGuarantorAddr(String guarantorAddr) {
		this.guarantorAddr = guarantorAddr;
	}
	public Timestamp getGuarantorDate() {
		return guarantorDate;
	}
	public void setGuarantorDate(Timestamp guarantorDate) {
		this.guarantorDate = guarantorDate;
	}
	public String getaGuarantor() {
		return aGuarantor;
	}
	public void setaGuarantor(String aGuarantor) {
		this.aGuarantor = aGuarantor;
	}
	public String getaGuarantorAcc() {
		return aGuarantorAcc;
	}
	public void setaGuarantorAcc(String aGuarantorAcc) {
		this.aGuarantorAcc = aGuarantorAcc;
	}
	public String getaGuarantorNo() {
		return aGuarantorNo;
	}
	public void setaGuarantorNo(String aGuarantorNo) {
		this.aGuarantorNo = aGuarantorNo;
	}
	public String getaGuarantorAddr() {
		return aGuarantorAddr;
	}
	public void setaGuarantorAddr(String aGuarantorAddr) {
		this.aGuarantorAddr = aGuarantorAddr;
	}
	public Timestamp getaGuarantorDate() {
		return aGuarantorDate;
	}
	public void setaGuarantorDate(Timestamp aGuarantorDate) {
		this.aGuarantorDate = aGuarantorDate;
	}
	public String geteGuarantor() {
		return eGuarantor;
	}
	public void seteGuarantor(String eGuarantor) {
		this.eGuarantor = eGuarantor;
	}
	public String geteGuarantorAcc() {
		return eGuarantorAcc;
	}
	public void seteGuarantorAcc(String eGuarantorAcc) {
		this.eGuarantorAcc = eGuarantorAcc;
	}
	public String geteGuarantorNo() {
		return eGuarantorNo;
	}
	public void seteGuarantorNo(String eGuarantorNo) {
		this.eGuarantorNo = eGuarantorNo;
	}
	public Timestamp geteGuarantorDate() {
		return eGuarantorDate;
	}
	public void seteGuarantorDate(Timestamp eGuarantorDate) {
		this.eGuarantorDate = eGuarantorDate;
	}
	public Long getPayId() {
		return payId;
	}
	public void setPayId(Long payId) {
		this.payId = payId;
	}
	public String getPreHand() {
		return preHand;
	}
	public void setPreHand(String preHand) {
		this.preHand = preHand;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getGuaranteeMode() {
		return guaranteeMode;
	}
	public void setGuaranteeMode(String guaranteeMode) {
		this.guaranteeMode = guaranteeMode;
	}
	public Integer getTrueFlag() {
		return trueFlag;
	}
	public void setTrueFlag(Integer trueFlag) {
		this.trueFlag = trueFlag;
	}
	public Timestamp getDrwgbckdt() {
		return drwgbckdt;
	}
	public void setDrwgbckdt(Timestamp drwgbckdt) {
		this.drwgbckdt = drwgbckdt;
	}
	public Timestamp getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Timestamp tallyDate) {
		this.tallyDate = tallyDate;
	}
	public String getCoptorId() {
		return coptorId;
	}
	public void setCoptorId(String coptorId) {
		this.coptorId = coptorId;
	}
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	public String getSignBank() {
		return signBank;
	}
	public void setSignBank(String signBank) {
		this.signBank = signBank;
	}
	public String getSignMan() {
		return signMan;
	}
	public void setSignMan(String signMan) {
		this.signMan = signMan;
	}
	public Timestamp getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Timestamp applyDate) {
		this.applyDate = applyDate;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public Integer getBudgetSign() {
		return budgetSign;
	}
	public void setBudgetSign(Integer budgetSign) {
		this.budgetSign = budgetSign;
	}
	public Timestamp getDiscountDate() {
		return discountDate;
	}
	public void setDiscountDate(Timestamp discountDate) {
		this.discountDate = discountDate;
	}
	public Integer getDaynum() {
		return daynum;
	}
	public void setDaynum(Integer daynum) {
		this.daynum = daynum;
	}
	public BigDecimal getInterestRates() {
		return interestRates;
	}
	public void setInterestRates(BigDecimal interestRates) {
		this.interestRates = interestRates;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public String getLsno() {
		return lsno;
	}
	public void setLsno(String lsno) {
		this.lsno = lsno;
	}
	public String getUserno() {
		return userno;
	}
	public void setUserno(String userno) {
		this.userno = userno;
	}
	public Integer getDiscountType() {
		return discountType;
	}
	public void setDiscountType(Integer discountType) {
		this.discountType = discountType;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getAcctId() {
		return acctId;
	}
	public void setAcctId(String acctId) {
		this.acctId = acctId;
	}
	public String getAcctAcctsvcr() {
		return acctAcctsvcr;
	}
	public void setAcctAcctsvcr(String acctAcctsvcr) {
		this.acctAcctsvcr = acctAcctsvcr;
	}
	public String getAgcyAcctsvcr() {
		return agcyAcctsvcr;
	}
	public void setAgcyAcctsvcr(String agcyAcctsvcr) {
		this.agcyAcctsvcr = agcyAcctsvcr;
	}
	public String getRpdmk() {
		return rpdmk;
	}
	public void setRpdmk(String rpdmk) {
		this.rpdmk = rpdmk;
	}
	public Timestamp getBussdt() {
		return bussdt;
	}
	public void setBussdt(Timestamp bussdt) {
		this.bussdt = bussdt;
	}
	public String getBankendrsmtmk() {
		return bankendrsmtmk;
	}
	public void setBankendrsmtmk(String bankendrsmtmk) {
		this.bankendrsmtmk = bankendrsmtmk;
	}
	public String getSttlmmk() {
		return sttlmmk;
	}
	public void setSttlmmk(String sttlmmk) {
		this.sttlmmk = sttlmmk;
	}
	public Timestamp getRpdopendt() {
		return rpdopendt;
	}
	public void setRpdopendt(Timestamp rpdopendt) {
		this.rpdopendt = rpdopendt;
	}
	public Timestamp getRpdduedt() {
		return rpdduedt;
	}
	public void setRpdduedt(Timestamp rpdduedt) {
		this.rpdduedt = rpdduedt;
	}
	public BigDecimal getRpdintrstrate() {
		return rpdintrstrate;
	}
	public void setRpdintrstrate(BigDecimal rpdintrstrate) {
		this.rpdintrstrate = rpdintrstrate;
	}
	public BigDecimal getRpdamt() {
		return rpdamt;
	}
	public void setRpdamt(BigDecimal rpdamt) {
		this.rpdamt = rpdamt;
	}
	public String getInvcnb() {
		return invcnb;
	}
	public void setInvcnb(String invcnb) {
		this.invcnb = invcnb;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public Timestamp getAppldt() {
		return appldt;
	}
	public void setAppldt(Timestamp appldt) {
		this.appldt = appldt;
	}
	public String getRsn() {
		return rsn;
	}
	public void setRsn(String rsn) {
		this.rsn = rsn;
	}
	public String getBackacctId() {
		return backacctId;
	}
	public void setBackacctId(String backacctId) {
		this.backacctId = backacctId;
	}
	public String getBackacctAcctsvcr() {
		return backacctAcctsvcr;
	}
	public void setBackacctAcctsvcr(String backacctAcctsvcr) {
		this.backacctAcctsvcr = backacctAcctsvcr;
	}
	public String getRcrsTp() {
		return rcrsTp;
	}
	public void setRcrsTp(String rcrsTp) {
		this.rcrsTp = rcrsTp;
	}
	public String getMsgid() {
		return msgid;
	}
	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}
	public Timestamp getMsgcredttm() {
		return msgcredttm;
	}
	public void setMsgcredttm(Timestamp msgcredttm) {
		this.msgcredttm = msgcredttm;
	}
	public Timestamp getNexttime() {
		return nexttime;
	}
	public void setNexttime(Timestamp nexttime) {
		this.nexttime = nexttime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMsgtype() {
		return msgtype;
	}
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}
	public String getLastid() {
		return lastid;
	}
	public void setLastid(String lastid) {
		this.lastid = lastid;
	}
	public String getBailAcc() {
		return bailAcc;
	}
	public void setBailAcc(String bailAcc) {
		this.bailAcc = bailAcc;
	}
	public BigDecimal getBailmoney() {
		return bailmoney;
	}
	public void setBailmoney(BigDecimal bailmoney) {
		this.bailmoney = bailmoney;
	}
	public BigDecimal getBailpercent() {
		return bailpercent;
	}
	public void setBailpercent(BigDecimal bailpercent) {
		this.bailpercent = bailpercent;
	}
	public String getOutBankAcc() {
		return outBankAcc;
	}
	public void setOutBankAcc(String outBankAcc) {
		this.outBankAcc = outBankAcc;
	}
	public String getOutBankAccName() {
		return outBankAccName;
	}
	public void setOutBankAccName(String outBankAccName) {
		this.outBankAccName = outBankAccName;
	}
	public BigDecimal getFeemoney() {
		return feemoney;
	}
	public void setFeemoney(BigDecimal feemoney) {
		this.feemoney = feemoney;
	}
	public Integer getBail() {
		return bail;
	}
	public void setBail(Integer bail) {
		this.bail = bail;
	}
	public Long getLastsysSubject() {
		return lastsysSubject;
	}
	public void setLastsysSubject(Long lastsysSubject) {
		this.lastsysSubject = lastsysSubject;
	}
	public String getTransfer() {
		return transfer;
	}
	public void setTransfer(String transfer) {
		this.transfer = transfer;
	}
	public Integer getPaydate() {
		return paydate;
	}
	public void setPaydate(Integer paydate) {
		this.paydate = paydate;
	}
	public Integer getDeferdate() {
		return deferdate;
	}
	public void setDeferdate(Integer deferdate) {
		this.deferdate = deferdate;
	}
	public Integer getContractFlag() {
		return contractFlag;
	}
	public void setContractFlag(Integer contractFlag) {
		this.contractFlag = contractFlag;
	}
	public String getSignUpFlag() {
		return signUpFlag;
	}
	public void setSignUpFlag(String signUpFlag) {
		this.signUpFlag = signUpFlag;
	}
	public String getCheckName() {
		return checkName;
	}
	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}
	public String getCheckBankacc() {
		return checkBankacc;
	}
	public void setCheckBankacc(String checkBankacc) {
		this.checkBankacc = checkBankacc;
	}
	public String getCheckBank() {
		return checkBank;
	}
	public void setCheckBank(String checkBank) {
		this.checkBank = checkBank;
	}
	public Integer getEndorsementCount() {
		return endorsementCount;
	}
	public void setEndorsementCount(Integer endorsementCount) {
		this.endorsementCount = endorsementCount;
	}
	public String getOutgolosttype() {
		return outgolosttype;
	}
	public void setOutgolosttype(String outgolosttype) {
		this.outgolosttype = outgolosttype;
	}
	public String getReceivebizId() {
		return receivebizId;
	}
	public void setReceivebizId(String receivebizId) {
		this.receivebizId = receivebizId;
	}
	public BigDecimal getBuyerRatio() {
		return buyerRatio;
	}
	public void setBuyerRatio(BigDecimal buyerRatio) {
		this.buyerRatio = buyerRatio;
	}
	public BigDecimal getBuyerInterest() {
		return buyerInterest;
	}
	public void setBuyerInterest(BigDecimal buyerInterest) {
		this.buyerInterest = buyerInterest;
	}
	public Integer getIsOnestep() {
		return isOnestep;
	}
	public void setIsOnestep(Integer isOnestep) {
		this.isOnestep = isOnestep;
	}
	public String getLastbillstatus() {
		return lastbillstatus;
	}
	public void setLastbillstatus(String lastbillstatus) {
		this.lastbillstatus = lastbillstatus;
	}
	public String getCrmrkbypropsr() {
		return crmrkbypropsr;
	}
	public void setCrmrkbypropsr(String crmrkbypropsr) {
		this.crmrkbypropsr = crmrkbypropsr;
	}
	public String getGuarantorBanktype() {
		return guarantorBanktype;
	}
	public void setGuarantorBanktype(String guarantorBanktype) {
		this.guarantorBanktype = guarantorBanktype;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPayeeBanktype() {
		return payeeBanktype;
	}
	public void setPayeeBanktype(String payeeBanktype) {
		this.payeeBanktype = payeeBanktype;
	}
	public String getGuarantorBankname() {
		return guarantorBankname;
	}
	public void setGuarantorBankname(String guarantorBankname) {
		this.guarantorBankname = guarantorBankname;
	}
	public String getUcondlprmsmrk() {
		return ucondlprmsmrk;
	}
	public void setUcondlprmsmrk(String ucondlprmsmrk) {
		this.ucondlprmsmrk = ucondlprmsmrk;
	}
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	public String getPayratetype() {
		return payratetype;
	}
	public void setPayratetype(String payratetype) {
		this.payratetype = payratetype;
	}
	public String getBusaccid() {
		return busaccid;
	}
	public void setBusaccid(String busaccid) {
		this.busaccid = busaccid;
	}
	public String getIscib() {
		return iscib;
	}
	public void setIscib(String iscib) {
		this.iscib = iscib;
	}
	public String getRedmtype() {
		return redmtype;
	}
	public void setRedmtype(String redmtype) {
		this.redmtype = redmtype;
	}
	public String getGuarnttype() {
		return guarnttype;
	}
	public void setGuarnttype(String guarnttype) {
		this.guarnttype = guarnttype;
	}
	public String getPayinacc() {
		return payinacc;
	}
	public void setPayinacc(String payinacc) {
		this.payinacc = payinacc;
	}
	public String getPayinbankno() {
		return payinbankno;
	}
	public void setPayinbankno(String payinbankno) {
		this.payinbankno = payinbankno;
	}
	public String getPaymenttype() {
		return paymenttype;
	}
	public void setPaymenttype(String paymenttype) {
		this.paymenttype = paymenttype;
	}
	public String getBusinessSign() {
		return businessSign;
	}
	public void setBusinessSign(String businessSign) {
		this.businessSign = businessSign;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getApplysign() {
		return applysign;
	}
	public void setApplysign(String applysign) {
		this.applysign = applysign;
	}
	public String getBiztyp() {
		return biztyp;
	}
	public void setBiztyp(String biztyp) {
		this.biztyp = biztyp;
	}
	public String getCollztnacctid() {
		return collztnacctid;
	}
	public void setCollztnacctid(String collztnacctid) {
		this.collztnacctid = collztnacctid;
	}
	public String getCcy() {
		return ccy;
	}
	public void setCcy(String ccy) {
		this.ccy = ccy;
	}
	public String getRechMoney() {
		return rechMoney;
	}
	public void setRechMoney(String rechMoney) {
		this.rechMoney = rechMoney;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public String getQuerySource() {
		return querySource;
	}
	public void setQuerySource(String querySource) {
		this.querySource = querySource;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAcctName() {
		return acctName;
	}
	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}
	public String getBackAcctName() {
		return backAcctName;
	}
	public void setBackAcctName(String backAcctName) {
		this.backAcctName = backAcctName;
	}
	public String getBackAcctBankName() {
		return backAcctBankName;
	}
	public void setBackAcctBankName(String backAcctBankName) {
		this.backAcctBankName = backAcctBankName;
	}
	public String getAutoAcceptance() {
		return autoAcceptance;
	}
	public void setAutoAcceptance(String autoAcceptance) {
		this.autoAcceptance = autoAcceptance;
	}
	public String getAutoIssuse() {
		return autoIssuse;
	}
	public void setAutoIssuse(String autoIssuse) {
		this.autoIssuse = autoIssuse;
	}
	public String getBankserialno() {
		return bankserialno;
	}
	public void setBankserialno(String bankserialno) {
		this.bankserialno = bankserialno;
	}
	public String getAcctBankName() {
		return acctBankName;
	}
	public void setAcctBankName(String acctBankName) {
		this.acctBankName = acctBankName;
	}
	public String getLastBriefId() {
		return lastBriefId;
	}
	public void setLastBriefId(String lastBriefId) {
		this.lastBriefId = lastBriefId;
	}
	public String getRcrsrsncd() {
		return rcrsrsncd;
	}
	public void setRcrsrsncd(String rcrsrsncd) {
		this.rcrsrsncd = rcrsrsncd;
	}
	public String getBailType() {
		return bailType;
	}
	public void setBailType(String bailType) {
		this.bailType = bailType;
	}
	public String getCmonid() {
		return cmonid;
	}
	public void setCmonid(String cmonid) {
		this.cmonid = cmonid;
	}
	public String getBatchNo() {
		if(StringUtil.isEmpty(batchNo)){
			setBatchNo(getContractNo());
		}
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getAcceptorBankAddress() {
		return acceptorBankAddress;
	}
	public void setAcceptorBankAddress(String acceptorBankAddress) {
		this.acceptorBankAddress = acceptorBankAddress;
	}
	public String getVvid() {
		return vvid;
	}
	public void setVvid(String vvid) {
		this.vvid = vvid;
	}
	public String getAccCmonid() {
		return accCmonid;
	}
	public void setAccCmonid(String accCmonid) {
		this.accCmonid = accCmonid;
	}
	public String getPdID() {
		return pdID;
	}
	public void setPdID(String pdID) {
		this.pdID = pdID;
	}
	public String getPoolSign() {
		//池ID和开票方式为质押开票则为票据池业务
		if(StringUtil.isNotEmpty(getPdID())|| AccebillConstant.AB_EMIT_TYPE_10.equals(getEmitType())){
			setPoolSign("1");
		}else{
			setPoolSign("0");
		}
		return poolSign;
	}
	public void setPoolSign(String poolSign) {
		this.poolSign = poolSign;
	}
	public String getAccptncagrmtnb() {
		return accptncagrmtnb;
	}
	public void setAccptncagrmtnb(String accptncagrmtnb) {
		this.accptncagrmtnb = accptncagrmtnb;
	}
	public String getEmitType() {
		return emitType;
	}
	public void setEmitType(String emitType) {
		this.emitType = emitType;
	}
	
	
	/**
     * 是否经办 0或空为否，1为是
     */
	public String getIsHandling() {
		return isHandling;
	}
	public void setIsHandling(String isHandling) {
		this.isHandling = isHandling;
	}
	public BigDecimal getBailrate() {
		return bailrate;
	}
	public void setBailrate(BigDecimal bailrate) {
		this.bailrate = bailrate;
	}
	/**
	 * 开票登记附件路径，多个以分号;为隔开
	 * @return
	 */
	public String getAttachmentPath() {
		return attachmentPath;
	}
	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}
	/**
	 * 手续费比例
	 */
	private BigDecimal feemoneyScale;
	public BigDecimal getFeemoneyScale() {
		return feemoneyScale;
	}
	public void setFeemoneyScale(BigDecimal feemoneyScale) {
		this.feemoneyScale = feemoneyScale;
	}

	public List<AddCreditInfoVo> getAddCreditInfoVoList() {
		return addCreditInfoVoList;
	}

	public void setAddCreditInfoVoList(List<AddCreditInfoVo> addCreditInfoVoList) {
		this.addCreditInfoVoList = addCreditInfoVoList;
	}

	public String getOldAccId() {
		return oldAccId;
	}

	public void setOldAccId(String oldAccId) {
		this.oldAccId = oldAccId;
	}

	public String getCopyFlag() {
		return copyFlag;
	}

	public void setCopyFlag(String copyFlag) {
		this.copyFlag = copyFlag;
	}

	public String getPayinaccname() {
		return payinaccname;
	}

	public void setPayinaccname(String payinaccname) {
		this.payinaccname = payinaccname;
	}

	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	@Override
	public String toString() {
		return "VoucherInfoVo{" +
				"voucherId='" + voucherId + '\'' +
				", origNo='" + origNo + '\'' +
				", billType=" + billType +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", billMoney=" + billMoney +
				", banendrsmtmk='" + banendrsmtmk + '\'' +
				", payee='" + payee + '\'' +
				", payeeAcc='" + payeeAcc + '\'' +
				", payeeBankName='" + payeeBankName + '\'' +
				", payeeBankNo='" + payeeBankNo + '\'' +
				", payer='" + payer + '\'' +
				", payerAcc='" + payerAcc + '\'' +
				", payerBankNo='" + payerBankNo + '\'' +
				", payerBankName='" + payerBankName + '\'' +
				", acceptor='" + acceptor + '\'' +
				", acceptorAcc='" + acceptorAcc + '\'' +
				", acceptorBankNo='" + acceptorBankNo + '\'' +
				", acceptorBankName='" + acceptorBankName + '\'' +
				", acceptorBankAddress='" + acceptorBankAddress + '\'' +
				", boxStatus=" + boxStatus +
				", billStatus='" + billStatus + '\'' +
				", sysSubject=" + sysSubject +
				", billFlag=" + billFlag +
				", openBill='" + openBill + '\'' +
				", acceptorDate=" + acceptorDate +
				", flwNo='" + flwNo + '\'' +
				", payerName='" + payerName + '\'' +
				", guarantor='" + guarantor + '\'' +
				", guarantorAcc='" + guarantorAcc + '\'' +
				", guarantorBankNo='" + guarantorBankNo + '\'' +
				", guarantorBank='" + guarantorBank + '\'' +
				", guaranttorBankType='" + guaranttorBankType + '\'' +
				", guarantorAddr='" + guarantorAddr + '\'' +
				", guarantorDate=" + guarantorDate +
				", aGuarantor='" + aGuarantor + '\'' +
				", aGuarantorAcc='" + aGuarantorAcc + '\'' +
				", aGuarantorNo='" + aGuarantorNo + '\'' +
				", aGuarantorAddr='" + aGuarantorAddr + '\'' +
				", aGuarantorDate=" + aGuarantorDate +
				", eGuarantor='" + eGuarantor + '\'' +
				", eGuarantorAcc='" + eGuarantorAcc + '\'' +
				", eGuarantorNo='" + eGuarantorNo + '\'' +
				", eGuarantorDate=" + eGuarantorDate +
				", payId=" + payId +
				", preHand='" + preHand + '\'' +
				", payStatus='" + payStatus + '\'' +
				", guaranteeMode='" + guaranteeMode + '\'' +
				", trueFlag=" + trueFlag +
				", drwgbckdt=" + drwgbckdt +
				", newStmIssuofpprInd='" + newStmIssuofpprInd + '\'' +
				", blgrdbagcrclPrmtInd='" + blgrdbagcrclPrmtInd + '\'' +
				", sbbllRngStrtSn='" + sbbllRngStrtSn + '\'' +
				", sbbllRngEndSn='" + sbbllRngEndSn + '\'' +
				", blStdAmt=" + blStdAmt +
				", blRmtrUnnIdNo='" + blRmtrUnnIdNo + '\'' +
				", blAcptrUnnIdNo='" + blAcptrUnnIdNo + '\'' +
				", blRcvpymtpsUnnIdNo='" + blRcvpymtpsUnnIdNo + '\'' +
				", blAcptrIdTpcd='" + blAcptrIdTpcd + '\'' +
				", blRcvpymtpsIdTpcd='" + blRcvpymtpsIdTpcd + '\'' +
				", ebillBlCrclIndc='" + ebillBlCrclIndc + '\'' +
				", cooperativeBankNo='" + cooperativeBankNo + '\'' +
				", blBsnTpcd='" + blBsnTpcd + '\'' +
				", recordId='" + recordId + '\'' +
				", vvid='" + vvid + '\'' +
				", briefId='" + briefId + '\'' +
				", bifCode='" + bifCode + '\'' +
				", returnCode='" + returnCode + '\'' +
				", returnMsg='" + returnMsg + '\'' +
				", tallyDate=" + tallyDate +
				", coptorId='" + coptorId + '\'' +
				", contractNo='" + contractNo + '\'' +
				", signBank='" + signBank + '\'' +
				", signMan='" + signMan + '\'' +
				", applyDate=" + applyDate +
				", summary='" + summary + '\'' +
				", budgetSign=" + budgetSign +
				", discountDate=" + discountDate +
				", daynum=" + daynum +
				", interestRates=" + interestRates +
				", interest=" + interest +
				", lsno='" + lsno + '\'' +
				", userno='" + userno + '\'' +
				", discountType=" + discountType +
				", uuid='" + uuid + '\'' +
				", acctId='" + acctId + '\'' +
				", acctAcctsvcr='" + acctAcctsvcr + '\'' +
				", agcyAcctsvcr='" + agcyAcctsvcr + '\'' +
				", rpdmk='" + rpdmk + '\'' +
				", bussdt=" + bussdt +
				", bankendrsmtmk='" + bankendrsmtmk + '\'' +
				", sttlmmk='" + sttlmmk + '\'' +
				", rpdopendt=" + rpdopendt +
				", rpdduedt=" + rpdduedt +
				", rpdintrstrate=" + rpdintrstrate +
				", rpdamt=" + rpdamt +
				", invcnb='" + invcnb + '\'' +
				", rate=" + rate +
				", appldt=" + appldt +
				", rsn='" + rsn + '\'' +
				", backacctId='" + backacctId + '\'' +
				", backacctAcctsvcr='" + backacctAcctsvcr + '\'' +
				", rcrsTp='" + rcrsTp + '\'' +
				", rcrsrsncd='" + rcrsrsncd + '\'' +
				", msgid='" + msgid + '\'' +
				", msgcredttm=" + msgcredttm +
				", nexttime=" + nexttime +
				", status=" + status +
				", msgtype='" + msgtype + '\'' +
				", lastid='" + lastid + '\'' +
				", reqSocNo='" + reqSocNo + '\'' +
				", recSocNo='" + recSocNo + '\'' +
				", recDghType='" + recDghType + '\'' +
				", settledate=" + settledate +
				", settletype='" + settletype + '\'' +
				", bailAcc='" + bailAcc + '\'' +
				", bailmoney=" + bailmoney +
				", bailrate=" + bailrate +
				", bailpercent=" + bailpercent +
				", outBankAcc='" + outBankAcc + '\'' +
				", outBankAccName='" + outBankAccName + '\'' +
				", feemoney=" + feemoney +
				", bail=" + bail +
				", lastsysSubject=" + lastsysSubject +
				", transfer='" + transfer + '\'' +
				", paydate=" + paydate +
				", deferdate=" + deferdate +
				", contractFlag=" + contractFlag +
				", signUpFlag='" + signUpFlag + '\'' +
				", checkName='" + checkName + '\'' +
				", checkBankacc='" + checkBankacc + '\'' +
				", checkBank='" + checkBank + '\'' +
				", endorsementCount=" + endorsementCount +
				", outgolosttype='" + outgolosttype + '\'' +
				", receivebizId='" + receivebizId + '\'' +
				", buyerRatio=" + buyerRatio +
				", buyerInterest=" + buyerInterest +
				", isOnestep=" + isOnestep +
				", lastbillstatus='" + lastbillstatus + '\'' +
				", crmrkbypropsr='" + crmrkbypropsr + '\'' +
				", guarantorBanktype='" + guarantorBanktype + '\'' +
				", remark='" + remark + '\'' +
				", payeeBanktype='" + payeeBanktype + '\'' +
				", guarantorBankname='" + guarantorBankname + '\'' +
				", ucondlprmsmrk='" + ucondlprmsmrk + '\'' +
				", paytype='" + paytype + '\'' +
				", payratetype='" + payratetype + '\'' +
				", busaccid='" + busaccid + '\'' +
				", iscib='" + iscib + '\'' +
				", redmtype='" + redmtype + '\'' +
				", guarnttype='" + guarnttype + '\'' +
				", payinacc='" + payinacc + '\'' +
				", payinaccname='" + payinaccname + '\'' +
				", payinbankno='" + payinbankno + '\'' +
				", paymenttype='" + paymenttype + '\'' +
				", businessSign='" + businessSign + '\'' +
				", tradeType='" + tradeType + '\'' +
				", applysign='" + applysign + '\'' +
				", biztyp='" + biztyp + '\'' +
				", collztnacctid='" + collztnacctid + '\'' +
				", ccy='" + ccy + '\'' +
				", rechMoney='" + rechMoney + '\'' +
				", money=" + money +
				", querySource='" + querySource + '\'' +
				", corpId='" + corpId + '\'' +
				", mobile='" + mobile + '\'' +
				", acctName='" + acctName + '\'' +
				", backAcctName='" + backAcctName + '\'' +
				", backAcctBankName='" + backAcctBankName + '\'' +
				", autoAcceptance='" + autoAcceptance + '\'' +
				", autoIssuse='" + autoIssuse + '\'' +
				", bankserialno='" + bankserialno + '\'' +
				", acctBankName='" + acctBankName + '\'' +
				", lastBriefId='" + lastBriefId + '\'' +
				", bailType='" + bailType + '\'' +
				", cmonid='" + cmonid + '\'' +
				", accCmonid='" + accCmonid + '\'' +
				", adr='" + adr + '\'' +
				", batchNo='" + batchNo + '\'' +
				", billId='" + billId + '\'' +
				", pdID='" + pdID + '\'' +
				", poolSign='" + poolSign + '\'' +
				", ratContractNo='" + ratContractNo + '\'' +
				", accptncagrmtnb='" + accptncagrmtnb + '\'' +
				", useType='" + useType + '\'' +
				", blHoldUnnIdNo='" + blHoldUnnIdNo + '\'' +
				", ebillBlCrclIndcd='" + ebillBlCrclIndcd + '\'' +
				", intpymtprtWay='" + intpymtprtWay + '\'' +
				", buyerpayintrcntno='" + buyerpayintrcntno + '\'' +
				", intpymtPsnNm='" + intpymtPsnNm + '\'' +
				", intpymtPsnAccno='" + intpymtPsnAccno + '\'' +
				", blRcvUnnIdNo='" + blRcvUnnIdNo + '\'' +
				", blReqUnnIdNo='" + blReqUnnIdNo + '\'' +
				", blRcvIdTpcd='" + blRcvIdTpcd + '\'' +
				", blReqIdTpcd='" + blReqIdTpcd + '\'' +
				", oldDraftId='" + oldDraftId + '\'' +
				", oldAccId='" + oldAccId + '\'' +
				", copyFlag='" + copyFlag + '\'' +
				", orgMoney=" + orgMoney +
				", orgSbbllRngStrtSn='" + orgSbbllRngStrtSn + '\'' +
				", orgSbbllRngEndSn='" + orgSbbllRngEndSn + '\'' +
				", splitMoney=" + splitMoney +
				", riskBillType='" + riskBillType + '\'' +
				", disountIncIf='" + disountIncIf + '\'' +
				", cashIncIf='" + cashIncIf + '\'' +
				", avoidClaimDebtsIf='" + avoidClaimDebtsIf + '\'' +
				", payerChannelName='" + payerChannelName + '\'' +
				", acceptorChannelName='" + acceptorChannelName + '\'' +
				", payeeChannelName='" + payeeChannelName + '\'' +
				", addCreditInfoVoList=" + addCreditInfoVoList +
				", isHandling='" + isHandling + '\'' +
				", emitType='" + emitType + '\'' +
				", bailMoneyType='" + bailMoneyType + '\'' +
				", attachmentPath='" + attachmentPath + '\'' +
				", attachmentId='" + attachmentId + '\'' +
				", isCreditDraw='" + isCreditDraw + '\'' +
				", lastCirculateCode='" + lastCirculateCode + '\'' +
				", lastBlBsnTpcd='" + lastBlBsnTpcd + '\'' +
				", feemoneyScale=" + feemoneyScale +
				'}';
	}
}
