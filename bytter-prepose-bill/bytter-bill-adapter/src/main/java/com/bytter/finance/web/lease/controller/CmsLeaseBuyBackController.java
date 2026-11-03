package com.bytter.finance.web.lease.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.constant.InlConstant;
import com.bytter.finance.domain.lease.entity.LeaseBuyBackInfo;
import com.bytter.finance.domain.lease.service.CmsFinancingLeaseInfoService;
import com.bytter.finance.domain.lease.service.CmsLeaseAfterBuyBackService;
import com.bytter.finance.domain.lease.vo.CmsAfterLeaseInfoVO;
import com.bytter.finance.enums.CmsLeaseEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * 融资租赁退租
 */
@Controller
@RequestMapping("/cms/afterLease/buyBack")
public class CmsLeaseBuyBackController extends BaseController {
    @Autowired
    CmsFinancingLeaseInfoService cmsFinancingLeaseInfoService;
    @Autowired
    private CmsLeaseAfterBuyBackService cmsLeaseAfterBuyBackService;
    /**
     * 融资租赁申请列表-页面跳转
     */
    @LogMessage(content = "查询融资租赁回购列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/lease/listLeaseBuyBackInfo";
    }
    /**
     * 融资租赁回购列表 - 数据
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput dataTablesInput) {
        return cmsLeaseAfterBuyBackService.getDataLeaseTables(dataTablesInput, getCurrentUser());
    }
    /**
     * 融资租赁回购页面跳转
     *
     * @return
     */
    @GetMapping("/repoLease/{id}")
    public String refund(@PathVariable("id") String id, Model model) {
        model.addAttribute("leaseId", id);
        LeaseBuyBackInfo leaseBuyBackInfo = cmsLeaseAfterBuyBackService.get(id);
        CmsAfterLeaseInfoVO vo = cmsLeaseAfterBuyBackService.getLeaseById(leaseBuyBackInfo.getId());
        model.addAttribute("leaseInfo", vo);
        return "cms/lease/buyBackLeaseInfo";
    }
    /**
     * 回购暂存打回编辑页面
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/repoUpdate/{id}")
    public String repoUpdate(@PathVariable("id") String id, Model model) {
        model.addAttribute("leaseId", id);
        LeaseBuyBackInfo leaseBuyBackInfo = cmsLeaseAfterBuyBackService.get(id);
        CmsAfterLeaseInfoVO vo = cmsLeaseAfterBuyBackService.getLeaseById(leaseBuyBackInfo.getId());
        model.addAttribute("leaseInfo", vo);
        CmsAfterLeaseInfoVO updateVO = cmsLeaseAfterBuyBackService.getRepo(id);
        model.addAttribute("updateVO", updateVO);
        return "cms/lease/buyBackLeaseInfoUpdate";
    }
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/lease/listApproveLeaseBuyBackInfo";
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
        return cmsLeaseAfterBuyBackService.approveDataTable(input, getCurrentUser());
    }
    /**
     * 保存数据
     *
     * @return
     * @Param
     */
    @LogMessage(content = "回购信息保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(LeaseBuyBackInfo entity) {
        if (entity.getStatus() == 0) {// 暂存
            entity.setStatus(InlConstant.TMP);
            cmsLeaseAfterBuyBackService.saveLeaseBuyBackInfo(entity, getCurrentUser());
        } else if (entity.getStatus() == 1) {// 提交
            if (cmsFinancingLeaseInfoService.get(entity.getLeaseId()).getControlStatus().equals("Y")) {
                throw new BusinessException(CmsLeaseEnum.CMS_BASEDATA_CHECK);
            } else {
                entity.setStatus(InlConstant.COMMIT_APPROVE);
                cmsLeaseAfterBuyBackService.saveLeaseBuyBackInfo(entity, getCurrentUser());
            }
        }
        return success();
    }
    /**
     * 暂存数据
     *
     * @return
     * @Param
     */
    @LogMessage(content = "回购信息保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/repoUpdate")
    @ResponseBody
    public AjaxJson update(LeaseBuyBackInfo entity) {
        if (entity.getStatus() == 0) {// 暂存
            entity.setStatus(InlConstant.TMP);
            cmsLeaseAfterBuyBackService.updateLeaseBuyBackInfo(entity, getCurrentUser());
        } else if (entity.getStatus() == 1) {// 提交
            if (cmsFinancingLeaseInfoService.get(entity.getLeaseId()).getControlStatus().equals("Y")) {
                throw new BusinessException(CmsLeaseEnum.CMS_BASEDATA_CHECK);
            } else {
                entity.setStatus(InlConstant.COMMIT_APPROVE);
                cmsLeaseAfterBuyBackService.updateLeaseBuyBackInfo(entity, getCurrentUser());
            }
        }
        return success();
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
        return "cms/lease/viewApproveRepoInfo";
    }
    /**
     * 查看审批续租
     *
     * @param id
     * @param flag
     * @param model
     * @return
     */
    @GetMapping("/viewApproveDate")
    public String view(String id, @RequestParam(required = false) String flag, Model model) {
        CmsAfterLeaseInfoVO vo = cmsLeaseAfterBuyBackService.queryLeaseObjectInfo(id);
        model.addAttribute("flag", flag);
        model.addAttribute("cmsAfterLeaseInfoVO", vo);
        return "cms/lease/viewLeaseBuyBack";
    }
}
