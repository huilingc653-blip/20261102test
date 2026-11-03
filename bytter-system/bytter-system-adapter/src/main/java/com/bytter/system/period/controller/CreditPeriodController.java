package com.bytter.system.period.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.enums.BaseDataMessageEnum;
import com.bytter.system.entity.TbCreditPeriod;
import com.bytter.system.service.CreditPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 贷款期限表
 * @date 2017-07-26 17:50:25
 */
@Controller
@RequestMapping("/period/tbcreditperiod")
public class CreditPeriodController extends BaseController {
    @Autowired
    private CreditPeriodService creditPeriodService;
    /**
     * 贷款期限表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询贷款期限表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/basedata/tbCreditPeriod/listTbCreditPeriod";
    }
    /**
     * 贷款期限表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        dataTablesInput.addFilter(MatchTypeEnum.IS_NULL, "interestRateType", 0);
        return creditPeriodService.getDataTables(dataTablesInput);
    }

    /**
     * <b>功能介绍：</b>本位币贷款期限<br>
     * <b>日期：</b>2017年7月28日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @RequestMapping("/allJson")
    @ResponseBody
    public List<TbCreditPeriod> getTbCreditPeriod() {
        return creditPeriodService.getTbCreditPeriod();
    }
    /**
     * <b>功能介绍：</b>本位币贷款期限<br>
     * <b>日期：</b>2017年7月28日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @RequestMapping("/rmbJson")
    @ResponseBody
    public List<TbCreditPeriod> getrmbTbCreditPeriod() {
        return creditPeriodService.getrmbTbCreditPeriod();
    }
    /**
     * <b>功能介绍：</b>外币贷款期限<br>
     * <b>日期：</b>2017年7月28日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @RequestMapping("/foreignJson")
    @ResponseBody
    public List<TbCreditPeriod> getForeignTbCreditPeriod() {
        return creditPeriodService.getforeignTbCreditPeriod();
    }
    /**
     * 贷款期限表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(TbCreditPeriod tbCreditPeriod, Model model) {
        if (StringUtils.isNotEmpty(tbCreditPeriod.getId())) {
            tbCreditPeriod = creditPeriodService.get(tbCreditPeriod.getId());
            model.addAttribute("tbCreditPeriodPage", tbCreditPeriod);
        }
        return "cms/basedata/tbCreditPeriod/addTbCreditPeriod";
    }
    /**
     * 贷款期限表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(TbCreditPeriod tbCreditPeriod, Model model) {
        if (StringUtils.isNotEmpty(tbCreditPeriod.getId())) {
            tbCreditPeriod = creditPeriodService.get(tbCreditPeriod.getId());
            model.addAttribute("tbCreditPeriodPage", tbCreditPeriod);
        }
        return "cms/basedata/tbCreditPeriod/updateTbCreditPeriod";
    }
    /**
     * 贷款期限表详情页
     *
     * @return
     */
    @LogMessage(content = "查询贷款期限表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        TbCreditPeriod tbCreditPeriod = creditPeriodService.get(id);
        model.addAttribute("tbCreditPeriodPage", tbCreditPeriod);
        return "cms/basedata/tbCreditPeriod/viewTbCreditPeriod";
    }
    /**
     * 添加贷款期限表
     *
     * @param tbCreditPeriod
     * @return
     */
    @LogMessage(content = "添加贷款期限表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(TbCreditPeriod tbCreditPeriod) {
        creditPeriodService.savePeriod(tbCreditPeriod);
        return success();
    }
    /**
     * 修改贷款期限表
     *
     * @param tbCreditPeriod
     * @return
     */
    @LogMessage(content = "修改贷款期限表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(TbCreditPeriod tbCreditPeriod) {
        TbCreditPeriod t = creditPeriodService.get(tbCreditPeriod.getId());
        if (!tbCreditPeriod.getCode().equals(t.getCode())) {// 编码发生变化需要验证是否重复
            if (creditPeriodService.findCreditPeriodByCode(tbCreditPeriod.getCode()) != null) {// 期限是否存在
                throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_CREDITPERIOD_EXIST);
            }
        }
        creditPeriodService.isAllowOp(t);// 是否允许操作验证
        BeanUtils.copyNotNullProperties(tbCreditPeriod, t);
        creditPeriodService.setCreditDays(t);// 设置贷款总天数
        creditPeriodService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除贷款期限表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除贷款期限表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        TbCreditPeriod tbCreditPeriod = creditPeriodService.get(id);
        creditPeriodService.isAllowOp(tbCreditPeriod);// 是否允许操作验证
        creditPeriodService.deleteEntity(tbCreditPeriod);
        return success();
    }
    /**
     * 批量删除贷款期限表
     *
     * @param ids
     * @return
     */
    // @LogMessage(content = "批量删除贷款期限表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(",")) {
            TbCreditPeriod tbCreditPeriod = creditPeriodService.get(id);
            creditPeriodService.isAllowOp(tbCreditPeriod);// 是否允许操作验证
            creditPeriodService.deleteEntity(tbCreditPeriod);
        }
        return success();
    }
}
