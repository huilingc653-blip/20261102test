package com.bytter.biz.budget.entity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 资金计划上报明细抽象类
 * @author onlineGenerator
 * @date 2017-07-17 14:51:48
 * @version 1.0
 *
 */
@MappedSuperclass
public abstract class FbsBudgetDtlAbs extends IdEntity {
	/**计划ID*/
	private String budgetId;
	/**科目代码*/
	private String itemCode;
	/**科目名称*/
	private String itemName;
	/**上报金额*/
	private BigDecimal bMoney;
	/**追加金额*/
	private BigDecimal bsuperMoney;
	/**本次追加金额*/
	private BigDecimal btempMoney;
	/**实际金额*/
	private BigDecimal factMoney;
	/**差异原因*/
	private String diffReason;
	/**分析状态*/
	private Integer analyseStatus;
	/**备注*/
	private String rmk;
	/**
	 *方法: 取得String
	 *@return: String  计划ID
	 */
	@Column(name ="BUDGET_ID",nullable=true,length=32)
	public String getBudgetId(){
		return this.budgetId;
	}
	/**
	 *方法: 设置String
	 *@param: String  计划ID
	 */
	public void setBudgetId(String budgetId){
		this.budgetId = budgetId;
	}
	/**
	 *方法: 取得String
	 *@return: String  科目代码
	 */
	@Column(name ="ITEM_CODE",nullable=false,length=40)
	public String getItemCode(){
		return this.itemCode;
	}
	/**
	 *方法: 设置String
	 *@param: String  科目代码
	 */
	public void setItemCode(String itemCode){
		this.itemCode = itemCode;
	}
	/**
	 *方法: 取得String
	 *@return: String  科目名称
	 */
	@Column(name ="ITEM_NAME",nullable=true,length=100)
	public String getItemName(){
		return this.itemName;
	}
	/**
	 *方法: 设置String
	 *@param: String  科目名称
	 */
	public void setItemName(String itemName){
		this.itemName = itemName;
	}
	
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  上报金额
	 */
	@Column(name ="B_MONEY",nullable=true,scale=2,length=15)
	public BigDecimal getbMoney() {
		return bMoney;
	}
	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  上报金额
	 */
	public void setbMoney(BigDecimal bMoney) {
		this.bMoney = bMoney;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  追加金额
	 */
	@Column(name ="BSUPER_MONEY",nullable=true,scale=2,length=15)
	public BigDecimal getBsuperMoney(){
		return this.bsuperMoney;
	}
	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  追加金额
	 */
	public void setBsuperMoney(BigDecimal bsuperMoney){
		this.bsuperMoney = bsuperMoney;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  本次追加金额
	 */
	@Column(name ="BTEMP_MONEY",nullable=true,scale=2,length=15)
	public BigDecimal getBtempMoney(){
		return this.btempMoney;
	}
	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  本次追加金额
	 */
	public void setBtempMoney(BigDecimal btempMoney){
		this.btempMoney = btempMoney;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  实际金额
	 */
	@Column(name ="FACT_MONEY",nullable=true,scale=2,length=15)
	public BigDecimal getFactMoney() {
		return factMoney;
	}
	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  实际金额
	 */
	public void setFactMoney(BigDecimal factMoney) {
		this.factMoney = factMoney;
	}
	/**
	 *方法: 取得String
	 *@return: String  差异原因
	 */
	@Column(name ="DIFF_REASON",nullable=true,length=100)
	public String getDiffReason() {
		return diffReason;
	}
	/**
	 *方法: 设置String
	 *@param: String  差异原因
	 */
	public void setDiffReason(String diffReason) {
		this.diffReason = diffReason;
	}
	/**
	 *方法: 取得Integer
	 *@return: Integer  分析状态
	 */
	@Column(name ="ANALYSE_STATUS",nullable=true,length=10)
	public Integer getAnalyseStatus() {
		return analyseStatus;
	}
    /**
     *方法: 设置Integer
     *@param: Integer  分析状态
     */
	public void setAnalyseStatus(Integer analyseStatus) {
		this.analyseStatus = analyseStatus;
	}
	@Column(name ="RMK")
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	/**
	 * 计划周期参数表
	 */
	public static Object[][] FBS_BUDGET_PERIOD_PARAM = {
		{"1","年计划","FBS_BUDGET_YEAR",FbsBudgetYear.class,"ycrl"},
		{"2","季计划","FBS_BUDGET_QUARTER",FbsBudgetQuarter.class ,"qcrl"},
		{"3","月计划","FBS_BUDGET_MONTH",FbsBudgetMonth.class,"mcrl"},
		{"4","周计划","FBS_BUDGET_WEEK",FbsBudgetWeek.class,"wcrl"},
		{"5","天计划","FBS_BUDGET_DAY",FbsBudgetDay.class,"dcrl"}
	};
	
	/**
	 * 根据周期类型获取实例
	 * @param timeType
	 * @return
	 */
	public static FbsBudgetDtlAbs getFbsBudgetDtlByTimeType(String timeType){
		Class clazz = null;
		FbsBudgetDtlAbs fbsBudgetDtlAbs = null;
		for(Object[] obj : FbsBudgetDtlAbs.FBS_BUDGET_PERIOD_PARAM){
			String type = (String)obj[0];
			if(type.equals(timeType)){
				clazz = (Class)obj[3];
				break;
			}
		}
		if(clazz != null){
			try {
				fbsBudgetDtlAbs = (FbsBudgetDtlAbs)clazz.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return fbsBudgetDtlAbs;
	}
	
	/**
	 * 根据周期类型获取上报明细表名
	 * @param timeType
	 * @return
	 */
	public static String getTableNameByTimeType(String timeType){
		for(Object[] obj : FbsBudgetDtlAbs.FBS_BUDGET_PERIOD_PARAM){
			String type = (String)obj[0];
			if(type.equals(timeType)){
				return (String)obj[2];
			}
		}
		return null;
	}
	
	/**
	 * 根据周期类型获取实体名称
	 * @param timeType
	 * @return
	 */
	public static String  getEntityNameByTimeType(String timeType){
		FbsBudgetDtlAbs abs =  getFbsBudgetDtlByTimeType(timeType);
		if(abs != null){
			return abs.getClass().getName();
		}
		return "";
	}
}
