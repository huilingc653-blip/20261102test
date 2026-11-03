package com.bytter.workflow.bpm;
import com.bytter.workflow.dto.IWorkFlowResult;
/**
 * Created by zhoul on 2018/5/18.
 */
public interface IWorkFlowRevokeListener {
    /**
     * 流程撤销
     */
    default void flowRevoke(IWorkFlowResult workFlowResult) {
    }
}
