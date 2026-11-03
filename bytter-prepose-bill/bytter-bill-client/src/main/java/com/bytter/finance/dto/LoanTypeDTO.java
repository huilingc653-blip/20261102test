package com.bytter.finance.dto;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
public class LoanTypeDTO implements Serializable {
    @Dict(tableName = "CMS_LOAN_TYPE", valueFieldName = "id", showFiledName = "NAME", inject = "name")
    private String id;
    /** 类型编号 */
    private String code;
    /** 类型名称 */
    private String name;
    /** 类型 */
    private String type;
    /** 状态 */
    private Integer status;
    /**
     * 是否默认 1：不可删除及更改
     */
    private String isDefault;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
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
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getIsDefault() {
        return isDefault;
    }
    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}
