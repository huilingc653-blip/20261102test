package com.bytter.system.api.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * Created by zhoul on 2017/10/26.
 */
public enum NotificationEnum implements IMessageEnum {
    /**
     * 消息推送分类标识不唯一
     */
    SYS_NOTIFICATION_CATEGORY_TAG_NOT_UNIQUE("00001");
    NotificationEnum(String code) {
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
