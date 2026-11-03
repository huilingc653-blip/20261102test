package com.bytter.workflow.constant;

/**
 * 流程配置相关常量
 * Created by chenlw on 2019/02/28.
 *
 * @history
 * 1.  2019-12-30 chenlw  单据配置功能已被启用，其功能可由定制显示字段来代替！TYPE_INVOICE_CONFIG字段废弃。
 * 2.  2019-12-31 chenlw  新增固定显示的字段KEY和名称。

 */
public class OptionConstant {

    /**
     * type = 1：流程条件
     */
    public static final int TYPE_CATEGORY_CONDITION = 1;
    /**
     * type = 2：任务节点
     */
    public static final int TYPE_USER_TASK = 2;
    /**
     * type = 3：消息模板
     */
    public static final int TYPE_MSG_TEMPLATE = 3;
    /**
     * type = 4：单据配置
     *
     * created by chenlw on 2019/12/30
     * 注意：单据配置功能已被启用，其功能可由定制显示字段来代替！
     *
     */
    public static final int TYPE_INVOICE_CONFIG = 4;
    /**
     * type = 5：审批数据定制显示
     */
    public static final int TYPE_EXTEND_PARAMS = 5;

    /**
     * type = 5，字段是否可见：值为1表示可见。
     */
    public static final int EXTEND_PARAMS_VISIBLE = 1;

    /**
     * type = 5，字段为固定显示的字段。
     * 固定显示的字段，在定制显示字段页面不允许删除。
     */
    public static final int EXTEND_PARAMS_IS_STABLE_FIELD = 1;


    /*************************  定制显示字段-固定显示的字段名称  ***************************/
    public static final String EXTEND_PARAM_BUSINESSID_KEY = "businessId";
    public static final String EXTEND_PARAM_BUSINESSID_KEY_NAME = "单据号";
    public static final String EXTEND_PARAM_CORP_NAME_KEY = "corpName";
    public static final String EXTEND_PARAM_CORP_NAME_KEY_NAME = "申请单位";
    public static final String EXTEND_PARAM_USERNAME_KEY = "username";
    public static final String EXTEND_PARAM_USERNAME_KEY_NAME = "申请人";
    public static final String EXTEND_PARAM_CREATE_TIME_KEY = "createTime";
    public static final String EXTEND_PARAM_CREATE_TIME_KEY_NAME = "申请时间";
    /*************************  定制显示字段-固定显示的字段名称  ***************************/

    public static final String DYNAMIC_APPROVERS = "dynamicApprovers";
}
