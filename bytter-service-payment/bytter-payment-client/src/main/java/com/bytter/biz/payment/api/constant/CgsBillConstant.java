package com.bytter.biz.payment.api.constant;
/**
 * 收款常量类
 * @author wangzx
 * @since 2017/6/21.
 */
public class CgsBillConstant {
    /*** 收款业务处理状态：0-暂存  */
    public final static Integer STATUE_TEMP = 0;
    /*** 收款业务处理状态：1-打回  */
    public final static Integer STATUE_BACK = -1;
    /*** 收款业务处理状态：-2-删除  */
    public final static Integer STATUE_DEL = -2;
    /*** 收款业务处理状态：11-待审批  */
    public final static Integer STATUE_WAIT_CHECK = 11;
    /*** 收款业务处理状态：95-审批通过  */
    public final static Integer STATUE_CHECK = 95;
    /*** 收款业务处理状态：96-记账暂存  */
    public final static Integer STATUE_TALLY_TEMP = 96;
    /*** 收款业务处理状态：97-记账待复核  */
    public final static Integer STATUE_TALLY_WAIT_CHECK = 97;
    /*** 收款业务处理状态：98-记账复核打回  */
    public final static Integer STATUE_TALLY_BACK = 98;
    /*** 收款业务处理状态：100-记账复核通过  */
    public final static Integer STATUE_TALLY_CHECKPASS = 100;
    /*** 收款业务签收标识：0-否  */
    public final static Integer BILL_SIGN_FLAG_N = 0;
    /*** 收款业务签收标识：1-是  */
    public final static Integer BILL_SIGN_FLAG_Y = 1;
    /*** 收款业务审核状态：0-默认未审核  */
    public final static Integer BILL_GATHER_AUDIT_STATE_DEFALUT = 0;
    /*** 收款业务审核状态：1-打回  */
    public final static Integer BILL_GATHER_AUDIT_STATE_BACK = 1;
    /*** 收款业务审核状态：2-审核中  */
    public final static Integer BILL_GATHER_AUDIT_STATE_WAIT = 2;
    /*** 收款业务审核状态：3-通过  */
    public final static Integer BILL_GATHER_AUDIT_STATE_SUCCESS = 3;
    /*** 收款业务安全认证结果：0-未开始认证  */
    public final static Integer BILL_JUDGE_RESULT_NOSTART = 0;
    /*** 收款业务安全认证结果：1-通过认证  */
    public final static Integer BILL_JUDGE_RESULT_SUCCESS = 1;
    /*** 收款业务安全认证结果：2-未通过认证打回  */
    public final static Integer BILL_JUDGE_RESULT_BACK = 2;
    /*** 收款业务审核状态：0-默认未审核  */
    public final static Integer BILL_CONFIRM_AUDIT_STATE_DEFALUT = 0;
    /*** 收款业务审核状态：1-打回  */
    public final static Integer BILL_CONFIRM_AUDIT_STATE_BACK = 1;
    /*** 收款业务审核状态：2-审核中  */
    public final static Integer BILL_CONFIRM_AUDIT_STATE_WAIT = 2;
    /*** 收款业务审核状态：3-通过  */
    public final static Integer BILL_CONFIRM_AUDIT_STATE_SUCCESS = 3;
    /*** 收款业务记账标识：0-未记账  */
    public final static Integer BILL_TALLY_FLAG_N = 0;
    /*** 收款业务记账标识：1-已记账  */
    public final static Integer BILL_TALLY_FLAG_Y = 1;
    /*** 收款业务记账标识：2-记账中  */
    public final static Integer BILL_TALLY_FLAG_W = 2;
    /*** 收款业务业务结束标志：0-未结束  */
    public final static Integer BILL_FINISH_FLAG_N = 0;
    /*** 收款业务业务结束标志：1-已结束成功收款且已记账  */
    public final static Integer BILL_FINISH_FLAG_S = 1;
    /*** 收款业务业务结束标志：2-已结束各种打回情况  */
    public final static Integer BILL_FINISH_FLAG_B = 2;
    /**	 * 审批通过！	 */
    public static final String CGS_LOG_APP_SUCC = "bill.cgs.app.succ";
    /**	 * 审批打回！	 */
    public static final String CGS_CGS_APP_BACK = "bill.cgs.app.back";
}
