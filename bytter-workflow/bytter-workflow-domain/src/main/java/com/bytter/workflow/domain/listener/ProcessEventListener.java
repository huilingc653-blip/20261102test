package com.bytter.workflow.domain.listener;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.cloud.common.core.util.JsonUtil;
import com.bytter.core.base.entity.CurrentUser;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.context.WorkFlowHelper;
import com.bytter.workflow.constant.WorkFlowConstant;
import com.bytter.workflow.domain.service.FlowMessageService;
import com.bytter.workflow.domain.service.FlowModelService;
import com.bytter.workflow.dto.WorkFlowResult;
import com.bytter.workflow.enums.WorkFlowMessageEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.pvm.PvmActivity;
import org.camunda.bpm.engine.impl.pvm.PvmTransition;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.spring.boot.starter.event.ExecutionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Component
@Slf4j
public class ProcessEventListener {
    private final RuntimeService runtimeService;
    private final HistoryService historyService;
    private final FlowMessageService flowMessageService;

    @DubboReference(check = false)
    private INotificationService notificationService;

    @DubboReference(check = false)
    private IUserService iUserService;

    @Autowired
    private FlowModelService flowModelService;

    public ProcessEventListener(RuntimeService runtimeService, HistoryService historyService, FlowMessageService flowMessageService) {
        this.runtimeService = runtimeService;
        this.historyService = historyService;
        this.flowMessageService = flowMessageService;
    }

    /**
     * @param executionDelegate 流程事件
     */
    @EventListener
    public void onDelegateExecution(DelegateExecution executionDelegate) {
        log.info("流程事件{}", executionDelegate.getProcessDefinitionId());
        FlowElement flowElement = executionDelegate.getBpmnModelElementInstance();
        switch (executionDelegate.getEventName()) {
            case ExecutionListener.EVENTNAME_START:
                setMulUsers(executionDelegate, flowElement);
                break;
            case ExecutionListener.EVENTNAME_TAKE:
                if (flowElement instanceof SequenceFlow) {
                    SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                    setMulUsers(executionDelegate, sequenceFlow.getTarget());
                }
                break;
            case ExecutionListener.EVENTNAME_END:
                String processInstanceId = executionDelegate.getProcessInstanceId();
                String activityInstanceId = executionDelegate.getActivityInstanceId();
                // 活动实例ID，这个ID可能是事件的ID，可能是运行实例的ID
                String executionId = executionDelegate.getId();
                String businessKey = executionDelegate.getBusinessKey();
                String defId = executionDelegate.getProcessDefinitionId();
                if (!StringUtils.isEmpty(processInstanceId) && processInstanceId.equals(activityInstanceId)) {
                    log.info("流程结束回调====");
                    Execution execution = runtimeService.createExecutionQuery().executionId(executionId).singleResult();
                    WorkFlowResult.ApprovalStatus approvalStatus = WorkFlowResult.ApprovalStatus.UKN;
                    ExecutionEntity delegate = (ExecutionEntity) executionDelegate;
                    ActivityImpl activity = delegate.getActivity();
                    List<PvmTransition> incomingTransitions=new ArrayList<>();
                    //打回制单 上层没有活跃节点
                    if (Objects.nonNull(activity)) {
                        incomingTransitions = activity.getIncomingTransitions();
                    }
                    String userId = null;
                    if (Objects.nonNull(execution)) {
                        Object taskPassFlag = runtimeService.getVariable(execution.getProcessInstanceId(), WorkFlowConstant.TASK_PASS_FLAG);
                        approvalStatus = WorkFlowResult.ApprovalStatus.of(taskPassFlag);
                        userId = UserContextHelper.helper().getCurrentUser().getId();
                        log.info("execution不为空, execution不为空={}, taskPassFlag={}, approvalStatus={}", execution, taskPassFlag, approvalStatus);
                    } else {
                        // REJECT_START 直接 deleteProcessInstance 后，runtime 的 execution 已被删除，
                        // 尝试从历史流程变量中读取审批标记，避免 approvalStatus 变成 UKN 导致业务回调被跳过
                        HistoricVariableInstance historicVar = historyService.createHistoricVariableInstanceQuery()
                                .processInstanceId(processInstanceId)
                                .variableName(WorkFlowConstant.TASK_PASS_FLAG)
                                .singleResult();
                        if (Objects.nonNull(historicVar) && Objects.nonNull(historicVar.getValue())) {
                            log.info("流程结束回调从历史变量读取审批标记, processInstanceId={}, taskPassFlag={}", processInstanceId, historicVar.getValue());
                            approvalStatus = WorkFlowResult.ApprovalStatus.of(historicVar.getValue());
                            userId = UserContextHelper.helper().getCurrentUser().getId();
                        } else {
                            log.warn("流程结束回调未从历史变量读取到审批标记, processInstanceId={}", processInstanceId);
                        }
                    }
                    //网关直接流入结束节点
                    if (Objects.isNull(execution) && WorkFlowResult.ApprovalStatus.UKN.equals(approvalStatus) && CollectionUtil.isNotEmpty(incomingTransitions)) {
                        PvmActivity source = incomingTransitions.get(0).getSource();
                        if (source.getId().startsWith("Gateway")) {
                            approvalStatus = WorkFlowResult.ApprovalStatus.PASS;
                            userId = "bpmAuto";
                        }
                    }
                    String cateKey = defId.split(":")[0];
                    // 获取流程分类对应的业务回调处理器，未注册时跳过回调（如撤销删除场景）
                    IWorkFlowListener listener = WorkFlowHelper.helper().get(cateKey);
                    if (Objects.isNull(listener)) {
                        log.warn("流程结束回调未找到处理器 , cateKey={}, processInstanceId={}", cateKey, processInstanceId);
                        throw new BusinessException(WorkFlowMessageEnum.WORKFLOW_NO_COMPLETE_ERR);
                    }
                    //查询流程变量审批值 如果网关配置条件直接审批通过 当前用户为空 设置成流程发起人
                    WorkFlowResult workFlowResult = WorkFlowResult.buildResult(businessKey, cateKey, approvalStatus,
                            buildUser(userId));
                    listener.flowCompleted(workFlowResult);
                }
                break;
            default:
                break;
        }
    }

    private CurrentUser buildUser(String uid) {
        UserDTO userDTO = iUserService.getByUserId(uid);
        CurrentUser currentUser = new CurrentUser();
        currentUser.setId(userDTO.getId());
        currentUser.setUsername(userDTO.getUsername());
        currentUser.setCorpId(userDTO.getCorpId());
        currentUser.setLoginName(userDTO.getLoginName());
        return currentUser;
    }

    private void setMulUsers(DelegateExecution executionDelegate, FlowElement flowElement) {
        if (flowElement instanceof UserTask) {
            UserTask userTask = (UserTask) flowElement;

            // 设置审批任务的流程多实例
            /*List<String> candidateUserIds = userTask.getCamundaCandidateUsersList();
            if (ListUtils.isNotNullOrEmpty(candidateUserIds)) {
                executionDelegate.setVariable("assigneeList", candidateUserIds);
            }*/

            //动态解析角色 审批用户
            Set<String> candidates = new HashSet<>();
            //获取用户
            List<String> candidatesByUid = userTask.getCamundaCandidateUsersList();
            if (!CollectionUtils.isEmpty(candidatesByUid)) {
                candidates.addAll(candidatesByUid);
            }
            //获取角色
            List<String> roleIds = userTask.getCamundaCandidateGroupsList();
            //设置的租户ID是单位ID    原先是租户ID 现在为多租户需要修改为单位ID
            String corpId = (String) executionDelegate.getVariable("workFlowCorpId");
            List<String> candidatesByRid = iUserService.listApproveUserIdsByRoleIdAndCorpIdNew(roleIds,corpId, null);
            if(!CollectionUtils.isEmpty(candidatesByRid)){
                candidates.addAll(candidatesByRid);
            }

             if (CollectionUtil.isNotEmpty(candidates)) {
                 executionDelegate.setVariable("assigneeList", candidates);
             }
        }
    }

    /**
     * 流程事件
     *
     * @param executionEvent 执行节点事件
     */
    @EventListener
    public void onExecutionEvent(ExecutionEvent executionEvent) {
        log.debug("executionEvent:{}", JsonUtil.toJson(executionEvent));
        switch (executionEvent.getEventName()) {
            case ExecutionListener.EVENTNAME_START:
                log.info("=======流程启动成功======= 流程定义  " + executionEvent.getProcessDefinitionId());
                break;
            case ExecutionListener.EVENTNAME_END:
                log.debug("流程审批完成回调业务系统=====");

                break;
            default:
                break;
        }
    }

    /**
     * 节点任务事件
     */
    @EventListener
    public void onDelegateTask(DelegateTask task) {
        switch (task.getEventName()) {
            case TaskListener.EVENTNAME_CREATE:
                log.info("===任务创建事件");
                break;
            case TaskListener.EVENTNAME_COMPLETE:
                log.info("====任务完成事件");
                break;
            default:
                break;
        }

    }


}
