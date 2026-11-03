package com.bytter.ras.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bytter.ras.vo.RasAuditStrategyVO;
import com.bytter.ras.vo.RasPayTypeVO;
import com.bytter.ras.vo.RasReceivedDtlVO;
import com.bytter.ras.vo.RasRevenueDtlVO;
import com.bytter.ras.vo.RasRulesVO;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.ras.api.constant.RasConstant;
import com.bytter.system.api.IMultiLangService;

/**
 * 营收稽核帮助类
 * @author zhengg
 *
 */
public class RasUtils {

	/**
	 * 营收数据导入列表
	 */
	private static List<Map<String,String>> revenueFieldConf=new ArrayList<>();
	
	/**
	 * 实收数据导入列表
	 */
	private static List<Map<String,String>> receivedFieldConf=new ArrayList<>();
	
	static{
//		serialNo交易流水号
//		orderNO营收订单号
//		shopName门店名称
//		transDate交易日期
//		money营收金额
//		payType缴款方式
//		custName客户名称
//		custAcc客户账号
//		rmk备注
//		List<Map<String,String>> listField;
//		Map<String,String> fieldMap; 
//		fieldMap.put(“key”, “serialNo”); --对应存储数据库的key值和对应营收数据的字段
//		fieldMap.put("nameCNY", "交易流水号");//列表中文名称
//		fieldMap.put(“name”, “国际化的key(交易流水号)”);--显示维护列表标题
//		fieldMap.put(“required”, “true\false|1\0”);--是否需要必填
//		fieldMap.put(“value”, “对应维护的值”);
//		listField.add(fieldMap);
		initRevenueFieldConf();
		initReceivedFieldConf();
	}

	/**
	 * 营收数据字典初始化
	 */
	private static void initRevenueFieldConf(){

		//营收数据字典初始化
		Map<String,String> fieldMap=null;
		
//		serialNo交易流水
		fieldMap=new HashMap<>();
		fieldMap.put("key", "serialNo"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "ras.revenue.Conf.serialNo");//显示维护列表标题
		fieldMap.put("nameCNY", "交易流水号");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		revenueFieldConf.add(fieldMap);
//		orderNo营收订单号
		fieldMap=new HashMap<>();
		fieldMap.put("key", "orderNo"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "ras.revenue.Conf.orderNo");//显示维护列表标题
		fieldMap.put("nameCNY", "营收订单号");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		revenueFieldConf.add(fieldMap);
//		shopName门店名称
		fieldMap=new HashMap<>();
		fieldMap.put("key", "shopName"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "ras.revenue.Conf.shopName");//显示维护列表标题
		fieldMap.put("nameCNY", "门店名称");//中文名称
		fieldMap.put("required", "true");//是否需要必填
		fieldMap.put("value", "");
		revenueFieldConf.add(fieldMap);
//		transDate交易日期
		fieldMap=new HashMap<>();
		fieldMap.put("key", "transDate"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "ras.revenue.Conf.transDate");//显示维护列表标题
		fieldMap.put("nameCNY", "交易日期");//中文名称
		fieldMap.put("required", "true");//是否需要必填
		fieldMap.put("value", "");
		revenueFieldConf.add(fieldMap);
//		money营收金额
		fieldMap=new HashMap<>();
		fieldMap.put("key", "money"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "ras.revenue.Conf.money");//显示维护列表标题
		fieldMap.put("nameCNY", "营收金额");//中文名称
		fieldMap.put("required", "true");//是否需要必填
		fieldMap.put("value", "");
		revenueFieldConf.add(fieldMap);
//		payType缴款方式
		fieldMap=new HashMap<>();
		fieldMap.put("key", "payType"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "ras.revenue.Conf.payType");//显示维护列表标题
		fieldMap.put("nameCNY", "缴款方式");//中文名称
		fieldMap.put("required", "true");//是否需要必填
		fieldMap.put("value", "");
		revenueFieldConf.add(fieldMap);
//		custName客户名称
		fieldMap=new HashMap<>();
		fieldMap.put("key", "custName"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "ras.revenue.Conf.custName");//显示维护列表标题
		fieldMap.put("nameCNY", "客户名称");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		revenueFieldConf.add(fieldMap);
//		custAcc客户账号
		fieldMap=new HashMap<>();
		fieldMap.put("key", "custAcc"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "ras.revenue.Conf.custAcc");//显示维护列表标题
		fieldMap.put("nameCNY", "客户账号");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		revenueFieldConf.add(fieldMap);
//		rmk备注
		fieldMap=new HashMap<>();
		fieldMap.put("key", "rmk"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "ras.revenue.Conf.rmk");//显示维护列表标题
		fieldMap.put("nameCNY", "备注");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		revenueFieldConf.add(fieldMap);
	}
	
	/**
	 * 实收数据字典初始化
	 */
	private static void initReceivedFieldConf(){

		//实收数据字典初始化
		Map<String,String> fieldMap=null;

//		serialNo交易流水号
		fieldMap=new HashMap<>();
		fieldMap.put("key", "serialNo"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "ras.received.Conf.serialNo");//显示维护列表标题
		fieldMap.put("nameCNY", "交易流水号");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		receivedFieldConf.add(fieldMap);
//		orderNo实收订单号
		fieldMap=new HashMap<>();
		fieldMap.put("key", "orderNO"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "ras.received.Conf.orderNo");//显示维护列表标题
		fieldMap.put("nameCNY", "实收订单号");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		receivedFieldConf.add(fieldMap);
//		shopName门店名称
		fieldMap=new HashMap<>();
		fieldMap.put("key", "shopName"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "ras.received.Conf.shopName");//显示维护列表标题
		fieldMap.put("nameCNY", "门店名称");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		receivedFieldConf.add(fieldMap);
//		payeeAcc收款账号
		fieldMap=new HashMap<>();
		fieldMap.put("key", "payeeAcc"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "ras.received.Conf.payeeAcc");//显示维护列表标题
		fieldMap.put("nameCNY", "收款账号");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		receivedFieldConf.add(fieldMap);
//		payeeAccName收款户名
		fieldMap=new HashMap<>();
		fieldMap.put("key", "payeeAccName"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "ras.received.Conf.payeeAccName");//显示维护列表标题
		fieldMap.put("nameCNY", "收款户名");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		receivedFieldConf.add(fieldMap);
//		payeeBankName收款账号开户行
		fieldMap=new HashMap<>();
		fieldMap.put("key", "payeeBankName"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "ras.received.Conf.payeeBankName");//显示维护列表标题
		fieldMap.put("nameCNY", "收款账号开户行");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		receivedFieldConf.add(fieldMap);
//		transDate交易日期
		fieldMap=new HashMap<>();
		fieldMap.put("key", "transDate"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "ras.received.Conf.transDate");//显示维护列表标题
		fieldMap.put("nameCNY", "交易日期");//中文名称
		fieldMap.put("required", "true");//是否需要必填
		fieldMap.put("value", "");
		receivedFieldConf.add(fieldMap);
//		payType缴款方式
		fieldMap=new HashMap<>();
		fieldMap.put("key", "rmpayTypek"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "ras.received.Conf.payType");//显示维护列表标题
		fieldMap.put("nameCNY", "缴款方式");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		receivedFieldConf.add(fieldMap);
//		money实收金额
		fieldMap=new HashMap<>();
		fieldMap.put("key", "money"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "ras.received.Conf.money");//显示维护列表标题
		fieldMap.put("nameCNY", "实收金额");//中文名称
		fieldMap.put("required", "true");//是否需要必填
		fieldMap.put("value", "");
		receivedFieldConf.add(fieldMap);
//		yhMoney优惠金额
		fieldMap=new HashMap<>();
		fieldMap.put("key", "yhMoney"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "ras.received.Conf.yhMoney");//显示维护列表标题
		fieldMap.put("nameCNY", "优惠金额");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		receivedFieldConf.add(fieldMap);
//		transMoney交易金额
		fieldMap=new HashMap<>();
		fieldMap.put("key", "transMoney"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "ras.received.Conf.transMoney");//显示维护列表标题
		fieldMap.put("nameCNY", "交易金额");//中文名称
		fieldMap.put("required", "true");//是否需要必填
		fieldMap.put("value", "");
		receivedFieldConf.add(fieldMap);
//		custName客户名称
		fieldMap=new HashMap<>();
		fieldMap.put("key", "custName"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "ras.received.Conf.custName");//显示维护列表标题
		fieldMap.put("nameCNY", "客户名称");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		receivedFieldConf.add(fieldMap);
//		custAcc客户账号
		fieldMap=new HashMap<>();
		fieldMap.put("key", "custAcc"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "ras.received.Conf.custAcc");//显示维护列表标题
		fieldMap.put("nameCNY", "客户账号");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		receivedFieldConf.add(fieldMap);
//		rmk备注
		fieldMap=new HashMap<>();
		fieldMap.put("key", "rmk"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "ras.received.Conf.rmk");//显示维护列表标题
		fieldMap.put("nameCNY", "备注");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		receivedFieldConf.add(fieldMap);
	}
	
	/**
	 * 获取营收配置列表信息
	 * @return
	 */
	public static List<Map<String,String>> getRevenueFieldConfList(){
		return revenueFieldConf;
	}
	
	/**
	 * 获取实收配置列表信息
	 * @return
	 */
	public static List<Map<String,String>> getReceivedFieldConfList(){
		return receivedFieldConf;
	}
	
	/**
	 * 设置稽核策略列表明细信息
	 * @param rules
	 * @param list
	 */
	public static void setRulesList(String rules,List<RasRulesVO> list){
		if(StringUtils.isNotEmpty(rules)&&list!=null&&list.size()<1){
			String[] rulesArray=StringUtils.split(rules,RasConstant.RAS_AUDIT_RULES_SEPARATORCHARS);
			for(String val:rulesArray){
				if(StringUtils.isNotEmpty(val)){
					RasRulesVO rulesVo=new RasRulesVO(val);
					list.add(rulesVo);
				}
			}
		}
	}

	/**
	 * 设置缴款方式详细明细信息
	 * @param rules
	 * @param list
	 */
	public static void setPayTypeList(String rules,List<RasPayTypeVO> list){
		if(StringUtils.isNotEmpty(rules)&&list!=null&&list.size()<1){
			String[] rulesArray=StringUtils.split(rules,RasConstant.RAS_AUDIT_TASK_SEPARATORCHARS);
			for(String val:rulesArray){
				if(StringUtils.isNotEmpty(val)){
					RasPayTypeVO rulesVo=new RasPayTypeVO(val);
					list.add(rulesVo);
				}
			}
		}
	}

	/**
	 * 设置稽核策略列表明细信息
	 * @param rules
	 * @param list
	 */
	public static String getRulesName(RasAuditStrategyVO vo){
		IMultiLangService langService=((IMultiLangService)ApplicationContextUtil.getContext().getBean("mutiLangServiceImpl"));
		if(vo==null||langService==null){
			return "";
		}
		List<RasRulesVO> list=vo.getRulesList();
		StringBuilder rulesStr=new StringBuilder();
		if(!EmptyUtils.isEmptyList(list)){
			String ys=String.valueOf(langService.getLang(vo.getLocale(), RasConstant.RAS_STRATEGY_YS));
			rulesStr.append(ys);
			for(RasRulesVO info:list){
				if(rulesStr.length()!=ys.length()){
					rulesStr.append("+");
				}
				rulesStr.append(info.getRulesName());
			}
			if(list.size()>1){
				rulesStr.append(" ").append(langService.getLang(vo.getLocale(), RasConstant.RAS_STRATEGY_AGREE));
			}else{
				rulesStr.append(" ").append(langService.getLang(vo.getLocale(), RasConstant.RAS_STRATEGY_SEP));
			}
		}
		return rulesStr.toString();
	}
	
	
	/**
	 * 根据策略信息获取拼接的sql查询语句
	 * @param rules 策略内容
	 * @param 营收数据别名
	 * @param 实收数据别名
	 * @param 拼接好的查询语句
	 */
	public static boolean getRulesCheck(String rules,RasRevenueDtlVO revenueDtl,RasReceivedDtlVO receivedDtl){
		boolean checkBoo=true;
		String[] rulesArray=StringUtils.split(rules,RasConstant.RAS_AUDIT_TASK_SEPARATORCHARS);
		for(String val:rulesArray){
			if(RasConstant.RAS_AUDIT_RULES_MONEY.equals(val)){
				if(revenueDtl.getMoney().compareTo(receivedDtl.getTransMoney())!=0){
					return false;
				}
			}else if(RasConstant.RAS_AUDIT_RULES_ORDERNO.equals(val)){
				if(!revenueDtl.getOrderNo().equals(receivedDtl.getOrderNo())){
					return false;
				}
			}else if(RasConstant.RAS_AUDIT_RULES_TRANSDATE.equals(val)){
				if(!revenueDtl.getStrTransDate().equals(receivedDtl.getStrTransDate())){
					return false;
				}
			}else if(RasConstant.RAS_AUDIT_RULES_SERIALNO.equals(val)){
				if(!revenueDtl.getSerialNo().equals(receivedDtl.getSerialNo())){
					return false;
				}
			}else{
				return false;
			}
		}
		return checkBoo;
	}
	
	/**
	 * 根据策略信息获取拼接的sql查询语句
	 * @param rules 策略内容
	 * @param 营收数据别名
	 * @param 实收数据别名
	 * @param 拼接好的查询语句
	 */
	public static String getRulesMapKey(String rules,RasRevenueDtlVO revenueDtl,RasReceivedDtlVO receivedDtl){
		if(revenueDtl==null&&receivedDtl==null){
			return null;
		}
		StringBuilder key=new StringBuilder();
		String[] rulesArray=StringUtils.split(rules,RasConstant.RAS_AUDIT_TASK_SEPARATORCHARS);
		for(String val:rulesArray){
			if(RasConstant.RAS_AUDIT_RULES_MONEY.equals(val)){
				if(revenueDtl!=null){
					key.append(RasConstant.RAS_MAP_KEYSEPARATORCHARS).append(NumberUtil.formatDouble(revenueDtl.getMoney()));
				}else{
					key.append(RasConstant.RAS_MAP_KEYSEPARATORCHARS).append(NumberUtil.formatDouble(receivedDtl.getTransMoney()));
				}
			}else if(RasConstant.RAS_AUDIT_RULES_ORDERNO.equals(val)){
				if(revenueDtl!=null){
					key.append(RasConstant.RAS_MAP_KEYSEPARATORCHARS).append(revenueDtl.getOrderNo());
				}else{
					key.append(RasConstant.RAS_MAP_KEYSEPARATORCHARS).append(receivedDtl.getOrderNo());
				}
			}else if(RasConstant.RAS_AUDIT_RULES_TRANSDATE.equals(val)){
				if(revenueDtl!=null){
					key.append(RasConstant.RAS_MAP_KEYSEPARATORCHARS).append(revenueDtl.getStrTransDate());
				}else{
					key.append(RasConstant.RAS_MAP_KEYSEPARATORCHARS).append(receivedDtl.getStrTransDate());
				}
			}else if(RasConstant.RAS_AUDIT_RULES_SERIALNO.equals(val)){
				if(revenueDtl!=null){
					key.append(RasConstant.RAS_MAP_KEYSEPARATORCHARS).append(revenueDtl.getSerialNo());
				}else{
					key.append(RasConstant.RAS_MAP_KEYSEPARATORCHARS).append(receivedDtl.getSerialNo());
				}
			}
		}
		return key.toString();
	}

	/**
	 * 根据策略信息获取拼接的sql查询语句
	 * @param rules 策略内容
	 * @param 营收数据别名
	 * @param 实收数据别名
	 * @param 拼接好的查询语句
	 */
	public static String getRulesWSql(String rules){
		String revenueAlias=RasConstant.RAS_AUTOT_REVENUE_ALIAS,receivedAlias=RasConstant.RAS_AUTOT_RECEIVED_ALIAS;
		StringBuilder rulesSql=new StringBuilder();
		String[] rulesArray=StringUtils.split(rules,RasConstant.RAS_AUDIT_TASK_SEPARATORCHARS);
		for(String val:rulesArray){
			if(rulesSql.length()>0){
				rulesSql.append(" and ");
			}
			if(RasConstant.RAS_AUDIT_RULES_MONEY.equals(val)){
				rulesSql.append(" ")
				.append(revenueAlias)
				.append(".")
				.append(RasConstant.RAS_AUTOT_REVENUE_FIELD_MONEY)
				.append("=")
				.append(receivedAlias)
				.append(".")
				.append(RasConstant.RAS_AUTOT_RECEIVED_FIELD_TRANSMONEY)
				.append(" ");
			}else if(RasConstant.RAS_AUDIT_RULES_ORDERNO.equals(val)){
				rulesSql.append(" ")
				.append(revenueAlias)
				.append(".")
				.append(RasConstant.RAS_AUTOT_REVENUE_FIELD_ORDERNO)
				.append("=")
				.append(receivedAlias)
				.append(".")
				.append(RasConstant.RAS_AUTOT_RECEIVED_FIELD_ORDERNO)
				.append(" ");
			}else if(RasConstant.RAS_AUDIT_RULES_TRANSDATE.equals(val)){
				rulesSql.append(" ")
				.append(revenueAlias)
				.append(".")
				.append(RasConstant.RAS_AUTOT_REVENUE_FIELD_STRTRANSDATE)
				.append("=")
				.append(receivedAlias)
				.append(".")
				.append(RasConstant.RAS_AUTOT_RECEIVED_FIELD_STRTRANSDATE)
				.append(" ");
			}else if(RasConstant.RAS_AUDIT_RULES_SERIALNO.equals(val)){
				rulesSql.append(" ")
				.append(revenueAlias)
				.append(".")
				.append(RasConstant.RAS_AUTOT_REVENUE_FIELD_SERIALNO)
				.append("=")
				.append(receivedAlias)
				.append(".")
				.append(RasConstant.RAS_AUTOT_RECEIVED_FIELD_SERIALNO)
				.append(" ");
			}
		}
		return rulesSql.toString();
	}
	/*
	 * 格式化划时间格式
	 */
	public static String formatToYYYYMMDDStr(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		return formatter.format(date);
	}
	
}
