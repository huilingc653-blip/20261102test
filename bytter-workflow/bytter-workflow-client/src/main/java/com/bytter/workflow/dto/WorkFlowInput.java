package com.bytter.workflow.dto;
import com.bytter.core.base.persistence.BasePersistence;
import com.bytter.core.common.page.IParameter;
import com.bytter.core.common.page.IParameterInput;
import com.bytter.core.common.page.ParamImpl;
import java.util.List;
/**
 * 工作流提交的信息
 *
 * @author zhoul
 * @since 2017/7/31.
 */
public class WorkFlowInput extends BasePersistence implements IParameterInput {
    /**
     * 获取审批意见
     *
     * @return 审批意见
     */
    public String getComment() {
        return getParameter().getPara("workflowComment", "");
    }
    /**
     * 审批意见
     *
     * @param comment
     *            审批意见
     */
    public void setComment(Object comment) {
        put("workflowComment", comment);
    }
    /**
     * 获取流程审批结果
     *
     * @return 审批记过
     */
    public String getWorkflowPass() {
        return getParameter().getPara("workflowPass").toString();
    }
    public void setTaskList(Object list) {
        put("taskList", list);
    }
    public List<String> getTaskList() {
        return getParameter().getList("taskList");
    }
    public void setWorkflowPass(Object flag) {
        put("workflowPass", flag);
    }
    /**
     * 获取手写审批的base64字符串
     *
     * @return 手写审批的base64字符串
     */
    public String getDrawComment() {
        return getParameter().getPara("drawComment", "");
    }
    public void setDrawComment(Object drawComment) {
        put("drawComment", drawComment);
    }
    public Boolean getIsNeedVerify() {
        return getParameter().get("isNeedVerify");
    }
    public void setIsNeedVerify(Object isNeedVerify) {
        put("isNeedVerify", isNeedVerify);
    }
    /**
     * 是否通过
     *
     * @return true通过、false不通过
     */
    public boolean isPass() {
        String workflowPass = getParameter().getPara("workflowPass");
        return workflowPass.equalsIgnoreCase("1");
    }
    @Override
    public IParameter getParameter() {
        return new ParamImpl(this);
    }
    @Override
    public Integer getPageNumber() {
        return null;
    }
    @Override
    public Integer getPageSize() {
        return null;
    }
    /**
     * 2019-01-07 chenlw 设置每页的大小
     *
     * @param pageSize
     */
    @Override
    public void setPageSize(Integer pageSize) {
    }
}
