package com.bytter.biz.finacial.paramconfig.vo;
import com.bytter.biz.finacial.paramconfig.entity.FcReportFormat;
import java.util.ArrayList;
import java.util.List;
/**   
 * @Title: Entity
 * @Description: 格式定义表
 * @author onlineGenerator
 * @date 2017-08-30 17:58:29
 * @version 1.0
 *
 */
public class FcReportFormatVO  implements java.io.Serializable {
	/**种类*/
	private java.lang.String id;
	/**种类*/
	private java.lang.String reportType;
	/**网点*/
	private java.lang.String netCode;
	/**需要复制的网点*/
	private java.lang.String newNetCode;
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
	
	private List<FcReportFormat> format = new ArrayList<>();
	public java.lang.String getReportType(){
		return this.reportType;
	}
	public void setReportType(java.lang.String reportType){
		this.reportType = reportType;
	}
	public java.lang.String getNetCode(){
		return this.netCode;
	}
	public void setNetCode(java.lang.String netCode){
		this.netCode = netCode;
	}
	public java.lang.Integer getLineNo(){
		return this.lineNo;
	}
	public void setLineNo(java.lang.Integer lineNo){
		this.lineNo = lineNo;
	}
	public java.lang.String getDirection(){
		return this.direction;
	}
	public void setDirection(java.lang.String direction){
		this.direction = direction;
	}
	public java.lang.String getItem(){
		return this.item;
	}
	public void setItem(java.lang.String item){
		this.item = item;
	}
	public java.lang.Integer getOrderNo(){
		return this.orderNo;
	}
	public void setOrderNo(java.lang.Integer orderNo){
		this.orderNo = orderNo;
	}
	public java.lang.Integer getRect(){
		return this.rect;
	}
	public void setRect(java.lang.Integer rect){
		this.rect = rect;
	}
	public java.lang.String getSubject(){
		return this.subject;
	}
	public void setSubject(java.lang.String subject){
		this.subject = subject;
	}
	public java.lang.String getFormula(){
		return this.formula;
	}
	public void setFormula(java.lang.String formula){
		this.formula = formula;
	}
	public java.lang.String getInitYear(){
		return this.initYear;
	}
	public void setInitYear(java.lang.String initYear){
		this.initYear = initYear;
	}
	public java.util.Date getCreateTime(){
		return this.createTime;
	}
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}
	
	public java.lang.String getCreateBy(){
		return this.createBy;
	}
	
	public void setCreateBy(java.lang.String createBy){
		this.createBy = createBy;
	}
	
	public java.util.Date getUpdateTime(){
		return this.updateTime;
	}
	
	public void setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;
	}
	
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}
	
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
	public java.lang.String getId() {
		return id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public List<FcReportFormat> getFormat() {
		return format;
	}
	public void setFormat(List<FcReportFormat> format) {
		this.format = format;
	}
	public java.lang.String getNewNetCode() {
		return newNetCode;
	}
	public void setNewNetCode(java.lang.String newNetCode) {
		this.newNetCode = newNetCode;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
}
