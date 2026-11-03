package com.bytter.system.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
/**   
 * @Title: Entity
 * @Description: 调拨策略表
 * @author onlineGenerator
 * @date 2017-06-16 14:38:52
 * @version 1.0
 *
 */
@Entity
@Table(name = "BIS_GATHER_POLICY", schema = "")
@SuppressWarnings("serial")
public class BisGatherPolicy extends IdEntity implements java.io.Serializable {
	/**调拨策略代码*/
	private java.lang.String gatherPolicyCode;
	/**调拨策略名称*/
	private java.lang.String gatherPolicyName;
	/**策略生效日期（yyyy/MM/dd）*/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date defDate;
	/**策略生效时间*/
	private java.lang.String launchTime;
	/**调拨类型0-定时归集，1-实时归集，2-定时下拨*/
	private java.lang.String gatherType;
	/**调拨周期4-按日，8-按周，16-按月，32-按季，64-按年*/
	private java.lang.Integer frqMode;
	/**调拨日 间隔时长以天为单位周策略：选择周一至周日月策略：选择1号到31号*/
	private java.lang.Integer serialDay;
	/**金额上限*/
	private java.math.BigDecimal maxAmt;
	/**金额下限（最小执行额度）*/
	private java.math.BigDecimal minAmt;
	/**策略模式（归集/下拨方式）*/
	private java.lang.String gatherMode;
	/**调拨金额（留底资金金额/取整单位/留底资金金额/固定上划金额/账户固定余额/下拨固定额度）*/
	private java.math.BigDecimal gatherAmt;
	/**下拨固定额度（定时下拨，余额不足时固定额度下拨时，保存下拨固定额度）*/
	private java.math.BigDecimal downFixedAmt;
	/**下一次下拔时间*/
	private java.util.Date appNextTime;
	/**比例*/
	private java.math.BigDecimal differScale;
	/**加急标志 1-是 0-否*/
	private java.lang.String isQuick;
	/**有效标志 1-有效 0-无效*/
	private java.lang.String validSign;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**币别*/
	private java.lang.String curId;
	/**智能跳过节假日*/
	private java.lang.String autoFilterHolidays;
	/**用途*/
	private String purpose;
	/**附言*/
	private String addWord;
	/**跳过节假日规则:1,不执行,2,提前执行,3,顺延执行*/
	private String autoFilterHolidaysRule;
	/**执行失败处理方式 0,反复执行至成功 1-执行一次*/
	private String failHandleMethod;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  调拨策略代码
	 */
	@Column(name ="GATHER_POLICY_CODE",nullable=false,length=4)
	public java.lang.String getGatherPolicyCode(){
		return this.gatherPolicyCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  调拨策略代码
	 */
	public void setGatherPolicyCode(java.lang.String gatherPolicyCode){
		this.gatherPolicyCode = gatherPolicyCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  调拨策略名称
	 */
	@Column(name ="GATHER_POLICY_NAME",nullable=true,length=60)
	public java.lang.String getGatherPolicyName(){
		return this.gatherPolicyName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  调拨策略名称
	 */
	public void setGatherPolicyName(java.lang.String gatherPolicyName){
		this.gatherPolicyName = gatherPolicyName;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  策略生效日期（yyyy/MM/dd）
	 */
	@Column(name ="DEF_DATE",nullable=false)
	public java.util.Date getDefDate(){
		return this.defDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  策略生效日期（yyyy/MM/dd）
	 */
	public void setDefDate(java.util.Date defDate){
		this.defDate = defDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  策略生效时间
	 */
	@Column(name ="LAUNCH_TIME",nullable=true,length=6)
	public java.lang.String getLaunchTime(){
		return this.launchTime;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  策略生效时间
	 */
	public void setLaunchTime(java.lang.String launchTime){
		this.launchTime = launchTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  调拨类型0-定时归集，1-实时归集，2-定时下拨
	 */
	@Column(name ="GATHER_TYPE",nullable=true,length=1)
	public java.lang.String getGatherType(){
		return this.gatherType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  调拨类型0-定时归集，1-实时归集，2-定时下拨
	 */
	public void setGatherType(java.lang.String gatherType){
		this.gatherType = gatherType;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  调拨周期4-按日，8-按周，16-按月，32-按季，64-按年
	 */
	@Column(name ="FRQ_MODE",nullable=true,length=10)
	public java.lang.Integer getFrqMode(){
		return this.frqMode;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  调拨周期4-按日，8-按周，16-按月，32-按季，64-按年
	 */
	public void setFrqMode(java.lang.Integer frqMode){
		this.frqMode = frqMode;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  调拨日 间隔时长以天为单位周策略：选择周一至周日月策略：选择1号到31号
	 */
	@Column(name ="SERIAL_DAY",nullable=true,length=10)
	public java.lang.Integer getSerialDay(){
		return this.serialDay;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  调拨日 间隔时长以天为单位周策略：选择周一至周日月策略：选择1号到31号
	 */
	public void setSerialDay(java.lang.Integer serialDay){
		this.serialDay = serialDay;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  金额上限
	 */
	@Column(name ="MAX_AMT",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getMaxAmt(){
		return this.maxAmt;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  金额上限
	 */
	public void setMaxAmt(java.math.BigDecimal maxAmt){
		this.maxAmt = maxAmt;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  金额下限（最小执行额度）
	 */
	@Column(name ="MIN_AMT",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getMinAmt(){
		return this.minAmt;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  金额下限（最小执行额度）
	 */
	public void setMinAmt(java.math.BigDecimal minAmt){
		this.minAmt = minAmt;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  策略模式（归集/下拨方式）
	 */
	@Column(name ="GATHER_MODE",nullable=false,length=1)
	public java.lang.String getGatherMode(){
		return this.gatherMode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  策略模式（归集/下拨方式）
	 */
	public void setGatherMode(java.lang.String gatherMode){
		this.gatherMode = gatherMode;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  调拨金额（留底资金金额/取整单位/留底资金金额/固定上划金额/账户固定余额/下拨固定额度）
	 */
	@Column(name ="GATHER_AMT",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getGatherAmt(){
		return this.gatherAmt;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  调拨金额（留底资金金额/取整单位/留底资金金额/固定上划金额/账户固定余额/下拨固定额度）
	 */
	public void setGatherAmt(java.math.BigDecimal gatherAmt){
		this.gatherAmt = gatherAmt;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  下拨固定额度（定时下拨，余额不足时固定额度下拨时，保存下拨固定额度）
	 */
	@Column(name ="DOWN_FIXED_AMT",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getDownFixedAmt(){
		return this.downFixedAmt;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  下拨固定额度（定时下拨，余额不足时固定额度下拨时，保存下拨固定额度）
	 */
	public void setDownFixedAmt(java.math.BigDecimal downFixedAmt){
		this.downFixedAmt = downFixedAmt;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  下一次下拔时间
	 */
	@Column(name ="APP_NEXT_TIME",nullable=true)
	public java.util.Date getAppNextTime(){
		return this.appNextTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  下一次下拔时间
	 */
	public void setAppNextTime(java.util.Date appNextTime){
		this.appNextTime = appNextTime;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  比例
	 */
	@Column(name ="DIFFER_SCALE",nullable=true,scale=3,length=4)
	public java.math.BigDecimal getDifferScale(){
		return this.differScale;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  比例
	 */
	public void setDifferScale(java.math.BigDecimal differScale){
		this.differScale = differScale;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  加急标志 1-是 0-否
	 */
	@Column(name ="IS_QUICK",nullable=true,length=1)
	public java.lang.String getIsQuick(){
		return this.isQuick;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  加急标志 1-是 0-否
	 */
	public void setIsQuick(java.lang.String isQuick){
		this.isQuick = isQuick;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  有效标志 1-有效 0-无效
	 */
	@Column(name ="VALID_SIGN",nullable=true,length=1)
	public java.lang.String getValidSign(){
		return this.validSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  有效标志 1-有效 0-无效
	 */
	public void setValidSign(java.lang.String validSign){
		this.validSign = validSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */
	@Column(name ="CREATE_BY",nullable=true,length=50)
	public java.lang.String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人登录名称
	 */
	public void setCreateBy(java.lang.String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
	@Column(name ="CREATE_DATE",nullable=true)
	public java.util.Date getCreateDate(){
		return this.createDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人登录名称
	 */
	@Column(name ="UPDATE_BY",nullable=true,length=50)
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人登录名称
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	@Column(name ="UPDATE_DATE",nullable=true)
	public java.util.Date getUpdateDate(){
		return this.updateDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setUpdateDate(java.util.Date updateDate){
		this.updateDate = updateDate;
	}
	@Column(name ="CUR_ID",nullable=true)
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	@Column(name ="AUTO_FILTER_HOLIDAYS",nullable=true,length=1)
	public String getAutoFilterHolidays() {
		return autoFilterHolidays;
	}
	public void setAutoFilterHolidays(String autoFilterHolidays) {
		this.autoFilterHolidays = autoFilterHolidays;
	}
	@Column(name ="PURPOSE")
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	@Column(name ="ADD_WORD")
	public String getAddWord() {
		return addWord;
	}
	public void setAddWord(String addWord) {
		this.addWord = addWord;
	}
	@Column(name ="AUTO_FILTER_HOLIDAYS_RULE")
	public String getAutoFilterHolidaysRule() {
		return autoFilterHolidaysRule;
	}
	public void setAutoFilterHolidaysRule(String autoFilterHolidaysRule) {
		this.autoFilterHolidaysRule = autoFilterHolidaysRule;
	}
	@Column(name ="FAIL_HANDLE_METHOD")
	public String getFailHandleMethod() {
		return failHandleMethod;
	}
	public void setFailHandleMethod(String failHandleMethod) {
		this.failHandleMethod = failHandleMethod;
	}
}
