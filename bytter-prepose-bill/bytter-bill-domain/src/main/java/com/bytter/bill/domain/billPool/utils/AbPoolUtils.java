package com.bytter.bill.domain.billPool.utils;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
public class AbPoolUtils {
    /**
     * 根据业务类型获取审批状态值
     *
     * @param businessType
     * @return
     */
    public static Integer[] getStatus(Integer businessType) {
        if (AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_1.equals(businessType)) {
            return new Integer[]{AbPoolConstant.AB_POOL_STATUS11};
        } else if (AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_2.equals(businessType)
                || AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_3.equals(businessType)
                || AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_4.equals(businessType)) {
            return new Integer[]{AbPoolConstant.AB_POOL_STATUS11/*, AbPoolConstant.AB_POOL_STATUS_1*/};
        } else if (AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_5.equals(businessType)
                || AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_6.equals(businessType)) {
            return new Integer[]{AbPoolConstant.AB_POOL_STATUS0, AbPoolConstant.AB_POOL_STATUS11/*, AbPoolConstant.AB_POOL_STATUS_1*/};
        } else {
            return null;
        }
    }
    /**
     * 根据业务类型获取指令状态值
     * @param businessType
     * @return
     */
    public static String[] getOptStatus(Integer businessType) {
        if (AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_1.equals(businessType)) {
            return new String[]{AbPoolConstant.AB_POOL_OPT_STATUS16,AbPoolConstant.AB_POOL_OPT_STATUS18};
        } else if (AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_2.equals(businessType)
                || AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_3.equals(businessType)
                || AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_4.equals(businessType)) {
            return new String[]{AbPoolConstant.AB_POOL_OPT_STATUS_1,AbPoolConstant.AB_POOL_OPT_STATUS16,AbPoolConstant.AB_POOL_OPT_STATUS18};
        } else if (AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_5.equals(businessType)
                || AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_6.equals(businessType)) {
            return new String[]{AbPoolConstant.AB_POOL_OPT_STATUS_1,AbPoolConstant.AB_POOL_OPT_STATUS16,AbPoolConstant.AB_POOL_OPT_STATUS18};
        } else {
            return null;
        }
    }
}
