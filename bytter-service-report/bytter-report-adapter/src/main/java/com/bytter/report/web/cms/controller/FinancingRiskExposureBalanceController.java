package com.bytter.report.web.cms.controller;
import com.bytter.report.domain.cms.service.FinancingRiskExposureBalanceService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
/**
 * <br>
 * <b>功能：</b> 融资风险敞口余额汇总表  Service <br>
 * <b>作者：</b>chenlw<br>
 * <b>日期：</b> 2020-12-8 <br>
 */
@Controller
@RequestMapping("/bi/cms/report/financingRiskExposureBalance")
@Slf4j
public class FinancingRiskExposureBalanceController extends BaseController {
    @Autowired
    private FinancingRiskExposureBalanceService financingRiskExposureBalanceService;
    @GetMapping("/list")
        public String list(Model model) {
        // 默认为系统当前日期
        model.addAttribute("queryDate", DateUtils.getDate());
        return "cms/financingRiskExposureBalance/listFinancingRiskExposureBalance";
    }
        @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        try {
            return financingRiskExposureBalanceService.getDataTablesOutput(input, getCurrentUser());
        } catch (Exception exception) {
            log.error("获取[融资风险敞口余额汇总表]数据-异常：" + exception.getMessage(), exception);
            return new DataTablesOutput(input.getDraw(), Collections.emptyList(), 0, 0);
        }
//        List<FinancingRiskExposureBalanceVO> mockDataList = new ArrayList<>();
//        for (int i = 1; i <= 30; i++) {
//            FinancingRiskExposureBalanceVO data = new FinancingRiskExposureBalanceVO();
//            data.setCorpName("CorpName" + i);
//            data.setFlowLoanRiskExpBal(BigDecimal.ZERO);
//            data.setBillRiskExpBal(BigDecimal.ZERO);
//            data.setBondRiskExpBal(BigDecimal.ZERO);
//            data.setRiskExpSum(BigDecimal.ZERO);
//            data.setNetInterestExpenseSum(BigDecimal.ZERO);
//            data.setChainIncrease(BigDecimal.ZERO);
//            data.setIncreaseComparedLastYearEnd(BigDecimal.ZERO);
//            data.setChainIncreaseRatioComparedLastYear(new BigDecimal("0.123"));
//            data.setIncreaseRatioComparedLastYearEnd(new BigDecimal("0.123"));
//            mockDataList.add(data);
//        }
//        DataTablesOutput output = new DataTablesOutput();
//        output.setData(mockDataList);
//        output.setDraw(input.getDraw());
//        output.setRecordsTotal(mockDataList.size());
//        output.setRecordsFiltered(mockDataList.size());
//        return output;
    }
}
