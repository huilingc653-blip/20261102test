package com.bytter.workflow.constant;
import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 流程配置相关常量
 * Created by chenlw on 2019/02/28.
 *
 * @history
 * 1.  2019-12-30 chenlw  单据配置功能已被启用，其功能可由定制显示字段来代替！TYPE_INVOICE_CONFIG字段废弃。
 * 2.  2019-12-31 chenlw  新增固定显示的字段KEY和名称。
 */
public interface FlowOptioneConst {
    /**
     * option类型
     */
    @Getter
    @AllArgsConstructor
    public enum OptionType{
        //
        _01(1,"流向"),
        _02(2,"任务节点"),
        _03(3,"消息模版"),
        _04(4,"单据配置"),
        _05(5,"审批数据定制显示"),
        ;
        private final Integer code;
        private final String desc;
    }
    /**
     * 定制显示字段-固定显示的字段名称
     */
    @Getter
    @AllArgsConstructor
    public enum OptionExtend{
        //
        BUSINESS_ID("businessId","单据号"),
        CORP_NAME("corpName","申请单位"),
        USERNAME("username","申请人"),
        CREATE_TIME("createTime","申请时间"),
        ;
        private final String fieldName;
        private final String name;
    }
}
