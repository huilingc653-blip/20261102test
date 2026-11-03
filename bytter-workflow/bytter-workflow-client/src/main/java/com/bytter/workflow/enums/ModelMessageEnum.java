package com.bytter.workflow.enums;
import com.bytter.core.enums.IMessageEnum;
public enum ModelMessageEnum implements IMessageEnum {
    /** 流程设计模型不存在*/
    BPM_MODEL_NOT_EXIST("01001"),
    /**
     * 该单位下已存在该流程类型
     */
    BPM_MODEL_CORP_ALREADY_EXIST("01001"),
    /** 流程设计模型存在排他网关*/
    BPM_MODEL_HAS_EXCLUSIVE_GATEWAY("01002"),
    /**
     * 该流程存在用户节点未分配用户或者角色！请检查！
     */
    BPM_MODEL_UNASSIGNED_USER_ROLE("01231"),
    /**
     * 不存在用户节点，请添加用户节点！
     */
    BPM_MODEL_NOT_USER_TASK_HAS("01232"),
    /**
     * 该流程中存在用户节点未命名，请给未命名的用户节点命名！
     */
    BPM_MODEL_USER_TASK_HAS_NOT_NAME("01232"),
    /**
     * 该流程中的流向存在条件表达式错误，请仔细检查
     */
    BPM_MODEL_SEQUENCE_FLOW_CONDITION_EXPRESSION_ERROR("01234"),
    /**
     * 流程设计错误，请检查流程中用户是否有审批权限！
     */
    BPM_MODEL_TASK_USER_NOT_HAVE_APPROVE_PERMISSION("01235"),
    /**
     * 流程中有审批权限的用户人数小于流程设计的最小审批人数，请联系管理员！用户节点{0},最少审批人数为{1}
     */
    BPM_MODEL_TASK_APPROVE_USER_NUMBER_LESS_THAN_PASS_NUMBER("01236"),
    /**
     * 流程设计错误，请检查流程中用户和角色的人员是否有审批权限！
     */
    BPM_MODEL_TASK_NO_APPROVE_USER("01237"),
    /**
     * 流程设计错误，请检查流程的流向是否闭合，不可有多余的流向或任务节点！
     */
    BPM_MODEL_FORMAT_ERROR("01001"),
    /**
     * 流程设计数据不存在
     *
     */
    BPM_MODEL_DATA_EXIST("01238"),
    NO_START_EVENT("01239"),
    NO_END_EVENT("01240"),
    NO_USER_TASK("01241"),
    ;
    ModelMessageEnum(String code) {
        this.code = code;
    }
    private String code;
    public void setCode(String code) {
        this.code = code;
    }
    @Override
    public Enum getEnum() {
        return this;
    }
    @Override
    public String getCode() {
        return code;
    }
}
