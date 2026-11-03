package com.bytter.increment.api.constant;
/**
 * 银行理财常量类
 * @author huangx
 * @since 2017/8/18.
 */
public class BankFinanceConstant {
	
	/*** 银行理财参数配置：choBank-银行理财 */
	public static final String CHO_BANK_CONFIG_TYPE = "choBank";
	/*** 银行理财参数配置：regularOnset-定期存款起存额度 */
	public static final String CHO_BANK_CONFIG_REGULARONSET = "regularOnset";
	/*** 银行理财参数配置：regularKeep-定期存款最低留存额度  */
	public static final String CHO_BANK_CONFIG_REGULARKEEP = "regularKeep";
	/*** 银行理财参数配置：regularNum-定期存款取款次数  */
	public static final String CHO_BANK_CONFIG_REGULARNUM = "regularNum";
	/*** 银行理财参数配置：noticeOnset-通知存款起存额度 */
	public static final String CHO_BANK_CONFIG_NOTICEONSET = "noticeOnset";
	/*** 银行理财参数配置：noticeKeep-通知存款最低留存额度  */
	public static final String CHO_BANK_CONFIG_NOTICEKEEP = "noticeKeep";
	/*** 银行理财参数配置：noticeMinLimit-通知存款最低取款额度  */
	public static final String CHO_BANK_CONFIG_NOTICEMINLIMIT = "noticeMinLimit";
	
  	/*** 银行理财处理状态：0-暂存  */
  	public final static Integer STATUE_TEMP = 0;
  	/*** 银行理财处理状态：1-打回  */
  	public final static Integer STATUE_BACK = -1;
  	/*** 银行理财处理状态：-2-删除  */
  	public final static Integer STATUE_DEL = -2;
  	/*** 银行理财处理状态：11-待审批  */
  	public final static Integer STATUE_WAIT_CHECK = 11;
  	/*** 银行理财处理状态：95-审批通过  */
  	public final static Integer STATUE_CHECK = 95;
  	/*** 业务类型：10-活期转定期  */
    public final static Integer BUS_TYPE_C2T = 10;
    /*** 业务类型：11-定期转活期  */
    public final static Integer BUS_TYPE_T2C = 11;
    /*** 业务类型：12-活期转通知  */
    public final static Integer BUS_TYPE_C2N = 12;
    /*** 业务类型：13-通知转活期  */
    public final static Integer BUS_TYPE_N2C = 13;
    
    /*** 期限：3M-3个月  */
    public final static String LIMIT_TIME_3M = "3M";
    /*** 期限：6M-6个月  */
    public final static String LIMIT_TIME_6M = "6M";
    /*** 期限：1Y-1年  */
    public final static String LIMIT_TIME_1Y = "1Y";
    /*** 期限：2Y-2年  */
    public final static String LIMIT_TIME_2Y = "2Y";
    /*** 期限：3Y-3年  */
    public final static String LIMIT_TIME_3Y = "3Y";
    /*** 期限：5Y-5年  */
    public final static String LIMIT_TIME_5Y = "5Y";
    
    /*** 到期处理方式：0-不处理  */
    public final static String CERTIFICATE_WAY_0 = "0";
    /*** 到期处理方式：1-本金续存  */
    public final static String CERTIFICATE_WAY_1 = "1";
    /*** 到期处理方式：2-本息续存  */
    public final static String CERTIFICATE_WAY_2 = "2";
    /*** 是否补录：0-否  */
    public final static String BL_0 = "0";
    /*** 是否补录：1-是  */
    public final static String BL_1 = "1";
}
