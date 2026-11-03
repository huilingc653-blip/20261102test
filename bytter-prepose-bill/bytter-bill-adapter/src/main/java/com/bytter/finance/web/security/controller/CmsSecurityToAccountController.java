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
import com.bytter.finance.domain.security.entity.CmsSecurityToAccount;
import com.bytter.finance.domain.security.service.CmsSecurityInfoService;
import com.bytter.finance.domain.security.service.CmsSecurityIssueService;
import com.bytter.finance.domain.security.service.CmsSecurityToAccountService;
import com.bytter.finance.domain.security.vo.CmsSecurityIssueVO;
import com.bytter.finance.domain.security.vo.CmsSecurityToAccountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
/**
 * 资产证券化到账
 *
 * @author tongqr
 */
@Controller
@RequestMapping("/cms/security/toAccount")
public class CmsSecurityToAccountController extends BaseController {
    @Autowired
    private CmsSecurityToAccountService cmsSecurityToAccountService;
    @Autowired
    private CmsSecurityInfoService cmsSecurityInfoService;
    @Autowired
    private CmsSecurityIssueService cmsSecurityIssueService;
    /**
     * 资产证券化登记主信息表跳转
     *
     * @return 页面路径
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_TOACCOUNT_QUERY)
    @GetMapping("/list")
    public String list() {
        return "cms/security/listSecurityInfoFortoAccount";
    }
    /**
     * 登记列表 - 数据
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_TOACCOUNT_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput dataTablesInput) {
        List<String> bizStatuss = new ArrayList<>();
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_95);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_21);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_25);
        DataTablesOutput out = cmsSecurityToAccountService.getDataTableInfo(dataTablesInput, getCurrentUser(), bizStatuss);
        return out;
    }
    /**
     * 资产证券化发行跳转
     *
     * @return 页面路径
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_TOACCOUNT_QUERY)
    @GetMapping("/issueList/{id}")
    public String issueList(@PathVariable("id") String id, Model model) {
        model.addAttribute("infoId", id);
        return "cms/security/listSecurityIssueToAccount";
    }
    /**
     * 发行列表 - 数据
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_TOACCOUNT_QUERY)
    @PostMapping("/issueDatatable")
    @ResponseBody
    public List<CmsSecurityIssueVO> datatable(String infoId) {
        List<String> bizStatuss = new ArrayList<>();
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_95);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_21);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_25);
        return cmsSecurityIssueService.getSuccessIssueForInfoId(infoId, bizStatuss);
    }
    /**
     * 获取资产证券化到账确认新增页路径
     *
     * @return
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_TOACCOUNT_OP)
    @GetMapping("/add/{id}")
    public String add(@PathVariable("id") String id, Model model) {
        CmsSecurityIssue issue = cmsSecurityIssueService.get(id);
        model.addAttribute("Issue", issue);
        if (!EmptyUtils.isEmpty(issue)) {
            CmsSecurityInfo info = cmsSecurityInfoService.get(issue.getSecurityInfoId());
            model.addAttribute("info", info);
        }
        return "cms/security/addSecurityTOAccount";
    }
    /**
     * 新增到账确认信息
     *
     * @param cmsSecurityToAccount 到账确认信息
     * @return 是否新增成功
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_INFO_ADD)
    @LogMessage(content = "新增到账确认信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsSecurityToAccount cmsSecurityToAccount) {
        cmsSecurityToAccountService.save(cmsSecurityToAccount, this.getCurrentUser());
        return success();
    }
    /**
     * 资产证券化到账信息详情页
     *
     * @param id    id
     * @param model model
     * @return 前端页面路径
     */
    @LogMessage(content = "查询资产证券化登记信息详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/query/{id}")
    public String query(@PathVariable("id") String id, Model model) {
        CmsSecurityIssue issue = cmsSecurityIssueService.get(id);
        List<CmsSecurityToAccountVO> toAccountVOS = cmsSecurityToAccountService.getIssueByCode(issue.getSecurityIssueCode());
        model.addAttribute("toAccountVOS", toAccountVOS);
        return "cms/security/viewSecurityToAccountList";
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
        return "cms/security/approve/listSecurityToAccountApprove";
    }
    /**
     * 资产证券化登记信息审批列表 - 数据
     *
     * @param input 前端传参
     */
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput datatableInfoApprove(@RequestBody DataTablesInput input) {
        return cmsSecurityToAccountService.dataTableApprove(input);
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
        CmsSecurityToAccount toAccount = cmsSecurityToAccountService.get(businessId);
        CmsSecurityIssue Issue = cmsSecurityIssueService.getSecurityIssueByCode(toAccount.getSecurityIssueCode(), null).get(0);
        model.addAttribute("toAccount", toAccount);
        model.addAttribute("Issue", Issue);
        return "cms/security/approve/viewSecurityToAccountApprove";
    }
}
