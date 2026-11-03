package com.bytter.biz.budget.saas.controller.budget;

import com.bytter.biz.budget.dao.FbsCollectCorpDao;
import com.bytter.biz.budget.service.FbsBudgetPeriodService;
import com.bytter.biz.budget.service.FbsItemService;
import com.bytter.biz.budget.utils.BudgetUtils;
import com.bytter.biz.budget.vo.FbsBudgetDtlAbsVO;
import com.bytter.biz.budget.vo.FbsBudgetPeriodVO;
import com.bytter.biz.budget.vo.FbsCollectCorpVO;
import com.bytter.biz.budget.vo.MergeQueryByNetIdVO;
import com.bytter.biz.report.service.FbsBudgetDownloadReportService;
import com.bytter.biz.report.service.FbsBudgetReportService;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/**
 * 资金计划上报报表
 *
 * @author zuoy 2018-02-06
 */
@Api(value = "/v1-plan/budget/fbsbudgetreport", tags = "资金计划上报报表")
@RestController("v1FbsBudgetReportController")
@RequestMapping("/v1-plan/budget/fbsbudgetreport")
@Slf4j
public class FbsBudgetReportController extends BaseController {
    @Autowired
    private FbsBudgetReportService fbsBudgetReportService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired(required = false)
    private FbsBudgetDownloadReportService fbsBudgetDownloadReportService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @Autowired(required = false)
    private FbsItemService fbsItemService;
    @DubboReference(check = false)
    private IDictService dictService;
    @Autowired(required = false)
    private FbsBudgetPeriodService fbsBudgetPeriodService;
    @Autowired(required = false)
    private FbsCollectCorpDao fbsCollectCorpDao;


    /**
     * 资金计划上报主表列表 页面跳转
     *
     * @return
     */
    @GetMapping("/mergeQuery")
    public AjaxJson<Map<String, Object>> mergeQuery() {
        Map<String, Object> model = new HashMap<>();
        //model.addAttribute("corpDate", this.fbsBudgetReportService.listEffectiveCorp(this.getCurrentUser()));
        //获取科目最大级别
        Integer maxLevel = this.fbsItemService.getItemMaxLevel();
        String maxLevelShow = "0";
        for (int i = 1; i <= maxLevel; i++) {
            maxLevelShow += CommonConstant.COMMA_SPLIT + i;
        }
        maxLevelShow += CommonConstant.COMMA_SPLIT + "-1";
        model.put("maxLevelShow", maxLevelShow);
        return AjaxJson.success(model);
    }

    /**
     * 资金计划上报主表列表 页面跳转
     *
     * @return
     */
    @RequestMapping("/mergeQueryByNetId")
    public AjaxJson<MergeQueryByNetIdVO> mergeQueryByNetId(String netId, String timeType, String beginDate, String showItem, String showMoney) {
        MergeQueryByNetIdVO model = new MergeQueryByNetIdVO();
        //获取有效的单位信息
        StopWatch stopWatch = new StopWatch("mergeQueryByNetId");
        stopWatch.start("fbsBudgetReportService.listEffectiveCorp");
        List<CorpDTO> listEffectiveCorp = this.fbsBudgetReportService.listEffectiveCorp(this.getCurrentUser());
        List<CorpDTO> listCorp = new ArrayList<>();
        if (EmptyUtils.isNotEmpty(listEffectiveCorp)) {
            for (CorpDTO corp : listEffectiveCorp) {
                if (corp.getId().equals(netId) || (StringUtils.isNotBlank(corp.getParentIds()) && corp.getParentIds().indexOf(netId) != -1)) {
                    listCorp.add(corp);
                }
            }
        }
        model.setNetId(netId);
        model.setCorpDate(listCorp);
        model.setTopCorp(corpService.get(netId));
        model.setTimeType(timeType);
        model.setBeginDate(beginDate);
        model.setShowItem(showItem);
        model.setShowMoney(showMoney);
        //获取科目最大级别
        Integer maxLevel = this.fbsItemService.getItemMaxLevel();
        String maxLevelShow = "0";
        for (int i = 1; i <= maxLevel; i++) {
            maxLevelShow += CommonConstant.COMMA_SPLIT + i;
        }
        maxLevelShow += CommonConstant.COMMA_SPLIT + "-1";
        model.setMaxLevelShow(maxLevelShow);
        stopWatch.stop();
        log.debug("mergeQueryByNetId all spends :" + stopWatch.prettyPrint());
        return AjaxJson.success(model);
    }

    /**
     * 查询
     *
     * @param timeType       预算类型
     * @param corpId         单位ID
     * @param seachBeginDate 开始日期
     * @param seachEndDate   结束日期
     * @param viewType       查询类型	0-列表 1-比较
     * @return
     */
    @GetMapping("/seachFbsBudget")
    public AjaxJson<MergeQueryByNetIdVO> seachFbsBudget(HttpServletRequest request, String timeType, String corpId, Date seachBeginDate, Date seachEndDate, String viewType) {
        MergeQueryByNetIdVO model = new MergeQueryByNetIdVO();
        Date beginDate = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(seachBeginDate, timeType).getBeginDate();
        Date endDate = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(seachEndDate, timeType).getEndDate();
        model.setTimeType(timeType);
        model.setCorpId(corpId);
        model.setSeachBeginDate(beginDate);
        model.setSeachEndDate(endDate);
        CorpDTO corpDTO = this.corpService.get(corpId);
        model.setCorpName(corpDTO.getName());
        FbsCollectCorpVO fbsCollectCorpVO = fbsCollectCorpDao.listCorpByCorpId(corpId);
        if (!EmptyUtils.isEmpty(fbsCollectCorpVO)) {//判断单位是不是有虚拟本单位
            return this.showFbsBudgetInfoCollect(request, model, timeType, corpDTO, beginDate, endDate);
        }
        if (BudgetConstant.BUDGET_REPORT_COMPARE.equalsIgnoreCase(viewType)) {
            return this.showFbsBudgetInfo(request, model, timeType, corpDTO, beginDate, endDate);
        } else {
            return AjaxJson.success(model);
        }
    }

    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableReport")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTableReport(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        Date beginDate = DateUtils.parseDate(String.valueOf(input.getFormdata().get("beginDate")));
        Date endDate = DateUtils.parseDate(String.valueOf(input.getFormdata().get("endDate")));
        String timeType = String.valueOf(input.getFormdata().get("eqs_timeType"));
        beginDate = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(beginDate, timeType).getBeginDate();
        endDate = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(endDate, timeType).getEndDate();
        input.getFormdata().put("ged_beginDate", DateUtils.format(beginDate, "yyyy-MM-dd"));
        input.getFormdata().put("led_endDate", DateUtils.format(endDate, "yyyy-MM-dd"));
        return AjaxJson.success(fbsBudgetReportService.dataTableReport(input, this.getCurrentUser()));
    }

    /**
     * 根据查询条件显示明细信息
     *
     * @param timeType
     * @param beginDate
     * @param endDate
     * @return
     */
    public AjaxJson<MergeQueryByNetIdVO> showFbsBudgetInfo(HttpServletRequest request, MergeQueryByNetIdVO model, String timeType, CorpDTO corpDTO, Date beginDate, Date endDate) {
        //年计划上报模式:0,年固定;1,年分解
        StopWatch stopWatch = new StopWatch("showFbsBudgetInfo");
        stopWatch.start("before fbsBudgetReportService.getFbsBudget:");
        String yimode = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YIMODE).getValue();
        //月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
        String mimode = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODE).getValue();
        boolean resolveYear = BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equalsIgnoreCase(yimode);
        boolean resolveMonth = BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equalsIgnoreCase(mimode);
        //获取顶级单位/网点的本位币
        CorpDTO topCorp = corpService.getTopCorpByCorpId(corpDTO.getId());
        if (topCorp == null) {
            topCorp = corpDTO;
        }
        BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(topCorp.getCurId());
        model.setBtCurrencyDTO(btCurrencyDTO);
        stopWatch.stop();
        if (resolveYear || resolveMonth) {
            // 年分解 || 月分解周
            Map<String, Object> map = this.fbsBudgetReportService.getFbsBudgetResolve(timeType, corpDTO.getId(), beginDate, endDate);
            if (!EmptyUtils.isEmpty(map)) {
                model.setTimeMap(map.get("timeMap"));
                model.setColSize(map.get("colSize"));
                model.setItemList(map.get("itemList"));
                model.setDtlMap(map.get("dtlMap"));
                if (!map.isEmpty() && !EmptyUtils.isEmpty(map.get("itemList"))) {
                    // 导出功能
                    String filePath = request.getSession().getServletContext().getRealPath("/") + "export" + File.separator + "budget" + File.separator + "计划上报情况表.xls";
                    this.fbsBudgetDownloadReportService.exporFbsBudgetResolve(request, map, filePath, corpDTO.getName(), timeType, beginDate, endDate, btCurrencyDTO.getCurName());
                }
            }
            model.setForwardUrl("report/reportFbsBudgetResolve");
            return AjaxJson.success(model);
        } else {
            stopWatch.start("this.fbsBudgetReportService.getFbsBudget");
            Date newEndDate = endDate;
            // 普通预算报表
            Map<String, Object> map = this.fbsBudgetReportService.getFbsBudget(timeType, corpDTO.getId(), beginDate, newEndDate);
            if (!EmptyUtils.isEmpty(map)) {
                model.setTimeMap(map.get("timeMap"));
                model.setItemList(map.get("itemList"));
                model.setDtlMap(map.get("dtlMap"));
                if (!map.isEmpty() && !EmptyUtils.isEmpty(map.get("itemList"))) {
                    // 导出功能
                    String filePath = request.getSession().getServletContext().getRealPath("/") + "export" + File.separator + "budget" + File.separator + "计划上报情况表.xls";
                    this.fbsBudgetDownloadReportService.exporFbsBudget(request, map, filePath, corpDTO.getName(), timeType, beginDate, endDate, btCurrencyDTO.getCurName());
                }
            }
            stopWatch.stop();
            log.debug("showFbsBudgetInfo all spends :" + stopWatch.prettyPrint());
            model.setForwardUrl("report/reportFbsBudget");
            return AjaxJson.success(model);
        }
    }

    /**
     * 根据查询条件显示明细信息
     *
     * @param timeType
     * @param beginDate
     * @param endDate
     * @return
     */
    public AjaxJson<MergeQueryByNetIdVO> showFbsBudgetInfoCollect(HttpServletRequest request, MergeQueryByNetIdVO model, String timeType, CorpDTO corpDTO, Date beginDate, Date endDate) {
        //获取顶级单位/网点的本位币
        CorpDTO topCorp = corpService.getTopCorpByCorpId(corpDTO.getId());
        if (topCorp == null) {
            topCorp = corpDTO;
        }
        BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(topCorp.getCurId());
        model.setBtCurrencyDTO(btCurrencyDTO);
        Date newEndDate = endDate;
        // 普通预算报表
        Map<String, Object> map = this.fbsBudgetReportService.getFbsBudgetCollect(timeType, corpDTO.getId(), beginDate, newEndDate);
        model.setName(corpDTO.getName());
        model.setCollectName(corpDTO.getName() + "(汇总)");
        if (!EmptyUtils.isEmpty(map)) {
            model.setTimeMap(map.get("timeMap"));
            model.setItemList(map.get("itemList"));
            model.setDtlMap(map.get("dtlMap"));
            if (!map.isEmpty() && !EmptyUtils.isEmpty(map.get("itemList"))) {
                // 导出功能
                String filePath = request.getSession().getServletContext().getRealPath("/") + "export" + File.separator + "budget" + File.separator + "计划上报情况表.xls";
                this.fbsBudgetDownloadReportService.exporFbsBudgetCollect(request, map, filePath, corpDTO.getName(), timeType, beginDate, endDate, btCurrencyDTO.getCurName());
            }
        }
        model.setForwardUrl("report/reportFbsBudgetCollect");
        return AjaxJson.success(model);
    }

    /**
     * 合并预算查询
     *
     * @param model
     * @param timeType  预算类型
     * @param seachDate 查询日期
     * @param showMoney 金额显示方式
     * @param showItem  科目显示方式
     * @return
     */
/*    @GetMapping("/seachMergeFbsBudgetTemp")
    public String seachMergeFbsBudgetTemp(Model model, String timeType, Date seachDate, String showMoney, String showItem) {
        model.addAttribute("timeType", timeType);
        model.addAttribute("seachDate", seachDate);
        model.addAttribute("showMoney", showMoney);
        model.addAttribute("showItem", showItem);
        return "report/reportMergeFbsBudgetTemp";
    }*/

    /**
     * 合并预算查询
     *
     * @param timeType  预算类型
     * @param corpIds   单位集合
     * @param seachDate 查询日期
     * @param showMoney 金额显示方式
     * @param showItem  科目显示方式
     * @return
     */
    @PostMapping("/seachMergeFbsBudgetPost")
    public AjaxJson<MergeQueryByNetIdVO> seachMergeFbsBudgetPost(HttpServletRequest request, String timeType, String corpIds, Date seachDate, String showMoney, String showItem) {
        MergeQueryByNetIdVO data = new MergeQueryByNetIdVO();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("before getMergeFbsBudgetResolve:");
        FbsBudgetPeriodVO fbsBudgetPeriodVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(seachDate, timeType);
        Date beginDate = fbsBudgetPeriodVO.getBeginDate();
        Date endDate = fbsBudgetPeriodVO.getEndDate();
        data.setTimeType(timeType);
        data.setSeachBeginDate(beginDate);
        data.setSeachEndDate(endDate);
        //本位币
        String[] corpIdArr = corpIds.split(CommonConstant.COMMA_SPLIT);
        //如果是一个汇总单位
        if (!EmptyUtils.isEmpty(corpIdArr) && corpIdArr.length == 1) {
            FbsCollectCorpVO fbsCollectCorpVO = fbsCollectCorpDao.listCorpByCorpId(corpIdArr[0]);
            if (!EmptyUtils.isEmpty(fbsCollectCorpVO)) {
                corpIds += "," + fbsCollectCorpVO.getId();
            }
        }
        //获取顶级单位/网点的本位币
        CorpDTO topCorp = corpService.getTopCorpByCorpId(corpIdArr[0]);
        if (topCorp == null) {
            topCorp = corpService.get(corpIdArr[0]);
        }
        BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(topCorp.getCurId());
        data.setCur(btCurrencyDTO);
        Map<String, String> moneyMap = new TreeMap<String, String>(
                new Comparator<String>() {
                    @Override
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        for (String itemType : showMoney.split(CommonConstant.COMMA_SPLIT)) {
            moneyMap.put(itemType, itemType);
            if ("1".equalsIgnoreCase(itemType)) {
                data.setBMoneyShow(true);
            } else if ("2".equalsIgnoreCase(itemType)) {
                data.setBsuperMoneyShow(true);
            } else if ("3".equalsIgnoreCase(itemType)) {
                data.setBtempMoneyShow(true);
            }
        }
        //年计划上报模式:0,年固定;1,年分解
        String yimode = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YIMODE).getValue();
        //月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
        String mimode = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODE).getValue();
        boolean resolveYear = BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equalsIgnoreCase(yimode);
        boolean resolveMonth = BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equalsIgnoreCase(mimode);
        stopWatch.stop();
        if (resolveYear || resolveMonth) {
            // 年分解 || 月分解周
            stopWatch.start("getMergeFbsBudgetResolve and exporMergeFbsBudgetResolve:");
            Map<String, Object> map = this.fbsBudgetReportService.getMergeFbsBudgetResolve(timeType, corpIds, beginDate, endDate, showItem, moneyMap);
            stopWatch.stop();
            stopWatch.start("exporMergeFbsBudgetResolve:");
            if (!EmptyUtils.isEmpty(map)) {
                data.setCorpMap(map.get("corpMap"));
                data.setMaxDateCount(map.get("maxDateCount"));
                data.setItemMap(map.get("itemMap"));
                data.setDtlMap(map.get("dtlMap"));
                data.setMoneyMap(moneyMap);
                data.setCollectMap(map.get("collectMap"));
                if (!map.isEmpty() && !EmptyUtils.isEmpty(map.get("itemMap"))) {
                    // 导出功能
                    String filePath = request.getSession().getServletContext().getRealPath("/") + "export" + File.separator + "budget" + File.separator + "合并计划上报表.xls";
                    this.fbsBudgetDownloadReportService.exporMergeFbsBudgetResolve(request, filePath, map, moneyMap, timeType, beginDate, endDate, btCurrencyDTO);
                }
            }
            stopWatch.stop();
            log.info("seachMergeFbsBudget total spends:" + stopWatch.prettyPrint());
            data.setForwardUrl("report/reportMergeFbsBudgetResolve");
            return AjaxJson.success(data);
        } else {
            // 普通预算报表
            Map<String, Object> map = this.fbsBudgetReportService.getMergFbsBudget(timeType, corpIds, beginDate, endDate, showItem, moneyMap);
            if (!EmptyUtils.isEmpty(map)) {
                data.setCorpMap(map.get("corpMap"));
                data.setTimeMap(map.get("timeMap"));
                data.setMoneyMap(moneyMap);
                data.setItemList(map.get("itemList"));
                data.setDtlMap(map.get("dtlMap"));
                data.setCollectMap(map.get("collectMap"));
                StringBuffer html = new StringBuffer("");
                if (!map.isEmpty() && !EmptyUtils.isEmpty(map.get("itemList"))) {
                    // 导出功能
                    String filePath = request.getSession().getServletContext().getRealPath("/") + "export" + File.separator + "budget" + File.separator + "合并计划上报表.xls";
                    this.fbsBudgetDownloadReportService.exporMergeFbsBudget(request, filePath, map, moneyMap, timeType, beginDate, endDate);
                    List<FbsBudgetDtlAbsVO> itemList = (List<FbsBudgetDtlAbsVO>) map.get("itemList");
                    Map<String, BigDecimal> collectMap = (Map<String, BigDecimal>) map.get("collectMap");
                    Map<String, String> corpMap = (Map<String, String>) map.get("corpMap");
                    Map<String, FbsBudgetDtlAbsVO> dtlMap = (Map<String, FbsBudgetDtlAbsVO>) map.get("dtlMap");
                    for (int i = 0; i < itemList.size(); i++) {
                        FbsBudgetDtlAbsVO vo = itemList.get(i);
                        String superitemCode = "";
                        if (EmptyUtils.isNotEmpty(vo.getItem().getSuperItemCode())) {
                            superitemCode = vo.getItem().getSuperItemCode();
                        }
                        html.append("<tr data-tt-id=\"" + vo.getItemCode() + "\" data-tt-parent-id=\"" + superitemCode + "\"\n" +
                                "        data-tt-subject-sort=\"" + vo.getItemCode() + "\">");
                        html.append("<td>" + (i + 1) + "</td>");
                        html.append("<td data-name=\"itemCode\">" + vo.getItemCode() + "</td>");
                        html.append("<td data-name=\"itemName\">" + vo.getItemName() + "</td>");
                        html.append("<td data-name=\"collectMoney\" class=\"textRight\">" + NumberUtil.formatDouble(collectMap.get(vo.getItemCode())) + "</td>");
                        for (String moneyKey : moneyMap.keySet()) {
                            for (String corpKey : corpMap.keySet()) {
                                for (String dtlKey : dtlMap.keySet()) {
                                    if ((vo.getItemCode() + "!~!" + corpKey).equals(dtlKey)) {
                                        if ("1".equals(moneyKey)) {
                                            html.append("<td data-name=\"bMoney" + corpKey + "\" class=\"textRight\">" + NumberUtil.formatNumeric(dtlMap.get(dtlKey).getbMoney()) + "</td>");
                                        } else if ("2".equals(moneyKey)) {
                                            html.append("<td data-name=\"bsuperMoney" + corpKey + "\" class=\"textRight\">" + NumberUtil.formatNumeric(dtlMap.get(dtlKey).getBsuperMoney()) + "</td>");
                                        } else if ("3".equals(moneyKey)) {
                                            html.append("<td data-name=\"btempMoney" + corpKey + "\" class=\"textRight\">" + NumberUtil.formatNumeric(dtlMap.get(dtlKey).getBtempMoney()) + "</td>");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                data.setHtml(html.toString());
            }
            data.setForwardUrl("report/reportMergeFbsBudgetResolve");
            return AjaxJson.success(data);
        }
    }

    /**
     * 合并预算查询
     *
     * @param model
     * @param timeType  预算类型
     * @param corpIds   单位集合
     * @param seachDate 查询日期
     * @param showMoney 金额显示方式
     * @param showItem  科目显示方式
     * @return
     */
    @GetMapping("/seachMergeFbsBudget")
    public AjaxJson<MergeQueryByNetIdVO> seachMergeFbsBudget(HttpServletRequest request, MergeQueryByNetIdVO model, String timeType, String corpIds, Date seachDate, String showMoney, String showItem) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("before getMergeFbsBudgetResolve:");
        FbsBudgetPeriodVO fbsBudgetPeriodVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(seachDate, timeType);
        Date beginDate = fbsBudgetPeriodVO.getBeginDate();
        Date endDate = fbsBudgetPeriodVO.getEndDate();
        model.setTimeType(timeType);
        model.setSeachBeginDate(beginDate);
        model.setSeachEndDate(endDate);
        //本位币
        String[] corpIdArr = corpIds.split(CommonConstant.COMMA_SPLIT);
        //获取顶级单位/网点的本位币
        CorpDTO topCorp = corpService.getTopCorpByCorpId(corpIdArr[0]);
        if (topCorp == null) {
            topCorp = corpService.get(corpIdArr[0]);
        }
        BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(topCorp.getCurId());
        model.setCur(btCurrencyDTO);
        Map<String, String> moneyMap = new TreeMap<String, String>(
                new Comparator<String>() {
                    @Override
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        for (String itemType : showMoney.split(CommonConstant.COMMA_SPLIT)) {
            moneyMap.put(itemType, itemType);
            if ("1".equalsIgnoreCase(itemType)) {
                model.setBMoneyShow(true);
            } else if ("2".equalsIgnoreCase(itemType)) {
                model.setBsuperMoneyShow(true);
            } else if ("3".equalsIgnoreCase(itemType)) {
                model.setBtempMoneyShow(true);
            }
        }
        //年计划上报模式:0,年固定;1,年分解
        String yimode = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YIMODE).getValue();
        //月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
        String mimode = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODE).getValue();
        boolean resolveYear = BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equalsIgnoreCase(yimode);
        boolean resolveMonth = BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equalsIgnoreCase(mimode);
        stopWatch.stop();
        if (resolveYear || resolveMonth) {
            // 年分解 || 月分解周
            stopWatch.start("getMergeFbsBudgetResolve and exporMergeFbsBudgetResolve:");
            Map<String, Object> map = this.fbsBudgetReportService.getMergeFbsBudgetResolve(timeType, corpIds, beginDate, endDate, showItem, moneyMap);
            stopWatch.stop();
            stopWatch.start("exporMergeFbsBudgetResolve:");
            if (!EmptyUtils.isEmpty(map)) {
                model.setCorpMap(map.get("corpMap"));
                model.setMaxDateCount(map.get("maxDateCount"));
                model.setItemMap(map.get("itemMap"));
                model.setDtlMap(map.get("dtlMap"));
                model.setMoneyMap(moneyMap);
                model.setCollectMap(map.get("collectMap"));
                if (!map.isEmpty() && !EmptyUtils.isEmpty(map.get("itemMap"))) {
                    // 导出功能
                    String filePath = request.getSession().getServletContext().getRealPath("/") + "export" + File.separator + "budget" + File.separator + "合并计划上报表.xls";
                    this.fbsBudgetDownloadReportService.exporMergeFbsBudgetResolve(request, filePath, map, moneyMap, timeType, beginDate, endDate, btCurrencyDTO);
                }
            }
            stopWatch.stop();
            log.info("seachMergeFbsBudget total spends:" + stopWatch.prettyPrint());
            model.setForwardUrl("report/reportMergeFbsBudgetResolve");
            return AjaxJson.success(model);
        } else {
            // 普通预算报表
            Map<String, Object> map = this.fbsBudgetReportService.getMergFbsBudget(timeType, corpIds, beginDate, endDate, showItem, moneyMap);
            if (!EmptyUtils.isEmpty(map)) {
                model.setCorpMap(map.get("corpMap"));
                model.setTimeMap(map.get("timeMap"));
                model.setMoneyMap(moneyMap);
                model.setItemList(map.get("itemList"));
                model.setDtlMap(map.get("dtlMap"));
                model.setCollectMap(map.get("collectMap"));
                if (!map.isEmpty() && !EmptyUtils.isEmpty(map.get("itemList"))) {
                    // 导出功能
                    String filePath = request.getSession().getServletContext().getRealPath("/") + "export" + File.separator + "budget" + File.separator + "合并计划上报表.xls";
                    this.fbsBudgetDownloadReportService.exporMergeFbsBudget(request, filePath, map, moneyMap, timeType, beginDate, endDate);
                }
            }
            model.setForwardUrl("report/reportMergeFbsBudget");
            return AjaxJson.success(model);
        }
    }

    /**
     * 导出预算模板
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping("/exportsBudge")
    public void exportsBudge(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileName = "计划上报情况表.xls";
        String filePath = request.getSession().getServletContext().getRealPath("/") + "export" + File.separator + "budget" + File.separator + fileName;
        this.fbsBudgetDownloadReportService.exports(request, response, fileName, filePath, BudgetConstant.BUDGET_EXPORTS_TYPE_BUDGET);
    }

    /**
     * 导出预算模板
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping("/exportsMergeBudge")
    public void exportsMergeBudge(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileName = "合并计划上报表.xls";
        String filePath = request.getSession().getServletContext().getRealPath("/") + "export" + File.separator + "budget" + File.separator + fileName;
        this.fbsBudgetDownloadReportService.exports(request, response, fileName, filePath, BudgetConstant.BUDGET_EXPORTS_TYPE_BUDGET);
    }

    /**
     * 移动端资金计划上报列表
     *
     * @return
     */
    @PostMapping("/listFbsBudgetForApp")
    public AjaxJson<MergeQueryByNetIdVO> showQuery(String timeType, String corpId,
                                                   Date seachBeginDate, Date seachEndDate, String currentCycle) {
        MergeQueryByNetIdVO model = new MergeQueryByNetIdVO();
        if (EmptyUtils.isEmpty(corpId)) {
            ICurrentUser currentUser = getCurrentUser();
            corpId = currentUser.getId();
        }
        if (EmptyUtils.isEmpty(timeType)) {
            timeType = "1";
        }
        if (EmptyUtils.isEmpty(seachBeginDate)) {
            seachBeginDate = DateUtils.str2Date(DateUtils.date2Str(DateUtils.date_sdf()), DateUtils.date_sdf());
        }
        if (EmptyUtils.isEmpty(seachEndDate)) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
            Date date = cal.getTime();
            seachEndDate = DateUtils.getDateYMD(date);
        }
        Date beginDate = BudgetUtils.getBeginDate(seachBeginDate, timeType);
        Date endDate = BudgetUtils.getEndDate(seachEndDate, timeType);
        model.setTimeType(timeType);
        model.setCorpId(corpId);
        model.setSeachBeginDate(seachBeginDate);
        model.setSeachEndDate(seachEndDate);
        CorpDTO corpDTO = this.corpService.get(corpId);
        model.setCorpName(corpDTO.getName());
        if (!EmptyUtils.isBlank(currentCycle)) {
            model.setCurrent(currentCycle);
        }
        Date newEndDate = endDate;
        // 普通预算报表
        Map<String, Object> map = this.fbsBudgetReportService.getFbsBudget(timeType, corpDTO.getId(), beginDate, newEndDate);
        if (!EmptyUtils.isEmpty(map)) {
            model.setTimeMap(map.get("timeMap"));
            model.setItemList(map.get("itemList"));
            model.setDtlMap(map.get("dtlMap"));
            formatData(model, timeType, map.get("timeMap"));
        }
        return AjaxJson.success(model);
    }

    /**
     * 根据map中的数据组装下拉选择框展示的值
     *
     * @param model
     * @param week
     */
    private void formatData(MergeQueryByNetIdVO model, String timeType, Object week) {
        if (EmptyUtils.isEmpty(week)) {
            return;
        }
        TreeMap weekMap = (TreeMap) week;
        Map weekInMonth = new HashMap();
        for (Object item : weekMap.values()) {
            String[] data = ((String) item).split("~");
            if (BudgetConstant.TIME_TYPE_WEEK.equalsIgnoreCase(timeType)) {
                int weeked = DateUtils.getWeekOfMonth(DateUtils.parse(data[0], "yyyy-MM-dd"));
                String[] dataStr = data[0].split("-");
                String weekStr = dataStr[0] + "年" + dataStr[1] + "月第" + weeked + "周";
                weekInMonth.put(item, weekStr);
            } else {
                if (!EmptyUtils.isEmpty(data[0]) && StringUtils.isNotEmpty(model.getYear())) {
                    String[] tempData = data[0].split("-");
                    model.setYear(tempData[0]);
                }
            }
        }
        model.setWeekInMonth(weekInMonth);
    }

    /**
     * 计划明细表查询
     *
     * @return
     */
/*    @GetMapping("/queryDetail")
    public String queryDetail(Model model) {
        *//**得到表格的列*//*
        List<String> dtlColList = new ArrayList<String>();
        List<String> dtlColValueList = new ArrayList<String>();
        String dtlCol = configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_DTLCOL).getValue();//从参数读到需求显示的列
        dtlCol = dtlCol.replace("[", "").replace("]", "").replaceAll("/\\s+/g", "");
        String[] dtlCols = dtlCol.split(CommonConstant.COMMA_SPLIT);
        for (int i = 0; i < dtlCols.length; i++) {
            dtlColList.add(dictService.get(BudgetConstant.BUDGET_DETAILCOL_DICT, dtlCols[i]));
            dtlColValueList.add(dtlCols[i]);
        }
        return "report/queryFbsBudgetDetail";
    }*/

    /**
     * 加载计划明细列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableDetailReport")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTableDetailReport(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(fbsBudgetReportService.dataTableDetailReport(input, this.getCurrentUser()));
    }
}
