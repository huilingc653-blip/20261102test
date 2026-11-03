package com.bytter.finance.web.basedata.controller;
import com.bytter.finance.domain.basedata.entity.CmsRepaymentSchedule;
import com.bytter.finance.domain.basedata.service.CmsRepaymentScheduleService;
import com.bytter.finance.domain.basedata.service.ExportScheduleService;
import com.bytter.finance.domain.basedata.service.RepayTypeService;
import com.bytter.finance.domain.basedata.service.RepaymentScheduleService;
import com.bytter.finance.domain.basedata.vo.RepayPlanVO;
import com.bytter.finance.domain.basedata.vo.RepaymentScheduleDtlVO;
import com.bytter.finance.domain.basedata.vo.RepaymentScheduleVO;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author songkk
 * @version 1.0
 * @Title: Controller
 * @Description: 还款计划表
 * @date 2017-08-01 09:58:44
 */
@Controller
@RequestMapping("/cms/basedata/repaymentschedule")
public class RepaymentScheduleController extends BaseController {
    @Autowired
    private RepaymentScheduleService repaymentScheduleService;
    @Autowired
    private CmsRepaymentScheduleService cmsRepaymentScheduleService;
    @Autowired
    private ExportScheduleService exportScheduleService;
    @Autowired
    private RepayTypeService repayTypeService;
    /**
     * 导出预算模板
     *
     * @param request
     * @param response
     * @param repayPlanVO
     * @throws Exception
     */
    @PostMapping("/exportBudget")
    public void exportBudget(HttpServletRequest request, HttpServletResponse response, RepayPlanVO repayPlanVO)
            throws Exception {
        this.exportScheduleService.exportBudget(request, response, repayPlanVO);
    }
    /**
     * 跳转到还款计划页面（可以修改数据:其他）
     *
     * @param repayPlanVO
     * @param model
     * @return
     */
    @GetMapping("/toRepayPlan")
    public String toRepayPlan(RepayPlanVO repayPlanVO, Model model) {
        // 判断数据（还款方式，金额等）有没有改变
        repayPlanVO.setPlanId(repaymentScheduleService.checkDataChange(repayPlanVO));
        model.addAttribute("repayPlan", repayPlanVO);
        //model.addAttribute("repayType", repayTypeService.get(repayPlanVO.getRepayType()));
        String loanDateStr = repayPlanVO.getLoanDate();
        if (EmptyUtils.isNotEmpty(loanDateStr)) {
            Date loanDate = DateUtils.str2Date(loanDateStr, DateUtils.date_sdf);
            Date startDate = DateUtils.addDay(loanDate, 1);
            model.addAttribute("repayStartDateStr", DateUtils.format(startDate, DateUtils.PATTERN_CLASSICAL_SIMPLE));
        } else {
            model.addAttribute("repayStartDateStr", repayPlanVO.getLoanDate());
        }
        if (repayPlanVO.getRepayType().equals(CmsConstant.REPAY_TYPE_03)
                || repayPlanVO.getRepayType().equals(CmsConstant.REPAY_TYPE_06)
                || repayPlanVO.getRepayType().equals(CmsConstant.REPAY_TYPE_07)) {
            return "cms/basedata/repayPlan/createRepayPlan";
        }
        return "cms/basedata/repayPlan/repayTypeRepayPlan";
    }
    /**
     * 指定还款计划（计提计息需求新增接口）
     *
     * @param repayPlanVO
     * @param model
     * @return
     */
    @GetMapping("/makeRepayPlan")
    public String makeRepayPlan(RepayPlanVO repayPlanVO, Model model) {
        // 判断数据（还款方式，金额等）有没有改变
        repayPlanVO.setPlanId(repaymentScheduleService.checkDataChange(repayPlanVO));
        model.addAttribute("repayPlan", repayPlanVO);
        model.addAttribute("repayType", repayTypeService.get(repayPlanVO.getRepayType()));
        model.addAttribute("repayStartDate",
                DateUtils.addDay(DateUtils.parse(repayPlanVO.getLoanDate(), "yyyy-MM-dd"), 1));
        return "cms/basedata/repayPlan/makeRepayPlan";
    }
    /**
     * 跳转到还款计划页面（只用于选择）
     *
     * @param repayPlanVO
     * @param model
     * @return
     */
    @GetMapping("/toRepayPlanSave")
    public String toRepayPlanSave(RepayPlanVO repayPlanVO, Model model) {
        model.addAttribute("repayPlan", repayPlanVO);
        return "cms/basedata/repayPlan/saveRepayPlan";
    }
    /**
     * 加载还款计划数据
     *
     * @param repayPlanVo
     * @return
     */
    @PostMapping("/getRepayMentScheduleByBillCode")
    @ResponseBody
    public List<RepaymentScheduleVO> listRepayMentSchedule(RepayPlanVO repayPlanVo) {
        List<RepaymentScheduleVO> list = repaymentScheduleService.listRepayMentSchedule(repayPlanVo);
        return list;
    }
    /**
     * 查询还款计划明细并计算是否可编辑
     *
     * @param repayPlanVo
     * @return
     */
    @PostMapping("/getRepayMentScheduleDtl")
    @ResponseBody
    public List<RepaymentScheduleDtlVO> listRepayMentScheduleDtl(RepayPlanVO repayPlanVo) {
        List<RepaymentScheduleDtlVO> list = repaymentScheduleService.getRepaymentScheduleDtlByPlanId(repayPlanVo);
        return list;
    }
    /**
     * 根据id来查询还款计划主表（还款开始日期，还款周期）
     *
     * @param planId
     * @return
     */
    @PostMapping("/getRepayMentScheduleById")
    @ResponseBody
    public AjaxJson isBudget(String planId) {
        AjaxJson json = new AjaxJson();
        if (StringUtils.isNotEmpty(planId)) {
            CmsRepaymentSchedule cms = cmsRepaymentScheduleService.findById(planId);
            Map<String, Object> attributes = new HashMap<String, Object>();
            attributes.put("beginDate", DateUtils.date2Str(cms.getRepayBeginDate(), DateUtils.date_sdf()));
            attributes.put("repayCycel", cms.getRepayCycel());
            json.setAttributes(attributes);
            json.setSuccess(true);
        }
        return json;
    }
    /**
     * 保存还款计划
     *
     * @param rp
     * @return
     */
    @LogMessage(content = "添加RepaymentSchedule", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(RepayPlanVO rp) {
        AjaxJson json = success();
        json.setObj(repaymentScheduleService.save(rp, getCurrentUser()));
        return json;
    }

    /**
     * 查询审批中还款计划明细并计算是否可编辑
     * @param repayPlanVo
     * @return
     */
    @PostMapping("/getApproveRepayMentScheduleDtl")
    @ResponseBody
    public List<RepaymentScheduleDtlVO> listApproveRepayMentScheduleDtl(RepayPlanVO repayPlanVo) {
        List<RepaymentScheduleDtlVO> list = repaymentScheduleService.getRepaymentScheduleDtlByPlanId(repayPlanVo,"11");
        return list;
    }

    /**
     * 前端可传递最大list
     */
    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.setAutoGrowCollectionLimit(2000);
    }
}
