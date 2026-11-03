package com.bytter.workflow.domain.vo;

import com.bytter.core.constant.CommonConstant;
import com.bytter.workflow.domain.entity.FlowOption;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;


@Data
@Accessors(chain = true)
public class FlowOptionVO {

    private String id;
    private String category;
    private String name;
    private String fieldName;
    private String event;
    private Integer type;
    private String selectType;
    private String dictGroup;
    private String msgTpl;
    /**
     * type = 5 使用，标识字段是否可见：true可见
     */
    private String visible;
    /**
     * type = 5 使用，标识字段的序号
     */
    private Integer sort;
    /**
     * type = 5 使用，标识option是否是固定的字段。true标识是固定字段。如果是固定字段，则在定制显示字段页面不允许修改和删除。
     */
    private String stableField;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    public FlowOptionVO(){}

    public static FlowOptionVO from(FlowOption flowOption) {
        return new FlowOptionVO().setCategory(flowOption.getCategory()).setName(flowOption.getName())
                .setFieldName(flowOption.getFieldName()).setEvent(flowOption.getEvent())
                .setType(flowOption.getType()).setSelectType(flowOption.getSelectType())
                .setDictGroup(flowOption.getDictGroup()).setMsgTpl(flowOption.getMsgTpl())
                .setVisible(flowOption.isVisible()?CommonConstant.YES:CommonConstant.NO)
                .setStableField(flowOption.isStableField()?CommonConstant.YES:CommonConstant.NO)
                .setCreateTime(flowOption.getCreateTime()).setUpdateTime(flowOption.getUpdateTime())
                .setId(flowOption.getId()).setSort(flowOption.getSort());
    }

}