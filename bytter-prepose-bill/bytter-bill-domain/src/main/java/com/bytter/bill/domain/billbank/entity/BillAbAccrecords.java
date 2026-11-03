package com.bytter.bill.domain.billbank.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: 票据流水信息
 * @author onlineGenerator
 * @date 2017-06-21 15:36:09
 * @version 1.0
 *
 */
@Entity
@Table(name = "AB_ACCRECORDS", schema = "")
@SuppressWarnings("serial")
public class BillAbAccrecords extends IdEntity implements java.io.Serializable {
	/**票据主表ID*/
	private String vid;
	/**票据从表id*/
	private String vvid;
	/**票据签收表id*/
	private String recid;
	/**票据划拨表ID*/
	private String trid;
	/**上一流水ID*/
	private String lastid;
	/**上一票据状态代码*/
	private String lastbillcode;
	/**处理标志：0、未完成；1可用；2已结束*/
	private String validSign;
	/**审批处理状态：-2已删除；-1打回；0 暂存；10提交；11-94审批中；95 已完成*/
	private Integer status;
	/**操作状态：15待复核；16待发送；17已发送待回应；18已回应待签收；19已签收成功业务完成；-1发送失败；-2打回；-3撤回；-4拒签*/
	private String optStatus;
	/**是否发送指令标志：0否； 1是 ；*/
	private String sendSign;
	/**返回代码*/
	private String returnCode;
	/**处理结果*/
	private String returnMsg;
	/**业务类型*/
	private String briefId;
	/**内部业务标志：0  否；1 是*/
	private Integer insideSing;
	/**所属库存:10单位库存11中心代管12银行托管*/
	private String enterStockNew;
	/**所属库存:10单位库存11中心代管12银行托管*/
	private String enterStockOld;
	/**保证类型:01开票保证，02承兑保证；03背书保证*/
	private String bailType;
	/**不得转让标记：EM00 可再转让；EM01 不得转让*/
	private String bankendrsmtmk="EM00";
	/**申请日期*/
	private Date applyDate;
	/**业务归属日期*/
	private Date businessDate;
	/**追索同意清偿金额*/
	private java.math.BigDecimal rcrsokmoney;
	/**前手*/
	private String cnAccrecords;
	/**SU00-同意  SU01-拒绝*/
	private String signUpFlag;
	/**摘要*/
	private String abs;
	/**备注*/
	private String rmk;
	/**授信代码*/
	private String creditCode;
	/**预算科目*/
	private String budgetCode;
	/**保证金账户*/
	private String bailAcc;
	/**保证金*/
	private java.math.BigDecimal bailmoney;
	/**保证金比例*/
	private java.math.BigDecimal bailpercent;
	/**保证金利率*/
	private java.math.BigDecimal bailrate;
	/**手续费*/
	private java.math.BigDecimal feemoney;
	/**追索理由代码:RC00承兑人被依法宣告破产RC01承兑人因违法被责令终止活动（非拒付追索时填写）*/
	private String rcrsrsncd;
	/**追索种类：RT00 拒付追索；RT01 非拒付追索*/
	private String rcrsTp;
	/**收款或付款确认人*/
	private String signerok;
	/**银行接口代码*/
	private String bifCode;
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
	/**业务接收人类别：RC00接入行RC01企业RC02人民银行RC03被代理行RC04被代理财务公司RC05接入财务公司*/
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
	/**保证人地址*/
	private String adr;
	/**开票信用等级*/
	private String cdtRatgs;
	/**开票评级机构*/
	private String cdtRatgGgcy;
	/**开票评级到期日*/
	private Date cdtRatgDuedt;
	private Integer billFlag;
	/**清算方式 SM00线上清算、SMO1线下清算*/
	private String sttlmmk;
	private String billId;//外围系统票据ID
	private String msgId;//发送指令消息ID
	/**票面金额*/
	private java.math.BigDecimal billMoney;
	private String pdId;
	/** 票据来源：10收票登记11背书登记12质押登记 */
	private String billSource;
	/** 是否需要经办 0或空 否  1是 */
	private String isHandling;
	private String reqSocNo;//业务申请人社会统一信用代码
	private String recSocNo;//业务接收人社会统一信用代码
	private String recDghType;//业务接收人识别类型。DT01 票据账户DT02 银行账户默认为DT02 “银行账户”
	private Date settledate;//结息日期
	private String settletype;//清算类型：CT01 全额清算，CT02净额清算，默全额清算
	private String lastCirculateCode;//票据上一票据流通状态
	private String lastBlBsnTpcd;//上一票据业务代码
	private String splitFlg;//SPLIT_FLG  此笔交易是否拆分，1是，其他为未拆
	private String oldAccId;//OLD_ACC_ID  记录拆分前的流水id，若是未拆分为空

	private String copyAccId;//目前执行可操作单据ID


	/**
	1是复制数据2是批量拆分数据
	 */
	private String copyFlag;

	private String excId;

	/**最终审批人*/
	private String finalApprover;

	@Column(name ="exc_Id")
	public String getExcId() {
		return excId;
	}

	public void setExcId(String excId) {
		this.excId = excId;
	}

	@Column(name ="SPLIT_FLG")
	public String getSplitFlg() {
		return splitFlg;
	}
	@Column(name ="OLD_ACC_ID")
	public String getOldAccId() {
		return oldAccId;
	}
	public void setSplitFlg(String splitFlg) {
		this.splitFlg = splitFlg;
	}
	public void setOldAccId(String oldAccId) {
		this.oldAccId = oldAccId;
	}
	@Column(name ="LAST_BL_BSN_TPCD")
	public String getLastBlBsnTpcd() {
		return lastBlBsnTpcd;
	}
	public void setLastBlBsnTpcd(String lastBlBsnTpcd) {
		this.lastBlBsnTpcd = lastBlBsnTpcd;
	}
	@Column(name ="LAST_CIRCULATE_CODE")
	public String getLastCirculateCode() {
		return lastCirculateCode;
	}
	public void setLastCirculateCode(String lastCirculateCode) {
		this.lastCirculateCode = lastCirculateCode;
	}
	@Column(name ="REQ_SOC_NO")
	public String getReqSocNo() {
		return reqSocNo;
	}
	public void setReqSocNo(String reqSocNo) {
		this.reqSocNo = reqSocNo;
	}
	@Column(name ="REC_SOC_NO")
	public String getRecSocNo() {
		return recSocNo;
	}
	public void setRecSocNo(String recSocNo) {
		this.recSocNo = recSocNo;
	}
	@Column(name ="REC_DGH_TYPE")
	public String getRecDghType() {
		return recDghType;
	}
	public void setRecDghType(String recDghType) {
		this.recDghType = recDghType;
	}
	@Column(name ="SETTLEDATE")
	public Date getSettledate() {
		return settledate;
	}
	public void setSettledate(Date settledate) {
		this.settledate = settledate;
	}
	@Column(name ="SETTLETYPE")
	public String getSettletype() {
		return settletype;
	}
	public void setSettletype(String settletype) {
		this.settletype = settletype;
	}
	@Column(name ="CDT_RATGS")
	public String getCdtRatgs() {
		return cdtRatgs;
	}
	public void setCdtRatgs(String cdtRatgs) {
		this.cdtRatgs = cdtRatgs;
	}
	@Column(name ="CDT_RATG_AGCY")
	public String getCdtRatgGgcy() {
		return cdtRatgGgcy;
	}
	public void setCdtRatgGgcy(String cdtRatgGgcy) {
		this.cdtRatgGgcy = cdtRatgGgcy;
	}
	@Column(name ="CDT_RATG_DUEDT")
	public Date getCdtRatgDuedt() {
		return cdtRatgDuedt;
	}
	public void setCdtRatgDuedt(Date cdtRatgDuedt) {
		this.cdtRatgDuedt = cdtRatgDuedt;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  票据主表ID
	 */
	@Column(name ="VID",nullable=true,length=32)
	public String getVid(){
		return this.vid;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  票据主表ID
	 */
	public void setVid(String vid){
		this.vid = vid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  票据从表id
	 */
	@Column(name ="VVID",nullable=true,length=32)
	public String getVvid(){
		return this.vvid;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  票据从表id
	 */
	public void setVvid(String vvid){
		this.vvid = vvid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  票据签收表id
	 */
	@Column(name ="RECID",nullable=true,length=32)
	public String getRecid(){
		return this.recid;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  票据签收表id
	 */
	public void setRecid(String recid){
		this.recid = recid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  票据划拨表ID
	 */
	@Column(name ="TRID",nullable=true,length=32)
	public String getTrid(){
		return this.trid;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  票据划拨表ID
	 */
	public void setTrid(String trid){
		this.trid = trid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  上一流水ID
	 */
	@Column(name ="LASTID",nullable=true,length=32)
	public String getLastid(){
		return this.lastid;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  上一流水ID
	 */
	public void setLastid(String lastid){
		this.lastid = lastid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  上一票据状态代码
	 */
	@Column(name ="LASTBILLCODE",nullable=true,length=10)
	public String getLastbillcode(){
		return this.lastbillcode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  上一票据状态代码
	 */
	public void setLastbillcode(String lastbillcode){
		this.lastbillcode = lastbillcode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String 处理标志：0、未完成；1可用；2已结束
	 */
	@Column(name ="VALID_SIGN",nullable=true,length=1)
	public String getValidSign() {
		return this.validSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String 处理标志：0、未完成；1可用；2已结束
	 */
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  审批处理状态：-2已删除；-1打回；0 暂存；10提交；11-94审批中；95 已完成
	 */
	@Column(name ="STATUS",nullable=true,length=10)
	public Integer getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  审批处理状态：-2已删除；-1打回；0 暂存；10提交；11-94审批中；95 已完成
	 */
	public void setStatus(Integer status){
		this.status = status;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  操作状态：15待复核；16待发送；17已发送待回应；18已回应待签收；19已签收成功业务完成；-1发送失败；-2打回；-3撤回；-4拒签
	 */
	@Column(name ="OPT_STATUS",nullable=true,length=10)
	public String getOptStatus(){
		return this.optStatus;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  操作状态：15待复核；16待发送；17已发送待回应；18已回应待签收；19已签收成功业务完成；-1发送失败；-2打回；-3撤回；-4拒签
	 */
	public void setOptStatus(String optStatus){
		this.optStatus = optStatus;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否发送指令标志：0否； 1是 ；
	 */
	@Column(name ="SEND_SIGN",nullable=true,length=1)
	public String getSendSign(){
		return this.sendSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否发送指令标志：0否； 1是 ；
	 */
	public void setSendSign(String sendSign){
		this.sendSign = sendSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  返回代码
	 */
	@Column(name ="RETURN_CODE",nullable=true,length=10)
	public String getReturnCode(){
		return this.returnCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  返回代码
	 */
	public void setReturnCode(String returnCode){
		this.returnCode = returnCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  处理结果
	 */
	@Column(name ="RETURN_MSG",nullable=true,length=200)
	public String getReturnMsg(){
		return this.returnMsg;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  处理结果
	 */
	public void setReturnMsg(String returnMsg){
		this.returnMsg = returnMsg;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务类型
	 */
	@Column(name ="BRIEF_ID",nullable=true,length=10)
	public String getBriefId(){
		return this.briefId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务类型
	 */
	public void setBriefId(String briefId){
		this.briefId = briefId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  内部业务标志：0  否；1 是
	 */
	@Column(name ="INSIDE_SING",nullable=true,length=10)
	public Integer getInsideSing(){
		return this.insideSing;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  内部业务标志：0  否；1 是
	 */
	public void setInsideSing(Integer insideSing){
		this.insideSing = insideSing;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属库存:10单位库存11中心代管12银行托管
	 */
	@Column(name ="ENTER_STOCK_NEW",nullable=true,length=10)
	public String getEnterStockNew(){
		return this.enterStockNew;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属库存:10单位库存11中心代管12银行托管
	 */
	public void setEnterStockNew(String enterStockNew){
		this.enterStockNew = enterStockNew;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属库存:10单位库存11中心代管12银行托管
	 */
	@Column(name ="ENTER_STOCK_OLD",nullable=true,length=10)
	public String getEnterStockOld(){
		return this.enterStockOld;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属库存:10单位库存11中心代管12银行托管
	 */
	public void setEnterStockOld(String enterStockOld){
		this.enterStockOld = enterStockOld;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  保证类型:01开票保证，02承兑保证；03背书保证
	 */
	@Column(name ="BAIL_TYPE",nullable=true,length=10)
	public String getBailType(){
		return this.bailType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  保证类型:01开票保证，02承兑保证；03背书保证
	 */
	public void setBailType(String bailType){
		this.bailType = bailType;
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  申请日期
	 */
	@Column(name ="APPLY_DATE",nullable=true)
	public Date getApplyDate(){
		return this.applyDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  申请日期
	 */
	public void setApplyDate(Date applyDate){
		this.applyDate = applyDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  业务归属日期
	 */
	@Column(name ="BUSINESS_DATE",nullable=true)
	public Date getBusinessDate(){
		return this.businessDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  业务归属日期
	 */
	public void setBusinessDate(Date businessDate){
		this.businessDate = businessDate;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  追索同意清偿金额
	 */
	@Column(name ="RCRSOKMONEY",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getRcrsokmoney(){
		return this.rcrsokmoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  追索同意清偿金额
	 */
	public void setRcrsokmoney(java.math.BigDecimal rcrsokmoney){
		this.rcrsokmoney = rcrsokmoney;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  前手
	 */
	@Column(name ="CN_ACCRECORDS",nullable=true,length=240)
	public String getCnAccrecords(){
		return this.cnAccrecords;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  前手
	 */
	public void setCnAccrecords(String cnAccrecords){
		this.cnAccrecords = cnAccrecords;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  SU00-同意  SU01-拒绝
	 */
	@Column(name ="SIGN_UP_FLAG",nullable=true,length=50)
	public String getSignUpFlag(){
		return this.signUpFlag;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  SU00-同意  SU01-拒绝
	 */
	public void setSignUpFlag(String signUpFlag){
		this.signUpFlag = signUpFlag;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  摘要
	 */
	@Column(name ="ABS",nullable=true,length=100)
	public String getAbs(){
		return this.abs;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  摘要
	 */
	public void setAbs(String abs){
		this.abs = abs;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	@Column(name ="RMK",nullable=true,length=100)
	public String getRmk(){
		return this.rmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备注
	 */
	public void setRmk(String rmk){
		this.rmk = rmk;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  授信代码
	 */
	@Column(name ="CREDIT_CODE",nullable=true,length=30)
	public String getCreditCode(){
		return this.creditCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  授信代码
	 */
	public void setCreditCode(String creditCode){
		this.creditCode = creditCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  预算科目
	 */
	@Column(name ="BUDGET_CODE",nullable=true,length=30)
	public String getBudgetCode(){
		return this.budgetCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  预算科目
	 */
	public void setBudgetCode(String budgetCode){
		this.budgetCode = budgetCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  保证金账户
	 */
	@Column(name ="BAIL_ACC",nullable=true,length=30)
	public String getBailAcc(){
		return this.bailAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  保证金账户
	 */
	public void setBailAcc(String bailAcc){
		this.bailAcc = bailAcc;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  保证金
	 */
	@Column(name ="BAILMONEY",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBailmoney(){
		return this.bailmoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  保证金
	 */
	public void setBailmoney(java.math.BigDecimal bailmoney){
		this.bailmoney = bailmoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  保证金比例
	 */
	@Column(name ="BAILPERCENT",nullable=true,scale=6,length=8)
	public java.math.BigDecimal getBailpercent(){
		return this.bailpercent;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  保证金比例
	 */
	public void setBailpercent(java.math.BigDecimal bailpercent){
		this.bailpercent = bailpercent;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  保证金利率
	 */
	@Column(name ="BAILRATE",nullable=true,scale=6,length=8)
	public java.math.BigDecimal getBailrate(){
		return this.bailrate;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  保证金利率
	 */
	public void setBailrate(java.math.BigDecimal bailrate){
		this.bailrate = bailrate;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  手续费
	 */
	@Column(name ="FEEMONEY",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getFeemoney(){
		return this.feemoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  手续费
	 */
	public void setFeemoney(java.math.BigDecimal feemoney){
		this.feemoney = feemoney;
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款或付款确认人
	 */
	@Column(name ="SIGNEROK",nullable=true,length=30)
	public String getSignerok(){
		return this.signerok;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款或付款确认人
	 */
	public void setSignerok(String signerok){
		this.signerok = signerok;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行接口代码
	 */
	@Column(name ="BIF_CODE",nullable=true,length=10)
	public String getBifCode(){
		return this.bifCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行接口代码
	 */
	public void setBifCode(String bifCode){
		this.bifCode = bifCode;
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
	@Column(name ="STTLMMK")
	public String getSttlmmk() {
		return sttlmmk;
	}
	public void setSttlmmk(String sttlmmk) {
		this.sttlmmk = sttlmmk;
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
	@Column(name ="ADR")
	public String getAdr() {
		return adr;
	}
	public void setAdr(String adr) {
		this.adr = adr;
	}
	@Transient
	public Integer getBillFlag() {
		return billFlag;
	}
	public void setBillFlag(Integer billFlag) {
		this.billFlag = billFlag;
	}
	@Column(name ="BILL_ID")
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	@Column(name ="MSG_ID")
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	@Transient
	public java.math.BigDecimal getBillMoney() {
		return billMoney;
	}
	public void setBillMoney(java.math.BigDecimal billMoney) {
		this.billMoney = billMoney;
	}
	@Column(name ="PD_ID")
	public String getPdId() {
		return pdId;
	}
	public void setPdId(String pdId) {
		this.pdId = pdId;
	}
	/**
	 * 方法: 取得java.lang.Integer
	 *
	 * @return: java.lang.Integer 10收票登记11背书登记12质押登记
	 */
	@Column(name = "BILL_SOURCE")
	public String getBillSource() {
		return this.billSource;
	}
	/**
	 * 方法: 设置java.lang.Integer
	 *
	 * @param: java.lang.Integer
	 *             10收票登记11背书登记12质押登记
	 */
	public void setBillSource(String billSource) {
		this.billSource = billSource;
	}
	@Column(name = "IS_HANDLING")
	public String getIsHandling() {
		return isHandling;
	}
	public void setIsHandling(String isHandling) {
		this.isHandling = isHandling;
	}

	@Column(name = "COPY_FLAG")
	public String getCopyFlag() {
		return copyFlag;
	}

	public void setCopyFlag(String copyFlag) {
		this.copyFlag = copyFlag;
	}

	@Column(name = "COPY_ACC_ID")
	public String getCopyAccId() {
		return copyAccId;
	}

	public void setCopyAccId(String copyAccId) {
		this.copyAccId = copyAccId;
	}

	@Column(name = "FINAL_APPROVER ")
	public String getFinalApprover() {
		return finalApprover;
	}

	public void setFinalApprover(String finalApprover) {
		this.finalApprover = finalApprover;
	}

	@Override
	public String toString() {
		return "BillAbAccrecords{" +
				"vid='" + vid + '\'' +
				", vvid='" + vvid + '\'' +
				", recid='" + recid + '\'' +
				", trid='" + trid + '\'' +
				", lastid='" + lastid + '\'' +
				", lastbillcode='" + lastbillcode + '\'' +
				", validSign='" + validSign + '\'' +
				", status=" + status +
				", optStatus='" + optStatus + '\'' +
				", sendSign='" + sendSign + '\'' +
				", returnCode='" + returnCode + '\'' +
				", returnMsg='" + returnMsg + '\'' +
				", briefId='" + briefId + '\'' +
				", insideSing=" + insideSing +
				", enterStockNew='" + enterStockNew + '\'' +
				", enterStockOld='" + enterStockOld + '\'' +
				", bailType='" + bailType + '\'' +
				", bankendrsmtmk='" + bankendrsmtmk + '\'' +
				", applyDate=" + applyDate +
				", businessDate=" + businessDate +
				", rcrsokmoney=" + rcrsokmoney +
				", cnAccrecords='" + cnAccrecords + '\'' +
				", signUpFlag='" + signUpFlag + '\'' +
				", abs='" + abs + '\'' +
				", rmk='" + rmk + '\'' +
				", creditCode='" + creditCode + '\'' +
				", budgetCode='" + budgetCode + '\'' +
				", bailAcc='" + bailAcc + '\'' +
				", bailmoney=" + bailmoney +
				", bailpercent=" + bailpercent +
				", bailrate=" + bailrate +
				", feemoney=" + feemoney +
				", rcrsrsncd='" + rcrsrsncd + '\'' +
				", rcrsTp='" + rcrsTp + '\'' +
				", signerok='" + signerok + '\'' +
				", bifCode='" + bifCode + '\'' +
				", reqRole='" + reqRole + '\'' +
				", reqNm='" + reqNm + '\'' +
				", reqCmonid='" + reqCmonid + '\'' +
				", reqAcctId='" + reqAcctId + '\'' +
				", reqAcctsvcr='" + reqAcctsvcr + '\'' +
				", reqAcctsvcrAg='" + reqAcctsvcrAg + '\'' +
				", recRole='" + recRole + '\'' +
				", recNm='" + recNm + '\'' +
				", recCmonid='" + recCmonid + '\'' +
				", recAcctId='" + recAcctId + '\'' +
				", recAcctsvcr='" + recAcctsvcr + '\'' +
				", recAcctsvcrAg='" + recAcctsvcrAg + '\'' +
				", createName='" + createName + '\'' +
				", createBy='" + createBy + '\'' +
				", createDate=" + createDate +
				", updateName='" + updateName + '\'' +
				", updateBy='" + updateBy + '\'' +
				", updateDate=" + updateDate +
				", adr='" + adr + '\'' +
				", cdtRatgs='" + cdtRatgs + '\'' +
				", cdtRatgGgcy='" + cdtRatgGgcy + '\'' +
				", cdtRatgDuedt=" + cdtRatgDuedt +
				", billFlag=" + billFlag +
				", sttlmmk='" + sttlmmk + '\'' +
				", billId='" + billId + '\'' +
				", msgId='" + msgId + '\'' +
				", billMoney=" + billMoney +
				", pdId='" + pdId + '\'' +
				", billSource='" + billSource + '\'' +
				", isHandling='" + isHandling + '\'' +
				", reqSocNo='" + reqSocNo + '\'' +
				", recSocNo='" + recSocNo + '\'' +
				", recDghType='" + recDghType + '\'' +
				", settledate=" + settledate +
				", settletype='" + settletype + '\'' +
				", lastCirculateCode='" + lastCirculateCode + '\'' +
				", lastBlBsnTpcd='" + lastBlBsnTpcd + '\'' +
				", splitFlg='" + splitFlg + '\'' +
				", oldAccId='" + oldAccId + '\'' +
				", copyAccId='" + copyAccId + '\'' +
				", copyFlag='" + copyFlag + '\'' +
				", excId='" + excId + '\'' +
				'}';
	}
}
