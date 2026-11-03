package com.bytter.biz.finacial.api.constant;
import com.bytter.core.constant.ConstantBean;
import java.io.Serializable;
/**
 * 科目常量类
 * @author caozx
 * @since 2017-7-14
 */
public class SubjectConstant implements Serializable{
    /**
     * 科目类型
     */
    public static class SubjectSort {
        public static final ConstantBean<String> SUBJECT_SORT_A = new ConstantBean<String>("资产", "A");
        public static final ConstantBean<String> SUBJECT_SORT_L = new ConstantBean<String>("负债", "L");
        public static final ConstantBean<String> SUBJECT_SORT_I = new ConstantBean<String>("损益", "I");
        public static final ConstantBean<String> SUBJECT_SORT_E = new ConstantBean<String>("权益", "E");
        public static final ConstantBean<String> SUBJECT_SORT_B = new ConstantBean<String>("资产负债共同类", "B");
        public static final ConstantBean<String> SUBJECT_SORT_G = new ConstantBean<String>("表外科目", "G");
    }
    /**
     * 系统科目
     */
    public static class SysSubject {
        public static final ConstantBean<String> INTEREST_PAYABLE_NOTICE_DEPOSIT = new ConstantBean<String>("应付利息-通知存款", "2013");
        public static final ConstantBean<String> INTEREST_EXPENSES_NOTICE_DEPOSIT = new ConstantBean<String>("利息支出-通知存款", "5213");
        public static final ConstantBean<String> INTEREST_PAYABLE_FIXED_DEPOSIT = new ConstantBean<String>("应付利息-企业定期存款", "2012");
        public static final ConstantBean<String> INTEREST_EXPENSES_FIXED_DEPOSIT = new ConstantBean<String>("利息支出-企业定期存款", "5211");
        public static final ConstantBean<String> INTEREST_PAYABLE_FIXED_BOND = new ConstantBean<String>("应付利息-定期保证金存款", "2019");
        public static final ConstantBean<String> INTEREST_EXPENSES_FIXED_BOND = new ConstantBean<String>("利息支出-定期保证金存款", "5219");
        public static final ConstantBean<String> INTEREST_EXPENSES_BANK_LOAN = new ConstantBean<String>("利息支出-银行借款", "5214");
        public static final ConstantBean<String> INTEREST_PAYABLE_BANK_LOAN = new ConstantBean<String>("应付利息-银行借款", "2014");
        //银行长期借款
        public static final ConstantBean<String> INTEREST_EXPENSES_BANK_LONG_LOAN = new ConstantBean<String>("利息支出-银行长期借款", "5217");
        public static final ConstantBean<String> INTEREST_PAYABLE_BANK_LONG_LOAN = new ConstantBean<String>("应付利息-银行长期借款", "2017");
        public static final ConstantBean<String> INTEREST_PAYABLE_LOAN = new ConstantBean<String>("应收利息-企业放款", "1031");
        public static final ConstantBean<String> INTEREST_EXPENSES_LOAN = new ConstantBean<String>("利息收入-放款", "5011");
        public static final ConstantBean<String> INTEREST_PAYABLE_CURRENT = new ConstantBean<String>("应付利息-企业活期存款", "2011");
        public static final ConstantBean<String> INTEREST_EXPENSES_CURRENT = new ConstantBean<String>("利息支出-企业活期存款", "5212");
        public static final ConstantBean<String> INTEREST_PAYABLE_CURRENT_BOND = new ConstantBean<String>("应付利息-活期保证金存款", "2018");
        public static final ConstantBean<String> INTEREST_EXPENSES_CURRENT_BOND = new ConstantBean<String>("利息支出-活期保证金存款", "5218");
        public static final ConstantBean<String> INTEREST_PAYABLE_BANK_CURRENT = new ConstantBean<String>("应收利息-银行活期存款", "1032");
        public static final ConstantBean<String> INTEREST_EXPENSES_BANK_CURRENT = new ConstantBean<String>("利息收入-银行活期存款", "5012");
        public static final ConstantBean<String> INTEREST_PAYABLE_BANK_FIXED = new ConstantBean<String>("应收利息-银行定期存款", "1033");
        public static final ConstantBean<String> INTEREST_EXPENSES_BANK_FIXED = new ConstantBean<String>("利息收入-银行定期存款", "5013");
        public static final ConstantBean<String> INTEREST_EXPENSES_INTER_BANK_LOANS = new ConstantBean<String>("拆入资金利息支出", "9021");
        public static final ConstantBean<String> INTEREST_PAYABLE_INTER_BANK_LOANS = new ConstantBean<String>("应付预提拆入资金利息", "7016");
        public static final ConstantBean<String> INTEREST_EXPENSES_OUTER_BANK_LOANS = new ConstantBean<String>("拆放同业利息收入", "9051");
        public static final ConstantBean<String> INTEREST_PAYABLE_OUTER_BANK_LOANS = new ConstantBean<String>("应收拆放同业预提利息", "6015");
        public static final ConstantBean<String> INTEREST_WAIT_LOAN = new ConstantBean<String>("待收利息", "6021");
        public static final ConstantBean<String> INTEREST_WAIT_COMPOUND_LOAN = new ConstantBean<String>("待收复利", "6022");
        public static final ConstantBean<String> INTEREST_NOT_ACCURED_LOAN = new ConstantBean<String>("非应计贷款", "1126");
        public static final ConstantBean<String> INTEREST_PAYABLE_OWE_INNERACC = new ConstantBean<String>("应收逾期利息", "1060");//应收利息-欠息
        public static final ConstantBean<String> INTEREST_ENTURST_LOANS = new ConstantBean<String>("委托放款", "1125");
        public static final ConstantBean<String> INTEREST_ENTUST_DEPOSIT = new ConstantBean<String>("委托存款", "291");
        public static final ConstantBean<String> INTEREST_EXPENSES_CURRENT_DEPOSIT = new ConstantBean<String>("企业活期存款", "2120");
        public static final ConstantBean<String> INTEREST_BILL_CURRENT_DEPOSIT = new ConstantBean<String>("票据企业活期", "2122");
        public static final ConstantBean<String> FIXED_DEPOSIT = new ConstantBean<String>("企业定期存款", "2130");
        public static final ConstantBean<String> NOTICE_DEPOSIT = new ConstantBean<String>("企业通知存款", "2140");
        public static final ConstantBean<String> INTEREST_EXPENSES_OUT_BUSINESS = new ConstantBean<String>("营业外收入", "5100");
        public static final ConstantBean<String> INTEREST_BANK_CURRENT_DEPOSIT = new ConstantBean<String>("银行活期存款", "1020");
        public static final ConstantBean<String> INTEREST_BANK_FIXED_DEPOSIT = new ConstantBean<String>("银行定期存款", "109");
        public static final ConstantBean<String> ENTERPRISE_LOAN = new ConstantBean<String>("企业放款", "1120");
        public static final ConstantBean<String> ENTERPRISE_LOAN_LONG_TERM = new ConstantBean<String>("中长期贷款", "1122");
        public static final ConstantBean<String> SUBJECT_CASH = new ConstantBean<String>("现金", "1010");
        public static final ConstantBean<String> BANK_LOAN = new ConstantBean<String>("银行借款", "2110");
        public static final ConstantBean<String> AB_RECEIVE_LOAN = new ConstantBean<String>("票据借贷", "550");
        public static final ConstantBean<String> TAX_BUSINESS = new ConstantBean<String>("应交税金", "205");
        public static final ConstantBean<String> TAX_SALE = new ConstantBean<String>("应交税金-营业税", "2051");
        public static final ConstantBean<String> TAX_CITY_BUILD = new ConstantBean<String>("应交税金-城建税", "2052");
        public static final ConstantBean<String> TAX_EDUCATION = new ConstantBean<String>("应交税金-教育费附加", "2053");
        public static final ConstantBean<String> TAX_EDUCATION_PART = new ConstantBean<String>("应交税费-地方教育费附加", "2054");
        public static final ConstantBean<String> TAX_MONEY = new ConstantBean<String>("营业税金及附加—营业税款", "5251");
        public static final ConstantBean<String> TAX_MONEY_APPEND = new ConstantBean<String>("营业税金及附加—税款及附加", "5252");
        public static final ConstantBean<String> EXCHANGE_TRADING = new ConstantBean<String>("外汇买卖", "281");
        public static final ConstantBean<String> EXCHANGE_TRADING_INCOME = new ConstantBean<String>("汇兑收益", "513");
        public static final ConstantBean<String> EXCHANGE_TRADING_LOSE = new ConstantBean<String>("汇兑损益", "535");
        public static final ConstantBean<String> PROFIT_YEAR = new ConstantBean<String>("本年利润", "311");
        public static final ConstantBean<String> WAIT_AMORTIZE_COST = new ConstantBean<String>("待摊费用", "150");
        public static final ConstantBean<String> PROCEDURE_FEE_INCOME = new ConstantBean<String>("手续费收入", "502");
        public static final ConstantBean<String> TEMPORARY_RECEIVE_MONEY = new ConstantBean<String>("暂收款项", "1050");
        public static final ConstantBean<String> DISCOUNT_INTEREST_INCOME = new ConstantBean<String>("利息收入-贴现", "5018");
        public static final ConstantBean<String> DISCOUNT_INTEREST_EXPENSES = new ConstantBean<String>("利息支出-贴现", "521Z");
        public static final ConstantBean<String> DISCOUNT_INTEREST_COMP_AGREED = new ConstantBean<String>("利息支出-企业协定存款", "5216");
        public static final ConstantBean<String> INTEREST_PAYABLE_COMP_AGREED = new ConstantBean<String>("应付利息-企业协定存款", "2016");
        public static final ConstantBean<String> INTEREST_COMP_AGREED = new ConstantBean<String>("企业协定存款", "2150");
        public static final ConstantBean<String> DEPOSIT_INTEREST_INCOME_REGULAR = new ConstantBean<String>("应付利息-企业定期存款", "2012");
        public static final ConstantBean<String> DEPOSIT_INTEREST_INCOME_NOTICE = new ConstantBean<String>("应付利息-通知存款", "2013");
        public static final ConstantBean<String> DEPOSIT_INTEREST_EXPENDITURE_REGULAR = new ConstantBean<String>("利息支出—企业定期存款", "5211");
        public static final ConstantBean<String> DEPOSIT_INTEREST_EXPENDITURE_NOTICE = new ConstantBean<String>("利息支出—企业通知存款", "5213");
        public static final ConstantBean<String> AB_INTEREST_INCOME = new ConstantBean<String>("贴现利息收入", "5018");//501004
        public static final ConstantBean<String> AB_BANK_INTEREST_EXPENDITURE = new ConstantBean<String>("银行贴现利息支出", "521Z");//521005
        public static final ConstantBean<String> AB_INTEREST_PROFIT = new ConstantBean<String>("递延收益-贴现利息收入", "2060");
    }
    /**利息支出*/
    public static final String SYSSUBJECT_INTEREST_EXPENSES="521";
    /**利息收入*/
    public static final String SYSSUBJECT_INTEREST_INCOME="501";
}
