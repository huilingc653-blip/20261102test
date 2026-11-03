package com.bytter.guarantee.constant;
/**
 * 合同管理模块常量类
 * 
 * @author zhengg
 *
 */
public class ConConstant {
    // 合同产品类型 CON_PRODUCT_TYPE
    // 合同产品类型：内部贷款 CON_PD_INNERLOAN
    // 合同产品类型：委托贷款 CON_PD_ENTRUSTLOAN
    // 合同产品类型：银行借款 CON_PD_BANKBORROW
    // 合同产品类型:授信管理 CON_PD_RAT
    // 合同签署类型 CON_SIGN_TYPE
    // 合同状态 CON_INFO_STATUS
    // 合同业务类型 CON_BUS_TYPE
    // 合同起草审批状态 CON_DRAFT_STATUS
    // 合同签署审批状态 CON_SIGN_STATUS
    // 合同登记、变更、履约审批状态 CON_OTHER_STATUS
    // 合同终止审批状态 CON_TERMINATE_STATUS
    // 合同日志类型 CON_LOG_TYPE
    public static final String CONLOG_LOG_TYPE = "CON_LOG_TYPE";
    // 合同管理日志信息
    /** * 合同管理日志-99 删除操作 */
    public static final String CONLOG_LOGTYPE_DELETE = "-99";
    /** * 合同管理日志-2签署打回 */
    public static final String CONLOG_LOGTYPE_SIGNREPULSE = "-2";
    /** * 合同管理日志-1审批打回 */
    public static final String CONLOG_LOGTYPE_REPULSE = "-1";
    /** * 合同管理日志01新增 */
    public static final String CONLOG_LOGTYPE_ADD = "01";
    /** * 合同管理日志02修改 */
    public static final String CONLOG_LOGTYPE_UPDATE = "02";
    /** * 合同管理日志03提交 */
    public static final String CONLOG_LOGTYPE_SUBMIT = "03";
    /** * 合同管理日志04批量提交 */
    public static final String CONLOG_LOGTYPE_BATCHSUBMIT = "04";
    /** * 合同管理日志05导入 */
    public static final String CONLOG_LOGTYPE_IMPORT = "05";
    /** * 合同管理日志95审批通过 */
    public static final String CONLOG_LOGTYPE_APP = "95";
    // 日志消息
    /** * 删除数据成功！ */
    public static final String LANG_LOG_DELETE_SUCC = "constract.log.delete.succ";
    /*** 合同起草操作资源资源 */
    public static final String CON_CONDRAFT_RESOURCE_OP = "con:condraft:resource:op";
    /*** 合同登记操作资源资源 */
    public static final String CON_CONREGISTER_RESOURCE_OP = "con:conregister:resource:op";
    /*** 合同变更操作资源资源 */
    public static final String CON_CONCHANGE_RESOURCE_OP = "con:conchange:resource:op";
    /*** 合同履约操作资源资源 */
    public static final String CON_CONPERFORMANCE_RESOURCE_OP = "con:conperformance:resource:op";
    /*** 合同预警规则操作资源 */
    public static final String CONTRACT_CONWARNRULES_OP = "contract:conwarnrules:op";
    /*** 合同类型要素映射操作资源 */
    public static final String CONTRACT_CONPDMAP_OP = "contract:conpdmap:op";
    // FIELD_TYPE 合同类型明细字段类型
    public static final String CON_FIELD_TYPE = "FIELD_TYPE";
    /** * 普通字符 01 */
    public static final String CON_FIELD_TYPE_01 = "01";
    /** * 金额 02 */
    public static final String CON_FIELD_TYPE_02 = "02";
    /** * 数字 03 */
    public static final String CON_FIELD_TYPE_03 = "03";
    /** * 日期 04 */
    public static final String CON_FIELD_TYPE_04 = "04";
    /** * 利率 05 */
    public static final String CON_FIELD_TYPE_05 = "05";
    // CON_PRODUCT_TYPE 合同产品类型
    public static final String CON_PRODUCT_TYPE = "CON_PRODUCT_TYPE";
    /** * 内部贷款 CON_PD_INNERLOAN */
    public static final String CON_PD_INNERLOAN = "CON_PD_INNERLOAN";
    /** * 委托贷款 CON_PD_ENTRUSTLOAN */
    public static final String CON_PD_ENTRUSTLOAN = "CON_PD_ENTRUSTLOAN";
    /** * 银行借款 CON_PD_BANKBORROW */
    public static final String CON_PD_BANKBORROW = "CON_PD_BANKBORROW";
    /** * 授信管理 CON_PD_RAT */
    public static final String CON_PD_RAT = "CON_PD_RAT";
    /** * 被担保管理 CON_PD_GUA */
    public static final String CON_PD_GUA = "CON_PD_GUA";
    /** * 对外担保管理 CON_PD_EXTGUA */
    public static final String CON_PD_EXTGUA = "CON_PD_EXTGUA";
    // CON_SIGN_TYPE 合同签署类型
    public static final String CON_SIGN_TYPE = "CON_SIGN_TYPE";
    /** * 线下签署 0 */
    /** * 线上签署 1 */
    public static final String CON_SIGN_TYPE_0 = "0";
    public static final String CON_SIGN_TYPE_1 = "1";
    // CON_INFO_STATUS 合同状态
    public static final String CON_INFO_STATUS = "CON_INFO_STATUS";
    /** * 起草中 10 */
    public static final Integer CON_INFO_STATUS_10 = 10;
    /** * 已起草 20 */
    public static final Integer CON_INFO_STATUS_20 = 20;
    /** * 签订中 30 */
    public static final Integer CON_INFO_STATUS_30 = 30;
    /** * 登记中 40 */
    public static final Integer CON_INFO_STATUS_40 = 40;
    /** * 已签订 50 */
    public static final Integer CON_INFO_STATUS_50 = 50;
    /** * 履约中 60 */
    public static final Integer CON_INFO_STATUS_60 = 60;
    /** * 变更中 70 */
    public static final Integer CON_INFO_STATUS_70 = 70;
    /** * 终止中 80 */
    public static final Integer CON_INFO_STATUS_80 = 80;
    /** * 已终止 90 */
    public static final Integer CON_INFO_STATUS_90 = 90;
    /** * 已删除 -99 */
    public static final Integer CON_INFO_STATUS_99 = -99;
    // CON_BUS_TYPE 合同业务类型
    public static final String CON_BUS_TYPE = "CON_BUS_TYPE";
    /** * 起草 01 */
    public static final String CON_BUS_TYPE_01 = "01";
    /** * 签署 02 */
    public static final String CON_BUS_TYPE_02 = "02";
    /** * 登记 03 */
    public static final String CON_BUS_TYPE_03 = "03";
    /** * 变更 04 */
    public static final String CON_BUS_TYPE_04 = "04";
    /** * 履约 05 */
    public static final String CON_BUS_TYPE_05 = "05";
    /** * 终止 06 */
    public static final String CON_BUS_TYPE_06 = "06";
    // CON_DRAFT_STATUS 合同起草审批状态
    public static final String CON_DRAFT_STATUS = "CON_DRAFT_STATUS";
    /** * 暂存 0 */
    public static final Integer CON_DRAFT_STATUS_0 = 0;
    /** * 待审批 11 */
    public static final Integer CON_DRAFT_STATUS_11 = 11;
    /** * 审批打回 -1 */
    public static final Integer CON_DRAFT_STATUS_1 = -1;
    /** * 审批通过 95 */
    public static final Integer CON_DRAFT_STATUS_95 = 95;
    /** * 签署打回 -2 */
    public static final Integer CON_DRAFT_STATUS_2 = -2;
    /** * 已删除 -99 */
    public static final Integer CON_DRAFT_STATUS_99 = -99;
    // CON_SIGN_STATUS 合同签署审批状态
    public static final String CON_SIGN_STATUS = "CON_SIGN_STATUS";
    /** * 待签署 0 */
    public static final Integer CON_SIGN_STATUS_0 = 0;
    /** * 待审批 11 */
    public static final Integer CON_SIGN_STATUS_11 = 11;
    /** * 审批打回 -1 */
    public static final Integer CON_SIGN_STATUS_1 = -1;
    /** * 审批通过 95 */
    public static final Integer CON_SIGN_STATUS_95 = 95;
    // CON_OTHER_STATUS 合同登记、变更、履约、终止审批状态
    public static final String CON_OTHER_STATUS = "CON_OTHER_STATUS";
    /** * 暂存 0 */
    public static final Integer CON_OTHER_STATUS_0 = 0;
    /** * 待审批 11 */
    public static final Integer CON_OTHER_STATUS_11 = 11;
    /** * 审批打回 -1 */
    public static final Integer CON_OTHER_STATUS_1 = -1;
    /** * 审批通过 95 */
    public static final Integer CON_OTHER_STATUS_95 = 95;
    /** * 已删除 -99 */
    public static final Integer CON_OTHER_STATUS_99 = -99;
    /** 合同终止审批状态 CON_TERMINATE_STATUS */
    public static final String CON_TERMINATE_STATUS = "CON_TERMINATE_STATUS";
    /** 0 暂存 */
    public static final Integer CON_TERMINATE_STATUS1 = 1;
    /** 1 可终止 */
    public static final Integer CON_TERMINATE_STATUS_0 = 0;
    /** 11 待审批 */
    public static final Integer CON_TERMINATE_STATUS_11 = 11;
    /** -1 审批打回 */
    public static final Integer CON_TERMINATE_STATUS_1 = -1;
    /** 95 终止成功 */
    public static final Integer CON_TERMINATE_STATUS_95 = 95;
    /** -99 已删除 */
    public static final Integer CON_TERMINATE_STATUS_99 = -99;
    // 有效标志：0业务中1业务完成2已作废
    public static final String CON_VALID_SIGN = "CON_VALID_SIGN";
    /** 0业务中 */
    public static final String CON_VALID_SIGN_0 = "0";
    /** 1业务完成 */
    public static final String CON_VALID_SIGN_1 = "1";
    /** 2已作废 */
    public static final String CON_VALID_SIGN_2 = "2";
    // 合同业务挂接标志：0已取消1已挂接
    /** 0已取消 */
    public static final String CON_BUSHOOK_VALID_SIGN_0 = "0";
    /** 1已挂接 */
    public static final String CON_BUSHOOK_VALID_SIGN_1 = "1";
    // 合同业务明细信息必填标记
    /** 0非必填 */
    public static final String CON_REQUIREDSIGN_0 = "0";
    /** 1必填 */
    public static final String CON_REQUIREDSIGN_1 = "1";
    /** 单位合同权限 */
    public static final String CONTRACT_TYPE_MANAGE_FST_CORP = "contract:type:manage:fst:corp";
    /** 共享合同权限 */
    public static final String CONTRACT_TYPE_MANAGE_FST_DOT = "contract:type:manage:fst:dot";
    /** 状态转换方法 */
    public static final String CONTRACT_CHBOX_CAST_OPT = "on";
    /** 是共享 */
    public static final String CONTRACT_TYPE_COMMON_SHARESIGN = "1";
    /** 是单位 */
    public static final String CONTRACT_TYPE_CORP_SHARESIGN = "0";
    // 操作类型
    /**
     * 操作-保存
     */
    public static final String CON_OPERATE_SAVE = "save";
    /**
     * 操作-提交
     */
    public static final String CON_OPERATE_SUBMIT = "submit";
    /**
     * 操作-编辑
     */
    public static final String CON_OPERATE_EDIT = "edit";
    // 工作流审批结果
    /** * 审批通过！ */
    public static final String CON_LOG_APP_SUCC = "contract.log.app.succ";
    /** * 审批打回！ */
    public static final String CON_LOG_APP_BACK = "contract.log.app.back";
    /** 合同类型是否开关常量 **/
    public static final String CON_TYPE_SETING_CHEBOX = "on";
    /**
     * 合同签署资源
     */
    public static final String CONTRACT_SIGNED_MANAGE_OPT = "contract:signed:manage:opt";
    /**
     * 同意操作权限
     */
    public static final String CONTRACT_SIGNED_SIGNED_POWER = "signed";
    /**
     * 拒绝操作权限
     */
    public static final String CONTRACT_SIGNED_REFUSED_POWER = "refused";
    /**
     * 拒绝操作权限
     */
    public static final String CONTRACT_SUSPEND_STOP_POWER = "stop";
    /**
     * 拒绝操作权限
     */
    public static final String CONTRACT_SUSPEND_STOP_CANCEL_POWER = "cancel";
    /**
     * 合同终止资源
     */
    public static final String CONTRACT_SUSPEND_MANAGE_OPT = "contract:suspend:manage:opt";
    /**
     * 合同查询资源权限
     */
    public static final String CONTRACT_OVERVIEW_QUERY_OPT = "contract:overview:query:opt";
    // CON_SIGN_STATUS 合同终止审批状态
    public static final String CON_SUSPEND_STATUS = "CON_SUSPEND_STATUS";
    /** * 暂存 */
    public static final Integer CON_SUSPEND_STATUS_0 = 0;
    /** * 审批中11 */
    public static final Integer CON_SUSPEND_STATUS_11 = 11;
    /** * 可终止 */
    public static final Integer CON_SUSPEND_STATUS_Y = 1;
    /** * 审批打回 -1 */
    public static final Integer CON_SUSPEND_STATUS_1 = -1;
    /** * 终止成功 */
    public static final Integer CON_SUSPEND_STATUS_95 = 95;
    /** * 已删除 */
    public static final Integer CON_SUSPEND_STATUS_99 = -99;
}
