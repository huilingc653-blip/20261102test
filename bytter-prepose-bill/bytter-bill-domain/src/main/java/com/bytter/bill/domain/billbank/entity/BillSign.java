package com.bytter.bill.domain.billbank.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: 票据签收信息
 * @author onlineGenerator
 * @date 2017-06-21 15:36:42
 * @version 1.0
 *
 */
@Entity
@Table(name = "AB_BILL_RECEIVEBIZ", schema = "")
@SuppressWarnings("serial")
public class BillSign extends IdEntity implements java.io.Serializable {
	/**追索同意清偿金额*/
	private BigDecimal rcrsokmoney;
	/**保证人地址*/
	private String adr;
	/**业务申请人类别RC00接入行RC01企业RC02人民银行RC03被代理行RC04被代理财务公司RC05接入财务公司*/
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
	/**业务申请人电子签名*/
	private String elctrncsgntr;
	/**批次号*/
	private String btchnb;
	/**业务接收人类别：RC00接入行RC01企业RC02人民银行RC03被代理行RC04被代理财务公司RC05接入财务公司*/
	private String recRole;
	/**业务接收人名称*/
	private String recNm;
	/**业务接收人组织机构代码*/
	private String recCmonid;
	/**业务接收人账号*/
	private String recAcctId;
	/**接收银行信息*/
	private String recAcctTypeName;
	/**业务接收人开户行号*/
	private String recAcctsvcr;
	/**业务接收人承接行号*/
	private String recAcctsvcrAg;
	/**业务接收人单位ID*/
	private String recCorpId;
	/**回复类别*/
	private String replyType;
	/**回复日期*/
	private Date replyDt;
	/**回复标志SU00同意签收SU01拒绝签收*/
	private String sgnupmk;
	/**签收标识：SU00 通用回复同意；SU01 通用回复拒绝 */
	private String signUpFlag;
	//DC00	与自己有直接债权债务关系的持票人未履行约定义务；DC01	持票人以欺诈、偷盗或者胁迫等手段取得票据；DC02	持票人明知有欺诈、偷盗或者胁迫等情形，出于恶意取得票据；DC03	持票人明知债务人与出票人或者持票人的前手之间存在抗辩事由而取得票据；DC04	持票人因重大过失取得不符合《票据法》规定的票据；DC05	超过提示付款期；DC06	被法院冻结或收到法院止付通知书；DC07	票据未到期；DC08	商业承兑汇票承兑人账户余额不足。DC09	其他（必须注明）
	/**拒付理由代码*/
	private String dshnrcd;
	/**拒付理由备注*/
	private String dshnrrsn;
	/**处理状态：18待签收19签成功-2打回-3撤回*/
	private Integer status;
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
	/**报文id*/
	private String msgid;
	/**报文时间*/
	private Date msdt;
	/**原申请报文id*/
	private String orgnlmsgid;
	/**原申请报文时间*/
	private Date orgnlmsdt;
	/**报文编号：017提示保证002提示承兑003提示收票010背书申请012回购式贴现赎回018质押申请019质押解除020提示付款021逾期付款022追索通知023追索同意清偿*/
	private String msgtype;
	/**票据号码*/
	private String origNo;
	/**票据种类：10 银票；11 商票*/
	private Integer billType;
	/**起始日期*/
	private Date startDate;
	/**到期日期*/
	private Date endDate;
	/**票据金额*/
	private BigDecimal billMoney;
	/**票据状态：参照人行标准格式的81个业务状态编号*/
	private String billStatus;
	/**收款人*/
	private String payee;
	/**收款人账号*/
	private String payeeacc;
	/**收款人开户行号*/
	private String payeebankno;
	/**收款人开户名称*/
	private String payeebankName;
	/**出票人*/
	private String payer;
	/**出票人账号*/
	private String payeracc;
	/**出票人开户行行号*/
	private String payerbankno;
	/**出票人开户名称*/
	private String payerbankName;
	/**承兑人*/
	private String acceptor;
	/**承兑人账号*/
	private String acceptoracc;
	/**承兑人开户行行号*/
	private String acceptorbankno;
	/**承兑人开户银行名称*/
	private String acceptorbankName;
	/**信用等级*/
	private String cdtratgs;
	/**评级机构*/
	private String cdtratgagcy;
	/**评级到期日*/
	private Date cdtratgduedt;
	/**业务申请日期*/
	private Date bussdt;
	/**承兑协议编号*/
	private String accptncagrmtnb;
	/**交易合同编号*/
	private String txlctrctnb;
	/**发票号码*/
	private String invcnb;
	/**承兑日期*/
	private Date acptDt;
	/**到期无条件支付承诺 承兑回复必填 CC00含委托/承诺兑付*/
	private String ucondlprmsmrk;
	/**线上清算标记： SM00 开启线上清算；SM01 关闭线上清算*/
	private String sttlmmk;
	/**不得转让标记：EM00 可再转让；EM01 不得转让*/
	private String bankendrsmtmk;
	/**贴现种类：RM00 买断式；RM01 回购式*/
	private String rpdmk;
	/**贴现利率*/
	private BigDecimal intrstrate;
	/**贴现金额*/
	private BigDecimal amt;
	/**入账账号*/
	private String aoaccninfId;
	/**入账账号行号*/
	private String aoaccninfAc;
	/**赎回开放日*/
	private Date rpdopendt;
	/**赎回截止日*/
	private Date rpdduedt;
	/**申请赎回利率*/
	private BigDecimal rpdintrstrate;
	/**赎回金额*/
	private BigDecimal rpdamt;
	/**买方付息标志*/
	private Integer buyerInterestSign;
	/**买方付息单位*/
	private String buyerInterestUnit;
	/**买方付息账号*/
	private String buyerInterestAccount;
	/**贴出人备注*/
	private String rmrkbypropsr;
	/**追索通知日期*/
	private Date appldt;
	/**原因说明*/
	private String rsn;
	/**追索种类：RT00 拒付追索；RT01 非拒付追索*/
	private String rcrsTp;
	/**追索金额*/
	private BigDecimal reqamt;
	/**追索理由代码:RC00承兑人被依法宣告破产RC01承兑人因违法被责令终止活动（非拒付追索时填写）*/
	private String rcrsrsncd;
	/**备注*/
	private String rmk;
	/**主表ID*/
	private String vid;
	/**从表ID*/
	private String vvid;
	/**待签收数据标识*/
	private String serialId;
	private String billId;	 //外部系统票据ID
	private String newStmIssuofpprInd = "0";//		出票标志。0：老系统出票,   1：新系统出票
	private String blgrdbagcrclPrmtInd = "0";//		是否允许分包。1 是  0 否
	private String sbbllRngStrtSn = "0";//		子票区间开始序号。旧票据默认0，新票据 12位长度，不足12位前面补0。
	private String sbbllRngEndSn = "0";//				子票区间结束序号。旧票据默认0，新票据 12位长度，不足12位前面补0。
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
	@Column(name ="NEW_STM_ISSUOFPPR_IND",nullable=false,length=1)
	public String getNewStmIssuofpprInd() {
		return newStmIssuofpprInd;
	}
	public void setNewStmIssuofpprInd(String newStmIssuofpprInd) {
		this.newStmIssuofpprInd = newStmIssuofpprInd;
	}
	@Column(name ="BLGRDBAGCRCL_PRMT_IND",nullable=false,length=1)
	public String getBlgrdbagcrclPrmtInd() {
		return blgrdbagcrclPrmtInd;
	}
	public void setBlgrdbagcrclPrmtInd(String blgrdbagcrclPrmtInd) {
		this.blgrdbagcrclPrmtInd = blgrdbagcrclPrmtInd;
	}
	@Column(name ="SBBLL_RNG_STRT_SN",nullable=false,length=12)
	public String getSbbllRngStrtSn() {
		return sbbllRngStrtSn;
	}
	public void setSbbllRngStrtSn(String sbbllRngStrtSn) {
		this.sbbllRngStrtSn = sbbllRngStrtSn;
	}
	@Column(name ="SBBLL_RNG_END_SN",nullable=false,length=12)
	public String getSbbllRngEndSn() {
		return sbbllRngEndSn;
	}
	public void setSbbllRngEndSn(String sbbllRngEndSn) {
		this.sbbllRngEndSn = sbbllRngEndSn;
	}
	@Column(name ="BL_RMTR_UNN_ID_NO",nullable=true,length=32)
	public String getBlRmtrUnnIdNo() {
		return blRmtrUnnIdNo;
	}
	public void setBlRmtrUnnIdNo(String blRmtrUnnIdNo) {
		this.blRmtrUnnIdNo = blRmtrUnnIdNo;
	}
	@Column(name ="BL_ACPTR_ID_TPCD",nullable=true,length=4)
	public String getBlAcptrIdTpcd() {
		return blAcptrIdTpcd;
	}
	public void setBlAcptrIdTpcd(String blAcptrIdTpcd) {
		this.blAcptrIdTpcd = blAcptrIdTpcd;
	}
	@Column(name ="BL_RCVPYMTPS_ID_TPCD",nullable=true,length=4)
	public String getBlRcvpymtpsIdTpcd() {
		return blRcvpymtpsIdTpcd;
	}
	public void setBlRcvpymtpsIdTpcd(String blRcvpymtpsIdTpcd) {
		this.blRcvpymtpsIdTpcd = blRcvpymtpsIdTpcd;
	}
	@Column(name ="EBILL_BL_CRCL_INDCD",nullable=true,length=10)
	public String getEbillBlCrclIndcd() {
		return ebillBlCrclIndcd;
	}
	public void setEbillBlCrclIndcd(String ebillBlCrclIndcd) {
		this.ebillBlCrclIndcd = ebillBlCrclIndcd;
	}
	@Column(name ="INTPYMTPRT_WAY",nullable=true,length=4)
	public String getIntpymtprtWay() {
		return intpymtprtWay;
	}
	public void setIntpymtprtWay(String intpymtprtWay) {
		this.intpymtprtWay = intpymtprtWay;
	}
	@Column(name ="BUYERPAYINTRCNTNO",nullable=true,length=32)
	public String getBuyerpayintrcntno() {
		return buyerpayintrcntno;
	}
	public void setBuyerpayintrcntno(String buyerpayintrcntno) {
		this.buyerpayintrcntno = buyerpayintrcntno;
	}
	@Column(name ="INTPYMT_PSN_NM",nullable=true,length=200)
	public String getIntpymtPsnNm() {
		return intpymtPsnNm;
	}
	public void setIntpymtPsnNm(String intpymtPsnNm) {
		this.intpymtPsnNm = intpymtPsnNm;
	}
	@Column(name ="INTPYMT_PSN_ACCNO",nullable=true,length=32)
	public String getIntpymtPsnAccno() {
		return intpymtPsnAccno;
	}
	public void setIntpymtPsnAccno(String intpymtPsnAccno) {
		this.intpymtPsnAccno = intpymtPsnAccno;
	}
	@Column(name ="BL_RCV_UNN_ID_NO",nullable=true,length=32)
	public String getBlRcvUnnIdNo() {
		return blRcvUnnIdNo;
	}
	public void setBlRcvUnnIdNo(String blRcvUnnIdNo) {
		this.blRcvUnnIdNo = blRcvUnnIdNo;
	}
	@Column(name ="BL_REQ_UNN_ID_NO",nullable=true,length=32)
	public String getBlReqUnnIdNo() {
		return blReqUnnIdNo;
	}
	public void setBlReqUnnIdNo(String blReqUnnIdNo) {
		this.blReqUnnIdNo = blReqUnnIdNo;
	}
	@Column(name ="BL_RCV_ID_TPCD",nullable=true,length=4)
	public String getBlRcvIdTpcd() {
		return blRcvIdTpcd;
	}
	public void setBlRcvIdTpcd(String blRcvIdTpcd) {
		this.blRcvIdTpcd = blRcvIdTpcd;
	}
	@Column(name ="BL_REQ_ID_TPCD",nullable=true,length=4)
	public String getBlReqIdTpcd() {
		return blReqIdTpcd;
	}
	public void setBlReqIdTpcd(String blReqIdTpcd) {
		this.blReqIdTpcd = blReqIdTpcd;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  追索同意清偿金额
	 */
	@Column(name ="RCRSOKMONEY",nullable=true,scale=2,length=15)
	public BigDecimal getRcrsokmoney(){
		return this.rcrsokmoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  追索同意清偿金额
	 */
	public void setRcrsokmoney(BigDecimal rcrsokmoney){
		this.rcrsokmoney = rcrsokmoney;
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
	 *@return: java.lang.String  业务申请人类别RC00接入行RC01企业RC02人民银行RC03被代理行RC04被代理财务公司RC05接入财务公司
	 */
	@Column(name ="REQ_ROLE",nullable=true,length=12)
	public String getReqRole(){
		return this.reqRole;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务申请人类别RC00接入行RC01企业RC02人民银行RC03被代理行RC04被代理财务公司RC05接入财务公司
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
	 *@return: java.lang.String  业务申请人电子签名
	 */
	@Column(name ="ELCTRNCSGNTR",nullable=true,length=2000)
	public String getElctrncsgntr(){
		return this.elctrncsgntr;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务申请人电子签名
	 */
	public void setElctrncsgntr(String elctrncsgntr){
		this.elctrncsgntr = elctrncsgntr;
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
	 *@return: java.lang.String  业务接收人类别：RC00接入行RC01企业RC02人民银行RC03被代理行RC04被代理财务公司RC05接入财务公司
	 */
	@Column(name ="REC_ROLE",nullable=true,length=12)
	public String getRecRole(){
		return this.recRole;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务接收人类别：RC00接入行RC01企业RC02人民银行RC03被代理行RC04被代理财务公司RC05接入财务公司
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
	@Column(name = "REC_ACCT_TYPE_NAME",nullable=true,length=200)

	public String getRecAcctTypeName() {
		return recAcctTypeName;
	}

	public void setRecAcctTypeName(String recAcctTypeName) {
		this.recAcctTypeName = recAcctTypeName;
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
	 *@return: java.lang.String  业务接收人单位ID
	 */
	@Column(name ="REC_CORP_ID",nullable=true,length=32)
	public String getRecCorpId(){
		return this.recCorpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务接收人单位ID
	 */
	public void setRecCorpId(String recCorpId){
		this.recCorpId = recCorpId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  回复类别
	 */
	@Column(name ="REPLY_TYPE",nullable=true,length=10)
	public String getReplyType(){
		return this.replyType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  回复类别
	 */
	public void setReplyType(String replyType){
		this.replyType = replyType;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  回复日期
	 */
	@Column(name ="REPLY_DT",nullable=true,scale=6)
	public Date getReplyDt(){
		return this.replyDt;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  回复日期
	 */
	public void setReplyDt(Date replyDt){
		this.replyDt = replyDt;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  回复标志SU00同意签收SU01拒绝签收
	 */
	@Column(name ="SGNUPMK",nullable=true,length=10)
	public String getSgnupmk(){
		return this.sgnupmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  回复标志SU00同意签收SU01拒绝签收
	 */
	public void setSgnupmk(String sgnupmk){
		this.sgnupmk = sgnupmk;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  签收标识：SU00 通用回复同意；SU01 通用回复拒绝
	 */
	@Column(name ="SIGN_UP_FLAG",nullable=true,length=50)
	public String getSignUpFlag(){
		return this.signUpFlag;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  签收标识：SU00 通用回复同意；SU01 通用回复拒绝
	 */
	public void setSignUpFlag(String signUpFlag){
		this.signUpFlag = signUpFlag;
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
	@Column(name ="DSHNRRSN",nullable=true,length=100)
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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  处理状态：18待签收19签成功-2打回-3撤回
	 */
	@Column(name ="STATUS",nullable=true,length=10)
	public Integer getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  处理状态：18待签收19签成功-2打回-3撤回
	 */
	public void setStatus(Integer status){
		this.status = status;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人名称
	 */
	@Column(name ="CREATE_NAME",nullable=true,length=50)
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
	 *@return: java.lang.String  更新人名称
	 */
	@Column(name ="UPDATE_NAME",nullable=true,length=50)
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
	@Column(name ="MSDT",nullable=true)
	public Date getMsdt(){
		return this.msdt;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  报文时间
	 */
	public void setMsdt(Date msdt){
		this.msdt = msdt;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  原申请报文id
	 */
	@Column(name ="ORGNLMSGID",nullable=true,length=30)
	public String getOrgnlmsgid(){
		return this.orgnlmsgid;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  原申请报文id
	 */
	public void setOrgnlmsgid(String orgnlmsgid){
		this.orgnlmsgid = orgnlmsgid;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  原申请报文时间
	 */
	@Column(name ="ORGNLMSDT",nullable=true)
	public Date getOrgnlmsdt(){
		return this.orgnlmsdt;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  原申请报文时间
	 */
	public void setOrgnlmsdt(Date orgnlmsdt){
		this.orgnlmsdt = orgnlmsdt;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  报文编号：017提示保证002提示承兑003提示收票010背书申请012回购式贴现赎回018质押申请019质押解除020提示付款021逾期付款022追索通知023追索同意清偿
	 */
	@Column(name ="MSGTYPE",nullable=true,length=3)
	public String getMsgtype(){
		return this.msgtype;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  报文编号：017提示保证002提示承兑003提示收票010背书申请012回购式贴现赎回018质押申请019质押解除020提示付款021逾期付款022追索通知023追索同意清偿
	 */
	public void setMsgtype(String msgtype){
		this.msgtype = msgtype;
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
	 *@return: java.lang.String  票据种类：10 银票；11 商票
	 */
	@Column(name ="BILL_TYPE",nullable=true,length=8)
	public Integer getBillType(){
		return this.billType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  票据种类：10 银票；11 商票
	 */
	public void setBillType(Integer billType){
		this.billType = billType;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  起始日期
	 */
	@Column(name ="START_DATE",nullable=true)
	public Date getStartDate(){
		return this.startDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  起始日期
	 */
	public void setStartDate(Date startDate){
		this.startDate = startDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  到期日期
	 */
	@Column(name ="END_DATE",nullable=true)
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
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  票据金额
	 */
	@Column(name ="BILL_MONEY",nullable=true,scale=2,length=15)
	public BigDecimal getBillMoney(){
		return this.billMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  票据金额
	 */
	public void setBillMoney(BigDecimal billMoney){
		this.billMoney = billMoney;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  票据状态：参照人行标准格式的81个业务状态编号
	 */
	@Column(name ="BILL_STATUS",nullable=true,length=10)
	public String getBillStatus(){
		return this.billStatus;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  票据状态：参照人行标准格式的81个业务状态编号
	 */
	public void setBillStatus(String billStatus){
		this.billStatus = billStatus;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款人
	 */
	@Column(name ="PAYEE",nullable=true,length=60)
	public String getPayee(){
		return this.payee;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款人
	 */
	public void setPayee(String payee){
		this.payee = payee;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款人账号
	 */
	@Column(name ="PAYEEACC",nullable=true,length=32)
	public String getPayeeacc(){
		return this.payeeacc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款人账号
	 */
	public void setPayeeacc(String payeeacc){
		this.payeeacc = payeeacc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款人开户行号
	 */
	@Column(name ="PAYEEBANKNO",nullable=true,length=32)
	public String getPayeebankno(){
		return this.payeebankno;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款人开户行号
	 */
	public void setPayeebankno(String payeebankno){
		this.payeebankno = payeebankno;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  出票人
	 */
	@Column(name ="PAYER",nullable=true,length=60)
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
	@Column(name ="PAYERACC",nullable=true,length=32)
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
	 *@return: java.lang.String  出票人开户行行号
	 */
	@Column(name ="PAYERBANKNO",nullable=true,length=32)
	public String getPayerbankno(){
		return this.payerbankno;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  出票人开户行行号
	 */
	public void setPayerbankno(String payerbankno){
		this.payerbankno = payerbankno;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  承兑人
	 */
	@Column(name ="ACCEPTOR",nullable=true,length=60)
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
	@Column(name ="ACCEPTORACC",nullable=true,length=32)
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
	 *@return: java.lang.String  承兑人行户行行号
	 */
	@Column(name ="ACCEPTORBANKNO",nullable=true,length=32)
	public String getAcceptorbankno(){
		return this.acceptorbankno;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  承兑人行户行行号
	 */
	public void setAcceptorbankno(String acceptorbankno){
		this.acceptorbankno = acceptorbankno;
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
	public Date getCdtratgduedt(){
		return this.cdtratgduedt;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  评级到期日
	 */
	public void setCdtratgduedt(Date cdtratgduedt){
		this.cdtratgduedt = cdtratgduedt;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  业务申请日期
	 */
	@Column(name ="BUSSDT",nullable=true)
	public Date getBussdt(){
		return this.bussdt;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  业务申请日期
	 */
	public void setBussdt(Date bussdt){
		this.bussdt = bussdt;
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
	 *@return: java.lang.String  交易合同编号
	 */
	@Column(name ="TXLCTRCTNB",nullable=true,length=150)
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
	@Column(name ="INVCNB",nullable=true,length=150)
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  承兑日期
	 */
	@Column(name ="ACCEPTORDATE")
	public Date getAcptDt(){
		return this.acptDt;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  承兑日期
	 */
	public void setAcptDt(Date acptDt){
		this.acptDt = acptDt;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  到期无条件支付承诺 承兑回复必填 CC00含委托/承诺兑付
	 */
	@Column(name ="UCONDLPRMSMRK",nullable=true,length=5)
	public String getUcondlprmsmrk(){
		return this.ucondlprmsmrk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  到期无条件支付承诺 承兑回复必填 CC00含委托/承诺兑付
	 */
	public void setUcondlprmsmrk(String ucondlprmsmrk){
		this.ucondlprmsmrk = ucondlprmsmrk;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  线上清算标记： SM00 开启线上清算；SM01 关闭线上清算
	 */
	@Column(name ="STTLMMK",nullable=true,length=4)
	public String getSttlmmk(){
		return this.sttlmmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  线上清算标记： SM00 开启线上清算；SM01 关闭线上清算
	 */
	public void setSttlmmk(String sttlmmk){
		this.sttlmmk = sttlmmk;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  不得转让标记：EM00 可再转让；EM01 不得转让
	 */
	@Column(name ="BANKENDRSMTMK",nullable=true,length=4)
	public String getBankendrsmtmk(){
		return this.bankendrsmtmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  不得转让标记：EM00 可再转让；EM01 不得转让
	 */
	public void setBankendrsmtmk(String bankendrsmtmk){
		this.bankendrsmtmk = bankendrsmtmk;
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
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  贴现利率
	 */
	@Column(name ="INTRSTRATE",nullable=true,scale=6,length=8)
	public BigDecimal getIntrstrate(){
		return this.intrstrate;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  贴现利率
	 */
	public void setIntrstrate(BigDecimal intrstrate){
		this.intrstrate = intrstrate;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  贴现金额
	 */
	@Column(name ="AMT",nullable=true,scale=2,length=15)
	public BigDecimal getAmt(){
		return this.amt;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  贴现金额
	 */
	public void setAmt(BigDecimal amt){
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  赎回开放日
	 */
	@Column(name ="RPDOPENDT",nullable=true)
	public Date getRpdopendt(){
		return this.rpdopendt;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  赎回开放日
	 */
	public void setRpdopendt(Date rpdopendt){
		this.rpdopendt = rpdopendt;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  赎回截止日
	 */
	@Column(name ="RPDDUEDT",nullable=true)
	public Date getRpdduedt(){
		return this.rpdduedt;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  赎回截止日
	 */
	public void setRpdduedt(Date rpdduedt){
		this.rpdduedt = rpdduedt;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  申请赎回利率
	 */
	@Column(name ="RPDINTRSTRATE",nullable=true,scale=4,length=2)
	public BigDecimal getRpdintrstrate(){
		return this.rpdintrstrate;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  申请赎回利率
	 */
	public void setRpdintrstrate(BigDecimal rpdintrstrate){
		this.rpdintrstrate = rpdintrstrate;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  赎回金额
	 */
	@Column(name ="RPDAMT",nullable=true,scale=2,length=15)
	public BigDecimal getRpdamt(){
		return this.rpdamt;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  赎回金额
	 */
	public void setRpdamt(BigDecimal rpdamt){
		this.rpdamt = rpdamt;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  买方付息标志
	 */
	@Column(name ="BUYER_INTEREST_SIGN",nullable=true,length=1)
	public Integer getBuyerInterestSign(){
		return this.buyerInterestSign;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  买方付息标志
	 */
	public void setBuyerInterestSign(Integer buyerInterestSign){
		this.buyerInterestSign = buyerInterestSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  买方付息单位
	 */
	@Column(name ="BUYER_INTEREST_UNIT",nullable=true,length=100)
	public String getBuyerInterestUnit(){
		return this.buyerInterestUnit;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  买方付息单位
	 */
	public void setBuyerInterestUnit(String buyerInterestUnit){
		this.buyerInterestUnit = buyerInterestUnit;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  买方付息账号
	 */
	@Column(name ="BUYER_INTEREST_ACCOUNT",nullable=true,length=50)
	public String getBuyerInterestAccount(){
		return this.buyerInterestAccount;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  买方付息账号
	 */
	public void setBuyerInterestAccount(String buyerInterestAccount){
		this.buyerInterestAccount = buyerInterestAccount;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  贴出人备注
	 */
	@Column(name ="RMRKBYPROPSR",nullable=true,length=256)
	public String getRmrkbypropsr(){
		return this.rmrkbypropsr;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  贴出人备注
	 */
	public void setRmrkbypropsr(String rmrkbypropsr){
		this.rmrkbypropsr = rmrkbypropsr;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  追索通知日期
	 */
	@Column(name ="APPLDT",nullable=true)
	public Date getAppldt(){
		return this.appldt;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  追索通知日期
	 */
	public void setAppldt(Date appldt){
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
	@Column(name ="REQAMT",nullable=true,scale=2,length=15)
	public BigDecimal getReqamt(){
		return this.reqamt;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  追索金额
	 */
	public void setReqamt(BigDecimal reqamt){
		this.reqamt = reqamt;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  追索理由代码:RC00承兑人被依法宣告破产RC01承兑人因违法被责令终止活动（非拒付追索时填写）
	 */
	@Column(name ="RCRSRSNCD",nullable=true,length=10)
	public String getRcrsrsncd(){
		return this.rcrsrsncd;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  追索理由代码:RC00承兑人被依法宣告破产RC01承兑人因违法被责令终止活动（非拒付追索时填写）
	 */
	public void setRcrsrsncd(String rcrsrsncd){
		this.rcrsrsncd = rcrsrsncd;
	}
	@Column(name ="RMK")
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	@Column(name ="BILL_ID")
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	@Transient
	public String getPayeebankName() {
		return payeebankName;
	}
	public void setPayeebankName(String payeebankName) {
		this.payeebankName = payeebankName;
	}
	@Column(name ="PAYER_BANK_NAME")
	public String getPayerbankName() {
		return payerbankName;
	}
	public void setPayerbankName(String payerbankName) {
		this.payerbankName = payerbankName;
	}
	@Transient
	public String getAcceptorbankName() {
		return acceptorbankName;
	}
	public void setAcceptorbankName(String acceptorbankName) {
		this.acceptorbankName = acceptorbankName;
	}
	@Transient
	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
	}
	@Transient
	public String getVvid() {
		return vvid;
	}
	public void setVvid(String vvid) {
		this.vvid = vvid;
	}
	@Column(name ="SERIAL_ID")
	public String getSerialId() {
		return serialId;
	}
	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}

	@Override
	public String toString() {
		return "BillSign{" +
				"rcrsokmoney=" + rcrsokmoney +
				", adr='" + adr + '\'' +
				", reqRole='" + reqRole + '\'' +
				", reqNm='" + reqNm + '\'' +
				", reqCmonid='" + reqCmonid + '\'' +
				", reqAcctId='" + reqAcctId + '\'' +
				", reqAcctsvcr='" + reqAcctsvcr + '\'' +
				", reqAcctsvcrAg='" + reqAcctsvcrAg + '\'' +
				", elctrncsgntr='" + elctrncsgntr + '\'' +
				", btchnb='" + btchnb + '\'' +
				", recRole='" + recRole + '\'' +
				", recNm='" + recNm + '\'' +
				", recCmonid='" + recCmonid + '\'' +
				", recAcctId='" + recAcctId + '\'' +
				", recAcctTypeName='" + recAcctTypeName + '\'' +
				", recAcctsvcr='" + recAcctsvcr + '\'' +
				", recAcctsvcrAg='" + recAcctsvcrAg + '\'' +
				", recCorpId='" + recCorpId + '\'' +
				", replyType='" + replyType + '\'' +
				", replyDt=" + replyDt +
				", sgnupmk='" + sgnupmk + '\'' +
				", signUpFlag='" + signUpFlag + '\'' +
				", dshnrcd='" + dshnrcd + '\'' +
				", dshnrrsn='" + dshnrrsn + '\'' +
				", status=" + status +
				", createName='" + createName + '\'' +
				", createBy='" + createBy + '\'' +
				", createDate=" + createDate +
				", updateName='" + updateName + '\'' +
				", updateBy='" + updateBy + '\'' +
				", updateDate=" + updateDate +
				", msgid='" + msgid + '\'' +
				", msdt=" + msdt +
				", orgnlmsgid='" + orgnlmsgid + '\'' +
				", orgnlmsdt=" + orgnlmsdt +
				", msgtype='" + msgtype + '\'' +
				", origNo='" + origNo + '\'' +
				", billType=" + billType +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", billMoney=" + billMoney +
				", billStatus='" + billStatus + '\'' +
				", payee='" + payee + '\'' +
				", payeeacc='" + payeeacc + '\'' +
				", payeebankno='" + payeebankno + '\'' +
				", payeebankName='" + payeebankName + '\'' +
				", payer='" + payer + '\'' +
				", payeracc='" + payeracc + '\'' +
				", payerbankno='" + payerbankno + '\'' +
				", payerbankName='" + payerbankName + '\'' +
				", acceptor='" + acceptor + '\'' +
				", acceptoracc='" + acceptoracc + '\'' +
				", acceptorbankno='" + acceptorbankno + '\'' +
				", acceptorbankName='" + acceptorbankName + '\'' +
				", cdtratgs='" + cdtratgs + '\'' +
				", cdtratgagcy='" + cdtratgagcy + '\'' +
				", cdtratgduedt=" + cdtratgduedt +
				", bussdt=" + bussdt +
				", accptncagrmtnb='" + accptncagrmtnb + '\'' +
				", txlctrctnb='" + txlctrctnb + '\'' +
				", invcnb='" + invcnb + '\'' +
				", acptDt=" + acptDt +
				", ucondlprmsmrk='" + ucondlprmsmrk + '\'' +
				", sttlmmk='" + sttlmmk + '\'' +
				", bankendrsmtmk='" + bankendrsmtmk + '\'' +
				", rpdmk='" + rpdmk + '\'' +
				", intrstrate=" + intrstrate +
				", amt=" + amt +
				", aoaccninfId='" + aoaccninfId + '\'' +
				", aoaccninfAc='" + aoaccninfAc + '\'' +
				", rpdopendt=" + rpdopendt +
				", rpdduedt=" + rpdduedt +
				", rpdintrstrate=" + rpdintrstrate +
				", rpdamt=" + rpdamt +
				", buyerInterestSign=" + buyerInterestSign +
				", buyerInterestUnit='" + buyerInterestUnit + '\'' +
				", buyerInterestAccount='" + buyerInterestAccount + '\'' +
				", rmrkbypropsr='" + rmrkbypropsr + '\'' +
				", appldt=" + appldt +
				", rsn='" + rsn + '\'' +
				", rcrsTp='" + rcrsTp + '\'' +
				", reqamt=" + reqamt +
				", rcrsrsncd='" + rcrsrsncd + '\'' +
				", rmk='" + rmk + '\'' +
				", vid='" + vid + '\'' +
				", vvid='" + vvid + '\'' +
				", serialId='" + serialId + '\'' +
				", billId='" + billId + '\'' +
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
				'}';
	}
}
