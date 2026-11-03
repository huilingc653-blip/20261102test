package com.bytter.system.api.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * Created by zhoul on 2017/5/12.
 */
public enum UserMessageEnum implements IMessageEnum {
    /**
     * {0}用户未分配角色
     */
    SYS_USER_NOT_ASSIGN_ROLES("04001"),
    SYS_USER_NOT_ALLOW_DOUBLE_ADMIN("04002"),
    SYS_USER_OLD_PASSWORD_NOT_CORRECT("04003"),
    SYS_USER_PASSWORD_NOT_CORRECT("04004"),
    /**
     * 单位id为空
     */
    SYS_USER_MESSAGE_CORPID_NULL("04007"),
    /**
     * 用户名已存在
     */
    SYS_USER_LOGIN_NAME_WAS_EXIST("04005"),
    /**
     * 新密码与前三次密码一致
     */
    SYS_USER_OLD_NEWPASSWORD_EQ_OLDTHREEPASSWORD("04004"),
    /**
     * 当前登陆密码为初始密码，请变更密码后重新登陆
     */
    SYS_USER_MESSAGE_PASSWORD_DEFAULT("02034"),
    /**
     * 当前登陆密码已到达使用期限，请重新修改密码
     */
    SYS_USER_MESSAGE_PASSWORD_DEADLINE("02035"),
    /**
     * 有效用户数量已超出产品许可所给予的{0}个
     */
    SYS_USER_MESSAGE_USER_NUMBER_MORE_THAN_LICENSE_NUMBER("02036"),
    /**
     * 首页组件不可重复
     */
    SYS_HOME_WIDGET_NOT_REPEAT("02037"),
    /**
     * 钉钉账号已存在映射关系
     */
    SYS_ACCOUNT_MAPPING_DINGTALK_EXIST("02038"),
    /**
     * 微信账号已存在映射关系
     */
    SYS_ACCOUNT_MAPPING_WEIXIN_EXIST("02039"),
    /**
     * 当前用户密码已修改，请确认是否由本人操作，即将强制退出。
     * sys.user.modified.pwd.logout
     */
    SYS_USER_MODIFIED_PWD_LOGOUT("02040"),
    /**
     * 该用户下存在待审批（或待处理）数据！
     */
    SYS_USER_NOT_DEL("02041"),
    /**
     * 当前租户用户数量：{0}; 需要添加用户数量：{1}; 客户签约时设置的用户数量：{2}; 已超出客户签约时设置的用户数量，请确认
     */
    SYS_TENANT_USER_NUM_OVER_LIMIT("02042"),
    ;
    UserMessageEnum(String code) {
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
