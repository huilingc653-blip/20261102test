package com.bytter.system.api.constant;
/**
 * Created by zhoul on 2017/5/14.
 */
public class UserConstant {
    /**
     * 系统默认操作员登录账号
     */
    public static final String SCHEDULER_STARTER = "scheduler";
    /**
     * 删除状态
     */
    public static final Integer STATUS_DELETE = 0;
    /**
     * 正常状态
     */
    public static final Integer STATUS_NORMAL = 1;
    /**
     * 禁止状态
     */
    public static final Integer STATUS_FORBIDDEN = 2;
    /**
     * 锁定状态
     */
    public static final Integer STATUS_LOCK = 3;
    /**
     * T2初始化状态
     */
    public static final Integer STATUS_SYS_INIT = 4;
    /**
     * 账号权限
     */
    public static final String PERMISSIONS_TYPE_ACC = "1";
    /**
     * 角色权限
     */
    public static final String PERMISSIONS_TYPE_ROLE = "2";
    /**
     * 特殊权限
     */
    public static final String PERMISSIONS_TYPE_SPECIAL = "3";
    /**
     * 替换复制
     */
    public static final String COPY_TYPE_REPLACE = "2";
    /**
     * 新增复制
     */
    public static final String COPY_TYPE_ADD = "1";
    /**
     * 是否管理员 1-是 0-否
     */
    public static final Integer ADMIN = 1;
    /**
     * 是否管理员 1-是 0-否
     */
    public static final Integer UN_ADMIN = 0;
    /**
     * 系统肤色-极简白
     */
    public static final String SYS_SKIN_LIGHT = "light-skin";
    /**
     * 系统肤色-星空黑
     */
    public static final String SYS_SKIN_DARK = "dark-skin";
    /**
     * 系统肤色-商务蓝
     */
    public static final String SYS_SKIN_BLUE = "'blue-skin";
    /**
     * 系统肤色-经典灰
     */
    public static final String SYS_SKIN_GRAY = "gray-skin";
    /**
     * 用户状态 字典DEY sys.user.status
     */
    public static final String USER_STATUS_DICT = "sys.user.status";
}
