package com.bytter.finance.web.lease.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.lease.entity.LeaseCapitalPlan;
import com.bytter.finance.domain.lease.service.CmsFinancingLeaseInfoService;
import com.bytter.finance.domain.lease.service.LeaseCapitalPlanDelService;
import com.bytter.finance.domain.lease.service.LeaseCapitalPlanService;
import com.bytter.finance.domain.lease.service.UpdateLeaseCapitalPlanService;
import com.bytter.finance.domain.lease.vo.CmsFinancingLeaseInfoVO;
import com.bytter.finance.domain.lease.vo.LeaseCapitalPlanDelVO;
import com.bytter.finance.domain.lease.vo.LeaseCapitalPlanVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
/**
 * @author mof
 * @version 1.0
 * @Title: Controller
 * @Description: 租赁计划修改
 * @date 2022-04-02 10:07:46
 */
@Controller
@RequestMapping("/cms/lease/updateLeaseCapitalPlan")
public class UpdateLeaseCapitalPlanController extends BaseController {
    @Autowired
    private LeaseCapitalPlanService leaseCapitalPlanService;
    @Autowired
    private LeaseCapitalPlanDelService leaseCapitalPlanDelService;
    @Autowired
    private CmsFinancingLeaseInfoService cmsFinancingLeaseInfoService;
    @Autowired
    private UpdateLeaseCapitalPlanService updateLeaseCapitalPlanService;
    /**
     * 租金计划登记表新增页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(String leaseId, Model model) {
        if (StringUtils.isNotEmpty(leaseId)) {
            CmsFinancingLeaseInfoVO cmsFinancingLeaseInfoVO = cmsFinancingLeaseInfoService.getLeaseById(leaseId);
            model.addAttribute("leaseInfoVo", cmsFinancingLeaseInfoVO);
            if (!EmptyUtils.isEmpty(cmsFinancingLeaseInfoVO)) {
                LeaseCapitalPlanVO leaseCapitalPlanVo = leaseCapitalPlanService.getByLeaseId(leaseId);
                if (!EmptyUtils.isEmpty(cmsFinancingLeaseInfoVO)) {
                    // 暂时默认是 07 自定义，等后面开放其他还款方式再删除
                    leaseCapitalPlanVo.setRepayType(CmsConstant.REPAY_TYPE_07);
                    model.addAttribute("leaseCapitalPlanVo", leaseCapitalPlanVo);
                }
            }
        }
        return "cms/lease/leasecapitalplan/updateLeaseCapitalPlan";
    }
    /**
     * 导出租金计划模板
     *
     * @param request
     * @param response
     * @param leaseCapitalPlanVO
     * @throws Exception
     */
    @GetMapping("/exportBudget")
    public void exportBudget(HttpServletRequest request, HttpServletResponse response,
            LeaseCapitalPlanVO leaseCapitalPlanVO) throws Exception {
        leaseCapitalPlanService.exportBudget(request, response, leaseCapitalPlanVO);
    }
    /******************** 审批 **********************/
    /**
     * 申请审批列表-页面跳转
     */
    @LogMessage(content = "查询借款申请审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listInfoApprove")
    public String listInfoApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/lease/approve/listUpdateLeaseCapitalPlanApprove";
    }
    /**
     * 申请审批列表 - 数据
     */
    @PostMapping("/datatableInfoApprove")
    @ResponseBody
    public DataTablesOutput datatableInfoApprove(@RequestBody DataTablesInput dataTablesInput) {
        return updateLeaseCapitalPlanService.getDataInfoApproveTables(dataTablesInput);
    }
    /**
     * 申请审批 详情页面
     */
    @GetMapping("/infoApprove")
    public String infoApprove(String businessId, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(businessId)) {
            LeaseCapitalPlan info = leaseCapitalPlanService.get(businessId);
            if (StringUtils.isNotEmpty(info) && StringUtils.isNotEmpty(info.getLeaseId())) {
                CmsFinancingLeaseInfoVO cmsFinancingLeaseInfoVO = cmsFinancingLeaseInfoService
                        .getLeaseById(info.getLeaseId());
                model.addAttribute("leaseInfoVo", cmsFinancingLeaseInfoVO);
            }
            List<LeaseCapitalPlanDelVO> leaseCapitalPlanDelVoList = new ArrayList<LeaseCapitalPlanDelVO>();
            if (StringUtils.isNotEmpty(info.getLeaseId())) {
                leaseCapitalPlanDelVoList = leaseCapitalPlanDelService.getInfoByLeaseId(info.getLeaseId());
            }
            model.addAttribute("leaseCapitalPlanDelVoList", leaseCapitalPlanDelVoList);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/lease/approve/viewUpdateLeaseCapitalPlanApprove";
    }
}
