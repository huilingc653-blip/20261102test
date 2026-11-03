package com.bytter.system.api.constant;
/**
 * 黑白名单常量信息
 * Created by zhengg on 2020/07/27
 */
public class SysBlackWhiteConstant {
    public static final String CACHE_NAME_CHECK = "blackWhiteCheckCache";
    /*** 有效标志：0否 */
    public static final String SYS_BLACK_WHITE_VALID_SIGN_0 = "0";
    /*** 有效标志：1是 */
    public static final String SYS_BLACK_WHITE_VALID_SIGN_1 = "1";
    /*** 有效标志：-2已删除 */
    public static final String SYS_BLACK_WHITE_VALID_SIGN_2 = "-2";
    /**
     * 客商属性 字典 SYS_BUS_ENTITY_TYPE
     */
    public static final String EXTERNALCORP_BWTYPE_DICTKEY = "SYS_BUS_ENTITY_TYPE";
    /**
     * 设置系统客商是否可以从其他业务层手动录入
     * 客商属性 00一般客商01黑名单02白名单
     */
    public static final String EXTERNALCORP_BWTYPE_00 = "00";
    public static final String EXTERNALCORP_BWTYPE_01 = "01";
    public static final String EXTERNALCORP_BWTYPE_02 = "02";
    /**
     * 黑名单收付限制
     */
    public static final String SYS_CONFIG_BLACKLISTCHECK = "blackListCheck";
    /**
     * 黑名单收付限制 1不提醒2只提醒不控制3控制
     * 1不提醒验证通过、2提醒不限制业务、3提醒且限制业务不允许操作
     */
    /**
     * 黑名单收付限制 1不提醒验证通过
     */
    public static final String SYS_CONFIG_BLACKLISTCHECK_NOWARN = "1";
    /**
     * 黑名单收付限制 2提醒不限制业务
     */
    public static final String SYS_CONFIG_BLACKLISTCHECK_WARN = "2";
    /**
     * 黑名单收付限制 3提醒且限制业务不允许操作
     */
    public static final String SYS_CONFIG_BLACKLISTCHECK_CONTROL = "3";
    public static String getBwType(String fegin) {
        return fegin.equals("black") ? EXTERNALCORP_BWTYPE_01 : EXTERNALCORP_BWTYPE_02;
    }
    /**
     * 是否支持强控:1是
     */
    public final static String SYS_BLACK_WHITE_ISCONTROL_Y = "1";
    /**
     * 是否支持强控:0否
     */
    public final static String SYS_BLACK_WHITE_ISCONTROL_N = "0";
    /**
     * 名单类别 字典 SYS_BM_LIST_TYPE
     */
    public static final String SYS_BLACK_WHITE_LISTTYPE_DICTKEY = "SYS_BM_LIST_TYPE";
//	名单类别：01客商、02承兑行
    /**
     * 名单类别：01客商
     */
    public static final String SYS_BLACK_WHITE_LISTTYPE_01 = "01";
    /**
     * 名单类别：02承兑行
     */
    public static final String SYS_BLACK_WHITE_LISTTYPE_02 = "02";
    /**
     * 数据来源 字典 SYS_BM_LIST_DATA_SOURCES
     */
    public static final String SYS_BLACK_WHITE_DATASOURCES_DICTKEY = "SYS_BM_LIST_DATA_SOURCES";
//	/***数据来源：01BFS、02手工、03ERP*/
    /***数据来源：01BFS*/
    public static final String SYS_BLACK_WHITE_DATASOURCES_01 = "01";
    /***数据来源：02手工*/
    public static final String SYS_BLACK_WHITE_DATASOURCES_02 = "02";
    /***数据来源：03ERP*/
    public static final String SYS_BLACK_WHITE_DATASOURCES_03 = "03";
    /***黑名单验证提醒内容： [{0}]客商为黑名单客户，是否提交？*/
    public static final String SYS_BLACK_WHITE_CHECK_EXTERNALCORP_WARN = "sys.black.white.check.externalcorp.warn";
    /***黑名单验证提醒内容：[{0}]客商为黑名单客户，限制付款！*/
    public static final String SYS_BLACK_WHITE_CHECK_EXTERNALCORP_CONTROL = "sys.black.white.check.externalcorp.control";
    /***黑名单验证提醒内容：单号[{1}],[{0}]客商为黑名单客户，是否提交？*/
    public static final String SYS_BLACK_WHITE_CHECK_EXTERNALCORP_BATCH_WARN = "sys.black.white.check.externalcorp.batch.warn";
    /***黑名单验证提醒内容：单号[{1}],[{0}]客商为黑名单客户，限制付款！*/
    public static final String SYS_BLACK_WHITE_CHECK_EXTERNALCORP_BATCH_CONTROL = "sys.black.white.check.externalcorp.batch.control";
    /***黑名单验证提醒内容：[{0}]承兑行为黑名单承兑行，是否提交？*/
    public static final String SYS_BLACK_WHITE_CHECK_BANKACCEPTANCE_WARN = "sys.black.white.check.bankacceptance.warn";
    /***黑名单验证提醒内容：[{0}]承兑行为黑名单承兑行，限制签收！*/
    public static final String SYS_BLACK_WHITE_CHECK_BANKACCEPTANCE_CONTROL = "sys.black.white.check.bankacceptance.control";
    /***黑名单验证提醒内容：单号[{1}],[{0}]承兑行为黑名单承兑行，是否提交？*/
    public static final String SYS_BLACK_WHITE_CHECK_BANKACCEPTANCE_BATCH_WARN = "sys.black.white.check.bankacceptance.batch.warn";
    /***黑名单验证提醒内容：单号[{1}],[{0}]承兑行为黑名单承兑行，限制签收！*/
    public static final String SYS_BLACK_WHITE_CHECK_BANKACCEPTANCE_BATCH_CONTROL = "sys.black.white.check.bankacceptance.batch.control";
    /**
     * 黑名单，白名单
     */
    public final static String SYS_BLACK_NAME = "黑名单";
    public final static String SYS_WHITE_NAME = "白名单";
    public final static String SYS_BLACK_WHITE_LISTTYPENAME01 = "客商";
    public final static String SYS_BLACK_WHITE_LISTTYPENAME02 = "承兑行";
    /**
     * 导入验证
     **/
    //导入的单位编号不存在系统中
    public static final String SYS_BLACK_WHITE_IMPORT_CORP_TIPS = "sys.black.white.import.corp.tips";
    //名单类型不允许为空
    public static final String SYS_BLACK_WHITE_IMPORT_BWTYPE_EMPTY_TIPS = "sys.black.white.import.bwtype.empty.tips";
    //不支持的名单类型，请检查导入的数据是否正确
    public static final String SYS_BLACK_WHITE_IMPORT_BWTYPE_NONENTITY_TIPS = "sys.black.white.import.bwtype.nonentity.types.tips";
    //只支持黑名单类型导入
    public static final String SYS_BLACK_WHITE_IMPORT_BWTYPE_BLACK_TIPS = "sys.black.white.import.bwtype.black.tips";
    //只支持白名单类型导入
    public static final String SYS_BLACK_WHITE_IMPORT_BWTYPE_WHITE_TIPS = "sys.black.white.import.bwtype.white.types.tips";
    //名单类别不允许为空
    public static final String SYS_BLACK_WHITE_IMPORT_LISTTYPE_EMPTY_TIPS = "sys.black.white.import.listType.empty.tips";
    //不支持的名单类别，请检查导入的数据是否正确
    public static final String SYS_BLACK_WHITE_IMPORT_LISTTYPE_NONENTITY_TIPS = "sys.black.white.import.listType.nonentity.tips";
    //名称不允许为空
    public static final String SYS_BLACK_WHITE_IMPORT_NAME_EMPTY_TIPS = "sys.black.white.import.name.empty.tips";
    //国家/地区不允许为空
    public static final String SYS_BLACK_WHITE_IMPORT_COUNTRY_EMPTY_TIPS = "sys.black.white.import.country.empty.tips";
    //国家地区不存在系统中
    public static final String SYS_BLACK_WHITE_IMPORT_COUNTRY_NONENTITY_TIPS = "sys.black.white.import.country.nonentity.tips";
    //不存在的是否共享值
    public static final String SYS_BLACK_WHITE_IMPORT_ISCOMMON_NONENTITY_TIPS = "sys.black.white.import.iscommon.nonentity.tips";
    //重复
    public static final String SYS_BLACK_WHITE_REPEAT = "sys.black.white.repeat";
    /***end**/
}
