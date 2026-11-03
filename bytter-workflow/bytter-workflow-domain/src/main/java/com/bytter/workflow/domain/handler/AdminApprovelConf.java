package com.bytter.workflow.domain.handler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdminApprovelConf {
    /**
     * 1 一级审批 递增
     */
    private Integer level;
    /**
     * 审批名称
     */
    private String name;
    /**
     * 绑定的角色编码|用户ID(测试使用)
     */
    private String roleId;
    /**
     * 最小审批人数
     */
    private Integer passNumber;
}
