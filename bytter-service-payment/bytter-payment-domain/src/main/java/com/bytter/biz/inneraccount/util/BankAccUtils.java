package com.bytter.biz.inneraccount.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账户管理帮助类
 */
public class BankAccUtils {
	/**
	 * 离线账户流水导入列表
	 */
	private static List<Map<String,String>> bisAccDtlFieldConfig=new ArrayList<>();
	
	static{
		initBtBankAccDtlFieldConfig();
	}
	/**
	 * 离线账户流水导入数据字典初始化
	 */
	private static void initBtBankAccDtlFieldConfig(){
		//离线账户流水导入数据字典初始化
		Map<String,String> fieldMap=null;
		/**
		 * 对应字段
		 * 本方账号bankAcc  对方账号oppAccNo  对方账户名oppAccName  对方开户行oppAccBank
		 * 支出金额payMoney 收入金额incomeMoney  币别curId 交易日期transDate 交易时间transTime
		 * 余额bal 摘要remark  用途uses 项目信息projectRemark
		 */
		//本方账号bankAcc
		fieldMap=new HashMap<>();
		fieldMap.put("key", "bankAcc"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "table.bisAccDtlClaim.bankAcc");//显示维护列表标题
		fieldMap.put("nameCNY", "本方账号");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		bisAccDtlFieldConfig.add(fieldMap);
		//对方账号oppAccNo
		fieldMap=new HashMap<>();
		fieldMap.put("key", "oppAccNo"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "table.bisAccDtlClaim.oppAccNo");//显示维护列表标题
		fieldMap.put("nameCNY", "对方账号");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		bisAccDtlFieldConfig.add(fieldMap);
		//对方账户名oppAccName
		fieldMap=new HashMap<>();
		fieldMap.put("key", "oppAccName"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "table.bisAccDtlClaim.oppAccName");//显示维护列表标题
		fieldMap.put("nameCNY", "对方账户名");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		bisAccDtlFieldConfig.add(fieldMap);
		//对方开户行oppAccBank
		fieldMap=new HashMap<>();
		fieldMap.put("key", "oppAccBank"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "table.btBankAccDtl.oppAccBank");//显示维护列表标题
		fieldMap.put("nameCNY", "对方开户行");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		bisAccDtlFieldConfig.add(fieldMap);
		//支出金额payMoney
		fieldMap=new HashMap<>();
		fieldMap.put("key", "payMoney"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "table.bisAccDtlImportConfig.payMoney");//显示维护列表标题
		fieldMap.put("nameCNY", "支出金额");//中文名称
		fieldMap.put("required", "true");//是否需要必填
		fieldMap.put("value", "");
		bisAccDtlFieldConfig.add(fieldMap);
		//收入金额incomeMoney
		fieldMap=new HashMap<>();
		fieldMap.put("key", "incomeMoney"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "table.bisAccDtlImportConfig.incomeMoney");//显示维护列表标题
		fieldMap.put("nameCNY", "收入金额");//中文名称
		fieldMap.put("required", "true");//是否需要必填
		fieldMap.put("value", "");
		bisAccDtlFieldConfig.add(fieldMap);
		//收支方向payMoney
		fieldMap=new HashMap<>();
		fieldMap.put("key", "cdSignStr"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "table.bisAccDtlImportConfig.cdSignStr");//显示维护列表标题
		fieldMap.put("nameCNY", "收支方向");//中文名称
		fieldMap.put("required", "true");//是否需要必填
		fieldMap.put("value", "");
		bisAccDtlFieldConfig.add(fieldMap);
		//交易金额transMoney
		fieldMap=new HashMap<>();
		fieldMap.put("key", "transMoney"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "table.bisAccDtl.amt");//显示维护列表标题
		fieldMap.put("nameCNY", "交易金额");//中文名称
		fieldMap.put("required", "true");//是否需要必填
		fieldMap.put("value", "");
		bisAccDtlFieldConfig.add(fieldMap);
		//币别curId
		fieldMap=new HashMap<>();
		fieldMap.put("key", "curId"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "table.btBankAccCur.cur");//显示维护列表标题
		fieldMap.put("nameCNY", "币别");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		bisAccDtlFieldConfig.add(fieldMap);
		//交易日期transDate
		fieldMap=new HashMap<>();
		fieldMap.put("key", "transDate"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "table.btNetChalk.transTime");//显示维护列表标题
		fieldMap.put("nameCNY", "交易日期");//中文名称
		fieldMap.put("required", "true");//是否需要必填
		fieldMap.put("value", "");
		bisAccDtlFieldConfig.add(fieldMap);
		//交易时间transTime
		fieldMap=new HashMap<>();
		fieldMap.put("key", "transTime"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "table.bisExc.transTime");//显示维护列表标题
		fieldMap.put("nameCNY", "交易时间");//中文名称
		fieldMap.put("required", "true");//是否需要必填
		fieldMap.put("value", "");
		bisAccDtlFieldConfig.add(fieldMap);
		//余额bal
		fieldMap=new HashMap<>();
		fieldMap.put("key", "bal"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "table.btBankAccDtl.bal");//显示维护列表标题
		fieldMap.put("nameCNY", "余额");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		bisAccDtlFieldConfig.add(fieldMap);
		//摘要remark
		fieldMap=new HashMap<>();
		fieldMap.put("key", "remark"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "table.btBankAccDtl.remark");//显示维护列表标题
		fieldMap.put("nameCNY", "摘要");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		bisAccDtlFieldConfig.add(fieldMap);
		//用途uses
		fieldMap=new HashMap<>();
		fieldMap.put("key", "uses"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "table.nisBillhead.purpose");//显示维护列表标题
		fieldMap.put("nameCNY", "用途");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		bisAccDtlFieldConfig.add(fieldMap);
		//项目信息projectRemark
		fieldMap=new HashMap<>();
		fieldMap.put("key", "projectRemark"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "table.btBankAccDtl.projectRemark");//显示维护列表标题
		fieldMap.put("nameCNY", "项目信息");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		bisAccDtlFieldConfig.add(fieldMap);
		//银行流水号 bankSerialNo
		fieldMap=new HashMap<>();
		fieldMap.put("key", "bankSerialNo"); //对应存储数据库的key值和对应营收数据的字段
		fieldMap.put("name", "table.bisExc.bankSerialNo");//显示维护列表标题
		fieldMap.put("nameCNY", "银行流水号");//中文名称
		fieldMap.put("required", "false");//是否需要必填
		fieldMap.put("value", "");
		bisAccDtlFieldConfig.add(fieldMap);
	}
	/**
	 * 获取离线账户流水列表信息
	 * @return
	 */
	public static List<Map<String,String>> getBisAccDtlFieldConfigList(){
		return bisAccDtlFieldConfig;
	}
}
