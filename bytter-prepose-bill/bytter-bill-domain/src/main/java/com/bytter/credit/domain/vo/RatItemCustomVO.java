package com.bytter.credit.domain.vo;
import java.io.Serializable;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
public class RatItemCustomVO implements Serializable {
    @Dict(tableName = "RAT_ITEM_CUSTOM", valueFieldName = "id", showFiledName = "NAME", inject = "name")
    private String id;
    /** 授信品种名称 */
    @BtCompositeSearch(fieldName = "rt.NAME")
    private String name;
    /** 授信品种名称 用于维护列表显示 */
    private String itemName;
    /** 综合标志 */
    private Integer flag;
    /** 品种类型 */
    @BtCompositeSearch(fieldName = "rt.TYPE")
    private Integer type;
    /** 系统授信品种 */
    @BtCompositeSearch(fieldName = "rt.SYS_ITEM_CODE")
    private String sysItemCode;
    /** 系统授信品种Id */
    @Dict(tableName = "RAT_SYS_ITEM", valueFieldName = "id", showFiledName = "NAME", inject = "sysItemName")
    private String sysItemId;
    /** 系统授信品种名称 */
    private String sysItemName;
    /** 授信品种类型 */
    private String ritCode;
    @Dict(tableName = "RAT_ITEM_TYPE", valueFieldName = "id", showFiledName = "RIT_NAME", inject = "ritName")
    /** 授信品种类型id */
    private String ritId;
    /** 授信品种类型名称 */
    @BtCompositeSearch(fieldName = "ri.RIT_NAME")
    private String ritName;
    /** 状态 */
    private Integer status;
    /** 备注 */
    @BtCompositeSearch(fieldName = "rt.REMARK")
    private String remark;
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
    public Integer getFlag() {
        return flag;
    }
    public void setFlag(Integer flag) {
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
    public String getSysItemName() {
        return sysItemName;
    }
    public void setSysItemName(String sysItemName) {
        this.sysItemName = sysItemName;
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
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public String getRitId() {
        return ritId;
    }
    public void setRitId(String ritId) {
        this.ritId = ritId;
    }
    public String getSysItemId() {
        return sysItemId;
    }
    public void setSysItemId(String sysItemId) {
        this.sysItemId = sysItemId;
    }
}
