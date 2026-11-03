package com.bytter.credential.api.constant;
import java.io.Serializable;
public class InternationalConstant implements Serializable {
    /** 注销**/
    public final static int CANCEL = -3;
    /** 已删除*/
    public final static int DELETED = -2;
    /** 已打回*/
    public final static int BACK = -1 ;
    /**登记**/
    public final static int TEMP = 0;
    /**暂存**/
    public final static int TEMPSTORE = 1;
    /**开证补录暂存**/
    public final static int SUPPLEMENTTEMPSTORE = 2;
    /**开证补录打回**/
    public final static int SUPPLEMENTBACK = 3;
    /** 提交审批 */
    public final static int COMMITAPPROVE = 11;
    /** 补录提交审批 */
    public final static int COMMITSUPPLEMENTAPPROVE = 21;
    /**流程等待状态    当前流程需要等待其他流程的执行结果时，使用的中间等待状态*/
    public final static int FLOWWAIT = 12;
    /** 审核通过 */
    public final static int APPROVED = 99;
    /** 补录审核通过 */
    public final static int SUPPLEMENTAPPROVED = 95;
    /**到期收款 -- 已收款**/
    public final static int FINISH = 5;
    /**申请抵押抵押 -- 已抵押**/
    public final static int MORTGAGE = 4;
    /**清零**/
    public final static int RESET = 101;
    /**开证入库**/
    public final static int STORAGE = 105;
    /**到单登记入库**/
    public final static int BILLARRIVALSTORAGE = 1;
    /**到单付汇申请**/
    public final static int BILLARRIVALPAYMENT = 2;
    /**到单押汇申请**/
    public final static int BILLARRIVALDEPOSIT = 3;
    /**付汇申请审批通过**/
    public final static int PAYMENAPPROVED = 95;
    /**押汇申请审批通过**/
    public final static int DEOPSITAPPROVED = 95;
    /**押汇补录完成**/
    public final static int DEOPSITSUPPLEMENT = 96;
    /**还押汇**/
    public final static int DEOPSITREPAY = 97;
    /**已展期**/
    public final static int DEOPSITDELAY = 99;
    /**业务结束**/
    public final static int BUSINESSFINISH= 999;
//是否挂授信
    public final static String ISHANGCREDIT_Y = "1";
    public final static String ISHANGCREDIT_N = "0";
//是否挂保证金
    public final static String ISHANGBOND_Y = "1";
    public final static String ISHANGBOND_N = "0";
    //    国际结算用途维护种类类别为5
    public final static String PURPOSE = "5";
    //    授信模式0单一授信1综合授信
    public final static String RATMODEL_0 = "0";
    public final static String RATMODEL_1 = "1";
//    是否代开信用证
    public final static String ISRECREDIT_Y = "1";
    public final static String isrecredit_n = "0";
    //    是否折算成本位币
    public final static String ISNATIVECUR_Y = "1";
    public final static String ISNATIVECUR_N = "0";
    //    是否溢短装
    public final static String ISSPILLOVER_Y = "1";
    public final static String ISSPILLOVER_N = "0";
//    是否释放剩余授信
    public final static String ISALLRELEASE_Y = "1";
    public final static String ISALLRELEASE_N = "0";
    public final static String LC_TYPE_OPEN = "0";
    public final static String LC_TYPE_RECEIVE = "1";
//    是否挂接担保
    public final static String IS_HANG_GUARANTEE_YES = "1";
    public final static String IS_HANG_GUARANTEE_NO = "0";
    /**
     * 未完结
     */
    public final static String DEPOSIT_STATUS_1 = "1";
    /**
     * 已完结
     */
    public final static String DEPOSIT_STATUS_2 = "2";
}
