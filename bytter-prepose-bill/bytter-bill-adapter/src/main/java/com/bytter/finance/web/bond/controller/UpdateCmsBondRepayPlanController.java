package com.bytter.finance.web.bond.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.bond.service.*;
import com.bytter.finance.domain.bond.vo.CmsBondIssueInfoVo;
import com.bytter.finance.domain.bond.vo.CmsBondRegisterInfoVo;
import com.bytter.finance.domain.bond.vo.CmsBondRepayPlanVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * @author mof
 * @version 1.0
 * @Title: Controller
 * @Description: 债券还款计划修改
 * @date 2022-06-09 10:07:46
 */
@Controller
@RequestMapping("/cms/bond/updatecmsbondrepayplan")
public class UpdateCmsBondRepayPlanController extends BaseController {
    @Autowired
    private CmsBondRepayPlanService cmsBondRepayPlanService;
    @Autowired
    private CmsBondIssueInfoService cmsBondIssueInfoService;
    @Autowired
    private CmsBondRepayPlanDtlService cmsBondRepayPlanDtlService;
    @Autowired
    private CmsBondRegisterInfoService cmsBondRegisterInfoService;
    @Autowired
    private UpdateCmsBondRepayPlanService updateCmsBondRepayPlanService;
    /**
     * 债券还款计划登记表新增页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(String registerId, Model model) {
        if (StringUtils.isNotEmpty(registerId)) {
            CmsBondRegisterInfoVo cmsBondRegisterInfoVo = cmsBondRegisterInfoService.getRegisterById(registerId);
            model.addAttribute("cmsBondRegisterInfoVo", cmsBondRegisterInfoVo);
            if (!EmptyUtils.isEmpty(cmsBondRegisterInfoVo)) {
                // 债券发行信息
                CmsBondIssueInfoVo cmsBondIssueInfoVo = cmsBondIssueInfoService.getByRegisterId(registerId);
                model.addAttribute("cmsBondIssueInfoVo", cmsBondIssueInfoVo);
                // 债券计划主表信息
                CmsBondRepayPlanVo planVO = cmsBondRepayPlanService.getByIssueId(cmsBondIssueInfoVo.getId());
                if (!EmptyUtils.isEmpty(cmsBondRegisterInfoVo)) {
                    // 暂时默认是 07 自定义，等后面开放其他还款方式再删除
                    planVO.setRepayType(CmsConstant.REPAY_TYPE_07);
                    model.addAttribute("cmsBondRepayPlanVo", planVO);
                }
            }
        }
        return "cms/bond/bondRepayPlan/updateCmsBondRepayPlan";
    }
    // 审批功能取消
    // /********************审批**********************/
    // /**
    // * 申请审批列表-页面跳转
    // */
    // @LogMessage(content = "查询借款申请审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    // @GetMapping("/listApprove")
    // public String listApprove(String flowStatus, String cycleTime, Model model) {
    // model.addAttribute("flowStatus", flowStatus);
    // model.addAttribute("cycleTime", cycleTime);
    // return "cms/bond/approve/listUpdateBondRepayPlanApprove";
    // }
    //
    // /**
    // * 申请审批列表 - 数据
    // */
    // @PostMapping("/dataTableApprove")
    // @ResponseBody
    // public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
    // return updateCmsBondRepayPlanService.dataTableApprove(dataTablesInput);
    // }
    //
    // /**
    // * 申请审批 详情页面
    // */
    // @GetMapping("/viewApprove")
    // public String viewApprove(String businessId, String taskId, Model model) {
    // if (!EmptyUtils.isEmpty(businessId)) {
    // CmsBondRepayPlan info = cmsBondRepayPlanService.get(businessId);
    // if (StringUtils.isNotEmpty(info) && StringUtils.isNotEmpty(info.getRegisterId())) {
    // CmsBondRegisterInfoVo cmsFinancingLeaseInfoVO = cmsBondRegisterInfoService.getRegisterById(info.getRegisterId());
    // model.addAttribute("cmsFinancingLeaseInfoVO", cmsFinancingLeaseInfoVO);
    // }
    //
    // List<CmsBondRepayPlanDtlVo> cmsBondRepayPlanDtlVoList = new ArrayList<CmsBondRepayPlanDtlVo>();
    // if (StringUtils.isNotEmpty(info.getRegisterId())) {
    // cmsBondRepayPlanDtlVoList = cmsBondRepayPlanDtlService.getByRegisterId(info.getRegisterId());
    // }
    // model.addAttribute("leaseCapitalPlanDelVoList", cmsBondRepayPlanDtlVoList);
    // }
    // model.addAttribute("taskId", taskId);//必须要做此步骤
    // return "cms/bond/approve/viewUpdateBondRepayPlanApprove";
    // }
}
