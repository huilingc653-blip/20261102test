package com.bytter.report.web.cms.controller;

import com.bytter.report.domain.cms.service.BusinessVolumeService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;


/**
 * @author zhengxn
 * @Description 业务量报表
 */

@Controller
@RequestMapping("/cms/report/businessvolume")
public class BusinessVolumeController {

    @Autowired
    private BusinessVolumeService businessVolumeService;

    //@RequiresPermissions("cms.report.bus.query")
    @GetMapping("/list")
    public String list(Model model){
        return "cms/report/businessVolume/listBusinessVolume";
    }

    //@RequiresPermissions("cms.report.bus.query")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) throws ParseException {
        return businessVolumeService.getDataTable(dataTablesInput);
    }
}
