package com.bytter.workflow.domain.listener;

import com.bytter.core.common.exception.AssertHelper;
import com.bytter.system.api.IUserService;
import com.bytter.workflow.domain.entity.FlowModel;
import com.bytter.workflow.domain.service.FlowModelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.camunda.bpm.engine.delegate.*;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * ExecutionListener 活动开始时初始化流程变量，专注于流程执行的各个阶段 event = start
 * TaskListener 专注于任务的生命周期事件 event = create
 *
 * userTask.setCamundaCandidateUsersList(); 流程定义时设置；部署时解析 使用
 * delegateTask.addCandidateGroups(); 流程执行时设置
 */
//@Slf4j
//@Component
//public class AssigneeListener implements TaskListener {
//    @DubboReference(check = false)
//    private IUserService iUserService;
//
//    @Autowired
//    private FlowModelService flowModelService;
//
//
//    /**
//     * 任务创建时
//     * @param delegateTask
//     */
//    @Override
//    public void notify(DelegateTask delegateTask) {
//        UserTask userTask = delegateTask.getBpmnModelElementInstance();
//        System.out.println("################ userTask_ID ###############:" + userTask.getId() );
//        //审批用户
//        Set<String> candidates = new HashSet<>();
//        //获取用户
//        List<String> candidatesByUid = userTask.getCamundaCandidateUsersList();
//        if (!CollectionUtils.isEmpty(candidatesByUid)) {
//            candidates.addAll(candidatesByUid);
//        }
//        //获取角色
//        List<String> roleIds = userTask.getCamundaCandidateGroupsList();
//        FlowModel flowModel = flowModelService.findOneByProDefId(delegateTask.getProcessDefinitionId()).get();
//        List<String> candidatesByRid = iUserService.listApproveUserIdsByRoleIdAndCorpId(roleIds, flowModel.getCorpId(), null);
//        if(!CollectionUtils.isEmpty(candidatesByRid)){
//            candidates.addAll(candidatesByRid);
//        }
//
//        //设置动态添加候选人
//        delegateTask.addCandidateGroups(candidates);
//        delegateTask.addCandidateUsers(candidates); //TODO 执行了，Candidate 是候选人、候选组
//        delegateTask.setAssignee("设置真正的待审批人");
//        userTask.setCamundaCandidateUsersList(new ArrayList<>(candidates));
//        userTask.updateAfterReplacement();
//    }
//
//    /**
//     * 这个是 实现 JavaDelegate
//     * @param delegateExecution
//     * @throws Exception
//     */
//    /*@Override
//    public void execute(DelegateExecution delegateExecution) throws Exception {
//        //获取当前执行的BPMN模型实例
//        BpmnModelInstance modelInstance = delegateExecution.getBpmnModelInstance();
//        //获取当前执行的活动（例如任务）的ID
//        String currentActivityId = delegateExecution.getCurrentActivityId();
//        //通过活动ID获取模型元素
//        org.camunda.bpm.model.bpmn.instance.UserTask userTask = modelInstance.getModelElementById(currentActivityId); //ModelElementInstance
//
//        //审批用户
//        Set<String> candidates = new HashSet<>();
//        //用户设置
//        List<String> candidatesByUid = userTask.getCamundaCandidateUsersList();
//        if (!CollectionUtils.isEmpty(candidatesByUid)) {
//            candidates.addAll(candidatesByUid);
//        }
//
//        List<String> roleIds = userTask.getCamundaCandidateGroupsList();
//        List<String> candidatesByRid = iUserService.listApproveUserIdsByRoleIdAndCorpId(roleIds, corpId, null);
//        if(!CollectionUtils.isEmpty(candidatesByRid)){
//            candidates.addAll(candidatesByRid);
//        }
//
//
//        //设置节点审批人
//        delegateExecution.setVariable("dynamicApprovers",new ArrayList<>(candidates)); //第二个参数可能会是 String，用逗号分隔
//    }*/
//
//    /**
//     * 实现 ExecutionListener
//     * 设置 变量使用 delegateExecution.setVariable("dynamicApprovers",new ArrayList<>(candidates));
//     * @param delegateExecution
//     * @throws Exception
//     */
////    @Override
////    public void notify(DelegateExecution delegateExecution) throws Exception {
////        //获取当前执行的BPMN模型实例
////        BpmnModelInstance modelInstance = delegateExecution.getBpmnModelInstance();
////        //获取当前执行的活动（例如任务）的ID
////        String currentActivityId = delegateExecution.getCurrentActivityId();
////        //通过活动ID获取模型元素 ; 这里是获取单个 TODO
////        org.camunda.bpm.model.bpmn.instance.UserTask userTask = modelInstance.getModelElementById(currentActivityId); //ModelElementInstance
////
////        //业务 通过 pro_def_id 查询扩展表
////        String processDefinitionId = delegateExecution.getProcessDefinitionId();
////        FlowModel flowModel = flowModelService.findOneByProDefId(processDefinitionId).get();
////
//////        Collection<UserTask> userTasks = modelInstance.getModelElementsByType(UserTask.class); //或者 直接获取所有，进行设置
////
////        //审批用户
////        Set<String> candidates = new HashSet<>();
////        //用户设置
////        List<String> candidatesByUid = userTask.getCamundaCandidateUsersList();
////        if (!CollectionUtils.isEmpty(candidatesByUid)) {
////            candidates.addAll(candidatesByUid);
////        }
////
////        List<String> roleIds = new ArrayList<>();
////        roleIds = userTask.getCamundaCandidateGroupsList(); //TODO 这里被 改成了 动态表达式，应该获取不到参数了;
////        String customizeGroups = (String) delegateExecution.getVariable("customizeGroups"); //bpm模板自定义的参数，逗号分隔 ； 或者直接 setVariable
////        if (ObjectUtils.isNotEmpty(customizeGroups)){
////            roleIds = Arrays.asList(customizeGroups.split(","));
////        }
////        List<String> candidatesByRid = iUserService.listApproveUserIdsByRoleIdAndCorpId(roleIds, flowModel.getCorpId(), null);
////        if(!CollectionUtils.isEmpty(candidatesByRid)){
////            candidates.addAll(candidatesByRid);
////        }
////
////
////        //设置节点审批人 TODO 哪种？
////        delegateExecution.setVariable("dynamicApprovers",new ArrayList<>(candidates)); //第二个参数可能会是 String，用逗号分隔
////
////        //AssertHelper.isTrue(candidates.size() > 0, userTask.getName() + "未配置审批用户");
////        //userTask.setCamundaCandidateUsersList(new ArrayList<>(candidates)); //这个在此处设置应该不生效 ，需要在 TaskListener 的 event = create 进行设置
////
////    }
//
//}
