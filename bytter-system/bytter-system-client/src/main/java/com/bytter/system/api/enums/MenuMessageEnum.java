package com.bytter.system.api.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * @author lifh
 * @description
 * @createDate 2019/6/13
 */
public enum MenuMessageEnum implements IMessageEnum {
    /**
     * 非二开菜单的上级菜单不能为二开菜单
     */
    SYS_MENU_PARENT_CAN_NOT_BE_EXTEND("01001"),
    /**
     * 该菜单为标准菜单，不能设置为二开菜单
     */
    SYS_MENU_CAN_NOT_BE_EXTEND("01001");
    MenuMessageEnum(String code) {
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
