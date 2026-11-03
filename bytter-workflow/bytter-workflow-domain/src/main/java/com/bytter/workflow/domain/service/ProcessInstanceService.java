package com.bytter.workflow.domain.service;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.jqgrid.TableGridResponse;
import com.bytter.core.common.page.IParameter;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.workflow.domain.entity.FlowCategory;
import com.bytter.workflow.domain.vo.ProcessInstanceVo;
import com.bytter.workflow.domain.vo.TaskVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.*;
import java.util.stream.Collectors;
/**
 * 运行流程实例service
 */
@Service
public class ProcessInstanceService {
    private final RuntimeService runtimeService;
    private final FlowCategoryService flowCategoryService;
    private final TaskService taskService;
    private final HistoryService historyService;
    @DubboReference
    private ICorpService corpService;
    @DubboReference
    private IUserService userService;
    public ProcessInstanceService(RuntimeService runtimeService, FlowCategoryService flowCategoryService,
                                  TaskService taskService,HistoryService historyService) {
        this.historyService = historyService;
        this.runtimeService = runtimeService;
        this.flowCategoryService = flowCategoryService;
        this.taskService = taskService;
    }
    public TableGridResponse<ProcessInstanceVo> listProcessVo(TableGridInput tableGridInput) {
        IParameter parameter = tableGridInput.getParameter();
        String categoryId = parameter.getPara("categoryId");
        Long total = 0L;
        List<ProcessInstanceVo> list = new ArrayList<>();
        if (StringUtils.isBlank(categoryId)) {
            return new TableGridResponse<>(tableGridInput, total, list);
        }
        FlowCategory flowCategory = flowCategoryService.getById(categoryId);
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        processInstanceQuery.processDefinitionKey(flowCategory.getCateKey());
        processInstanceQuery.tenantIdIn(flowCategory.getTenantId());
        total = processInstanceQuery.count();
        if (total > 0) {
            processInstanceQuery.orderByProcessInstanceId();
            if (tableGridInput.getSort().equals("desc")) {
                processInstanceQuery.desc();
            } else {
                processInstanceQuery.asc();
            }
            List<ProcessInstance> processInstanceList = processInstanceQuery.listPage(tableGridInput.getStart(), tableGridInput.getRows());
            if (CollectionUtils.isEmpty(processInstanceList)) {
                return new TableGridResponse<>(tableGridInput, total, list);
            }
            list = buildVo(processInstanceList, flowCategory);
        }
        return new TableGridResponse<>(tableGridInput, total, list);
    }
    private List<ProcessInstanceVo> buildVo(List<ProcessInstance> processInstances,FlowCategory flowCategory) {
        //流程实例ID查询历史表获取startUid 运行中流程表数据较少查询快 运行流程表未保存启动用户id
        Set<String> instanceIds = new HashSet<>();
        List<String> corpIds = new ArrayList<>();
        for (ProcessInstance processInstance : processInstances) {
            instanceIds.add(processInstance.getId());
            corpIds.add(processInstance.getTenantId());
        }
        List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery().
                processInstanceIds(instanceIds).list();
        Map<String, String> startUidMap = historicProcessInstances.stream().collect(Collectors.toMap(HistoricProcessInstance::getId, HistoricProcessInstance::getStartUserId));
        //查询用户信息与单位信息
        Map<String, CorpDTO> corpDTOMap = corpService.listCorpDtoByCorpIds(corpIds);
        Map<String, String> userNameMap = userService.getUserNameByIds(new ArrayList<>(startUidMap.values()));
        TaskQuery taskQuery = taskService.createTaskQuery();
        return processInstances.stream().map(processInstance -> {
            ProcessInstanceVo processInstanceVo = new ProcessInstanceVo();
            BeanUtils.copyProperties(processInstance, processInstanceVo);
            List<Task> taskList = taskQuery.processInstanceId(processInstanceVo.getProcessInstanceId()).list();
            processInstanceVo.setTaskVoList(taskList.stream().map(TaskVo::from).collect(Collectors.toList()));
            if (taskList.size() > 0) {
                processInstanceVo.setStartTime(taskList.get(0).getCreateTime());
            }
            processInstanceVo.setFlowCategoryId(flowCategory.getId());
            processInstanceVo.setProcessDefinitionName(flowCategory.getName());
            CorpDTO corpDTO = corpDTOMap.get(processInstance.getTenantId());
            if (Objects.nonNull(corpDTO)) {
                processInstanceVo.setCorpCode(corpDTO.getCode());
                processInstanceVo.setCorpName(corpDTO.getName());
            }
            processInstanceVo.setStartUserId(startUidMap.get(processInstance.getId()));
            if (StringUtils.isNotBlank(processInstanceVo.getStartUserId())) {
                processInstanceVo.setUsername(userNameMap.get(processInstanceVo.getStartUserId()));
            }
            return processInstanceVo;
        }).collect(Collectors.toList());
    }

    public List<ProcessInstance> listProcessesByTaskIds(List<String> taskIds) {
        List<Task> taskList = taskService.createTaskQuery().taskIdIn(taskIds.stream().toArray(String[]::new)).list();
        // 任务已被其他窗口处理完时，运行时任务为空；避免 processInstanceIds(空集合) 抛出 Set of process instance ids is empty
        if (CollectionUtils.isEmpty(taskList)) {
            return Collections.emptyList();
        }
        return runtimeService.createProcessInstanceQuery().processInstanceIds(taskList.stream().map(Task::getProcessInstanceId)
                .collect(Collectors.toSet())).list();
    }
}
