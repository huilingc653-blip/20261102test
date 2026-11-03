package com.bytter.bill.domain.billbank.vo;
import com.bytter.bill.domain.billbank.util.EDraftConstant;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
/**
 * <br>
 * <b>功能：</b>业务签收实体<br>
 * <b>作者：</b>郑鸿杰<br>
 * <b>日期：</b> 2017-06-20 <br>
 */
public class BillSignVo implements Serializable{
	private static final long serialVersionUID = 4834312035567516451L;
	private String id;
	private String acceptor; // 承兑人
	private String acceptoracc;// 承兑人账号
	private String acceptorbankno;// 承兑人行户行行号

	private String acceptorBankName;// 出票人账号行名
	private String accptncagrmtnb;// 承兑协议编号 ？
	private String adr;// 保证人地址 ？
	private BigDecimal amt;// 贴现金额
	private String aoaccninfAc;// 入账账号行号
	private String aoaccninfId;// 入账账号
	private Date appldt;// 追索通知日期
	private String bankendrsmtmk;// 不得转让标记
	private BigDecimal billMoney;// 票据金额
	private String billType;// 票据类型
	private String btchnb;// 批次号 ？
	private Timestamp bussdt;// 业务申请日期 背书，贴现等
	private String cdtratgagcy;// 评级机构 ？
	private Date cdtratgduedt;// 评级到期日 ？
	private String cdtratgs;// 信用等级 ？
	private String dptymk;// 是否代客户回复 商票提示付款回复使用 ？
	private String dshnrcd;// 拒付理由代码？
	private String dshnrrsn;// 拒付理由备注？
	private Date startDate;// 票据开始日期
	private Date endDate;// 票据到期日期
	private BigDecimal intrstrate;// 贴现利率 转贴现利率 再贴现利率
	private String invcnb;// 发票号码
	private Timestamp msdt;// 报文时间
	private String msgid;// 报文ID
	private String msgtype;// 报文类型
	
	private Date orgnlmsdt;// 前一报文时间
	private String orgnlmsgid;// 前一报文ID
	private String origNo;// 票据号码
	private String payee;// 收款人
	private String payeeacc;// 收款人账号
	private String payeebankno;// 收款人账号行号
	
	private String payeeBankName;// 收款人账号行名
	private String payer;// 出票人
	private String payeracc;// 出票人账号
	
	private String payerbankno;// 出票人账号行号
	
	private String payerBankName;// 出票人账号行名
	private String prccd;// 确认码 ?
	private String prcmsg;// 确认信息 ?
	private String rcrsTp;// 追索种类
	
	private BigDecimal reqamt;// 追索金额 ？
	private String rcrsrsncd;// 追索理由代码 ？
	
	private String elctrncSgntr; // 电子签名 ?
	private Integer recCorpId;// 业务接收人单位ID ？
	private String recAcctId;// 业务接收人账号
	private String recAcctsvcr;// 业务接收人开户行号
	private String recAcctsvcrAg;// 业务接收人承接行号
	private String recAcctTypeName;//接收方银行信息
	private String recCmonid;// 业务接收人组织结构ID ？
	private String recNm;// 业务接收人名称
	private String recRole;// 业务接收人类别 ?
	private Date replyDt;// 回复日期 ？
	private String replyType;// 回复类别 ？
	private String reqAcctId;// 业务发起人账号 ？
	private String reqAcctsvcr;// 业务发起人开户行号 ？
	private String reqAcctsvcrAg;// 业务发起人承接行号 ?
	private String reqCmonid;// 业务发起人组织结构ID ?
	private String reqNm;// 业务发起人名称 ?
	private String reqRole;// 业务发起人类别 ？
	private BigDecimal rpdamt;// 赎回金额
	
	private Date rpdduedt;// 赎回开始日期
	private BigDecimal rpdintrstrate;// 贴现赎回利率
    
	private String rpdmk;// 贴现种类
	private Date rpdopendt;// 赎回截止日期
	private String rsn;// 原因说明
	private String sgnupmk;// 回复标志 ?
	private String sttlmmk;// 线上清算标记
	private String txlctrctnb;// 交易合同编号
	private String ucondlprmsmrk;// 到期无条件支付承诺 ?
	private String billStatus;// 票据状态
	private int status;// 业务处理状态 -1：审批打回 0:接收到待签收 1：签收待审批 2：审批中 3：签收完成
	private String uuid;// 审批id
	private Long boxid;// 本库箱
	
	private Long bookid;//持票人
	private String signUpFlag;//签收标识 SU00-通用回复同意  SU01-通用回复拒绝
	
	private String coptorId;
	
	private String isSuccessSap;//fangh 2011-1-9 发送到SAP是否成功
	
	private String rmrkByPropsr ;// 贴出人备注 //add by wangcq 信息接收时缺少贴出人填写备注的信息
	
	/**
	 * @Fields businessSign : 业务标识号
	 */
	private String businessSign;  //业务标识号
	
	private BigDecimal rechMoney;  // 杨明义 2012-10-19 同意清偿金额
	
	private String reqAcctsvcrNm;  // 业务申请人开户行名
	
	private String recAcctsvcrNm;  // 业务接收人开户行名
	
	private String businessType;   // 业务类型
	
	private String billId;	 //票据ID
	
	private String billCecmNo;//	票据编码
	
    private Timestamp acptDt;//	承兑日
    
	private String conferNo;//	交易合同号
	
	private String serialId;
	
	/**买方付息标志*/
	private Integer buyerInterestSign;
	private String newStmIssuofpprInd;//		出票标志。0：老系统出票,   1：新系统出票
	private String blgrdbagcrclPrmtInd;//		是否允许分包。1 是  0 否
	private String sbbllRngStrtSn;//		子票区间开始序号。旧票据默认0，新票据 12位长度，不足12位前面补0。
	private String sbbllRngEndSn;//				子票区间结束序号。旧票据默认0，新票据 12位长度，不足12位前面补0。
	private String blRmtrUnnIdNo;//			出票人统一信用代码
	private String blAcptrIdTpcd;//			承兑人识别类型。DT01 票据账户DT02 银行账户
	private String blRcvpymtpsIdTpcd;//			收款人人识别类型。DT01 票据账户DT02
	private String ebillBlCrclIndcd;//票据流通标(签收表)
	private String intpymtprtWay;//付息方式。必填， 1 卖方2 买方3 协议4 他方付息，默认为1
	private String buyerpayintrcntno;//买方付息合作协议编号
	private String intpymtPsnNm;//付息人名称
	private String intpymtPsnAccno;//付息人账号
	private String blRcvUnnIdNo;//业务接收人社会统一信用代码
	private String blReqUnnIdNo;//业务申请人社会统一信用代码
	private String blRcvIdTpcd;//业务接收人识别类型 Dt01 票据账户dt02 银行账户
	private String blReqIdTpcd;//业务申请人识别类型 Dt01 票据账户dt02 银行账
	private String blBsnTpcd;//新票据业务类型
	private String blAcptrUnnIdNo;//			承兑人统一信用代码
	private String blRcvpymtpsUnnIdNo;//			收款人统一信用代码
	private String blHoldUnnIdNo;//票据单位社会统一信用代码
	public String getBlBsnTpcd() {
		return blBsnTpcd;
	}
	public void setBlBsnTpcd(String blBsnTpcd) {
		this.blBsnTpcd = blBsnTpcd;
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
	public String getBlHoldUnnIdNo() {
		return blHoldUnnIdNo;
	}
	public void setBlHoldUnnIdNo(String blHoldUnnIdNo) {
		this.blHoldUnnIdNo = blHoldUnnIdNo;
	}
	public String getNewStmIssuofpprInd() {
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
	public BillSignVo() {
		super();
	}
	/**
     * @return the businessType
     */
    public String getBusinessType() {
        return businessType;
    }
    /**
     * @param businessType the businessType to set
     */
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
    /**
     * @return the reqAcctsvcrNm
     */
    public String getReqAcctsvcrNm() {
        return reqAcctsvcrNm;
    }
    /**
     * @param reqAcctsvcrNm the reqAcctsvcrNm to set
     */
    public void setReqAcctsvcrNm(String reqAcctsvcrNm) {
        this.reqAcctsvcrNm = reqAcctsvcrNm;
    }
    /**
     * @return the recAcctsvcrNm
     */
    public String getRecAcctsvcrNm() {
        return recAcctsvcrNm;
    }
    /**
     * @param recAcctsvcrNm the recAcctsvcrNm to set
     */
    public void setRecAcctsvcrNm(String recAcctsvcrNm) {
        this.recAcctsvcrNm = recAcctsvcrNm;
    }
    /**
     * @return the rechMoney
     */
    public BigDecimal getRechMoney() {
        return rechMoney;
    }
    /**
     * @param rechMoney the rechMoney to set
     */
    public void setRechMoney(BigDecimal rechMoney) {
        this.rechMoney = rechMoney;
    }
    /**
     * @return the businessSign
     */
    public String getBusinessSign() {
        return businessSign;
    }
    /**
     * @param businessSign the businessSign to set
     */
    public void setBusinessSign(String businessSign) {
        this.businessSign = businessSign;
    }
    public String getRmrkByPropsr() {
		return rmrkByPropsr;
	}
	
	public void setRmrkByPropsr(String rmrkByPropsr) {
		this.rmrkByPropsr = rmrkByPropsr;
	}
	public Long getBookid() {
		return bookid;
	}
	public void setBookid(Long bookid) {
		this.bookid = bookid;
	}
	public String getSignUpFlag() {
		return signUpFlag;
	}
	public void setSignUpFlag(String signUpFlag) {
		this.signUpFlag = signUpFlag;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAcceptor() {
		return this.acceptor;
	}
	public void setAcceptor(String acceptor) {
		this.acceptor = acceptor;
	}
	public String getAcceptoracc() {
		return this.acceptoracc;
	}
	public void setAcceptoracc(String acceptoracc) {
		this.acceptoracc = acceptoracc;
	}
	public String getAcceptorbankno() {
		return this.acceptorbankno;
	}
	public void setAcceptorbankno(String acceptorbankno) {
		this.acceptorbankno = acceptorbankno;
	}
	public String getAccptncagrmtnb() {
		return this.accptncagrmtnb;
	}
	public void setAccptncagrmtnb(String accptncagrmtnb) {
		this.accptncagrmtnb = accptncagrmtnb;
	}
	public String getAdr() {
		return this.adr;
	}
	public void setAdr(String adr) {
		this.adr = adr;
	}
	public BigDecimal getAmt() {
		return this.amt;
	}
	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}
	public String getAoaccninfAc() {
		return this.aoaccninfAc;
	}
	public void setAoaccninfAc(String aoaccninfAc) {
		this.aoaccninfAc = aoaccninfAc;
	}
	public String getAoaccninfId() {
		return this.aoaccninfId;
	}
	public void setAoaccninfId(String aoaccninfId) {
		this.aoaccninfId = aoaccninfId;
	}
	public Date getAppldt() {
		return this.appldt;
	}
	public void setAppldt(Date appldt) {
		this.appldt = appldt;
	}
	public String getBankendrsmtmk() {
		return this.bankendrsmtmk;
	}
	public void setBankendrsmtmk(String bankendrsmtmk) {
		this.bankendrsmtmk = bankendrsmtmk;
	}
	public BigDecimal getBillMoney() {
		return this.billMoney;
	}
	public void setBillMoney(BigDecimal billMoney) {
		this.billMoney = billMoney;
	}
	public String getBillType() {
		return this.billType;
	}
	public void setBillType(String billType) {
//		票据种类：10 银票；11 商票
		if(EDraftConstant.BILL_TYPE_AC01.equals(billType)){
			this.billType = String.valueOf(EDraftConstant.BILL_TYPE_10);
		}else if(EDraftConstant.BILL_TYPE_AC02.equals(billType)){
			this.billType = String.valueOf(EDraftConstant.BILL_TYPE_11);
		}else{
			this.billType = billType;
		}
	}
	public String getBtchnb() {
		return this.btchnb;
	}
	public void setBtchnb(String btchnb) {
		this.btchnb = btchnb;
	}
	public Timestamp getBussdt() {
		return this.bussdt;
	}
	public void setBussdt(Timestamp bussdt) {
		this.bussdt = bussdt;
	}
	public String getCdtratgagcy() {
		return this.cdtratgagcy;
	}
	public void setCdtratgagcy(String cdtratgagcy) {
		this.cdtratgagcy = cdtratgagcy;
	}
	public Date getCdtratgduedt() {
		return this.cdtratgduedt;
	}
	public void setCdtratgduedt(Date cdtratgduedt) {
		this.cdtratgduedt = cdtratgduedt;
	}
	public String getCdtratgs() {
		return this.cdtratgs;
	}
	public void setCdtratgs(String cdtratgs) {
		this.cdtratgs = cdtratgs;
	}
	public String getDptymk() {
		return this.dptymk;
	}
	public void setDptymk(String dptymk) {
		this.dptymk = dptymk;
	}
	public String getDshnrcd() {
		return this.dshnrcd;
	}
	public void setDshnrcd(String dshnrcd) {
		this.dshnrcd = dshnrcd;
	}
	public String getDshnrrsn() {
		return this.dshnrrsn;
	}
	public void setDshnrrsn(String dshnrrsn) {
		this.dshnrrsn = dshnrrsn;
	}
	public Date getEndDate() {
		return this.endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public BigDecimal getIntrstrate() {
		return this.intrstrate;
	}
	public void setIntrstrate(BigDecimal intrstrate) {
		this.intrstrate = intrstrate;
	}
	public String getInvcnb() {
		return this.invcnb;
	}
	public void setInvcnb(String invcnb) {
		this.invcnb = invcnb;
	}
	public Timestamp getMsdt() {
		return this.msdt;
	}
	public void setMsdt(Timestamp msdt) {
		this.msdt = msdt;
	}
	public String getMsgid() {
		return this.msgid;
	}
	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}
	public String getMsgtype() {
		return this.msgtype;
	}
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}
	public Date getOrgnlmsdt() {
		return this.orgnlmsdt;
	}
	public void setOrgnlmsdt(Date orgnlmsdt) {
		this.orgnlmsdt = orgnlmsdt;
	}
	public String getOrgnlmsgid() {
		return this.orgnlmsgid;
	}
	public void setOrgnlmsgid(String orgnlmsgid) {
		this.orgnlmsgid = orgnlmsgid;
	}
	public String getOrigNo() {
		return this.origNo;
	}
	public void setOrigNo(String origNo) {
		this.origNo = origNo;
	}
	public String getPayee() {
		return this.payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public String getPayeeacc() {
		return this.payeeacc;
	}
	public void setPayeeacc(String payeeacc) {
		this.payeeacc = payeeacc;
	}
	public String getPayeebankno() {
		return this.payeebankno;
	}
	public void setPayeebankno(String payeebankno) {
		this.payeebankno = payeebankno;
	}
	public String getPayer() {
		return this.payer;
	}
	public void setPayer(String payer) {
		this.payer = payer;
	}
	public String getPayeracc() {
		return this.payeracc;
	}
	public void setPayeracc(String payeracc) {
		this.payeracc = payeracc;
	}
	public String getPayerbankno() {
		return this.payerbankno;
	}
	public void setPayerbankno(String payerbankno) {
		this.payerbankno = payerbankno;
	}
	public String getPrccd() {
		return this.prccd;
	}
	public void setPrccd(String prccd) {
		this.prccd = prccd;
	}
	public String getPrcmsg() {
		return this.prcmsg;
	}
	public void setPrcmsg(String prcmsg) {
		this.prcmsg = prcmsg;
	}
	public String getRcrsTp() {
		return this.rcrsTp;
	}
	public void setRcrsTp(String rcrsTp) {
		this.rcrsTp = rcrsTp;
	}
	public String getRcrsrsncd() {
		return this.rcrsrsncd;
	}
	public void setRcrsrsncd(String rcrsrsncd) {
		this.rcrsrsncd = rcrsrsncd;
	}
	public String getRecAcctId() {
		return this.recAcctId;
	}
	public void setRecAcctId(String recAcctId) {
		this.recAcctId = recAcctId;
	}
	public String getRecAcctsvcr() {
		return this.recAcctsvcr;
	}
	public void setRecAcctsvcr(String recAcctsvcr) {
		this.recAcctsvcr = recAcctsvcr;
	}
	public String getRecAcctsvcrAg() {
		return this.recAcctsvcrAg;
	}
	public void setRecAcctsvcrAg(String recAcctsvcrAg) {
		this.recAcctsvcrAg = recAcctsvcrAg;
	}
	public String getRecCmonid() {
		return this.recCmonid;
	}
	public void setRecCmonid(String recCmonid) {
		this.recCmonid = recCmonid;
	}
	public String getRecNm() {
		return this.recNm;
	}
	public void setRecNm(String recNm) {
		this.recNm = recNm;
	}
	public String getRecRole() {
		return this.recRole;
	}
	public void setRecRole(String recRole) {
		this.recRole = recRole;
	}
	public Date getReplyDt() {
		return this.replyDt;
	}
	public void setReplyDt(Date replyDt) {
		this.replyDt = replyDt;
	}
	public String getReplyType() {
		return this.replyType;
	}
	public void setReplyType(String replyType) {
		this.replyType = replyType;
	}
	public String getReqAcctId() {
		return this.reqAcctId;
	}
	public void setReqAcctId(String reqAcctId) {
		this.reqAcctId = reqAcctId;
	}
	public String getReqAcctsvcr() {
		return this.reqAcctsvcr;
	}
	public void setReqAcctsvcr(String reqAcctsvcr) {
		this.reqAcctsvcr = reqAcctsvcr;
	}
	public String getReqAcctsvcrAg() {
		return this.reqAcctsvcrAg;
	}
	public void setReqAcctsvcrAg(String reqAcctsvcrAg) {
		this.reqAcctsvcrAg = reqAcctsvcrAg;
	}
	public String getReqCmonid() {
		return this.reqCmonid;
	}
	public void setReqCmonid(String reqCmonid) {
		this.reqCmonid = reqCmonid;
	}
	public String getReqNm() {
		return this.reqNm;
	}
	public void setReqNm(String reqNm) {
		this.reqNm = reqNm;
	}
	public String getReqRole() {
		return this.reqRole;
	}
	public void setReqRole(String reqRole) {
		this.reqRole = reqRole;
	}
	public BigDecimal getReqamt() {
		return this.reqamt;
	}
	public void setReqamt(BigDecimal reqamt) {
		this.reqamt = reqamt;
	}
	public BigDecimal getRpdamt() {
		return this.rpdamt;
	}
	public void setRpdamt(BigDecimal rpdamt) {
		this.rpdamt = rpdamt;
	}
	public Date getRpdduedt() {
		return this.rpdduedt;
	}
	public void setRpdduedt(Date rpdduedt) {
		this.rpdduedt = rpdduedt;
	}
	public BigDecimal getRpdintrstrate() {
		return this.rpdintrstrate;
	}
	public void setRpdintrstrate(BigDecimal rpdintrstrate) {
		this.rpdintrstrate = rpdintrstrate;
	}
	public String getRpdmk() {
		return this.rpdmk;
	}
	public void setRpdmk(String rpdmk) {
		this.rpdmk = rpdmk;
	}
	public Date getRpdopendt() {
		return this.rpdopendt;
	}
	public void setRpdopendt(Date rpdopendt) {
		this.rpdopendt = rpdopendt;
	}
	public String getRsn() {
		return this.rsn;
	}
	public void setRsn(String rsn) {
		this.rsn = rsn;
	}
	public String getSgnupmk() {
		return this.sgnupmk;
	}
	public void setSgnupmk(String sgnupmk) {
		this.sgnupmk = sgnupmk;
	}
	public Date getStartDate() {
		return this.startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getSttlmmk() {
		return this.sttlmmk;
	}
	public void setSttlmmk(String sttlmmk) {
		this.sttlmmk = sttlmmk;
	}
	public String getTxlctrctnb() {
		return this.txlctrctnb;
	}
	public void setTxlctrctnb(String txlctrctnb) {
		this.txlctrctnb = txlctrctnb;
	}
	public String getUcondlprmsmrk() {
		return this.ucondlprmsmrk;
	}
	public void setUcondlprmsmrk(String ucondlprmsmrk) {
		this.ucondlprmsmrk = ucondlprmsmrk;
	}
	public String getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Integer getRecCorpId() {
		return recCorpId;
	}
	public void setRecCorpId(Integer recCorpId) {
		this.recCorpId = recCorpId;
	}
	public String getElctrncSgntr() {
		return elctrncSgntr;
	}
	public void setElctrncSgntr(String elctrncSgntr) {
		this.elctrncSgntr = elctrncSgntr;
	}
//	public Integer getUserType() {
//		return userType;
//	}
//
//	public void setUserType(Integer userType) {
//		this.userType = userType;
//	}
//
//	public String getBusscode() {
//		return busscode;
//	}
//
//	public void setBusscode(String busscode) {
//		this.busscode = busscode;
//	}
	public Long getBoxid() {
		return boxid;
	}
	public void setBoxid(Long boxid) {
		this.boxid = boxid;
	}
//	public BigDecimal getBuyerInterest() {
//		return buyerInterest;
//	}
//
//	public void setBuyerInterest(BigDecimal buyerInterest) {
//		this.buyerInterest = buyerInterest;
//	}
//
//	public String getAttFileName() {
//		return attFileName;
//	}
//
//	public void setAttFileName(String attFileName) {
//		this.attFileName = attFileName;
//	}
//
//    public int getLoginType()
//    {
//        return loginType;
//    }
//
//    public void setLoginType(int loginType)
//    {
//        this.loginType = loginType;
//    }
//
//    public String getSignData()
//    {
//        return signData;
//    }
//
//    public void setSignData(String signData)
//    {
//        this.signData = signData;
//    }
//
//	public Long getRecord_id() {
//		return record_id;
//	}
//
//	public void setRecord_id(Long record_id) {
//		this.record_id = record_id;
//	}
	public String getIsSuccessSap() {
		return isSuccessSap;
	}
	public void setIsSuccessSap(String isSuccessSap) {
		this.isSuccessSap = isSuccessSap;
	}
	public String getCoptorId() {
		return coptorId;
	}
	public void setCoptorId(String coptorId) {
		this.coptorId = coptorId;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getBillCecmNo() {
		return billCecmNo;
	}
	public void setBillCecmNo(String billCecmNo) {
		this.billCecmNo = billCecmNo;
	}
	public Timestamp getAcptDt() {
		return acptDt;
	}
	public void setAcptDt(Timestamp acptDt) {
		this.acptDt = acptDt;
	}
	public String getConferNo() {
		return conferNo;
	}
	public void setConferNo(String conferNo) {
		this.conferNo = conferNo;
	}
	public String getAcceptorBankName() {
		return acceptorBankName;
	}
	public void setAcceptorBankName(String acceptorBankName) {
		this.acceptorBankName = acceptorBankName;
	}
	public String getPayeeBankName() {
		return payeeBankName;
	}
	public void setPayeeBankName(String payeeBankName) {
		this.payeeBankName = payeeBankName;
	}
	public String getPayerBankName() {
		return payerBankName;
	}
	public void setPayerBankName(String payerBankName) {
		this.payerBankName = payerBankName;
	}
	public String getSerialId() {
		return serialId;
	}
	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}
	public Integer getBuyerInterestSign() {
		return buyerInterestSign;
	}
	public void setBuyerInterestSign(Integer buyerInterestSign) {
		this.buyerInterestSign = buyerInterestSign;
	}

	public String getRecAcctTypeName() {
		return recAcctTypeName;
	}

	public void setRecAcctTypeName(String recAcctTypeName) {
		this.recAcctTypeName = recAcctTypeName;
	}

	@Override
	public String toString() {
		return "BillSignVo{" +
				"id='" + id + '\'' +
				", acceptor='" + acceptor + '\'' +
				", acceptoracc='" + acceptoracc + '\'' +
				", acceptorbankno='" + acceptorbankno + '\'' +
				", acceptorBankName='" + acceptorBankName + '\'' +
				", accptncagrmtnb='" + accptncagrmtnb + '\'' +
				", adr='" + adr + '\'' +
				", amt=" + amt +
				", aoaccninfAc='" + aoaccninfAc + '\'' +
				", aoaccninfId='" + aoaccninfId + '\'' +
				", appldt=" + appldt +
				", bankendrsmtmk='" + bankendrsmtmk + '\'' +
				", billMoney=" + billMoney +
				", billType='" + billType + '\'' +
				", btchnb='" + btchnb + '\'' +
				", bussdt=" + bussdt +
				", cdtratgagcy='" + cdtratgagcy + '\'' +
				", cdtratgduedt=" + cdtratgduedt +
				", cdtratgs='" + cdtratgs + '\'' +
				", dptymk='" + dptymk + '\'' +
				", dshnrcd='" + dshnrcd + '\'' +
				", dshnrrsn='" + dshnrrsn + '\'' +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", intrstrate=" + intrstrate +
				", invcnb='" + invcnb + '\'' +
				", msdt=" + msdt +
				", msgid='" + msgid + '\'' +
				", msgtype='" + msgtype + '\'' +
				", orgnlmsdt=" + orgnlmsdt +
				", orgnlmsgid='" + orgnlmsgid + '\'' +
				", origNo='" + origNo + '\'' +
				", payee='" + payee + '\'' +
				", payeeacc='" + payeeacc + '\'' +
				", payeebankno='" + payeebankno + '\'' +
				", payeeBankName='" + payeeBankName + '\'' +
				", payer='" + payer + '\'' +
				", payeracc='" + payeracc + '\'' +
				", payerbankno='" + payerbankno + '\'' +
				", payerBankName='" + payerBankName + '\'' +
				", prccd='" + prccd + '\'' +
				", prcmsg='" + prcmsg + '\'' +
				", rcrsTp='" + rcrsTp + '\'' +
				", reqamt=" + reqamt +
				", rcrsrsncd='" + rcrsrsncd + '\'' +
				", elctrncSgntr='" + elctrncSgntr + '\'' +
				", recCorpId=" + recCorpId +
				", recAcctId='" + recAcctId + '\'' +
				", recAcctsvcr='" + recAcctsvcr + '\'' +
				", recAcctsvcrAg='" + recAcctsvcrAg + '\'' +
				", recAcctTypeName='" + recAcctTypeName + '\'' +
				", recCmonid='" + recCmonid + '\'' +
				", recNm='" + recNm + '\'' +
				", recRole='" + recRole + '\'' +
				", replyDt=" + replyDt +
				", replyType='" + replyType + '\'' +
				", reqAcctId='" + reqAcctId + '\'' +
				", reqAcctsvcr='" + reqAcctsvcr + '\'' +
				", reqAcctsvcrAg='" + reqAcctsvcrAg + '\'' +
				", reqCmonid='" + reqCmonid + '\'' +
				", reqNm='" + reqNm + '\'' +
				", reqRole='" + reqRole + '\'' +
				", rpdamt=" + rpdamt +
				", rpdduedt=" + rpdduedt +
				", rpdintrstrate=" + rpdintrstrate +
				", rpdmk='" + rpdmk + '\'' +
				", rpdopendt=" + rpdopendt +
				", rsn='" + rsn + '\'' +
				", sgnupmk='" + sgnupmk + '\'' +
				", sttlmmk='" + sttlmmk + '\'' +
				", txlctrctnb='" + txlctrctnb + '\'' +
				", ucondlprmsmrk='" + ucondlprmsmrk + '\'' +
				", billStatus='" + billStatus + '\'' +
				", status=" + status +
				", uuid='" + uuid + '\'' +
				", boxid=" + boxid +
				", bookid=" + bookid +
				", signUpFlag='" + signUpFlag + '\'' +
				", coptorId='" + coptorId + '\'' +
				", isSuccessSap='" + isSuccessSap + '\'' +
				", rmrkByPropsr='" + rmrkByPropsr + '\'' +
				", businessSign='" + businessSign + '\'' +
				", rechMoney=" + rechMoney +
				", reqAcctsvcrNm='" + reqAcctsvcrNm + '\'' +
				", recAcctsvcrNm='" + recAcctsvcrNm + '\'' +
				", businessType='" + businessType + '\'' +
				", billId='" + billId + '\'' +
				", billCecmNo='" + billCecmNo + '\'' +
				", acptDt=" + acptDt +
				", conferNo='" + conferNo + '\'' +
				", serialId='" + serialId + '\'' +
				", buyerInterestSign=" + buyerInterestSign +
				", newStmIssuofpprInd='" + newStmIssuofpprInd + '\'' +
				", blgrdbagcrclPrmtInd='" + blgrdbagcrclPrmtInd + '\'' +
				", sbbllRngStrtSn='" + sbbllRngStrtSn + '\'' +
				", sbbllRngEndSn='" + sbbllRngEndSn + '\'' +
				", blRmtrUnnIdNo='" + blRmtrUnnIdNo + '\'' +
				", blAcptrIdTpcd='" + blAcptrIdTpcd + '\'' +
				", blRcvpymtpsIdTpcd='" + blRcvpymtpsIdTpcd + '\'' +
				", ebillBlCrclIndcd='" + ebillBlCrclIndcd + '\'' +
				", intpymtprtWay='" + intpymtprtWay + '\'' +
				", buyerpayintrcntno='" + buyerpayintrcntno + '\'' +
				", intpymtPsnNm='" + intpymtPsnNm + '\'' +
				", intpymtPsnAccno='" + intpymtPsnAccno + '\'' +
				", blRcvUnnIdNo='" + blRcvUnnIdNo + '\'' +
				", blReqUnnIdNo='" + blReqUnnIdNo + '\'' +
				", blRcvIdTpcd='" + blRcvIdTpcd + '\'' +
				", blReqIdTpcd='" + blReqIdTpcd + '\'' +
				", blBsnTpcd='" + blBsnTpcd + '\'' +
				", blAcptrUnnIdNo='" + blAcptrUnnIdNo + '\'' +
				", blRcvpymtpsUnnIdNo='" + blRcvpymtpsUnnIdNo + '\'' +
				", blHoldUnnIdNo='" + blHoldUnnIdNo + '\'' +
				'}';
	}
}
