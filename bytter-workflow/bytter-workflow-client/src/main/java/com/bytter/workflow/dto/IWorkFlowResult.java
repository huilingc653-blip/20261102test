package com.bytter.workflow.dto;
import com.bytter.core.base.entity.ICurrentUser;
import java.io.Serializable;
/**
 * 工作流执行结果集
 *
 * @author zhoul
 * @since 2018/1/5.
 */
public interface IWorkFlowResult extends Serializable {
    /**
     * @return 流程定义KEY
     */
    String getProcessDefinitionKey();
    /**
     * @return 业务ID
     */
    String getBusinessKey();
    /**
     * @return 是否通过
     */
    Boolean getIsPass();
    /**
     * 兼容T6 old
     * @return 是否通过
     */
    Boolean isPass();
    /**
     * @return 当前审批人
     */
    ICurrentUser getCurrentUser();
    /**
     * 兼容T6 old
     * @return 当前审批用户
     */
    ICurrentUser getUser();
}
