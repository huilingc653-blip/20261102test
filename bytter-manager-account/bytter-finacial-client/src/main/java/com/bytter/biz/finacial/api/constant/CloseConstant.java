package com.bytter.biz.finacial.api.constant;
import com.bytter.core.constant.ConstantBean;
/**
 * 
 * 期终处理通用常量
 * @author lixi
 * @since 2011-8-21
 */
public class CloseConstant {
	/**
	 * 系统代码
	 */
	public static final String SYS_CODE = "cld";
	
	/**
	 *存款管理录入
	 */
	public static final String CLOSE_INPUT = "1";
	
	/**
	 *存款管理查询
	 */
	public static final String CLOSE_QUERY = "2";
	/**
	 * 日结状态-已日结
	 */
	public final static String DAY_STATUS = "已日结";
	/**
	 * 日结状态-反日结
	 */
	public final static String RESERVE_DAY_STATUS = "反日结";
	/**
	* 日结状态-未日结
	*/
	public final static String NO_DAY_STATUS = "未日结";
	/**
	 * 日结状态-已月结
	 */
	public final static String MONTH_STATUS = "已月结";
	/**
	 * 日结状态-反月结
	 */
	public final static String RESERVE_MONTH_STATUS = "反月结";
	/**
	 * 日结状态-未月结
	 */
	public final static String NO_MONTH_STATUS = "未月结";
	/**
	 * 日结参数
	 */
	public static final String PARAM_DAY_OVER = "day_over";
	/**
	 * 月结参数
	 */
	public static final String PARAM_MONTH_OVER = "month_over";
	/**
	 * 结转类型
	 */
	public static class CarryType {
		public static final ConstantBean<String> CARRY_TYPE_HANDLE_HARGE = new ConstantBean<String>("包括手续费结转", "H");
		public static final ConstantBean<String> CARRY_TYPE_TAX = new ConstantBean<String>("税金计提", "S");
		public static final ConstantBean<String> CARRY_TYPE_INCOME = new ConstantBean<String>("损益结转", "I");
		public static final ConstantBean<String> CARRY_TYPE_EXCHANGE = new ConstantBean<String>("汇兑损益结转", "E");
		public static final ConstantBean<String> CARRY_TYPE_DAY_OVER = new ConstantBean<String>("日终结转", "D");
		public static final ConstantBean<String> CARRY_TYPE_MONTH_OVER = new ConstantBean<String>("月终结转", "M");
		public static final ConstantBean<String> CARRY_TYPE_POOL_OVER = new ConstantBean<String>("资金池日终结转", "P");
		public static final ConstantBean<String> CARRY_TYPE_DAY_START = new ConstantBean<String>("日初结转", "S");
	}
	
	/**
	 * 计息预提标识-计息(已计息、已预提)
	 */
	public static final String INTEREST_SIGN_Y = "1";
	/**
	 * 计息预提标识-不计息(未计息、未预提)
	 */
	public static final String INTEREST_SIGN_N = "0";
	/**
	 * 积数协定标识,非协定
	 */
	public static final String AGREE_SIGN_NO = "0";
	/**
	 * 积数协定标识,协定
	 */
	public static final String AGREE_SIGN_YES = "1";
	/**
	 * 不满足协定积数的默认段
	 */
	public static final String DEFAULT_AGREE_TYPE = "0";
	/**
	 * 日结检查模块
	 */
	public static class DayCheckModel{
        public static final ConstantBean<String> CREDIT = new ConstantBean<String>("信贷业务","1");
        public static final ConstantBean<String> ACCEPTANCE_BILL = new ConstantBean<String>("承兑汇票","2");
        public static final ConstantBean<String> DEPOSIT = new ConstantBean<String>("存款业务","3");
        public static final ConstantBean<String> ACCOUNT = new ConstantBean<String>("账户管理","4");
        public static final ConstantBean<String> EBANK = new ConstantBean<String>("网银控制台","5");
        public static final ConstantBean<String> COUNTER = new ConstantBean<String>("账务处理","6");
        public static final ConstantBean<String> VOUCHER = new ConstantBean<String>("凭证管理","7");
        public static final ConstantBean<String> OUTER_COUNTER = new ConstantBean<String>("表外账务","8");
        public static final ConstantBean<String> INTEREST = new ConstantBean<String>("账务处理","9");
        public static final ConstantBean<String> INTEREST_IST = new ConstantBean<String>("利息管理","10");
    }
	/**
	 * 日结检查模块
	 */
	public static class DayCheckControlItem{
        public static final ConstantBean<String> ACCOUNT = new ConstantBean<String>("账户","CheckAccount");
        public static final ConstantBean<String> ACCOUNT_INITCHECK = new ConstantBean<String>("账户初始化复核","CheckAccountInit");
        public static final ConstantBean<String> FREEZE = new ConstantBean<String>("冻结或解冻","CheckFreeze");
        public static final ConstantBean<String> FREEZE_DEDUCT = new ConstantBean<String>("扣划","CheckFreezeDeduct");
        public static final ConstantBean<String> DEPOSIT_DRAW = new ConstantBean<String>("存取款","CheckDepositDraw");
        public static final ConstantBean<String> NOTICE_REGISTER = new ConstantBean<String>("取消通知信息登记","CheckNoticeRegister");
        public static final ConstantBean<String> FIXED_DEPOSIT = new ConstantBean<String>("质押或解除质押","CheckFixedDeposit");
        public static final ConstantBean<String> AGREED_DEPOSTI  = new ConstantBean<String>("协定存款","CheckAgreedDeposit");
        public static final ConstantBean<String> COUNTER_ACCOUNT = new ConstantBean<String>("账务处理","CheckCounterAccounts");
        public static final ConstantBean<String> COUNTER_OUTER_ACCOUNT = new ConstantBean<String>("表外账务","CheckCounterOuterAccounts");
        public static final ConstantBean<String> CLOSE_ACCOUNT = new ConstantBean<String>("销户","CheckCloseAccount");
        public static final ConstantBean<String> UINT_WITHDRAW = new ConstantBean<String>("单位请款","CheckUnitWithdraw");
        public static final ConstantBean<String> CENTER_WITHDRAW = new ConstantBean<String>("中心请款","CheckCenterWithdraw");
        public static final ConstantBean<String> ENTRUST_RECEIPT = new ConstantBean<String>("委托收款","CheckEntrustReceipt");
        public static final ConstantBean<String> BANK_INTERFACE_RECEIPT = new ConstantBean<String>("银行接口收款","CheckBankInterfaceReceipt");
        public static final ConstantBean<String> SALE_EXCHANGE = new ConstantBean<String>("结售汇","CheckSaleExchange");
        public static final ConstantBean<String> DEPOSIT_DRAW_WITH_NET = new ConstantBean<String>("存取款","CheckDepositDrawWithNet");
        public static final ConstantBean<String> BANK_LEND = new ConstantBean<String>("银行借款","CheckBankLend");
        public static final ConstantBean<String> INTER_BANK_IN = new ConstantBean<String>("同业拆借","CheckInterBankIn");
        public static final ConstantBean<String> INTRA_FAMILY_LOAN = new ConstantBean<String>("企业内部借款","CheckEnterpriseLoan");
        public static final ConstantBean<String> ENTRUST_LOAN = new ConstantBean<String>("委托贷款","CheckEntrustLoan");
        public static final ConstantBean<String> DEFERRED_DEAL = new ConstantBean<String>("定期到期处理","CheckDeferredDeal");
        public static final ConstantBean<String> INTEREST = new ConstantBean<String>("利息经办","CheckIstFcInterestDtl");
		public static final ConstantBean<String> INTEREST_LOAN_BOND = new ConstantBean<String>("贷款保证金利息","CheckFreezeBondIst");
    }
	
	/**
	 * 月结提醒
	 */
	public static class ClsMonthCheck{
        public static final ConstantBean<String> HANDING_CHARGE = new ConstantBean<String>("存在未入账的手续费","1");
        public static final ConstantBean<String> PCA_BALANCE_CHECK = new ConstantBean<String>("存在损益类的科目余额是否为0","2");
        public static final ConstantBean<String> NO_DAY_CLOSE = new ConstantBean<String>("月结的月份未做完日结不能做月终结转","3");
    }
	
}
