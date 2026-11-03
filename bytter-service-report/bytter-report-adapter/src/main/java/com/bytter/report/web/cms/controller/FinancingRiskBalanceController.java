package com.bytter.report.web.cms.controller;
import com.bytter.report.domain.cms.service.FinancingRiskBalanceService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
/**
 * 融资风险汇总表
 *
 * @author zhenghj 2020-12-8
 */
@Controller
@RequestMapping("/bi/cms/financingRiskBalance")
@Slf4j
public class FinancingRiskBalanceController extends BaseController {
    @Autowired
    public FinancingRiskBalanceService financingRiskBalanceService;
    @GetMapping("/listFinancingRisk")
    public String listFinancingRisk(Model model) {
        return "cms/listFinancingRiskBalance";
    }
    /**
     * 融资风险汇总查询列表页面
     *
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        try {
            return this.financingRiskBalanceService.getDataTablesOutput(dataTablesInput, this.getCurrentUser());
        } catch (Exception exception) {
            log.error("获取[融资风险汇总表]数据-异常：" + exception.getMessage(), exception);
            return new DataTablesOutput(dataTablesInput.getDraw(), Collections.emptyList(), 0, 0);
        }
    }
}
