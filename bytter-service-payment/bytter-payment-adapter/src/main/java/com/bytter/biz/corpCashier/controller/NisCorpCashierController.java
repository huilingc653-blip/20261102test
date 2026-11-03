package com.bytter.biz.corpCashier.controller;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.corpCashier.service.NisCorpCashierService;
import com.bytter.biz.corpCashier.vo.NisCorpCashierVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description: 单位出纳维护
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/7/11
 */
@Controller
@RequestMapping("/netbank/corpCashier")
public class NisCorpCashierController extends BaseController {
    @Autowired
    private NisCorpCashierService corpCashierService;

    @RequestMapping("/list")
    public String list() {
        return "corpCashier/listNisCorpCashier";
    }

    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return corpCashierService.getDataTablesUnion(dataTablesInput);
    }

    @GetMapping("/add")
    public String add(Model model){
        return "corpCashier/addNisCorpCashier";
    }

    @GetMapping("/update")
    public String update(String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            NisCorpCashierVO nisCorpCashierVO = corpCashierService.getCorpCashier(id);
            model.addAttribute("nisCorpCashierVO", nisCorpCashierVO);
        }
        return "corpCashier/addNisCorpCashier";
    }

    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(NisCorpCashierVO vo){
        corpCashierService.save(vo,getCurrentUser());
        return success();
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        corpCashierService.deleteCorpCashier(id);
        return success();
    }

    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson batchDelete(@RequestParam("ids") String ids){
        if (ObjectUtil.isNotEmpty(ids)){
            for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
                corpCashierService.deleteCorpCashier(id);
            }
        }
        return success();
    }

    /**
     * 保存导入数据
     * @param list
     * @return
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<NisCorpCashierVO> list) {
        corpCashierService.saveImport(list, this.getCurrentUser());
        return success();
    }
}
