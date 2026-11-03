package com.bytter.finance.constant;
import java.io.Serializable;
public class InlConstant implements Serializable {
    /**
     * 作废
     */
    public final static int DEFERRED = -3;
    /**
     * 删除
     */
    public final static int DELETE = -2;
    /**
     * 打回
     */
    public final static int BACK = -1;
    /**
     * 暂存
     */
    public final static int TMP = 0;
    /**
     * 审批中
     */
    public final static int COMMIT_APPROVE = 11;
    /**
     * 中间等待状态
     */
    public final static int WAIT = 21;
    /**
     * 审批通过
     */
    public final static int APPROVED = 95;
    /**
     * 放款/还款确认
     */
    public final static int CONFIRM = 96;
    /** 内部拆借申请 */
    public final static String CMS_INL_INFO_NO = "NZ";
    /** 内部拆借放款 */
    public final static String CMS_INL_SEND_NO = "NS";
    /** 内部拆借作废 */
    public final static String CMS_INL_ABOLIS_NO = "NA";
    /** 内部拆借展期 */
    public final static String CMS_INL_DEFERRED_NO = "ND";
    /** 内部拆借利率调整 */
    public final static String CMS_INL_ADJUSTRATE_NO = "NR";
    /** 内部拆借还款 */
    public final static String CMS_INL_REPAY_NO = "NC";
    /** 内部拆借还款计划修改 */
    public final static String CMS_INL_REPAYCAHNEG_NO = "NA";
    /** 合同类型Id */
    public final static String CMS_INL_CON_TYPE = "IBL01";
    /** 合同类型 */
    public final static String CMS_INL_CON_PRO = "CON_PD_INTERNALBORROW_LEND";
    /** 合同履约 */
    public final static String CMS_INL_CON_SEGMENT = "con_segment";
    /**
     * 是否挂接合同
     */
    public final static String CMS_INL_CON_IS_CON = "is_con";
    /**
     * 是否挂资金计划
     */
    public final static String CMS_INL_IS_BUDGET = "is_budget";
    /**
     * 是否挂接授信
     */
    public final static String CMS_INL_IS_RATION = "is_hang_credit";
    /**
     * 是否挂接授信
     */
    public final static String CMS_INL_IS_GUS = "is_hang_credit";
    /**
     * 拆入单位不能为空
     */
    public final static String CMS_INL_REPORT_IMPORT_ERROR1 = "cms.inl.report.import.error1";
    /**
     * 拆出单位不能为空
     */
    public final static String CMS_INL_REPORT_IMPORT_ERROR2 = "cms.inl.report.import.error2";
    /**
     * 币种不能为空
     */
    public final static String CMS_INL_REPORT_IMPORT_ERROR3 = "cms.inl.report.import.error3";
    /**
     * 总拆借金额不能为空
     */
    public final static String CMS_INL_REPORT_IMPORT_ERROR4 = "cms.inl.report.import.error4";
    /**
     * 拆借日期不能为空
     */
    public final static String CMS_INL_REPORT_IMPORT_ERROR5 = "cms.inl.report.import.error5";
    /**
     * 协议到期日期不能为空
     */
    public final static String CMS_INL_REPORT_IMPORT_ERROR6 = "cms.inl.report.import.error6";
    /**
     * 已放款金额不能为空
     */
    public final static String CMS_INL_REPORT_IMPORT_ERROR7 = "cms.inl.report.import.error7";
    /**
     * 已还本金金额不能为空
     */
    public final static String CMS_INL_REPORT_IMPORT_ERROR8 = "cms.inl.report.import.error8";
    /**
     * 已还利息金额不能为空
     */
    public final static String CMS_INL_REPORT_IMPORT_ERROR9 = "cms.inl.report.import.error9";
    /**
     * 拆入单位错误
     */
    public final static String CMS_INL_REPORT_IMPORT_CORPERROR1 = "cms.inl.report.import.corpError1";
    /**
     * 拆出单位错误
     */
    public final static String CMS_INL_REPORT_IMPORT_CORPERROR2 = "cms.inl.report.import.corpError2";
    /**
     * 币种错误
     */
    public final static String CMS_INL_REPORT_IMPORT_CURERROR2 = "cms.inl.report.import.curError";
    /**
     * 开始日期必须小于结束日期
     */
    public final static String CMS_INL_REPORT_IMPORT_DATEERROR = "cms.inl.report.import.dateError";
}
