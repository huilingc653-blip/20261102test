package com.bytter.biz.bdlink.controller;

import com.bytter.biz.bdlink.entity.BisBifMonitor;
import com.bytter.biz.bdlink.service.BisBifMonitorService;
import com.bytter.biz.bdlink.vo.BisBifMonitorVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author huangx
 * @version 1.0
 * @Title: Controller
 * @Description: 银行接口监控预警表
 * @date 2022-07-07 17:07:12
 */
@Controller
@RequestMapping("/bdlink/bisbifmonitor")
public class BisBifMonitorController extends BaseController {
    @Autowired
    private BisBifMonitorService bisBifMonitorService;
    /**
     * 银行接口监控预警维护列表 页面跳转
     *
     * @param model
     * @return
     */
    @LogMessage(content = "查询银行接口监控预警维护列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "basic/business/listBisBifMonitor";
    }
    /**
     * 银行接口监控预警维护 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput dataTablesOutput = bisBifMonitorService.getDataTablesUnion(dataTablesInput);
        return dataTablesOutput;
    }
    /**
     * 银行接口监控预警维护新增页面跳转
     *
     * @param bisBifMonitor
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String add(BisBifMonitor bisBifMonitor, Model model) {
        if (StringUtils.isNotEmpty(bisBifMonitor.getId())) {
            bisBifMonitor = bisBifMonitorService.get(bisBifMonitor.getId());
            model.addAttribute("bisBifMonitorPage", bisBifMonitor);
        }
        List<BisBifMonitor> list = bisBifMonitorService.listAll();
        String filterBisBifIds = "";
        for (BisBifMonitor monitor : list) {
            filterBisBifIds += monitor.getBisBifId() + CommonConstant.COMMA_SPLIT;
        }
        model.addAttribute("filterBisBifIds", filterBisBifIds);
        return "basic/business/addBisBifMonitor";
    }
    /**
     * 银行接口监控预警维护编辑页面跳转
     *
     * @param bisBifMonitor
     * @param model
     * @return
     */
    @GetMapping("/update")
    public String update(BisBifMonitorVO bisBifMonitor, Model model) {
        if (StringUtils.isNotEmpty(bisBifMonitor.getId())) {
            bisBifMonitor = bisBifMonitorService.getByIdUnion(bisBifMonitor.getId());
            model.addAttribute("bisBifMonitorPage", bisBifMonitor);
        }
        return "basic/business/updateBisBifMonitor";
    }
    /**
     * 添加银行接口监控预警维护
     *
     * @param bisBifMonitor
     * @return
     */
    @LogMessage(content = "添加银行接口监控预警维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(BisBifMonitorVO bisBifMonitor) {
        bisBifMonitorService.saveBisBifMonitor(bisBifMonitor, this.getCurrentUser());
        return success();
    }
    /**
     * 修改银行接口监控预警维护
     *
     * @param bisBifMonitor
     * @return
     */
    @LogMessage(content = "修改银行接口监控预警维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(BisBifMonitorVO bisBifMonitor) {
        bisBifMonitorService.updateBisBifMonitor(bisBifMonitor, this.getCurrentUser());
        return success();
    }
    /**
     * 删除银行接口监控预警维护
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除银行接口监控预警维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        BisBifMonitor bisBifMonitor = bisBifMonitorService.get(id);
        bisBifMonitorService.deleteEntity(bisBifMonitor);
        return success();
    }
    /**
     * 批量删除银行接口监控预警维护
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除银行接口监控预警维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BisBifMonitor bisBifMonitor = bisBifMonitorService.get(id);
            bisBifMonitorService.deleteEntity(bisBifMonitor);
        }
        return success();
    }
}
