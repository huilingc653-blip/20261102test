package com.bytter.finance.web.security.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.finance.domain.security.entity.CmsSecurityIssue;
import com.bytter.finance.domain.security.service.CmsSecurityInfoService;
import com.bytter.finance.domain.security.service.CmsSecurityIssueService;
import com.bytter.finance.domain.security.service.CmsSecurityPlanService;
import com.bytter.finance.domain.security.utils.SecurityUtils;
import com.bytter.finance.domain.security.vo.CmsSecurityInfoVO;
import com.bytter.finance.domain.security.vo.CmsSecurityIssueVO;
import com.bytter.finance.domain.security.vo.CmsSecurityLevelInfoVO;
import com.bytter.finance.domain.security.vo.CmsSecurityPlanVO;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.system.api.IBaseRateService;
import com.bytter.system.api.ICreditPeriodService;
import com.bytter.system.api.dto.TbBaseRateDTO;
import com.bytter.system.api.dto.TbCreditPeriodDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
/**
 * 资产证券化发行
 *
 * @author tongqr
 */
@Controller
@RequestMapping("/cms/security/issue")
public class CmsSecurityIssueController extends BaseController {
    @Autowired
    private CmsSecurityInfoService cmsSecurityInfoService;
    @Autowired
    private CmsSecurityIssueService cmsSecurityIssueService;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;
    @DubboReference(check = false)
    private IBaseRateService iBaseRateService;
    @DubboReference(check = false)
    private ICreditPeriodService iCreditPeriodService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupyService;
    @Resource(name = "guaPledgeOccupyServiceImpl")
    private IGuaPledgeOccupyService pledgeOccupyService;
    @Autowired
    private CmsSecurityPlanService cmsSecurityPlanService;
    /**
     * 资产证券化登记主信息表跳转
     *
     * @return 页面路径
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_ISSUE_QUERY)
    @GetMapping("/list")
    public String list() {
        return "cms/security/listSecurityInfoForissue";
    }
    /**
     * 资产证券化登记主信息表跳转
     *
     * @return 页面路径
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_ISSUEQUERY_QUERY)
    @GetMapping("/queryList")
    public String queryList() {
        return "cms/security/listSecurityInfoForQuery";
    }
    /**
     * 资产证券化发行跳转
     *
     * @return 页面路径
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_ISSUEQUERY_QUERY)
    @GetMapping("/queryIssueList/{id}")
    public String queryIssueList(@PathVariable("id") String id, Model model) {
        model.addAttribute("infoId", id);
        return "cms/security/listSecurityQueryIssue";
    }
    /**
     * 资产证券化发行跳转
     *
     * @return 页面路径
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_ISSUE_QUERY)
    @GetMapping("/issueList/{id}")
    public String issueList(@PathVariable("id") String id, Model model) {
        model.addAttribute("infoId", id);
        return "cms/security/listSecurityIssue";
    }
    /**
     * 发行列表 - 数据
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_ISSUEQUERY_QUERY)
    @PostMapping("/issueQueryDatatable")
    @ResponseBody
    public List<CmsSecurityIssueVO> issueQueryDatatable(String infoId) {
        return cmsSecurityIssueService.getIssueForInfoId(infoId, "query");
    }
    /**
     * 发行列表 - 数据
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_ISSUE_QUERY)
    @PostMapping("/issueDatatable")
    @ResponseBody
    public List<CmsSecurityIssueVO> issueDatatable(String infoId) {
        return cmsSecurityIssueService.getIssueForInfoId(infoId, null);
    }
    /**
     * 登记列表 - 数据
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_ISSUE_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput out = cmsSecurityInfoService.getDataTableForissue(dataTablesInput, getCurrentUser());
        return out;
    }
    /**
     * 登记列表 - 数据
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_ISSUEQUERY_QUERY)
    @PostMapping("/queryDatatable")
    @ResponseBody
    public DataTablesOutput queryDatatable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput out = cmsSecurityInfoService.getDataTable(dataTablesInput, this.getCurrentUser(), "query");
        return out;
    }
    /**
     * 获取资产证券化发行新增页路径
     *
     * @return
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_ISSUE_ADD)
    @GetMapping("/add")
    public String add(String infoId, Model model) {
        CmsSecurityInfoVO info = cmsSecurityInfoService.getInfoById(infoId);
        model.addAttribute("info", info);
        model.addAttribute("isHookBond", "1");
        model.addAttribute("pledgeConfig", "1");
        model.addAttribute("isGuarantee", "1");
        return "cms/security/addSecurityIssue";
    }
    @GetMapping("/addPlan")
    public String addPlan(String securityIssueCode, String issueMoney, String corpId, Model model) {
        model.addAttribute("issueMoney", issueMoney);
        model.addAttribute("securityIssueCode", securityIssueCode);
        model.addAttribute("corpId", corpId);
        model.addAttribute("isPass", "isPass");
        CmsSecurityPlanVO planVO = cmsSecurityPlanService.getPlanBySecurityCode(securityIssueCode);
        if (!EmptyUtils.isEmpty(planVO)) {
            model.addAttribute("planId", planVO.getId());
        }
        return "cms/security/addSecurityPlanDetail";
    }
    /**
     * 新增资产证券化发行信息
     *
     * @param cmsSecurityIssueVO 资产证券化发行信息
     * @return 是否新增成功
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_ISSUE_ADD)
    @LogMessage(content = "新增资产证券化发行信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsSecurityIssueVO cmsSecurityIssueVO) {
        cmsSecurityIssueService.saveSecurityIssue(cmsSecurityIssueVO, getCurrentUser());
        return success();
    }
    /**
     * 获取债券发行信息编辑页路径
     *
     * @param id    债券发行信息id
     * @param model 模型
     * @return 债券发行信息编辑页路径
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_ISSUE_EDIT)
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        CmsSecurityIssue cmsSecurityIssue = cmsSecurityIssueService.get(id, true);
//        model.addAttribute("cmsBondRepayPlanVo", cmsBondRepayPlanService.getByIssueId(cmsBondIssueInfo.getId()));
        model.addAttribute("Issue", cmsSecurityIssue);
        CmsSecurityInfoVO info = cmsSecurityInfoService.getInfoById(cmsSecurityIssue.getSecurityInfoId());
        model.addAttribute("info", info);
        SecurityUtils.setBondParam(model, "1", cmsSecurityIssue, fcBondHookService);
        SecurityUtils.setGuaParams(model, "0", "1", cmsSecurityIssue, guaContractOccupyService);
        //抵质押参数
        SecurityUtils.setPledgeParam(model, "1", cmsSecurityIssue, pledgeOccupyService);
        //第三方费率信息
        List<DfIntermediateCostVO> costList = cmsSecurityIssueService.listIntermediateCostByDfId(cmsSecurityIssue.getId());
        model.addAttribute("costVOS", costList);
        return "cms/security/updateSecurityIssue";
    }
    /**
     * 新增资产证券化发行信息
     *
     * @param cmsSecurityIssueVO 资产证券化发行信息
     * @return 是否新增成功
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_ISSUE_EDIT)
    @LogMessage(content = "新增资产证券化发行信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(CmsSecurityIssueVO cmsSecurityIssueVO) {
        cmsSecurityIssueService.modifySecurityIssue(cmsSecurityIssueVO, getCurrentUser());
        return success();
    }
    /**
     * 发行信息表详情页
     *
     * @param id    id
     * @param model model
     * @return 前端页面路径
     */
    @LogMessage(content = "查询发行信息详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        CmsSecurityIssue cmsSecurityIssue = cmsSecurityIssueService.get(id, true);
//        model.addAttribute("cmsBondRepayPlanVo", cmsBondRepayPlanService.getByIssueId(cmsBondIssueInfo.getId()));
        model.addAttribute("Issue", cmsSecurityIssue);
        CmsSecurityInfoVO info = cmsSecurityInfoService.getInfoById(cmsSecurityIssue.getSecurityInfoId());
        model.addAttribute("info", info);
        //TODO 补充利率相关信息
        Optional.ofNullable(cmsSecurityIssue).ifPresent(precativeDTO -> {
            if (StringUtils.isNotEmpty(cmsSecurityIssue.getReferenceRateType())){
                TbBaseRateDTO baseRateDTO = iBaseRateService.getBaseRateById(cmsSecurityIssue.getReferenceRateType());
                Optional.ofNullable(baseRateDTO).ifPresent(rateDTO -> model.addAttribute("tbBaseRate", rateDTO));
            }
            if (StringUtils.isNotEmpty(cmsSecurityIssue.getRepayCycle())){
                TbCreditPeriodDTO creditPeriodDTO = iCreditPeriodService.getById(cmsSecurityIssue.getRepayCycle());
                Optional.ofNullable(creditPeriodDTO).ifPresent(periodDTO -> model.addAttribute("tbCreditPeriod", periodDTO));
            }
        });
        SecurityUtils.setBondParam(model, "1", cmsSecurityIssue, fcBondHookService);
        SecurityUtils.setGuaParams(model, "1", "1", cmsSecurityIssue, guaContractOccupyService);
        //抵质押参数
        SecurityUtils.setPledgeParam(model, "1", cmsSecurityIssue, pledgeOccupyService);
        //第三方费率信息
        List<DfIntermediateCostVO> costList = cmsSecurityIssueService.listIntermediateCostByDfId(cmsSecurityIssue.getId());
        model.addAttribute("costVOS", costList);
        return "cms/security/viewSecurityIssue";
    }
    /**
     * 删除发行信息
     *
     * @param id 发行信息id
     * @return 是否删除成功
     */
//    @RequiresPermissions(ResourceConstant.CMS_SECURITY_ISSUE_DELETE)
    @LogMessage(content = "删除发行信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        cmsSecurityIssueService.deleteIssueInfo(id, this.getCurrentUser());
        return success();
    }
    /**
     * 资产层级信息选择
     *
     * @return 前端页面路径
     */
    @GetMapping("/checkLevel")
    public String checkLevel(String infoId, Model model) {
        List<CmsSecurityLevelInfoVO> levelInfoVOS = cmsSecurityInfoService.listLevelInfo(infoId);
        model.addAttribute("levelInfoVOS", levelInfoVOS);
        return "cms/security/checkSecurityLevel";
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
        return "cms/security/approve/listSecurityIssueApprove";
    }
    /**
     * 资产证券化登记信息审批列表 - 数据
     *
     * @param input 前端传参
     */
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput datatableInfoApprove(@RequestBody DataTablesInput input) {
        return cmsSecurityIssueService.dataTableApprove(input);
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
        model.addAttribute("taskId", taskId);//必须要做此步骤
        CmsSecurityIssue cmsSecurityIssue = cmsSecurityIssueService.get(businessId, true);
//        model.addAttribute("cmsBondRepayPlanVo", cmsBondRepayPlanService.getByIssueId(cmsBondIssueInfo.getId()));
        model.addAttribute("Issue", cmsSecurityIssue);
        CmsSecurityInfoVO info = cmsSecurityInfoService.getInfoById(cmsSecurityIssue.getSecurityInfoId());
        model.addAttribute("info", info);
        //TODO 补充利率相关信息
        Optional.ofNullable(cmsSecurityIssue).ifPresent(precativeDTO -> {
            if (StringUtils.isNotEmpty(cmsSecurityIssue.getReferenceRateType())){
                TbBaseRateDTO baseRateDTO = iBaseRateService.getBaseRateById(cmsSecurityIssue.getReferenceRateType());
                Optional.ofNullable(baseRateDTO).ifPresent(rateDTO -> model.addAttribute("tbBaseRate", rateDTO));
            }
            if (StringUtils.isNotEmpty(cmsSecurityIssue.getRepayCycle())){
                TbCreditPeriodDTO creditPeriodDTO = iCreditPeriodService.getById(cmsSecurityIssue.getRepayCycle());
                Optional.ofNullable(creditPeriodDTO).ifPresent(periodDTO -> model.addAttribute("tbCreditPeriod", periodDTO));
            }
        });
        SecurityUtils.setBondParam(model, "1", cmsSecurityIssue, fcBondHookService);
        SecurityUtils.setGuaParams(model, "1", "1", cmsSecurityIssue, guaContractOccupyService);
        //抵质押参数
        SecurityUtils.setPledgeParam(model, "1", cmsSecurityIssue, pledgeOccupyService);
        //第三方费率信息
        List<DfIntermediateCostVO> costList = cmsSecurityIssueService.listIntermediateCostByDfId(cmsSecurityIssue.getId());
        model.addAttribute("costVOS", costList);
        return "cms/security/approve/viewSecurityIssueApprove";
    }
}
