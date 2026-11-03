package com.bytter.workflow.web.controller;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.jqgrid.TableGridResponse;
import com.bytter.workflow.domain.service.MyHistoryService;
import com.bytter.workflow.domain.vo.HistoricProcessInstanceVO;
import com.bytter.workflow.domain.vo.HistoricTaskInstanceVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static com.bytter.core.common.model.json.AjaxJson.success;
@Controller
@RequestMapping("/bpm/history")
public class HistoryController {
    private final MyHistoryService myHistoryService;
    public HistoryController(MyHistoryService myHistoryService) {
        this.myHistoryService = myHistoryService;
    }
    @GetMapping("/historicProcessInstanceList")
    public void historicProcessInstanceList() {
    }
    @PostMapping("/historicProcessInstanceDataTable")
    @ResponseBody
    public TableGridResponse<HistoricProcessInstanceVO> historicProcessInstanceDataTable(@RequestBody TableGridInput tableGridInput) {
        return myHistoryService.historicProcessInstanceDataTable(tableGridInput);
    }
    //流程历史节点列表
    @GetMapping("/historicTaskInstanceList")
    public void historicTaskInstanceList() {
    }
    @PostMapping("/historicTaskInstanceDataTable")
    @ResponseBody
    public DataTablesOutput<HistoricTaskInstanceVO> historicTaskInstanceDataTable(@RequestBody DataTablesInput dataTablesInput) {
        return myHistoryService.historicTaskInstanceDataTable(dataTablesInput);
    }
    @GetMapping(value = "/detail/{processInstanceId}")
    public String detail(@PathVariable("processInstanceId") String processInstanceId, Model model) {
        //实例节点
        model.addAttribute("historicActivityInstanceList", myHistoryService.activityInstanceVo(processInstanceId));
        model.addAttribute("historicVariableInstanceList", myHistoryService.activityInstanceVar(processInstanceId));
        return "bpm/history/detail";
    }
    @PostMapping("/delete")
    @ResponseBody
    public Object delete(@RequestParam("ids[]") List<String> ids){
        return success(myHistoryService.batchDelete(ids));
    }
}
