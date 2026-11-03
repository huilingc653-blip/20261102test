package com.bytter.erp.common.constant;

import com.baomidou.mybatisplus.annotation.DbType;

/**
 * Description: 常量类
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2026
 *
 * @author bytter
 * @date 2026/1/8
 */
public class ErpConstant {
    /**
     * 数据库类型
     */
    public static DbType dbType;
    /***
     * 分页参数，每页数量
     */
    public static final String PARAM_DATA_SIZE = "dataSize";
    /***
     * 处理业务交互常量：流水号
     */
    public static final String DO_BIZ_SERIAL_NO = "serialNo";

    /***
     * 处理业务交互常量：租户code
     */
    public static final String DO_BIZ_TENANT_CODE = "tenantCode";
    public static final String DO_BIZ_TENANT_ID = "tenantId";
    /***
     * 处理业务交互常量：报文
     */
    public static final String DO_BIZ_DATA = "data";
    /***
     * 处理业务交互常量：状态
     */
    public static final String DO_BIZ_STATUS = "status";
    /***
     * 处理业务交互常量：消息
     */
    public static final String DO_BIZ_MSG = "msg";

    /***
     * 异常返回状态
     */
    public static final String SERVER_FAIL_STATUS = "F";
    /***
     * 正常返回状态
     */
    public static final String SERVER_SUCCESS_STATUS = "S";

    /***
     * 项目根目录
     */
    public static final String USER_DIR = "user.dir";
    public static final String SEPARATOR = "/";

    /**
     * 是否：0-否
     */
    public final static String NO = "0";

    /**
     * 是否：1-是
     */
    public final static String YES = "1";

    /**
     * 是否启用redis
     */
    public static final String REDIS_ENABLE = "redisEnable";
    public static final String REDIS_KEY_TENANT_ID = "erp:tenant:id:";
    public static final String REDIS_KEY_BANK_ACC_INFO = "erp:bankAcc:info:";
    public static final String REDIS_KEY_BANK_ACC_LIST = "erp:bankAcc:list:";
    public static final String REDIS_KEY_CUR_INFO = "erp:cur:info:";
    public static final String REDIS_KEY_VOUCHER_TYPE_INFO = "erp:voucherType:info:";
    public static final String REDIS_KEY_BANK_CODE_INFO = "erp:bankCode:info";
    public static final String REDIS_KEY_SYS_CORP_INFO = "erp:sysCorp:info:";
    public static final String REDIS_KEY_SYS_REGION_INFO = "erp:sysRegion:info";
    public static final String REDIS_KEY_SERIAL_NO = "router:serialNo:";

    /***
     * 查询日期 开始/结束 区间
     */
    public static final String PARAM_DATE_RANGE = "dateRange";
    /** 银行账户信息缓存时间*/
    public final static String PARAM_BANKACC_CACHE_TIME = "bankAccCacheTime";
    /** 币种信息缓存时间*/
    public final static String PARAM_CUR_CACHE_TIME = "curCacheTime";
    /** 付款方式信息缓存时间*/
    public final static String PARAM_VOUCHER_TYPE_CACHE_TIME = "voucherTypeCacheTime";
    /** 联行号信息缓存时间*/
    public final static String PARAM_BANK_CODE_CACHE_TIME = "bankCodeCacheTime";
    /** 系统单位信息缓存时间*/
    public final static String PARAM_SYS_CORP_CACHE_TIME = "sysCorpCacheTime";
    /** 省市区信息缓存时间*/
    public final static String PARAM_SYS_REGION_CACHE_TIME = "sysRegionCacheTime";

    /***
     * 请求模式：银行模式-bank，企业模式-ent
     */
    public static final String REQUEST_MODE_BANK = "bank";
    public static final String REQUEST_MODE_ENT = "ent";
}
