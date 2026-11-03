package com.bytter.biz.budget.saas.controller.budget;

import com.bytter.biz.budget.entity.FbsBudgetPeriod;
import com.bytter.biz.budget.service.FbsBudgetPeriodService;
import com.bytter.biz.budget.vo.FbsBudgetPeriodTransmitVO;
import com.bytter.biz.budget.vo.FbsBudgetPeriodVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: FBS_BUDGET_PERIOD
 * @date 2017-07-07 16:09:08
 */
@Api(value = "/v1-plan/budget/fbsbudgetperiod", tags = "周期自定义设置")
@RestController("v1FbsBudgetPeriodService")
@RequestMapping("/v1-plan/budget/fbsbudgetperiod")
public class FbsBudgetPeriodController extends BaseController {
    @Autowired
    private FbsBudgetPeriodService fbsBudgetPeriodService;

    /**
     * FBS_BUDGET_PERIOD列表 页面跳转
     *
     * @return
     */

    @LogMessage(content = "查询FBS_BUDGET_PERIOD列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public AjaxJson<Map<String, Object>> list() {
        Map<String, Object> map = new HashMap<>();
        int defaultYear = DateUtils.getYear();
        map.put("defaultYear", defaultYear);
        return AjaxJson.success(map);
    }

    /**
     * FBS_BUDGET_PERIOD table列表
     *
     * @return
     */

    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(fbsBudgetPeriodService.getDataTables(input));
    }

    /**
     * FBS_BUDGET_PERIOD编辑页面跳转
     *
     * @return
     */

    @GetMapping("/update/{id}")
    public AjaxJson<FbsBudgetPeriod> update(@PathVariable String id) {
     return AjaxJson.success( fbsBudgetPeriodService.get(id));
    }

    /**
     * FBS_BUDGET_PERIOD详情页
     *
     * @return
     */

    @LogMessage(content = "查询FBS_BUDGET_PERIOD详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<FbsBudgetPeriod>  view(@PathVariable("id") String id) {
		return AjaxJson.success( fbsBudgetPeriodService.get(id));
    }

    /**
     * 添加FBS_BUDGET_PERIOD
     *
     * @param fbsBudgetPeriodBean
     * @return
     */

    @LogMessage(content = "添加FBS_BUDGET_PERIOD", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody  FbsBudgetPeriodTransmitVO fbsBudgetPeriodBean) {
        fbsBudgetPeriodService.toSave(fbsBudgetPeriodBean, this.getCurrentUser());
        return success();
    }

    /**
     * 修改FBS_BUDGET_PERIOD
     *
     * @param fbsBudgetPeriod
     * @return
     */

    @LogMessage(content = "修改FBS_BUDGET_PERIOD", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(@RequestBody  FbsBudgetPeriod fbsBudgetPeriod) {
        FbsBudgetPeriod t = fbsBudgetPeriodService.get(fbsBudgetPeriod.getId());
        BeanUtils.copyNotNullProperties(fbsBudgetPeriod, t);
        fbsBudgetPeriodService.saveOrUpdate(t);
        return success();
    }

    /**
     * 删除FBS_BUDGET_PERIOD
     *
     * @param id
     * @return
     */

    @LogMessage(content = "删除FBS_BUDGET_PERIOD", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        FbsBudgetPeriod fbsBudgetPeriod = fbsBudgetPeriodService.get(id);
        fbsBudgetPeriodService.deleteEntity(fbsBudgetPeriod);
        return success();
    }

    /**
     * 批量删除FBS_BUDGET_PERIOD
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除FBS_BUDGET_PERIOD", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            FbsBudgetPeriod fbsBudgetPeriod = fbsBudgetPeriodService.get(id);
            fbsBudgetPeriodService.deleteEntity(fbsBudgetPeriod);
        }
        return success();
    }

    /**
     * 得到可选的年份
     *
     * @return
     */
    @GetMapping("/getChooseYear")
    @ResponseBody
    public AjaxJson<List<Map<String, Object>>> getChooseYear() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        int defaultYear = DateUtils.getYear();
        int startYear = defaultYear - 2;
        for (int i = 0; i < 13; i++, startYear++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("year", startYear);
            list.add(map);
        }
        return AjaxJson.success(list);
    }

    /**
     * 得到表格的值
     *
     * @return
     */
    @PostMapping("/getBtTable")
    @ResponseBody
    public AjaxJson<List<FbsBudgetPeriodVO>> getBtTable(String year) {
        return AjaxJson.success(fbsBudgetPeriodService.getBtTable(year));
    }

    /**
     * 自动计算当年的周期自然周
     *
     * @param year
     * @return
     */
    @PostMapping("/autoAenerate")
    @ResponseBody
    public  AjaxJson<List<FbsBudgetPeriodVO> > autoAenerate(String year) {
        return AjaxJson.success(fbsBudgetPeriodService.autoAenerate(year));
    }

    /**
     * 自动计算当年的周期标准周
     *
     * @param year
     * @return
     */
    @PostMapping("/autoStandard")
    @ResponseBody
    public AjaxJson<List<FbsBudgetPeriodVO>> autoStandard(String year) {
        return AjaxJson.success(fbsBudgetPeriodService.autoStandard(year));
    }



    /**
     * 得到时间段的工作日
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    @PostMapping("/getWorkDays")
    @ResponseBody
    public AjaxJson<Integer> getWorkDays(String beginDate, String endDate) {
        return AjaxJson.success(fbsBudgetPeriodService.getWorkDays(beginDate, endDate));
    }
}
