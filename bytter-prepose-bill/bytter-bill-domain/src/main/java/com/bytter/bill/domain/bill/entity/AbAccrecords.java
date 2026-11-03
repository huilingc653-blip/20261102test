package com.bytter.bill.domain.bill.entity;
import com.bytter.bill.api.bill.dto.CorpDTO;
import com.bytter.core.base.entity.IdEntity;
import com.bytter.core.constant.ConstantBean;
import com.bytter.credit.dto.RatCpUrationDTO;
import com.bytter.credit.dto.RatUrationDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
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
public class AbAccrecords extends IdEntity implements java.io.Serializable {
	// 付款通知单单号（冗余）
	private String billNo;
	//付款通知单ID
	private String billheadId;
	// 付款账号（冗余）
	private String payerAcc;
	// 付款账号银行类型（冗余）
	private String payerAccBankTypeId;
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
	/**是否隐藏1是0否*/
	private String isHide;
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
	/**撤回类型*/
	private String recallType;
	/**不得转让标记：EM00 可再转让；EM01 不得转让*/
	private String bankendrsmtmk;
	/**申请日期*/
	private Date applyDate;
	/**业务归属日期*/
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
	private Date businessDate;
	/**追索同意清偿金额*/
	private java.math.BigDecimal rcrsokmoney;
	/**前手*/
	private String cnAccrecords;
	/**SU00-同意  SU01-拒绝*/
	private String signUpFlag;
	/**摘要  */
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
	// 业务申请人账号ID
	private String reqBankAccId;
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
	/**保证人地址 背书时候保存对内背书的单位*/
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
	/**全局业务ID*/
	private String busGlobalId;
	/** 附件ID */
	private String attachmentId;
	/** 票据来源：10收票登记11背书登记12质押登记14贴现登记15归集收入16上划收入17下拨收入 */
	private String billSource;
	private AbVoucherinfo vouInfo;
	private AbVoucherinfoVice vouViceInfo;
	private RatUrationDTO ratUrationDTO;
	private RatCpUrationDTO ratCpUrationDTO;
	/**
	 * 创建人是否为空 1为是，0和空位否
	 */
	private String isCreateByNull;
	/**
	 * 兑付方式:0或空线下兑付,1线上兑付'
	 */
	private String emitSttlmmk;
	private String corpId;
	private String netId;
	private String otherCorpId;
	private CorpDTO corpInfo;
	private String sysRaceCode;
	private ConstantBean<Integer> busOriType;
	private String billId;//外围系统票据ID
	private String msgId;//发送指令消息ID
	/**
	 * 交票人(领票人)
	 */
	private String trader;
	/**
	 * 逾期托收原因说明
	 */
	private String rsn;
	/** 票据池交易明细id */
	private String pdId;
	/** 自动签收标记 IS_AUTOSIGN 1自动0或空手动默认为0 */
	private String isAutosign;
	/** 是否需要经办 0或空 否  1是	*/
	private String isHandLing;
	/**
	 * 流水号
	 */
	private String serialNo;
	/**
	 * 是否同意背书请求
	 */
	private String isEndorse;
	
	
	/**
	 * 业务申请人社会统一信用代码
	 */
	private String reqSocNo;
	/**
	 * 业务接收人社会统一信用代码
	 */
	private String recSocNo;
	/**
	 * 业务接收人识别类型
	 */
	private String recDghType;
	private Date settledate;//结息日期 SETTLEDATE
	private String settletype;//结息类型 SETTLETYPE
	private String lastCirculateCode;//票据上一票据流通状态 LAST_CIRCULATE_CODE
	/**
	* 手续费比例
	 */
	private BigDecimal feemoneyScale;
	private String lastBlBsnTpcd;//上一票据业务代码   LAST_BL_BSN_TPCD
	private String splitFlg;//SPLIT_FLG  此笔交易是否拆分，1是，其他为未拆
	private String oldAccId;//OLD_ACC_ID  记录拆分前的流水id，若是未拆分为空

	/*
	1是复制数据2是批量拆分数据
	 */
	private String copyFlag = "0";
	private String copyAccId;

	private String excId;

	private String insideEndorseAutoRegSign;//内部交易线上背给线下自动登记标志：1-已登记 0-未登记

	/**
	 * 批次开票顺序号
	 */
	private Integer batchDtlNo ;

	/** 业务是否完成 0-否 1-是 */
	private String businessIsOver;

	/** 最终审批人 */
	private String finalApprover;

	@Column(name = "FINAL_APPROVER ")
	public String getFinalApprover() {
		return finalApprover;
	}

	public void setFinalApprover(String finalApprover) {
		this.finalApprover = finalApprover;
	}

	@Transient
	public String getBusinessIsOver() {
		return businessIsOver;
	}

	public void setBusinessIsOver(String businessIsOver) {
		this.businessIsOver = businessIsOver;
	}

	@Column(name = "BATCH_DTL_NO ")
	public Integer getBatchDtlNo() {
		return batchDtlNo;
	}

	public void setBatchDtlNo(Integer batchDtlNo) {
		this.batchDtlNo = batchDtlNo;
	}

	@Column(name = "INSIDE_ENDORSE_AUTO_REG_SIGN")
	public String getInsideEndorseAutoRegSign() {
		return insideEndorseAutoRegSign;
	}

	public void setInsideEndorseAutoRegSign(String insideEndorseAutoRegSign) {
		this.insideEndorseAutoRegSign = insideEndorseAutoRegSign;
	}

	@Column(name = "exc_Id")
	public String getExcId() {
		return excId;
	}

	public void setExcId(String excId) {
		this.excId = excId;
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
	@Column(name = "feemoney_Scale")
	public BigDecimal getFeemoneyScale() {
		return feemoneyScale;
	}
	public void setFeemoneyScale(BigDecimal feemoneyScale) {
		this.feemoneyScale = feemoneyScale;
	}
	@Column(name = "SPLIT_FLG")
	public String getSplitFlg() {
		return splitFlg;
	}
	@Column(name = "OLD_ACC_ID")
	public String getOldAccId() {
		return oldAccId;
	}
	public void setSplitFlg(String splitFlg) {
		this.splitFlg = splitFlg;
	}
	public void setOldAccId(String oldAccId) {
		this.oldAccId = oldAccId;
	}
	@Column(name = "SETTLEDATE")
	public Date getSettledate() {
		return settledate;
	}
	@Column(name = "SETTLETYPE")
	public String getSettletype() {
		return settletype;
	}
	@Column(name = "LAST_CIRCULATE_CODE")
	public String getLastCirculateCode() {
		return lastCirculateCode;
	}
	@Column(name = "LAST_BL_BSN_TPCD")
	public String getLastBlBsnTpcd() {
		return lastBlBsnTpcd;
	}
	public void setSettledate(Date settledate) {
		this.settledate = settledate;
	}
	public void setSettletype(String settletype) {
		this.settletype = settletype;
	}
	public void setLastCirculateCode(String lastCirculateCode) {
		this.lastCirculateCode = lastCirculateCode;
	}
	public void setLastBlBsnTpcd(String lastBlBsnTpcd) {
		this.lastBlBsnTpcd = lastBlBsnTpcd;
	}
	@Column(name = "REQ_SOC_NO")
	public String getReqSocNo() {
		return reqSocNo;
	}
	public void setReqSocNo(String reqSocNo) {
		this.reqSocNo = reqSocNo;
	}
	@Column(name = "REC_SOC_NO")
	public String getRecSocNo() {
		return recSocNo;
	}
	public void setRecSocNo(String recSocNo) {
		this.recSocNo = recSocNo;
	}
	@Column(name = "REC_DGH_TYPE")
	public String getRecDghType() {
		return recDghType;
	}
	public void setRecDghType(String recDghType) {
		this.recDghType = recDghType;
	}
	@Column(name = "bill_no")
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	@Column(name = "payer_acc")
	public String getPayerAcc() {
		return payerAcc;
	}
	public void setPayerAcc(String payerAcc) {
		this.payerAcc = payerAcc;
	}
	@Column(name = "payer_acc_bank_type_id")
	public String getPayerAccBankTypeId() {
		return payerAccBankTypeId;
	}
	public void setPayerAccBankTypeId(String payerAccBankTypeId) {
		this.payerAccBankTypeId = payerAccBankTypeId;
	}
	@Column(name = "req_bank_acc_id")
	public String getReqBankAccId() {
		return reqBankAccId;
	}
	public void setReqBankAccId(String reqBankAccId) {
		this.reqBankAccId = reqBankAccId;
	}
	@Column(name = "IS_ENDORSE")
	public String getIsEndorse() {
		return isEndorse;
	}
	public void setIsEndorse(String isEndorse) {
		this.isEndorse = isEndorse;
	}
	@Column(name = "SERIAL_NO")
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	@Column(name ="IS_AUTOSIGN")
	public String getIsAutosign() {
		return isAutosign;
	}
	public void setIsAutosign(String isAutosign) {
		this.isAutosign = isAutosign;
	}
	@Transient
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	@Transient
	public String getOtherCorpId() {
		return otherCorpId;
	}
	public void setOtherCorpId(String otherCorpId) {
		this.otherCorpId = otherCorpId;
	}
	@Transient
	public CorpDTO getCorpInfo() {
		return corpInfo;
	}
	public void setCorpInfo(CorpDTO corpInfo) {
		this.corpInfo = corpInfo;
	}
	@Transient
	public String getSysRaceCode() {
		return sysRaceCode;
	}
	public void setSysRaceCode(String sysRaceCode) {
		this.sysRaceCode = sysRaceCode;
	}
	@Transient
	public ConstantBean<Integer> getBusOriType() {
		return busOriType;
	}
	public void setBusOriType(ConstantBean<Integer> busOriType) {
		this.busOriType = busOriType;
	}
	@Transient
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	@Transient
	public String getIsCreateByNull() {
		return isCreateByNull;
	}
	public void setIsCreateByNull(String isCreateByNull) {
		this.isCreateByNull = isCreateByNull;
	}
	@Column(name ="EMIT_STTLMMK")
	public String getEmitSttlmmk() {
		return emitSttlmmk;
	}
	public void setEmitSttlmmk(String emitSttlmmk) {
		this.emitSttlmmk = emitSttlmmk;
	}
	@Transient
	public RatUrationDTO getRatUrationDTO() {
		return ratUrationDTO;
	}
	public void setRatUrationDTO(RatUrationDTO ratUrationDTO) {
		this.ratUrationDTO = ratUrationDTO;
	}
	@Transient
	public AbVoucherinfo getVouInfo() {
		return vouInfo;
	}
	public void setVouInfo(AbVoucherinfo vouInfo) {
		this.vouInfo = vouInfo;
	}
	@Transient
	public AbVoucherinfoVice getVouViceInfo() {
		return vouViceInfo;
	}
	public void setVouViceInfo(AbVoucherinfoVice vouViceInfo) {
		this.vouViceInfo = vouViceInfo;
	}
	@Column(name ="BUS_GLOBAL_ID")
	public String getBusGlobalId() {
		return busGlobalId;
	}
	public void setBusGlobalId(String busGlobalId) {
		this.busGlobalId = busGlobalId;
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
	@Column(name ="VID")
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
	@Column(name ="VVID")
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
	@Column(name ="RECID")
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
	@Column(name ="TRID")
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
	@Column(name ="LASTID")
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
	@Column(name ="LASTBILLCODE")
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
	@Column(name ="VALID_SIGN")
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
	@Column(name ="STATUS")
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
	@Column(name ="OPT_STATUS")
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
	@Column(name ="SEND_SIGN")
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
	@Column(name ="RETURN_CODE")
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
	@Column(name ="RETURN_MSG")
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
	@Column(name ="BRIEF_ID")
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
	@Column(name ="INSIDE_SING")
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
	@Column(name ="ENTER_STOCK_NEW")
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
	@Column(name ="ENTER_STOCK_OLD")
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
	@Column(name ="BAIL_TYPE")
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
	 *@return: java.lang.String  撤回类型
	 */
	@Column(name ="RECALL_TYPE")
	public String getRecallType(){
		return this.recallType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  撤回类型
	 */
	public void setRecallType(String recallType){
		this.recallType = recallType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  不得转让标记：EM00 可再转让；EM01 不得转让
	 */
	@Column(name ="BANKENDRSMTMK")
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
	@Column(name ="RCRSOKMONEY")
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
	@Column(name ="CN_ACCRECORDS")
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
	@Column(name ="SIGN_UP_FLAG")
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
	@Column(name ="ABS")
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
	@Column(name ="RMK")
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
	@Column(name ="CREDIT_CODE")
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
	@Column(name ="BUDGET_CODE")
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
	@Column(name ="BAIL_ACC")
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
	@Column(name ="BAILMONEY")
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
	@Column(name ="BAILPERCENT" )
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
	@Column(name ="BAILRATE" )
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
	@Column(name ="FEEMONEY")
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
	@Column(name ="RCRSRSNCD")
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
	@Column(name ="RCRS_TP")
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
	@Column(name ="SIGNEROK")
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
	@Column(name ="BIF_CODE")
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
	@Column(name ="REQ_ROLE")
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
	@Column(name ="REQ_NM")
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
	@Column(name ="REQ_CMONID")
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
	@Column(name ="REQ_ACCT_ID")
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
	@Column(name ="REQ_ACCTSVCR")
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
	@Column(name ="REQ_ACCTSVCR_AG")
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
	@Column(name ="REC_ROLE")
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
	@Column(name ="REC_NM")
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
	@Column(name ="REC_CMONID")
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
	@Column(name ="REC_ACCT_ID")
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
	@Column(name ="REC_ACCTSVCR")
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
	@Column(name ="REC_ACCTSVCR_AG")
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
	@Column(name ="STTLMMK")
	public String getSttlmmk() {
		return sttlmmk;
	}
	public void setSttlmmk(String sttlmmk) {
		this.sttlmmk = sttlmmk;
	}
	/**
	 * 是否隐藏
	 * @return 1是0否
	 */
	@Column(name ="IS_HIDE")
	public String getIsHide() {
		return isHide;
	}
	/**
	 * 是否隐藏
	 * @param isHide 1是0否
	 */
	public void setIsHide(String isHide) {
		this.isHide = isHide;
	}
	/**
	 * 方法: 取得java.lang.Integer
	 *
	 * @return: java.lang.Integer 10收票登记11背书登记12质押登记14贴现登记15归集收入16上划收入17下拨收入
	 */
	@Column(name = "BILL_SOURCE")
	public String getBillSource() {
		return this.billSource;
	}
	/**
	 * 方法: 设置java.lang.Integer
	 *
	 * @param: java.lang.Integer
	 *             10收票登记11背书登记12质押登记14贴现登记15归集收入16上划收入17下拨收入
	 */
	public void setBillSource(String billSource) {
		this.billSource = billSource;
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
	/**
	 * 交票人(领票人)
	 * @return
	 */
	@Column(name ="TRADER")
	public String getTrader() {
		return trader;
	}
	public void setTrader(String trader) {
		this.trader = trader;
	}
	/**
	 *  逾期托收原因说明
	 * @return
	 */
	@Column(name ="RSN")
	public String getRsn() {
		return rsn;
	}
	public void setRsn(String rsn) {
		this.rsn = rsn;
	}
	@Column(name ="PD_ID")
	public String getPdId() {
		return pdId;
	}
	public void setPdId(String pdId) {
		this.pdId = pdId;
	}
	@Column(name ="BILLhEAD_ID")
	public String getBillheadId() {
		return billheadId;
	}
	public void setBillheadId(String billheadId) {
		this.billheadId = billheadId;
	}
	@Column(name ="ATTACHMENT_ID")
	public String getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	@Column(name = "IS_HANDLING")
	public String getIsHandLing() {
		return isHandLing;
	}
	public void setIsHandLing(String isHandLing) {
		this.isHandLing = isHandLing;
	}
	@Override
	public String toString() {
		return "AbAccrecords{" +
				"billNo='" + billNo + '\'' +
				", billheadId='" + billheadId + '\'' +
				", payerAcc='" + payerAcc + '\'' +
				", payerAccBankTypeId='" + payerAccBankTypeId + '\'' +
				", vid='" + vid + '\'' +
				", vvid='" + vvid + '\'' +
				", recid='" + recid + '\'' +
				", trid='" + trid + '\'' +
				", lastid='" + lastid + '\'' +
				", lastbillcode='" + lastbillcode + '\'' +
				", validSign='" + validSign + '\'' +
				", isHide='" + isHide + '\'' +
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
				", recallType='" + recallType + '\'' +
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
				", reqBankAccId='" + reqBankAccId + '\'' +
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
				", busGlobalId='" + busGlobalId + '\'' +
				", attachmentId='" + attachmentId + '\'' +
				", billSource='" + billSource + '\'' +
				", vouInfo=" + vouInfo +
				", vouViceInfo=" + vouViceInfo +
				", ratUrationDTO=" + ratUrationDTO +
				", ratCpUrationDTO=" + ratCpUrationDTO +
				", isCreateByNull='" + isCreateByNull + '\'' +
				", emitSttlmmk='" + emitSttlmmk + '\'' +
				", corpId='" + corpId + '\'' +
				", netId='" + netId + '\'' +
				", otherCorpId='" + otherCorpId + '\'' +
				", corpInfo=" + corpInfo +
				", sysRaceCode='" + sysRaceCode + '\'' +
				", busOriType=" + busOriType +
				", billId='" + billId + '\'' +
				", msgId='" + msgId + '\'' +
				", trader='" + trader + '\'' +
				", rsn='" + rsn + '\'' +
				", pdId='" + pdId + '\'' +
				", isAutosign='" + isAutosign + '\'' +
				", isHandLing='" + isHandLing + '\'' +
				", serialNo='" + serialNo + '\'' +
				", isEndorse='" + isEndorse + '\'' +
				", reqSocNo='" + reqSocNo + '\'' +
				", recSocNo='" + recSocNo + '\'' +
				", recDghType='" + recDghType + '\'' +
				", settledate=" + settledate +
				", settletype='" + settletype + '\'' +
				", lastCirculateCode='" + lastCirculateCode + '\'' +
				", feemoneyScale=" + feemoneyScale +
				", lastBlBsnTpcd='" + lastBlBsnTpcd + '\'' +
				", splitFlg='" + splitFlg + '\'' +
				", oldAccId='" + oldAccId + '\'' +
				", copyFlag='" + copyFlag + '\'' +
				", copyAccId='" + copyAccId + '\'' +
				'}';
	}
	@Transient
	public RatCpUrationDTO getRatCpUrationDTO() {
		return ratCpUrationDTO;
	}
	public void setRatCpUrationDTO(RatCpUrationDTO ratCpUrationDTO) {
		this.ratCpUrationDTO = ratCpUrationDTO;
	}
}
