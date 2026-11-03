package com.bytter.system.rate.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.entity.TbBaseRate;
import com.bytter.system.service.BaseRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 基准利率表
 * @date 2017-07-26 17:33:59
 */
@Controller
//@RequestMapping("/cms/basedata/tbbaserate")
@RequestMapping("/rate/tbbaserate")
public class BaseRateController extends BaseController {
    @Autowired
    private BaseRateService baseRateService;
    /**
     * 基准利率表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询基准利率表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/basedata/tbbaserate/listTbBaseRate";
    }
    /**
     * 基准利率表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return baseRateService.getDataTables(dataTablesInput);
    }
    /**
     * 基准利率表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(TbBaseRate tbBaseRate, Model model) {
        if (StringUtils.isNotEmpty(tbBaseRate.getId())) {
            tbBaseRate = baseRateService.get(tbBaseRate.getId());
            model.addAttribute("tbBaseRatePage", tbBaseRate);
        }
        return "cms/basedata/tbbaserate/addTbBaseRate";
    }
    /**
     * 基准利率表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(TbBaseRate tbBaseRate, Model model) {
        if (StringUtils.isNotEmpty(tbBaseRate.getId())) {
            tbBaseRate = baseRateService.get(tbBaseRate.getId());
            model.addAttribute("tbBaseRatePage", tbBaseRate);
        }
        return "cms/basedata/tbbaserate/updateTbBaseRate";
    }
    /**
     * 基准利率表详情页
     *
     * @return
     */
    @LogMessage(content = "查询基准利率表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        TbBaseRate tbBaseRate = baseRateService.get(id);
        model.addAttribute("tbBaseRatePage", tbBaseRate);
        return "cms/basedata/tbbaserate/viewTbBaseRate";
    }
    /**
     * 添加基准利率表
     *
     * @param tbBaseRate
     * @return
     */
    @LogMessage(content = "添加基准利率表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(TbBaseRate tbBaseRate) {
        baseRateService.save(tbBaseRate);
        return success();
    }
    /**
     * 修改基准利率表
     *
     * @param tbBaseRate
     * @return
     */
    @LogMessage(content = "修改基准利率表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(TbBaseRate tbBaseRate) {
        TbBaseRate t = baseRateService.get(tbBaseRate.getId());
        BeanUtils.copyNotNullProperties(tbBaseRate, t);
        baseRateService.doUpdate(t, tbBaseRate.getId());
        return success();
    }
    /**
     * 删除基准利率表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除基准利率表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        baseRateService.doDelete(id);
        return success();
    }
    @GetMapping("/json")
    @ResponseBody
    public List<TbBaseRate> getListByValidSign() {
        return baseRateService.getListTbBaseRate();
    }
}
