package com.bytter.core.constant;
/**
 * @author zhoul
 */
public final class Globals {
    /**
     * 人员类型
     */
    public static final Short User_Normal = 1;// 正常
    public static final Short User_Forbidden = 0;// 禁用
    public static final Short User_ADMIN = -1;// 超级管理员
    /**
     * 逻辑删除标记
     */
    /**
     * 删除
     */
    public static final Short Delete_Forbidden = 1;
    /**
     * 正常
     */
    public static final Short Delete_Normal = 0;
    /**
     * 日志级别定义
     */
    public static final short Log_Leavel_INFO = 1;
    public static final short Log_Leavel_WARRING = 2;
    public static final short Log_Leavel_ERROR = 3;
    /*** 日志类型 0其他 */
    public static final short Log_Type_OTHER = 0; // 其他
    /*** 日志类型 1登陆 */
    public static final short Log_Type_LOGIN = 1; // 登陆
    /*** 日志类型 2退出 */
    public static final short Log_Type_EXIT = 2; // 退出
    /*** 日志类型 3插入 */
    public static final short Log_Type_INSERT = 3; // 插入
    /*** 日志类型 4删除 */
    public static final short Log_Type_DEL = 4; // 删除
    /*** 日志类型 5更新 */
    public static final short Log_Type_UPDATE = 5; // 更新
    /*** 日志类型 6上传 */
    public static final short Log_Type_UPLOAD = 6; // 上传
    /*** 日志类型 7查询 */
    public static final short Log_Type_SELECT = 7; // 查询
    /*** 日志类型 8批量删除 */
    public static final short Log_Type_BATCHDEL = 8; // 批量删除
    /*** 日志类型 9复制 */
    public static final short Log_Type_COPY = 9; // 复制
    /*** 日志类型 10打印 */
    public static final short Log_Type_PRINT = 10; // 打印
    /*** 日志类型 11批量同意 */
    public static final short Log_Type_BATCHCONSENT = 11; // 批量同意
    /*** 日志类型 12批量不同意 */
    public static final short Log_Type_BATCHNOCONSENT = 12; // 批量不同意
    /*** 日志类型 13同意 */
    public static final short Log_Type_CONSENT = 13; // 同意
    /*** 日志类型 14不同意 */
    public static final short Log_Type_NOCONSENT = 14; // 不同意
    /*** 日志类型 15批量发送 */
    public static final short Log_Type_BATCHSEND = 15; // 批量发送
    /*** 日志类型 16批量打回 */
    public static final short Log_Type_BATCHREPULSE = 16; // 批量打回
    /*** 日志类型 17发送 */
    public static final short Log_Type_SEND = 17; // 发送
    /*** 日志类型 18打回 */
    public static final short Log_Type_REPULSE = 18; // 打回
    /*** 日志类型 19批量申请 */
    public static final short Log_Type_BATCHAPPLY = 19; // 批量申请
    /*** 日志类型 20批量成功 */
    public static final short Log_Type_BATCHSUCCE = 20; // 批量成功
    /*** 日志类型 21 导入 */
    public static final short Log_Type_Import = 21; // 导入
    public static final long ONE_SECOND = 1000L;
    public static final long ONE_MINUTE = 60000L;
    public static final long ONE_HOUR = 3600000L;
    public static final long ONE_DAY = 86400000L;
    public static final long ONE_MONTH = 2592000000L;
    public static final long ONE_YEAR = 31536000000L;
    /**
     * 文件分片缓存参数
     */
    public static final String ATTACHMENTCACHENAME = "attachmentCacheName";
    // JPush Key
    public final static String JPUSH_APP_KEY = "jpushKey";
    public final static String JPUSH_SECRET_KEY = "jpushSecretKey";
    // 定时任务配置key
    public static final String TASK_KEY = "taskManage";
    public static String DEFAULT_ENCODING = "UTF-8";
    public static boolean DEFAULT_DEV_MODE = false;
    public static String DEFAULT_URL_PARA_SEPARATOR = "-";
    public static int DEFAULT_MAX_POST_SIZE = 1024 * 1024 * 10; // Default max post size of multipart request: 10 Meg
    public static int DEFAULT_I18N_MAX_AGE_OF_COOKIE = 999999999;
    public static int DEFAULT_FREEMARKER_TEMPLATE_UPDATE_DELAY = 3600; // For not devMode only
    public static String DEFAULT_TOKEN_NAME = "_bytter_token";
    public static int DEFAULT_SECONDS_OF_TOKEN_TIME_OUT = 900; // 900 seconds ---> 15 minutes
    int MIN_SECONDS_OF_TOKEN_TIME_OUT = 300;
}
