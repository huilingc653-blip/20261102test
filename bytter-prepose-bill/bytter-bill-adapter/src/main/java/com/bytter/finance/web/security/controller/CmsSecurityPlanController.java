package com.bytter.finance.web.security.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.security.entity.CmsSecurityInfo;
import com.bytter.finance.domain.security.entity.CmsSecurityIssue;
import com.bytter.finance.domain.security.entity.CmsSecurityPlan;
import com.bytter.finance.domain.security.service.CmsSecurityInfoService;
import com.bytter.finance.domain.security.service.CmsSecurityIssueService;
import com.bytter.finance.domain.security.service.CmsSecurityPlanDetailService;
import com.bytter.finance.domain.security.service.CmsSecurityPlanService;
import com.bytter.finance.domain.security.vo.CmsSecurityIssueVO;
import com.bytter.finance.domain.security.vo.CmsSecurityPlanDetailVO;
import com.bytter.finance.domain.security.vo.CmsSecurityPlanVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
/**
 * 资产证券化计划
 *
 * @author tongqr
 */
@Controller
@RequestMapping("/cms/security/plan")
public class CmsSecurityPlanController extends BaseController {
    @Autowired
    private CmsSecurityPlanService cmsSecurityPlanService;
    @Autowired
    private CmsSecurityPlanDetailService cmsSecurityPlanDetailService;
    @Autowired
    private CmsSecurityIssueService cmsSecurityIssueService;
    @Autowired
    private CmsSecurityInfoService cmsSecurityInfoService;
    /**
     * 资产证券化登记主信息表跳转
     *
     * @return 页面路径
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_PLAN_QUERY)
    @GetMapping("/list")
    public String list() {
        return "cms/security/listSecurityInfoForPlan";
    }
    /**
     * 资产证券化修改主信息表跳转
     *
     * @return 页面路径
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_PLAN_UPDATE_QUERY)
    @GetMapping("/listUpdate")
    public String listUpdate() {
        return "cms/security/listSecurityInfoForPlanUpdate";
    }
    /**
     * 登记列表 - 数据
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_PLAN_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput dataTablesInput) {
        List<String> bizStatuss = new ArrayList<>();
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_96);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_31);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS__3);
        DataTablesOutput output = cmsSecurityPlanService.getDataTableInfo(dataTablesInput, this.getCurrentUser(), bizStatuss);
        return output;
    }
    /**
     * 修改列表 - 数据
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_PLAN_QUERY)
    @PostMapping("/datatableupdate")
    @ResponseBody
    public DataTablesOutput datatableupdate(@RequestBody DataTablesInput dataTablesInput) {
        List<String> bizStatuss = new ArrayList<>();
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_97);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_41);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS__4);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_98);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_55);
        DataTablesOutput output = cmsSecurityPlanService.getupDataTableInfo(dataTablesInput, this.getCurrentUser(), bizStatuss);
        return output;
    }
    /**
     * 资产证券化发行跳转
     *
     * @return 页面路径
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_PLAN_QUERY)
    @GetMapping("/issueList/{id}")
    public String issueList(@PathVariable("id") String id, Model model) {
        model.addAttribute("infoId", id);
        return "cms/security/listSecurityIssuePlan";
    }
    /**
     * 资产证券化发行跳转
     *
     * @return 页面路径
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_PLAN_UPDATE_QUERY)
    @GetMapping("/issueListUpdate/{id}")
    public String issueListUpdate(@PathVariable("id") String id, Model model) {
        model.addAttribute("infoId", id);
        return "cms/security/listSecurityIssuePlanUpdate";
    }
    /**
     * 发行列表 - 数据
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_PLAN_QUERY)
    @PostMapping("/issueDatatable")
    @ResponseBody
    public List<CmsSecurityIssueVO> issueDatatable(String infoId) {
        List<String> bizStatuss = new ArrayList<>();
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_96);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_31);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS__3);
        return cmsSecurityIssueService.getSuccessIssueForInfoId(infoId, bizStatuss);
    }
    /**
     * 发行列表 - 数据
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_PLAN_UPDATE_QUERY)
    @PostMapping("/issueDatatableUpdate")
    @ResponseBody
    public List<CmsSecurityIssueVO> issueDatatableUpdate(String infoId) {
        List<String> bizStatuss = new ArrayList<>();
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_97);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_41);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS__4);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_98);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_55);
        return cmsSecurityIssueService.getSuccessIssueForInfoId(infoId, bizStatuss);
    }
    /**
     * 获取资产证券化计划新增页路径
     *
     * @return
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_PLAN_OP)
    @GetMapping("/add/{id}")
    public String add(@PathVariable("id") String id, Model model) {
        CmsSecurityIssue issue = cmsSecurityIssueService.get(id);
        model.addAttribute("issueMoney", issue.getThisIssueMoney());
        model.addAttribute("securityIssueCode", issue.getSecurityIssueCode());
        if (!EmptyUtils.isEmpty(issue)) {
            CmsSecurityInfo info = cmsSecurityInfoService.get(issue.getSecurityInfoId());
            model.addAttribute("corpId", info.getCorpId());
        }
        return "cms/security/addSecurityPlanDetail";
    }
    /**
     * 获取资产证券化计划修改页路径
     *
     * @return
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_PLAN_UPDATE_OP)
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        CmsSecurityIssue issue = cmsSecurityIssueService.get(id);
        CmsSecurityPlanVO planVO = cmsSecurityPlanService.getPlanBySecurityCode(issue.getSecurityIssueCode());
        model.addAttribute("planId", planVO.getId());
        model.addAttribute("issueMoney", issue.getThisIssueMoney());
        model.addAttribute("securityIssueCode", issue.getSecurityIssueCode());
        if (!EmptyUtils.isEmpty(issue)) {
            CmsSecurityInfo info = cmsSecurityInfoService.get(issue.getSecurityInfoId());
            model.addAttribute("corpId", info.getCorpId());
        }
        return "cms/security/addSecurityPlanDetail";
    }
    /**
     * 加载计划明细数据
     *
     * @param securityIssueCode
     * @return
     */
    @PostMapping("/getPlanDetailBysecurityIssueCode")
    @ResponseBody
    public List<CmsSecurityPlanDetailVO> listPlanDetail(String securityIssueCode) {
        //计划明细
        return cmsSecurityPlanDetailService.getPlanDetailByCode(securityIssueCode);
    }
    /**
     * 资产证券化计划登记详情页
     *
     * @param id    id
     * @param model model
     * @return 前端页面路径
     */
    @LogMessage(content = "查询资产证券化登记信息详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        CmsSecurityIssue issue = cmsSecurityIssueService.get(id);
        model.addAttribute("securityIssueCode", issue.getSecurityIssueCode());
        return "cms/security/viewSecurityPlan";
    }
    /**
     * 计划登记/修改保存
     */
    @LogMessage(content = "计划登记保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsSecurityPlanVO vo, String isPass) {
        cmsSecurityPlanService.Save(vo, this.getCurrentUser(), isPass);
        return success();
    }
    /**
     * 跳转至审核列表
     *
     * @return String
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/security/approve/listSecurityPlanApprove";
    }
    /**
     * 跳转至审核列表
     *
     * @return String
     */
    @GetMapping("/listUpdateApprove")
    public String listUpdateApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/security/approve/listSecurityPlanUpdateApprove";
    }
    /**
     * 信息审批列表 - 数据
     *
     * @param input 前端传参
     */
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput datatableApprove(@RequestBody DataTablesInput input) {
        return cmsSecurityPlanService.dataTableApprove(input);
    }
    /**
     * 信息审批列表 - 数据
     *
     * @param input 前端传参
     */
    @PostMapping("/dataTableUpdateApprove")
    @ResponseBody
    public DataTablesOutput datatableUpdateApprove(@RequestBody DataTablesInput input) {
        return cmsSecurityPlanService.dataTableUpdateApprove(input);
    }
    /**
     * 跳转至审核详情界面
     *
     * @param businessId id
     * @param taskId     审批任务id
     * @param model      模型
     * @return 前端页面路径
     */
    @GetMapping("/approve")
    public String approve(String businessId, String taskId, Model model) {
        model.addAttribute("taskId", taskId);
        CmsSecurityPlan cmsSecurityPlan = cmsSecurityPlanService.get(businessId);
        model.addAttribute("securityIssueCode", cmsSecurityPlan.getSecurityIssueCode());
        return "cms/security/approve/viewSecurityPlanApprove";
    }
}
