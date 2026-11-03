package com.bytter.system.enums;
/**
 * 人脸复核状态Enum
 *
 * @author: chenlw
 * @date: 2018/9/19  13:59
 */
public enum UserFaceVerifyStatusEnum {
    VERIFIED(1, "人脸图像已通过复核"),
    NOT_VERIFIED(0, "人脸图像待复核");
    private Integer value;
    private String name;
    UserFaceVerifyStatusEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }
    public Integer getValue() {
        return value;
    }
    public void setValue(Integer value) {
        this.value = value;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
