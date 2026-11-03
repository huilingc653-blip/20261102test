package com.bytter.credit.web.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatCreditCooperationBank;
import com.bytter.credit.domain.entity.RatYearFinPlanRegister;
import com.bytter.credit.domain.service.RatYearFinPlanRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 年度融资规划登记
 *
 * @author fangcf
 * @date 2024-06-28
 */
@Controller
@RequestMapping("/cms/rat/yearFinPlanRegister")
public class RatYearFinPlanRegisterController extends BaseController {

    @Autowired
    private RatYearFinPlanRegisterService ratYearFinPlanRegisterService;

    /**
     * 年度融资规划登记 页面跳转
     *
     * @return 页面地址
     */
    @LogMessage(content = "查询年度融资规划登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/rat/yearfinplanregister/listRatYearFinPlanRegister";
    }

    /**
     * 年度融资规划登记 table列表
     *
     * @param input 入参对象
     * @return 出参对象
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return ratYearFinPlanRegisterService.listOutputData(input, getUserId());
    }

    /**
     * 年度融资规划登记新增页面跳转
     *
     * @param model model对象
     * @return 页面地址
     */
    @GetMapping("/add")
    public String add(Model model) {
        return "cms/rat/yearfinplanregister/addRatYearFinPlanRegister";
    }

    /**
     * 年度融资规划登记编辑页面跳转
     *
     * @param entity 年度融资规划登记对象
     * @param model  model对象
     * @return 页面地址
     */
    @GetMapping("/update")
    public String update(RatYearFinPlanRegister entity, Model model) {
        if (StringUtils.isNotEmpty(entity.getId())) {
            entity = ratYearFinPlanRegisterService.get(entity.getId(), true);
            model.addAttribute("entity", entity);
        }
        return "cms/rat/yearfinplanregister/updateRatYearFinPlanRegister";
    }

    /**
     * 新增年度融资规划登记
     *
     * @param entity 年度融资规划登记对象
     * @return 出参对象
     */
    @LogMessage(content = "新增年度融资规划登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(RatYearFinPlanRegister entity) {
        ratYearFinPlanRegisterService.saveYearFinPlanRegister(entity, getCurrentUser());
        return success();
    }

    /**
     * 删除年度融资规划登记
     *
     * @param id 主键id
     * @return 出参对象
     */
    @LogMessage(content = "删除年度融资规划登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        ratYearFinPlanRegisterService.deleteYearFinPlanRegister(id);
        return success();
    }

    /**
     * 修改年度融资规划登记
     *
     * @param entity 年度融资规划登记对象
     * @return 出参对象
     */
    @LogMessage(content = "修改年度融资规划登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(RatYearFinPlanRegister entity) {
        ratYearFinPlanRegisterService.updateYearFinPlanRegister(entity, getCurrentUser());
        return success();
    }

    /**
     * 授信合作银行导入
     *
     * @param list 导入数据集合
     * @return 出参对象
     */
    @LogMessage(content = "授信合作银行导入", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_Import)
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<RatYearFinPlanRegister> list) {
        ratYearFinPlanRegisterService.saveImport(list, getCurrentUser());
        return success();
    }
}
