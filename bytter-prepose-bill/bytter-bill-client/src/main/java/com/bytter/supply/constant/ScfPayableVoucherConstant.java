package com.bytter.supply.constant;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ScfPayableVoucherConstant {

    //暂存标志符号
    public static final String SCF_PAYABLE_SAVE_FLAG_0 = "0";
    //提交标志符号
    public static final String SCF_PAYABLE_SAVE_FLAG_1 = "1";
    //线下符号
    public static final String SCF_PAYABLE_NOT_ONLINE_0 = "0";
    //线上符号
    public static final String SCF_PAYABLE_IS_ONLINE_1 = "1";
    //流程状态
    /**暂存 0*/
    public static final String SCF_PAYABLE_STATUS_TEMP = "0";//暂存
    /**开立打回 -10*/
    public static final String SCF_PAYABLE_STATUS_OPENED_BACK = "-10";//开立打回

    /**兑付打回 -11*/
    public static final String SCF_PAYABLE_STATUS_REDEMPTION_BACK = "-11";//兑付打回
    /**已删除 -3*/
    public static final String SCF_PAYABLE_STATUS_DELETE = "-3";//已删除
    /**已作废 -2*/
    public static final String SCF_PAYABLE_STATUS_CHANGE_REFUSE = "-2";//已作废
    /**开立审批中 11*/
    public static final String SCF_PAYABLE_STATUS_WAIT_OPENED_CHECK = "11";//开立待审批
    /**兑付审批中 12*/
    public static final String SCF_PAYABLE_STATUS_WAIT_REDEMPTION_CHECK = "12";//兑付待审批
    /**开立审批通过 95*/
    public static final String SCF_PAYABLE_STATUS_OPENED_CHECK = "95";//开立审批通过

    /**兑付审批通过 96*/
    public static final String SCF_PAYABLE_STATUS_REDEMPTION_CHECK = "96";//兑付审批通过


    //业务状态
    /**未开立*/
    public static final String SCF_PAYABLE_BUSINESS_UNOPENED = "0";//未开立
    /**开立中*/
    public static final String SCF_PAYABLE_BUSINESS_OPENING = "1";//开立中
    /**已开立*/
    public static final String SCF_PAYABLE_BUSINESS_OPENED = "2";//已开立
    /**已拒签*/
    public static final String SCF_PAYABLE_BUSINESS_REJECTED = "3";//已拒签
    /**已撤销*/
    public static final String SCF_PAYABLE_BUSINESS_CANCELED = "4";//已撤销
    /**已兑付*/
    public static final String SCF_PAYABLE_BUSINESS_REDEEMED = "5";//已兑付
    /**兑付中*/
    public static final String SCF_PAYABLE_BUSINESS_REDEEMING = "6";//兑付中
    /**已作废*/
    public static final String SCF_PAYABLE_BUSINESS_VOIDED = "7";//已作废


    // ==================== 兑付审批状态码 ====================

    public static final String SCF_REDEMPTION_STATUS_TEMP = "0";//暂存
    public static final String SCF_REDEMPTION_STATUS_BACK = "-1";//打回
    public static final String SCF_REDEMPTION_STATUS_DELETE = "-3";//已删除
    public static final String SCF_REDEMPTION_STATUS_CHANGE_REFUSE = "-2";//已作废
    public static final String SCF_REDEMPTION_STATUS_WAIT_CHECK = "11";//待审批
    public static final String SCF_REDEMPTION_STATUS_CHECK = "95";//审批通过

    // ==================== 流程状态映射表 ====================
    public static final Map<String, String> PROCESS_STATUS_MAP;

    // ==================== 业务状态映射表 ====================
    public static final Map<String, String> BUSINESS_STATUS_MAP;

    public static final Map<String, String> REDMPTION_STATUS_MAP;

    static {
        // 初始化流程状态映射
        Map<String, String> processMap = new HashMap<>();
        // 单据状态名称只有 暂存/打回/已作废/审批中/审批通过 五种，开立与兑付共用同一套显示名，
        // 与字典 scf.payable.approvalStatus 维护的 5 条保持一致。
        // 这里刻意保留全部 9 个码值（含 -3 及 -11/12/96），避免历史数据落到 getApprovalStatusName
        // 的兜底值"未知流程状态"。
        processMap.put(SCF_PAYABLE_STATUS_TEMP, "暂存");
        processMap.put(SCF_PAYABLE_STATUS_OPENED_BACK, "打回");
        processMap.put(SCF_PAYABLE_STATUS_REDEMPTION_BACK, "打回");
        processMap.put(SCF_PAYABLE_STATUS_DELETE, "已删除");
        processMap.put(SCF_PAYABLE_STATUS_CHANGE_REFUSE, "已作废");
        processMap.put(SCF_PAYABLE_STATUS_WAIT_OPENED_CHECK, "审批中");
        processMap.put(SCF_PAYABLE_STATUS_WAIT_REDEMPTION_CHECK, "审批中");
        processMap.put(SCF_PAYABLE_STATUS_OPENED_CHECK, "审批通过");
        processMap.put(SCF_PAYABLE_STATUS_REDEMPTION_CHECK, "审批通过");
        PROCESS_STATUS_MAP = Collections.unmodifiableMap(processMap);

        // 初始化业务状态映射
        Map<String, String> redemptionProcessMap = new HashMap<>();
        redemptionProcessMap.put(SCF_REDEMPTION_STATUS_TEMP, "暂存");
        redemptionProcessMap.put(SCF_REDEMPTION_STATUS_BACK, "打回");
        redemptionProcessMap.put(SCF_REDEMPTION_STATUS_DELETE, "已删除");
        redemptionProcessMap.put(SCF_REDEMPTION_STATUS_CHANGE_REFUSE, "已作废");
        redemptionProcessMap.put(SCF_REDEMPTION_STATUS_WAIT_CHECK, "待审批");
        redemptionProcessMap.put(SCF_REDEMPTION_STATUS_CHECK, "审批通过");
        REDMPTION_STATUS_MAP = Collections.unmodifiableMap(redemptionProcessMap);
        // 初始化业务状态映射
        Map<String, String> businessMap = new HashMap<>();
        businessMap.put(SCF_PAYABLE_BUSINESS_UNOPENED, "未开立");
        businessMap.put(SCF_PAYABLE_BUSINESS_OPENING, "开立中");
        businessMap.put(SCF_PAYABLE_BUSINESS_OPENED, "已开立");
        businessMap.put(SCF_PAYABLE_BUSINESS_REJECTED, "已拒签");
        businessMap.put(SCF_PAYABLE_BUSINESS_CANCELED, "已撤销");
        businessMap.put(SCF_PAYABLE_BUSINESS_REDEEMED, "已兑付");
        businessMap.put(SCF_PAYABLE_BUSINESS_REDEEMING, "兑付中");
        businessMap.put(SCF_PAYABLE_BUSINESS_VOIDED, "已作废");
        BUSINESS_STATUS_MAP = Collections.unmodifiableMap(businessMap);
    }

    // ==================== 便捷获取方法 ====================
    public static String getApprovalStatusName(String code) {
        return PROCESS_STATUS_MAP.getOrDefault(code, "未知流程状态");
    }

    public static String getBusinessStatusName(String code) {
        return BUSINESS_STATUS_MAP.getOrDefault(code, "未知业务状态");
    }

    public static String getRedemptionStatusName(String code) {
        return REDMPTION_STATUS_MAP.getOrDefault(code, "未知兑付状态");
    }
}
