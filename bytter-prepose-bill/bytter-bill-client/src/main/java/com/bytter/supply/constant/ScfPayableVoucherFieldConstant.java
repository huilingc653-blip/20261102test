package com.bytter.supply.constant;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 应付债权凭证新增字段枚举常量
 * 单据来源 / 融资付息方式
 */
public class ScfPayableVoucherFieldConstant {

    // ==================== 单据来源(复用字典组 scf.voucher.source) ====================
    /** 手工新增 */
    public static final String VOUCHER_SOURCE_MANUAL = "MANUAL";
    /** 付款单生成 */
    public static final String VOUCHER_SOURCE_PAYMENT = "PAYMENT";
    /** 付款单生成(ERP) */
    public static final String VOUCHER_SOURCE_ERP = "ERP";
    /** 系统生成(供应链金融平台) */
    public static final String VOUCHER_SOURCE_SUPPLY_PLATFORM = "SUPPLY_PLATFORM";

    // ==================== 融资付息方式(参照 scf_loan_info.interest_mode) ====================
    /** 卖方付息 */
    public static final String INTEREST_MODE_SELLER = "1";
    /** 买方付息 */
    public static final String INTEREST_MODE_BUYER = "2";

    // ==================== 单据来源映射表 ====================
    public static final Map<String, String> VOUCHER_SOURCE_MAP;

    // ==================== 融资付息方式映射表 ====================
    public static final Map<String, String> INTEREST_MODE_MAP;

    /** 中文->编码（反向映射，用于入参VO中文转存数据库编码） */
    public static final Map<String, String> INTEREST_MODE_REVERSE_MAP;

    static {
        Map<String, String> sourceMap = new HashMap<>();
        sourceMap.put(VOUCHER_SOURCE_MANUAL, "手工新增");
        sourceMap.put(VOUCHER_SOURCE_PAYMENT, "付款单生成");
        sourceMap.put(VOUCHER_SOURCE_ERP, "ERP");
        sourceMap.put(VOUCHER_SOURCE_SUPPLY_PLATFORM, "供应链金融平台");
        VOUCHER_SOURCE_MAP = Collections.unmodifiableMap(sourceMap);

        Map<String, String> interestMap = new HashMap<>();
        interestMap.put(INTEREST_MODE_SELLER, "卖方付息");
        interestMap.put(INTEREST_MODE_BUYER, "买方付息");
        INTEREST_MODE_MAP = Collections.unmodifiableMap(interestMap);

        // 构建反向映射：付息方式：中文 -> code
        Map<String, String> reverseInterestMap = new HashMap<>();
        for (Map.Entry<String, String> entry : interestMap.entrySet()) {
            reverseInterestMap.put(entry.getValue(), entry.getKey());
        }
        INTEREST_MODE_REVERSE_MAP = Collections.unmodifiableMap(reverseInterestMap);
    }

    // ==================== 便捷获取方法 ====================
    public static String getVoucherSourceName(String code) {
        return VOUCHER_SOURCE_MAP.getOrDefault(code, "");
    }

    public static String getInterestModeName(String code) {
        return INTEREST_MODE_MAP.getOrDefault(code, "");
    }


    /**
     * 付息方式：中文 → 编码（VO入参中文转数据库存储编码）
     * @param desc 中文：卖方付息 / 买方付息
     * @return SELLER / BUYER，找不到返回null
     */
    public static String toInterestCode(String desc) {
        if (desc == null || desc.trim().length() == 0) {
            return null;
        }
        return INTEREST_MODE_REVERSE_MAP.get(desc.trim());
    }
}
