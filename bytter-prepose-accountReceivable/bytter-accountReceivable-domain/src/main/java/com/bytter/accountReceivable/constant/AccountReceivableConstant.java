package com.bytter.accountReceivable.constant;

public class AccountReceivableConstant {
    /**
     * 应收单审批
     */
    public final static String DOC_MANAGE_APPROVE = "docManageApprove";

    /** 应收单-核销处理 审批 **/
    public final static String DOC_WRITE_OFF_APPROVE = "docWriteOff";


    public static final String WRITEOFFSTATUS_SAVE = "0";//未核销

    /**业务状态常量*/

    public static final String STATUS_TEMP = "0";//暂存
    public static final String STATUS_BACK = "-1";//打回
    public static final String STATUS_DELETE = "-3";//已删除
    public static final String STATUS_CHANGE_REFUSE = "-2";//已作废
    public static final String STATUS_WAIT_CHECK = "11";//待审批
    public static final String STATUS_CHECK = "95";//审批通过

    public static final String STATUS_WAIT_CHANGE_REFUSE="102";//作废待审批
    public static final String STATUS__WAIT_INVALIDATED = "104";//变更待审批
    public static final String STATUS_BACK_CHANGE_REFUSE = "105";//变更作废打回

//    public static final String DOC_BIGDECIMAL_REGEX = "^(?:[1-9]\\d{11}|(0|[1-9]\\d*)(\\.\\d{2})?)$";//正则匹配金额

    public static final String DOC_BIGDECIMAL_REGEX = "^[1-9]\\d{0,11}(\\.\\d{1,2})?$";//正则匹配金额

}
