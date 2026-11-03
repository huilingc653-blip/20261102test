package com.bytter.biz.budget.controller;
import com.bytter.biz.budget.entity.FbsBudget;
import com.bytter.biz.budget.entity.FbsBudgetDtlAbs;
import com.bytter.biz.budget.entity.FbsBudgetProcess;
import com.bytter.biz.budget.entity.FbsSalesReturnAnalyse;
import com.bytter.biz.budget.service.*;
import com.bytter.biz.budget.service.FbsSalesReturnAnalyseService;
import com.bytter.biz.budget.vo.*;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @Title: Controller
 * @Description: 资金计划上报主表
 * @author onlineGenerator
 * @date 2018-11-09 14:51:48
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/budget/fbsbudgetanalyse")
public class FbsBudgetAnalyseController extends BaseController {
    @Autowired
    private FbsBudgetService fbsBudgetService;
    @Autowired
    private FbsBudgetDtlAbsService fbsBudgetDtlAbsService;
    @Autowired
    private FbsBudgetProcessService fbsBudgetProcessService;
    @DubboReference(check = false)
	private IConfigService configService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Autowired
    private FbsBudgetDetailService fbsBudgetDetailService;
    @Autowired
    private FbsItemService fbsItemService;
    @DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
    @Autowired
    private FbsSalesReturnAnalyseService fbsSalesReturnAnalyseService;
    @DubboReference(check = false)
	private IFcExcrateAdjustService fcExcrateAdjustService;
    @Autowired(required = false)
    private FbsBudgetPeriodService fbsBudgetPeriodService;
    /**
     * 资金计划上报主表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "计划实际差异分析", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/factinput")
    public String factInput(Model model) throws Exception {
        FbsBudgetAnalyseVO fbsBudgetAnalyse = new FbsBudgetAnalyseVO();
        //查询时间默认上个月
        Date queryDate = DateUtils.addMonth(DateUtils.getDateYMD(), -1);
        fbsBudgetAnalyse.setQueryDate(DateUtils.formatDate(queryDate, "yyyy-MM"));
        FbsBudgetVO fbsBudget = new FbsBudgetVO();
        model.addAttribute("fbsBudgetPage", fbsBudget);
        fbsBudgetAnalyse.setTimeType(BudgetConstant.TIME_TYPE_MONTH);//月计划
        model.addAttribute("fbsBudgetAnalyse", fbsBudgetAnalyse);
        model.addAttribute("showFlag", false);
        return "budget/fbsBudgetFactAnalyseInput";
    }
    /**
     * 资金计划上报主表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "计划实际差异分析", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/factquery")
    public String factQuery(FbsBudgetAnalyseVO fbsBudgetAnalyse, Model model) throws Exception {
        FbsBudgetVO fbsBudget = new FbsBudgetVO();
        if (fbsBudgetAnalyse != null && StringUtils.isNotEmpty(fbsBudgetAnalyse.getCorpId())) {
            Date beginDate = DateUtils.parseDate(fbsBudgetAnalyse.getQueryDate() + "-01");
            Date endDate = DateUtils.getLastMonthDate(beginDate);
            FbsBudget budget = fbsBudgetService.getCorpBudget(fbsBudgetAnalyse.getCorpId(), fbsBudgetAnalyse.getTimeType(), beginDate, endDate);
            if (budget != null && budget.getStatus() >= BudgetConstant.STATUE_CHECK) {
                fbsBudget = fbsBudgetService.getBudget(budget.getId());
            }else{
                fbsBudget.setBeginDate(beginDate);
                fbsBudget.setEndDate(endDate);
                fbsBudget.setCorpId(fbsBudget.getCorpId());
                fbsBudget.setCorpName(corpService.get(fbsBudgetAnalyse.getCorpId()).getName());
                fbsBudget.setCurId(btCurrencyService.getNatualCurrency().getId());
            }
            fbsBudget.setDtlList(this.getFbsBudgetDtlAbsList(fbsBudget, fbsBudgetAnalyse, model));
        }
        model.addAttribute("fbsBudgetPage", fbsBudget);
        model.addAttribute("fbsBudgetAnalyse", fbsBudgetAnalyse);
        model.addAttribute("showFlag", true);
        return "budget/fbsBudgetFactAnalyseInput";
    }
    /**
     * 封装
     * @param fbsBudget
     * @return
     */
    private List<FbsBudgetDtlAbsVO> getFbsBudgetDtlAbsList(FbsBudgetVO fbsBudget, FbsBudgetAnalyseVO fbsBudgetAnalyse, Model model){
        String lowerLmit = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_LOWERLMIT).getValue();//计划差异原因分析金额下限
        BigDecimal lowerLmitVal = BigDecimal.ZERO;
        if (StringUtils.isNotEmpty(lowerLmit)) {
            lowerLmitVal = new BigDecimal(lowerLmit);
        }
        List<FbsBudgetDtlAbsVO> dltList = new ArrayList<>();
        BigDecimal incomeTotal = BigDecimal.ZERO;//计划收入总额
        BigDecimal payTotal = BigDecimal.ZERO;//计划支出总额
        BigDecimal factIncomeTotal = BigDecimal.ZERO;//实际收入总额
        BigDecimal factPayTotal = BigDecimal.ZERO;//实际支出总额
        for (FbsBudgetDtlAbsVO dtl : fbsBudget.getDtlList()) {
            dtl.setbMoney(dtl.getbMoney().add(dtl.getBsuperMoney()));//实际金额 = 上报金额 + 已追加金额
            if (dtl.getAnalyseStatus() == null) {
                dtl.setFactMoney(fbsBudgetProcessService.collectAlreadyProcessCurMoney(fbsBudget.getBeginDate(), fbsBudget.getEndDate(), dtl.getItemCode(), fbsBudget.getCorpId()));
                dtl.setAnalyseStatus(BudgetConstant.ANALYSE_STATUE_TEMP);
            } else {
                if(dtl.getFactMoney() == null) {
                    dtl.setFactMoney(BigDecimal.ZERO);
                }
            }
            dtl.setDiffMoney(dtl.getbMoney().subtract(dtl.getFactMoney()));//差异金额 = 计划金额 - 实际金额
            //差异比率=差异金额/计划金额
            if (dtl.getDiffMoney().compareTo(BigDecimal.ZERO) > 0 && dtl.getbMoney().compareTo(BigDecimal.ZERO) > 0) {
                dtl.setDiffRatio(dtl.getDiffMoney().divide(dtl.getbMoney(), 6, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
            } else {
                dtl.setDiffRatio(BigDecimal.ZERO);
            }
            if (dtl.getDiffMoney().abs().compareTo(lowerLmitVal) >= 0) {
                dtl.setShowDiffReason(true);
            } else {
                dtl.setAnalyseStatus(null);//分析状态设置为空
                dtl.setShowDiffReason(false);
            }
            if (dtl.getItem().getSign() == 1) {
                if (BudgetConstant.ITEM_CODE_200.equals(dtl.getItemCode())) {
                    incomeTotal = incomeTotal.add(dtl.getbMoney());
                    factIncomeTotal = factIncomeTotal.add(dtl.getFactMoney());
                }
            } else {
                if (BudgetConstant.ITEM_CODE_400.equals(dtl.getItemCode())) {
                    payTotal = payTotal.add(dtl.getbMoney());
                    factPayTotal = factPayTotal.add(dtl.getFactMoney());
                }
            }
            //状态过滤
            if(StringUtils.isNotEmpty(fbsBudgetAnalyse.getAnalyseStatus())){
                if(dtl.getAnalyseStatus() == null || fbsBudgetAnalyse.getAnalyseStatus().intValue() != dtl.getAnalyseStatus()){
                    continue;
                }
            }
            dltList.add(dtl);
        }
        model.addAttribute("dltList", dltList);
        model.addAttribute("incomeTotal", incomeTotal);
        model.addAttribute("payTotal", payTotal);
        model.addAttribute("factIncomeTotal", factIncomeTotal);
        model.addAttribute("factPayTotal", factPayTotal);
        return dltList;
    }
    /**
     * 计划实际差异分析提交主表
     *
     * @param fbsBudgetAnalyse
     * @param childIds
     * @return
     */
    @LogMessage(content = "添加资金计划上报主表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/factsave")
    @ResponseBody
    public AjaxJson factSave(FbsBudgetAnalyseVO fbsBudgetAnalyse, @RequestParam("childIds[]") List<String> childIds, @RequestParam("diffReasons[]") List<String> diffReasons,
                             @RequestParam("factMoneys[]") List<String> factMoneys) {
        fbsBudgetService.saveFact(fbsBudgetAnalyse, childIds, diffReasons, factMoneys);
        return success();
    }
    /**
     * 计划实际差异分析复核列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "计划实际差异分析复核", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/factcheck")
    public String factCheck(Model model) throws Exception {
        FbsBudgetAnalyseVO fbsBudgetAnalyse = new FbsBudgetAnalyseVO();
        //查询时间默认上个月
        Date queryDate = DateUtils.addMonth(DateUtils.getDateYMD(), -1);
        fbsBudgetAnalyse.setQueryDate(DateUtils.formatDate(queryDate, "yyyy-MM"));
        fbsBudgetAnalyse.setAnalyseStatus(BudgetConstant.ANALYSE_STATUE_WAIT_CHECK);
        FbsBudgetVO fbsBudget = new FbsBudgetVO();
        model.addAttribute("fbsBudgetPage", fbsBudget);
        fbsBudgetAnalyse.setTimeType(BudgetConstant.TIME_TYPE_MONTH);//月计划
        model.addAttribute("fbsBudgetAnalyse", fbsBudgetAnalyse);
        model.addAttribute("showFlag", false);
        return "budget/fbsBudgetFactAnalyseCheck";
    }
    /**
     * 计划实际差异分析复核列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "计划实际差异分析复核", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/factcheckquery")
    public String factCheckQuery(FbsBudgetAnalyseVO fbsBudgetAnalyse, Model model) throws Exception {
        FbsBudgetVO fbsBudget = new FbsBudgetVO();
        if (fbsBudgetAnalyse != null && StringUtils.isNotEmpty(fbsBudgetAnalyse.getCorpId())) {
            Date beginDate = DateUtils.parseDate(fbsBudgetAnalyse.getQueryDate() + "-01");
            Date endDate = DateUtils.getLastMonthDate(beginDate);
            FbsBudget budget = fbsBudgetService.getCorpBudget(fbsBudgetAnalyse.getCorpId(), fbsBudgetAnalyse.getTimeType(), beginDate, endDate);
            if (budget != null && budget.getStatus() >= BudgetConstant.STATUE_CHECK) {
                fbsBudget = fbsBudgetService.getBudget(budget.getId());
            }else{
                fbsBudget.setBeginDate(beginDate);
                fbsBudget.setEndDate(endDate);
                fbsBudget.setCorpId(fbsBudget.getCorpId());
                fbsBudget.setCorpName(corpService.get(fbsBudgetAnalyse.getCorpId()).getName());
                fbsBudget.setCurId(btCurrencyService.getNatualCurrency().getId());
            }
            fbsBudget.setDtlList(this.getFbsBudgetDtlAbsList(fbsBudget, fbsBudgetAnalyse, model));
        }
        model.addAttribute("fbsBudgetPage", fbsBudget);
        model.addAttribute("fbsBudgetAnalyse", fbsBudgetAnalyse);
        model.addAttribute("showFlag", true);
        return "budget/fbsBudgetFactAnalyseCheck";
    }
    /**
     * 计划实际差异分析提交主表
     *
     * @param fbsBudgetAnalyse
     * @param childIds
     * @return
     */
    @LogMessage(content = "审核计划实际差异分析", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/batchfactcheck")
    @ResponseBody
    public AjaxJson batchFactCheck(FbsBudgetAnalyseVO fbsBudgetAnalyse, @RequestParam("childIds[]") List<String> childIds, @RequestParam("passFlag") String passFlag) {
        fbsBudgetService.saveCheckFact(fbsBudgetAnalyse, childIds, passFlag);
        return success();
    }
    /**
     * 计划实际差异分析提交主表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "审核计划实际差异分析", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/factchecksave/{id}/{timeType}/{detailId}/{passFlag}")
    @ResponseBody
    public AjaxJson factCheck(@PathVariable("id") String id, @PathVariable("timeType") String timeType,
                              @PathVariable("detailId") String detailId, @PathVariable("passFlag") String passFlag) {
        FbsBudgetAnalyseVO fbsBudgetAnalyse = new FbsBudgetAnalyseVO();
        fbsBudgetAnalyse.setId(id);
        fbsBudgetAnalyse.setTimeType(timeType);
        fbsBudgetAnalyse.setAnalyseStatus(BudgetConstant.ANALYSE_STATUE_WAIT_CHECK);//默认待复核
        List<String> childIds = new ArrayList<String>();
        childIds.add(detailId);
        fbsBudgetService.saveCheckFact(fbsBudgetAnalyse, childIds, passFlag);
        return success();
    }
    /**
     * 计划实际差异分析查看
     *
     * @param id
     * @return
     */
    @LogMessage(content = "计划实际差异分析查看", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @GetMapping("/view/{id}/{timeType}/{detailId}")
    public String view(@PathVariable("id") String id, @PathVariable("timeType") String timeType,
                              @PathVariable("detailId") String detailId, Model model) {
        //差异计划明细
        List<FbsBudgetDetailVO> planData = fbsBudgetDetailService.getBtTable(detailId);
        FbsBudgetDtlAbs dtl = this.fbsBudgetDtlAbsService.getById(detailId, timeType);
        BtCurrencyDTO cur = btCurrencyService.getNatualCurrency();
        BigDecimal planTotal = BigDecimal.ZERO;//合计计划总额
        BigDecimal planNatualTotal = BigDecimal.ZERO;//合计折合人民币总额
        BigDecimal factTotal = BigDecimal.ZERO;//合计实际总额
        BigDecimal factNatualPayTotal = BigDecimal.ZERO;//合计实际折合人民币总额
        if(planData.isEmpty()){
            FbsBudgetDetailVO dtlNew = new FbsBudgetDetailVO();
            dtlNew.setItemCode(dtl.getItemCode());
            dtlNew.setItemName(dtl.getItemName());
            dtlNew.setBMoney(dtl.getbMoney().add(dtl.getBsuperMoney()));
            dtlNew.setNatualMoney(dtlNew.getBMoney());
            dtlNew.setItem(fbsItemService.getFbsItemVO(null, dtl.getItemCode()));
            dtlNew.setCurName(cur.getCurName());
            planData.add(dtlNew);
            planTotal = dtlNew.getBMoney();
            planNatualTotal = dtlNew.getBMoney();
        }else{
            for(FbsBudgetDetailVO vo : planData){
                vo.setItem(fbsItemService.getFbsItemVO(null, vo.getItemCode()));
                if(vo.getBMoney()== null){
                    vo.setBMoney(BigDecimal.ZERO);
                }
                if(vo.getBsuperMoney()== null){
                    vo.setBsuperMoney(BigDecimal.ZERO);
                }
                vo.setBMoney(vo.getBMoney().add(vo.getBsuperMoney()));
                planTotal = planTotal.add(vo.getBMoney()).add(vo.getBsuperMoney());
                planNatualTotal = planTotal.add(vo.getNatualMoney());
            }
        }
        //差异实际明细
        FbsBudgetVO budget = fbsBudgetService.getBudget(id);
        List<FbsBudgetProcess> listBudgetProcess = fbsBudgetProcessService.listBudgetAlreadyProcessByCorpId(budget.getCorpId(), budget.getBeginDate(), budget.getEndDate(), dtl.getItemCode());
        List<FbsBudgetProcessVO> factData = new ArrayList<FbsBudgetProcessVO>();
        if(!listBudgetProcess.isEmpty()){
            for (FbsBudgetProcess pro : listBudgetProcess){
                FbsBudgetProcessVO vo = new FbsBudgetProcessVO();
                BeanUtils.copyNotNullProperties(pro,vo);
                vo.setItem(fbsItemService.getFbsItemVO(null, vo.getItemCode()));
                if(StringUtils.isEmpty(pro.getCurId())){
                    vo.setCurName(cur.getCurName());
                }else{
                    vo.setCurName(btCurrencyService.getObjectById(pro.getCurId()).getCurName());
                }
                vo.setProcessNatualMoney(pro.getProcessMoney());
                BigDecimal curExrate = fcExcrateAdjustService.getExchangeRateBy(pro.getCurId(), btCurrencyService.getRMBCurrency().getId(), DateUtils.getDateTime(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
                if (!EmptyUtils.isEmpty(curExrate)) {
                    vo.setProcessNatualMoney(pro.getProcessMoney().multiply(curExrate));
                }
                factData.add(vo);
                factTotal = factTotal.add(vo.getProcessMoney());
                factNatualPayTotal = factNatualPayTotal.add(vo.getProcessNatualMoney());
            }
        }
        model.addAttribute("planData", planData);
        model.addAttribute("factData", factData);
        model.addAttribute("planTotal", planTotal);
        model.addAttribute("planNatualTotal", planNatualTotal);
        model.addAttribute("factTotal", factTotal);
        model.addAttribute("factNatualPayTotal", factNatualPayTotal);
        return "budget/viewFbsBudgetFactAnalyse";
    }
    /**
     * 销售回款差异分析列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "销售回款差异分析", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/salesinput")
    public String salesInput(Model model) throws Exception {
        //查询时间默认上个月
        FbsBudgetAnalyseVO fbsBudgetAnalyse = new FbsBudgetAnalyseVO();
        fbsBudgetAnalyse.setQueryDate(DateUtils.formatDate(DateUtils.getDateYMD(), "yyyy-MM"));
        fbsBudgetAnalyse.setTimeType(BudgetConstant.TIME_TYPE_MONTH);
        model.addAttribute("fbsBudgetAnalyse", fbsBudgetAnalyse);
        model.addAttribute("showFlag", false);
        return "budget/fbsBudgetSalesReturnAnalyseInput";
    }
    /**
     * 销售回款差异分析列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "销售回款差异分析", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/salesquery")
    public String salesQuery(FbsBudgetAnalyseVO fbsBudgetAnalyse, Model model) throws Exception {
        BigDecimal planTotal = BigDecimal.ZERO;//计划总额
        BigDecimal factTotal = BigDecimal.ZERO;//实际总额
        BigDecimal diffTotal = BigDecimal.ZERO;//差异总额
        List<FbsSalesReturnAnalyse> list = fbsSalesReturnAnalyseService.initSalesReturnAnalyse(fbsBudgetAnalyse);
        List<FbsSalesReturnAnalyseVO> dltList = this.getFbsSalesReturnAnalyseList(fbsBudgetAnalyse, list);
        for(FbsSalesReturnAnalyseVO vo : dltList){
            planTotal = planTotal.add(vo.getPlanCashMoney()).add(vo.getPlanBillMoney());
            factTotal = factTotal.add(vo.getFactCashMoney()).add(vo.getFactBillMoney());
            diffTotal = diffTotal.add(vo.getDiffMoney());
        }
        model.addAttribute("dltList", dltList);
        model.addAttribute("corp", corpService.get(fbsBudgetAnalyse.getCorpId()));
        model.addAttribute("fbsBudgetAnalyse", fbsBudgetAnalyse);
        model.addAttribute("showFlag", true);
        model.addAttribute("planTotal", planTotal);
        model.addAttribute("factTotal", factTotal);
        model.addAttribute("diffTotal", diffTotal);
        return "budget/fbsBudgetSalesReturnAnalyseInput";
    }
    /**
     * 封装
     * @param fbsBudgetAnalyse
     * @param list
     * @return
     */
    private  List<FbsSalesReturnAnalyseVO> getFbsSalesReturnAnalyseList(FbsBudgetAnalyseVO fbsBudgetAnalyse, List<FbsSalesReturnAnalyse> list){
        String ratioLimit = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_RATIOLIMIT).getValue();//销售回款原因分析比率限制
        BigDecimal ratioLimitVal = BigDecimal.ZERO;
        if (StringUtils.isNotEmpty(ratioLimit)) {
            ratioLimitVal = new BigDecimal(ratioLimit).multiply(new BigDecimal(100));
        }
        List<FbsSalesReturnAnalyseVO> dltList = new ArrayList<>();
        for(FbsSalesReturnAnalyse sales : list){
            FbsSalesReturnAnalyseVO vo = new FbsSalesReturnAnalyseVO();
            BeanUtils.copyNotNullProperties(sales, vo);
            vo.setDiffCashMoney(vo.getPlanCashMoney().subtract(vo.getFactCashMoney()));
            vo.setDiffBillMoney(vo.getPlanBillMoney().subtract(vo.getFactBillMoney()));
            vo.setDiffMoney(vo.getDiffCashMoney().add(vo.getDiffBillMoney()));
            vo.setDiffRatio(vo.getDiffMoney().divide((vo.getPlanCashMoney().add(vo.getPlanBillMoney())),6, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
            String corpName = corpService.getCorpName(vo.getCorpId());
            vo.setCorpName(corpName);
            if (vo.getDiffRatio().abs().compareTo(ratioLimitVal) >= 0) {
                vo.setShowDiffReason(true);
            } else {
                vo.setAnalyseStatus(null);//分析状态设置为空
                vo.setShowDiffReason(false);
            }
            //差异金额过滤
            if((fbsBudgetAnalyse.getDiffBeginMoney()!=null && fbsBudgetAnalyse.getDiffBeginMoney().compareTo(vo.getDiffMoney())>0)
                    ||(fbsBudgetAnalyse.getDiffEndMoney()!=null && fbsBudgetAnalyse.getDiffEndMoney().compareTo(vo.getDiffMoney())<0)){
                continue;
            }
            //状态过滤
            if(StringUtils.isNotEmpty(fbsBudgetAnalyse.getAnalyseStatus())){
                if(vo.getAnalyseStatus() == null || fbsBudgetAnalyse.getAnalyseStatus().intValue() != vo.getAnalyseStatus()){
                    continue;
                }
            }
            dltList.add(vo);
        }
        return dltList;
    }
    /**
     * 销售回款差异分析提交主表
     *
     * @param fbsBudgetAnalyse
     * @param childIds
     * @return
     */
    @LogMessage(content = "销售回款主表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/salessave")
    @ResponseBody
    public AjaxJson salesSave(FbsBudgetAnalyseVO fbsBudgetAnalyse, @RequestParam("childIds[]") List<String> childIds, @RequestParam("diffReasons[]") List<String> diffReasons) {
        fbsSalesReturnAnalyseService.saveSales(childIds, diffReasons);
        return success();
    }
    /**
     * 销售回款差异分析复核列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "计划实际差异分析复核", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/salescheck")
    public String salesCheck(Model model) throws Exception {
        //查询时间默认上个月
        FbsBudgetAnalyseVO fbsBudgetAnalyse = new FbsBudgetAnalyseVO();
        fbsBudgetAnalyse.setQueryDate(DateUtils.formatDate(DateUtils.getDateYMD(), "yyyy-MM"));
        fbsBudgetAnalyse.setTimeType(BudgetConstant.TIME_TYPE_MONTH);
        fbsBudgetAnalyse.setAnalyseStatus(BudgetConstant.ANALYSE_STATUE_WAIT_CHECK);//默认待复核
        model.addAttribute("fbsBudgetAnalyse", fbsBudgetAnalyse);
        model.addAttribute("showFlag", false);
        return "budget/fbsBudgetSalesReturnAnalyseCheck";
    }
    /**
     * 销售回款差异分析列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "销售回款差异分析", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/salescheckquery")
    public String salesCheckQuery(FbsBudgetAnalyseVO fbsBudgetAnalyse, Model model) throws Exception {
        List<FbsSalesReturnAnalyseVO> dltList = new ArrayList<>();
        BigDecimal planTotal = BigDecimal.ZERO;//计划总额
        BigDecimal factTotal = BigDecimal.ZERO;//实际总额
        BigDecimal diffTotal = BigDecimal.ZERO;//差异总额
        List<FbsSalesReturnAnalyse> list = fbsSalesReturnAnalyseService.initSalesReturnAnalyse(fbsBudgetAnalyse);
        if(list.isEmpty()){
            model.addAttribute("showFlag", false);
        }else{
            dltList = this.getFbsSalesReturnAnalyseList(fbsBudgetAnalyse, list);
            for(FbsSalesReturnAnalyseVO vo : dltList){
                planTotal = planTotal.add(vo.getPlanCashMoney()).add(vo.getPlanBillMoney());
                factTotal = factTotal.add(vo.getFactCashMoney()).add(vo.getFactBillMoney());
                diffTotal = diffTotal.add(vo.getDiffMoney());
            }
            model.addAttribute("showFlag", true);
        }
        model.addAttribute("dltList", dltList);
        model.addAttribute("corp", corpService.get(fbsBudgetAnalyse.getCorpId()));
        model.addAttribute("fbsBudgetAnalyse", fbsBudgetAnalyse);
        model.addAttribute("planTotal", planTotal);
        model.addAttribute("factTotal", factTotal);
        model.addAttribute("diffTotal", diffTotal);
        return "budget/fbsBudgetSalesReturnAnalyseCheck";
    }
    /**
     * 销售回款分析提交主表
     *
     * @param fbsBudgetAnalyse
     * @param childIds
     * @return
     */
    @LogMessage(content = "审核销售回款分析", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/batchsalescheck")
    @ResponseBody
    public AjaxJson batchSalesCheck(FbsBudgetAnalyseVO fbsBudgetAnalyse, @RequestParam("childIds[]") List<String> childIds, @RequestParam("passFlag") String passFlag) {
        fbsSalesReturnAnalyseService.saveCheckSales(childIds, passFlag);
        return success();
    }
    /**
     * 计划实际差异分析提交主表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "审核计划实际差异分析", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/saleschecksave/{id}/{passFlag}")
    @ResponseBody
    public AjaxJson salesCheck(@PathVariable("id") String id, @PathVariable("passFlag") String passFlag) {
        List<String> childIds = new ArrayList<String>();
        childIds.add(id);
        fbsSalesReturnAnalyseService.saveCheckSales(childIds, passFlag);
        return success();
    }
    /**
     * 收/支执行排行表 页面跳转
     * @param cdSign 收支标志
     * @return
     */
    @LogMessage(content = "收/支执行排行表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/analysereport/{cdSign}")
    public String analyseReport(@PathVariable("cdSign") String cdSign, Model model) {
        //获取启用的周期类型
        String itemTypes = "";
        int num = 6;
        for(int i=1; i<num; i++) {
            if (fbsBudgetService.isExistTimeType(i+"")) {
                itemTypes = itemTypes + i + ",";
            }
        }
        FbsBudgetAnalyseVO fbsBudgetAnalyse = new FbsBudgetAnalyseVO();
        fbsBudgetAnalyse.setCdSign(cdSign);
        model.addAttribute("showFlag", false);
        //存在月计划则默认月计划
        if(fbsBudgetService.isExistTimeType(BudgetConstant.TIME_TYPE_MONTH)) {
            fbsBudgetAnalyse.setTimeType(BudgetConstant.TIME_TYPE_MONTH);//默认月计划
            Date queryDate = DateUtils.getFirstMonthDate(DateUtils.addMonth(DateUtils.getDateYMD(), -1));//日期默认当系统当前时间的前一个月
            fbsBudgetAnalyse.setQueryDate(DateUtils.formatDate(queryDate, "yyyy-MM-dd"));
            String itemCode = BudgetConstant.ITEM_CODE_400;//支出
            if("0".equals(fbsBudgetAnalyse.getCdSign())){
                itemCode = BudgetConstant.ITEM_CODE_200;//支出
            }
            Date beginDate = DateUtils.getFirstMonthDate(queryDate);
            Date endDate = DateUtils.getLastMonthDate(beginDate);
            List<FbsBudgetAnalyseResportVO> dltList = fbsSalesReturnAnalyseService.listBudgetByTimeType(fbsBudgetAnalyse.getTimeType(),beginDate,endDate,itemCode);
            model.addAttribute("showFlag", true);
            model.addAttribute("dltList", dltList);
        }
        model.addAttribute("fbsBudgetAnalyse", fbsBudgetAnalyse);
        model.addAttribute("itemTypes", itemTypes);
        return "budget/fbsBudgetAnalyseReport";
    }
    /**
     * 收/支执行排行表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "收/支执行排行表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/analysequery")
    public String analyseQuery(FbsBudgetAnalyseVO fbsBudgetAnalyse, Model model) throws Exception {
        Date beginDate = null;
        Date endDate = null;
        Date queryDate = DateUtils.parseDate(fbsBudgetAnalyse.getQueryDate());
        FbsBudgetPeriodVO fbsBudgetPeriodVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(queryDate,fbsBudgetAnalyse.getTimeType());
        beginDate = fbsBudgetPeriodVO.getBeginDate();
        endDate = fbsBudgetPeriodVO.getEndDate();
        //获取启用的周期类型
        String itemTypes = "";
        int num = 6;
        for(int i=1; i<num; i++) {
            if (fbsBudgetService.isExistTimeType(i+"")) {
                itemTypes = itemTypes + i + ",";
            }
        }
        String itemCode = BudgetConstant.ITEM_CODE_400;//支出
        if("0".equals(fbsBudgetAnalyse.getCdSign())){
            itemCode = BudgetConstant.ITEM_CODE_200;//支出
        }
        List<FbsBudgetAnalyseResportVO> dltList = fbsSalesReturnAnalyseService.listBudgetByTimeType(fbsBudgetAnalyse.getTimeType(),beginDate,endDate,itemCode);
        model.addAttribute("dltList", dltList);
        model.addAttribute("itemTypes", itemTypes);
        model.addAttribute("showFlag", true);
        model.addAttribute("fbsBudgetAnalyse", fbsBudgetAnalyse);//默认月计划
        return "budget/fbsBudgetAnalyseReport";
    }
}
