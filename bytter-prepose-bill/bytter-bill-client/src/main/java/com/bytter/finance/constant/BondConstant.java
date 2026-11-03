package com.bytter.finance.constant;
import java.io.Serializable;
/**
 * 债券常量类
 */
public class BondConstant implements Serializable {
    /**
     * 债券还款计划前缀
     */
    public final static String BONDREPAYPLAN_BOND_NO = "B4";
    /**
     * 债券还款前缀
     */
    public final static String BONDPAY_BOND_NO = "B5";
    /**
     * 债券还款计划导入错误提示
     */
    /* 债券发行编号不能为空！ */
    public static final String CMS_CMSBONDREPAYPLANDTL_BONDISSUENUM_ISNULL = "cms.cmsbondrepayplandtl.bondissuenum.isnull";
    /* 计划还款日不能为空！ */
    public static final String CMS_CMSBONDREPAYPLANDTL_PLANREPAYDATE_ISNULL = "cms.cmsbondrepayplandtl.planrepaydate.isnull";
    /* 还款本金值不能为空！ */
    public static final String CMS_CMSBONDREPAYPLANDTL_REPAYMONEY_ISNULL = "cms.cmsbondrepayplandtl.repaymoney.isnull";
    /* 本次还款金额不能为空！ */
    public static final String CMS_CMSBONDREPAYPLANDTL_REPAYAMOUNT_ISNULL = "cms.cmsbondrepayplandtl.repayAmount.isnull";
    /* 是否已还标识不能为空！ */
    public static final String CMS_CMSBONDREPAYPLANDTL_ISREPAY_ISNULL = "cms.cmsbondrepayplandtl.isRepay.isnull";
    /* 是否已还标识输入值错误！ */
    public static final String CMS_CMSBONDREPAYPLANDTL_ISREPAY_ERROR = "cms.cmsbondrepayplandtl.isRepay.error";
}
