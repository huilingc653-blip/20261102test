package com.bytter.workflow.domain.service;
import com.bytter.workflow.dto.WorkFlowResult;
import com.bytter.core.common.page.PageParam;
import com.bytter.core.common.page.PageResult;
import com.bytter.core.utils.BeanUtils;
import com.bytter.system.api.IUserService;
import com.bytter.workflow.constant.WorkFlowConstant;
import com.bytter.workflow.domain.mapper.FlowTaskMapper;
import com.bytter.workflow.domain.vo.ProcessInstanceVo;
import com.bytter.workflow.domain.vo.TaskDetailVo;
import com.bytter.workflow.domain.vo.TaskVo;
import com.bytter.workflow.domain.vo.TodoNumberVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.camunda.bpm.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Comment;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class FlowTaskService {
	
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
    private final TaskService taskService;
    private final RuntimeService runtimeService;
    private final FlowTaskMapper flowTaskMapper;
    private final HistoryService historyService;
    @DubboReference
    private IUserService userService;
    public FlowTaskService(TaskService taskService, RuntimeService runtimeService,
                           FlowTaskMapper flowTaskMapper,HistoryService historyService) {
        this.flowTaskMapper = flowTaskMapper;
        this.taskService = taskService;
        this.runtimeService = runtimeService;
        this.historyService = historyService;
    }
    public PageResult<ProcessInstanceVo> pageInstancesByUserId(String userId, PageParam pageParam) {
        TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateUser(userId).or().taskAssignee(userId);
        List<ProcessInstanceVo> list = new ArrayList<>();
        long total = taskQuery.count();
        if (total == 0) {
            return PageResult.emptyPage(pageParam.getPageNo());
        }
        List<Task> taskList = taskQuery.orderByTaskId().desc()
                .listPage(pageParam.getPageNo(), pageParam.getPageSize());
        // 根据流程的业务ID查询实体并关联
        for (Task task : taskList) {
            String processInstanceId = task.getProcessInstanceId();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
            if (processInstance == null) {
                continue;
            }
            String businessKey = processInstance.getBusinessKey();
            if (businessKey == null) {
                continue;
            }
            ProcessInstanceVo processInstanceTemp = BeanUtils.copyNewInstanceProperties(processInstance, ProcessInstanceVo.class);
            processInstanceTemp.setTaskVO(BeanUtils.copyNewInstanceProperties(task, TaskVo.class));
            list.add(processInstanceTemp);
        }
        return PageResult.of(pageParam.getPageNo(), (int)total, list);
    }
    /**
     * 待审批数据
     * @param userId 用户ID
     * @return 数量
     */
    public Long todoNumber(String userId) {
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery.active().taskAssignee(userId);
        return taskQuery.count();
    }
    /**
     *  已完成数据
     * @param userId 用户ID
     * @return 数量
     */
    public Long finishNumber(String userId) {
        return historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(userId).finished().count();
    }
    /**
     * 用户待审批数据
     * @param userId
     * @return
     */
    public List<TodoNumberVo> getUserTodoData(String userId) {
        return flowTaskMapper.countTodoNumberList(userId, null);
    }
    /**
     * 用户已审批数据
     *
     * @param userId
     * @return
     */
    public List<TodoNumberVo> countFinishGpCateKey(String userId, String cycleTime) {
        return flowTaskMapper.countFinishGpCateKey(userId, resolveCycleStartTime(cycleTime, DEFAULT_FINISH_DAYS), new Date());
    }

    /**
     * 已审批默认回溯天数，与前端默认选中的「本周」保持一致
     */
    private static final int DEFAULT_FINISH_DAYS = 7;

    /**
     * 计算「已审批」时间窗口的起始时间，分类角标与明细列表共用，保证两端口径一致
     *
     * @param cycleTime   周期，取值见 {@link WorkFlowConstant}
     * @param defaultDays 未传 cycleTime 时默认回溯的天数，小于等于 0 表示不限制时间范围
     * @return 起始时间，为 null 表示不限制时间范围
     */
    public static Date resolveCycleStartTime(String cycleTime, int defaultDays) {
        Integer days = null;
        if (WorkFlowConstant.MONTH.equals(cycleTime)) {
            days = 30;
        } else if (WorkFlowConstant.HALF_YEAR.equals(cycleTime)) {
            days = 178;
        } else if (WorkFlowConstant.YEAR.equals(cycleTime)) {
            days = 365;
        } else if (WorkFlowConstant.THREE_YEAR.equals(cycleTime)) {
            days = 365 * 3;
        } else if (WorkFlowConstant.WEEK.equals(cycleTime)) {
            days = DEFAULT_FINISH_DAYS;
        } else if (defaultDays > 0) {
            days = defaultDays;
        }
        if (days == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -days);
        return calendar.getTime();
    }
    
    /**
     * 用户已审批数据
     *
     * @param userId
     * @return
     */
    public List<TodoNumberVo> countFinishGpCateKeyNew(String userId) {
        int days = (int)calculateDaysFrom(2024,1,1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -days);
        return flowTaskMapper.countFinishGpCateKey(userId, calendar.getTime(), new Date());
    }
    
    public static long calculateDaysFrom(int startYear, int startMonth, int startDay) {
        LocalDate startDate = LocalDate.of(startYear, startMonth, startDay);
        LocalDate currentDate = LocalDate.now();
        return ChronoUnit.DAYS.between(startDate, currentDate) + 1;
    }
    /**
     * 流程ID获取流程查看明细
     *
     * @param processInstanceId 流程ID
     * @return
     */
    public List<TaskDetailVo> listTaskResponse(String processInstanceId) {
        //返回给视图层的数据
        List<TaskDetailVo> taskResponseList = new ArrayList<>();
        //判断变量taskPassFlag是否为撤回，撤回的话，要单独处理返回的数据
        List<HistoricVariableInstance> historicVariableInstanceList = historyService.createHistoricVariableInstanceQuery()
                .variableName(WorkFlowConstant.TASK_PASS_FLAG).processInstanceId(processInstanceId).list();
        if(historicVariableInstanceList.size()>0) {
	        log.info("==============================11111===========================historicVariableInstanceList.size()"+historicVariableInstanceList.size()
	        +"historicVariableInstanceList.get(0).getVariableName()"+historicVariableInstanceList.get(0).getVariableName()
	        +" historicVariableInstanceList.get(0).getValue()"+historicVariableInstanceList.get(0).getValue());
	        System.out.println("==============================11111===========================historicVariableInstanceList.size()"+historicVariableInstanceList.size()
	        +"historicVariableInstanceList.get(0).getVariableName()"+historicVariableInstanceList.get(0).getVariableName()
	        +" historicVariableInstanceList.get(0).getValue()"+historicVariableInstanceList.get(0).getValue());
        }else {
        	log.info("==============================22222");
        	System.out.println("==============================22222");
        }
        //只有一个taskPassFlag变量，且是撤回的话就要进行单独处理返回值
        if (historicVariableInstanceList.size() == 1 && historicVariableInstanceList.get(0).getValue().equals(WorkFlowResult.ApprovalStatus.RK.getCode())) {
            //撤回时，节点事件是startEvent而不是userTask
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).finished().singleResult();
            if (Objects.isNull(historicProcessInstance)) {
                log.warn("撤回流程查询历史实例为空, processInstanceId={}", processInstanceId);
                return taskResponseList;
            }
            log.info("historicProcessInstance========================================================="+historicProcessInstance.getStartUserId());
            System.out.println("historicProcessInstance========================================================="+historicProcessInstance.getStartUserId());
            //撤回流程实例，没有task，因此相关的时间参数以ProcessInstance为准
            TaskDetailVo detailVo = TaskDetailVo.from(historicProcessInstance);
            detailVo.setTaskPassFlag(WorkFlowResult.ApprovalStatus.RK.getCode());
            log.info("detailVo========================================================="+detailVo.getTaskId()+" |detailVo.getUserId()"+detailVo.getUserId()+ " |detailVo.getUserName()"+detailVo.getUserName());
            System.out.println("detailVo========================================================="+detailVo.getTaskId()+" |detailVo.getUserId()"+detailVo.getUserId()+ " |detailVo.getUserName()"+detailVo.getUserName());
            taskResponseList.add(detailVo);
        } else {
            List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
            log.info("processInstanceId========================================================="+processInstanceId);
            log.info("taskList.size========================================================="+taskList.size());
            System.out.println("processInstanceId========================================================="+processInstanceId);
            System.out.println("taskList.size========================================================="+taskList.size());
            for(Task task:taskList) {
            	log.info("task========================================task.getAssignee("+task.getAssignee()+" |task.getId()="+task.getId()+" |task.getName()=="+task.getName());
            	System.out.println("task========================================task.getAssignee("+task.getAssignee()+" |task.getId()="+task.getId()+" |task.getName()=="+task.getName());
            }
            List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().finished().processInstanceId(processInstanceId).list();
            List<Comment> comments = taskService.getProcessInstanceComments(processInstanceId);
            for (Task task : taskList) {
                taskResponseList.add(TaskDetailVo.from(task));
            }
            if(!CollectionUtils.isEmpty(historicTaskInstanceList)) {
	            for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceList) {
	                TaskDetailVo taskResponse = TaskDetailVo.from(historicTaskInstance);
	                for (Comment comment : comments) {
	                    if (comment.getTaskId().equals(taskResponse.getTaskId())) {
	                        taskResponse.setComment(comment.getFullMessage());
	                    }
	                }
	                if(!CollectionUtils.isEmpty(historicVariableInstanceList)) {
		                for (HistoricVariableInstance historicVariableInstance : historicVariableInstanceList) {
		                    if (historicVariableInstance instanceof HistoricVariableInstanceEntity) {
		                        HistoricVariableInstanceEntity historicVariableInstanceEntity = (HistoricVariableInstanceEntity) historicVariableInstance;
		                        HistoricTaskInstanceEntity historicTaskInstanceEntity = (HistoricTaskInstanceEntity) historicTaskInstance;
		                        if (!StringUtils.isEmpty(historicVariableInstanceEntity.getTaskId())
		                                && historicVariableInstanceEntity.getTaskId().equals(historicTaskInstanceEntity.getId())) {
		                            taskResponse.setTaskPassFlag(historicVariableInstanceEntity.getTextValue());
		                        }
		                    }
		                }
	                }
	                taskResponseList.add(taskResponse);
	            }
            }
            log.info("task========================================3333");
            System.out.println("task========================================3333");
            taskResponseList.sort((o1, o2) -> (int) (o1.getStartTime().getTime() - o2.getStartTime().getTime()));
            for(TaskDetailVo vo:taskResponseList) {
            	log.info("task========================================vo.getUserId()"+vo.getUserId()+" |vo.getUserName()"+vo.getUserName()+" |vo.getTaskId()=="+vo.getTaskId());
            	System.out.println("task========================================vo.getUserId()"+vo.getUserId()+" |vo.getUserName()"+vo.getUserName()+" |vo.getTaskId()=="+vo.getTaskId());
            }
            log.info("task========================================4444");
            System.out.println("task========================================4444");
        }
        buildUserName(taskResponseList);
        return taskResponseList;
    }
    private void buildUserName(List<TaskDetailVo> taskResponseList) {
        Set<String> userIds = taskResponseList.stream().map(TaskDetailVo::getUserId).collect(Collectors.toSet());
        Map<String, String> userNameMap = userService.getUserNameByIds(new ArrayList<>(userIds));
        for (TaskDetailVo taskDetailVo : taskResponseList) {
            taskDetailVo.setUserName(userNameMap.getOrDefault(taskDetailVo.getUserId(), ""));
        }
    }
    /**
     * 通过业务ID查询流程实例
     * @param businessId
     * @return
     */
    public String getProcessInstanceId(String businessId) {
        String processInstanceId = null;
        List<HistoricProcessInstance> historicProcessInstanceList = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(businessId)
                .orderByProcessInstanceStartTime()
                .desc()
                .listPage(0, 1);
        if (!CollectionUtils.isEmpty(historicProcessInstanceList)) {
            HistoricProcessInstance historicProcessInstance = historicProcessInstanceList.get(0);
            processInstanceId = historicProcessInstance.getId();
        }
        return processInstanceId;
    }
    public Long trackTotal(String currentUid) {
        return flowTaskMapper.countTrackTotal(currentUid,null);
    }
    public List<TodoNumberVo> countTrackNumberGpCateKey(String currentUid) {
        return flowTaskMapper.countTrackNumberGpCateKey(currentUid, null);
    }
}
