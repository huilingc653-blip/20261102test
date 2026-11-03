package com.bytter.bill.domain.billbank.pool.utils;
import java.util.HashMap;
import java.util.Map;
/**
 * 票据池帮助类
 * @author BYTTER
 *
 */
public class BillPoolUtils {
	/**额度操作类型和表对应Map**/
	public static Map<String,String> limitTypeToTable=new HashMap<String,String>();
	static{
		limitTypeToTable.put(BillPoolConstant.LIMIT_TYPE_APPLY, "AB_POOL_LIMIT_APPLY");
		limitTypeToTable.put(BillPoolConstant.LIMIT_TYPE_APPLYCHECK, "AB_POOL_LIMIT_APPLYCHECK");
		limitTypeToTable.put(BillPoolConstant.LIMIT_TYPE_ALLOT, "AB_POOL_LIMIT_ALLOT");
		limitTypeToTable.put(BillPoolConstant.LIMIT_TYPE_ALLOTCHECK, "AB_POOL_LIMIT_ALLOTCHECK");
	}
	
	/**
	 * 根据额度类型获取对应表名称
	 * @param limitType 额度类型
	 * @return 表名
	 */
	public static String getLimitTableByType(String limitType){
		return limitTypeToTable.get(limitType);
	}
}
