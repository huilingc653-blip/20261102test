package com.bytter.workflow.web.controller;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.jqgrid.TableGridResponse;
import com.bytter.workflow.domain.handler.ProcessDiagramGenerator;
import com.bytter.workflow.domain.service.ProcessInstanceService;
import com.bytter.workflow.domain.vo.ProcessInstanceVo;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.impl.RepositoryServiceImpl;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhoul on 2017/7/13.
 * 流程实例
 * 正在运行的流程，也叫做流程实例
 */
@Controller
@RequestMapping("/bpm/processInstance")
public class ProcessInstanceController extends BaseController {
    private final RepositoryService repositoryService;
    private final HistoryService historyService;
    private final ProcessInstanceService processInstanceService;
    private final TaskService taskService;
    public ProcessInstanceController(RepositoryService repositoryService,TaskService taskService,
                                     HistoryService historyService,ProcessInstanceService processInstanceService){
        this.repositoryService = repositoryService;
        this.historyService = historyService;
        this.processInstanceService = processInstanceService;
        this.taskService = taskService;
    }
    //列表
    @GetMapping("/list")
    public void list() {
    }
    /**
     * 读取带跟踪的图片
     */
    @RequestMapping(value = "/trace/{processInstanceId}")
    public void readResource(@PathVariable("processInstanceId") String processInstanceId, HttpServletResponse response) throws Exception {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        //获取流程定义ID
        String processDefinitionId = historicProcessInstance.getProcessDefinitionId();
        ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(processDefinitionId);
        //审批中的任务高亮显示
        List<String> activityHiLight = taskService.createTaskQuery()
                .processInstanceId(processInstanceId).list().stream()
                .map(Task::getTaskDefinitionKey).collect(Collectors.toList());
        InputStream processDiagram = ProcessDiagramGenerator.generateDiagram(definition, "png", activityHiLight);
        response.setContentType("image/png");
        // 输出资源内容到相应对象
        byte[] b = new byte[1024];
        if (processDiagram == null) {
            response.getOutputStream().write(b, 0, 0);
            return;
        }
        int len;
        while ((len = processDiagram.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }
    @PostMapping("/tableGrid")
    @ResponseBody
    public TableGridResponse<ProcessInstanceVo> dataTable(@RequestBody TableGridInput tableGridInput) {
        return processInstanceService.listProcessVo(tableGridInput);
    }
}
