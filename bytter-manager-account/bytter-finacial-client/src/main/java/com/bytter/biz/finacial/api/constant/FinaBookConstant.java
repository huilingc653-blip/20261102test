package com.bytter.biz.finacial.api.constant;
import com.bytter.core.constant.ConstantBean;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
/**
 * 账务常量
 * @author lixi
 * @since 2011-8-21
 */
public class FinaBookConstant implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
     * 业务来源与产生方式的映射关系
     */
    private static final Map<Integer, String> ORI_TYPE_GENERATE_MODE_MAPPING = new HashMap<Integer, String>();
    /**
     * 业务来源模块
     */
	public static class GenerateMode {
        public final static ConstantBean<String> TAILLY = new ConstantBean<String>("手工记账", "T");
        public final static ConstantBean<String> HANDLE_CHARGE = new ConstantBean<String>("手续费结转", "H");
        public final static ConstantBean<String> DEPOSIT = new ConstantBean<String>("存款管理", "S");
        public final static ConstantBean<String> INTEREST = new ConstantBean<String>("利息单", "I");
        public final static ConstantBean<String> UNION_ACCBOOK = new ConstantBean<String>("联行来账处理", "N");
        public final static ConstantBean<String> RUSH_BOOK = new ConstantBean<String>("冲账", "R");
        public final static ConstantBean<String> EXCHANGE = new ConstantBean<String>("汇兑损益", "E");
        public final static ConstantBean<String> PROFIT_BOOK = new ConstantBean<String>("损益转本年利润", "P");
        public final static ConstantBean<String> TAX = new ConstantBean<String>("税金提取", "A");
        public final static ConstantBean<String> INNER_PAY_BOOK = new ConstantBean<String>("内网付款记账", "D");
        public final static ConstantBean<String> ENTRUST_COLLECT_BOOK = new ConstantBean<String>("委托收款记账", "G");
        public final static ConstantBean<String> CREDIT_BOOK = new ConstantBean<String>("信贷记账", "C");
        public final static ConstantBean<String> ACCEPTANCE_BOOK = new ConstantBean<String>("承兑汇票记账", "B");
        public final static ConstantBean<String> BANK_COLLECT_BOOK = new ConstantBean<String>("银企收款记账", "Y");
        public final static ConstantBean<String> BANK_POOL = new ConstantBean<String>("资金池", "Z");
        public final static ConstantBean<String> CHO_INNER_FINANCE = new ConstantBean<String>("理财内部存款", "W");

        public final static ConstantBean<String> BIS_ACC_DTL_OTHER_RECEIVE_BOOK = new ConstantBean<String>("总账户其他收款流水", "L");
        public final static ConstantBean<String> BIS_ACC_DTL_OTHER_PAY_BOOK = new ConstantBean<String>("总账户其他付款流水", "O");
        public final static ConstantBean<String> BIS_ACC_DTL_DOWN_BOOK = new ConstantBean<String>("总账户下拨流水", "X");
    }
    /**
     * 业务来源类型
     */
	public static class BusOriType {
        public final static ConstantBean<Integer> TAILLY = new ConstantBean<Integer>("手工记账", 10);
        public final static ConstantBean<Integer> HANDLE_CHARGE = new ConstantBean<Integer>("手续费结转", 11);
        public final static ConstantBean<Integer> CURRENT_DEDUCT = new ConstantBean<Integer>("扣划", 12);
        public final static ConstantBean<Integer> CURRENT_DORMANCY = new ConstantBean<Integer>("休眠", 13);
        public final static ConstantBean<Integer> FIX_DEPOSIT = new ConstantBean<Integer>("定期存入", 14);
        public final static ConstantBean<Integer> FIX_DRAW = new ConstantBean<Integer>("定期取款", 15);
        public final static ConstantBean<Integer> FIX_EXPIRES = new ConstantBean<Integer>("到期处理", 16);
        public final static ConstantBean<Integer> NOTIFY_DEPOSIT = new ConstantBean<Integer>("通知存入", 17);
        public final static ConstantBean<Integer> NOTIFY_DRAW = new ConstantBean<Integer>("通知取款", 18);
        public final static ConstantBean<Integer> CURRENT_MARGINS_DEPOSIT = new ConstantBean<Integer>("活期保证金存入", 19);
        public final static ConstantBean<Integer> CURRENT_MARGINS_DRAW = new ConstantBean<Integer>("活期保证金取款", 20);
        public final static ConstantBean<Integer> FIX_MARGINS_DEPOSIT = new ConstantBean<Integer>("定期保证金存入", 21);
        public final static ConstantBean<Integer> FIX_MARGINS_DRAW = new ConstantBean<Integer>("定期保证金取款", 22);
        public final static ConstantBean<Integer> RUSH = new ConstantBean<Integer>("冲账", 23);
        public final static ConstantBean<Integer> CANCEL_ACCOUNT = new ConstantBean<Integer>("销户", 24);
        public final static ConstantBean<Integer> NB_FIX_DEPOSIT = new ConstantBean<Integer>("网银定期存款", 25);
        public final static ConstantBean<Integer> NB_FIX_DRAW = new ConstantBean<Integer>("网银定期取款", 26);
        public final static ConstantBean<Integer> NB_NOTIFY_DEPOSIT = new ConstantBean<Integer>("网银通知存款", 27);
        public final static ConstantBean<Integer> NB_NOTIFY_DRAW = new ConstantBean<Integer>("网银通知取款", 28);
        public final static ConstantBean<Integer> NB_BANKDIRLINK_DEPOSIT = new ConstantBean<Integer>("网银银企直连收款", 29);
        public final static ConstantBean<Integer> NB_UNIT_COMMON_PAYMENT = new ConstantBean<Integer>("单位申请付款-一般付款", 30);
        public final static ConstantBean<Integer> NB_COMMISSION_DEPOSIT = new ConstantBean<Integer>("委托收款", 31);
        public final static ConstantBean<Integer> NB_UNIT_ASK_PAYMENT = new ConstantBean<Integer>("单位申请付款-请款", 32);
        public final static ConstantBean<Integer> NB_CNT_DOWN_PAYMENT = new ConstantBean<Integer>("中心申请付款-下拨", 33);
        public final static ConstantBean<Integer> NB_CNT_POS_PAYMENT = new ConstantBean<Integer>("中心申请付款-头寸调拨", 34);
        public final static ConstantBean<Integer> NB_BANK_LOAN = new ConstantBean<Integer>("银行借款-借款", 35);
        public final static ConstantBean<Integer> NB_BANK_REPAYMENT = new ConstantBean<Integer>("银行借款-线下还款", 36);
        public final static ConstantBean<Integer> NB_BANK_REPAYMENT_ONLINE = new ConstantBean<Integer>("银行借款-线上还款", 55);
        public final static ConstantBean<Integer> NB_BANK_REPAYMENT_ONLINE_OK = new ConstantBean<Integer>("银行借款-线上还款确认扣款", 56);
        public final static ConstantBean<Integer> NB_ENTERPRISE_LOAN = new ConstantBean<Integer>("企业贷款-放款", 37);
        public final static ConstantBean<Integer> NB_ENTERPRISE_REPAYMENT = new ConstantBean<Integer>("企业贷款-还款", 38);
        public static final ConstantBean<Integer> NB_ENTERPRISE_LOAN_NET = new ConstantBean<Integer>("网点借款", 39);
        public static final ConstantBean<Integer> NB_ENTERPRISE_REPAYMENT_NET = new ConstantBean<Integer>("网点还款", 40);
        public static final ConstantBean<Integer> NB_SALE_EXCHANGE = new ConstantBean<Integer>("结售汇", 41);
        public static final ConstantBean<Integer> NB_INTER_BANK_IN = new ConstantBean<Integer>("同业拆入-拆入", 42);
        public static final ConstantBean<Integer> NB_INTER_BANK_IN_REPAY = new ConstantBean<Integer>("同业拆入-归还", 43);
        public static final ConstantBean<Integer> NB_INTER_BANK_OUT = new ConstantBean<Integer>("同业拆出-拆出", 44);
        public static final ConstantBean<Integer> NB_INTER_BANK_OUT_REPAY = new ConstantBean<Integer>("同业拆出-归还", 45);
        public final static ConstantBean<Integer> NB_PRECATIVE_LOAN = new ConstantBean<Integer>("委托贷款-贷款", 46);
        public final static ConstantBean<Integer> NB_PRECATIVE_REPAYMENT = new ConstantBean<Integer>("委托贷款-还款", 47);
        public final static ConstantBean<Integer> NB_CNT_POS_CENTPAY = new ConstantBean<Integer>("中心申请付款(一般)", 48);
        public final static ConstantBean<Integer> NB_COLLECTION_NET = new ConstantBean<Integer>("网点间来账", 49);
        public final static ConstantBean<Integer> FINAL_CARRYOVER_EXCHANGE = new ConstantBean<Integer>("汇兑损益", 50);
        public final static ConstantBean<Integer> FINAL_CARRYOVER_TAX = new ConstantBean<Integer>("税金提取", 51);
        public final static ConstantBean<Integer> FINAL_CARRYOVER_PROFIT_BOOK = new ConstantBean<Integer>("损益转本年利润", 52);
        public final static ConstantBean<Integer> IST_INTEREST_BOOK = new ConstantBean<Integer>("利息管理", 53);
        public final static ConstantBean<Integer> NB_COLLECT_CANCEL = new ConstantBean<Integer>("收款取票核销", 54);
        public final static ConstantBean<Integer> IST_INTEREST_CURRENT = new ConstantBean<Integer>("个别计息-企业活期存款", 55);
        public final static ConstantBean<Integer> IST_INTEREST_FIX = new ConstantBean<Integer>("个别计息-企业定期存款", 56);
        public final static ConstantBean<Integer> IST_INTEREST_NOTIFY = new ConstantBean<Integer>("个别计息-企业通知存款", 57);
        public final static ConstantBean<Integer> IST_INTEREST_LOAN = new ConstantBean<Integer>("个别计息-内部贷款", 58);
        public final static ConstantBean<Integer> NB_BILL_LOAN = new ConstantBean<Integer>("票据借款", 78);
        public final static ConstantBean<Integer> NB_BILL_REPAY = new ConstantBean<Integer>("票据还款", 79);
        public static final ConstantBean<Integer> NB_BILL_CORP_IN_NET = new ConstantBean<Integer>("单位入库中心-入库", 80);
        public static final ConstantBean<Integer> NB_BILL_CORP_MOVE_NET = new ConstantBean<Integer>("单位移存中心-移存", 81);
        public static final ConstantBean<Integer> NB_BILL_CORP_CENTER_MOVE_BANK = new ConstantBean<Integer>("中心移存银行-移存", 82);
        public static final ConstantBean<Integer> NB_BILL_CORP_GET = new ConstantBean<Integer>("单位票据取回-取票", 83);
        public static final ConstantBean<Integer> NB_BILL_BANK_TO_NET_GET = new ConstantBean<Integer>("银行取回中心-取票", 109);
        public static final ConstantBean<Integer> NB_BILL_CORP_CENTER_COLLECTION = new ConstantBean<Integer>("中心代管-托收在途",84);
        public static final ConstantBean<Integer> NB_BILL_CORP_CENTER_COLLECTION_DAOZHANG = new ConstantBean<Integer>("中心代管-托收到账", 85);
        public static final ConstantBean<Integer> NB_BILL_CORP_CENTER_IN_ENDORSEMENT = new ConstantBean<Integer>("中心代管-网点内背书", 86);
        public static final ConstantBean<Integer> NB_BILL_CORP_CENTER_OUT_ENDORSEMENT = new ConstantBean<Integer>("中心代管-网点外背书", 87);
        public static final ConstantBean<Integer> NB_BILL_CORP_CENTER_PLEDGE_NET = new ConstantBean<Integer>("中心代管-质押中心", 88);
        public static final ConstantBean<Integer> NB_BILL_CORP_CENTER_PLEDGE_NETGET = new ConstantBean<Integer>("中心代管-质押收回", 89);
        public static final ConstantBean<Integer> NB_BILL_CORP_PLEDGE_NET = new ConstantBean<Integer>("单位库存-质押中心", 90);
        public static final ConstantBean<Integer> NB_BILL_CORP_PLEDGE_NETGET = new ConstantBean<Integer>("单位库存-质押收回", 91);
        public static final ConstantBean<Integer> NB_BILL_CORP_DISCOUNT_NET_MD = new ConstantBean<Integer>("单位库贴中心-贴现(买断式)", 92);
        public static final ConstantBean<Integer> NB_BILL_CORP_DISCOUNT_NET_SH = new ConstantBean<Integer>("单位库贴中心-贴现(赎回式)", 93);
        public static final ConstantBean<Integer> NB_BILL_CORP_DISCOUNT_NET_GET = new ConstantBean<Integer>("单位库贴中心-贴现赎回", 94);
        public static final ConstantBean<Integer> NB_BILL_CORP_CENTER_DISCOUNT_NET_MD = new ConstantBean<Integer>("中心代管贴中心-贴现(买断式)", 95);
        public static final ConstantBean<Integer> NB_BILL_CORP_CENTER_DISCOUNT_NET_SH = new ConstantBean<Integer>("中心代管贴中心-贴现(赎回式)", 96);
        public static final ConstantBean<Integer> NB_BILL_CORP_CENTER_DISCOUNT_NET_GET = new ConstantBean<Integer>("中心代管贴中心-贴现赎回", 97);
        public static final ConstantBean<Integer> NB_BILL_CORP_CENTER_BACEBILL = new ConstantBean<Integer>("中心代管-退票", 98);
        public static final ConstantBean<Integer> NB_BILL_DELIVER = new ConstantBean<>("划拨记账-上划", 99);
        public static final ConstantBean<Integer> NB_BILL_ALLOCATE = new ConstantBean<>("划拨记账-下拨", 100);
        public static final ConstantBean<Integer> NB_BILL_NET_COLLECTION = new ConstantBean<>("中心自办-托收在途(来源库存)", 101);
        public static final ConstantBean<Integer> NB_BILL_NET_COLLECTION_DAOZHANG = new ConstantBean<Integer>("中心自办-托收到账", 102);
        public static final ConstantBean<Integer> NB_BILL_NET_DISCOUNT_BANK = new ConstantBean<>("中心自办银行贴现-贴现在途(来源库存)", 103);
        public static final ConstantBean<Integer> NB_BILL_NET_DISCOUNT_BANK_DAOZHANG = new ConstantBean<>("中心自办银行贴现-贴现到账", 104);
        public static final ConstantBean<Integer> NB_BILL_NET_DISCOUNT_BANK_GET = new ConstantBean<>("中心自办银行贴现-贴现赎回(来源库存)", 105);
        public static final ConstantBean<Integer> NB_BILL_NET_COLLECTION_TX = new ConstantBean<>("中心自办-托收在途(来源贴现)", 106);
        public static final ConstantBean<Integer> NB_BILL_NET_DISCOUNT_BANK_TX = new ConstantBean<>("中心自办银行贴现-贴现在途(来源贴现)", 107);
        public static final ConstantBean<Integer> NB_BILL_NET_DISCOUNT_BANK_GET_TX = new ConstantBean<>("中心自办银行贴现-贴现赎回(来源贴现)", 108);
        public static final ConstantBean<Integer> NB_POOL_DOWN_PAYMENT = new ConstantBean<>("资金池-下拨", 110);
        public static final ConstantBean<Integer> NB_POOL_UP_PAYMENT = new ConstantBean<>("资金池-归集", 111);
        public static final ConstantBean<Integer> NB_POOL_INTEREST = new ConstantBean<>("资金池-利息核算", 112);
        public static final ConstantBean<Integer> CH_DEPOSIT_CURRENT_TO_REG_PAYMENT = new ConstantBean<>("活期转定期 ", 113);
        public static final ConstantBean<Integer> CH_DEPOSIT_REG_TO_CURRENT_PAYMENT = new ConstantBean<>("定期转活期 ", 114);
        public static final ConstantBean<Integer> CH_DEPOSIT_INFORM_TO_CURRENT_PAYMENT = new ConstantBean<>("通知转活期", 116);
        public static final ConstantBean<Integer> CH_DEPOSIT_CURRENT_TO_INFORM_PAYMENT = new ConstantBean<>("活期转通知", 115);

        public final static ConstantBean<Integer> BIS_ACC_DTL_OTHER_RECEIVE = new ConstantBean<Integer>("总账户其他收款流水", 130);
        public final static ConstantBean<Integer> BIS_ACC_DTL_OTHER_PAY = new ConstantBean<Integer>("总账户其他付款流水", 131);
        public final static ConstantBean<Integer> BIS_ACC_DTL_DOWN = new ConstantBean<Integer>("总账户下拨流水", 132);
    }
    /**
     * 业务来源类型对应业务来源模块
     */
	static {
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.TAILLY.get(), GenerateMode.TAILLY.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.RUSH.get(), GenerateMode.RUSH_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.HANDLE_CHARGE.get(), GenerateMode.HANDLE_CHARGE.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.CURRENT_DEDUCT.get(), GenerateMode.DEPOSIT.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.CURRENT_DORMANCY.get(), GenerateMode.DEPOSIT.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.FIX_DEPOSIT.get(), GenerateMode.DEPOSIT.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.FIX_DRAW.get(), GenerateMode.DEPOSIT.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.FIX_EXPIRES.get(), GenerateMode.DEPOSIT.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NOTIFY_DEPOSIT.get(), GenerateMode.DEPOSIT.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NOTIFY_DRAW.get(), GenerateMode.DEPOSIT.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.CURRENT_MARGINS_DEPOSIT.get(), GenerateMode.DEPOSIT.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.CURRENT_MARGINS_DRAW.get(), GenerateMode.DEPOSIT.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.FIX_MARGINS_DEPOSIT.get(), GenerateMode.DEPOSIT.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.FIX_MARGINS_DRAW.get(), GenerateMode.DEPOSIT.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.CANCEL_ACCOUNT.get(), GenerateMode.DEPOSIT.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.FINAL_CARRYOVER_EXCHANGE.get(), GenerateMode.EXCHANGE.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.FINAL_CARRYOVER_PROFIT_BOOK.get(), GenerateMode.PROFIT_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.FINAL_CARRYOVER_TAX.get(), GenerateMode.TAX.get());
        //TODO 需要确定网银控制台的GENERATE_MODE
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_FIX_DEPOSIT.get(), GenerateMode.DEPOSIT.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_FIX_DRAW.get(), GenerateMode.DEPOSIT.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_NOTIFY_DEPOSIT.get(), GenerateMode.DEPOSIT.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_NOTIFY_DRAW.get(), GenerateMode.DEPOSIT.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BANKDIRLINK_DEPOSIT.get(), GenerateMode.BANK_COLLECT_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_UNIT_COMMON_PAYMENT.get(), GenerateMode.INNER_PAY_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_COMMISSION_DEPOSIT.get(), GenerateMode.ENTRUST_COLLECT_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_UNIT_ASK_PAYMENT.get(), GenerateMode.INNER_PAY_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_CNT_DOWN_PAYMENT.get(), GenerateMode.INNER_PAY_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_CNT_POS_PAYMENT.get(), GenerateMode.INNER_PAY_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BANK_LOAN.get(), GenerateMode.CREDIT_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BANK_REPAYMENT.get(), GenerateMode.CREDIT_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BANK_REPAYMENT_ONLINE.get(), GenerateMode.CREDIT_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BANK_REPAYMENT_ONLINE_OK.get(), GenerateMode.CREDIT_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_ENTERPRISE_LOAN.get(), GenerateMode.CREDIT_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_ENTERPRISE_REPAYMENT.get(), GenerateMode.CREDIT_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_ENTERPRISE_LOAN_NET.get(), GenerateMode.CREDIT_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_ENTERPRISE_REPAYMENT_NET.get(), GenerateMode.CREDIT_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_SALE_EXCHANGE.get(), GenerateMode.INNER_PAY_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_INTER_BANK_IN.get(), GenerateMode.CREDIT_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_INTER_BANK_IN_REPAY.get(), GenerateMode.CREDIT_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_INTER_BANK_OUT.get(), GenerateMode.CREDIT_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_INTER_BANK_OUT_REPAY.get(), GenerateMode.CREDIT_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_PRECATIVE_LOAN.get(), GenerateMode.CREDIT_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_PRECATIVE_REPAYMENT.get(), GenerateMode.CREDIT_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_CNT_POS_CENTPAY.get(), GenerateMode.INNER_PAY_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_COLLECTION_NET.get(), GenerateMode.UNION_ACCBOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.IST_INTEREST_BOOK.get(), GenerateMode.INTEREST.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.IST_INTEREST_CURRENT.get(), GenerateMode.INTEREST.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.IST_INTEREST_FIX.get(), GenerateMode.INTEREST.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.IST_INTEREST_NOTIFY.get(), GenerateMode.INTEREST.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.IST_INTEREST_LOAN.get(), GenerateMode.INTEREST.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_COLLECT_CANCEL.get(), GenerateMode.TAILLY.get());
        /**票据库存管理*/
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_CORP_IN_NET.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_CORP_MOVE_NET.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_CORP_CENTER_MOVE_BANK.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_CORP_GET.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_BANK_TO_NET_GET.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        /**承兑汇票贴现*/
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_CORP_DISCOUNT_NET_MD.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_CORP_DISCOUNT_NET_SH.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_CORP_DISCOUNT_NET_GET.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_CORP_CENTER_DISCOUNT_NET_MD.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_CORP_CENTER_DISCOUNT_NET_SH.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_CORP_CENTER_DISCOUNT_NET_GET.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_NET_DISCOUNT_BANK.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_NET_DISCOUNT_BANK_DAOZHANG.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_NET_DISCOUNT_BANK_GET.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_NET_COLLECTION_TX.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_NET_DISCOUNT_BANK_TX.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_NET_DISCOUNT_BANK_GET_TX.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        /**承兑汇票账务托收*/
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_CORP_CENTER_COLLECTION.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_CORP_CENTER_COLLECTION_DAOZHANG.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_NET_COLLECTION.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_NET_COLLECTION_DAOZHANG.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        /**承兑汇票账务质押*/
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_CORP_CENTER_PLEDGE_NET.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_CORP_CENTER_PLEDGE_NETGET.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_CORP_PLEDGE_NET.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_CORP_PLEDGE_NETGET.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        /**承兑汇票 背书*/
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_CORP_CENTER_IN_ENDORSEMENT.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_CORP_CENTER_OUT_ENDORSEMENT.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        /**承兑汇票划拨记账*/
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_DELIVER.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_ALLOCATE.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        /**承兑汇票退票*/
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_BILL_CORP_CENTER_BACEBILL.get(), GenerateMode.ACCEPTANCE_BOOK.get());
        /**资金池归集*/
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_POOL_UP_PAYMENT.get(), GenerateMode.BANK_POOL.get());
        /**资金池下拨*/
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_POOL_DOWN_PAYMENT.get(), GenerateMode.BANK_POOL.get());
        /**资金池计息*/
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.NB_POOL_INTEREST.get(), GenerateMode.BANK_POOL.get());
        /**网银控制台活期转定期*/
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.CH_DEPOSIT_CURRENT_TO_REG_PAYMENT.get(), GenerateMode.CHO_INNER_FINANCE.get());
        /**网银控制台定期转活期*/
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.CH_DEPOSIT_REG_TO_CURRENT_PAYMENT.get(), GenerateMode.CHO_INNER_FINANCE.get());
        /**网银控制台通知转活期*/
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.CH_DEPOSIT_INFORM_TO_CURRENT_PAYMENT.get(), GenerateMode.CHO_INNER_FINANCE.get());
        /**网银控制台活期转通知*/
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.CH_DEPOSIT_CURRENT_TO_INFORM_PAYMENT.get(), GenerateMode.CHO_INNER_FINANCE.get());

        /**总账户其他收款流水*/
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.BIS_ACC_DTL_OTHER_RECEIVE.get(), GenerateMode.BIS_ACC_DTL_OTHER_RECEIVE_BOOK.get());
        /**总账户其他付款流水*/
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.BIS_ACC_DTL_OTHER_PAY.get(), GenerateMode.BIS_ACC_DTL_OTHER_PAY_BOOK.get());
        /**总账户下拨流水*/
        ORI_TYPE_GENERATE_MODE_MAPPING.put(BusOriType.BIS_ACC_DTL_DOWN.get(), GenerateMode.BIS_ACC_DTL_DOWN_BOOK.get());

    }
    /**内部网上银行系统
     */
    private static final Map<Integer, String> entityMap = new HashMap<Integer, String>();
    static{
        synchronized (entityMap) {
    			/*内部网上银行系统*/
            // 单位申请付款-一般付款 Code : 30
            entityMap.put(BusOriType.NB_UNIT_COMMON_PAYMENT.get(), "nis_billhead");
            // 单位申请付款-请款(资金调拨) Code : 32
            entityMap.put(BusOriType.NB_UNIT_ASK_PAYMENT.get(), "nis_allocation_req");
            // 中心申请付款-下拨 Code : 33
            entityMap.put(BusOriType.NB_CNT_DOWN_PAYMENT.get(), "nis_allocation_req");
            // 中心申请付款-头寸调拨 Code : 34
            entityMap.put(BusOriType.NB_CNT_POS_PAYMENT.get(), "nis_allocation_req");
            // 网银银企直连收款 Code : 29
            entityMap.put(BusOriType.NB_BANKDIRLINK_DEPOSIT.get(), "bt_net_chalk");

            // 总账户其他收款流水 Code : 130
            entityMap.put(BusOriType.BIS_ACC_DTL_OTHER_RECEIVE.get(), "bt_net_chalk");
            // 总账户其他付款流水 Code : 131
            entityMap.put(BusOriType.BIS_ACC_DTL_OTHER_PAY.get(), "bis_acc_dtl");
            // 总账户下拨流水 Code : 132
            entityMap.put(BusOriType.BIS_ACC_DTL_DOWN.get(), "bis_acc_dtl");
        }
    }
    /**
     * 根据业务来源获取记账业务表。
     * @param oriType 业务来源类型
     * @return 业务模块
     */
    public static String getBusTableByOriType(Integer oriType) {
        return entityMap.get(oriType);
    }
    /**
     * 根据业务来源获取产生方式。
     * @param oriType 业务来源类型
     * @return 业务来源
     */
    public static String getGenerateModeByOriType(Integer oriType) {
        return ORI_TYPE_GENERATE_MODE_MAPPING.get(oriType);
    }
    /**
     * 业务规则定义(系统业务品种)
     */
    public static class SysRaceCode {
        public static final ConstantBean<String> DPS_FIX_DRAW = new ConstantBean<String>("定期转活期", "1001");
        public static final ConstantBean<String> DPS_NOTIFY_DRAW = new ConstantBean<String>("通知转活期", "1002");
        public static final ConstantBean<String> DPS_DORMANCY_DRAW = new ConstantBean<String>("休眠", "1003");
        public static final ConstantBean<String> DPS_CURRENT_MARGINS_DRAW = new ConstantBean<String>("活期保证金转活期", "1004");
        public static final ConstantBean<String> DPS_FIX_MARGINS_DRAW = new ConstantBean<String>("定期保证金转活期", "1005");
        public static final ConstantBean<String> DPS_DEDUCT_DRAW = new ConstantBean<String>("扣划", "1006");
        public static final ConstantBean<String> DPS_ACTIVE_FIX_ASSURE = new ConstantBean<String>("活期转定期保证金", "1007");
        public static final ConstantBean<String> DPS_ACTIVE_ACTIVE_ASSURE = new ConstantBean<String>("活期转活期保证金", "1008");
        public static final ConstantBean<String> DPS_FIX_DEFERRED = new ConstantBean<String>("定期延期处理", "1009");
        public static final ConstantBean<String> DPS_ACTIVE_FIX = new ConstantBean<String>("活期转定期存款", "1010");
        public static final ConstantBean<String> DPS_ACTIVE_CALL = new ConstantBean<String>("活期转通知存款", "1011");
        public static final ConstantBean<String> IST_INTEREST_CURRENT = new ConstantBean<String>("个别计息-企业活期存款", "1012");
        public static final ConstantBean<String> IST_INTEREST_FIX = new ConstantBean<String>("个别计息-企业定期存款", "1013");
        public static final ConstantBean<String> IST_INTEREST_NOTIFY = new ConstantBean<String>("个别计息-企业通知存款", "1014");
        public static final ConstantBean<String> IST_INTEREST_LOAN = new ConstantBean<String>("个别计息-内部贷款", "1015");
        public static final ConstantBean<String> NB_FIX_DRAW = new ConstantBean<String>("网银控制台定期转活期", "2001");
        public static final ConstantBean<String> NB_NOTIFY_DRAW = new ConstantBean<String>("网银控制台通知转活期", "2002");
        public static final ConstantBean<String> NB_UNIT_COMMON_PAYMENT_SAME = new ConstantBean<String>("单位申请付款-一般付款-内转同网点", "2003");
        public static final ConstantBean<String> NB_UNIT_COMMON_PAYMENT_DIFF = new ConstantBean<String>("单位申请付款-一般付款-内转不同网点", "2004");
        public static final ConstantBean<String> NB_UNIT_COMMON_PAYMENT_OUT = new ConstantBean<String>("单位申请付款-一般付款-对外付款", "2005");
        public static final ConstantBean<String> NB_FIX_DEPOSIT = new ConstantBean<String>("网银控制台活期转定期", "2006");
        public static final ConstantBean<String> NB_NOTICE_DEPOSIT = new ConstantBean<String>("网银控制台活期转通知", "2007");
        public static final ConstantBean<String> NB_CGS_BILL = new ConstantBean<String>("网银控制台委托收款", "2008");
        public static final ConstantBean<String> NB_BANK_COLLECT = new ConstantBean<String>("银企接口收款", "2009");
        public static final ConstantBean<String> NB_ENTERPRISE_LOAN = new ConstantBean<String>("企业贷款放款", "2010");
        public static final ConstantBean<String> NB_ENTERPRISE_REPAYMENT = new ConstantBean<String>("企业贷款还款", "2011");
        public static final ConstantBean<String> NB_ENTERPRISE_LOAN_NET = new ConstantBean<String>("网点借款", "2012");
        public static final ConstantBean<String> NB_ENTERPRISE_REPAYMENT_NET = new ConstantBean<String>("网点还款", "2013");
        public static final ConstantBean<String> NB_SALE_EXCHANGE = new ConstantBean<String>("结售汇", "2014");
        public static final ConstantBean<String> NB_PRECATIVE_LOAN = new ConstantBean<String>("委托贷款", "2015");
        public static final ConstantBean<String> NB_PRECATIVE_REPAYMENT = new ConstantBean<String>("委托贷款还款", "2016");
        public static final ConstantBean<String> NB_COLLECTION_NET = new ConstantBean<String>("网点间来账", "2017");
        public static final ConstantBean<String> NB_CNT_POS_CENTPAY = new ConstantBean<String>("中心申请付款(一般)", "2018");
        public static final ConstantBean<String> NB_UNIT_ASK_PAYMENT = new ConstantBean<String>("单位申请付款(请款)", "2019");
        public static final ConstantBean<String> NB_CNT_DOWN_PAYMENT = new ConstantBean<String>("中心申请付款(下拨)", "2020");
        public static final ConstantBean<String> NB_CNT_POS_PAYMENT = new ConstantBean<String>("中心申请付款(头寸调拨)", "2021");
        public static final ConstantBean<String> NB_BANK_LOAN = new ConstantBean<String>("银行借款-借款", "2022");
        public static final ConstantBean<String> NB_BANK_REPAYMENT = new ConstantBean<String>("银行借款-线下还款", "2023");
        public static final ConstantBean<String> NB_BANK_REPAYMENT_ONLINE = new ConstantBean<String>("银行借款-线上还款", "2035");
        public static final ConstantBean<String> NB_BANK_REPAYMENT_ONLINE_OK = new ConstantBean<String>("银行借款-线上还款确认扣款", "2036");
        public static final ConstantBean<String> NB_INTER_BANK_IN = new ConstantBean<String>("同业拆入(拆入)", "2024");
        public static final ConstantBean<String> NB_INTER_BANK_IN_REPAY = new ConstantBean<String>("同业拆入(归还)", "2025");
        public static final ConstantBean<String> NB_INTER_BANK_OUT = new ConstantBean<String>("同业拆出(拆出)", "2026");
        public static final ConstantBean<String> NB_INTER_BANK_OUT_REPAY = new ConstantBean<String>("同业拆出(归还)", "2027");
        public static final ConstantBean<String> NB_FEX_SETT = new ConstantBean<String>("代客结汇", "2028");
        public static final ConstantBean<String> NB_FEX_SALT = new ConstantBean<String>("代客购汇", "2029");
        public static final ConstantBean<String> NB_SEL_SETT = new ConstantBean<String>("自营结汇", "2032");
        public static final ConstantBean<String> NB_SEL_SALT = new ConstantBean<String>("自营购汇", "2033");
        public static final ConstantBean<String> NB_COLLECT_CANCEL = new ConstantBean<String>("收款取票核销", "2030");
        public static final ConstantBean<String> NB_CHARGE_ACCRUE = new ConstantBean<String>("手续费计提", "2031");
        public static final ConstantBean<String> NB_BANK_PAY = new ConstantBean<String>("银企接口付款", "2034");
        public static final ConstantBean<String> CANCEL_ACCOUNT = new ConstantBean<String>("账户注销", "3001");
        public static final ConstantBean<String> TAX_PROVISION = new ConstantBean<String>("税金计提", "3002");
        public static final ConstantBean<String> IST_INTEREST = new ConstantBean<String>("利息", "3003");
        public static final ConstantBean<String> CARRYOVER_EXCHANGE = new ConstantBean<String>("汇兑损益", "3004");
        public static final ConstantBean<String> CARRYOVER_PROFIT_BOOK_INCOME = new ConstantBean<String>("损益转本年利润-收入类", "3005");
        public static final ConstantBean<String> CARRYOVER_PROFIT_BOOK_PAYOUT = new ConstantBean<String>("损益转本年利润-支出类", "3006");
        public static final ConstantBean<String> NB_BILL_CORP_IN_NET = new ConstantBean<String>("单位入库中心-入库", "4001");
        public static final ConstantBean<String> NB_BILL_CORP_MOVE_NET = new ConstantBean<String>("单位移存中心-移存", "4002");
        public static final ConstantBean<String> NB_BILL_CORP_CENTER_MOVE_BANK = new ConstantBean<String>("中心移存银行-移存", "4003");
        public static final ConstantBean<String> NB_BILL_CORP_GET = new ConstantBean<String>("单位票据取回-取票", "4004");
        public static final ConstantBean<String> NB_BILL_BANK_TO_NET_GET = new ConstantBean<String>("银行取回中心-取票", "4204");
        public static final ConstantBean<String> NB_BILL_CORP_CENTER_COLLECTION = new ConstantBean<String>("中心代管-托收在途", "4005");
        public static final ConstantBean<String> NB_BILL_CORP_CENTER_COLLECTION_DAOZHANG = new ConstantBean<String>("中心代管-托收到账", "4006");
        public static final ConstantBean<String> NB_BILL_CORP_CENTER_IN_ENDORSEMENT = new ConstantBean<String>("中心代管-对内背书", "4007");
        public static final ConstantBean<String> NB_BILL_CORP_CENTER_OUT_ENDORSEMENT = new ConstantBean<String>("中心代管-对外背书", "4008");
        public static final ConstantBean<String> NB_BILL_CORP_CENTER_PLEDGE_NET = new ConstantBean<String>("中心代管-质押中心", "4009");
        public static final ConstantBean<String> NB_BILL_CORP_CENTER_PLEDGE_NETGET = new ConstantBean<String>("中心代管-质押收回", "4010");
        public static final ConstantBean<String> NB_BILL_CORP_PLEDGE_NET = new ConstantBean<String>("单位库存-质押中心", "4011");
        public static final ConstantBean<String> NB_BILL_CORP_PLEDGE_NETGET = new ConstantBean<String>("单位库存-质押收回", "4012");
        public static final ConstantBean<String> NB_BILL_CORP_DISCOUNT_NET_MD = new ConstantBean<String>("单位库贴中心-贴现(买断式)", "4013");
        public static final ConstantBean<String> NB_BILL_CORP_DISCOUNT_NET_SH = new ConstantBean<String>("单位库贴中心-贴现(赎回式)", "4014");
        public static final ConstantBean<String> NB_BILL_CORP_DISCOUNT_NET_GET = new ConstantBean<String>("单位库贴中心-贴现赎回", "4016");
        public static final ConstantBean<String> NB_BILL_CORP_CENTER_DISCOUNT_NET_MD = new ConstantBean<String>("中心代管贴中心-贴现(买断式)", "4017");
        public static final ConstantBean<String> NB_BILL_CORP_CENTER_DISCOUNT_NET_SH = new ConstantBean<String>("中心代管贴中心-贴现(赎回式)", "4018");
        public static final ConstantBean<String> NB_BILL_CORP_CENTER_DISCOUNT_NET_GET = new ConstantBean<String>("中心代管贴中心-贴现赎回", "4019");
        public static final ConstantBean<String> NB_BILL_CORP_CENTER_BACEBILL = new ConstantBean<String>("中心代管-退票", "4020");
        public static final ConstantBean<String> NB_BILL_DELIVER = new ConstantBean<String>("划拨记账-上划", "4021");
        public static final ConstantBean<String> NB_BILL_ALLOCATE = new ConstantBean<String>("划拨记账-下拨", "4022");
        public static final ConstantBean<String> NB_BILL_NET_COLLECTION = new ConstantBean<String>("中心自办-托收在途(来源库存)", "4105");
        public static final ConstantBean<String> NB_BILL_NET_COLLECTION_DAOZHANG = new ConstantBean<String>("中心自办-托收到账", "4106");
        public static final ConstantBean<String> NB_BILL_NET_DISCOUNT_BANK = new ConstantBean<String>("中心自办银行贴现-贴现在途(来源库存)", "4113");
        public static final ConstantBean<String> NB_BILL_NET_DISCOUNT_BANK_DAOZHANG = new ConstantBean<String>("中心自办银行贴现-贴现到账", "4115");
        public static final ConstantBean<String> NB_BILL_NET_DISCOUNT_BANK_GET = new ConstantBean<String>("中心自办银行贴现-贴现赎回(来源库存)", "4116");
        public static final ConstantBean<String> NB_BILL_NET_COLLECTION_TX = new ConstantBean<String>("中心自办-托收在途(来源贴现)", "4605");
        public static final ConstantBean<String> NB_BILL_NET_DISCOUNT_BANK_TX = new ConstantBean<String>("中心自办银行贴现-贴现在途(来源贴现)", "4613");
        public static final ConstantBean<String> NB_BILL_NET_DISCOUNT_BANK_GET_TX = new ConstantBean<String>("中心自办银行贴现-贴现赎回(来源贴现)", "4616");
        public static final ConstantBean<String> NB_BILL_LOAN = new ConstantBean<String>("票据借款", "4040");
        public static final ConstantBean<String> NB_BILL_REPAY = new ConstantBean<String>("票据还款", "4041");
        public static final ConstantBean<String> NB_POOL_UP_PAY = new ConstantBean<String>("资金池归集", "5001");
        public static final ConstantBean<String> NB_POOL_DOWN_PAY = new ConstantBean<String>("资金池下拨", "5002");
        public static final ConstantBean<String> NB_POOL_INTEREST = new ConstantBean<String>("资金池利息核算", "5003");
        public static final ConstantBean<String> NB_OTHER = new ConstantBean<String>("其它", "9999");

        public static final ConstantBean<String> NB_BIS_ACC_DTL_COLLECT = new ConstantBean<String>("网络记账-下拨", "6001");

    }
    /**
     * 凭证状态
     */
    public static class VoucherStatus {
        /**记账-冲账打回*/
        public final static ConstantBean<String> STATUS_BACK = new ConstantBean<String>("打回", "F");
        /**记账-冲账暂存*/
        public final static ConstantBean<String> STATUS_TEMP = new ConstantBean<String>("暂存", "T");
        /**记账-冲账待复核*/
        public final static ConstantBean<String> STATUS_WAIT_CHECK = new ConstantBean<String>("待复核", "C");
        /**记账-冲账复核*/
        public final static ConstantBean<String> STATUS_CHECK = new ConstantBean<String>("复核", "N");
        /**作废打回*/
        public final static ConstantBean<String> STATUS_INVALID_BACK = new ConstantBean<String>("作废打回", "M");
        /**作废暂存*/
        public final static ConstantBean<String> STATUS_INVALID_TEMP = new ConstantBean<String>("作废暂存", "D");
        /**作废待复核*/
        public final static ConstantBean<String> STATUS_INVALID_WAIT_CHECK = new ConstantBean<String>("作废待复核", "W");
        /**作废复核*/
        public final static ConstantBean<String> STATUS_INVALID_CHECK = new ConstantBean<String>("作废复核", "B");
    }
    /**
     * 历史凭证操作类型
     */
    public static class VoucherOpType {
        //改账
        public final static ConstantBean<String> OpType_M = new ConstantBean<String>("改账", "M");
        //抹账
        public final static ConstantBean<String> OpType_D = new ConstantBean<String>("抹账", "D");
        //作废
        public final static ConstantBean<String> OpType_B = new ConstantBean<String>("作废", "B");
    }
    /**
     * 加急标识
     */
    public static class EmergencySign {
        public static final ConstantBean<String> EMERGENCY_SIGN_GENERAL = new ConstantBean<String>("普通", "G");
        public static final ConstantBean<String> EMERGENCY_SIGN_FAST = new ConstantBean<String>("加急", "F");
        public static final ConstantBean<String> EMERGENCY_SIGN_NULL = new ConstantBean<String>("不考虑", "N");
    }
    /**
     * 获取系统业务品种
     * @param sysRaceCode 系统业务品种代码
     * @return 系统业务品种
     */
    public static ConstantBean<String> getSysRaceBean(String sysRaceCode) {
    	for (Field field : SysRaceCode.class.getFields()) {
            ConstantBean<String> constantBean;
			try {
				constantBean = (ConstantBean<String>) field.get(null);
				if (constantBean.get().equals(sysRaceCode)) {
					return constantBean;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    	return null;
    }
    /**
     * 联行往来记账信息表单状态
     */
    public static class UnionAccbookBillState {
        public static final ConstantBean<String> W = new ConstantBean<String>("接收方待记账", "W");
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
    public static void main(String[] args) {
		System.out.println(getSysRaceBean(SysRaceCode.NB_OTHER.get()));
	}
}
