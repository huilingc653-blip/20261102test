package com.bytter.biz.creditRating.controller;


import com.bytter.biz.creditRating.service.CrIndicatorCategoryService;
import com.bytter.biz.creditRating.vo.CrIndicatorCategoryVo;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 指标类别(已弃用)
 */
@Controller
@RequestMapping("/creditRating/crIndicatorCategory")
public class CrIndicatorCategoryController extends BaseController {

    @Autowired
    private CrIndicatorCategoryService crIndicatorCategoryService;

    /**
     * 跳转指标类别列表
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String list(Model model){
        return "creditRating/listInDicatorCategory";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable("id")String id, Model model){
        CrIndicatorCategoryVo crIndicatorCategoryVo = crIndicatorCategoryService.getById(id);
        model.addAttribute("vo", crIndicatorCategoryVo);
        return "creditRating/viewInDicatorCategory";
    }
    /**
     * 查询指标类别列表数据
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/dataTable")
    @ResponseBody
    public DataTablesOutput getDataTable(@RequestBody DataTablesInput dataTablesInput){
        return crIndicatorCategoryService.getDataTable(dataTablesInput);
    }

    /**
     * 跳转指标类别新增页面
     * @return
     */
    @GetMapping("/add")
    public String addIndicatorCategory(){
        return "creditRating/addInDicatorCategory";
    }

    /**
     * 保存指标类别
     * @param crIndicatorCategoryVo
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CrIndicatorCategoryVo crIndicatorCategoryVo){
        crIndicatorCategoryService.save(crIndicatorCategoryVo,getCurrentUser().getId());
        return success();
    }

    /**
     * 跳转指标类别编辑页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/edit")
    public String edit(String id, Model model){
        CrIndicatorCategoryVo crIndicatorCategoryVo = crIndicatorCategoryService.getById(id);
        model.addAttribute("indicatorCategoryVo", crIndicatorCategoryVo);
        return "creditRating/editInDicatorCategory";
    }

    /**
     * 更新指标类别
     * @param crIndicatorCategoryVo
     * @return
     */
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(CrIndicatorCategoryVo crIndicatorCategoryVo){
        crIndicatorCategoryService.update(crIndicatorCategoryVo);
        return success();
    }

    /**
     * 删除指标类别(逻辑删除)
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id")String id){
        crIndicatorCategoryService.delete(id);
        return success();
    }

    /**
     * 获取有校的指标类别
     * @return
     */
    @PostMapping("/getEffectData")
    @ResponseBody
    public AjaxJson getEffectData(){
        List<CrIndicatorCategoryVo> effectData = crIndicatorCategoryService.getEffectData();
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        ajaxJson.setObj(effectData);
        return ajaxJson;
    }
    /**
     * 获取有校的指标类别
     * @return
     */
    @GetMapping("/json")
    @ResponseBody
    public  List<CrIndicatorCategoryVo> json(){
        List<CrIndicatorCategoryVo> effectData = crIndicatorCategoryService.getEffectData();
        return effectData;
    }
}
