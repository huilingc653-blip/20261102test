package com.bytter.workflow.domain.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytter.core.base.entity.IdEntity;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.constant.FlowOptioneConst;
import com.bytter.workflow.domain.vo.FlowOptionVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.Date;
/**
 * 工作流条件配置表
 * Created by zhoul on 2017/8/3.
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Accessors(chain = true)
@TableName("act_extend_option")
public class FlowOption extends IdEntity{
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
    private boolean visible;
    /**
     * type = 5 使用，标识字段的序号
     */
    private Integer sort;
    /**
     * type = 5 使用，标识option是否是固定的字段。true标识是固定字段。如果是固定字段，则在定制显示字段页面不允许修改和删除。
     */
    private boolean stableField;
    private Date createTime;
    private Date updateTime;
    public FlowOption(){}
    public FlowOption(String name, String fieldName, Integer type, boolean visible, Integer sort, boolean stableField){
        this.name = name;
        this.fieldName = fieldName;
        this.type = type;
        this.visible = visible;
        this.sort = sort;
        this.stableField = stableField;
    }
    public static FlowOption createExtendOption(String name, String filedName, Integer sort) {
        return new FlowOption().setName(name)
                .setFieldName(filedName)
                .setType(FlowOptioneConst.OptionType._05.getCode())
                .setVisible(Boolean.TRUE)
                .setSort(sort).setStableField(Boolean.TRUE);
    }

    public static FlowOption from(FlowOptionVO flowOptionVO) {
        FlowOption flowOption = new FlowOption().setCategory(flowOptionVO.getCategory()).setName(flowOptionVO.getName())
                .setFieldName(flowOptionVO.getFieldName())
                .setType(flowOptionVO.getType()).setVisible(CommonConstant.YES.equals(flowOptionVO.getVisible()))
                .setSelectType(flowOptionVO.getSelectType()).setDictGroup(flowOptionVO.getDictGroup())
                .setSort(flowOptionVO.getSort()).setStableField(CommonConstant.YES.equals(flowOptionVO.getStableField()));
        if (StringUtils.isNotEmpty(flowOptionVO.getId())) {
            flowOption.setId(flowOptionVO.getId());
        }
        return flowOption;
    }


}
