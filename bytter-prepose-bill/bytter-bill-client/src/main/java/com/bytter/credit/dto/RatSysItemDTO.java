package com.bytter.credit.dto;
import java.io.Serializable;
/**
 * 系统授信品种VO
 * 
 * @author liyh
 *
 */
public class RatSysItemDTO implements Serializable {
    /**
     * 系统品种代码
     */
    private String code;
    /**
     * 系统品种名称
     */
    private String name;
    /**
     * 综合标识
     */
    private String flag;
    /**
     * 品种主体类型
     */
    private Integer type;
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getFlag() {
        return flag;
    }
    public void setFlag(String flag) {
        this.flag = flag;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
}
