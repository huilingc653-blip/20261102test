package com.bytter.workflow.bpm;

/**
 * Created by zhoul on 2017/7/24.
 * 用于工作流开始完成，结束的回调监听器
 */
public interface IWorkFlowListener extends IWorkFlowStartedListener, IWorkFlowCompletedListener,IWorkFlowRevokeListener,IWorkFlowSubmitVerifyService {

}
