package com.bytter.biz.payment.api.constant;
import java.util.HashMap;
import java.util.Map;
/**
 * 电子回单打印常量类
 * @author zhangxb
 * @since 2017/5/31.
 */
public class ReceiptConstant {
    /**all-全部电子回单*/
    public static final String RECEIPT_BILL_TYPE_ALL = "all";
    /**01-存款利息单*/
    public static final String RECEIPT_BILL_TYPE_01 = "01";
    /**02-贷款利息单*/
    public static final String RECEIPT_BILL_TYPE_02 = "02";
    /**03-委托贷款利息清单*/
    public static final String RECEIPT_BILL_TYPE_03 = "03";
    /**04-定期利息单*/
    public static final String RECEIPT_BILL_TYPE_04 = "04";
    /**05-通知利息单*/
    public static final String RECEIPT_BILL_TYPE_05 = "05";
    /**19-活期保证金利息单*/
    public static final String RECEIPT_BILL_TYPE_19 = "19";
    /**20-定期保证金利息单*/
    public static final String RECEIPT_BILL_TYPE_20 = "20";
    /**06-委托收款通知单*/
    public static final String RECEIPT_BILL_TYPE_06 = "06";
    /**07-贷款通知单*/
    public static final String RECEIPT_BILL_TYPE_07 = "07";
    /**08-付款通知单*/
    public static final String RECEIPT_BILL_TYPE_08 = "08";
    /**09-收款通知单*/
    public static final String RECEIPT_BILL_TYPE_09 = "09";
    /**10-内转通知单*/
    public static final String RECEIPT_BILL_TYPE_10 = "10";
    /**11-手续费通知单*/
    public static final String RECEIPT_BILL_TYPE_11 = "11";
    /**12-活期转通知*/
    public static final String RECEIPT_BILL_TYPE_12 = "12";
    /**13-活期转定期*/
    public static final String RECEIPT_BILL_TYPE_13 = "13";
    /**14-定期转活期*/
    public static final String RECEIPT_BILL_TYPE_14 = "14";
    /**15-通知转活期*/
    public static final String RECEIPT_BILL_TYPE_15 = "15";
    /**16-承兑汇票电子回单*/
    public static final String RECEIPT_BILL_TYPE_16 = "16";
    /**17-对账单电子回单*/
    public static final String RECEIPT_BILL_TYPE_17 = "17";
    /**18-内部结售汇电子回单*/
    public static final String RECEIPT_BILL_TYPE_18 = "18";
    /**21-委托贷款通知单*/
    public static final String RECEIPT_BILL_TYPE_21 = "21";
    /**22-委托贷款还款通知单*/
    public static final String RECEIPT_BILL_TYPE_22 = "22";
    /**
     * 利息清单电子回单
     * 01-存款利息单
     02-贷款利息单
     03-委托贷款利息清单
     04-定期利息单
     05-通知利息单
     19-活期保证金利息单
     20-定期保证金利息单
     */
    public static final String E_ACCRUAL_BILL = "E_ACCRUAL_BILL";
    public static final String E_CONSIGN_BILL = "E_CONSIGN_BILL";
    public static final String E_LOAN_BILL = "E_LOAN_BILL";
    public static final String E_NOTICE_BILL = "E_NOTICE_BILL";
    public static final String E_POUNDAGE_BILL = "E_POUNDAGE_BILL";
    /**
     * 账户转换电子回单表
     12-活期转通知
     13-活期转定期
     14-定期转活期
     15-通知转活期
     */
    public static final String E_TRANS_BILL = "E_TRANS_BILL";
    public static final String E_ACCEPT_BILL = "E_ACCEPT_BILL";
    public static final String E_COMPARE_BILL = "E_COMPARE_BILL";
    public static final String E_EXCHANGE_BILL = "E_EXCHANGE_BILL";
    public static final String E_PRECATIVE_LOAN_BILL = "E_PRECATIVE_LOAN_BILL";
    public static final Map<String,String> BILL_MAP = new HashMap<String,String>();
    static {
        BILL_MAP.put(RECEIPT_BILL_TYPE_01, E_ACCRUAL_BILL);//存款利息单
        BILL_MAP.put(RECEIPT_BILL_TYPE_02, E_ACCRUAL_BILL);//贷款利息单
        BILL_MAP.put(RECEIPT_BILL_TYPE_03, E_ACCRUAL_BILL);//委托贷款利息清单
        BILL_MAP.put(RECEIPT_BILL_TYPE_04, E_ACCRUAL_BILL);//定期存款利息单
        BILL_MAP.put(RECEIPT_BILL_TYPE_05, E_ACCRUAL_BILL);//通知存款利息单
        BILL_MAP.put(RECEIPT_BILL_TYPE_06, E_CONSIGN_BILL);
        BILL_MAP.put(RECEIPT_BILL_TYPE_07, E_LOAN_BILL);
        BILL_MAP.put(RECEIPT_BILL_TYPE_08, E_NOTICE_BILL);
        BILL_MAP.put(RECEIPT_BILL_TYPE_09, E_NOTICE_BILL);
        BILL_MAP.put(RECEIPT_BILL_TYPE_10, E_NOTICE_BILL);
        BILL_MAP.put(RECEIPT_BILL_TYPE_11, E_POUNDAGE_BILL);
        BILL_MAP.put(RECEIPT_BILL_TYPE_12, E_TRANS_BILL);//通知存单
        BILL_MAP.put(RECEIPT_BILL_TYPE_13, E_TRANS_BILL);//定期存单
        BILL_MAP.put(RECEIPT_BILL_TYPE_14, E_TRANS_BILL);//定期取款单
        BILL_MAP.put(RECEIPT_BILL_TYPE_15, E_TRANS_BILL);//通知取款单
        BILL_MAP.put(RECEIPT_BILL_TYPE_16, E_ACCEPT_BILL);
        BILL_MAP.put(RECEIPT_BILL_TYPE_17, E_COMPARE_BILL);
        BILL_MAP.put(RECEIPT_BILL_TYPE_18, E_EXCHANGE_BILL);
        BILL_MAP.put(RECEIPT_BILL_TYPE_19, E_ACCRUAL_BILL);
        BILL_MAP.put(RECEIPT_BILL_TYPE_20, E_ACCRUAL_BILL);
        BILL_MAP.put(RECEIPT_BILL_TYPE_21, E_PRECATIVE_LOAN_BILL);
        BILL_MAP.put(RECEIPT_BILL_TYPE_22, E_PRECATIVE_LOAN_BILL);
    }
    /**
     *回单状态
     */
    public static final String VALID_SIGN="1";
    public static final String INVALID_SIGN="-1";
    /**
     * 打印状态：已打印
     */
    public static final String PRINT_STATUS_YES="Y" ;
    /**
     * 打印状态：未打印
     */
    public static final String PRINT_STATUS_NO="N" ;
    /**贷款*/
    public static final String BILL_LOAN_TYPE_1="1";
    /**还款*/
    public static final String BILL_LOAN_TYPE_2="2";
    /**状态1，新输入*/
    public static final Integer LOAN_BILL_STATUS_INPUT=1;
    /**状态-1，删除*/
    public static final Integer LOAN_BILL_STATUS_DELETE=-1;
    /**状态2，冲帐*/
    public static final Integer LOAN_BILL_STATUS_RESERVE=2;
    /**电子回单消息操作类型:1 表示生成数据*/
    public static final Integer BILL_MSG_OP_TYPE_CREATE=1;
    /**电子回单消息操作类型:2 表示作废数据*/
    public static final Integer BILL_MSG_OP_TYPE_CANCELL=2;
    /**电子回单消息操作类型:3 表示删除数据*/
    public static final Integer BILL_MSG_OP_TYPE_DELETE=3;
    /**电子回单消息操作类型:4 表示打印数据*/
    public static final Integer BILL_MSG_OP_TYPE_PRINT=4;
    /**电子回单消息信息：生成单据*/
    public static final String BILL_MSG_INFO_CREATE="生成单据";
    /**电子回单消息信息：作废单据*/
    public static final String BILL_MSG_INFO_CANCELL="作废单据";
    /**电子回单消息信息：作废单据-成员单位打印,中心未打*/
    public static final String BILL_MSG_INFO_CANCELL1="作废单据-成员单位打印,中心未打";
    /**电子回单消息信息：作废单据-中心打印,成员单位未打*/
    public static final String BILL_MSG_INFO_CANCELL2="作废单据-中心打印,成员单位未打";
    /**电子回单消息信息：删除单据*/
    public static final String BILL_MSG_INFO_DELETE="删除单据";
    /**电子回单消息信息：打印单据*/
    public static final String BILL_MSG_INFO_PRINT="打印单据";
    /**记录凭证的的借贷 B一借一贷*/
    public static final String BILL_TYPE_B="B";
    /**记录凭证的的借贷 C一借多贷*/
    public static final String BILL_TYPE_C="C";
    /**记录凭证的的借贷 D多借一贷*/
    public static final String BILL_TYPE_D="D";
    /*** 电子回单参数配置：ele-电子回单 电子回单历史数据迁移提前天数*/
    public static final String ELE_CONFIG_TRANSFERDAYS = "transferDays";
    /**
     * 电子回单参数配置，每次迁移的数量
     */
    public static final String ELE_CONFIG_TRANSFER_NUMBER = "transferNumber";
    /**
     * RPA查询天数
     */
    public static final String ELE_CONFIG_RPA_DATE_NUMBER = "rpaDateNumber";
    /**
     * 是否启用模糊匹配
     */
    public static final String ELE_CONFIG_FUZZY_MATCHING = "fuzzyMatching";
    public static final String BIS_CHECK_REASON_QUERY ="bis:check:reason:query";
    public static final String BIS_BANK_ENTERPRISE_CHECK_QUERY ="bis:bank:enterprise:check:query";
    public static final String BIS_BANK_ENTERPRISE_CHECK_OPT ="bis:bank:enterprise:check:opt";
    public static final String BIS_BANK_STATETRACKING_CHECK_QUERY ="bis:bank:statetracking:check:query";
    public static final String BIS_BANK_STATETRACKING_CHECK_OPT ="bis:bank:statetracking:check:opt";
    public static final String BIS_CHECK_BUS_QUERY ="bis:check:tracking:query";
    public static final String BIS_BANK_VALID_SIGN_0 ="0";
    public static final String BIS_BANK_VALID_SIGN_1 ="1";
    public static final String BIS_BANK_VALID_SIGN_2 ="2";
    //指令类型 1-银企对账明细查询指令  2-银企对账结果发送指令 3-对账结果查询指令 4-银企对账复核结果发送指令
    public static final String BIS_BANK_BUS_TYPE_1 ="1";
    public static final String BIS_BANK_BUS_TYPE_2 ="2";
    public static final String BIS_BANK_BUS_TYPE_3 ="3";
    public static final String BIS_BANK_BUS_TYPE_4 ="4";
    //对账结果标志
    public static final String BIS_BANK_CHECK_RESULT_T ="T";
    public static final String BIS_BANK_CHECK_RESULT_N ="N";
    public static final String BIS_BANK_CHECK_RESULT_Y ="Y";
}
