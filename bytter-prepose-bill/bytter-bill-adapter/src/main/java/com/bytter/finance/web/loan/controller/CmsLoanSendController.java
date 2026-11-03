package com.bytter.finance.web.loan.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.api.IRatCpUrationService;
import com.bytter.credit.constant.RationConstant;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.basedata.service.LoanTypeService;
import com.bytter.finance.domain.loan.entity.CmsLoanInfo;
import com.bytter.finance.domain.loan.entity.CmsLoanRepay;
import com.bytter.finance.domain.loan.entity.CmsLoanSend;
import com.bytter.finance.domain.loan.service.CmsLoanInfoService;
import com.bytter.finance.domain.loan.service.CmsLoanSendService;
import com.bytter.finance.domain.loan.vo.CmsLoanInfoVO;
import com.bytter.finance.domain.loan.vo.CmsLoanSendVO;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.TbBaseRateDTO;
import com.bytter.system.api.dto.TbCreditPeriodDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 贷款发放表
 * @date 2017-07-28 11:27:00
 */
@Controller
@RequestMapping("/cms/loan/cmsloansend")
public class CmsLoanSendController extends BaseController {
    @Autowired
    private CmsLoanSendService cmsLoanSendService;
    @Autowired
    private CmsLoanInfoService cmsLoanInfoService;
    @Autowired
    private IRatCpUrationService ucprationService;
    @Autowired
    private LoanTypeService loanTypeService;
    /**
     * 贷款合同生成列表
     *
     * @return
     */
    // @LogMessage(content = "查询贷款发放表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("pageType", "createContract");
        return "cms/loan/createLoan/listLoanSend";
    }
    /**
     * <b>功能介绍：</b>待还款申请列表<br>
     * <b>日期：</b>2017年8月3日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/listStayRepay")
    public String listStayRepay(Model model, String repayPlan) {
        if (StringUtils.isNotEmpty(repayPlan)) {
            model.addAttribute("repayPlan", repayPlan);
        }
        model.addAttribute("pageType", "stayRepay");
        return "cms/loan/createLoan/listLoanStayRepay";
    }
    /**
     * <b>功能介绍：</b>待展期申请列表<br>
     * <b>日期：</b>2017年8月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/listDeferred")
    public String listDeferred(Model model) {
        model.addAttribute("pageType", "createDeferred");
        model.addAttribute("isMoreDeferred", cmsLoanSendService.isMoreDeferred());
        return "cms/loan/createLoan/listLoanDeferred";
    }
    /**
     * 贷款发放表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput, String pageType) {
        return cmsLoanSendService.listCmsLoanSend(dataTablesInput, pageType);
    }
    /**
     * <b>功能介绍：</b>贷款合同生成列表<br>
     * <b>日期：</b>2017年8月7日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @PostMapping("/loanContractList")
    @ResponseBody
    public DataTablesOutput loanContractList(@RequestBody DataTablesInput dataTablesInput) {
        return cmsLoanSendService.loanContractList(dataTablesInput, getUserId());
    }
    /**
     * <b>功能介绍：</b>贷款还款申请列表<br>
     * <b>日期：</b>2017年8月7日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @PostMapping("/loanRepayList")
    @ResponseBody
    public DataTablesOutput loanRepayList(@RequestBody DataTablesInput dataTablesInput) {
        return cmsLoanSendService.loanRepayList(dataTablesInput, getUserId());
    }
    /**
     * <b>功能介绍：</b>贷款还款计划修改列表<br>
     * <b>日期：</b>2017年9月21日<br>
     * <b>作者：</b>songkk<br>
     *
     * @version 1.0
     */
    @PostMapping("/dataRepayPlanable")
    @ResponseBody
    public DataTablesOutput dataRepayPlanable(@RequestBody DataTablesInput dataTablesInput) {
        return cmsLoanSendService.dataRepayPlanable(dataTablesInput);
    }
    /**
     * <b>功能介绍：</b>贷款展期申请列表<br>
     * <b>日期：</b>2017年8月7日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @PostMapping("/loanDeferredList")
    @ResponseBody
    public DataTablesOutput loanDeferredList(@RequestBody DataTablesInput dataTablesInput) {
        return cmsLoanSendService.loanDeferredList(dataTablesInput, getUserId());
    }
    /**
     * <b>功能介绍：</b>还款明细<br>
     * <b>日期：</b>2017年11月15日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param sendCode
     * @param model
     * @return
     * @version 1.0
     */
    @GetMapping("/repayInfolist")
    public String repayInfolist(String sendCode, String type, Model model) {
        if (EmptyUtils.isNotEmpty(sendCode)) {
            List<CmsLoanRepay> repays = cmsLoanSendService.getCmsLoanRepayBySendCode(sendCode, type);
            model.addAttribute("cmsloanRepays", repays);
        }
        return "cms/loan/loanRepay/listLoanRepayItem";
    }
    /**
     * <b>功能介绍：</b>发放审批列表跳转<br>
     * <b>日期：</b>2017年8月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/listSendApprove")
    public String listSendApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/loan/createLoan/listLoanSendApprove";
    }
    /**
     * <b>功能介绍：</b>发放审批列表数据<br>
     * <b>日期：</b>2017年8月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @PostMapping("/listSendApproveData")
    @ResponseBody
    public DataTablesOutput listSendApproveData(@RequestBody DataTablesInput dataTablesInput) {
        return cmsLoanSendService.listSendApproveData(dataTablesInput);
    }
    /**
     * <b>功能介绍：</b>跳转单笔审批页面<br>
     * <b>日期：</b>2017年8月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/approve")
    public String approve(Model model, String id, String taskId) {
        if (StringUtils.isNotEmpty(id)) {
            boolean isCharging = false;
            CmsLoanSendVO cmsLoanSend = cmsLoanSendService.getCmsLoanSendById(id);
            CmsLoanInfoVO cmsLoanInfo = cmsLoanSendService.getLoanInfoByLoanSend(cmsLoanSendService.get(id));
            cmsLoanInfo.setLoanGenreName(loanTypeService
                    .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
            if (RationConstant.RAT_COMMON_Y.equals(cmsLoanInfo.getIsHangCredit())) {
                model.addAttribute("cpURationDTO",
                        ucprationService.getRatUrationDTOByBillId(ConfigConstant.CMS, cmsLoanInfo.getId()));// 授信占用明细
            }
            ConfigDTO isGuarantee = cmsLoanInfoService.getIsGuarantee();// 获取参数设置是否挂接担保
            model.addAttribute("isGuarantee", isGuarantee.getValue());
            model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);// 申请页
            model.addAttribute("cmsLoanSendPage", cmsLoanSend);
            model.addAttribute("inwayMoney", cmsLoanSendService.getInwayMoney(cmsLoanInfo.getBillCode()));// 在途金额
            model.addAttribute("repayedMoney", cmsLoanSendService.getRepayedMoney(cmsLoanInfo.getBillCode()));// 已还款金额
            model.addAttribute("isCharging", isCharging);
            model.addAttribute("taskId", taskId);
        }
        return "cms/loan/createLoan/cmsLoanSendApprove";
    }
    /**
     * <b>功能介绍：</b>发放流程手机端详情页面<br>
     * <b>日期：</b>2017年10月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param businessId
     * @param taskId
     * @param model
     * @return
     * @version 1.0
     */
    @GetMapping("/mobileSendApprove")
    public String mobileSendApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            CmsLoanSendVO cmsLoanSend = cmsLoanSendService.getCmsLoanSendById(businessId);
            CmsLoanInfoVO cmsLoanInfo = cmsLoanSendService.getLoanInfoByLoanSend(cmsLoanSendService.get(businessId));
            cmsLoanInfo.setLoanGenreName(loanTypeService
                    .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
            model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);// 申请页
            model.addAttribute("cmsLoanSendPage", cmsLoanSend);
            model.addAttribute("loanMoneyUpper", StringUtils.chineseMoney(cmsLoanSend.getLoanMoney()));
            model.addAttribute("taskId", taskId);
        }
        return "cms/loan/createLoan/mobileSendApprove";
    }
    /**
     * 贷款发放表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(CmsLoanSend cmsLoanSend, Model model) {
        if (StringUtils.isNotEmpty(cmsLoanSend.getId())) {
            cmsLoanSend = cmsLoanSendService.get(cmsLoanSend.getId());
            model.addAttribute("cmsLoanSendPage", cmsLoanSend);
        }
        return "cms/loan/createLoan/addCmsLoanSend";
    }
    /**
     * 贷款发放表编辑页面跳转
     *
     * @return
     */
    // @GetMapping("/update")
    public String update(CmsLoanSend cmsLoanSend, Model model) {
        if (StringUtils.isNotEmpty(cmsLoanSend.getId())) {
            cmsLoanSend = cmsLoanSendService.get(cmsLoanSend.getId());
            model.addAttribute("cmsLoanSendPage", cmsLoanSend);
        }
        return "cms/loan/createLoan/updateCmsLoanSend";
    }
    /**
     * 贷款发放表详情页
     *
     * @return
     */
    // @LogMessage(content = "查询贷款发放表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, String param, Model model) {
        boolean isCharging = false;
        TbBaseRateDTO baseRateDTO;
        TbCreditPeriodDTO creditPeriodDTO;
        CmsLoanSendVO cmsLoanSend = cmsLoanSendService.getCmsLoanSendById(id);
        CmsLoanInfoVO cmsLoanInfo = cmsLoanSendService.getLoanInfoByLoanSend(cmsLoanSendService.get(id, true));
        cmsLoanInfo.setLoanGenreName(loanTypeService
                .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
        if (RationConstant.RAT_COMMON_Y.equals(cmsLoanInfo.getIsHangCredit())) {
            model.addAttribute("cpURationDTO",
                    ucprationService.getRatUrationDTOByBillId(ConfigConstant.CMS, cmsLoanInfo.getId()));// 授信占用明细
        }
        ConfigDTO isGuarantee = cmsLoanInfoService.getIsGuarantee();// 获取参数设置是否挂接担保
        model.addAttribute("isGuarantee", isGuarantee.getValue());
        model.addAttribute("cmsLoanSendPage", cmsLoanSend);// 发放页
        model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);// 申请页
        model.addAttribute("inwayMoney", cmsLoanSendService.getInwayMoney(cmsLoanInfo.getBillCode()));// 在途金额
        model.addAttribute("repayedMoney", cmsLoanSendService.getRepayedMoney(cmsLoanInfo.getBillCode()));// 已还款金额
        model.addAttribute("loanMoneyUpper", StringUtils.chineseMoney(cmsLoanInfo.getLoanMoney()));
        model.addAttribute("isCharging", isCharging);
        if (StringUtils.isEmpty(param)) {
            model.addAttribute("param", true);
        } else {
            model.addAttribute("param", false);
        }
        model.addAttribute("show", 1);
        return "cms/loan/createLoan/viewCmsLoanSend";
    }
    @GetMapping("/viewByBillCode/{billCode}")
    public String viewByBillCode(@PathVariable("billCode") String billCode, String param, Model model) {
        boolean isCharging = false;
        CmsLoanSendVO cmsLoanSend = cmsLoanSendService.getCmsLoanSendVOByBillCode(billCode);
        CmsLoanInfoVO cmsLoanInfo = cmsLoanSendService
                .getLoanInfoByLoanSend(cmsLoanSendService.get(cmsLoanSend.getId(), true));
        cmsLoanInfo.setLoanGenreName(loanTypeService
                .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
        if (RationConstant.RAT_COMMON_Y.equals(cmsLoanInfo.getIsHangCredit())) {
            model.addAttribute("cpURationDTO",
                    ucprationService.getRatUrationDTOByBillId(ConfigConstant.CMS, cmsLoanInfo.getId()));// 授信占用明细
        }
        model.addAttribute("cmsLoanSendPage", cmsLoanSend);// 发放页
        model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);// 申请页
        model.addAttribute("inwayMoney", cmsLoanSendService.getInwayMoney(cmsLoanInfo.getBillCode()));// 在途金额
        model.addAttribute("repayedMoney", cmsLoanSendService.getRepayedMoney(cmsLoanInfo.getBillCode()));// 已还款金额
        model.addAttribute("loanMoneyUpper", StringUtils.chineseMoney(cmsLoanInfo.getLoanMoney()));
        model.addAttribute("isCharging", isCharging);
        if (StringUtils.isEmpty(param)) {
            model.addAttribute("param", true);
        } else {
            model.addAttribute("param", false);
        }
        model.addAttribute("show", 0);
        model.addAttribute("viewFlag", "0");// 查看标识
        return "cms/loan/createLoan/viewCmsLoanSend";
    }
    /**
     * 报表查询贷款详情
     *
     * @return
     */
    // @LogMessage(content = "报表查询贷款详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/queryView")
    public String queryView(String billCode, int type, Model model) {
        boolean isCharging = false;
        model.addAttribute("isCharging", isCharging);
        ConfigDTO isGuarantee = cmsLoanInfoService.getIsGuarantee();// 获取参数设置是否挂接担保
        model.addAttribute("isGuarantee", isGuarantee.getValue());
        String page = "";
        CmsLoanInfoVO cmsLoanInfo;
        if (type == 1) {
            CmsLoanInfo loanInfo = cmsLoanInfoService.getCmsLoanInfoBybillCode(billCode);
            cmsLoanInfo = cmsLoanInfoService.getCmsLoanInfoById(loanInfo.getId());
            cmsLoanInfo.setLoanGenreName(loanTypeService
                    .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
            model.addAttribute("loanMoneyUpper", StringUtils.chineseMoney(cmsLoanInfo.getLoanMoney()));
            model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);
            model.addAttribute("viewType", "loanInfo");
            page = "viewCmsLoanInfo";
        } else {
            CmsLoanSend sendInfo = cmsLoanSendService.getLoanSendByBillCode(billCode);
            CmsLoanSendVO cmsLoanSend = cmsLoanSendService.getCmsLoanSendById(sendInfo.getId());
            cmsLoanInfo = cmsLoanSendService.getLoanInfoByLoanSend(sendInfo);
            cmsLoanInfo.setLoanGenreName(loanTypeService
                    .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
            model.addAttribute("loanMoneyUpper", StringUtils.chineseMoney(cmsLoanInfo.getLoanMoney()));
            model.addAttribute("cmsLoanSendPage", cmsLoanSend);// 发放页
            model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);// 申请页
            model.addAttribute("inwayMoney", cmsLoanSendService.getInwayMoney(cmsLoanInfo.getBillCode()));// 在途金额
            model.addAttribute("repayedMoney", cmsLoanSendService.getRepayedMoney(cmsLoanInfo.getBillCode()));// 已还款金额
            page = "viewCmsLoanSend";
        }
        if (CmsConstant.IS_TRUE.equals(cmsLoanInfo.getIsHangCredit())) {
            model.addAttribute("cpURationDTO",
                    ucprationService.getRatUrationDTOByBillId(ConfigConstant.CMS, cmsLoanInfo.getId()));// 授信占用明细
        }
        return "cms/loan/createLoan/" + page;
    }
    /**
     * 添加贷款发放表
     *
     * @param cmsLoanSend
     * @return
     */
    @LogMessage(content = "添加贷款发放表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsLoanSend cmsLoanSend, String sendBillCode) {
        cmsLoanSend.setStatus(CmsConstant.COMMITAPPROVE);
        cmsLoanSend.setBillCode(cmsLoanSendService.getLoanSendBillCode(CmsConstant.LOAN_SEND_BILL_CODE_PREFIX));
        cmsLoanSendService.saveLoanSendInfo(cmsLoanSend, getCurrentUser(), sendBillCode);
        return success();
    }
    /**
     * 修改贷款发放表
     *
     * @param cmsLoanSend
     * @return
     */
    @LogMessage(content = "修改贷款发放表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(CmsLoanSend cmsLoanSend) {
        CmsLoanSend t = cmsLoanSendService.get(cmsLoanSend.getId());
        BeanUtils.copyNotNullProperties(cmsLoanSend, t);
        cmsLoanSendService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除贷款发放表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除贷款发放表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        CmsLoanSend cmsLoanSend = cmsLoanSendService.get(id);
        cmsLoanSend.setStatus(CmsConstant.DELETED);// 更新状态为删除
        cmsLoanSendService.updateEntity(cmsLoanSend);
        return success();
    }
    /**
     * 批量删除贷款发放表
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除贷款发放表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(",")) {
            CmsLoanSend cmsLoanSend = cmsLoanSendService.get(id);
            cmsLoanSendService.deleteEntity(cmsLoanSend);
        }
        return success();
    }
    @PostMapping("/getLoanTallyList")
    @ResponseBody
    public DataTablesOutput getLoanTallyList(@RequestBody DataTablesInput dataTablesInput, String opType) {
        return cmsLoanSendService.getLoanTallyList(dataTablesInput, opType, getUserId());

    }
}
