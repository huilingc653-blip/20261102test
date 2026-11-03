package com.bytter.biz.fundwarn.util;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.bytter.biz.fundwarn.entity.BtWarnStrategy;
import com.bytter.biz.fundwarn.vo.BtWarnStrategyVO;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.biz.fundwarn.spi.constant.FundWarnConstant;
import com.bytter.system.api.constant.NotificationConstant;
public class BtFundWarnUtils {
	/*** 预警数据主信息列表头 */
	public static Map<String,List<Map<String,String>>> WARNDATA_HEADER=new LinkedHashMap<>();
	/*** 预警数据明细信息列表头 */
	public static Map<String,List<Map<String,String>>> WARNDATA_HEADERDTL=new LinkedHashMap<>();
	/*** 预警策略类型和消息通知映射 */
	public static Map<String,String> WARN_NOTIFICATION=new LinkedHashMap<>();
	
	static{
		initHeader();
		initHeaderDtl();
		initWarnNotification();
	}
	
	/**
	 * 预警策略类型和消息通知映射 初始化
	 */
	public static void initWarnNotification(){
		WARN_NOTIFICATION.put(FundWarnConstant.TYPECODE_BALOVER+"--"+FundWarnConstant.DIMENSIONCODE_CORP, NotificationConstant.FUNDWARN.FUNDWARN_BALOVER_CORP);
		WARN_NOTIFICATION.put(FundWarnConstant.TYPECODE_BALOVER+"--"+FundWarnConstant.DIMENSIONCODE_BANKACC, NotificationConstant.FUNDWARN.FUNDWARN_BALOVER_BANKACC);
		WARN_NOTIFICATION.put(FundWarnConstant.TYPECODE_EQCORPPAY+"--"+FundWarnConstant.DIMENSIONCODE_CORP, NotificationConstant.FUNDWARN.FUNDWARN_EQCORPPAY_CORP);
		WARN_NOTIFICATION.put(FundWarnConstant.TYPECODE_BANKACCPAY+"--"+FundWarnConstant.DIMENSIONCODE_BANKACC, NotificationConstant.FUNDWARN.FUNDWARN_BANKACCPAY_BANKACC);
		WARN_NOTIFICATION.put(FundWarnConstant.TYPECODE_INFLOWOVER+"--"+FundWarnConstant.DIMENSIONCODE_CORP, NotificationConstant.FUNDWARN.FUNDWARN_INFLOWOVER_CORP);
		WARN_NOTIFICATION.put(FundWarnConstant.TYPECODE_INFLOWOVER+"--"+FundWarnConstant.DIMENSIONCODE_BANKACC, NotificationConstant.FUNDWARN.FUNDWARN_INFLOWOVER_BANKACC);
		WARN_NOTIFICATION.put(FundWarnConstant.TYPECODE_BALDOWN+"--"+FundWarnConstant.DIMENSIONCODE_CORP, NotificationConstant.FUNDWARN.FUNDWARN_BALDOWN_CORP);
		WARN_NOTIFICATION.put(FundWarnConstant.TYPECODE_BALDOWN+"--"+FundWarnConstant.DIMENSIONCODE_BANKACC, NotificationConstant.FUNDWARN.FUNDWARN_BALDOWN_BANKACC);
		WARN_NOTIFICATION.put(FundWarnConstant.TYPECODE_OUTPAY+"--"+FundWarnConstant.DIMENSIONCODE_CORP, NotificationConstant.FUNDWARN.FUNDWARN_OUTPAY_CORP);
		WARN_NOTIFICATION.put(FundWarnConstant.TYPECODE_OUTPAY+"--"+FundWarnConstant.DIMENSIONCODE_BANKACC, NotificationConstant.FUNDWARN.FUNDWARN_OUTPAY_BANKACC);
		WARN_NOTIFICATION.put(FundWarnConstant.TYPECODE_EXPENDINFLOW+"--"+FundWarnConstant.DIMENSIONCODE_CORP, NotificationConstant.FUNDWARN.FUNDWARN_EXPENDINFLOW_CORP);
		WARN_NOTIFICATION.put(FundWarnConstant.TYPECODE_EXPENDINFLOW+"--"+FundWarnConstant.DIMENSIONCODE_BANKACC, NotificationConstant.FUNDWARN.FUNDWARN_EXPENDINFLOW_BANKACC);
		WARN_NOTIFICATION.put(FundWarnConstant.TYPECODE_CASHOVER+"--"+FundWarnConstant.DIMENSIONCODE_CORP, NotificationConstant.FUNDWARN.FUNDWARN_CASHOVER_CORP);
		WARN_NOTIFICATION.put(FundWarnConstant.TYPECODE_CASHOVER+"--"+FundWarnConstant.DIMENSIONCODE_FCACC, NotificationConstant.FUNDWARN.FUNDWARN_CASHOVER_FCACC);
		WARN_NOTIFICATION.put(FundWarnConstant.TYPECODE_DEPOSITLOANOVER+"--"+FundWarnConstant.DIMENSIONCODE_CORP, NotificationConstant.FUNDWARN.FUNDWARN_DEPOSITLOANOVER_CORP);
		WARN_NOTIFICATION.put(FundWarnConstant.TYPECODE_CASHPAY+"--"+FundWarnConstant.DIMENSIONCODE_FCACC, NotificationConstant.FUNDWARN.FUNDWARN_CASHPAY_FCACC);
		WARN_NOTIFICATION.put(FundWarnConstant.TYPECODE_OLDBANKACC+"--"+FundWarnConstant.DIMENSIONCODE_BANKACC, NotificationConstant.FUNDWARN.FUNDWARN_OLDBANKACC_BANKACC);
		WARN_NOTIFICATION.put(FundWarnConstant.TYPECODE_BUDGETPROGRESS+"--"+FundWarnConstant.DIMENSIONCODE_CORP, NotificationConstant.FUNDWARN.FUNDWARN_BUDGETPROGRESS_CORP);
		WARN_NOTIFICATION.put(FundWarnConstant.TYPECODE_DAYSUMPAY+"--"+FundWarnConstant.DIMENSIONCODE_CORP, NotificationConstant.FUNDWARN.FUNDWARN_DAYSUMPAY_CORP);
	}
	
	/**
	 * 预警数据主信息列表头 初始化
	 */
	public static void initHeader(){
		Map<String,String> enity=null;
		List<Map<String,String>> entityList=null;
//		enity.put("field", "");
//		enity.put("type", "num");
//		enity.put("type", "money");
//		enity.put("text", "名称");
		//预警数据主信息列表头
//		1、预警类型-余额超标--预警维度-单位
		entityList=new ArrayList<>();
		WARNDATA_HEADER.put(FundWarnConstant.TYPECODE_BALOVER+"--"+FundWarnConstant.DIMENSIONCODE_CORP, entityList);
		enity=new HashMap<>();
		enity.put("field", "warnDate");
		enity.put("type", "date");
		enity.put("text", "table.btwarnstrategy.warnDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "createDate");
		enity.put("type", "date");
		enity.put("text", "common.message.createDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "updateDate");
		enity.put("type", "date");
		enity.put("text", "btwarnstrategy.updateDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "corpName");
		enity.put("text", "table.btwarnstrategy.corpName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "code");
		enity.put("text", "table.btwarnstrategy.code");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "name");
		enity.put("text", "table.btwarnstrategy.name");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "typeName");
		enity.put("text", "table.btwarnstrategy.typeCode");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "table.btwarnstrategy.curName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "triggerNum");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.money");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "conditions");
		enity.put("text", "btwarnstrategy.condition");entityList.add(enity);
		
//		2、预警类型-余额超标--预警维度-银行账户
		entityList=new ArrayList<>();
		WARNDATA_HEADER.put(FundWarnConstant.TYPECODE_BALOVER+"--"+FundWarnConstant.DIMENSIONCODE_BANKACC, entityList);
		enity=new HashMap<>();
		enity.put("field", "warnDate");
		enity.put("type", "date");
		enity.put("text", "table.btwarnstrategy.warnDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "createDate");
		enity.put("type", "date");
		enity.put("text", "common.message.createDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "updateDate");
		enity.put("type", "date");
		enity.put("text", "btwarnstrategy.updateDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "bankAcc");
		enity.put("text", "btwarnstrategy.bankAcc");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "code");
		enity.put("text", "table.btwarnstrategy.code");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "name");
		enity.put("text", "table.btwarnstrategy.name");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "typeName");
		enity.put("text", "table.btwarnstrategy.typeCode");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "table.btwarnstrategy.curName");entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "bankTypeName");
		enity.put("text", "btwarnstrategy.bankTypeName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "triggerNum");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.money");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "conditions");
		enity.put("text", "btwarnstrategy.condition");entityList.add(enity);
		
//		3、预警类型-相同公司付款--预警维度-单位
		entityList=new ArrayList<>();
		WARNDATA_HEADER.put(FundWarnConstant.TYPECODE_EQCORPPAY+"--"+FundWarnConstant.DIMENSIONCODE_CORP, entityList);
		enity=new HashMap<>();
		enity.put("field", "warnDate");
		enity.put("type", "date");
		enity.put("text", "table.btwarnstrategy.warnDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "createDate");
		enity.put("type", "date");
		enity.put("text", "common.message.createDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "updateDate");
		enity.put("type", "date");
		enity.put("text", "btwarnstrategy.updateDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "corpName");
		enity.put("text", "table.btwarnstrategy.corpName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "code");
		enity.put("text", "table.btwarnstrategy.code");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "name");
		enity.put("text", "table.btwarnstrategy.name");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "typeName");
		enity.put("text", "table.btwarnstrategy.typeCode");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "table.btwarnstrategy.curName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "sizeNum");
		enity.put("type", "num");
		enity.put("text", "condition.btwarnstrategy.oldBankAcc.head");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "triggerNum");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.trandMoney");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "payeeName");
		enity.put("text", "btwarnstrategy.payeeName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "conditions");
		enity.put("text", "btwarnstrategy.condition");entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "isQuery");
		enity.put("type", "query");
		enity.put("text", "common.button.view");entityList.add(enity);
		
//		4、预警类型-大额账户支出--预警维度-银行账户
		entityList=new ArrayList<>();
		WARNDATA_HEADER.put(FundWarnConstant.TYPECODE_BANKACCPAY+"--"+FundWarnConstant.DIMENSIONCODE_BANKACC, entityList);
		enity=new HashMap<>();
		enity.put("field", "warnDate");
		enity.put("type", "date");
		enity.put("text", "table.btwarnstrategy.warnDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "createDate");
		enity.put("type", "date");
		enity.put("text", "common.message.createDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "updateDate");
		enity.put("type", "date");
		enity.put("text", "btwarnstrategy.updateDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "bankAcc");
		enity.put("text", "btwarnstrategy.bankAcc");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "code");
		enity.put("text", "table.btwarnstrategy.code");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "name");
		enity.put("text", "table.btwarnstrategy.name");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "typeName");
		enity.put("text", "table.btwarnstrategy.typeCode");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "bankTypeName");
		enity.put("text", "btwarnstrategy.bankTypeName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "no");
		enity.put("text", "btwarnstrategy.payNo");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "table.btwarnstrategy.curName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "triggerNum");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.trandMoney");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "conditions");
		enity.put("text", "btwarnstrategy.condition");entityList.add(enity);
		
//		5、预警类型-大额现金支出--预警维度-内部账户
		entityList=new ArrayList<>();
		WARNDATA_HEADER.put(FundWarnConstant.TYPECODE_CASHPAY+"--"+FundWarnConstant.DIMENSIONCODE_FCACC, entityList);
		enity=new HashMap<>();
		enity.put("field", "warnDate");
		enity.put("type", "date");
		enity.put("text", "table.btwarnstrategy.warnDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "createDate");
		enity.put("type", "date");
		enity.put("text", "common.message.createDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "updateDate");
		enity.put("type", "date");
		enity.put("text", "btwarnstrategy.updateDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "fcAcc");
		enity.put("text", "btwarnstrategy.fcAcc");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "code");
		enity.put("text", "table.btwarnstrategy.code");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "name");
		enity.put("text", "table.btwarnstrategy.name");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "typeName");
		enity.put("text", "table.btwarnstrategy.typeCode");entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "no");
		enity.put("text", "btwarnstrategy.payNo");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "table.btwarnstrategy.curName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "triggerNum");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.trandMoney");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "conditions");
		enity.put("text", "btwarnstrategy.condition");entityList.add(enity);
		
//		6、预警类型-系统外付款--预警维度-单位
		entityList=new ArrayList<>();
		WARNDATA_HEADER.put(FundWarnConstant.TYPECODE_OUTPAY+"--"+FundWarnConstant.DIMENSIONCODE_CORP, entityList);
		enity=new HashMap<>();
		enity.put("field", "warnDate");
		enity.put("type", "date");
		enity.put("text", "table.btwarnstrategy.warnDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "createDate");
		enity.put("type", "date");
		enity.put("text", "common.message.createDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "updateDate");
		enity.put("type", "date");
		enity.put("text", "btwarnstrategy.updateDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "corpName");
		enity.put("text", "table.btwarnstrategy.corpName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "code");
		enity.put("text", "table.btwarnstrategy.code");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "name");
		enity.put("text", "table.btwarnstrategy.name");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "typeName");
		enity.put("text", "table.btwarnstrategy.typeCode");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "table.btwarnstrategy.curName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "sizeNum");
		enity.put("type", "num");
		enity.put("text", "condition.btwarnstrategy.oldBankAcc.head");entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "isQuery");
		enity.put("type", "query");
		enity.put("text", "common.button.view");entityList.add(enity);
		
//		7、预警类型-系统外付款--预警维度-银行账户
		entityList=new ArrayList<>();
		WARNDATA_HEADER.put(FundWarnConstant.TYPECODE_OUTPAY+"--"+FundWarnConstant.DIMENSIONCODE_BANKACC, entityList);
		enity=new HashMap<>();
		enity.put("field", "warnDate");
		enity.put("type", "date");
		enity.put("text", "table.btwarnstrategy.warnDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "createDate");
		enity.put("type", "date");
		enity.put("text", "common.message.createDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "updateDate");
		enity.put("type", "date");
		enity.put("text", "btwarnstrategy.updateDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "bankAcc");
		enity.put("text", "btwarnstrategy.bankAcc");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "code");
		enity.put("text", "table.btwarnstrategy.code");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "name");
		enity.put("text", "table.btwarnstrategy.name");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "typeName");
		enity.put("text", "table.btwarnstrategy.typeCode");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "table.btwarnstrategy.curName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "sizeNum");
		enity.put("type", "num");
		enity.put("text", "condition.btwarnstrategy.oldBankAcc.head");entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "isQuery");
		enity.put("type", "query");
		enity.put("text", "common.button.view");entityList.add(enity);
		
//		8、预警类型-库存现金超标--预警维度-单位
		entityList=new ArrayList<>();
		WARNDATA_HEADER.put(FundWarnConstant.TYPECODE_CASHOVER+"--"+FundWarnConstant.DIMENSIONCODE_CORP, entityList);
		enity=new HashMap<>();
		enity.put("field", "warnDate");
		enity.put("type", "date");
		enity.put("text", "table.btwarnstrategy.warnDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "createDate");
		enity.put("type", "date");
		enity.put("text", "common.message.createDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "updateDate");
		enity.put("type", "date");
		enity.put("text", "btwarnstrategy.updateDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "corpName");
		enity.put("text", "table.btwarnstrategy.corpName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "code");
		enity.put("text", "table.btwarnstrategy.code");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "name");
		enity.put("text", "table.btwarnstrategy.name");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "typeName");
		enity.put("text", "table.btwarnstrategy.typeCode");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "table.btwarnstrategy.curName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "triggerNum");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.sumMoney");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "conditions");
		enity.put("text", "btwarnstrategy.condition");entityList.add(enity);
//		9、预警类型-库存现金超标--预警维度-内部账户
		entityList=new ArrayList<>();
		WARNDATA_HEADER.put(FundWarnConstant.TYPECODE_CASHOVER+"--"+FundWarnConstant.DIMENSIONCODE_FCACC, entityList);
		enity=new HashMap<>();
		enity.put("field", "warnDate");
		enity.put("type", "date");
		enity.put("text", "table.btwarnstrategy.warnDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "createDate");
		enity.put("type", "date");
		enity.put("text", "common.message.createDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "updateDate");
		enity.put("type", "date");
		enity.put("text", "btwarnstrategy.updateDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "fcAcc");
		enity.put("text", "btwarnstrategy.fcAcc");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "code");
		enity.put("text", "table.btwarnstrategy.code");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "name");
		enity.put("text", "table.btwarnstrategy.name");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "typeName");
		enity.put("text", "table.btwarnstrategy.typeCode");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "table.btwarnstrategy.curName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "triggerNum");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.money");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "conditions");
		enity.put("text", "btwarnstrategy.condition");entityList.add(enity);
		
//		10、预警类型-支出户流入--预警维度-单位
		entityList=new ArrayList<>();
		WARNDATA_HEADER.put(FundWarnConstant.TYPECODE_EXPENDINFLOW+"--"+FundWarnConstant.DIMENSIONCODE_CORP, entityList);
		enity=new HashMap<>();
		enity.put("field", "warnDate");
		enity.put("type", "date");
		enity.put("text", "table.btwarnstrategy.warnDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "createDate");
		enity.put("type", "date");
		enity.put("text", "common.message.createDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "updateDate");
		enity.put("type", "date");
		enity.put("text", "btwarnstrategy.updateDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "corpName");
		enity.put("text", "table.btwarnstrategy.corpName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "code");
		enity.put("text", "table.btwarnstrategy.code");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "name");
		enity.put("text", "table.btwarnstrategy.name");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "typeName");
		enity.put("text", "table.btwarnstrategy.typeCode");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "table.btwarnstrategy.curName");entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "sizeNum");
		enity.put("type", "num");
		enity.put("text", "condition.btwarnstrategy.oldBankAcc.head");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "triggerNum");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.triggerNum");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "conditions");
		enity.put("text", "btwarnstrategy.condition");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "isQuery");
		enity.put("type", "query");
		enity.put("text", "common.button.view");entityList.add(enity);
		
//		11、预警类型-支出户流入--预警维度-银行账户
		entityList=new ArrayList<>();
		WARNDATA_HEADER.put(FundWarnConstant.TYPECODE_EXPENDINFLOW+"--"+FundWarnConstant.DIMENSIONCODE_BANKACC, entityList);
		enity=new HashMap<>();
		enity.put("field", "warnDate");
		enity.put("type", "date");
		enity.put("text", "table.btwarnstrategy.warnDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "createDate");
		enity.put("type", "date");
		enity.put("text", "common.message.createDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "updateDate");
		enity.put("type", "date");
		enity.put("text", "btwarnstrategy.updateDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "bankAcc");
		enity.put("text", "btwarnstrategy.bankAcc");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "code");
		enity.put("text", "table.btwarnstrategy.code");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "name");
		enity.put("text", "table.btwarnstrategy.name");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "typeName");
		enity.put("text", "table.btwarnstrategy.typeCode");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "table.btwarnstrategy.curName");entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "sizeNum");
		enity.put("type", "num");
		enity.put("text", "condition.btwarnstrategy.oldBankAcc.head");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "triggerNum");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.triggerNum");entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "conditions");
		enity.put("text", "btwarnstrategy.condition");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "isQuery");
		enity.put("type", "query");
		enity.put("text", "common.button.view");entityList.add(enity);
		
//		12、预警类型-净流入透支--预警维度-单位
		entityList=new ArrayList<>();
		WARNDATA_HEADER.put(FundWarnConstant.TYPECODE_INFLOWOVER+"--"+FundWarnConstant.DIMENSIONCODE_CORP, entityList);
		enity=new HashMap<>();
		enity.put("field", "warnDate");
		enity.put("type", "date");
		enity.put("text", "table.btwarnstrategy.warnDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "createDate");
		enity.put("type", "date");
		enity.put("text", "common.message.createDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "updateDate");
		enity.put("type", "date");
		enity.put("text", "btwarnstrategy.updateDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "corpName");
		enity.put("text", "table.btwarnstrategy.corpName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "code");
		enity.put("text", "table.btwarnstrategy.code");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "name");
		enity.put("text", "table.btwarnstrategy.name");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "typeName");
		enity.put("text", "table.btwarnstrategy.typeCode");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "table.btwarnstrategy.curName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "oneNum");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.allNum");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "twoNum");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.outNum");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "triggerNum");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.totalNum");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "conditions");
		enity.put("text", "btwarnstrategy.condition");entityList.add(enity);
		
//		13、预警类型-净流入透支--预警维度-银行账户
		entityList=new ArrayList<>();
		WARNDATA_HEADER.put(FundWarnConstant.TYPECODE_INFLOWOVER+"--"+FundWarnConstant.DIMENSIONCODE_BANKACC, entityList);
		enity=new HashMap<>();
		enity.put("field", "warnDate");
		enity.put("type", "date");
		enity.put("text", "table.btwarnstrategy.warnDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "createDate");
		enity.put("type", "date");
		enity.put("text", "common.message.createDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "updateDate");
		enity.put("type", "date");
		enity.put("text", "btwarnstrategy.updateDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "bankAcc");
		enity.put("text", "btwarnstrategy.bankAcc");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "code");
		enity.put("text", "table.btwarnstrategy.code");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "name");
		enity.put("text", "table.btwarnstrategy.name");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "typeName");
		enity.put("text", "table.btwarnstrategy.typeCode");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "table.btwarnstrategy.curName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "oneNum");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.allNum");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "twoNum");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.outNum");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "triggerNum");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.totalNum");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "conditions");
		enity.put("text", "btwarnstrategy.condition");entityList.add(enity);
		
//		14、预警类型-账户余额持续下降--预警维度-单位
		entityList=new ArrayList<>();
		WARNDATA_HEADER.put(FundWarnConstant.TYPECODE_BALDOWN+"--"+FundWarnConstant.DIMENSIONCODE_CORP, entityList);
		enity=new HashMap<>();
		enity.put("field", "warnDate");
		enity.put("type", "date");
		enity.put("text", "table.btwarnstrategy.warnDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "createDate");
		enity.put("type", "date");
		enity.put("text", "common.message.createDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "updateDate");
		enity.put("type", "date");
		enity.put("text", "btwarnstrategy.updateDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "corpName");
		enity.put("text", "table.btwarnstrategy.corpName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "code");
		enity.put("text", "table.btwarnstrategy.code");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "name");
		enity.put("text", "table.btwarnstrategy.name");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "typeName");
		enity.put("text", "table.btwarnstrategy.typeCode");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "table.btwarnstrategy.curName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "triggerNum");
		enity.put("type", "num");
		enity.put("text", "btwarnstrategy.months");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "conditions");
		enity.put("text", "btwarnstrategy.condition");entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "isQuery");
		enity.put("type", "query");
		enity.put("text", "common.button.view");entityList.add(enity);
		
//		15、预警类型-账户余额持续下降--预警维度-银行账户
		entityList=new ArrayList<>();
		WARNDATA_HEADER.put(FundWarnConstant.TYPECODE_BALDOWN+"--"+FundWarnConstant.DIMENSIONCODE_BANKACC, entityList);
		enity=new HashMap<>();
		enity.put("field", "warnDate");
		enity.put("type", "date");
		enity.put("text", "table.btwarnstrategy.warnDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "createDate");
		enity.put("type", "date");
		enity.put("text", "common.message.createDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "updateDate");
		enity.put("type", "date");
		enity.put("text", "btwarnstrategy.updateDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "bankAcc");
		enity.put("text", "btwarnstrategy.bankAcc");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "code");
		enity.put("text", "table.btwarnstrategy.code");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "name");
		enity.put("text", "table.btwarnstrategy.name");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "typeName");
		enity.put("text", "table.btwarnstrategy.typeCode");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "table.btwarnstrategy.curName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "triggerNum");
		enity.put("type", "num");
		enity.put("text", "btwarnstrategy.months");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "conditions");
		enity.put("text", "btwarnstrategy.condition");entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "isQuery");
		enity.put("type", "query");
		enity.put("text", "common.button.view");entityList.add(enity);
		
//		16、预警类型-存贷比超标预警--预警维度-单位
		entityList=new ArrayList<>();
		WARNDATA_HEADER.put(FundWarnConstant.TYPECODE_DEPOSITLOANOVER+"--"+FundWarnConstant.DIMENSIONCODE_CORP, entityList);
		enity=new HashMap<>();
		enity.put("field", "warnDate");
		enity.put("type", "date");
		enity.put("text", "table.btwarnstrategy.warnDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "createDate");
		enity.put("type", "date");
		enity.put("text", "common.message.createDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "updateDate");
		enity.put("type", "date");
		enity.put("text", "btwarnstrategy.updateDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "corpName");
		enity.put("text", "table.btwarnstrategy.corpName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "code");
		enity.put("text", "table.btwarnstrategy.code");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "name");
		enity.put("text", "table.btwarnstrategy.name");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "typeName");
		enity.put("text", "table.btwarnstrategy.typeCode");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "table.btwarnstrategy.curName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "triggerNum");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.percent");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "oneNum");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.balance");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "twoNum");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.realBalance");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "conditions");
		enity.put("text", "btwarnstrategy.condition");entityList.add(enity);
		
//		17、预警类型-久悬户预警--预警维度-银行账户
		entityList=new ArrayList<>();
		WARNDATA_HEADER.put(FundWarnConstant.TYPECODE_OLDBANKACC+"--"+FundWarnConstant.DIMENSIONCODE_BANKACC, entityList);
		enity=new HashMap<>();
		enity.put("field", "warnDate");
		enity.put("type", "date");
		enity.put("text", "table.btwarnstrategy.warnDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "createDate");
		enity.put("type", "date");
		enity.put("text", "common.message.createDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "updateDate");
		enity.put("type", "date");
		enity.put("text", "btwarnstrategy.updateDate");entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "bankAcc");
		enity.put("text", "btwarnstrategy.bankAcc");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "code");
		enity.put("text", "table.btwarnstrategy.code");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "name");
		enity.put("text", "table.btwarnstrategy.name");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "typeName");
		enity.put("text", "table.btwarnstrategy.typeCode");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "table.btwarnstrategy.curName");entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "bankTypeName");
		enity.put("text", "btwarnstrategy.bankTypeName");entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "triggerNum");
		enity.put("type", "num");
		enity.put("text", "condition.btwarnstrategy.oldBankAcc.head");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "reserve1");
		enity.put("text", "btwarnstrategy.remindCycle");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "oneNum");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.accountBalance");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "conditions");
		enity.put("text", "btwarnstrategy.condition");entityList.add(enity);
		
//		18、预警类型-单日累计支付--预警维度-单位
		entityList=new ArrayList<>();
		WARNDATA_HEADER.put(FundWarnConstant.TYPECODE_DAYSUMPAY+"--"+FundWarnConstant.DIMENSIONCODE_CORP, entityList);
		enity=new HashMap<>();
		enity.put("field", "warnDate");
		enity.put("type", "date");
		enity.put("text", "table.btwarnstrategy.warnDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "createDate");
		enity.put("type", "date");
		enity.put("text", "common.message.createDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "updateDate");
		enity.put("type", "date");
		enity.put("text", "btwarnstrategy.updateDate");entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "corpName");
		enity.put("text", "table.btwarnstrategy.corpName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "code");
		enity.put("text", "table.btwarnstrategy.code");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "name");
		enity.put("text", "table.btwarnstrategy.name");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "typeName");
		enity.put("text", "table.btwarnstrategy.typeCode");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "table.btwarnstrategy.curName");entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "triggerNum");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.dayPayment");entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "conditions");
		enity.put("text", "btwarnstrategy.condition");entityList.add(enity);
//		19、预警类型-预算执行进度--预警维度-单位
		entityList=new ArrayList<>();
		WARNDATA_HEADER.put(FundWarnConstant.TYPECODE_BUDGETPROGRESS+"--"+FundWarnConstant.DIMENSIONCODE_CORP, entityList);
		enity=new HashMap<>();
		enity.put("field", "warnDate");
		enity.put("type", "date");
		enity.put("text", "table.btwarnstrategy.warnDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "createDate");
		enity.put("type", "date");
		enity.put("text", "common.message.createDate");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "updateDate");
		enity.put("type", "date");
		enity.put("text", "btwarnstrategy.updateDate");entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "corpName");
		enity.put("text", "table.btwarnstrategy.corpName");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "code");
		enity.put("text", "table.btwarnstrategy.code");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "name");
		enity.put("text", "table.btwarnstrategy.name");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "typeName");
		enity.put("text", "table.btwarnstrategy.typeCode");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "table.btwarnstrategy.curName");entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "reserve1");
		enity.put("text", "btwarnstrategy.budgetTime");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "triggerNum");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.percent");entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "oneNum");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.budgetNum");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "twoNum");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.upBudgetNum");entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "conditions");
		enity.put("text", "btwarnstrategy.condition");entityList.add(enity);
	}
	
	/**
	 * 预警数据明细信息列表头初始化
	 */
	public static void initHeaderDtl(){
		Map<String,String> enity=null;
		List<Map<String,String>> entityList=null;
//		enity.put("field", "");
//		enity.put("type", "num");
//		enity.put("type", "money");
//		enity.put("text", "名称");
		//预警数据主信息列表头
//		1、预警类型-相同公司付款--预警维度-单位
		entityList=new ArrayList<>();
		WARNDATA_HEADERDTL.put(FundWarnConstant.TYPECODE_EQCORPPAY+"--"+FundWarnConstant.DIMENSIONCODE_CORP, entityList);
		
		enity=new HashMap<>();
		enity.put("field", "no");
		enity.put("text", "btwarnstrategy.payNo");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "payerAcc");
		enity.put("text", "btwarnstrategy.bankAcc");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "btwarnstrategy.currency");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "payerName");
		enity.put("text", "btwarnstrategy.accountName");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "payerBankName");
		enity.put("text", "btwarnstrategy.openBank");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "transDate");
		enity.put("type", "date");
		enity.put("text", "btwarnstrategy.tradeTime");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "payMoney");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.trandMoney");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "payeeAcc");
		enity.put("text", "btwarnstrategy.otherAccount");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "payeeName");
		enity.put("text", "btwarnstrategy.otherAccountName");
		entityList.add(enity);
//		2、预警类型-系统外付款--预警维度-单位
		entityList=new ArrayList<>();
		WARNDATA_HEADERDTL.put(FundWarnConstant.TYPECODE_OUTPAY+"--"+FundWarnConstant.DIMENSIONCODE_CORP, entityList);
		enity=new HashMap<>();
		enity.put("field", "payerAcc");
		enity.put("text", "btwarnstrategy.bankAcc");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "btwarnstrategy.currency");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "corpName");
		enity.put("text", "table.btwarnstrategy.corpName");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "transDate");
		enity.put("type", "date");
		enity.put("text", "btwarnstrategy.tradeTime");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "payMoney");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.trandMoney");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "payeeAcc");
		enity.put("text", "btwarnstrategy.otherAccount");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "payeeName");
		enity.put("text", "btwarnstrategy.otherAccountName");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "purpose");
		enity.put("text", "btwarnstrategy.purpose");
		entityList.add(enity);
//		3、预警类型-系统外付款--预警维度-银行账户
		entityList=new ArrayList<>();
		WARNDATA_HEADERDTL.put(FundWarnConstant.TYPECODE_OUTPAY+"--"+FundWarnConstant.DIMENSIONCODE_BANKACC, entityList);
		
		enity=new HashMap<>();
		enity.put("field", "payerAcc");
		enity.put("text", "btwarnstrategy.bankAcc");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "btwarnstrategy.currency");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "corpName");
		enity.put("text", "table.btwarnstrategy.corpName");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "transDate");
		enity.put("type", "date");
		enity.put("text", "btwarnstrategy.tradeTime");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "payMoney");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.trandMoney");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "payeeAcc");
		enity.put("text", "btwarnstrategy.otherAccount");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "payeeName");
		enity.put("text", "btwarnstrategy.otherAccountName");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "purpose");
		enity.put("text", "btwarnstrategy.purpose");
		entityList.add(enity);
//		4、预警类型-支出户流入--预警维度-单位
		entityList=new ArrayList<>();
		WARNDATA_HEADERDTL.put(FundWarnConstant.TYPECODE_EXPENDINFLOW+"--"+FundWarnConstant.DIMENSIONCODE_CORP, entityList);
		enity=new HashMap<>();
		enity.put("field", "payerAcc");
		enity.put("text", "btwarnstrategy.bankAcc");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "btwarnstrategy.currency");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "payerName");
		enity.put("text", "btwarnstrategy.accountName");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "payerBankName");
		enity.put("text", "btwarnstrategy.openBank");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "transDate");
		enity.put("type", "date");
		enity.put("text", "btwarnstrategy.tradeTime");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "payMoney");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.trandMoney");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "payeeAcc");
		enity.put("text", "btwarnstrategy.otherAccount");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "payeeName");
		enity.put("text", "btwarnstrategy.otherAccountName");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "payeeBankName");
		enity.put("text", "btwarnstrategy.otherBank");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "purpose");
		enity.put("text", "btwarnstrategy.purpose");
		entityList.add(enity);
		
//		5、预警类型-支出户流入--预警维度-银行账户
		entityList=new ArrayList<>();
		WARNDATA_HEADERDTL.put(FundWarnConstant.TYPECODE_EXPENDINFLOW+"--"+FundWarnConstant.DIMENSIONCODE_BANKACC, entityList);
		enity=new HashMap<>();
		enity.put("field", "payerAcc");
		enity.put("text", "btwarnstrategy.bankAcc");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "btwarnstrategy.currency");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "payerName");
		enity.put("text", "btwarnstrategy.accountName");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "payerBankName");
		enity.put("text", "btwarnstrategy.openBank");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "transDate");
		enity.put("type", "date");
		enity.put("text", "btwarnstrategy.tradeTime");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "payMoney");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.trandMoney");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "payeeAcc");
		enity.put("text", "btwarnstrategy.otherAccount");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "payeeName");
		enity.put("text", "btwarnstrategy.otherAccountName");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "payeeBankName");
		enity.put("text", "btwarnstrategy.otherBank");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "purpose");
		enity.put("text", "btwarnstrategy.purpose");
		entityList.add(enity);
		
//		6、预警类型-账户余额持续下降--预警维度-单位
		entityList=new ArrayList<>();
		WARNDATA_HEADERDTL.put(FundWarnConstant.TYPECODE_BALDOWN+"--"+FundWarnConstant.DIMENSIONCODE_CORP, entityList);
		
		enity=new HashMap<>();
		enity.put("field", "corpName");
		enity.put("text", "table.btwarnstrategy.corpName");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "btwarnstrategy.currency");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "monthNum");
		enity.put("type", "num");
		enity.put("text", "btwarnstrategy.month");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "payMoney");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.averageAmount");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "excessMoney");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.monthEndMoney");
		entityList.add(enity);
//		7、预警类型-账户余额持续下降--预警维度-银行账户
		entityList=new ArrayList<>();
		WARNDATA_HEADERDTL.put(FundWarnConstant.TYPECODE_BALDOWN+"--"+FundWarnConstant.DIMENSIONCODE_BANKACC, entityList);
		
		enity=new HashMap<>();
		enity.put("field", "payerAcc");
		enity.put("text", "btwarnstrategy.bankAcc");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "corpName");
		enity.put("text", "table.btwarnstrategy.corpName");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "curName");
		enity.put("text", "btwarnstrategy.currency");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "payerName");
		enity.put("text", "btwarnstrategy.accountName");
		entityList.add(enity);
		enity=new HashMap<>();
		enity.put("field", "payerBankName");
		enity.put("text", "btwarnstrategy.openBank");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "monthNum");
		enity.put("type", "num");
		enity.put("text", "btwarnstrategy.month");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "payMoney");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.averageAmount");
		entityList.add(enity);
		
		enity=new HashMap<>();
		enity.put("field", "excessMoney");
		enity.put("type", "money");
		enity.put("text", "btwarnstrategy.monthEndMoney");
		entityList.add(enity);
		
	}
	
	/**
	 * 获取所有预警类型和预警维度对应关系信息
	 * @return 对应关系map
	 */
	public static Map<String,String[]> getAllTypecodeDimension(){
		return FundWarnConstant.TYPECODE_DIMENSION;
	}
	
	/**
	 * 根据预警类型获取对应的预警维度信息
	 * @param typeCode 预警类型
	 * @return 对应预警维护数组
	 */
	public static String[] getDimensionByTypecode(String typeCode){
		return FundWarnConstant.TYPECODE_DIMENSION.get(typeCode);
	}
	
	/**
	 * 根据预警类型获和预警维度取 预警数据主信息列表头信息
	 * @param typeCode 预警类型
	 * @param dimension 预警维度
	 * @return 预警数据主信息列表头信息list
	 */
	public static List<Map<String,String>> getWarndataHeader(String typeCode, String dimension){
		initHeader();
		return WARNDATA_HEADER.get(typeCode+"--"+dimension);
	}
	
	/**
	 * 根据预警类型获和预警维度取 预警数据明细信息列表头信息
	 * @param typeCode 预警类型
	 * @param dimension 预警维度
	 * @return 预警数据明细信息列表头信息list
	 */
	public static List<Map<String,String>> getWarndataheaderDtl(String typeCode, String dimension){
//		initHeaderDtl();
		return WARNDATA_HEADERDTL.get(typeCode+"--"+dimension);
	}
	/**
	 * 根据预警类型代码获取名称
	 * @param dimension 预警维度代码
	 * @return 预警类型名称
	 */
	public static String getTypeNameByCode(String typeCode){
		return FundWarnConstant.TYPECODE_NAME.get(typeCode);
	}
	
	/**
	 * 根据预警维度代码获取名称
	 * @param dimension 预警维度代码
	 * @return 预警维度名称
	 */
	public static String getDimensionNameByCode(String dimensionCode){
		return FundWarnConstant.DIMENSION_NAME.get(dimensionCode);
	}
	
	/**
	 * 根据预警类型获和预警维度取 预警消息通知常量
	 * @param typeCode 预警类型
	 * @param dimension 预警维度
	 * @return 预警消息通知常量
	 */
	public static String getWarnNotification(String typeCode, String dimension){
		return WARN_NOTIFICATION.get(typeCode+"--"+dimension);
	}
	
	/**
	 * 获取写入数据信息的触发条件
	 * @param entity
	 * @return
	 */
	public static String getConditionValue(BtWarnStrategyVO entity,String dataVal){
		StringBuilder condition=new StringBuilder();
		if(entity.getBeginNum()!=null){
			condition.append(dataVal).append(getCompareSql(entity.getBeginOperator()));
			condition.append(NumberUtil.formatDouble(entity.getBeginNum()));
			if(entity.getEndNum()!=null){
				condition.append(getRelationCNY(entity.getNumOperator()));
			}
		}
		if(entity.getEndNum()!=null){
			condition.append(dataVal).append(getCompareSql(entity.getEndOperator()));
			condition.append(NumberUtil.formatDouble(entity.getEndNum()));
		}
		return condition.toString();
	}
	
	/**
	 * 根据预警策略过滤条件拼接sql预警
	 * @param entity 策略实体
	 * @return  返回拼接sql预警 ( field >= BeginNum or field <= EndNum )
	 */
	public static String getConditionSql(BtWarnStrategyVO entityVo,String field,Map<String,Object> params){
		BtWarnStrategy entity=new BtWarnStrategy();
		BeanUtils.copyProperties(entityVo, entity);
		return getConditionSql(entity, field, params);
	}
	/**
	 * 根据预警策略过滤条件拼接sql预警
	 * @param entity 策略实体
	 * @return  返回拼接sql预警 ( field >= BeginNum or field <= EndNum )
	 */
	public static String getConditionSql(BtWarnStrategy entity,String field){
		return getConditionSql(entity, field, null);
	}
	
	/**
	 * 根据预警策略过滤条件拼接sql预警
	 * @param entity 策略实体
	 * @return  返回拼接sql预警 ( field >= BeginNum or field <= EndNum )
	 */
	public static String getConditionSql(BtWarnStrategy entity,String field,Map<String,Object> params){
		StringBuilder sql=new StringBuilder();
		sql.append(" ( ");
		if(entity==null){
			sql.append(" 1=1 ");
		}else{
			if(entity.getBeginNum()!=null){
				sql.append(field).append(getCompareSql(entity.getBeginOperator()));
				if(params!=null){
					sql.append(":btBeginNumParams");
					params.put("btBeginNumParams", entity.getBeginNum());
				}else{
					sql.append(NumberUtil.formatDouble(entity.getBeginNum()));
				}
				if(entity.getEndNum()!=null){
					sql.append(getRelationSql(entity.getNumOperator()));
				}
			}
			if(entity.getEndNum()!=null){
				sql.append(field).append(getCompareSql(entity.getEndOperator()));
				if(params!=null){
					sql.append(":btEndNumParams");
					params.put("btEndNumParams", entity.getEndNum());
				}else{
					sql.append(NumberUtil.formatDouble(entity.getEndNum()));
				}
			}
		}
		sql.append(" ) ");
		return sql.toString();
	}
	
	/**
	 * 获取字段比较 运算符
	 * @param operator
	 * @return
	 */
	public static String getCompareSql(String operator){
		if(FundWarnConstant.BJ_OPERATOR_GT.equals(operator)){
			return " > ";
		}else if(FundWarnConstant.BJ_OPERATOR_GTEQ.equals(operator)){
			return " >= ";
		}else if(FundWarnConstant.BJ_OPERATOR_LT.equals(operator)){
			return " < ";
		}else if(FundWarnConstant.BJ_OPERATOR_LTEQ.equals(operator)){
			return " <= ";
		}else{
			return " = ";
		}
	}
	
	/**
	 * 获取条件关系 运算符
	 * @param operator
	 * @return
	 */
	public static String getRelationSql(String numOperator){
		if(FundWarnConstant.GX_OPERATOR_OR.equals(numOperator)){
			return " OR ";
		}else if(FundWarnConstant.GX_OPERATOR_AND.equals(numOperator)){
			return " AND ";
		}else{
			return " ";
		}
	}
	
	/**
	 * 获取条件关系 运算符
	 * @param operator
	 * @return
	 */
	public static String getRelationCNY(String numOperator){
		if(FundWarnConstant.GX_OPERATOR_OR.equals(numOperator)){
			return " 或者 ";
		}else if(FundWarnConstant.GX_OPERATOR_AND.equals(numOperator)){
			return " 并且 ";
		}else{
			return " ";
		}
	}
	
	/**
	 * 金额条件区间判断 是否满足策略
	 * @param operator
	 * @return
	 */
	public static boolean checkConditionMoney(BtWarnStrategyVO entity,BigDecimal conditionMoney){
		boolean reBoo=false;
		if(entity.getBeginNum()!=null){
			boolean beginBoo=checkConditionMoneyByCompare(entity.getBeginOperator(), entity.getBeginNum(), conditionMoney);
			if(entity.getEndNum()!=null){
				boolean endBoo=checkConditionMoneyByCompare(entity.getEndOperator(), entity.getEndNum(), conditionMoney);
				if(FundWarnConstant.GX_OPERATOR_OR.equals(entity.getNumOperator())){
					reBoo=beginBoo||endBoo;
				}else if(FundWarnConstant.GX_OPERATOR_AND.equals(entity.getNumOperator())){
					reBoo=beginBoo&&endBoo;
				}
			}else{
				reBoo=beginBoo;
			}
		}else if(entity.getEndNum()!=null){
			reBoo=checkConditionMoneyByCompare(entity.getEndOperator(), entity.getEndNum(), conditionMoney);
		}
		return reBoo;
	}
	
	/**
	 * 金额比较判断
	 * @param operator 比较运算符
	 * @param numMeny 被比较金额
	 * @param conditionMoney 比较金额
	 * @return
	 */
	public static boolean checkConditionMoneyByCompare(String operator,BigDecimal numMeny,BigDecimal conditionMoney){
		if(FundWarnConstant.BJ_OPERATOR_GT.equals(operator)){
			if(conditionMoney.compareTo(numMeny)>0){
				return true;
			}
		}else if(FundWarnConstant.BJ_OPERATOR_GTEQ.equals(operator)){
			if(conditionMoney.compareTo(numMeny)>=0){
				return true;
			}
		}else if(FundWarnConstant.BJ_OPERATOR_LT.equals(operator)){
			if(conditionMoney.compareTo(numMeny)<0){
				return true;
			}
		}else if(FundWarnConstant.BJ_OPERATOR_LTEQ.equals(operator)){
			if(conditionMoney.compareTo(numMeny)<=0){
				return true;
			}
		}
		return false;
	}
}
