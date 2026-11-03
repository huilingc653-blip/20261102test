package com.bytter.biz.report.controller;
import com.bytter.biz.budget.entity.FbsBudgetSuperadd;
import com.bytter.biz.budget.entity.FbsBudgetSuperaddDetail;
import com.bytter.biz.budget.service.FbsBudgetService;
import com.bytter.biz.budget.service.FbsBudgetSuperaddDetailService;
import com.bytter.biz.budget.service.FbsBudgetSuperaddService;
import com.bytter.biz.budget.vo.FbsBudgetVO;
import com.bytter.biz.budget.vo.FbsCollectCorpVO;
import com.bytter.biz.report.service.FbsBudgetSuperAddReportService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@Controller
@RequestMapping("/budget/fbsBudgetSuperAddReport")
public class FbsBudgetSuperAddReportController extends BaseController {
    @Autowired
    FbsBudgetSuperAddReportService fbsBudgetSuperAddReportService;
    @Autowired
    FbsBudgetService fbsBudgetService;
    @Autowired
    FbsBudgetSuperaddService fbsBudgetSuperaddService;
    @Autowired
    FbsBudgetSuperaddDetailService fbsBudgetSuperaddDetailService;
    @GetMapping("/query")
    public String query() {
        return "report/querySuperAddFbsBudget";
    }
    /**
     * 加载计划明细列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTable")
    @ResponseBody
    public DataTablesOutput dataTableDetailReport(@RequestBody DataTablesInput dataTablesInput) {
        return fbsBudgetSuperAddReportService.dataTable(dataTablesInput, this.getCurrentUser());
    }
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        FbsBudgetVO fbsBudget = fbsBudgetService.getBudgetInfo(id, "input");
        List<FbsBudgetSuperadd> list = fbsBudgetSuperaddService.listByBudgetId(id);
        //追加列表
        List<Map<String, BigDecimal>> addList = new ArrayList<>();
        //标题列表
        List<String> titleList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            String title = "第"+(i+1)+"次追加</br>("+ DateUtils.format(list.get(i).getCreateDate(),"yyyy-MM-dd")+")";
            titleList.add(title);
            List<FbsBudgetSuperaddDetail> addDetailList = fbsBudgetSuperaddDetailService.listBySuperaddId(list.get(i).getId());
            Map<String, BigDecimal> map = new HashMap<>();
            for(FbsBudgetSuperaddDetail addDetail : addDetailList){
                map.put(addDetail.getItemCode(),addDetail.getBsuperMoney());
            }
            addList.add(map);
        }
        if(EmptyUtils.isEmpty(fbsBudget.getCorpName())){
            FbsCollectCorpVO fbsCollectCorpVO = this.fbsBudgetSuperAddReportService.listCorpById(fbsBudget.getCorpId());
            if(!EmptyUtils.isEmpty(fbsCollectCorpVO)) {
                fbsBudget.setCorpName(fbsCollectCorpVO.getCorpName());
            }
        }
        model.addAttribute("fbsBudgetPage", fbsBudget);
        model.addAttribute("dltList", fbsBudget.getDtlList());
        model.addAttribute("addList", addList);
        model.addAttribute("titleList", titleList);
        return "report/viewSuperAddFbsBudget";
    }
}
