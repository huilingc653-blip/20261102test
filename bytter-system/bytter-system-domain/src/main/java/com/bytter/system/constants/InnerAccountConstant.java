package com.bytter.system.constants;
import java.util.HashMap;
import java.util.Map;
/**
 * 内部账号常量类
 * Created by zhangxb on 2017/5/31.
 */
public class InnerAccountConstant {
    //账户类别
    /*** 账户类别：01-企业活期存款  */
    public static final String FCACC_BUSINESS_ACCOUNT = "01";
    /*** 账户类别：02-银行活期存款  */
    public static final String FCACC_BANK_DEMAND = "02";
    /*** 账户类别：03-放款  */
    public static final String FCACC_LOAN = "03";
    /*** 账户类别：04-银行借款  */
    public static final String FCACC_BANK_BORROW = "04";
    /*** 账户类别：05-企业定期  */
    public static final String FCACC_BUSINESS_BEGULAR = "05";
    /*** 账户类别：06-银行定期  */
    public static final String FCACC_BANK_BEGULAR = "06";
    /*** 账户类别：07-内部账户  */
    public static final String FCACC_INNER_ACCOUNT = "07";
    /*** 账户类别：08-通知存款  */
    public static final String FCACC_BANK_DEPOSIT = "08";
    /*** 账户类别：09-委托贷款  */
    public static final String FCACC_LOANBYMANDATE = "09";
    /*** 账户类别：10-委托存款  */
    public static final String FCACC_ENTRUSTED_DEPOSIT = "10";
    /*** 账户类别：14-表外  */
    public static final String FCACC_OFFBALANCE = "14";
    /*** 账户类别：16-协定存款  */
    public static final String FCACC_AGREEMENT = "16";
    //账户状态
    /*** 账户状态：W-新建  */
    public static final String ACC_STATE_NEW = "W";//("新建","W")),
    /*** 账户状态：N-正常  */
    public static final String ACC_STATE_NORMAL = "N";//("正常","N")),
    /*** 账户状态：R-申请注销  */
    public static final String ACC_STATE_REQUISITION = "R";//("申请注销","R")),
    /*** 账户状态：D-注销  */
    public static final String ACC_STATE_DESTORY = "D";//("注销","D")),
    /*** 账户状态：A-完全冻结  */
    public static final String ACC_STATE_ALL_FREEZE = "A";//("完全冻结","A")),
    /*** 账户状态：P-部分冻结  */
    public static final String ACC_STATE_PART_FREEZE = "P";//("部分冻结","P")),
    /*** 账户状态：B-开户复核打回  */
    public static final String ACC_STATE_BACK = "B";//("开户复核打回","B")),
    /*** 账户状态：T-暂存  */
    public static final String ACC_STATE_TEMP = "T";//("暂存 ","T")),
    /*** 账户状态：F-改户复核打回  */
    public static final String ACC_STATE_F = "F";//("改户复核打回","F")),
    /*** 账户状态：E-改户申请  */
    public static final String ACC_STATE_EDIT = "E";//("改户申请","E")),
    /*** 账户状态：G-改户暂存  */
    public static final String ACC_STATE_G = "G";//("改户暂存","G")),
    /*** 账户状态：Z-注销复核打回  */
    public static final String ACC_STATE_Z = "Z";//("注销复核打回","Z")),
    /*** 账户状态：S-冻结只收不支  */
    public static final String ACC_STATE_S = "S";//("冻结只收不支","S")),
    /*** 账户状态：H-定期质押申请  */
    public static final String ACC_STATE_H = "H";//("定期质押申请","H")),
    /*** 账户状态：I-定期质押  */
    public static final String ACC_STATE_I = "I";//("定期质押","I")),
    /*** 账户状态：M-网上办理改户待复核  */
    public static final String ACC_STATE_M = "M";//("网上办理改户待复核","M")),
    /*** 账户状态：U-销户暂存  */
    public static final String ACC_STATE_U = "U";//("销户暂存","U")),
    /*** 账户状态：J-定期续存  */
    public static final String ACC_STATE_J = "J";//("定期续存","J")),
    /*** 账户状态：Q-非正常状态  */
    public static final String ACC_STATE_Q = "Q";//("非正常状态","Q")),//用于存入开户产生的小账号
    /*** 账户状态：C-休眠待复核  */
    public static final String ACC_STATE_C = "C";//("休眠待复核","C")),
    /*** 账户状态：O-休眠打回  */
    public static final String ACC_STATE_O = "O";//("休眠打回","O")),
    /*** 账户状态：K-休眠复核通过  */
    public static final String ACC_STATE_K = "K";//("休眠复核通过","K")),
    /*** 账户状态：V-冻结申请  */
    public static final String ACC_STATE_V = "V";//("冻结申请","V"));
    public static final Map AccTypeMap = new HashMap();
    static {
        AccTypeMap.put(FCACC_BUSINESS_ACCOUNT, "FcAccBusinessAccount");
        AccTypeMap.put(FCACC_BANK_DEMAND, "FcAccBankDemand");
        AccTypeMap.put(FCACC_LOAN, "FcAccLoan");
        AccTypeMap.put(FCACC_BANK_BORROW, "FcAccBankBorrow");
        AccTypeMap.put(FCACC_BUSINESS_BEGULAR, "FcAccBusinessBegular");
        AccTypeMap.put(FCACC_BANK_BEGULAR, "FcAccBankBegular");
        AccTypeMap.put(FCACC_INNER_ACCOUNT, "FcAccInnerAccount");
        AccTypeMap.put(FCACC_BANK_DEPOSIT, "FcAccBankDeposit");
        AccTypeMap.put(FCACC_LOANBYMANDATE, "FcAccLoanByMandate");
        AccTypeMap.put(FCACC_ENTRUSTED_DEPOSIT, "FcAccEntrustedDeposit");
        AccTypeMap.put(FCACC_OFFBALANCE, "FcAccOffBalance");
        AccTypeMap.put(FCACC_AGREEMENT, "FcAccAgreement");
    }
    /*** 透支标识：N-不允许透支 */
    public static final String OVERDRAFT_SIGN_N = "N";
    /*** 透支标识：R-受限透支 */
    public static final String OVERDRAFT_SIGN_R = "R";
    /*** 透支标识：D-不受限透支 */
    public static final String OVERDRAFT_SIGN_D = "D";
    /*** 冻结类型：A-不收不支 */
    public static final String FREEZE_TYPE_ALL = "A"; //不收不支
    /*** 冻结类型：P-定额冻结 */
    public static final String FREEZE_TYPE_PART ="P";    //
    /*** 冻结类型：S-只收不支 */
    public static final String FREEZE_TYPE_S ="S";    //
    /*** 计息标志： 1-计息*/
    public static final String INTERESTSIGN_YES ="1";
    /*** 计息标志： 0-不计息*/
    public static final String INTERESTSIGN_NO ="0";
    /***是否标志： 1-是*/
    public static final String BOOLEAN_TYPE_YES="1";
    /***是否标志： 0-否*/
    public static final String BOOLEAN_TYPE_NO="0";
    /***冻结标志: C-冻结*/
    public static final String BUS_TYPE_FREEZE="C" ;
    /***冻结标志: T-解冻*/
    public static final String BUS_TYPE_UNFREEZE="T";
    /***导入错误提示: 账号为空*/
    public static final String NETBANK_INNER_EXCEL_ERRORMSG_ACCOUNT_NULL="inner.error.msg.account.null" ;
    /***导入错误提示: 账号无效 或 账号不可初始化*/
    public static final String NETBANK_INNER_EXCEL_ERRORMSG_ACCOUNT_INVALID="inner.error.msg.account.invalid" ;
    /***导入错误提示: 账号的借方余额无效*/
    public static final String NETBANK_INNER_EXCEL_ERRORMSG_DEBITWAY_NULL="inner.error.msg.debitWay.null" ;
    /***导入错误提示: 账号的贷方余额无效*/
    public static final String NETBANK_INNER_EXCEL_ERRORMSG_CREDITWAY_NULL="inner.error.msg.creditWay.null" ;
    /*** 所有 */
    public static final String LOG_TYPE_ALL="ALL";
    //业务类型
    /***业务类型: 10-开户*/
    public static final String BIZ_TYPE_OPEN="10";
    /***业务类型: 11-改户*/
    public static final String BIZ_TYPE_CHANGE="11";
    /***业务类型: 12-初始化*/
    public static final String BIZ_TYPE_INIT="12";
    /***业务类型: 13-销户*/
    public static final String BIZ_TYPE_CANCEL="13";
    /***业务类型: 14-冻结*/
    public static final String BIZ_TYPE_FCFREEZE="14";
    /***业务类型: 15-解冻*/
    public static final String BIZ_TYPE_FCUNFREEZE="15";
    /**
     * 业务信息
     */
    public static final String BIZ_TYPE_AUTO_SUBMIT="自动提交";
    public static final String BIZ_TYPE_TEMP="暂存";
    public static final String BIZ_TYPE_SUBMIT="提交";
    public static final String BIZ_TYPE_DELETE="删除";
    public static final String BIZ_TYPE_CHECK_AUTO_SUCCESS="系统自动复核通过";
    public static final String BIZ_TYPE_CHECK_SUCCESS="复核通过";
    public static final String BIZ_TYPE_CHECK_BACK="复核打回";
    public static final String BIZ_TYPE_CANCEL_INFO="还原改户申请前数据";
    public static final String BIZ_TYPE_FCFREEZE_INFO="还原成冻结前的状态";
    /**
     * 资金池标识 0-非资金池
     */
    public final static String POOL_SIGN_0 = "0";
    /**
     * 资金池标识 1-资金池
     */
    public final static String POOL_SIGN_1 = "1";
}
