package com.bytter.bill.domain.billbank.vo;
import java.math.BigDecimal;
import java.sql.Timestamp;
/**   
 * @Description: 票据背面信息
 * @author onlineGenerator
 * @date 2017-06-21 15:36:23
 * @version 1.0
 *
 */
public class BillAbBackbillinfoVo implements java.io.Serializable {
	private static final long serialVersionUID = 4645205545262620431L;

	private String uuid;//查询指令表id
	private String rpdmk; // 贴现种类
	private Timestamp bussdt; // 业务日期 
	
	private Timestamp redt;// 业务解除日期
	private BigDecimal intrstRate; // 贴现利率
	
	/**贴现利率*/
	private BigDecimal intrstrate;
	private BigDecimal amt; // 贴现实付金额
	private String aoaccninfId; // 入账帐号
	private String aoaccninfAc; // 入账行号
	private String banendrsmtmk; // 不得转让
	private String sttlmmk; // 线上清算标记
	private Timestamp rpdOpenDT; // 贴现赎回开放日,
	private Timestamp rpdDueDT; // 贴现赎回截止日
	private BigDecimal rpdIntrstRate; // 贴现赎回利率
	/**申请赎回利率*/
	private BigDecimal rpdintrstrate;
	private BigDecimal rpdAmt; // 贴现赎回金额
	private String txlCtrctNb; // 交易合同编号
	private String invcNb; // 发票号码
	private Timestamp applDt;// 提示付款申请日期
	private String rsn; // 逾期原因说明
	private String rcrsTp;// 追索类型
	private BigDecimal reqAmt; // 追索金额
	private String rcrsRsnCd; // 追索理由代码
	private String msgId; // 报文标识号
	private Timestamp msDt;// 报文时间
	private String origNo; // 票据号码
	private String reqRole; // 业务发起人，类别
	private String reqNm; // 业务发起人名称
	private String reqCmonid; // 组织机构代码
	private String reqAcctId; // 帐号
	private String reqAcctSvcr; // 开户行
	private String reqAcctsvcrAG; // 承接行
	private String btchNb;
	private String recRole; // 接收人类别
	private String recNm; // 业务接收人名称
	private String recCmonid; // 组织机构代码
	private String recAcctId; // 帐号
	private String recAcctSvcr; // 开户行
	private String recAcctsvcrAG; // 承接行
	private String cdtRatgs; // 信用等级
	private String cdtRatgAgcy; // 评级机构
	private Timestamp cdtRatgDueDt; // 评级到期日
	private String adr; // 保证人地址 VARCHAR2(100),
	private String ucondlConsgnmtMrk; // 到期无条件支付委托 VARCHAR2(5),
	private String ucondlPrmsMrk;// 到期无条件支付承诺
	private String AccptncAgrmtNb; // 承诺协议编号
	private String dptymk;
	private String dshnrCd;// 拒付理由代码
	private String dshnrRsn; // 拒付备注
	private String dscntAgrmtNb; // 贴现协议编号
	private String sgnUpMk; // 提示付款回复标识
	private String bustype; // 业务类型
	private String rmrkByPropsr; // 提示付款人备注
	private String rmrkBySgnr; // 回复人备注
	private String prxtProp;
	private String prxySgntr; // 代理回复标识
	
	/**
	 *  返回代码
	 */
	private String returnCode;
	
	/**
	 *  返回消息
	 */
	private String returnMsg;
	
	/**申请日期*/
	private java.util.Date appldt;
	
	/**回复标记：SU00:同意签收；SU01：拒收*/
	private String sgnupmk;
	
	/**不得转让标记：EM00 可再转让；EM01 得转让*/
	private String bankendrsmtmk;
	
	/**
	 * 排序
	 */
	private Integer seqnum;
	/**
	 *否允许分包 1 是  0 否，默认为否
	 */
	private String blgrdbagcrclPrmtInd;
	/**
	 *子票区间开始序号
	 */
	private String sbbllRngStrtSn;
	/**
	 *子票区间结束序号
	 */
	private String sbbllRngEndSn;
	/**
	 *业务申请人统一信用代码
	 */
	private String blReqUnnIdNo;
	/**
	 *业务接收人统一信用代码
	 */
	private String blRcvUnnIdNo;
	/**
	 *业务申请人识别类型 DT01 票据账户DT02 银行账户  暂时固定为“银行账户”
	 */
	private String blReqIdTpcd;
	/**
	 *业务接收人识别类型。DT01 票据账户DT02 银行账户
	 */
	private String blRcvIdTpcd;
	/**
	 *付息方式， 1 卖方2 买方3 协议4 他方付息
	 */
	private String intpymtprtWay;
	/**
	 *买方付息合作协议编号
	 */
	private String buyerpayintrcntno;
	/**
	 *付息人名称
	 */
	private String intpymtPsnNm;
	/**
	 *付息人账号
	 */
	private String intpymtPsnAccno;
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
	public String getBlReqUnnIdNo() {
		return blReqUnnIdNo;
	}
	public void setBlReqUnnIdNo(String blReqUnnIdNo) {
		this.blReqUnnIdNo = blReqUnnIdNo;
	}
	public String getBlRcvUnnIdNo() {
		return blRcvUnnIdNo;
	}
	public void setBlRcvUnnIdNo(String blRcvUnnIdNo) {
		this.blRcvUnnIdNo = blRcvUnnIdNo;
	}
	public String getBlReqIdTpcd() {
		return blReqIdTpcd;
	}
	public void setBlReqIdTpcd(String blReqIdTpcd) {
		this.blReqIdTpcd = blReqIdTpcd;
	}
	public String getBlRcvIdTpcd() {
		return blRcvIdTpcd;
	}
	public void setBlRcvIdTpcd(String blRcvIdTpcd) {
		this.blRcvIdTpcd = blRcvIdTpcd;
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
	public java.util.Date getAppldt() {
		return appldt;
	}
	public void setAppldt(java.util.Date appldt) {
		this.appldt = appldt;
	}
	public String getSgnupmk() {
		return sgnupmk;
	}
	public void setSgnupmk(String sgnupmk) {
		this.sgnupmk = sgnupmk;
	}
	public String getBankendrsmtmk() {
		return bankendrsmtmk;
	}
	public void setBankendrsmtmk(String bankendrsmtmk) {
		this.bankendrsmtmk = bankendrsmtmk;
	}
	public String getRpdmk() {
		return rpdmk;
	}
	/**
	 * 贴现种类
	 * @param rpdmk
	 */
	public void setRpdmk(String rpdmk) {
		this.rpdmk = rpdmk;
	}
	public Timestamp getBussdt() {
		return bussdt;
	}
	/**
	 * bussdt
	 * @param bussdt
	 */
	public void setBussdt(Timestamp bussdt) {
		this.bussdt = bussdt;
	}
	/**
	 * @return the redt
	 */
	public Timestamp getRedt() {
		return redt;
	}
	/**
	 * 业务解除日期
	 * @param redt the redt to set
	 */
	public void setRedt(Timestamp redt) {
		this.redt = redt;
	}
	public BigDecimal getIntrstRate() {
		return intrstRate;
	}
	/**
	 * 贴现利率
	 * @param intrstRate
	 */
	public void setIntrstRate(BigDecimal intrstRate) {
		this.intrstRate = intrstRate;
		setIntrstrate(intrstRate);
	}
	
	public BigDecimal getIntrstrate() {
		return intrstrate;
	}
	/**
	 * 贴现利率
	 * @param intrstRate
	 */
	public void setIntrstrate(BigDecimal intrstrate) {
		this.intrstrate = intrstrate;
	}
	public BigDecimal getAmt() {
		return amt;
	}
	/**
	 *  贴现实付金额
	 * @param amt
	 */
	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}
	public String getAoaccninfId() {
		return aoaccninfId;
	}
	/**
	 * 入账帐号
	 * @param aoaccninfId
	 */
	public void setAoaccninfId(String aoaccninfId) {
		this.aoaccninfId = aoaccninfId;
	}
	public String getAoaccninfAc() {
		return aoaccninfAc;
	}
	/**
	 * 入账行号
	 * @param aoaccninfAc
	 */
	public void setAoaccninfAc(String aoaccninfAc) {
		this.aoaccninfAc = aoaccninfAc;
	}
	public String getBanendrsmtmk() {
		return banendrsmtmk;
	}
	/**
	 * 不得转让标记
	 * @param banendrsmtmk
	 */
	public void setBanendrsmtmk(String banendrsmtmk) {
		this.banendrsmtmk = banendrsmtmk;
		setBankendrsmtmk(banendrsmtmk);
	}
	public String getSttlmmk() {
		return sttlmmk;
	}
	/**
	 * 线上清算标记
	 * @param sttlmmk
	 */
	public void setSttlmmk(String sttlmmk) {
		this.sttlmmk = sttlmmk;
	}
	public Timestamp getRpdOpenDT() {
		return rpdOpenDT;
	}
	/**
	 * 贴现赎回开放日
	 * @param rpdOpenDT
	 */
	public void setRpdOpenDT(Timestamp rpdOpenDT) {
		this.rpdOpenDT = rpdOpenDT;
	}
	public Timestamp getRpdDueDT() {
		return rpdDueDT;
	}
	/**
	 * 贴现赎回截止日
	 * @param rpdDueDT
	 */
	public void setRpdDueDT(Timestamp rpdDueDT) {
		this.rpdDueDT = rpdDueDT;
	}
	public BigDecimal getRpdIntrstRate() {
		return rpdIntrstRate;
	}
	/**
	 * 贴现赎回利率
	 * @param rpdIntrstRate
	 */
	public void setRpdIntrstRate(BigDecimal rpdIntrstRate) {
		this.rpdIntrstRate = rpdIntrstRate;
		setRpdintrstrate(rpdIntrstRate);
	}
	public BigDecimal getRpdintrstrate() {
		return rpdintrstrate;
	}
	/**
	 * 贴现赎回利率
	 * @param rpdIntrstRate
	 */
	public void setRpdintrstrate(BigDecimal rpdintrstrate) {
		this.rpdintrstrate = rpdintrstrate;
	}
	public String getTxlCtrctNb() {
		return txlCtrctNb;
	}
	/**
	 * 交易合同编号
	 * @param txlCtrctNb
	 */
	public void setTxlCtrctNb(String txlCtrctNb) {
		this.txlCtrctNb = txlCtrctNb;
	}
	public String getInvcNb() {
		return invcNb;
	}
	/**
	 * 发票号码
	 * @param invcNb
	 */
	public void setInvcNb(String invcNb) {
		this.invcNb = invcNb;
	}
	public Timestamp getApplDt() {
		return applDt;
	}
	/**
	 * 提示付款申请日期
	 * @param applDt
	 */
	public void setApplDt(Timestamp applDt) {
		this.applDt = applDt;
		setAppldt(applDt);
	}
	public String getRsn() {
		return rsn;
	}
	/**
	 * 逾期原因说明
	 * @param rsn
	 */
	public void setRsn(String rsn) {
		this.rsn = rsn;
	}
	public String getRcrsTp() {
		return rcrsTp;
	}
	/**
	 * 追索类型
	 * @param rcrsTp
	 */
	public void setRcrsTp(String rcrsTp) {
		this.rcrsTp = rcrsTp;
	}
	public BigDecimal getReqAmt() {
		return reqAmt;
	}
	/**
	 * 追索金额
	 * @param reqAmt
	 */
	public void setReqAmt(BigDecimal reqAmt) {
		this.reqAmt = reqAmt;
	}
	public String getRcrsRsnCd() {
		return rcrsRsnCd;
	}
	/**
	 * 追索理由代码
	 * @param rcrsRsnCd
	 */
	public void setRcrsRsnCd(String rcrsRsnCd) {
		this.rcrsRsnCd = rcrsRsnCd;
	}
	public String getMsgId() {
		return msgId;
	}
	/**
	 * 报文标识号
	 * @param msgId
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public Timestamp getMsDt() {
		return msDt;
	}
	/**
	 * msDt
	 * @param msDt
	 */
	public void setMsDt(Timestamp msDt) {
		this.msDt = msDt;
	}
	public String getOrigNo() {
		return origNo;
	}
	/**
	 * 票据号码
	 * @param origNo
	 */
	public void setOrigNo(String origNo) {
		this.origNo = origNo;
	}
	public String getReqRole() {
		return reqRole;
	}
	/**
	 * 业务发起人，类别
	 * @param reqRole
	 */
	public void setReqRole(String reqRole) {
		this.reqRole = reqRole;
	}
	public String getReqNm() {
		return reqNm;
	}
	/**
	 * 业务发起人名称
	 * @param reqNm
	 */
	public void setReqNm(String reqNm) {
		this.reqNm = reqNm;
	}
	public String getReqCmonid() {
		return reqCmonid;
	}
	/**
	 * 组织机构代码
	 * @param reqCmonid
	 */
	public void setReqCmonid(String reqCmonid) {
		this.reqCmonid = reqCmonid;
	}
	public String getReqAcctId() {
		return reqAcctId;
	}
	/**
	 * 发起人账号
	 * @param reqAcctId
	 */
	public void setReqAcctId(String reqAcctId) {
		this.reqAcctId = reqAcctId;
	}
	public String getReqAcctSvcr() {
		return reqAcctSvcr;
	}
	/**
	 * 发起人开户行
	 * @param reqAcctSvcr
	 */
	public void setReqAcctSvcr(String reqAcctSvcr) {
		this.reqAcctSvcr = reqAcctSvcr;
	}
	public String getReqAcctsvcrAG() {
		return reqAcctsvcrAG;
	}
	/**
	 *  承接行
	 * @param reqAcctsvcrAG
	 */
	public void setReqAcctsvcrAG(String reqAcctsvcrAG) {
		this.reqAcctsvcrAG = reqAcctsvcrAG;
	}
	public String getBtchNb() {
		return btchNb;
	}
	public void setBtchNb(String btchNb) {
		this.btchNb = btchNb;
	}
	public String getRecRole() {
		return recRole;
	}
	/**
	 * 接收人类别
	 * @param recRole
	 */
	public void setRecRole(String recRole) {
		this.recRole = recRole;
	}
	public String getRecNm() {
		return recNm;
	}
	/**
	 * 业务接收人名称
	 * @param recNm
	 */
	public void setRecNm(String recNm) {
		this.recNm = recNm;
	}
	public String getRecCmonid() {
		return recCmonid;
	}
	/**
	 * 组织机构代码
	 * @param recCmonid
	 */
	public void setRecCmonid(String recCmonid) {
		this.recCmonid = recCmonid;
	}
	public String getRecAcctId() {
		return recAcctId;
	}
	/**
	 * 接收人账号
	 * @param recAcctId
	 */
	public void setRecAcctId(String recAcctId) {
		this.recAcctId = recAcctId;
	}
	public String getRecAcctSvcr() {
		return recAcctSvcr;
	}
	/**
	 * 接收人开户行
	 * @param recAcctSvcr
	 */
	public void setRecAcctSvcr(String recAcctSvcr) {
		this.recAcctSvcr = recAcctSvcr;
	}
	public String getRecAcctsvcrAG() {
		return recAcctsvcrAG;
	}
	/**
	 * 接收人承接行
	 * @param recAcctsvcrAG
	 */
	public void setRecAcctsvcrAG(String recAcctsvcrAG) {
		this.recAcctsvcrAG = recAcctsvcrAG;
	}
	public String getCdtRatgs() {
		return cdtRatgs;
	}
	/**
	 * 信用等级
	 * @param cdtRatgs
	 */
	public void setCdtRatgs(String cdtRatgs) {
		this.cdtRatgs = cdtRatgs;
	}
	public String getCdtRatgAgcy() {
		return cdtRatgAgcy;
	}
	/**
	 * 评级机构
	 * @param cdtRatgAgcy
	 */
	public void setCdtRatgAgcy(String cdtRatgAgcy) {
		this.cdtRatgAgcy = cdtRatgAgcy;
	}
	public Timestamp getCdtRatgDueDt() {
		return cdtRatgDueDt;
	}
	/**
	 * 评级到期日
	 * @param cdtRatgDueDt
	 */
	public void setCdtRatgDueDt(Timestamp cdtRatgDueDt) {
		this.cdtRatgDueDt = cdtRatgDueDt;
	}
	public String getAdr() {
		return adr;
	}
	/**
	 * 保证人地址 
	 * @param adr
	 */
	public void setAdr(String adr) {
		this.adr = adr;
	}
	public String getUcondlConsgnmtMrk() {
		return ucondlConsgnmtMrk;
	}
	/**
	 * 到期无条件支付委托
	 * @param ucondlConsgnmtMrk
	 */
	public void setUcondlConsgnmtMrk(String ucondlConsgnmtMrk) {
		this.ucondlConsgnmtMrk = ucondlConsgnmtMrk;
	}
	public String getUcondlPrmsMrk() {
		return ucondlPrmsMrk;
	}
	/**
	 * 到期无条件支付承诺
	 * @param ucondlPrmsMrk
	 */
	public void setUcondlPrmsMrk(String ucondlPrmsMrk) {
		this.ucondlPrmsMrk = ucondlPrmsMrk;
	}
	public String getAccptncAgrmtNb() {
		return AccptncAgrmtNb;
	}
	/**
	 * 承诺协议编号
	 * @param accptncAgrmtNb
	 */
	public void setAccptncAgrmtNb(String accptncAgrmtNb) {
		AccptncAgrmtNb = accptncAgrmtNb;
	}
	public String getDptymk() {
		return dptymk;
	}
	public void setDptymk(String dptymk) {
		this.dptymk = dptymk;
	}
	public String getDshnrCd() {
		return dshnrCd;
	}
	/**
	 * 拒付理由代码
	 * @param dshnrCd
	 */
	public void setDshnrCd(String dshnrCd) {
		this.dshnrCd = dshnrCd;
	}
	public String getDshnrRsn() {
		return dshnrRsn;
	}
	/**
	 * 拒付备注
	 * @param dshnrRsn
	 */
	public void setDshnrRsn(String dshnrRsn) {
		this.dshnrRsn = dshnrRsn;
	}
	public String getDscntAgrmtNb() {
		return dscntAgrmtNb;
	}
	/**
	 * 贴现协议编号
	 * @param dscntAgrmtNb
	 */
	public void setDscntAgrmtNb(String dscntAgrmtNb) {
		this.dscntAgrmtNb = dscntAgrmtNb;
	}
	public String getSgnUpMk() {
		return sgnUpMk;
	}
	/**
	 * 提示付款回复标识
	 * @param sgnUpMk
	 */
	public void setSgnUpMk(String sgnUpMk) {
		this.sgnUpMk = sgnUpMk;
		setSgnupmk(sgnUpMk);
	}
	public String getBustype() {
		return bustype;
	}
	/**
	 *  业务类型
	 * @param bustype
	 */
	public void setBustype(String bustype) {
		this.bustype = bustype;
	}
	public String getRmrkByPropsr() {
		return rmrkByPropsr;
	}
	/**
	 * 提示付款人备注
	 * @param rmrkByPropsr
	 */
	public void setRmrkByPropsr(String rmrkByPropsr) {
		this.rmrkByPropsr = rmrkByPropsr;
	}
	public String getRmrkBySgnr() {
		return rmrkBySgnr;
	}
	/**
	 *  回复人备注
	 * @param rmrkBySgnr
	 */
	public void setRmrkBySgnr(String rmrkBySgnr) {
		this.rmrkBySgnr = rmrkBySgnr;
	}
	public String getPrxtProp() {
		return prxtProp;
	}
	public void setPrxtProp(String prxtProp) {
		this.prxtProp = prxtProp;
	}
	public String getPrxySgntr() {
		return prxySgntr;
	}
	/**
	 *  代理回复标识
	 * @param prxySgntr
	 */
	public void setPrxySgntr(String prxySgntr) {
		this.prxySgntr = prxySgntr;
	}
	public BigDecimal getRpdAmt() {
		return rpdAmt;
	}
	/**
	 * 贴现赎回金额
	 * @param rpdAmt
	 */
	public void setRpdAmt(BigDecimal rpdAmt) {
		this.rpdAmt = rpdAmt;
	}
	public String getReturnCode() {
		return returnCode;
	}
	/**
	 * 返回代码
	 * @param returnCode
	 */
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	/**
	 * 返回代码
	 * @param returnMsg
	 */
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	
	
	
	public Integer getSeqnum() {
		return seqnum;
	}
	public void setSeqnum(Integer seqnum) {
		this.seqnum = seqnum;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "BillAbBackbillinfoVo{" +
				"uuid='" + uuid + '\'' +
				", rpdmk='" + rpdmk + '\'' +
				", bussdt=" + bussdt +
				", redt=" + redt +
				", intrstRate=" + intrstRate +
				", intrstrate=" + intrstrate +
				", amt=" + amt +
				", aoaccninfId='" + aoaccninfId + '\'' +
				", aoaccninfAc='" + aoaccninfAc + '\'' +
				", banendrsmtmk='" + banendrsmtmk + '\'' +
				", sttlmmk='" + sttlmmk + '\'' +
				", rpdOpenDT=" + rpdOpenDT +
				", rpdDueDT=" + rpdDueDT +
				", rpdIntrstRate=" + rpdIntrstRate +
				", rpdintrstrate=" + rpdintrstrate +
				", rpdAmt=" + rpdAmt +
				", txlCtrctNb='" + txlCtrctNb + '\'' +
				", invcNb='" + invcNb + '\'' +
				", applDt=" + applDt +
				", rsn='" + rsn + '\'' +
				", rcrsTp='" + rcrsTp + '\'' +
				", reqAmt=" + reqAmt +
				", rcrsRsnCd='" + rcrsRsnCd + '\'' +
				", msgId='" + msgId + '\'' +
				", msDt=" + msDt +
				", origNo='" + origNo + '\'' +
				", reqRole='" + reqRole + '\'' +
				", reqNm='" + reqNm + '\'' +
				", reqCmonid='" + reqCmonid + '\'' +
				", reqAcctId='" + reqAcctId + '\'' +
				", reqAcctSvcr='" + reqAcctSvcr + '\'' +
				", reqAcctsvcrAG='" + reqAcctsvcrAG + '\'' +
				", btchNb='" + btchNb + '\'' +
				", recRole='" + recRole + '\'' +
				", recNm='" + recNm + '\'' +
				", recCmonid='" + recCmonid + '\'' +
				", recAcctId='" + recAcctId + '\'' +
				", recAcctSvcr='" + recAcctSvcr + '\'' +
				", recAcctsvcrAG='" + recAcctsvcrAG + '\'' +
				", cdtRatgs='" + cdtRatgs + '\'' +
				", cdtRatgAgcy='" + cdtRatgAgcy + '\'' +
				", cdtRatgDueDt=" + cdtRatgDueDt +
				", adr='" + adr + '\'' +
				", ucondlConsgnmtMrk='" + ucondlConsgnmtMrk + '\'' +
				", ucondlPrmsMrk='" + ucondlPrmsMrk + '\'' +
				", AccptncAgrmtNb='" + AccptncAgrmtNb + '\'' +
				", dptymk='" + dptymk + '\'' +
				", dshnrCd='" + dshnrCd + '\'' +
				", dshnrRsn='" + dshnrRsn + '\'' +
				", dscntAgrmtNb='" + dscntAgrmtNb + '\'' +
				", sgnUpMk='" + sgnUpMk + '\'' +
				", bustype='" + bustype + '\'' +
				", rmrkByPropsr='" + rmrkByPropsr + '\'' +
				", rmrkBySgnr='" + rmrkBySgnr + '\'' +
				", prxtProp='" + prxtProp + '\'' +
				", prxySgntr='" + prxySgntr + '\'' +
				", returnCode='" + returnCode + '\'' +
				", returnMsg='" + returnMsg + '\'' +
				", appldt=" + appldt +
				", sgnupmk='" + sgnupmk + '\'' +
				", bankendrsmtmk='" + bankendrsmtmk + '\'' +
				", seqnum=" + seqnum +
				", blgrdbagcrclPrmtInd='" + blgrdbagcrclPrmtInd + '\'' +
				", sbbllRngStrtSn='" + sbbllRngStrtSn + '\'' +
				", sbbllRngEndSn='" + sbbllRngEndSn + '\'' +
				", blReqUnnIdNo='" + blReqUnnIdNo + '\'' +
				", blRcvUnnIdNo='" + blRcvUnnIdNo + '\'' +
				", blReqIdTpcd='" + blReqIdTpcd + '\'' +
				", blRcvIdTpcd='" + blRcvIdTpcd + '\'' +
				", intpymtprtWay='" + intpymtprtWay + '\'' +
				", buyerpayintrcntno='" + buyerpayintrcntno + '\'' +
				", intpymtPsnNm='" + intpymtPsnNm + '\'' +
				", intpymtPsnAccno='" + intpymtPsnAccno + '\'' +
				'}';
	}
	
}
