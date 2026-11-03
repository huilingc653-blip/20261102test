package com.bytter.system.web.controller;

import com.bytter.basic.service.LogService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zhouj on 2017/10/23.
 */
@Controller
@RequestMapping("/log/operate")
public class BtLogController extends BaseController {
    @Autowired
    private LogService logService;

    @GetMapping("/list")
    public String list(Model model) {
        return "log/listLog";
    }

    /**
     * table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return logService.getLogList(dataTablesInput, getCurrentUser());
    }

}
