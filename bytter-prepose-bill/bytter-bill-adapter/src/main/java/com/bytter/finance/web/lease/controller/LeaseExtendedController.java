package com.bytter.finance.web.lease.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.basedata.service.LoanRateMaintenceService;
import com.bytter.finance.domain.lease.entity.CmsFinancingLeaseInfo;
import com.bytter.finance.domain.lease.entity.LeaseExtend;
import com.bytter.finance.domain.lease.service.CmsFinancingLeaseInfoService;
import com.bytter.finance.domain.lease.service.LeaseExtendService;
import com.bytter.finance.domain.lease.vo.LeaseExtendVO;
import com.bytter.finance.enums.CmsLeaseEnum;
import com.bytter.system.api.dto.TbRateMaintenceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/cms/LeaseExtend")
public class LeaseExtendedController extends BaseController {
    @Autowired
    private LeaseExtendService leaseExtendService;
    @Autowired
    CmsFinancingLeaseInfoService cmsFinancingLeaseInfoService;
    @Autowired
    private LoanRateMaintenceService loanRateMaintenceService;
    /**
     * 融资展期申请列表
     *
     * @return
     */
    @LogMessage(content = "跳转融资租赁展期页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list() {
        return "cms/lease/listLeaseExtend";
    }
    /**
     * 融资展期申请列表 - leaseExtendService
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput dataTablesInput) {
        return leaseExtendService.getDataLeaseTables(dataTablesInput, getCurrentUser());
    }
    /*
     * 融资租赁展期
     */
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = cmsFinancingLeaseInfoService.get(id, true);
        TbRateMaintenceDTO tbLoanRateMaintence = leaseExtendService.getLoanRateMaintence(cmsFinancingLeaseInfo);
        if (null != tbLoanRateMaintence) {
            model.addAttribute("tbLoanRateMaintence", tbLoanRateMaintence);
        }
        model.addAttribute("leaseId", id);
        model.addAttribute("leaseInfo", cmsFinancingLeaseInfo);
        return "cms/lease/updateleaseExtend";
    }
    /*
     * 融资租赁展期
     */
    @LogMessage(content = "融资租赁展期", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(LeaseExtendVO leaseExtendVO) {
        if (leaseExtendVO.getStatus() == 0) {// 暂存
            leaseExtendVO.setStatus(CmsConstant.TEMP);
            leaseExtendService.modifyLeaseExtend(leaseExtendVO, getCurrentUser());
        } else if (leaseExtendVO.getStatus() == 1) {// 提交
            if (cmsFinancingLeaseInfoService.get(leaseExtendVO.getLeaseId()).getControlStatus().equals("Y")) {// 验证当前合同是否被其他业务占用
                throw new BusinessException(CmsLeaseEnum.LEASE_EXTENDY_CHECK); // 当前合同被其他业务占用，不可展期
            } else {
                leaseExtendVO.setStatus(CmsConstant.COMMITAPPROVE);
                leaseExtendService.modifyLeaseExtend(leaseExtendVO, getCurrentUser());
            }
        }
        return success();
    }
    /*
     * 融资租赁展期编辑
     */
    @GetMapping("/extendUpdate/{id}")
    public String extendUpdate(@PathVariable("id") String id, Model model) {
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = cmsFinancingLeaseInfoService.get(id, true);
        TbRateMaintenceDTO tbLoanRateMaintence = leaseExtendService.getLoanRateMaintence(cmsFinancingLeaseInfo);
        if (null != tbLoanRateMaintence) {
            model.addAttribute("tbLoanRateMaintence", tbLoanRateMaintence);
        }
        LeaseExtendVO leaseExtendVO = leaseExtendService.getExtend(id);
        model.addAttribute("leaseExtendVO", leaseExtendVO);
        model.addAttribute("leaseId", id);
        model.addAttribute("leaseInfo", cmsFinancingLeaseInfo);
        return "cms/lease/updateleaseExtendEdit";
    }
    /*
     * 融资租赁展期
     */
    @LogMessage(content = "融资租赁展期编辑", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/extendEdit")
    @ResponseBody
    public AjaxJson extendEdit(LeaseExtendVO leaseExtendVO) {
        if (leaseExtendVO.getStatus() == 0) {// 暂存
            leaseExtendVO.setStatus(CmsConstant.TEMP);
            leaseExtendService.editLeaseExtend(leaseExtendVO, getCurrentUser());
        } else if (leaseExtendVO.getStatus() == 1) {// 提交
            if (cmsFinancingLeaseInfoService.get(leaseExtendVO.getLeaseId()).getControlStatus().equals("Y")) {// 验证当前合同是否被其他业务占用
                throw new BusinessException(CmsLeaseEnum.LEASE_EXTENDY_CHECK); // 当前合同被其他业务占用，不可展期
            } else {
                leaseExtendVO.setStatus(CmsConstant.COMMITAPPROVE);
                leaseExtendService.editLeaseExtend(leaseExtendVO, getCurrentUser());
            }
        }
        return success();
    }
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        LeaseExtend leaseExtend = leaseExtendService.get(id);
        leaseExtend.setStatus(CmsConstant.DELETED);
        leaseExtendService.updateEntity(leaseExtend);
        return success();
    }
    /*
     * 查看日志跳转
     */
    @GetMapping("/viewlistMsg/{id}")
    public String viewlistMsg(@PathVariable("id") String id, Model model) {
        model.addAttribute("leaseId", id);
        return "cms/lease/viewLeaseMsgData";
    }
    /**
     * 日志列表
     */
    @PostMapping("/listMsgData")
    @ResponseBody
    public DataTablesOutput listMsgData(@RequestBody DataTablesInput dataTablesInput, String leaseId) {
        return leaseExtendService.getDataListMsgData(dataTablesInput, leaseId);
    }
    /*
     * 审批列表
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/lease/listApproveLeaseExtend";
    }
    /**
     * 列表查询
     *
     * @param input
     * @return
     */
    @ResponseBody
    @PostMapping("/approveDataTable")
    public DataTablesOutput listInlInfoApprove(@RequestBody DataTablesInput input) {
        return leaseExtendService.approveDataTable(input, getCurrentUser());
    }
    /**
     * 查看审批页面跳转
     *
     * @return
     */
    @GetMapping("/viewApprove")
    public String viewApprove(String id, String taskId, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("taskId", taskId);
        return "cms/lease/viewApproveLeaseExtend";
    }
    /*
     * 查看审批租赁物
     */
    @GetMapping("/viewApproveDate")
    public String view(String id, @RequestParam(required = false) String flag, Model model) {
        LeaseExtendVO leaseExtendVO = leaseExtendService.queryLeaseObjectInfo(id);
        model.addAttribute("flag", flag);
        model.addAttribute("leaseExtendVO", leaseExtendVO);
        return "cms/lease/viewLeaseExtend";
    }
}
