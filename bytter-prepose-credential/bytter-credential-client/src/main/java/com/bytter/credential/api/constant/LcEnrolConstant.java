package com.bytter.credential.api.constant;
import java.io.Serializable;
public class LcEnrolConstant implements Serializable {
	/**开证billCode前缀*/
	public final static String LC_BILL_CODE_PREFIX = "L1";
	/**收证billCode前缀*/
	public final static String LC_RECEIVE_BILL_CODE_PREFIX = "L2";
	/**交单billCode前缀*/
	public final static String LC_RECEIVE_BILL_CODE_ARRIVAL = "L3";
	/**到期收款billCode前缀*/
	public final static String LC_RECEIVE_BILL_CODE_INCOMING = "L4";
	/**出口押汇billCode前缀*/
	public final static String LC_RECEIVE_BILL_CODE_MORTGAGE = "L5";
	/**到单billCode前缀*/
	public final static String LC_BILL_CODE_ARRIVAL = "L6";
	/**付汇流水号前缀*/
	public final static String LC_PAYMENT = "L7";
	/**押汇流水号前缀*/
	public final static String LC_DEPOSIT = "L8";
	/**押汇展期流水号前缀*/
	public final static String LC_DEPOSIT_DELAY = "L9";
	/**押汇流水号前缀*/
	public final static String LC_DEPOSIT_REPAY = "L10";
}
