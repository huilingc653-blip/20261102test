package com.bytter.core.enums;
/**
 * Created by zhoul on 2017/5/12.
 */
public enum CommonMessageEnum implements IMessageEnum {
    /**
     * 操作成功
     */
    COMMON_MESSAGE_SUCCESS("00000"),
    /**
     * 登录成功
     */
    COMMON_MESSAGE_LOGIN_SUCCESS("00001"),
    /**
     * 时间格式化失败
     */
    COMMON_DATE_FORMAT_MESSAGE_ERROR("00002"),
    /**
     * 令牌已失效，请刷新后再尝试！
     */
    COMMON_MESSAGE_TOKE_EXPIRE_PLEASE_REFRESH("00003"),
    /**
     * 用户密码强度错误，密码需由特殊字符、字母以及数字组成
     */
    COMMON_MESSAGE_PASSWORD_VOL_ERROR_ERROR("99994"),
    /**
     * 登陆失败，请联系系统管理员维护账户对应关系！
     */
    COMMON_MESSAGE_THIRD_LOGIN_ERROR("99995"),
    COMMON_MESSAGE_PARAM_ERROR("99996"),
    /**
     * 您没有权限进行当前操作！
     */
    COMMON_MESSAGE_UNAUTHORIZED_ERROR("99998"),
    /**
     * 操作失败
     */
    COMMON_MESSAGE_UNKNOWN_ERROR("99999"),

    /**
     * 参数异常
     */
    COMMON_MESSAGE_REQ_PARAM_ERROR("120000"),

    /**
     * 操作失败
     */
    BILL_BACKINFO_SYNC_PRO_ERROR("1999998");
    private String code;
    CommonMessageEnum(String code) {
        this.code = code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    @Override
    public Enum getEnum() {
        return this;
    }
    @Override
    public String getCode() {
        return code;
    }
}
