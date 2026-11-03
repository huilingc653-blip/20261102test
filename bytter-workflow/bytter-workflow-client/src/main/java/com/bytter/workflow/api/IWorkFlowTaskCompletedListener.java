package com.bytter.workflow.api;

import com.bytter.workflow.dto.IWorkFlowResult;

/**
 * @author lifh
 * @description
 * @createDate 2019/5/31
 */
public interface IWorkFlowTaskCompletedListener {
    /**
     * 任务节点完成
     *
     */
    void taskCompleted(IWorkFlowResult workFlowResult);
}
