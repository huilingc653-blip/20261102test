package com.bytter.workflow.domain.vo;

import java.io.Serializable;


/**
 * H5审批详情页面的VO
 */
public class H5ApproveVO extends ApproveVO implements Serializable {

    /**
     * 是否显示自定义排列显示字段：值为1标识显示；空值或其他值则不显示。
     */
    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
