package com.bytter.biz.netbank.controller;

import com.bytter.biz.netbank.entity.BtBankAccUse;
import com.bytter.biz.netbank.service.BtBankAccUseService;
import com.bytter.biz.netbank.vo.BtBankAccUseVO;
import com.bytter.biz.netbank.vo.FcBankAccGroupVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description: 账号用途维护
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2024
 *
 * @author ZRS
 * @date 2024/10/23
 */
@Controller
@RequestMapping("/netbank/bankAccUse")
public class BtBankAccUseController extends BaseController {
    @Autowired
    private BtBankAccUseService service;

    @LogMessage(content = "账号用途维护列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_SELECT)
    @GetMapping("/list")
    public String list(Model model) {
        return "netbank/listBankAccUse";
    }

    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return service.listData(dataTablesInput);
    }

    /**
     * 新增账户用途
     * @return
     */
    @GetMapping("/add")
    public String add(Model model) {
        return "netbank/addBankAccUse";
    }

    @LogMessage(content = "新增账户用途",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(BtBankAccUseVO vo){
        service.save(vo,getCurrentUser());
        return success();
    }

    /**
     * 修改账户用途
     * @return
     */
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            model.addAttribute("bankAccUse", service.get(id, true));
        }
        return "netbank/updateBankAccUse";
    }

    @LogMessage(content = "删除账户用途",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        service.deleteById(id);
        return success();
    }

    /**
     * excel 导入
     * @param
     * @return
     */
    @LogMessage(content = "添加账号用途", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<BtBankAccUseVO> list) {
        service.saveImport(list, getCurrentUser());
        return success();
    }

    @GetMapping("/json")
    @ResponseBody
    public List<BtBankAccUse> json(){
        return service.listAll();
    }
}
