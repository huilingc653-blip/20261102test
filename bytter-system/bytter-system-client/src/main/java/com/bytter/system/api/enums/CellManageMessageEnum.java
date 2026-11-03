package com.bytter.system.api.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * @author wangzy
 * @Title: CellManageMessageEnum
 * @Description: 管理单元枚举类
 * @date 2020/5/14 16:36
 */
public enum CellManageMessageEnum implements IMessageEnum {
    /**
     * 管理单元代码已存在，请重新输入！
     */
    SYS_CELL_CODE_NOT_REPEAT("100001"),
    /**
     * 管理单元下有子单元，无法修改为无效！
     */
    SYS_CELL_SON_NOT_UPDATE("100002"),
    /**
     * 管理单元已被使用，无法修改为无效！
     */
    SYS_CELL_USED_NOT_UPDATE("100003"),
    /**
     * 管理单元下有子单元，无法删除！
     */
    SYS_CELL_SON_NOT_DELETE("100004"),
    /**
     * 管理单元已被使用，无法删除！
     */
    SYS_CELL_USED_NOT_DELETE("100005"),
    ;
    CellManageMessageEnum(String code) {
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
