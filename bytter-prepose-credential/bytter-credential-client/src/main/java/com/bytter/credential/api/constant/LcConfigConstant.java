package com.bytter.credential.api.constant;
import java.io.Serializable;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-01-14 15:29
 * @Version 1.0
 */
public class LcConfigConstant implements Serializable {
    /*信用证开证*/
    public final static String LC_CONFIG_TYPE = "international";
    /*信用证收证*/
    public final static String LC_CONFIG_RECEIVE = "receive";
    /*信用证到期提醒天数*/
    public final static String LC_CONFIG_EXPIRYDAYS = "expiryDays";
    /*信用证是否挂授信*/
    public final static String LC_CONFIG_CREDITISHANG = "creditIsHang";
    /*信用证是否挂保证金*/
    public final static String LC_CONFIG_CREDITISBOND = "creditIsBond";
    /*信用证开证-是否挂计划*/
    public final static String LC_CONFIG_CREDIT_BUDGETSEL = "budgetSel";
    /*信用证开证-默认走的计划科目*/
    public final static String LC_CONFIG_CREDIT_PAYITEM= "payItem";
    /*信用证收证-是否挂计划*/
    public final static String LC_CONFIG_RECEIVE_BUDGETSEL = "budgetSel";
    /*信用证收证-默认走的计划科目*/
    public final static String LC_CONFIG_RECEIVE_INCITEM = "incItem";
}
