package com.bytter.tally.api.constant;
import com.bytter.core.constant.ConstantBean;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
/**
 * 网银控制台常量类
 * @author lixi
 * @since 2017-7-6
 */
public class NbconsoleConstant {
	/**系统代码*/
	public static final String NBC_SYS_CODE = "nbc";
	/**查询*/
	public static final String NBC_QUERY = "2";
	/**输入*/
	public static final String NBC_INPUT = "1";
	
	/**地址*/
	public static final String EXPEXLEL_BASEURL = File.separator + "nbconsole" + File.separator + "jxlsTemplates"
			+ File.separator;
	/**
	 * 记账类型
	 */
	public static class OpType {
		public static final ConstantBean<String> INIT = new ConstantBean<String>("记账", "Init");
		public static final ConstantBean<String> EDIT = new ConstantBean<String>("改账", "Edit");
		public static final ConstantBean<String> CHECK = new ConstantBean<String>("复核", "Check");
	}
	
	/**
	 * 票据状态
	 */
	public static class BillState {
		public static final ConstantBean<String> N_NOPRINT = new ConstantBean<String>("未打印", "N");
		public static final ConstantBean<String> C_COLLECT = new ConstantBean<String>("托收", "C");
		public static final ConstantBean<String> P_TAKE = new ConstantBean<String>("收妥", "P");
		public static final ConstantBean<String> B_NOUSE = new ConstantBean<String>("作废", "B");
		public static final ConstantBean<String> R_EQUAL = new ConstantBean<String>("冲帐", "R");
		public static final ConstantBean<String> Q_RETURN = new ConstantBean<String>("退票", "Q");
	}  
	
	/**
	 * 票据种类
	 */
	public static class BillType {
		public static final ConstantBean<String> X_CHECK = new ConstantBean<String>("现金支票", "01");
		public static final ConstantBean<String> Z_CHECK = new ConstantBean<String>("转帐支票", "02");
		public static final ConstantBean<String> R_CHECK = new ConstantBean<String>("进账单", "03");
		public static final ConstantBean<String> H_CHECK = new ConstantBean<String>("汇票", "04");
		public static final ConstantBean<String> M_CHECK = new ConstantBean<String>("信汇", "05");
		public static final ConstantBean<String> T_CHECK = new ConstantBean<String>("电汇", "06");
		public static final ConstantBean<String> B_CHECK = new ConstantBean<String>("本票", "07");  
	}   
	/**
	 * 银企接口记账状态
	 */
	public static class NbcEstate {
		public static final ConstantBean<String> E_STATE_CHECK = new ConstantBean<String>("待记账", "0");
		public static final ConstantBean<String> E_STATE_CHECK_PASS = new ConstantBean<String>("记账复核通过", "1");
		public static final ConstantBean<String> E_STATE_UNAVAIL = new ConstantBean<String>("记账无效", "2");
		public static final ConstantBean<String> E_STATE_WAIT_CHECK = new ConstantBean<String>("记账提交待复核", "3");
		public static final ConstantBean<String> E_STATE_PASS_BACK = new ConstantBean<String>("记账复核打回", "4");
		public static final ConstantBean<String> E_STATE_TEMP = new ConstantBean<String>("记账暂存", "5");
		public static final ConstantBean<String> E_STATE_BACK = new ConstantBean<String>("改账打回", "6");
		public static final ConstantBean<String> E_STATE_AUTOACC_FAIL = new ConstantBean<String>("自动记账失败", "8");
	}
	/**
	 * 付款方式
	 */
	public static class PayType {
		public static final ConstantBean<String> PAY_TYPE_01 = new ConstantBean<String>("转收", "01");
		public static final ConstantBean<String> PAY_TYPE_02 = new ConstantBean<String>("转付", "02");
		public static final ConstantBean<String> PAY_TYPE_03 = new ConstantBean<String>("现收", "03");
		public static final ConstantBean<String> PAY_TYPE_04 = new ConstantBean<String>("现付", "04");
		public static final ConstantBean<String> PAY_TYPE_05 = new ConstantBean<String>("内转", "05");
		public static final ConstantBean<String> PAY_TYPE_06 = new ConstantBean<String>("电汇", "06");
		public static final ConstantBean<String> PAY_TYPE_07 = new ConstantBean<String>("汇票", "07");
		public static final ConstantBean<String> PAY_TYPE_08 = new ConstantBean<String>("本票", "08");
		public static final ConstantBean<String> PAY_TYPE_09 = new ConstantBean<String>("手续费", "09");
		public static final ConstantBean<String> PAY_TYPE_10 = new ConstantBean<String>("拨付", "10");
		public static final ConstantBean<String> PAY_TYPE_11 = new ConstantBean<String>("其它", "11");
		public static final ConstantBean<String> PAY_TYPE_12 = new ConstantBean<String>("信汇", "12");
		public static final ConstantBean<String> PAY_TYPE_13 = new ConstantBean<String>("联行转账", "13");
		public static final ConstantBean<String> PAY_TYPE_30 = new ConstantBean<String>("活期转定期", "30");
		public static final ConstantBean<String> PAY_TYPE_31 = new ConstantBean<String>("活期转通知", "31");
		public static final ConstantBean<String> PAY_TYPE_32 = new ConstantBean<String>("联行转账", "32");
		public static final ConstantBean<String> PAY_TYPE_33 = new ConstantBean<String>("代付", "33");
		public static final ConstantBean<String> PAY_TYPE_34 = new ConstantBean<String>("外付网银", "34");
		public static final ConstantBean<String> PAY_TYPE_35 = new ConstantBean<String>("代理支付", "35");
		public static final ConstantBean<String> PAY_TYPE_36 = new ConstantBean<String>("个人报账", "36");
		public static final ConstantBean<String> PAY_TYPE_37 = new ConstantBean<String>("资金调拨", "37");
		public static final ConstantBean<String> PAY_TYPE_38 = new ConstantBean<String>("证券汇划", "38");
		public static final ConstantBean<String> PAY_TYPE_40 = new ConstantBean<String>("外付票据", "40");
		public static final ConstantBean<String> PAY_TYPE_41 = new ConstantBean<String>("票据支付", "41");
		public static final ConstantBean<String> PAY_TYPE_46 = new ConstantBean<String>("代客结汇", "46");
		public static final ConstantBean<String> PAY_TYPE_47 = new ConstantBean<String>("代客购汇", "47");
		public static final ConstantBean<String> PAY_TYPE_48 = new ConstantBean<String>("代发工资", "48");
		public static final ConstantBean<String> PAY_TYPE_49 = new ConstantBean<String>("承兑汇票", "49");
		public static final ConstantBean<String> PAY_TYPE_50 = new ConstantBean<String>("批量托收", "50");
		public static final ConstantBean<String> PAY_TYPE_51 = new ConstantBean<String>("线下支票", "51");
		public static final ConstantBean<String> PAY_TYPE_55 = new ConstantBean<String>("批量计息", "55");
		public static final ConstantBean<String> PAY_TYPE_56 = new ConstantBean<String>("自营结汇", "56");
		public static final ConstantBean<String> PAY_TYPE_57 = new ConstantBean<String>("自营购汇", "57");
		public static final ConstantBean<String> PAY_TYPE_60 = new ConstantBean<String>("归集", "60");
		public static final ConstantBean<String> PAY_TYPE_61 = new ConstantBean<String>("下拨", "61");
		public static final ConstantBean<String> PAY_TYPE_62 = new ConstantBean<String>("现金支票", "62");
		public static final ConstantBean<String> PAY_TYPE_63 = new ConstantBean<String>("转账支票", "63");
	}
	/**
	 * 还款方式
	 */
	public static class NbcRepayType {
		public static final ConstantBean<String> Repay_Type_01 = new ConstantBean<String>("还本付息", "01");
		public static final ConstantBean<String> Repay_Type_02 = new ConstantBean<String>("分期等额本金", "02");
		public static final ConstantBean<String> Repay_Type_03 = new ConstantBean<String>("其它", "03");
		public static final ConstantBean<String> Repay_Type_04 = new ConstantBean<String>("分期等额本息", "04");
	}
	/**
	 * 贷款种类
	 */
   
	public static class NbcLoanClass {
		public static final ConstantBean<String> LOAN_CLASS_01 = new ConstantBean<String>("短期", "01");
		public static final ConstantBean<String> LOAN_CLASS_02 = new ConstantBean<String>("中期", "02");
		public static final ConstantBean<String> LOAN_CLASS_03 = new ConstantBean<String>("长期", "03");
	}
	/**
	 * 企业贷款放款/还款状态
	 */
	public static class NbcEnterpriseStatus {
		public static final ConstantBean<Integer> NBC_STATUS_DELETED = new ConstantBean<Integer>("已删除", -2);
		public static final ConstantBean<Integer> NBC_NET_BACK = new ConstantBean<Integer>("上级网点记账打回", -1);
		public static final ConstantBean<Integer> NBC_STATUS_TALLY = new ConstantBean<Integer>("待记账", 97);
		public static final ConstantBean<Integer> NBC_STATUS_TEMP = new ConstantBean<Integer>("记账暂存", 120);
		public static final ConstantBean<Integer> NBC_STATUS_MODIFY_BACK = new ConstantBean<Integer>("改账打回", 101);
		public static final ConstantBean<Integer> NBC_STATUS_CHECK_BACK = new ConstantBean<Integer>("复核打回", 100);
		public static final ConstantBean<Integer> NBC_STATUS_SUBMIT = new ConstantBean<Integer>("记账提交待复核", 98);
		public static final ConstantBean<Integer> NBC_STATUS_MODIFY_COMPLETE = new ConstantBean<Integer>("改账完成", 99);
		public static final ConstantBean<Integer> NBC_STATUS_PASS = new ConstantBean<Integer>("待还款", 102);
		public static final ConstantBean<Integer> NBC_STATUS_EXTENSION = new ConstantBean<Integer>("展期申请中", 103);
		public static final ConstantBean<Integer> NBC_STATUS_OTHER1 = new ConstantBean<Integer>("未知1", 106);
		public static final ConstantBean<Integer> NBC_STATUS_OTHER2 = new ConstantBean<Integer>("展期完成", 104);
		public static final ConstantBean<Integer> NBC_STATUS_OTHER3 = new ConstantBean<Integer>("审核通过", 95);
		public static final ConstantBean<Integer> NBC_STATUS_CONTRACT = new ConstantBean<Integer>("待生成合同", 96);
		public static final ConstantBean<Integer> NBC_STATUS_FINISH = new ConstantBean<Integer>("业务结束", 110);
	}
	/**
	 * 委托贷款/还款状态
	 */
	public static class NbcPrecativeStatus {
		public static final ConstantBean<Integer> NBC_STATUS_TALLY = new ConstantBean<Integer>("待记账", 97);
		public static final ConstantBean<Integer> NBC_STATUS_TEMP = new ConstantBean<Integer>("记账暂存", 98);
		public static final ConstantBean<Integer> NBC_STATUS_MODIFY_BACK = new ConstantBean<Integer>("改账打回", 100);
		public static final ConstantBean<Integer> NBC_STATUS_CHECK_BACK = new ConstantBean<Integer>("复核打回", 101);
		public static final ConstantBean<Integer> NBC_STATUS_SUBMIT = new ConstantBean<Integer>("记账提交待复核", 99);
		public static final ConstantBean<Integer> NBC_STATUS_PASS = new ConstantBean<Integer>("记账复核通过", 102);
		public static final ConstantBean<Integer> NBC_STATUS_EXTENSION_PASS = new ConstantBean<Integer>("展期通过待还款", 104);
		public static final ConstantBean<Integer> NBC_STATUS_FINISH = new ConstantBean<Integer>("业务结束", 110);
	}
	/**
	 * 描述：记帐标识
	 */
	public static class TallyFlag {
		public static final ConstantBean<Integer> TALLY_FLAG_0 = new ConstantBean<Integer>("未记帐", 0);
		public static final ConstantBean<Integer> TALLY_FLAG_1 = new ConstantBean<Integer>("已记帐", 1);
		public static final ConstantBean<Integer> TALLY_FLAG_2 = new ConstantBean<Integer>("记帐中打回", 2);
		public static final ConstantBean<Integer> TALLY_FLAG_3 = new ConstantBean<Integer>("记帐待复核", 3);
		public static final ConstantBean<Integer> TALLY_FLAG_4 = new ConstantBean<Integer>("记帐复核打回", 4);
		public static final ConstantBean<Integer> TALLY_FLAG_5 = new ConstantBean<Integer>("记帐暂存", 5);
	}
	/**
	 * 描述： 业务结束标志
	 */
	public static class FinishFlag {
		public static final ConstantBean<Integer> FINISH_FLAG_0 = new ConstantBean<Integer>("未结束", 0);
		public static final ConstantBean<Integer> FINISH_FLAG_1 = new ConstantBean<Integer>("已结束（成功付款，且已记帐）", 1);
		public static final ConstantBean<Integer> FINISH_FLAG_2 = new ConstantBean<Integer>("已结束（各种打回情况）", 2);
	}
	/**
	 * 确认审批状态
	 */
	public static class ConfirmAuditState {
		public static final ConstantBean<Long> CONFIRM_AUDIT_STATE_0 = new ConstantBean<Long>("未审核", 0l);
		public static final ConstantBean<Long> CONFIRM_AUDIT_STATE_1 = new ConstantBean<Long>("打回", 1l);
		public static final ConstantBean<Long> CONFIRM_AUDIT_STATE_2 = new ConstantBean<Long>("审核中", 2l);
		public static final ConstantBean<Long> CONFIRM_AUDIT_STATE_3 = new ConstantBean<Long>("通过", 3l);
	}
	/**
	 * 结售汇的交易方式
	 */
	public static class FxType {
		public static final ConstantBean<Integer> FX_TYPE_1 = new ConstantBean<Integer>("信用证", 1);
		public static final ConstantBean<Integer> FX_TYPE_2 = new ConstantBean<Integer>("托收", 2);
		public static final ConstantBean<Integer> FX_TYPE_3 = new ConstantBean<Integer>("电汇", 3);
		public static final ConstantBean<Integer> FX_TYPE_4 = new ConstantBean<Integer>("信汇", 4);
		public static final ConstantBean<Integer> FX_TYPE_5 = new ConstantBean<Integer>("票汇", 5);
		public static final ConstantBean<Integer> FX_TYPE_6 = new ConstantBean<Integer>("其它", 6);
	}
	/**
	 * 是否是外汇市场交易的数据
	 */
	public static class FxOrnot {
		public static final ConstantBean<Integer> YES = new ConstantBean<Integer>("是", 10);
		public static final ConstantBean<Integer> NO = new ConstantBean<Integer>("否", 11);
	}
	/**
	 * 担保方式系统类型
	 */
	public static class GuaMainType {
		public static final ConstantBean<String> Enterprise_LOAN = new ConstantBean<String>("内部贷款", "A");
		public static final ConstantBean<String> BANK_LOAN = new ConstantBean<String>("银行借款", "B");
		public static final ConstantBean<String> PRECATIVE_LOAN = new ConstantBean<String>("委托贷款", "C");
		public static final ConstantBean<String> INTERBANK_LOAN = new ConstantBean<String>("同业拆借", "E");
		public static final ConstantBean<String> GUARANTEE_LOAN = new ConstantBean<String>("保函管理", "F");
	}
	/**
	 * 担保方式
	 */
	public static class GuaMainName {
		public static final ConstantBean<String> TYPE_NAME_01 = new ConstantBean<String>("信用", "01");
		public static final ConstantBean<String> TYPE_NAME_02 = new ConstantBean<String>("保证", "02");
		public static final ConstantBean<String> TYPE_NAME_03 = new ConstantBean<String>("抵押", "03");
		public static final ConstantBean<String> TYPE_NAME_04 = new ConstantBean<String>("质押", "04");
		public static final ConstantBean<String> TYPE_NAME_05 = new ConstantBean<String>("组合担保", "05");
		public static final ConstantBean<String> TYPE_NAME_06 = new ConstantBean<String>("其它", "06");
	}
	/**
	 * 拆借方式
	 */
	public static class InterBankTyaape {
		public static final ConstantBean<String> DETACH_IN = new ConstantBean<String>("拆入", "1");
		public static final ConstantBean<String> DETACH_OUT = new ConstantBean<String>("拆出", "2");
	}
	/**
	 * 联行往来记账信息表单状态
	 */
	public static class UnionAccbookBillState {
		public static final ConstantBean<String> W = new ConstantBean<String>("接收方待记账", "W");
		//add by 文春宏
		public static final ConstantBean<String> V = new ConstantBean<String>("接收方暂存", "V");
		public static final ConstantBean<String> Z = new ConstantBean<String>("接收方复核打回", "Z");
		public static final ConstantBean<String> G = new ConstantBean<String>("接收方待审批", "G");
		public static final ConstantBean<String> B = new ConstantBean<String>("发起方已作废", "B");
		public static final ConstantBean<String> R = new ConstantBean<String>("发起方已冲账", "R");
		public static final ConstantBean<String> Q = new ConstantBean<String>("接收方已打回", "Q");
		public static final ConstantBean<String> T = new ConstantBean<String>("接收方已记账", "T");
		public static final ConstantBean<String> C = new ConstantBean<String>("发起方已取消记账", "C");
		public static final ConstantBean<String> S = new ConstantBean<String>("发起方作废接收方已记账", "S");
		public static final ConstantBean<String> M = new ConstantBean<String>("发起方作废接收方已抹账", "M");
		public static final ConstantBean<String> N = new ConstantBean<String>("发起方作废接收方已冲账", "N");
		public static final ConstantBean<String> F = new ConstantBean<String>("发起方作废接收方已作废", "F");
		public static final ConstantBean<String> O = new ConstantBean<String>("发起方冲账接收方已作废", "O");
		public static final ConstantBean<String> P = new ConstantBean<String>("发起方冲账接收方已冲账", "P");
		public static final ConstantBean<String> D = new ConstantBean<String>("发起方冲账接收方已抹账", "D");
		public static final ConstantBean<String> E = new ConstantBean<String>("发起方冲账接收方已记账", "E");
	}
	/**
	 * 联行往来记账信息业务来源
	 */
	public static class UnionAccbookBusOriType {
		public static final ConstantBean<Integer> ENTRUST_COLLECT = new ConstantBean<Integer>("委托收款", 10);
		public static final ConstantBean<Integer> N2N_BROW = new ConstantBean<Integer>("网点间借款", 11);
		public static final ConstantBean<Integer> N2N_PAY = new ConstantBean<Integer>("网点间付款", 12);
		public static final ConstantBean<Integer> N2N_COLLECT = new ConstantBean<Integer>("网点间收款", 13);
	}
	/**
	 * 网银控制台数据权限控制
	 * @param opType 类型
	 * @param userCode 用户id
	 * @param name 名称
	 * @return sql
	 */
	public static String produceUserHql(String opType, String userCode, String name) {
		StringBuffer userHql = new StringBuffer();
		if (OpType.CHECK.get().equals(opType)) {
			userHql.append(" and ");
			userHql.append(name);
			userHql.append("!='");
			userHql.append(userCode);
			userHql.append("' ");
		} else if (OpType.EDIT.get().equals(opType)) {
			userHql.append(" and ");
			userHql.append(name);
			userHql.append("='");
			userHql.append(userCode);
			userHql.append("' ");
		}
		return userHql.toString();
	}
	/**
	 * 网银控制台主页业务常量
	 */
	public static class NbcMain {
		public static final ConstantBean<String> BUS_MAIN_01 = new ConstantBean<String>("单位一般付款", "A1");
		public static final ConstantBean<String> BUS_MAIN_02 = new ConstantBean<String>("请款", "A2");
		public static final ConstantBean<String> BUS_MAIN_03 = new ConstantBean<String>("中心一般付款", "A3");
		public static final ConstantBean<String> BUS_MAIN_04 = new ConstantBean<String>("头寸调拨", "A4");
		public static final ConstantBean<String> BUS_MAIN_05 = new ConstantBean<String>("下拨", "A5");
		public static final ConstantBean<String> BUS_MAIN_06 = new ConstantBean<String>("委托收款", "A6");
		public static final ConstantBean<String> BUS_MAIN_07 = new ConstantBean<String>("银企接口收款", "A7");
		public static final ConstantBean<String> BUS_MAIN_08 = new ConstantBean<String>("结汇", "A8");
		public static final ConstantBean<String> BUS_MAIN_09 = new ConstantBean<String>("购汇", "A9");
		public static final ConstantBean<String> BUS_MAIN_10 = new ConstantBean<String>("活期转定期", "A10");
		public static final ConstantBean<String> BUS_MAIN_11 = new ConstantBean<String>("定期转活期", "A11");
		public static final ConstantBean<String> BUS_MAIN_12 = new ConstantBean<String>("活期转通知", "A12");
		public static final ConstantBean<String> BUS_MAIN_13 = new ConstantBean<String>("通知转活期", "A13");
		public static final ConstantBean<String> BUS_MAIN_24 = new ConstantBean<String>("网点间来账", "A14");
		public static final ConstantBean<String> BUS_MAIN_25 = new ConstantBean<String>("网点间借款", "A15");
		public static final ConstantBean<String> BUS_MAIN_26 = new ConstantBean<String>("网点间借款还款", "A16");
		public static final ConstantBean<String> BUS_MAIN_14 = new ConstantBean<String>("放款", "B1");
		public static final ConstantBean<String> BUS_MAIN_15 = new ConstantBean<String>("放款还款", "B2");
		public static final ConstantBean<String> BUS_MAIN_16 = new ConstantBean<String>("委托贷款", "B3");
		public static final ConstantBean<String> BUS_MAIN_17 = new ConstantBean<String>("委托贷款归还", "B4");
		public static final ConstantBean<String> BUS_MAIN_18 = new ConstantBean<String>("同业拆出", "B5");
		public static final ConstantBean<String> BUS_MAIN_19 = new ConstantBean<String>("同业拆出归还", "B6");
		public static final ConstantBean<String> BUS_MAIN_20 = new ConstantBean<String>("借款", "B7");
		public static final ConstantBean<String> BUS_MAIN_21 = new ConstantBean<String>("借款还款", "B8");
		public static final ConstantBean<String> BUS_MAIN_22 = new ConstantBean<String>("同业拆入", "B9");
		public static final ConstantBean<String> BUS_MAIN_23 = new ConstantBean<String>("同业拆入归还", "B10");
		
		public static final ConstantBean<String> BUS_MAIN_27 = new ConstantBean<String>("入库","C1");
		public static final ConstantBean<String> BUS_MAIN_28 = new ConstantBean<String>("移存","C2");
		public static final ConstantBean<String> BUS_MAIN_29 = new ConstantBean<String>("取票","C3");
		public static final ConstantBean<String> BUS_MAIN_30 = new ConstantBean<String>("退票","C4");
		public static final ConstantBean<String> BUS_MAIN_31 = new ConstantBean<String>("中心托收","C5");
		public static final ConstantBean<String> BUS_MAIN_32 = new ConstantBean<String>("中心托收到账","C6");
		public static final ConstantBean<String> BUS_MAIN_33 = new ConstantBean<String>("对内背书","C7");
		public static final ConstantBean<String> BUS_MAIN_34 = new ConstantBean<String>("对外背书","C8");
		public static final ConstantBean<String> BUS_MAIN_35 = new ConstantBean<String>("中心贴现","C9");
		public static final ConstantBean<String> BUS_MAIN_36 = new ConstantBean<String>("质押","C10");
		public static final ConstantBean<String> BUS_MAIN_37 = new ConstantBean<String>("质押收回","C11");
		public static final ConstantBean<String> BUS_MAIN_38 = new ConstantBean<String>("银行托收","C12");
		public static final ConstantBean<String> BUS_MAIN_39 = new ConstantBean<String>("银行托收到账","C13");
		public static final ConstantBean<String> BUS_MAIN_40 = new ConstantBean<String>("银行贴现","C14");
		public static final ConstantBean<String> BUS_MAIN_41 = new ConstantBean<String>("银行贴现到账","C15");
		
		public static final ConstantBean<String> BUS_MAIN_42 = new ConstantBean<String>("上划","C16");
		public static final ConstantBean<String> BUS_MAIN_43 = new ConstantBean<String>("下拨","C17");
		public static final ConstantBean<String> BUS_MAIN_44 = new ConstantBean<String>("归集","C18");
		
	}
	/**
	 * 网银控制台主页业务类型与路径对应配置
	 */
	public static class NbcMainUrl {
		public static final ConstantBean<String> BUS_MAIN_01 = new ConstantBean<String>("commonPayment", "A1");
		public static final ConstantBean<String> BUS_MAIN_02 = new ConstantBean<String>("commonPayment", "A2");
		public static final ConstantBean<String> BUS_MAIN_03 = new ConstantBean<String>("centerPayment", "A3");
		public static final ConstantBean<String> BUS_MAIN_04 = new ConstantBean<String>("centerPayment", "A4");
		public static final ConstantBean<String> BUS_MAIN_05 = new ConstantBean<String>("centerPayment", "A5");
		public static final ConstantBean<String> BUS_MAIN_06 = new ConstantBean<String>("cgsBill", "A6");
		public static final ConstantBean<String> BUS_MAIN_07 = new ConstantBean<String>("bankCollect", "A7");
		public static final ConstantBean<String> BUS_MAIN_08 = new ConstantBean<String>("saleExchange", "A8");
		public static final ConstantBean<String> BUS_MAIN_09 = new ConstantBean<String>("saleExchange", "A9");
		public static final ConstantBean<String> BUS_MAIN_10 = new ConstantBean<String>("depositOrDraw", "A10");
		public static final ConstantBean<String> BUS_MAIN_11 = new ConstantBean<String>("depositOrDraw", "A11");
		public static final ConstantBean<String> BUS_MAIN_12 = new ConstantBean<String>("noticeDraw", "A12");
		public static final ConstantBean<String> BUS_MAIN_13 = new ConstantBean<String>("noticeDraw", "A13");
		public static final ConstantBean<String> BUS_MAIN_24 = new ConstantBean<String>("collectionNet", "A14");
		public static final ConstantBean<String> BUS_MAIN_25 = new ConstantBean<String>("enterpriseLoanNet", "A15");
		public static final ConstantBean<String> BUS_MAIN_26 = new ConstantBean<String>("enterpriseRepaymentNet", "A16");
		
		public static final ConstantBean<String> BUS_MAIN_14 = new ConstantBean<String>("enterpriseLoan", "B1");
		public static final ConstantBean<String> BUS_MAIN_15 = new ConstantBean<String>("enterpriseLoan", "B2");
		public static final ConstantBean<String> BUS_MAIN_16 = new ConstantBean<String>("precativeLoan", "B3");
		public static final ConstantBean<String> BUS_MAIN_17 = new ConstantBean<String>("precativeLoan", "B4");
		public static final ConstantBean<String> BUS_MAIN_18 = new ConstantBean<String>("interBankOut", "B5");
		public static final ConstantBean<String> BUS_MAIN_19 = new ConstantBean<String>("interBankOut", "B6");
		public static final ConstantBean<String> BUS_MAIN_20 = new ConstantBean<String>("bankLoan", "B7");
		public static final ConstantBean<String> BUS_MAIN_21 = new ConstantBean<String>("bankLoan", "B8");
		public static final ConstantBean<String> BUS_MAIN_22 = new ConstantBean<String>("interBankIn", "B9");
		public static final ConstantBean<String> BUS_MAIN_23 = new ConstantBean<String>("interBankIn", "B10");
		
		public static final ConstantBean<String> BUS_MAIN_27 = new ConstantBean<String>("draftInAccount","C1");
		public static final ConstantBean<String> BUS_MAIN_28 = new ConstantBean<String>("draftMoveAccount","C2");
		public static final ConstantBean<String> BUS_MAIN_29 = new ConstantBean<String>("draftGetAccount","C3");
		public static final ConstantBean<String> BUS_MAIN_30 = new ConstantBean<String>("draftReturnAccount","C4");
		public static final ConstantBean<String> BUS_MAIN_31 = new ConstantBean<String>("centercollect","C5");
		public static final ConstantBean<String> BUS_MAIN_32 = new ConstantBean<String>("centercollectget","C6");
		public static final ConstantBean<String> BUS_MAIN_33 = new ConstantBean<String>("internallyEndorsement","C7");
		public static final ConstantBean<String> BUS_MAIN_34 = new ConstantBean<String>("externalEndorsement","C8");
		public static final ConstantBean<String> BUS_MAIN_35 = new ConstantBean<String>("centerdiscount","C9");
		public static final ConstantBean<String> BUS_MAIN_36 = new ConstantBean<String>("impawnAccount","C10");
		public static final ConstantBean<String> BUS_MAIN_37 = new ConstantBean<String>("impawnRecycleAccount","C11");
		public static final ConstantBean<String> BUS_MAIN_38 = new ConstantBean<String>("bankcollection","C12");
		public static final ConstantBean<String> BUS_MAIN_39 = new ConstantBean<String>("bankcollectionget","C13");
		public static final ConstantBean<String> BUS_MAIN_40 = new ConstantBean<String>("bankdiscount","C14");
		public static final ConstantBean<String> BUS_MAIN_41 = new ConstantBean<String>("centerbankdiscountget","C15");
		
		public static final ConstantBean<String> BUS_MAIN_42 = new ConstantBean<String>("billDeliver","C16");
		public static final ConstantBean<String> BUS_MAIN_43 = new ConstantBean<String>("billAllocate","C17");
		public static final ConstantBean<String> BUS_MAIN_44 = new ConstantBean<String>("accumulate","C18");
		
	}
	/**
	 * 网银控制台主页路径与对业务类型应配置
	 */
	public static class NbcMainBusUrl {
		public static final ConstantBean<String> BUS_COMMON_PAYMENT = new ConstantBean<String>("A1,A2", "commonPayment");
		public static final ConstantBean<String> BUS_CENTER_PAYMENT = new ConstantBean<String>("A3,A4,A5", "centerPayment");
		public static final ConstantBean<String> BUS_CGS_BILL = new ConstantBean<String>("A6", "cgsBill");
		public static final ConstantBean<String> BUS_BANK_COLLECT = new ConstantBean<String>("A7", "bankCollect");
		public static final ConstantBean<String> BUS_SALE_EXCHANGE = new ConstantBean<String>("A8,A9", "saleExchange");
		public static final ConstantBean<String> BUS_DEPOSIT_ORDRAW = new ConstantBean<String>("A10,A11", "depositOrDraw");
		public static final ConstantBean<String> BUS_NOTICE_DRAW = new ConstantBean<String>("A12,A13", "noticeDraw");
		public static final ConstantBean<String> BUS_COLLECTIONNET = new ConstantBean<String>("A14", "collectionNet");
		public static final ConstantBean<String> BUS_ENTERPRISE_LOANNET = new ConstantBean<String>("A15", "enterpriseLoanNet");
		public static final ConstantBean<String> BUS_ENTERPRISE_REPAYMENTNET = new ConstantBean<String>("A16", "enterpriseRepaymentNet");
		
		public static final ConstantBean<String> BUS_ENTERPRISE_LOAN = new ConstantBean<String>("B1,B2", "enterpriseLoan");
		public static final ConstantBean<String> BUS_PRECATIVE_LOAN = new ConstantBean<String>("B3,B4", "precativeLoan");
		public static final ConstantBean<String> BUS_INTER_BANKOUT = new ConstantBean<String>("B5,B6", "interBankOut");
		public static final ConstantBean<String> BUS_BANK_LOAN = new ConstantBean<String>("B7,B8", "bankLoan");
		public static final ConstantBean<String> BUS_INTER_BANKIN = new ConstantBean<String>("B9,B10", "interBankIn");
		public static final ConstantBean<String> BUS_DRAFT_INACCOUNT = new ConstantBean<String>("C1,C2,C3,C4","draftInAccount");
		public static final ConstantBean<String> BUS_DRAFT_MOVE_ACCOUNT = new ConstantBean<String>("C2","draftMoveAccount");
		public static final ConstantBean<String> BUS_DRAFT_GET_ACCOUNT = new ConstantBean<String>("C3","draftGetAccount");
		public static final ConstantBean<String> BUS_DRAFT_RETURN_ACCOUNT = new ConstantBean<String>("C4","draftReturnAccount");
		public static final ConstantBean<String> BUS_CENTER_COLLECT = new ConstantBean<String>("C5","centercollect");
		public static final ConstantBean<String> BUS_CENTER_COLLECTGET = new ConstantBean<String>("C6","centercollectget");
		public static final ConstantBean<String> BUS_INTERNALLY_ENDORSEMENT = new ConstantBean<String>("C7","internallyEndorsement");
		public static final ConstantBean<String> BUS_EXTERNAL_ENDORSEMENT = new ConstantBean<String>("C8","externalEndorsement");
		public static final ConstantBean<String> BUS_CENTER_DISCOUNT = new ConstantBean<String>("C9","centerdiscount");
		public static final ConstantBean<String> BUS_IMPAWN_ACCOUNT = new ConstantBean<String>("C10","impawnAccount");
		public static final ConstantBean<String> BUS_IMPAWN_RECYCLE_ACCOUNT = new ConstantBean<String>("C11","impawnRecycleAccount");
		public static final ConstantBean<String> BUS_BANK_COLLECTION = new ConstantBean<String>("C12","bankcollection");
		public static final ConstantBean<String> BUS_BANK_COLLECTIONGET = new ConstantBean<String>("C13","bankcollectionget");
		public static final ConstantBean<String> BUS_BANK_DISCOUNT = new ConstantBean<String>("C14","bankdiscount");
		public static final ConstantBean<String> BUS_CENTER_BANK_DISCOUNTGET = new ConstantBean<String>("C15","centerbankdiscountget");
		public static final ConstantBean<String> BUS_BILL_DELIVER = new ConstantBean<String>("C16,C17,C18", "billDeliver");
		public static final ConstantBean<String> BUS_BILL_ALLOCATE = new ConstantBean<String>("C17", "billAllocate");
		public static final ConstantBean<String> BUS_BILL_ACCUMULATE = new ConstantBean<String>("C18", "accumulate");
		
	}
	
	/**
	 * 承兑汇票记账状态
	 */
	public static class IsTallyStatus {
		/** 记账打回 **/
		public static final ConstantBean<Integer> ISTALLYSTATUS_BACKINIT = new ConstantBean<Integer>("记账打回", -1);
		/** 未记帐 **/
		public static final ConstantBean<Integer> ISTALLYSTATUS_ACCOUNTING = new ConstantBean<Integer>("未记帐", 0);
		/** 记账复核通过 **/
		public static final ConstantBean<Integer> ISTALLYSTATUS_PASS = new ConstantBean<Integer>("记账复核通过", 1);
		/** 暂存 **/
		public static final ConstantBean<Integer> ISTALLYSTATUS_TEMP = new ConstantBean<Integer>("暂存", 96);
		/** 记账提交 **/
		public static final ConstantBean<Integer> ISTALLYSTATUS_SUBMIT = new ConstantBean<Integer>("记账提交", 97);
		/** 复核打回 **/
		public static final ConstantBean<Integer> ISTALLYSTATUS_BACK = new ConstantBean<Integer>("打回", 98);
	}
	
	/**
	 * 网银控制台还款时 所需还款类型
	 */
	public static class repayWay {
		//1 归还本金   2 归还本金及归还金额利息  3归还本金及所有利息
		public static final ConstantBean<Integer>   RETURN_MONEY1 = new ConstantBean<Integer>("归还本金", 1);
		public static final ConstantBean<Integer>   RETURN_MONEY2 = new ConstantBean<Integer>("归还本金及归还金额利息", 2);
		public static final ConstantBean<Integer>   RETURN_MONEY3 = new ConstantBean<Integer>("归还本金及所有利息", 3);
	}
	
	/**
	 * 是否生成指令 1：是;0：否
	 */
	public static class IsBisExc {
		public static final ConstantBean<String> BIS_EXC_NO = new ConstantBean<String>("否", "0");
		public static final ConstantBean<String> BIS_EXC_YES = new ConstantBean<String>("是", "1");
	}
	
	/**
	 * 贷款类型 1：流动资金贷款;2：票据借贷
	 */
	public static class LoanGenre {
		public static final ConstantBean<String> GENRE_CASH_LOAN = new ConstantBean<String>("流动资金贷款", "1");
		public static final ConstantBean<String> GENRE_BILL_LOAN = new ConstantBean<String>("票据借贷", "2");
	}
	
	/**
	 * 贷款还款时的支付方式 1：现汇支付;2：票据支付
	 */
	public static class CmsPayType {
		public static final ConstantBean<String> LOAN_REPAY_PAY_MONEY = new ConstantBean<String>("现汇支付", "1");
		public static final ConstantBean<String> LOAN_REPAY_PAY_RECEIPT = new ConstantBean<String>("票据支付", "2");
	}
	
	/**
	 * 贷款还款时的支付方式 1：现汇支付;2：票据支付
	 */
	public static class MessageType {
		public static final ConstantBean<Integer> LOG_TALLY_BACK = new ConstantBean<Integer>("记账打回", 27);
		public static final ConstantBean<Integer> LOG_MODIFY_BACK = new ConstantBean<Integer>("改账打回", 28);
		public static final ConstantBean<Integer> LOG_CHECK_BACK = new ConstantBean<Integer>("复核打回", 29);
		public static final ConstantBean<Integer> LOG_TALLY_TEMP = new ConstantBean<Integer>("记账暂存", 96);
		public static final ConstantBean<Integer> LOG_TALLY_SUBMIT = new ConstantBean<Integer>("记账提交", 97);
		public static final ConstantBean<Integer> LOG_MODIFY_SUBMIT = new ConstantBean<Integer>("改账提交", 98);
		public static final ConstantBean<Integer> LOG_MODIFY_TEMP = new ConstantBean<Integer>("改账暂存", 99);
		public static final ConstantBean<Integer> LOG_CHECK_PASS = new ConstantBean<Integer>("复核通过", 100);
	}
	/**
	 * 日志模块类型
	 */
	public static class LogType {
		public static final ConstantBean<Integer> LOG_TYPE_SH = new ConstantBean<Integer>("上划", 10);
		public static final ConstantBean<Integer> LOG_TYPE_XB = new ConstantBean<Integer>("下拨", 11);
		public static final ConstantBean<Integer> LOG_TYPE_QK = new ConstantBean<Integer>("请款", 12);
		public static final ConstantBean<Integer> LOG_TYPE_DB = new ConstantBean<Integer>("头寸调拨", 13);
	}
	/**
	 * 日志操作类型
	 */
	public static class NisLogMessageType {
		public static final ConstantBean<Integer> LOG_TYPE_TEMP = new ConstantBean<Integer>("暂存", 10);
		public static final ConstantBean<Integer> LOG_TYPE_MODIFY = new ConstantBean<Integer>("修改", 11);
		public static final ConstantBean<Integer> LOG_TYPE_DELETE = new ConstantBean<Integer>("删除", 12);
		public static final ConstantBean<Integer> LOG_TYPE_SUBMIT = new ConstantBean<Integer>("提交", 13);
		public static final ConstantBean<Integer> LOG_TYPE_CHECK = new ConstantBean<Integer>("审批", 14);
		public static final ConstantBean<Integer> LOG_TYPE_BACK = new ConstantBean<Integer>("打回", 15);
		public static final ConstantBean<Integer> LOG_TYPE_PASS = new ConstantBean<Integer>("审批通过", 16);
		public static final ConstantBean<Integer> LOG_TYPE_BACK_ONE = new ConstantBean<Integer>("审批撤回", 17);
		public static final ConstantBean<Integer> LOG_TYPE_JZ_ZC = new ConstantBean<Integer>("记账暂存", 96);
		public static final ConstantBean<Integer> LOG_TYPE_JZ_TJ = new ConstantBean<Integer>("记账提交", 97);
		public static final ConstantBean<Integer> LOG_TYPE_JZ_DH = new ConstantBean<Integer>("记账打回", 27);
		
		public static final ConstantBean<Integer> LOG_TYPE_GZ_ZC = new ConstantBean<Integer>("改账暂存", 99);
		public static final ConstantBean<Integer> LOG_TYPE_GZ_TJ = new ConstantBean<Integer>("改账提交", 98);
		public static final ConstantBean<Integer> LOG_TYPE_GZ_DH = new ConstantBean<Integer>("改账打回", 28);
		
		public static final ConstantBean<Integer> LOG_TYPE_FH_TG = new ConstantBean<Integer>("记账复核通过", 100);
		public static final ConstantBean<Integer> LOG_TYPE_FH_DH = new ConstantBean<Integer>("记账复核打回", 29);
   
		private final static Map<String, ConstantBean<Integer>> MESSAGE_TYPE_MAPPING = new HashMap<String, ConstantBean<Integer>>();
		static {
			MESSAGE_TYPE_MAPPING.put(LOG_TYPE_TEMP.get() + "", LOG_TYPE_TEMP);
			MESSAGE_TYPE_MAPPING.put(LOG_TYPE_MODIFY.get() + "", LOG_TYPE_MODIFY);
			MESSAGE_TYPE_MAPPING.put(LOG_TYPE_DELETE.get() + "", LOG_TYPE_DELETE);
			MESSAGE_TYPE_MAPPING.put(LOG_TYPE_SUBMIT.get() + "", LOG_TYPE_SUBMIT);
			MESSAGE_TYPE_MAPPING.put(LOG_TYPE_CHECK.get() + "", LOG_TYPE_CHECK);
			MESSAGE_TYPE_MAPPING.put(LOG_TYPE_BACK.get() + "", LOG_TYPE_BACK);
			MESSAGE_TYPE_MAPPING.put(LOG_TYPE_PASS.get() + "", LOG_TYPE_PASS);
			MESSAGE_TYPE_MAPPING.put(LOG_TYPE_BACK_ONE.get() + "", LOG_TYPE_BACK_ONE);
			
			MESSAGE_TYPE_MAPPING.put(LOG_TYPE_JZ_ZC.get() + "", LOG_TYPE_JZ_ZC);
			MESSAGE_TYPE_MAPPING.put(LOG_TYPE_JZ_TJ.get() + "", LOG_TYPE_JZ_TJ);
			MESSAGE_TYPE_MAPPING.put(LOG_TYPE_JZ_DH.get() + "", LOG_TYPE_JZ_DH);
			MESSAGE_TYPE_MAPPING.put(LOG_TYPE_GZ_ZC.get() + "", LOG_TYPE_GZ_ZC);
			MESSAGE_TYPE_MAPPING.put(LOG_TYPE_GZ_TJ.get() + "", LOG_TYPE_GZ_TJ);
			MESSAGE_TYPE_MAPPING.put(LOG_TYPE_GZ_DH.get() + "", LOG_TYPE_GZ_DH);
			MESSAGE_TYPE_MAPPING.put(LOG_TYPE_FH_TG.get() + "", LOG_TYPE_FH_TG);
			MESSAGE_TYPE_MAPPING.put(LOG_TYPE_FH_DH.get() + "", LOG_TYPE_FH_DH);
		}
		public static String getTypeName(Integer messageType) {
			return MESSAGE_TYPE_MAPPING.get(messageType + "").getName();
		}
	}
	
}
