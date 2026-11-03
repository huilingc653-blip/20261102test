package com.bytter.system.api.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * Created by zhoul on 2017/5/12.
 */
public enum RoleMessageEnum implements IMessageEnum {
    /**
     * 该用角色下已分配用户，请解除用户与角色关系后再作删除操作！
     */
    SYS_ROLE_HAS_ASSIGN_CAN_NOT_DELETE("04001"),
    SYS_ROLE_PLEASE_CHOICE_MENU_OR_RESOURCE("04002"),
    /**
     * 角色名称已存在
     */
    SYS_ROLE_NAME_WAS_EXIST("04003"),
    /**
     * 分配用户不能为空！
     */
    SYS_ROLE_ASSIGN_USER_CAN_NOT_NULL("04004"),
    /**
     * 角色代码已经存在
     */
    SYS_ROLE_CODE_WAS_EXIST("04005"),
    /**
     * 角色状态为2的不可操作
     */
    SYS_ROLE_NOT_OPERATION("04006");
    RoleMessageEnum(String code) {
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
