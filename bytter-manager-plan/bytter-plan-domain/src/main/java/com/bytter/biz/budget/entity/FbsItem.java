package com.bytter.biz.budget.entity;
import java.math.BigDecimal;
import java.util.Date;
import java.lang.String;
import java.lang.Double;
import java.lang.Integer;
import java.math.BigDecimal;
import javax.xml.soap.Text;
import java.sql.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 预算科目
 * @author onlineGenerator
 * @date 2017-07-06 16:40:26
 * @version 1.0
 *
 */
@Entity
@Table(name = "FBS_ITEM", schema = "")
@SuppressWarnings("serial")
public class FbsItem extends IdEntity implements java.io.Serializable {
	/**科目代码*/
	private java.lang.String itemCode;
	/**科目名称*/
	private java.lang.String itemName;
	/**父科目*/
	private java.lang.String superItemCode;
	/**科目层级*/
	private java.lang.Integer levels;
	/**资金流向1， 资金流入-1 ，资金流出*/
	private java.lang.Integer sign;
	/**是否冲抵科目  1-是 0-否*/
	private java.lang.String isInternal;
	/**是否计划控制科目 1-是 0-否*/
	private java.lang.String isControl;
	/**是否刚性科目 1-是 0-否*/
	private java.lang.String isRigid;
	/**是否期初科目 1-是 0-否*/
	private java.lang.String isBeginPeriod;
	/**是否期末科目 1-是 0-否*/
	private java.lang.String isEndPeriod;
	/**有效标志 0 无效 1 有效*/
	private java.lang.String validSign;
	/**同级科目关系*/
	private java.lang.String itemRelation;
	/**超计划比例*/
	private java.math.BigDecimal overPerSpend;
	/**现金流科目代码*/
	private java.lang.String cashItemCode;
	/**现金流科目名称*/
	private java.lang.String cashItemName;
	/**调拨科目*/
	private java.lang.String allotItemCode;
	/**备注*/
	private java.lang.String rmk;
	/**是否系统科目（系统科目不能删除）系统科目：100-期初资金，200-资金增加，300-可用资金，400-资金减少，500-资金不足或多余，600-新增筹资或还款，700-期末资金，800表外科目*/
	private java.lang.String isSystem;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**是否允许填写计划 1-是 0-否*/
	private String isWrite;
	/**预算科目流水号*/
	private String seqCode;
	/**存父的所有的编码,用逗号分隔*/
	private String superItemCodes;
	/**科目明细是否必须录入 1-是 0-否*/
	private String isWriteDetail;
	@Column(name ="IS_WRITE",nullable=true,length=1)
	public String getIsWrite() {
		return isWrite;
	}
	public void setIsWrite(String isWrite) {
		this.isWrite = isWrite;
	}
	@Column(name ="SUPER_ITEM_CODES",nullable=true,length=150)
	public String getSuperItemCodes() {
		return superItemCodes;
	}
	public void setSuperItemCodes(String superItemCodes) {
		this.superItemCodes = superItemCodes;
	}
	@Column(name ="SEQ_CODE",nullable=true,length=40)
	public String getSeqCode() {
		return seqCode;
	}
	public void setSeqCode(String seqCode) {
		this.seqCode = seqCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  科目代码
	 */
	@Column(name ="ITEM_CODE",nullable=false,length=40)
	public java.lang.String getItemCode(){
		return this.itemCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  科目代码
	 */
	public void setItemCode(java.lang.String itemCode){
		this.itemCode = itemCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  科目名称
	 */
	@Column(name ="ITEM_NAME",nullable=true,length=100)
	public java.lang.String getItemName(){
		return this.itemName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  科目名称
	 */
	public void setItemName(java.lang.String itemName){
		this.itemName = itemName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  父科目
	 */
	@Column(name ="SUPER_ITEM_CODE",nullable=true,length=40)
	public java.lang.String getSuperItemCode(){
		return this.superItemCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  父科目
	 */
	public void setSuperItemCode(java.lang.String superItemCode){
		this.superItemCode = superItemCode;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  科目层级
	 */
	@Column(name ="LEVELS",nullable=true,length=5)
	public java.lang.Integer getLevels(){
		return this.levels;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  科目层级
	 */
	public void setLevels(java.lang.Integer levels){
		this.levels = levels;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  资金流向1， 资金流入-1 ，资金流出
	 */
	@Column(name ="SIGN",nullable=true,length=5)
	public java.lang.Integer getSign(){
		return this.sign;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  资金流向1， 资金流入-1 ，资金流出
	 */
	public void setSign(java.lang.Integer sign){
		this.sign = sign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否冲抵科目  1-是 0-否
	 */
	@Column(name ="IS_INTERNAL",nullable=false,length=1)
	public java.lang.String getIsInternal(){
		return this.isInternal;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否冲抵科目  1-是 0-否
	 */
	public void setIsInternal(java.lang.String isInternal){
		this.isInternal = isInternal;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否计划控制科目 1-是 0-否
	 */
	@Column(name ="IS_CONTROL",nullable=true,length=1)
	public java.lang.String getIsControl(){
		return this.isControl;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否计划控制科目 1-是 0-否
	 */
	public void setIsControl(java.lang.String isControl){
		this.isControl = isControl;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否刚性科目 1-是 0-否
	 */
	@Column(name ="IS_RIGID",nullable=true,length=1)
	public java.lang.String getIsRigid(){
		return this.isRigid;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否刚性科目 1-是 0-否
	 */
	public void setIsRigid(java.lang.String isRigid){
		this.isRigid = isRigid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否期初科目 1-是 0-否
	 */
	@Column(name ="IS_BEGIN_PERIOD",nullable=true,length=1)
	public java.lang.String getIsBeginPeriod(){
		return this.isBeginPeriod;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否期初科目 1-是 0-否
	 */
	public void setIsBeginPeriod(java.lang.String isBeginPeriod){
		this.isBeginPeriod = isBeginPeriod;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否期末科目 1-是 0-否
	 */
	@Column(name ="IS_END_PERIOD",nullable=true,length=1)
	public java.lang.String getIsEndPeriod(){
		return this.isEndPeriod;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否期末科目 1-是 0-否
	 */
	public void setIsEndPeriod(java.lang.String isEndPeriod){
		this.isEndPeriod = isEndPeriod;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  有效标志 0 无效 1 有效
	 */
	@Column(name ="VALID_SIGN",nullable=false,length=1)
	public java.lang.String getValidSign(){
		return this.validSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  有效标志 0 无效 1 有效
	 */
	public void setValidSign(java.lang.String validSign){
		this.validSign = validSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  同级科目关系
	 */
	@Column(name ="ITEM_RELATION",nullable=true,length=500)
	public java.lang.String getItemRelation(){
		return this.itemRelation;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  同级科目关系
	 */
	public void setItemRelation(java.lang.String itemRelation){
		this.itemRelation = itemRelation;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  超计划比例
	 */
	@Column(name ="OVER_PER_SPEND",nullable=true,scale=2,length=10)
	public java.math.BigDecimal getOverPerSpend(){
		return this.overPerSpend;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  超计划比例
	 */
	public void setOverPerSpend(java.math.BigDecimal overPerSpend){
		this.overPerSpend = overPerSpend;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  现金流科目代码
	 */
	@Column(name ="CASH_ITEM_CODE",nullable=true,length=60)
	public java.lang.String getCashItemCode(){
		return this.cashItemCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  现金流科目代码
	 */
	public void setCashItemCode(java.lang.String cashItemCode){
		this.cashItemCode = cashItemCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  现金流科目名称
	 */
	@Column(name ="CASH_ITEM_NAME",nullable=true,length=100)
	public java.lang.String getCashItemName(){
		return this.cashItemName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  现金流科目名称
	 */
	public void setCashItemName(java.lang.String cashItemName){
		this.cashItemName = cashItemName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  调拨科目代码
	 */
	@Column(name ="ALLOT_ITEM_CODE",nullable=true,length=40)
	public String getAllotItemCode() {
		return allotItemCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  调拨科目代码
	 */
	public void setAllotItemCode(String allotItemCode) {
		this.allotItemCode = allotItemCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	@Column(name ="RMK",nullable=true,length=200)
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
	 *@return: java.lang.String  是否系统科目（系统科目不能删除）系统科目：100-期初资金，200-资金增加，300-可用资金，400-资金减少，500-资金不足或多余，600-新增筹资或还款，700-期末资金，800表外科目
	 */
	@Column(name ="IS_SYSTEM",nullable=true,length=1)
	public java.lang.String getIsSystem(){
		return this.isSystem;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否系统科目（系统科目不能删除）系统科目：100-期初资金，200-资金增加，300-可用资金，400-资金减少，500-资金不足或多余，600-新增筹资或还款，700-期末资金，800表外科目
	 */
	public void setIsSystem(java.lang.String isSystem){
		this.isSystem = isSystem;
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
	@Column(name ="IS_WRITE_DETAIL",nullable=true)
	public String getIsWriteDetail() {
		return isWriteDetail;
	}
	public void setIsWriteDetail(String isWriteDetail) {
		this.isWriteDetail = isWriteDetail;
	}
}
