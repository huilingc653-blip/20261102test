package com.bytter.third.enums;
/**
 * 第三方免登陆类型
 */
public enum ThirdType {
    CP_WX("0", "微信", "wxProperties", "feishuLogin"),
    DD("1", "钉钉", "dingDingProperties", "feishuLogin"),
    FS("2", "飞书", "feiShuProperties", "feishuLogin"),
    ;
    private final String code;
    private final String desc;
    private final String propertiesBean;
    private final String authIndex;
    ThirdType(String code, String desc, String propertiesBean, String authIndex) {
        this.code = code;
        this.desc = desc;
        this.propertiesBean = propertiesBean;
        this.authIndex = authIndex;
    }
    public String getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
    public String getPropertiesBean() {
        return propertiesBean;
    }
    public String getAuthIndex() {
        return authIndex;
    }
    public static ThirdType of(String type) {
        for (ThirdType value : ThirdType.values()) {
            if (value.getCode().equals(type)) {
                return value;
            }
        }
        return null;
    }
}
