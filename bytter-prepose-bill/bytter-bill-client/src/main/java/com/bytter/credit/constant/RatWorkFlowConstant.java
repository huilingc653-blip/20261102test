package com.bytter.credit.constant;
import java.io.Serializable;
/**
 * 授信工作流常量类
 * 
 * @author liyh
 *
 */
public class RatWorkFlowConstant implements Serializable {
    /**
     * 授信录入
     */
    public final static String RAT_BKRATION = "ratBkration";
    /**
     * 授信分配
     */
    public final static String RAT_BKRATION_DIS = "ratBkrationDis";
    /**
     * 授信银行追加
     */
    public final static String RAT_BKRATION_ADD = "ratBkrationAdd";
    /**
     * 授信集团追加
     */
    public final static String RAT_BKRATION_CPADD = "ratBkrationCpAdd";
    /**
     * 授信调剂
     */
    public final static String RAT_BKRATION_RELIEF = "ratBkrationRelief";
    /**
     * 授信占用
     */
    public final static String RAT_URATION_APPLY = "ratUrationApply";
    /**
     * 授信延期
     */
    public final static String RAT_RATION_DELAY = "ratRationDelay";
    /**
     * 银行授信最高额度调整
     */
    public final static String RAT_BKRATION_ADJUST = "ratBkrationAdjust";
    /**
     * 内部授信录入
     */
    public final static String RAT_CPRATION = "ratCpration";
    /**
     * 内部授信追加
     */
    public final static String RAT_CPRATION_ADD = "ratCprationAdd";
    /**
     * 内部授信分配
     */
    public final static String RAT_CPRATION_DIS = "ratCprationDis";
    /**
     * 内部授信占用
     */
    public final static String RAT_CPRATION_APPLY = "ratCprationApply";
    /**
     * 内部授信调剂
     */
    public final static String RAT_CPRATION_RELIEF = "ratCprationRelief";
}
