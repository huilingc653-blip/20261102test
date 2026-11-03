package com.bytter.finance.web.lease.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.InlConstant;
import com.bytter.finance.domain.lease.entity.LeaseRefundInfo;
import com.bytter.finance.domain.lease.service.CmsFinancingLeaseInfoService;
import com.bytter.finance.domain.lease.service.CmsLeaseAfterRefundService;
import com.bytter.finance.domain.lease.vo.CmsAfterLeaseInfoVO;
import com.bytter.finance.domain.lease.vo.LeaseRefundUpdateVO;
import com.bytter.finance.domain.lease.vo.LeaseRefundVO;
import com.bytter.finance.enums.CmsLeaseEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * 融资租赁退租
 */
@Controller
@RequestMapping("/cms/afterLease/refund")
public class CmsRefundController extends BaseController {
    @Autowired
    CmsFinancingLeaseInfoService cmsFinancingLeaseInfoService;
    @Autowired
    private CmsLeaseAfterRefundService cmsLeaseAfterRefundService;
    /**
     * 融资租赁退租列表-页面跳转
     */
    @LogMessage(content = "融资租赁退租列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/lease/listLeaseRefundInfo";
    }
    /**
     * 融资退租列表 - 数据
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput dataTablesInput) {
        return cmsLeaseAfterRefundService.getDataLeaseTables(dataTablesInput, getCurrentUser());
    }
    /**
     * 保存数据
     *
     * @return
     * @Param
     */
    @LogMessage(content = "退租信息保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(LeaseRefundInfo entity) {
        if (entity.getStatus() == 0) {// 暂存
            entity.setStatus(InlConstant.TMP);
            cmsLeaseAfterRefundService.saveLeaseRefundInfo(entity, getCurrentUser());
        } else if (entity.getStatus() == 1) {// 提交
            if (cmsFinancingLeaseInfoService.get(entity.getLeaseId()).getControlStatus().equals("Y")) {
                throw new BusinessException(CmsLeaseEnum.CMS_BASEDATA_CHECK);
            } else {
                entity.setStatus(InlConstant.COMMIT_APPROVE);
                cmsLeaseAfterRefundService.saveLeaseRefundInfo(entity, getCurrentUser());
            }
        }
        return success();
    }
    /**
     * 退租暂存保存
     *
     * @param entity
     * @return
     */
    @LogMessage(content = "退租信息保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/refundEdit")
    @ResponseBody
    public AjaxJson update(LeaseRefundInfo entity) {
        if (entity.getStatus() == 0) {// 暂存
            entity.setStatus(InlConstant.TMP);
            cmsLeaseAfterRefundService.updateLeaseRefundInfo(entity, getCurrentUser());
        } else if (entity.getStatus() == 1) {// 提交
            if (cmsFinancingLeaseInfoService.get(entity.getLeaseId()).getControlStatus().equals("Y")) {
                throw new BusinessException(CmsLeaseEnum.CMS_BASEDATA_CHECK);
            } else {
                entity.setStatus(InlConstant.COMMIT_APPROVE);
                cmsLeaseAfterRefundService.updateLeaseRefundInfo(entity, getCurrentUser());
            }
        }
        return success();
    }
    /**
     * 融资租赁退租页面跳转
     *
     * @return
     */
    @GetMapping("/refundEdit/{id}")
    public String refund(@PathVariable("id") String id, Model model) {
        model.addAttribute("leaseId", id);
        LeaseRefundInfo leaseRefundInfo = cmsLeaseAfterRefundService.get(id);
        LeaseRefundVO vo = cmsLeaseAfterRefundService.getLeaseById(leaseRefundInfo.getId());
        model.addAttribute("leaseInfo", vo);
        return "cms/lease/refundLeaseInfo";
    }
    /**
     * 退租暂存打回编辑页面
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/refundUpdate/{id}")
    public String refundUpdate(@PathVariable("id") String id, Model model) {
        model.addAttribute("leaseId", id);
        LeaseRefundInfo leaseRefundInfo = cmsLeaseAfterRefundService.get(id);
        LeaseRefundVO vo = cmsLeaseAfterRefundService.getLeaseById(leaseRefundInfo.getId());
        model.addAttribute("leaseInfo", vo);
        LeaseRefundUpdateVO updateVO = cmsLeaseAfterRefundService.getRefund(id);
        model.addAttribute("updateVO", updateVO);
        return "cms/lease/refundLeaseInfoUpdate";
    }
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String leaseId) {
        LeaseRefundInfo leaseRefundInfo = cmsLeaseAfterRefundService.get(leaseId);
        leaseRefundInfo.setStatus(CmsConstant.DELETED);
        cmsLeaseAfterRefundService.updateEntity(leaseRefundInfo);
        return success();
    }
    /**
     * 审核页面跳转
     *
     * @param flowStatus
     * @param model
     * @return
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/lease/listApproveLeaseRefundInfo";
    }
    @GetMapping("/viewApprove")
    public String viewApprove(String id, String taskId, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("taskId", taskId);
        return "cms/lease/viewApproveRefundInfo";
    }
    /**
     * 查看审批退租
     *
     * @param id
     * @param flag
     * @param model
     * @return
     */
    @GetMapping("/viewApproveDate")
    public String view(String id, @RequestParam(required = false) String flag, Model model) {
        CmsAfterLeaseInfoVO vo = cmsLeaseAfterRefundService.queryLeaseObjectInfo(id);
        model.addAttribute("flag", flag);
        model.addAttribute("cmsAfterLeaseInfoVO", vo);
        return "cms/lease/viewLeaseRefund";
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
        return cmsLeaseAfterRefundService.approveDataTable(input, getCurrentUser());
    }
}
