package com.bytter.workflow.mobile;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.persistence.BasePersistence;
import com.bytter.workflow.dto.WorkFlowInput;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.common.rest.RestInput;
import com.bytter.core.common.rest.RestResponse;
import com.bytter.core.enums.CommonMessageEnum;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.workflow.constant.WorkFlowConstant;
import com.bytter.workflow.domain.entity.FlowCategory;
import com.bytter.workflow.domain.service.FlowCategoryService;
import com.bytter.workflow.domain.service.FlowTaskService;
import com.bytter.workflow.domain.service.MobileAppTaskService;
import com.bytter.workflow.domain.service.MyTaskService;
import com.bytter.workflow.domain.vo.AppTaskVo;
import com.bytter.workflow.domain.vo.ApproveVO;
import com.bytter.workflow.domain.vo.UserTaskVO;
import com.bytter.workflow.enums.WorkFlowMessageEnum;
import com.bytter.core.common.WorkFlowVerifyException;

import cn.hutool.core.util.ObjectUtil;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Comment;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.stream.Collectors;
/**
 * 提供给手机端处理的API控制器
 */
@Controller
@RequestMapping("/api/flow")
public class MobileApiController extends BaseController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());


    @Resource
    private FlowCategoryService flowCategoryService;

    @Resource
    private TaskService taskService;
    @Resource
    private FlowTaskService flowTaskService;

    @Resource
    private MobileAppTaskService mobileAppTaskService;

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private HistoryService historyService;

    @Resource
    private RuntimeService runtimeService;

    @Autowired
    private IWorkFlowService workFlowService;
    
    @Resource
    private MyTaskService myTaskService;
    
    @Value("${bytter.app.url}")
    private String appUrl;

    /**
     * 流程分类
     */
    @GetMapping("/category")
    @ResponseBody
    public Object category() {
        return flowCategoryService.makeSecondLevelCategory(getUserId());
    }


    /**
     * 流程审批-汇总列表接口
     * <p>
     * 审批-按业务类型展示待审批数量接口开发
     *
     * @param restInput
     * @return
     */
    @PostMapping("/approveFlowCategoryList")
    @ResponseBody
    public Object approveFlowCategoryList(@RequestBody RestInput restInput) {
        Integer type = (Integer) restInput.get("type");
        List<Map<String, Object>> result = null;
        switch (type) {
            case 0:
                // 待审批
                result = mobileAppTaskService.getTodoFlowCategoryList(restInput);
                break;
            case 1:
                // 已审批
                result = mobileAppTaskService.getHistoryFlowCategoryList(restInput);
                break;
            default:
                return null;
        }
        return result;
    }
    
    
    /**
     * 列表，待办、已办
     */
    @PostMapping("/list")
    @ResponseBody
    public Object list(@RequestBody RestInput restInput) {
        Integer type = (Integer) restInput.get("type");
        IPageResult pageResult;
        switch (type) {
            case 0:
                pageResult = myTaskService.todo(restInput);
                break;
            case 1:
                pageResult = myTaskService.historic(restInput);
                break;
            default:
                return null;
        }
        if (pageResult != null) {
            return pageResult.convert(RestResponse.class);
        }
        return null;
    }





    /**
     * 【app审批】
     */
    @PostMapping("/approve")
    @ResponseBody
    public Object approve(@RequestBody BasePersistence basePersistence) {
        try {
            WorkFlowInput workFlowInput = new WorkFlowInput();
            workFlowInput.setComment(basePersistence.get("comment"));
            workFlowInput.setTaskList(basePersistence.get("ids"));
            Integer action = (Integer) basePersistence.get("action");

            //手写审批  drawComment base64图片字符串
            workFlowInput.setDrawComment(basePersistence.get("drawComment"));
            workFlowInput.setIsNeedVerify(basePersistence.get("isNeedVerify"));


            switch (action) {
                case 1:
                    workFlowInput.setWorkflowPass(WorkFlowConstant.PASS);
                    break;
                case 2:
                	workFlowInput.setWorkflowPass(WorkFlowConstant.REJECT);
                    break;
                case 3:
                    workFlowInput.setWorkflowPass(WorkFlowConstant.REJECT_START);
                    break;
            }
            workFlowService.complete(workFlowInput,UserContextHelper.helper().getCurrentUser());
        } catch (WorkFlowVerifyException workFlowVerifyException) {
            AjaxJson ajaxJson = new AjaxJson();
            ajaxJson.setMessageEnum(workFlowVerifyException.getIMessageEnum());
            Map<String, Object> extendsParams = new HashMap<>();
            extendsParams.put("verifyStatus", workFlowVerifyException.getVerifyStatus());
            ajaxJson.setAttributes(extendsParams);
            ajaxJson.setSuccess(Boolean.FALSE); //app
            return ajaxJson;
        } catch (BusinessException businessException) {
            AjaxJson ajaxJson = new AjaxJson();
            ajaxJson.setMessageEnum(businessException.getIMessageEnum());
            return ajaxJson;
        } catch (Exception e) {
            logger.error("工作审批提交方法发生错误：{}", e, e);
            if (e instanceof ParameterException) {
                ParameterException paramException = (ParameterException) e;
                AjaxJson ajaxJson = new AjaxJson();
                ajaxJson.setMessageEnum(paramException.getIMessageEnum());
                return ajaxJson;
            }
            AjaxJson ajaxJson = new AjaxJson();
            ajaxJson.setMessageEnum(CommonMessageEnum.COMMON_MESSAGE_UNKNOWN_ERROR);
            ajaxJson.setMsg(e.getMessage());
            return ajaxJson;
        }
        return success();
    }


    @GetMapping("/detail")
    @ResponseBody
    public Object detail(String id, String businessId, String categoryId) {
        Task task = taskService.createTaskQuery().taskId(id).singleResult();
        if (task == null) {
            // 流程已经被处理
            throw new BusinessException(WorkFlowMessageEnum.WORK_FLOW_TASK_WAS_DEAL);
        }
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        ApproveVO approveVO = new ApproveVO();
        approveVO.setId(task.getId());
        approveVO.setCorpId(processInstance.getTenantId());
//        approveVO.setUserId(processInstance.getStartUserId());
//        approveVO.setCreateTime(processInstance.getStartTime().getTime());
        approveVO.setCategoryId(processInstance.getBusinessKey());
        approveVO.setCategoryName(flowCategoryService.getFullCategoryNameByKey(processInstance.getBusinessKey(), " > "));
        FlowCategory flowCategory = flowCategoryService.findByKey(processInstance.getBusinessKey());
        approveVO.setStatus(0);
        //过滤非移动端审批数据,设置状态为99进行占位，客户端不显示审批相关按钮
        if (flowCategory != null && flowCategory.getMobileApprovalFlag() == 0) {
            approveVO.setStatus(99);
        }
        approveVO.setBusinessId(processInstance.getBusinessKey());
//        iInjection.inject(approveVO);
        return approveVO;
    }

    
    /**
     * 跳转查看附件页面
     * @return
     */
    @GetMapping("/lookAnnex")
    public String lookAnnex(String url,Model model) {
        model.addAttribute("url",url);
        return "sys/resources/lookAnnex";
    }

    /**
     * 审批详情
     *
     * @param id         当type=0时，为待审批列表的ID，type=1时实例ID,type=99兼容PC审批通知数据
     * @param businessId 业务ID
     * @param categoryId 分类ID
     * @param type       0待审批，1已审批
     * @return
     */
    @GetMapping("/detailPage")
    public String detailPage(String id, String businessId, String categoryId, Integer type, Model model,HttpServletRequest request) {
        //当传入type等于99时，表示非移动端审批给予提示
        if (type == 99) {
            model.addAttribute("errorMessage", "该流程只能在PC端进行审批，请前往PC端！");
            return "error/mobileError";
        }
        Task task =  taskService.createTaskQuery().taskId(id).singleResult();
        if (type == 0) {
            if (task == null) {
                model.addAttribute("errorMessage", "该流程已完成审批，请重新加载数据！");
                return "error/mobileError";
            }
            id = task.getProcessInstanceId();
        }else {
        	if(!ObjectUtils.isEmpty(task)) {
        		id = task.getProcessInstanceId();
        	}
        }
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(id).singleResult();
        String processDefinitionId = historicProcessInstance.getProcessDefinitionId();
        String businessKey = historicProcessInstance.getBusinessKey();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        Map<String, String[]> queryParams = request.getParameterMap();
        String key = processDefinition.getKey();
        FlowCategory flowCategory = flowCategoryService.findByKey(key);
        String mobileUrl = flowCategory.getMobileUrl();
        if (StringUtils.isBlank(mobileUrl)) {
            // throw new BusinessException("尚未配置手机审批详情地址！请联系管理员");
            model.addAttribute("errorMessage", "尚未配置手机审批详情地址！请联系管理员！");
            return "error/mobileError";
        }
//        StringBuilder sb = new StringBuilder();
        String newUriPath = mobileUrl + "?" + queryParams.entrySet().stream()
                .filter(entry -> ObjectUtil.notEqual(entry.getKey(), "forwardURL"))
                .map(entry -> entry.getKey() + "=" + entry.getValue()[0])
                .collect(Collectors.joining("&"));
        String method = "&customMethod=GET";
        if (ObjectUtils.isNotEmpty(task)){
            method = method +"&taskId="+task.getId();
        }
        return "redirect:" + appUrl + newUriPath + method;
//        sb.append("redirect:"+appUrl)
//                .append(mobileUrl)
//                .append("?businessId=").append(businessKey);
//
//        if (ObjectUtils.isNotEmpty(task) && StringUtils.isNotBlank(task.getId())) {
//            sb.append("&taskId=").append(task.getId());
//        }
//        sb.append("&customMethod=GET");
//        if(ObjectUtils.isNotEmpty(queryParams.get("access_token"))) {
//        	sb.append("&Authorization="+queryParams.get("access_token"));
//        }
        
//        return sb.toString();
    }

    /**
     * 审批详情
     *
     * @param attachmentId         当type=0时，为待审批列表的ID，type=1时实例ID,type=99兼容PC审批通知数据
     * @param type       0待审批，1已审批
     * @return
     */
    @GetMapping("/detailAttachmentPage")
    public String detailAttachmentPage(String attachmentId, Integer type, Model model) {
        String mobileUrl="/attachment/sysattachment/mobileApprove";
        StringBuilder sb = new StringBuilder();
        sb.append("forward:")
                .append(mobileUrl);
        return sb.toString();
    }

    private Date getFirstTaskStartTime(List<Task> taskList) {
        if (!CollectionUtils.isEmpty(taskList)) {
            return taskList.get(0).getCreateTime();
        }
        return null;
    }

    /**
     * 【流程跟踪】
     */
    @GetMapping("/trace")
    @ResponseBody
    public Object trace(String businessId) {
        List<Object> objectList = new ArrayList<>();
        String processInstanceId = flowTaskService.getProcessInstanceId(businessId);
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
//        List<Task> taskList = taskService.createTaskQuery().processInstanceBusinessKey(businessId).list();

        if (taskList != null && !taskList.isEmpty()) {
            List<UserTaskVO> taskResponseList = new ArrayList<>();
            UserTaskVO userTaskVO = new UserTaskVO();
            Task taskFirst = taskList.get(0);
            userTaskVO.setTaskName(taskFirst.getName());
            userTaskVO.setTaskDefinitionKey(taskFirst.getTaskDefinitionKey());
            Date userTaskStartTime = getFirstTaskStartTime(taskList);
            if (userTaskStartTime != null) {
                userTaskVO.setStartTime(userTaskStartTime);
            }
            List<String> assigneeLists = taskList.stream().map(Task::getAssignee).collect(Collectors.toList());
            Map<String, String> loginNameMap = myTaskService.getLoginNameByIds(assigneeLists);
            Map<String, String> userNameMap = myTaskService.getUserNameByIds(assigneeLists);
            for (Task task : taskList) {
                AppTaskVo taskVO = new AppTaskVo();
                taskVO.setAssignee(task.getAssignee());
                taskVO.setAssigneeName(loginNameMap.get(task.getAssignee()));
                taskVO.setUsername(userNameMap.get(task.getAssignee()));
                taskVO.setCreateTime(task.getCreateTime());
                userTaskVO.addTask(taskVO);
            }
            taskResponseList.add(userTaskVO);
            objectList.add(taskResponseList);
        }


//        List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery().orderByProcessInstanceEndTime().desc().processInstanceBusinessKey(businessId).list();
//        for (HistoricProcessInstance historicProcessInstance : historicProcessInstanceList) {
//            String processInstanceId = historicProcessInstance.getId();
            List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().finished().processInstanceId(processInstanceId).list();
            if (historicTaskInstanceList.isEmpty()) {
//                continue;
                return objectList;
            }
            List<HistoricVariableInstance> historicVariableInstanceList = historyService.createHistoricVariableInstanceQuery().variableName(WorkFlowConstant.TASK_PASS_FLAG).processInstanceId(processInstanceId).list();
            List<Comment> taskComments = taskService.getProcessInstanceComments(processInstanceId);

            List<String> collect = taskComments.stream().map(Comment::getUserId).collect(Collectors.toList());
            Map<String, String> userNameMap = myTaskService.getUserNameByIds(collect);

            List<UserTaskVO> taskResponseList = new ArrayList<>();
            for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceList) {
                //如果为最后的节点，则不做处理
                if ("MI_END".equals(historicTaskInstance.getDeleteReason())) {
                    continue;
                }
                if (taskResponseList.isEmpty()) {
                    UserTaskVO userTaskVO = new UserTaskVO();
                    userTaskVO.setTaskDefinitionKey(historicTaskInstance.getTaskDefinitionKey());
                    userTaskVO.setTaskName(historicTaskInstance.getName());
                    if (historicTaskInstance.getStartTime() != null) {
                        userTaskVO.setStartTime(historicTaskInstance.getStartTime());
                    }
                    taskResponseList.add(userTaskVO);
                }
                UserTaskVO userTaskVO = taskResponseList.get(taskResponseList.size() - 1);
                AppTaskVo taskVO = new AppTaskVo();
                //设置审批状态
                for (HistoricVariableInstance historicVariableInstance : historicVariableInstanceList) {
                		HistoricVariableInstanceEntity instanceEntity = (HistoricVariableInstanceEntity)historicVariableInstance;
                        String taskId = instanceEntity.getTaskId();
                        if (taskId != null && taskId.equals(historicTaskInstance.getId())) {
                            String textValue = instanceEntity.getTextValue();
                            if (textValue != null) {
                                Integer flag = Integer.parseInt(textValue);
                                switch (flag) {
                                    case 3:
                                    case 4:
                                        flag--;
                                }
                                taskVO.setStatus(flag);
                            }
                      }
                }
                if (taskVO.getStatus() == null) {
                    continue;
                }
                taskVO.setAssignee(historicTaskInstance.getAssignee());
                taskVO.setCreateTime(historicTaskInstance.getDueDate());
                taskVO.setDueDate(historicTaskInstance.getEndTime());
                //设置审批意见
                for (Comment comment : taskComments) {
                    if (comment.getTaskId().equals(historicTaskInstance.getId())) {
                    	taskVO.setComment(comment.getFullMessage());
                        taskVO.setDrawComment(comment.getFullMessage());
                        taskVO.setUsername(userNameMap.get(comment.getUserId())); //设置审批通过的username
                        taskVO.setCreateTime(comment.getTime());
                        taskVO.setEntTime(comment.getTime());
                    }
                }
                if (userTaskVO.getTaskDefinitionKey().equals(historicTaskInstance.getTaskDefinitionKey())) {
//                    iInjection.inject(taskVO);
                    // 设置任务节点的开始时间
                    if (historicTaskInstance.getStartTime() != null) {
                        userTaskVO.setStartTime(historicTaskInstance.getStartTime());
                    }
                    userTaskVO.addTask(taskVO);
                } else {
                    UserTaskVO userTaskVO2 = new UserTaskVO();
                    userTaskVO2.setTaskDefinitionKey(historicTaskInstance.getTaskDefinitionKey());
                    userTaskVO2.setTaskName(historicTaskInstance.getName());
                    // 设置任务节点的开始时间
                    if (historicTaskInstance.getStartTime() != null) {
                        userTaskVO2.setStartTime(historicTaskInstance.getStartTime());
                    }
//                    iInjection.inject(taskVO);
                    userTaskVO2.addTask(taskVO);
                    taskResponseList.add(userTaskVO2);
                }
            }
            objectList.add(taskResponseList);
//        }
        return objectList;
    }


}
