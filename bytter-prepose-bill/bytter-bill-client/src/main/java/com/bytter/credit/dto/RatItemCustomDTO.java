package com.bytter.credit.dto;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
/**
 * 授信品种DTO
 * 
 * @author liyh
 *
 */
public class RatItemCustomDTO implements Serializable {
    /** 授信品种ID */
    @Dict(tableName = "RAT_ITEM_CUSTOM", valueFieldName = "id", showFiledName = "NAME", inject = "name")
    private String id;
    /** 授信品种名称 */
    private String name;
    /** 综合标志 */
    private String flag;
    /** 品种类型 */
    private Integer type;
    /** 系统授信品种 */
    private String sysItemCode;
    /** 授信品种类型 */
    private String ritCode;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public String getSysItemCode() {
        return sysItemCode;
    }
    public void setSysItemCode(String sysItemCode) {
        this.sysItemCode = sysItemCode;
    }
    public String getRitCode() {
        return ritCode;
    }
    public void setRitCode(String ritCode) {
        this.ritCode = ritCode;
    }
}
