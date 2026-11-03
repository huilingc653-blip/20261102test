package com.bytter.credit.dto;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
/**
 * 授信品种类型DTO
 * 
 * @author liyh
 *
 */
public class RatItemTypeDTO implements Serializable {
    /** 品种类型编码 */
    @Dict(tableName = "RAT_ITEM_TYPE", valueFieldName = "id", showFiledName = "RIT_NAME", inject = "ritName")
    private String id;
    /** 品种类型编码 */
    private String ritCode;
    /** 品种类型名称 */
    private String ritName;
    /** 品种类型名称 用于维护列表显示 */
    private String itemTypeName;
    /** 品种类型描述 */
    private String ritDesc;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getRitCode() {
        return ritCode;
    }
    public void setRitCode(String ritCode) {
        this.ritCode = ritCode;
    }
    public String getRitName() {
        return ritName;
    }
    public void setRitName(String ritName) {
        this.ritName = ritName;
    }
    public String getRitDesc() {
        return ritDesc;
    }
    public void setRitDesc(String ritDesc) {
        this.ritDesc = ritDesc;
    }
    public String getItemTypeName() {
        return itemTypeName;
    }
    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }
}
