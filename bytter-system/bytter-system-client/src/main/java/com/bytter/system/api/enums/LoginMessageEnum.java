package com.bytter.system.api.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * Created by zhoul on 2017/5/12.
 */
public enum LoginMessageEnum implements IMessageEnum {
    /**
     * 帐号不能为空
     */
    SYS_LOGIN_MESSAGE_ACCOUNT_NULL("02001"),
    /**
     * 密码不能为空
     */
    SYS_LOGIN_MESSAGE_PASSWORD_NULL("02002"),
    /**
     * 验证码不能为空
     */
    SYS_LOGIN_MESSAGE_CAPTCHA_NULL("02010"),
    /**
     * 验证码错误
     */
    SYS_LOGIN_MESSAGE_CAPTCHA_ERROR("02011"),
    /**
     * 帐号不存在
     */
    SYS_LOGIN_MESSAGE_ACCOUNT_NOT_EXISTS("02020"),
    /**
     * 帐号已锁定
     */
    SYS_LOGIN_MESSAGE_ACCOUNT_LOCK("02021"),
    /**
     * 密码错误
     */
    SYS_LOGIN_MESSAGE_PASSWORD_ERROR("02022"),
    /**
     * 用户未分配角色
     */
    SYS_LOGIN_USER_NO_ASSIGN_ROLES("02024"),
    /**
     * 用户未绑定证书
     */
    SYS_LOGIN_CA_NO_BINDING("02025"),
    /**
     * 用户绑定证书与选择证书不一致
     */
    SYS_LOGIN_CA_NOT_EXISTS("02026"),
    /**
     * 证书验证失败
     */
    SYS_LOGIN_CA_VERIFY_FAIL("02027"),
    /**
     * 此证书已被绑定
     */
    SYS_CA_CABINDING("02028"),
    /**
     * 许可证于{0}过期，请联系超级管理员更新许可！
     */
    SYS_LICENSE_EXPIRE("02029"),
    /**
     * 许可证于{0}过期，请更新！
     */
    SYS_LICENSE_EXPIRE_PLEASE_UPDATE("02029"),
    /**
     * 许可证将于{0}到期，请联系超级管理员更新许可证！
     */
    SYS_LICENSE_READY_EXPIRE("02029"),
    /**
     * 您的帐号在 时间 IP 登录，如果这不是您本人的操作，您的账户密码可能已泄漏。请重新登录修改您的账户密码，或联系管理员锁定您的帐号。
     */
    SYS_LOGIN_USER_OTHER_PLACE_LOGIN("02031"),
    /**
     * 密码输错次数过多，请{0}分钟后再试！
     */
    SYS_LOGIN_USER_RETRY_TOO_MUCH("02033"),
    /**
     * 登录成功，正在跳转....
     **/
    SYS_LOGIN_MESSAGE_LOGIN_SUCCESS("02023"),
    SYS_LOGIN_IP_BANNED("02022"),
    /**
     * 帐号已删除
     */
    SYS_LOGIN_MESSAGE_ACCOUNT_DELETED("02034"),
    /**
     * 帐号已禁用
     */
    SYS_LOGIN_MESSAGE_ACCOUNT_DISABLED("02035"),
    
    /**许可证将到期
     * */
    SYS_LICENSE_HAVA_EXPIRE("02036")
    ;
    LoginMessageEnum(String code) {
        this.code = code;
    }
    private String code;
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
