package com.bytter.workflow.constant;
/**
 * Created by zhoul on 2017/8/16.
 */
public class FlowCategoryConstant {
    /**
     * 审批类型流程
     */
    public static final String FLOW_CATEGORY_TYPE_APPROVE = "1";
    /**
     * 业务类型流程
     */
    public static final String FLOW_CATEGORY_TYPE_BUSINESS = "2";
    /**
     * 值为1表示工作流是移动审批
     */
    public static final int IS_MOBILE_APPROVAL = 1;
    /**
     * 值为0表示工作流不是移动审批
     */
    public static final int IS_NOT_MOBILE_APPROVAL = 0;
    /**
     * 工作流分类默认parent_id为0
     */
    public static final String FLOW_CATEGORY_DEFAULT_PARENT = "0";
}
