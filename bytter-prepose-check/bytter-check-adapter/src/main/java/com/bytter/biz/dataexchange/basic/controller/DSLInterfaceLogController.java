package com.bytter.biz.dataexchange.basic.controller;

import com.bytter.biz.dataexchange.basic.service.DSLInterfaceLogService2;
import com.bytter.biz.dataexchange.basic.vo.DSLInterfaceLogVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * @author lijinquan
 * @description:
 * @date 2024-04-30 16:36
 */

@Controller
@RequestMapping("/dataexchange2/interfacelog")
public class DSLInterfaceLogController extends BaseController {

    @Autowired
    private DSLInterfaceLogService2 logService;

    @GetMapping("/list")
    public String list(Model model) {
        return "dataexchange/interfacelog/list";
    }

    @PostMapping("/query")
    @ResponseBody
    public DataTablesOutput query(@RequestBody DataTablesInput dataTablesInput) {
        return logService.getDataTablesUnion(dataTablesInput);
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        DSLInterfaceLogVo vo = logService.getErpInterfaceLogVo(id);
        model.addAttribute("dslInterfaceLogVo", vo);
        return "dataexchange/interfacelog/view";
    }
}
