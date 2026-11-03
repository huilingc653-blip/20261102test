package com.bytter.system.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.common.queue.QueueManage;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.entity.SysTask;
import com.bytter.system.service.SysTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
/**
 * Created by chenlj on 2018/3/16
 */
@Controller
@RequestMapping("/sys/serviceConsume")
public class ServiceConsumeController extends BaseController {
    @Autowired
    private SysTaskService sysTaskService;
    @GetMapping("/list")
    public String list(Model model) {
        return "sys/serviceConsume/listSysTask";
    }
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        Map<String, Object> formdata = dataTablesInput.getFormdata();
        formdata.put("eqi_type", "3");
        dataTablesInput.setFormdata(formdata);
        return sysTaskService.getDataTables(dataTablesInput);
    }
    @GetMapping("/add")
    public String add(SysTask sysTask, Model model) {
        if (StringUtils.isNotEmpty(sysTask.getId())) {
            sysTask = sysTaskService.get(sysTask.getId());
            model.addAttribute("sysTaskPage", sysTask);
        }
        return "sys/serviceConsume/addSysTask";
    }
    @GetMapping("/update")
    public String update(SysTask sysTask, Model model) {
        if (StringUtils.isNotEmpty(sysTask.getId())) {
            sysTask = sysTaskService.get(sysTask.getId());
            model.addAttribute("sysTaskPage", sysTask);
        }
        return "sys/serviceConsume/updateSysTask";
    }
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        SysTask sysConsumeTask = sysTaskService.get(id);
        model.addAttribute("sysTaskPage", sysConsumeTask);
        return "sys/serviceConsume/viewSysTask";
    }
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(SysTask sysTask) {
        sysTaskService.saveServiceConsume(sysTask, getUserLoginName());
        return success();
    }
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(SysTask sysTask) {
        SysTask t = sysTaskService.get(sysTask.getId());
        BeanUtils.copyNotNullProperties(sysTask, t);
        sysTaskService.saveOrUpdate(t);
        return success();
    }
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        SysTask sysTask = sysTaskService.get(id);
        sysTask.setActive(0);
        sysTask.setDeletedsw(1);
        sysTaskService.update(sysTask, getUserLoginName());
        return success();
    }
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(",")) {
            SysTask sysTask = sysTaskService.get(id);
            sysTask.setActive(0);
            sysTask.setDeletedsw(1);
            sysTaskService.update(sysTask, getUserLoginName());
        }
        return success();
    }
    @ResponseBody
    @PostMapping("/runnow/{id}")
    public AjaxJson runnow(@PathVariable("id") String taskId) {
        if (StringUtils.isNotBlank(taskId)) {
            SysTask gTask = sysTaskService.get(taskId, true);
            if (gTask != null) {
                QueueManage.getInstance().addMessageObject(gTask);
            }
        }
        return success();
    }
    @PostMapping("/status/{id}")
    @ResponseBody
    public AjaxJson status(@PathVariable("id") String id) {
        SysTask sysTask = sysTaskService.get(id);
        if (!EmptyUtils.isEmpty(sysTask)) {
            sysTask.setActive(sysTask.getActive() == 1 ? 0 : 1);
            sysTaskService.update(sysTask, getUserLoginName());
        }
        return success();
    }
}
