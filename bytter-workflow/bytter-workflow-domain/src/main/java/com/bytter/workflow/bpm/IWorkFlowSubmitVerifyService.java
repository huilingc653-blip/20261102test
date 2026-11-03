package com.bytter.workflow.bpm;

import com.bytter.core.common.WorkFlowVerifyException;

import java.util.List;
import java.util.Map;

/**
 * 工作流审批校验接口
 *
 * @author chenlw
 * @since 2020-04-30
 */
public interface IWorkFlowSubmitVerifyService {
    /**
     * 工作流审批校验
     *
     * @param params       Map<流程分类KEY, 业务ID列表>
     * @param workflowPass 审批操作标识：1，同意；3，打回上一级；4，打回制单处。
     * @param isNeedVerify 是否要校验工作流审批：true表示要校验；false表示不用校验。用户在看到校验失败提示信息后，仍然确定提交审批，则isNeedVerify为false。
     */
    default void submitVerify(Map<String, List<String>> params, String workflowPass, boolean isNeedVerify) throws WorkFlowVerifyException {};
}
