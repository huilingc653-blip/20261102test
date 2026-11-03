package com.bytter.biz.budget.saas.controller.budget;

import com.bytter.biz.budget.entity.FbsBudgetSuperadd;
import com.bytter.biz.budget.entity.FbsBudgetSuperaddDetail;
import com.bytter.biz.budget.service.FbsBudgetService;
import com.bytter.biz.budget.service.FbsBudgetSuperaddDetailService;
import com.bytter.biz.budget.service.FbsBudgetSuperaddService;
import com.bytter.biz.budget.vo.BudgetProcessReportVO;
import com.bytter.biz.budget.vo.FbsBudgetVO;
import com.bytter.biz.budget.vo.FbsCollectCorpVO;
import com.bytter.biz.report.service.FbsBudgetSuperAddReportService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资金计划追加查询
 * add by qijh
 */
@RestController("v1FbsBudgetSuperAddReportController")
@Api(value = "/v1-plan/budget/fbsBudgetSuperAddReport", tags = "资金计划追加查询")
@RequestMapping("/v1-plan/budget/fbsBudgetSuperAddReport")
public class FbsBudgetSuperAddReportController extends BaseController {
    @Autowired
    FbsBudgetSuperAddReportService fbsBudgetSuperAddReportService;
    @Autowired
    FbsBudgetService fbsBudgetService;
    @Autowired
    FbsBudgetSuperaddService fbsBudgetSuperaddService;
    @Autowired
    FbsBudgetSuperaddDetailService fbsBudgetSuperaddDetailService;

    /**
     * 加载计划明细列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTableDetailReport(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(fbsBudgetSuperAddReportService.dataTable(input, this.getCurrentUser()));
    }

    @GetMapping("/view/{id}")
    public AjaxJson<BudgetProcessReportVO> view(@PathVariable("id") String id) {
        BudgetProcessReportVO model = new BudgetProcessReportVO();
        FbsBudgetVO fbsBudget = fbsBudgetService.getBudgetInfo(id, "input");
        List<FbsBudgetSuperadd> list = fbsBudgetSuperaddService.listByBudgetId(id);
        //追加列表
        List<Map<String, BigDecimal>> addList = new ArrayList<>();
        //标题列表
        List<String> titleList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String title = "第" + (i + 1) + "次追加</br>(" + DateUtils.format(list.get(i).getCreateDate(), "yyyy-MM-dd") + ")";
            titleList.add(title);
            List<FbsBudgetSuperaddDetail> addDetailList = fbsBudgetSuperaddDetailService.listBySuperaddId(list.get(i).getId());
            Map<String, BigDecimal> map = new HashMap<>();
            for (FbsBudgetSuperaddDetail addDetail : addDetailList) {
                map.put(addDetail.getItemCode(), addDetail.getBsuperMoney());
            }
            addList.add(map);
        }
        if (EmptyUtils.isEmpty(fbsBudget.getCorpName())) {
            FbsCollectCorpVO fbsCollectCorpVO = this.fbsBudgetSuperAddReportService.listCorpById(fbsBudget.getCorpId());
            if (!EmptyUtils.isEmpty(fbsCollectCorpVO)) {
                fbsBudget.setCorpName(fbsCollectCorpVO.getCorpName());
            }
        }
        model.setFbsBudgetPage(fbsBudget);
        model.setDtlList(fbsBudget.getDtlList());
        model.setAddList(addList);
        model.setTitleList(titleList);
        return AjaxJson.success(model);
    }
}
