package com.bytter.system.api.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * Created by zhoul on 2017/5/12.
 */
public enum RegionMessageEnum implements IMessageEnum {
    /**
     * 该省市下存在数据，不可删除
     */
    SYS_REGION_HAS_CHILD("04001"),
    SYS_REGION_NAME_WAS_EXIST("04002");
    RegionMessageEnum(String code) {
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
