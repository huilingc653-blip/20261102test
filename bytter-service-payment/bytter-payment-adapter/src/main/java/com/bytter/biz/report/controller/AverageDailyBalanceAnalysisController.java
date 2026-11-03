package com.bytter.biz.report.controller;
import com.bytter.biz.report.service.AverageDailyBalanceAnalysisService;
import com.bytter.biz.report.vo.AverageDailyBalanceAnalysisChartVO;
import com.bytter.biz.report.vo.AverageDailyBalanceAnalysisConditionVO;
import com.bytter.biz.report.vo.AverageDailyBalanceAnalysisVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 分析系统当前存款余额与上月的差异、与年初的差异。
 *
 * @author qinl
 * @description 存款余额对比分析
 * @date 2020.02.12
 */
@Controller
@RequestMapping("/report/deposit/average/balance")
public class AverageDailyBalanceAnalysisController extends BaseController {
    @Autowired
    private AverageDailyBalanceAnalysisService averageDailyBalanceAnalysisService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IBtCurrencyService currencyService;
    @DubboReference(check = false)
	private IBankTypeService bankTypeService;
    /**
     * 报表查询页面
     * @param model
     * @return
     */
    //////@RequiresPermissions("deposit:average:balance:analysis:query")
    @GetMapping("/view")
    public String view(Model model) {
        return "report/averageDailyBalanceAnalysisReport";
    }
    /**
     * 获取列表数据
     * @param dataTablesInput
     * @return
     */
    //////@RequiresPermissions("deposit:average:balance:analysis:query")
    @PostMapping("/list")
    @ResponseBody
    public DataTablesOutput<AverageDailyBalanceAnalysisVO> list(@RequestBody DataTablesInput dataTablesInput) {
        return averageDailyBalanceAnalysisService.entrance(dataTablesInput);
    }
    /* echart 独立模块    begin*/
    /**
     * echart图表弹窗页面
     * @return
     */
    @GetMapping("/viewChartType")
    public String viewChartType(AverageDailyBalanceAnalysisConditionVO vo,Model model) {
        model.addAttribute("vo",vo);
        return "report/averageDailyBalanceAnalysisReportChartType";
    }
    @GetMapping("/viewChartAvg")
    public String viewChartAvg(AverageDailyBalanceAnalysisConditionVO vo, String average,Model model) {
        model.addAttribute("vo",vo);
        model.addAttribute("average",average);
        return "report/averageDailyBalanceAnalysisReportChartAvg";
    }
    @PostMapping("/chatForCorp")
    @ResponseBody
    public DataTablesOutput<AverageDailyBalanceAnalysisChartVO> chatForCorp(@RequestBody DataTablesInput dataTablesInput) {
        String corpName = (String)dataTablesInput.getFormdata().get("corpName");
        String statistical = (String) dataTablesInput.getFormdata().get("statistical");//维度统计参数
        if(CommonConstant.NO.equals(statistical)) {
            CorpDTO corpIdByName = corpService.get(corpName);
            dataTablesInput.getFormdata().put("corpName", corpIdByName.getName());
        }else{
            BankTypeDTO bankType = bankTypeService.getObjectById(corpName);
            dataTablesInput.getFormdata().put("corpName", bankType!=null ? bankType.getTypeName():"");
        }
        return averageDailyBalanceAnalysisService.echartDataTable(dataTablesInput);
    }
    @GetMapping("/chatAvgData")
    @ResponseBody
    public List<AverageDailyBalanceAnalysisChartVO> chatData(AverageDailyBalanceAnalysisConditionVO vo) {
        DataTablesInput input = new DataTablesInput();
        input.getFormdata().put("beforeDate",vo.getBeforeDate());
        input.getFormdata().put("afterDate",vo.getAfterDate());
        input.getFormdata().put("curId",vo.getCurId());
        input.getFormdata().put("depositType",vo.getDepositType());
        input.getFormdata().put("frequency",vo.getFrequency());
        input.getFormdata().put("isConverted",vo.getIsConverted());
        input.getFormdata().put("moneyUnit",vo.getMoneyUnit());
        input.getFormdata().put("statistical",vo.getStatistical());
        input.getFormdata().put("reducedCurId",vo.getReducedCurId());
        input.getFormdata().put("corpIds",vo.getCorpIds());
        if(CommonConstant.NO.equals(vo.getStatistical())) {
            CorpDTO corpIdByName = corpService.get(vo.getCorpName());
            input.getFormdata().put("corpName", corpIdByName.getName());
        }else{
            BankTypeDTO bankType = bankTypeService.getObjectById(vo.getCorpName());
            input.getFormdata().put("corpName", bankType!=null ? bankType.getTypeName():"");
        }
        input.getFormdata().put("corpCurId",vo.getCorpCurId());
        if(CommonConstant.YES.equals(vo.getIsConverted())){
            StringBuilder sb = new StringBuilder();
            List<BtCurrencyDTO> btCurrency = currencyService.getBtCurrency();
            for(BtCurrencyDTO dto:btCurrency){
                sb.append(dto.getId()).append(",");
            }
            String s = sb.toString();
            String substring = s.substring(0, s.length() - 2);
            input.getFormdata().put("curId",substring);
        }
        return averageDailyBalanceAnalysisService.echartList(input);
    }
    /* echart 独立模块    end*/
}
