package com.bytter.biz.budget.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
import java.math.BigDecimal;
/**   
 * @Title: Entity
 * @Description: FBS_BUDGET_RESOLVE_MONTH
 * @author onlineGenerator
 * @date 2017-08-04 10:03:40
 * @version 1.0
 *
 */
@Entity
@Table(name = "FBS_BUDGET_RESOLVE_MONTH", schema = "")
@SuppressWarnings("serial")
public class FbsBudgetResolveMonth extends IdEntity implements java.io.Serializable {
	/**月计划ID*/
	private java.lang.String budgetId;
	/**科目代码*/
	private java.lang.String itemCode;
	/**科目名称*/
	private java.lang.String itemName;
	/**上报金额*/
	private java.math.BigDecimal bMoney;
	/**第1周上报金额*/
	private java.math.BigDecimal bMoney1;
	/**第2周上报金额*/
	private java.math.BigDecimal bMoney2;
	/**第3周上报金额*/
	private java.math.BigDecimal bMoney3;
	/**第4周上报金额*/
	private java.math.BigDecimal bMoney4;
	/**第5周上报金额*/
	private java.math.BigDecimal bMoney5;
	/**第6周上报金额*/
	private java.math.BigDecimal bMoney6;
	/**追加金额*/
	private BigDecimal bsuperMoney;
	/**第1周追加金额*/
	private BigDecimal bsuperMoney1;
	/**第2周追加金额*/
	private BigDecimal bsuperMoney2;
	/**第3周追加金额*/
	private BigDecimal bsuperMoney3;
	/**第4周追加金额*/
	private BigDecimal bsuperMoney4;
	/**第5周追加金额*/
	private BigDecimal bsuperMoney5;
	/**第6周追加金额*/
	private BigDecimal bsuperMoney6;
	/**本次追加金额*/
	private BigDecimal btempMoney;
	/**第1周本次追加金额*/
	private BigDecimal btempMoney1;
	/**第2周本次追加金额*/
	private BigDecimal btempMoney2;
	/**第3周本次追加金额*/
	private BigDecimal btempMoney3;
	/**第4周本次追加金额*/
	private BigDecimal btempMoney4;
	/**第5周本次追加金额*/
	private BigDecimal btempMoney5;
	/**第6周本次追加金额*/
	private BigDecimal btempMoney6;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  月计划ID
	 */
	@Column(name ="BUDGET_ID",nullable=false,length=32)
	public java.lang.String getBudgetId(){
		return this.budgetId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  月计划ID
	 */
	public void setBudgetId(java.lang.String budgetId){
		this.budgetId = budgetId;
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
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  上报金额
	 */
	@Column(name ="B_MONEY",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBMoney(){
		return this.bMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  上报金额
	 */
	public void setBMoney(java.math.BigDecimal bMoney){
		this.bMoney = bMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  第1周上报金额
	 */
	@Column(name ="B_MONEY_1",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBMoney1(){
		return this.bMoney1;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  第1周上报金额
	 */
	public void setBMoney1(java.math.BigDecimal bMoney1){
		this.bMoney1 = bMoney1;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  第2周上报金额
	 */
	@Column(name ="B_MONEY_2",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBMoney2(){
		return this.bMoney2;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  第2周上报金额
	 */
	public void setBMoney2(java.math.BigDecimal bMoney2){
		this.bMoney2 = bMoney2;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  第3周上报金额
	 */
	@Column(name ="B_MONEY_3",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBMoney3(){
		return this.bMoney3;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  第3周上报金额
	 */
	public void setBMoney3(java.math.BigDecimal bMoney3){
		this.bMoney3 = bMoney3;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  第4周上报金额
	 */
	@Column(name ="B_MONEY_4",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBMoney4(){
		return this.bMoney4;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  第4周上报金额
	 */
	public void setBMoney4(java.math.BigDecimal bMoney4){
		this.bMoney4 = bMoney4;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  第5周上报金额
	 */
	@Column(name ="B_MONEY_5",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBMoney5(){
		return this.bMoney5;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  第5周上报金额
	 */
	public void setBMoney5(java.math.BigDecimal bMoney5){
		this.bMoney5 = bMoney5;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  第6周上报金额
	 */
	@Column(name ="B_MONEY_6",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBMoney6(){
		return this.bMoney6;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  第6周上报金额
	 */
	public void setBMoney6(java.math.BigDecimal bMoney6){
		this.bMoney6 = bMoney6;
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
	@Column(name ="BSUPER_MONEY",nullable=true,scale=2,length=15)
	public BigDecimal getBsuperMoney() {
		return bsuperMoney;
	}
	public void setBsuperMoney(BigDecimal bsuperMoney) {
		this.bsuperMoney = bsuperMoney;
	}
	@Column(name ="BSUPER_MONEY_1",nullable=true,scale=2,length=15)
	public BigDecimal getBsuperMoney1() {
		return bsuperMoney1;
	}
	public void setBsuperMoney1(BigDecimal bsuperMoney1) {
		this.bsuperMoney1 = bsuperMoney1;
	}
	@Column(name ="BSUPER_MONEY_2",nullable=true,scale=2,length=15)
	public BigDecimal getBsuperMoney2() {
		return bsuperMoney2;
	}
	public void setBsuperMoney2(BigDecimal bsuperMoney2) {
		this.bsuperMoney2 = bsuperMoney2;
	}
	@Column(name ="BSUPER_MONEY_3",nullable=true,scale=2,length=15)
	public BigDecimal getBsuperMoney3() {
		return bsuperMoney3;
	}
	public void setBsuperMoney3(BigDecimal bsuperMoney3) {
		this.bsuperMoney3 = bsuperMoney3;
	}
	@Column(name ="BSUPER_MONEY_4",nullable=true,scale=2,length=15)
	public BigDecimal getBsuperMoney4() {
		return bsuperMoney4;
	}
	public void setBsuperMoney4(BigDecimal bsuperMoney4) {
		this.bsuperMoney4 = bsuperMoney4;
	}
	@Column(name ="BSUPER_MONEY_5",nullable=true,scale=2,length=15)
	public BigDecimal getBsuperMoney5() {
		return bsuperMoney5;
	}
	public void setBsuperMoney5(BigDecimal bsuperMoney5) {
		this.bsuperMoney5 = bsuperMoney5;
	}
	@Column(name ="BSUPER_MONEY_6",nullable=true,scale=2,length=15)
	public BigDecimal getBsuperMoney6() {
		return bsuperMoney6;
	}
	public void setBsuperMoney6(BigDecimal bsuperMoney6) {
		this.bsuperMoney6 = bsuperMoney6;
	}
	@Column(name ="BTEMP_MONEY",nullable=true,scale=2,length=15)
	public BigDecimal getBtempMoney() {
		return btempMoney;
	}
	public void setBtempMoney(BigDecimal btempMoney) {
		this.btempMoney = btempMoney;
	}
	@Column(name ="BTEMP_MONEY_1",nullable=true,scale=2,length=15)
	public BigDecimal getBtempMoney1() {
		return btempMoney1;
	}
	public void setBtempMoney1(BigDecimal btempMoney1) {
		this.btempMoney1 = btempMoney1;
	}
	@Column(name ="BTEMP_MONEY_2",nullable=true,scale=2,length=15)
	public BigDecimal getBtempMoney2() {
		return btempMoney2;
	}
	public void setBtempMoney2(BigDecimal btempMoney2) {
		this.btempMoney2 = btempMoney2;
	}
	@Column(name ="BTEMP_MONEY_3",nullable=true,scale=2,length=15)
	public BigDecimal getBtempMoney3() {
		return btempMoney3;
	}
	public void setBtempMoney3(BigDecimal btempMoney3) {
		this.btempMoney3 = btempMoney3;
	}
	@Column(name ="BTEMP_MONEY_4",nullable=true,scale=2,length=15)
	public BigDecimal getBtempMoney4() {
		return btempMoney4;
	}
	public void setBtempMoney4(BigDecimal btempMoney4) {
		this.btempMoney4 = btempMoney4;
	}
	@Column(name ="BTEMP_MONEY_5",nullable=true,scale=2,length=15)
	public BigDecimal getBtempMoney5() {
		return btempMoney5;
	}
	public void setBtempMoney5(BigDecimal btempMoney5) {
		this.btempMoney5 = btempMoney5;
	}
	@Column(name ="BTEMP_MONEY_6",nullable=true,scale=2,length=15)
	public BigDecimal getBtempMoney6() {
		return btempMoney6;
	}
	public void setBtempMoney6(BigDecimal btempMoney6) {
		this.btempMoney6 = btempMoney6;
	}
}
