package com.bytter.workflow.dto;
import java.io.Serializable;
/**
 * Created by liyh on 2021-08-27
 */
public class OptionDTO implements Serializable {
    private String category;
    private String name;
    private String fieldName;
    private String event;
    private Integer type;
    private String selectType;
    private String dictGroup;
    private String msgTpl;
    /**
     * type = 5 使用，标识字段是否可见：1可见
     */
    private Integer visible;
    /**
     * type = 5 使用，标识字段的序号
     */
    private Integer sort;
    /**
     * type = 5 使用，标识option是否是固定的字段。1标识是固定字段。如果是固定字段，则在定制显示字段页面不允许修改和删除。
     */
    private Integer isStableField;
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    public String getEvent() {
        return event;
    }
    public void setEvent(String event) {
        this.event = event;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public String getSelectType() {
        return selectType;
    }
    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }
    public String getDictGroup() {
        return dictGroup;
    }
    public void setDictGroup(String dictGroup) {
        this.dictGroup = dictGroup;
    }
    public String getMsgTpl() {
        return msgTpl;
    }
    public void setMsgTpl(String msgTpl) {
        this.msgTpl = msgTpl;
    }
    public Integer getVisible() {
        return visible;
    }
    public void setVisible(Integer visible) {
        this.visible = visible;
    }
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    public Integer getIsStableField() {
        return isStableField;
    }
    public void setIsStableField(Integer isStableField) {
        this.isStableField = isStableField;
    }
}
