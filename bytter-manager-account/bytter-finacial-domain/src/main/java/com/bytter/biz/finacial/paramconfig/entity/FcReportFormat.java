package com.bytter.biz.finacial.paramconfig.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: 格式定义表
 * @author onlineGenerator
 * @date 2017-08-30 17:58:29
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_REPORT_FORMAT", schema = "")
@SuppressWarnings("serial")
public class FcReportFormat extends IdEntity implements java.io.Serializable {
	/**种类*/
	private java.lang.String reportType;
	/**网点*/
	private java.lang.String netCode;
	/**行号*/
	private java.lang.Integer lineNo;
	/**左右*/
	private java.lang.String direction;
	/**项目*/
	private java.lang.String item;
	/**
	 * 项目id
	 */
	private java.lang.String itemId;
	/**行次*/
	private java.lang.Integer orderNo;
	/**正负*/
	private java.lang.Integer rect;
	/**科目*/
	private java.lang.String subject;
	/**计算公式*/
	private java.lang.String formula;
	/**是否年初*/
	private java.lang.String initYear;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private java.lang.String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private java.lang.String updateBy;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  种类
	 */
	@Column(name ="REPORT_TYPE")
	public java.lang.String getReportType(){
		return this.reportType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  种类
	 */
	public void setReportType(java.lang.String reportType){
		this.reportType = reportType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  网点
	 */
	@Column(name ="NET_CODE")
	public java.lang.String getNetCode(){
		return this.netCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  网点
	 */
	public void setNetCode(java.lang.String netCode){
		this.netCode = netCode;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  行号
	 */
	@Column(name ="LINE_NO")
	public java.lang.Integer getLineNo(){
		return this.lineNo;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  行号
	 */
	public void setLineNo(java.lang.Integer lineNo){
		this.lineNo = lineNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  左右
	 */
	@Column(name ="DIRECTION")
	public java.lang.String getDirection(){
		return this.direction;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  左右
	 */
	public void setDirection(java.lang.String direction){
		this.direction = direction;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  项目
	 */
	@Column(name ="ITEM")
	public java.lang.String getItem(){
		return this.item;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  项目
	 */
	public void setItem(java.lang.String item){
		this.item = item;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  行次
	 */
	@Column(name ="ORDER_NO")
	public java.lang.Integer getOrderNo(){
		return this.orderNo;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  行次
	 */
	public void setOrderNo(java.lang.Integer orderNo){
		this.orderNo = orderNo;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  正负
	 */
	@Column(name ="RECT")
	public java.lang.Integer getRect(){
		return this.rect;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  正负
	 */
	public void setRect(java.lang.Integer rect){
		this.rect = rect;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  科目
	 */
	@Column(name ="SUBJECT")
	public java.lang.String getSubject(){
		return this.subject;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  科目
	 */
	public void setSubject(java.lang.String subject){
		this.subject = subject;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  计算公式
	 */
	@Column(name ="FORMULA")
	public java.lang.String getFormula(){
		return this.formula;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  计算公式
	 */
	public void setFormula(java.lang.String formula){
		this.formula = formula;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否年初
	 */
	@Column(name ="INIT_YEAR")
	public java.lang.String getInitYear(){
		return this.initYear;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否年初
	 */
	public void setInitYear(java.lang.String initYear){
		this.initYear = initYear;
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
	@Column(name ="ITEM_ID")
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
}
