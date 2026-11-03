package com.bytter.supply.constant;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ScfEntBankOpenConstant {

    /**指令发送模式 手动发送0*/
    public static final String SCF_INSTRUMENT_MODE_0 = "0";

    /**指令发送模式 自动发送1*/
    public static final String SCF_INSTRUMENT_MODE_1 = "1";

    /**指令标识  应付债权凭证标识0*/
    public static final String SCF_PAYABLE_INSTRUMENT_SIGN_0 = "0";

    /**指令标识  应收债权凭证标识1*/
    public static final String SCF_RECEIVABLE_INSTRUMENT_SIGN_1 = "1";
    // ====================== 应收债权凭证 scf.interface.receivable ======================
    /**
     * 应收债权凭证-债权凭证查询：1
     */
    public static final String RECEIVABLE_QUERY_CREDIT_QUALITY = "1";

    /**
     * 应收债权凭证-债权凭证签收：2
     */
    public static final String RECEIVABLE_VOUCHER_SIGN = "2";

    /**
     * 应收债权凭证-债权凭证转让：3
     */
    public static final String RECEIVABLE_VOUCHER_TRANSFER = "3";

    /**
     * 应收债权凭证-转让结果：4
     */
    public static final String RECEIVABLE_TRANSFER_RESULT = "4";

    /**
     * 应收债权凭证-融资申请：5
     */
    public static final String RECEIVABLE_FINANCE_APPLY = "5";

    /**
     * 应收债权凭证-融资结果查询：6
     */
    public static final String RECEIVABLE_FINANCE_RESULT_QUERY = "6";

    /**
     * 应收债权凭证-到期收款查询：7
     */
    public static final String RECEIVABLE_DUE_COLLECT_QUERY = "7";

    // ====================== 应付债卷凭证 scf.interface.payable ======================
    /**
     * 应付债券凭证-凭证开立：61
     */
    public static final String PAYABLE_VOUCHER_OPEN = "61";

    /**
     * 应付债券凭证-开立撤销：62
     */
    public static final String PAYABLE_VOUCHER_CANCEL = "62";

    /**
     * 应付债券凭证-兑付结果查询：63
     */
    public static final String PAYABLE_CASH_RESULT_QUERY = "63";

    // ====================== 额度管理 scf.interface.credit ======================
    /**
     * 额度管理-额度查询：60
     */
    public static final String CREDIT_LIMIT_QUERY = "60";

    // ====================== 额度管理 bisExc.voucherStat ======================
    /**
     * 交易状态
     */
    /** 待发送 */
    public static final String INSTRUCTION_PENDING_SEND = "1";

    /** 发送失败 */
    public static final String INSTRUCTION_SEND_FAIL = "2";

    /** 发送成功 */
    public static final String INSTRUCTION_SEND_SUCCESS = "3";

    /** 打回 */
    public static final String INSTRUCTION_REJECTED = "4";

    /** 等待结果 */
    public static final String INSTRUCTION_WAITING_RESULT = "5";

    /** 交易成功 */
    public static final String INSTRUCTION_TRADE_SUCCESS = "6";

    /** 交易失败 */
    public static final String INSTRUCTION_TRADE_FAIL = "7";

    // ====================== 字典组编码常量（分组标识） ======================
    /**
     * 字典组编码：应收债权凭证
     */
    public static final String DICT_GROUP_RECEIVABLE = "scf.interface.receivable";

    /**
     * 字典组编码：应付债券凭证
     */
    public static final String DICT_GROUP_PAYABLE = "scf.interface.payable";

    /**
     * 字典组编码：额度管理
     */
    public static final String DICT_GROUP_CREDIT = "scf.interface.credit";

    // ====================== 接口编码→名称映射表 ======================
    public static final Map<String, String> INTERFACE_NAME_MAP;

    static {
        Map<String, String> map = new HashMap<>();
        map.put(RECEIVABLE_QUERY_CREDIT_QUALITY, "应收债权凭证-债权凭证查询");
        map.put(RECEIVABLE_VOUCHER_SIGN, "应收债权凭证-债权凭证签收");
        map.put(RECEIVABLE_VOUCHER_TRANSFER, "应收债权凭证-债权凭证转让");
        map.put(RECEIVABLE_TRANSFER_RESULT, "应收债权凭证-转让结果");
        map.put(RECEIVABLE_FINANCE_APPLY, "应收债权凭证-融资申请");
        map.put(RECEIVABLE_FINANCE_RESULT_QUERY, "应收债权凭证-融资结果查询");
        map.put(RECEIVABLE_DUE_COLLECT_QUERY, "应收债权凭证-到期收款查询");
        map.put(PAYABLE_VOUCHER_OPEN, "应付债券凭证-凭证开立");
        map.put(PAYABLE_VOUCHER_CANCEL, "应付债券凭证-开立撤销");
        map.put(PAYABLE_CASH_RESULT_QUERY, "应付债券凭证-兑付结果查询");
        map.put(CREDIT_LIMIT_QUERY, "额度管理-额度查询");
        INTERFACE_NAME_MAP = Collections.unmodifiableMap(map);
    }

    // ====================== 便捷获取方法 ======================
    public static String getInterfaceName(String code) {
        return INTERFACE_NAME_MAP.getOrDefault(code, "未知接口");
    }
}

