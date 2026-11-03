package com.bytter.biz.transfer.swap.util;
import java.util.*;
public class ErpHisDtlUtils {
	/*** 预警数据主信息列表头 */
	public static Map<String,List<Map<String,String>>> WARNDATA_HEADER=new LinkedHashMap<>();
	/*** 单据补录*/
	public final static String TYPECODE_NEWSET = "newSet";
	/*** 模板补录*/
	public final static String TYPECODE_TEMSET = "temSet";
	static{
		initHeader();
	}
	
	/**
	 * 预警数据明细信息列表头初始化
	 */
	public static void initHeader(){
		Map<String,String> enity=null;
		List<Map<String,String>> entityList=null;
//		enity.put("field", "");
//		enity.put("type", "num");
//		enity.put("type", "money");
//		enity.put("text", "名称");
		entityList=new ArrayList<>();
		/*** 单据补录*/
		WARNDATA_HEADER.put(TYPECODE_NEWSET, entityList);
		enity=new HashMap<>();
		enity.put("field", "isAction");
		enity.put("type", "action");
//		enity.put("text", "操作");
		enity.put("text", "table.createRepayPlan.operation");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "status");
//		enity.put("text", "状态");
		enity.put("text", "table.fbsBudget.status");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "transTime");
		enity.put("type", "date");
//		enity.put("text", "交易时间");
		enity.put("text", "table.bisExc.transTime");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "corpName");
//		enity.put("text", "单位名称");
		enity.put("text", "table.nisPersonnelDetail.corpId");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "bankAcc");
//		enity.put("text", "本方账户");
		enity.put("text", "table.bisAccDtlClaim.bankAcc");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "accName");
//		enity.put("text", "本方账户名");
		enity.put("text", "table.bisAccDtlClaim.accName");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "oppAccName");
//		enity.put("text", "对方账户");
		enity.put("text", "table.bisAccDtlClaim.oppAccNo");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "oppAccNo");
//		enity.put("text", "对方账户名");
		enity.put("text", "table.bisAccDtlClaim.oppAccName");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "amt");
		enity.put("type", "money");
//		enity.put("text", "交易金额");
		enity.put("text", "table.btBankAccDtl.amt");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "curName");
//		enity.put("text", "币别");
		enity.put("text", "table.fbsBudgetDetail.curId");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "remark");
//		enity.put("text", "摘要");
		enity.put("text", "table.fcUnionAccbook.summaryContent");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "opName");
//		enity.put("text", "操作人");
		enity.put("text", "table.fcPredrawRushDtl.operatorCode");
		entityList.add(enity);
		entityList=new ArrayList<>();
		WARNDATA_HEADER.put(TYPECODE_TEMSET, entityList);
		enity=new HashMap<>();
		enity.put("field", "isAction");
		enity.put("type", "action");
//		enity.put("text", "操作");
		enity.put("text", "table.createRepayPlan.operation");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "statusN");
//		enity.put("text", "状态");
		enity.put("text", "table.fbsBudget.status");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "transTime");
		enity.put("type", "date");
//		enity.put("text", "交易时间");
		enity.put("text", "table.bisExc.transTime");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "corpName");
//		enity.put("text", "单位名称");
		enity.put("text", "table.nisPersonnelDetail.corpId");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "bankAcc");
//		enity.put("text", "本方账户");
		enity.put("text", "table.bisAccDtlClaim.bankAcc");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "accName");
//		enity.put("text", "本方账户名");
		enity.put("text", "table.bisAccDtlClaim.accName");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "oppAccName");
//		enity.put("text", "对方账户");
		enity.put("text", "table.bisAccDtlClaim.oppAccNo");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "oppAccNo");
//		enity.put("text", "对方账户名");
		enity.put("text", "table.bisAccDtlClaim.oppAccName");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "amt");
		enity.put("type", "money");
//		enity.put("text", "交易金额");
		enity.put("text", "table.btBankAccDtl.amt");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "curName");
//		enity.put("text", "币别");
		enity.put("text", "table.fbsBudgetDetail.curId");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "remark");
//		enity.put("text", "摘要");
		enity.put("text", "table.fcUnionAccbook.summaryContent");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "opName");
//		enity.put("text", "操作人");
		enity.put("text", "table.fcPredrawRushDtl.operatorCode");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "templateName");
//		enity.put("text", "模板名称");
		enity.put("text", "table.transfer.templateName");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "typeName");
//		enity.put("text", "模板类型名称");
		enity.put("text", "table.transfer.templateTypeName");
		entityList.add(enity);
	}
	/**
	 * 根据模板补录和单据补录取 数据主信息列表头信息
	 * @param typeCode
	 * @return 数据主信息列表头信息list
	 */
	public static List<Map<String,String>> getDataHeader(String typeCode){
		initHeader();
		return WARNDATA_HEADER.get(typeCode);
	}
}
