package com.bytter.system.api.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * @author zhenghj
 * @Title: OrgantypeMessageEnum
 * @Description: 组织架构枚举类
 * @date 2020/5/21
 */
public enum OrgantypeMessageEnum implements IMessageEnum {
    /**
     * 单位下有子单位，无法删除！
     */
    SYS_ORGANTYPE_CORPSON_NOTDELETE("110000"),
    /**
     * 传递参数值不允许为空！
     */
    SYS_ORGANTYPE_MESSAGE_ERROR_VALISNOTNULL("110001");
    OrgantypeMessageEnum(String code) {
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
