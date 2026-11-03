package com.bytter.system.constants;

import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.utils.StringUtils;

/**
 * @author lifh
 * @description
 * @createDate 2019/5/24
 */
public class ExternalCorpConstants {
    public static final String AUTO_AUDIT_PASS = "自动复核通过";

    /**
     * 单位缓存
     */
    public static final String CORP_CACHE_NAME = "corpCache";

    /**
     * 英文大小写字母和英文标点
     */
    public static final String EXTERNALCORP_ENGLISHONLY_REGEX = "^[A-Za-z0-9\\s.,!?;:'\"()-]+$";

    /**
     * 英文大小写字母和数字
     */
    public static final String EXTERNALCORP_ALPHANUMERIC_REGEX = "^[a-zA-Z0-9]+$";

    /**
     * 邮箱格式
     */
    public static final String EXTERNALCORP_EMAIL_REGEX = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";

    /**
     * 资金收付产品 PAYPAL
     */
    public static final String EXTERNALCORP_QUICKPAY_PAYPAL_ID = "4c182f17455f4614a79d66bdfd755c8d";

    /**
     * 资金收付产品 FPS
     */
    public static final String EXTERNALCORP_QUICKPAY_FPS_ID = "53f8408e666b42b9abbe2dcf8372bc76";

    /**
     * 租户化产品ID分隔符
     */
    public static final String TENANT_ID_SEPARATOR = "_";

    /**
     * 获取当前租户下的 PAYPAL 快捷支付产品ID
     *
     * @return PAYPAL 产品ID，格式：基础产品ID_租户ID
     */
    public static String getExternalCorpQuickPayPaypalId() {
        return appendCurrentTenantId(EXTERNALCORP_QUICKPAY_PAYPAL_ID);
    }

    /**
     * 获取当前租户下的 FPS 快捷支付产品ID
     *
     * @return FPS 产品ID，格式：基础产品ID_租户ID
     */
    public static String getExternalCorpQuickPayFpsId() {
        return appendCurrentTenantId(EXTERNALCORP_QUICKPAY_FPS_ID);
    }

    /**
     * 将快捷支付基础产品ID转换为当前租户下的产品ID
     *
     * @param proId 产品ID
     * @return PAYPAL/FPS 基础产品ID返回租户化ID，其他产品ID原样返回
     */
    public static String getCurrentTenantQuickPayProductId(String proId) {
        if (StringUtils.isBlank(proId)) {
            return proId;
        }
        if (EXTERNALCORP_QUICKPAY_PAYPAL_ID.equals(proId)) {
            return getExternalCorpQuickPayPaypalId();
        }
        if (EXTERNALCORP_QUICKPAY_FPS_ID.equals(proId)) {
            return getExternalCorpQuickPayFpsId();
        }
        return proId;
    }

    /**
     * 判断产品ID是否为 PAYPAL 快捷支付产品
     *
     * @param proId 产品ID
     * @return true：PAYPAL 基础产品ID或当前租户 PAYPAL 产品ID
     */
    public static boolean isExternalCorpQuickPayPaypalId(String proId) {
        return EXTERNALCORP_QUICKPAY_PAYPAL_ID.equals(proId) || getExternalCorpQuickPayPaypalId().equals(proId);
    }

    /**
     * 判断产品ID是否为 FPS 快捷支付产品
     *
     * @param proId 产品ID
     * @return true：FPS 基础产品ID或当前租户 FPS 产品ID
     */
    public static boolean isExternalCorpQuickPayFpsId(String proId) {
        return EXTERNALCORP_QUICKPAY_FPS_ID.equals(proId) || getExternalCorpQuickPayFpsId().equals(proId);
    }

    /**
     * 判断产品ID是否为快捷支付产品
     *
     * @param proId 产品ID
     * @return true：PAYPAL/FPS 基础产品ID或当前租户产品ID
     */
    public static boolean isExternalCorpQuickPayProductId(String proId) {
        return isExternalCorpQuickPayPaypalId(proId) || isExternalCorpQuickPayFpsId(proId);
    }

    /**
     * 给基础产品ID追加当前租户ID
     *
     * @param proId 基础产品ID
     * @return 有租户上下文时返回 基础产品ID_租户ID，否则原样返回
     */
    private static String appendCurrentTenantId(String proId) {
        String tenantId = UserContextHelper.helper().getTenantId();
        if (StringUtils.isBlank(tenantId)) {
            return proId;
        }
        String tenantSuffix = TENANT_ID_SEPARATOR + tenantId;
        if (proId.endsWith(tenantSuffix)) {
            return proId;
        }
        return proId + tenantSuffix;
    }
}
