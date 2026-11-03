package com.bytter.finance.constant;
import java.io.Serializable;
/**
 * 委托贷款参数
 * 
 * @author lixi
 *
 */
public class PrecativeConstant implements Serializable {
    /** 委托贷款申请billCode前缀 */
    public final static String INFO_BILL_CODE_PREFIX = "C1";
    /** 委托贷款发放billCode前缀 */
    public final static String SEND_BILL_CODE_PREFIX = "C2";
    /** 委托贷款还款billCode前缀 */
    public final static String REPAY_BILL_CODE_PREFIX = "C3";
    /** 委托贷款展期billCode前缀 */
    public final static String DEFERRED_BILL_CODE_PREFIX = "C4";
}
