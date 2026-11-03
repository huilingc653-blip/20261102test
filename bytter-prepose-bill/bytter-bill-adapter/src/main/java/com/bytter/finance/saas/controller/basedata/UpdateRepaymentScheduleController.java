package com.bytter.finance.saas.controller.basedata;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.domain.basedata.service.UpdateRepaymentScheduleService;
import com.bytter.finance.domain.basedata.vo.RepayPlanVO;
import com.bytter.finance.domain.basedata.vo.RepaymentScheduleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiangs
 * @version 1.0
 * @Title: Controller
 * @Description: 还款计划修改表（改造为Rest接口给前端使用）
 * @date 2026年9月18日 09:58:44
 */
@Api(value = "/v1-cms/cms/basedata/updaterepaymentschedule", tags = "还款计划修改")
@RestController("v1UpdateRepaymentScheduleController")
@RequestMapping("/v1-cms/cms/basedata/updaterepaymentschedule")
public class UpdateRepaymentScheduleController extends BaseController {
    @Autowired
    private UpdateRepaymentScheduleService updateRepaymentScheduleService;

    /**
     * 加载还款计划修改页面主数据（原toRepayPlanUpdate，不再返回页面，返回JSON）
     *
     * @param repayPlanVO
     * @return
     */
    @ApiOperation("加载还款计划修改-主信息")
    @GetMapping("/toRepayPlanUpdate")
    public AjaxJson<Map<String, Object>> toRepayPlanUpdate(RepayPlanVO repayPlanVO) {
        Map<String, Object> resultMap = new HashMap<>();
        repayPlanVO = updateRepaymentScheduleService.toRepayPlanUpdate(repayPlanVO);

        String repayStartDateStr;
        if (EmptyUtils.isNotEmpty(repayPlanVO.getLoanDate())) {
            String loanStartDate = updateRepaymentScheduleService.getMaxRepayedDate(repayPlanVO);
            Date loanDate = DateUtils.str2Date(loanStartDate, DateUtils.date_sdf);
            Date startDate = DateUtils.addDay(loanDate, 1);
            repayPlanVO.setRepayBeginDate(DateUtils.format(startDate, DateUtils.PATTERN_CLASSICAL_SIMPLE));
            repayStartDateStr = DateUtils.format(startDate, DateUtils.PATTERN_CLASSICAL_SIMPLE);
        } else {
            repayStartDateStr = repayPlanVO.getLoanDate();
        }
        resultMap.put("repayPlan", repayPlanVO);
        resultMap.put("repayStartDateStr", repayStartDateStr);
        return AjaxJson.success(resultMap);
    }

    /**
     * 还款计划修改模块加载还款计划明细列表
     *
     * @param id
     * @return
     */
    @ApiOperation("查询修改模式还款计划明细列表")
    @PostMapping("/listUpdateRepayPlan")
    public AjaxJson<List<RepaymentScheduleVO>> listUpdateRepayPlan(@RequestParam String id) {
        List<RepaymentScheduleVO> list = updateRepaymentScheduleService.listUpdateRepayPlan(id);
        return AjaxJson.success(list);
    }

    /**
     * 保存还款计划修改数据
     *
     * @param rp
     * @return
     * @throws ParseException
     */
    @ApiOperation("保存修改后的还款计划")
    @LogMessage(content = "添加RepaymentSchedule", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/updateRepayPlan")
    public AjaxJson updateRepayPlan(@RequestBody RepayPlanVO rp) throws ParseException {
        AjaxJson json = success();
        json.setObj(updateRepaymentScheduleService.updateRepayPlan(rp, getCurrentUser()));
        return json;
    }
}
