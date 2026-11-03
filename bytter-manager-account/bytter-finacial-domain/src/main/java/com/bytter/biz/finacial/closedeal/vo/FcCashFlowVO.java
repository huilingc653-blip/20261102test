package com.bytter.biz.finacial.closedeal.vo;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: FC_CASH_FLOW
 * @author onlineGenerator
 * @date 2017-07-17 18:36:08
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class FcCashFlowVO implements Serializable {
	@Id
	private String id;
	/**日期*/
	private Date tallyDate;
	/**网点*/
	private String netCode;
	private String netName;
	/**币别*/
	private String curCode;
	private String curName;
	/**项目*/
	private String cashItem;
	/**
	 *项目名
	 */
	private String cashItemName;
	/**
	 * lineNo
	 */
	private Integer lineNo;
	/**
	 * 行次
	 */
	private Integer orderNo;
	/**
	 * formula
	 */
	private String formula;
	/**
	 * 正负
	 */
	private Integer rect;
	/**
	 * 左右
	 */
	private String direction;
	/**金额*/
	private BigDecimal money;
	/**创建时间*/
	private Date createTime;
	/**创建用户代码*/
	private String createBy;
	/**最后更新时间*/
	private Date updateTime;
	/**最后更新用户代码*/
	private String updateBy;
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  日期
	 */
	public Date getTallyDate(){
		return this.tallyDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  日期
	 */
	public void setTallyDate(Date tallyDate){
		this.tallyDate = tallyDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  网点
	 */
	public String getNetCode(){
		return this.netCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  网点
	 */
	public void setNetCode(String netCode){
		this.netCode = netCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别
	 */
	public String getCurCode(){
		return this.curCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别
	 */
	public void setCurCode(String curCode){
		this.curCode = curCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  项目
	 */
	public String getCashItem(){
		return this.cashItem;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  项目
	 */
	public void setCashItem(String cashItem){
		this.cashItem = cashItem;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  金额
	 */
	public BigDecimal getMoney(){
		return this.money;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  金额
	 */
	public void setMoney(BigDecimal money){
		this.money = money;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	public Date getCreateTime(){
		return this.createTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建用户代码
	 */
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
	public Date getUpdateTime(){
		return this.updateTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  最后更新时间
	 */
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  最后更新用户代码
	 */
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public String getCashItemName() {
		return cashItemName;
	}
	public void setCashItemName(String cashItemName) {
		this.cashItemName = cashItemName;
	}
	public Integer getLineNo() {
		return lineNo;
	}
	public void setLineNo(Integer lineNo) {
		this.lineNo = lineNo;
	}
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public Integer getRect() {
		return rect;
	}
	public void setRect(Integer rect) {
		this.rect = rect;
	}
}
