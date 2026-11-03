package com.bytter.biz.budget.saas.controller.budget;

import com.bytter.biz.budget.entity.FbsBudget;
import com.bytter.biz.budget.entity.FbsBudgetSuperadd;
import com.bytter.biz.budget.service.*;
import com.bytter.biz.budget.spi.constant.FbsMessageConstant;
import com.bytter.biz.budget.spi.dto.FbsBudgetInputCollectDTO;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.budget.vo.*;
import com.bytter.biz.dto.FbsBudgetSaveDTO;
import com.bytter.biz.report.service.BudgetUtilService;
import com.bytter.biz.report.service.FbsBudgetProcessReportService;
import com.bytter.biz.report.vo.FbsBudgetProcessReportVO;
import com.bytter.biz.vo.FbsBudgetInputContentVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.*;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 资金计划上报主表
 * @date 2017-07-14 14:51:48
 */
@Api(value = "/v1-plan/budget/fbsbudget", tags = "资金计划上报主表")
@RestController("V1FbsBudgetController")
@RequestMapping("/v1-plan/budget/fbsbudget")
public class FbsBudgetController extends BaseController {
    @Autowired
    private FbsBudgetService fbsBudgetService;
    @Autowired
    private FbsBudgetDtlAbsService fbsBudgetDtlAbsService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private FbsBudgetResolveYearService fbsBudgetResolveYearService;
    @Autowired
    private FbsBudgetResolveMonthService fbsBudgetResolveMonthService;
    @Autowired
    private FbsBudgetSuperaddService fbsBudgetSuperaddService;
    @Autowired
    private FbsBudgetRollMonthWeekService fbsBudgetRollMonthWeekService;
    @Autowired
    private FbsCorpItemService fbsCorpItemService;
    @Autowired
    private FbsBudgetPeriodService fbsBudgetPeriodService;
    @DubboReference(check = false)
    private IFbsCorpBudgetService fbsCorpBudgetService;
    @Autowired
    private BudgetUtilService budgetUtilService;
    @DubboReference(check = false)
    private IConfigService configService;
    @Autowired
    private FbsBudgetProcessService fbsBudgetProcessService;
    @Autowired
    private FbsBudgetRollQuarterService fbsBudgetRollQuarterService;
    @Autowired
    private FbsBudgetRollWeekService fbsBudgetRollWeekService;
    @Autowired
    private FbsBudgetProcessReportService fbsBudgetProcessReportService;
    @Autowired
    private FbsItemService fbsItemService;
    @Autowired
    private  RedisUtils redisUtils;




    /**
     * 资金计划上报主表列表 页面跳转
     *
     * @param timeType 周期类型
     * @return
     */

    @LogMessage(content = "资金计划上报", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/inputPre/{timeType}")
    public AjaxJson<FbsBudgetInputContentVO> inputPre(@PathVariable("timeType") String timeType, HttpServletRequest request) {
        FbsBudgetInputContentVO vo = new FbsBudgetInputContentVO();
        String layId = null;
        String key = getUserId() + "_" + "layId";
        Object layIdOb = redisUtils.get(key);
        if (!EmptyUtils.isEmpty(layIdOb)) {
            layId = layIdOb.toString();
        }
      //  String layId = (String) request.getSession().getAttribute("layId");
        //判断是否启用对应的周期计划,未启用时给出提示
        if (!fbsBudgetService.isExistTimeType(timeType)) {
            vo.setMsg(BudgetConstant.MESSAGE_TIMETYPE);
            vo.setForwardUrl("budget/inputPreFbsBudgetError");
            return AjaxJson.success(vo);
        }
        //计划周期类型:1-年 2-季 3-月 4-周 5-天
        vo.setTimeType(timeType);
        vo.setShowType(BudgetConstant.SHOW_TYPE_1);
        //周计划整月上报
        String wimodeOneTime = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODEONETIME);
        if (BudgetConstant.TIME_TYPE_WEEK.equals(timeType) && "1".equals(wimodeOneTime)) {
            vo.setShowType(BudgetConstant.SHOW_TYPE_WEEK_1);
            Integer curPeriod = budgetUtilService.getPeriodOfDate(DateUtils.getDate());
            vo.setCurPeriod(curPeriod);
            vo.setMap(fbsBudgetService.listTreeDataWeek(timeType, BudgetConstant.SHOW_TYPE_WEEK_1, this.getCurrentUser()));
            if (StringUtils.isBlank(layId)) {
                layId = curPeriod.toString();
            }
            vo.setLayId(layId);
            vo.setForwardUrl("budget/inputPreFbsBudgetWeek");
            return AjaxJson.success(vo);
        }
        //天计划连续上报
        String dimodeOneTime = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_DIMODEONETIME);
        if (BudgetConstant.TIME_TYPE_DAY.equals(timeType) && "1".equals(dimodeOneTime)) {
            vo.setShowType(BudgetConstant.SHOW_TYPE_1);
            vo.setCurPeriod(Integer.valueOf(CommonConstant.YES));
            vo.setMap(fbsBudgetService.listTreeDataDay(timeType, BudgetConstant.SHOW_TYPE_1, this.getCurrentUser()));
            if (StringUtils.isBlank(layId)) {
                layId = CommonConstant.YES;
            }
            vo.setLayId(layId);
            vo.setForwardUrl("budget/inputPreFbsBudgetDay");
            return AjaxJson.success(vo);
        }

        List<CorpDTO> list = fbsBudgetService.listEffectiveCorpV2(BudgetConstant.SHOW_TYPE_1, this.getCurrentUser());
        vo.setCurDate(fbsBudgetService.listTreeData(timeType, BudgetConstant.SHOW_TYPE_1, 0, "input", list));
        vo.setNextDate(fbsBudgetService.listTreeData(timeType, BudgetConstant.SHOW_TYPE_1, 1, "input", list));
        //默认tab页签
        if (StringUtils.isBlank(layId)) {
            layId = "nextPeriod";//默认下期
        }
        vo.setLayId(layId);
    //    request.getSession().removeAttribute("layId");
        redisUtils.remove(key);
        vo.setForwardUrl("budget/inputPreFbsBudget");
        return AjaxJson.success(vo);
    }

    /**
     * 资金计划上报主表列表 页面跳转
     *
     * @return
     */

    @LogMessage(content = "资金计划上报", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/query")
    public AjaxJson<FbsBudgetInputContentVO> query(@RequestBody FbsBudgetInputPreVO fbsBudgetInputPre, HttpServletRequest request) throws Exception {
        FbsBudgetInputContentVO model = new FbsBudgetInputContentVO();
        model.setLayId(fbsBudgetInputPre.getLayId());
        model.setTimeType(fbsBudgetInputPre.getTimeType());
        model.setShowType(fbsBudgetInputPre.getShowType());
        //判断是否启用对应的周期计划,未启用时给出提示
        if (!fbsBudgetService.isExistTimeType(fbsBudgetInputPre.getTimeType())) {
            model.setMsg(BudgetConstant.MESSAGE_TIMETYPE);
            model.setForwardUrl("budget/inputPreFbsBudgetError");
            return AjaxJson.success(model);
        }
        //周计划整月上报
        String wimodeOneTime = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODEONETIME);
        if (BudgetConstant.TIME_TYPE_WEEK.equals(fbsBudgetInputPre.getTimeType()) && CommonConstant.YES.equals(wimodeOneTime)) {
            model.setCurPeriod(budgetUtilService.getPeriodOfDate(DateUtils.getDate()));
            model.setMap(fbsBudgetService.listTreeDataWeek(fbsBudgetInputPre.getTimeType(), fbsBudgetInputPre.getShowType(), this.getCurrentUser()));
            model.setForwardUrl("budget/inputPreFbsBudgetWeek");
            return AjaxJson.success(model);
        }
        //天计划连续上报
        String dimodeOneTime = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_DIMODEONETIME);
        if (BudgetConstant.TIME_TYPE_DAY.equals(fbsBudgetInputPre.getTimeType()) && CommonConstant.YES.equals(dimodeOneTime)) {
            model.setCurPeriod(Integer.valueOf(fbsBudgetInputPre.getTimeType()));
            model.setMap(fbsBudgetService.listTreeDataDay(fbsBudgetInputPre.getTimeType(), BudgetConstant.SHOW_TYPE_1, this.getCurrentUser()));
            model.setForwardUrl("budget/inputPreFbsBudgetDay");
            return AjaxJson.success(model);
        }
        List<CorpDTO>  list = fbsBudgetService.listEffectiveCorp(BudgetConstant.SHOW_TYPE_1, this.getCurrentUser());//获取管理的单位
        model.setCurDate(fbsBudgetService.listTreeData(fbsBudgetInputPre.getTimeType(), fbsBudgetInputPre.getShowType(), 0, "", list));
        model.setNextDate(fbsBudgetService.listTreeData(fbsBudgetInputPre.getTimeType(), fbsBudgetInputPre.getShowType(), 1, "", list));
        model.setForwardUrl("budget/inputPreFbsBudget");
        return AjaxJson.success(model);
    }

    /**
     * 资金计划上报主表新增页面跳转
     *
     * @return
     */
    @PostMapping("/input")
    public AjaxJson<FbsBudgetInputContentVO> input(@RequestBody FbsBudgetInputPreVO inputPre, HttpServletRequest request) {
        FbsBudgetInputContentVO model = new FbsBudgetInputContentVO();
        String key = getUserId() + "_" + "layId";
        redisUtils.put(key, inputPre.getLayId());
       // request.getSession().setAttribute("layId", inputPre.getLayId());
        //获取单位分配的科目
        List<FbsItemVO> listFbsItem = null;
        if ("1".equals(inputPre.getFictitiousCorp())) {
            FbsCollectCorpVO fbsCollectCorpVO = fbsBudgetService.listCorpById(inputPre.getCorpId());
            listFbsItem = fbsCorpItemService.getAllItemByCorpIdNotSort(fbsCollectCorpVO.getCorpId());
        } else {
            listFbsItem = fbsCorpItemService.getAllItemByCorpIdNotSort(inputPre.getCorpId());
        }
        if (EmptyUtils.isEmptyList(listFbsItem)) {
            model.setMsg(BudgetConstant.MESSAGE_ITEMCODE);
            model.setForwardUrl("budget/inputPreFbsBudgetError");
            return AjaxJson.success(model);
        }
        //判断是否存在父计划，且父计划控制子计划，父计划未上报，则子计划不允许上报
        String parentTimeType = fbsBudgetService.getParentTimeType(inputPre.getTimeType());
        if (!StringUtils.isBlank(parentTimeType)) {
            FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(inputPre.getBeginDate(), parentTimeType);
            //获取单位父周期计划
            FbsBudget budget = fbsBudgetService.getCorpBudget(inputPre.getCorpId(), parentTimeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate());
            //父周期计划未上报或者未编制完成
            if (budget == null || budget.getStatus() < BudgetConstant.STATUE_CHECK) {
                model.setMsg(BudgetConstant.MESSAGE_PARENT_TIMETYPE);
                model.setForwardUrl("budget/inputPreFbsBudgetError");
                return AjaxJson.success(model);
            }
        }
        //新增单位初始化零计划控制
        if (fbsCorpBudgetService.getCollectFlag(inputPre.getCorpId())) {
            //获取上级单位计划是否已上报
            CorpDTO corp = corpService.get(inputPre.getCorpId());
            FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(inputPre.getBeginDate(), inputPre.getTimeType());
            FbsBudget budget = fbsBudgetService.getCorpBudget(corp.getParentId(), inputPre.getTimeType(), fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate());
            if (!EmptyUtils.isEmpty(budget) && budget.getStatus() >= BudgetConstant.STATUE_CHECK) {
                model.setMsg(BudgetConstant.MESSAGE_PARENT_CORP_INIT);

                model.setForwardUrl("budget/inputPreFbsBudgetError");
                return AjaxJson.success(model);
            }
        }
        //默认tab页签
        model.setLayId(inputPre.getLayId());
        FbsBudgetVO fbsBudget = new FbsBudgetVO();
        if (StringUtils.isEmpty(inputPre.getId())) {
            fbsBudget = fbsBudgetService.initBudget(inputPre.getCorpId(), inputPre.getTimeType(), inputPre.getBeginDate(), inputPre.getEndDate(), BudgetConstant.STATUE_TEMP, this.getCurrentUser());
        } else {
            fbsBudget = fbsBudgetService.getBudgetInfo(inputPre.getId(), BudgetConstant.BUDGET_TARGET_INPUT);
        }
        fbsBudget.setBeginDateStr(DateUtils.formatDate(fbsBudget.getBeginDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        fbsBudget.setEndDateStr(DateUtils.formatDate(fbsBudget.getEndDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        //获取上级计划周期
        fbsBudget.setParentTimeType(parentTimeType);
        model.setFbsBudgetPage(fbsBudget);
        model.setDtlList(fbsBudget.getDtlList());
        model.setIsCollect(inputPre.getIsCollect());
        model.setTarget(inputPre.getTarget());
        if (BudgetConstant.BUDGET_TARGET_SUPER.equals(inputPre.getTarget())) {
            model.setBusName(FbsMessageConstant.BUS_NAME_SUPERADD);
        } else {
            model.setBusName(FbsMessageConstant.BUS_NAME_INPUT);
        }
        //年计划上报模式:0,年固定;1,年分解
        String yimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_YIMODE);
        //月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
        String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
        //mRollSubperiod-月滚动模式 滚动分解子周期
        int mRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MROLLSUBPERIOD));
        //季计划上报模式:1,季固定;2,季滚动;
        String qimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODE);
        //周计划上报模式:1,固定;2,周滚动;
        String wimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODE);
        //可上报/可追加金额计算
        //年分解月上报月或月分解周上报周情况下，可上报金额根据参数“分解计划上报余额控制方式”来控制
        if (BudgetConstant.TIME_TYPE_MONTH.equals(fbsBudget.getTimeType()) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode)) {
            fbsBudgetResolveYearService.calculateCanBMoney(fbsBudget, inputPre.getTarget());
        } else if (BudgetConstant.TIME_TYPE_WEEK.equals(fbsBudget.getTimeType()) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode)) {
            fbsBudgetResolveMonthService.calculateCanBMoney(fbsBudget, inputPre.getTarget());
        } else {
            fbsBudgetService.calculateCanBMoney(fbsBudget, inputPre.getTarget());
        }
        //判断单位是否是汇总单位
        if (fbsCorpBudgetService.getChildCollectFlag(inputPre.getCorpId())) {
            List<FbsBudgetInputPreVO> childCorpData = fbsBudgetService.getChildCorpBudget(inputPre.getCorpId(), inputPre.getTimeType(), inputPre.getBeginDate(), inputPre.getEndDate());
            //收支净额
            model.setShowNetAmount(CommonConstant.NO);
            model.setChildCorpData(childCorpData);
            //年计划上报模式:0,年固定;1,年分解
            //汇总时是否允许修改直属下级单位
            String allowEditDirectUderCorp = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_ALLOWEDITDIRECTUDERCORP).getValue();
            model.setAllowEditDirectUderCorp(allowEditDirectUderCorp);
            //汇总时是否允许跨级修改下级单位
            String allowEditUderCorp = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_ALLOWEDITUDERCORP).getValue();
            model.setAllowEditUderCorp(allowEditUderCorp);
            //汇总时是否允许修改上报金额
            String allowEditCollectMoney = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_ALLOWEDITCOLLECTMONEY).getValue();
            model.setAllowEditCollectMoney(allowEditCollectMoney);

            model.setForwardUrl("budget/inputFbsBudgetCollect");
            return AjaxJson.success(model);
        } else {
            //年计划上报+年分解
            if (BudgetConstant.TIME_TYPE_YEAR.equals(inputPre.getTimeType()) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode)) {
                List<FbsBudgetResolveYearVO> resolveList = fbsBudgetResolveYearService.initResolveYearBudget(fbsBudget, this.getCurrentUser());
                model.setResolveList(resolveList);
                if (!EmptyUtils.isEmptyList(resolveList)) {
                    model.setMonths(resolveList.get(0).getbMoneyMap());
                }
                model.setForwardUrl("budget/inputFbsBudgetResolveYear");
                return AjaxJson.success(model);
            }
            //季计划模式控制
            if (BudgetConstant.TIME_TYPE_QUARTER.equals(inputPre.getTimeType())) {
                //季滚动
                if (BudgetConstant.FBS_CONFIG_QIMODE_ROLL.equals(qimode)) {
                    fbsBudgetRollQuarterService.initRollQuarterBudget(fbsBudget, model, inputPre.getFictitiousCorp(), this.getCurrentUser());
                    model.setForwardUrl("budget/inputFbsBudgetRollQuarter");
                    return AjaxJson.success(model);
                }
            }
            //月计划模式控制
            if (BudgetConstant.TIME_TYPE_MONTH.equals(inputPre.getTimeType())) {
                //月分解周为0时只滚动
                if (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING.equals(mimode) || BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING_WEEK.equals(mimode)) {
                    //月计划上报+月滚动
                    model.setMRollSubperiod(mRollSubperiod);
                    fbsBudgetRollMonthWeekService.initRollMonthBudget(fbsBudget, model, inputPre.getFictitiousCorp(), this.getCurrentUser());
                    model.setForwardUrl("budget/inputFbsBudgetRollMonthWeek");
                    return AjaxJson.success(model);
                } else if (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode)) {
                    List<FbsBudgetResolveMonthVO> resolveList = fbsBudgetResolveMonthService.initResolveMonthBudget(fbsBudget, this.getCurrentUser());
                    model.setResolveList(resolveList);
                    if (!EmptyUtils.isEmptyList(resolveList)) {
                        model.setWeeks(resolveList.get(0).getbMoneyMap());
                        model.setWeekNum(resolveList.get(0).getbMoneyMap().size());
                    }
                    //月计划上报+月分解周
                    model.setForwardUrl("budget/inputFbsBudgetResolveMonth");
                    return AjaxJson.success(model);
                }
            }
            //周计划模式控制
            if (BudgetConstant.TIME_TYPE_WEEK.equals(inputPre.getTimeType())) {
                //周滚动
                if (BudgetConstant.FBS_CONFIG_WIMODE_ROLL.equals(wimode)) {
                    fbsBudgetRollWeekService.initRollWeekBudget(fbsBudget, model, inputPre.getFictitiousCorp(), this.getCurrentUser());
                    model.setForwardUrl("budget/inputFbsBudgetRollWeek");
                    return AjaxJson.success(model);
                }
            }

            model.setForwardUrl("budget/inputFbsBudget");
            return AjaxJson.success(model);
        }
    }

    /**
     * 资金计划上报主表新增页面跳转
     *
     * @return
     */
    @GetMapping("/inputDetail/{id}/{superaddId}/{target}")
    public AjaxJson<FbsBudgetInputContentVO> inputDetail(@PathVariable("id") String id, @PathVariable("target") String target,
                                                         @PathVariable("superaddId") String superaddId) {

        FbsBudgetInputContentVO model = new FbsBudgetInputContentVO();
        FbsBudgetVO fbsBudget = fbsBudgetService.getBudget(id);
        model.setFbsBudgetPage(fbsBudget);
        model.setDtlList(fbsBudget.getDtlList());
        model.setBusName(FbsMessageConstant.BUS_NAME_INPUT);
        //汇总时是否允许修改直属下级单位
        String allowEditDirectUderCorp = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_ALLOWEDITDIRECTUDERCORP).getValue();
        model.setAllowEditDirectUderCorp(allowEditDirectUderCorp);
        //汇总时是否允许跨级修改下级单位
        String allowEditUderCorp = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_ALLOWEDITUDERCORP).getValue();
        model.setAllowEditUderCorp(allowEditUderCorp);
        //汇总时是否允许修改上报金额
        String allowEditCollectMoney = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_ALLOWEDITCOLLECTMONEY).getValue();
        model.setAllowEditCollectMoney(allowEditCollectMoney);
        //年计划上报模式:0,年固定;1,年分解
        String yimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_YIMODE);
        //季计划上报模式:1,季固定;2,季滚动;
        String qimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODE);
        //月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
        String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
        //mRollSubperiod-月滚动模式 滚动分解子周期
        int mRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MROLLSUBPERIOD));
        //周计划上报模式:1,固定;2,周滚动;
        String wimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODE);
        //追加
        if (BudgetConstant.BUDGET_TARGET_SUPER.equals(target)) {
            if (StringUtils.isNotEmpty(superaddId) && !"0".equals(superaddId)) {
                fbsBudget.setSuperaddId(superaddId);//追加ID
                fbsBudget = fbsBudgetService.initBudgetSuperadd(fbsBudget, this.getCurrentUser());//获取明细
            }
            model.setBusName(FbsMessageConstant.BUS_NAME_SUPERADD);
            //年计划上报+年分解
            if (BudgetConstant.TIME_TYPE_YEAR.equals(fbsBudget.getTimeType()) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode)) {
                List<FbsBudgetResolveYearVO> resolveList = fbsBudgetResolveYearService.getResolveYearBudgetBySuperadd(fbsBudget, this.getCurrentUser());
                model.setResolveList(resolveList);
                if (!EmptyUtils.isEmptyList(resolveList)) {
                    model.setMonths(resolveList.get(0).getBtempMoneyMap());
                }
                model.setMinMonth(DateUtils.getMonth(DateUtils.getDate()));
                model.setSaveMethod("inputResolveYearSave");
                model.setForwardUrl("budget/inputFbsBudgetResolveYearCollectDetailSuper");
                return AjaxJson.success(model);
            }
            //月计划上报+月分解周
            if (BudgetConstant.TIME_TYPE_MONTH.equals(fbsBudget.getTimeType()) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode)) {
                List<FbsBudgetResolveMonthVO> resolveList = fbsBudgetResolveMonthService.getResolveMonthBudget(fbsBudget, this.getCurrentUser());
                model.setResolveList(resolveList);
                if (!EmptyUtils.isEmptyList(resolveList)) {
                    model.setWeeks(resolveList.get(0).getBtempMoneyMap());
                    model.setWeekNum(resolveList.get(0).getBtempMoneyMap().size());
                }
                model.setMinMonth(DateUtils.getWeekOfMonth(DateUtils.getDate()));
                model.setSaveMethod("inputResolveMonthSave");
                model.setForwardUrl("budget/inputFbsBudgetResolveMonthCollectDetailSuper");
                return AjaxJson.success(model);
            }
            model.setSaveMethod("inputSave");
            model.setForwardUrl("budget/inputFbsBudgetCollectDetail");
            return AjaxJson.success(model);
        }
        //年计划上报+年分解
        if (BudgetConstant.TIME_TYPE_YEAR.equals(fbsBudget.getTimeType()) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode)) {
            List<FbsBudgetResolveYearVO> resolveList = fbsBudgetResolveYearService.initResolveYearBudget(fbsBudget, this.getCurrentUser());
            model.setResolveList(resolveList);
            if (!EmptyUtils.isEmptyList(resolveList)) {
                model.setMonths(resolveList.get(0).getbMoneyMap());
            }
            model.setSaveMethod("inputResolveYearSave");
            model.setForwardUrl("budget/inputFbsBudgetResolveYearCollectDetail");
            return AjaxJson.success(model);
        }
        //季计划模式控制
        if (BudgetConstant.TIME_TYPE_QUARTER.equals(fbsBudget.getTimeType())) {
            //季滚动
            if (BudgetConstant.FBS_CONFIG_QIMODE_ROLL.equals(qimode)) {
                fbsBudgetRollQuarterService.initRollQuarterBudget(fbsBudget, model, null, this.getCurrentUser());
                model.setSaveMethod("inputRollQuarterSave");
                model.setForwardUrl("budget/inputFbsBudgetRollQuarterCollectDetail");
                return AjaxJson.success(model);
            }
        }
        //月计划模式控制
        if (BudgetConstant.TIME_TYPE_MONTH.equals(fbsBudget.getTimeType())) {
            if (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING.equals(mimode) || BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING_WEEK.equals(mimode)) {
                //月计划上报+（月滚动+双月周明细）
                model.setSaveMethod("inputRollMonthWeekSave");
                fbsBudgetRollMonthWeekService.initRollMonthBudget(fbsBudget, model, null, this.getCurrentUser());
                model.setForwardUrl("budget/inputFbsBudgetRollMonthWeekCollectDetail");
                return AjaxJson.success(model);
            } else if (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode)) {
                //月计划上报+月分解周
                List<FbsBudgetResolveMonthVO> resolveList = fbsBudgetResolveMonthService.initResolveMonthBudget(fbsBudget, this.getCurrentUser());
                model.setResolveList(resolveList);
                if (!EmptyUtils.isEmptyList(resolveList)) {
                    model.setWeeks(resolveList.get(0).getbMoneyMap());
                }
                model.setSaveMethod("inputResolveMonthSave");
                model.setForwardUrl("budget/inputFbsBudgetResolveMonthCollectDetail");
                return AjaxJson.success(model);
            }
        }
        //周计划模式控制
        if (BudgetConstant.TIME_TYPE_WEEK.equals(fbsBudget.getTimeType())) {
            //周滚动
            if (BudgetConstant.FBS_CONFIG_WIMODE_ROLL.equals(wimode)) {
                fbsBudgetRollWeekService.initRollWeekBudget(fbsBudget, model, null, this.getCurrentUser());
                model.setSaveMethod("inputRollWeekSave");
                model.setForwardUrl("budget/inputFbsBudgetRollWeekCollectDetail");
                return AjaxJson.success(model);
            }
        }
        model.setSaveMethod("inputSave");
        model.setForwardUrl("budget/inputFbsBudgetCollectDetail");
        return AjaxJson.success(model);
    }

    /**
     * 资金计划上报主表详情页
     *
     * @return
     */
    @LogMessage(content = "查询资金计划上报主表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}/{target}")
    public AjaxJson<FbsBudgetInputContentVO> view(@PathVariable("id") String id, @PathVariable("target") String target) {
        FbsBudgetInputContentVO model = new FbsBudgetInputContentVO();
        FbsBudgetVO fbsBudget = fbsBudgetService.getBudgetInfo(id, target);
        FbsCollectCorpVO vo = fbsBudgetService.listCorpById(fbsBudget.getCorpId());
        if (!EmptyUtils.isEmpty(vo)) {
            fbsBudget.setCorpName(vo.getCorpName());
        }
        model.setFbsBudgetPage(fbsBudget);
        model.setIsCollect(this.fbsCorpBudgetService.getChildCollectFlag(fbsBudget.getCorpId()));
        model.setDtlList(fbsBudget.getDtlList());
        model.setTarget(target);
        //年计划上报模式:0,年固定;1,年分解
        String yimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_YIMODE);
        //季计划上报模式:1,季固定;2,季滚动;
        String qimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODE);
        //月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
        String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
        //mRollSubperiod-月滚动模式 滚动分解子周期
        //int mRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MROLLSUBPERIOD));
        //周计划上报模式:1,固定;2,周滚动;
        String wimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODE);
        //追加
        if (BudgetConstant.BUDGET_TARGET_SUPER.equals(target)) {
            FbsBudgetSuperadd superadd = fbsBudgetSuperaddService.getLastSuperaddByBudgetId(fbsBudget.getId());
            if (superadd != null) {
                fbsBudget = fbsBudgetService.initBudgetSuperadd(fbsBudget, this.getCurrentUser());//获取明细
            }
            model.setSuperaddPage(superadd);
            model.setBusName(FbsMessageConstant.BUS_NAME_SUPERADD);
            //年计划上报+年分解
            if (BudgetConstant.TIME_TYPE_YEAR.equals(fbsBudget.getTimeType()) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode)) {
                List<FbsBudgetResolveYearVO> resolveList = fbsBudgetResolveYearService.initResolveYearBudget(fbsBudget, this.getCurrentUser());
                model.setResolveList(resolveList);
                if (!EmptyUtils.isEmptyList(resolveList)) {
                    model.setResolves(resolveList.get(0).getbMoneyMap());
                }
                model.setForwardUrl("budget/viewFbsBudgetResolve");
                return AjaxJson.success(model);
            }
            //月计划上报+月分解周
            if (BudgetConstant.TIME_TYPE_MONTH.equals(fbsBudget.getTimeType()) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode)) {
                //月计划上报+月分解周
                List<FbsBudgetResolveMonthVO> resolveList = fbsBudgetResolveMonthService.initResolveMonthBudget(fbsBudget, this.getCurrentUser());
                model.setResolveList(resolveList);
                if (!EmptyUtils.isEmptyList(resolveList)) {
                    model.setResolves(resolveList.get(0).getbMoneyMap());
                }

                model.setForwardUrl("budget/viewFbsBudgetResolve");
                return AjaxJson.success(model);
            }
        } else {
            model.setBusName(FbsMessageConstant.BUS_NAME_INPUT);
            //年计划模式控制
            if (BudgetConstant.TIME_TYPE_YEAR.equals(fbsBudget.getTimeType()) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode)) {
                List<FbsBudgetResolveYearVO> resolveList = fbsBudgetResolveYearService.initResolveYearBudget(fbsBudget, this.getCurrentUser());
                model.setResolveList(resolveList);
                if (!EmptyUtils.isEmptyList(resolveList)) {
                    model.setResolves(resolveList.get(0).getbMoneyMap());
                }
                model.setForwardUrl("budget/viewFbsBudgetResolve");
                return AjaxJson.success(model);
            }
            //季计划模式控制
            if (BudgetConstant.TIME_TYPE_QUARTER.equals(fbsBudget.getTimeType())) {
                //季滚动
                if (BudgetConstant.FBS_CONFIG_QIMODE_ROLL.equals(qimode)) {
                    fbsBudgetRollQuarterService.initRollQuarterBudget(fbsBudget, model, null, this.getCurrentUser());
                    model.setForwardUrl("budget/viewFbsBudgetRollQuarter");
                    return AjaxJson.success(model);
                }
            }
            //月计划模式控制
            if (BudgetConstant.TIME_TYPE_MONTH.equals(fbsBudget.getTimeType())) {
                //月滚动模式
                int mimodeRoll = Integer.parseInt(configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODEROLL).getValue());
                //是否有年计划
                String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
                //是否可以跨年：未启用年计划 或者 启用了年计划但不控制子周期时支持跨年
                boolean isSpanYear = false;
                if (BudgetConstant.CONFIG_CRL_0.equalsIgnoreCase(ycrl)
                        || BudgetConstant.CONFIG_CRL_2.equals(ycrl)) {
                    isSpanYear = true;
                }
                int month = DateUtils.getMonth(fbsBudget.getBeginDate());
                List<String> months = new ArrayList<String>();
                for (int i = 1; i <= mimodeRoll; i++) {
                    if (!isSpanYear) {
                        if (month <= 12) {
                            months.add(month + "");
                            month++;
                        }
                    } else {
                        months.add(month + "");
                        month++;
                        if (month > 12) {
                            month = 1;
                        }
                    }
                }
                if (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING.equals(mimode) || BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING_WEEK.equals(mimode)) {
                    //月计划上报+月滚动
                    fbsBudgetRollMonthWeekService.initRollMonthBudget(fbsBudget, model, null, this.getCurrentUser());
                    fbsBudget.setEndDate(DateUtils.getLastMonthDate(DateUtils.addMonth(fbsBudget.getBeginDate(), (months.size() - 1))));

                    model.setForwardUrl("budget/viewFbsBudgetRollMonthWeek");
                    return AjaxJson.success(model);
                } else if (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode)) {
                    //月计划上报+月分解周
                    List<FbsBudgetResolveMonthVO> resolveList = fbsBudgetResolveMonthService.initResolveMonthBudget(fbsBudget, this.getCurrentUser());
                    model.setResolveList(resolveList);
                    if (!EmptyUtils.isEmptyList(resolveList)) {
                        model.setResolves(resolveList.get(0).getbMoneyMap());
                    }
                    model.setForwardUrl("budget/viewFbsBudgetResolve");
                    return AjaxJson.success(model);
                }
            }
            //周计划模式控制
            if (BudgetConstant.TIME_TYPE_WEEK.equals(fbsBudget.getTimeType())) {
                //周滚动滚动
                if (BudgetConstant.FBS_CONFIG_WIMODE_ROLL.equals(wimode)) {
                    fbsBudgetRollWeekService.initRollWeekBudget(fbsBudget, model, null, this.getCurrentUser());
                    model.setForwardUrl("budget/viewFbsBudgetRollWeek");
                    return AjaxJson.success(model);
                }
            }
        }
        model.setForwardUrl("budget/viewFbsBudget");
        return AjaxJson.success(model);
    }

    /**
     * 资金计划上报主表详情页
     *
     * @return
     */
    @LogMessage(content = "查询资金计划上报主表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/curView/{corpId}/{timeType}/{beginDate}/{endDate}/{target}")
    public AjaxJson<FbsBudgetInputContentVO> curView(@PathVariable("corpId") String corpId, @PathVariable("timeType") String timeType, @PathVariable("beginDate") String beginDate,
                                                     @PathVariable("endDate") String endDate, @PathVariable("target") String target) {
        FbsBudgetInputContentVO model = new FbsBudgetInputContentVO();
        FbsBudgetVO fbsBudget = new FbsBudgetVO();
        fbsBudget.setCorpId(corpId);
        fbsBudget.setCorpName(corpService.get(corpId).getName());
        fbsBudget.setTimeType(timeType);
        fbsBudget.setBeginDate(DateUtils.parseDate(beginDate));
        fbsBudget.setEndDate(DateUtils.parseDate(endDate));
        //BtCurrencyDTO currencyDTO = null;
        //获取单位分配的科目
        List<FbsItemVO> itemList = fbsCorpItemService.getAllItemByCorpId(fbsBudget.getCorpId());
        //获取汇总单位的资金计划
        FbsBudget corpBudget = this.fbsBudgetService.getCorpBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), fbsBudget.getBeginDate(), fbsBudget.getEndDate());
        if (corpBudget != null) {
            BeanUtils.copyNotNullProperties(corpBudget, fbsBudget);
            Map<String, FbsItemVO> itemMap = new HashMap<>();
            for (FbsItemVO fbsItemVO : itemList) {
                itemMap.put(fbsItemVO.getId(), fbsItemVO);
            }
            List<FbsBudgetDtlAbsVO> voList = this.fbsBudgetDtlAbsService.listByBudgetId(corpBudget.getId(), corpBudget.getTimeType());
            for (FbsBudgetDtlAbsVO fbsBudgetDtlAbsVO : voList) {
                if (itemMap.containsKey(fbsBudgetDtlAbsVO.getItemId())) {
                    fbsBudgetDtlAbsVO.setItem(itemMap.get(fbsBudgetDtlAbsVO.getItemId()));
                }
            }
            fbsBudget.setDtlList(voList);
        } else {
            //汇总单位的本位币
            fbsBudget.setCurId(corpService.get(corpId).getCurId());
            List<FbsBudgetDtlAbsVO> list = new ArrayList<>();
            for (FbsItemVO itemVO : itemList) {
                FbsBudgetDtlAbsVO dtlAbsVO = new FbsBudgetDtlAbsVO();
                dtlAbsVO.setItem(itemVO);
                dtlAbsVO.setItemCode(itemVO.getItemCode());
                dtlAbsVO.setItemName(itemVO.getItemName());
                dtlAbsVO.setbMoney(BigDecimal.ZERO);
                dtlAbsVO.setBtempMoney(BigDecimal.ZERO);
                dtlAbsVO.setBsuperMoney(BigDecimal.ZERO);
                list.add(dtlAbsVO);
            }
            fbsBudget.setDtlList(list);
        }
        model.setFbsBudgetPage(fbsBudget);
        model.setDtlList(fbsBudget.getDtlList());
        model.setTarget(target);
        //获取下级单位
        List<FbsBudgetInputPreVO> childCorps = fbsBudgetService.getChildCorpBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), fbsBudget.getBeginDate(), fbsBudget.getEndDate());
        LinkedHashMap<String, List<FbsBudgetDtlAbsVO>> childCorpMap = new LinkedHashMap<String, List<FbsBudgetDtlAbsVO>>();
        //存储下级单位计划上报ID
        List<String> budgetIds = new ArrayList<String>();
        //汇总单位
        HashMap<String, FbsBudgetDtlAbsVO> itemSumMap = new HashMap<String, FbsBudgetDtlAbsVO>();
        for (FbsBudgetInputPreVO input : childCorps) {
            //获取下级单位资金计划
            FbsBudget entity = fbsBudgetService.getCorpBudget(input.getCorpId(), fbsBudget.getTimeType(), fbsBudget.getBeginDate(), fbsBudget.getEndDate());
            List<FbsBudgetDtlAbsVO> listDtl = null;
            if (entity != null && !entity.getStatus().equals(BudgetConstant.STATUE_NULL)) {
                //获取下级单位资金计划明细
                listDtl = fbsBudgetDtlAbsService.listByBudgetId(entity.getId(), entity.getTimeType());
                if (!EmptyUtils.isEmptyList(listDtl)) {
                    budgetIds.add(entity.getId());
                    //存储下级单位分配的科目
                    HashMap<String, FbsBudgetDtlAbsVO> itemMap = new HashMap<String, FbsBudgetDtlAbsVO>();
                    for (FbsBudgetDtlAbsVO dtl : listDtl) {
                        itemMap.put(dtl.getItemCode(), dtl);
                        itemSumMap.put(dtl.getItemCode(), dtl);
                    }
                    for (FbsBudgetDtlAbsVO vo : fbsBudget.getDtlList()) {
                        for (FbsBudgetDtlAbsVO childVo : listDtl) {
                            if (childVo.getItemCode().equals(vo.getItemCode())) {
                                childVo.setbMoney(childVo.getbMoney().multiply(entity.getExchangeRate()));
                                childVo.setBsuperMoney(childVo.getBsuperMoney().multiply(entity.getExchangeRate()));
                                childVo.setBtempMoney(childVo.getBtempMoney().multiply(entity.getExchangeRate()));
                                //设置汇总金额
                                vo.setbMoney(vo.getbMoney().add(childVo.getbMoney()));
                                vo.setBsuperMoney(vo.getBsuperMoney().add(childVo.getBsuperMoney()));
                                vo.setBtempMoney(vo.getBtempMoney().add(childVo.getBtempMoney()));
                            }
                        }
                        //汇总单位分配的科目，下级单位没有，则初始化该科目的信息
                        if (!itemMap.containsKey(vo.getItemCode())) {
                            FbsBudgetDtlAbsVO dtl = new FbsBudgetDtlAbsVO();
                            BeanUtils.copyNotNullProperties(vo, dtl);
                            dtl.setbMoney(BigDecimal.ZERO);
                            dtl.setBsuperMoney(BigDecimal.ZERO);
                            dtl.setBtempMoney(BigDecimal.ZERO);
                            listDtl.add(dtl);
                        }
                    }
                }
            }
            if (EmptyUtils.isEmptyList(listDtl)) {
                listDtl = new ArrayList<FbsBudgetDtlAbsVO>();
                for (FbsBudgetDtlAbsVO vo : fbsBudget.getDtlList()) {
                    FbsBudgetDtlAbsVO dtl = new FbsBudgetDtlAbsVO();
                    BeanUtils.copyNotNullProperties(vo, dtl);
                    dtl.setbMoney(BigDecimal.ZERO);
                    dtl.setBsuperMoney(BigDecimal.ZERO);
                    dtl.setBtempMoney(BigDecimal.ZERO);
                    listDtl.add(dtl);
                }
            }
            childCorpMap.put(input.getCorpName(), listDtl);
        }
        //获取汇总金额
        String budgetAuthSql = SqlUtil.getInSql(budgetIds, "b.budget_id", true);
        List<FbsBudgetDtlAbsVO> listSumDtl = fbsBudgetDtlAbsService.collectByBudgetIds(fbsBudget.getTimeType(), budgetAuthSql);
        for (FbsBudgetDtlAbsVO vo : fbsBudget.getDtlList()) {
            for (FbsBudgetDtlAbsVO childVo : listSumDtl) {
                if (childVo.getItemCode().equals(vo.getItemCode())) {
                    //设置汇总金额
                    vo.setbMoney(vo.getbMoney().add(childVo.getbMoney()));
                    vo.setBsuperMoney(vo.getBsuperMoney().add(childVo.getBsuperMoney()));
                    vo.setBtempMoney(vo.getBtempMoney().add(childVo.getBtempMoney()));
                }
            }
            if (!itemSumMap.containsKey(vo.getItemCode())) {
                FbsBudgetDtlAbsVO dtl = new FbsBudgetDtlAbsVO();
                BeanUtils.copyNotNullProperties(vo, dtl);
                dtl.setbMoney(BigDecimal.ZERO);
                dtl.setBsuperMoney(BigDecimal.ZERO);
                dtl.setBtempMoney(BigDecimal.ZERO);
                listSumDtl.add(dtl);
            }
        }
        if (EmptyUtils.isEmptyList(listSumDtl)) {
            listSumDtl = new ArrayList<FbsBudgetDtlAbsVO>();
            for (FbsBudgetDtlAbsVO vo : fbsBudget.getDtlList()) {
                FbsBudgetDtlAbsVO dtl = new FbsBudgetDtlAbsVO();
                BeanUtils.copyNotNullProperties(vo, dtl);
                dtl.setbMoney(BigDecimal.ZERO);
                dtl.setBsuperMoney(BigDecimal.ZERO);
                dtl.setBtempMoney(BigDecimal.ZERO);
                listSumDtl.add(dtl);
            }
        }
        childCorpMap.put("sumMoney", listSumDtl);
        model.setChildCorpMap(childCorpMap);
        model.setChildCorpSize(childCorps.size() + 1);
        model.setChildCorpData(childCorps);
        model.setForwardUrl("budget/curViewFbsBudget");
        return AjaxJson.success(model);
    }

    /**
     * 资金计划上报主表详情页
     *
     * @return
     */
    @LogMessage(content = "查询资金计划上报主表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/curSuperView/{corpId}/{timeType}/{beginDate}/{endDate}/{target}")
    public AjaxJson<FbsBudgetInputContentVO> curSuperView(@PathVariable("corpId") String corpId, @PathVariable("timeType") String timeType, @PathVariable("beginDate") String beginDate,
                                                          @PathVariable("endDate") String endDate, @PathVariable("target") String target) {
        FbsBudgetInputContentVO model = new FbsBudgetInputContentVO();
        FbsBudgetVO fbsBudget = new FbsBudgetVO();
        fbsBudget.setCorpId(corpId);
        fbsBudget.setCorpName(corpService.get(corpId).getName());
        fbsBudget.setTimeType(timeType);
        fbsBudget.setBeginDate(DateUtils.parseDate(beginDate));
        fbsBudget.setEndDate(DateUtils.parseDate(endDate));
        //BtCurrencyDTO currencyDTO = null;
        //获取单位分配的科目
        List<FbsItemVO> itemList = fbsCorpItemService.getAllItemByCorpId(fbsBudget.getCorpId());
        //获取汇总单位的资金计划
        FbsBudget corpBudget = this.fbsBudgetService.getCorpBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), fbsBudget.getBeginDate(), fbsBudget.getEndDate());
        if (corpBudget != null) {
            BeanUtils.copyNotNullProperties(corpBudget, fbsBudget);
            Map<String, FbsItemVO> itemMap = new HashMap<>();
            for (FbsItemVO fbsItemVO : itemList) {
                itemMap.put(fbsItemVO.getId(), fbsItemVO);
            }
            List<FbsBudgetDtlAbsVO> voList = this.fbsBudgetDtlAbsService.listByBudgetId(corpBudget.getId(), corpBudget.getTimeType());
            for (FbsBudgetDtlAbsVO fbsBudgetDtlAbsVO : voList) {
                if (itemMap.containsKey(fbsBudgetDtlAbsVO.getItemId())) {
                    fbsBudgetDtlAbsVO.setItem(itemMap.get(fbsBudgetDtlAbsVO.getItemId()));
                }
            }
            fbsBudget.setDtlList(voList);
        } else {
            //汇总单位的本位币
            fbsBudget.setCurId(corpService.get(corpId).getCurId());
            List<FbsBudgetDtlAbsVO> list = new ArrayList<>();
            for (FbsItemVO itemVO : itemList) {
                FbsBudgetDtlAbsVO dtlAbsVO = new FbsBudgetDtlAbsVO();
                dtlAbsVO.setItem(itemVO);
                dtlAbsVO.setItemCode(itemVO.getItemCode());
                dtlAbsVO.setItemName(itemVO.getItemName());
                dtlAbsVO.setbMoney(BigDecimal.ZERO);
                dtlAbsVO.setBtempMoney(BigDecimal.ZERO);
                dtlAbsVO.setBsuperMoney(BigDecimal.ZERO);
                list.add(dtlAbsVO);
            }
            fbsBudget.setDtlList(list);
        }
        model.setFbsBudgetPage(fbsBudget);
        model.setDtlList(fbsBudget.getDtlList());
        model.setTarget(target);
        //获取下级单位
        List<FbsBudgetInputPreVO> childCorps = fbsBudgetService.getChildCorpBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), fbsBudget.getBeginDate(), fbsBudget.getEndDate());
        List<FbsBudgetInputPreVO> childCorpsTemp = new ArrayList<>();
        for (FbsBudgetInputPreVO vo : childCorps) {
            if (vo.getStatus() >= BudgetConstant.STATUE_SUPERADD_TEMP) {
                childCorpsTemp.add(vo);
            }
        }
        LinkedHashMap<String, List<FbsBudgetDtlAbsVO>> childCorpMap = new LinkedHashMap<String, List<FbsBudgetDtlAbsVO>>();
        //存储下级单位计划上报ID
        List<String> budgetIds = new ArrayList<String>();
        //汇总单位
        HashMap<String, FbsBudgetDtlAbsVO> itemSumMap = new HashMap<String, FbsBudgetDtlAbsVO>();
        for (FbsBudgetInputPreVO input : childCorps) {
            //获取下级单位资金计划
            FbsBudget entity = fbsBudgetService.getCorpBudget(input.getCorpId(), fbsBudget.getTimeType(), fbsBudget.getBeginDate(), fbsBudget.getEndDate());
            List<FbsBudgetDtlAbsVO> listDtl = null;
            if (entity != null && !entity.getStatus().equals(BudgetConstant.STATUE_NULL)) {
                //获取下级单位资金计划明细
                listDtl = fbsBudgetDtlAbsService.listByBudgetId(entity.getId(), entity.getTimeType());
                if (!EmptyUtils.isEmptyList(listDtl)) {
                    budgetIds.add(entity.getId());
                    //存储下级单位分配的科目
                    HashMap<String, FbsBudgetDtlAbsVO> itemMap = new HashMap<String, FbsBudgetDtlAbsVO>();
                    for (FbsBudgetDtlAbsVO dtl : listDtl) {
                        itemMap.put(dtl.getItemCode(), dtl);
                        itemSumMap.put(dtl.getItemCode(), dtl);
                    }
                    for (FbsBudgetDtlAbsVO vo : fbsBudget.getDtlList()) {
                        for (FbsBudgetDtlAbsVO childVo : listDtl) {
                            if (childVo.getItemCode().equals(vo.getItemCode())) {
                                childVo.setbMoney(childVo.getbMoney().multiply(entity.getExchangeRate()));
                                childVo.setBsuperMoney(childVo.getBsuperMoney().multiply(entity.getExchangeRate()));
                                childVo.setBtempMoney(childVo.getBtempMoney().multiply(entity.getExchangeRate()));
                                //设置汇总金额
                                vo.setbMoney(vo.getbMoney().add(childVo.getbMoney()));
                                vo.setBsuperMoney(vo.getBsuperMoney().add(childVo.getBsuperMoney()));
                                vo.setBtempMoney(vo.getBtempMoney().add(childVo.getBtempMoney()));
                            }
                        }
                        //汇总单位分配的科目，下级单位没有，则初始化该科目的信息
                        if (!itemMap.containsKey(vo.getItemCode())) {
                            FbsBudgetDtlAbsVO dtl = new FbsBudgetDtlAbsVO();
                            BeanUtils.copyNotNullProperties(vo, dtl);
                            dtl.setbMoney(BigDecimal.ZERO);
                            dtl.setBsuperMoney(BigDecimal.ZERO);
                            dtl.setBtempMoney(BigDecimal.ZERO);
                            listDtl.add(dtl);
                        }
                    }
                }
            }
            if (EmptyUtils.isEmptyList(listDtl)) {
                listDtl = new ArrayList<FbsBudgetDtlAbsVO>();
                for (FbsBudgetDtlAbsVO vo : fbsBudget.getDtlList()) {
                    FbsBudgetDtlAbsVO dtl = new FbsBudgetDtlAbsVO();
                    BeanUtils.copyNotNullProperties(vo, dtl);
                    dtl.setbMoney(BigDecimal.ZERO);
                    dtl.setBsuperMoney(BigDecimal.ZERO);
                    dtl.setBtempMoney(BigDecimal.ZERO);
                    listDtl.add(dtl);
                }
            }
            if (input.getStatus() >= BudgetConstant.STATUE_SUPERADD_TEMP) {
                childCorpMap.put(input.getCorpName(), listDtl);
            }
        }
        //获取汇总金额
        String budgetAuthSql = SqlUtil.getInSql(budgetIds, "b.budget_id", true);
        List<FbsBudgetDtlAbsVO> listSumDtl = fbsBudgetDtlAbsService.collectByBudgetIds(fbsBudget.getTimeType(), budgetAuthSql);
        for (FbsBudgetDtlAbsVO vo : fbsBudget.getDtlList()) {
            for (FbsBudgetDtlAbsVO childVo : listSumDtl) {
                if (childVo.getItemCode().equals(vo.getItemCode())) {
                    //设置汇总金额
                    vo.setbMoney(vo.getbMoney().add(childVo.getbMoney()));
                    vo.setBsuperMoney(vo.getBsuperMoney().add(childVo.getBsuperMoney()));
                    vo.setBtempMoney(vo.getBtempMoney().add(childVo.getBtempMoney()));
                }
            }
            if (!itemSumMap.containsKey(vo.getItemCode())) {
                FbsBudgetDtlAbsVO dtl = new FbsBudgetDtlAbsVO();
                BeanUtils.copyNotNullProperties(vo, dtl);
                dtl.setbMoney(BigDecimal.ZERO);
                dtl.setBsuperMoney(BigDecimal.ZERO);
                dtl.setBtempMoney(BigDecimal.ZERO);
                listSumDtl.add(dtl);
            }
        }
        if (EmptyUtils.isEmptyList(listSumDtl)) {
            listSumDtl = new ArrayList<FbsBudgetDtlAbsVO>();
            for (FbsBudgetDtlAbsVO vo : fbsBudget.getDtlList()) {
                FbsBudgetDtlAbsVO dtl = new FbsBudgetDtlAbsVO();
                BeanUtils.copyNotNullProperties(vo, dtl);
                dtl.setbMoney(BigDecimal.ZERO);
                dtl.setBsuperMoney(BigDecimal.ZERO);
                dtl.setBtempMoney(BigDecimal.ZERO);
                listSumDtl.add(dtl);
            }
        }
        childCorpMap.put("sumMoney", listSumDtl);
        model.setChildCorpMap(childCorpMap);
        model.setChildCorpSize(childCorpsTemp.size() + 1);
        model.setChildCorpData(childCorpsTemp);
        model.setForwardUrl("budget/curSuperViewFbsBudget");
        return AjaxJson.success(model);
    }

    /**
     * 添加资金计划上报主表
     *
     * @return
     */
    @LogMessage(content = "添加资金计划上报主表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/inputSave")
    public AjaxJson inputSave(@RequestBody FbsBudgetSaveDTO saveDTO, HttpServletRequest request) {
        String key = getUserId() + "_" + "layId";
        redisUtils.put(key, saveDTO.getLayId());
      // request.getSession().setAttribute("layId", saveDTO.getLayId());
        //判断是否是追加
        if (BudgetConstant.BUDGET_TARGET_SUPER.equals(saveDTO.getTarget())) {
            fbsBudgetService.saveFbsBudgetSuperAdd(saveDTO, this.getCurrentUser());
        } else {
            fbsBudgetService.saveFbsBudget(saveDTO, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 审批时保存修改计划上报
     *
     * @param input
     * @return
     */
    @LogMessage(content = "添加资金计划上报主表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/inputSaveApprove")
    public AjaxJson inputSaveApprove(@RequestBody FbsBudgetInputVO input) {
        //判断是否是追加
        if (BudgetConstant.BUDGET_TARGET_SUPER.equals(input.getTarget())) {
            fbsBudgetService.saveFbsBudgetSuperAddForApprove(input, this.getCurrentUser());
        } else {
            fbsBudgetService.saveFbsBudgetForApprove(input, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 添加资金计划上报主表-年分解
     *
     * @param input
     * @return
     */
    @LogMessage(content = "添加资金计划上报主表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/inputResolveYearSave")
    public AjaxJson inputResolveYearSave(@RequestBody FbsBudgetInputResolveYearVO input, HttpServletRequest request) {
      //  request.getSession().setAttribute("layId", input.getLayId());
        String key = getUserId() + "_" + "layId";
        redisUtils.put(key, input.getLayId());
        //判断是否是追加
        if (BudgetConstant.BUDGET_TARGET_SUPER.equals(input.getTarget())) {
            fbsBudgetResolveYearService.saveFbsBudgetSuperAdd(input, this.getCurrentUser());
        } else {
            fbsBudgetResolveYearService.saveFbsBudget(input, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 添加资金计划上报主表-年分解
     *
     * @param input
     * @return
     */
    @PostMapping("/inputResolveYearSaveForImport")
    public AjaxJson inputResolveYearSaveForImport(@RequestBody FbsBudgetInputResolveYearVO input) {
        //判断是否是追加
        if (BudgetConstant.BUDGET_TARGET_SUPER.equals(input.getTarget())) {
            fbsBudgetResolveYearService.saveFbsBudgetSuperAddForImport(input, this.getCurrentUser());
        } else {
            fbsBudgetResolveYearService.saveFbsBudgetForImport(input, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 添加资金计划上报主表-年分解审批保存
     *
     * @param input
     * @return
     */
    @LogMessage(content = "添加资金计划上报主表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/inputResolveYearSaveApprove")
    public AjaxJson inputResolveYearSaveApprove(@RequestBody FbsBudgetInputResolveYearVO input) {
        //判断是否是追加
        if (BudgetConstant.BUDGET_TARGET_SUPER.equals(input.getTarget())) {
            fbsBudgetResolveYearService.saveFbsBudgetSuperAddForApprove(input, this.getCurrentUser());
        } else {
            fbsBudgetResolveYearService.saveFbsBudgetForApprove(input, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 添加资金计划上报主表-月分解
     *
     * @param input
     * @return
     */
    @LogMessage(content = "添加资金计划上报主表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/inputResolveMonthSave")
    public AjaxJson inputResolveMonthSave(@RequestBody FbsBudgetInputResolveMonthVO input, HttpServletRequest request) {
      //  request.getSession().setAttribute("layId", input.getLayId());
        String key = getUserId() + "_" + "layId";
        redisUtils.put(key, input.getLayId());
        //判断是否是追加
        if (BudgetConstant.BUDGET_TARGET_SUPER.equals(input.getTarget())) {
            fbsBudgetResolveMonthService.saveFbsBudgetSuperAdd(input, this.getCurrentUser());
        } else {
            fbsBudgetResolveMonthService.saveFbsBudget(input, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 添加资金计划上报主表-月分解审批保存
     *
     * @param input
     * @return
     */
    @LogMessage(content = "添加资金计划上报主表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/inputResolveMonthSaveApprove")
    public AjaxJson inputResolveMonthSaveApprove(@RequestBody FbsBudgetInputResolveMonthVO input) {
        //判断是否是追加
        if (BudgetConstant.BUDGET_TARGET_SUPER.equals(input.getTarget())) {
            fbsBudgetResolveMonthService.saveFbsBudgetSuperAddForApprove(input, this.getCurrentUser());
        } else {
            fbsBudgetResolveMonthService.saveFbsBudgetForApprove(input, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 删除资金计划上报主表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除资金计划上报主表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}/{timeType}/{layId}")
    public AjaxJson delete(@PathVariable("id") String id, @PathVariable("timeType") String timeType,
                           @PathVariable("layId") String layId, HttpServletRequest request) {

        String key = getUserId() + "_" + "layId";
        redisUtils.put(key, layId);
      //  request.getSession().setAttribute("layId", layId);
        //年计划上报模式:0,年固定;1,年分解
        String yimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_YIMODE);
        //季计划上报模式:1,季固定;2,季滚动;
        String qimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODE);
        //月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
        String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
        //mRollSubperiod-月滚动模式 滚动分解子周期
        int mRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MROLLSUBPERIOD));
        //周计划上报模式:1,固定;2,周滚动;
        String wimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODE);
        if (BudgetConstant.TIME_TYPE_YEAR.equals(timeType) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode)) {
            //年计划上报+年分解
            fbsBudgetResolveYearService.deleteFbsBudget(id, timeType, this.getCurrentUser());
        } else if (BudgetConstant.TIME_TYPE_QUARTER.equals(timeType) && BudgetConstant.FBS_CONFIG_QIMODE_ROLL.equals(qimode)) {
            //季滚动删除
            fbsBudgetRollQuarterService.deleteFbsBudget(id, timeType, this.getCurrentUser());
        } else if (BudgetConstant.TIME_TYPE_MONTH.equals(timeType) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode)) {
            //月计划上报+月分解
            fbsBudgetResolveMonthService.deleteFbsBudget(id, timeType, this.getCurrentUser());
        } else if (BudgetConstant.TIME_TYPE_MONTH.equals(timeType) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING.equals(mimode)) {
            //月计划上报+月滚动
            fbsBudgetRollMonthWeekService.deleteFbsBudget(id, timeType, this.getCurrentUser());
        } else if (BudgetConstant.TIME_TYPE_WEEK.equals(timeType) && BudgetConstant.FBS_CONFIG_QIMODE_ROLL.equals(wimode)) {
            //周滚动删除
            fbsBudgetRollWeekService.deleteFbsBudget(id, timeType, this.getCurrentUser());
        } else {
            //固定模式
            fbsBudgetService.deleteFbsBudget(id, timeType, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 打回资金计划上报主表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "修改资金计划上报主表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/back")
    public AjaxJson back(String id, String backMsg) {
        //获取计划上报信息
        FbsBudget budget = fbsBudgetService.get(id, true);
        //判断计划上报状态大于编制完成，表示追加打回
        if (budget.getStatus() >= BudgetConstant.STATUE_CHECK) {
            //获取计划追加信息
            FbsBudgetSuperadd superAdd = fbsBudgetSuperaddService.getLastSuperaddByBudgetId(id);
            fbsBudgetService.backFbsBudgetSuperAdd(budget, superAdd, backMsg, this.getCurrentUser());
        } else {
            //年计划上报模式:0,年固定;1,年分解
            String yimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_YIMODE);
            //月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
            String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
            if (BudgetConstant.TIME_TYPE_YEAR.equals(budget.getTimeType()) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode)) {
                //年计划上报+年分解
                fbsBudgetResolveYearService.backFbsBudget(budget, backMsg, this.getCurrentUser());
            } else if (BudgetConstant.TIME_TYPE_MONTH.equals(budget.getTimeType()) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode)) {
                //月计划上报+月分解
                fbsBudgetResolveMonthService.backFbsBudget(budget, backMsg, this.getCurrentUser());
            } else {
                fbsBudgetService.backFbsBudget(budget, backMsg, this.getCurrentUser());
            }
        }
        return success();
    }

    /**
     * 汇总资金计划上报主表
     *
     * @return
     */
    @LogMessage(content = "添加资金计划上报主表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/collect")
    public AjaxJson collect(@RequestBody FbsBudgetInputCollectDTO inputDTO, HttpServletRequest request) {
        List<String> childIds = inputDTO.getChildIds();
        FbsBudgetInputVO input = new FbsBudgetInputVO();
        BeanUtils.copyProperties(inputDTO, input);
        //年计划上报模式:0,年固定;1,年分解
        String yimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_YIMODE);
        //季计划上报模式:1,季固定;2,季滚动;
        String qimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODE);
        //月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
        String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
        //mRollSubperiod-月滚动模式 滚动分解子周期
        int mRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MROLLSUBPERIOD));
        //周计划上报模式:1,固定;2,周滚动;
        String wimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODE);
        //追加汇总
        if (BudgetConstant.BUDGET_TARGET_SUPER.equals(input.getTarget())) {
            HashMap<String, FbsItemVO> fbsItemVOHashMap = (HashMap<String, FbsItemVO>) request.getSession().getAttribute("allItemMap");
            if (BudgetConstant.TIME_TYPE_YEAR.equals(input.getTimeType()) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode)) {
                //年计划上报+年分解
                fbsBudgetResolveYearService.collectFbsBudgetSuperAdd(input, childIds, this.getCurrentUser(), fbsItemVOHashMap);
            } else if (BudgetConstant.TIME_TYPE_MONTH.equals(input.getTimeType()) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode)) {
                //月计划上报+月分解
                fbsBudgetResolveMonthService.collectFbsBudgetSuperAdd(input, childIds, this.getCurrentUser(), fbsItemVOHashMap);
            } else {
                fbsBudgetService.collectFbsBudgetSuperAdd(input, childIds, this.getCurrentUser(), fbsItemVOHashMap);
            }
        } else {
            if (BudgetConstant.TIME_TYPE_YEAR.equals(input.getTimeType()) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode)) {
                //年计划上报+年分解
                fbsBudgetResolveYearService.collectFbsBudget(input, childIds, this.getCurrentUser());
            } else if (BudgetConstant.TIME_TYPE_MONTH.equals(input.getTimeType()) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode)) {
                //月计划上报+月分解
                fbsBudgetResolveMonthService.collectFbsBudget(input, childIds, this.getCurrentUser());
            } else if (BudgetConstant.TIME_TYPE_QUARTER.equals(input.getTimeType()) && BudgetConstant.FBS_CONFIG_QIMODE_ROLL.equals(qimode)) {
                //季滚动上报
                fbsBudgetRollQuarterService.collectFbsBudget(input, childIds, this.getCurrentUser());
            } else if (BudgetConstant.TIME_TYPE_MONTH.equals(input.getTimeType()) && (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING.equals(mimode) || BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING_WEEK.equals(mimode))) {
                //月计划上报+月滚动
                fbsBudgetRollMonthWeekService.collectFbsBudget(input, childIds, this.getCurrentUser());
            } else if (BudgetConstant.TIME_TYPE_WEEK.equals(input.getTimeType()) && BudgetConstant.FBS_CONFIG_WIMODE_ROLL.equals(wimode)) {
                //周滚动上报
                fbsBudgetRollWeekService.collectFbsBudget(input, childIds, this.getCurrentUser());
            } else {
                fbsBudgetService.collectFbsBudget(input, childIds, this.getCurrentUser());
            }
        }
        return success();
    }

    /**
     * 资金计划追加主表列表 页面跳转
     *
     * @param timeType 周期类型
     * @return
     */

    @LogMessage(content = "资金计划追加", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/superPre/{timeType}")
    public AjaxJson<Map<String, Object>> superPre(@PathVariable("timeType") String timeType, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        //计划周期类型:1-年 2-季 3-月 4-周 5-天
        map.put("timeType", timeType);
        List<CorpDTO> list = (List<CorpDTO>) redisUtils.get(getUserId()+"_"+"fbsBudgetCorpDTOAdd");
        if (EmptyUtils.isEmpty(list)) {
            list = fbsBudgetService.listEffectiveCorp(null, this.getCurrentUser());//获取管理的单位
            String key = getUserId() + "_" + "fbsBudgetCorpDTOAdd";
            redisUtils.put(key, list);
           // request.getSession().setAttribute("fbsBudgetCorpDTOAdd", list);
        }
        HashMap<String, FbsItemVO> fbsItemVOHashMap = (HashMap<String, FbsItemVO>) request.getSession().getAttribute("allItemMap");
        if (EmptyUtils.isEmpty(fbsItemVOHashMap)) {
            fbsItemVOHashMap = fbsItemService.getAllItemMap();
          //  request.getSession().setAttribute("allItemMap", fbsItemVOHashMap);
            String key = getUserId() + "_" + "allItemMap";
            redisUtils.put(key, fbsItemVOHashMap);
        }
        map.put("curDate", fbsBudgetService.listTreeData(timeType, null, 0, "add", list));
        return AjaxJson.success(map);
    }

    /**
     * 添加资金计划上报主表-月滚动上报
     *
     * @return
     */
    @LogMessage(content = "添加资金计划上报主表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/inputRollMonthWeekSave")
    public AjaxJson inputRollMonthWeekSave(FbsBudgetInputRollMonthWeekVO input, HttpServletRequest request) {
     //   request.getSession().setAttribute("layId", input.getLayId());
        String key = getUserId() + "_" + "layId";
        redisUtils.put(key, input.getLayId());
        fbsBudgetRollMonthWeekService.saveFbsBudget(input, request, this.getCurrentUser(), false);
        return success();
    }

    /**
     * 添加资金计划上报主表-月滚动上报
     *
     * @return
     */
    @LogMessage(content = "添加资金计划上报主表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/inputRollMonthWeekSaveApprove")
    public AjaxJson inputRollMonthWeekSaveApprove(FbsBudgetInputRollMonthWeekVO input, HttpServletRequest request) {
        fbsBudgetRollMonthWeekService.saveFbsBudget(input, request, this.getCurrentUser(), true);
        return success();
    }

    /**
     * 添加资金计划上报主表-季滚动上报
     *
     * @return
     */
    @LogMessage(content = "添加资金计划上报主表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/inputRollQuarterSave")
    public AjaxJson inputRollQuarterSave( FbsBudgetInputRollMonthWeekVO input, HttpServletRequest request) {
       // request.getSession().setAttribute("layId", input.getLayId());
        String key = getUserId() + "_" + "layId";
        redisUtils.put(key, input.getLayId());
        fbsBudgetRollQuarterService.saveFbsBudget(input, request, this.getCurrentUser(), false);
        return success();
    }

    /**
     * 添加资金计划上报主表-季滚动上报
     *
     * @return
     */
    @LogMessage(content = "添加资金计划上报主表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/inputRollQuarterSaveApprove")
    public AjaxJson inputRollQuarterSaveApprove( FbsBudgetInputRollMonthWeekVO input, HttpServletRequest request) {
        fbsBudgetRollQuarterService.saveFbsBudget(input, request, this.getCurrentUser(), true);
        return success();
    }

    /**
     * 添加资金计划上报主表-周滚动上报
     *
     * @return
     */
    @LogMessage(content = "添加资金计划上报主表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/inputRollWeekSave")
    public AjaxJson inputRollWeekSave( FbsBudgetInputRollMonthWeekVO input, HttpServletRequest request) {
     //   request.getSession().setAttribute("layId", input.getLayId());
        String key = getUserId() + "_" + "layId";
        redisUtils.put(key, input.getLayId());
        fbsBudgetRollWeekService.saveFbsBudget(input, request, this.getCurrentUser(), false);
        return success();
    }

    /**
     * 添加资金计划上报主表-周滚动上报
     *
     * @return
     */
    @LogMessage(content = "添加资金计划上报主表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/inputRollWeekSaveApprove")
    public AjaxJson inputRollWeekSaveApprove( FbsBudgetInputRollMonthWeekVO input, HttpServletRequest request) {
        fbsBudgetRollWeekService.saveFbsBudget(input, request, this.getCurrentUser(), true);
        return success();
    }

    /**
     * 审批列表
     *
     * @return
     */
    @GetMapping("/listApprove")
    public AjaxJson<Map<String, Object>> listApprove(String flowStatus, String cycleTime) {
        Map<String, Object> model = new HashMap<>();
        model.put("flowStatus", flowStatus);
        model.put("cycleTime", cycleTime);
        return AjaxJson.success(model);
    }

    /**
     * 审批详情
     *
     * @param id
     * @param taskId
     * @return
     */
    @GetMapping("/approve")
    public AjaxJson<FbsBudgetInputContentVO> approve(String id, String taskId) {
        FbsBudgetInputContentVO data = new FbsBudgetInputContentVO();
        FbsBudgetVO fbsBudget = fbsBudgetService.getBudgetInfo(id, BudgetConstant.BUDGET_TARGET_INPUT);
        //获取上级计划周期
        fbsBudget.setParentTimeType(fbsBudgetService.getParentTimeType(fbsBudget.getTimeType()));
        //年计划上报模式:0,年固定;1,年分解
        String yimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_YIMODE);
        //季计划上报模式:1,季固定;2,季滚动;
        String qimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODE);
        //月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
        String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
        //mRollSubperiod-月滚动模式 滚动分解子周期
        int mRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MROLLSUBPERIOD));
        //可上报/可追加金额计算
        //年分解月上报月或月分解周上报周情况下，可上报金额根据参数“分解计划上报余额控制方式”来控制
        //周计划上报模式:1,固定;2,周滚动;
        String wimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODE);
        String target = "input";
        if (BudgetConstant.TIME_TYPE_MONTH.equals(fbsBudget.getTimeType()) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode)) {
            fbsBudgetResolveYearService.calculateCanBMoney(fbsBudget, target);
        } else if (BudgetConstant.TIME_TYPE_WEEK.equals(fbsBudget.getTimeType()) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode)) {
            fbsBudgetResolveMonthService.calculateCanBMoney(fbsBudget, target);
        } else {
            fbsBudgetService.calculateCanBMoney(fbsBudget, target);
        }
        //判断是否存在父计划，且父计划控制子计划，父计划未上报，则子计划不允许上报
        String parentTimeType = fbsBudgetService.getParentTimeType(fbsBudget.getTimeType());
        fbsBudget.setParentTimeType(parentTimeType);
        data.setFbsBudgetPage(fbsBudget);
        data.setDltList(fbsBudget.getDtlList());
        data.setTaskId(taskId);
        data.setCollectFlag(fbsCorpBudgetService.getChildCollectFlag(fbsBudget.getCorpId()) ? "1" : "0");  //判断单位是否是汇总单位 1 为汇总单位。。汇总单位金额不能修改
        data.setBusName(FbsMessageConstant.BUS_NAME_INPUT);
        data.setShowNetAmount(CommonConstant.NO);//收支净额
        //审批时是否允许修改上报金额
        String allowEditMoney = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_ALLOWEDITMONEY).getValue();
        String suffix = "";
        if (CommonConstant.NO.equals(allowEditMoney)) {
            suffix = "View";
        }
        //年计划上报+年分解
        if (BudgetConstant.TIME_TYPE_YEAR.equals(fbsBudget.getTimeType()) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode)) {
            List<FbsBudgetResolveYearVO> resolveList = fbsBudgetResolveYearService.initResolveYearBudget(fbsBudget, this.getCurrentUser());
            data.setResolveList(resolveList);
            if (!EmptyUtils.isEmptyList(resolveList)) {
                data.setMonths(resolveList.get(0).getbMoneyMap());
            }
            data.setForwardUrl("budget/approveInputBudgetResolveYear" + suffix);
            return AjaxJson.success(data);
        }
        //季计划模式控制
        if (BudgetConstant.TIME_TYPE_QUARTER.equals(fbsBudget.getTimeType())) {
            //季滚动
            if (BudgetConstant.FBS_CONFIG_QIMODE_ROLL.equals(qimode)) {
                fbsBudgetRollQuarterService.initRollQuarterBudget(fbsBudget, data, null, this.getCurrentUser());
                data.setForwardUrl("budget/approveInputFbsBudgetRollQuarter" + suffix);
                return AjaxJson.success(data);
            }
        }
        //月计划模式控制
        if (BudgetConstant.TIME_TYPE_MONTH.equals(fbsBudget.getTimeType())) {
            if (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING.equals(mimode) || BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING_WEEK.equals(mimode)) {
                //月计划上报+（月滚动+双月周明细）
                fbsBudgetRollMonthWeekService.initRollMonthBudget(fbsBudget, data, null, this.getCurrentUser());
                data.setForwardUrl("budget/approveInputFbsBudgetRollMonthWeek" + suffix);
                return AjaxJson.success(data);
            } else if (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode)) {
                List<FbsBudgetResolveMonthVO> resolveList = fbsBudgetResolveMonthService.initResolveMonthBudget(fbsBudget, this.getCurrentUser());
                data.setResolveList(resolveList);
                if (!EmptyUtils.isEmptyList(resolveList)) {
                    data.setWeeks(resolveList.get(0).getbMoneyMap());
                }
                //月计划上报+月分解周
                data.setForwardUrl("budget/approveInputBudgetResolveMonth" + suffix);
                return AjaxJson.success(data);
            }
        }
        //周计划模式控制
        if (BudgetConstant.TIME_TYPE_WEEK.equals(fbsBudget.getTimeType())) {
            //季滚动
            if (BudgetConstant.FBS_CONFIG_WIMODE_ROLL.equals(wimode)) {
                fbsBudgetRollWeekService.initRollWeekBudget(fbsBudget, data, null, this.getCurrentUser());
                data.setForwardUrl("budget/approveInputFbsBudgetRollWeek" + suffix);
                return AjaxJson.success(data);

            }
        }
        data.setForwardUrl("budget/approveInputBudget" + suffix);
        return AjaxJson.success(data);
    }

    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(fbsBudgetService.dataTableApprove(input));
    }

    /**
     * 审批详情
     *
     * @param id
     * @param taskId
     * @return
     */
    @GetMapping("/superAddApprove")
    public AjaxJson<FbsBudgetInputContentVO> superAddApprove(String id, String taskId) {
        FbsBudgetInputContentVO data = new FbsBudgetInputContentVO();
        FbsBudgetVO fbsBudget = fbsBudgetService.getBudgetInfo(id, BudgetConstant.BUDGET_TARGET_SUPER);
        //获取上级计划周期
        fbsBudget.setParentTimeType(fbsBudgetService.getParentTimeType(fbsBudget.getTimeType()));
        //年计划上报模式:0,年固定;1,年分解
        String yimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_YIMODE);
        //月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
        String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
        //可上报/可追加金额计算
        //年分解月上报月或月分解周上报周情况下，可上报金额根据参数“分解计划上报余额控制方式”来控制
        String target = "super";
        if (BudgetConstant.TIME_TYPE_MONTH.equals(fbsBudget.getTimeType()) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode)) {
            fbsBudgetResolveYearService.calculateCanBMoney(fbsBudget, target);
        } else if (BudgetConstant.TIME_TYPE_WEEK.equals(fbsBudget.getTimeType()) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode)) {
            fbsBudgetResolveMonthService.calculateCanBMoney(fbsBudget, target);
        } else {
            fbsBudgetService.calculateCanBMoney(fbsBudget, target);
        }
        //判断单位是否是汇总单位
        data.setCollectFlag(fbsCorpBudgetService.getChildCollectFlag(fbsBudget.getCorpId()) ? "1" : "0");  //判断单位是否是汇总单位 1 为汇总单位。。汇总单位金额不能修改
        data.setBusName(FbsMessageConstant.BUS_NAME_SUPERADD);
        if (StringUtils.isNotEmpty(fbsBudget.getId())) {
            FbsBudgetSuperadd budgetSuperadd = fbsBudgetSuperaddService.getLastSuperaddByBudgetId(fbsBudget.getId());
            if (budgetSuperadd != null) {
                fbsBudget.setSuperaddId(budgetSuperadd.getId());
            }
        }
        fbsBudget = fbsBudgetService.initBudgetSuperadd(fbsBudget, this.getCurrentUser());//获取明细
        data.setFbsBudgetPage(fbsBudget);
        data.setDltList(fbsBudget.getDtlList());
        data.setTaskId(taskId);
        data.setTarget(target);
        //审批时是否允许修改上报金额
        String allowEditMoney = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_ALLOWEDITMONEY).getValue();
        String suffix = "";
        if (CommonConstant.NO.equals(allowEditMoney)) {
            suffix = "View";
        }
        //年计划上报+年分解
        if (BudgetConstant.TIME_TYPE_YEAR.equals(fbsBudget.getTimeType()) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode)) {
            List<FbsBudgetResolveYearVO> resolveList = fbsBudgetResolveYearService.getResolveYearBudgetBySuperadd(fbsBudget, this.getCurrentUser());
            data.setResolveList(resolveList);
            if (!EmptyUtils.isEmptyList(resolveList)) {
                data.setMonths(resolveList.get(0).getBtempMoneyMap());
            }
            data.setMinMonth(DateUtils.getMonth(DateUtils.getDate()));
            data.setForwardUrl("budget/approveInputFbsBudgetResolveYearSuper" + suffix);
            return AjaxJson.success(data);
        }
        //月计划上报+月分解周
        if (BudgetConstant.TIME_TYPE_MONTH.equals(fbsBudget.getTimeType()) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode)) {
            List<FbsBudgetResolveMonthVO> resolveList = fbsBudgetResolveMonthService.getResolveMonthBudget(fbsBudget, this.getCurrentUser());
            data.setResolveList(resolveList);
            if (!EmptyUtils.isEmptyList(resolveList)) {
                data.setWeeks(resolveList.get(0).getBtempMoneyMap());
                data.setWeekNum(resolveList.get(0).getBtempMoneyMap().size());
            }
            data.setMinWeek(DateUtils.getWeekOfMonth(DateUtils.getDate()));
            data.setForwardUrl("budget/approveInputFbsBudgetResolveMonthSuper" + suffix);
            return AjaxJson.success(data);
        }
        if (BudgetConstant.BUDGET_TARGET_SUPER.equals(target)) {
            data.setBusName(FbsMessageConstant.BUS_NAME_SUPERADD);
        } else {
            data.setBusName(FbsMessageConstant.BUS_NAME_INPUT);
        }
        data.setForwardUrl("budget/approveSuperAddBudget" + suffix);
        return AjaxJson.success(data);
    }


    /**
     * 加载追加审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableSuperAddApprove")
    public AjaxJson<DataTablesOutput> dataTableSuperAddApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(fbsBudgetService.dataTableSuperAddApprove(input));
    }

    /**
     * 跳转至手机审核详情界面
     *
     * @param businessId
     * @param taskId
     * @return String
     */
    @GetMapping("/mobileApprove")
    public AjaxJson<MobileApproveVO> mobileApprove(String businessId, String taskId) {
        MobileApproveVO data = new MobileApproveVO();
        if (StringUtils.isNotEmpty(businessId)) {
            FbsBudgetVO fbsBudget = fbsBudgetService.getBudget(businessId);
            data.setFbsBudgetPage(fbsBudget);
            data.setDltList(fbsBudget.getDtlList());
            data.setTaskId(taskId);
        }
        return AjaxJson.success(data);
    }

    /**
     * 查看收支净额
     *
     * @return
     */
    @LogMessage(content = "查看收支净额", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/showNetAmount/{id}")
    public AjaxJson<NetAmountVO> showNetAmount(@PathVariable("id") String id) {
        NetAmountVO data = new NetAmountVO();
        FbsBudgetVO fbsBudget = this.fbsBudgetService.getBudget(id);
        List<FbsBudgetInputPreVO> childCorpData = fbsBudgetService.getChildCorpBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), fbsBudget.getBeginDate(), fbsBudget.getEndDate());
        data.setFbsBudget(fbsBudget);
        data.setShowNetAmount(CommonConstant.NO);//收支净额
        data.setChildCorpData(childCorpData);
        return AjaxJson.success(data);
    }


    /**
     * 资金计划上报表
     *
     * @return
     */
    @RequestMapping("/mobileReqInput")
    public AjaxJson<List<Map<String, Object>>> mobileReqInput() {
        //获取业务类型
        return AjaxJson.success(budgetUtilService.getTimeType());
    }

    /**
     * 资金计划执行表
     *
     * @return
     */
    @RequestMapping("/mobileReqProess")
    public AjaxJson<List<Map<String, Object>>> mobileReqProess() {
        //获取业务类型
        return AjaxJson.success(budgetUtilService.getTimeType());
    }

    /**
     * 资金计划列表
     */
    @PostMapping("/getInputMoblieList")
    public AjaxJson<List<FbsBudgetVO>> getInputMoblieList(String timeType, String corpId, String startDateStr, String endDateStr, String resource) {
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, this.getUserId()), "fbs.corp_Id", true);
        return AjaxJson.success(this.fbsBudgetService.getBudgetInputMoblieList(corpId, timeType, startDateStr, endDateStr, corpAuthSql));
    }

    /**
     * 资金计划明细
     */
    @RequestMapping("/mobileFbsBudgetInfo")
    public AjaxJson<FbsBudgetVO> mobileFbsBudgetInfo(String id) {
        return AjaxJson.success(fbsBudgetService.getBudget(id));
    }

    /**
     * 资金计划执行列表
     */
    @RequestMapping("/mobileFbsBudgetProessList")
    public AjaxJson<FbsBudgetProcessListVO> mobileFbsBudgetProessList(String id) {
        FbsBudgetProcessListVO data = new FbsBudgetProcessListVO();
        FbsBudgetVO fbsBudgetVO = fbsBudgetService.getBudget(id);
        List<FbsBudgetProcessReportVO> reportList = this.fbsBudgetProcessReportService.listFbsBudgetProcessReport(fbsBudgetVO.getTimeType(), fbsBudgetVO.getCorpId(), fbsBudgetVO.getBeginDate(), fbsBudgetVO.getEndDate());
        data.setReportList(reportList);
        data.setFbsBudgetVO(fbsBudgetVO);
        return AjaxJson.success(data);
    }

    @RequestMapping("/mobileFbsBudgetProessDetailList")
    public AjaxJson<BudgetProcessDetailListVO> mobileFbsBudgetProessDetailList(String itemCode, String corpId, Date beginDate, Date endDate, boolean statusFlag) {
        BudgetProcessDetailListVO data = new BudgetProcessDetailListVO();
        CorpDTO corpDTO = this.corpService.get(corpId);
        List<FbsBudgetProcessVO> fbsBudgetProcessVOList = this.fbsBudgetProcessReportService.mobileFbsBudgetProessDetailList(itemCode, corpId, beginDate, endDate, statusFlag);
        data.setCorpName(corpDTO.getName());
        data.setFbsBudgetProcessVOList(fbsBudgetProcessVOList);
        return AjaxJson.success(data);
    }
}
