package com.bytter.check.constant;
/**
 * 核算模块资源常量类
 * @author liyh
 * @since 2011-8-21
 */
public class FinacialResourceConstant {
    /**
     * 核算 - 基础模块
     */
	/*** 会计科目 - 查询*/
    public static final String FINACIAL_PARAMCONFIG_SUBJECT_QUERY = "finacial:paramconfig:subject:query";
    /*** 会计科目 - 导入*/
    public static final String FINACIAL_PARAMCONFIG_SUBJECT_IMPORT = "finacial:paramconfig:subject:import";
    /*** 会计科目 - 编辑*/
    public static final String FINACIAL_PARAMCONFIG_SUBJECT_EDIT = "finacial:paramconfig:subject:edit";
    /*** 会计科目 - 删除*/
    public static final String FINACIAL_PARAMCONFIG_SUBJECT_DELETE = "finacial:paramconfig:subject:delete";
    /**
     * 核算 - 对账
     */
    /*** 中心对账单 - 查询*/
    public static final String FINACIAL_CHECK_CENTRALITY_QUERY = "finacial:check:centrality:query";
    /*** 中心对账单 - 编辑*/
    public static final String FINACIAL_CHECK_CENTRALITY_EDIT = "finacial:check:centrality:edit";
    /*** 中心对账单 - 删除*/
    public static final String FINACIAL_CHECK_CENTRALITY_DELETE = "finacial:check:centrality:delete";
    /*** 中心对账 - 数据生成查询*/
    public static final String FINACIAL_CHECK_CENTRALITY_CREATE_QUERY = "finacial:check:centrality:create:query";
    /*** 中心对账 - 数据生成*/
    public static final String FINACIAL_CHECK_CENTRALITY_CREATE = "finacial:check:centrality:create";
    /*** 中心对账 - 余额初始化查询*/
    public static final String FINACIAL_CHECK_CENTRALITY_BALANCE_INIT_QUERY = "finacial:check:centrality:balance:init:query";
    /*** 中心对账 - 余额初始化*/
    public static final String FINACIAL_CHECK_CENTRALITY_BALANCE_INIT = "finacial:check:centrality:balance:init";
    /*** 中心对账 - 对账*/
    public static final String FINACIAL_CHECK_CENTRALITY_CHECK = "finacial:check:centrality:check";
    /*** 自动对账配置 - 查询*/
    public static final String FINACIAL_CHECK_FCAUTOCHECKCONFIG_QUERY = "finacial:check:fcautocheckconfig:query";
    /*** 自动对账配置 - 操作*/
    public static final String FINACIAL_CHECK_FCAUTOCHECKCONFIG_OP = "finacial:check:fcautocheckconfig:op";
    /*** 自动对账结果 - 查询*/
    public static final String FINACIAL_CHECK_AOTORESULT_QUERY = "finacial:check:aotoResult:query";
    /*** 银行对账单 - 查询*/
    public static final String FINACIAL_CHECK_BANK_QUERY = "finacial:check:bank:query";
    /*** 银行对账单- 编辑*/
    public static final String FINACIAL_CHECK_BANK_EDIT = "finacial:check:bank:edit";
    /*** 银行对账单 - 删除*/
    public static final String FINACIAL_CHECK_BANK_DELETE = "finacial:check:bank:delete";
    /*** 银行接口导入 - 查询*/
    public static final String FINACIAL_CHECK_BANK_INTERFACE_QUERY = "finacial:check:bank:interface:query";
    /*** 银行接口导入 - 提取*/
    public static final String FINACIAL_CHECK_BANK_INTERFACE_EXTRACT = "finacial:check:bank:interface:extract";
    /*** 银行接口导入 - 保存*/
    public static final String FINACIAL_CHECK_BANK_INTERFACE_SAVE = "finacial:check:bank:interface:save";
    /*** 银企对账查询 - 查询*/
    public static final String FINACIAL_CHECK_BANK_ENTERPRISE_QUERY = "finacial:check:bank:enterprise:query";
    /*** 反结转 - 查询*/
    public static final String FINACIAL_CHECK_OPPOSITE_CARRYOVER_QUERY = "finacial:check:opposite:carryover:query";
    /*** 余额调节表 - 查询*/
    public static final String FINACIAL_CHECK_BALANCE_ADJUST_QUERY = "finacial:check:balance:adjust:query";
    /*** ERP对账单查询 - 查询*/
    public static final String FINACIAL_CHECK_ERP_QUERY = "finacial:check:erp:query";
    /*** ERP数据维护 - 查询*/
    public static final String FINACIAL_CHECK_ERP_DATA_QUERY = "finacial:check:erp:data:query";
    /*** ERP数据维护 - 编辑*/
    public static final String FINACIAL_CHECK_ERP_DATA_EDIT = "finacial:check:erp:data:edit";
    /*** ERP数据维护 - 删除*/
    public static final String FINACIAL_CHECK_ERP_DATA_DELETE = "finacial:check:erp:data:delete";
    /*** ERP数据导入 - 查询*/
    public static final String FINACIAL_CHECK_ERP_DATA_IMPROT = "finacial:check:erp:data:improt";
    /*** ERP接口导入 - 查询*/
    public static final String FINACIAL_CHECK_ERP_DATA_INTERFACE_QUERY = "finacial:check:erp:data:interface:query";
    /*** ERP接口导入 - 提取*/
    public static final String FINACIAL_CHECK_ERP_DATA_INTERFACE_EXTRACT = "finacial:check:erp:data:interface:extract";
    /*** ERP接口导入 - 保存*/
    public static final String FINACIAL_CHECK_ERP_DATA_INTERFACE_SAVE = "finacial:check:erp:data:interface:save";
    /*** ERP对账 - 对账*/
    public static final String FINACIAL_CHECK_ERP_CHECK = "finacial:check:erp:check";
    /**ERP初始化余额查询**/
    public static final String FINACIAL_CHECK_ERP_BALANCE_INIT_QUERY = "finacial:check:erp:balance:init:query";
    /***ERP - 余额初始化*/
    public static final String FINACIAL_CHECK_ERP_BALANCE_INIT = "finacial:check:erp:balance:init";
    /**
     * 表外
     */
    /**表外流水账查询**/
    public static final String FINACIAL_OUTACCBOOK_QUERY = "finacial:outaccbook:query";
    /**记帐凭证查询**/
    public static final String FINACIAL_OUTACCBOOK_VOUCHERLIST_QUERY = "finacial:outaccbook:voucherList:query";
    /**分户账查询**/
    public static final String FINACIAL_OUTACCBOOK_DIVIDEDACCOUNTLIST_QUERY = "finacial:outaccbook:dividedAccountList:query";
    /**总账查询**/
    public static final String FINACIAL_OUTACCBOOK_LEDGERQUERYLIST_QUERY = "finacial:outaccbook:ledgerQueryList:query";
    /**余额查询**/
    public static final String FINACIAL_OUTACCBOOK_BALANCELIST_QUERY = "finacial:outaccbook:balanceList:query";
    /**日计表查询**/
    public static final String FINACIAL_OUTACCBOOK_DAILYACCOUNTLIST_QUERY = "finacial:outaccbook:dailyAccountList:query";
    /**科目余额汇总查询***/
    public static final String FINACIAL_OUTACCBOOK_SUBJECTBALANCELIST_QUERY = "finacial:outaccbook:subjectBalanceList:query";
    /**
     * 表外-经办
     */
    /**表外记账**/
    public static final String FINACIAL_OUTACCBOOK_TALL_QUERY = "finacial:outaccbook:tall:query";
    /**作废申请**/
    public static final String FINACIAL_OUTACCBOOK_TALLINVALID_QUERY = "finacial:outaccbook:tallInvalid:query";
    /**作废修改**/
    public static final String FINACIAL_OUTACCBOOK_TALLYCANCELUPDATE_QUERY = "finacial:outaccbook:TallyCancelUpdat:query";
    /**冲账申请**/
    public static final String FINACIAL_OUTACCBOOK_TALLYCHARGE_QUERY = "finacial:outaccbook:tallycharge:query";
    /**冲账修改**/
    public static final String FINACIAL_OUTACCBOOK_TALLYCHARGEUPDATE_QUERY = "finacial:outaccbook:TallyChargeUpdate:query";
    /**
     * 利息
     */
    /**积数查询**/
    public static final String FINACIAL_ACCUBALANCEF_QUERY = "finacial:accubalancef:query";
    /**资金池积数查询**/
    public static final String FINACIAL_ACCUBALANCEPOOL_QUERY = "finacial:accubalancepool:query";
    /**利息管理调整积数查询**/
    public static final String FINACIAL_ACCUBALANCEA_QUERY = "finacial:accubalancea:query";
    /**利息管理调整积数--保存**/
    public static final String FINACIAL_ACCUBALANCEA_SAVE = "finacial:accubalancea:save";
    /**利息明细查询**/
    public static final String FINACIAL_FCINTERESTDTL_QUERY = "finacial:fcinterestdtl:query";
    /**利息明细打印**/
    public static final String FINACIAL_FCINTERESTDTL_PRINT = "finacial:fcinterestdtl:print";
    /**预提明细查询**/
    public static final String FINACIAL_FCPREDRAWDTL_DETAIL_QUERY = "finacial:fcpredrawdtl:detail:query";
    /**利息预提情况查询**/
    public static final String FINACIAL_FCPREDRAWDTL_FACTSHEET_QUERY = "finacial:fcpredrawdtl:factsheet:query";
    /**批量计息-经办*/
    public static final String FINACIAL_INTEREST_CALCBYBATCH_HAND = "finacial:interest:calcbybatch:hand";
    /**批量计息-复核*/
    public static final String FINACIAL_INTEREST_CALCBYBATCH_CHECK = "finacial:interest:calcbybatch:check";
    /**资金池批量计息-经办*/
    public static final String FINACIAL_POOLINTEREST_CALCBYBATCH_HAND = "finacial:poolinterest:calcbybatch:hand";
    /**资金池批量计息-复核*/
    public static final String FINACIAL_POOLINTEREST_CALCBYBATCH_CHECK = "finacial:poolinterest:calcbybatch:check";
    /**冲预提-经办*/
    public static final String FINACIAL_INTEREST_RUNSH_HAND = "finacial:interest:runsh:hand";
    /**冲预提-复核*/
    public static final String FINACIAL_INTEREST_RUNSH_CHECK = "finacial:interest:runsh:check";
    /**应收利息还款-登记*/
    public static final String FINACIAL_INTEREST_FCLOANREPAY_HAND = "finacial:interest:fcloanrepay:hand";
    /**应收利息还款-复核*/
    public static final String FINACIAL_INTEREST_FCLOANREPAY_CHECK = "finacial:interest:fcloanrepay:check";
    /**
     * 期终-查询
     */
    /**日结状态查询**/
    public static final String FINACIAL_FCCARRY_DAYREPORT_QUERY = "finacial:fccarry:dayreport:query";
    /**日结日志查询**/
    public static final String FINACIAL_FCCARRY_LOG_QUERY = "finacial:fccarry:log:query";
    /**账户每日余额表**/
    public static final String FINACIAL_FCACCOUNTBALANCE_QUERY = "finacial:fcaccountbalance:query";
    /**科目日结单**/
    public static final String FINACIAL_FCSUBJECTDAY_QUERY = "finacial:fcsubjectday:query";
    /**科目汇总表**/
    public static final String FINACIAL_FCSUBJECTDAY_SUM_QUERY = "finacial:fcsubjectday:sum:query";
    /**
     * 报表
     */
    /**账户实时余额查询**/
    public static final String FINACIAL_BALANCEQUERY_QUERY = "finacial:balancequery:query";
    /**账户变化情况表**/
    public static final String FINACIAL_ACCOUNT_CHANGE_QUERY = "finacial:account:change:query";
    /**记账凭证导入操作权限**/
    public static final String FINACIAL_VOUCHERIMPORT_APPLY = "finacial:voucherimport:apply";
    /**科目账户余额表查询*/
    public static final String FINACIAL_SUBJECTACCBALANCE_QUERY = "finacial:subjectAccBalance:query";
    /*** 现金流量 - 查询*/
    public static final String FINACIAL_PARAMCONFIG_CASHITEM_QUERY = "finacial:paramconfig:cashItem:query";
    /*** 现金流量 - 编辑*/
    public static final String FINACIAL_PARAMCONFIG_CASHITEM_EDIT = "finacial:paramconfig:cashItem:edit";
    /*** 现金流量 - 删除*/
    public static final String FINACIAL_PARAMCONFIG_CASHITEM_DELETE = "finacial:paramconfig:cashItem:delete";
}
