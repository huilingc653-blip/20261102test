package com.bytter.system.vo;
public class ThirdLoginVo {
    /**
     * 授权码
     */
    private String code;
    /**
     * 第三方类型 参照ThirdType
     */
    private String type;
    private ThirdLoginVo() {
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
