package com.bytter.workflow.domain.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.constant.MsgRecordStatus;
import com.bytter.core.common.WorkFlowVerifyException;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.workflow.dto.WorkFlowInput;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.workflow.dto.WorkFlowResult;
import com.bytter.core.common.exception.AssertHelper;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.page.IParameter;
import com.bytter.core.common.page.IParameterInput;
import com.bytter.core.enums.CommonMessageEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.StringUtil;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.*;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.workflow.constant.OptionConstant;
import com.bytter.workflow.constant.WorkFlowConstant;
import com.bytter.workflow.domain.entity.FlowCategory;
import com.bytter.workflow.domain.entity.FlowModel;
import com.bytter.workflow.domain.entity.FlowMsgRecord;
import com.bytter.workflow.domain.entity.FlowOption;
import com.bytter.workflow.domain.mapper.FlowTaskMapper;
import com.bytter.workflow.domain.service.*;
import com.bytter.workflow.domain.util.BpmnModelUtils;
import com.bytter.workflow.domain.vo.TaskDetailVo;
import com.bytter.workflow.dto.*;
import com.bytter.workflow.enums.FlowCategoryMessageEnum;
import com.bytter.workflow.enums.WorkFlowMessageEnum;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcException;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstanceQuery;
import org.camunda.bpm.engine.impl.RepositoryServiceImpl;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceModificationInstantiationBuilder;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
@Service
@Slf4j
public class WorkFlowServiceImpl implements IWorkFlowService {
    private final RuntimeService runtimeService;
    private final FlowModelService flowModelService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private FlowTaskMapper flowTaskMapper;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private FlowCategoryService flowCategoryService;
    @Autowired
    private FlowMessageService flowMessageService;
    @Autowired
    private FlowOptionService flowOptionService;
    @Autowired
    private FlowMsgRecordService flowMsgRecordService;
    @Autowired
    private FlowTaskService flowTaskService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false, async = true)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @Autowired
    private ProcessInstanceService processInstanceService;

    //资金调拨类
    private static final Set<String> netbankAllocationSet= Sets.newHashSet("netbankAllocationDown", "netbankAllocationAsk", "netbankAllocationPos","netbankAllocationUp","netbankAllocationTurn");
    private static final Set<String> cmsLoanWorkflowSet= Sets.newHashSet("cmsloanInfo", "cmsloanRepair");

    public WorkFlowServiceImpl(RuntimeService runtimeService, FlowModelService flowModelService) {
        this.flowModelService = flowModelService;
        this.runtimeService = runtimeService;
    }
    @Override
    public List<CommentDTO> listComments(String processInstanceId) {
        return null;
    }
    @Override
    public WorkFlowBusiness getWorkFlowBusiness(BusinessTaskQueryDTO queryDTO) {
        List<BusinessTaskDTO> businessTaskDTOS = flowTaskMapper.listBusinessTask(queryDTO.getUserId(), queryDTO.getCateKey());
        return taskDTO2WorkFlowBusiness(businessTaskDTOS);
    }
    @Override
    public WorkFlowBusiness getWorkFlowBusiness(String processDefinitionKey, IParameterInput pagePara) {
        IParameter parameter = pagePara.getParameter();
        String flowStatus = parameter.get("flowStatus");
        String cycleTime = parameter.get("cycleTime");
        BusinessTaskQueryDTO queryDTO = BusinessTaskQueryDTO.change(processDefinitionKey, pagePara);
        switch (flowStatus) {
            case IWorkFlowService.TODO:
                return getWorkFlowBusiness(queryDTO);
            case IWorkFlowService.TRACK:
                return getTrackWorkFlowBusiness(processDefinitionKey, pagePara.getCurrentUserId());
            case IWorkFlowService.PASS:
                return getFinishedFlowWorkFlowBusiness(processDefinitionKey, cycleTime, pagePara.getCurrentUserId());
            default:
                throw new ParameterException("illegal parameter");
        }
    }
    private WorkFlowBusiness getFinishedFlowWorkFlowBusiness(String processDefinitionKey, String cycleTime, String userId ){
        WorkFlowBusiness workFlowBusiness = new WorkFlowBusiness();
        //与「已审批」分类角标口径保持一致：同一个人、同一个流程、同一个时间窗，未传 cycleTime 时不限制
        Date startTime = FlowTaskService.resolveCycleStartTime(cycleTime, 0);
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
        historicTaskInstanceQuery.taskAssignee(userId);
        historicTaskInstanceQuery.processDefinitionKey(processDefinitionKey);
        historicTaskInstanceQuery.finished();
        if (Objects.nonNull(startTime)) {
            historicTaskInstanceQuery.startedAfter(startTime);
        }
        List<HistoricTaskInstance> historicTaskInstances = historicTaskInstanceQuery.list();
        Set<String> processInstanceIds = historicTaskInstances.stream().map(HistoricTaskInstance::getProcessInstanceId).collect(Collectors.toSet());
        if (processInstanceIds.isEmpty()) {
            //Camunda 的 processInstanceIds 传空集合时不会拼查询条件，会查出全部流程实例
            workFlowBusiness.setData(new ArrayList<>());
            return workFlowBusiness;
        }
        //用 finished 而非 completed：正常结束的流程实例 DELETE_REASON_ 可能被置为 completed，
        //completed 会把这些数据漏掉，与角标统计的口径不一致
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().processInstanceIds(processInstanceIds).finished().list();
        List<WorkFlowPersistence> userTasks = list.stream().map(item -> {
            WorkFlowPersistence workFlowPersistence = new WorkFlowPersistence();
            workFlowPersistence.setProcessInstanceId(item.getRootProcessInstanceId());
            workFlowPersistence.setBusinessKey(item.getBusinessKey());
            return workFlowPersistence;
        }).collect(Collectors.toList());
        workFlowBusiness.setData(userTasks);

        return workFlowBusiness;
    }
    @Override
    public List<String> listFinishedBusinessKeys(String processDefinitionKey, String cycleTime, String userId) {
        //时间窗口与 countFinishGpCateKey 用同一个方法计算，保证明细与角标口径一致
        return flowTaskMapper.listFinishedBusinessKeys(userId, processDefinitionKey,
                FlowTaskService.resolveCycleStartTime(cycleTime, 0), new Date());
    }

    private WorkFlowBusiness getTrackWorkFlowBusiness(String processDefinitionKey, String userId) {
        //用户启动的流程和用户审批的流程
        List<BusinessTaskDTO> businessTaskDTOS = flowTaskMapper.listTrackTask(userId, processDefinitionKey);
        return taskDTO2WorkFlowBusiness(businessTaskDTOS);
    }

    private WorkFlowBusiness taskDTO2WorkFlowBusiness(List<BusinessTaskDTO> businessTaskDTOS) {
        WorkFlowBusiness workFlowBusiness = new WorkFlowBusiness();
        businessTaskDTOS.forEach(item-> workFlowBusiness.add(new WorkFlowPersistence(item.getTaskId(), item.getTaskName(),
                item.getProcessInstanceId(), item.getBusinessKey())));
        return workFlowBusiness;
    }

    @Override
    public WorkFlowBusiness getWorkFlowBusiness(String processDefinitionKey, String userId) {
        BusinessTaskQueryDTO queryDTO = new BusinessTaskQueryDTO();
        queryDTO.setCateKey(processDefinitionKey);
        queryDTO.setUserId(userId);
        return getWorkFlowBusiness(queryDTO);
    }
    @Override
    public List<String> getTaskIdsByBusinessKeysAndCateKey(List<String> businessKeys) {
        return taskService.createTaskQuery().processInstanceBusinessKeyIn(businessKeys.toArray(new String[0]))
                .list().stream().map(Task::getId).collect(Collectors.toList());
    }

    /**
     * 【启动流程实例】
     * @param piStartDTO 流程启动key
     * @param variable
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean start(PIStartDTO piStartDTO,Map<String,Object> variable) {
        FlowCategory flowCategory = flowCategoryService.findByKey(piStartDTO.getCateKey());
        AssertHelper.isTrue(Objects.nonNull(flowCategory), FlowCategoryMessageEnum.BPM_FLOW_CATEGORY_NOT_EXISTS);
        //查询单位下发布过的流程 前置已经校验此处不在校验
        FlowModel model = flowModelService.findDeployByCateKeyAndCorpId(piStartDTO.getCateKey(), piStartDTO.getCorpId()).get();
        AssertHelper.isTrue(StringUtil.isNotEmpty(piStartDTO.getUserId()), WorkFlowMessageEnum.BPM_MODEL_START_USER_NULL);
        //增加业务是否已经存在流程问题校验 防止重复生成 暂时不加锁 数据库兜底
        long count = runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(piStartDTO.getBusinessKey())
                .processDefinitionId(model.getProDefId()).count();
        AssertHelper.isTrue(count <= 0L, WorkFlowMessageEnum.WORK_FLOW_WAS_EXISTS, piStartDTO.getBusinessKey());
        setAuthenticatedContext(piStartDTO.getUserId());
        try {
            //增加创建人保存
            UserDTO userDTO = userService.getByUserId(piStartDTO.getUserId());
            variable.put(OptionConstant.EXTEND_PARAM_USERNAME_KEY, userDTO.getUsername());
            runtimeService.startProcessInstanceById(model.getProDefId(), piStartDTO.getBusinessKey(), filterVariables(model.getCategoryId(), variable)); //启动流程实例
            //启动工作流刷新消息待办页面消息通知 广播 不进行过滤
            notificationService.broadcastMessage(SocketMsg.build(SocketMsg.Type.HOME_NOTICE));
        }catch (Exception e){
            log.error("流程启动异常", e);
            throw new BusinessException(WorkFlowMessageEnum.WORK_FLOW_DESIGN_ERROR);
        }
        return Boolean.TRUE;
    }


    private Map<String, Object> filterVariables(String cateKey, Map<String, Object> variables) {
        if (CollectionUtils.isEmpty(variables)) {
            return variables;
        }
        Map<String, Object> filterVars = new HashMap<>();
        //配置条件
        List<FlowOption> flowOptions = flowOptionService.listConditionByCategoryId(cateKey);
        flowOptions.stream().filter(item -> StringUtil.isNotEmpty(item.getFieldName())
                        &&variables.containsKey(item.getFieldName()))
                .forEach(item->{
                    Object value = variables.get(item.getFieldName());
                    if (value instanceof BigDecimal) {
                        filterVars.put(item.getFieldName(), ((BigDecimal) value).doubleValue());
                    }else{
                        filterVars.put(item.getFieldName(), value);
                    }
                });
        //网关条件可能直接引用 voucherType（如 ${voucherType>="500000"}），即使流程条件配置未包含该字段也必须保证变量存在，
        //否则流程经过排他网关时抛 Cannot resolve identifier 'voucherType'
        if (!filterVars.containsKey("voucherType")) {
            filterVars.put("voucherType", variables.getOrDefault("voucherType", "0"));
        }
        //启动流程时数据快照,app根据配置字段展示
        setBaseData(variables);
        filterVars.put(WorkFlowConstant.BUSINESS_JSON, JsonUtils.toJSONString(variables));
        filterVars.put("workFlowCorpId", variables.get("workFlowCorpId"));

        return filterVars;
    }

    //如果是基础模块的数据会默认冗余
    private void setBaseData(Map<String,Object> variables) {
        if (variables.containsKey("curId") && variables.get("curId") != null) {
            BtCurrencyDTO btCurrencyDTO = currencyService.getObjectById((String) variables.get("curId"));
            //多个币种
            if (ObjectUtil.isNotNull(btCurrencyDTO)) {
                variables.put("curName", btCurrencyDTO.getCurName());
            }
        }
    }

    /**
     * 返回单位下业务是否有审批流模型 同时校验是否开启必须审批
     *
     */
    @Override
    public Boolean hasApproveModel(String cateKey, String corpId) {
        FlowCategory flowCategory = flowCategoryService.findByKey(cateKey);
        AssertHelper.isTrueBusEx(Objects.nonNull(flowCategory), FlowCategoryMessageEnum.BPM_FLOW_CATEGORY_NOT_EXISTS);
        //查询单位下发布过的流程
        Optional<FlowModel> model = flowModelService.findDeployByCateKeyAndCorpId(cateKey, corpId);
        //校验是否必须挂接审批
        if (flowCategory.isMustApprove()) {
            CorpDTO corpDTO = corpService.get(corpId);
            AssertHelper.isTrueBusEx(model.isPresent(), (WorkFlowMessageEnum.WORK_FLOW_MUST_APPROVE), corpDTO.getCode(), corpDTO.getName());
        }
        return model.isPresent();
    }

    @Override
    public List<BusinessTaskDTO> listBusinessTask(String userId, String cateKey) {
        return flowTaskMapper.listBusinessTask(userId,cateKey);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean start(String processDefinitionKey, String businessKey, Object variables, String corpId, String startUserId) {
        PIStartDTO piStartDTO = PIStartDTO.builder().cateKey(processDefinitionKey)
                .businessKey(businessKey).corpId(corpId).userId(startUserId).comment("启动工作流").build();

        Map<String, Object> map = BeanUtils.beanToMap(variables);
        map.put("workFlowCorpId",corpId);
        return start(piStartDTO, map);
    }

    /**
     * 设置流程引擎认证上下文（用户 + 租户）。
     * 流程定义是按租户部署的（FlowModelService.deploy 设置 tenantId），Camunda 引擎在启动/审批/撤回时
     * 会做租户校验，必须把当前租户设置为认证租户，否则抛 ENGINE-13034
     * "Cannot create an instance of the process definition ... because it belongs to no authenticated tenant"。
     * 注意：identityService.setAuthenticatedUserId() 内部构造的是无租户的 Authentication，会清空认证租户，
     * 因此这里统一走 setAuthentication(userId, groups, tenantIds)。
     *
     * @param userId 当前操作人ID
     */
    private void setAuthenticatedContext(String userId) {
        String tenantId = UserContextHelper.helper().getTTLTenant();
        if (StringUtil.isNotEmpty(tenantId)) {
            identityService.setAuthentication(userId, null, Collections.singletonList(tenantId));
        } else {
            identityService.setAuthenticatedUserId(userId);
        }
    }
    @Override
    public ProcessInstanceDTO startByScheduler(String processDefinitionKey, String businessKey, Object variables, String corpId) {
        throw new BusinessException("方法废弃，定时任务调用工作流请使用getStartHandler().handler 传递WorkFlowConstant.SCH_UID 启动");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void complete(WorkFlowInput input, ICurrentUser currentUser) {
        List<String> taskList = input.getTaskList();
        AssertHelper.isTrueBusEx(CollectionUtil.isNotEmpty(taskList), CommonMessageEnum.COMMON_MESSAGE_UNKNOWN_ERROR);
        // 查询任务前设置当前用户和租户，避免线程残留的认证租户将有效任务过滤掉。
        setAuthenticatedContext(currentUser.getId());
        List<ProcessInstance> processInstances = processInstanceService.listProcessesByTaskIds(taskList);
        // 多窗口同时审核：一方通过后任务已不存在，给出明确提示，避免底层空集合异常
        AssertHelper.isTrueBusEx(CollectionUtil.isNotEmpty(processInstances), WorkFlowMessageEnum.WORK_FLOW_TASK_WAS_DEAL);
        boolean isNeedVerify = false;
        if (input.getParameter().get("isNeedVerify") != null) {
            isNeedVerify = input.getParameter().get("isNeedVerify");
        }
        try {
        	if(input.getWorkflowPass().equals(WorkFlowConstant.PASS)) {//只有审批通过才校验
        		beforeSubmitVerify(processInstances, isNeedVerify); //当前只有部分业务做了前置校验，很多业务没有做；
        	}

        }catch (WorkFlowVerifyException w){
            log.info("工作流校验异常: ",w);
            throw w; //app 需要特殊处理 响应码
        }catch (RpcException rpcException){
            log.error("工作流 RpcException 异常: ",rpcException);
            log.info("errro暂未增加校验");
            //throw rpcException;
        }

        for (String taskId : taskList) {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            AssertHelper.isTrueBusEx(Objects.nonNull(task), WorkFlowMessageEnum.WORK_FLOW_TASK_WAS_DEAL);
            String comment = input.getComment();
            if (StringUtils.isEmpty(comment)) {
                comment = "";
            }
            String processInstanceId = task.getProcessInstanceId();
            taskService.createComment(taskId, processInstanceId, comment);
            switch (input.getWorkflowPass()) {
                case WorkFlowConstant.PASS:
                    String executionId = task.getExecutionId();
                    // 记录当前任务节点的状态为通过
                    taskService.setVariableLocal(taskId, WorkFlowConstant.TASK_PASS_FLAG, WorkFlowConstant.PASS);
                    runtimeService.setVariable(executionId, WorkFlowConstant.TASK_PASS_FLAG, WorkFlowConstant.PASS);
                    // 解决流程审批回调【流程审批标志】变量不正确的问题。
                    Map<String, Object> taskVariables = runtimeService.getVariables(executionId);
                    taskVariables.put(WorkFlowConstant.TASK_PASS_FLAG, WorkFlowConstant.PASS);
                    //历史流程实例可能缺少网关条件引用的变量（如 voucherType），
                    //从启动时的业务数据快照恢复缺失变量，避免排他网关条件表达式解析失败
                    fillMissingVariablesFromSnapshot(taskVariables);
                    taskService.complete(taskId, taskVariables); // 之后调用具体 Service 类的 flowCompleted 方法 ; -- ProcessEventListener监听 bpm在spring的事件，之后发送消息 -- WorkflowCallBackEvent 消费消息，最后调用 -- flowCompleted 方法
                    break;
                case WorkFlowConstant.REJECT:
                    // 打回上一级
                    taskService.setVariableLocal(taskId, WorkFlowConstant.TASK_PASS_FLAG, WorkFlowConstant.REJECT);
                    runtimeService.setVariable(task.getExecutionId(), WorkFlowConstant.TASK_PASS_FLAG, WorkFlowConstant.REJECT);
                    rejectBeforeLevel(task, comment);
                    break;
                case WorkFlowConstant.REJECT_START:
                    // 打回制单处
                    taskService.setVariableLocal(taskId, WorkFlowConstant.TASK_PASS_FLAG, WorkFlowConstant.REJECT_START);
                    runtimeService.setVariable(task.getExecutionId(), WorkFlowConstant.TASK_PASS_FLAG, WorkFlowConstant.REJECT_START);
                    terminateProcess(task, comment);
                    break;
                default:
                    break;
            }
        }
        //审批通过后刷新页面数据
        notificationService.broadcastMessage(SocketMsg.build(SocketMsg.Type.HOME_NOTICE));
    }

    /**
     * 从启动流程时的业务数据快照 businessJson 中恢复缺失的流程变量。
     * 历史流程实例启动时可能因条件字段未配置或旧版本代码未传参，
     * 缺少网关条件引用的变量（如 ${voucherType>="500000"} 中的 voucherType），
     * 审批经过排他网关时会抛 Cannot resolve identifier 异常导致流程卡死。
     * 只补缺失的变量，不覆盖已有值；快照中也没有 voucherType 时兜底 "0"，保证表达式可计算。
     */
    private void fillMissingVariablesFromSnapshot(Map<String, Object> taskVariables) {
        Object snapshotJson = taskVariables.get(WorkFlowConstant.BUSINESS_JSON);
        if (snapshotJson instanceof String && StringUtil.isNotEmpty((String) snapshotJson)) {
            Map<?, ?> snapshot = JsonUtils.json2Map((String) snapshotJson);
            if (!CollectionUtils.isEmpty(snapshot)) {
                snapshot.forEach((key, value) -> taskVariables.putIfAbsent((String) key, value));
            }
        }
        //快照中也不存在时兜底，"0" 无论是按数值还是按字符串比较都小于 "500000"，不会误入高档分支
        taskVariables.putIfAbsent("voucherType", "0");
    }

    /**
     * 业务前置校验
     * @param isNeedVerify 校验失败是否允许提交 true不允许
     */
    public void beforeSubmitVerify(List<ProcessInstance> processInstances, boolean isNeedVerify){
        //通过流程定义获取流程分类key
        String processDefinitionId = processInstances.get(0).getProcessDefinitionId();
        String cateKey = processDefinitionId.split(":")[0];
        //资金归结相关校验共用一个sevice实现 需要特殊处理
        if (netbankAllocationSet.contains(cateKey)) {
            cateKey = "netbankAllocation";
        }
        if (cmsLoanWorkflowSet.contains(cateKey)) {
            cateKey = "cmsLoanWorkflow";
        }

        ReferenceConfig<IWorkFlowListener> referenceConfig=new ReferenceConfig<>();
        referenceConfig.setGroup(cateKey);
        referenceConfig.setInterface(IWorkFlowListener.class);
        IWorkFlowListener iWorkFlowListener = referenceConfig.get();
        Map<String, List<String>> params = new HashMap<>();
        params.put(cateKey, processInstances.stream().map(ProcessInstance::getBusinessKey).collect(Collectors.toList()));
        iWorkFlowListener.submitVerify(params, WorkFlowConstant.PASS, isNeedVerify);

    }
    private void rejectBeforeLevel(Task currTask,String comment) {
        // 取得流程定义
        ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(currTask.getProcessDefinitionId());
        if (Objects.isNull(definition)) {
            throw new BusinessException("未找到流程定义");
        }
        // 取得流程实例
        ProcessInstance processInstance = runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(currTask.getProcessInstanceId())
                .singleResult();
        if (Objects.isNull(processInstance)) {
            log.error("流程已经结束，不能驳回。当前任务：{}", currTask);
            throw new BusinessException("流程已经结束，不能驳回");
        }
        // 取得上一步活动
        ActivityImpl currActivity = definition.findActivity(currTask.getTaskDefinitionKey());
        UserTask backUserTask = getBackUserTask(definition, currActivity);
        if (Objects.isNull(backUserTask)) {
            // 没有可以选择的驳回节点，终止流程
            terminateProcess(currTask, comment);
            return;
        }
        // 上一级审批活动节点的ID
        String backActivityId = backUserTask.getId();
        ActivityImpl backActivity = definition.findActivity(backActivityId);
        if (Objects.nonNull(backActivity) && backActivity.isMultiInstance()) {
            // 流程多实例时启动，活动ID需要加上#multiInstanceBody
            backActivityId = backActivityId + "#multiInstanceBody";
        }


//        List<String> candidateUserIds = backUserTask.getCamundaCandidateUsersList();
        //动态解析角色
        Set<String> candidateUserIds = this.getAssigneeList(backUserTask, currTask.getProcessDefinitionId());

        ProcessInstanceModificationInstantiationBuilder modificationBuilder = runtimeService
                .createProcessInstanceModification(processInstance.getProcessInstanceId())
                // 关闭相关任务
                .cancelAllForActivity(currActivity.getActivityId())
                // 启动目标活动节点
                .startBeforeActivity(backActivityId);
        if (backActivity.isMultiInstance()) {
            //
            // 设置流程会签人员
            modificationBuilder = modificationBuilder.setVariable("assigneeList", candidateUserIds);
        }
        modificationBuilder.execute();
    }

    /**
     * 角色解析设置
     * @param userTask
     * @param processDefinitionId
     * @return
     */
    private Set<String> getAssigneeList(UserTask userTask, String processDefinitionId){
        //审批用户
        Set<String> candidates = new HashSet<>();
        //获取用户
        List<String> candidatesByUid = userTask.getCamundaCandidateUsersList();
        if (!CollectionUtils.isEmpty(candidatesByUid)) {
            candidates.addAll(candidatesByUid);
        }
        //获取角色
        List<String> roleIds = userTask.getCamundaCandidateGroupsList();
//        String processDefinitionId = executionDelegate.getProcessDefinitionId();
        FlowModel flowModel = flowModelService.findOneByProDefId(processDefinitionId).orElse(null);
        if (Objects.isNull(flowModel)) {
            log.warn("未找到流程模型, processDefinitionId={}", processDefinitionId);
            return candidates;
        }
        List<String> candidatesByRid = userService.listApproveUserIdsByRoleIdAndCorpId(roleIds, flowModel.getCorpId(), null);
        if(!CollectionUtils.isEmpty(candidatesByRid)){
            candidates.addAll(candidatesByRid);
        }
        return candidates;
    }


    private void terminateProcess(Task task, String comment) {
        String processInstanceId = task.getProcessInstanceId();
        String taskId = task.getId();
        taskService.createComment(taskId, processInstanceId, comment);
        runtimeService.deleteProcessInstance(processInstanceId, TaskEntity.DELETE_REASON_COMPLETED);
    }
    private UserTask getBackUserTask(ProcessDefinitionEntity processDefinition,
                                     ActivityImpl currActivity) {
        // 需要根据部署ID去查找流程模型实例，因为不同的模型版本，它的流程是不一样的
        BpmnModelInstance modelInstance = Bpmn.readModelFromStream(repositoryService.getResourceAsStream(
                processDefinition.getDeploymentId(),
                processDefinition.getResourceName()));
        if (Objects.nonNull(modelInstance)) {
            // 当前审批任务
            UserTask currentUserTask = BpmnModelUtils.findUserTask(modelInstance, currActivity.getActivityId());
            if (Objects.isNull(currentUserTask)) {
                log.error("找不到当前审批任务：ActivityName={}", currActivity.getName());
                return null;
            }
            // 上一级审批节点集合
            List<UserTask> backUserTasks = new ArrayList<>();
            // 查找上一级的用户审批任务
            List<FlowNode> previousNodes = currentUserTask.getPreviousNodes().list();
            if (!CollectionUtils.isEmpty(previousNodes)) {
                for (FlowNode node : previousNodes) {
                    BpmnModelUtils.findBackUserTask(node, backUserTasks);
                }
            }
            if (CollectionUtils.isEmpty(backUserTasks)) {
                // 找不到上一级的用户审批任务
                return null;
            }
            return backUserTasks.get(0);
        }
        return null;
    }
    @Override
    public Map<String, Map<String, WorkFlowMessageEnum>> batchRevoke(String[] businessIds, String userId) {
        if (businessIds.length <= 0 || StringUtils.isEmpty(userId)) {
            throw new BusinessException("参数缺失");
        }
        // 允许撤回的流程实例ID集合
        List<String> canRevokeProcessInstanceIdList = new ArrayList<>(businessIds.length);
        // <单位ID,<businessId, WorkFlowRevokeFailureMessageVo>>
        Map<String, Map<String, WorkFlowMessageEnum>> errorMap = new HashMap<>(8);
        for (String businessId : businessIds) {
            // 获得网点，如果corpId=null的话，说明流程不存在
            String corpId = null;
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceBusinessKey(businessId).singleResult();
            if (Objects.isNull(processInstance)) {
                List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessId).list();;
                if (!CollectionUtils.isEmpty(historicProcessInstanceList)) {
                    // 获取流程实例的单位ID
                    corpId = historicProcessInstanceList.get(0).getTenantId();
                    revokeResultMap(errorMap, corpId, businessId, WorkFlowMessageEnum.WORK_FLOW_TASK_WAS_DEAL);
                    continue;
                } else {
                    revokeResultMap(errorMap, corpId, businessId, WorkFlowMessageEnum.WORK_FLOW_NOT_EXISTS);
                    continue;
                }
            }
            // 获取流程实例的单位ID
            corpId = processInstance.getTenantId();;
            if (Objects.isNull(corpId)) {
                continue;
            }
            // 业务要点1:model必须配置成可撤回的才能撤回
            Optional<FlowModel> optionalFlowModel = flowModelService.findOneByProDefId(processInstance.getProcessDefinitionId());
            if (optionalFlowModel.isPresent() && !optionalFlowModel.get().isRevokeAble()) {
                revokeResultMap(errorMap, corpId, businessId, WorkFlowMessageEnum.WORK_FLOW_NOT_ALLOW_REVOKE);
                continue;
            }
            // 业务要点2:撤回人和业务发起人必须是同一个人
            HistoricProcessInstance historicProcessInstance = getHistoricProcessInstance(businessId, processInstance.getId());
            if (Objects.nonNull(historicProcessInstance)) {
                String processStartUserId = historicProcessInstance.getStartUserId();
                if (!userId.equals(processStartUserId)) {
                    revokeResultMap(errorMap, corpId, businessId, WorkFlowMessageEnum.WORK_FLOW_REVOKE_USER_ERROR);
                    continue;
                }
            }
            String processInstanceId = processInstance.getId();
            //业务要点3：有人审批过则不能撤回
            long finishedCount = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).finished().count();
            if (finishedCount != 0) {
                revokeResultMap(errorMap, corpId, businessId, WorkFlowMessageEnum.WORK_FLOW_APPROVED);
                continue;
            }
            canRevokeProcessInstanceIdList.add(processInstanceId);
        }
        setAuthenticatedContext(userId);
        for (String tmpProcessInstanceId : canRevokeProcessInstanceIdList) {
            List<Task> taskList = taskService
                    .createTaskQuery()
                    .processInstanceId(tmpProcessInstanceId)
                    .list();
            // 设置审批任务状态为撤回
            for (Task task : taskList) {
                taskService.setVariable(task.getId(), WorkFlowConstant.TASK_PASS_FLAG, WorkFlowResult.ApprovalStatus.RK.getCode());
            }
            // 设置流程状态为撤回
            runtimeService.setVariable(tmpProcessInstanceId, WorkFlowConstant.TASK_PASS_FLAG,  WorkFlowResult.ApprovalStatus.RK.getCode());
            // 删除流程实例
            runtimeService.deleteProcessInstance(tmpProcessInstanceId, "REVOKE");
        }
        //撤回刷新首页消息
        notificationService.broadcastMessage(SocketMsg.build(SocketMsg.Type.HOME_NOTICE));
        return errorMap;
    }
    private HistoricProcessInstance getHistoricProcessInstance(String businessId,
                                                               String processInstanceId) {
        return historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .processInstanceBusinessKey(businessId)
                .singleResult();
    }
    private Map<String, Map<String, WorkFlowMessageEnum>> revokeResultMap(Map<String, Map<String, WorkFlowMessageEnum>> errorMap, String corpId, String businessId, WorkFlowMessageEnum workFlowMessageEnum) {
        errorMap.computeIfAbsent(corpId, k -> new HashMap<>());
        errorMap.get(corpId).put(businessId, workFlowMessageEnum);
        return errorMap;
    }
    @Override
    public List<String> checkWorkFlowMustApprovedByCorpIds(String processDefinitionKey, List<String> corpIds) {
        return null;
    }
    @Override
    public void deleteRuntimeTaskByCategoryAndBusinessId(String category, String businessId) {
    }
    @Override
    public List<TaskResponseDTO> listCommentsByBusinessId(String businessId) {
        String processInstanceId = "";
        List<TaskDetailVo> taskResponses = new ArrayList<>();
        List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessId).orderByProcessInstanceStartTime().desc().list();
        if (EmptyUtils.isNotEmpty(historicProcessInstanceList)) {
            for(HistoricProcessInstance historicProcessInstance:historicProcessInstanceList) {
                processInstanceId = historicProcessInstance.getId();
                if (com.bytter.core.utils.StringUtils.isNotBlank(processInstanceId)) {
                    List<TaskDetailVo> taskResponsesTemp = flowTaskService.listTaskResponse(processInstanceId);
                    if (!CollectionUtils.isEmpty(taskResponsesTemp)) {
                        taskResponses.addAll(taskResponsesTemp);
                    }
                }
            }
        }
        if (CollectionUtils.isEmpty(taskResponses)) {
            return BeanUtils.copyListProperties(taskResponses, TaskResponseDTO.class);
        }
        Set<String> userIds = new HashSet<>();
        for (TaskDetailVo taskResponse : taskResponses) {
            userIds.add(taskResponse.getUserId());
        }
        Map<String, Set<String>> roleMap = new HashMap<>();

        List<UserRoleDTO> list = userService.getUserRole(userIds);
        for (UserRoleDTO userRoleVo : list) {
            Set<String> roleNames;
            if (roleMap.containsKey(userRoleVo.getUserId())) {
                roleNames = roleMap.get(userRoleVo.getUserId());
            } else {
                roleNames = new HashSet<>();
            }
            roleNames.add(userRoleVo.getRoleName());
            roleMap.put(userRoleVo.getUserId(), roleNames);
        }
        for (TaskDetailVo taskResponse : taskResponses) {
            taskResponse.setRoleNames(roleMap.get(taskResponse.getUserId()));
        }
        return BeanUtils.copyListProperties(taskResponses, TaskResponseDTO.class);
    }
    @Override
    public List<TaskResponseDTO> listAllCommentsByBusinessId(String businessId) {
        // 通过业务ID查询流程实例
        String processInstanceId = "";
        List<TaskDetailVo> taskResponses = new ArrayList<>();
        List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(businessId)
                .orderByProcessInstanceStartTime()
                .desc().list();
        if (EmptyUtils.isNotEmpty(historicProcessInstanceList)) {
            for(HistoricProcessInstance historicProcessInstance:historicProcessInstanceList) {
                processInstanceId = historicProcessInstance.getId();
                if (com.bytter.core.utils.StringUtils.isNotBlank(processInstanceId)) {
                    List<TaskDetailVo> taskResponsesTemp = flowTaskService.listTaskResponse(processInstanceId);
                    if (!CollectionUtils.isEmpty(taskResponsesTemp)) {
                        taskResponses.addAll(taskResponsesTemp);
                    }
                }
            }
        }
        if (CollectionUtils.isEmpty(taskResponses)) {
            return BeanUtils.copyListProperties(taskResponses, TaskResponseDTO.class);
        }
        Set<String> userIds = new HashSet<>();
        for (TaskDetailVo taskResponse : taskResponses) {
            userIds.add(taskResponse.getUserId());
        }
        Map<String, Set<String>> roleMap = new HashMap<>();

        List<UserRoleDTO> list = userService.getUserRole(userIds);
        for (UserRoleDTO userRoleVo : list) {
            Set<String> roleNames;
            if (roleMap.containsKey(userRoleVo.getUserId())) {
                roleNames = roleMap.get(userRoleVo.getUserId());
            } else {
                roleNames = new HashSet<>();
            }
            roleNames.add(userRoleVo.getRoleName());
            roleMap.put(userRoleVo.getUserId(), roleNames);
        }
        for (TaskDetailVo taskResponse : taskResponses) {
            taskResponse.setRoleNames(roleMap.get(taskResponse.getUserId()));
        }
        return BeanUtils.copyListProperties(taskResponses, TaskResponseDTO.class);
    }
    @Override
    public List<TaskResponseDTO> listAllOperatedCommentsByBusinessId(String businessId) {
        return null;
    }
    @Override
    public Integer getTodoByUserId(String userId) {
        return null;
    }
    @Override
    public Boolean flowIsHasUserId(String userId) {
        return null;
    }
    @Override
    public Boolean isPastTask(TaskResponseDTO taskResponseDTO) {
        return null;
    }

    @Override
    public Boolean updateMsgRecord(String businessKey,Integer status, String exMsg) {
        return flowMsgRecordService.lambdaUpdate()
                .eq(FlowMsgRecord::getBusinessKey, businessKey)
                .set(FlowMsgRecord::getStatus, status)
                .set(FlowMsgRecord::getExMsg, exMsg).update();
    }

    @Override
    public Boolean saveMsgRecord(WorkFlowResult workFlowResult, MsgRecordStatus status) {
        return flowMsgRecordService.saveMsgRecord(workFlowResult,status);
    }

}
