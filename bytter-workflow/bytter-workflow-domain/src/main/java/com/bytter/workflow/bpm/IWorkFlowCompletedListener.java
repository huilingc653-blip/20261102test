package com.bytter.workflow.bpm;
import com.bytter.workflow.dto.IWorkFlowResult;
/**
 * Created by zhoul on 2017/8/31.
 */
public interface IWorkFlowCompletedListener {
    /**
     * 流程结束
     */
    void flowCompleted(IWorkFlowResult workFlowResult);
}
