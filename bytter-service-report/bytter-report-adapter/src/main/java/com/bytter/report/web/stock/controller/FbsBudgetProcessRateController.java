package com.bytter.report.web.stock.controller;
import com.bytter.biz.budget.spi.service.IFbsBudgetService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.report.domain.stock.service.FbsBudgetProcessRateService;
import com.bytter.report.domain.stock.vo.FbsBudgetProcessRateQueryVO;
import com.bytter.report.domain.stock.vo.FbsBudgetProcessRateVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
/**
 * @Title: Controller
 * @Description: 计划执行进度表
 * @author huangx
 * @date 2018-12-1 14:51:48
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bi/budget")
public class FbsBudgetProcessRateController extends BaseController {
    @Autowired
    private FbsBudgetProcessRateService fbsBudgetProcessRateService;
    @DubboReference(check = false)
	private IFbsBudgetService fbsBudgetService;
    @DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    /**
     * 计划执行进度表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "计划执行进度表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/processRate")
    public String processRate(Model model) throws Exception {
        //获取启用的周期类型
        String itemTypes = "";
        for(int i=1; i<6; i++) {
            if (fbsBudgetService.isExistTimeType(i+"")) {
                itemTypes = itemTypes + i + ",";
            }
        }
        model.addAttribute("itemTypes", itemTypes);
        FbsBudgetProcessRateQueryVO queryVO = new FbsBudgetProcessRateQueryVO();
        queryVO.setTimeType(BudgetConstant.TIME_TYPE_MONTH);
        Date preMonth = DateUtils.addMonth(DateUtils.getDateYMD(), -1);
        queryVO.setBeginDate(DateUtils.getFirstMonthDate(preMonth));
        queryVO.setEndDate(DateUtils.getLastMonthDate(preMonth));
        List<FbsBudgetProcessRateVO> dtlList = this.getBudgetProcessRateList(queryVO);
        model.addAttribute("dtlList", dtlList);
        model.addAttribute("queryVO", queryVO);
        return "budget/listFbsBudgetProcessRate";
    }
    /**
     * 计划执行进度表列表 页面跳转
     *
     * @return
     */
        @LogMessage(content = "计划执行进度表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/processRateQuery")
    public String processRateQuery(FbsBudgetProcessRateQueryVO queryVO, Model model) throws Exception {
        //获取启用的周期类型
        String itemTypes = "";
        for(int i=1; i<6; i++) {
            if (fbsBudgetService.isExistTimeType(i+"")) {
                itemTypes = itemTypes + i + ",";
            }
        }
        List<FbsBudgetProcessRateVO> dtlList = this.getBudgetProcessRateList(queryVO);
        model.addAttribute("itemTypes", itemTypes);
        model.addAttribute("dtlList", dtlList);
        model.addAttribute("queryVO", queryVO);
        return "budget/listFbsBudgetProcessRate";
    }
    /**
     * 数据封装
     * @param queryVO
     * @return
     */
    private List<FbsBudgetProcessRateVO> getBudgetProcessRateList(FbsBudgetProcessRateQueryVO queryVO){
        List<FbsBudgetProcessRateVO> dtlList = fbsBudgetProcessRateService.queryBudgetProcessRate(queryVO.getCorpId(), queryVO.getTimeType(), queryVO.getBeginDate(), queryVO.getEndDate(), this.getCurrentUser());
        for(FbsBudgetProcessRateVO dtl : dtlList){
            if(BudgetConstant.TIME_TYPE_YEAR.equals(queryVO.getTimeType())) {//年计划
                dtl.setTimeTypeShow(DateUtils.formatDate(dtl.getBeginDate(), "yyyy年"));
            }else if(BudgetConstant.TIME_TYPE_QUARTER.equals(queryVO.getTimeType())) {//季计划
                dtl.setTimeTypeShow(DateUtils.formatDate(dtl.getBeginDate(), "yyyy年")+DateUtils.getQuarterOfYear(dtl.getBeginDate())+"季度");
            }else if(BudgetConstant.TIME_TYPE_MONTH.equals(queryVO.getTimeType())) {//月计划
                dtl.setTimeTypeShow(DateUtils.formatDate(dtl.getBeginDate(), "yyyy年MM月"));
            }else if(BudgetConstant.TIME_TYPE_WEEK.equals(queryVO.getTimeType())) {//周计划
                dtl.setTimeTypeShow(DateUtils.formatDate(dtl.getBeginDate(), "yyyy年MM月"+"第"+DateUtils.getWeekOfMonth(dtl.getBeginDate())+"周"));
            }else if(BudgetConstant.TIME_TYPE_DAY.equals(queryVO.getTimeType())) {//天计划
                dtl.setTimeTypeShow(DateUtils.formatDate(dtl.getBeginDate(), "yyyy年MM月dd日"));
            }
            if(dtl.getbMoney() == null){
                dtl.setbMoney(BigDecimal.ZERO);
            }
            if(dtl.getProcessMoney() == null){
                dtl.setProcessMoney(BigDecimal.ZERO);
            }
            if(dtl.getbMoney().compareTo(BigDecimal.ZERO)>0){
                dtl.setProcessRatio(dtl.getProcessMoney().divide(dtl.getbMoney(),6, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
            }else{
                dtl.setProcessRatio(BigDecimal.ZERO);
            }
            CorpDTO corpDTO= corpService.get(dtl.getCorpId());
            dtl.setCorpCode(corpDTO.getCode());
            dtl.setCorpName(corpDTO.getName());
            BtCurrencyDTO btCurrencyDTO = currencyService.getObjectById(dtl.getCurId());
            dtl.setCurName(btCurrencyDTO.getCurName());
        }
        return dtlList;
    }
    /**
     * 计划执行进度表详情页
     *
     * @return
     */
    @LogMessage(content = "计划执行进度表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/processRateDtlQuery/{id}/{curId}/{timeType}/{beginDate}/{endDate}")
    public String processRateDtlQuery(@PathVariable("id") String id, @PathVariable("curId") String curId, @PathVariable("timeType") String timeType, @PathVariable("beginDate") String beginDate,
                                      @PathVariable("endDate") String endDate, Model model) {
        List<FbsBudgetProcessRateVO> dtlList = fbsBudgetProcessRateService.queryBudgetProcessRateDtl(id, timeType, DateUtils.parseDate(beginDate), DateUtils.parseDate(endDate));
        for(FbsBudgetProcessRateVO dtl : dtlList){
            if(dtl.getbMoney() == null){
                dtl.setbMoney(BigDecimal.ZERO);
            }
            if(dtl.getProcessMoney() == null){
                dtl.setProcessMoney(BigDecimal.ZERO);
            }
            if(dtl.getbMoney().compareTo(BigDecimal.ZERO)>0){
                dtl.setProcessRatio(dtl.getProcessMoney().divide(dtl.getbMoney(),6, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
            }else{
                dtl.setProcessRatio(BigDecimal.ZERO);
            }
        }
        model.addAttribute("dtlList", dtlList);
        model.addAttribute("id", id);
        model.addAttribute("timeType", timeType);
        model.addAttribute("beginDate", beginDate);
        model.addAttribute("endDate", endDate);
        //获取币别
        BtCurrencyDTO currencyDTO = btCurrencyService.getObjectById(curId);
        model.addAttribute("cur", currencyDTO);
        return "budget/viewFbsBudgetProcessRateDtl";
    }
    /**
     * 创建计划执行进度表（柱状图）
     *
     * @return
     * @throws IOException
     */
    @PostMapping("/createFbsBudgetProcessRateBar")
    @ResponseBody
    public Object createFbsBudgetProcessRateBar(FbsBudgetProcessRateVO fbsBudgetProcessRateVO) throws IOException {
        return this.fbsBudgetProcessRateService.createFbsBudgetProcessRateBar(fbsBudgetProcessRateVO, this.getCurrentUser());
    }
}
