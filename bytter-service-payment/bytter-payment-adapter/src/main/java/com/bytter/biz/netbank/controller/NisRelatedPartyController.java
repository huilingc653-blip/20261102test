package com.bytter.biz.netbank.controller;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.netbank.entity.NisRelatedParty;
import com.bytter.biz.netbank.service.NisRelatedPartyService;
import com.bytter.biz.netbank.vo.NisRelatedPartyVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Description: 关联方维护与报表
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/10/17
 */
@Controller
@RequestMapping("/netbank/relatedParty")
public class NisRelatedPartyController extends BaseController {
    @Autowired
    private NisRelatedPartyService nisRelatedPartyService;

    @GetMapping("/list")
    public String list(Model model) {
        return "netbank/listNisRelatedParty";
    }

    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return nisRelatedPartyService.getDataTables(dataTablesInput,false);
    }

    @GetMapping("/add")
    public String add(Model model) {
        return "netbank/addNisRelatedParty";
    }

    @GetMapping("/update")
    public String update(NisRelatedParty entity, Model model) {
        if (StringUtils.isNotEmpty(entity.getId())) {
            entity = nisRelatedPartyService.get(entity.getId(), true);
            model.addAttribute("entity", entity);
        }
        return "netbank/updateNisRelatedParty";
    }

    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(NisRelatedPartyVO vo) {
        nisRelatedPartyService.save(vo, this.getCurrentUser());
        return success();
    }

    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(NisRelatedPartyVO vo) {
        nisRelatedPartyService.update(vo, this.getCurrentUser());
        return success();
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        nisRelatedPartyService.delete(id);
        return success();
    }

    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson batchDelete(@RequestParam("ids") String ids){
        if (ObjectUtil.isNotEmpty(ids)){
            for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
                nisRelatedPartyService.delete(id);
            }
        }
        return success();
    }

    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<NisRelatedPartyVO> list) {
        nisRelatedPartyService.saveImport(list, this.getCurrentUser());
        return success();
    }

    /// ///////////////////////////////////报表///////////////////////////////

    @GetMapping("/reportList")
    public String reportList(Model model) {
        model.addAttribute("queryDate", DateUtil.formatDate(new Date()));
        return "report/listRelatedPartyReport";
    }

    @PostMapping("/reportDatatable")
    @ResponseBody
    public DataTablesOutput reportDatatable(@RequestBody DataTablesInput dataTablesInput) {
        return nisRelatedPartyService.reportDatatable(dataTablesInput,getCurrentUser());
    }
}
