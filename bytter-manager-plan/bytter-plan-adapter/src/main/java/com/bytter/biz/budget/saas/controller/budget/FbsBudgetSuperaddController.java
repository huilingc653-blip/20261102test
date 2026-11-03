package com.bytter.biz.budget.saas.controller.budget;

import com.bytter.biz.budget.entity.FbsBudget;
import com.bytter.biz.budget.entity.FbsBudgetSuperadd;
import com.bytter.biz.budget.service.*;
import com.bytter.biz.budget.spi.constant.FbsMessageConstant;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.budget.vo.*;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: FBS_BUDGET_SUPERADD
 * @date 2017-07-28 11:36:40
 */
@RestController("v1FbsBudgetSuperaddController")
@Api(value = "/v1-plan/budget/fbsBudgetSuperAddReport", tags = "FbsBudgetSuperaddController")
@RequestMapping("/v1-plan/budget/fbsbudgetsuperadd")
public class FbsBudgetSuperaddController extends BaseController {
    @Autowired
    private FbsBudgetService fbsBudgetService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private FbsBudgetResolveYearService fbsBudgetResolveYearService;
    @Autowired
    private FbsBudgetResolveMonthService fbsBudgetResolveMonthService;
    @Autowired
    private FbsBudgetRollMonthService fbsBudgetRollMonthService;
    @Autowired
    private FbsBudgetRollMonthWeekService fbsBudgetRollMonthWeekService;
    @Autowired
    private FbsBudgetSuperaddService fbsBudgetSuperaddService;
    @DubboReference(check = false)
    private IFbsCorpBudgetService fbsCorpBudgetService;
    @Autowired
    private FbsBudgetPeriodService fbsBudgetPeriodService;
    @DubboReference(check = false)
    private IConfigService configService;

    /**
     * 资金计划追加页面跳转
     *
     * @return
     */
    @PostMapping("/superadd")
    public AjaxJson<SuperAddVO> superadd(@RequestBody FbsBudgetInputPreVO inputPre) {
        SuperAddVO model = new SuperAddVO();
        //年计划上报模式:0,年固定;1,年分解
        String yimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_YIMODE);
        //月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
        String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
        //是否有年计划
        String ycrl = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_YCRL);
        //是否有月计划
        String mcrl = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MCRL);
        //获取资金计划上报信息
        FbsBudgetVO fbsBudget = fbsBudgetService.getBudgetInfo(inputPre.getId(), BudgetConstant.BUDGET_TARGET_SUPER);
        fbsBudget.setSuperaddId(inputPre.getSuperaddId());//追加ID
        fbsBudget = fbsBudgetService.initBudgetSuperadd(fbsBudget, this.getCurrentUser());//获取明细
        //获取上级计划周期
        fbsBudget.setParentTimeType(fbsBudgetService.getParentTimeType(fbsBudget.getTimeType()));
        //可上报/可追加金额计算
        //年分解月上报月或月分解周上报周情况下，可上报金额根据参数“分解计划上报余额控制方式”来控制
        if (BudgetConstant.TIME_TYPE_MONTH.equals(fbsBudget.getTimeType()) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode) && !CommonConstant.NO.equalsIgnoreCase(ycrl)) {
            fbsBudgetResolveYearService.calculateCanBMoney(fbsBudget, inputPre.getTarget());
        } else if (BudgetConstant.TIME_TYPE_WEEK.equals(fbsBudget.getTimeType()) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode) && !CommonConstant.NO.equalsIgnoreCase(mcrl)) {
            fbsBudgetResolveMonthService.calculateCanBMoney(fbsBudget, inputPre.getTarget());
        } else {
            fbsBudgetService.calculateCanBMoney(fbsBudget, inputPre.getTarget());
        }
        //币别不允许编辑
        fbsBudget.setIsEditCur(CommonConstant.NO);
        model.setFbsBudgetPage(fbsBudget);
        model.setDtlList(fbsBudget.getDtlList());
        model.setIsCollect(inputPre.getIsCollect());
        model.setTarget(inputPre.getTarget());
        model.setBusName(FbsMessageConstant.BUS_NAME_SUPERADD);
        if (fbsCorpBudgetService.getChildCollectFlag(fbsBudget.getCorpId())) {
            model.setChildCorpData(fbsBudgetService.getChildCorpBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(),
                    fbsBudget.getBeginDate(), fbsBudget.getEndDate()));
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
            if (BudgetConstant.TIME_TYPE_YEAR.equals(fbsBudget.getTimeType()) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode)) {
                List<FbsBudgetResolveYearVO> resolveList = fbsBudgetResolveYearService.getResolveYearBudgetBySuperadd(fbsBudget, this.getCurrentUser());
                model.setResolveList(resolveList);
                if (!EmptyUtils.isEmptyList(resolveList)) {
                    model.setMonths(resolveList.get(0).getBtempMoneyMap());
                }
                model.setMinMonth(DateUtils.getMonth(DateUtils.getDate()));
                model.setForwardUrl("budget/inputFbsBudgetResolveYearSuper");
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
                model.setMinWeek(fbsBudgetPeriodService.getWeekNoByTime(DateUtils.getDate()));
                model.setForwardUrl("budget/inputFbsBudgetResolveMonthSuper");
                return AjaxJson.success(model);
            }
            model.setForwardUrl("budget/inputFbsBudget");
            return AjaxJson.success(model);
        }
    }

    /**
     * 删除资金计划追加
     *
     * @param budgetId
     * @param superaddId
     * @return
     */
    @LogMessage(content = "删除资金计划追加主表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{budgetId}/{superaddId}")
    public AjaxJson delete(@PathVariable("budgetId") String budgetId,
                           @PathVariable("superaddId") String superaddId) {
        //年计划上报模式:0,年固定;1,年分解
        String yimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_YIMODE);
        //月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
        String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
        FbsBudget budget = this.fbsBudgetService.get(budgetId, true);
        if (BudgetConstant.TIME_TYPE_YEAR.equals(budget.getTimeType()) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode)) {
            this.fbsBudgetResolveYearService.deleteFbsBudgetSuperAdd(budget, superaddId, this.getCurrentUser());
        } else if (BudgetConstant.TIME_TYPE_MONTH.equals(budget.getTimeType()) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode)) {
            //月计划上报+月分解周
            this.fbsBudgetResolveMonthService.deleteFbsBudgetSuperAdd(budget, superaddId, this.getCurrentUser());
        } else {
            fbsBudgetService.deleteFbsBudgetSuperAdd(budget, superaddId, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 跳转至手机审核详情界面
     *
     * @param businessId
     * @param taskId
     * @return String
     */
    @GetMapping("/mobileApprove")
    public AjaxJson<SuperAddVO> mobileApprove(String businessId, String taskId) {
        SuperAddVO model = new SuperAddVO();
        if (StringUtils.isNotEmpty(businessId)) {
            FbsBudgetSuperadd superadd = fbsBudgetSuperaddService.get(businessId, true);
            FbsBudgetVO fbsBudget = fbsBudgetService.getBudget(superadd.getBudgetId());
            model.setFbsBudgetPage(fbsBudget);
            model.setFbsBudgetSuperaddPage(superadd);
            model.setDtlList(fbsBudget.getDtlList());
            model.setTaskId(taskId);
        }
        return AjaxJson.success(model);
    }
}
