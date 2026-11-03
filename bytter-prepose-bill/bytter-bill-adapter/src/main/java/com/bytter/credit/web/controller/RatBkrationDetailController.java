package com.bytter.credit.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.domain.entity.RatBkrationDetail;
import com.bytter.credit.domain.service.RatBkrationDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @Title: Controller
 * @Description: 授信录入明细表
 * @author onlineGenerator
 * @date 2017-07-21 14:35:53
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/rat/ratbkrationdetail")
public class RatBkrationDetailController extends BaseController {
    @Autowired
    private RatBkrationDetailService ratBkrationDetailService;
    /**
     * 授信录入明细表列表 页面跳转
     *
     * @return
     */
    //
    @LogMessage(content = "查询授信录入明细表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "rat/listRatBkrationDetail";
    }
    /**
     * 授信录入明细表 table列表
     *
     * @return
     */
    //
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return ratBkrationDetailService.getDataTables(dataTablesInput);
    }
    /**
     * 授信录入明细表新增页面跳转
     *
     * @return
     */
    //
    @GetMapping("/add")
    public String add(RatBkrationDetail ratBkrationDetail, Model model) {
        if (StringUtils.isNotEmpty(ratBkrationDetail.getId())) {
            ratBkrationDetail = ratBkrationDetailService.get(ratBkrationDetail.getId());
            model.addAttribute("ratBkrationDetailPage", ratBkrationDetail);
        }
        return "rat/addRatBkrationDetail";
    }
    /**
     * 授信录入明细表编辑页面跳转
     *
     * @return
     */
    //
    @GetMapping("/update")
    public String update(RatBkrationDetail ratBkrationDetail, Model model) {
        if (StringUtils.isNotEmpty(ratBkrationDetail.getId())) {
            ratBkrationDetail = ratBkrationDetailService.get(ratBkrationDetail.getId());
            model.addAttribute("ratBkrationDetailPage", ratBkrationDetail);
        }
        return "rat/updateRatBkrationDetail";
    }
    /**
     * 授信录入明细表详情页
     *
     * @return
     */
    //
    @LogMessage(content = "查询授信录入明细表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        RatBkrationDetail ratBkrationDetail = ratBkrationDetailService.get(id);
        model.addAttribute("ratBkrationDetailPage", ratBkrationDetail);
        return "rat/viewRatBkrationDetail";
    }
    /**
     * 添加授信录入明细表
     * 
     * @param ratBkrationDetail
     * @return
     */
    //
    @LogMessage(content = "添加授信录入明细表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(RatBkrationDetail ratBkrationDetail) {
        ratBkrationDetailService.save(ratBkrationDetail);
        return success();
    }
    /**
     * 修改授信录入明细表
     * 
     * @param ratBkrationDetail
     * @return
     */
    //
    @LogMessage(content = "修改授信录入明细表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(RatBkrationDetail ratBkrationDetail) {
        RatBkrationDetail t = ratBkrationDetailService.get(ratBkrationDetail.getId());
        BeanUtils.copyNotNullProperties(ratBkrationDetail, t);
        ratBkrationDetailService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除授信录入明细表
     * 
     * @param id
     * @return
     */
    //
    @LogMessage(content = "删除授信录入明细表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        RatBkrationDetail ratBkrationDetail = ratBkrationDetailService.get(id);
        ratBkrationDetailService.deleteEntity(ratBkrationDetail);
        return success();
    }
    /**
     * 批量删除授信录入明细表
     * 
     * @param ids
     * @return
     */
    //
    @LogMessage(content = "批量删除授信录入明细表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(",")) {
            RatBkrationDetail ratBkrationDetail = ratBkrationDetailService.get(id);
            ratBkrationDetailService.deleteEntity(ratBkrationDetail);
        }
        return success();
    }
}
