package com.bytter.biz.fundwarn.spi.service;
import com.bytter.core.base.entity.ICurrentUser;
/**
 * 预警公用接口
 * @author zhengg
 */
public interface IFundWarnService {
	
	/**
	 * 处理事中预警 功能执行
	 * @param info 对应数据实体信息
	 * @param typeCode 预警类型（FundWarnConstant:TYPECODE_EQCORPPAY相同公司付款、TYPECODE_BANKACCPAY大额账户支出、TYPECODE_CASHPAY大额现金支出、TYPECODE_BUDGETPROGRESS 预算执行进度、TYPECODE_DAYSUMPAY 单日累计支付）
	 */
	public void warnExecute(Object info,String typeCode,ICurrentUser user);
	
	/**
	 * 处理 相同公司付款事中预警 功能执行
	 * @param info 对应数据实体信息
	 */
	public void warnExecuteByEqCorpPay(Object info,ICurrentUser user);
	
	/**
	 * 处理 大额账户支出事中预警 功能执行
	 * @param info 对应数据实体信息
	 */
	public void warnExecuteBankAccPay(Object info,ICurrentUser user);
	
	/**
	 * 处理 大额现金支出事中预警 功能执行
	 * @param info 对应数据实体信息
	 */
	public void warnExecuteCashPay(Object info,ICurrentUser user);
	/**
	 * 处理 预算执行进度事中预警 功能执行
	 * @param info 对应数据实体信息
	 */
	public void warnExecuteBudgetProgress(Object info,ICurrentUser user);
	/**
	 * 处理 单日累计支付 功能执行
	 * @param info 对应数据实体信息
	 */
	public void warnExecuteDaySumPay(Object info,ICurrentUser user);
}
