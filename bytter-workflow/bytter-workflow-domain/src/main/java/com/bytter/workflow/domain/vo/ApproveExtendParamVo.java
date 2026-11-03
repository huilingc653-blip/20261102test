package com.bytter.workflow.domain.vo;

import java.io.Serializable;

/**
 * 2019-02-26
 * chenlw
 * 审批需要自定义排列显示的字段配置
 */
public class ApproveExtendParamVo implements Serializable {

    private String key;
    private Integer sort;
    private Object value;

    public ApproveExtendParamVo(){

    }

    public ApproveExtendParamVo(String key, Object value, Integer sort) {
        this.key = key;
        this.value = value;
        this.sort = sort;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
