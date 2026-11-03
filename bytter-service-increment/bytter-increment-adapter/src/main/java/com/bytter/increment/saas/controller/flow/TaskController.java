package com.bytter.increment.saas.controller.flow;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.WorkFlowVerifyException;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.jqgrid.TableGridResponse;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.common.page.PageParam;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.workflow.domain.service.FlowTaskService;
import com.bytter.workflow.domain.vo.ProcessInstanceVo;
import com.bytter.workflow.domain.vo.RevokeResultVo;
import com.bytter.workflow.dto.WorkFlowInput;
import com.bytter.workflow.enums.WorkFlowMessageEnum;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by zhoul on 2017/7/13.
 * 任务列表 工作流合并任务入口放在自己服务
 */
@Api(value = "/increment/v1-workflow/bpm/task",tags = "任务列表")
@RestController("v1TaskController")
@RequestMapping("/increment/v1-workflow/bpm/task")
@Slf4j
public class TaskController extends BaseController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private FlowTaskService flowTaskService;
    @DubboReference(check = false)
    private ICorpService corpService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/todoListDataTable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<ProcessInstanceVo>> todoListDataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(flowTaskService.pageInstancesByUserId(getUserId(), PageParam.of(input.getStart(), input.getPageSize()))
                .convert(DataTablesOutput.class));
    }
    @PostMapping(value = "/todoListGrid")
    @ResponseBody
    public AjaxJson<TableGridResponse<ProcessInstanceVo>> todoListGrid(@RequestBody TableGridInput tableGridInput) {
        return AjaxJson.success(flowTaskService.pageInstancesByUserId(getUserId(), PageParam.of(tableGridInput.getStart(), tableGridInput.getPageSize()))
                .convert(TableGridResponse.class));
    }


    @GetMapping("/claim/{taskId}")
    @ResponseBody
    public AjaxJson claim(@PathVariable("taskId") String taskId) {
        taskService.claim(taskId, getUserId());
        return AjaxJson.success();
    }

    /**
     * 【工作流审】
     * @param input
     * @return
     */
    @PostMapping("/complete")
    @ResponseBody
    public AjaxJson complete(@RequestBody WorkFlowInput input) {
        try {
            workFlowService.complete(input,getCurrentUser());
            //特殊处理流程校验异常 改异常涉及不同弹窗
        } catch (WorkFlowVerifyException workFlowVerifyException) {
            AjaxJson ajaxJson = new AjaxJson();
            ajaxJson.setMessageEnum(workFlowVerifyException.getIMessageEnum());
            Map<String, Object> extendsParams = new HashMap<>();
            extendsParams.put("verifyStatus", workFlowVerifyException.getVerifyStatus());
            ajaxJson.setAttributes(extendsParams);
            ajaxJson.setParams(workFlowVerifyException.getParams());
            return ajaxJson;
        }
        return success();
    }

    @PostMapping("/batchRevoke")
    @ResponseBody
    public AjaxJson batchRevoke(@RequestParam("ids") String businessIds) {
        String[] ids = businessIds.split(",");
        Integer total = ids.length;
        Map<String, Map<String, WorkFlowMessageEnum>> resultMap = workFlowService.batchRevoke(ids, getUserId());
        return AjaxJson.success(buildRevokeResult(ids, total, resultMap));
    }

    private Object buildRevokeResult(String[] ids, Integer total, Map<String, Map<String, WorkFlowMessageEnum>> resultMap) {
        if (CollectionUtils.isEmpty(resultMap)) {
            AjaxJson success = success(WorkFlowMessageEnum.WORK_FLOW_REVOKE_SUCCESS_NUM, String.valueOf(ids.length));
            //避免前端报错，返回一个空的数组回去
            success.setObj(new ArrayList<RevokeResultVo>());
            return success;
        } else {
            //总条数
            Integer successNum = total;
            //记录了哪个网点撤回出错，以及出错的数据详情
            List<RevokeResultVo> revokeResultVoList = new ArrayList<>();
            Set<String> corpIds = resultMap.keySet();
            Map<String, CorpDTO> corpDTOMap = corpService.listCorpDtoByCorpIds(new ArrayList<>(corpIds));
            for (Map.Entry<String, Map<String, WorkFlowMessageEnum>> entry : resultMap.entrySet()) {
                RevokeResultVo revokeResultVo = new RevokeResultVo();
                String corpId = entry.getKey();
                revokeResultVo.setCorpId(corpId);
                revokeResultVo.setCorpName(corpDTOMap.get(corpId).getName());
                revokeResultVo.setErrorNum(0);
                revokeResultVo.setBusinessIdList(new ArrayList<>());
                revokeResultVo.setMessageList(new ArrayList<>());
                Map<String, WorkFlowMessageEnum> errorMap = entry.getValue();
                for (Map.Entry<String, WorkFlowMessageEnum> tmpEntry : errorMap.entrySet()) {
                    revokeResultVo.getBusinessIdList().add(tmpEntry.getKey());
                    revokeResultVo.getMessageList().add(tmpEntry.getValue());
                    revokeResultVo.setErrorNum(revokeResultVo.getErrorNum() + 1);
                }
                revokeResultVoList.add(revokeResultVo);
                successNum = successNum - revokeResultVo.getErrorNum();
            }
            AjaxJson success;
            if (successNum.equals(0)) {
                success = success(WorkFlowMessageEnum.WORK_FLOW_REVOKE_ALL_FAIL, String.valueOf(successNum));
                success.setSuccess(false);
            } else {
                success = success(WorkFlowMessageEnum.WORK_FLOW_REVOKE_SUCCESS_NUM, String.valueOf(successNum));
                success.setSuccess(false);
            }
            success.setObj(revokeResultVoList);
            return success;
        }
    }
    @GetMapping("/view/{taskId}")
    public AjaxJson< Map<String,String>> view(@PathVariable("taskId") String taskId) {
        Map<String,String> model=new HashMap<>();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        String businessKey = processInstance.getBusinessKey();
        model.put("processInstanceId", task.getProcessInstanceId());
        model.put("businessKey", businessKey);
        model.put("taskId", task.getId());
        return AjaxJson.success(model);
    }


    @GetMapping("/detail")
    public AjaxJson< Map<String,String>> detail(String taskId) {
        Map<String,String> model=new HashMap<>();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        model.put("processInstanceId", task.getProcessInstanceId());
        return AjaxJson.success(model);
    }
    /**
     * 删除流程节点
     *
     * @param ids
     */
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson delete(@RequestParam("ids[]") List<String> ids) {
        runtimeService.deleteProcessInstancesAsync(ids, "System delete");
        return AjaxJson.success();
    }
}
