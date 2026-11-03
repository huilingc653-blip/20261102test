package com.bytter.workflow.constant;
/**
 * Created by zhoul on 2017/8/9.
 */
public class WorkFlowConstant {
    public static final String PASS = "1";
    public static final String UN_PASS = "2";
    public static final String REJECT = "3";
    public static final String REJECT_START = "4";
    /**
     * confirm：弹出对话框给用户，用户确认是否要提交。
     */
    public static final String VERITY_STATUS_CONFIRM = "0";
    /**
     * alert：只弹出对话框给用户，提示用户。
     */
    public static final String VERITY_STATUS_ALERT = "1";
    
    /**
               *  控制提交。
     */
    public static final String VERITY_STATUS_CONTROLLER = "2";
    /**流程设计资源**/
    /**流程设计资源 操作资源 bpm:mode:op **/
    public static final String RESOURCE_BPM_MODE_OP = "bpm:mode:op";
    public static final String TASK_PASS_FLAG = "taskPassFlag";
    /**
     * 一周
     */
    public static final String WEEK = "week";
    /**
     * 一个月
     */
    public static final String MONTH = "month";
    /**
     * 三年
     */
    public static final String THREE_YEAR = "threeYear";
    /**
     * 半年
     */
    public static final String  HALF_YEAR = "halfYear";
    /**
     * 一年
     */
    public static final String YEAR = "year";
    /**
     * 业务启动工作流数据快照 存放变量key
     */
    public static final String BUSINESS_JSON = "businessJson";

    //启动定时任务专用ID
    public static final String SCH_UID = "scheduler";
}
