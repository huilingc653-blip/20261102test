package com.bytter.biz.budget.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
import java.math.BigDecimal;
/**   
 * @Title: Entity
 * @Description: FBS_BUDGET_RESOLVE_YEAR
 * @author onlineGenerator
 * @date 2017-07-26 14:29:58
 * @version 1.0
 *
 */
@Entity
@Table(name = "FBS_BUDGET_RESOLVE_YEAR", schema = "")
@SuppressWarnings("serial")
public class FbsBudgetResolveYear extends IdEntity implements java.io.Serializable {
	/**年计划ID*/
	private java.lang.String budgetId;
	/**科目代码*/
	private java.lang.String itemCode;
	/**科目名称*/
	private java.lang.String itemName;
	/**上报金额*/
	private java.math.BigDecimal bMoney;
	/**1月上报金额*/
	private java.math.BigDecimal bMoney1;
	/**2月上报金额*/
	private java.math.BigDecimal bMoney2;
	/**3月上报金额*/
	private java.math.BigDecimal bMoney3;
	/**4月上报金额*/
	private java.math.BigDecimal bMoney4;
	/**5月上报金额*/
	private java.math.BigDecimal bMoney5;
	/**6月上报金额*/
	private java.math.BigDecimal bMoney6;
	/**7月上报金额*/
	private java.math.BigDecimal bMoney7;
	/**8月上报金额*/
	private java.math.BigDecimal bMoney8;
	/**9月上报金额*/
	private java.math.BigDecimal bMoney9;
	/**10月上报金额*/
	private java.math.BigDecimal bMoney10;
	/**11月上报金额*/
	private java.math.BigDecimal bMoney11;
	/**12月上报金额*/
	private java.math.BigDecimal bMoney12;
	/**追加金额*/
	private BigDecimal bsuperMoney;
	/**1月追加金额*/
	private BigDecimal bsuperMoney1;
	/**2月追加金额*/
	private BigDecimal bsuperMoney2;
	/**3月追加金额*/
	private BigDecimal bsuperMoney3;
	/**4月追加金额*/
	private BigDecimal bsuperMoney4;
	/**5月追加金额*/
	private BigDecimal bsuperMoney5;
	/**6月追加金额*/
	private BigDecimal bsuperMoney6;
	/**7月追加金额*/
	private BigDecimal bsuperMoney7;
	/**8月追加金额*/
	private BigDecimal bsuperMoney8;
	/**9月追加金额*/
	private BigDecimal bsuperMoney9;
	/**10月追加金额*/
	private BigDecimal bsuperMoney10;
	/**11月追加金额*/
	private BigDecimal bsuperMoney11;
	/**12月追加金额*/
	private BigDecimal bsuperMoney12;
	/**本次追加金额*/
	private BigDecimal btempMoney;
	/**1月本次追加金额*/
	private BigDecimal btempMoney1;
	/**2月本次追加金额*/
	private BigDecimal btempMoney2;
	/**3月本次追加金额*/
	private BigDecimal btempMoney3;
	/**4月本次追加金额*/
	private BigDecimal btempMoney4;
	/**5月本次追加金额*/
	private BigDecimal btempMoney5;
	/**6月本次追加金额*/
	private BigDecimal btempMoney6;
	/**7月本次追加金额*/
	private BigDecimal btempMoney7;
	/**8月本次追加金额*/
	private BigDecimal btempMoney8;
	/**9月本次追加金额*/
	private BigDecimal btempMoney9;
	/**10月本次追加金额*/
	private BigDecimal btempMoney10;
	/**11月本次追加金额*/
	private BigDecimal btempMoney11;
	/**12月本次追加金额*/
	private BigDecimal btempMoney12;
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
	 *@return: java.lang.String  年计划ID
	 */
	@Column(name ="BUDGET_ID",nullable=false,length=32)
	public java.lang.String getBudgetId(){
		return this.budgetId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  年计划ID
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
	 *@return: java.math.BigDecimal  1月上报金额
	 */
	@Column(name ="B_MONEY_1",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBMoney1(){
		return this.bMoney1;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  1月上报金额
	 */
	public void setBMoney1(java.math.BigDecimal bMoney1){
		this.bMoney1 = bMoney1;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  2月上报金额
	 */
	@Column(name ="B_MONEY_2",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBMoney2(){
		return this.bMoney2;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  2月上报金额
	 */
	public void setBMoney2(java.math.BigDecimal bMoney2){
		this.bMoney2 = bMoney2;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  3月上报金额
	 */
	@Column(name ="B_MONEY_3",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBMoney3(){
		return this.bMoney3;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  3月上报金额
	 */
	public void setBMoney3(java.math.BigDecimal bMoney3){
		this.bMoney3 = bMoney3;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  4月上报金额
	 */
	@Column(name ="B_MONEY_4",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBMoney4(){
		return this.bMoney4;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  4月上报金额
	 */
	public void setBMoney4(java.math.BigDecimal bMoney4){
		this.bMoney4 = bMoney4;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  5月上报金额
	 */
	@Column(name ="B_MONEY_5",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBMoney5(){
		return this.bMoney5;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  5月上报金额
	 */
	public void setBMoney5(java.math.BigDecimal bMoney5){
		this.bMoney5 = bMoney5;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  6月上报金额
	 */
	@Column(name ="B_MONEY_6",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBMoney6(){
		return this.bMoney6;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  6月上报金额
	 */
	public void setBMoney6(java.math.BigDecimal bMoney6){
		this.bMoney6 = bMoney6;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  7月上报金额
	 */
	@Column(name ="B_MONEY_7",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBMoney7(){
		return this.bMoney7;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  7月上报金额
	 */
	public void setBMoney7(java.math.BigDecimal bMoney7){
		this.bMoney7 = bMoney7;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  8月上报金额
	 */
	@Column(name ="B_MONEY_8",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBMoney8(){
		return this.bMoney8;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  8月上报金额
	 */
	public void setBMoney8(java.math.BigDecimal bMoney8){
		this.bMoney8 = bMoney8;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  9月上报金额
	 */
	@Column(name ="B_MONEY_9",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBMoney9(){
		return this.bMoney9;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  9月上报金额
	 */
	public void setBMoney9(java.math.BigDecimal bMoney9){
		this.bMoney9 = bMoney9;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  10月上报金额
	 */
	@Column(name ="B_MONEY_10",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBMoney10(){
		return this.bMoney10;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  10月上报金额
	 */
	public void setBMoney10(java.math.BigDecimal bMoney10){
		this.bMoney10 = bMoney10;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  11月上报金额
	 */
	@Column(name ="B_MONEY_11",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBMoney11(){
		return this.bMoney11;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  11月上报金额
	 */
	public void setBMoney11(java.math.BigDecimal bMoney11){
		this.bMoney11 = bMoney11;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  12月上报金额
	 */
	@Column(name ="B_MONEY_12",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBMoney12(){
		return this.bMoney12;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  12月上报金额
	 */
	public void setBMoney12(java.math.BigDecimal bMoney12){
		this.bMoney12 = bMoney12;
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
	@Column(name ="BSUPER_MONEY_7", nullable=true,scale=2,length=15)
	public BigDecimal getBsuperMoney7() {
		return bsuperMoney7;
	}
	public void setBsuperMoney7(BigDecimal bsuperMoney7) {
		this.bsuperMoney7 = bsuperMoney7;
	}
	@Column(name ="BSUPER_MONEY_8",nullable=true,scale=2,length=15)
	public BigDecimal getBsuperMoney8() {
		return bsuperMoney8;
	}
	public void setBsuperMoney8(BigDecimal bsuperMoney8) {
		this.bsuperMoney8 = bsuperMoney8;
	}
	@Column(name ="BSUPER_MONEY_9",nullable=true,scale=2,length=15)
	public BigDecimal getBsuperMoney9() {
		return bsuperMoney9;
	}
	public void setBsuperMoney9(BigDecimal bsuperMoney9) {
		this.bsuperMoney9 = bsuperMoney9;
	}
	@Column(name ="BSUPER_MONEY_10",nullable=true,scale=2,length=15)
	public BigDecimal getBsuperMoney10() {
		return bsuperMoney10;
	}
	public void setBsuperMoney10(BigDecimal bsuperMoney10) {
		this.bsuperMoney10 = bsuperMoney10;
	}
	@Column(name ="BSUPER_MONEY_11",nullable=true,scale=2,length=15)
	public BigDecimal getBsuperMoney11() {
		return bsuperMoney11;
	}
	public void setBsuperMoney11(BigDecimal bsuperMoney11) {
		this.bsuperMoney11 = bsuperMoney11;
	}
	@Column(name ="BSUPER_MONEY_12",nullable=true,scale=2,length=15)
	public BigDecimal getBsuperMoney12() {
		return bsuperMoney12;
	}
	public void setBsuperMoney12(BigDecimal bsuperMoney12) {
		this.bsuperMoney12 = bsuperMoney12;
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
	@Column(name ="BTEMP_MONEY_7",nullable=true,scale=2,length=15)
	public BigDecimal getBtempMoney7() {
		return btempMoney7;
	}
	public void setBtempMoney7(BigDecimal btempMoney7) {
		this.btempMoney7 = btempMoney7;
	}
	@Column(name ="BTEMP_MONEY_8",nullable=true,scale=2,length=15)
	public BigDecimal getBtempMoney8() {
		return btempMoney8;
	}
	public void setBtempMoney8(BigDecimal btempMoney8) {
		this.btempMoney8 = btempMoney8;
	}
	@Column(name ="BTEMP_MONEY_9",nullable=true,scale=2,length=15)
	public BigDecimal getBtempMoney9() {
		return btempMoney9;
	}
	public void setBtempMoney9(BigDecimal btempMoney9) {
		this.btempMoney9 = btempMoney9;
	}
	@Column(name ="BTEMP_MONEY_10",nullable=true,scale=2,length=15)
	public BigDecimal getBtempMoney10() {
		return btempMoney10;
	}
	public void setBtempMoney10(BigDecimal btempMoney10) {
		this.btempMoney10 = btempMoney10;
	}
	@Column(name ="BTEMP_MONEY_11",nullable=true,scale=2,length=15)
	public BigDecimal getBtempMoney11() {
		return btempMoney11;
	}
	public void setBtempMoney11(BigDecimal btempMoney11) {
		this.btempMoney11 = btempMoney11;
	}
	@Column(name ="BTEMP_MONEY_12",nullable=true,scale=2,length=15)
	public BigDecimal getBtempMoney12() {
		return btempMoney12;
	}
	public void setBtempMoney12(BigDecimal btempMoney12) {
		this.btempMoney12 = btempMoney12;
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
}
