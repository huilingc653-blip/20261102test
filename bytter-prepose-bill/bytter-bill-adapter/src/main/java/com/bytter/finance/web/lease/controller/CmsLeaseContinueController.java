package com.bytter.finance.web.lease.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.constant.InlConstant;
import com.bytter.finance.domain.lease.entity.LeaseContinueInfo;
import com.bytter.finance.domain.lease.service.CmsFinancingLeaseInfoService;
import com.bytter.finance.domain.lease.service.CmsLeaseAfterContinueService;
import com.bytter.finance.domain.lease.vo.CmsAfterLeaseInfoVO;
import com.bytter.finance.enums.CmsLeaseEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * 融资租赁续租
 */
@Controller
@RequestMapping("/cms/afterLease/continue")
public class CmsLeaseContinueController extends BaseController {
    @Autowired
    CmsFinancingLeaseInfoService cmsFinancingLeaseInfoService;
    @Autowired
    private CmsLeaseAfterContinueService cmsLeaseAfterContinueService;
    /**
     * 融资续租列表-页面跳转
     */
    @LogMessage(content = "融资续租列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/lease/listLeaseContinueInfo";
    }
    /**
     * 融资续租列表 - 数据
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput out = cmsLeaseAfterContinueService.getDataLeaseTables(dataTablesInput, getCurrentUser());
        return out;
    }
    /**
     * 融资租赁续租页面跳转
     *
     * @return
     */
    @GetMapping("/contLease/{id}")
    public String contLease(@PathVariable("id") String id, Model model) {
        model.addAttribute("leaseId", id);
        LeaseContinueInfo info = cmsLeaseAfterContinueService.get(id);
        CmsAfterLeaseInfoVO vo = cmsLeaseAfterContinueService.getLeaseById(info.getId());
        model.addAttribute("leaseInfo", vo);
        return "cms/lease/continueLeaseInfo";
    }
    /**
     * 续租暂存打回编辑页面
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/continueUpdate/{id}")
    public String refundUpdate(@PathVariable("id") String id, Model model) {
        model.addAttribute("leaseId", id);
        LeaseContinueInfo leaseContinueInfo = cmsLeaseAfterContinueService.get(id);
        CmsAfterLeaseInfoVO vo = cmsLeaseAfterContinueService.getLeaseById(leaseContinueInfo.getId());
        model.addAttribute("leaseInfo", vo);
        CmsAfterLeaseInfoVO updateVO = cmsLeaseAfterContinueService.getContinue(id);
        model.addAttribute("updateVO", updateVO);
        return "cms/lease/continueLeaseInfoUpdate";
    }
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/lease/listApproveLeaseContinueInfo";
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
        return cmsLeaseAfterContinueService.approveDataTable(input, getCurrentUser());
    }
    /**
     * 保存数据
     *
     * @return
     * @Param
     */
    @LogMessage(content = "续租信息保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(LeaseContinueInfo entity) {
        if (entity.getStatus() == 0) {// 暂存
            entity.setStatus(InlConstant.TMP);
            cmsLeaseAfterContinueService.saveLeaseContinueInfo(entity, getCurrentUser());
        } else if (entity.getStatus() == 1) {// 提交
            if (cmsFinancingLeaseInfoService.get(entity.getLeaseId()).getControlStatus().equals("Y")) {
                throw new BusinessException(CmsLeaseEnum.CMS_BASEDATA_CHECK);
            } else {
                entity.setStatus(InlConstant.COMMIT_APPROVE);
                cmsLeaseAfterContinueService.saveLeaseContinueInfo(entity, getCurrentUser());
            }
        }
        return success();
    }
    /**
     * 修改数据
     *
     * @return
     * @Param
     */
    @LogMessage(content = "续租信息修改", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/updateContinue")
    @ResponseBody
    public AjaxJson update(LeaseContinueInfo entity) {
        if (entity.getStatus() == 0) {// 暂存
            entity.setStatus(InlConstant.TMP);
            cmsLeaseAfterContinueService.updateLeaseContinueInfo(entity, getCurrentUser());
        } else if (entity.getStatus() == 1) {// 提交
            if (cmsFinancingLeaseInfoService.get(entity.getLeaseId()).getControlStatus().equals("Y")) {
                throw new BusinessException(CmsLeaseEnum.CMS_BASEDATA_CHECK);
            } else {
                entity.setStatus(InlConstant.COMMIT_APPROVE);
                cmsLeaseAfterContinueService.updateLeaseContinueInfo(entity, getCurrentUser());
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
        return "cms/lease/viewApproveContinueInfo";
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
        CmsAfterLeaseInfoVO vo = cmsLeaseAfterContinueService.queryLeaseContinueInfo(id);
        model.addAttribute("flag", flag);
        model.addAttribute("cmsAfterLeaseInfoVO", vo);
        return "cms/lease/viewLeaseContinue";
    }
    /**
     * 查看日志界面
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/viewlistMsg/{id}")
    public String viewlistMsg(@PathVariable("id") String id, Model model) {
        model.addAttribute("leaseId", id);
        return "cms/lease/viewLeaseContMsgData";
    }
    /**
     * 日志列表
     */
    @PostMapping("/listMsgData")
    @ResponseBody
    public DataTablesOutput listMsgData(@RequestBody DataTablesInput dataTablesInput, String leaseId) {
        return cmsLeaseAfterContinueService.getDataListMsgData(dataTablesInput, leaseId);
    }
}
