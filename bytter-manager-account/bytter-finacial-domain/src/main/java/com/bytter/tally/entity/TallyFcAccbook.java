package com.bytter.tally.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.*;
/**   
 * @Title: Entity
 * @Description: FC_ACCBOOK
 * @author onlineGenerator
 * @date 2017-05-31 15:02:10
 * @version 1.0
 * TODO 非本域实体，临时解决
 */
@Entity
@Table(name = "FC_ACCBOOK", schema = "")
@SuppressWarnings("serial")
public class TallyFcAccbook extends IdEntity implements java.io.Serializable {
	/**所属网点*/
	private String netId;
	/**流水号*/
	private String orderNo;
	/**凭证号*/
	private String voucherNo;
	/**录入日期*/
	private java.util.Date enrolTime;
	/**记账日期*/
	private java.util.Date tallyDate;
	/**起息日期*/
	private java.util.Date interestStartDate;
	/**凭证种类代码*/
	private String voucherType;
	/**原凭证号*/
	private String origVoucherNo;
	/**加急标志*/
	private String emergencySign;
	/**记账员代码*/
	private String operatorCode;
	/**复核员代码*/
	private String checkerCode;
	/**复核日期*/
	private java.util.Date checkTime;
	/**附件张数*/
	private Integer affixAmount;
	/**红蓝标志*/
	private String redblueSign;
	/**凭证状态*/
	private String voucherState;
	/**已计手续费标志*/
	private String chargedSign;
	/**冲账标志*/
	private String rushSign;
	/**产生方式*/
	private String generateMode;
	/**业务来源类型*/
	private Integer busOriType;
	/**业务种类*/
	private String busRaceId;
	/**备注*/
	private String rmk;
	/**凭证附言*/
	private String postscript;
	/**币别代码*/
	private String curCode;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private String updateBy;
	/**全局业务ID*/
	private String busGlobalId;
	/**业务种类名称*/
	private String busRaceName;
	/**凭证种类名称*/
	private String voucherName;
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属网点
	 */
	@Column(name ="NET_ID")
	public String getNetId(){
		return this.netId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属网点
	 */
	public void setNetId(String netId){
		this.netId = netId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  流水号
	 */
	@Column(name ="ORDER_NO")
	public String getOrderNo(){
		return this.orderNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  流水号
	 */
	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  凭证号
	 */
	@Column(name ="VOUCHER_NO")
	public String getVoucherNo(){
		return this.voucherNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证号
	 */
	public void setVoucherNo(String voucherNo){
		this.voucherNo = voucherNo;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  录入日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
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
	@Temporal(TemporalType.DATE)
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
	@Temporal(TemporalType.DATE)
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
	public String getVoucherType(){
		return this.voucherType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证种类代码
	 */
	public void setVoucherType(String voucherType){
		this.voucherType = voucherType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  原凭证号
	 */
	@Column(name ="ORIG_VOUCHER_NO")
	public String getOrigVoucherNo(){
		return this.origVoucherNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  原凭证号
	 */
	public void setOrigVoucherNo(String origVoucherNo){
		this.origVoucherNo = origVoucherNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  加急标志
	 */
	@Column(name ="EMERGENCY_SIGN")
	public String getEmergencySign(){
		return this.emergencySign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  加急标志
	 */
	public void setEmergencySign(String emergencySign){
		this.emergencySign = emergencySign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  记账员代码
	 */
	@Column(name ="OPERATOR_CODE")
	public String getOperatorCode(){
		return this.operatorCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  记账员代码
	 */
	public void setOperatorCode(String operatorCode){
		this.operatorCode = operatorCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  复核员代码
	 */
	@Column(name ="CHECKER_CODE")
	public String getCheckerCode(){
		return this.checkerCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  复核员代码
	 */
	public void setCheckerCode(String checkerCode){
		this.checkerCode = checkerCode;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  复核日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
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
	public Integer getAffixAmount(){
		return this.affixAmount;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  附件张数
	 */
	public void setAffixAmount(Integer affixAmount){
		this.affixAmount = affixAmount;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  红蓝标志
	 */
	@Column(name ="REDBLUE_SIGN")
	public String getRedblueSign(){
		return this.redblueSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  红蓝标志
	 */
	public void setRedblueSign(String redblueSign){
		this.redblueSign = redblueSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  凭证状态
	 */
	@Column(name ="VOUCHER_STATE")
	public String getVoucherState(){
		return this.voucherState;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证状态
	 */
	public void setVoucherState(String voucherState){
		this.voucherState = voucherState;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  已计手续费标志
	 */
	@Column(name ="CHARGED_SIGN")
	public String getChargedSign(){
		return this.chargedSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  已计手续费标志
	 */
	public void setChargedSign(String chargedSign){
		this.chargedSign = chargedSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  冲账标志
	 */
	@Column(name ="RUSH_SIGN")
	public String getRushSign(){
		return this.rushSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  冲账标志
	 */
	public void setRushSign(String rushSign){
		this.rushSign = rushSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  产生方式
	 */
	@Column(name ="GENERATE_MODE")
	public String getGenerateMode(){
		return this.generateMode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  产生方式
	 */
	public void setGenerateMode(String generateMode){
		this.generateMode = generateMode;
	}
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  业务种类
	 */
	@Column(name ="BUS_ORI_TYPE")
	public Integer getBusOriType(){
		return this.busOriType;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  业务种类
	 */
	public void setBusOriType(Integer busOriType){
		this.busOriType = busOriType;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务种类
	 */
	@Column(name ="BUS_RACE_ID")
	public String getBusRaceId(){
		return this.busRaceId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.Integer  业务种类
	 */
	public void setBusRaceId(String busRaceId){
		this.busRaceId = busRaceId;
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
	 *@return: java.lang.String  凭证附言
	 */
	@Column(name ="POSTSCRIPT")
	public String getPostscript(){
		return this.postscript;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证附言
	 */
	public void setPostscript(String postscript){
		this.postscript = postscript;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别代码
	 */
	@Column(name ="CUR_CODE")
	public String getCurCode(){
		return this.curCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别代码
	 */
	public void setCurCode(String curCode){
		this.curCode = curCode;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
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
	public String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建用户代码
	 */
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  最后更新时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
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
	public String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  最后更新用户代码
	 */
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  全局业务ID
	 */
	@Column(name ="BUS_GLOBAL_ID")
	public String getBusGlobalId(){
		return this.busGlobalId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  全局业务ID
	 */
	public void setBusGlobalId(String busGlobalId){
		this.busGlobalId = busGlobalId;
	}
	@Transient
	public String getBusRaceName() {
		return busRaceName;
	}
	public void setBusRaceName(String busRaceName) {
		this.busRaceName = busRaceName;
	}
	@Transient
	public String getVoucherName() {
		return voucherName;
	}
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
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
