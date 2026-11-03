package com.bytter.workflow.bpm;
import com.bytter.workflow.bpm.context.WorkFlowStartHandler;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.core.utils.ApplicationContextUtil;

/**
 * Created by zhoul on 2017/8/31.
 */
public interface IWorkFlowStartedListener {
    /**
     * 启动完成
     *
     */
   default void flowStarted(IWorkFlowResult workFlowResult){

   };

    /**
     * 工作流流程启动处理类
     * @return
     */
    default WorkFlowStartHandler getStartHandler() {
        return ApplicationContextUtil.getContext().getBean(WorkFlowStartHandler.class);
    }
}
