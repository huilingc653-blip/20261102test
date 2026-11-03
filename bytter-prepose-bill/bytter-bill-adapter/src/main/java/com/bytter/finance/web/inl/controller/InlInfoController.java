package com.bytter.finance.web.inl.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.InlConstant;
import com.bytter.finance.domain.basedata.service.LoanRateMaintenceService;
import com.bytter.finance.domain.inl.InlUtil;
import com.bytter.finance.domain.inl.entity.CmsInlInfo;
import com.bytter.finance.domain.inl.service.InlInfoService;
import com.bytter.finance.domain.inl.vo.CmsInlInfoVO;
import com.bytter.finance.domain.inl.vo.GuaContractCmsInlVO;
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.TbRateMaintenceDTO;
import com.google.common.collect.Maps;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;
@Controller
@RequestMapping("/inl/info")
public class InlInfoController extends BaseController {
    @DubboReference(check = false)
    private IConfigService configService;
    @Autowired
    private InlInfoService inlInfoService;
    @Resource
    private IConService conCommonService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupServie;
    @DubboReference(check = false)
    private IBaseRateService iBaseRateService;
    @DubboReference(check = false)
    private ICreditPeriodService iCreditPeriodService;
    @Autowired
    private LoanRateMaintenceService loanRateMaintenceService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    /**
     * 列表条转
     *
     * @return
     */
    @GetMapping("/list")
    public String listInlInfo(Model model) {
        return "cms/inl/info/listInlInfo";
    }
    /**
     * 列表条转
     *
     * @return
     */
    @GetMapping("/add")
    public String addInlInfo(Model model) {
        Map<String, Object> config = configService.listByType(ConfigConstant.INL);
        model.addAttribute("config", config);
        Map<String, Object> inlConfig = (Map<String, Object>) config.get(ConfigConstant.INL);
        model.addAttribute("relateCon", inlConfig.get("is_con"));
        model.addAttribute("conType", inlConfig.get("con_type"));
        Map<String, Object> inl = (Map<String, Object>) config.getOrDefault("inl", Maps.newHashMap());
        model.addAttribute("relateRat",inl.get("is_hang_credit"));
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_INL_SEND);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
        return "cms/inl/info/addInlInfo";
    }
    /**
     * 编辑
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/edit")
    public String editInlInfo(String id, Model model) {
        CmsInlInfo cmsInlInfo = inlInfoService.get(id);
        model.addAttribute("corp", corpService.get(cmsInlInfo.getBorrowInCorp()));
        Map<String, Object> inlConfig = (Map<String, Object>) configService.listByType(ConfigConstant.INL)
                .get(ConfigConstant.INL);
        model.addAttribute("relateCon", inlConfig.get("is_con"));
        model.addAttribute("conType", inlConfig.get("con_type"));
        model.addAttribute("conId", cmsInlInfo.getConId());
        InlUtil.setParams(configService, model, cmsInlInfo.getId(), inlInfoService, conCommonService, guaContractOccupServie);
        model.addAttribute("inlInfo", cmsInlInfo);
        model.addAttribute("repayPlan", inlInfoService.getRepayPlan(cmsInlInfo.getId()));
        model.addAttribute("baseRate", iBaseRateService.getBaseRateById(cmsInlInfo.getReferenceRateType()));
        TbRateMaintenceDTO tbLoanRateMaintence = loanRateMaintenceService.getLoanRateMaintence(cmsInlInfo.getCurId(),
                cmsInlInfo.getReferenceRateType(), cmsInlInfo.getRepayCycle());
        if (null != tbLoanRateMaintence) {
            model.addAttribute("tbLoanRateMaintence", tbLoanRateMaintence);
        }
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_INL_SEND);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
        return "cms/inl/info/editInlInfo";
    }
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/inl/info/listApproveInlInfo";
    }
    @GetMapping("/view")
    public String viewInlInfo(String id, @RequestParam(required = false) String flag, Model model) {
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(id);
        model.addAttribute("corp", corpService.get(cmsInlInfoVO.getBorrowInCorp()));
        InlUtil.setParams(configService, model, cmsInlInfoVO.getId(), inlInfoService, conCommonService,
                guaContractOccupServie);
        model.addAttribute("inlInfo", cmsInlInfoVO);
        model.addAttribute("flag", flag);
        model.addAttribute("baseRate", iBaseRateService.getBaseRateById(cmsInlInfoVO.getReferenceRateType()));
        if (StringUtils.isNotEmpty(cmsInlInfoVO.getRepayCycle())) {
            model.addAttribute("creditPeriod", iCreditPeriodService.getById(cmsInlInfoVO.getRepayCycle()));
        }
        /**
         * 资金计划相关参数
         */
        Map<String, Object> config = configService.listByType(ConfigConstant.INL);
        model.addAttribute("config", config);
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_INL_SEND);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
        return "cms/inl/info/viewInlInfo";
    }
    /**
     * 数据保存方法
     *
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsInlInfoVO inlInfoVO, Model model) {
        inlInfoService.save(inlInfoVO, getCurrentUser());
        return success();
    }
    /**
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson deleteById(String id) {
        CmsInlInfo cmsInlInfo = inlInfoService.get(id);
        cmsInlInfo.setStatus(InlConstant.DELETE);
        inlInfoService.updateEntity(cmsInlInfo);
        /**
         * 业务挂接释放
         */
        inlInfoService.releaseBusiness(cmsInlInfo, getCurrentUser());
        return success();
    }
    /**
     * 列表查询
     *
     * @param input
     * @return
     */
    @ResponseBody
    @PostMapping("/dataTable")
    public DataTablesOutput datatable(@RequestBody DataTablesInput input) {
        return inlInfoService.datatale(input, getCurrentUser());
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
        return inlInfoService.approveDataTable(input, getCurrentUser());
    }
    /**
     * 查看页面跳转
     *
     * @return
     */
    @GetMapping("/viewApprove")
    public String viewApprove(String id, String taskId, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("taskId", taskId);
        return "cms/inl/info/viewApproveInlInfo";
    }
    /**
     * 选择账户页面
     *
     * @return
     */
    @GetMapping("/selectAcc")
    public String selectAcc(String borrowInCorp, String borrowOutCorp, String flag, String curId, String accId,
            Model model) {
        model.addAttribute("borrowInCorp", borrowInCorp);
        model.addAttribute("borrowOutCorp", borrowOutCorp);
        model.addAttribute("curId", curId);
        model.addAttribute("accId", accId);
        model.addAttribute("flag", flag);
        BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccById(accId);
        if (null != btBankAccDTO) {
            model.addAttribute("accType", "bankAcc");
        } else {
            model.addAttribute("accType", "fcAcc");
        }
        return "cms/inl/info/inlSelectAcc";
    }
    @GetMapping("/selectAcc2")
    public String selectAcc2(String borrowInCorp, String borrowOutCorp, String flag, String curId, String accId,
            Model model) {
        model.addAttribute("borrowInCorp", borrowInCorp);
        model.addAttribute("borrowOutCorp", borrowOutCorp);
        model.addAttribute("curId", curId);
        model.addAttribute("accId", accId);
        model.addAttribute("flag", flag);
        return "cms/inl/info/inlSelectAcc2";
    }
    /**
     * @return
     */
    @ResponseBody
    @PostMapping("/listAccDataTable")
    public DataTablesOutput listAccDataTable(@RequestBody DataTablesInput input) {
        return inlInfoService.listAcc(input);
    }
    /**
     * @return
     */
    @ResponseBody
    @PostMapping("/listBankAccDataTable")
    public DataTablesOutput listBankAccDataTable(@RequestBody DataTablesInput input) {
        return inlInfoService.listBankAcc(input);
    }
    /**
     * @return
     */
    @ResponseBody
    @PostMapping("/listFcAccDataTable")
    public DataTablesOutput listFcAccDataTable(@RequestBody DataTablesInput input) {
        return inlInfoService.listFcAcc(input);
    }
    /**
     * 获取挂接担保合同的内部拆借数据
     *
     * @param dataTablesInput 查询参数
     * @return 列表数据
     */
    @ResponseBody
    @PostMapping("/getGuaContract")
    public DataTablesOutput<GuaContractCmsInlVO> getUseChangelist(@RequestBody DataTablesInput dataTablesInput) {
        return inlInfoService.getGuaForInl(dataTablesInput, getCurrentUser());
    }
}
