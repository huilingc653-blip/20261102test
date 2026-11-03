package com.bytter.finance.web.lease.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.domain.lease.entity.LeasePay;
import com.bytter.finance.domain.lease.service.CmsFinancingLeaseInfoService;
import com.bytter.finance.domain.lease.service.LeaseCapitalPlanService;
import com.bytter.finance.domain.lease.service.LeasePayService;
import com.bytter.finance.domain.lease.vo.CmsFinancingLeaseInfoVO;
import com.bytter.finance.domain.lease.vo.LeaseCapitalPlanVO;
import com.bytter.finance.domain.lease.vo.LeasePayVO;
import com.bytter.system.api.IBtBankAccService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @author mof
 * @version 1.0
 * @Title: Controller
 * @Description: 租赁租金支付表
 * @date 2022-04-15 10:07:46
 */
@Controller
@RequestMapping("/cms/lease/leasepay")
public class LeasePayController extends BaseController {
    @Autowired
    private LeasePayService leasePayService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @Autowired
    private LeaseCapitalPlanService leaseCapitalPlanService;
    @Autowired
    private CmsFinancingLeaseInfoService cmsFinancingLeaseInfoService;
    /**
     * 租金支付 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询租金支付列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/lease/leasepay/listLeasePay";
    }
    /**
     * 租金支付 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return leasePayService.getDataTablesUnion(dataTablesInput);
    }
    /**
     * 租金还款新增页面跳转
     */
    @GetMapping("/edit")
    public String edit(String tempOrSubmitSign, LeasePayVO leasePayVo, Model model, String downSign) {
        if (StringUtils.isNotEmpty(leasePayVo.getId())) {
            List<LeasePayVO> leasePayVOList = leasePayService.getByLeaseId(leasePayVo.getLeaseId());
            if (!ListUtils.isNullOrEmpty(leasePayVOList)) {
                leasePayVo = leasePayVOList.get(0);
            }
            leasePayVo.setCreateDate(new Date());
        } else {
            leasePayVo.setCreateDate(new Date());
        }
        CmsFinancingLeaseInfoVO cmsFinancingLeaseInfoVO = cmsFinancingLeaseInfoService
                .getLeaseById(leasePayVo.getLeaseId());
        LeaseCapitalPlanVO leaseCapitalPlanVO = leaseCapitalPlanService.getByLeaseId(leasePayVo.getLeaseId());
        leasePayVo.setLeasePlanId(leaseCapitalPlanVO.getId());
        String onlineSign = leasePayVo.getOnlineSign();
        model.addAttribute("onlineSign", onlineSign);
        // 最大输入金额控制
        BigDecimal sendBalance = leasePayService.completeSendBalance(leasePayVo.getLeaseId(), null);
        model.addAttribute("leaseInfoVo", cmsFinancingLeaseInfoVO);
        model.addAttribute("leasePayVo", leasePayVo);
        model.addAttribute("downSign", downSign);
        model.addAttribute("sendBalance", sendBalance);
        model.addAttribute("tempOrSubmitSign", tempOrSubmitSign);
        return "cms/lease/leasepay/editCmsLeasePay";
    }
    /**
     * 租金申请查询- 公用页面
     */
    @LogMessage(content = "租金申请查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewLeasePay")
    public String viewRepay(String leaseId, Model model) {
        if (EmptyUtils.isNotEmpty(leaseId)) {
            CmsFinancingLeaseInfoVO cmsFinancingLeaseInfoVO = cmsFinancingLeaseInfoService.getLeaseById(leaseId);
            List<LeasePayVO> leasePayVOList = leasePayService.getByLeaseId(leaseId);
            model.addAttribute("leasePayVOList", leasePayVOList);
            model.addAttribute("leaseInfoVo", cmsFinancingLeaseInfoVO);
        }
        return "cms/lease/leasepay/viewCmsLeasePay";
    }
    /**
     * 编辑租金付款
     *
     * @return
     */
    @LogMessage(content = "编辑租金付款", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson tempOrSubmit(LeasePayVO vo, String saveType, String tempOrSubmitSign, String sign) {
        leasePayService.tempOrSubmit(vo, saveType, tempOrSubmitSign, getCurrentUser(), sign);
        return success();
    }
    /******************** 审批BEGIN **********************/
    /**
     * 租金支付申请审批列表页面
     */
    @LogMessage(content = "租金支付申请审批列表页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listLeasePayApprove")
    public String listLeasePayApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/lease/approve/listLeasePayApprove";
    }
    /**
     * 租金申请审批列表 - 数据
     */
    @PostMapping("/dataTableLeasePayApprove")
    @ResponseBody
    public DataTablesOutput dataTableLeasePayApprove(@RequestBody DataTablesInput dataTablesInput) {
        return leasePayService.dataTableLeasePayApprove(dataTablesInput);
    }
    /**
     * 租金申请 详情页面
     */
    @GetMapping("/viewLeasePayApprove")
    public String viewLeasePayApprove(String businessId, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(businessId)) {
            LeasePay leasePay = leasePayService.get(businessId);
            if (StringUtils.isNotEmpty(leasePay) && StringUtils.isNotEmpty(leasePay.getLeaseId())) {
                CmsFinancingLeaseInfoVO cmsFinancingLeaseInfoVO = cmsFinancingLeaseInfoService
                        .getLeaseById(leasePay.getLeaseId());
                List<LeasePayVO> leasePayVOList = new ArrayList<>();
                LeasePayVO leasePayVO = leasePayService.getById(leasePay.getId());
                leasePayVOList.add(leasePayVO);
                model.addAttribute("leasePayVOList", leasePayVOList);
                model.addAttribute("leaseInfoVo", cmsFinancingLeaseInfoVO);
            }
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/lease/approve/viewRepayApprove";
    }
}
