package com.bytter.finance.web.inl.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.domain.basedata.service.RepayTypeService;
import com.bytter.finance.domain.basedata.vo.RepayTypeVO;
import com.bytter.finance.domain.inl.InlUtil;
import com.bytter.finance.domain.inl.service.InlInfoService;
import com.bytter.finance.domain.inl.service.InlSendService;
import com.bytter.finance.domain.inl.vo.CmsInlInfoVO;
import com.bytter.finance.domain.inl.vo.CmsInlSendVO;
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.system.api.IBaseRateService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.ICreditPeriodService;
import com.bytter.system.api.constant.ConfigConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
@Controller
@RequestMapping("/inl/send")
public class InlSendController extends BaseController {
    @DubboReference(check = false)
    private IConfigService configService;
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
    private InlSendService inlSendService;
    @Autowired
    private InlInfoService inlInfoService;
    @Autowired
    private RepayTypeService repayTypeService;
    @GetMapping("/listSendApp")
    public String listSendApp(Model model) {
        return "cms/inl/send/listSendApp";
    }
    @GetMapping("/listSendUpdate")
    public String listSendUpdate(Model model) {
        return "cms/inl/send/listSendUpdate";
    }
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/inl/send/listApproveInlSend";
    }
    /**
     * 查询申请审批通过数据
     *
     * @param input
     * @return
     */
    @ResponseBody
    @PostMapping("/approvedDataTable")
    public DataTablesOutput listInlInfoApproved(@RequestBody DataTablesInput input) {
        return inlSendService.listInlInfoApproved(input, getCurrentUser());
    }
    /**
     * 查询暂存打回数据
     *
     * @param input
     * @return
     */
    @ResponseBody
    @PostMapping("/listSendUpdateDataTable")
    public DataTablesOutput listSendUpdateDataTable(@RequestBody DataTablesInput input) {
        return inlSendService.listSendUpdateDataTable(input, getCurrentUser());
    }
    /**
     * 查询待审批数据
     *
     * @param input
     * @return
     */
    @ResponseBody
    @PostMapping("/approveDataTable")
    public DataTablesOutput listInlsendApprove(@RequestBody DataTablesInput input) {
        return inlSendService.approveDataTable(input, getCurrentUser());
    }
    @GetMapping("/listSendConfirm")
    public String listConfirmSend(Model model) {
        return "cms/inl/send/listSendConfirm";
    }
    /**
     * 查询待确认数据
     *
     * @param input
     * @return
     */
    @ResponseBody
    @PostMapping("/listSendConfirmDataTable")
    public DataTablesOutput listSendConfirmDataTable(@RequestBody DataTablesInput input) {
        return inlSendService.listSendConfirmDataTable(input, getCurrentUser());
    }
    @ResponseBody
    @PostMapping("/sendConfirm")
    public AjaxJson sendConfirm(String id, Date realSendDate) {
        inlSendService.sendConfirm(id, realSendDate);
        return success();
    }
    /**
     * 保存数据
     *
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsInlSendVO cmsInlSendVO, Model model) {
        inlSendService.save(cmsInlSendVO, getCurrentUser());
        return success();
    }
    @GetMapping("/add")
    public String add(String id, Model model) {
        CmsInlInfoVO cmsInlInfoVO = inlSendService.getInlInfoById(id);
        InlUtil.setParams(configService, model, cmsInlInfoVO.getId(), inlInfoService, conCommonService,
                guaContractOccupServie);
        model.addAttribute("corp", corpService.get(cmsInlInfoVO.getBorrowInCorp()));
        model.addAttribute("inlInfo", cmsInlInfoVO);
        model.addAttribute("baseRate", iBaseRateService.getBaseRateById(cmsInlInfoVO.getReferenceRateType()));
        model.addAttribute("creditPeriod", iCreditPeriodService.getById(cmsInlInfoVO.getRepayCycle()));
        model.addAttribute("repayPlan", inlSendService.getRepayPlan(cmsInlInfoVO.getId()));
        Map<String, Object> config = (Map<String, Object>) configService.listByType(ConfigConstant.INL);
        model.addAttribute("config", config);
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_INL_SEND);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
        return "cms/inl/send/addInlSend";
    }
    /**
     * 编辑
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/edit")
    public String edit(String id, Model model) {
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(id);
        CmsInlInfoVO cmsInlInfoVO = inlSendService.getInlInfoById(cmsInlSendVO.getInlInfoId());
        InlUtil.setParams(configService, model, cmsInlInfoVO.getId(), inlInfoService, conCommonService,
                guaContractOccupServie);
        model.addAttribute("corp", corpService.get(cmsInlInfoVO.getBorrowInCorp()));
        model.addAttribute("inlInfo", cmsInlInfoVO);
        model.addAttribute("inlSend", cmsInlSendVO);
        model.addAttribute("baseRate", iBaseRateService.getBaseRateById(cmsInlSendVO.getReferenceRateType()));
        if (StringUtils.isNotEmpty(cmsInlSendVO.getRepayCycle())) {
            model.addAttribute("creditPeriod", iCreditPeriodService.getById(cmsInlSendVO.getRepayCycle()));
        }
        model.addAttribute("repayPlan", inlSendService.getRepayPlan(cmsInlSendVO.getId()));
        Map<String, Object> config = (Map<String, Object>) configService.listByType(ConfigConstant.INL);
        model.addAttribute("config", config);
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_INL_SEND);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
        return "cms/inl/send/editInlSend";
    }
    /**
     * 审批页面
     *
     * @return
     */
    @GetMapping("/viewApprove")
    public String viewApprove(String id, String taskId, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("taskId", taskId);
        return "cms/inl/send/viewApproveInlInfo";
    }
    /**
     * 查看页面
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/view")
    public String view(String id, Model model) {
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(id);
        CmsInlInfoVO cmsInlInfoVO = inlSendService.getInlInfoById(cmsInlSendVO.getInlInfoId());
        InlUtil.setParams(configService, model, cmsInlInfoVO.getId(), inlInfoService, conCommonService,
                guaContractOccupServie);
        model.addAttribute("corp", corpService.get(cmsInlInfoVO.getBorrowInCorp()));
        model.addAttribute("inlInfo", cmsInlInfoVO);
        model.addAttribute("inlSend", cmsInlSendVO);
        if (StringUtils.isNotEmpty(cmsInlSendVO.getReferenceRateType())) {
            model.addAttribute("baseRate", iBaseRateService.getBaseRateById(cmsInlSendVO.getReferenceRateType()));
        }
        if (StringUtils.isNotEmpty(cmsInlSendVO.getRepayCycle())) {
            model.addAttribute("creditPeriod", iCreditPeriodService.getById(cmsInlSendVO.getRepayCycle()));
        }
        if(StringUtils.isNotEmpty(cmsInlSendVO.getRepayType())){
            RepayTypeVO repayType= repayTypeService.getRepayTypeInfoById(cmsInlSendVO.getRepayType());
            if(!EmptyUtils.isEmpty(repayType)) {
                cmsInlSendVO.setRepayTypeName(repayType.getRepayName());
            }
        }
        /**
         * 资金计划相关参数
         */
        Map<String, Object> config = configService.listByType(ConfigConstant.INL);
        model.addAttribute("config", config);
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_INL_SEND);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
        return "cms/inl/send/viewInlSend";
    }
    /**
     * 查看页面
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/viewAllInlSend")
    public String viewAllInlSend(String id, Model model) {
        CmsInlInfoVO cmsInlInfoVO = inlSendService.getInlInfoById(id);
        model.addAttribute("corp", corpService.get(cmsInlInfoVO.getBorrowInCorp()));
        model.addAttribute("inlInfo", cmsInlInfoVO);
        model.addAttribute("inlSends", inlSendService.getSendsByInfoId(cmsInlInfoVO.getId()));
        InlUtil.setParams(configService, model, cmsInlInfoVO.getId(), inlInfoService, conCommonService,
                guaContractOccupServie);
        return "cms/inl/send/viewAllInlSend";
    }
    /**
     * 逻辑删除
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson removeById(String id) {
        inlSendService.removeById(id);
        return success();
    }
}
