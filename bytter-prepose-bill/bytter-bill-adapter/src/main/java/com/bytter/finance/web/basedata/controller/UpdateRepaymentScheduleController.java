package com.bytter.finance.web.basedata.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.domain.basedata.service.UpdateRepaymentScheduleService;
import com.bytter.finance.domain.basedata.vo.RepayPlanVO;
import com.bytter.finance.domain.basedata.vo.RepaymentScheduleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
/**
 * @author songkk
 * @version 1.0
 * @Title: Controller
 * @Description: 还款计划修改表
 * @date 2017-08-01 09:58:44
 */
@Controller
@RequestMapping("/cms/basedata/updaterepaymentschedule")
public class UpdateRepaymentScheduleController extends BaseController {
    @Autowired
    private UpdateRepaymentScheduleService updateRepaymentScheduleService;
    /**
     * 跳转到还款计划修改页面
     *
     * @param repayPlanVO
     * @param model
     * @return
     */
    @GetMapping("/toRepayPlanUpdate")
    public String toRepayPlanUpdate(RepayPlanVO repayPlanVO, Model model) {
        repayPlanVO = updateRepaymentScheduleService.toRepayPlanUpdate(repayPlanVO);
        if (EmptyUtils.isNotEmpty(repayPlanVO.getLoanDate())) {
            String loanStartDate = updateRepaymentScheduleService.getMaxRepayedDate(repayPlanVO);// 获取最大的还款开始日期
            Date loanDate = DateUtils.str2Date(loanStartDate, DateUtils.date_sdf);
            Date startDate = DateUtils.addDay(loanDate, 1);
            repayPlanVO.setRepayBeginDate(DateUtils.format(startDate, DateUtils.PATTERN_CLASSICAL_SIMPLE));
            model.addAttribute("repayStartDateStr", DateUtils.format(startDate, DateUtils.PATTERN_CLASSICAL_SIMPLE));
        } else {
            model.addAttribute("repayStartDateStr", repayPlanVO.getLoanDate());
        }
        model.addAttribute("repayPlan", repayPlanVO);
        return "cms/basedata/repayPlan/updateRepayPlan";
    }
    /**
     * 还款计划修改模块加载还款计划
     *
     * @param id
     * @return
     */
    @PostMapping("/listUpdateRepayPlan")
    @ResponseBody
    public List<RepaymentScheduleVO> listUpdateRepayPlan(String id) {
        List<RepaymentScheduleVO> list = updateRepaymentScheduleService.listUpdateRepayPlan(id);
        return list;
    }
    /**
     * 保存还款计划修改数据
     *
     * @param rp
     * @return
     */
    @LogMessage(content = "添加RepaymentSchedule", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/updateRepayPlan")
    @ResponseBody
    public AjaxJson updateRepayPlan(RepayPlanVO rp) throws ParseException {
        AjaxJson json = success();
        json.setObj(updateRepaymentScheduleService.updateRepayPlan(rp, getCurrentUser()));
        return json;
    }
}
