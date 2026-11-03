package com.bytter.core.constant;
import java.util.Calendar;
import java.util.Date;
/**
 * 资金计划常量类
 * @author huangx
 * @since 2017/7/12
 */
public class BudgetConstant {
	/*** 资金计划参数配置：fbs-资金计划 */
	public static final String FBS_CONFIG_TYPE = "fbs";
	/*** 资金计划参数配置：useBalance-上期计划余额下期是否可用  */
	public static final String FBS_CONFIG_USEBALANCE = "useBalance";
	/*** 资金计划参数配置：timeType-计划周期类型  */
	public static final String FBS_CONFIG_TIMETYPE = "timeType";
	/*** 资金计划参数配置：ycrl-是否有年计划及控制  */
	public static final String FBS_CONFIG_YCRL = "ycrl";
	/*** 资金计划参数配置：qcrl-是否有季计划及控制  */
	public static final String FBS_CONFIG_QCRL = "qcrl";
	/*** 资金计划参数配置：mcrl-是否有月计划及控制  */
	public static final String FBS_CONFIG_MCRL = "mcrl";
	/*** 资金计划参数配置：wcrl-是否有周计划及控制  */
	public static final String FBS_CONFIG_WCRL = "wcrl";
	/*** 资金计划参数配置：dcrl-是否有天计划及控制  */
	public static final String FBS_CONFIG_DCRL = "dcrl";
	/*** 资金计划参数配置：yimode-年计划上报模式  */
	public static final String FBS_CONFIG_YIMODE = "yimode";
	/*** 资金计划参数配置：yimodeM-年分解月（月是否上报计划）  */
	public static final String FBS_CONFIG_YIMODEM = "yimodeM";
	/*** 资金计划参数配置：qimode-季计划上报模式  */
	public static final String FBS_CONFIG_QIMODE = "qimode";
	/*** 资金计划参数配置：qimode-季计划滚动模式  */
	public static final String FBS_CONFIG_QIMODEROLL = "qimodeRoll";
	/*** 资金计划参数配置：qimode-季计划滚动模式 滚动分解子周期  */
	public static final String FBS_CONFIG_QROLLSUBPERIOD = "qRollSubperiod";
	/*** 资金计划参数配置：mimode-月计划上报模式  */
	public static final String FBS_CONFIG_MIMODE = "mimode";
	/*** 资金计划参数配置：mimodeRoll-月滚动模式  */
	public static final String FBS_CONFIG_MIMODEROLL = "mimodeRoll";
	/*** 资金计划参数配置：mRollSubperiod-月滚动模式 滚动分解子周期  */
	public static final String FBS_CONFIG_MROLLSUBPERIOD = "mRollSubperiod";
    /*** 资金计划参数配置：wimode-周计划上报模式  */
    public static final String FBS_CONFIG_WIMODE = "wimode";
    /*** 资金计划参数配置：wimodeRoll-周计划滚动模式  */
    public static final String FBS_CONFIG_WIMODEROLL = "wimodeRoll";
	/*** 资金计划参数配置：wimodeOneTime-周计划是否整月上报  */
	public static final String FBS_CONFIG_WIMODEONETIME = "wimodeOneTime";
	/*** 资金计划参数配置：dimodeOneTime-天计划是否连续上报  */
	public static final String FBS_CONFIG_DIMODEONETIME = "dimodeOneTime";
	/*** 资金计划参数配置：dimodeOneTimeDays-天计划连续上报天数  */
	public static final String FBS_CONFIG_DIMODEONETIMEDAYS = "dimodeOneTimeDays";
    /*** 资金计划参数配置：qimode-周计划滚动模式 滚动分解子周期  */
    public static final String FBS_CONFIG_WROLLSUBPERIOD = "wRollSubperiod";
	/*** 资金计划参数配置：inputBlance-分解计划上报余额控制方式  */
	public static final String FBS_CONFIG_INPUTBLANCE = "inputBlance";
	/*** 资金计划参数配置：dtlCol-明细字段  */
	public static final String FBS_CONFIG_DTLCOL = "dtlCol";
	/*** 资金计划参数配置：dtlColSel-明细项控制模式  */
	public static final String FBS_CONFIG_DTLCOLSEL = "dtlColSel";
	/*** 资金计划参数配置：itemBalance-计划执行科目余额控制方式  */
	public static final String FBS_CONFIG_ITEMBALANCE = "itemBalance";
	/*** 资金计划参数配置：salesReturnItem-销售回款科目设置  */
	public static final String FBS_CONFIG_SALESRETURNITEM = "salesReturnItem";
	/*** 资金计划参数配置：lowerLmit-计划差异原因分析金额下限  */
	public static final String FBS_CONFIG_LOWERLMIT= "lowerLmit";
	/*** 资金计划参数配置：ratioLimit-销售回款原因分析比率限制  */
	public static final String FBS_CONFIG_RATIOLIMIT = "ratioLimit";
	/*** 资金计划参数配置：allowEditDirectUderCorp-汇总时是否允许修改直属下级单位  */
	public static final String FBS_CONFIG_ALLOWEDITDIRECTUDERCORP = "allowEditDirectUderCorp";
	/*** 资金计划参数配置：allowEditUderCorp-汇总时是否允许跨级修改下级单位  */
	public static final String FBS_CONFIG_ALLOWEDITUDERCORP = "allowEditUderCorp";
	/*** 资金计划参数配置：allowEditCollectMoney-汇总时是否允许修改上报金额  */
	public static final String FBS_CONFIG_ALLOWEDITCOLLECTMONEY = "allowEditCollectMoney";
	/*** 资金计划参数配置：allowEditMoney-审批时是否允许修改上报金额  */
	public static final String FBS_CONFIG_ALLOWEDITMONEY = "allowEditMoney";
	/*** 资金计划参数配置：collectBalance-计划执行汇总单位科目余额控制  */
	public static final String FBS_CONFIG_COLLECTBALANCE = "collectBalance";
	/*** 计划上报状态：0-录入  */
	public final static Integer STATUE_TEMP = 0;
	/*** 计划上报状态：1-打回  */
	public final static Integer STATUE_BACK = -1;
	/*** 计划上报状态：-2-未编制  */
	public final static Integer STATUE_NULL = -2;
	/*** 计划上报状态：11-待审批  */
	public final static Integer STATUE_WAIT_CHECK = 11;
	/*** 计划上报状态：80-待汇总  */
	public final static Integer STATUE_WAIT_COLLECT = 80;
	/*** 计划上报状态：81-汇总中  */
	public final static Integer STATUE_WAIT_COLLECTED = 81;
	/*** 计划上报状态：82-汇总审批中  */
	public final static Integer STATUE_WAIT_CHECK_COLLECT = 82;
	/*** 计划上报状态：83-汇总审批打回  */
	public final static Integer STATUE_COLLECT_BACK = 83;
	/*** 计划上报状态：95-计划编制完成  */
	public final static Integer STATUE_CHECK = 95;
	/*** 计划上报状态：96-计划追加录入  */
	public final static Integer STATUE_SUPERADD_TEMP = 96;
	/*** 计划上报状态：97-计划追加打回  */
	public final static Integer STATUE_SUPERADD_BACK = 97;
	/*** 计划上报状态：98-计划追加审批中  */
	public final static Integer STATUE_SUPERADD_WAIT_CHECK = 98;
	/*** 计划上报状态：99-计划追加待汇总  */
	public final static Integer STATUE_SUPERADD_WAIT_COLLECT = 99;
	/*** 计划上报状态：100-计划追加汇总中   */
	public final static Integer STATUE_SUPERADD_WAIT_COLLECTED = 100;
	/*** 计划上报状态：101-计划追加审批打回   */
	public final static Integer STATUE_SUPERADD_COLLECT_BAC = 101;
	/*** 计划周期类型：1-年  */
	public final static String TIME_TYPE_YEAR = "1";
	/*** 计划周期类型：2-季  */
	public final static String TIME_TYPE_QUARTER = "2";
	/*** 计划周期类型：3-月  */
	public final static String TIME_TYPE_MONTH = "3";
	/*** 计划周期类型：4-周  */
	public final static String TIME_TYPE_WEEK = "4";
	/*** 计划周期类型：5-天  */
	public final static String TIME_TYPE_DAY = "5";
	/*** 显示类型：1-全部  */
	public final static String SHOW_TYPE_1 = "1";
	/*** 显示类型：2-录入  */
	public final static String SHOW_TYPE_2 = "2";
	/*** 显示类型（整月上报）：1-本月周计划  */
	public final static String SHOW_TYPE_WEEK_1 = "1";
	/*** 显示类型（整月上报）：2-下月周计划  */
	public final static String SHOW_TYPE_WEEK_2 = "2";
	/*** 系统计划科目：100-期初资金  */
	public final static String ITEM_CODE_100 = "100";
	/*** 系统计划科目：200-资金增加  */
	public final static String ITEM_CODE_200 = "200";
	/*** 系统计划科目：300-可用资金  */
	public final static String ITEM_CODE_300 = "300";
	/*** 系统计划科目：400-资金减少  */
	public final static String ITEM_CODE_400 = "400";
	/*** 系统计划科目：500-资金多余或不足  */
	public final static String ITEM_CODE_500 = "500";
	/*** 系统计划科目：600-新增筹资  */
	public final static String ITEM_CODE_600 = "600";
	/*** 系统计划科目：700-期末资金   */
	public final static String ITEM_CODE_700 = "700";
	/*** 系统计划科目：800-表外科目  */
	public final static String ITEM_CODE_800 = "800";
	/*** 提示消息：计划周期类型未开启  */
	public final static String MESSAGE_TIMETYPE = "fbs.budget.input.message.tiemtype";
	/*** 提示消息：单位未分配计划科目  */
	public final static String MESSAGE_ITEMCODE = "fbs.budget.input.message.itmecode";
	/*** 提示消息：父周期计划未上报编制完成，不允许上报子周期计划 */
	public final static String MESSAGE_PARENT_TIMETYPE = "fbs.budget.input.message.parenttiemtype";
	/*** 提示消息：上级单位计划已汇总并且编制完成，请初始化零计划 */
	public final static String MESSAGE_PARENT_CORP_INIT = "fbs.budget.input.message.parentcorp.init";
	/*** 提示消息：月计划未上报编制完成，不允许追加年计划 */
	public final static String MESSAGE_MONEY_NOT_REPORT = "fbs.budget.input.message.money.not.report";
	/*** 提示消息：周计划未上报编制完成，不允许追加月计划 */
	public final static String MESSAGE_WEEK_NOT_REPORT = "fbs.budget.input.message.week.not.report";
	/*** 提示消息：执行业务单位不能为空！  */
	public final static String MESSAGE_PROCESS_CORP = "fbs.budget.process.message.corp";
	/*** 提示消息：存在部分单位不需要做计划控制，无法进行批量挂接计划!  */
	public final static String MESSAGE_PROCESS_CORP_REPORTBUDGET = "fbs.budget.process.message.corp.reportBudget";
	/*** 提示消息：单位控制方式不一致，无法进行批量挂接计划! */
	public final static String MESSAGE_PROCESS_CORP_CONTROLBUDGET = "fbs.budget.process.message.corp.controlBudget";
	/*** 提示消息：单位本位币不一致，无法进行批量挂接计划! */
	public final static String MESSAGE_PROCESS_CORP_CUR = "fbs.budget.process.message.corp.cur";
	/*** 提示消息：存在部分单位没有可用的计划，无法进行批量挂接计划! */
	public final static String MESSAGE_PROCESS_BUDGET = "fbs.budget.process.message.budget";
	/*** 提示消息：单位计划上报币种不一致，无法进行批量挂接计划! */
	public final static String MESSAGE_PROCESS_BUDGET_CUR = "fbs.budget.process.message.budget.cur";
	/*** 上报金额不能为空，检查导入模板是否匹配 */
	public final static String FBS_BUDGET_DETAIL_MESSAGE_BMONE_MISMATCH = "fbs.budget.detail.message.bmoney.mismatch";
	/*** 追加金额不能为，检查导入模板是否匹配 */
	public final static String FBS_BUDGET_DETAIL_MESSAGE_BTEMPMONE_MISMATCH = "fbs.budget.detail.message.btempmoney.mismatch";
	/*** 计划科目操作类型：add-计划执行  */
	public final static String ITEM_TYPE_ADD = "add";
	/*** 计划科目操作类型：select-挂接计划科目  */
	public final static String ITEM_TYPE_SELECT = "select";
	/*** 执行系统名称：fbs-资金计划  */
	public final static String PROCESS_SYSNAME_FBS = "fbs";
	/*** 执行系统名称：nis-结算管理  */
	public final static String PROCESS_SYSNAME_NIS = "nis";
	/*** 执行系统名称：tp-第三方支付  */
	public final static String PROCESS_SYSNAME_TP = "tp";
	/*** 执行系统名称：ab-票据管理  */
	public final static String PROCESS_SYSNAME_AB = "ab";
	/*** 执行系统名称：cms-信贷融资  */
	public final static String PROCESS_SYSNAME_CMS = "cms";
	/*** 执行系统名称：fex-外汇管理  */
	public final static String PROCESS_SYSNAME_FEX = "fex";
	/*** 执行系统名称：fex-国际结算  */
	public final static String PROCESS_SYSNAME_INTERNATIONAL = "international";
	/*** 执行业务名称：input-手工录入  */
	public final static String BUSINESS_NAME_INPUT = "input";
	/*** 执行业务名称：pay-付款  */
	public final static String BUSINESS_NAME_PAY = "pay";
	/*** 执行业务名称：ttpayment-第三方付款  */
	public final static String BUSINESS_NAME_TTPAYMENT = "ttPayment";
	/*** 执行业务名称：gather-收款  */
	public final static String BUSINESS_NAME_GATHER = "gather";
	/*** 执行业务名称：expense-费用报销  */
	public final static String BUSINESS_NAME_EXPENSE = "expense";
	/*** 执行业务名称：allocation-资金调拨  */
	public final static String BUSINESS_NAME_ALLOCATION = "allocation";
	/*** 执行业务名称：wage-代发工资  */
	public final static String BUSINESS_NAME_WAGE = "wage";
	/*** 执行业务名称：emit-开票  */
	public final static String BUSINESS_NAME_EMIT = "emit";
	/*** 执行业务名称：rec-收票  */
	public final static String BUSINESS_NAME_REC = "rec";
	/*** 执行业务名称：endorse-背书  */
	public final static String BUSINESS_NAME_ENDORSE = "endorse";
	/*** 执行业务名称：discount-贴现  */
	public final static String BUSINESS_NAME_DISCOUNT = "discount";
	/*** 执行业务名称：collection-托收  */
	public final static String BUSINESS_NAME_COLLECTION = "collection";
	/*** 执行业务名称：precative-委托贷款 */
	public final static String BUSINESS_NAME_PRECATIVE = "precative";
	/*** 执行业务名称：precative.repay-委托贷款归还  */
	public final static String BUSINESS_NAME_PRECATIVE_REPAY = "precative.repay";
	/*** 执行业务名称：foreign-外币汇款  */
	public final static String BUSINESS_NAME_FOREIGN = "foreign";
	/*** 执行业务名称：tran-外汇交易  */
	public final static String BUSINESS_NAME_TRAN = "tran";
	/*** 执行业务名称：tranExercise-行权  */
	public final static String BUSINESS_NAME_TRANEXERCISE = "tranExercise";
	/*** 执行业务名称：precative-委托贷款 补录**/
	public final static String BUSINESS_NAME_PRECATIVE_ADDITION = "precative.addition";
	/*** 执行业务名称：precative-委托贷款还款补录**/
	public final static String BUSINESS_NAME_PRECATIVE_REPAYADDITION = "repayAddition";
	/*** 执行业务名称：provide.addition-银行借款补录  */
	public final static String BUSINESS_NAME_PROVIDE_ADDITION = "provide.addition";
	/*** 执行业务名称：provide.send-银行借款发放  */
	public final static String BUSINESS_NAME_PROVIDE_SEND = "provide.send";
	/*** 执行业务名称：provide.repay-银行借款还款  */
	public final static String BUSINESS_NAME_PROVIDE_REPAY = "provide.repay";
	/*** 执行业务名称：直融业务登记 */
	public final static String BUSINESS_NAME_DF_INFO = "df.info";
	/*** 执行业务名称：直融业务还款 */
	public final static String BUSINESS_NAME_DF_REPAY = "df.repay";
	/*** 执行业务名称：内部拆借登记 */
	public final static String BUSINESS_NAME_INL_SEND = "inl.send";
	/*** 执行业务名称：内部拆借还款 */
	public final static String BUSINESS_NAME_INL_REPAY = "inl.repay";
	/*** 执行业务名称：bisaccdtl-银行流水 */
	public final static String BUSINESS_NAME_BISACCDTL = "bisaccdtl";
	/*** 执行业务名称：fcAccBookDtl-内部流水 */
	public final static String BUSINESS_NAME_FCACCBOOKDTL = "fcAccBookDtl";
	/*** 执行业务名称：bisaccdtl-流水认领 */
	public final static String BUSINESS_BISACCDTL_CLAIM = "bisdtlclaim";
	/*** 执行业务名称：credit-信用证开证  */
	public final static String BUSINESS_NAME_CREDIT = "credit";
	/*** 执行业务名称：pay-信用证收证  */
	public final static String BUSINESS_NAME_RECEIVE = "receive";
	/*** 本期 */
	public final static String TIME_TYPE_PREIODNAME_CUR = "fbs.fbsBudget.curPeriod";
	/*** 下期 */
	public final static String TIME_TYPE_PREIODNAME_NEXT = "fbs.fbsBudget.nextPeriod";
	/*** 现款 */
	public final static String SETTLTYPE_0 = "0";
	/*** 票据 */
	public final static String SETTLTYPE_1 = "1";
	/*** 信用证 */
	public final static String SETTLTYPE_2 = "2";
	/*** 上报金额 */
	public final static String B_MONEY = "bMoney";
	/*** 可上报金额 */
	public final static String CAN_BMONEY = "canBmoney";
	/*** 留存金额 */
	public final static String REMAIN_BMONEY = "remainBmoney";
	/*** 没有 */
	public final static String CONFIG_CRL_0 = "0";
	/*** 有并控制子周期计划 */
	public final static String CONFIG_CRL_1 = "1";
	/*** 有但不控制子周期计划 */
	public final static String CONFIG_CRL_2 = "2";
	/*** 单位是否汇总 0-否 1-是 */
	public final static Integer CORP_BUDGET_COLLECT_FLAG_NO = 0;
	/*** 单位是否汇总 0-否 1-是 */
	public final static Integer CORP_BUDGET_COLLECT_FLAG_YES = 1;
	/*** 单位是否受控 0-否 1-是 */
	public final static Integer CORP_BUDGET_CONTROL_BUDGET_NO = 0;
	/*** 单位是否受控 0-否 1-是 */
	public final static Integer CORP_BUDGET_CONTROL_BUDGET_YES = 1;
	/*** 单位是否上报 0-否 1-是 */
	public final static Integer CORP_BUDGET_REPORT_BUDGET_NO = 0;
	/*** 单位是否上报 0-否 1-是 */
	public final static Integer CORP_BUDGET_REPORT_BUDGET_YES = 1;
	/*** 预算报表分割常量*/
	public final static String BUDGET_SPLIT = "!~!";
	/*** 预算报表查询模式：列表*/
	public final static String BUDGET_REPORT_VIEW = "0";
	/*** 预算报表查询模式：比较*/
	public final static String BUDGET_REPORT_COMPARE = "1";
	/*** 资金计划参数配置：yimode-年计划上报模式 	1-年分解月*/
	public final static String FBS_CONFIG_YIMODE_RESOLVE_YEAR = "1";
	/*** 资金计划参数配置：qimode-季计划上报模式 	2-季滚动*/
	public final static String FBS_CONFIG_QIMODE_ROLL = "2";
	/*** 资金计划参数配置：mimode-月计划上报模式  1-月滚动*/
	public static final String FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING = "1";
	/*** 资金计划参数配置：mimode-月计划上报模式  2,月滚动+双月周明细*/
	public static final String FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING_WEEK = "2";
	/*** 资金计划参数配置：mimode-月计划上报模式  3-月分解周*/
	public static final String FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK = "3";
    /*** 资金计划参数配置：qimode-周计划上报模式 	2-周滚动*/
    public final static String FBS_CONFIG_WIMODE_ROLL = "2";
	/*** 资金计划参数配置：wimode-周计划是否整月一起上报  1-是*/
	public static final String FBS_CONFIG_WIMODE_MONTH = "wimode";
	/*** 资金计划操作类型：input-上报*/
	public final static String BUDGET_TARGET_INPUT = "input";
	/*** 资金计划操作类型：super-追加*/
	public final static String BUDGET_TARGET_SUPER = "super";
	/*** 内转标识 0-非内转 */
	public final static String INNER_SIGN_0 = "0";
	/*** 内转标识 1-内转 */
	public final static String INNER_SIGN_1 = "1";
	/*** 预算执行分割常量*/
	public final static String BUDGET_PROCESS_SPLIT = "!-!";
	/*** 导出类型-计划上报情况表*/
	public final static String BUDGET_EXPORTS_TYPE_BUDGET = "1";
	/*** 导出类型-合并计划上报表*/
	public final static String BUDET_EXPORTS_TYPE_BUDGETPMERGE = "2";
	/*** 导出类型-资金计划执行表*/
	public final static String BUDGET_EXPORTS_TYPE_BUDGETPROCESS = "3";
	/*** 导入科目与页面科目不一致 */
	public final static String FBS_BUDGET_DETAIL_MESSAGE_ITEMCODE_EQ = "fbs.budget.detail.message.itemCode.eq";
	/*** 金额不能为空 */
	public final static String FBS_BUDGET_DETAIL_MESSAGE_MONEY_NULL = "fbs.budget.detail.message.money.null";
	/*** 上报金额不能为0 */
	public final static String FBS_BUDGET_DETAIL_MESSAGE_BMONE_EQ_ZERO = "fbs.budget.detail.message.bmoney.null";
	/*** 追加金额不能为0 */
	public final static String FBS_BUDGET_DETAIL_MESSAGE_BTEMPMONE_EQ_ZERO = "fbs.budget.detail.message.btempmoney.null";
	/*** 币别不能为空 */
	public final static String FBS_BUDGET_DETAIL_MESSAGE_CURID_NULL = "fbs.budget.detail.message.curid.null";
	/*** 币别在系统中不存在 */
	public final static String FBS_BUDGET_DETAIL_MESSAGE_CUR_NULL = "fbs.budget.detail.message.cur.null";
	/*** 结算方式格式不符 */
	public final static String FBS_BUDGET_DETAIL_MESSAGE_SETTLTYPE_FORMAT = "fbs.budget.detail.message.settltype.format";
	/*** 归属日期不能为空 */
	public final static String FBS_BUDGET_DETAIL_MESSAGE_VESTINGDATE_NULL = "fbs.budget.detail.message.vestingdate.null";
	/*** 归属日期应该属于当前计划周期! */
	public final static String FBS_BUDGET_DETAIL_MESSAGE_VESTINGDATE_ERROR = "fbs.budget.detail.message.vestingdate.error";
	/*** 计划执行科目余额控制方式!  受本科目控制*/
	public final static String FBS_CONFIG_ITEMBALANCE_ONESELF = "0";
	/*** 计划执行科目余额控制方式!  受上级科目控制*/
	public final static String FBS_CONFIG_ITEMBALANCE_SUPER = "2";
	/*** 计划执行科目余额控制方式!  受大科目控制*/
	public final static String FBS_CONFIG_ITEMBALANCE_TOP = "1";
	/*** 分析状态：0-待分析  */
	public final static Integer ANALYSE_STATUE_TEMP = 0;
	/*** 分析状态：1-审核打回  */
	public final static Integer ANALYSE_STATUE_BACK = -1;
	/*** 分析状态：11-待审核  */
	public final static Integer ANALYSE_STATUE_WAIT_CHECK = 11;
	/*** 分析状态：95-审核通过  */
	public final static Integer  ANALYSE_STATUE_CHECK = 95;
	/**
	 * 根据定期期限得到相应时间
	 *
	 * @param regulars
	 * @return 时间
	 */
	public static Date getRegularsDate(String regulars) {
		Calendar cal = Calendar.getInstance();
		if ("01".equals(regulars)) {
			cal.add(Calendar.MONTH, 1);
		} else if ("02".equals(regulars)) {
			cal.add(Calendar.MONTH, 3);
		} else if ("03".equals(regulars)) {
			cal.add(Calendar.MONTH, 6);
		} else if ("04".equals(regulars)) {
			cal.add(Calendar.YEAR, 1);
		} else if ("05".equals(regulars)) {
			cal.add(Calendar.YEAR, 2);
		} else if ("06".equals(regulars)) {
			cal.add(Calendar.YEAR, 3);
		} else if ("07".equals(regulars)) {
			cal.add(Calendar.YEAR, 5);
		} else {
			cal.getTime();
		}
		return cal.getTime();
	}
	/*** 计划执行规则：1-临时资金计划  */
	public final static String PROCESS_RULE_1 = "1";
	/*** 计划执行规则：2-未编制完成计划  */
	public final static String PROCESS_RULE_2 = "2";
	/*** 计划执行规则：3-父周期计划  */
	public final static String PROCESS_RULE_3 = "3";
	/*** 计划执行规则：4-上期资金计划比例  */
	public final static String PROCESS_RULE_4 = "4";
	/*** 计划执行规则：5-先执行不控制  */
	public final static String PROCESS_RULE_5 = "5";
	/**
	 * 资金流向 字典DEY valid_sign
	 */
	public static final String ITEM_SIGN_DICT = "fbsItem.sign";
	/**
	 * 计划周期类型 字典DEY fbs.timeType
	 */
	public static final String BUDGET_TIMETYPE_DICT = "fbs.timeType";
	/**
	 * 状态 字典DEY fbs.timeType
	 */
	public static final String BUDGET_STATUS_DICT = "fbsBudget.status";
	/**
	 * 计划执行状态 字典DEY fbsBudgetProcess.status
	 */
	public static final String BUDGET_PROCESS_STATUS_DICT = "fbsBudgetProcess.status";
	/**
	 * 资金计划明细列 字典DEY detailCol
	 */
	public static final String BUDGET_DETAILCOL_DICT = "fbsBudget.detailCol";
	/**
	 * 结算类型 字典DEY settlType
	 */
	public static final String BUDGET_SETTLTYPE_DICT = "settlType";

	/*** 执行业务名称：budgetSel-应收单  */
	public final static String BUSINESS_NAME_BUDGETSEL = "budgetSel";
	public final static String BUSINESS_NAME_DEFAULT_BUDGETSEL = "defaultBudgetSel";


	/*** 执行业务名称：basic-应收张款 */
	public final static String BUSINESS_NAME_BASIC = "basic";

}
