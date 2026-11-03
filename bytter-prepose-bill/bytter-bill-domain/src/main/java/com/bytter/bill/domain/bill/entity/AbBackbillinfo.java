package com.bytter.bill.domain.bill.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: 票据背面信息
 * @author onlineGenerator
 * @date 2017-06-21 15:36:23
 * @version 1.0
 *
 */
@Entity
@Table(name = "AB_BACKBILLINFO", schema = "")
@SuppressWarnings("serial")
public class AbBackbillinfo extends IdEntity implements java.io.Serializable {
	/**贴现种类：RM00 买断式；RM01 回购式*/
	private String rpdmk;
	/**业务完成日期*/
	private java.util.Date bussdt;
	/**贴现利率*/
	private java.math.BigDecimal intrstrate;
	/**贴现金额*/
	private java.math.BigDecimal amt;
	/**入账账号*/
	private String aoaccninfId;
	/**入账账号行号*/
	private String aoaccninfAc;
	/**不得转让标记：EM00 可再转让；EM01 得转让*/
	private String bankendrsmtmk;
	/**线上清算标记：SM00 开启线上清算；SM01 关闭线上清算*/
	private String sttlmmk;
	/**赎回开放日*/
	private java.util.Date rpdopendt;
	/**赎回截止日*/
	private java.util.Date rpdduedt;
	/**申请赎回利率*/
	private java.math.BigDecimal rpdintrstrate;
	/**赎回金额*/
	private java.math.BigDecimal rpdamt;
	/**交易合同编号*/
	private String txlctrctnb;
	/**发票号码*/
	private String invcnb;
	/**追索通知日期*/
	private java.util.Date appldt;
	/**原因说明*/
	private String rsn;
	/**追索种类：RT00 拒付追索；RT01 非拒付追索*/
	private String rcrsTp;
	/**追索金额*/
	private java.math.BigDecimal reqamt;
	/**追索理由代码：RC00 承兑人被依法宣告破产；RC01 承兑人因违法被责令终止活动（非拒付追索时填写）*/
	private String rcrsrsncd;
	/**报文id*/
	private String msgid;
	/**报文时间*/
	private java.util.Date msdt;
	/**业务类型：002承兑003收票010背书011贴现012贴现赎回013转贴现014转贴现赎回015再贴现016再贴现赎回017保证018质押019解除质押020提示付款021逾期提示付款022追索025央行卖票*/
	private String bustype;
	/**票据号码*/
	private String origNo;
	/**业务申请人类别*/
	private String reqRole;
	/**业务申请人名称*/
	private String reqNm;
	/**业务申请人组织机构代码*/
	private String reqCmonid;
	/**业务申请人账号*/
	private String reqAcctId;
	/**业务申请人开户行号*/
	private String reqAcctsvcr;
	/**业务申请人承接行号*/
	private String reqAcctsvcrAg;
	/**批次号*/
	private String btchnb;
	/**业务接收人类别*/
	private String recRole;
	/**业务接收人名称*/
	private String recNm;
	/**业务接收人组织机构代码*/
	private String recCmonid;
	/**业务接收人账号*/
	private String recAcctId;
	/**业务接收人开户行号*/
	private String recAcctsvcr;
	/**业务接收人承接行号*/
	private String recAcctsvcrAg;
	/**信用等级*/
	private String cdtratgs;
	/**评级机构*/
	private String cdtratgagcy;
	/**评级到期日*/
	private java.util.Date cdtratgduedt;
	/**保证人地址*/
	private String adr;
	/**到期无条件支付承诺*/
	private String ucondlprmsmrk;
	/**承兑协议编号*/
	private String accptncagrmtnb;
	/**是否代客户回复*/
	private String dptymk;
	/**拒付理由代码*/
	private String dshnrcd;
	/**拒付理由备注*/
	private String dshnrrsn;
	/**回复标记：SU00:同意签收；SU01：拒收*/
	private String sgnupmk;
	/**到期无条件支付委托:提示承兑专用 CC00含委托/承诺兑付*/
	private String ucondlconsgnmtmrk;
	/**贴现协议编号*/
	private String dscntagrmtnb;
	/**回复人备注*/
	private String rmrkbypropsr;
	/**申请人备注*/
	private String rmrkbysgnr;
	/**代理回复标识*/
	private String prxysgntr;
	private String blgrdbagcrclPrmtInd;//Blgrdbagcrcl_Prmt_Ind     是否允许分包。1 是  0 否，默认为否  新系统出票标志为"0"(老系统出 票)时，只能填写"0"(不可分包流 转)
	private String sbbllRngStrtSn;//Sbbll_Rng_Strt_Sn     子票区间开始序号
	private String sbbllRngEndSn;//Sbbll_Rng_End_Sn     子票区间结束序号
	private String blReqUnnIdNo;//Bl_Req_Unn_Id_No     业务申请人统一信用代码
	private String blReqIdTpcd;//Bl_Req_Id_Tpcd     业务申请人识别类型。DT01 票据账户DT02 银行账户  暂时固定为“银行账户”。旧票据可无
	private String blRcvIdTpcd;//Bl_Rcv_Id_Tpcd     业务接收人识别类型。DT01 票据账户DT02 银行账户  暂时固定为“银行账户”。旧票据可无
	private String blRcvUnnIdNo;//Bl_Rcv_Unn_Id_No     业务接收人社会统一信用代码
	private String intpymtprtWay;//Intpymtprt_Way     付息方式， 1 卖方2 买方3 协议4 他方付息
	private String buyerpayintrcntno;//Buyerpayintrcntno     买方付息合作协议编号
	private String intpymtPsnNm;//Intpymt_Psn_Nm     付息人名称
	private String intpymtPsnAccno;//Intpymt_Psn_Accno     付息人账号
	@Column(name ="Blgrdbagcrcl_Prmt_Ind",nullable=true,length=1)
	public String getBlgrdbagcrclPrmtInd() {
		return blgrdbagcrclPrmtInd;
	}
	@Column(name ="Sbbll_Rng_Strt_Sn",nullable=true,length=12)
	public String getSbbllRngStrtSn() {
		return sbbllRngStrtSn;
	}
	@Column(name ="Sbbll_Rng_End_Sn",nullable=true,length=12)
	public String getSbbllRngEndSn() {
		return sbbllRngEndSn;
	}
	@Column(name ="Bl_Req_Unn_Id_No",nullable=true,length=32)
	public String getBlReqUnnIdNo() {
		return blReqUnnIdNo;
	}
	@Column(name ="Bl_Req_Id_Tpcd",nullable=true,length=4)
	public String getBlReqIdTpcd() {
		return blReqIdTpcd;
	}
	@Column(name ="Bl_Rcv_Id_Tpcd",nullable=true,length=4)
	public String getBlRcvIdTpcd() {
		return blRcvIdTpcd;
	}
	@Column(name ="Bl_Rcv_Unn_Id_No",nullable=true,length=32)
	public String getBlRcvUnnIdNo() {
		return blRcvUnnIdNo;
	}
	@Column(name ="Intpymtprt_Way",nullable=true,length=1)
	public String getIntpymtprtWay() {
		return intpymtprtWay;
	}
	@Column(name ="Buyerpayintrcntno",nullable=true,length=32)
	public String getBuyerpayintrcntno() {
		return buyerpayintrcntno;
	}
	@Column(name ="INTPYMT_PSN_ACCNO",nullable=true,length=200)
	public String getIntpymtPsnNm() {
		return intpymtPsnNm;
	}
	@Column(name ="INTPYMT_PSN_NM",nullable=true,length=32)
	public String getIntpymtPsnAccno() {
		return intpymtPsnAccno;
	}
	public void setBlgrdbagcrclPrmtInd(String blgrdbagcrclPrmtInd) {
		this.blgrdbagcrclPrmtInd = blgrdbagcrclPrmtInd;
	}
	public void setSbbllRngStrtSn(String sbbllRngStrtSn) {
		this.sbbllRngStrtSn = sbbllRngStrtSn;
	}
	public void setSbbllRngEndSn(String sbbllRngEndSn) {
		this.sbbllRngEndSn = sbbllRngEndSn;
	}
	public void setBlReqUnnIdNo(String blReqUnnIdNo) {
		this.blReqUnnIdNo = blReqUnnIdNo;
	}
	public void setBlReqIdTpcd(String blReqIdTpcd) {
		this.blReqIdTpcd = blReqIdTpcd;
	}
	public void setBlRcvIdTpcd(String blRcvIdTpcd) {
		this.blRcvIdTpcd = blRcvIdTpcd;
	}
	public void setBlRcvUnnIdNo(String blRcvUnnIdNo) {
		this.blRcvUnnIdNo = blRcvUnnIdNo;
	}
	public void setIntpymtprtWay(String intpymtprtWay) {
		this.intpymtprtWay = intpymtprtWay;
	}
	public void setBuyerpayintrcntno(String buyerpayintrcntno) {
		this.buyerpayintrcntno = buyerpayintrcntno;
	}
	public void setIntpymtPsnNm(String intpymtPsnNm) {
		this.intpymtPsnNm = intpymtPsnNm;
	}
	public void setIntpymtPsnAccno(String intpymtPsnAccno) {
		this.intpymtPsnAccno = intpymtPsnAccno;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  贴现种类：RM00 买断式；RM01 回购式
	 */
	@Column(name ="RPDMK",nullable=true,length=4)
	public String getRpdmk(){
		return this.rpdmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  贴现种类：RM00 买断式；RM01 回购式
	 */
	public void setRpdmk(String rpdmk){
		this.rpdmk = rpdmk;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  业务完成日期
	 */
	@Column(name ="BUSSDT",nullable=true,scale=6)
	public java.util.Date getBussdt(){
		return this.bussdt;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  业务完成日期
	 */
	public void setBussdt(java.util.Date bussdt){
		this.bussdt = bussdt;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  贴现利率
	 */
	@Column(name ="INTRSTRATE",nullable=true,scale=6,length=8)
	public java.math.BigDecimal getIntrstrate(){
		return this.intrstrate;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  贴现利率
	 */
	public void setIntrstrate(java.math.BigDecimal intrstrate){
		this.intrstrate = intrstrate;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  贴现金额
	 */
	@Column(name ="AMT",nullable=true,scale=2,length=14)
	public java.math.BigDecimal getAmt(){
		return this.amt;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  贴现金额
	 */
	public void setAmt(java.math.BigDecimal amt){
		this.amt = amt;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  入账账号
	 */
	@Column(name ="AOACCNINF_ID",nullable=true,length=32)
	public String getAoaccninfId(){
		return this.aoaccninfId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  入账账号
	 */
	public void setAoaccninfId(String aoaccninfId){
		this.aoaccninfId = aoaccninfId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  入账账号行号
	 */
	@Column(name ="AOACCNINF_AC",nullable=true,length=12)
	public String getAoaccninfAc(){
		return this.aoaccninfAc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  入账账号行号
	 */
	public void setAoaccninfAc(String aoaccninfAc){
		this.aoaccninfAc = aoaccninfAc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  不得转让标记：EM00 可再转让；EM01 得转让
	 */
	@Column(name ="BANKENDRSMTMK",nullable=true,length=4)
	public String getBankendrsmtmk(){
		return this.bankendrsmtmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  不得转让标记：EM00 可再转让；EM01 得转让
	 */
	public void setBankendrsmtmk(String bankendrsmtmk){
		this.bankendrsmtmk = bankendrsmtmk;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  线上清算标记：SM00 开启线上清算；SM01 关闭线上清算
	 */
	@Column(name ="STTLMMK",nullable=true,length=4)
	public String getSttlmmk(){
		return this.sttlmmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  线上清算标记：SM00 开启线上清算；SM01 关闭线上清算
	 */
	public void setSttlmmk(String sttlmmk){
		this.sttlmmk = sttlmmk;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  赎回开放日
	 */
	@Column(name ="RPDOPENDT",nullable=true,scale=6)
	public java.util.Date getRpdopendt(){
		return this.rpdopendt;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  赎回开放日
	 */
	public void setRpdopendt(java.util.Date rpdopendt){
		this.rpdopendt = rpdopendt;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  赎回截止日
	 */
	@Column(name ="RPDDUEDT",nullable=true,scale=6)
	public java.util.Date getRpdduedt(){
		return this.rpdduedt;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  赎回截止日
	 */
	public void setRpdduedt(java.util.Date rpdduedt){
		this.rpdduedt = rpdduedt;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  申请赎回利率
	 */
	@Column(name ="RPDINTRSTRATE",nullable=true,scale=4,length=2)
	public java.math.BigDecimal getRpdintrstrate(){
		return this.rpdintrstrate;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  申请赎回利率
	 */
	public void setRpdintrstrate(java.math.BigDecimal rpdintrstrate){
		this.rpdintrstrate = rpdintrstrate;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  赎回金额
	 */
	@Column(name ="RPDAMT",nullable=true,scale=2,length=14)
	public java.math.BigDecimal getRpdamt(){
		return this.rpdamt;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  赎回金额
	 */
	public void setRpdamt(java.math.BigDecimal rpdamt){
		this.rpdamt = rpdamt;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  交易合同编号
	 */
	@Column(name ="TXLCTRCTNB",nullable=true,length=50)
	public String getTxlctrctnb(){
		return this.txlctrctnb;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  交易合同编号
	 */
	public void setTxlctrctnb(String txlctrctnb){
		this.txlctrctnb = txlctrctnb;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  发票号码
	 */
	@Column(name ="INVCNB",nullable=true,length=50)
	public String getInvcnb(){
		return this.invcnb;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  发票号码
	 */
	public void setInvcnb(String invcnb){
		this.invcnb = invcnb;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  追索通知日期
	 */
	@Column(name ="APPLDT",nullable=true,scale=6)
	public java.util.Date getAppldt(){
		return this.appldt;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  追索通知日期
	 */
	public void setAppldt(java.util.Date appldt){
		this.appldt = appldt;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  原因说明
	 */
	@Column(name ="RSN",nullable=true,length=100)
	public String getRsn(){
		return this.rsn;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  原因说明
	 */
	public void setRsn(String rsn){
		this.rsn = rsn;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  追索种类：RT00 拒付追索；RT01 非拒付追索
	 */
	@Column(name ="RCRS_TP",nullable=true,length=4)
	public String getRcrsTp(){
		return this.rcrsTp;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  追索种类：RT00 拒付追索；RT01 非拒付追索
	 */
	public void setRcrsTp(String rcrsTp){
		this.rcrsTp = rcrsTp;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  追索金额
	 */
	@Column(name ="REQAMT",nullable=true,scale=2,length=14)
	public java.math.BigDecimal getReqamt(){
		return this.reqamt;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  追索金额
	 */
	public void setReqamt(java.math.BigDecimal reqamt){
		this.reqamt = reqamt;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  追索理由代码：RC00 承兑人被依法宣告破产；RC01 承兑人因违法被责令终止活动（非拒付追索时填写）
	 */
	@Column(name ="RCRSRSNCD",nullable=true,length=10)
	public String getRcrsrsncd(){
		return this.rcrsrsncd;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  追索理由代码：RC00 承兑人被依法宣告破产；RC01 承兑人因违法被责令终止活动（非拒付追索时填写）
	 */
	public void setRcrsrsncd(String rcrsrsncd){
		this.rcrsrsncd = rcrsrsncd;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  报文id
	 */
	@Column(name ="MSGID",nullable=true,length=30)
	public String getMsgid(){
		return this.msgid;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  报文id
	 */
	public void setMsgid(String msgid){
		this.msgid = msgid;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  报文时间
	 */
	@Column(name ="MSDT",nullable=true,scale=6)
	public java.util.Date getMsdt(){
		return this.msdt;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  报文时间
	 */
	public void setMsdt(java.util.Date msdt){
		this.msdt = msdt;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务类型：002承兑003收票010背书011贴现012贴现赎回013转贴现014转贴现赎回015再贴现016再贴现赎回017保证018质押019解除质押020提示付款021逾期提示付款022追索025央行卖票
	 */
	@Column(name ="BUSTYPE",nullable=true,length=10)
	public String getBustype(){
		return this.bustype;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务类型：002承兑003收票010背书011贴现012贴现赎回013转贴现014转贴现赎回015再贴现016再贴现赎回017保证018质押019解除质押020提示付款021逾期提示付款022追索025央行卖票
	 */
	public void setBustype(String bustype){
		this.bustype = bustype;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  票据号码
	 */
	@Column(name ="ORIG_NO",nullable=true,length=40)
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务申请人类别
	 */
	@Column(name ="REQ_ROLE",nullable=true,length=12)
	public String getReqRole(){
		return this.reqRole;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务申请人类别
	 */
	public void setReqRole(String reqRole){
		this.reqRole = reqRole;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务申请人名称
	 */
	@Column(name ="REQ_NM",nullable=true,length=60)
	public String getReqNm(){
		return this.reqNm;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务申请人名称
	 */
	public void setReqNm(String reqNm){
		this.reqNm = reqNm;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务申请人组织机构代码
	 */
	@Column(name ="REQ_CMONID",nullable=true,length=32)
	public String getReqCmonid(){
		return this.reqCmonid;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务申请人组织机构代码
	 */
	public void setReqCmonid(String reqCmonid){
		this.reqCmonid = reqCmonid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务申请人账号
	 */
	@Column(name ="REQ_ACCT_ID",nullable=true,length=32)
	public String getReqAcctId(){
		return this.reqAcctId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务申请人账号
	 */
	public void setReqAcctId(String reqAcctId){
		this.reqAcctId = reqAcctId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务申请人开户行号
	 */
	@Column(name ="REQ_ACCTSVCR",nullable=true,length=12)
	public String getReqAcctsvcr(){
		return this.reqAcctsvcr;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务申请人开户行号
	 */
	public void setReqAcctsvcr(String reqAcctsvcr){
		this.reqAcctsvcr = reqAcctsvcr;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务申请人承接行号
	 */
	@Column(name ="REQ_ACCTSVCR_AG",nullable=true,length=12)
	public String getReqAcctsvcrAg(){
		return this.reqAcctsvcrAg;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务申请人承接行号
	 */
	public void setReqAcctsvcrAg(String reqAcctsvcrAg){
		this.reqAcctsvcrAg = reqAcctsvcrAg;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  批次号
	 */
	@Column(name ="BTCHNB",nullable=true,length=12)
	public String getBtchnb(){
		return this.btchnb;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  批次号
	 */
	public void setBtchnb(String btchnb){
		this.btchnb = btchnb;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务接收人类别
	 */
	@Column(name ="REC_ROLE",nullable=true,length=12)
	public String getRecRole(){
		return this.recRole;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务接收人类别
	 */
	public void setRecRole(String recRole){
		this.recRole = recRole;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务接收人名称
	 */
	@Column(name ="REC_NM",nullable=true,length=60)
	public String getRecNm(){
		return this.recNm;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务接收人名称
	 */
	public void setRecNm(String recNm){
		this.recNm = recNm;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务接收人组织机构代码
	 */
	@Column(name ="REC_CMONID",nullable=true,length=32)
	public String getRecCmonid(){
		return this.recCmonid;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务接收人组织机构代码
	 */
	public void setRecCmonid(String recCmonid){
		this.recCmonid = recCmonid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务接收人账号
	 */
	@Column(name ="REC_ACCT_ID",nullable=true,length=32)
	public String getRecAcctId(){
		return this.recAcctId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务接收人账号
	 */
	public void setRecAcctId(String recAcctId){
		this.recAcctId = recAcctId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务接收人开户行号
	 */
	@Column(name ="REC_ACCTSVCR",nullable=true,length=12)
	public String getRecAcctsvcr(){
		return this.recAcctsvcr;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务接收人开户行号
	 */
	public void setRecAcctsvcr(String recAcctsvcr){
		this.recAcctsvcr = recAcctsvcr;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务接收人承接行号
	 */
	@Column(name ="REC_ACCTSVCR_AG",nullable=true,length=12)
	public String getRecAcctsvcrAg(){
		return this.recAcctsvcrAg;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务接收人承接行号
	 */
	public void setRecAcctsvcrAg(String recAcctsvcrAg){
		this.recAcctsvcrAg = recAcctsvcrAg;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  信用等级
	 */
	@Column(name ="CDTRATGS",nullable=true,length=10)
	public String getCdtratgs(){
		return this.cdtratgs;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  信用等级
	 */
	public void setCdtratgs(String cdtratgs){
		this.cdtratgs = cdtratgs;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  评级机构
	 */
	@Column(name ="CDTRATGAGCY",nullable=true,length=60)
	public String getCdtratgagcy(){
		return this.cdtratgagcy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  评级机构
	 */
	public void setCdtratgagcy(String cdtratgagcy){
		this.cdtratgagcy = cdtratgagcy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  评级到期日
	 */
	@Column(name ="CDTRATGDUEDT",nullable=true,scale=6)
	public java.util.Date getCdtratgduedt(){
		return this.cdtratgduedt;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  评级到期日
	 */
	public void setCdtratgduedt(java.util.Date cdtratgduedt){
		this.cdtratgduedt = cdtratgduedt;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  保证人地址
	 */
	@Column(name ="ADR",nullable=true,length=100)
	public String getAdr(){
		return this.adr;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  保证人地址
	 */
	public void setAdr(String adr){
		this.adr = adr;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  到期无条件支付承诺
	 */
	@Column(name ="UCONDLPRMSMRK",nullable=true,length=5)
	public String getUcondlprmsmrk(){
		return this.ucondlprmsmrk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  到期无条件支付承诺
	 */
	public void setUcondlprmsmrk(String ucondlprmsmrk){
		this.ucondlprmsmrk = ucondlprmsmrk;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  承兑协议编号
	 */
	@Column(name ="ACCPTNCAGRMTNB",nullable=true,length=30)
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
	 *@return: java.lang.String  是否代客户回复
	 */
	@Column(name ="DPTYMK",nullable=true,length=10)
	public String getDptymk(){
		return this.dptymk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否代客户回复
	 */
	public void setDptymk(String dptymk){
		this.dptymk = dptymk;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  拒付理由代码
	 */
	@Column(name ="DSHNRCD",nullable=true,length=10)
	public String getDshnrcd(){
		return this.dshnrcd;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  拒付理由代码
	 */
	public void setDshnrcd(String dshnrcd){
		this.dshnrcd = dshnrcd;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  拒付理由备注
	 */
	@Column(name ="DSHNRRSN",nullable=true,length=60)
	public String getDshnrrsn(){
		return this.dshnrrsn;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  拒付理由备注
	 */
	public void setDshnrrsn(String dshnrrsn){
		this.dshnrrsn = dshnrrsn;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  回复标记：SU00:同意签收；SU01：拒收
	 */
	@Column(name ="SGNUPMK",nullable=true,length=10)
	public String getSgnupmk(){
		return this.sgnupmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  回复标记：SU00:同意签收；SU01：拒收
	 */
	public void setSgnupmk(String sgnupmk){
		this.sgnupmk = sgnupmk;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  到期无条件支付委托:提示承兑专用 CC00含委托/承诺兑付
	 */
	@Column(name ="UCONDLCONSGNMTMRK",nullable=true,length=5)
	public String getUcondlconsgnmtmrk(){
		return this.ucondlconsgnmtmrk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  到期无条件支付委托:提示承兑专用 CC00含委托/承诺兑付
	 */
	public void setUcondlconsgnmtmrk(String ucondlconsgnmtmrk){
		this.ucondlconsgnmtmrk = ucondlconsgnmtmrk;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  贴现协议编号
	 */
	@Column(name ="DSCNTAGRMTNB",nullable=true,length=30)
	public String getDscntagrmtnb(){
		return this.dscntagrmtnb;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  贴现协议编号
	 */
	public void setDscntagrmtnb(String dscntagrmtnb){
		this.dscntagrmtnb = dscntagrmtnb;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  回复人备注
	 */
	@Column(name ="RMRKBYPROPSR",nullable=true,length=200)
	public String getRmrkbypropsr(){
		return this.rmrkbypropsr;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  回复人备注
	 */
	public void setRmrkbypropsr(String rmrkbypropsr){
		this.rmrkbypropsr = rmrkbypropsr;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  申请人备注
	 */
	@Column(name ="RMRKBYSGNR",nullable=true,length=200)
	public String getRmrkbysgnr(){
		return this.rmrkbysgnr;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  申请人备注
	 */
	public void setRmrkbysgnr(String rmrkbysgnr){
		this.rmrkbysgnr = rmrkbysgnr;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  代理回复标识
	 */
	@Column(name ="PRXYSGNTR",nullable=true,length=4)
	public String getPrxysgntr(){
		return this.prxysgntr;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  代理回复标识
	 */
	public void setPrxysgntr(String prxysgntr){
		this.prxysgntr = prxysgntr;
	}
	@Override
	public String toString() {
		return "AbBackbillinfo [rpdmk=" + rpdmk + ", bussdt=" + bussdt + ", intrstrate=" + intrstrate + ", amt=" + amt
				+ ", aoaccninfId=" + aoaccninfId + ", aoaccninfAc=" + aoaccninfAc + ", bankendrsmtmk=" + bankendrsmtmk
				+ ", sttlmmk=" + sttlmmk + ", rpdopendt=" + rpdopendt + ", rpdduedt=" + rpdduedt + ", rpdintrstrate="
				+ rpdintrstrate + ", rpdamt=" + rpdamt + ", txlctrctnb=" + txlctrctnb + ", invcnb=" + invcnb
				+ ", appldt=" + appldt + ", rsn=" + rsn + ", rcrsTp=" + rcrsTp + ", reqamt=" + reqamt + ", rcrsrsncd="
				+ rcrsrsncd + ", msgid=" + msgid + ", msdt=" + msdt + ", bustype=" + bustype + ", origNo=" + origNo
				+ ", reqRole=" + reqRole + ", reqNm=" + reqNm + ", reqCmonid=" + reqCmonid + ", reqAcctId=" + reqAcctId
				+ ", reqAcctsvcr=" + reqAcctsvcr + ", reqAcctsvcrAg=" + reqAcctsvcrAg + ", btchnb=" + btchnb
				+ ", recRole=" + recRole + ", recNm=" + recNm + ", recCmonid=" + recCmonid + ", recAcctId=" + recAcctId
				+ ", recAcctsvcr=" + recAcctsvcr + ", recAcctsvcrAg=" + recAcctsvcrAg + ", cdtratgs=" + cdtratgs
				+ ", cdtratgagcy=" + cdtratgagcy + ", cdtratgduedt=" + cdtratgduedt + ", adr=" + adr
				+ ", ucondlprmsmrk=" + ucondlprmsmrk + ", accptncagrmtnb=" + accptncagrmtnb + ", dptymk=" + dptymk
				+ ", dshnrcd=" + dshnrcd + ", dshnrrsn=" + dshnrrsn + ", sgnupmk=" + sgnupmk + ", ucondlconsgnmtmrk="
				+ ucondlconsgnmtmrk + ", dscntagrmtnb=" + dscntagrmtnb + ", rmrkbypropsr=" + rmrkbypropsr
				+ ", rmrkbysgnr=" + rmrkbysgnr + ", prxysgntr=" + prxysgntr + "]";
	}
	
}
