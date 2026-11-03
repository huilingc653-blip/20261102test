package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.entity.AbBillPlanAmtMaintain;
import com.bytter.bill.domain.bill.service.AbBillPlanAmtMaintainService;
import com.bytter.bill.domain.bill.vo.AbBillPlanAmtMaintainVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 银行计划开票金额维护
 *
 * @author fangcf
 * @date 2025-06-30
 */
@Api(value = "/v1-preposebill/bill/accebill/planAmtMaintain", tags = "银行计划开票金额维护")
@RestController("v1AbBillPlanAmtMaintainController")
@RequestMapping("/v1-preposebill/bill/accebill/planAmtMaintain")
public class AbBillPlanAmtMaintainController extends BaseController {

    @Autowired
    private AbBillPlanAmtMaintainService abBillPlanAmtMaintainService;

    /**
     * 银行计划开票金额维护列表 页面初始化
     */
    @LogMessage(content = "银行计划开票金额维护列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    @ApiOperation("银行计划开票金额维护列表页面初始化")
    public AjaxJson<Map<String, Object>> list() {
        ConcurrentModel model = new ConcurrentModel();
        return AjaxJson.success(model.asMap());
    }

    /**
     * 银行计划开票金额维护列表 table列表
     */
    @PostMapping("/datatable")
    @ApiOperation("银行计划开票金额维护列表分页查询")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillPlanAmtMaintainService.dataTable(input, this.getCurrentUser());
        return AjaxJson.success(output);
    }

    /**
     * 银行计划开票金额维护列表新增页面初始化
     */
    @GetMapping("/add")
    @ApiOperation("银行计划开票金额维护新增页面初始化")
    public AjaxJson<Map<String, Object>> add() {
        ConcurrentModel model = new ConcurrentModel();
        // “周开始日”必须是周一。获取2000年-2099年所有周一日期，然后日期字符串拼接
        String showAllMonday = abBillPlanAmtMaintainService.getMondays();
        model.addAttribute("showAllMonday", showAllMonday);
        return AjaxJson.success(model.asMap());
    }

    /**
     * 银行计划开票金额维护列表编辑页面初始化
     */
    @GetMapping("/update")
    @ApiOperation("银行计划开票金额维护编辑页面初始化")
    public AjaxJson<Map<String, Object>> update(AbBillPlanAmtMaintainVO vo) {
        ConcurrentModel model = new ConcurrentModel();
        if (StringUtils.isNotEmpty(vo.getId())) {
            AbBillPlanAmtMaintain entity = abBillPlanAmtMaintainService.get(vo.getId(), true);
            model.addAttribute("planAmtPage", entity);
            // “周开始日”必须是周一。获取2000年-2099年所有周一日期，然后日期字符串拼接
            String showAllMonday = abBillPlanAmtMaintainService.getMondays();
            model.addAttribute("showAllMonday", showAllMonday);
        }
        return AjaxJson.success(model.asMap());
    }

    /**
     * 保存银行计划开票金额提交
     */
    @LogMessage(content = "保存银行计划开票金额", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ApiOperation("新增银行计划开票金额")
    public AjaxJson save(AbBillPlanAmtMaintain form) {
        abBillPlanAmtMaintainService.savePlanAmt(form, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 银行计划开票金额删除
     */
    @LogMessage(content = "删除银行计划开票金额", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ApiOperation("删除银行计划开票金额")
    public AjaxJson delete(@PathVariable("id") String id) {
        abBillPlanAmtMaintainService.delPlanAmt(id);
        return AjaxJson.success();
    }

    /**
     * 编辑银行计划开票金额提交
     */
    @LogMessage(content = "编辑银行计划开票金额", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ApiOperation("编辑银行计划开票金额")
    public AjaxJson modify(AbBillPlanAmtMaintain form) {
        abBillPlanAmtMaintainService.updatePlanAmt(form, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 银行计划开票金额导入
     */
    @PostMapping("/saveImport")
    @ApiOperation("银行计划开票金额导入")
    public AjaxJson saveImport(@RequestBody List<AbBillPlanAmtMaintain> list) {
        abBillPlanAmtMaintainService.saveImport(list, getCurrentUser());
        return AjaxJson.success();
    }
}