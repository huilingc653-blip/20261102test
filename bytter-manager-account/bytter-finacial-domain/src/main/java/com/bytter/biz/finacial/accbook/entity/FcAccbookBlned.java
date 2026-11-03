package com.bytter.biz.finacial.accbook.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @Title: FC_ACCBOOK_BLNED
 * @Description: 已结流水账主档表
 * @author onlineGenerator
 * @date 2017-06-19 11:28:14
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_ACCBOOK_BLNED", schema = "")
@SuppressWarnings("serial")
public class FcAccbookBlned extends IdEntity implements java.io.Serializable {

	/**
	 * 租户ID
	 */
	private String tenantId;
	/**所属网点*/
	private java.lang.String netId;
	/**流水号*/
	private java.lang.String orderNo;
	/**凭证号*/
	private java.lang.String voucherNo;
	/**录入日期*/
	private java.util.Date enrolTime;
	/**记账日期*/
	private java.util.Date tallyDate;
	/**起息日期*/
	private java.util.Date interestStartDate;
	/**凭证种类代码*/
	private java.lang.String voucherType;
	/**原凭证号*/
	private java.lang.String origVoucherNo;
	/**加急标志*/
	private java.lang.String emergencySign;
	/**记账员代码*/
	private java.lang.String operatorCode;
	/**复核员代码*/
	private java.lang.String checkerCode;
	/**复核日期*/
	private java.util.Date checkTime;
	/**附件张数*/
	private java.lang.Integer affixAmount;
	/**红蓝标志*/
	private java.lang.String redblueSign;
	/**凭证状态*/
	private java.lang.String voucherState;
	/**已计手续费标志*/
	private java.lang.String chargedSign;
	/**冲账标志*/
	private java.lang.String rushSign;
	/**产生方式*/
	private java.lang.String generateMode;
	/**业务来源类型*/
	private java.lang.Integer busOriType;
	/**业务种类*/
	private java.lang.String busRaceId;
	/**备注*/
	private java.lang.String rmk;
	/**凭证附言*/
	private java.lang.String postscript;
	/**币别代码*/
	private java.lang.String curCode;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private java.lang.String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private java.lang.String updateBy;

	/**全局业务ID*/
	private java.lang.String busGlobalId;
	/**内部资金计划标识*/
	private String innerSign;
	/**
	 * 附件ID
	 */
	private String attachmentId;
	/**
	 * 是否对现金流项目做过更改
	 */
	private String isChangeCashflux;
	/**
	 * 主档表，不能随意新增字段
	 * 新增冗余字段 user_name、corp_name、acc_name、subject_id、subject_name、cur_id
	 */
	/*private String userName;
	private String corpName;
	private String accName;
	private String subjectId;
	private String subjectName;
	private String curId;
	@Column(name ="USER_NAME")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(name = "CORP_NAME")
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	@Column(name = "ACC_NAME")
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	@Column(name = "SUBJECT_ID")
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	@Column(name = "SUBJECT_NAME")
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	@Column(name = "CUR_ID")
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
*/

	@Column(name ="TENANT_ID")
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属网点
	 */
	@Column(name ="NET_ID")
	public java.lang.String getNetId(){
		return this.netId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属网点
	 */
	public void setNetId(java.lang.String netId){
		this.netId = netId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  流水号
	 */
	@Column(name ="ORDER_NO")
	public java.lang.String getOrderNo(){
		return this.orderNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  流水号
	 */
	public void setOrderNo(java.lang.String orderNo){
		this.orderNo = orderNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  凭证号
	 */
	@Column(name ="VOUCHER_NO")
	public java.lang.String getVoucherNo(){
		return this.voucherNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证号
	 */
	public void setVoucherNo(java.lang.String voucherNo){
		this.voucherNo = voucherNo;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  录入日期
	 */
	@Column(name ="ENROL_TIME")
	public java.util.Date getEnrolTime(){
		return this.enrolTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  录入日期
	 */
	public void setEnrolTime(java.util.Date enrolTime){
		this.enrolTime = enrolTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  记账日期
	 */
	@Column(name ="TALLY_DATE")
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  起息日期
	 */
	@Column(name ="INTEREST_START_DATE")
	public java.util.Date getInterestStartDate(){
		return this.interestStartDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  起息日期
	 */
	public void setInterestStartDate(java.util.Date interestStartDate){
		this.interestStartDate = interestStartDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  凭证种类代码
	 */
	@Column(name ="VOUCHER_TYPE")
	public java.lang.String getVoucherType(){
		return this.voucherType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证种类代码
	 */
	public void setVoucherType(java.lang.String voucherType){
		this.voucherType = voucherType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  原凭证号
	 */
	@Column(name ="ORIG_VOUCHER_NO")
	public java.lang.String getOrigVoucherNo(){
		return this.origVoucherNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  原凭证号
	 */
	public void setOrigVoucherNo(java.lang.String origVoucherNo){
		this.origVoucherNo = origVoucherNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  加急标志
	 */
	@Column(name ="EMERGENCY_SIGN")
	public java.lang.String getEmergencySign(){
		return this.emergencySign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  加急标志
	 */
	public void setEmergencySign(java.lang.String emergencySign){
		this.emergencySign = emergencySign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  记账员代码
	 */
	@Column(name ="OPERATOR_CODE")
	public java.lang.String getOperatorCode(){
		return this.operatorCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  记账员代码
	 */
	public void setOperatorCode(java.lang.String operatorCode){
		this.operatorCode = operatorCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  复核员代码
	 */
	@Column(name ="CHECKER_CODE")
	public java.lang.String getCheckerCode(){
		return this.checkerCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  复核员代码
	 */
	public void setCheckerCode(java.lang.String checkerCode){
		this.checkerCode = checkerCode;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  复核日期
	 */
	@Column(name ="CHECK_TIME")
	public java.util.Date getCheckTime(){
		return this.checkTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  复核日期
	 */
	public void setCheckTime(java.util.Date checkTime){
		this.checkTime = checkTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  附件张数
	 */
	@Column(name ="AFFIX_AMOUNT")
	public java.lang.Integer getAffixAmount(){
		return this.affixAmount;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  附件张数
	 */
	public void setAffixAmount(java.lang.Integer affixAmount){
		this.affixAmount = affixAmount;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  红蓝标志
	 */
	@Column(name ="REDBLUE_SIGN")
	public java.lang.String getRedblueSign(){
		return this.redblueSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  红蓝标志
	 */
	public void setRedblueSign(java.lang.String redblueSign){
		this.redblueSign = redblueSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  凭证状态
	 */
	@Column(name ="VOUCHER_STATE")
	public java.lang.String getVoucherState(){
		return this.voucherState;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证状态
	 */
	public void setVoucherState(java.lang.String voucherState){
		this.voucherState = voucherState;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  已计手续费标志
	 */
	@Column(name ="CHARGED_SIGN")
	public java.lang.String getChargedSign(){
		return this.chargedSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  已计手续费标志
	 */
	public void setChargedSign(java.lang.String chargedSign){
		this.chargedSign = chargedSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  冲账标志
	 */
	@Column(name ="RUSH_SIGN")
	public java.lang.String getRushSign(){
		return this.rushSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  冲账标志
	 */
	public void setRushSign(java.lang.String rushSign){
		this.rushSign = rushSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  产生方式
	 */
	@Column(name ="GENERATE_MODE")
	public java.lang.String getGenerateMode(){
		return this.generateMode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  产生方式
	 */
	public void setGenerateMode(java.lang.String generateMode){
		this.generateMode = generateMode;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  业务来源类型
	 */
	@Column(name ="BUS_ORI_TYPE")
	public java.lang.Integer getBusOriType(){
		return this.busOriType;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  业务来源类型
	 */
	public void setBusOriType(java.lang.Integer busOriType){
		this.busOriType = busOriType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务种类
	 */
	@Column(name ="BUS_RACE_ID")
	public java.lang.String getBusRaceId(){
		return this.busRaceId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.Integer  业务种类
	 */
	public void setBusRaceId(java.lang.String busRaceId){
		this.busRaceId = busRaceId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	@Column(name ="RMK")
	public java.lang.String getRmk(){
		return this.rmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备注
	 */
	public void setRmk(java.lang.String rmk){
		this.rmk = rmk;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  凭证附言
	 */
	@Column(name ="POSTSCRIPT")
	public java.lang.String getPostscript(){
		return this.postscript;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证附言
	 */
	public void setPostscript(java.lang.String postscript){
		this.postscript = postscript;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别代码
	 */
	@Column(name ="CUR_CODE")
	public java.lang.String getCurCode(){
		return this.curCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别代码
	 */
	public void setCurCode(java.lang.String curCode){
		this.curCode = curCode;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="CREATE_TIME")
	public java.util.Date getCreateTime(){
		return this.createTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建用户代码
	 */
	@Column(name ="CREATE_BY")
	public java.lang.String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建用户代码
	 */
	public void setCreateBy(java.lang.String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  最后更新时间
	 */
	@Column(name ="UPDATE_TIME")
	public java.util.Date getUpdateTime(){
		return this.updateTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  最后更新时间
	 */
	public void setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  最后更新用户代码
	 */
	@Column(name ="UPDATE_BY")
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  最后更新用户代码
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  全局业务ID
	 */
	@Column(name ="BUS_GLOBAL_ID")
	public java.lang.String getBusGlobalId(){
		return this.busGlobalId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  全局业务ID
	 */
	public void setBusGlobalId(java.lang.String busGlobalId){
		this.busGlobalId = busGlobalId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  内部资金计划标识
	 */
	@Column(name ="INNER_SIGN")
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
	 * 附件ID
	 */
	@Column(name = "ATTACHMENT_ID")
	public String getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	@Column(name = "IS_CHANGE_CASHFLUX")
	public String getIsChangeCashflux() {
		return isChangeCashflux;
	}
	public void setIsChangeCashflux(String isChangeCashflux) {
		this.isChangeCashflux = isChangeCashflux;
	}
}
