package com.bytter.workflow.domain.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Created by zhoul on 2017/8/3.
 */
@Entity
@Table(name = "BPM_OPTION")
public class Option extends IdEntity{
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

    private Date createTime;
    private Date updateTime;



    public Option(){}

    public Option(String name,String fieldName,Integer type,Integer visible,Integer sort,Integer isStableField){
        this.name = name;
        this.fieldName = fieldName;
        this.type = type;
        this.visible = visible;
        this.sort = sort;
        this.isStableField = isStableField;
    }


    @Column(name = "CATEGORY")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "FIELD_NAME")
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Column(name = "EVENT")
    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Column(name = "TYPE")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


    @Column(name = "CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "UPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "SELECT_TYPE")
    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    @Column(name = "DICT_GROUP")
    public String getDictGroup() {
        return dictGroup;
    }

    public void setDictGroup(String dictGroup) {
        this.dictGroup = dictGroup;
    }


    @Column(name = "MSG_TPL")
    public String getMsgTpl() {
        return msgTpl;
    }

    public void setMsgTpl(String msgTpl) {
        this.msgTpl = msgTpl;
    }

    @Column(name = "VISIBLE")
    public Integer getVisible() {
        return visible;
    }
    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    @Column(name = "SORT")
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Column(name = "IS_STABLE_FIELD")
    public Integer getIsStableField() {
        return isStableField;
    }

    public void setIsStableField(Integer isStableField) {
        this.isStableField = isStableField;
    }
}
