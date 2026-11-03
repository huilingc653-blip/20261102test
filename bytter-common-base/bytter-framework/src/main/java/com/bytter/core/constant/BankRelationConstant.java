package com.bytter.core.constant;
/**
 * @author fusb
 *	@version 1.0
 *  @Title BankRelationConstant 
 *  @Description 银企关系常量类
 *  @date 2018年11月20日
 */
public class BankRelationConstant {
	
	//取值来源
	/*** 取值来源："1"-自动获取  */
	public final static String SOURCE_AUTO_GET="1";
	/*** 取值来源："2"-手工录入  */
	public final static String SOURCE_MANUAL_ENTRY="2";
	
	//评分规则
	/*** 评分规则："1"-由高至低自动排序评分  */
	public final static String RULE_HIGH_TO_LOW="1";
	/*** 评分规则："2"-由低至高自动排序评分  */
	public final static String RULE_LOW_TO_HIGH="2";
	/*** 评分规则："3"-随机排序手动评分  */
	public final static String RULE_RANDOM="3";
	//状态
	public final static String RELATION_GRADE_STATUS_Y="1";
	public final static String RELATION_GRADE_STATUS_N="0";
	
	/*** 查询评级指标资源 */
	public static final String RB_BANK_RELATION_GRADEINDEX_QUERY = "bank:relation:gradeindex:query";
	/*** 新增评级指标资源 */
	public static final String RB_BANK_RELATION_GRADEINDEX_APPLY = "bank:relation:gradeindex:apply";
	/**查询资源*/
	public final static String BANK_RELATION_VALUE_QUERY = "bankRelation:relation:value:query";
	
	/** 字典组*/
	public final static String RB_RELATION_MODULE_NAME = "relation.moduleName";
	
}
