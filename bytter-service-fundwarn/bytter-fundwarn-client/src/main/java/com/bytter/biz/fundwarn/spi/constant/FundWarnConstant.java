package com.bytter.biz.fundwarn.spi.constant;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 * 预警模块常量类
 * @author zhengg
 *
 */
public class FundWarnConstant {
	
	/**定时任务执行serviceBean**/
	public final static String FUNDWARN_TASKCOMMAND = "com.bytter.biz.fundwarn.service.FundWarnTaskService";
//	预警类型：WARN_TYPE_CODE		
//	序号	代码	名称
//	1	balOver	余额超标
//	2	eqCorpPay	相同公司付款
//	3	bankAccPay	大额账户支出
//	4	inflowOver	净流入透支
//	5	balDown	账户余额持续下降
//	6	outPay	系统外付款
//	7	expendInflow	支出户流入
//	8	cashOver	库存现金超标
//	9	depositLoanOver	存贷比超标
//	10	cashPay	大额现金支出
//	11	oldBankAcc	久悬户预警
//	12	budgetProgress	预算执行进度
//	13	daySumPay	单日累计支付
	/*** 预警类型-余额超标*/
	public final static String TYPECODE_BALOVER = "balOver";
	/*** 预警类型-相同公司付款*/
	public final static String TYPECODE_EQCORPPAY = "eqCorpPay";
	/*** 预警类型-大额账户支出*/
	public final static String TYPECODE_BANKACCPAY = "bankAccPay";
	/*** 预警类型-净流入透支*/
	public final static String TYPECODE_INFLOWOVER = "inflowOver";
	/*** 预警类型-账户余额持续下降*/
	public final static String TYPECODE_BALDOWN = "balDown";
	/*** 预警类型-系统外付款*/
	public final static String TYPECODE_OUTPAY = "outPay";
	/*** 预警类型-支出户流入*/
	public final static String TYPECODE_EXPENDINFLOW = "expendInflow";
	/*** 预警类型-库存现金超标*/
	public final static String TYPECODE_CASHOVER = "cashOver";
	/*** 预警类型-存贷比超标*/
	public final static String TYPECODE_DEPOSITLOANOVER = "depositLoanOver";
	/*** 预警类型-大额现金支出*/
	public final static String TYPECODE_CASHPAY = "cashPay";
	/*** 预警类型-久悬户预警*/
	public final static String TYPECODE_OLDBANKACC = "oldBankAcc";
	/*** 预警类型-预算执行进度*/
	public final static String TYPECODE_BUDGETPROGRESS = "budgetProgress";
	/*** 预警类型-单日累计支付*/
	public final static String TYPECODE_DAYSUMPAY = "daySumPay";
	
	/*** 预警类型-相同公司付款 对应传递实体字段*/
	public static class EQCORPPAY{
		/*** 单位id*/
		public final static String FIELD_CORPID = "corpId";
		/*** 银行账号*/
		public final static String FIELD_BANKACC = "payerAcc";
		/*** 币别id*/
		public final static String FIELD_CURID = "curId";
		/*** 付款金额*/
		public final static String FIELD_PAYMONEY = "payMoney";
		/*** 付款日期*/
		public final static String FIELD_PAYDATE = "wishPayday";
		/*** 付款人名称*/
		public final static String FIELD_PAYERNAME = "payerAccName";
		/*** 收款人名称*/
		public final static String FIELD_PAYEENAME = "payeeAccName";
	}
	/*** 预警类型-单日累计支付 对应传递实体字段*/
	public static class DAYSUMPAY{
		/*** 单位id*/
		public final static String FIELD_CORPID = "corpId";
		/*** 银行账号*/
		public final static String FIELD_BANKACC = "payerAcc";
		/*** 币别id*/
		public final static String FIELD_CURID = "curId";
		/*** 付款金额*/
		public final static String FIELD_PAYMONEY = "payMoney";
		/*** 付款日期*/
		public final static String FIELD_PAYDATE = "wishPayday";
		/*** 付款人名称*/
		public final static String FIELD_PAYERNAME = "payerAccName";
		/*** 收款人名称*/
		public final static String FIELD_PAYEENAME = "payeeAccName";
		/*** 对应单号*/
		public final static String FIELD_NO = "billNo";
	}
	
	/*** 预警类型-预算执行进度 对应传递实体字段*/
	public static class BUDGETPROGRESS{
		/*** 单位id*/
		public final static String FIELD_CORPID = "corpId";
		/*** 币别id*/
		public final static String FIELD_CURID = "curId";
	}
	
	/*** 预警类型-大额账户支出 对应传递实体字段*/
	public static class BANKACCPAY{
		/*** 单位id*/
		public final static String FIELD_CORPID = "corpId";
		/*** 银行账号*/
		public final static String FIELD_BANKACC = "payerAcc";
		/*** 对应单号*/
		public final static String FIELD_NO = "billNo";
		/*** 币别id*/
		public final static String FIELD_CURID = "curId";
		/*** 付款金额*/
		public final static String FIELD_PAYMONEY = "payMoney";
		/*** 付款日期*/
		public final static String FIELD_PAYDATE = "wishPayday";
	}
	/*** 预警类型-大额现金支出 对应传递实体字段*/
	public static class CASHPAY{
		/*** 单位id*/
		public final static String FIELD_CORPID = "corpId";
		/*** 对应单号*/
		public final static String FIELD_NO = "billNo";
		/*** 内部账号*/
		public final static String FIELD_FCACC = "payerAcc";
		/*** 币别id*/
		public final static String FIELD_CURID = "curId";
		/*** 付款金额*/
		public final static String FIELD_PAYMONEY = "payMoney";
	}
	
//	
//预警维度：WARN_DIMENSION_CODE		
//序号	代码	名称
//1	corp	单位
//2	bankAcc	银行账号
//3	fcAcc	内部账号
	/*** 预警维度-单位 */
	public final static String DIMENSIONCODE_CORP = "corp";
	/*** 预警维度-银行账号 */
	public final static String DIMENSIONCODE_BANKACC = "bankAcc";
	/*** 预警维度-内部账号 */
	public final static String DIMENSIONCODE_FCACC = "fcAcc";
	//预警级别：WARN_LEVEL
	//序号	代码	名称
	//1	0	低
	//2	1	中
	//3	2	高
	/*** 预警级别-低 */
	public final static String WARNLEVEL_0 = "0";
	/*** 预警级别-中 */
	public final static String WARNLEVEL_1 = "1";
	/*** 预警级别-高 */
	public final static String WARNLEVEL_2 = "2";
	/*** 预警类型-预警维度 对应关系 */
	public static Map<String,String[]> TYPECODE_DIMENSION=new LinkedHashMap<>();
	
	/*** 预警类型-名称 对应关系 */
	public static Map<String,String> TYPECODE_NAME=new LinkedHashMap<>();
	
	/*** 预警维度-名称 对应关系 */
	public static Map<String,String> DIMENSION_NAME=new LinkedHashMap<>();
	/*** 状态-名称 对应关系 */
	public static Map<String,String> STATUS_NAME=new LinkedHashMap<>();
	/*** 预警级别-名称 对应关系 */
	public static Map<String,String> WARNLEVEL_NAME=new LinkedHashMap<>();
	
	static{
		//预警类型-预警维度 对应关系 
		TYPECODE_DIMENSION.put(TYPECODE_BALOVER, new String[]{DIMENSIONCODE_CORP,DIMENSIONCODE_BANKACC});
		TYPECODE_DIMENSION.put(TYPECODE_EQCORPPAY, new String[]{DIMENSIONCODE_CORP});
		TYPECODE_DIMENSION.put(TYPECODE_BANKACCPAY, new String[]{DIMENSIONCODE_BANKACC});
		TYPECODE_DIMENSION.put(TYPECODE_CASHPAY, new String[]{DIMENSIONCODE_FCACC});
		TYPECODE_DIMENSION.put(TYPECODE_OUTPAY, new String[]{DIMENSIONCODE_CORP,DIMENSIONCODE_BANKACC});
		TYPECODE_DIMENSION.put(TYPECODE_CASHOVER, new String[]{DIMENSIONCODE_CORP,DIMENSIONCODE_FCACC});
		TYPECODE_DIMENSION.put(TYPECODE_EXPENDINFLOW, new String[]{DIMENSIONCODE_CORP,DIMENSIONCODE_BANKACC});
		TYPECODE_DIMENSION.put(TYPECODE_INFLOWOVER, new String[]{DIMENSIONCODE_CORP,DIMENSIONCODE_BANKACC});
		TYPECODE_DIMENSION.put(TYPECODE_BALDOWN, new String[]{DIMENSIONCODE_CORP,DIMENSIONCODE_BANKACC});
		TYPECODE_DIMENSION.put(TYPECODE_DEPOSITLOANOVER, new String[]{DIMENSIONCODE_CORP});
		TYPECODE_DIMENSION.put(TYPECODE_OLDBANKACC, new String[]{DIMENSIONCODE_BANKACC});
		TYPECODE_DIMENSION.put(TYPECODE_BUDGETPROGRESS, new String[]{DIMENSIONCODE_CORP});
		TYPECODE_DIMENSION.put(TYPECODE_DAYSUMPAY, new String[]{DIMENSIONCODE_CORP});
		
//		预警类型-名称 对应关系
		TYPECODE_NAME.put(TYPECODE_BALOVER, "余额超标");
		TYPECODE_NAME.put(TYPECODE_EQCORPPAY, "相同公司付款");
		TYPECODE_NAME.put(TYPECODE_BANKACCPAY, "大额账户支出");
		TYPECODE_NAME.put(TYPECODE_INFLOWOVER, "净流入透支");
		TYPECODE_NAME.put(TYPECODE_BALDOWN, "账户余额持续下降");
		TYPECODE_NAME.put(TYPECODE_OUTPAY, "系统外付款");
		TYPECODE_NAME.put(TYPECODE_EXPENDINFLOW, "支出户流入");
		TYPECODE_NAME.put(TYPECODE_CASHOVER, "库存现金超标");
		TYPECODE_NAME.put(TYPECODE_DEPOSITLOANOVER, "存贷比超标");
		TYPECODE_NAME.put(TYPECODE_CASHPAY, "大额现金支出");
		TYPECODE_NAME.put(TYPECODE_OLDBANKACC, "久悬户预警");
		TYPECODE_NAME.put(TYPECODE_BUDGETPROGRESS, "预算执行进度");
		TYPECODE_NAME.put(TYPECODE_DAYSUMPAY, "单日累计支付");
		
//		预警维度-名称 对应关系
		DIMENSION_NAME.put(DIMENSIONCODE_CORP, "单位");
		DIMENSION_NAME.put(DIMENSIONCODE_BANKACC, "银行账号");
		DIMENSION_NAME.put(DIMENSIONCODE_FCACC, "内部账号");
		//预警级别-名称 对应关系
		WARNLEVEL_NAME.put(WARNLEVEL_0, "低");
		WARNLEVEL_NAME.put(WARNLEVEL_1, "中");
		WARNLEVEL_NAME.put(WARNLEVEL_2, "高");
	}
//	日志类型：
//	01新增02修改03删除04策略执行
	/*** 日志类型-新增*/
	public final static String LOGTYPE_ADD = "01";
	/*** 日志类型-修改*/
	public final static String LOGTYPE_UPDATE = "02";
	/*** 日志类型-删除*/
	public final static String LOGTYPE_DELETE = "03";
	/*** 日志类型-策略执行*/
	public final static String LOGTYPE_EXECUTE = "04";
	/*** 预警操作类型- update 编辑更新*/
	public final static String WARNSTRATEGY_OPTYPE_UPDATE = "update";
	/*** 预警操作类型- copy  复制*/
	public final static String WARNSTRATEGY_OPTYPE_COPY = "copy";
	
//			
//			
//	状态：WARN_STATUS		
//	序号	代码	名称
//	1	1	启动
//	2	0	停止
	/*** 预警状态-启动 */
	public final static String STATUS_START = "1";
	/*** 预警状态-停止 */
	public final static String STATUS_STORP = "0";
	static {
		//状态-名称 对应关系
		STATUS_NAME.put(STATUS_START, "启动");
		STATUS_NAME.put(STATUS_STORP, "停止");
	}
//			
//			
//	数值比较运算符：WARN_BJ_OPERATOR		
//	序号	代码	名称
//	1	10	＞
//	2	11	＞＝
//	3	20	＜
//	4	21	＜＝
	/*** 预警数值比较运算符-＞*/
	public final static String BJ_OPERATOR_GT = "10";
	/*** 预警数值比较运算符-＞＝ */
	public final static String BJ_OPERATOR_GTEQ = "11";
	/*** 预警数值比较运算符-＜ */
	public final static String BJ_OPERATOR_LT = "20";
	/*** 预警数值比较运算符-＜＝ */
	public final static String BJ_OPERATOR_LTEQ = "21";
//			
//	值关系运算符：WARN_GX_OPERATOR		
//	序号	代码	名称
//	1	01	或者
//	2	02	并且
	/*** 预警值关系运算符-或者 */
	public final static String GX_OPERATOR_OR = "01";
	/*** 预警值关系运算符-并且 */
	public final static String GX_OPERATOR_AND = "02";
//			
//			
//	预警过滤条件类型：WARN_CONDITION_TYPE		
//	序号	代码	名称
//	1	corp	单位
//	2	bankAcc	银行账户
//	3	fcAcc 内部账户
//	4	bankAccAttribute	银行账号属性
//	5	bankAccType	银行账号类别
//	6	bankAccNature	银行账号性质
//	7	fcAccNature	内部账户性质
//	8	accSubject	会计科目
	/*** 预警过滤条件类型-单位 */
	public final static String CONDITION_TYPE_CORP = "corp";
	/*** 预警过滤条件类型-银行账户 */
	public final static String CONDITION_TYPE_BANKACC = "bankAcc";
	/*** 预警过滤条件类型-内部账户 */
	public final static String CONDITION_TYPE_FCACC = "fcAcc";
	/*** 预警过滤条件类型-银行账号属性*/
	public final static String CONDITION_TYPE_BANKACCATTRIBUTE = "bankAccAttribute";
	/*** 预警过滤条件类型-银行账号类别 */
	public final static String CONDITION_TYPE_BANKACCTYPE = "bankAccType";
	/*** 预警过滤条件类型-时间类型 */
	public final static String CONDITION_TYPE_TIME_TYPE = "timeType";
	/*** 预警过滤条件类型-银行账号性质 */
	public final static String CONDITION_TYPE_BANKACCNATURE = "bankAccNature";
	/*** 预警过滤条件类型-内部账户性质 */
	public final static String CONDITION_TYPE_FCACCNATURE = "fcAccNature";
	/*** 预警过滤条件类型-会计科目 */
	public final static String CONDITION_TYPE_ACCSUBJECT = "accSubject";
	/**预警策略查询资源权限**/
	public final static String RESOURCE_WARNSTRATEGY_QUERY = "fundwarn:warnstrategy:query";
	/**预警策略操作资源权限**/
	public final static String RESOURCE_WARNSTRATEGY_OP = "fundwarn:warnstrategy:op";
	/**预警数据查询资源权限**/
	public final static String RESOURCE_WARNSDATA_QUERY = "fundwarn:warnsdata:query";
	/**预警数据网点操作资源权限**/
	public final static String RESOURCE_WARNSDATA_NETOP = "fundwarn:warnsdata:netop";

	/*** 告警类型 0-应用接口 1-前置机服务 */
	public static final String EXCEPTION_WARN_TYPE_0 = "0";
	public static final String EXCEPTION_WARN_TYPE_1 = "1";

	/*** 渠道 all-全渠道发送 0-企业微信 1-钉钉 */
	public static final String EXCEPTION_WARN_CHANNEL_ALL = "all";
	public static final String EXCEPTION_WARN_CHANNEL_0 = "0";
	public static final String EXCEPTION_WARN_CHANNEL_1 = "1";

	/*** 发送状态 0-发送成功 1-发送失败 */
	public static final String EXCEPTION_WARN_STATUS_0 = "0";
	public static final String EXCEPTION_WARN_STATUS_1 = "1";

	/*** 预警过滤条件类型-逾期账款比例大于 */
	public final static String CONDITION_TYPE_WARNCONDITIONRATE = "warnConditionRate";
	/*** 预警过滤条件类型-客商信用等级 */
	public final static String CONDITION_TYPE_RATINGSELECTION = "ratingSelection";
	/*** 预警过滤条件类型-已逾期天数达到X天在当天预警 */
	public final static String CONDITION_TYPE_DATENUM = "dateNum";
}
